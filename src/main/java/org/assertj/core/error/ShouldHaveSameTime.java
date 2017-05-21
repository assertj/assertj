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

import java.util.Date;

/**
 * Creates an error message indicating that an assertion that verifies that a date has same time as other date.
 *
 * @author Michal Kordas
 */
public class ShouldHaveSameTime extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveSameTime}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expected the expected timestamp.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSameTime(Date actual, Date expected) {
    return new ShouldHaveSameTime(actual, expected);
  }

  private ShouldHaveSameTime(Date actual, Date expected) {
    super("%nExpecting%n" +
          "  <%s>%n" +
          "to have the same time as:%n" +
          "  <%s>%n" +
          "but actual time is%n" +
          "  <%s>%n" +
          "and expected was:%n" +
          "  <%s>",
          actual, expected, actual.getTime(), expected.getTime());
  }
}
