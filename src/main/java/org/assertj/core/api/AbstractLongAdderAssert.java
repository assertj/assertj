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
package org.assertj.core.api;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Longs;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

import java.util.Comparator;
import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

/**
 * Base class for all implementations of assertions for {@link LongAdder}s.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *               target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *               for more details.
 *
 * @author Grzegorz Piwowarek
 * @since 3.16.0
 */
public class AbstractLongAdderAssert<SELF extends AbstractLongAdderAssert<SELF>> extends AbstractAssert<SELF, LongAdder> implements NumberAssert<SELF, Long>, ComparableAssert<SELF, Long> {

  @VisibleForTesting
  Longs longs = Longs.instance();

  public AbstractLongAdderAssert(LongAdder longAdder, Class<?> selfType) {
    super(longAdder, selfType);
  }

  /**
   * Verifies that the actual sum has the given value.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertion will pass
   * LongAdder actual = new LongAdder();
   * actual.add(42);
   * assertThat(actual).hasValue(42);
   *
   * // assertion will fail
   * assertThat(actual).hasValue(0);</code></pre>
   *
   * @param expectedValue the value not expected .
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @since 2.16.0 / 3.16.0
   */
  public SELF hasValue(long expectedValue) {
    isNotNull();
    long actualValue = actual.sum();
    if (!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldHaveValue(actual, expectedValue));
    }
    return myself;
  }

  /**
   * Verifies that the actual sum is not the given value.
   * <p>
   * Example:
   * <pre><code class='java'>
   * // assertion will pass
   * LongAdder actual = new LongAdder();
   * actual.add(42);
   * assertThat(actual).doesNotHaveValue(0);
   *
   * // assertion will fail
   * assertThat(actual).doesNotHaveValue(42);</code></pre>
   *
   * @param expectedValue the value not expected .
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum is not the given value.
   * @since 2.16.0 / 3.16.0
   */
  public SELF doesNotHaveValue(long expectedValue) {
    isNotNull();
    long actualValue = actual.sum();
    if (objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throwAssertionError(shouldNotContainValue(actual, expectedValue));
    }
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super LongAdder> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super LongAdder> customComparator, String customComparatorDescription) {
    longs = new Longs(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    longs = Longs.instance();
    return super.usingDefaultComparator();
  }

  @Override
  public SELF isZero() {
    longs.assertIsZero(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isNotZero() {
    longs.assertIsNotZero(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isOne() {
    longs.assertIsOne(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isPositive() {
    longs.assertIsPositive(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isNegative() {
    longs.assertIsNegative(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isNotNegative() {
    longs.assertIsNotNegative(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isNotPositive() {
    longs.assertIsNotPositive(info, actual.longValue());
    return myself;
  }

  @Override
  public SELF isEqualByComparingTo(Long other) {
    longs.assertEqualByComparison(info, actual.longValue(), other);
    return myself;
  }

  @Override
  public SELF isNotEqualByComparingTo(Long other) {
    longs.assertNotEqualByComparison(info, actual.longValue(), other);
    return myself;
  }

  @Override
  public SELF isLessThan(Long other) {
    longs.assertLessThan(info, actual.longValue(), other);
    return myself;
  }

  @Override
  public SELF isLessThanOrEqualTo(Long other) {
    longs.assertLessThanOrEqualTo(info, actual.longValue(), other);
    return myself;
  }

  @Override
  public SELF isGreaterThan(Long other) {
    longs.assertGreaterThan(info, actual.longValue(), other);
    return myself;
  }

  @Override
  public SELF isGreaterThanOrEqualTo(Long other) {
    longs.assertGreaterThanOrEqualTo(info, actual.longValue(), other);
    return myself;
  }

  @Override
  public SELF isBetween(Long start, Long end) {
    longs.assertIsBetween(info, actual.longValue(), start, end);
    return myself;
  }

  @Override
  public SELF isStrictlyBetween(Long start, Long end) {
    longs.assertIsStrictlyBetween(info, actual.longValue(), start, end);
    return myself;
  }

  @Override
  public SELF isCloseTo(Long expected, Offset<Long> offset) {
    longs.assertIsCloseTo(info, actual.longValue(), expected, offset);
    return myself;
  }

  @Override
  public SELF isNotCloseTo(Long expected, Offset<Long> offset) {
    longs.assertIsNotCloseTo(info, actual.longValue(), expected, offset);
    return myself;
  }

  @Override
  public SELF isCloseTo(Long expected, Percentage percentage) {
    longs.assertIsCloseToPercentage(info, actual.longValue(), expected, percentage);
    return myself;
  }

  @Override
  public SELF isNotCloseTo(Long expected, Percentage percentage) {
    longs.assertIsNotCloseToPercentage(info, actual.longValue(), expected, percentage);
    return myself;
  }
}
