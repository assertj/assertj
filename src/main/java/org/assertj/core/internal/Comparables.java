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
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeGreater.shouldBeGreater;
import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.error.ShouldBeLess.shouldBeLess;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.util.Comparator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Comparable}</code>s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Comparables {

  private static final Comparables INSTANCE = new Comparables();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * 
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Comparables instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();
  final ComparisonStrategy comparisonStrategy;

  @VisibleForTesting
  public Comparables() {
    this(StandardComparisonStrategy.instance());
  }

  public Comparables(ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy) {
      return ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator();
    }
    return null;
  }

  @VisibleForTesting
  void setFailures(Failures failures) {
    this.failures = failures;
  }

  @VisibleForTesting
  void resetFailures() {
    this.failures = Failures.instance();
  }

  /**
   * Asserts that two T instances are equal.
   * 
   * @param <T> the type of actual and expected
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   *           {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual
   *           values are not equal.
   */
  public <T> void assertEqual(AssertionInfo info, T actual, T expected) {
    assertNotNull(info, actual);
    if (areEqual(actual, expected)) return;
    throw failures.failure(info, shouldBeEqual(actual, expected, comparisonStrategy, info.representation()));
  }

  protected <T> boolean areEqual(T actual, T expected) {
    return comparisonStrategy.areEqual(actual, expected);
  }

  /**
   * Asserts that two T instances are not equal.
   * 
   * @param <T> the type of actual and expected
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the other one.
   */
  public <T> void assertNotEqual(AssertionInfo info, T actual, T other) {
    assertNotNull(info, actual);
    if (!areEqual(actual, other))
      return;
    throw failures.failure(info, shouldNotBeEqual(actual, other, comparisonStrategy));
  }

  /**
   * Asserts that two <code>{@link Comparable}</code>s are equal by invoking
   * <code>{@link Comparable#compareTo(Object)}</code>.<br>
   * Note that it does not rely on the custom {@link #comparisonStrategy} if one has been set.
   * 
   * @param <T> used to guarantee that two objects of the same type are being compared against each other.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param expected the expected value.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the expected one. This method will throw a
   *           {@code org.junit.ComparisonFailure} instead if JUnit is in the classpath and the expected and actual
   *           values are not equal.
   */
  public <T extends Comparable<? super T>> void assertEqualByComparison(AssertionInfo info, T actual, T expected) {
    assertNotNull(info, actual);
    // we don't delegate to comparisonStrategy, as this assertion makes it clear it relies on Comparable
    if (actual.compareTo(expected) == 0)
      return;
    throw failures.failure(info, shouldBeEqual(actual, expected, info.representation()));
  }

  /**
   * Asserts that two <code>{@link Comparable}</code>s are not equal by invoking
   * <code>{@link Comparable#compareTo(Object)}</code> .<br>
   * Note that it does not rely on the custom {@link #comparisonStrategy} if one has been set.
   * 
   * @param <T> used to guarantee that two objects of the same type are being compared against each other.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the other one.
   */
  public <T extends Comparable<? super T>> void assertNotEqualByComparison(AssertionInfo info, T actual, T other) {
    assertNotNull(info, actual);
    // we don't delegate to comparisonStrategy, as this assertion makes it clear it relies on Comparable
    if (actual.compareTo(other) != 0)
      return;
    throw failures.failure(info, shouldNotBeEqual(actual, other));
  }

  /**
   * Asserts that the actual value is less than the other one.
   * 
   * @param <T> used to guarantee that two objects of the same type are being compared against each other.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not less than the other one: this assertion will fail if the actual
   *           value is equal to or greater than the other value.
   */
  public <T extends Comparable<? super T>> void assertLessThan(AssertionInfo info, T actual, T other) {
    assertNotNull(info, actual);
    if (isLessThan(actual, other))
      return;
    throw failures.failure(info, shouldBeLess(actual, other, comparisonStrategy));
  }

  /**
   * Asserts that the actual value is less than or equal to the other one.
   * 
   * @param <T> used to guarantee that two objects of the same type are being compared against each other.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the other one.
   */
  public <T extends Comparable<? super T>> void assertLessThanOrEqualTo(AssertionInfo info, T actual, T other) {
    assertNotNull(info, actual);
    if (!isGreaterThan(actual, other))
      return;
    throw failures.failure(info, shouldBeLessOrEqual(actual, other, comparisonStrategy));
  }

  /**
   * Asserts that the actual value is greater than the other one.
   * 
   * @param <T> used to guarantee that two objects of the same type are being compared against each other.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not greater than the other one: this assertion will fail if the
   *           actual value is equal to or less than the other value.
   */
  public <T extends Comparable<? super T>> void assertGreaterThan(AssertionInfo info, T actual, T other) {
    assertNotNull(info, actual);
    if (isGreaterThan(actual, other))
      return;
    throw failures.failure(info, shouldBeGreater(actual, other, comparisonStrategy));
  }

  /**
   * delegates to {@link #comparisonStrategy#isGreaterThan(Object, Object)}
   */
  private boolean isGreaterThan(Object actual, Object other) {
    return comparisonStrategy.isGreaterThan(actual, other);
  }

  /**
   * Asserts that the actual value is greater than or equal to the other one.
   * 
   * @param <T> used to guarantee that two objects of the same type are being compared against each other.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param other the value to compare the actual value to.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the other one.
   */
  public <T extends Comparable<? super T>> void assertGreaterThanOrEqualTo(AssertionInfo info, T actual, T other) {
    assertNotNull(info, actual);
    if (!isLessThan(actual, other))
      return;
    throw failures.failure(info, shouldBeGreaterOrEqual(actual, other, comparisonStrategy));
  }

  private boolean isLessThan(Object actual, Object other) {
    return comparisonStrategy.isLessThan(actual, other);
  }

  protected static <T> void assertNotNull(AssertionInfo info, T actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  /**
   * Asserts that the actual value is between start and end, inclusive or not.
   * 
   * @param <T> used to guarantee that two objects of the same type are being compared against each other.
   * @param info contains information about the assertion.
   * @param actual the actual value.
   * @param start the start value.
   * @param end the end value.
   * @param inclusiveStart if start is inclusive (fail is actual == start and inclusiveStart is false).
   * @param inclusiveEnd if end is inclusive (fail is actual == end and inclusiveEnd is false).
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not between start and end.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws IllegalArgumentException if end value is less than start value.
   */
  public <T extends Comparable<? super T>> void assertIsBetween(AssertionInfo info, T actual, T start, T end,
                                                                boolean inclusiveStart, boolean inclusiveEnd) {
    assertNotNull(info, actual);
    checkNotNull(start, "The start range to compare actual with should not be null");
    checkNotNull(end, "The end range to compare actual with should not be null");
    checkArgument(inclusiveEnd && inclusiveStart && comparisonStrategy.isLessThanOrEqualTo(start, end) ||
                  !inclusiveEnd && !inclusiveStart && comparisonStrategy.isLessThan(start, end),
                  String.format("The end value <%s> must not be %s the start value <%s>%s!", end,
                                (inclusiveEnd && inclusiveStart ? "less than" : "less than or equal to"), start,
                                (comparisonStrategy.isStandard() ? "" : " (using " + comparisonStrategy + ")")));
    boolean checkLowerBoundaryRange = inclusiveStart ? !isGreaterThan(start, actual)
        : isLessThan(start, actual);
    boolean checkUpperBoundaryRange = inclusiveEnd ? !isGreaterThan(actual, end)
        : isLessThan(actual, end);
    if (checkLowerBoundaryRange && checkUpperBoundaryRange)
      return;
    throw failures.failure(info, shouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, comparisonStrategy));
  }
}
