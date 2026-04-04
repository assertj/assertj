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
package org.assertj.core.api;

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

import java.util.Comparator;
import java.util.concurrent.atomic.LongAdder;

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.Longs;

/**
 * Base class for all implementations of assertions for {@link LongAdder}s.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="https://bit.ly/1IZIRcY"
 *               target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *               for more details.
 *
 * @author Grzegorz Piwowarek
 * @since 3.16.0
 */
public class AbstractLongAdderAssert<SELF extends AbstractLongAdderAssert<SELF>>
    extends AbstractAssertWithComparator<SELF, LongAdder>
    implements NumberAssert<SELF, Long>, ComparableAssert<SELF, Long> {

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Longs longs = Longs.instance();

  protected AbstractLongAdderAssert(LongAdder longAdder, Class<?> selfType) {
    super(longAdder, selfType);
  }

  /**
   * Verifies that the actual sum has the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * LongAdder actual = new LongAdder();
   * actual.add(42);
   * assertThat(actual).hasValue(42);
   *
   * // assertion will fail
   * assertThat(actual).hasValue(0);</code></pre>
   *
   * @param expected the expected value.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual adder is {@code null}.
   */
  public SELF hasValue(long expected) {
    return executeAssertion(() -> {
      isNotNull();
      long actualValue = actual.sum();
      if (!objects.getComparisonStrategy().areEqual(actualValue, expected)) {
        throwAssertionError(shouldHaveValue(actual, expected));
      }
    });
  }

  /**
   * Verifies that the actual sum has not the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * LongAdder actual = new LongAdder();
   * actual.add(42);
   * assertThat(actual).doesNotHaveValue(0);
   *
   * // assertion will fail
   * assertThat(actual).doesNotHaveValue(42);</code></pre>
   *
   * @param unexpected the unexpected value.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual adder is {@code null}.
   * @throws AssertionError if the actual sum is not the given value.
   */
  public SELF doesNotHaveValue(long unexpected) {
    return executeAssertion(() -> {
      isNotNull();
      long actualValue = actual.sum();
      if (objects.getComparisonStrategy().areEqual(actualValue, unexpected)) {
        throwAssertionError(shouldNotContainValue(actual, unexpected));
      }
    });
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
    return executeAssertion(() -> longs.assertIsZero(info, actual.longValue()));
  }

  @Override
  public SELF isNotZero() {
    return executeAssertion(() -> longs.assertIsNotZero(info, actual.longValue()));
  }

  @Override
  public SELF isOne() {
    return executeAssertion(() -> longs.assertIsOne(info, actual.longValue()));
  }

  @Override
  public SELF isPositive() {
    return executeAssertion(() -> longs.assertIsPositive(info, actual.longValue()));
  }

  @Override
  public SELF isNegative() {
    return executeAssertion(() -> longs.assertIsNegative(info, actual.longValue()));
  }

  @Override
  public SELF isNotNegative() {
    return executeAssertion(() -> longs.assertIsNotNegative(info, actual.longValue()));
  }

  @Override
  public SELF isNotPositive() {
    return executeAssertion(() -> longs.assertIsNotPositive(info, actual.longValue()));
  }

  @Override
  public SELF isEqualByComparingTo(Long other) {
    return executeAssertion(() -> longs.assertEqualByComparison(info, actual.longValue(), other));
  }

  @Override
  public SELF isNotEqualByComparingTo(Long other) {
    return executeAssertion(() -> longs.assertNotEqualByComparison(info, actual.longValue(), other));
  }

  @Override
  public SELF isLessThan(Long other) {
    return executeAssertion(() -> longs.assertLessThan(info, actual.longValue(), other));
  }

  @Override
  public SELF isLessThanOrEqualTo(Long other) {
    return executeAssertion(() -> longs.assertLessThanOrEqualTo(info, actual.longValue(), other));
  }

  @Override
  public SELF isGreaterThan(Long other) {
    return executeAssertion(() -> longs.assertGreaterThan(info, actual.longValue(), other));
  }

  @Override
  public SELF isGreaterThanOrEqualTo(Long other) {
    return executeAssertion(() -> longs.assertGreaterThanOrEqualTo(info, actual.longValue(), other));
  }

  @Override
  public SELF isBetween(Long start, Long end) {
    return executeAssertion(() -> longs.assertIsBetween(info, actual.longValue(), start, end));
  }

  @Override
  public SELF isStrictlyBetween(Long start, Long end) {
    return executeAssertion(() -> longs.assertIsStrictlyBetween(info, actual.longValue(), start, end));
  }

  @Override
  public SELF isCloseTo(Long expected, Offset<Long> offset) {
    return executeAssertion(() -> longs.assertIsCloseTo(info, actual.longValue(), expected, offset));
  }

  @Override
  public SELF isNotCloseTo(Long expected, Offset<Long> offset) {
    return executeAssertion(() -> longs.assertIsNotCloseTo(info, actual.longValue(), expected, offset));
  }

  @Override
  public SELF isCloseTo(Long expected, Percentage percentage) {
    return executeAssertion(() -> longs.assertIsCloseToPercentage(info, actual.longValue(), expected, percentage));
  }

  @Override
  public SELF isNotCloseTo(Long expected, Percentage percentage) {
    return executeAssertion(() -> longs.assertIsNotCloseToPercentage(info, actual.longValue(), expected, percentage));
  }

}
