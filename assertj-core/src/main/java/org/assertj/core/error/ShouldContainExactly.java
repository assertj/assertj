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

import static java.lang.String.format;
import static org.assertj.core.util.IterableUtil.isNullOrEmpty;
import static org.assertj.core.util.Strings.escapePercent;

import java.util.List;

import org.assertj.core.configuration.Configuration;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.IndexedDiff;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains exactly a given set
 * of values and nothing else failed, exactly meaning same elements in same order. A group of elements can be a
 * collection, an array or a {@code String}.
 * 
 * @author Joel Costigliola
 * @author Yanming Zhou
 */
public class ShouldContainExactly extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainExactly}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notExpected values in {@code actual} that were not in {@code expected}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   * 
   */
  public static ErrorMessageFactory shouldContainExactly(Object actual, Iterable<?> expected,
                                                         Iterable<?> notFound, Iterable<?> notExpected,
                                                         ComparisonStrategy comparisonStrategy) {
    if (isNullOrEmpty(notExpected) && isNullOrEmpty(notFound))
      return new ShouldContainExactly(actual, expected, comparisonStrategy);
    if (isNullOrEmpty(notExpected))
      return new ShouldContainExactly(actual, expected, notFound, comparisonStrategy);
    if (isNullOrEmpty(notFound))
      return new ShouldContainExactly(actual, expected, comparisonStrategy, notExpected);
    return new ShouldContainExactly(actual, expected, notFound, notExpected, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainExactly}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param notFound values in {@code expected} not found in {@code actual}.
   * @param notExpected values in {@code actual} that were not in {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainExactly(Object actual, Iterable<?> expected,
                                                         Iterable<?> notFound, Iterable<?> notExpected) {
    return shouldContainExactly(actual, expected, notFound, notExpected, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new {@link ShouldContainExactly}.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param indexDifferences the {@link IndexedDiff} the actual and expected differ at.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   *
   */
  public static ErrorMessageFactory shouldContainExactlyWithIndexes(Object actual, Iterable<?> expected,
                                                                    List<IndexedDiff> indexDifferences,
                                                                    ComparisonStrategy comparisonStrategy) {
    return new ShouldContainExactly(actual, expected, indexDifferences, comparisonStrategy);
  }

  /**
   * Creates a new {@link ShouldContainExactly}.
   *
   * @param actual the actual value in the failed assertion.
   * @param expected values expected to be contained in {@code actual}.
   * @param indexDifferences the {@link IndexedDiff} the actual and expected differ at.
   * @return the created {@code ErrorMessageFactory}.
   *
   */
  public static ErrorMessageFactory shouldContainExactlyWithIndexes(Object actual, Iterable<?> expected,
                                                                    List<IndexedDiff> indexDifferences) {
    return new ShouldContainExactly(actual, expected, indexDifferences, StandardComparisonStrategy.instance());
  }

  private ShouldContainExactly(Object actual, Object expected, ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Expecting actual:%n" +
          "  %s%n" +
          "to contain exactly (and in same order):%n" +
          "  %s%n",
          actual, expected, comparisonStrategy);
  }

  private ShouldContainExactly(Object actual, Object expected, Object notFound, Object notExpected,
                               ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Expecting actual:%n" +
          "  %s%n" +
          "to contain exactly (and in same order):%n" +
          "  %s%n" +
          "but some elements were not found:%n" +
          "  %s%n" +
          "and others were not expected:%n" +
          "  %s%n%s",
          actual, expected, notFound, notExpected, comparisonStrategy);
  }

  private ShouldContainExactly(Object actual, Object expected, Object notFound, ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Expecting actual:%n" +
          "  %s%n" +
          "to contain exactly (and in same order):%n" +
          "  %s%n" +
          "but could not find the following elements:%n" +
          "  %s%n%s",
          actual, expected, notFound, comparisonStrategy);
  }

  private ShouldContainExactly(Object actual, Object expected, ComparisonStrategy comparisonStrategy,
                               Object unexpected) {
    super("%n" +
          "Expecting actual:%n" +
          "  %s%n" +
          "to contain exactly (and in same order):%n" +
          "  %s%n" +
          "but some elements were not expected:%n" +
          "  %s%n%s",
          actual, expected, unexpected, comparisonStrategy);
  }

  private ShouldContainExactly(Object actual, Object expected, List<IndexedDiff> indexDiffs,
                               ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Expecting actual:%n" +
          "  %s%n" +
          "to contain exactly (and in same order):%n" +
          "  %s%n" +
          formatIndexDifferences(indexDiffs), actual, expected, comparisonStrategy);
  }

  private static String formatIndexDifferences(List<IndexedDiff> indexedDiffs) {
    StringBuilder sb = new StringBuilder();
    sb.append("but there were differences at these indexes");
    if (indexedDiffs.size() >= Configuration.MAX_INDICES_FOR_PRINTING) {
      sb.append(format(" (only showing the first %d mismatches)", Configuration.MAX_INDICES_FOR_PRINTING));
    }
    sb.append(":%n");
    for (IndexedDiff diff : indexedDiffs) {
      sb.append(escapePercent(format("  - element at index %d: expected \"%s\" but was \"%s\"%n",
                                     diff.index, diff.expected, diff.actual)));
    }
    return sb.toString();
  }

  /**
   * Creates a new <code>{@link ShouldContainExactly}</code> for the case where actual and expected have the same
   * elements in different order according to the given {@link ComparisonStrategy}.
   * 
   * @param actualElement the actual element at indexOfDifferentElements index.
   * @param expectedElement the expected element at indexOfDifferentElements index.
   * @param indexOfDifferentElements index where actual and expect differs.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory elementsDifferAtIndex(Object actualElement, Object expectedElement,
                                                          int indexOfDifferentElements,
                                                          ComparisonStrategy comparisonStrategy) {
    return new ShouldContainExactly(actualElement, expectedElement, indexOfDifferentElements, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldContainExactly}</code> for the case where actual and expected have the same
   * elements in different order.
   * 
   * @param actualElement the actual element at indexOfDifferentElements index.
   * @param expectedElement the expected element at indexOfDifferentElements index.
   * @param indexOfDifferentElements index where actual and expect differs.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory elementsDifferAtIndex(Object actualElement, Object expectedElement,
                                                          int indexOfDifferentElements) {
    return new ShouldContainExactly(actualElement, expectedElement, indexOfDifferentElements,
                                    StandardComparisonStrategy.instance());
  }

  private ShouldContainExactly(Object actualElement, Object expectedElement, int indexOfDifferentElements,
                               ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Actual and expected have the same elements but not in the same order, at index %s actual element was:%n" +
          "  %s%n" +
          "whereas expected element was:%n" +
          "  %s%n%s",
          indexOfDifferentElements, actualElement, expectedElement, comparisonStrategy);
  }

}
