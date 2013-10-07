/*
 * Created on Aug 5, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Creates an <code>{@link AssertionError}</code> indicating that an assertion that verifies that two dates (with max precision) are equals failed.
 *
 * @author William Delanoue
 */
public class ShouldBeEqualWithTimePrecision extends BasicErrorMessageFactory {

  private static final String EXPECTED_BUT_WAS_MESSAGE = "\nExpecting:\n <%s>\nto be equal to:\n <%s>\nbut was not using max precision of <%s>.";

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
    super(EXPECTED_BUT_WAS_MESSAGE, actual, expected, precision.name());
  }

}
