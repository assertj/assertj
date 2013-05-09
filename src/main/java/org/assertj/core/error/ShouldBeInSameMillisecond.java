/*
 * Created on Oct 18, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;

import java.util.Date;

import org.assertj.core.util.Dates;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Date} is in same year, month, day
 * of month, hour, minute, second and millisecond as another one failed.
 * 
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class ShouldBeInSameMillisecond extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ShouldBeInSameMillisecond}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInSameMillisecond(Date actual, Date other) {
    return new ShouldBeInSameMillisecond(actual, other);
  }

  private ShouldBeInSameMillisecond(Date actual, Date other) {

    super(format(
        "\nExpecting:\n <%s>\nto be on same year, month, day, hour, minute, second and millisecond as:\n <%s>",
        Dates.formatAsDatetimeWithMs(actual), Dates.formatAsDatetimeWithMs(other)));
  }
}
