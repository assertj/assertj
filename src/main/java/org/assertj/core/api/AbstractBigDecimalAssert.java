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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import java.math.BigDecimal;
import java.util.Comparator;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link BigDecimal}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * 
 * @author David DIDIER
 * @author Ted M. Young
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public abstract class AbstractBigDecimalAssert<S extends AbstractBigDecimalAssert<S>> extends
    AbstractComparableAssert<S, BigDecimal> implements NumberAssert<S, BigDecimal> {

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
   * </p>
   */
  @Override
  public S isZero() {
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
   * </p>
   */
  @Override
  public S isNotZero() {
    bigDecimals.assertIsNotZero(info, actual);
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
   * </p>
   */
  @Override
  public S isPositive() {
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
   * </p>
   */
  @Override
  public S isNegative() {
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
   * </p>
   */
  @Override
  public S isNotPositive() {
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
   * </p>
   */
  @Override
  public S isNotNegative() {
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
   * 
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isBetween(new BigDecimal(&quot;6.0&quot;), new BigDecimal(&quot;7.0&quot;));</code></pre>
   * 
   * Note that comparison of {@link BigDecimal} is done by value without scale consideration, i.e 2.0 and 2.00 are
   * considered equal in value (not like {@link BigDecimal#equals(Object)}.
   * </p>
   */
  @Override
  public S isBetween(BigDecimal start, BigDecimal end) {
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
   * </p>
   */
  @Override
  public S isStrictlyBetween(BigDecimal start, BigDecimal end) {
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
   * </p>
   */
  public S isEqualTo(String expected) {
    return isEqualTo(new BigDecimal(expected));
  }

  /**
   * Same as {@link AbstractComparableAssert#isEqualByComparingTo(Comparable) isEqualByComparingTo(BigDecimal)} but
   * takes care of converting given String to {@link BigDecimal} for you.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isEqualByComparingTo(&quot;8.0&quot;);
   * // assertion will pass because 8.0 is equals to 8.00 using {@link BigDecimal#compareTo(Object)}
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isEqualByComparingTo(&quot;8.00&quot;);
   * 
   * // assertion will fail
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isEqualByComparingTo(&quot;2.0&quot;);</code></pre>
   */
  public S isEqualByComparingTo(String expected) {
    return isEqualByComparingTo(new BigDecimal(expected));
  }

  @Override
  public S usingComparator(Comparator<? super BigDecimal> customComparator) {
    super.usingComparator(customComparator);
    this.bigDecimals = new BigDecimals(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public S usingDefaultComparator() {
    super.usingDefaultComparator();
    this.bigDecimals = BigDecimals.instance();
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example:
   * <pre><code class='java'> final BigDecimal actual = new BigDecimal("8.1");
   * final BigDecimal other =  new BigDecimal("8.0");
   *
   * // valid assertion
   * assertThat(actual).isCloseTo(other, within(new BigDecimal("0.2")));
   *
   * // if difference is exactly equals to given offset value, it's ok
   * assertThat(actual).isCloseTo(other, within(new BigDecimal("0.1")));
   *
   * // BidDecimal format has no impact on the assertion, this assertion is valid:
   * assertThat(actual).isCloseTo(new BigDecimal("8.00"), within(new BigDecimal("0.100")));
   *
   * // but if difference is greater than given offset value assertion will fail :
   * assertThat(actual).isCloseTo(other, within(new BigDecimal("0.01")));</code></pre>
   */
  @Override
  public S isCloseTo(final BigDecimal other, final Offset<BigDecimal> offset) {
    bigDecimals.assertIsCloseTo(info, actual, other, offset);
    return myself;
  }

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with BigDecimal:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(BigDecimal.valueOf(11.0)).isCloseTo(BigDecimal.valueOf(10.0), withinPercentage(BigDecimal.valueOf(20d)));
   *
   * // if difference is exactly equals to the computed offset (1.0), it's ok
   * assertThat(BigDecimal.valueOf(11.0)).isCloseTo(BigDecimal.valueOf(10.0), withinPercentage(BigDecimal.valueOf(10d)));
   *
   * // assertion will fail
   * assertThat(BigDecimal.valueOf(11.0)).isCloseTo(BigDecimal.valueOf(10.0), withinPercentage(BigDecimal.valueOf(5d)));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage between 0 and 100.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  @Override
  public S isCloseTo(BigDecimal expected, Percentage percentage) {
    bigDecimals.assertIsCloseToPercentage(info, actual, expected, percentage);
    return myself;
  }
}
