package iot.technology.client.toolkit.nb.service.node;

import iot.technology.client.toolkit.common.constants.GlobalConstants;
import iot.technology.client.toolkit.common.constants.NbSettingsCodeEnum;
import iot.technology.client.toolkit.common.constants.StorageConstants;
import iot.technology.client.toolkit.common.rule.NodeContext;
import iot.technology.client.toolkit.common.rule.TkNode;
import iot.technology.client.toolkit.common.utils.ColorUtils;
import iot.technology.client.toolkit.common.utils.StringUtils;

import java.util.ResourceBundle;

/**
 * @author mushuwei
 */
public class LwM2MLifeTimeNode implements TkNode {

    ResourceBundle bundle = ResourceBundle.getBundle(StorageConstants.LANG_MESSAGES);

    public static Integer defaultLifeTime = 300;

    @Override
    public void prePrompt(NodeContext context) {
        System.out.format(ColorUtils.greenItalic(bundle.getString(NbSettingsCodeEnum.NB_LWM2M_LIFETIME.getCode() + GlobalConstants.prePrompt)) + "%n");
    }

    @Override
    public boolean check(NodeContext context) {
        if (StringUtils.isBlank(context.getData())) {
            context.setData(String.valueOf(defaultLifeTime));
            context.setCheck(true);
            return true;
        }
        try {
            defaultLifeTime = Integer.parseInt(context.getData());
        } catch (NumberFormatException e) {
            System.out.format(ColorUtils.redError(bundle.getString("param.error")));
            context.setCheck(false);
            return false;
        }
        context.setCheck(true);
        return true;
    }

    @Override
    public String nodePrompt() {
        return bundle.getString(NbSettingsCodeEnum.NB_LWM2M_LIFETIME.getCode() + GlobalConstants.promptSuffix) +
                GlobalConstants.promptSeparator;
    }

    @Override
    public String nextNode(NodeContext context) {
        if (!context.isCheck()) {
            return NbSettingsCodeEnum.NB_LWM2M_LIFETIME.getCode();
        }
        return NbSettingsCodeEnum.NB_LWM2M_COMMUNICATION_PERIOD.getCode();
    }

    @Override
    public String getValue(NodeContext context) {
        return context.getData();
    }
}
