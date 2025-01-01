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
package org.assertj.core.api;

import java.math.BigDecimal;
import java.util.Comparator;

import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for {@link Comparable} assertions.
 * <p>
 * This class offers better compatibility than {@link ComparableAssert} and related implementations, currently limited
 * due to the upper bound of {@link ComparableAssert}'s type parameters.
 * <p> 
 * Let's take an example with a class <code>Name</code> implementing <code>Comparable&lt;Name&gt;</code>.
 * <pre><code class='java'> Comparable&lt;Name&gt; name1 = new Name("abc");</code></pre>
 * <p>
 * The following does not compile or work as expected: 
 * <pre><code class='java'> // does not compile as assertThat(name1) resolves to Object assertions
 * assertThat(name1).isLessThanOrEqualTo(name1);
 * 
 * // compiles fine but requires a raw Comparable cast (assertThat resolves to AbstractComparableAssert)
 * assertThat((Comparable)name1).isLessThanOrEqualTo(name1);
 * 
 * // does not compile: Cannot infer type arguments for GenericComparableAssert&lt;&gt;
 * new GenericComparableAssert&lt;&gt;(name1).isLessThanOrEqualTo(name3);
 * 
 * // compiles fine with the concrete type (assertThat resolves to AbstractComparableAssert)
 * Name name = name1;
 * assertThat(name).isEqualByComparingTo(name);</code></pre>
 * <p>
 * This class aims to allow writing 
 * <pre><code class='java'> // assertThatComparable resolves to AbstractUniversalComparableAssert
 * assertThatComparable(name1).isLessThanOrEqualTo(name1);
 * 
 * // it works with the concrete type too
 * assertThatComparable(name).isEqualByComparingTo(name);</code></pre>
 *
 * @see Assertions#assertThatComparable(Comparable)
 * @since 3.23.0
 */
public abstract class AbstractUniversalComparableAssert<SELF extends AbstractUniversalComparableAssert<SELF, T>, T>
    extends AbstractObjectAssert<SELF, Comparable<T>> {

  @VisibleForTesting
  Comparables comparables = new Comparables();

  protected AbstractUniversalComparableAssert(Comparable<T> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual value is equal to the given one by invoking
   * <code>{@link Comparable#compareTo(Object)}</code>.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThatComparable(1.0).isEqualByComparingTo(1.0);
   * // assertion will pass because 8.0 is equal to 8.00 using {@link BigDecimal#compareTo(BigDecimal)}
   * assertThatComparable(new BigDecimal(&quot;8.0&quot;)).isEqualByComparingTo(new BigDecimal(&quot;8.00&quot;));
   *
   * // assertion will fail
   * assertThatComparable(new BigDecimal(1.0)).isEqualByComparingTo(new BigDecimal(2.0));</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal when comparing to the given one.
   */
  public SELF isEqualByComparingTo(T other) {
    comparables.assertEqualByComparison(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is not equal to the given one by invoking
   * <code>{@link Comparable#compareTo(Object)}</code>.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThatComparable(new BigDecimal(1.0)).isNotEqualByComparingTo(new BigDecimal(2.0));
   *
   * // assertion will fail
   * assertThatComparable(1.0).isNotEqualByComparingTo(1.0);
   * // assertion will fail because 8.0 is equal to 8.00 using {@link BigDecimal#compareTo(BigDecimal)}
   * assertThatComparable(new BigDecimal(&quot;8.0&quot;)).isNotEqualByComparingTo(new BigDecimal(&quot;8.00&quot;));</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal when comparing to the given one.
   */
  public SELF isNotEqualByComparingTo(T other) {
    comparables.assertNotEqualByComparison(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThatComparable('a').isLessThan('b');
   * assertThatComparable(BigInteger.ZERO).isLessThan(BigInteger.ONE);
   *
   * // assertions will fail
   * assertThatComparable('a').isLessThan('a');
   * assertThatComparable(BigInteger.ONE).isLessThan(BigInteger.ZERO);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public SELF isLessThan(T other) {
    comparables.assertLessThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThatComparable('a').isLessThanOrEqualTo('b');
   * assertThatComparable('a').isLessThanOrEqualTo('a');
   * assertThatComparable(BigInteger.ZERO).isLessThanOrEqualTo(BigInteger.ZERO);
   *
   * // assertions will fail
   * assertThatComparable('b').isLessThanOrEqualTo('a');
   * assertThatComparable(BigInteger.ONE).isLessThanOrEqualTo(BigInteger.ZERO);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public SELF isLessThanOrEqualTo(T other) {
    comparables.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThatComparable('b').isGreaterThan('a');
   * assertThatComparable(BigInteger.ONE).isGreaterThan(BigInteger.ZERO);
   *
   * // assertions will fail
   * assertThatComparable('b').isGreaterThan('a');
   * assertThatComparable(BigInteger.ZERO).isGreaterThan(BigInteger.ZERO);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public SELF isGreaterThan(T other) {
    comparables.assertGreaterThan(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThatComparable('b').isGreaterThanOrEqualTo('a');
   * assertThatComparable(BigInteger.ONE).isGreaterThanOrEqualTo(BigInteger.ONE);
   *
   * // assertions will fail
   * assertThatComparable('a').isGreaterThanOrEqualTo('b');
   * assertThatComparable(BigInteger.ZERO).isGreaterThanOrEqualTo(BigInteger.ONE);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public SELF isGreaterThanOrEqualTo(T other) {
    comparables.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual value is in [start, end] range (start included, end included).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThatComparable('b').isBetween('a', 'c');
   * assertThatComparable('a').isBetween('a', 'b');
   * assertThatComparable('b').isBetween('a', 'b');
   *
   * // assertions fail
   * assertThatComparable('a').isBetween('b', 'c');
   * assertThatComparable('c').isBetween('a', 'b');</code></pre>
   *
   * @param startInclusive the start value (inclusive), expected not to be null.
   * @param endInclusive the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in [start, end] range.
   */
  public SELF isBetween(T startInclusive, T endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThatComparable('b').isStrictlyBetween('a', 'c');
   *
   * // assertions fail
   * assertThatComparable('d').isStrictlyBetween('a', 'c');
   * assertThatComparable('a').isStrictlyBetween('b', 'd');
   * assertThatComparable('a').isStrictlyBetween('a', 'b');
   * assertThatComparable('b').isStrictlyBetween('a', 'b');</code></pre>
   *
   * @param startExclusive the start value (exclusive), expected not to be null.
   * @param endExclusive the end value (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ range.
   */
  public SELF isStrictlyBetween(T startExclusive, T endExclusive) {
    comparables.assertIsBetween(info, actual, startExclusive, endExclusive, false, false);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Comparable<T>> customComparator) {
    return super.usingComparator(customComparator);
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super Comparable<T>> customComparator, String customComparatorDescription) {
    this.comparables = new Comparables(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
    return super.usingComparator(customComparator, customComparatorDescription);
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    this.comparables = new Comparables();
    return super.usingDefaultComparator();
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF inHexadecimal() {
    return super.inHexadecimal();
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF inBinary() {
    return super.inBinary();
  }

}
