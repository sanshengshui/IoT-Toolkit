package iot.technology.client.toolkit.nb.service.processor.lwm2m;

import iot.technology.client.toolkit.common.rule.ProcessContext;
import iot.technology.client.toolkit.common.rule.TkAbstractProcessor;
import iot.technology.client.toolkit.common.rule.TkProcessor;

/**
 * @author mushuwei
 */
public class LwM2MDeleteProcessor extends TkAbstractProcessor implements TkProcessor {

    @Override
    public boolean supports(ProcessContext context) {
        return false;
    }

    @Override
    public void handle(ProcessContext context) {

    }
}
