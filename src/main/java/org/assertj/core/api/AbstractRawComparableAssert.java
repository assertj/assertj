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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

import org.assertj.core.internal.Comparables;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Comparable assertion for generic type which {@link AbstractComparableAssert} does not fully support due to some generic signature.
 * <p> 
 * Let's take an example with a class <code>Name</code> implementing <code>Comparable&lt;Name&gt;</code>.
 * <pre><code class='java'> Comparable&lt;Name&gt; name1 = new Name("abc");</code></pre>
 * <p>
 * The following does not compile or work as expected: 
 * <pre><code class='java'> // does not compile as assertThat(name1) resolves to Object assertions
 * assertThat(name1).isLessThanOrEqualTo(name1);
 * 
 * // works but need to cast raw Comparable (assertThat resolves to AbstractComparableAssert)
 * assertThat((Comparable)name1).isLessThanOrEqualTo(name1);
 * 
 * // does not compile: Cannot infer type arguments for GenericComparableAssert&lt;&gt;
 * new GenericComparableAssert&lt;&gt;(name1).isLessThanOrEqualTo(name3);
 * 
 * // compiles fine without generic Comparable (assertThat resolves to AbstractComparableAssert)
 * Name name = name1;
 * assertThat(name).isEqualByComparingTo(name);</code></pre>
 * <p>
 * This class aims to allow writing 
 * <pre><code class='java'> // assertThatComparable resolves to AbstractGenericComparableAssert
 * assertThatComparable(name1).isLessThanOrEqualTo(name1);
 * 
 * // it works without generic Comparable too 
 * assertThat(name).isEqualByComparingTo(name);</code></pre>
 */
public abstract class AbstractRawComparableAssert<SELF extends AbstractRawComparableAssert<SELF, T>, T>
    extends AbstractObjectAssert<SELF, Comparable<T>> {

  @VisibleForTesting
  Comparables comparables = new Comparables();

  protected AbstractRawComparableAssert(Comparable<T> actual, Class<?> selfType) {
    super(actual, selfType);
  }

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
  public <C extends Comparable<? super T>> SELF isLessThan(C other) {
    comparables.assertLessThan(info, actual, other);
    return myself;
  }

  public <C extends Comparable<? super T>> SELF isEqualByComparingTo(C other) {
    comparables.assertEqualByComparison(info, actual, other);
    return myself;
  }

  public <C extends Comparable<? super T>> SELF isNotEqualByComparingTo(C other) {
    comparables.assertNotEqualByComparison(info, actual, other);
    return myself;
  }

  public <C extends Comparable<? super T>> SELF isLessThanOrEqualTo(C other) {
    comparables.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  public <C extends Comparable<? super T>> SELF isGreaterThan(C other) {
    comparables.assertGreaterThan(info, actual, other);
    return myself;
  }

  public <C extends Comparable<? super T>> SELF isGreaterThanOrEqualTo(C other) {
    comparables.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }

  public <C extends Comparable<? super T>> SELF isBetween(C startInclusive, C endInclusive) {
    comparables.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return myself;
  }

  public <C extends Comparable<? super T>> SELF isStrictlyBetween(C startExclusive, C endExclusive) {
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
