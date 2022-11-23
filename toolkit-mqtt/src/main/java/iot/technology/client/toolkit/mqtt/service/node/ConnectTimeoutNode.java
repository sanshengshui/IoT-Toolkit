package iot.technology.client.toolkit.mqtt.service.node;

import iot.technology.client.toolkit.common.constants.GlobalConstants;
import iot.technology.client.toolkit.common.constants.MqttSettingsCodeEnum;
import iot.technology.client.toolkit.common.constants.StorageConstants;
import iot.technology.client.toolkit.common.rule.NodeContext;
import iot.technology.client.toolkit.common.rule.TkNode;
import iot.technology.client.toolkit.common.utils.StringUtils;

import java.util.ResourceBundle;

/**
 * @author mushuwei
 */
public class ConnectTimeoutNode implements TkNode {

	ResourceBundle bundle = ResourceBundle.getBundle(StorageConstants.LANG_MESSAGES);

	@Override
	public void check(NodeContext context) {
		if (!StringUtils.isBlank(context.getData()) && !StringUtils.isNumeric(context.getData())) {
			throw new IllegalArgumentException(bundle.getString("number.error"));
		}
	}

	@Override
	public String nodePrompt() {
		return bundle.getString(MqttSettingsCodeEnum.CONNECT_TIMEOUT.getCode() + GlobalConstants.promptSuffix) +
				GlobalConstants.promptSeparator;
	}

	@Override
	public String nextNode(NodeContext context) {
		return MqttSettingsCodeEnum.KEEP_ALIVE.getCode();
	}

	@Override
	public String getValue(NodeContext context) {
		return StringUtils.isBlank(context.getData()) ? "10" : context.getData();
	}

	@Override
	public void prePrompt(NodeContext context) {
	}
}
