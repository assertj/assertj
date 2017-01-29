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
import java.util.concurrent.TimeUnit;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two dates  are equals
 * up to a given precision failed.
 *
 * @author William Delanoue
 * @author Joel Costigliola
 */
public class ShouldBeEqualWithTimePrecision extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link org.assertj.core.error.ShouldBeEqualWithTimePrecision}</code>.
   *
   * @param actual    the actual value in the failed assertion.
   * @param expected  the expected value in the failed assertion.
   * @param precision the {@link TimeUnit} used to compare actual with expected.
   * @return the created {@code AssertionErrorFactory}.
   */
  public static ErrorMessageFactory shouldBeEqual(Date actual, Date expected, TimeUnit precision) {
    return new ShouldBeEqualWithTimePrecision(actual, expected, precision);
  }

  private ShouldBeEqualWithTimePrecision(Date actual, Date expected, TimeUnit precision) {
    super(buildErrorMessageTemplate(precision), actual, expected);
  }

  private static String buildErrorMessageTemplate(final TimeUnit precision) {
    String fields = "";
    String lastField = "";
    if (precision.equals(TimeUnit.HOURS)) {
      lastField = "day";
    } else if (precision.equals(TimeUnit.MINUTES)) {
      fields = ", day";
      lastField = "hour";
    } else if (precision.equals(TimeUnit.SECONDS)) {
      fields = ", day, hour";
      lastField = "minute";
    } else if (precision.equals(TimeUnit.MILLISECONDS)) {
      fields = ", day, hour, minute";
      lastField = "second";
    }
    return "%nExpecting:%n  <%s>%nto have same year, month" + fields + " and " + lastField + " as:%n  <%s>%nbut had " +
             "not.";
  }

}
