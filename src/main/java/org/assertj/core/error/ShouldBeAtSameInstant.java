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

import java.time.OffsetDateTime;

/**
 * Creates an error message indicating that an assertion that verifies that two {@link OffsetDateTime OffsetDateTimes}
 * have the same {@link java.time.Instant}, failed.
 *
 * @author Raymond Pressly
 */
public class ShouldBeAtSameInstant extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeAtSameInstant}</code>.
   * @param actual the actual value in the failed assertion.
   * @param other the expected value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeAtSameInstant(OffsetDateTime actual, OffsetDateTime other) {
    return new ShouldBeAtSameInstant(actual, other);
  }

  private ShouldBeAtSameInstant(OffsetDateTime actual, OffsetDateTime expected) {
    super("%nExpecting%n" +
          "  <%s>%n" +
          "to be at the same instant as:%n" +
          "  <%s>%n" +
          "but actual instance was%n" +
          "  <%s>%n" +
          "and expected instant was:%n" +
          "  <%s>",
          actual, expected, actual.toInstant(), expected.toInstant());
  }

}
