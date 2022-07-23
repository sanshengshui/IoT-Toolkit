package iot.technology.client.toolkit.mqtt.command.sub;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.concurrent.Future;
import iot.technology.client.toolkit.mqtt.service.MqttClientConfig;
import iot.technology.client.toolkit.mqtt.service.MqttClientService;
import iot.technology.client.toolkit.mqtt.service.domain.MqttConnectResult;
import iot.technology.client.toolkit.mqtt.service.impl.MqttClientServiceImpl;
import picocli.CommandLine;

import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author mushuwei
 */
@CommandLine.Command(
		name = "publish",
		aliases = "pub",
		version = "0.0.1",
		requiredOptionMarker = '*',
		description = "publish a message to the broker",
		optionListHeading = "%nOptions are:%n",
		mixinStandardHelpOptions = true,
		sortOptions = false,
		footerHeading = "%nCopyright (c) 2019-2022, IoT Technology",
		footer = "%nDeveloped by mushuwei"
)
public class MqttPublishCommand implements Callable<Integer> {

	private static final Charset UTF8 = Charset.forName("UTF-8");

	private final MqttClientConfig mqttClientConfig;
	private final MqttClientService mqttClientService;

	public MqttPublishCommand() {
		this.mqttClientConfig = new MqttClientConfig();
		this.mqttClientService = new MqttClientServiceImpl(mqttClientConfig, null);
	}

	@CommandLine.Option(
			order = 0,
			names = {"-h", "--hostname"},
			required = true,
			description = "the broker host")
	String host;

	@CommandLine.Option(
			order = 1,
			names = {"-p", "--port"},
			required = true,
			description = "the broker port",
			defaultValue = "1883",
			showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
	Integer port;

	@CommandLine.Option(
			order = 2,
			names = {"-i", "--client-id"},
			description = "the client id"
	)
	String clientId;

	@CommandLine.Option(
			order = 3,
			names = {"-u", "--username"},
			description = "the username"
	)
	String username;

	@CommandLine.Option(
			order = 4,
			names = {"-P", "--password"},
			description = "the password"
	)
	String password;

	@CommandLine.Option(
			order = 5,
			names = {"-t", "--topic"},
			description = "the message topic")
	String topic;

	@CommandLine.Option(
			order = 6,
			names = {"-q", "--qos"},
			description = "the QoS of the message, 0/1/2")
	Integer qos;

	@CommandLine.Option(
			order = 7,
			names = {"-m", "--message"},
			description = "the message body")
	String message;

	@Override
	public Integer call() throws Exception {
		Future<MqttConnectResult> connectFuture = mqttClientService.connect(host, port);
		MqttConnectResult result;
		try {
			result = connectFuture.get(mqttClientConfig.getTimeoutSeconds(), TimeUnit.SECONDS);
		} catch (TimeoutException ex) {
			connectFuture.cancel(true);
			mqttClientService.disconnect();
			String hostPort = host + ":" + port;
			throw new RuntimeException(String.format("Failed to connect to MQTT broker at %s.", hostPort));
		}
		if (!result.isSuccess()) {
			connectFuture.cancel(true);
			mqttClientService.disconnect();
			String hostPort = host + ":" + port;
			throw new RuntimeException(
					String.format("Failed to connect to MQTT broker at %s. Result code is: %s", hostPort, result.getReturnCode()));
		}
		mqttClientService.publish(topic, Unpooled.wrappedBuffer(message.getBytes(UTF8)), MqttQoS.AT_MOST_ONCE, false);
		return 0;
	}
}
