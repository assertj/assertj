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

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a group of elements does not end with a
 * given value or sequence of values failed. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Michal Kordas
 */
public class ShouldNotEndWith extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotEndWith}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected the value or sequence of values that {@code actual} is expected to not end with.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotEndWith(Object actual, Object expected,
      ComparisonStrategy comparisonStrategy) {
    return new ShouldNotEndWith(actual, expected, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldNotEndWith}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected the value or sequence of values that {@code actual} is expected to not end with.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotEndWith(Object actual, Object expected) {
    return new ShouldNotEndWith(actual, expected, StandardComparisonStrategy.instance());
  }

  private ShouldNotEndWith(Object actual, Object expected, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n  <%s>%nnot to end with:%n  <%s>%n%s", actual, expected, comparisonStrategy);
  }
}
