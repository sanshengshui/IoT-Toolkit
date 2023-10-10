/*
 * Copyright © 2019-2023 The Toolkit Authors
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
package iot.technology.client.toolkit.nb.service.processor.telecom;

import iot.technology.client.toolkit.common.constants.StorageConstants;
import iot.technology.client.toolkit.common.rule.ProcessContext;
import iot.technology.client.toolkit.common.rule.TkProcessor;
import iot.technology.client.toolkit.common.utils.ColorUtils;
import iot.technology.client.toolkit.common.utils.StringUtils;

import java.util.List;
import java.util.ResourceBundle;

/**
 * @author mushuwei
 */
public class TelHelpProcessor implements TkProcessor {

	ResourceBundle bundle = ResourceBundle.getBundle(StorageConstants.LANG_MESSAGES);

	@Override
	public boolean supports(ProcessContext context) {
		return context.getData().startsWith("help");
	}

	@Override
	public void handle(ProcessContext context) {
		List<String> arguArgs = List.of(context.getData().split(" "));
		if (arguArgs.size() > 2) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format(ColorUtils.redError("argument:%s is illegal"), context.getData()))
					.append(StringUtils.lineSeparator());
			sb.append(ColorUtils.blackBold("usage: help [subCommand]"));
			System.out.println(sb);
			return;
		}
		//user type help
		if (arguArgs.size() == 1) {
			printAllHelpInfo();
			return;
		}
		String subCommand = arguArgs.get(1);
		StringBuilder sb = new StringBuilder();
		switch (subCommand) {
			case "list":
			case "ls":
				sb.append(ColorUtils.colorBold("Usage:  ", "black")
								+ String.format("> %s [%s <searchValue>] [%s <pageNo>] [%s <pageSize>]",
								ColorUtils.colorBold("ls,list", "green"),
								ColorUtils.colorBold("-sv", "green"),
								ColorUtils.colorBold("-pn", "green"),
								ColorUtils.colorBold("-ps", "green")));
				sb.append(StringUtils.lineSeparator());
				sb.append(bundle.getString("nb.operation.list.desc")).append(StringUtils.lineSeparator());
				sb.append(StringUtils.lineSeparator());
				sb.append("Options:").append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-sv --searchValue  ", "green"),
								bundle.getString("nb.cmd.sv.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-pn --pageNo       ", "green"),
								bundle.getString("nb.cmd.pn.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-ps --pageSize     ", "green"),
								bundle.getString("nb.cmd.ps.desc")))
						.append(StringUtils.lineSeparator());
				System.out.print(sb);
				break;
			case "add":
				sb.append(ColorUtils.colorBold("Usage:  ", "black")
						+ String.format("> %s %s <imei> %s <name> [%s] [%s <imsi>] [%s <pskType:0 or 1>] [%s <pskValue>]",
						ColorUtils.colorBold("add", "green"),
						ColorUtils.colorBold("-imei", "green"),
						ColorUtils.colorBold("-name", "green"),
						ColorUtils.colorBold("-notObserv", "green"),
						ColorUtils.colorBold("-imsi", "green"),
						ColorUtils.colorBold("-pskType", "green"),
						ColorUtils.colorBold("-pskValue", "green")));
				sb.append(StringUtils.lineSeparator());
				sb.append(bundle.getString("nb.operation.add.desc")).append(StringUtils.lineSeparator());
				sb.append(StringUtils.lineSeparator());
				sb.append("Options:").append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-imei       ", "green"),
								bundle.getString("nb.cmd.imei.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-name       ", "green"),
								bundle.getString("nb.cmd.name.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-notObserv  ", "green"),
								bundle.getString("nb.cmd.imsi.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-imsi       ", "green"),
								bundle.getString("nb.cmd.notObserv.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-pskType    ", "green"),
								bundle.getString("nb.cmd.pskType.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-pskValue   ", "green"),
								bundle.getString("nb.cmd.pskValue.desc")))
						.append(StringUtils.lineSeparator());
				System.out.print(sb);
				break;
			case "update":
				sb.append(ColorUtils.colorBold("Usage:  ", "black")
						+ String.format("> %s %s <imei> [%s <name>] [%s] [%s <imsi>]",
						ColorUtils.colorBold("update", "green"),
						ColorUtils.colorBold("-imei", "green"),
						ColorUtils.colorBold("-name", "green"),
						ColorUtils.colorBold("-notObserv", "green"),
						ColorUtils.colorBold("-imsi", "green")));
				sb.append(StringUtils.lineSeparator());
				sb.append(bundle.getString("nb.operation.update.desc")).append(StringUtils.lineSeparator());
				sb.append(StringUtils.lineSeparator());
				sb.append("Options:").append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-imei       ", "green"),
								bundle.getString("nb.cmd.imei.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-name       ", "green"),
								bundle.getString("nb.cmd.name.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-notObserv  ", "green"),
								bundle.getString("nb.cmd.imsi.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-imsi       ", "green"),
								bundle.getString("nb.cmd.notObserv.desc")))
						.append(StringUtils.lineSeparator());
				System.out.print(sb);
				break;
			case "del":
				sb.append(ColorUtils.colorBold("Usage:  ", "black")
						+ String.format("> %s %s",
						ColorUtils.colorBold("del,delete", "green"),
						ColorUtils.colorBold("imei", "green")));
				sb.append(StringUtils.lineSeparator());
				sb.append(bundle.getString("nb.operation.del.desc")).append(StringUtils.lineSeparator());
				sb.append(StringUtils.lineSeparator());
				System.out.print(sb);
				break;
			case "show":
				sb.append(ColorUtils.colorBold("Usage:  ", "black")
						+ String.format("> %s %s",
						ColorUtils.colorBold("show", "green"),
						ColorUtils.colorBold("imei", "green")));
				sb.append(StringUtils.lineSeparator());
				sb.append(bundle.getString("nb.operation.get.desc")).append(StringUtils.lineSeparator());
				sb.append(StringUtils.lineSeparator());
				System.out.print(sb);
				break;
			case "log":
				sb.append(ColorUtils.colorBold("Usage:  ", "black")
						+ String.format("> %s %s <imei> [%s <name>] [%s] [%s <imsi>]",
						ColorUtils.colorBold("log", "green"),
						ColorUtils.colorBold("-imei", "green"),
						ColorUtils.colorBold("-limit", "green"),
						ColorUtils.colorBold("-startTime", "green"),
						ColorUtils.colorBold("-endTime", "green"),
						ColorUtils.colorBold("-hex", "green")));
				sb.append(StringUtils.lineSeparator());
				sb.append(bundle.getString("nb.operation.log.desc")).append(StringUtils.lineSeparator());
				sb.append(StringUtils.lineSeparator());
				sb.append("Options:").append(StringUtils.lineSeparator());

				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-imei        ", "green"),
								bundle.getString("nb.cmd.imei.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-limit        ", "green"),
								bundle.getString("nb.cmd.imei.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-startTime    ", "green"),
								bundle.getString("nb.cmd.imei.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-endTime      ", "green"),
								bundle.getString("nb.cmd.imei.desc")))
						.append(StringUtils.lineSeparator());
				sb.append(String.format("%s %s",
								ColorUtils.colorBold("-hex          ", "green"),
								bundle.getString("nb.cmd.imei.desc")))
						.append(StringUtils.lineSeparator());
				break;
			case "command":
			case "cmd":
				sb.append(ColorUtils.cyanAnnotation("command:  " + bundle.getString("nb.operation.command.desc")))
						.append(StringUtils.lineSeparator());
				sb.append("    usage: time format:2019-02-01T00:01:01").append(StringUtils.lineSeparator());
				sb.append("         - command imei : print first page delivery command of the device").append(StringUtils.lineSeparator());
				sb.append("         - command imei pageNo : print pageNo delivery command of the device").append(StringUtils.lineSeparator());
				System.out.format(sb.toString());
				break;
			default:
				break;

		}

	}

	public void printAllHelpInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("").append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold("Usage:", "black") + ColorUtils.colorBold(" > ", "green")
				+ "{ add | del | update | show | list | log | command | exit }").append(StringUtils.lineSeparator());
		sb.append("").append(StringUtils.lineSeparator());
		sb.append(bundle.getString("nb.shellmode.help")).append(StringUtils.lineSeparator());
		sb.append("").append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold(bundle.getString("general.commands"), "black"))
				.append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold("  help             ", "green")).append(bundle.getString("general.subCommand.help")).append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold("  add              ", "green")).append(bundle.getString("nb.operation.add.desc")).append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold("  del, delete      ", "green")).append(bundle.getString("nb.operation.del.desc")).append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold("  update           ", "green")).append(bundle.getString("nb.operation.update.desc")).append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold("  show             ", "green")).append(bundle.getString("nb.operation.get.desc")).append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold("  ls, list         ", "green")).append(bundle.getString("nb.operation.list.desc")).append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold("  log              ", "green")).append(bundle.getString("nb.operation.log.desc")).append(StringUtils.lineSeparator());
		sb.append(ColorUtils.colorBold("  command          ", "green")).append(bundle.getString("nb.operation.command.desc")).append(StringUtils.lineSeparator());
		System.out.println(sb);

	}
}
