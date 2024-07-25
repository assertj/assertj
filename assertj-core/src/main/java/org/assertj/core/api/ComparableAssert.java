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

import java.math.BigDecimal;

/**
 * Assertion methods applicable to <code>{@link Comparable}</code>s.
 *
 * @author Alex Ruiz
 * @author Ted M. Young
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ACTUAL> the type of the "actual" value.
 */
public interface ComparableAssert<SELF extends ComparableAssert<SELF, ACTUAL>, ACTUAL extends Comparable<? super ACTUAL>> {

  /**
   * Verifies that the actual value is equal to the given one by invoking
   * <code>{@link Comparable#compareTo(Object)}</code>.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(1.0).isEqualByComparingTo(1.0);
   * // assertion will pass because 8.0 is equal to 8.00 using {@link BigDecimal#compareTo(BigDecimal)}
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isEqualByComparingTo(new BigDecimal(&quot;8.00&quot;));
   *
   * // assertion will fail
   * assertThat(new BigDecimal(1.0)).isEqualByComparingTo(new BigDecimal(2.0));</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal when comparing to the given one.
   */
  SELF isEqualByComparingTo(ACTUAL other);

  /**
   * Verifies that the actual value is not equal to the given one by invoking
   * <code>{@link Comparable#compareTo(Object)}</code>.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new BigDecimal(1.0)).isNotEqualByComparingTo(new BigDecimal(2.0));
   *
   * // assertion will fail
   * assertThat(1.0).isNotEqualByComparingTo(1.0);
   * // assertion will fail because 8.0 is equal to 8.00 using {@link BigDecimal#compareTo(BigDecimal)}
   * assertThat(new BigDecimal(&quot;8.0&quot;)).isNotEqualByComparingTo(new BigDecimal(&quot;8.00&quot;));</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal when comparing to the given one.
   */
  SELF isNotEqualByComparingTo(ACTUAL other);

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat('a').isLessThan('b');
   * assertThat(BigInteger.ZERO).isLessThan(BigInteger.ONE);
   * 
   * // assertions will fail
   * assertThat('a').isLessThan('a');
   * assertThat(BigInteger.ONE).isLessThan(BigInteger.ZERO);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  SELF isLessThan(ACTUAL other);

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat('a').isLessThanOrEqualTo('b');
   * assertThat('a').isLessThanOrEqualTo('a');
   * assertThat(BigInteger.ZERO).isLessThanOrEqualTo(BigInteger.ZERO);
   * 
   * // assertions will fail
   * assertThat('b').isLessThanOrEqualTo('a');
   * assertThat(BigInteger.ONE).isLessThanOrEqualTo(BigInteger.ZERO);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  SELF isLessThanOrEqualTo(ACTUAL other);

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat('b').isGreaterThan('a');
   * assertThat(BigInteger.ONE).isGreaterThan(BigInteger.ZERO);
   * 
   * // assertions will fail
   * assertThat('b').isGreaterThan('a');
   * assertThat(BigInteger.ZERO).isGreaterThan(BigInteger.ZERO);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  SELF isGreaterThan(ACTUAL other);

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat('b').isGreaterThanOrEqualTo('a');
   * assertThat(BigInteger.ONE).isGreaterThanOrEqualTo(BigInteger.ONE);
   * 
   * // assertions will fail
   * assertThat('a').isGreaterThanOrEqualTo('b');
   * assertThat(BigInteger.ZERO).isGreaterThanOrEqualTo(BigInteger.ONE);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  SELF isGreaterThanOrEqualTo(ACTUAL other);

  /**
   * Verifies that the actual value is in [start, end] range (start included, end included).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat('b').isBetween('a', 'c');
   * assertThat('a').isBetween('a', 'b');
   * assertThat('b').isBetween('a', 'b');
   * 
   * // assertions fail
   * assertThat('a').isBetween('b', 'c');
   * assertThat('c').isBetween('a', 'b');</code></pre>
   * 
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in [start, end] range.
   * 
   * @since 2.5.0 / 3.5.0
   */
  SELF isBetween(ACTUAL startInclusive, ACTUAL endInclusive);

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat('b').isStrictlyBetween('a', 'c');
   * 
   * // assertions fail
   * assertThat('d').isStrictlyBetween('a', 'c');
   * assertThat('a').isStrictlyBetween('b', 'd');
   * assertThat('a').isStrictlyBetween('a', 'b');
   * assertThat('b').isStrictlyBetween('a', 'b');</code></pre>
   * 
   * @param startExclusive the start value (exclusive), expected not to be null.
   * @param endExclusive the end value (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ range.
   * 
   * @since 2.5.0 / 3.5.0
   */
  SELF isStrictlyBetween(ACTUAL startExclusive, ACTUAL endExclusive);
}
