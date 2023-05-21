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

import static org.assertj.core.error.GroupTypeDescription.getGroupTypeDescription;
import static org.assertj.core.error.ShouldContainOnly.ErrorType.NOT_EXPECTED_ONLY;
import static org.assertj.core.error.ShouldContainOnly.ErrorType.NOT_FOUND_ONLY;
import static org.assertj.core.util.IterableUtil.isNullOrEmpty;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains only a given set of
 * values and
 * nothing else failed. A group of elements can be a collection, an array or a {@code String}.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldContainOnly extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainOnly}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notExpected values in {@code actual} that were not in {@code expected}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnly(Object actual, Object expected, Iterable<?> notFound,
                                                      Iterable<?> notExpected, ComparisonStrategy comparisonStrategy) {
    GroupTypeDescription groupTypeDescription = getGroupTypeDescription(actual);
    return shouldContainOnly(actual, expected, notFound, notExpected, comparisonStrategy, groupTypeDescription);
  }

  public static ErrorMessageFactory shouldContainOnly(Object actual, Object expected, Iterable<?> notFound,
                                                      Iterable<?> notExpected, GroupTypeDescription groupTypeDescription) {
    return shouldContainOnly(actual, expected, notFound, notExpected, StandardComparisonStrategy.instance(),
                             groupTypeDescription);
  }

  private static ErrorMessageFactory shouldContainOnly(Object actual, Object expected, Iterable<?> notFound,
                                                       Iterable<?> notExpected, ComparisonStrategy comparisonStrategy,
                                                       GroupTypeDescription groupTypeDescription) {
    if (isNullOrEmpty(notExpected))
      return new ShouldContainOnly(actual, expected, notFound, NOT_FOUND_ONLY, comparisonStrategy, groupTypeDescription);
    if (isNullOrEmpty(notFound))
      return new ShouldContainOnly(actual, expected, notExpected, NOT_EXPECTED_ONLY, comparisonStrategy, groupTypeDescription);
    return new ShouldContainOnly(actual, expected, notFound, notExpected, comparisonStrategy, groupTypeDescription);
  }

  /**
   * Creates a new <code>{@link ShouldContainOnly}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notExpected values in {@code actual} that were not in {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnly(Object actual, Object expected, Iterable<?> notFound,
                                                      Iterable<?> notExpected) {
    return shouldContainOnly(actual, expected, notFound, notExpected, StandardComparisonStrategy.instance());
  }

  private ShouldContainOnly(Object actual, Object expected, Iterable<?> notFound, Iterable<?> notExpected,
                            ComparisonStrategy comparisonStrategy, GroupTypeDescription groupTypeDescription) {
    super("%n" +
          "Expecting " + groupTypeDescription.getGroupTypeName() + ":%n" +
          "  %s%n" +
          "to contain only:%n" +
          "  %s%n" +
          groupTypeDescription.getElementTypeName() + " not found:%n" +
          "  %s%n" +
          "and " + groupTypeDescription.getElementTypeName() + " not expected:%n" +
          "  %s%n%s", actual,
          expected, notFound, notExpected, comparisonStrategy);
  }

  private ShouldContainOnly(Object actual, Object expected, Iterable<?> notFoundOrNotExpected, ErrorType errorType,
                            ComparisonStrategy comparisonStrategy, GroupTypeDescription groupTypeDescription) {
    // @format:off
    super("%n" +
          "Expecting "+groupTypeDescription.getGroupTypeName()+":%n" +
          "  %s%n" +
          "to contain only:%n" +
          "  %s%n" + (errorType == NOT_FOUND_ONLY ?
          "but could not find the following "+groupTypeDescription.getElementTypeName()+":%n" : "but the following "+groupTypeDescription.getElementTypeName()+" were unexpected:%n") +
          "  %s%n%s",
          actual, expected, notFoundOrNotExpected, comparisonStrategy);
    // @format:on
  }

  public enum ErrorType {
    NOT_FOUND_ONLY, NOT_EXPECTED_ONLY
  }

}
