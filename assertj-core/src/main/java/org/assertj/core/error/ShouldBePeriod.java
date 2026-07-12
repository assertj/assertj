/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import java.time.Period;

/**
 * Creates an error message indicating that a {@code Period} error.
 *
 * @author Hayden Meloche
 */
public class ShouldBePeriod extends BasicErrorMessageFactory {

  private static final String EXPECTED_PREFIX = "%nExpecting Period:%n  %s%nto be ";

  private ShouldBePeriod(Period actual, String metric) {
    super(EXPECTED_PREFIX + metric, actual);
  }

  /**
   * Creates an error for a period expected to be positive.
   *
   * @param actual the actual period
   * @return the error message factory
   */
  public static ShouldBePeriod shouldBePositive(Period actual) {
    return new ShouldBePeriod(actual, "positive");
  }

  /**
   * Creates an error for a period expected to be negative.
   *
   * @param actual the actual period
   * @return the error message factory
   */
  public static ShouldBePeriod shouldBeNegative(Period actual) {
    return new ShouldBePeriod(actual, "negative");
  }
}
