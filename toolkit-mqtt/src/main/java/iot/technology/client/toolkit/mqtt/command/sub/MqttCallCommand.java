/*
 * Copyright © 2019-2022 The Toolkit Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package iot.technology.client.toolkit.mqtt.command.sub;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.concurrent.Future;
import iot.technology.client.toolkit.common.constants.*;
import iot.technology.client.toolkit.common.rule.NodeContext;
import iot.technology.client.toolkit.common.rule.TkNode;
import iot.technology.client.toolkit.common.utils.ColorUtils;
import iot.technology.client.toolkit.common.utils.ObjectUtils;
import iot.technology.client.toolkit.common.utils.StringUtils;
import iot.technology.client.toolkit.mqtt.service.MqttClientConfig;
import iot.technology.client.toolkit.mqtt.service.MqttClientService;
import iot.technology.client.toolkit.mqtt.service.MqttSettingsRuleChainProcessor;
import iot.technology.client.toolkit.mqtt.service.domain.MqttCallDomain;
import iot.technology.client.toolkit.mqtt.service.domain.MqttConnectResult;
import iot.technology.client.toolkit.mqtt.service.handler.MqttPubMessageHandler;
import iot.technology.client.toolkit.mqtt.service.handler.MqttSubMessageHandler;
import iot.technology.client.toolkit.mqtt.service.impl.MqttClientServiceImpl;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author mushuwei
 */
@CommandLine.Command(
		name = "call",
		requiredOptionMarker = '*',
		description = "${bundle:mqtt.call.desc}",
		optionListHeading = "%n${bundle:general.option}:%n",
		sortOptions = false,
		footerHeading = "%nCopyright (c) 2019-2022, ${bundle:general.copyright}",
		footer = "%nDeveloped by mushuwei",
		versionProvider = iot.technology.client.toolkit.common.constants.VersionInfo.class
)
public class MqttCallCommand implements Callable<Integer> {

	ResourceBundle bundle = ResourceBundle.getBundle(StorageConstants.LANG_MESSAGES);
	private static final Charset UTF8 = StandardCharsets.UTF_8;

	@Override
	public Integer call() throws Exception {
		Terminal terminal = TerminalBuilder.builder()
				.system(true)
				.build();
		LineReader reader = LineReaderBuilder.builder()
				.terminal(terminal)
				.parser(new DefaultParser())
				.build();
		MqttSettingsRuleChainProcessor ruleChain = new MqttSettingsRuleChainProcessor();
		Map<String, String> processor = ruleChain.getProcessor();
		String code = ruleChain.getRootNode();

		MqttCallDomain domain = new MqttCallDomain();
		NodeContext context = new NodeContext();
		context.setType(NodeTypeEnum.MQTT_DEFAULT.getType());
		while (true) {
			String data = null;
			try {
				String node = processor.get(code);
				TkNode tkNode = ObjectUtils.initComponent(node);
				if (tkNode == null) {
					break;
				}
				tkNode.prePrompt(context);
				data = reader.readLine(tkNode.nodePrompt());
				context.setData(data);
				tkNode.check(context);
				if (!StringUtils.isBlank(tkNode.getValue(context))) {
					System.out.format(ColorUtils.blackFaint(bundle.getString("call.prompt") + tkNode.getValue(context)) + "%n");
				}
				ObjectUtils.setValue(domain, code, tkNode.getValue(context));
				mqttBizLogic(code, data, domain);

				code = tkNode.nextNode(context);
				if (code.equals("end")) {
					break;
				}
			} catch (UserInterruptException e) {
				return ExitCodeEnum.ERROR.getValue();
			} catch (EndOfFileException e) {
				return ExitCodeEnum.ERROR.getValue();
			}
		}
		return ExitCodeEnum.NOTEND.getValue();
	}

	/**
	 * @param code   node code
	 * @param data   user input data
	 * @param domain mqtt settings domain
	 */
	public void mqttBizLogic(String code, String data, MqttCallDomain domain) {
		if (code.equals(MqttSettingsCodeEnum.MQTT_BIZ_TYPE.getCode())) {
			MqttClientService mqttClientService = connectBroker(domain);
			domain.setClient(mqttClientService);
		}
		if (code.equals(MqttSettingsCodeEnum.PUBLISH_MESSAGE.getCode())) {
			PubData pubData = PubData.validate(data);
			mqttPubLogic(pubData, domain.getClient());
		}
		if (code.equals(MqttSettingsCodeEnum.SUBSCRIBE_MESSAGE.getCode())) {
			SubData subData = SubData.validate(data);
			mqttSubLogic(subData, domain.getClient());
		}
	}

	private MqttClientService connectBroker(MqttCallDomain domain) {
		MqttClientConfig config = domain.convertMqttClientConfig(domain);
		MqttClientService mqttClientService = new MqttClientServiceImpl(config, new MqttPubMessageHandler());
		Future<MqttConnectResult> connectFuture = mqttClientService.connect(domain.getHost(), Integer.parseInt(domain.getPort()));
		MqttConnectResult result;
		try {
			result = connectFuture.get(config.getTimeoutSeconds(), TimeUnit.SECONDS);
			System.out.format("%s%s %s %s%s" + "%n",
					bundle.getString("mqtt.clientId"),
					domain.getClientId(),
					bundle.getString("mqtt.connect.broker"),
					domain.getHost() + ":" + domain.getPort(),
					String.format(EmojiEnum.smileEmoji));
		} catch (TimeoutException | InterruptedException | ExecutionException ex) {
			connectFuture.cancel(true);
			mqttClientService.disconnect();
			String hostPort = domain.getHost() + ":" + domain.getPort();
			throw new RuntimeException(
					String.format("%s %s %s.",
							bundle.getString("mqtt.failed.connect"),
							hostPort,
							String.format(EmojiEnum.pensiveEmoji)));
		}
		if (!result.isSuccess()) {
			connectFuture.cancel(true);
			mqttClientService.disconnect();
			String hostPort = domain.getHost() + ":" + domain.getPort();
			throw new RuntimeException(
					String.format("%s %s. %s %s", bundle.getString("mqtt.failed.connect"),
							hostPort, bundle.getString("mqtt.result.code"), result.getReturnCode()));
		}
		return mqttClientService;
	}

	private void mqttPubLogic(PubData data, MqttClientService client) {
		MqttQoS qosLevel = MqttQoS.valueOf(data.getQos());
		client.publish(data.getTopic(), Unpooled.wrappedBuffer(data.getPayload().getBytes(UTF8)), qosLevel, false);
		System.out.format(data.getPayload() + bundle.getString("publishMessage.success") + String.format(EmojiEnum.successEmoji) + "%n");
	}

	private void mqttSubLogic(SubData data, MqttClientService client) {
		MqttQoS qosLevel = MqttQoS.valueOf(data.getQos());
		MqttSubMessageHandler handler = new MqttSubMessageHandler();
		if (data.getOperation().equals("add")) {
			Future<Void> resultFuture = client.on(data.getTopic(), handler, qosLevel);
		} else {
			Future<Void> resultFuture = client.off(data.getTopic());
		}

	}

}
