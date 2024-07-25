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

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Comparator;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link BigDecimal}s.
 *
 * @author Drummond Dawson
 * @author David DIDIER
 * @author Ted M. Young
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 */
public abstract class AbstractBigDecimalAssert<SELF extends AbstractBigDecimalAssert<SELF>> extends
    AbstractComparableAssert<SELF, BigDecimal> implements NumberAssert<SELF, BigDecimal> {

  @VisibleForTesting
  BigDecimals bigDecimals = BigDecimals.instance();

  protected AbstractBigDecimalAssert(BigDecimal actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(BigDecimal.ZERO).isZero();
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.00&quot;)).isZero();</code></pre>
   *
   */
  @Override
  public SELF isZero() {
    bigDecimals.assertIsZero(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigDecimal(&quot;8.00&quot;)).isNotZero();
   *
   * // assertion will fail
   * assertThat(BigDecimal.ZERO).isNotZero();</code></pre>
   *
   */
  @Override
  public SELF isNotZero() {
    bigDecimals.assertIsNotZero(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(BigDecimal.ONE).isOne();
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.00&quot;)).isOne();</code></pre>
   *
   */
  @Override
  public SELF isOne() {
    bigDecimals.assertIsOne(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isPositive();
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;-8.0&quot;)).isPositive();</code></pre>
   *
   */
  @Override
  public SELF isPositive() {
    bigDecimals.assertIsPositive(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigDecimal(&quot;-8.0&quot;)).isNegative();
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isNegative();</code></pre>
   *
   */
  @Override
  public SELF isNegative() {
    bigDecimals.assertIsNegative(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigDecimal(&quot;-8.0&quot;)).isNotPositive();
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isNotPositive();</code></pre>
   *
   */
  @Override
  public SELF isNotPositive() {
    bigDecimals.assertIsNotPositive(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isNotNegative();
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;-8.0&quot;)).isNotNegative();</code></pre>
   *
   */
  @Override
  public SELF isNotNegative() {
    bigDecimals.assertIsNotNegative(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual value is in [start, end] range (start and end included).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isBetween(new BigDecimal(&quot;7.0&quot;), new BigDecimal(&quot;9.0&quot;));
   * assertThat(new BigDecimal(&quot;8.00&quot;)).isBetween(new BigDecimal(&quot;7.0&quot;), new BigDecimal(&quot;9.0&quot;));
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isBetween(new BigDecimal(&quot;8.0&quot;), new BigDecimal(&quot;9.0&quot;));
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isBetween(new BigDecimal(&quot;7.0&quot;), new BigDecimal(&quot;8.0&quot;));
   * // comparison is performed without scale consideration:
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isBetween(new BigDecimal(&quot;8.0&quot;), new BigDecimal(&quot;8.00&quot;));
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isBetween(new BigDecimal(&quot;6.0&quot;), new BigDecimal(&quot;7.0&quot;));</code></pre>
   *
   * Note that comparison of {@link BigDecimal} is done by value without scale consideration, i.e 2.0 and 2.00 are
   * considered equal in value unlike {@link BigDecimal#equals(Object)}.
   */
  @Override
  public SELF isBetween(BigDecimal start, BigDecimal end) {
    bigDecimals.assertIsBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   *
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isStrictlyBetween(new BigDecimal(&quot;7.0&quot;), new BigDecimal(&quot;9.0&quot;));
   *
   * // assertions will fail
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isStrictlyBetween(new BigDecimal(&quot;8.0&quot;), new BigDecimal(&quot;9.0&quot;));
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isStrictlyBetween(new BigDecimal(&quot;7.0&quot;), new BigDecimal(&quot;8.0&quot;));</code></pre>
   *
   */
  @Override
  public SELF isStrictlyBetween(BigDecimal start, BigDecimal end) {
    bigDecimals.assertIsStrictlyBetween(info, actual, start, end);
    return myself;
  }

  /**
   * Same as {@link AbstractAssert#isEqualTo(Object) isEqualTo(BigDecimal)} but takes care of converting given String to
   * {@link BigDecimal} for you.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isEqualTo(&quot;8.0&quot;);
   *
   * // assertion will fail because 8.00 is not equals to 8.0
   * assertThat(new BigDecimal(&quot;8.00&quot;)).isEqualTo(&quot;8.0&quot;);</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @return {@code this} assertion object.
   */
  public SELF isEqualTo(String expected) {
    return isEqualTo(new BigDecimal(expected));
  }

  /**
   * Same as {@link AbstractComparableAssert#isEqualByComparingTo(Comparable) isEqualByComparingTo(BigDecimal)} but
   * takes care of converting given String to {@link BigDecimal}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isEqualByComparingTo(&quot;8.0&quot;);
   * // assertion will pass because 8.0 is equals to 8.00 using {@link BigDecimal#compareTo(Object)}
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isEqualByComparingTo(&quot;8.00&quot;);
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isEqualByComparingTo(&quot;2.0&quot;);</code></pre>
   * @param expected the expected {@link BigDecimal} passed as a String
   * @return {@code this} assertion object.
   */
  public SELF isEqualByComparingTo(String expected) {
    return isEqualByComparingTo(new BigDecimal(expected));
  }

  /**
   * Same as {@link AbstractComparableAssert#isNotEqualByComparingTo(Comparable) isNotEqualByComparingTo(BigDecimal)} but
   * takes care of converting given String to {@link BigDecimal}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isNotEqualByComparingTo(&quot;7.99&quot;);
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isNotEqualByComparingTo(&quot;8.00&quot;);</code></pre>
   * @param notExpected the {@link BigDecimal} value passed as a String not to expect.
   * @return {@code this} assertion object.
   */
  public SELF isNotEqualByComparingTo(String notExpected) {
    return isNotEqualByComparingTo(new BigDecimal(notExpected));
  }

  /**
   * Verifies the BigDecimal under test has the given scale.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new BigDecimal(&quot;8.00&quot;)).hasScaleOf(2);
   * assertThat(new BigDecimal(&quot;8.00&quot;).setScale(4)).hasScaleOf(4);
   *
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.00&quot;)).hasScaleOf(3);
   * assertThat(new BigDecimal(&quot;8.00&quot;).setScale(4)).hasScaleOf(2);</code></pre>
   * 
   * @param expectedScale the expected scale value.
   * @return {@code this} assertion object.
   */
  public SELF hasScaleOf(int expectedScale) {
    bigDecimals.assertHasScale(info, actual, expectedScale);
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super BigDecimal> customComparator) {
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super BigDecimal> customComparator, String customComparatorDescription) {
    this.bigDecimals = new BigDecimals(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    this.bigDecimals = BigDecimals.instance();
    return super.usingDefaultComparator();
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(BigDecimal)} or {@link Offset#offset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(BigDecimal)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(BigDecimal)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(BigDecimal)} to get the old behavior.
   * <p>
   * Examples:
   * <pre><code class='java'> final BigDecimal eightDotOne = new BigDecimal("8.1");
   * final BigDecimal eight =  new BigDecimal("8.0");
   *
   * // assertions succeed
   * assertThat(eightDotOne).isCloseTo(eight, within(new BigDecimal("0.2")));
   * assertThat(eightDotOne).isCloseTo(eight, byLessThan(new BigDecimal("0.2"))); // strict
   *
   * // assertions succeed when the difference == offset value ...
   * assertThat(eightDotOne).isCloseTo(eight, within(new BigDecimal("0.1")));
   * // ... except when using byLessThan which implies a strict comparison
   * assertThat(eightDotOne).isCloseTo(eight, byLessThan(new BigDecimal("0.1"))); // strict =&gt; fail
   *
   * // this assertion also fails
   * assertThat(eightDotOne).isCloseTo(eight, within(new BigDecimal("0.001")));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(final BigDecimal expected, final Offset<BigDecimal> offset) {
    bigDecimals.assertIsCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#byLessThan(BigDecimal)} or {@link Offset#strictOffset(Number)}</li>
   * <li><b>fails</b> when using {@link Assertions#within(BigDecimal)} or {@link Offset#offset(Number)}</li>
   * </ul>
   * <p>
   * <b>Breaking change</b> since 2.9.0/3.9.0: using {@link Assertions#byLessThan(BigDecimal)} implies a <b>strict</b> comparison,
   * use {@link Assertions#within(BigDecimal)} to get the old behavior.
   * <p>
   * Example:
   * <pre><code class='java'> final BigDecimal eightDotOne = new BigDecimal("8.1");
   * final BigDecimal eight =  new BigDecimal("8.0");
   *
   * // assertions succeed
   * assertThat(eightDotOne).isNotCloseTo(eight, byLessThan(new BigDecimal("0.01")));
   * assertThat(eightDotOne).isNotCloseTo(eight, within(new BigDecimal("0.01")));
   * // diff == offset but isNotCloseTo succeeds as we use byLessThan
   * assertThat(eightDotOne).isNotCloseTo(eight, byLessThan(new BigDecimal("0.1")));
   *
   * // assertions fail
   * assertThat(eightDotOne).isNotCloseTo(eight, within(new BigDecimal("0.1")));
   * assertThat(eightDotOne).isNotCloseTo(eight, within(new BigDecimal("0.2")));
   * assertThat(eightDotOne).isNotCloseTo(eight, byLessThan(new BigDecimal("0.2")));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   * @see Assertions#byLessThan(BigDecimal)
   * @since 2.6.0 / 3.6.0
   */
  @Override
  public SELF isNotCloseTo(final BigDecimal expected, final Offset<BigDecimal> offset) {
    bigDecimals.assertIsNotCloseTo(info, actual, expected, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with BigDecimal:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(BigDecimal.valueOf(11.0)).isCloseTo(BigDecimal.TEN, withinPercentage(BigDecimal.valueOf(20d)));
   *
   * // if difference is exactly equals to the computed offset (1.0), it's ok
   * assertThat(BigDecimal.valueOf(11.0)).isCloseTo(BigDecimal.TEN, withinPercentage(BigDecimal.valueOf(10d)));
   *
   * // assertion will fail
   * assertThat(BigDecimal.valueOf(11.0)).isCloseTo(BigDecimal.TEN, withinPercentage(BigDecimal.valueOf(5d)));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  public SELF isCloseTo(BigDecimal expected, Percentage percentage) {
    bigDecimals.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual number is not close to the given one by the given percentage.<br>
   * If difference is equal to the percentage value, the assertion fails.
   * <p>
   * Example with BigDecimal:
   * <pre><code class='java'> BigDecimal eleven = BigDecimal.valueOf(11.0);
   *
   * // assertion will pass:
   * assertThat(eleven).isNotCloseTo(BigDecimal.TEN, withinPercentage(new BigDecimal("5")));
   *
   * // assertion will fail as the difference is exactly equals to the computed offset (1.0)
   * assertThat(eleven).isNotCloseTo(BigDecimal.TEN, withinPercentage(new BigDecimal("10")));
   *
   * // assertion will fail
   * assertThat(eleven).isNotCloseTo(BigDecimal.TEN, withinPercentage(new BigDecimal("20")));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is close to the given one.
   */
  @Override
  public SELF isNotCloseTo(BigDecimal expected, Percentage percentage) {
    bigDecimals.assertIsNotCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(BigDecimal.ONE).isLessThanOrEqualTo(BigDecimal.TEN);
   * assertThat(BigDecimal.ONE).isLessThanOrEqualTo(BigDecimal.ONE);
   * // comparison is performed without scale consideration:
   * assertThat(BigDecimal.ONE).isLessThanOrEqualTo(new BigDecimal("1.00"));
   *
   * // assertions will fail
   * assertThat(BigDecimal.ONE).isLessThanOrEqualTo(BigDecimal.ZERO);</code></pre>
   *
   * Note that comparison of {@link BigDecimal} is done by value without scale consideration, i.e 2.0 and 2.00 are
   * considered equal in value unlike {@link BigDecimal#equals(Object)}.
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  @Override
  public SELF isLessThanOrEqualTo(BigDecimal other) {
    return super.isLessThanOrEqualTo(other);
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(BigDecimal.ONE).isGreaterThanOrEqualTo(BigDecimal.ZERO);
   * assertThat(BigDecimal.ONE).isGreaterThanOrEqualTo(BigDecimal.ONE);
   * // comparison is performed without scale consideration:
   * assertThat(BigDecimal.ONE).isGreaterThanOrEqualTo(new BigDecimal("1.00"));
   *
   * // assertions will fail
   * assertThat(BigDecimal.ZERO).isGreaterThanOrEqualTo(BigDecimal.ONE);</code></pre>
   *
   * Note that comparison of {@link BigDecimal} is done by value without scale consideration, i.e 2.0 and 2.00 are
   * considered equal in value unlike {@link BigDecimal#equals(Object)}.
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  @Override
  public SELF isGreaterThanOrEqualTo(BigDecimal other) {
    return super.isGreaterThanOrEqualTo(other);
  }

  /**
   * Returns an {@code Assert} object that allows performing assertions on the scale of the {@link BigDecimal} under test.
   * <p>
   * Once this method is called, the object under test is no longer the {@link BigDecimal} but its scale.
   * To perform assertions on the {@link BigDecimal}, call {@link AbstractBigDecimalScaleAssert#returnToBigDecimal()}.
   * <p>
   * Example:
   * <pre><code class='java'> BigDecimal bgDecimal = new BigDecimal(&quot;9.3231&quot;);
   *
   * // assertions succeed
   * assertThat(bgDecimal).scale().isGreaterThan(1)
   *                              .isLessThan(5)
   *                      .returnToBigDecimal().isPositive();
   * assertThat(bgDecimal.setScale(5)).scale().isLessThan(6);
   *
   * // assertions fails
   * assertThat(bgDecimal).scale().isBetween(5, 8);
   * assertThat(bgDecimal.setScale(5)).scale().isLessThan(5);</code></pre>
   *
   * @return AbstractBigDecimalScaleAssert built with the {@code BigDecimal}'s scale.
   * @throws NullPointerException if the given {@code BigDecimal} is {@code null}.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public AbstractBigDecimalScaleAssert<SELF> scale() {
    requireNonNull(actual, "Can not perform assertions on the scale of a null BigDecimal");
    return new BigDecimalScaleAssert(myself);
  }
}
