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

import static org.assertj.core.api.filter.Filters.filter;
import static org.assertj.core.description.Description.mostRelevantDescription;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOf;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOfMethod;
import static org.assertj.core.extractor.Extractors.resultOf;
import static org.assertj.core.internal.CommonValidations.checkSequenceIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkSubsequenceIsNotNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.assertj.core.api.filter.FilterOperator;
import org.assertj.core.api.filter.Filters;
import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.condition.Not;
import org.assertj.core.data.Index;
import org.assertj.core.description.Description;
import org.assertj.core.groups.FieldsOrPropertiesExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.AtomicReferenceArrayElementComparisonStrategy;
import org.assertj.core.internal.CommonErrors;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.FieldByFieldComparator;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.OnFieldsComparator;
import org.assertj.core.internal.RecursiveFieldByFieldComparator;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.VisibleForTesting;
import org.assertj.core.util.introspection.IntrospectionError;

public class AtomicReferenceArrayAssert<T>
    extends AbstractAssert<AtomicReferenceArrayAssert<T>, AtomicReferenceArray<T>>
    implements IndexedObjectEnumerableAssert<AtomicReferenceArrayAssert<T>, T>,
    ArraySortedAssert<AtomicReferenceArrayAssert<T>, T> {

  private T[] array;
  @VisibleForTesting
  ObjectArrays arrays = ObjectArrays.instance();
  @VisibleForTesting
  Iterables iterables = Iterables.instance();

  private TypeComparators comparatorsByType = new TypeComparators();
  private Map<String, Comparator<?>> comparatorsForElementPropertyOrFieldNames = new HashMap<>();
  private TypeComparators comparatorsForElementPropertyOrFieldTypes = new TypeComparators();

  public AtomicReferenceArrayAssert(AtomicReferenceArray<T> actual) {
    super(actual, AtomicReferenceArrayAssert.class);
    array = array(actual);
  }

  @Override
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> as(Description description) {
    return super.as(description);
  }

  @Override
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> as(String description, Object... args) {
    return super.as(description, args);
  }

  /**
   * Verifies that the AtomicReferenceArray is {@code null} or empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new AtomicReferenceArray&lt;&gt;(new String[0])).isNullOrEmpty();
   * AtomicReferenceArray array = null;
   * assertThat(array).isNullOrEmpty();
   * 
   * // assertion will fail
   * assertThat(new AtomicReferenceArray&lt;&gt;(new String[] {"a", "b", "c"})).isNullOrEmpty();</code></pre>
   *
   * @throws AssertionError if the AtomicReferenceArray is not {@code null} or not empty.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public void isNullOrEmpty() {
    if (actual == null) return;
    isEmpty();
  }

  /**
   * Verifies that the AtomicReferenceArray is empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicReferenceArray&lt;&gt;(new String[0])).isEmpty();
   * 
   * // assertion will fail
   * assertThat(new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"})).isEmpty();</code></pre>
   *
   * @throws AssertionError if the AtomicReferenceArray is not empty.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public void isEmpty() {
    arrays.assertEmpty(info, array);
  }

  /**
   * Verifies that the AtomicReferenceArray is not empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"})).isNotEmpty();
   * 
   * // assertion will fail
   * assertThat(new AtomicReferenceArray&lt;&gt;(new String[0])).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the AtomicReferenceArray is empty.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> isNotEmpty() {
    arrays.assertNotEmpty(info, array);
    return myself;
  }

  /**
   * Verifies that the AtomicReferenceArray has the given array.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; atomicArray = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * 
   * // assertion will pass
   * assertThat(atomicArray).hasArray(new String[]{"a", "b", "c"});
   * 
   * // assertion will fail
   * assertThat(atomicArray).hasArray(new String[]{"a", "b", "c", "d"});</code></pre>
   *
   * @param expected the array expected to be in the actual AtomicReferenceArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the AtomicReferenceArray does not have the given array.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicReferenceArrayAssert<T> hasArray(T[] expected) {
    arrays.assertContainsExactly(info, array, expected);
    return myself;
  }

  /**
   * Verifies that the number of values in the AtomicReferenceArray is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; atomicArray = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * 
   * assertThat(atomicArray).hasSize(3);
   * 
   * // assertion will fail
   * assertThat(atomicArray).hasSize(1);</code></pre>
   *
   * @param expected the expected number of values in the actual AtomicReferenceArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the AtomicReferenceArray is not equal to the given one.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> hasSize(int expected) {
    arrays.assertHasSize(info, array, expected);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray has the same size as the given array.
   * <p>
   * Parameter is declared as Object to accept both {@code Object[]} and primitive arrays (e.g. {@code int[]}).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * int[] fourFiveSix = {4, 5, 6};
   * int[] sevenEight = {7, 8};
   *
   * // assertion will pass
   * assertThat(abc).hasSameSizeAs(fourFiveSix);
   *
   * // assertion will fail
   * assertThat(abc).hasSameSizeAs(sevenEight);</code></pre>
   *
   * @param other the array to compare size with actual AtomicReferenceArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual AtomicReferenceArray and given array don't have the same size.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> hasSameSizeAs(Object other) {
    arrays.assertHasSameSizeAs(info, array, other);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray has the same size as the given {@link Iterable}.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertion will pass
   * assertThat(abc).hasSameSizeAs(elvesRings);
   * 
   * // assertion will fail
   * assertThat(abc).hasSameSizeAs(Arrays.asList("a", "b"));</code></pre>
   *
   * @param other the {@code Iterable} to compare size with actual AtomicReferenceArray.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the other {@code Iterable} is {@code null}.
   * @throws AssertionError if actual AtomicReferenceArray and given {@code Iterable} don't have the same size.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> hasSameSizeAs(Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, array, other);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains the given values, in any order.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   *
   * // assertions will pass
   * assertThat(abc).contains("b", "a")
   *                .contains("b", "a", "b");
   *
   * // assertions will fail
   * assertThat(abc).contains("d");
   * assertThat(abc).contains("c", "d");</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given values.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> contains(@SuppressWarnings("unchecked") T... values) {
    arrays.assertContains(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains only the given values and nothing else, <b>in any order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   *
   * // assertions will pass
   * assertThat(abc).containsOnly("c", "b", "a")
   *                .containsOnly("a", "a", "b", "c", "c");
   *
   * // assertion will fail because "c" is missing
   * assertThat(abc).containsOnly("a", "b");</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given values, i.e. the actual AtomicReferenceArray contains some
   *           or none of the given values, or the actual AtomicReferenceArray contains more values than the given ones.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsOnly(@SuppressWarnings("unchecked") T... values) {
    arrays.assertContainsOnly(info, array, values);
    return myself;
  }

  /**
   * Same semantic as {@link #containsOnly(Object[])} : verifies that actual contains all elements of the given
   * {@code Iterable} and nothing else, <b>in any order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; rings = new AtomicReferenceArray&lt;&gt;(new Ring[]{nenya, vilya});
   *
   * // assertions will pass
   * assertThat(rings).containsOnlyElementsOf(newArrayList(nenya, vilya))
   *                  .containsOnlyElementsOf(newArrayList(nenya, nenya, vilya, vilya));
   *
   * // assertion will fail as actual does not contain narya
   * assertThat(rings).containsOnlyElementsOf(newArrayList(nenya, vilya, narya));
   * // assertion will fail as actual contains nenya
   * assertThat(rings).containsOnlyElementsOf(newArrayList(vilya));</code></pre>
   *
   * @param iterable the given {@code Iterable} we will get elements from.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsOnlyElementsOf(Iterable<? extends T> iterable) {
    return containsOnly(toArray(iterable));
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains only null elements and nothing else.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * AtomicReferenceArray&lt;String&gt; items = new AtomicReferenceArray&lt;&gt;(new String[]{null, null, null});
   * assertThat(items).containsOnlyNulls();
   *
   * // assertion will fail because items2 contains not null element
   * AtomicReferenceArray&lt;String&gt; items2 = new AtomicReferenceArray&lt;&gt;(new String[]{null, null, "notNull"});
   * assertThat(items2).containsOnlyNulls();
   * 
   * // assertion will fail since an empty array does not contain any element and therefore no null ones.
   * AtomicReferenceArray&lt;String&gt; empty = new AtomicReferenceArray&lt;&gt;(new String[0]);
   * assertThat(empty).containsOnlyNulls();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray is empty or contains non null elements.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsOnlyNulls() {
    arrays.assertContainsOnlyNulls(info, array);
    return myself;
  }

  /**
   * An alias of {@link #containsOnlyElementsOf(Iterable)} : verifies that actual contains all elements of the
   * given {@code Iterable} and nothing else, <b>in any order</b>.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
   *
   * // assertions will pass:
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya, vilya))
   *                       .hasSameElementsAs(newArrayList(nenya, narya, vilya, nenya));
   *
   * // assertions will fail:
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya));
   * assertThat(elvesRings).hasSameElementsAs(newArrayList(nenya, narya, vilya, oneRing));</code></pre>
   *
   * @param iterable the {@code Iterable} whose elements we expect to be present
   * @return this assertion object
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}
   * @throws NullPointerException if the given {@code Iterable} is {@code null}
   * @throws AssertionError if the actual {@code Iterable} does not have the same elements, in any order, as the given
   *           {@code Iterable}
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> hasSameElementsAs(Iterable<? extends T> iterable) {
    return containsOnlyElementsOf(iterable);
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains the given values only once.
   * <p>
   * Examples :
   * <pre><code class='java'> // array is a factory method to create arrays.
   * AtomicReferenceArray&lt;String&gt; got = new AtomicReferenceArray&lt;&gt;(new String[]{&quot;winter&quot;, &quot;is&quot;, &quot;coming&quot;});
   *
   * // assertions will pass
   * assertThat(got).containsOnlyOnce(&quot;winter&quot;)
   *                .containsOnlyOnce(&quot;coming&quot;, &quot;winter&quot;);
   *
   * // assertions will fail
   * AtomicReferenceArray&lt;String&gt; stark= new AtomicReferenceArray&lt;&gt;(new String[]{&quot;Arya&quot;, &quot;Stark&quot;, &quot;daughter&quot;, &quot;of&quot;, &quot;Ned&quot;, &quot;Stark&quot;)});
   * assertThat(got).containsOnlyOnce(&quot;Lannister&quot;);
   * assertThat(stark).containsOnlyOnce(&quot;Stark&quot;);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given values, i.e. the actual AtomicReferenceArray contains some
   *           or none of the given values, or the actual AtomicReferenceArray contains more than once these values.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsOnlyOnce(@SuppressWarnings("unchecked") T... values) {
    arrays.assertContainsOnlyOnce(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains only the given values and nothing else, <b>in order</b>.<br>
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
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
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given values with same order, i.e. the actual AtomicReferenceArray
   *           contains some or none of the given values, or the actual AtomicReferenceArray contains more values than the given ones
   *           or values are the same but the order is not.
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsExactly(@SuppressWarnings("unchecked") T... values) {
    arrays.assertContainsExactly(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains exactly the given values and nothing else, <b>in any order</b>.<br>
   *
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactlyInAnyOrder(vilya, vilya, nenya, narya);
   *
   * // assertion will fail as vilya is contained twice in elvesRings.
   * assertThat(elvesRings).containsExactlyInAnyOrder(nenya, vilya, narya);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given values, i.e. it
   *           contains some or none of the given values, or more values than the given ones.
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsExactlyInAnyOrder(@SuppressWarnings("unchecked") T... values) {
    arrays.assertContainsExactlyInAnyOrder(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains exactly the given values and nothing else, <b>in any order</b>.<br>
   *
   * <p>
   * Example :
   * <pre><code class='java'>
   * AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray(new Ring[]{vilya, nenya, narya, vilya});
   * AtomicReferenceArray&lt;Ring&gt; elvesRingsSomeMissing = new AtomicReferenceArray(new Ring[]{vilya, nenya, narya});
   * AtomicReferenceArray&lt;Ring&gt; elvesRingsDifferentOrder = new AtomicReferenceArray(new Ring[]{nenya, narya, vilya, vilya});
   *
   * // assertion will pass
   * assertThat(elvesRings).containsExactlyInAnyOrder(elvesRingsDifferentOrder);
   *
   * // assertion will fail as vilya is contained twice in elvesRings.
   * assertThat(elvesRings).containsExactlyInAnyOrder(elvesRingsSomeMissing);</code></pre>
   *
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group
   *           contains some or none of the given values, or the actual group contains more values than the given ones.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsExactlyInAnyOrderElementsOf(Iterable<? extends T> values) {
    return containsExactlyInAnyOrder(toArray(values));
  }

  /**
   * Same as {@link #containsExactly(Object...)} but handles the {@link Iterable} to array conversion : verifies that
   * actual contains all elements of the given {@code Iterable} and nothing else <b>in the same order</b>.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
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
  public AtomicReferenceArrayAssert<T> containsExactlyElementsOf(Iterable<? extends T> iterable) {
    return containsExactly(toArray(iterable));
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains the given sequence in the correct order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #containsSubsequence(Object...)} to allow values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
   *
   * // assertion will pass
   * assertThat(elvesRings).containsSequence(vilya, nenya)
   *                       .containsSequence(nenya, narya);
   *
   * // assertions will fail, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).containsSequence(vilya, narya);
   * assertThat(elvesRings).containsSequence(nenya, vilya);</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given sequence.
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsSequence(@SuppressWarnings("unchecked") T... sequence) {
    arrays.assertContainsSequence(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains the given sequence in the correct order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #containsSubsequence(Object...)} to allow values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
   *
   * // assertion will pass
   * assertThat(elvesRings).containsSequence(newArrayList(vilya, nenya))
   *                       .containsSequence(newArrayList(nenya, narya));
   *
   * // assertions will fail, the elements order is correct but there is a value between them (nenya)
   * assertThat(elvesRings).containsSequence(newArrayList(vilya, narya));
   * assertThat(elvesRings).containsSequence(newArrayList(nenya, vilya));</code></pre>
   *
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given sequence.
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsSequence(Iterable<? extends T> sequence) {
    checkSequenceIsNotNull(sequence);
    arrays.assertContainsSequence(info, array, toArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains the given sequence in the given order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #doesNotContainSubsequence(Object...)} to also ensure the sequence does not exist with values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
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
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given sequence.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotContainSequence(@SuppressWarnings("unchecked") T... sequence) {
    arrays.assertDoesNotContainSequence(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains the given sequence in the given order and <b>without extra values between the sequence values</b>.
   * <p>
   * Use {@link #doesNotContainSubsequence(Iterable)} to also ensure the sequence does not exist with values between the expected sequence values.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
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
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given sequence.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotContainSequence(Iterable<? extends T> sequence) {
    checkSequenceIsNotNull(sequence);
    arrays.assertDoesNotContainSequence(info, array, toArray(sequence));
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains the given subsequence in the correct order (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
   *
   * // assertions will pass
   * assertThat(elvesRings).containsSubsequence(vilya, nenya)
   *                       .containsSubsequence(vilya, narya);
   *
   * // assertion will fail
   * assertThat(elvesRings).containsSubsequence(nenya, vilya);</code></pre>
   *
   * @param subsequence the subsequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given subsequence.
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsSubsequence(@SuppressWarnings("unchecked") T... subsequence) {
    arrays.assertContainsSubsequence(info, array, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains the given subsequence in the correct order (possibly with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
   *
   * // assertions will pass
   * assertThat(elvesRings).containsSubsequence(newArrayList(vilya, nenya))
   *                       .containsSubsequence(newArrayList(vilya, narya));
   *
   * // assertion will fail
   * assertThat(elvesRings).containsSubsequence(newArrayList(nenya, vilya));</code></pre>
   *
   * @param subsequence the subsequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given subsequence.
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsSubsequence(Iterable<? extends T> subsequence) {
    checkSubsequenceIsNotNull(subsequence);
    arrays.assertContainsSubsequence(info, array, toArray(subsequence));
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray does not contain the given subsequence in the correct order (possibly
   * with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
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
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray contains the given subsequence.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotContainSubsequence(@SuppressWarnings("unchecked") T... subsequence) {
    arrays.assertDoesNotContainSubsequence(info, array, subsequence);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray does not contain the given subsequence in the correct order (possibly
   * with other values between them).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
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
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray contains the given subsequence.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotContainSubsequence(Iterable<? extends T> subsequence) {
    checkSubsequenceIsNotNull(subsequence);
    arrays.assertDoesNotContainSubsequence(info, array, toArray(subsequence));
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains the given object at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
   *
   * // assertions will pass
   * assertThat(elvesRings).contains(vilya, atIndex(0))
   *                       .contains(nenya, atIndex(1))
   *                       .contains(narya, atIndex(2));
   *
   * // assertions will fail
   * assertThat(elvesRings).contains(vilya, atIndex(1));
   * assertThat(elvesRings).contains(nenya, atIndex(2));
   * assertThat(elvesRings).contains(narya, atIndex(0));</code></pre>
   *
   * @param value the object to look for.
   * @param index the index where the object should be stored in the actual AtomicReferenceArray.
   * @return this assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of the actual
   *           AtomicReferenceArray.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain the given object at the given index.
   */
  @Override
  public AtomicReferenceArrayAssert<T> contains(T value, Index index) {
    arrays.assertContains(info, array, value, index);
    return myself;
  }

  /**
   * Verifies that all elements of the actual group are instances of given classes or interfaces.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Object&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Object[]{"", new StringBuilder()});
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
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if not all elements of the actual group are instances of one of the given types
   * @since 2.7.0 / 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> hasOnlyElementsOfTypes(Class<?>... types) {
    arrays.assertHasOnlyElementsOfTypes(info, array, types);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray does not contain the given object at the given index.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
   *
   * // assertions will pass
   * assertThat(elvesRings).doesNotContain(vilya, atIndex(1))
   *                       .doesNotContain(nenya, atIndex(2))
   *                       .doesNotContain(narya, atIndex(0));
   *
   * // assertions will fail
   * assertThat(elvesRings).doesNotContain(vilya, atIndex(0));
   * assertThat(elvesRings).doesNotContain(nenya, atIndex(1));
   * assertThat(elvesRings).doesNotContain(narya, atIndex(2));</code></pre>
   *
   * @param value the object to look for.
   * @param index the index where the object should not be stored in the actual AtomicReferenceArray.
   * @return this assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray contains the given object at the given index.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotContain(T value, Index index) {
    arrays.assertDoesNotContain(info, array, value, index);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray does not contain the given values.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
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
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray contains any of the given values.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotContain(@SuppressWarnings("unchecked") T... values) {
    arrays.assertDoesNotContain(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray does not contain any elements of the given {@link Iterable} (i.e. none).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   *
   * // assertion will pass
   * assertThat(actual).doesNotContainAnyElementsOf(newArrayList("d", "e"));
   *
   * // assertions will fail
   * assertThat(actual).doesNotContainAnyElementsOf(newArrayList("a", "b"));
   * assertThat(actual).doesNotContainAnyElementsOf(newArrayList("d", "e", "a"));</code></pre>
   *
   * @param iterable the {@link Iterable} whose elements must not be in the actual AtomicReferenceArray.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty iterable.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray contains some elements of the given {@link Iterable}.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotContainAnyElementsOf(Iterable<? extends T> iterable) {
    arrays.assertDoesNotContainAnyElementsOf(info, array, iterable);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray does not contain duplicates.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * AtomicReferenceArray&lt;String&gt; aaa = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "a", "a"});
   *
   * // assertion will pass
   * assertThat(abc).doesNotHaveDuplicates();
   *
   * // assertion will fail
   * assertThat(aaa).doesNotHaveDuplicates();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray contains duplicates.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotHaveDuplicates() {
    arrays.assertDoesNotHaveDuplicates(info, array);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray starts with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the first element in the
   * sequence is also the first element of the actual AtomicReferenceArray.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
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
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not start with the given sequence of objects.
   */
  @Override
  public AtomicReferenceArrayAssert<T> startsWith(@SuppressWarnings("unchecked") T... sequence) {
    arrays.assertStartsWith(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual AtomicReferenceArray.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
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
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not end with the given sequence of objects.
   */
  @Override
  public AtomicReferenceArrayAssert<T> endsWith(T first, @SuppressWarnings("unchecked") T... sequence) {
    arrays.assertEndsWith(info, array, first, sequence);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual AtomicReferenceArray.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   *
   * // assertions will pass
   * assertThat(abc).endsWith(new String[0])
   *                .endsWith(new String[] {"b", "c"});
   *
   * // assertion will fail
   * assertThat(abc).endsWith(new String[] {"a"});</code></pre>
   *
   * @param sequence the (possibly empty) sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not end with the given sequence of objects.
   */
  @Override
  public AtomicReferenceArrayAssert<T> endsWith(T[] sequence) {
    arrays.assertEndsWith(info, array, sequence);
    return myself;
  }

  /**
   * Verifies that all elements of actual are present in the given {@code Iterable}.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
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
  public AtomicReferenceArrayAssert<T> isSubsetOf(Iterable<? extends T> values) {
    arrays.assertIsSubsetOf(info, array, values);
    return myself;
  }

  /**
   * Verifies that all elements of actual are present in the given values.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Ring&gt; elvesRings = new AtomicReferenceArray&lt;&gt;(new Ring[]{vilya, nenya, narya});
   *
   * // assertions will pass:
   * assertThat(elvesRings).isSubsetOf(vilya, nenya, narya)
   *                       .isSubsetOf(vilya, nenya, narya, dwarfRing);
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
  public AtomicReferenceArrayAssert<T> isSubsetOf(@SuppressWarnings("unchecked") T... values) {
    arrays.assertIsSubsetOf(info, array, Arrays.asList(values));
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains at least a null element.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * AtomicReferenceArray&lt;String&gt; abNull = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", null});
   *
   * // assertion will pass
   * assertThat(abNull).containsNull();
   *
   * // assertion will fail
   * assertThat(abc).containsNull();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain a null element.
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsNull() {
    arrays.assertContainsNull(info, array);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray does not contain null elements.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * AtomicReferenceArray&lt;String&gt; abNull = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", null});
   *
   * // assertion will pass
   * assertThat(abc).doesNotContainNull();
   *
   * // assertion will fail
   * assertThat(abNull).doesNotContainNull();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray contains a null element.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotContainNull() {
    arrays.assertDoesNotContainNull(info, array);
    return myself;
  }

  /**
   * Verifies that each element value satisfies the given condition
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * AtomicReferenceArray&lt;String&gt; abcc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "cc"});
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
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements don't satisfy the given condition.
   */
  @Override
  public AtomicReferenceArrayAssert<T> are(Condition<? super T> condition) {
    arrays.assertAre(info, array, condition);
    return myself;
  }

  /**
   * Verifies that each element value does not satisfy the given condition
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * AtomicReferenceArray&lt;String&gt; abcc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "cc"});
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
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements satisfy the given condition.
   */
  @Override
  public AtomicReferenceArrayAssert<T> areNot(Condition<? super T> condition) {
    arrays.assertAreNot(info, array, condition);
    return myself;
  }

  /**
   * Verifies that all elements satisfy the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * AtomicReferenceArray&lt;String&gt; abcc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "cc"});
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
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements do not satisfy the given condition.
   */
  @Override
  public AtomicReferenceArrayAssert<T> have(Condition<? super T> condition) {
    arrays.assertHave(info, array, condition);
    return myself;
  }

  /**
   * Verifies that all elements don't satisfy the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
   * AtomicReferenceArray&lt;String&gt; abcc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "cc"});
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
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements satisfy the given condition.
   */
  @Override
  public AtomicReferenceArrayAssert<T> doNotHave(Condition<? super T> condition) {
    arrays.assertDoNotHave(info, array, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>at least</b> <i>n</i> elements in the actual AtomicReferenceArray satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Integer&gt; oneThwoThree = new AtomicReferenceArray&lt;&gt;(new Integer[]{1, 2, 3});
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneThwoThree.areAtLeast(2, oddNumber);
   *
   * // assertion will fail
   * oneThwoThree.areAtLeast(3, oddNumber);</code></pre>
   *
   * @param times the minimum number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element can not be cast to T.
   * @throws AssertionError if the number of elements satisfying the given condition is &lt; n.
   */
  @Override
  public AtomicReferenceArrayAssert<T> areAtLeast(int times, Condition<? super T> condition) {
    arrays.assertAreAtLeast(info, array, times, condition);
    return myself;
  }

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual AtomicReferenceArray satisfying the given condition.
   * <p>
   * This method is an alias for {@code areAtLeast(1, condition)}.
   * <p>
   * Example:
   * <pre><code class='java'> // jedi is a Condition&lt;String&gt;
   * AtomicReferenceArray&lt;String&gt; rebels = new AtomicReferenceArray&lt;&gt;(new String[]{"Luke", "Solo", "Leia"});
   * 
   * assertThat(rebels).areAtLeastOne(jedi);</code></pre>
   *
   * @see #haveAtLeast(int, Condition)
   */
  @Override
  public AtomicReferenceArrayAssert<T> areAtLeastOne(Condition<? super T> condition) {
    areAtLeast(1, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>at most</b> <i>n</i> elements in the actual AtomicReferenceArray satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Integer&gt; oneThwoThree = new AtomicReferenceArray&lt;&gt;(new Integer[]{1, 2, 3});
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertions will pass
   * oneThwoThree.areAtMost(2, oddNumber);
   * oneThwoThree.areAtMost(3, oddNumber);
   *
   * // assertion will fail
   * oneThwoThree.areAtMost(1, oddNumber);</code></pre>
   *
   * @param times the number of times the condition should be at most verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  @Override
  public AtomicReferenceArrayAssert<T> areAtMost(int times, Condition<? super T> condition) {
    arrays.assertAreAtMost(info, array, times, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>exactly</b> <i>n</i> elements in the actual AtomicReferenceArray satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Integer&gt; oneThwoThree = new AtomicReferenceArray&lt;&gt;(new Integer[]{1, 2, 3});
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneThwoThree.areExactly(2, oddNumber);
   *
   * // assertions will fail
   * oneThwoThree.areExactly(1, oddNumber);
   * oneThwoThree.areExactly(3, oddNumber);</code></pre>
   *
   * @param times the exact number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  @Override
  public AtomicReferenceArrayAssert<T> areExactly(int times, Condition<? super T> condition) {
    arrays.assertAreExactly(info, array, times, condition);
    return myself;
  }

  /**
   * Verifies that there is <b>at least <i>one</i></b> element in the actual AtomicReferenceArray satisfying the given condition.
   * <p>
   * This method is an alias for {@code haveAtLeast(1, condition)}.
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;BasketBallPlayer&gt; bullsPlayers = new AtomicReferenceArray&lt;&gt;(new BasketBallPlayer[]{butler, rose});
   *
   * // potentialMvp is a Condition&lt;BasketBallPlayer&gt;
   * assertThat(bullsPlayers).haveAtLeastOne(potentialMvp);</code></pre>
   *
   * @see #haveAtLeast(int, Condition)
   */
  @Override
  public AtomicReferenceArrayAssert<T> haveAtLeastOne(Condition<? super T> condition) {
    return haveAtLeast(1, condition);
  }

  /**
   * Verifies that there are <b>at least <i>n</i></b> elements in the actual AtomicReferenceArray satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Integer&gt; oneThwoThree = new AtomicReferenceArray&lt;&gt;(new Integer[]{1, 2, 3});
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneThwoThree.haveAtLeast(2, oddNumber);
   *
   * // assertion will fail
   * oneThwoThree.haveAtLeast(3, oddNumber);</code></pre>
   *
   * This method is an alias for {@link #areAtLeast(int, Condition)}.
   */
  @Override
  public AtomicReferenceArrayAssert<T> haveAtLeast(int times, Condition<? super T> condition) {
    arrays.assertHaveAtLeast(info, array, times, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>at most</b> <i>n</i> elements in the actual AtomicReferenceArray satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Integer&gt; oneThwoThree = new AtomicReferenceArray&lt;&gt;(new Integer[]{1, 2, 3});
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertions will pass
   * oneThwoThree.haveAtMost(2, oddNumber);
   * oneThwoThree.haveAtMost(3, oddNumber);
   *
   * // assertion will fail
   * oneThwoThree.haveAtMost(1, oddNumber);</code></pre>
   *
   * This method is an alias {@link #areAtMost(int, Condition)}.
   */
  @Override
  public AtomicReferenceArrayAssert<T> haveAtMost(int times, Condition<? super T> condition) {
    arrays.assertHaveAtMost(info, array, times, condition);
    return myself;
  }

  /**
   * Verifies that there are <b>exactly</b> <i>n</i> elements in the actual AtomicReferenceArray satisfying the given condition.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;Integer&gt; oneThwoThree = new AtomicReferenceArray&lt;&gt;(new Integer[]{1, 2, 3});
   *
   * Condition&lt;Integer&gt; oddNumber = new Condition&lt;&gt;(value % 2 == 1, "odd number");
   *
   * // assertion will pass
   * oneThwoThree.haveExactly(2, oddNumber);
   *
   * // assertions will fail
   * oneThwoThree.haveExactly(1, oddNumber);
   * oneThwoThree.haveExactly(3, oddNumber);</code></pre>
   *
   * This method is an alias {@link #areExactly(int, Condition)}.
   */
  @Override
  public AtomicReferenceArrayAssert<T> haveExactly(int times, Condition<? super T> condition) {
    arrays.assertHaveExactly(info, array, times, condition);
    return myself;
  }

  /**
   * Verifies that at least one element in the actual AtomicReferenceArray has the specified type (matching
   * includes subclasses of the given type).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Number&gt; numbers = new AtomicReferenceArray&lt;&gt;(new Number[]{ 2, 6L, 8.0 });
   * 
   * // successful assertion:
   * assertThat(numbers).hasAtLeastOneElementOfType(Long.class);
   * 
   * // assertion failure:
   * assertThat(numbers).hasAtLeastOneElementOfType(Float.class);</code></pre>
   *
   * @param expectedType the expected type.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not have any elements of the given type.
   */
  @Override
  public AtomicReferenceArrayAssert<T> hasAtLeastOneElementOfType(Class<?> expectedType) {
    arrays.assertHasAtLeastOneElementOfType(info, array, expectedType);
    return myself;
  }

  /**
   * Verifies that all the elements in the actual AtomicReferenceArray belong to the specified type (matching includes
   * subclasses of the given type).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Number&gt; numbers = new AtomicReferenceArray&lt;&gt;(new Number[]{ 2, 6, 8 });
   * 
   * // successful assertion:
   * assertThat(numbers).hasOnlyElementsOfType(Integer.class);
   * 
   * // assertion failure:
   * assertThat(numbers).hasOnlyElementsOfType(Long.class);</code></pre>
   *
   * @param expectedType the expected type.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if one element is not of the expected type.
   */
  @Override
  public AtomicReferenceArrayAssert<T> hasOnlyElementsOfType(Class<?> expectedType) {
    arrays.assertHasOnlyElementsOfType(info, array, expectedType);
    return myself;
  }

  /**
   * Verifies that all the elements in the actual AtomicReferenceArray do not belong to the specified types (including subclasses).
   * <p>
   * Example:
   * <pre><code class='java'> AtomicReferenceArray&lt;Number&gt; numbers = new AtomicReferenceArray&lt;&gt;(new Number[]{ 2, 6, 8.0 });
   *
   * // successful assertion:
   * assertThat(numbers).doesNotHaveAnyElementsOfTypes(Long.class, Float.class);
   *
   * // assertion failure:
   * assertThat(numbers).doesNotHaveAnyElementsOfTypes(Long.class, Integer.class);</code></pre>
   *
   * @param unexpectedTypes the not expected types.
   * @return this assertion object.
   * @throws NullPointerException if the given types is {@code null}.
   * @throws AssertionError if one element's type matches the given types.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> doesNotHaveAnyElementsOfTypes(Class<?>... unexpectedTypes) {
    arrays.assertDoesNotHaveAnyElementsOfTypes(info, array, unexpectedTypes);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public AtomicReferenceArrayAssert<T> isSorted() {
    arrays.assertIsSorted(info, array);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public AtomicReferenceArrayAssert<T> isSortedAccordingTo(Comparator<? super T> comparator) {
    arrays.assertIsSortedAccordingToComparator(info, array, comparator);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains all the elements of given {@code Iterable}, in any order.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"});
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
   * @throws AssertionError if the actual AtomicReferenceArray is {@code null}.
   * @throws AssertionError if the actual AtomicReferenceArray does not contain all the elements of given {@code Iterable}.
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsAll(Iterable<? extends T> iterable) {
    arrays.assertContainsAll(info, array, iterable);
    return myself;
  }

  /**
   * Use given custom comparator instead of relying on actual element type <code>equals</code> method to compare AtomicReferenceArray
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
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
   */
  @Override
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> usingElementComparator(Comparator<? super T> elementComparator) {
    this.arrays = new ObjectArrays(new ComparatorBasedComparisonStrategy(elementComparator));
    objects = new Objects(new AtomicReferenceArrayElementComparisonStrategy<>(elementComparator));
    return myself;
  }


  private AtomicReferenceArrayAssert<T> usingExtendedByTypesElementComparator(Comparator<Object> elementComparator) {
    return usingElementComparator(new ExtendedByTypesComparator(elementComparator, comparatorsByType));
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> usingDefaultElementComparator() {
    this.arrays = ObjectArrays.instance();
    return myself;
  }

  /**
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
   * AtomicReferenceArray&lt;TolkienCharacter&gt; hobbits = new AtomicReferenceArray&lt;&gt;(new TolkienCharacter[]{frodo});
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
   * @param <C> the type to compare.
   * @param comparator the {@link java.util.Comparator} to use
   * @param elementPropertyOrFieldNames the names of the properties and/or fields of the elements the comparator should be used for
   * @return {@code this} assertions object
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public <C> AtomicReferenceArrayAssert<T> usingComparatorForElementFieldsWithNames(Comparator<C> comparator,
                                                                                    String... elementPropertyOrFieldNames) {
    for (String elementPropertyOrField : elementPropertyOrFieldNames) {
      comparatorsForElementPropertyOrFieldNames.put(elementPropertyOrField, comparator);
    }
    return myself;
  }

  /**
   * Allows to set a specific comparator to compare properties or fields of elements with the given type.
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
   * AtomicReferenceArray&lt;TolkienCharacter&gt; hobbits = new AtomicReferenceArray&lt;&gt;(new TolkienCharacter[]{frodo});
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
   * @param <C> the type to compare.
   * @param comparator the {@link java.util.Comparator} to use
   * @param type the {@link java.lang.Class} of the type of the element fields the comparator should be used for
   * @return {@code this} assertions object
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public <C> AtomicReferenceArrayAssert<T> usingComparatorForElementFieldsWithType(Comparator<C> comparator,
                                                                                   Class<C> type) {
    comparatorsForElementPropertyOrFieldTypes.put(type, comparator);
    return myself;
  }

  /**
   * Allows to set a specific comparator for the given type of elements or their fields.
   * Extends {@link #usingComparatorForElementFieldsWithType} by applying comparator specified for given type
   * to elements themselves, not only to their fields.
   * <p>
   * Usage of this method affects comparators set by next methods:
   * <ul>
   * <li>{@link #usingFieldByFieldElementComparator}</li>
   * <li>{@link #usingElementComparatorOnFields}</li>
   * <li>{@link #usingElementComparatorIgnoringFields}</li>
   * <li>{@link #usingRecursiveFieldByFieldElementComparator}</li>
   * </ul>
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicReferenceArray&lt;&gt;(new Object[] { "some", new BigDecimal("4.2") }))
   *       .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
   *       .contains(new BigDecimal("4.20"));
   * </code></pre>
   *
   * @param <C> the type to compare.
   * @param comparator the {@link java.util.Comparator} to use
   * @param type the {@link java.lang.Class} of the type of the element or element fields the comparator should be used for
   * @return {@code this} assertions object
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  public <C> AtomicReferenceArrayAssert<T> usingComparatorForType(Comparator<C> comparator, Class<C> type) {
    if (arrays.getComparator() == null) {
      usingElementComparator(new ExtendedByTypesComparator(comparatorsByType));
    }

    comparatorsForElementPropertyOrFieldTypes.put(type, comparator);
    comparatorsByType.put(type, comparator);

    return myself;
  }

  /**
   * Use field/property by field/property comparison (including inherited fields/properties) instead of relying on
   * actual type A <code>equals</code> method to compare AtomicReferenceArray elements for incoming assertion checks. Private fields
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
   * </p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   *
   * // Fail if equals has not been overridden in TolkienCharacter as equals default implementation only compares references
   * assertThat(atomicArray(frodo)).contains(frodoClone);
   *
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(atomicArray(frodo)).usingFieldByFieldElementComparator().contains(frodoClone);</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> usingFieldByFieldElementComparator() {
    return usingExtendedByTypesElementComparator(new FieldByFieldComparator(comparatorsForElementPropertyOrFieldNames,
                                                             comparatorsForElementPropertyOrFieldTypes));
  }

  /**
   * Use a recursive field/property by field/property comparison (including inherited fields/properties)
   * instead of relying on actual type A <code>equals</code> method to compare AtomicReferenceArray elements for incoming
   * assertion checks. This can be useful if actual's {@code equals} implementation does not suit you.
   * <p>
   * The recursive property/field comparison is <b>not</b> applied on fields having a custom {@code equals}
   * implementation, i.e. the overridden {@code equals} method will be used instead of a field/property by field/property comparison.
   * <p>
   * You can specify a custom comparator per (nested) name or type of element field with
   * {@link #usingComparatorForElementFieldsWithNames(Comparator, String...) usingComparatorForElementFieldsWithNames}
   * and {@link #usingComparatorForElementFieldsWithType(Comparator, Class) usingComparatorForElementFieldsWithType}.
   * <p>
   * The recursive comparison handles cycles.
   * <p>
   * The objects to compare can be of different types but must have the same properties/fields. For example if actual object has a
   * {@code name} String field, the other object must also have one.
   * <p>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p>
   * Example:
   *
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter pippin = new TolkienCharacter("Pippin", 28, HOBBIT);
   * frodo.setFriend(pippin);
   * pippin.setFriend(frodo);
   *
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter pippinClone = new TolkienCharacter("Pippin", 28, HOBBIT);
   * frodoClone.setFriend(pippinClone);
   * pippinClone.setFriend(frodoClone);
   *
   * AtomicReferenceArray&lt;TolkienCharacter&gt; hobbits = new AtomicReferenceArray&lt;&gt;(new TolkienCharacter[] {frodo, pippin});
   *
   * // fails if equals has not been overridden in TolkienCharacter as it would compares object references
   * assertThat(hobbits).contains(frodoClone, pippinClone);
   *
   * // frodo/frodoClone and pippin/pippinClone are equals when doing a recursive property/field by property/field comparison
   * assertThat(hobbits).usingRecursiveFieldByFieldElementComparator()
   *                    .contains(frodoClone, pippinClone);</code></pre>
   *
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> usingRecursiveFieldByFieldElementComparator() {
    return usingExtendedByTypesElementComparator(new RecursiveFieldByFieldComparator(comparatorsForElementPropertyOrFieldNames,
                                                                      comparatorsForElementPropertyOrFieldTypes));
  }

  /**
   * Use field/property by field/property comparison on the <b>given fields/properties only</b> (including inherited
   * fields/properties) instead of relying on actual type A <code>equals</code> method to compare AtomicReferenceArray elements for
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
   * </p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   *
   * // frodo and sam both are hobbits, so they are equals when comparing only race
   * assertThat(atomicArray(frodo)).usingElementComparatorOnFields("race").contains(sam); // OK
   *
   * // ... but not when comparing both name and race
   * assertThat(atomicArray(frodo)).usingElementComparatorOnFields("name", "race").contains(sam); // FAIL</code></pre>
   *
   * @param fields the fields to compare field/property by field/property.
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> usingElementComparatorOnFields(String... fields) {
    return usingExtendedByTypesElementComparator(new OnFieldsComparator(comparatorsForElementPropertyOrFieldNames,
                                                         comparatorsForElementPropertyOrFieldTypes, fields));
  }

  /**
   * Use field/property by field/property on all fields/properties <b>except</b> the given ones (including inherited
   * fields/properties) instead of relying on actual type A <code>equals</code> method to compare AtomicReferenceArray elements for
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
   * </p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   *
   * // frodo and sam both are hobbits, so they are equals when comparing only race (i.e. ignoring all other fields)
   * assertThat(atomicArray(frodo)).usingElementComparatorIgnoringFields("name", "age").contains(sam); // OK
   *
   * // ... but not when comparing both name and race
   * assertThat(atomicArray(frodo)).usingElementComparatorIgnoringFields("age").contains(sam); // FAIL</code></pre>
   *
   * @param fields the names of the fields/properties to ignore
   * @return {@code this} assertion object.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> usingElementComparatorIgnoringFields(String... fields) {
    return usingExtendedByTypesElementComparator(new IgnoringFieldsComparator(comparatorsForElementPropertyOrFieldNames,
                                                               comparatorsForElementPropertyOrFieldTypes, fields));
  }

  /**
   * Extract the values of given field or property from the array's elements under test into a new array, this new array
   * becoming the array under test.
   * <p>
   * It allows you to test a field/property of the array's elements instead of testing the elements themselves, which can
   * be much less work !
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build a array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
   * // they can be public field or properties, both works when extracting their values.
   * AtomicReferenceArray&lt;TolkienCharacter&gt; fellowshipOfTheRing = new AtomicReferenceArray&lt;&gt;(new TolkienCharacter[]{
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
   * @return a new assertion object whose object under test is the array of extracted field/property values.
   * @throws IntrospectionError if no field or property exists with the given name
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public ObjectArrayAssert<Object> extracting(String fieldOrProperty) {
    Object[] values = FieldsOrPropertiesExtractor.extract(array, byName(fieldOrProperty));
    String extractedDescription = extractedDescriptionOf(fieldOrProperty);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return new ObjectArrayAssert<>(values).as(description);
  }

  /**
   * Extract the values of given field or property from the array's elements under test into a new array, this new array
   * becoming the array under test with type.
   * <p>
   * It allows you to test a field/property of the array's elements instead of testing the elements themselves, which can
   * be much less work !
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build an array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
   * // they can be public field or properties, both works when extracting their values.
   * AtomicReferenceArray&lt;TolkienCharacter&gt; fellowshipOfTheRing = new AtomicReferenceArray&lt;&gt;(new TolkienCharacter[]{
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
   * @param <P> the extracted type
   * @param fieldOrProperty the field/property to extract from the array under test
   * @param extractingType type to return
   * @return a new assertion object whose object under test is the array of extracted field/property values.
   * @throws IntrospectionError if no field or property exists with the given name
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public <P> ObjectArrayAssert<P> extracting(String fieldOrProperty, Class<P> extractingType) {
    @SuppressWarnings("unchecked")
    P[] values = (P[]) FieldsOrPropertiesExtractor.extract(array, byName(fieldOrProperty));
    String extractedDescription = extractedDescriptionOf(fieldOrProperty);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return new ObjectArrayAssert<>(values).as(description);
  }

  /**
   * Extract the values of given fields/properties from the array's elements under test into a new array composed of
   * Tuple (a simple data structure), this new array becoming the array under test.
   * <p>
   * It allows you to test fields/properties of the the array's elements instead of testing the elements themselves, it
   * can be sometimes much less work !
   * <p>
   * The Tuple data corresponds to the extracted values of the given fields/properties, for instance if you ask to
   * extract "id", "name" and "email" then each Tuple data will be composed of id, name and email extracted from the
   * element of the initial array (the Tuple's data order is the same as the given fields/properties order).
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build an array of TolkienCharacter, a TolkienCharacter has a name (String) and a Race (a class)
   * // they can be public field or properties, both works when extracting their values.
   * AtomicReferenceArray&lt;TolkienCharacter&gt; fellowshipOfTheRing = new AtomicReferenceArray&lt;&gt;(new TolkienCharacter[]{
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
   * @return a new assertion object whose object under test is the list of Tuple with extracted properties/fields values
   *         as data.
   * @throws IntrospectionError if one of the given name does not match a field or property in one of the initial
   *         Iterable's element.
   */
  @CheckReturnValue
  public ObjectArrayAssert<Tuple> extracting(String... propertiesOrFields) {
    Object[] values = FieldsOrPropertiesExtractor.extract(array, byName(propertiesOrFields));
    Tuple[] result = Arrays.copyOf(values, values.length, Tuple[].class);
    String extractedDescription = extractedDescriptionOf(propertiesOrFields);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return new ObjectArrayAssert<>(result).as(description);
  }

  /**
   * Extract the values from the array's elements by applying an extracting function on them. The returned
   * array becomes a new object under test.
   * <p>
   * It allows to test values from the elements in safer way than by using {@link #extracting(String)}, as it
   * doesn't utilize introspection.
   * <p>
   * Let's take a look an example:
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * AtomicReferenceArray&lt;TolkienCharacter&gt; fellowshipOfTheRing = new AtomicReferenceArray&lt;&gt;(new TolkienCharacter[]{
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
   *
   * // this extracts the race
   * Extractor&lt;TolkienCharacter, Race&gt; race = new Extractor&lt;TolkienCharacter, Race&gt;() {
   *    {@literal @}Override
   *    public Race extract(TolkienCharacter input) {
   *        return input.getRace();
   *    }
   * }
   *
   * // fellowship has hobbits, right, my presioussss?
   * assertThat(fellowshipOfTheRing).extracting(race).contains(HOBBIT);</code></pre>
   *
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   *
   * @param <U> the extracted values type
   * @param extractor the object transforming input object to desired one
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public <U> ObjectArrayAssert<U> extracting(Extractor<? super T, U> extractor) {
    U[] extracted = FieldsOrPropertiesExtractor.extract(array, extractor);

    return new ObjectArrayAssert<>(extracted);
  }

  /**
   * Extract the values from the array's elements by applying an extracting function (which might throw an
   * exception) on them. The returned array becomes a new object under test.
   * <p>
   * Any checked exception raised in the extractor is rethrown wrapped in a {@link RuntimeException}.
   * <p>
   * It allows to test values from the elements in safer way than by using {@link #extracting(String)}, as it
   * doesn't utilize introspection.
   * <p>
   * Let's take a look an example:
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * AtomicReferenceArray&lt;TolkienCharacter&gt; fellowshipOfTheRing = new AtomicReferenceArray&lt;&gt;(new TolkienCharacter[]{
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
   *
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   *
   * @param <U> the extracted values type
   * @param <EXCEPTION> the exception type
   * @param extractor the object transforming input object to desired one
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.7.0
   */
  @CheckReturnValue
  public <U, EXCEPTION extends Exception> ObjectArrayAssert<U> extracting(ThrowingExtractor<? super T, U, EXCEPTION> extractor) {
    U[] extracted = FieldsOrPropertiesExtractor.extract(array, extractor);

    return new ObjectArrayAssert<>(extracted);
  }

  /**
   * Extract the Iterable values from the array's elements by applying an Iterable extracting function on them
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
   * Extractor&lt;CartoonCharacter, List&lt;CartoonCharacter&gt;&gt; childrenOf = new Extractor&lt;CartoonCharacter, List&lt;CartoonCharacter&gt;&gt;() {
   *    {@literal @}Override
   *    public List&lt;CartoonChildren&gt; extract(CartoonCharacter input) {
   *        return input.getChildren();
   *    }
   * }
   *
   * AtomicReferenceArray&lt;CartoonCharacter&gt; parents = new AtomicReferenceArray&lt;&gt;(new CartoonCharacter[]{ homer, fred });
   * // check children
   * assertThat(parents).flatExtracting(childrenOf)
   *                    .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The order of extracted values is consisted with both the order of the collection itself, as well as the extracted
   * collections.
   *
   * @param <U> the type of elements to extract.
   * @param <C> the type of collection to flat/extract.
   * @param extractor the object transforming input object to an Iterable of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public <U, C extends Collection<U>> ObjectArrayAssert<U> flatExtracting(Extractor<? super T, C> extractor) {
    return doFlatExtracting(extractor);
  }

  /**
   * Extract the Iterable values from the array's elements by applying an Iterable extracting function (which might
   * throw an exception) on them and concatenating the result lists into an array which becomes the new object under
   * test.
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
   * AtomicReferenceArray&lt;CartoonCharacter&gt; parents = new AtomicReferenceArray&lt;&gt;(new CartoonCharacter[]{ homer, fred });
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
   * @param <U> the type of elements to extract.
   * @param <C> the type of collection to flat/extract.
   * @param <EXCEPTION> the exception type
   * @param extractor the object transforming input object to an Iterable of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.7.0
   */
  @CheckReturnValue
  public <U, C extends Collection<U>, EXCEPTION extends Exception> ObjectArrayAssert<U> flatExtracting(ThrowingExtractor<? super T, C, EXCEPTION> extractor) {
    return doFlatExtracting(extractor);
  }

  private <U, C extends Collection<U>> ObjectArrayAssert<U> doFlatExtracting(Extractor<? super T, C> extractor) {
    final List<C> extractedValues = FieldsOrPropertiesExtractor.extract(Arrays.asList(array), extractor);

    final List<U> result = newArrayList();
    for (C e : extractedValues) {
      result.addAll(e);
    }

    return new ObjectArrayAssert<>(IterableUtil.toArray(result));
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
   * AtomicReferenceArray&lt;CartoonCharacter&gt; parents = new AtomicReferenceArray&lt;&gt;(new CartoonCharacter[]{ homer, fred });
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
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public ObjectArrayAssert<Object> flatExtracting(String propertyName) {
    List<Object> extractedValues = newArrayList();
    List<?> extractedGroups = FieldsOrPropertiesExtractor.extract(Arrays.asList(array), byName(propertyName));
    for (Object group : extractedGroups) {
      // expecting AtomicReferenceArray to be an iterable or an array
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
    return new ObjectArrayAssert<>(extractedValues.toArray());
  }

  /**
   * Extract the result of given method invocation from the array's elements under test into a new array, this new array
   * becoming the array under test.
   * <p>
   * It allows you to test a method results of the array's elements instead of testing the elements themselves, which can be
   * much less work!
   * <p>
   * It is especially useful for classes that does not conform to the Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   * AtomicReferenceArray&lt;WesterosHouse&gt; greatHousesOfWesteros = new AtomicReferenceArray&lt;&gt;(new WesterosHouse[]{ 
   *     new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Coming&quot;),
   *     new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;), 
   *     new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;),
   *     new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;), 
   *     new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;),
   *     new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;) });
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
   * @return a new assertion object whose object under test is the array of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void, or method accepts arguments.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public ObjectArrayAssert<Object> extractingResultOf(String method) {
    Object[] values = FieldsOrPropertiesExtractor.extract(array, resultOf(method));
    String extractedDescription = extractedDescriptionOfMethod(method);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return new ObjectArrayAssert<>(values).as(description);
  }

  /**
   * Extract the result of given method invocation from the array's elements under test into a new array, this new array
   * becoming the array under test.
   * <p>
   * It allows you to test a method results of the array's elements instead of testing the elements themselves, which can be
   * much less work!
   * <p>
   * It is especially useful for classes that do not conform to the Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   * AtomicReferenceArray&lt;WesterosHouse&gt; greatHousesOfWesteros = new AtomicReferenceArray&lt;&gt;(new WesterosHouse[]{ 
   *     new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Coming&quot;),
   *     new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;), 
   *     new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;),
   *     new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;), 
   *     new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;),
   *     new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;) });
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
   * @param <P> the extracted type
   * @param method the name of the method which result is to be extracted from the array under test
   * @param extractingType type to return
   * @return a new assertion object whose object under test is the array of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void, or method accepts arguments.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public <P> ObjectArrayAssert<P> extractingResultOf(String method, Class<P> extractingType) {
    @SuppressWarnings("unchecked")
    P[] values = (P[]) FieldsOrPropertiesExtractor.extract(array, resultOf(method));
    String extractedDescription = extractedDescriptionOfMethod(method);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return new ObjectArrayAssert<>(values).as(description);
  }

  /**
   * Enable hexadecimal object representation of Iterable elements instead of standard java representation in error
   * messages.
   * <p>
   * It can be useful to better understand what the error was with a more meaningful error message.
   * <p>
   * Example
   * <pre><code class='java'> 
   * AtomicReferenceArray&lt;Byte&gt; bytes = new AtomicReferenceArray&lt;&gt;(new Byte[]{ 0x10, 0x20 });
   * assertThat(bytes).inHexadecimal().contains(new Byte[] { 0x30 });</code></pre>
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
   * @since 2.7.0 / 3.7.0
   */
  @Override
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> inHexadecimal() {
    return super.inHexadecimal();
  }

  @Override
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> inBinary() {
    return super.inBinary();
  }

  /**
   * Filter the array under test keeping only elements having a property or field equal to {@code expectedValue}, the
   * property/field is specified by {@code propertyOrFieldName} parameter.
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
   * AtomicReferenceArray&lt;Employee&gt; employees = new AtomicReferenceArray&lt;&gt;(new Employee[]{ yoda, luke, obiwan, noname });
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
   * If you need more complex filter, use {@link #filteredOn(Condition)} and provide a {@link Condition} to specify the
   * filter to apply.
   *
   * @param propertyOrFieldName the name of the property or field to read
   * @param expectedValue the value to compare element's property or field with
   * @return a new assertion object with the filtered array under test
   * @throws IllegalArgumentException if the given propertyOrFieldName is {@code null} or empty.
   * @throws IntrospectionError if the given propertyOrFieldName can't be found in one of the array elements.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> filteredOn(String propertyOrFieldName, Object expectedValue) {
    Iterable<? extends T> filteredIterable = filter(array).with(propertyOrFieldName, expectedValue).get();
    return new AtomicReferenceArrayAssert<>(new AtomicReferenceArray<>(toArray(filteredIterable)));
  }

  /**
   * Filter the array under test keeping only elements whose property or field specified by {@code propertyOrFieldName}
   * is null.
   * <p>
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
   * AtomicReferenceArray&lt;Employee&gt; employees = new AtomicReferenceArray&lt;&gt;(new Employee[]{ yoda, luke, obiwan, noname });
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
   * <p>
   * If you need more complex filter, use {@link #filteredOn(Condition)} and provide a {@link Condition} to specify the
   * filter to apply.
   *
   * @param propertyOrFieldName the name of the property or field to read
   * @return a new assertion object with the filtered array under test
   * @throws IntrospectionError if the given propertyOrFieldName can't be found in one of the array elements.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> filteredOnNull(String propertyOrFieldName) {
    // need to cast nulll to Object otherwise it calls :
    // filteredOn(String propertyOrFieldName, FilterOperation<?> filterOperation)
    return filteredOn(propertyOrFieldName, (Object) null);
  }

  /**
   * Filter the array under test keeping only elements having a property or field matching the filter expressed with
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
   *
   * As an example, let's check stuff on some special employees :
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   *
   * AtomicReferenceArray&lt;Employee&gt; employees = new AtomicReferenceArray&lt;&gt;(new Employee[]{ yoda, luke, obiwan, noname });
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
   *
   * If you need more complex filter, use {@link #filteredOn(Condition)} and provide a {@link Condition} to specify the
   * filter to apply.
   *
   * @param propertyOrFieldName the name of the property or field to read
   * @param filterOperator the filter operator to apply
   * @return a new assertion object with the filtered array under test
   * @throws IllegalArgumentException if the given propertyOrFieldName is {@code null} or empty.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> filteredOn(String propertyOrFieldName, FilterOperator<?> filterOperator) {
    checkNotNull(filterOperator);
    Filters<? extends T> filter = filter(array).with(propertyOrFieldName);
    filterOperator.applyOn(filter);
    return new AtomicReferenceArrayAssert<>(new AtomicReferenceArray<>(toArray(filter.get())));
  }

  /**
   * Filter the array under test keeping only elements matching the given {@link Condition}.
   * <p>
   * Let's check old employees whose age &gt; 100:
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * AtomicReferenceArray&lt;Employee&gt; employees = new AtomicReferenceArray&lt;&gt;(new Employee[]{ yoda, luke, obiwan, noname });
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
   * @return a new assertion object with the filtered array under test
   * @throws IllegalArgumentException if the given condition is {@code null}.
   * @since 2.7.0 / 3.7.0
   */
  @CheckReturnValue
  public AtomicReferenceArrayAssert<T> filteredOn(Condition<? super T> condition) {
    Iterable<? extends T> filteredIterable = filter(array).being(condition).get();
    return new AtomicReferenceArrayAssert<>(new AtomicReferenceArray<>(toArray(filteredIterable)));
  }

  /**
   * Verifies that all elements match the given {@link Predicate}.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[] {"a", "b", "c"});
   * AtomicReferenceArray&lt;String&gt; abcc = new AtomicReferenceArray&lt;&gt;(new String[] {"a", "b", "cc"});
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
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements don't satisfy the given predicate.
   * @since 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> allMatch(Predicate<? super T> predicate) {
    iterables.assertAllMatch(info, newArrayList(array), predicate, PredicateDescription.GIVEN);
    return myself;
  }

  /**
   * Verifies that all the elements of actual's array match the given {@link Predicate}. The predicate description is used
   * to get an informative error message.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[] {"a", "b", "c"});
   * AtomicReferenceArray&lt;String&gt; abcc = new AtomicReferenceArray&lt;&gt;(new String[] {"a", "b", "cc"});
   *
   * // assertion will pass
   * assertThat(abc).allMatch(s -&gt; s.length() == 1, "length of 1");
   *
   * // assertion will fail
   * assertThat(abcc).allMatch(s -&gt; s.length() == 1, "length of 1");</code></pre>
   *
   * The message of the failed assertion would be:
   * <pre><code class='java'>Expecting all elements of:
   *  &lt;["a", "b", "cc"]&gt;
   *  to match 'length of 1' predicate but this element did not:
   *  &lt;"cc"&gt;</code></pre>
   *
   *
   * @param predicate the given {@link Predicate}.
   * @param predicateDescription a description of the {@link Predicate} used in the error message
   * @return {@code this} object.
   * @throws NullPointerException if the given predicate is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements don't satisfy the given predicate.
   * @since 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> allMatch(Predicate<? super T> predicate, String predicateDescription) {
    iterables.assertAllMatch(info, newArrayList(array), predicate, new PredicateDescription(predicateDescription));
    return myself;
  }

  /**
   * Verifies that all the elements satisfy given requirements expressed as a {@link Consumer}.
   * <p>
   * This is useful to perform a group of assertions on elements.
   * <p>
   * Grouping assertions example:
   * <pre><code class='java'> // myIcelanderFriends is an AtomicReferenceArray&lt;Person&gt; 
   * assertThat(myIcelanderFriends).allSatisfy(person -&gt; {
   *                                 assertThat(person.getCountry()).isEqualTo("Iceland");
   *                                 assertThat(person.getPhoneCountryCode()).isEqualTo("+354");
   *                               });</code></pre>
   *
   * @param requirements the given {@link Consumer}.
   * @return {@code this} object.
   * @throws NullPointerException if the given {@link Consumer} is {@code null}.
   * @throws AssertionError if one or more elements don't satisfy given requirements.
   * @since 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> allSatisfy(Consumer<? super T> requirements) {
    iterables.assertAllSatisfy(info, newArrayList(array), requirements);
    return myself;
  }

  /**
   * Verifies that at least one element satisfies the given requirements expressed as a {@link Consumer}.
   * <p>
   * This is useful to check that a group of assertions is verified by (at least) one element.
   * <p>
   * If the {@link AtomicReferenceArray} to assert is empty, the assertion will fail.
   * <p>
   * Grouping assertions example:
   * <pre><code class='java'> // myIcelanderFriends is an AtomicReferenceArray&lt;Person&gt;
   * assertThat(myIcelanderFriends).anySatisfy(person -&gt; {
   *                                 assertThat(person.getCountry()).isEqualTo("Iceland");
   *                                 assertThat(person.getPhoneCountryCode()).isEqualTo("+354");
   *                                 assertThat(person.getSurname()).endsWith("son");
   *                               });
   *                               
   * // assertion fails for empty group, whatever the requirements are.  
   * assertThat(emptyArray).anySatisfy($ -&gt; {
   *                         assertThat(true).isTrue();
   *                       });</code></pre>
   *
   * @param requirements the given {@link Consumer}.
   * @return {@code this} object.
   * @throws NullPointerException if the given {@link Consumer} is {@code null}.
   * @throws AssertionError if all elements don't satisfy given requirements.
   * @since 3.7.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> anySatisfy(Consumer<? super T> requirements) {
    iterables.assertAnySatisfy(info, newArrayList(array), requirements);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"}); 
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
   * @param values the values whose at least one which is expected to be in the {@code AtomicReferenceArray} under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and the {@code AtomicReferenceArray} under test is not empty.
   * @throws AssertionError if the {@code AtomicReferenceArray} under test is {@code null}.
   * @throws AssertionError if the {@code AtomicReferenceArray} under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsAnyOf(@SuppressWarnings("unchecked") T... values) {
    arrays.assertContainsAnyOf(info, array, values);
    return myself;
  }

  /**
   * Verifies that the actual AtomicReferenceArray contains at least one of the given {@link Iterable} elements.
   * <p>
   * Example :
   * <pre><code class='java'> AtomicReferenceArray&lt;String&gt; abc = new AtomicReferenceArray&lt;&gt;(new String[]{"a", "b", "c"}); 
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
   * @param iterable the iterable whose at least one element is expected to be in the {@code AtomicReferenceArray} under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the iterable of expected values is {@code null}.
   * @throws IllegalArgumentException if the iterable of expected values is empty and the {@code AtomicReferenceArray} under test is not empty.
   * @throws AssertionError if the {@code AtomicReferenceArray} under test is {@code null}.
   * @throws AssertionError if the {@code AtomicReferenceArray} under test does not contain any of elements from the given {@code Iterable}.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public AtomicReferenceArrayAssert<T> containsAnyElementsOf(Iterable<T> iterable) {
    return containsAnyOf(toArray(iterable));
  }

}
