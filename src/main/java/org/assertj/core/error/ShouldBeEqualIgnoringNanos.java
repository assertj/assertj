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

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

/**
 * Creates an error message indicating that an assertion that verifies that :
 * <ul>
 * <li>two {@link ZonedDateTime}, {@link LocalDateTime} have same year, month, day, hour, minute and second failed.</li>
 * <li>two {@link LocalTime} have same hour, minute and second failed.</li>
 * <li>two {@link java.time.OffsetTime} have same hour, minute and second failed.</li>
 * </ul>
 * 
 * @author Joel Costigliola
 */
public class ShouldBeEqualIgnoringNanos extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeEqualIgnoringNanos}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualIgnoringNanos(Object actual, Object other) {
	return new ShouldBeEqualIgnoringNanos(actual, other);
  }

  /**
   * Creates a new <code>{@link ShouldBeEqualIgnoringNanos}</code>.
   * 
   * @param actual the actual LocalTime in the failed assertion.
   * @param other the LocalTime used in the failed assertion to compare the actual LocalTime to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualIgnoringNanos(LocalTime actual, LocalTime other) {
	return new ShouldBeEqualIgnoringNanos(actual, other);
  }

  /**
   * Creates a new <code>{@link ShouldBeEqualIgnoringNanos}</code>.
   *
   * @param actual the actual OffsetTime in the failed assertion.
   * @param other the OffsetTime used in the failed assertion to compare the actual OffsetTime to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualIgnoringNanos(OffsetTime actual, OffsetTime other) {
      return new ShouldBeEqualIgnoringNanos(actual, other);
  }

  private ShouldBeEqualIgnoringNanos(Object actual, Object other) {
	super("%nExpecting:%n  <%s>%nto have same year, month, day, hour, minute and second as:%n  <%s>%nbut had not.",
	      actual, other);
  }

  private ShouldBeEqualIgnoringNanos(LocalTime actual, LocalTime other) {
	super("%nExpecting:%n  <%s>%nto have same hour, minute and second as:%n  <%s>%nbut had not.", actual, other);
  }

  private ShouldBeEqualIgnoringNanos(OffsetTime actual, OffsetTime other) {
      super("%nExpecting:%n  <%s>%nto have same hour, minute and second as:%n  <%s>%nbut had not.", actual, other);
  }
}
