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

import static org.assertj.core.util.IterableUtil.isNullOrEmpty;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies a group of elements contains exactly a given set
 * of values and nothing else failed, exactly meaning same elements in same order. A group of elements can be a
 * collection, an array or a {@code String}.
 * 
 * @author Joel Costigliola
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
   */
  public static ErrorMessageFactory shouldContainExactly(Object actual, Object expected, Object notFound,
                                                         Object notExpected, ComparisonStrategy comparisonStrategy) {
    return new ShouldContainExactly(actual, expected, notFound, notExpected, comparisonStrategy);
  }

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
  public static ErrorMessageFactory shouldContainExactly(Object actual, Object expected, Object notFound,
                                                         Iterable<?> notExpected, ComparisonStrategy comparisonStrategy) {
    if (isNullOrEmpty(notExpected)) {
      return new ShouldContainExactly(actual, expected, notFound, comparisonStrategy);
    }

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
  public static ErrorMessageFactory shouldContainExactly(Object actual, Object expected, Object notFound,
                                                         Object notExpected) {
    return new ShouldContainExactly(actual, expected, notFound, notExpected, StandardComparisonStrategy.instance());
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
  public static ErrorMessageFactory shouldContainExactly(Object actual, Object expected, Object notFound,
                                                         Iterable<?> notExpected) {

    return shouldContainExactly(actual, expected, notFound, notExpected, StandardComparisonStrategy.instance());
  }

  public static ErrorMessageFactory shouldHaveSameSize(Object actual, Object expected, int actualSize,
                                                       int expectedSize, ComparisonStrategy comparisonStrategy) {
    return StandardComparisonStrategy.instance().equals(comparisonStrategy) ?
        new ShouldContainExactly(actual, expected, actualSize, expectedSize) :
        new ShouldContainExactly(actual, expected, actualSize, expectedSize, comparisonStrategy);
  }

  private ShouldContainExactly(Object actual, Object expected, int actualSize, int expectedSize,
                               ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Actual and expected should have same size but actual size was:%n" +
          "  <%s>%n" +
          "while expected size was:%n" +
          "  <%s>%n" +
          "Actual was:%n" +
          "  <%s>%n" +
          "Expected was:%n" +
          "  <%s>%n%s",
          actualSize, expectedSize, actual, expected, comparisonStrategy);
  }

  private ShouldContainExactly(Object actual, Object expected, int actualSize, int expectedSize) {
    super("%n" +
          "Actual and expected should have same size but actual size was:%n" +
          "  <%s>%n" +
          "while expected size was:%n" +
          "  <%s>%n" +
          "Actual was:%n" +
          "  <%s>%n" +
          "Expected was:%n" +
          "  <%s>%n",
          actualSize, expectedSize, actual, expected);
  }

  private ShouldContainExactly(Object actual, Object expected, Object notFound, Object notExpected,
                               ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to contain exactly (and in same order):%n" +
          "  <%s>%n" +
          "but some elements were not found:%n" +
          "  <%s>%n" +
          "and others were not expected:%n" +
          "  <%s>%n%s",
          actual, expected, notFound, notExpected, comparisonStrategy);
  }

  private ShouldContainExactly(Object actual, Object expected, Object notFound, ComparisonStrategy comparisonStrategy) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to contain exactly (and in same order):%n" +
          "  <%s>%n" +
          "but could not find the following elements:%n" +
          "  <%s>%n%s",
          actual, expected, notFound, comparisonStrategy);
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
          "  <%s>%n" +
          "whereas expected element was:%n" +
          "  <%s>%n%s",
          indexOfDifferentElements, actualElement, expectedElement, comparisonStrategy);
  }

}
