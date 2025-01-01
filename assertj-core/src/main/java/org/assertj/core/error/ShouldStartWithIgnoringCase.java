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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.ComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies {@link CharSequence} starts with a given value
 * (ignoring case considerations) failed.
 */
public class ShouldStartWithIgnoringCase extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldStartWithIgnoringCase}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the value or sequence of values that {@code actual} is expected to start with, ignoring case.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldStartWithIgnoringCase(CharSequence actual, CharSequence expected,
                                                                ComparisonStrategy comparisonStrategy) {
    return new ShouldStartWithIgnoringCase(actual, expected, comparisonStrategy);
  }

  private ShouldStartWithIgnoringCase(CharSequence actual, CharSequence expected, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto start with (ignoring case):%n  %s%n%s", actual, expected, comparisonStrategy);
  }
}
