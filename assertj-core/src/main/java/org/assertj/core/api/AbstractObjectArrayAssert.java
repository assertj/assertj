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

import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.filter.Filters.filter;
import static org.assertj.core.description.Description.mostRelevantDescription;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOf;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOfMethod;
import static org.assertj.core.extractor.Extractors.resultOf;
import static org.assertj.core.internal.CommonValidations.checkSequenceIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkSubsequenceIsNotNull;
import static org.assertj.core.internal.Iterables.byPassingAssertions;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.filter.FilterOperator;
import org.assertj.core.api.filter.Filters;
import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.condition.Not;
import org.assertj.core.data.Index;
import org.assertj.core.description.Description;
import org.assertj.core.groups.FieldsOrPropertiesExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.CommonErrors;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ConfigurableRecursiveFieldByFieldComparator;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.FieldByFieldComparator;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.ObjectArrayElementComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.OnFieldsComparator;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.Strings;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Assertion methods for arrays of objects.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Object[])}</code>.
 * </p>
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Mateusz Haligowski
 * @author Lovro Pandzic
 *
 * @param <ELEMENT> the type of elements of the "actual" value.
 */
// suppression of deprecation works in Eclipse to hide warning for the deprecated classes in the imports
// IntelliJ thinks this is redundant when it is not.
@SuppressWarnings({ "deprecation", "RedundantSuppression" })
public abstract class AbstractObjectArrayAssert<SELF extends AbstractObjectArrayAssert<SELF, ELEMENT>, ELEMENT> extends
    AbstractAssert<SELF, ELEMENT[]>
    implements IndexedObjectEnumerableAssert<AbstractObjectArrayAssert<SELF, ELEMENT>, ELEMENT>,
    ArraySortedAssert<AbstractObjectArrayAssert<SELF, ELEMENT>, ELEMENT> {

  private static final String ASSERT = "Assert";

  @VisibleForTesting
  ObjectArrays arrays = ObjectArrays.instance();
  @VisibleForTesting
  Iterables iterables = Iterables.instance();

  // not private because AbstractIterableAssert.withAssertionState needs to access them
  TypeComparators comparatorsByType;
  Map<String, Comparator<?>> comparatorsForElementPropertyOrFieldNames = new TreeMap<>();
  TypeComparators comparatorsForElementPropertyOrFieldTypes;

  protected AbstractObjectArrayAssert(ELEMENT[] actual, Class<?> selfType) {
    super(actual, selfType);
  }

  @Override
  public SELF as(Description description) {
    return super.as(description);
  }

  @Override
  public SELF as(String description, Object... args) {
    return super.as(description, args);
  }

  /**
   * {@inheritDoc}
   *
   * @throws AssertionError {@inheritDoc}
   */
  @Override
  public void isNullOrEmpty() {
    arrays.assertNullOrEmpty(info, actual);
  }

  /**
   * {@inheritDoc}
   *
   * @throws AssertionError {@inheritDoc}
   */
  @Override
  public void isEmpty() {
    arrays.assertEmpty(info, actual);
  }

  /**
   * {@inheritDoc}
   *
   * @throws AssertionError {@inheritDoc}
   */
  @Override
  public SELF isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   *
   * @throws AssertionError {@inheritDoc}
   */
  @Override
  public SELF hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is greater than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeGreaterThan(1);
   *
   * // assertion will fail
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeGreaterThan(2);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not greater than the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThan(int boundary) {
    arrays.assertHasSizeGreaterThan(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is greater than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeGreaterThanOrEqualTo(1)
   *                                      .hasSizeGreaterThanOrEqualTo(2);
   *
   * // assertion will fail
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeGreaterThanOrEqualTo(3);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not greater than or equal to the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThanOrEqualTo(int boundary) {
    arrays.assertHasSizeGreaterThanOrEqualTo(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is less than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeLessThan(5);
   *
   * // assertion will fail
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeLessThan(2);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not less than the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThan(int boundary) {
    arrays.assertHasSizeLessThan(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is less than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeLessThanOrEqualTo(3)
   *                                      .hasSizeLessThanOrEqualTo(2);
   *
   * // assertion will fail
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeLessThanOrEqualTo(1);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not less than or equal to the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThanOrEqualTo(int boundary) {
    arrays.assertHasSizeLessThanOrEqualTo(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual array is between the given boundaries (inclusive).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeBetween(0, 3)
   *                                      .hasSizeBetween(2, 2);
   *
   * // assertions will fail
   * assertThat(new String[] { &quot;a&quot;, &quot;b&quot; }).hasSizeBetween(3, 4);</code></pre>
   *
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual array is not between the boundaries.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeBetween(int lowerBoundary, int higherBoundary) {
    arrays.assertHasSizeBetween(info, actual, lowerBoundary, higherBoundary);
    return myself;
  }

  /**
   * Verifies that the actual array has the same size as the given array.
   * <p>
   * Parameter is declared as Object to accept both {@code Object[]} and primitive arrays (e.g. {@code int[]}).
   * <p>
   * Example:
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   * int[] fourFiveSix = {4, 5, 6};
   * int[] sevenEight = {7, 8};
   *
   * // assertion will pass
   * assertThat(oneTwoThree).hasSameSizeAs(fourFiveSix);
   *
   * // assertion will fail
   * assertThat(oneTwoThree).hasSameSizeAs(sevenEight);</code></pre>
   *
   * @param other the array to compare size with actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual array and given array don't have the same size.
   */
  @Override
  public SELF hasSameSizeAs(Object other) {
    // same implementation as in AbstractArrayAssert, but can't inherit from it due to generics problem ...
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array has the same size as the given {@link Iterable}.
   * <p>
   * Example:
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertion will pass
   * assertThat(oneTwoThree).hasSameSizeAs(elvesRings);
   *
   * // assertion will fail
   * assertThat(oneTwoThree).hasSameSizeAs(Arrays.asList("a", "b"));</code></pre>
   *
   * @param other the {@code Iterable} to compare size with actual array.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the other {@code Iterable} is {@code null}.
   * @throws AssertionError if actual array and given {@code Iterable} don't have the same size.
   */
  @Override
  public SELF hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given values, in any order.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertions will pass
   * assertThat(abc).contains("b", "a");
   * assertThat(abc).contains("b", "a", "b");
   *
   * // assertions will fail
   * assertThat(abc).contains("d");
   * assertThat(abc).contains("c", "d");</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values.
   */
  @Override
  @SafeVarargs
  public final SELF contains(ELEMENT... values) {
    return containsForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsForProxy(ELEMENT[] values) {
    arrays.assertContains(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains only the given values and nothing else, in any order and ignoring duplicates (i.e. once a value is found, its duplicates are also considered found).
   * <p>
   * If you need to check exactly the elements and their duplicates use:
   * <ul>
   * <li>{@link #containsExactly(Object...) containsExactly(Object...)} if the order does matter</li>
   * <li>{@link #containsExactlyInAnyOrder(Object...) containsExactlyInAnyOrder(Object...)} if the order does not matter</li>
   * </ul>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertions succeed
   * assertThat(abc).containsOnly("c", "b", "a");
   * // duplicates are ignored
   * assertThat(abc).containsOnly("a", "a", "b", "c", "c");
   * // ... on both actual and expected values
   * assertThat(new String[] { "a", "a", "b" }).containsOnly("a", "b")
   *                                           .containsOnly("a", "a", "b", "b");
   *
   * // assertion will fail because the given values do not contain "c"
   * assertThat(abc).containsOnly("a", "b");
   * // assertion will fail because abc does not contain "d"
   * assertThat(abc).containsOnly("a", "b", "c", "d");</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more values than the given ones.
   */
  @Override
  @SafeVarargs
  public final SELF containsOnly(ELEMENT... values) {
    return containsOnlyForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsOnlyForProxy(ELEMENT[] values) {
    arrays.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * Same semantic as {@link #containsOnly(Object[])} : verifies that actual contains all elements of the given
   * {@code Iterable} and nothing else, <b>in any order</b>  and ignoring duplicates (i.e. once a value is found, its duplicates are also considered found).
   * <p>
   * Example :
   * <pre><code class='java'> Ring[] rings = {nenya, vilya};
   *
   * // assertions will pass
   * assertThat(rings).containsOnlyElementsOf(newArrayList(nenya, vilya));
   * assertThat(rings).containsOnlyElementsOf(newArrayList(nenya, nenya, vilya, vilya));
   * assertThat(newArrayList(nenya, nenya, vilya, vilya)).containsOnlyElementsOf(rings);
   *
   * // assertion will fail as actual does not contain narya
   * assertThat(rings).containsOnlyElementsOf(newArrayList(nenya, vilya, narya));
   * // assertion will fail as actual contains nenya
   * assertThat(rings).containsOnlyElementsOf(newArrayList(vilya));</code></pre>
   *
   * @param iterable the given {@code Iterable} we will get elements from.
   */
  @Override
  public SELF containsOnlyElementsOf(Iterable<? extends ELEMENT> iterable) {
    return containsOnly(toArray(iterable));
  }

  /**
   * Verifies that the actual array contains only null elements.
   * <p>
   * Example :
   * <pre><code class='java'> Person[] persons1 = {null, null, null};
   * Person[] persons2 = {null, null, person};
   *
   * // assertion will pass
   * assertThat(persons1).containsOnlyNulls();
   *
   * // assertions will fail
   * assertThat(persons2).containsOnlyNulls();
   * assertThat(new Person[0]).containsOnlyNulls();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array is empty or contains a non null element
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public SELF containsOnlyNulls() {
    arrays.assertContainsOnlyNulls(info, actual);
    return myself;
  }

  /**
   * Verifies that the unique element of the array satisfies the given assertions expressed as a {@link Consumer},
   * if it does not, only the first error is reported, use {@link SoftAssertions} to get all the errors.
   * <p>
   * Example:
   * <pre><code class='java'> Jedi[] jedis = array(new Jedi("Yoda", "red"));
   *
   * // assertions will pass
   *
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; assertThat(yoda.getName()).startsWith("Y"));
   *
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; {
   *   assertThat(yoda.getName()).isEqualTo("Yoda");
   *   assertThat(yoda.getLightSaberColor()).isEqualTo("red");
   * });
   *
   * // assertions will fail
   *
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; assertThat(yoda.getName()).startsWith("Vad"));
   *
   * // fail as one the assertions is not satisfied
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; {
   *   assertThat(yoda.getName()).isEqualTo("Yoda");
   *   assertThat(yoda.getLightSaberColor()).isEqualTo("purple");
   * });
   *
   * // fail but only report the first error
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; {
   *   assertThat(yoda.getName()).isEqualTo("Luke");
   *   assertThat(yoda.getLightSaberColor()).isEqualTo("green");
   * });
   *
   * // fail and reports the errors thanks to Soft assertions
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; {
   *   SoftAssertions softly = new SoftAssertions();
   *   softly.assertThat(yoda.getName()).isEqualTo("Luke");
   *   softly.assertThat(yoda.getLightSaberColor()).isEqualTo("green");
   *   softly.assertAll();
   * });
   *
   * // even if the assertion is correct, there are too many jedis !
   * jedis = array(new Jedi("Yoda", "red"), new Jedi("Luke", "green"));
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; assertThat(yoda.getName()).startsWith("Yo"));</code></pre>
   *
   * @param elementAssertions the assertions to perform on the unique element.
   * @throws AssertionError if the array does not have a unique element.
   * @throws AssertionError if the array's unique element does not satisfies the given assertions.
   * @since 3.5.0
   */
  @Override
  public SELF hasOnlyOneElementSatisfying(Consumer<? super ELEMENT> elementAssertions) {
    iterables.assertHasOnlyOneElementSatisfying(info, newArrayList(actual), elementAssertions);
    return myself;
  }

  /**
   * An alias of {@link #containsOnlyElementsOf(Iterable)} : verifies that actual contains all elements of the
   * given {@code Iterable} and nothing else, <b>in any order</b>.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertions will pass:
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya, vilya));
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya, vilya, nenya));
   *
   * // assertions will fail:
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya));
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya, vilya, oneRing));</code></pre>
   *
   * @param iterable the {@code Iterable} whose elements we expect to be present
   * @return this assertion object
   * @throws AssertionError if the actual array is {@code null}
   * @throws NullPointerException if the given {@code Iterable} is {@code null}
   * @throws AssertionError if the actual {@code Iterable} does not have the same elements, in any order, as the given
   *           {@code Iterable}
   */
  @Override
  public SELF hasSameElementsAs(Iterable<? extends ELEMENT> iterable) {
    return containsOnlyElementsOf(iterable);
  }

  /**
   * Verifies that the actual array contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // array is a factory method to create arrays.
   *
   * // assertions will pass
   * assertThat(array(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;winter&quot;);
   * assertThat(array(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;coming&quot;, &quot;winter&quot;);
   *
   * // assertions will fail
   * assertThat(array(&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;)).containsOnlyOnce(&quot;Lannister&quot;);
   * assertThat(array(&quot;Arya&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnce(&quot;Stark&quot;);
   * assertThat(array(&quot;Arya&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)).containsOnlyOnce(&quot;Stark&quot;, &quot;Lannister&quot;, &quot;Arya&quot;);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given values, i.e. the actual array contains some
   *           or none of the given values, or the actual array contains more than once these values.
   */
  @Override
  @SafeVarargs
  public final SELF containsOnlyOnce(ELEMENT... values) {
    return containsOnlyOnceForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsOnlyOnceForProxy(ELEMENT[] values) {
    arrays.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsOnlyOnceElementsOf(Iterable<? extends ELEMENT> iterable) {
    return containsOnlyOnce(toArray(iterable));
  }

  /**
   * Verifies that the actual array contains exactly the given values and nothing else, <b>in order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactly(vilya, nenya, narya);
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(elvesRings).containsExactly(nenya, vilya, narya);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given values with same order, i.e. the actual array
   *           contains some or none of the given values, or the actual array contains more values than the given ones
   *           or values are the same but the order is not.
   */
  @Override
  @SafeVarargs
  public final SELF containsExactly(ELEMENT... values) {
    return containsExactlyForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsExactlyForProxy(ELEMENT[] values) {
    arrays.assertContainsExactly(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains exactly the given values and nothing else, <b>in any order</b>.<br>
   *
   * <p>
   * Example :
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya, vilya};
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactlyInAnyOrder(vilya, vilya, nenya, narya);
   *
   * // assertion will fail as vilya exists twice in elvesRings
   * assertThat(elvesRings).containsExactlyInAnyOrder(nenya, vilya, narya);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual arrray does not contain the given values, i.e. the actual array
   *           contains some or none of the given values, or the actual array contains more values than the given ones.
   */
  @Override
  @SafeVarargs
  public final SELF containsExactlyInAnyOrder(ELEMENT... values) {
    return containsExactlyInAnyOrderForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsExactlyInAnyOrderForProxy(ELEMENT[] values) {
    arrays.assertContainsExactlyInAnyOrder(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsExactlyInAnyOrderElementsOf(Iterable<? extends ELEMENT> values) {
    return containsExactlyInAnyOrder(toArray(values));
  }

  /**
   * Same as {@link #containsExactly(Object...)} but handles the {@link Iterable} to array conversion : verifies that
   * actual contains exactly the elements of the given {@code Iterable} and nothing else <b>in the same order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactlyElementsOf(newLinkedList(vilya, nenya, narya));
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(elvesRings).containsExactlyElementsOf(newLinkedList(nenya, vilya, narya));</code></pre>
   *
   * @param iterable the given {@code Iterable} we will get elements from.
   */
  @Override
  public SELF containsExactlyElementsOf(Iterable<? extends ELEMENT> iterable) {
    return containsExactly(toArray(iterable));
  }

  /**
   * Verifies that the actual array contains the given sequence in the correct order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #containsSubsequence(Object...)} to allow values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertion will pass
   * assertThat(elvesRings).containsSequence(vilya, nenya);
   * assertThat(elvesRings).containsSequence(nenya, narya);
   *
   * // assertions will fail, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).containsSequence(vilya, narya);
   * assertThat(elvesRings).containsSequence(nenya, vilya);</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  @Override
  @SafeVarargs
  public final SELF containsSequence(ELEMENT... sequence) {
    return containsSequenceForProxy(sequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsSequenceForProxy(ELEMENT[] sequence) {
    arrays.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given sequence in the correct order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #containsSubsequence(Iterable)} to allow values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertion will pass
   * assertThat(elvesRings).containsSequence(newArrayList(vilya, nenya));
   * assertThat(elvesRings).containsSequence(newArrayList(nenya, narya));
   *
   * // assertions will fail, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).containsSequence(newArrayList(vilya, narya));
   * assertThat(elvesRings).containsSequence(newArrayList(nenya, vilya));</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given argument is an empty iterable and the actual array is not empty.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  @Override
  public SELF containsSequence(Iterable<? extends ELEMENT> sequence) {
    checkSequenceIsNotNull(sequence);
    arrays.assertContainsSequence(info, actual, toArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given sequence in the given order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #doesNotContainSubsequence(Object...)} to also ensure the sequence does not exist with values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertion will pass, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).containsSequence(vilya, narya);
   * assertThat(elvesRings).containsSequence(nenya, vilya);
   *
   * // assertions will fail
   * assertThat(elvesRings).containsSequence(vilya, nenya);
   * assertThat(elvesRings).containsSequence(nenya, narya);</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  @Override
  @SafeVarargs
  public final SELF doesNotContainSequence(ELEMENT... sequence) {
    return doesNotContainSequenceForProxy(sequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF doesNotContainSequenceForProxy(ELEMENT[] sequence) {
    arrays.assertDoesNotContainSequence(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given sequence in the given order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #doesNotContainSubsequence(Object...)} to also ensure the sequence does not exist with values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertion will pass, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).containsSequence(newArrayList(vilya, narya));
   * assertThat(elvesRings).containsSequence(newArrayList(nenya, vilya));
   *
   * // assertions will fail
   * assertThat(elvesRings).containsSequence(newArrayList(vilya, nenya));
   * assertThat(elvesRings).containsSequence(newArrayList(nenya, narya));</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given sequence.
   */
  @Override
  public SELF doesNotContainSequence(Iterable<? extends ELEMENT> sequence) {
    checkSequenceIsNotNull(sequence);
    arrays.assertDoesNotContainSequence(info, actual, toArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence in the correct order (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertions will pass
   * assertThat(elvesRings).containsSubsequence(vilya, nenya);
   * assertThat(elvesRings).containsSubsequence(vilya, narya);
   *
   * // assertion will fail
   * assertThat(elvesRings).containsSubsequence(nenya, vilya);</code></pre>
   *
   * @param subsequence the subsequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  @Override
  @SafeVarargs
  public final SELF containsSubsequence(ELEMENT... subsequence) {
    return containsSubsequenceForProxy(subsequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsSubsequenceForProxy(ELEMENT[] subsequence) {
    arrays.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array contains the given subsequence in the correct order (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertions will pass
   * assertThat(elvesRings).containsSubsequence(newArrayList(vilya, nenya));
   * assertThat(elvesRings).containsSubsequence(newArrayList(vilya, narya));
   *
   * // assertion will fail
   * assertThat(elvesRings).containsSubsequence(newArrayList(nenya, vilya));</code></pre>
   *
   * @param subsequence the subsequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array does not contain the given subsequence.
   */
  @Override
  public SELF containsSubsequence(Iterable<? extends ELEMENT> subsequence) {
    checkSubsequenceIsNotNull(subsequence);
    arrays.assertContainsSubsequence(info, actual, toArray(subsequence));
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given subsequence in the correct order (possibly with other
   * values between them).
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertions will pass
   * assertThat(elvesRings).doesNotContainSubsequence(nenya, vilya);
   *
   * // assertion will fail
   * assertThat(elvesRings).doesNotContainSubsequence(vilya, nenya);
   * assertThat(elvesRings).doesNotContainSubsequence(vilya, narya);</code></pre>
   *
   * @param subsequence the subsequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array contains the given subsequence.
   */
  @Override
  @SafeVarargs
  public final SELF doesNotContainSubsequence(ELEMENT... subsequence) {
    return doesNotContainSubsequenceForProxy(subsequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF doesNotContainSubsequenceForProxy(ELEMENT[] subsequence) {
    arrays.assertDoesNotContainSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given subsequence in the correct order (possibly with other
   * values between them).
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertions will pass
   * assertThat(elvesRings).doesNotContainSubsequence(newArrayList(nenya, vilya));
   *
   * // assertion will fail
   * assertThat(elvesRings).doesNotContainSubsequence(newArrayList(vilya, nenya));
   * assertThat(elvesRings).doesNotContainSubsequence(newArrayList(vilya, narya));</code></pre>
   *
   * @param subsequence the subsequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array contains the given subsequence.
   */
  @Override
  public SELF doesNotContainSubsequence(Iterable<? extends ELEMENT> subsequence) {
    checkSubsequenceIsNotNull(subsequence);
    arrays.assertDoesNotContainSubsequence(info, actual, toArray(subsequence));
    return myself;
  }

  /**
   * Verifies that the actual array contains the given object at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertions will pass
   * assertThat(elvesRings).contains(vilya, atIndex(0));
   * assertThat(elvesRings).contains(nenya, atIndex(1));
   * assertThat(elvesRings).contains(narya, atIndex(2));
   *
   * // assertions will fail
   * assertThat(elvesRings).contains(vilya, atIndex(1));
   * assertThat(elvesRings).contains(nenya, atIndex(2));
   * assertThat(elvesRings).contains(narya, atIndex(0));</code></pre>
   *
   * @param value the object to look for.
   * @param index the index where the object should be stored in the actual array.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of the actual
   *           group.
   * @throws AssertionError if the actual array does not contain the given object at the given index.
   */
  @Override
  public SELF contains(ELEMENT value, Index index) {
    arrays.assertContains(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that all elements of the actual array are instances of given classes or interfaces.
   * <p>
   * Example :
   * <pre><code class='java'> Object[] objects = { "foo", new StringBuilder() };
   *
   * // assertions will pass
   * assertThat(objects).hasOnlyElementsOfTypes(CharSequence.class);
   * assertThat(objects).hasOnlyElementsOfTypes(String.class, StringBuilder.class);
   *
   * // assertions will fail
   * assertThat(objects).hasOnlyElementsOfTypes(Number.class);
   * assertThat(objects).hasOnlyElementsOfTypes(String.class, Number.class);
   * assertThat(objects).hasOnlyElementsOfTypes(String.class);</code></pre>
   *
   * @param types the expected classes and interfaces
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if not all elements of the actual array are instances of one of the given types
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public SELF hasOnlyElementsOfTypes(Class<?>... types) {
    arrays.assertHasOnlyElementsOfTypes(info, actual, types);
    return myself;
  }

  /**
   * Verifies that the actual elements are of the given types in the given order, there should be as many expected types as there are actual elements.
   * <p>
   * Example:
   * <pre><code class='java'> Object[] objects = { 1, "a", "b", 1.00 };
   *
   * // assertion succeeds
   * assertThat(objects).hasExactlyElementsOfTypes(Integer.class, String.class, String.class, Double.class);
   *
   * // assertions fail
   * // missing second String type
   * assertThat(objects).hasExactlyElementsOfTypes(Integer.class, String.class, Double.class);
   * // no Float type in actual
   * assertThat(objects).hasExactlyElementsOfTypes(Float.class, String.class, String.class, Double.class);
   * // correct types but wrong order
   * assertThat(objects).hasExactlyElementsOfTypes(String.class, Integer.class, String.class, Double.class);
   * // actual has more elements than the specified expected types
   * assertThat(objects).hasExactlyElementsOfTypes(String.class);</code></pre>
   *
   * @param expectedTypes the expected types
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given type array is {@code null}.
   * @throws AssertionError if actual is {@code null}.
   * @throws AssertionError if the actual elements types don't exactly match the given ones (in the given order).
   */
  @Override
  public SELF hasExactlyElementsOfTypes(Class<?>... expectedTypes) {
    arrays.assertHasExactlyElementsOfTypes(info, actual, expectedTypes);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given object at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertions will pass
   * assertThat(elvesRings).doesNotContain(vilya, atIndex(1));
   * assertThat(elvesRings).doesNotContain(nenya, atIndex(2));
   * assertThat(elvesRings).doesNotContain(narya, atIndex(0));
   *
   * // assertions will fail
   * assertThat(elvesRings).doesNotContain(vilya, atIndex(0));
   * assertThat(elvesRings).doesNotContain(nenya, atIndex(1));
   * assertThat(elvesRings).doesNotContain(narya, atIndex(2));</code></pre>
   *
   * @param value the object to look for.
   * @param index the index where the object should not be stored in the actual array.
   * @return this assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual array contains the given object at the given index.
   */
  @Override
  public SELF doesNotContain(ELEMENT value, Index index) {
    arrays.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain the given values.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertion will pass
   * assertThat(abc).doesNotContain("d", "e");
   *
   * // assertions will fail
   * assertThat(abc).doesNotContain("a");
   * assertThat(abc).doesNotContain("a", "b", "c");
   * assertThat(abc).doesNotContain("a", "x");</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains any of the given values.
   */
  @Override
  @SafeVarargs
  public final SELF doesNotContain(ELEMENT... values) {
    return doesNotContainForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF doesNotContainForProxy(ELEMENT[] values) {
    arrays.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain any elements of the given {@link Iterable} (i.e. none).
   * <p>
   * Example:
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertion will pass
   * assertThat(actual).doesNotContainAnyElementsOf(newArrayList("d", "e"));
   *
   * // assertions will fail
   * assertThat(actual).doesNotContainAnyElementsOf(newArrayList("a", "b"));
   * assertThat(actual).doesNotContainAnyElementsOf(newArrayList("d", "e", "a"));</code></pre>
   *
   * @param iterable the {@link Iterable} whose elements must not be in the actual array.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty iterable.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains some elements of the given {@link Iterable}.
   */
  @Override
  public SELF doesNotContainAnyElementsOf(Iterable<? extends ELEMENT> iterable) {
    arrays.assertDoesNotContainAnyElementsOf(info, actual, iterable);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain duplicates.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   * String[] lotsOfAs = {"a", "a", "a"};
   *
   * // assertion will pass
   * assertThat(abc).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(lotsOfAs).doesNotHaveDuplicates();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains duplicates.
   */
  @Override
  public SELF doesNotHaveDuplicates() {
    arrays.assertDoesNotHaveDuplicates(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual array starts with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the first element in the
   * sequence is also the first element of the actual array.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertion will pass
   * assertThat(abc).startsWith("a", "b");
   *
   * // assertion will fail
   * assertThat(abc).startsWith("c");</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the given argument is an empty array and the actual array is not empty.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not start with the given sequence of objects.
   */
  @Override
  @SafeVarargs
  public final SELF startsWith(ELEMENT... sequence) {
    return startsWithForProxy(sequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF startsWithForProxy(ELEMENT[] sequence) {
    arrays.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertions will pass
   * assertThat(abc).endsWith(new String[0])
   *                .endsWith(new String[] {"b", "c"});
   *
   * // assertion will fail
   * assertThat(abc).endsWith(new String[] {"a"});</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence of objects.
   */
  @Override
  public SELF endsWith(ELEMENT[] sequence) {
    arrays.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /**
   * Verifies that the actual array ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual array.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertion will pass
   * assertThat(abc).endsWith("b", "c");
   *
   * // assertion will fail
   * assertThat(abc).endsWith("a");</code></pre>
   *
   * @param first the first element of the end sequence of objects to look for.
   * @param sequence the rest of the end sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not end with the given sequence of objects.
   */
  @Override
  @SafeVarargs
  public final SELF endsWith(ELEMENT first, ELEMENT... sequence) {
    return endsWithForProxy(first, sequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF endsWithForProxy(ELEMENT first, ELEMENT[] sequence) {
    arrays.assertEndsWith(info, actual, first, sequence);
    return myself;
  }

  /**
   * Verifies that all elements of actual are present in the given {@code Iterable}.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   * List&lt;Ring&gt; ringsOfPower = newArrayList(oneRing, vilya, nenya, narya, dwarfRing, manRing);
   *
   * // assertion will pass:
   * assertThat(elvesRings).isSubsetOf(ringsOfPower);
   *
   * // assertion will fail:
   * assertThat(elvesRings).isSubsetOf(newArrayList(nenya, narya));</code></pre>
   *
   * @param values the {@code Iterable} that should contain all actual elements.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the actual {@code Iterable} is not subset of set {@code Iterable}.
   */
  @Override
  public SELF isSubsetOf(Iterable<? extends ELEMENT> values) {
    arrays.assertIsSubsetOf(info, actual, values);
    return myself;
  }

  /**
   * Verifies that all elements of actual are present in the given values.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = {vilya, nenya, narya};
   *
   * // assertions will pass:
   * assertThat(elvesRings).isSubsetOf(vilya, nenya, narya);
   * assertThat(elvesRings).isSubsetOf(vilya, nenya, narya, dwarfRing);
   *
   * // assertions will fail:
   * assertThat(elvesRings).isSubsetOf(vilya, nenya);
   * assertThat(elvesRings).isSubsetOf(vilya, nenya, dwarfRing);</code></pre>
   *
   * @param values the values that should be used for checking the elements of actual.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Iterable} is {@code null}.
   * @throws AssertionError if the actual {@code Iterable} is not subset of the given values.
   */
  @Override
  @SafeVarargs
  public final SELF isSubsetOf(ELEMENT... values) {
    return isSubsetOfForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF isSubsetOfForProxy(ELEMENT[] values) {
    arrays.assertIsSubsetOf(info, actual, Arrays.asList(values));
    return myself;
  }

  /**
   * Verifies that the actual array contains at least a null element.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   * String[] abNull = {"a", "b", null};
   *
   * // assertion will pass
   * assertThat(abNull).containsNull();
   *
   * // assertion will fail
   * assertThat(abc).containsNull();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain a null element.
   */
  @Override
  public SELF containsNull() {
    arrays.assertContainsNull(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual array does not contain null elements.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   * String[] abNull = {"a", "b", null};
   *
   * // assertion will pass
   * assertThat(abc).doesNotContainNull();
   *
   * // assertion will fail
   * assertThat(abNull).doesNotContainNull();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array contains a null element.
   */
  @Override
  public SELF doesNotContainNull() {
    arrays.assertDoesNotContainNull(info, actual);
    return myself;
  }

  /**
   * Verifies that each element value satisfies the given condition
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc  = {"a", "b", "c"};
   * String[] abcc = {"a", "b", "cc"};
   *
   * Condition&lt;String&gt; singleCharacterString
   *      = new Condition&lt;&gt;(s -&gt; s.length() == 1, "single character String");
   *
   * // assertion will pass
   * assertThat(abc).are(singleCharacterString);
   *
   * // assertion will fail
   * assertThat(abcc).are(singleCharacterString);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to ELEMENT.
   * @throws AssertionError if one or more elements don't satisfy the given condition.
   */
  @Override
  public SELF are(Condition<? super ELEMENT> condition) {
    arrays.assertAre(info, actual, condition);
    return myself;
  }

  /**
   * Verifies that each element value does not satisfy the given condition
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc  = {"a", "b", "c"};
   * String[] abcc = {"a", "b", "cc"};
   *
   * Condition&lt;String&gt; moreThanOneCharacter =
   *     = new Condition&lt;&gt;(s -&gt; s.length() &gt; 1, "more than one character");
   *
   * // assertion will pass
   * assertThat(abc).areNot(moreThanOneCharacter);
   *
   * // assertion will fail
   * assertThat(abcc).areNot(moreThanOneCharacter);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to ELEMENT.
   * @throws AssertionError if one or more elements satisfy the given condition.
   */
  @Override
  public SELF areNot(Condition<? super ELEMENT> condition) {
    arrays.assertAreNot(info, actual, condition);
    return myself;
  }

  /**
   * Verifies that all elements satisfy the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc  = {"a", "b", "c"};
   * String[] abcc = {"a", "b", "cc"};
   *
   * Condition&lt;String&gt; onlyOneCharacter =
   *     = new Condition&lt;&gt;(s -&gt; s.length() == 1, "only one character");
   *
   * // assertion will pass
   * assertThat(abc).have(onlyOneCharacter);
   *
   * // assertion will fail
   * assertThat(abcc).have(onlyOneCharacter);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to ELEMENT.
   * @throws AssertionError if one or more elements do not satisfy the given condition.
   */
  @Override
  public SELF have(Condition<? super ELEMENT> condition) {
    arrays.assertHave(info, actual, condition);
    return myself;
  }

  /**
   * Verifies that all elements don't satisfy the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc  = {"a", "b", "c"};
   * String[] abcc = {"a", "b", "cc"};
   *
   * Condition&lt;String&gt; moreThanOneCharacter =
   *     = new Condition&lt;&gt;(s -&gt; s.length() &gt; 1, "more than one character");
   *
   * // assertion will pass
   * assertThat(abc).doNotHave(moreThanOneCharacter);
   *
   * // assertion will fail
   * assertThat(abcc).doNotHave(moreThanOneCharacter);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to ELEMENT.
   * @throws AssertionError if one or more elements satisfy the given condition.
   */
  @Override
  public SELF doNotHave(Condition<? super ELEMENT> condition) {
    arrays.assertDoNotHave(info, actual, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>at least</b> <i>n</i> elements in the actual array satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneTwoThree.areAtLeast(2, oddNumber);
   *
   * // assertion will fail
   * oneTwoThree.areAtLeast(3, oddNumber);</code></pre>
   *
   * @param times the minimum number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element can not be cast to T.
   * @throws AssertionError if the number of elements satisfying the given condition is &lt; n.
   */
  @Override
  public SELF areAtLeast(int times, Condition<? super ELEMENT> condition) {
    arrays.assertAreAtLeast(info, actual, times, condition);
    return myself;
  }

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual array satisfying the given condition.
   * <p>
   * This method is an alias for {@code areAtLeast(1, condition)}.
   * <p>
   * Example:
   * <pre><code class='java'> // jedi is a Condition&lt;String&gt;
   * assertThat(new String[]{"Luke", "Solo", "Leia"}).areAtLeastOne(jedi);</code></pre>
   *
   * @see #haveAtLeast(int, Condition)
   */
  @Override
  public SELF areAtLeastOne(Condition<? super ELEMENT> condition) {
    areAtLeast(1, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>at most</b> <i>n</i> elements in the actual array satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertions will pass
   * oneTwoThree.areAtMost(2, oddNumber);
   * oneTwoThree.areAtMost(3, oddNumber);
   *
   * // assertion will fail
   * oneTwoThree.areAtMost(1, oddNumber);</code></pre>
   *
   * @param times the number of times the condition should be at most verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to ELEMENT.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  @Override
  public SELF areAtMost(int times, Condition<? super ELEMENT> condition) {
    arrays.assertAreAtMost(info, actual, times, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>exactly</b> <i>n</i> elements in the actual array satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneTwoThree.areExactly(2, oddNumber);
   *
   * // assertions will fail
   * oneTwoThree.areExactly(1, oddNumber);
   * oneTwoThree.areExactly(3, oddNumber);</code></pre>
   *
   * @param times the exact number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to ELEMENT.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  @Override
  public SELF areExactly(int times, Condition<? super ELEMENT> condition) {
    arrays.assertAreExactly(info, actual, times, condition);
    return myself;
  }

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual array satisfying the given condition.
   * <p>
   * This method is an alias for {@code haveAtLeast(1, condition)}.
   * <p>
   * Example:
   * <pre><code class='java'> BasketBallPlayer[] bullsPlayers = {butler, rose};
   *
   * // potentialMvp is a Condition&lt;BasketBallPlayer&gt;
   * assertThat(bullsPlayers).haveAtLeastOne(potentialMvp);</code></pre>
   *
   * @see #haveAtLeast(int, Condition)
   */
  @Override
  public SELF haveAtLeastOne(Condition<? super ELEMENT> condition) {
    return haveAtLeast(1, condition);
  }

  /**
   * Verifies that there are <b>at least <i>n</i></b> elements in the actual array satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneTwoThree.haveAtLeast(2, oddNumber);
   *
   * // assertion will fail
   * oneTwoThree.haveAtLeast(3, oddNumber);</code></pre>
   *
   * This method is an alias for {@link #areAtLeast(int, Condition)}.
   */
  @Override
  public SELF haveAtLeast(int times, Condition<? super ELEMENT> condition) {
    arrays.assertHaveAtLeast(info, actual, times, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>at most</b> <i>n</i> elements in the actual array satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertions will pass
   * oneTwoThree.haveAtMost(2, oddNumber);
   * oneTwoThree.haveAtMost(3, oddNumber);
   *
   * // assertion will fail
   * oneTwoThree.haveAtMost(1, oddNumber);</code></pre>
   *
   * This method is an alias {@link #areAtMost(int, Condition)}.
   */
  @Override
  public SELF haveAtMost(int times, Condition<? super ELEMENT> condition) {
    arrays.assertHaveAtMost(info, actual, times, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>exactly</b> <i>n</i> elements in the actual array satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneTwoThree.haveExactly(2, oddNumber);
   *
   * // assertions will fail
   * oneTwoThree.haveExactly(1, oddNumber);
   * oneTwoThree.haveExactly(3, oddNumber);</code></pre>
   *
   * This method is an alias {@link #areExactly(int, Condition)}.
   */
  @Override
  public SELF haveExactly(int times, Condition<? super ELEMENT> condition) {
    arrays.assertHaveExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasAtLeastOneElementOfType(Class<?> type) {
    arrays.assertHasAtLeastOneElementOfType(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasOnlyElementsOfType(Class<?> type) {
    arrays.assertHasOnlyElementsOfType(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF doesNotHaveAnyElementsOfTypes(Class<?>... unexpectedTypes) {
    arrays.assertDoesNotHaveAnyElementsOfTypes(info, actual, unexpectedTypes);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isSorted() {
    arrays.assertIsSorted(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isSortedAccordingTo(Comparator<? super ELEMENT> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  /**
   * Verifies that the actual array contains all the elements of given {@code Iterable}, in any order.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertion will pass
   * assertThat(abc).containsAll(Arrays.asList("b", "c"));
   *
   * // assertions will fail
   * assertThat(abc).containsAll(Arrays.asList("d"));
   * assertThat(abc).containsAll(Arrays.asList("a", "b", "c", "d"));</code></pre>
   *
   * @param iterable the given {@code Iterable} we will get elements from.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array does not contain all the elements of given {@code Iterable}.
   */
  @Override
  public SELF containsAll(Iterable<? extends ELEMENT> iterable) {
    arrays.assertContainsAll(info, actual, iterable);
    return myself;
  }

  /**
   * Use given custom comparator instead of relying on actual type A <code>equals</code> method to compare group
   * elements for incoming assertion checks.
   * <p>
   * Custom comparator is bound to assertion instance, meaning that if a new assertion is created, it will use default
   * comparison strategy.
   * <p>
   * Examples :
   * <pre><code class='java'> // compares invoices by payee
   * assertThat(invoiceArray).usingComparator(invoicePayeeComparator).isEqualTo(expectedinvoiceArray).
   *
   * // compares invoices by date, doesNotHaveDuplicates and contains both use the given invoice date comparator
   * assertThat(invoiceArray).usingComparator(invoiceDateComparator).doesNotHaveDuplicates().contains(may2010Invoice)
   *
   * // as assertThat(invoiceArray) creates a new assertion, it falls back to standard comparison strategy
   * // based on Invoice's equal method to compare invoiceArray elements to lowestInvoice.
   * assertThat(invoiceArray).contains(lowestInvoice).
   *
   * // standard comparison : the fellowshipOfTheRing includes Gandalf but not Sauron (believe me) ...
   * assertThat(fellowshipOfTheRing).contains(gandalf)
   *                                .doesNotContain(sauron);
   *
   * // ... but if we compare only races, Sauron is in fellowshipOfTheRing because he's a Maia like Gandalf.
   * assertThat(fellowshipOfTheRing).usingElementComparator(raceComparator)
   *                                .contains(sauron);</code></pre>
   *
   * @param elementComparator the comparator to use for incoming assertion checks.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super ELEMENT> elementComparator) {
    this.arrays = new ObjectArrays(new ComparatorBasedComparisonStrategy(elementComparator));
    this.iterables = new Iterables(new ComparatorBasedComparisonStrategy(elementComparator));
    // to have the same semantics on base assertions like isEqualTo, we need to use an iterable comparator comparing
    // elements with elementComparator parameter
    objects = new Objects(new ObjectArrayElementComparisonStrategy<>(elementComparator));
    return myself;
  }

  private SELF usingExtendedByTypesElementComparator(Comparator<Object> elementComparator) {
    return usingElementComparator(new ExtendedByTypesComparator(elementComparator, getComparatorsByType()));
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    this.arrays = ObjectArrays.instance();
    return myself;
  }

  /**
   * <u><b>Deprecated javadoc</b></u>
   * <p>
   * Allows to set a comparator to compare properties or fields of elements with the given names.
   * A typical usage is for comparing fields of numeric type at a given precision.
   * <p>
   * To be used, comparators need to be specified by this method <b>before</b> calling any of:
   * <ul>
   * <li>{@link #usingFieldByFieldElementComparator}</li>
   * <li>{@link #usingElementComparatorOnFields}</li>
   * <li>{@link #usingElementComparatorIgnoringFields}</li>
   * <li>{@link #usingRecursiveFieldByFieldElementComparator}</li>
   * </ul>
   * <p>
   * Comparators specified by this method have precedence over comparators specified by
   * {@link #usingComparatorForElementFieldsWithType(Comparator, Class) usingComparatorForElementFieldsWithType}.
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   private String name;
   *   private double height;
   *   // constructor omitted
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * Comparator&lt;Double&gt; closeEnough = new Comparator&lt;Double&gt;() {
   *   double precision = 0.5;
   *   public int compare(Double d1, Double d2) {
   *     return Math.abs(d1 - d2) &lt;= precision ? 0 : 1;
   *   }
   * };
   *
   * TolkienCharacter[] hobbits = new TolkienCharacter[] {frodo};
   *
   * // assertions will pass
   * assertThat(hobbits).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                    .usingFieldByFieldElementComparator()
   *                    .contains(tallerFrodo);
   *
   * assertThat(hobbits).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                    .usingElementComparatorOnFields(&quot;height&quot;)
   *                    .contains(tallerFrodo);
   *
   * assertThat(hobbits).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                    .usingElementComparatorIgnoringFields(&quot;name&quot;)
   *                    .contains(tallerFrodo);
   *
   * assertThat(hobbits).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                    .usingRecursiveFieldByFieldElementComparator()
   *                    .contains(tallerFrodo);
   *
   * // assertion will fail
   * assertThat(hobbits).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                    .usingFieldByFieldElementComparator()
   *                    .containsExactly(reallyTallFrodo);</code></pre>
   *
   * @param <C> the type of elements to compare.
   * @param comparator the {@link java.util.Comparator} to use
   * @param elementPropertyOrFieldNames the names of the properties and/or fields of the elements the comparator should be used for
   * @return {@code this} assertions object
   * @since 2.5.0 / 3.5.0
   * @deprecated This method is used with {@link #usingFieldByFieldElementComparator()} which is deprecated in favor of
   * {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)} or {@link #usingRecursiveComparison()}.
   * <p>
   * When using {@link #usingRecursiveComparison()} the equivalent is:
   * <ul>
   * <li>{@link RecursiveComparisonAssert#withEqualsForFields(java.util.function.BiPredicate, String...)}</li>
   * <li>{@link RecursiveComparisonAssert#withComparatorForFields(Comparator, String...)}</li>
   * </ul>
   * <p>
   * and when using {@link RecursiveComparisonConfiguration}:
   * <ul>
   * <li>{@link RecursiveComparisonConfiguration.Builder#withEqualsForFields(java.util.function.BiPredicate, String...)}</li>
   * <li>{@link RecursiveComparisonConfiguration.Builder#withComparatorForFields(Comparator, String...)}</li>
   * </ul>
   */
  @Deprecated
  @CheckReturnValue
  public <C> SELF usingComparatorForElementFieldsWithNames(Comparator<C> comparator,
                                                           String... elementPropertyOrFieldNames) {
    for (String elementPropertyOrField : elementPropertyOrFieldNames) {
      comparatorsForElementPropertyOrFieldNames.put(elementPropertyOrField, comparator);
    }
    return myself;
  }

  /**
   * <u><b>Deprecated javadoc</b></u>
   * <p>
   * Allows to set a specific comparator to compare properties or fields of elements with the given type.
   * A typical usage is for comparing fields of numeric type at a given precision.
   * <p>
   * To be used, comparators need to be specified by this method <b>before</b> calling any of:
   * <ul>
   * <li>{@link #usingFieldByFieldElementComparator()}</li>
   * <li>{@link #usingElementComparatorOnFields(String...)}</li>
   * <li>{@link #usingElementComparatorIgnoringFields(String...)}</li>
   * </ul>
   * <p>
   * Comparators specified by {@link #usingComparatorForElementFieldsWithNames(Comparator, String...) usingComparatorForElementFieldsWithNames}
   * have precedence over comparators specified by this method.
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   private String name;
   *   private double height;
   *   // constructor omitted
   * }
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * Comparator&lt;Double&gt; closeEnough = new Comparator&lt;Double&gt;() {
   *   double precision = 0.5;
   *   public int compare(Double d1, Double d2) {
   *     return Math.abs(d1 - d2) &lt;= precision ? 0 : 1;
   *   }
   * };
   *
   * TolkienCharacter[] hobbits = new TolkienCharacter[] {frodo};
   *
   * // assertions will pass
   * assertThat(hobbits).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                    .usingFieldByFieldElementComparator()
   *                    .contains(tallerFrodo);
   *
   * assertThat(hobbits).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                    .usingElementComparatorOnFields(&quot;height&quot;)
   *                    .contains(tallerFrodo);
   *
   * assertThat(hobbits).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                    .usingElementComparatorIgnoringFields(&quot;name&quot;)
   *                    .contains(tallerFrodo);
   *
   * assertThat(hobbits).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                    .usingRecursiveFieldByFieldElementComparator()
   *                    .contains(tallerFrodo);
   *
   * // assertion will fail
   * assertThat(hobbits).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                    .usingFieldByFieldElementComparator()
   *                    .contains(reallyTallFrodo);</code></pre>
   *
   * If multiple compatible comparators have been registered for a given {@code type}, the closest in the inheritance
   * chain to the given {@code type} is chosen in the following order:
   * <ol>
   * <li>The comparator for the exact given {@code type}</li>
   * <li>The comparator of a superclass of the given {@code type}</li>
   * <li>The comparator of an interface implemented by the given {@code type}</li>
   * </ol>
   *
   * @param <C> the type of elements to compare.
   * @param comparator the {@link java.util.Comparator} to use
   * @param type the {@link java.lang.Class} of the type of the element fields the comparator should be used for
   * @return {@code this} assertions object
   * @since 2.5.0 / 3.5.0
   * @deprecated This method is used with {@link #usingFieldByFieldElementComparator()} which is deprecated in favor of
   * {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)} or {@link #usingRecursiveComparison()}.
   * <p>
   * When using {@link #usingRecursiveComparison()} the equivalent is:
   * <ul>
   * <li>{@link RecursiveComparisonAssert#withEqualsForType(java.util.function.BiPredicate, Class)}</li>
   * <li>{@link RecursiveComparisonAssert#withComparatorForType(Comparator, Class)}</li>
   * </ul>
   * <p>
   * and when using {@link RecursiveComparisonConfiguration}:
   * <ul>
   * <li>{@link RecursiveComparisonConfiguration.Builder#withEqualsForType(java.util.function.BiPredicate, Class)}</li>
   * <li>{@link RecursiveComparisonConfiguration.Builder#withComparatorForType(Comparator, Class)}</li>
   * </ul>
   */
  @Deprecated
  @CheckReturnValue
  public <C> SELF usingComparatorForElementFieldsWithType(Comparator<C> comparator, Class<C> type) {
    getComparatorsForElementPropertyOrFieldTypes().registerComparator(type, comparator);
    return myself;
  }

  /**
   * Allows to set a specific comparator for the given type of elements or their fields.
   * Extends {@link #usingComparatorForElementFieldsWithType} by applying comparator specified for given type
   * to elements themselves, not only to their fields.
   * <p>
   * Usage of this method affects comparators set by the following methods:
   * <ul>
   * <li>{@link #usingFieldByFieldElementComparator()}</li>
   * <li>{@link #usingElementComparatorOnFields(String...)}</li>
   * <li>{@link #usingElementComparatorIgnoringFields(String...)}</li>
   * </ul>
   * <p>
   * Example:
   * <pre><code class='java'> Person obiwan = new Person("Obi-Wan");
   * obiwan.setHeight(new BigDecimal("1.820"));
   *
   * // assertion will pass
   * assertThat(obiwan).extracting("name", "height")
   *                   .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
   *                   .containsExactly("Obi-Wan", new BigDecimal("1.82"));</code></pre>
   *
   * @param <C> the type of elements to compare.
   * @param comparator the {@link java.util.Comparator} to use
   * @param type the {@link java.lang.Class} of the type of the element or element fields the comparator should be used for
   * @return {@code this} assertions object
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  public <C> SELF usingComparatorForType(Comparator<C> comparator, Class<C> type) {
    if (arrays.getComparator() == null) {
      usingElementComparator(new ExtendedByTypesComparator(getComparatorsByType()));
    }
    getComparatorsForElementPropertyOrFieldTypes().registerComparator(type, comparator);
    getComparatorsByType().registerComparator(type, comparator);
    return myself;
  }

  /**
   * <b><u>Deprecated javadoc</u></b>
   * <p>
   * Use field/property by field/property comparison (including inherited fields/properties) instead of relying on
   * actual type A <code>equals</code> method to compare group elements for incoming assertion checks. Private fields
   * are included but this can be disabled using {@link Assertions#setAllowExtractingPrivateFields(boolean)}.
   * <p>
   * This can be handy if <code>equals</code> method of the objects to compare does not suit you.
   * <p>
   * You can specify a custom comparator per name or type of element field with
   * {@link #usingComparatorForElementFieldsWithNames(Comparator, String...)}
   * and {@link #usingComparatorForElementFieldsWithType(Comparator, Class)}.
   * <p>
   * Note that the comparison is <b>not</b> recursive, if one of the fields/properties is an Object, it will be compared
   * to the other field/property using its <code>equals</code> method.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   *
   * // Fail if equals has not been overridden in TolkienCharacter as equals default implementation only compares references
   * assertThat(array(frodo)).contains(frodoClone);
   *
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(array(frodo)).usingFieldByFieldElementComparator().contains(frodoClone);</code></pre>
   *
   * @return {@code this} assertion object.
   * @deprecated This method is deprecated because it performs a <b>shallow</b> field by field comparison, i.e. elements are compared
   * field by field but the fields are compared with equals, use {@link #usingRecursiveFieldByFieldElementComparator()}
   * or {@link #usingRecursiveComparison()} instead to perform a true recursive comparison.
   * <br>See <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   */
  @Deprecated
  @CheckReturnValue
  public SELF usingFieldByFieldElementComparator() {
    return usingExtendedByTypesElementComparator(new FieldByFieldComparator(comparatorsForElementPropertyOrFieldNames,
                                                                            getComparatorsForElementPropertyOrFieldTypes()));
  }

  /**
   * Enable using a recursive field by field comparison strategy similar to {@link #usingRecursiveComparison()} but contrary to the latter <b>you can chain any iterable assertions after this method</b> (this is why this method exists).
   * <p>
   * This method uses the default {@link RecursiveComparisonConfiguration}, if you need to customize it use {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)} instead.
   * <p>
   * <b>Breaking change:</b> since 3.20.0 the comparison won't use any comparators set with:
   * <ul>
   *   <li>{@link #usingComparatorForType(Comparator, Class)}</li>
   *   <li>{@link #withTypeComparators(TypeComparators)}</li>
   *   <li>{@link #usingComparatorForElementFieldsWithType(Comparator, Class)}</li>
   *   <li>{@link #withComparatorsForElementPropertyOrFieldTypes(TypeComparators)}</li>
   *   <li>{@link #usingComparatorForElementFieldsWithNames(Comparator, String...)}</li>
   *   <li>{@link #withComparatorsForElementPropertyOrFieldNames(Map)}</li>
   * </ul>
   * <p>
   * These features (and many more) are provided through {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)} with a customized {@link RecursiveComparisonConfiguration} where there methods are called:
   * <ul>
   *   <li>{@link RecursiveComparisonConfiguration#registerComparatorForType(Comparator, Class) registerComparatorForType(Comparator, Class)} / {@link RecursiveComparisonConfiguration.Builder#withComparatorForType(Comparator, Class) withComparatorForType(Comparator, Class)} (using {@link RecursiveComparisonConfiguration.Builder})</li>
   *   <li>{@link RecursiveComparisonConfiguration#registerEqualsForType(java.util.function.BiPredicate, Class) registerEqualsForType(BiPredicate, Class)} / {@link RecursiveComparisonConfiguration.Builder#withComparatorForType(Comparator, Class) withComparatorForType(Comparator, Class)} (using {@link RecursiveComparisonConfiguration.Builder})</li>
   *   <li>{@link RecursiveComparisonConfiguration#registerComparatorForFields(Comparator, String...) registerComparatorForFields(Comparator comparator, String... fields)} / {@link RecursiveComparisonConfiguration.Builder#withComparatorForFields(Comparator, String...) withComparatorForField(Comparator comparator, String... fields)} (using {@link RecursiveComparisonConfiguration.Builder})</li>
   * </ul>
   * <p>
   * There are differences between this approach and {@link #usingRecursiveComparison()}:
   * <ul>
   *   <li>contrary to {@link RecursiveComparisonAssert}, you can chain any iterable assertions after this method.</li>
   *   <li>no comparators registered with {@link AbstractIterableAssert#usingComparatorForType(Comparator, Class)} will be used, you need to register them in the configuration object.</li>
   *   <li>the assertion errors won't be as detailed as {@link RecursiveComparisonAssert#isEqualTo(Object)} which shows the field differences.</li>
   * </ul>
   * <p>
   * This last point makes sense, take the {@link #contains(Object...)} assertion, it would not be relevant to report the differences of all the iterable's elements differing from the values to look for.
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   boolean hasPhd;
   * }
   *
   * public class Doctor {
   *  String name;
   *  boolean hasPhd;
   * }
   *
   * Doctor drSheldon = new Doctor("Sheldon Cooper", true);
   * Doctor drLeonard = new Doctor("Leonard Hofstadter", true);
   * Doctor drRaj = new Doctor("Raj Koothrappali", true);
   *
   * Person sheldon = new Person("Sheldon Cooper", true);
   * Person leonard = new Person("Leonard Hofstadter", true);
   * Person raj = new Person("Raj Koothrappali", true);
   * Person howard = new Person("Howard Wolowitz", true);
   *
   * Doctor[] doctors = array(drSheldon, drLeonard, drRaj);
   * Person[] people = array(sheldon, leonard, raj);
   *
   * // assertion succeeds as both lists contains equivalent items in order.
   * assertThat(doctors).usingRecursiveFieldByFieldElementComparator()
   *                    .contains(sheldon);
   *
   * // assertion fails because leonard names are different.
   * leonard.setName("Leonard Ofstater");
   * assertThat(doctors).usingRecursiveFieldByFieldElementComparator()
   *                    .contains(leonard);
   *
   * // assertion fails because howard is missing and leonard is not expected.
   * people = list(howard, sheldon, raj)
   * assertThat(doctors).usingRecursiveFieldByFieldElementComparator()
   *                    .contains(howard);</code></pre>
   * <p>
   * Another point worth mentioning: <b>elements order does matter if the expected iterable is ordered</b>, for example comparing a {@code Set<Person>} to a {@code List<Person>} fails as {@code List} is ordered and {@code Set} is not.<br>
   * The ordering can be ignored by calling {@link RecursiveComparisonAssert#ignoringCollectionOrder ignoringCollectionOrder} allowing ordered/unordered iterable comparison, note that {@link RecursiveComparisonAssert#ignoringCollectionOrder ignoringCollectionOrder} is applied recursively on any nested iterable fields, if this behavior is too generic,
   * use the more fine grained {@link RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...) ignoringCollectionOrderInFields} or
   * {@link RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...) ignoringCollectionOrderInFieldsMatchingRegexes}.
   *
   * @return {@code this} assertion object.
   * @see RecursiveComparisonConfiguration
   * @see #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)
   * @since 2.5.0 / 3.5.0 - breaking change in 3.20.0
   */
  @CheckReturnValue
  public SELF usingRecursiveFieldByFieldElementComparator() {
    return usingRecursiveFieldByFieldElementComparator(new RecursiveComparisonConfiguration());
  }

  /**
   * Enable using a recursive field by field comparison strategy similar to {@link #usingRecursiveComparison()} but contrary to the latter <b>you can chain any iterable assertions after this method</b> (this is why this method exists).
   * <p>
   * The given {@link RecursiveComparisonConfiguration} is used to tweak the comparison behavior, for example by {@link RecursiveComparisonConfiguration#ignoreCollectionOrder(boolean) ignoring collection order}.
   * <p>
   * <b>Warning:</b> the comparison won't use any comparators set with:
   * <ul>
   *   <li>{@link #usingComparatorForType(Comparator, Class)}</li>
   *   <li>{@link #withTypeComparators(TypeComparators)}</li>
   *   <li>{@link #usingComparatorForElementFieldsWithType(Comparator, Class)}</li>
   *   <li>{@link #withComparatorsForElementPropertyOrFieldTypes(TypeComparators)}</li>
   *   <li>{@link #usingComparatorForElementFieldsWithNames(Comparator, String...)}</li>
   *   <li>{@link #withComparatorsForElementPropertyOrFieldNames(Map)}</li>
   * </ul>
   * <p>
   * These features (and many more) are provided through {@link RecursiveComparisonConfiguration} with:
   * <ul>
   *   <li>{@link RecursiveComparisonConfiguration#registerComparatorForType(Comparator, Class) registerComparatorForType(Comparator, Class)} / {@link RecursiveComparisonConfiguration.Builder#withComparatorForType(Comparator, Class) withComparatorForType(Comparator, Class)} (using {@link RecursiveComparisonConfiguration.Builder})</li>
   *   <li>{@link RecursiveComparisonConfiguration#registerEqualsForType(java.util.function.BiPredicate, Class) registerEqualsForType(BiPredicate, Class)} / {@link RecursiveComparisonConfiguration.Builder#withComparatorForType(Comparator, Class) withComparatorForType(Comparator, Class)} (using {@link RecursiveComparisonConfiguration.Builder})</li>
   *   <li>{@link RecursiveComparisonConfiguration#registerComparatorForFields(Comparator, String...) registerComparatorForFields(Comparator comparator, String... fields)} / {@link RecursiveComparisonConfiguration.Builder#withComparatorForFields(Comparator, String...) withComparatorForField(Comparator comparator, String... fields)} (using {@link RecursiveComparisonConfiguration.Builder})</li>
   * </ul>
   * <p>
   * RecursiveComparisonConfiguration exposes a {@link RecursiveComparisonConfiguration.Builder builder} to ease setting the comparison behaviour,
   * call {@link RecursiveComparisonConfiguration#builder() RecursiveComparisonConfiguration.builder()} to start building your configuration.
   * <p>
   * There are differences between this approach and {@link #usingRecursiveComparison()}:
   * <ul>
   *   <li>contrary to {@link RecursiveComparisonAssert}, you can chain any iterable assertions after this method.</li>
   *   <li>no comparators registered with {@link AbstractIterableAssert#usingComparatorForType(Comparator, Class)} will be used, you need to register them in the configuration object.</li>
   *   <li>the assertion errors won't be as detailed as {@link RecursiveComparisonAssert#isEqualTo(Object)} which shows the field differences.</li>
   * </ul>
   * <p>
   * This last point makes sense, take the {@link #contains(Object...)} assertion, it would not be relevant to report the differences of all the iterable's elements differing from the values to look for.
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   boolean hasPhd;
   * }
   *
   * public class Doctor {
   *  String name;
   *  boolean hasPhd;
   * }
   *
   * Doctor drSheldon = new Doctor("Sheldon Cooper", true);
   * Doctor drLeonard = new Doctor("Leonard Hofstadter", true);
   * Doctor drRaj = new Doctor("Raj Koothrappali", true);
   *
   * Person sheldon = new Person("Sheldon Cooper", false);
   * Person leonard = new Person("Leonard Hofstadter", false);
   * Person raj = new Person("Raj Koothrappali", false);
   * Person howard = new Person("Howard Wolowitz", false);
   *
   * Doctor[] doctors = array(drSheldon, drLeonard, drRaj);
   * Person[] people = array(sheldon, leonard, raj);
   *
   * RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
   *                                                                                  .withIgnoredFields​("hasPhd");
   *
   * // assertion succeeds as both lists contains equivalent items in order.
   * assertThat(doctors).usingRecursiveFieldByFieldElementComparator(configuration)
   *                    .contains(sheldon);
   *
   * // assertion fails because leonard names are different.
   * leonard.setName("Leonard Ofstater");
   * assertThat(doctors).usingRecursiveFieldByFieldElementComparator(configuration)
   *                    .contains(leonard);
   *
   * // assertion fails because howard is missing and leonard is not expected.
   * people = list(howard, sheldon, raj)
   * assertThat(doctors).usingRecursiveFieldByFieldElementComparator(configuration)
   *                    .contains(howard);</code></pre>
   *
   * A detailed documentation for the recursive comparison is available here: <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>.
   * <p>
   * A point worth mentioning: <b>elements order does matter if the expected iterable is ordered</b>, for example comparing a {@code Set<Person>} to a {@code List<Person>} fails as {@code List} is ordered and {@code Set} is not.<br>
   * The ordering can be ignored by calling {@link RecursiveComparisonAssert#ignoringCollectionOrder ignoringCollectionOrder} allowing ordered/unordered iterable comparison, note that {@link RecursiveComparisonAssert#ignoringCollectionOrder ignoringCollectionOrder} is applied recursively on any nested iterable fields, if this behavior is too generic,
   * use the more fine grained {@link RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...) ignoringCollectionOrderInFields} or
   * {@link RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...) ignoringCollectionOrderInFieldsMatchingRegexes}.
   *
   * @param configuration the recursive comparison configuration.
   *
   * @return {@code this} assertion object.
   * @see RecursiveComparisonConfiguration
   * @since 3.20.0
   */
  public SELF usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration configuration) {
    return usingElementComparator(new ConfigurableRecursiveFieldByFieldComparator(configuration));
  }

  /**
   * <b><u>Deprecated javadoc</u></b>
   * <p>
   * Use field/property by field/property comparison on the <b>given fields/properties only</b> (including inherited
   * fields/properties) instead of relying on actual type A <code>equals</code> method to compare group elements for
   * incoming assertion checks. Private fields are included but this can be disabled using
   * {@link Assertions#setAllowExtractingPrivateFields(boolean)}.
   * <p>
   * This can be handy if <code>equals</code> method of the objects to compare does not suit you.
   * <p>
   * You can specify a custom comparator per name or type of element field with
   * {@link #usingComparatorForElementFieldsWithNames(Comparator, String...)}
   * and {@link #usingComparatorForElementFieldsWithType(Comparator, Class)}.
   * <p>
   * Note that the comparison is <b>not</b> recursive, if one of the fields/properties is an Object, it will be compared
   * to the other field/property using its <code>equals</code> method.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   *
   * // frodo and sam both are hobbits, so they are equals when comparing only race
   * assertThat(array(frodo)).usingElementComparatorOnFields("race").contains(sam); // OK
   *
   * // ... but not when comparing both name and race
   * assertThat(array(frodo)).usingElementComparatorOnFields("name", "race").contains(sam); // FAIL</code></pre>
   *
   * @param fields the name of the fields to use the element comparator on
   * @return {@code this} assertion object.
   * @see #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)
   * @see <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   * @deprecated This method is deprecated because it performs a <b>shallow</b> field by field comparison, i.e. elements are
   * compared field by field but the fields are compared with equals, use {@link #usingRecursiveFieldByFieldElementComparatorOnFields(String...)} instead.
   * <br>See <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   */
  @Deprecated
  @CheckReturnValue
  public SELF usingElementComparatorOnFields(String... fields) {
    return usingExtendedByTypesElementComparator(new OnFieldsComparator(comparatorsForElementPropertyOrFieldNames,
                                                                        getComparatorsForElementPropertyOrFieldTypes(),
                                                                        fields));
  }

  /**
   * The assertions chained after this method will use a recursive field by field comparison on the given fields (including
   * inherited fields) instead of relying on the element <code>equals</code> method.
   * This is handy when the element <code>equals</code> method is not overridden or implemented as you expect.
   * <p>
   * Nested fields are supported and are expressed like: {@code name.first}
   * <p>
   * The comparison is <b>recursive</b>: elements are compared field by field, if a field type has fields they are also compared
   * field by field (and so on).
   * <p>
   * Example:
   * <pre><code class='java'> Player derrickRose = new Player(new Name("Derrick", "Rose"), "Chicago Bulls");
   * derrickRose.nickname = new Name("Crazy", "Dunks");
   *
   * Player jalenRose = new Player(new Name("Jalen", "Rose"), "Chicago Bulls");
   * jalenRose.nickname = new Name("Crazy", "Defense");
   *
   * // assertion succeeds as all compared fields match
   * assertThat(array(derrickRose)).usingRecursiveFieldByFieldElementComparatorOnFields("name.last", "team", "nickname.first")
   *                               .contains(jalenRose);
   *
   * // assertion fails, name.first values differ
   * assertThat(array(derrickRose)).usingRecursiveFieldByFieldElementComparatorOnFields("name")
   *                               .contains(jalenRose);</code></pre>
   * <p>
   * This method is actually a shortcut of {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)}
   * with a configuration comparing only the given fields, the previous example can be written as:
   * <pre><code class='java'> RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
   *                                                                                  .withComparedFields("name.last", "team", "nickname.first")
   *                                                                                  .build();
   *
   * assertThat(array(derrickRose)).usingRecursiveFieldByFieldElementComparator(configuration)
   *                               .contains(jalenRose);</code></pre>
   * The recursive comparison is documented here: <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   * <p>
   * @param fields the field names to exclude in the elements comparison.
   * @return {@code this} assertion object.
   * @see #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)
   * @see <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   * @since 3.20.0
   */
  @CheckReturnValue
  public SELF usingRecursiveFieldByFieldElementComparatorOnFields(String... fields) {
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = RecursiveComparisonConfiguration.builder()
                                                                                                        .withComparedFields(fields)
                                                                                                        .build();
    return usingRecursiveFieldByFieldElementComparator(recursiveComparisonConfiguration);
  }

  /**
   * <b><u>Deprecated javadoc</u></b>
   * <p>
   * Use field/property by field/property on all fields/properties <b>except</b> the given ones (including inherited
   * fields/properties) instead of relying on actual type A <code>equals</code> method to compare group elements for
   * incoming assertion checks. Private fields are included but this can be disabled using
   * {@link Assertions#setAllowExtractingPrivateFields(boolean)}.
   * <p>
   * This can be handy if <code>equals</code> method of the objects to compare does not suit you.
   * <p>
   * You can specify a custom comparator per name or type of element field with
   * {@link #usingComparatorForElementFieldsWithNames(Comparator, String...)}
   * and {@link #usingComparatorForElementFieldsWithType(Comparator, Class)}.
   * <p>
   * Note that the comparison is <b>not</b> recursive, if one of the fields/properties is an Object, it will be compared
   * to the other field/property using its <code>equals</code> method.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   *
   * // frodo and sam both are hobbits, so they are equals when comparing only race (i.e. ignoring all other fields)
   * assertThat(array(frodo)).usingElementComparatorIgnoringFields("name", "age").contains(sam); // OK
   *
   * // ... but not when comparing both name and race
   * assertThat(array(frodo)).usingElementComparatorIgnoringFields("age").contains(sam); // FAIL</code></pre>
   *
   * @param fields the name of the fields to ignore
   * @return {@code this} assertion object.
   * @see #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)
   * @see <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   * @deprecated This method is deprecated because it performs a <b>shallow</b> field by field comparison, i.e. elements are
   * compared field by field but the fields are compared with equals, use {@link #usingRecursiveFieldByFieldElementComparatorIgnoringFields(String...)} instead.
   * <br>See <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   */
  @Deprecated
  @CheckReturnValue
  public SELF usingElementComparatorIgnoringFields(String... fields) {
    return usingExtendedByTypesElementComparator(new IgnoringFieldsComparator(comparatorsForElementPropertyOrFieldNames,
                                                                              getComparatorsForElementPropertyOrFieldTypes(),
                                                                              fields));
  }

  /**
   * The assertions chained after this method will use a recursive field by field comparison on all fields (including inherited
   * fields) <b>except</b> the given ones instead of relying on the element <code>equals</code> method.
   * This is handy when the element <code>equals</code> method is not overridden or implemented as you expect.
   * <p>
   * Nested fields are supported and are expressed like: {@code name.first}
   * <p>
   * The comparison is <b>recursive</b>: elements are compared field by field, if a field type has fields they are also compared
   * field by field (and so on).
   * <p>
   * Example:
   * <pre><code class='java'> Player derrickRose = new Player(new Name("Derrick", "Rose"), "Chicago Bulls");
   * derrickRose.nickname = new Name("Crazy", "Dunks");
   *
   * Player jalenRose = new Player(new Name("Jalen", "Rose"), "Chicago Bulls");
   * jalenRose.nickname = new Name("Crazy", "Defense");
   *
   * // assertion succeeds
   * assertThat(array(derrickRose)).usingRecursiveFieldByFieldElementComparatorIgnoringFields("name.first", "nickname.last")
   *                               .contains(jalenRose);
   *
   * // assertion fails, names are ignored but nicknames are not and nickname.last values differ
   * assertThat(array(derrickRose)).usingRecursiveFieldByFieldElementComparatorIgnoringFields("name")
   *                               .contains(jalenRose);</code></pre>
   * <p>
   * This method is actually a shortcut of {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)}
   * with a configuration ignoring the given fields, the previous example can be written as:
   * <pre><code class='java'> RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
   *                                                                                  .withIgnoredFields("name.first", "nickname.last")
   *                                                                                  .build();
   *
   * assertThat(array(derrickRose)).usingRecursiveFieldByFieldElementComparator(configuration)
   *                               .contains(jalenRose);</code></pre>
   * The recursive comparison is documented here: <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   * <p>
   * @param fields the field names to exclude in the elements comparison.
   * @return {@code this} assertion object.
   * @see #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)
   * @see <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   * @since 3.20.0
   */
  @CheckReturnValue
  public SELF usingRecursiveFieldByFieldElementComparatorIgnoringFields(String... fields) {
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = RecursiveComparisonConfiguration.builder()
                                                                                                        .withIgnoredFields(fields)
                                                                                                        .build();
    return usingRecursiveFieldByFieldElementComparator(recursiveComparisonConfiguration);
  }

  /**
   * Extract the values of given field or property from the array's elements under test into a new list, this new list
   * becoming the object under test.
   * <p>
   * It allows you to test a field/property of the array's elements instead of testing the elements themselves, which can
   * be much less work !
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build a array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
   * // they can be public field or properties, both works when extracting their values.
   * TolkienCharacter[] fellowshipOfTheRing = new TolkienCharacter[] {
   *   new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT),
   *   new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT),
   *   new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA),
   *   new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF),
   *   new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT),
   *   new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF),
   *   new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN,
   *   new TolkienCharacter(&quot;Boromir&quot;, 37, MAN)
   * };
   *
   * // let's verify the names of TolkienCharacter in fellowshipOfTheRing :
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // you can also extract nested field/property like the name of Race :
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;race.name&quot;)
   *                                .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *                                .doesNotContain(&quot;Orc&quot;);</code></pre>
   *
   * A property with the given name is looked for first, if it does not exist then a field with the given name
   * is looked for.
   * <p>
   * Note that the order of extracted field/property values is consistent with the array order.
   *
   * @param fieldOrProperty the field/property to extract from the array under test
   * @return a new assertion object whose object under test is the list of extracted field/property values.
   * @throws IntrospectionError if no field or property exists with the given name
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> extracting(String fieldOrProperty) {
    Object[] values = FieldsOrPropertiesExtractor.extract(actual, byName(fieldOrProperty));
    String extractedDescription = extractedDescriptionOf(fieldOrProperty);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(newArrayList(values)).withAssertionState(myself).as(description);
  }

  /**
   * Extract the values of given field or property from the array's elements under test into a new list, this new list of the provided type
   * becoming the object under test.
   * <p>
   * It allows you to test a field/property of the array's elements instead of testing the elements themselves, which can
   * be much less work !
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build an array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
   * // they can be public field or properties, both works when extracting their values.
   * TolkienCharacter[] fellowshipOfTheRing = new TolkienCharacter[] {
   *   new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT),
   *   new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT),
   *   new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA),
   *   new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF),
   *   new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT),
   *   new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF),
   *   new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN,
   *   new TolkienCharacter(&quot;Boromir&quot;, 37, MAN)
   * };
   *
   * // let's verify the names of TolkienCharacter in fellowshipOfTheRing :
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, String.class)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // you can also extract nested field/property like the name of Race :
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;race.name&quot;, String.class)
   *                                .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *                                .doesNotContain(&quot;Orc&quot;);</code></pre>
   *
   * A property with the given name is looked for first, if it does not exist then a field with the given name
   * is looked for.
   * <p>
   * Note that the order of extracted field/property values is consistent with the order of the array under test.
   *
   * @param <P> the type of elements to extract.
   * @param fieldOrProperty the field/property to extract from the array under test
   * @param extractingType type to return
   * @return a new assertion object whose object under test is the list of extracted field/property values.
   * @throws IntrospectionError if no field or property exists with the given name
   */
  @CheckReturnValue
  public <P> AbstractListAssert<?, List<? extends P>, P, ObjectAssert<P>> extracting(String fieldOrProperty,
                                                                                     Class<P> extractingType) {
    @SuppressWarnings("unchecked")
    List<P> values = (List<P>) FieldsOrPropertiesExtractor.extract(Arrays.asList(actual), byName(fieldOrProperty));
    String extractedDescription = extractedDescriptionOf(fieldOrProperty);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(values).withAssertionState(myself).as(description);
  }

  /**
   * Extract the values of given fields/properties from the array's elements under test into a list composed of
   * Tuple (a simple data structure), this new list becoming the object under test.
   * <p>
   * It allows you to test fields/properties of the array's elements instead of testing the elements themselves, it
   * can be sometimes much less work !
   * <p>
   * The Tuple data corresponds to the extracted values of the given fields/properties, for instance if you ask to
   * extract "id", "name" and "email" then each Tuple data will be composed of id, name and email extracted from the
   * element of the initial array (the Tuple's data order is the same as the given fields/properties order).
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build an array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
   * // they can be public field or properties, both works when extracting their values.
   * TolkienCharacter[] fellowshipOfTheRing = new TolkienCharacter[] {
   *   new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT),
   *   new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT),
   *   new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA),
   *   new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF),
   *   new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT),
   *   new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF),
   *   new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN,
   *   new TolkienCharacter(&quot;Boromir&quot;, 37, MAN)
   * };
   *
   * // let's verify 'name' and 'age' of some TolkienCharacter in fellowshipOfTheRing :
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, &quot;age&quot;)
   *                                .contains(tuple(&quot;Boromir&quot;, 37),
   *                                          tuple(&quot;Sam&quot;, 38),
   *                                          tuple(&quot;Legolas&quot;, 1000));
   *
   *
   * // extract 'name', 'age' and Race name values.
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, &quot;age&quot;, &quot;race.name&quot;)
   *                                .contains(tuple(&quot;Boromir&quot;, 37, &quot;Man&quot;),
   *                                          tuple(&quot;Sam&quot;, 38, &quot;Hobbit&quot;),
   *                                          tuple(&quot;Legolas&quot;, 1000, &quot;Elf&quot;));</code></pre>
   *
   * A property with the given name is looked for first, if it does not exist the a field with the given name is
   * looked for.
   * <p>
   * Note that the order of extracted property/field values is consistent with the iteration order of the array under
   * test.
   *
   * @param propertiesOrFields the properties/fields to extract from the initial array under test
   * @return a new assertion object whose object under test is the list of Tuple with extracted properties/fields values as data.
   * @throws IntrospectionError if one of the given name does not match a field or property in one of the initial Iterable's element.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extracting(String... propertiesOrFields) {
    List<Tuple> values = FieldsOrPropertiesExtractor.extract(Arrays.asList(actual), byName(propertiesOrFields));
    String extractedDescription = extractedDescriptionOf(propertiesOrFields);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(values).withAssertionState(myself).as(description);
  }

  /**
   * Extract the values from the array's elements by applying an extracting function on them, the resulting list becomes
   * the new object under test.
   * <p>
   * This method is similar to {@link #extracting(String)} but more refactoring friendly as it does not use introspection.
   * <p>
   * Let's take a look an example:
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * TolkienCharacter[] fellowshipOfTheRing = new TolkienCharacter[] {
   *   new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT),
   *   new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT),
   *   new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA),
   *   new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF),
   *   new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT),
   *   new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF),
   *   new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN,
   *   new TolkienCharacter(&quot;Boromir&quot;, 37, MAN)
   * };
   *
   * // fellowship has hobbitses, right, my presioussss?
   * assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getRace).contains(HOBBIT);</code></pre>
   *
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   *
   * @param <U> the type of elements to extract.
   * @param extractor the object transforming input object to desired one
   * @return a new assertion object whose object under test is the list of extracted values.
   */
  @CheckReturnValue
  public <U> AbstractListAssert<?, List<? extends U>, U, ObjectAssert<U>> extracting(Function<? super ELEMENT, U> extractor) {
    List<U> values = FieldsOrPropertiesExtractor.extract(Arrays.asList(actual), extractor);
    return newListAssertInstance(values).withAssertionState(myself);
  }

  /**
   * Extract the values from the array's elements by applying an extracting function (which might throw an exception)
   * on them, the resulting list of extracted values becomes a new object under test.
   * <p>
   * Any checked exception raised in the extractor is rethrown wrapped in a {@link RuntimeException}.
   * <p>
   * It allows to test values from the elements in safer way than by using {@link #extracting(String)}, as it
   * doesn't use introspection.
   * <p>
   * Let's take a look an example:
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * TolkienCharacter[] fellowshipOfTheRing = new TolkienCharacter[] {
   *   new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT),
   *   new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT),
   *   new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA),
   *   new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF),
   *   new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT),
   *   new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF),
   *   new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN,
   *   new TolkienCharacter(&quot;Boromir&quot;, 37, MAN)
   * };
   *
   * assertThat(fellowshipOfTheRing).extracting(input -&gt; {
   *   if (input.getAge() &lt; 20) {
   *     throw new Exception("age &lt; 20");
   *   }
   *   return input.getName();
   * }).contains("Frodo");</code></pre>
   * <p>
   * Note that the order of extracted property/field values is consistent with the iteration order of the array under test.
   *
   * @param <V> the type of elements to extract.
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param extractor the object transforming input object to desired one
   * @return a new assertion object whose object under test is the list of extracted values
   * @since 3.7.0
   */
  @CheckReturnValue
  public <V, EXCEPTION extends Exception> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> extracting(ThrowingExtractor<? super ELEMENT, V, EXCEPTION> extractor) {
    List<V> values = FieldsOrPropertiesExtractor.extract(newArrayList(actual), extractor);
    return newListAssertInstance(values).withAssertionState(myself);
  }

  /**
   * Use the given {@link Function}s to extract the values from the array's elements into a new list
   * composed of {@link Tuple}s (a simple data structure containing the extracted values), this new list becoming the
   * object under test.
   * <p>
   * It allows you to test values from the array's elements instead of testing the elements themselves, which sometimes can be
   * much less work!
   * <p>
   * The {@link Tuple} data corresponds to the extracted values from the arrays's elements, for instance if you pass functions
   * extracting "id", "name" and "email" values then each {@link Tuple}'s data will be composed of an id, a name and an email
   * extracted from the element of the initial array (the Tuple's data order is the same as the given functions order).
   * <p>
   * Let's take a look at an example to make things clearer :
   * <pre><code class='java'> // Build an array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
   * // they can be public field or properties, both works when extracting their values.
   * TolkienCharacter[] fellowshipOfTheRing = new TolkienCharacter[] {
   *   new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT),
   *   new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT),
   *   new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA),
   *   new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF),
   *   new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT),
   *   new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF),
   *   new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN,
   *   new TolkienCharacter(&quot;Boromir&quot;, 37, MAN)
   * };
   *
   * // let's verify 'name', 'age' and Race of some TolkienCharacter in fellowshipOfTheRing :
   * assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName,
   *                                            character -&gt; character.getAge(),
   *                                            TolkienCharacter::getRace)
   *                                .containsOnly(tuple(&quot;Frodo&quot;, 33, HOBBIT),
   *                                              tuple(&quot;Sam&quot;, 38, HOBBIT),
   *                                              tuple(&quot;Gandalf&quot;, 2020, MAIA),
   *                                              tuple(&quot;Legolas&quot;, 1000, ELF),
   *                                              tuple(&quot;Pippin&quot;, 28, HOBBIT),
   *                                              tuple(&quot;Gimli&quot;, 139, DWARF),
   *                                              tuple(&quot;Aragorn&quot;, 87, MAN),
   *                                              tuple(&quot;Boromir&quot;, 37, MAN));</code></pre>
   * You can use lambda expression or a method reference to extract the expected values.
   * <p>
   * Use {@link Tuple#tuple(Object...)} to initialize the expected values.
   * <p>
   * Note that the order of the extracted tuples list is consistent with the iteration order of the array under test,
   * for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted tuples order.
   *
   * @param extractors the extractor functions to extract a value from an element of the array under test.
   * @return a new assertion object whose object under test is the list of Tuples containing the extracted values.
   */
  @CheckReturnValue
  @SafeVarargs
  public final AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extracting(Function<? super ELEMENT, ?>... extractors) {
    return extractingForProxy(extractors);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extractingForProxy(Function<? super ELEMENT, ?>[] extractors) {

    Function<ELEMENT, Tuple> tupleExtractor = objectToExtractValueFrom -> new Tuple(Stream.of(extractors)
                                                                                          .map(extractor -> extractor.apply(objectToExtractValueFrom))
                                                                                          .toArray());
    List<Tuple> tuples = stream(actual).map(tupleExtractor).collect(toList());
    return newListAssertInstance(tuples).withAssertionState(myself);
  }

  /**
   * Extract the Iterable values from arrays elements under test by applying an Iterable extracting function on them
   * and concatenating the result lists into an array which becomes the new object under test.
   * <p>
   * It allows testing the results of extracting values that are represented by Iterables.
   * <p>
   * For example:
   * <pre><code class='java'> CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.addChildren(bart, lisa, maggie);
   *
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   *
   * CartoonCharacter[] parents = new CartoonCharacter[] { homer, fred };
   * // check children
   * assertThat(parents).flatExtracting(CartoonCharacter::getChildren)
   *                    .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The order of extracted values is consisted with both the order of the collection itself, as well as the extracted
   * collections.
   *
   * @param <V> the type of elements to extract.
   * @param <C> the type of collection to flat/extract.
   * @param extractor the object transforming input object to an Iterable of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   */
  @CheckReturnValue
  public <V, C extends Collection<V>> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> flatExtracting(Function<? super ELEMENT, C> extractor) {
    return doFlatExtracting(extractor);
  }

  /**
   * Extract the Iterable values from arrays elements under test by applying an Iterable extracting function (which
   * might throw an exception) on them and concatenating the result lists into an array which becomes the new object
   * under test.
   * <p>
   * It allows testing the results of extracting values that are represented by Iterables.
   * <p>
   * For example:
   * <pre><code class='java'> CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.addChildren(bart, lisa, maggie);
   *
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   *
   * CartoonCharacter[] parents = new CartoonCharacter[] { homer, fred };
   * // check children
   * assertThat(parents).flatExtracting(input -&gt; {
   *   if (input.getChildren().size() == 0) {
   *     throw new Exception("no children");
   *   }
   *   return input.getChildren();
   * }).containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The order of extracted values is consisted with both the order of the collection itself, as well as the extracted
   * collections.
   *
   * @param <V> the type of elements to extract.
   * @param <C> the type of collection to flat/extract.
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param extractor the object transforming input object to an Iterable of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.7.0
   */
  @CheckReturnValue
  public <V, C extends Collection<V>, EXCEPTION extends Exception> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> flatExtracting(ThrowingExtractor<? super ELEMENT, C, EXCEPTION> extractor) {
    return doFlatExtracting(extractor);
  }

  private <V, C extends Collection<V>> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> doFlatExtracting(Function<? super ELEMENT, C> extractor) {
    List<V> result = FieldsOrPropertiesExtractor.extract(Arrays.asList(actual), extractor).stream()
                                                .flatMap(Collection::stream).collect(toList());
    return newListAssertInstance(result).withAssertionState(myself);
  }

  /**
   * Extract from array's elements the Iterable/Array values corresponding to the given property/field name and
   * concatenate them into a single array becoming the new object under test.
   * <p>
   * It allows testing the elements of extracting values that are represented by iterables or arrays.
   * <p>
   * For example:
   * <pre><code class='java'> CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.addChildren(bart, lisa, maggie);
   *
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   *
   * CartoonCharacter[] parents = new CartoonCharacter[] { homer, fred };
   * // check children
   * assertThat(parents).flatExtracting("children")
   *                    .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The order of extracted values is consisted with both the order of the collection itself, as well as the extracted
   * collections.
   *
   * @param propertyName the object transforming input object to an Iterable of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @throws IllegalArgumentException if one of the extracted property value was not an array or an iterable.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatExtracting(String propertyName) {
    List<Object> extractedValues = newArrayList();
    List<?> extractedGroups = FieldsOrPropertiesExtractor.extract(Arrays.asList(actual), byName(propertyName));
    for (Object group : extractedGroups) {
      // expecting group to be an iterable or an array
      if (isArray(group)) {
        int size = Array.getLength(group);
        for (int i = 0; i < size; i++) {
          extractedValues.add(Array.get(group, i));
        }
      } else if (group instanceof Iterable) {
        Iterable<?> iterable = (Iterable<?>) group;
        for (Object value : iterable) {
          extractedValues.add(value);
        }
      } else {
        CommonErrors.wrongElementTypeForFlatExtracting(group);
      }
    }
    return newListAssertInstance(extractedValues).withAssertionState(myself);
  }

  /**
   * Extract the result of given method invocation from the array's elements under test into a list, this list becoming
   * the object under test.
   * <p>
   * It allows you to test a method results of the array's elements instead of testing the elements themselves, which can be
   * much less work!
   * <p>
   * It is especially useful for classes that does not conform to the Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   * WesterosHouse[] greatHousesOfWesteros = new WesterosHouse[] { new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Coming&quot;),
   *     new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;), new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;),
   *     new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;), new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;),
   *     new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;) };
   *
   * // let's verify the words of the great houses of Westeros:
   * assertThat(greatHousesOfWesteros).extractingResultOf(&quot;sayTheWords&quot;)
   *                                  .contains(&quot;Winter is Coming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *                                  .doesNotContain(&quot;Lannisters always pay their debts&quot;);</code></pre>
   *
   * <p>
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method cannot accept any arguments,</li>
   * <li>method cannot return void.</li>
   * </ul>
   * <p>
   * Note that the order of extracted values is consistent with the order of the array under test.
   *
   * @param method the name of the method which result is to be extracted from the array under test
   * @return a new assertion object whose object under test is the list of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void, or method accepts arguments.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> extractingResultOf(String method) {
    Object[] values = FieldsOrPropertiesExtractor.extract(actual, resultOf(method));
    String extractedDescription = extractedDescriptionOfMethod(method);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(newArrayList(values)).withAssertionState(myself).as(description);
  }

  /**
   * Extract the result of given method invocation from the array's elements under test into a list, this list becoming
   * the object under test.
   * <p>
   * It allows you to test a method results of the array's elements instead of testing the elements themselves, which can be
   * much less work!
   * <p>
   * It is especially useful for classes that do not conform to the Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   * WesterosHouse[] greatHousesOfWesteros = new WesterosHouse[] { new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Coming&quot;),
   *     new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;), new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;),
   *     new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;), new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;),
   *     new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;) };
   *
   * // let's verify the words of the great houses of Westeros:
   * assertThat(greatHousesOfWesteros).extractingResultOf(&quot;sayTheWords&quot;, String.class)
   *                                  .contains(&quot;Winter is Coming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *                                  .doesNotContain(&quot;Lannisters always pay their debts&quot;);</code></pre>
   *
   * <p>
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method can not accept any arguments,</li>
   * <li>method can not return void.</li>
   * </ul>
   * <p>
   * Note that the order of extracted values is consistent with the order of the array under test.
   *
   * @param <P> the type of elements extracted.
   * @param method the name of the method which result is to be extracted from the array under test
   * @param extractingType type to return
   * @return a new assertion object whose object under test is the list of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void, or method accepts arguments.
   */
  @CheckReturnValue
  public <P> AbstractListAssert<?, List<? extends P>, P, ObjectAssert<P>> extractingResultOf(String method,
                                                                                             Class<P> extractingType) {
    @SuppressWarnings("unchecked")
    P[] values = (P[]) FieldsOrPropertiesExtractor.extract(actual, resultOf(method));
    String extractedDescription = extractedDescriptionOfMethod(method);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(newArrayList(values)).withAssertionState(myself).as(description);
  }

  /**
   * Enable hexadecimal object representation of Iterable elements instead of standard java representation in error
   * messages.
   * <p>
   * It can be useful to better understand what the error was with a more meaningful error message.
   * <p>
   * Example
   * <pre><code class='java'> assertThat(new Byte[] { 0x10, 0x20 }).inHexadecimal().contains(new Byte[] { 0x30 });</code></pre>
   *
   * With standard error message:
   * <pre><code class='java'> Expecting:
   *  &lt;[16, 32]&gt;
   * to contain:
   *  &lt;[48]&gt;
   * but could not find:
   *  &lt;[48]&gt;</code></pre>
   *
   * With Hexadecimal error message:
   * <pre><code class='java'> Expecting:
   *  &lt;[0x10, 0x20]&gt;
   * to contain:
   *  &lt;[0x30]&gt;
   * but could not find:
   *  &lt;[0x30]&gt;</code></pre>
   *
   * @return {@code this} assertion object.
   */
  @Override
  @CheckReturnValue
  public SELF inHexadecimal() {
    return super.inHexadecimal();
  }

  @Override
  @CheckReturnValue
  public SELF inBinary() {
    return super.inBinary();
  }

  /**
   * Filter the array under test into a list composed of the elements elements having a property or field equal to
   * {@code expectedValue}, the property/field is specified by {@code propertyOrFieldName} parameter.
   * <p>
   * The filter first tries to get the value from a property (named {@code propertyOrFieldName}), if no such property
   * exists it tries to read the value from a field. Reading private fields is supported by default, this can be
   * globally disabled by calling {@link Assertions#setAllowExtractingPrivateFields(boolean)
   * Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * When reading <b>nested</b> property/field, if an intermediate value is null the whole nested property/field is
   * considered to be null, thus reading "address.street.name" value will return null if "street" value is null.
   * <p>
   *
   * As an example, let's check all employees 800 years old (yes, special employees):
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * Employee[] employees = new Employee[] { yoda, luke, obiwan, noname };
   *
   * assertThat(employees).filteredOn("age", 800)
   *                      .containsOnly(yoda, obiwan);</code></pre>
   *
   * Nested properties/fields are supported:
   * <pre><code class='java'> // Name is bean class with 'first' and 'last' String properties
   *
   * // name is null for noname =&gt; it does not match the filter on "name.first"
   * assertThat(employees).filteredOn("name.first", "Luke")
   *                      .containsOnly(luke);
   *
   * assertThat(employees).filteredOn("name.last", "Vader")
   *                      .isEmpty();</code></pre>
   * <p>
   * If you want to filter on null value, use {@link #filteredOnNull(String)} as Java will resolve the call to
   * {@link #filteredOn(String, FilterOperator)} instead of this method.
   * <p>
   * An {@link IntrospectionError} is thrown if the given propertyOrFieldName can't be found in one of the array
   * elements.
   * <p>
   * You can chain filters:
   * <pre><code class='java'> // fellowshipOfTheRing is an array of TolkienCharacter having race and name fields
   * // 'not' filter is statically imported from Assertions.not
   *
   * assertThat(fellowshipOfTheRing).filteredOn("race.name", "Man")
   *                                .filteredOn("name", not("Boromir"))
   *                                .containsOnly(aragorn);</code></pre>
   * <p>
   * If you need more complex filter, use {@link #filteredOn(Condition)} or {@link #filteredOn(Predicate)} and
   * provide a {@link Condition} or {@link Predicate} to specify the filter to apply.
   *
   * @param propertyOrFieldName the name of the property or field to read
   * @param expectedValue the value to compare element's property or field with
   * @return a new assertion object with the filtered list under test
   * @throws IllegalArgumentException if the given propertyOrFieldName is {@code null} or empty.
   * @throws IntrospectionError if the given propertyOrFieldName can't be found in one of the array elements.
   */
  @CheckReturnValue
  public SELF filteredOn(String propertyOrFieldName, Object expectedValue) {
    List<ELEMENT> filteredList = filter(actual).with(propertyOrFieldName, expectedValue).get();
    return newObjectArrayAssert(filteredList);
  }

  /**
   * Filter the array under test into a list composed of the elements whose property or field specified
   * by {@code propertyOrFieldName} are null.
   * <p>
   * The filter first tries to get the value from a property (named {@code propertyOrFieldName}), if no such property
   * exists it tries to read the value from a field. Reading private fields is supported by default, this can be
   * globally disabled by calling {@link Assertions#setAllowExtractingPrivateFields(boolean)
   * Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * When reading <b>nested</b> property/field, if an intermediate value is null the whole nested property/field is
   * considered to be null, thus reading "address.street.name" value will return null if "street" value is null.
   * <p>
   * As an example, let's check all employees 800 years old (yes, special employees):
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * Employee[] employees = new Employee[] { yoda, luke, obiwan, noname };
   *
   * assertThat(employees).filteredOnNull("name")
   *                      .containsOnly(noname);</code></pre>
   *
   * Nested properties/fields are supported:
   * <pre><code class='java'> // Name is bean class with 'first' and 'last' String properties
   *
   * assertThat(employees).filteredOnNull("name.last")
   *                      .containsOnly(yoda, obiwan, noname);</code></pre>
   *
   * An {@link IntrospectionError} is thrown if the given propertyOrFieldName can't be found in one of the array
   * elements.
   *
   * @param propertyOrFieldName the name of the property or field to read
   * @return a new assertion object with the filtered list under test
   * @throws IntrospectionError if the given propertyOrFieldName can't be found in one of the array elements.
   */
  @CheckReturnValue
  public SELF filteredOnNull(String propertyOrFieldName) {
    // can't call filteredOn(String propertyOrFieldName, null) as it does not work with soft assertions proxying
    // mechanism, it would lead to double proxying which is not handle properly (improvements needed in our proxy mechanism)
    List<ELEMENT> filteredList = filter(actual).with(propertyOrFieldName, null).get();
    return newObjectArrayAssert(filteredList);
  }

  /**
   * Filter the array under test into a list composed of elements having a property or field matching the filter expressed with
   * the {@link FilterOperator}, the property/field is specified by {@code propertyOrFieldName} parameter.
   * <p>
   * The existing filters are :
   * <ul>
   * <li> {@link Assertions#not(Object) not(Object)}</li>
   * <li> {@link Assertions#in(Object...) in(Object...)}</li>
   * <li> {@link Assertions#notIn(Object...) notIn(Object...)}</li>
   * </ul>
   * <p>
   * Whatever filter is applied, it first tries to get the value from a property (named {@code propertyOrFieldName}), if
   * no such property exists it tries to read the value from a field. Reading private fields is supported by default,
   * this can be globally disabled by calling {@link Assertions#setAllowExtractingPrivateFields(boolean)
   * Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * When reading <b>nested</b> property/field, if an intermediate value is null the whole nested property/field is
   * considered to be null, thus reading "address.street.name" value will return null if "street" value is null.
   * <p>
   * As an example, let's check stuff on some special employees :
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   *
   * Employee[] employees = new Employee[] { yoda, luke, obiwan, noname };
   *
   * // 'not' filter is statically imported from Assertions.not
   * assertThat(employees).filteredOn("age", not(800))
   *                      .containsOnly(luke);
   *
   * // 'in' filter is statically imported from Assertions.in
   * // Name is bean class with 'first' and 'last' String properties
   * assertThat(employees).filteredOn("name.first", in("Yoda", "Luke"))
   *                      .containsOnly(yoda, luke);
   *
   * // 'notIn' filter is statically imported from Assertions.notIn
   * assertThat(employees).filteredOn("name.first", notIn("Yoda", "Luke"))
   *                      .containsOnly(obiwan);</code></pre>
   *
   * An {@link IntrospectionError} is thrown if the given propertyOrFieldName can't be found in one of the array
   * elements.
   * <p>
   * Note that combining filter operators is not supported, thus the following code is not correct:
   * <pre><code class='java'> // Combining filter operators like not(in(800)) is NOT supported
   * // -&gt; throws UnsupportedOperationException
   * assertThat(employees).filteredOn("age", not(in(800)))
   *                      .contains(luke);</code></pre>
   * <p>
   * You can chain filters:
   * <pre><code class='java'> // fellowshipOfTheRing is an array of TolkienCharacter having race and name fields
   * // 'not' filter is statically imported from Assertions.not
   *
   * assertThat(fellowshipOfTheRing).filteredOn("race.name", "Man")
   *                                .filteredOn("name", not("Boromir"))
   *                                .containsOnly(aragorn);</code></pre>
   * <p>
   * If you need more complex filter, use {@link #filteredOn(Condition)} or {@link #filteredOn(Predicate)} and
   * provide a {@link Condition} or {@link Predicate} to specify the filter to apply.
   *
   * @param propertyOrFieldName the name of the property or field to read
   * @param filterOperator the filter operator to apply
   * @return a new assertion object with the filtered list under test
   * @throws IllegalArgumentException if the given propertyOrFieldName is {@code null} or empty.
   */
  @CheckReturnValue
  public SELF filteredOn(String propertyOrFieldName, FilterOperator<?> filterOperator) {
    checkNotNull(filterOperator);
    Filters<ELEMENT> filter = filter(actual).with(propertyOrFieldName);
    filterOperator.applyOn(filter);
    return newObjectArrayAssert(filter.get());
  }

  /**
   * Filter the array under test into a list composed of the elements matching the given {@link Condition},
   * allowing to perform assertions on the filtered list.
   * <p>
   * Let's check old employees whose age &gt; 100:
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * Employee[] employees = new Employee[] { yoda, luke, obiwan, noname };
   *
   * // old employee condition, "old employees" describes the condition in error message
   * // you just have to implement 'matches' method
   * Condition&lt;Employee&gt; oldEmployees = new Condition&lt;Employee&gt;("old employees") {
   *       {@literal @}Override
   *       public boolean matches(Employee employee) {
   *         return employee.getAge() &gt; 100;
   *       }
   *     };
   *   }
   * assertThat(employees).filteredOn(oldEmployees)
   *                      .containsOnly(yoda, obiwan);</code></pre>
   *
   * You can combine {@link Condition} with condition operator like {@link Not}:
   * <pre><code class='java'> // 'not' filter is statically imported from Assertions.not
   * assertThat(employees).filteredOn(not(oldEmployees))
   *                      .contains(luke, noname);</code></pre>
   *
   * @param condition the filter condition / predicate
   * @return a new assertion object with the filtered list under test
   * @throws IllegalArgumentException if the given condition is {@code null}.
   */
  @CheckReturnValue
  public SELF filteredOn(Condition<? super ELEMENT> condition) {
    List<ELEMENT> filteredList = filter(actual).being(condition).get();
    return newObjectArrayAssert(filteredList);
  }

  /**
   * Filter the array under test into a list composed of the elements matching the given {@link Predicate},
   * allowing to perform assertions on the filtered list.
   * <p>
   * Example : check old employees whose age &gt; 100:
   *
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   *
   * Employee[] employees = new Employee[] { yoda, luke, obiwan };
   *
   * assertThat(employees).filteredOn(employee -&gt; employee.getAge() &gt; 100)
   *                      .containsOnly(yoda, obiwan);</code></pre>
   *
   * @param predicate the filter predicate
   * @return a new assertion object with the filtered list under test
   * @throws IllegalArgumentException if the given predicate is {@code null}.
   */
  public SELF filteredOn(Predicate<? super ELEMENT> predicate) {
    return internalFilteredOn(predicate);
  }

  /**
   * Filter the array under test into a list composed of the elements for which the result of the {@code function} is equal to {@code expectedValue}.
   * <p>
   * It allows to filter elements in more safe way than by using {@link #filteredOn(String, Object)} as it doesn't utilize introspection.
   * <p>
   * As an example, let's check all employees 800 years old (yes, special employees):
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * Employee[] employees = new Employee[] { yoda, luke, obiwan, noname };
   *
   * assertThat(employees).filteredOn(Employee::getAge, 800)
   *                      .containsOnly(yoda, obiwan);
   *
   * assertThat(employees).filteredOn(e -&gt; e.getName(), null)
   *                      .containsOnly(noname);</code></pre>
   *
   * If you need more complex filter, use {@link #filteredOn(Predicate)} or {@link #filteredOn(Condition)}.
   *
   * @param <T> result type of the filter function
   * @param function the filter function
   * @param expectedValue the expected value of the filter function
   * @return a new assertion object with the filtered list under test
   * @throws IllegalArgumentException if the given function is {@code null}.
   * @since 3.17.0
   */
  @CheckReturnValue
  public <T> SELF filteredOn(Function<? super ELEMENT, T> function, T expectedValue) {
    checkArgument(function != null, "The filter function should not be null");
    // call internalFilteredOn to avoid double proxying in soft assertions
    return internalFilteredOn(element -> java.util.Objects.equals(function.apply(element), expectedValue));
  }

  /**
   * Filter the array under test keeping only elements matching the given assertions specified with a {@link Consumer}.
   * <p>
   * Example : check old employees whose age &gt; 100:
   *
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   *
   * Employee[] employees = new Employee[] { yoda, luke, obiwan };
   *
   * assertThat(employees).filteredOnAssertions(employee -&gt; assertThat(employee.getAge()).isGreaterThan(100))
   *                      .containsOnly(yoda, obiwan);</code></pre>
   *
   * @param elementAssertions containing AssertJ assertions to filter on
   * @return a new assertion object with the filtered iterable under test
   * @throws IllegalArgumentException if the given predicate is {@code null}.
   * @since 3.11.0
   */
  public SELF filteredOnAssertions(Consumer<? super ELEMENT> elementAssertions) {
    return internalFilteredOnAssertions(elementAssertions);
  }

  /**
   * Filter the array under test keeping only elements matching the given assertions specified with a {@link Consumer}.
   * <p>
   * Example : check old employees whose age &gt; 100:
   *
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   *
   * Employee[] employees = new Employee[] { yoda, luke, obiwan };
   * 
   * // compiles even if getAge() throws a checked exception unlike filteredOnAssertions(Consumer)
   * assertThat(employees).filteredOnAssertions(employee -&gt; assertThat(employee.getAge()).isGreaterThan(100))
   *                      .containsOnly(yoda, obiwan);</code></pre>
   *
   * @param elementAssertions containing AssertJ assertions to filter on
   * @return a new assertion object with the filtered iterable under test
   * @throws IllegalArgumentException if the given predicate is {@code null}.
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @since 3.21.0
   */
  public SELF filteredOnAssertions(ThrowingConsumer<? super ELEMENT> elementAssertions) {
    return internalFilteredOnAssertions(elementAssertions);
  }

  private SELF internalFilteredOnAssertions(Consumer<? super ELEMENT> elementAssertions) {
    checkArgument(elementAssertions != null, "The element assertions should not be null");
    List<ELEMENT> filteredIterable = stream(actual).filter(byPassingAssertions(elementAssertions)).collect(toList());
    return newObjectArrayAssert(filteredIterable).withAssertionState(myself);
  }

  /**
   * Verifies that all elements match the given {@link Predicate}.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc  = {"a", "b", "c"};
   * String[] abcc  = {"a", "b", "cc"};
   *
   * // assertion will pass
   * assertThat(abc).allMatch(s -&gt; s.length() == 1);
   *
   * // assertion will fail
   * assertThat(abcc).allMatch(s -&gt; s.length() == 1);</code></pre>
   *
   * Note that you can achieve the same result with {@link #are(Condition) are(Condition)} or {@link #have(Condition) have(Condition)}.
   *
   * @param predicate the given {@link Predicate}.
   * @return {@code this} object.
   * @throws NullPointerException if the given predicate is {@code null}.
   * @throws AssertionError if an element cannot be cast to ELEMENT.
   * @throws AssertionError if one or more elements don't satisfy the given predicate.
   */
  @Override
  public SELF allMatch(Predicate<? super ELEMENT> predicate) {
    iterables.assertAllMatch(info, newArrayList(actual), predicate, PredicateDescription.GIVEN);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF allMatch(Predicate<? super ELEMENT> predicate, String predicateDescription) {
    iterables.assertAllMatch(info, newArrayList(actual), predicate, new PredicateDescription(predicateDescription));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF allSatisfy(Consumer<? super ELEMENT> requirements) {
    return internalAllSatisfy(requirements);
  }

  /**
   * Verifies that all the elements satisfy the given requirements expressed as a {@link ThrowingConsumer}.
   * <p>
   * This is useful to perform a group of assertions on elements.
   * <p>
   * This is the same assertion as {@link #allSatisfy(Consumer)} but the given consumer can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}. 
   * <p>
   * Example:
   * <pre><code class='java'>  // read() throws IOException
   * // note that the code would not compile if isNotEmpty, startsWithA or startsWithZ were declared as a Consumer&lt;Reader&gt; 
   * ThrowingConsumer&lt;Reader&gt; isNotEmpty = reader -&gt; assertThat(reader.read()).isEqualTo(-1);
   * ThrowingConsumer&lt;Reader&gt; startsWithA = reader -&gt; assertThat(reader.read()).isEqualTo('A');
   *
   * // ABC.txt contains: ABC  
   * // XYZ.txt contains: XYZ  
   * FileReader[] readers = { new FileReader("ABC.txt"), new FileReader("XYZ.txt") }; 
   * 
   * // assertion succeeds as none of the files are empty
   * assertThat(readers).allSatisfy(isNotEmpty);
   *
   * // assertion fails as XYZ.txt does not start with 'A':
   * assertThat(readers).allSatisfy(startsWithA);</code></pre>
   * <p>
   * If the actual array is empty, this assertion succeeds as there is nothing to check.
   *
   * @param requirements the given {@link ThrowingConsumer}.
   * @return {@code this} object.
   * @throws NullPointerException if given {@link ThrowingConsumer} is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if one or more elements don't satisfy the given requirements.
   * @since 3.21.0
   */
  @Override
  public SELF allSatisfy(ThrowingConsumer<? super ELEMENT> requirements) {
    return internalAllSatisfy(requirements);
  }

  private SELF internalAllSatisfy(Consumer<? super ELEMENT> requirements) {
    iterables.assertAllSatisfy(info, newArrayList(actual), requirements);
    return myself;
  }

  /**
   * Verifies whether any elements match the provided {@link Predicate}.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abcc = { "a", "b", "cc" };
   *
   * // assertion will pass
   * assertThat(abc).anyMatch(s -&gt; s.length() == 2);
   *
   * // assertion will fail
   * assertThat(abcc).anyMatch(s -&gt; s.length() &gt; 2);</code></pre>
   *
   * Note that you can achieve the same result with {@link #areAtLeastOne(Condition) areAtLeastOne(Condition)}
   * or {@link #haveAtLeastOne(Condition) haveAtLeastOne(Condition)}.
   *
   * @param predicate the given {@link Predicate}.
   * @return {@code this} object.
   * @throws NullPointerException if the given predicate is {@code null}.
   * @throws AssertionError if no elements satisfy the given predicate.
   * @since 3.9.0
   */
  @Override
  public SELF anyMatch(Predicate<? super ELEMENT> predicate) {
    iterables.assertAnyMatch(info, newArrayList(actual), predicate, PredicateDescription.GIVEN);
    return myself;
  }

  /**
   * Verifies that the zipped pairs of actual and other elements, i.e: (actual 1st element, other 1st element), (actual 2nd element, other 2nd element), ...
   * all satisfy the given {@code zipRequirements}.
   * <p>
   * This assertion assumes that actual and other have the same size but they can contain different type of elements
   * making it handy to compare objects converted to another type, for example Domain and View/DTO objects.
   * <p>
   * Example:
   * <pre><code class='java'> Adress[] addressModels = findGoodRestaurants();
   * AdressView[] addressViews = convertToView(addressModels);
   *
   * // compare addressViews and addressModels respective paired elements.
   * assertThat(addressViews).zipSatisfy(addressModels, (AdressView view, Adress model) -&gt; {
   *    assertThat(view.getZipcode() + ' ' + view.getCity()).isEqualTo(model.getCityLine());
   *    assertThat(view.getStreet()).isEqualTo(model.getStreet().toUpperCase());
   * });</code></pre>
   *
   * @param <OTHER_ELEMENT> the type of the other array elements.
   * @param other the array to zip actual with.
   * @param zipRequirements the given requirements that each pair must satisfy.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given zipRequirements {@link BiConsumer} is {@code null}.
   * @throws NullPointerException if the other array to zip actual with is {@code null}.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if actual and other don't have the same size.
   * @throws AssertionError if one or more pairs don't satisfy the given requirements.
   * @since 3.9.0
   */
  public <OTHER_ELEMENT> SELF zipSatisfy(OTHER_ELEMENT[] other,
                                         BiConsumer<? super ELEMENT, OTHER_ELEMENT> zipRequirements) {
    iterables.assertZipSatisfy(info, newArrayList(actual), newArrayList(other), zipRequirements);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF anySatisfy(Consumer<? super ELEMENT> requirements) {
    return internalAnySatisfy(requirements);
  }

  /**
   * Verifies that at least one element satisfies the given requirements expressed as a {@link ThrowingConsumer}.
   * <p>
   * This is useful to check that a group of assertions is verified by (at least) one element.
   * <p>
   * This is the same assertion as {@link #anySatisfy(Consumer)} but the given consumer can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}. 
   * <p>
   * Example:
   * <pre><code class='java'>  // read() throws IOException
   * // note that the code would not compile if startsWithA, startsWithY or startsWithZ were declared as a Consumer&lt;Reader&gt; 
   * ThrowingConsumer&lt;Reader&gt; startsWithA = reader -&gt; assertThat(reader.read()).isEqualTo('A');
   * ThrowingConsumer&lt;Reader&gt; startsWithZ = reader -&gt; assertThat(reader.read()).isEqualTo('Z');
   *
   * // ABC.txt contains: ABC  
   * // XYZ.txt contains: XYZ  
   * FileReader[] readers = { new FileReader("ABC.txt"), new FileReader("XYZ.txt") };
   *  
   * // assertion succeeds as ABC.txt starts with 'A'
   * assertThat(readers).anySatisfy(startsWithA);
   *
   * // assertion fails none of the files starts with 'Z':
   * assertThat(readers).anySatisfy(startsWithZ);</code></pre>
   * <p>
   * If the actual array is empty, this assertion succeeds as there is nothing to check.
   *
   * @param requirements the given {@link ThrowingConsumer}.
   * @return {@code this} object.
   * @throws NullPointerException if given {@link ThrowingConsumer} is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError no elements satisfy the given requirements.
   * @since 3.21.0
   */
  @Override
  public SELF anySatisfy(ThrowingConsumer<? super ELEMENT> requirements) {
    return internalAnySatisfy(requirements);
  }

  private SELF internalAnySatisfy(Consumer<? super ELEMENT> requirements) {
    iterables.assertAnySatisfy(info, newArrayList(actual), requirements);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF noneSatisfy(Consumer<? super ELEMENT> restrictions) {
    return internalNoneSatisfy(restrictions);
  }

  /**
   * Verifies that no elements satisfy the given restrictions expressed as a {@link Consumer}.
   * <p>
   * This is useful to check that a group of assertions is verified by (at least) one element.
   * <p>
   * This is the same assertion as {@link #anySatisfy(Consumer)} but the given consumer can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}. 
   * <p>
   * Example:
   * <pre><code class='java'>  // read() throws IOException
   * // note that the code would not compile if startsWithA, startsWithY or startsWithZ were declared as a Consumer&lt;Reader&gt; 
   * ThrowingConsumer&lt;Reader&gt; startsWithA = reader -&gt; assertThat(reader.read()).isEqualTo('A');
   * ThrowingConsumer&lt;Reader&gt; startsWithZ = reader -&gt; assertThat(reader.read()).isEqualTo('Z');
   *
   * // ABC.txt contains: ABC  
   * // XYZ.txt contains: XYZ  
   * FileReader[] readers = { new FileReader("ABC.txt"), new FileReader("XYZ.txt") }; 
   * 
   * // assertion succeeds as none of the file starts 'Z'
   * assertThat(readers).noneSatisfy(startsWithZ);
   *
   * // assertion fails as ABC.txt starts with 'A':
   * assertThat(readers).noneSatisfy(startsWithA);</code></pre>
   * <p>
   * Note that this assertion succeeds if the group (collection, array, ...) is empty whatever the restrictions are.
   *
   * @param restrictions the given {@link ThrowingConsumer}.
   * @return {@code this} object.
   * @throws NullPointerException if given {@link ThrowingConsumer} is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if one or more elements satisfy the given requirements.
   * @since 3.21.0
   */
  @Override
  public SELF noneSatisfy(ThrowingConsumer<? super ELEMENT> restrictions) {
    return internalNoneSatisfy(restrictions);
  }

  private SELF internalNoneSatisfy(Consumer<? super ELEMENT> restrictions) {
    iterables.assertNoneSatisfy(info, newArrayList(actual), restrictions);
    return myself;
  }

  /**
   * Verifies that each element satisfies the requirements corresponding to its index, so the first element must satisfy the
   * first requirements, the second element the second requirements etc...
   * <p>
   * Each requirements are expressed as a {@link Consumer}, there must be as many requirements as there are iterable elements.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter[] characters = {frodo, aragorn, legolas};
   *
   * // assertions succeed
   * assertThat(characters).satisfiesExactly(character -&gt; assertThat(character.getRace()).isEqualTo("Hobbit"),
   *                                         character -&gt; assertThat(character.isMortal()).isTrue(),
   *                                         character -&gt; assertThat(character.getName()).isEqualTo("Legolas"));
   *
   * // you can specify more that one assertion per requirements
   * assertThat(characters).satisfiesExactly(character -&gt; {
   *                                            assertThat(character.getRace()).isEqualTo("Hobbit");
   *                                            assertThat(character.getName()).isEqualTo("Frodo");
   *                                         },
   *                                         character -&gt; {
   *                                            assertThat(character.isMortal()).isTrue();
   *                                            assertThat(character.getName()).isEqualTo("Aragorn");
   *                                         },
   *                                         character -&gt; {
   *                                            assertThat(character.getRace()).isEqualTo("Elf");
   *                                            assertThat(character.getName()).isEqualTo("Legolas");
   *                                         });
   *
   * // assertion fails as aragorn does not meet the second requirements
   * assertThat(characters).satisfiesExactly(character -&gt; assertThat(character.getRace()).isEqualTo("Hobbit"),
   *                                         character -&gt; assertThat(character.isMortal()).isFalse(),
   *                                         character -&gt; assertThat(character.getName()).isEqualTo("Legolas"));</code></pre>
   *
   * @param requirements the requirements to meet.
   * @return {@code this} to chain assertions.
   * @throws NullPointerException if given requirements are null.
   * @throws AssertionError if any element does not satisfy the requirements at the same index
   * @throws AssertionError if there are not as many requirements as there are iterable elements.
   * @since 3.19.0
   */
  @Override
  @SafeVarargs
  public final SELF satisfiesExactly(Consumer<? super ELEMENT>... requirements) {
    return satisfiesExactlyForProxy(requirements);
  }

  /**
   * Verifies that each element satisfies the requirements corresponding to its index, so the first element must satisfy the
   * first requirements, the second element the second requirements etc...
   * <p>
   * Each requirements are expressed as a {@link ThrowingConsumer}, there must be as many requirements as there are iterable elements.
   * <p>
   * This is the same assertion as {@link #satisfiesExactly(Consumer...)} but the given consumers can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}. 
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter[] characters = {frodo, aragorn, legolas};
   * 
   * // the code would compile even if TolkienCharacter.getRace(), isMortal() or getName() threw a checked exception
   *
   * // assertions succeed
   * assertThat(characters).satisfiesExactly(character -&gt; assertThat(character.getRace()).isEqualTo("Hobbit"),
   *                                         character -&gt; assertThat(character.isMortal()).isTrue(),
   *                                         character -&gt; assertThat(character.getName()).isEqualTo("Legolas"));
   *
   * // you can specify more that one assertion per requirements
   * assertThat(characters).satisfiesExactly(character -&gt; {
   *                                            assertThat(character.getRace()).isEqualTo("Hobbit");
   *                                            assertThat(character.getName()).isEqualTo("Frodo");
   *                                         },
   *                                         character -&gt; {
   *                                            assertThat(character.isMortal()).isTrue();
   *                                            assertThat(character.getName()).isEqualTo("Aragorn");
   *                                         },
   *                                         character -&gt; {
   *                                            assertThat(character.getRace()).isEqualTo("Elf");
   *                                            assertThat(character.getName()).isEqualTo("Legolas");
   *                                         });
   *
   * // assertion fails as aragorn does not meet the second requirements
   * assertThat(characters).satisfiesExactly(character -&gt; assertThat(character.getRace()).isEqualTo("Hobbit"),
   *                                         character -&gt; assertThat(character.isMortal()).isFalse(),
   *                                         character -&gt; assertThat(character.getName()).isEqualTo("Legolas"));</code></pre>
   *
   * @param requirements the requirements to meet.
   * @return {@code this} to chain assertions.
   * @throws NullPointerException if given requirements are null.
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if any element does not satisfy the requirements at the same index
   * @throws AssertionError if there are not as many requirements as there are iterable elements.
   * @since 3.21.0
   */
  @Override
  @SafeVarargs
  public final SELF satisfiesExactly(ThrowingConsumer<? super ELEMENT>... requirements) {
    return satisfiesExactlyForProxy(requirements);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF satisfiesExactlyForProxy(Consumer<? super ELEMENT>[] requirements) {
    iterables.assertSatisfiesExactly(info, newArrayList(actual), requirements);
    return myself;
  }

  /**
   * Verifies that at least one combination of iterable elements exists that satisfies the consumers in order (there must be as
   * many consumers as iterable elements and once a consumer is matched it cannot be reused to match other elements).
   * <p>
   * This is a variation of {@link #satisfiesExactly(Consumer...)} where order does not matter.
   * <p>
   * Examples:
   * <pre><code class='java'> String[] starWarsCharacterNames = {"Luke", "Leia", "Yoda"};
   *
   * // these assertions succeed:
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"), // matches "Yoda"
   *                                                               name -&gt; assertThat(name).contains("L"), // matches "Luke" and "Leia"
   *                                                               name -&gt; {
   *                                                                 assertThat(name).hasSize(4);
   *                                                                 assertThat(name).doesNotContain("a"); // matches "Luke" but not "Leia"
   *                                                               })
   *                                   .satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Yo"),
   *                                                               name -&gt; assertThat(name).contains("Lu"),
   *                                                               name -&gt; assertThat(name).contains("Le"))
   *                                   .satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Le"),
   *                                                               name -&gt; assertThat(name).contains("Yo"),
   *                                                               name -&gt; assertThat(name).contains("Lu"));
   *
   * // this assertion fails as 3 consumer/requirements are expected
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"),
   *                                                               name -&gt; assertThat(name).contains("L"));
   *
   * // this assertion fails as no element contains "Han" (first consumer/requirements can't be met)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Han"),
   *                                                               name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("Y"));
   *
   * // this assertion fails as "Yoda" element can't satisfy any consumers/requirements (even though all consumers/requirements are met)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("L"));
   *
   * // this assertion fails as no combination of elements can satisfy the consumers in order
   * // the problem is if the last consumer is matched by Leia then no other consumer can match Luke (and vice versa)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"),
   *                                                               name -&gt; assertThat(name).contains("o"),
   *                                                               name -&gt; assertThat(name).contains("L"));</code></pre>
   *
   * @param requirements the consumers that are expected to be satisfied by the elements of the given {@code Iterable}.
   * @return this assertion object.
   * @throws NullPointerException if the given consumers array or any consumer is {@code null}.
   * @throws AssertionError if there is no permutation of elements that satisfies the individual consumers in order
   * @throws AssertionError if there are not as many requirements as there are iterable elements.
   *
   * @since 3.19.0
   */
  @Override
  @SafeVarargs
  public final SELF satisfiesExactlyInAnyOrder(Consumer<? super ELEMENT>... requirements) {
    return satisfiesExactlyInAnyOrderForProxy(requirements);
  }

  /**
   * Verifies that at least one combination of iterable elements exists that satisfies the {@link ThrowingConsumer}s in order (there must be as
   * many consumers as iterable elements and once a consumer is matched it cannot be reused to match other elements).
   * <p>
   * This is a variation of {@link #satisfiesExactly(ThrowingConsumer...)} where order does not matter.
   * <p>
   * Examples:
   * <pre><code class='java'> String[] starWarsCharacterNames = {"Luke", "Leia", "Yoda"};
   *
   * // these assertions succeed:
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"), // matches "Yoda"
   *                                                               name -&gt; assertThat(name).contains("L"), // matches "Luke" and "Leia"
   *                                                               name -&gt; {
   *                                                                 assertThat(name).hasSize(4);
   *                                                                 assertThat(name).doesNotContain("a"); // matches "Luke" but not "Leia"
   *                                                               })
   *                                   .satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Yo"),
   *                                                               name -&gt; assertThat(name).contains("Lu"),
   *                                                               name -&gt; assertThat(name).contains("Le"))
   *                                   .satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Le"),
   *                                                               name -&gt; assertThat(name).contains("Yo"),
   *                                                               name -&gt; assertThat(name).contains("Lu"));
   *
   * // this assertion fails as 3 consumers/requirements are expected
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"),
   *                                                               name -&gt; assertThat(name).contains("L"));
   *
   * // this assertion fails as no element contains "Han" (first consumer/requirements can't be met)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Han"),
   *                                                               name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("Y"));
   *
   * // this assertion fails as "Yoda" element can't satisfy any consumers/requirements (even though all consumers/requirements are met)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("L"),
   *                                                               name -&gt; assertThat(name).contains("L"));
   *
   * // this assertion fails as no combination of elements can satisfy the consumers in order
   * // the problem is if the last consumer is matched by Leia then no other consumer can match Luke (and vice versa)
   * assertThat(starWarsCharacterNames).satisfiesExactlyInAnyOrder(name -&gt; assertThat(name).contains("Y"),
   *                                                               name -&gt; assertThat(name).contains("o"),
   *                                                               name -&gt; assertThat(name).contains("L"));</code></pre>
   *
   * @param requirements the consumers that are expected to be satisfied by the elements of the given {@code Iterable}.
   * @return this assertion object.
   * @throws NullPointerException if the given consumers array or any consumer is {@code null}.
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if there is no permutation of elements that satisfies the individual consumers in order
   * @throws AssertionError if there are not as many requirements as there are iterable elements.
   * @since 3.21.0
   */
  @Override
  @SafeVarargs
  public final SELF satisfiesExactlyInAnyOrder(ThrowingConsumer<? super ELEMENT>... requirements) {
    return satisfiesExactlyInAnyOrderForProxy(requirements);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF satisfiesExactlyInAnyOrderForProxy(Consumer<? super ELEMENT>[] requirements) {
    iterables.assertSatisfiesExactlyInAnyOrder(info, newArrayList(actual), requirements);
    return myself;
  }

  /**
   * Verifies that there is exactly one element in the array under test that satisfies the {@link Consumer}.
   * <p>
   * Examples:
   * <pre><code class='java'> String[] starWarsCharacterNames = {"Luke", "Leia", "Yoda"};
   *
   * // these assertions succeed:
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Y")) // matches only "Yoda"
   *                                   .satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Lu")) // matches only "Luke"
   *                                   .satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Le")); // matches only "Leia"
   *
   * // this assertion fails because the requirements are satisfied two times
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("a")); // matches "Leia" and "Yoda"
   *
   * // this assertion fails because no element contains "Han"
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Han"));</code></pre>
   *
   * @param requirements the {@link Consumer} that is expected to be satisfied only once by the elements of the given {@code Iterable}.
   * @return this assertion object.
   * @throws NullPointerException if the given requirements are {@code null}.
   * @throws AssertionError if the requirements are not satisfied only once
   * @since 3.24.0
   */
  @Override
  public SELF satisfiesOnlyOnce(Consumer<? super ELEMENT> requirements) {
    return satisfiesOnlyOnceForProxy(requirements);
  }

  /**
   * Verifies that there is exactly one element in the array under test that satisfies the {@link ThrowingConsumer}.
   * <p>
   * Examples:
   * <pre><code class='java'> String[] starWarsCharacterNames = {"Luke", "Leia", "Yoda"};
   *
   * // these assertions succeed:
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Y")) // matches only "Yoda"
   *                                   .satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Lu")) // matches only "Luke"
   *                                   .satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Le")); // matches only "Leia"
   *
   * // this assertion fails because the requirements are satisfied two times
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("a")); // matches "Leia" and "Yoda"
   *
   * // this assertion fails because no element contains "Han"
   * assertThat(starWarsCharacterNames).satisfiesOnlyOnce(name -&gt; assertThat(name).contains("Han"));</code></pre>
   *
   * @param requirements the {@link ThrowingConsumer} that is expected to be satisfied only once by the elements of the given {@code Iterable}.
   * @return this assertion object.
   * @throws NullPointerException if the given requirements are {@code null}.
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.    
   * @throws AssertionError if the requirements are not satisfied only once
   * @since 3.24.0
   */
  @Override
  public SELF satisfiesOnlyOnce(ThrowingConsumer<? super ELEMENT> requirements) {
    return satisfiesOnlyOnceForProxy(requirements);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF satisfiesOnlyOnceForProxy(Consumer<? super ELEMENT> requirements) {
    iterables.assertSatisfiesOnlyOnce(info, newArrayList(actual), requirements);
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertions will pass
   * assertThat(abc).containsAnyOf("b")
   *                .containsAnyOf("b", "c")
   *                .containsAnyOf("a", "b", "c")
   *                .containsAnyOf("a", "b", "c", "d")
   *                .containsAnyOf("e", "f", "g", "b");
   *
   * // assertions will fail
   * assertThat(abc).containsAnyOf("d");
   * assertThat(abc).containsAnyOf("d", "e", "f", "g");</code></pre>
   *
   * @param values the values whose at least one which is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws AssertionError if the array of values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  @SafeVarargs
  public final SELF containsAnyOf(ELEMENT... values) {
    return containsAnyOfForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsAnyOfForProxy(ELEMENT[] values) {
    arrays.assertContainsAnyOf(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual array contains at least one of the given {@link Iterable} elements.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abc = {"a", "b", "c"};
   *
   * // assertions will pass
   * assertThat(abc).containsAnyElementsOf(Arrays.asList("b"))
   *                .containsAnyElementsOf(Arrays.asList("b", "c"))
   *                .containsAnyElementsOf(Arrays.asList("a", "b", "c"))
   *                .containsAnyElementsOf(Arrays.asList("a", "b", "c", "d"))
   *                .containsAnyElementsOf(Arrays.asList("e", "f", "g", "b"));
   *
   * // assertions will fail
   * assertThat(abc).containsAnyElementsOf(Arrays.asList("d"));
   * assertThat(abc).containsAnyElementsOf(Arrays.asList("d", "e", "f", "g"));</code></pre>
   *
   * @param iterable the iterable whose at least one element is expected to be in the array under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the iterable of expected values is {@code null}.
   * @throws AssertionError if the iterable of expected values is empty and the array under test is not empty.
   * @throws AssertionError if the array under test is {@code null}.
   * @throws AssertionError if the array under test does not contain any of elements from the given {@code Iterable}.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public SELF containsAnyElementsOf(Iterable<? extends ELEMENT> iterable) {
    return containsAnyOf(toArray(iterable));
  }

  /**
   * Verifies that no elements match the given {@link Predicate}.
   * <p>
   * Example :
   * <pre><code class='java'> String[] abcc = { "a", "b", "cc" };
   *
   * // assertion will pass
   * assertThat(abcc).noneMatch(s -&gt; s.isEmpty());
   *
   * // assertion will fail
   * assertThat(abcc).noneMatch(s -&gt; s.length() == 2);</code></pre>
   *
   * Note that you can achieve the same result with {@link #areNot(Condition) areNot(Condition)}
   * or {@link #doNotHave(Condition) doNotHave(Condition)}.
   *
   * @param predicate the given {@link Predicate}.
   * @return {@code this} object.
   * @throws NullPointerException if the given predicate is {@code null}.
   * @throws AssertionError if an element cannot be cast to ELEMENT.
   * @throws AssertionError if any element satisfy the given predicate.
   * @since 3.9.0
   */
  @Override
  public SELF noneMatch(Predicate<? super ELEMENT> predicate) {
    iterables.assertNoneMatch(info, newArrayList(actual), predicate, PredicateDescription.GIVEN);
    return myself;
  }

  /**
   * Create a friendly soft or "hard" assertion.
   * <p>
   * Implementations need to redefine either to be proxy friendly (i.e. no final assertion methods)
   * or generic vararg friendly (to use {@link SafeVarargs} annotation which requires final method).
   * <p>
   * The default implementation will assume that this concrete implementation is NOT a soft assertion.
   *
   * @param <E> the type of elements.
   * @param newActual new value
   * @return a new {@link AbstractListAssert}.
   */
  @Override
  protected <E> AbstractListAssert<?, List<? extends E>, E, ObjectAssert<E>> newListAssertInstance(List<? extends E> newActual) {
    return new ListAssert<>(newActual);
  }

  /**
   * Enable using a recursive field by field comparison strategy when calling the chained {@link RecursiveComparisonAssert},
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   boolean hasPhd;
   * }
   *
   * public class Doctor {
   *  String name;
   *  boolean hasPhd;
   * }
   *
   * Doctor drSheldon = new Doctor("Sheldon Cooper", true);
   * Doctor drLeonard = new Doctor("Leonard Hofstadter", true);
   * Doctor drRaj = new Doctor("Raj Koothrappali", true);
   *
   * Person sheldon = new Person("Sheldon Cooper", true);
   * Person leonard = new Person("Leonard Hofstadter", true);
   * Person raj = new Person("Raj Koothrappali", true);
   * Person howard = new Person("Howard Wolowitz", false);
   *
   * Doctor[] doctors = { drSheldon, drLeonard, drRaj };
   * Person[] people = { sheldon, leonard, raj };
   *
   * // assertion succeeds as both lists contains equivalent items in order.
   * assertThat(doctors).usingRecursiveComparison()
   *                    .isEqualTo(people);
   *
   * // assertion fails because leonard names are different.
   * leonard.setName("Leonard Ofstater");
   * assertThat(doctors).usingRecursiveComparison()
   *                    .isEqualTo(people);
   *
   * // assertion fails because howard is missing and leonard is not expected.
   * Person[] otherPeople = { howard, sheldon, raj };
   * assertThat(doctors).usingRecursiveComparison()
   *                    .isEqualTo(otherPeople);</code></pre>
   *
   * A detailed documentation for the recursive comparison is available here: <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>.
   * <p>
   * The default recursive comparison behavior is {@link RecursiveComparisonConfiguration configured} as follows:
   * <ul>
   *   <li> different types of iterable can be compared by default, this allows to compare for example an {@code Person[]} and a {@code PersonDto[]}.<br>
   *        This behavior can be turned off by calling {@link RecursiveComparisonAssert#withStrictTypeChecking() withStrictTypeChecking}.</li>
   *   <li>overridden equals methods are used in the comparison (unless stated otherwise - see <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals">https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals</a>)</li>
   *   <li>the following types are compared with these comparators:
   *     <ul>
   *       <li>{@code java.lang.Double}: {@code DoubleComparator} with precision of 1.0E-15</li>
   *       <li>{@code java.lang.Float}: {@code FloatComparator }with precision of 1.0E-6</li>
   *       <li>any comparators previously registered with {@link AbstractIterableAssert#usingComparatorForType(Comparator, Class)} </li>
   *     </ul>
   *   </li>
   * </ul>
   *
   * @return a new {@link RecursiveComparisonAssert} instance
   * @see RecursiveComparisonConfiguration RecursiveComparisonConfiguration
   */
  @Override
  @Beta
  public RecursiveComparisonAssert<?> usingRecursiveComparison() {
    // overridden for javadoc and to make this method public
    return super.usingRecursiveComparison();
  }

  /**
   * Same as {@link #usingRecursiveComparison()} but allows to specify your own {@link RecursiveComparisonConfiguration}.
   * @param recursiveComparisonConfiguration the {@link RecursiveComparisonConfiguration} used in the chained {@link RecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion.
   *
   * @return a new {@link RecursiveComparisonAssert} instance built with the given {@link RecursiveComparisonConfiguration}.
   */
  @Override
  @Beta
  public RecursiveComparisonAssert<?> usingRecursiveComparison(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    return super.usingRecursiveComparison(recursiveComparisonConfiguration).withTypeComparators(comparatorsByType);
  }

  /**
   * <p>Asserts that the given predicate is met for all fields of the object under test <b>recursively</b> (but not the object itself).</p>
   *
   * <p>For example if the object under test is an instance of class A, A has a B field and B a C field then the assertion checks A's B field and B's C field and all C's fields.</p>
   *
   * <p>The recursive algorithm employs cycle detection, so object graphs with cyclic references can safely be asserted over without causing looping.</p>
   *
   * <p>This method enables recursive asserting using default configuration, which means all fields of all objects have the  
   * {@link java.util.function.Predicate} applied to them (including primitive fields), no fields are excluded, but:
   * <ul>
   *   <li>The recursion does not enter into Java Class Library types (java.*, javax.*)</li>
   *   <li>The {@link java.util.function.Predicate} is applied to {@link java.util.Collection} and array elements (but not the collection/array itself)</li>
   *   <li>The {@link java.util.function.Predicate} is applied to {@link java.util.Map} values but not the map itself or its keys</li>
   *   <li>The {@link java.util.function.Predicate} is applied to {@link java.util.Optional} and primitive optional values</li>
   * </ul>
   * <p>You can change how the recursive assertion deals with arrays, collections, maps and optionals, see:</p>
   * <ul>
   *   <li>{@link RecursiveAssertionAssert#withCollectionAssertionPolicy(RecursiveAssertionConfiguration.CollectionAssertionPolicy)} for collections and arrays</li>
   *   <li>{@link RecursiveAssertionAssert#withMapAssertionPolicy(RecursiveAssertionConfiguration.MapAssertionPolicy)} for maps</li>
   *   <li>{@link RecursiveAssertionAssert#withOptionalAssertionPolicy(RecursiveAssertionConfiguration.OptionalAssertionPolicy)} for optionals</li>
   * </ul>
   *
   * <p>It is possible to assert several predicates over the object graph in a row.</p>
   *
   * <p>The classes used in recursive asserting are <em>not</em> thread safe. Care must be taken when running tests in parallel
   * not to run assertions over object graphs that are being shared between tests.</p>
   *
   * <p><strong>Example</strong></p>
   * <pre><code style='java'> class Author {
   *   String name;
   *   String email;
   *   List&lt;Book&gt; books = new ArrayList&lt;&gt;();
   *
   *   Author(String name, String email) {
   *     this.name = name;
   *     this.email = email;
   *   }
   * }
   *
   * class Book {
   *   String title;
   *   Author[] authors;
   *
   *   Book(String title, Author[] authors) {
   *     this.title = title;
   *     this.authors = authors;
   *   }
   * }
   *
   * Author pramodSadalage = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
   * Author martinFowler = new Author("Martin Fowler", "m.fowler@recursive.test");
   * Author kentBeck = new Author("Kent Beck", "k.beck@recursive.test");
   *
   * Book noSqlDistilled = new Book("NoSql Distilled", new Author[] {pramodSadalage, martinFowler});
   * pramodSadalage.books.add(noSqlDistilled);
   * martinFowler.books.add(noSqlDistilled);
   *
   * Book refactoring = new Book("Refactoring", new Author[] {martinFowler, kentBeck});
   * martinFowler.books.add(refactoring);
   * kentBeck.books.add(refactoring);
   *
   * // assertion succeeds
   * Author[] authors = { pramodSadalage, kentBeck };
   * assertThat(authors).usingRecursiveAssertion()
   *                    .allFieldsSatisfy(field -> field != null); </code></pre>
   *
   * <p>In case one or more fields in the object graph fails the predicate test, the entire assertion will fail. Failing fields
   * will be listed in the failure report using a JSON path-ish notation.</p>
   *
   * @return A new instance of {@link RecursiveAssertionAssert} built with a default {@link RecursiveAssertionConfiguration}.
   */
  @Override
  public RecursiveAssertionAssert usingRecursiveAssertion() {
    return super.usingRecursiveAssertion();
  }

  /**
   * <p>The same as {@link #usingRecursiveAssertion()}, but this method allows the developer to pass in an explicit recursion
   * configuration. This configuration gives fine-grained control over what to include in the recursion, such as:</p>
   *
   * <ul>
   *   <li>Exclusion of fields that are null</li>
   *   <li>Exclusion of fields by path</li>
   *   <li>Exclusion of fields by type</li>
   *   <li>Exclusion of primitive fields</li>
   *   <li>Inclusion of Java Class Library types in the recursive execution</li>
   *   <li>Treatment of {@link java.util.Collection} and array objects</li>
   *   <li>Treatment of {@link java.util.Map} objects</li>
   *   <li>Treatment of Optional and primitive Optional objects</li>
   * </ul>
   *
   * <p>Please refer to the documentation of {@link RecursiveAssertionConfiguration.Builder} for more details.</p>
   *
   * @param recursiveAssertionConfiguration The recursion configuration described above.
   * @return A new instance of {@link RecursiveAssertionAssert} built with a default {@link RecursiveAssertionConfiguration}.
   */
  @Override
  public RecursiveAssertionAssert usingRecursiveAssertion(RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    return super.usingRecursiveAssertion(recursiveAssertionConfiguration);
  }

  /**
   * Verifies that the array under test contains a single element and allows to perform assertions on that element.
   * <p>
   * By default available assertions after {@code singleElement()} are {@code Object} assertions, it is possible though to
   * get more specific assertions by using {@link #singleElement(InstanceOfAssertFactory) singleElement(element assert factory)}
   * <p>
   * Example:
   * <pre><code class='java'> String[] babySimpsons = { "Maggie" };
   *
   * // assertion succeeds, only Object assertions are available after singleElement()
   * assertThat(babySimpsons).singleElement()
   *                         .isEqualTo("Maggie");
   *
   * // assertion fails
   * assertThat(babySimpsons).singleElement()
   *                         .isEqualTo("Homer");
   *
   * // assertion fails because list contains no elements
   * assertThat(emptyList()).singleElement();
   *
   *
   * // assertion fails because list contains more than one element
   * String[] simpsons = { "Homer", "Marge", "Lisa", "Bart", "Maggie" };
   * assertThat(simpsons).singleElement();</code></pre>
   *
   * @return the assertion on the first element
   * @throws AssertionError if the actual array does not contain exactly one element.
   * @see #singleElement(InstanceOfAssertFactory)
   * @since 3.22.0
   */
  @CheckReturnValue
  public ObjectAssert<ELEMENT> singleElement() {
    return internalSingleElement();
  }

  /**
   * Verifies that the array under test contains a single element and allows to perform assertions on that element, 
   * the assertions are strongly typed according to the given {@link AssertFactory} parameter.
   * <p>
   * Example: use of {@code String} assertions after {@code singleElement(as(STRING))}
   * <pre><code class='java'> import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
   * import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
   * import static org.assertj.core.api.Assertions.as; // syntactic sugar
   *
   * String[] babySimpsons = { "Maggie" };
   *
   * // assertion succeeds
   * assertThat(babySimpsons).singleElement(as(STRING))
   *                         .startsWith("Mag");
   *
   * // assertion fails
   * assertThat(babySimpsons).singleElement(as(STRING))
   *                         .startsWith("Lis");
   *
   * // assertion fails because of wrong factory type
   * assertThat(babySimpsons).singleElement(as(INTEGER))
   *                         .isZero();
   *
   * // assertion fails because list contains no elements
   * assertThat(emptyList()).singleElement(as(STRING));
   *
   * // assertion fails because list contains more than one element
   * String[] simpsons = { "Homer", "Marge", "Lisa", "Bart", "Maggie" };
   * assertThat(simpsons).singleElement(as(STRING));</code></pre>
   *
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the single element
   * @throws AssertionError if the actual array does not contain exactly one element.
   * @throws NullPointerException if the given factory is {@code null}.
   * @since 3.22.0
   */
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT singleElement(InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return internalSingleElement().asInstanceOf(assertFactory);
  }

  private ObjectAssert<ELEMENT> internalSingleElement() {
    arrays.assertHasSize(info, actual, 1);
    return toAssert(actual[0], navigationDescription("check single element"));
  }

  protected String navigationDescription(String propertyName) {
    String text = descriptionText();
    if (Strings.isNullOrEmpty(text)) {
      text = removeAssert(this.getClass().getSimpleName());
    }
    return text + " " + propertyName;
  }

  private static String removeAssert(String text) {
    return text.endsWith(ASSERT) ? text.substring(0, text.length() - ASSERT.length()) : text;
  }

  private ObjectAssert<ELEMENT> toAssert(ELEMENT value, String description) {
    return new ObjectAssert<>(value).as(description);
  }

  // lazy init TypeComparators
  protected TypeComparators getComparatorsByType() {
    if (comparatorsByType == null) comparatorsByType = defaultTypeComparators();
    return comparatorsByType;
  }

  // lazy init TypeComparators
  protected TypeComparators getComparatorsForElementPropertyOrFieldTypes() {
    if (comparatorsForElementPropertyOrFieldTypes == null) comparatorsForElementPropertyOrFieldTypes = defaultTypeComparators();
    return comparatorsForElementPropertyOrFieldTypes;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  SELF withAssertionState(AbstractAssert assertInstance) {
    if (assertInstance instanceof AbstractObjectArrayAssert) {
      AbstractObjectArrayAssert objectArrayAssert = (AbstractObjectArrayAssert) assertInstance;
      return (SELF) super.withAssertionState(assertInstance).withIterables(objectArrayAssert.iterables)
                                                            .withObjectArrays(objectArrayAssert.arrays)
                                                            .withTypeComparators(objectArrayAssert.comparatorsByType)
                                                            .withComparatorsForElementPropertyOrFieldNames(objectArrayAssert.comparatorsForElementPropertyOrFieldNames)
                                                            .withComparatorsForElementPropertyOrFieldTypes(objectArrayAssert.comparatorsForElementPropertyOrFieldTypes);
    }
    return super.withAssertionState(assertInstance);
  }

  SELF withIterables(Iterables iterables) {
    this.iterables = iterables;
    return myself;
  }

  SELF withObjectArrays(ObjectArrays arrays) {
    this.arrays = arrays;
    return myself;
  }

  SELF withTypeComparators(TypeComparators comparatorsByType) {
    this.comparatorsByType = comparatorsByType;
    return myself;
  }

  SELF withComparatorsForElementPropertyOrFieldNames(Map<String, Comparator<?>> comparatorsForElementPropertyOrFieldNames) {
    this.comparatorsForElementPropertyOrFieldNames = comparatorsForElementPropertyOrFieldNames;
    return myself;
  }

  SELF withComparatorsForElementPropertyOrFieldTypes(TypeComparators comparatorsForElementPropertyOrFieldTypes) {
    this.comparatorsForElementPropertyOrFieldTypes = comparatorsForElementPropertyOrFieldTypes;
    return myself;
  }

  // to implement to return the correct AbstractObjectArrayAssert subtype
  protected abstract SELF newObjectArrayAssert(ELEMENT[] array);

  /**
   * build a new ObjectArrayAssert from the filtered list propagating the assertion state (description, comparators, ...)
   * @param filteredList the list to convert
   * @return a new ObjectArrayAssert
   */
  private SELF newObjectArrayAssert(List<ELEMENT> filteredList) {
    ELEMENT[] filteredArray = toGenericArray(filteredList);
    return newObjectArrayAssert(filteredArray).withAssertionState(myself);
  }

  // this method assumes list size < actual array.
  @SuppressWarnings("unchecked")
  private ELEMENT[] toGenericArray(List<ELEMENT> filteredList) {
    // this is the only way to create a generic array:
    // - make a copy of an existing one (actual!) to the correct size
    // - fill the new array with some list elements
    ELEMENT[] actualCopy = (ELEMENT[]) copyOf(actual, filteredList.size(), actual.getClass());
    return filteredList.toArray(actualCopy);
  }

  private SELF internalFilteredOn(Predicate<? super ELEMENT> predicate) {
    checkArgument(predicate != null, "The filter predicate should not be null");
    List<ELEMENT> filteredList = stream(actual).filter(predicate).collect(toList());
    return newObjectArrayAssert(filteredList);
  }
}
