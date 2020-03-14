/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import java.util.List;

public class AssertionErrorMessagesAggregrator {

  public static String aggregrateErrorMessages(List<String> errors) {
    StringBuilder msg = new StringBuilder("%nThe following ");
    countAssertions(errors, msg);
    msg.append(" failed:%n");

    for (int i = 0; i < errors.size(); i++) {
      msg.append(i + 1).append(") ").append(errors.get(i)).append("%n");
    }
    return MessageFormatter.instance().format(null, null, msg.toString());
  }

  private static void countAssertions(List<String> errors, StringBuilder msg) {
    int size = errors.size();
    if (size == 1) {
      msg.append("assertion");
    } else {
      msg.append(size).append(" assertions");
    }
  }

}
