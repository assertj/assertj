/*
 * Created on Oct 18, 2010
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
package org.fest.assertions.error;

import static java.lang.String.format;

import static org.fest.util.Dates.formatAsDatetimeWithMs;

import java.util.Date;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Date} is close to another one from some delta
 * failed.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeCloseTo extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldBeCloseTo}</code>.
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @param deltaInMilliseconds the delta used for date comparison, expressed in milliseconds.
   * @param difference the difference in milliseconds between actual and other dates.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeCloseTo(Date actual, Date other, long deltaInMilliseconds, long difference) {
    return new ShouldBeCloseTo(actual, other, deltaInMilliseconds, difference);
  }

  private ShouldBeCloseTo(Date actual, Date other, long deltaInMilliseconds, long difference) {
    // format Date up to the given ms, because defaut format is the second, thus dates with a difference less than 1s
    // seems equal in the error message.
    // Use standard formatting to avoid calling ToString.toStringOf for long that adds a 'L' (like 100L) to
    // differentiate integer from long (here there is no ambiguity).
    super(format("expected '%s' to be close to '%s' by less than %sms but difference was of %sms",
        formatAsDatetimeWithMs(actual), formatAsDatetimeWithMs(other), deltaInMilliseconds, difference));
  }
}
