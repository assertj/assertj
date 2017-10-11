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
package org.assertj.core.api;

import java.math.BigInteger;
import java.util.Comparator;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.BigIntegers;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link BigInteger}s.
 *
 * @since 2.7.0 / 3.7.0
 */
public class AbstractBigIntegerAssert<SELF extends AbstractBigIntegerAssert<SELF>> extends
    AbstractComparableAssert<SELF, BigInteger> implements NumberAssert<SELF, BigInteger> {

  @VisibleForTesting
  BigIntegers bigIntegers = BigIntegers.instance();

  public AbstractBigIntegerAssert(BigInteger actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is equal to zero.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(BigInteger.ZERO).isZero();
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;8&quot;)).isZero();</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isZero() {
    bigIntegers.assertIsZero(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to zero. 
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigInteger(&quot;8&quot;)).isNotZero();
   *
   * // assertion will fail
   * assertThat(BigInteger.ZERO).isNotZero();</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isNotZero() {
    bigIntegers.assertIsNotZero(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is equal to one. 
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(BigInteger.ONE).isOne();
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;8&quot;)).isOne();</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isOne() {
    bigIntegers.assertIsOne(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is positive. 
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigInteger(&quot;8&quot;)).isPositive();
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;-8&quot;)).isPositive();</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isPositive() {
    bigIntegers.assertIsPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is negative. 
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigInteger(&quot;-8&quot;)).isNegative();
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;8&quot;)).isNegative();</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isNegative() {
    bigIntegers.assertIsNegative(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is non negative (positive or equal zero). 
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigInteger(&quot;8&quot;)).isNotNegative();
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;-8&quot;)).isNotNegative();</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isNotNegative() {
    bigIntegers.assertIsNotNegative(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is non positive (negative or equal zero). 
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigInteger(&quot;-8&quot;)).isNotPositive();
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;8&quot;)).isNotPositive();</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isNotPositive() {
    bigIntegers.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> import static org.assertj.core.api.Assertions.within;
   *  
   * final BigInteger actual = new BigInteger("8");
   * final BigInteger other =  new BigInteger("10");
   *
   * // valid assertion
   * assertThat(actual).isCloseTo(other, within(new BigInteger("3")));
   *
   * // if difference is exactly equals to given offset value, it's ok
   * assertThat(actual).isCloseTo(other, within(new BigInteger("2")));
   *
   * // but if difference is greater than given offset value assertion will fail :
   * assertThat(actual).isCloseTo(other, within(new BigInteger("1")));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   *
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isCloseTo(BigInteger expected, Offset<BigInteger> offset) {
    bigIntegers.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example:
   * <pre><code class='java'> import static org.assertj.core.api.Assertions.byLessThan;
   *  
   * final BigInteger actual = new BigInteger("8");
   * final BigInteger other =  new BigInteger("10");
   *
   * // this assertion succeeds
   * assertThat(actual).isNotCloseTo(other, byLessThan(new BigInteger("1")));
   *
   * // the assertion fails if the difference is equal to the given offset value
   * assertThat(actual).isNotCloseTo(other, byLessThan(new BigInteger("2")));
   *
   * // the assertion fails if the difference is greater than the given offset value
   * assertThat(actual).isNotCloseTo(other, byLessThan(new BigInteger("3")));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one within the offset value.
   * @see Assertions#byLessThan(BigInteger)
   *
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isNotCloseTo(BigInteger expected, Offset<BigInteger> offset) {
    bigIntegers.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with BigInteger:
   * <pre><code class='java'> import static org.assertj.core.api.Assertions.withinPercentage; 
   * 
   * // assertions will pass:
   * assertThat(new BigInteger("11")).isCloseTo(BigInteger.TEN, withinPercentage(20));
   *
   * // if difference is exactly equals to the computed offset (1), it's ok
   * assertThat(new BigInteger("11")).isCloseTo(BigInteger.TEN, withinPercentage(10));
   *
   * // assertion will fail
   * assertThat(new BigInteger("11")).isCloseTo(BigInteger.TEN, withinPercentage(5));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   *
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isCloseTo(BigInteger expected, Percentage percentage) {
    bigIntegers.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with BigInteger:
   * <pre><code class='java'> import static org.assertj.core.api.Assertions.withinPercentage; 
   * 
   * BigInteger eleven = new BigInteger("11");
   *
   * // assertion will pass:
   * assertThat(eleven).isNotCloseTo(BigInteger.TEN, withinPercentage(5));
   *
   * // assertion will fail as the difference is exactly equals to the computed offset (1)
   * assertThat(eleven).isNotCloseTo(BigInteger.TEN, withinPercentage(10));
   *
   * // assertion will fail
   * assertThat(eleven).isNotCloseTo(BigInteger.TEN, withinPercentage(20));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   *
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isNotCloseTo(BigInteger expected, Percentage percentage) {
    bigIntegers.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual value is in [start, end] range (start and end included).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new BigInteger(&quot;8&quot;)).isBetween(new BigInteger(&quot;7&quot;), new BigInteger(&quot;9&quot;));
   * assertThat(new BigInteger(&quot;8&quot;)).isBetween(new BigInteger(&quot;8&quot;), new BigInteger(&quot;9&quot;));
   * assertThat(new BigInteger(&quot;8&quot;)).isBetween(new BigInteger(&quot;7&quot;), new BigInteger(&quot;8&quot;));
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;8&quot;)).isBetween(new BigInteger(&quot;6&quot;), new BigInteger(&quot;7&quot;));</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isBetween(BigInteger start, BigInteger end) {
    bigIntegers.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   *
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigInteger(&quot;8&quot;)).isStrictlyBetween(new BigInteger(&quot;7&quot;), new BigInteger(&quot;9&quot;));
   *
   * // assertions will fail
   * assertThat(new BigInteger(&quot;8&quot;)).isStrictlyBetween(new BigInteger(&quot;8&quot;), new BigInteger(&quot;9&quot;));
   * assertThat(new BigInteger(&quot;8&quot;)).isStrictlyBetween(new BigInteger(&quot;7&quot;), new BigInteger(&quot;8&quot;));</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF isStrictlyBetween(BigInteger start, BigInteger end) {
    bigIntegers.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Same as {@link AbstractAssert#isEqualTo(Object) isEqualTo(BigInteger)} but takes care of converting given String to
   * {@link BigInteger} for you.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigInteger(&quot;8&quot;)).isEqualTo(&quot;8&quot;);
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;8&quot;)).isEqualTo(&quot;2&quot;);</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public SELF isEqualTo(String expected) {
    return isEqualTo(new BigInteger(expected));
  }

  /**
   * Same as {@link AbstractAssert#isEqualTo(Object) isEqualTo(BigInteger)} but takes care of converting given int to
   * {@link BigInteger} for you.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigInteger(&quot;8&quot;)).isEqualTo(8);
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;8&quot;)).isEqualTo(2);</code></pre>
   *
   * @param expected the expected value
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public SELF isEqualTo(int expected) {
    return isEqualTo(new BigInteger(Integer.toString(expected)));
  }

  /**
   * Same as {@link AbstractAssert#isEqualTo(Object) isEqualTo(BigInteger)} but takes care of converting given int to
   * {@link BigInteger} for you.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigInteger(&quot;8&quot;)).isEqualTo(8L);
   *
   * // assertion will fail
   * assertThat(new BigInteger(&quot;8&quot;)).isEqualTo(2L);</code></pre>
   *
   * @param expected the expected value
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  public SELF isEqualTo(long expected) {
    return isEqualTo(new BigInteger(Long.toString(expected)));
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super BigInteger> customComparator) {
    super.usingComparator(customComparator);
    this.bigIntegers = new BigIntegers(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    super.usingDefaultComparator();
    this.bigIntegers = BigIntegers.instance();
    return myself;
  }

}
