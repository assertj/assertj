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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.util.IterableUtil.isNullOrEmpty;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies map contains only a given set of values and
 * nothing else failed.
 *
 * @author Ilya Koshaleu
 */
public class ShouldContainOnlyValues extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainOnlyValues}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notExpected values in {@code actual} that were not in {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnlyValues(Object actual, Object expected, Object notFound,
                                                          Iterable<?> notExpected) {
    if (isNullOrEmpty(notExpected)) {
      return new ShouldContainOnlyValues(actual, expected, notFound, StandardComparisonStrategy.instance());
    }
    return new ShouldContainOnlyValues(actual, expected, notFound, notExpected, StandardComparisonStrategy.instance());
  }

  public ShouldContainOnlyValues(Object actual, Object expected, Object notFound, Object notExpected,
                                 ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Expecting actual:%n" +
          "  %s%n" +
          "to contain only following values:%n" +
          "  %s%n" +
          "values not found:%n" +
          "  %s%n" +
          "and values not expected:%n" +
          "  %s%n%s", actual,
          expected, notFound, notExpected, comparisonStrategy);
  }

  private ShouldContainOnlyValues(Object actual, Object expected, Object notFound, ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Expecting actual:%n" +
          "  %s%n" +
          "to contain only following values:%n" +
          "  %s%n" +
          "but could not find the following values:%n" +
          "  %s%n%s",
          actual, expected, notFound, comparisonStrategy);
  }
}
