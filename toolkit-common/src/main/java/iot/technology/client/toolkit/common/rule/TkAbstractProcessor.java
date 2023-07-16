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
package iot.technology.client.toolkit.common.rule;

import iot.technology.client.toolkit.common.utils.ColorUtils;
import iot.technology.client.toolkit.common.utils.StringUtils;

/**
 * @author mushuwei
 */
public abstract class TkAbstractProcessor implements TkProcessor {

	public boolean validateParam(String param) {
		if (!StringUtils.isNumeric(param)) {
			System.out.format(ColorUtils.blackBold("%s is illegal"), param);
			System.out.format(" " + "%n");
			return false;
		}

		try {
			Integer.parseInt(param);
		} catch (NumberFormatException e) {
			System.out.format(ColorUtils.blackBold("%s is illegal"), param);
			System.out.format(" " + "%n");
			return false;
		}

		return true;
	}
}
