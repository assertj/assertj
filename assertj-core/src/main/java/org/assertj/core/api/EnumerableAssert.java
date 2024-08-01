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

/**
 * Assertions applicable to groups of values that can be enumerated (e.g. arrays, collections or strings.)
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ELEMENT> the type of elements of the "actual" value.
 */
public interface EnumerableAssert<SELF extends EnumerableAssert<SELF, ELEMENT>, ELEMENT> {

  /**
   * Verifies that the actual group of values is {@code null} or empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * List&lt;String&gt; strings = new ArrayList&lt;&gt;();
   * assertThat(strings).isNullOrEmpty();
   * assertThat(new int[] { }).isNullOrEmpty();
   *
   * // assertions will fail
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot;}).isNullOrEmpty();
   * assertThat(Arrays.asList(1, 2, 3)).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the actual group of values is not {@code null} or not empty.
   */
  void isNullOrEmpty();

  /**
   * Verifies that the actual group of values is empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new ArrayList()).isEmpty();
   * assertThat(new int[] { }).isEmpty();
   *
   * // assertions will fail
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).isEmpty();
   * assertThat(Arrays.asList(1, 2, 3)).isEmpty();</code></pre>
   *
   * @throws AssertionError if the actual group of values is not empty.
   */
  void isEmpty();

  /**
   * Verifies that the actual group of values is not empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).isNotEmpty();
   * assertThat(Arrays.asList(1, 2, 3)).isNotEmpty();
   *
   * // assertions will fail
   * assertThat(new ArrayList()).isNotEmpty();
   * assertThat(new int[] { }).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group of values is empty.
   */
  SELF isNotEmpty();

  /**
   * Verifies that the number of values in the actual group is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSize(2);
   * assertThat(Arrays.asList(1, 2, 3)).hasSize(3);
   *
   * // assertions will fail
   * assertThat(new ArrayList()).hasSize(1);
   * assertThat(new int[] { 1, 2, 3 }).hasSize(2);</code></pre>
   *
   * @param expected the expected number of values in the actual group.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual group is not equal to the given one.
   */
  SELF hasSize(int expected);

  /**
   * Verifies that the number of values in the actual group is greater than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeGreaterThan(1);
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeGreaterThan(2);
   *
   * // assertions will fail
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeGreaterThan(3);
   * assertThat(new int[] { 1, 2, 3 }).hasSizeGreaterThan(6);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual group is not greater than the boundary.
   */
  SELF hasSizeGreaterThan(int boundary);

  /**
   * Verifies that the number of values in the actual group is greater than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeGreaterThanOrEqualTo(3);
   * assertThat(Arrays.asList(1, 2)).hasSizeGreaterThanOrEqualTo(1);
   *
   * // assertions will fail
   * assertThat(Arrays.asList(1, 2)).hasSizeGreaterThanOrEqualTo(3);
   * assertThat(new int[] { 1, 2, 3 }).hasSizeGreaterThanOrEqualTo(4);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual group is not greater than or equal to the boundary.
   */
  SELF hasSizeGreaterThanOrEqualTo(int boundary);

  /**
   * Verifies that the number of values in the actual group is less than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeLessThan(5);
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeLessThan(4);
   *
   * // assertions will fail
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeLessThan(3);
   * assertThat(new int[] { 1, 2, 3 }).hasSizeLessThan(2);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual group is not less than the boundary.
   */
  SELF hasSizeLessThan(int boundary);

  /**
   * Verifies that the number of values in the actual group is less than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeLessThanOrEqualTo(3);
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeLessThanOrEqualTo(5)
   *                                   .hasSizeLessThanOrEqualTo(3);
   *
   * // assertions will fail
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeLessThanOrEqualTo(2);
   * assertThat(new int[] { 1, 2, 3 }).hasSizeLessThanOrEqualTo(1);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual group is not less than or equal to the boundary.
   */
  SELF hasSizeLessThanOrEqualTo(int boundary);

  /**
   * Verifies that the number of values in the actual group is between the given boundaries (inclusive).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeBetween(0, 4);
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeBetween(2, 3)
   *                                   .hasSizeBetween(3, 4)
   *                                   .hasSizeBetween(3, 3);
   *
   * // assertions will fail
   * assertThat(new ArrayList()).hasSizeBetween(1, 3);
   * assertThat(new int[] { 1, 2, 3 }).hasSizeBetween(4, 6);
   * assertThat(new int[] { 1, 2, 3, 4 }).hasSizeBetween(0, 2);</code></pre>
   *
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual group is not between the boundaries.
   */
  SELF hasSizeBetween(int lowerBoundary, int higherBoundary);

  /**
   * Verifies that the actual group has the same size as given {@link Iterable}.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;String&gt; abc = newArrayList("a", "b", "c");
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertion will pass
   * assertThat(elvesRings).hasSameSizeAs(abc);
   *
   * // assertions will fail
   * assertThat(elvesRings).hasSameSizeAs(Arrays.asList(1, 2));
   * assertThat(elvesRings).hasSameSizeAs(Arrays.asList(1, 2, 3, 4));</code></pre>
   *
   * @param other the {@code Iterable} to compare size with actual group.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other {@code Iterable} is {@code null}.
   * @throws AssertionError if actual group and given {@code Iterable} don't have the same size.
   */
  SELF hasSameSizeAs(Iterable<?> other);

  /**
   * Verifies that the actual group has the same size as given array.
   * <p>
   * Parameter is declared as Object to accept both Object[] and primitive arrays (e.g. int[]).
   * </p>
   * Example:
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertion will pass
   * assertThat(elvesRings).hasSameSizeAs(oneTwoThree);
   *
   * // assertions will fail
   * assertThat(elvesRings).hasSameSizeAs(new int[] { 1, 2});
   * assertThat(elvesRings).hasSameSizeAs(new int[] { 1, 2, 3, 4});</code></pre>
   *
   * @param array the array to compare size with actual group.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual group and given array don't have the same size.
   */
  SELF hasSameSizeAs(Object array);

  /**
   * Use given custom comparator instead of relying on actual type A <code>equals</code> method to compare group
   * elements for incoming assertion checks.
   * <p>
   * Custom comparator is bound to assertion instance, meaning that if a new assertion is created, it will use default
   * comparison strategy.
   * </p>
   * Examples :
   * <pre><code class='java'> // compares invoices by payee
   * assertThat(invoiceList).usingComparator(invoicePayeeComparator).isEqualTo(expectedInvoiceList);
   *
   * // compares invoices by date, doesNotHaveDuplicates and contains both use the given invoice date comparator
   * assertThat(invoiceList).usingComparator(invoiceDateComparator).doesNotHaveDuplicates().contains(may2010Invoice);
   *
   * // as assertThat(invoiceList) creates a new assertion, it falls back to standard comparison strategy
   * // based on Invoice's equal method to compare invoiceList elements to lowestInvoice.
   * assertThat(invoiceList).contains(lowestInvoice);
   *
   * // standard comparison : the fellowshipOfTheRing includes Gandalf but not Sauron (believe me) ...
   * assertThat(fellowshipOfTheRing).contains(gandalf)
   *                                .doesNotContain(sauron);
   *
   * // ... but if we compare only races, Sauron is in fellowshipOfTheRing because he's a Maia like Gandalf.
   * assertThat(fellowshipOfTheRing).usingElementComparator(raceComparator)
   *                                .contains(sauron);</code></pre>
   *
   * @param customComparator the comparator to use for incoming assertion checks.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  SELF usingElementComparator(Comparator<? super ELEMENT> customComparator);

  /**
   * Revert to standard comparison for incoming assertion group element checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling
   * {@link #usingElementComparator(Comparator)}.
   *
   * @return {@code this} assertion object.
   */
  SELF usingDefaultElementComparator();
}
