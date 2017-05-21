/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.util.DateUtil.formatTimeDifference;

import java.util.Date;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Date} is in same hour window as
 * another one failed.
 *
 * @author Joel Costigliola
 */
public class ShouldBeInSameHourWindow extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeInSameHourWindow}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param other  the value used in the failed assertion to compare the actual value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInSameHourWindow(Date actual, Date other) {
    return new ShouldBeInSameHourWindow(actual, other);
  }

  private ShouldBeInSameHourWindow(Date actual, Date other) {
    super("%nExpecting:%n  <%s>%nto be close to:%n  <%s>%nby less than one hour (strictly) but difference was: "
            + formatTimeDifference(actual, other), actual, other);
  }
}
