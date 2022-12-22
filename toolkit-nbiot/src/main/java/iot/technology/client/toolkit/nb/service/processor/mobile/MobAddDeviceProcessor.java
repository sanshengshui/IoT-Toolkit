package iot.technology.client.toolkit.nb.service.processor.mobile;

import iot.technology.client.toolkit.common.rule.ProcessContext;
import iot.technology.client.toolkit.common.rule.TkProcessor;
import iot.technology.client.toolkit.common.utils.ColorUtils;
import iot.technology.client.toolkit.nb.service.mobile.MobileDeviceService;
import iot.technology.client.toolkit.nb.service.mobile.domain.MobileConfigDomain;
import iot.technology.client.toolkit.nb.service.mobile.domain.action.device.MobAddDeviceResponse;
import iot.technology.client.toolkit.nb.service.processor.MobProcessContext;

import java.util.List;

/**
 * @author mushuwei
 */
public class MobAddDeviceProcessor implements TkProcessor {

	private final MobileDeviceService mobileDeviceService = new MobileDeviceService();

	@Override
	public boolean supports(ProcessContext context) {
		return context.getData().startsWith("add");
	}

	@Override
	public void handle(ProcessContext context) {
		List<String> arguArgs = List.of(context.getData().split(" "));
		if (arguArgs.size() != 3) {
			System.out.format(ColorUtils.blackBold("argument:%s is illegal"), context.getData());
			System.out.format(" " + "%n");
			return;
		}
		MobProcessContext mobProcessContext = (MobProcessContext) context;
		MobileConfigDomain mobileConfigDomain = mobProcessContext.getMobileConfigDomain();
		String imei = arguArgs.get(1);
		String name = arguArgs.get(2);
		MobAddDeviceResponse response = mobileDeviceService.addDevice(mobileConfigDomain, imei, name, null);
		if (response.isSuccess()) {
			System.out.format(ColorUtils.blackBold("imei:%s add success"), imei);
			System.out.format(" " + "%n");
		}
	}
}
