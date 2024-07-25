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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Shorts;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Short}s.
 *
 * @author Drummond Dawson
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 * @author Cal027
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 */
public abstract class AbstractShortAssert<SELF extends AbstractShortAssert<SELF>> extends AbstractComparableAssert<SELF, Short>
    implements NumberAssert<SELF, Short> {

  @VisibleForTesting
  Shorts shorts = Shorts.instance();

  protected AbstractShortAssert(Short actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isEqualTo((short) 1);
   *
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;-1&quot;)).isEqualTo((short) 1);</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SELF isEqualTo(short expected) {
    shorts.assertEqual(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf((&quot;-1&quot;)).isNotEqualTo((short) 1);
   *
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;1&quot;)).isNotEqualTo((short) 1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public SELF isNotEqualTo(short other) {
    shorts.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isZero() {
    shorts.assertIsZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotZero() {
    shorts.assertIsNotZero(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isOne() {
    shorts.assertIsOne(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isPositive() {
    shorts.assertIsPositive(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNegative() {
    shorts.assertIsNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotNegative() {
    shorts.assertIsNotNegative(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotPositive() {
    shorts.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is even.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat((short) 12).isEven();
   * assertThat((short) -46).isEven();
   *
   * // assertions will fail
   * assertThat((short) 3).isEven();
   * assertThat((short) 15).isEven();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive.
   * @since 3.17.0
   */
  public SELF isEven() {
    shorts.assertIsEven(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is odd.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat((short) 3).isOdd();
   * assertThat((short) -17).isOdd();
   *
   * // assertions will fail
   * assertThat((short) 2).isOdd();
   * assertThat((short) -24).isOdd();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive.
   * @since 3.17.0
   */
  public SELF isOdd() {
    shorts.assertIsOdd(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThan((short) 2);
   *
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThan((short) 0);
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThan((short) 1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public SELF isLessThan(short other) {
    shorts.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThanOrEqualTo((short) 1);
   *
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;1&quot;)).isLessThanOrEqualTo((short) 0);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public SELF isLessThanOrEqualTo(short other) {
    shorts.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isGreaterThan((short) 0);
   *
   * // assertions will fail:
   * assertThat(Short.valueOf(&quot;0&quot;)).isGreaterThan((short) 1);
   * assertThat(Short.valueOf(&quot;0&quot;)).isGreaterThan((short) 0);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public SELF isGreaterThan(short other) {
    shorts.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass:
   * assertThat(Short.valueOf(&quot;1&quot;)).isGreaterThanOrEqualTo((short) 1);
   *
   * // assertion will fail:
   * assertThat(Short.valueOf(&quot;0&quot;)).isGreaterThanOrEqualTo((short) 1);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public SELF isGreaterThanOrEqualTo(short other) {
    shorts.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is in [start, end] range (start included, end included).
   *
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat((short) 1).isBetween((short) -1, (short) 2);
   * assertThat((short) 1).isBetween((short) 1, (short) 2);
   * assertThat((short) 1).isBetween((short) 0, (short) 1);
   *
   * // assertion will fail
   * assertThat((short) 1).isBetween((short) 2, (short) 3);</code></pre>
   */
  @Override
  public SELF isBetween(Short start, Short end) {
    shorts.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   *
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat((short) 1).isStrictlyBetween((short) -1, (short) 2);
   *
   * // assertions will fail
   * assertThat((short) 1).isStrictlyBetween((short) 1, (short) 2);
   * assertThat((short) 1).isStrictlyBetween((short) 0, (short) 1);
   * assertThat((short) 1).isStrictlyBetween((short) 2, (short) 3);</code></pre>
   */
  @Override
  public SELF isStrictlyBetween(Short start, Short end) {
    shorts.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Short)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Short)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Short)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(Short)} to get the old behavior.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short) 5).isCloseTo((short) 7, within((short) 3));
   * assertThat((short) 5).isCloseTo((short) 7, byLessThan((short) 3));
   *
   * // if difference is exactly equals to the offset, it's ok ...
   * assertThat((short) 5).isCloseTo((short) 7, within((short) 2));
   * // ... but not with byLessThan which implies a strict comparison
   * assertThat((short) 5).isCloseTo((short) 7, byLessThan((short) 2)); // FAIL
   *
   * // assertions will fail
   * assertThat((short) 5).isCloseTo((short) 7, within((short) 1));
   * assertThat((short) 5).isCloseTo((short) 7, byLessThan((short) 1));
   * assertThat((short) 5).isCloseTo((short) 7, byLessThan((short) 2));</code></pre>
   *
   * @param expected the given int to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not close enough to the given one.
   */
  public SELF isCloseTo(short expected, Offset<Short> offset) {
    shorts.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#byLessThan(Short)} or {@link Offset#strictOffset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#within(Short)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Short)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(Short)} to get the old behavior.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short) 5).isNotCloseTo((short) 7, byLessThan((short) 1));
   * assertThat((short) 5).isNotCloseTo((short) 7, within((short) 1));
   * // diff == offset but isNotCloseTo succeeds as we use byLessThan
   * assertThat((short) 5).isNotCloseTo((short) 7, byLessThan((short) 2));
   *
   * // assertions will fail
   * assertThat((short) 5).isNotCloseTo((short) 7, within((short) 2));
   * assertThat((short) 5).isNotCloseTo((short) 7, byLessThan((short) 3));</code></pre>
   *
   * @param expected the given int to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Short)
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotCloseTo(short expected, Offset<Short> offset) {
    shorts.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Short)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Short)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Short)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(Short)} to get the old behavior.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short) 5).isCloseTo((short) 7, within((short) 3));
   * assertThat((short) 5).isCloseTo((short) 7, byLessThan((short) 3));
   *
   * // if difference is exactly equals to the offset, it's ok ...
   * assertThat((short) 5).isCloseTo((short) 7, within((short) 2));
   * // ... but not with byLessThan which implies a strict comparison
   * assertThat((short) 5).isCloseTo((short) 7, byLessThan((short) 2)); // FAIL
   *
   * // assertions will fail
   * assertThat((short) 5).isCloseTo((short) 7, within((short) 1));
   * assertThat((short) 5).isCloseTo((short) 7, byLessThan((short) 1));
   * assertThat((short) 5).isCloseTo((short) 7, byLessThan((short) 2));</code></pre>
   *
   * @param expected the given int to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is not close enough to the given one.
   */
  @Override
  public SELF isCloseTo(Short expected, Offset<Short> offset) {
    shorts.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#byLessThan(Short)} or {@link Offset#strictOffset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#within(Short)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(Short)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(Short)} to get the old behavior.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short) 5).isNotCloseTo((short) 7, byLessThan((short) 1));
   * assertThat((short) 5).isNotCloseTo((short) 7, within((short) 1));
   * // diff == offset but isNotCloseTo succeeds as we use byLessThan
   * assertThat((short) 5).isNotCloseTo((short) 7, byLessThan((short) 2));
   *
   * // assertions will fail
   * assertThat((short) 5).isNotCloseTo((short) 7, within((short) 2));
   * assertThat((short) 5).isNotCloseTo((short) 7, byLessThan((short) 3));</code></pre>
   *
   * @param expected the given int to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(Short)
   * @since 2.6.0 / 3.6.0
   */
  @Override
  public SELF isNotCloseTo(Short expected, Offset<Short> offset) {
    shorts.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with short:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short) 11).isCloseTo(Short.valueOf(10), withinPercentage((short) 20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat((short) 11).isCloseTo(Short.valueOf(10), withinPercentage((short) 10));
   *
   * // assertion will fail
   * assertThat((short) 11).isCloseTo(Short.valueOf(10), withinPercentage((short) 5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(Short expected, Percentage percentage) {
    shorts.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with short:
   * <pre><code class='java'> // assertion will pass:
   * assertThat((short) 11).isNotCloseTo(Short.valueOf(10), withinPercentage((short) 5));
   *
   * // assertions will fail
   * assertThat((short) 11).isNotCloseTo(Short.valueOf(10), withinPercentage((short) 10));
   * assertThat((short) 11).isNotCloseTo(Short.valueOf(10), withinPercentage((short) 20));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @since 2.6.0 / 3.6.0
   */
  @Override
  public SELF isNotCloseTo(Short expected, Percentage percentage) {
    shorts.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with short:
   * <pre><code class='java'> // assertions will pass:
   * assertThat((short) 11).isCloseTo((short) 10, withinPercentage((short) 20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat((short) 11).isCloseTo((short) 10, withinPercentage((short) 10));
   *
   * // assertion will fail
   * assertThat((short) 11).isCloseTo((short) 10, withinPercentage((short) 5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  public SELF isCloseTo(short expected, Percentage percentage) {
    shorts.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with short:
   * <pre><code class='java'> // assertion will pass:
   * assertThat((short) 11).isNotCloseTo((short) 10, withinPercentage((short) 5));
   *
   * // assertions will fail
   * assertThat((short) 11).isNotCloseTo((short) 10, withinPercentage((short) 10));
   * assertThat((short) 11).isNotCloseTo((short) 10, withinPercentage((short) 20));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @since 2.6.0 / 3.6.0
   */
  public SELF isNotCloseTo(short expected, Percentage percentage) {
    shorts.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Short> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Short> customComparator, String customComparatorDescription) {
    shorts = new Shorts(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    shorts = Shorts.instance();
    return super.usingDefaultComparator();
  }
}
