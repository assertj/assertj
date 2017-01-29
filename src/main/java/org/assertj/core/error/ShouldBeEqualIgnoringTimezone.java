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

import java.time.OffsetDateTime;
import java.time.OffsetTime;

/**
 * Creates an error message indicating that an assertion that verifies that two {@link java.time.OffsetTime} have same
 * time fields except the timezone.
 *
 * @author Alexander Bischof
 */
public class ShouldBeEqualIgnoringTimezone extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeEqualIgnoringTimezone}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualIgnoringTimezone(OffsetTime actual, OffsetTime other) {
    return new ShouldBeEqualIgnoringTimezone(actual, other);
  }

  /**
   * Creates a new <code>{@link ShouldBeEqualIgnoringTimezone}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param other the value used in the failed assertion to compare the actual value to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualIgnoringTimezone(OffsetDateTime actual, OffsetDateTime other) {
      return new ShouldBeEqualIgnoringTimezone(actual, other);
  }

  private ShouldBeEqualIgnoringTimezone(Object actual, Object other) {
    super("%nExpecting:%n  <%s>%nto have same time fields except timezone as:%n  <%s>%nbut had not.", actual, other);
  }
}
