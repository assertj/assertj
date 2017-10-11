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

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.filter.Filters.filter;
import static org.assertj.core.description.Description.mostRelevantDescription;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOf;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOfMethod;
import static org.assertj.core.extractor.Extractors.resultOf;
import static org.assertj.core.internal.CommonValidations.checkSequenceIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkSubsequenceIsNotNull;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.IterableAssert.LazyIterable;
import org.assertj.core.api.filter.FilterOperator;
import org.assertj.core.api.filter.Filters;
import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.condition.Not;
import org.assertj.core.description.Description;
import org.assertj.core.extractor.Extractors;
import org.assertj.core.groups.FieldsOrPropertiesExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.CommonErrors;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.FieldByFieldComparator;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.IterableElementComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.OnFieldsComparator;
import org.assertj.core.internal.RecursiveFieldByFieldComparator;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Preconditions;
import org.assertj.core.util.Strings;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Base class for implementations of <code>{@link ObjectEnumerableAssert}</code> whose actual value type is
 * <code>{@link Collection}</code>.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * @param <ELEMENT> the type of elements of the "actual" value.
 * @param <ELEMENT_ASSERT> used for navigational assertions to return the right assert type.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mathieu Baechler
 * @author Joel Costigliola
 * @author Maciej Jaskowski
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Mateusz Haligowski
 * @author Lovro Pandzic
 * @author Marko Bekhta
 */
//@format:off
public abstract class AbstractIterableAssert<SELF extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>,
                                             ACTUAL extends Iterable<? extends ELEMENT>,
                                             ELEMENT,
                                             ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractAssert<SELF, ACTUAL>
       implements ObjectEnumerableAssert<SELF, ELEMENT> {
//@format:on

  private static final String ASSERT = "Assert";

  private TypeComparators comparatorsByType = new TypeComparators();
  private Map<String, Comparator<?>> comparatorsForElementPropertyOrFieldNames = new HashMap<>();
  private TypeComparators comparatorsForElementPropertyOrFieldTypes = new TypeComparators();

  protected Iterables iterables = Iterables.instance();

  public AbstractIterableAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  protected static <T> Iterable<T> toLazyIterable(Iterator<T> actual) {
    if (actual == null) {
      return null;
    }
    return new LazyIterable<>(actual);
  }

  /**
   * Create a friendly soft or "hard" assertion.
   * <p>
   * Implementations need to redefine it so that some methods, such as {@link #extracting(Extractor)}, are able
   * to build the appropriate list assert (eg: {@link ListAssert} versus {@link SoftAssertionListAssert}).
   * <p>
   * The default implementation will assume that this concrete implementation is NOT a soft assertion.
   *
   * @param <E> the type of elements.
   * @param newActual new value
   * @return a new {@link AbstractListAssert}.
   */
  protected <E> AbstractListAssert<?, List<? extends E>, E, ObjectAssert<E>> newListAssertInstance(List<? extends E> newActual) {
    // this might not be the best implementation (SoftAssertion needs to override this).
    return new ListAssert<>(newActual);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void isNullOrEmpty() {
    iterables.assertNullOrEmpty(info, actual);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void isEmpty() {
    iterables.assertEmpty(info, actual);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotEmpty() {
    iterables.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF hasSize(int expected) {
    iterables.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the unique element of the {@link Iterable} satifies the given assertions expressed as a {@link Consumer},
   * if it does not, only the first error is reported, use {@link SoftAssertions} to get all the errors.
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;Jedi&gt; jedis = asList(new Jedi("Yoda", "red"));
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
   * jedis.add(new Jedi("Luke", "green"));
   * assertThat(jedis).hasOnlyOneElementSatisfying(yoda -&gt; assertThat(yoda.getName()).startsWith("Yo"));</code></pre>
   *
   * @param elementAssertions the assertions to perform on the unique element.
   * @throws AssertionError if the {@link Iterable} does not have a unique element.
   * @throws AssertionError if the {@link Iterable}'s unique element does not satifies the given assertions.
   *
   * @since 3.5.0
   */
  public void hasOnlyOneElementSatisfying(Consumer<ELEMENT> elementAssertions) {
    iterables.assertHasSize(info, actual, 1);
    elementAssertions.accept(actual.iterator().next());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF hasSameSizeAs(Object other) {
    iterables.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF hasSameSizeAs(Iterable<?> other) {
    iterables.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF contains(@SuppressWarnings("unchecked") ELEMENT... values) {
    iterables.assertContains(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsOnly(@SuppressWarnings("unchecked") ELEMENT... values) {
    iterables.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsOnlyOnce(@SuppressWarnings("unchecked") ELEMENT... values) {
    iterables.assertContainsOnlyOnce(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsOnlyNulls() {
    iterables.assertContainsOnlyNulls(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsExactly(@SuppressWarnings("unchecked") ELEMENT... values) {
    iterables.assertContainsExactly(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF containsExactlyInAnyOrder(@SuppressWarnings("unchecked") ELEMENT... values) {
    iterables.assertContainsExactlyInAnyOrder(info, actual, values);
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
   * {@inheritDoc}
   */
  @Override
  public SELF isSubsetOf(Iterable<? extends ELEMENT> values) {
    iterables.assertIsSubsetOf(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isSubsetOf(@SuppressWarnings("unchecked") ELEMENT... values) {
    iterables.assertIsSubsetOf(info, actual, Arrays.asList(values));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsSequence(@SuppressWarnings("unchecked") ELEMENT... sequence) {
    iterables.assertContainsSequence(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsSequence(Iterable<? extends ELEMENT> sequence) {
    checkSequenceIsNotNull(sequence);
    iterables.assertContainsSequence(info, actual, toArray(sequence));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotContainSequence(@SuppressWarnings("unchecked") ELEMENT... sequence) {
    iterables.assertDoesNotContainSequence(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotContainSequence(Iterable<? extends ELEMENT> sequence) {
    checkSequenceIsNotNull(sequence);
    iterables.assertDoesNotContainSequence(info, actual, toArray(sequence));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsSubsequence(@SuppressWarnings("unchecked") ELEMENT... subsequence) {
    iterables.assertContainsSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsSubsequence(Iterable<? extends ELEMENT> subsequence) {
    checkSubsequenceIsNotNull(subsequence);
    iterables.assertContainsSubsequence(info, actual, toArray(subsequence));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotContainSubsequence(@SuppressWarnings("unchecked") ELEMENT... subsequence) {
    iterables.assertDoesNotContainSubsequence(info, actual, subsequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotContainSubsequence(Iterable<? extends ELEMENT> subsequence) {
    checkSubsequenceIsNotNull(subsequence);
    iterables.assertDoesNotContainSubsequence(info, actual, toArray(subsequence));
    return myself;
  }

  @Override
  public SELF doesNotContain(@SuppressWarnings("unchecked") ELEMENT... values) {
    iterables.assertDoesNotContain(info, actual, values);
    return myself;
  }

  @Override
  public SELF doesNotContainAnyElementsOf(Iterable<? extends ELEMENT> iterable) {
    iterables.assertDoesNotContainAnyElementsOf(info, actual, iterable);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotHaveDuplicates() {
    iterables.assertDoesNotHaveDuplicates(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF startsWith(@SuppressWarnings("unchecked") ELEMENT... sequence) {
    iterables.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF endsWith(ELEMENT first, @SuppressWarnings("unchecked") ELEMENT... rest) {
    iterables.assertEndsWith(info, actual, first, rest);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF endsWith(ELEMENT[] sequence) {
    iterables.assertEndsWith(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsNull() {
    iterables.assertContainsNull(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotContainNull() {
    iterables.assertDoesNotContainNull(info, actual);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF are(Condition<? super ELEMENT> condition) {
    iterables.assertAre(info, actual, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF areNot(Condition<? super ELEMENT> condition) {
    iterables.assertAreNot(info, actual, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF have(Condition<? super ELEMENT> condition) {
    iterables.assertHave(info, actual, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doNotHave(Condition<? super ELEMENT> condition) {
    iterables.assertDoNotHave(info, actual, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF areAtLeastOne(Condition<? super ELEMENT> condition) {
    areAtLeast(1, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF areAtLeast(int times, Condition<? super ELEMENT> condition) {
    iterables.assertAreAtLeast(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF areAtMost(int times, Condition<? super ELEMENT> condition) {
    iterables.assertAreAtMost(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF areExactly(int times, Condition<? super ELEMENT> condition) {
    iterables.assertAreExactly(info, actual, times, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF haveAtLeastOne(Condition<? super ELEMENT> condition) {
    return haveAtLeast(1, condition);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF haveAtLeast(int times, Condition<? super ELEMENT> condition) {
    iterables.assertHaveAtLeast(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF haveAtMost(int times, Condition<? super ELEMENT> condition) {
    iterables.assertHaveAtMost(info, actual, times, condition);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF haveExactly(int times, Condition<? super ELEMENT> condition) {
    iterables.assertHaveExactly(info, actual, times, condition);
    return myself;
  }

  /**
   * Verifies that at least one element in the actual {@code Iterable} has the specified type (matching includes
   * subclasses of the given type).
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;Number&gt; numbers = new ArrayList&lt;Number&gt;();
   * numbers.add(1);
   * numbers.add(2L);
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
   * @throws AssertionError if the actual {@code Object} group does not have any elements of the given type.
   */
  @Override
  public SELF hasAtLeastOneElementOfType(Class<?> expectedType) {
    // reuse code from object arrays as the logic is the same
    // (ok since this assertion doesn't rely on a comparison strategy)
    ObjectArrays.instance().assertHasAtLeastOneElementOfType(info, toArray(actual), expectedType);
    return myself;
  }

  /**
   * Verifies that all elements in the actual {@code Iterable} have the specified type (matching includes
   * subclasses of the given type).
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;Number&gt; numbers = new ArrayList&lt;Number&gt;();
   * numbers.add(1);
   * numbers.add(2);
   * numbers.add(3);
   *
   * // successful assertions:
   * assertThat(numbers).hasOnlyElementsOfType(Number.class);
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
  public SELF hasOnlyElementsOfType(Class<?> expectedType) {
    // reuse code from object arrays as the logic is the same
    // (ok since this assertion doesn't rely on a comparison strategy)
    ObjectArrays.instance().assertHasOnlyElementsOfType(info, toArray(actual), expectedType);
    return myself;
  }

  /**
   * Verifies that all elements in the actual {@code Iterable} do not have the specified typetypes (including subclasses).
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;Number&gt; numbers = new ArrayList&lt;&gt;();
   * numbers.add(1);
   * numbers.add(2);
   * numbers.add(3.0);
   *
   * // successful assertions:
   * assertThat(numbers).doesNotHaveAnyElementsOfTypes(Long.class, Float.class);
   *
   * // assertion failure:
   * assertThat(numbers).doesNotHaveAnyElementsOfTypes(Long.class, Integer.class);</code></pre>
   *
   * @param unexpectedTypes the not expected types.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if one element's type matches the given types.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public SELF doesNotHaveAnyElementsOfTypes(Class<?>... unexpectedTypes) {
    ObjectArrays.instance().assertDoesNotHaveAnyElementsOfTypes(info, toArray(actual), unexpectedTypes);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasOnlyElementsOfTypes(Class<?>... types) {
    ObjectArrays.instance().assertHasOnlyElementsOfTypes(info, toArray(actual), types);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsAll(Iterable<? extends ELEMENT> iterable) {
    iterables.assertContainsAll(info, actual, iterable);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super ELEMENT> elementComparator) {
    this.iterables = new Iterables(new ComparatorBasedComparisonStrategy(elementComparator));
    // to have the same semantics on base assertions like isEqualTo, we need to use an iterable comparator comparing
    // elements with elementComparator parameter
    objects = new Objects(new IterableElementComparisonStrategy<>(elementComparator));
    return myself;
  }

  @CheckReturnValue
  private SELF usingExtendedByTypesElementComparator(Comparator<Object> elementComparator) {
    return usingElementComparator(new ExtendedByTypesComparator(elementComparator, comparatorsByType));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    usingDefaultComparator();
    this.iterables = Iterables.instance();
    return myself;
  }

  /**
   * Verifies that the actual {@link Iterable} contains at least one of the given values.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc = Arrays.asList("a", "b", "c");
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
   * @param values the values whose at least one which is expected to be in the {@code Iterable} under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and the {@code Iterable} under test is not empty.
   * @throws AssertionError if the {@code Iterable} under test is {@code null}.
   * @throws AssertionError if the {@code Iterable} under test does not contain any of the given {@code values}.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public SELF containsAnyOf(@SuppressWarnings("unchecked") ELEMENT... values) {
    iterables.assertContainsAnyOf(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the {@link Iterable} under test contains at least one of the given {@link Iterable} elements.
   * <p>
   * Example :
   * <pre><code class='java'> Iterable&lt;String&gt; abc = Arrays.asList("a", "b", "c");
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
   * @param iterable the iterable whose at least one element is expected to be in the {@code Iterable} under test.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the iterable of expected values is {@code null}.
   * @throws IllegalArgumentException if the iterable of expected values is empty and the {@code Iterable} under test is not empty.
   * @throws AssertionError if the {@code Iterable} under test is {@code null}.
   * @throws AssertionError if the {@code Iterable} under test does not contain any of elements from the given {@code Iterable}.
   * @since 2.9.0 / 3.9.0
   */
  @Override
  public SELF containsAnyElementsOf(Iterable<ELEMENT> iterable) {
    return containsAnyOf(toArray(iterable));
  }

  /**
   * Extract the values of the given field or property from the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test a property/field of the the Iterable's elements instead of testing the elements themselves, which
   * can be be much less work !
   * <p>
   * Let's take a look at an example to make things clearer :
   * <pre><code class='java'> // build a list of TolkienCharacters: a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * List&lt;TolkienCharacter&gt; fellowshipOfTheRing = new ArrayList&lt;TolkienCharacter&gt;();
   *
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN);
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Boromir&quot;, 37, MAN));
   *
   * // let's verify the names of the TolkienCharacters in fellowshipOfTheRing :
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // you can extract nested properties/fields like the name of the race :
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;race.name&quot;)
   *                                .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *                                .doesNotContain(&quot;Orc&quot;);</code></pre>
   * <p>
   * A property with the given name is searched for first. If it doesn't exist a field with the given name is looked
   * for. If the field does not exist an {@link IntrospectionError} is thrown. By default private fields are read but
   * you can change this with {@link Assertions#setAllowComparingPrivateFields(boolean)}. Trying to read a private field
   * when it's not allowed leads to an {@link IntrospectionError}.
   * <p>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   * <hr>
   * <p>
   * Extracting also support maps, that is, instead of extracting values from an Object, it extracts maps values
   * corresponding to the given keys.
   * <p>
   * Example:
   * <pre><code class='java'> Employee yoda = new Employee(1L, new Name("Yoda"), 800);
   * Employee luke = new Employee(2L, new Name("Luke"), 22);
   * Employee han = new Employee(3L, new Name("Han"), 31);
   *
   * // build two maps
   * Map&lt;String, Employee&gt; map1 = new HashMap&lt;&gt;();
   * map1.put("key1", yoda);
   * map1.put("key2", luke);
   *
   * Map&lt;String, Employee&gt; map2 = new HashMap&lt;&gt;();
   * map2.put("key1", yoda);
   * map2.put("key2", han);
   *
   * // instead of a list of objects, we have a list of maps
   * List&lt;Map&lt;String, Employee&gt;&gt; maps = asList(map1, map2);
   *
   * // extracting a property in that case = get values from maps using the property as a key
   * assertThat(maps).extracting("key2").containsExactly(luke, han);
   * assertThat(maps).extracting("key1").containsExactly(yoda, yoda);
   *
   * // type safe version
   * assertThat(maps).extracting(key2, Employee.class).containsExactly(luke, han);
   *
   * // it works with several keys, extracted values being wrapped in a Tuple
   * assertThat(maps).extracting("key1", "key2").containsExactly(tuple(yoda, luke), tuple(yoda, han));
   *
   * // unknown keys leads to null (map behavior)
   * assertThat(maps).extracting("bad key").containsExactly(null, null);</code></pre>
   *
   * @param propertyOrField the property/field to extract from the elements of the Iterable under test
   * @return a new assertion object whose object under test is the list of extracted property/field values.
   * @throws IntrospectionError if no field or property exists with the given name in one of the initial
   *         Iterable's element.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> extracting(String propertyOrField) {
    List<Object> values = FieldsOrPropertiesExtractor.extract(actual, byName(propertyOrField));
    String extractedDescription = extractedDescriptionOf(propertyOrField);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(values).as(description);
  }

  /**
   * Extract the result of given method invocation on the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test the method results of the Iterable's elements instead of testing the elements themselves. This
   * is especially useful for classes that do not conform to the Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take a look at an example to make things clearer :
   * <pre><code class='java'> // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   *
   * List&lt;WesterosHouse&gt; greatHouses = new ArrayList&lt;WesterosHouse&gt;();
   * greatHouses.add(new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Coming&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;));
   *
   * // let's verify the words of the great houses of Westeros:
   * assertThat(greatHouses).extractingResultOf(&quot;sayTheWords&quot;)
   *                        .contains(&quot;Winter is Coming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *                        .doesNotContain(&quot;Lannisters always pay their debts&quot;);</code></pre>
   *
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method cannot accept any arguments,</li>
   * <li>method cannot return void.</li>
   * </ul>
   * <p>
   * Note that the order of extracted results is consistent with the iteration order of the Iterable under test, for
   * example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted results order.
   *
   * @param method the name of the method which result is to be extracted from the array under test
   * @return a new assertion object whose object under test is the Iterable of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void, or method accepts arguments.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> extractingResultOf(String method) {
    // can't refactor by calling extractingResultOf(method, Object.class) as SoftAssertion would fail
    List<Object> values = FieldsOrPropertiesExtractor.extract(actual, resultOf(method));
    String extractedDescription = extractedDescriptionOfMethod(method);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(values).as(description);
  }

  /**
   * Extract the result of given method invocation on the Iterable's elements under test into a new list of the given
   * class, this new List becoming the object under test.
   * <p>
   * It allows you to test the method results of the Iterable's elements instead of testing the elements themselves, it
   * is especially useful for classes that do not conform to the Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build a array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
   * List&lt;WesterosHouse&gt; greatHouses = new ArrayList&lt;WesterosHouse&gt;();
   * greatHouses.add(new WesterosHouse(&quot;Stark&quot;, &quot;Winter is Coming&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Lannister&quot;, &quot;Hear Me Roar!&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Greyjoy&quot;, &quot;We Do Not Sow&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Baratheon&quot;, &quot;Our is the Fury&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Martell&quot;, &quot;Unbowed, Unbent, Unbroken&quot;));
   * greatHouses.add(new WesterosHouse(&quot;Tyrell&quot;, &quot;Growing Strong&quot;));
   *
   * // let's verify the words of the great houses of Westeros:
   * assertThat(greatHouses).extractingResultOf(&quot;sayTheWords&quot;, String.class)
   *                        .contains(&quot;Winter is Coming&quot;, &quot;We Do Not Sow&quot;, &quot;Hear Me Roar&quot;)
   *                        .doesNotContain(&quot;Lannisters always pay their debts&quot;);</code></pre>
   *
   * Following requirements have to be met to extract method results:
   * <ul>
   * <li>method has to be public,</li>
   * <li>method cannot accept any arguments,</li>
   * <li>method cannot return void.</li>
   * </ul>
   * <p>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions of the extracted values
   * order.
   *
   * @param <P> the type of elements extracted.
   * @param method the name of the method which result is to be extracted from the array under test
   * @param extractedType type of element of the extracted List
   * @return a new assertion object whose object under test is the Iterable of extracted values.
   * @throws IllegalArgumentException if no method exists with the given name, or method is not public, or method does
   *           return void or method accepts arguments.
   */
  @CheckReturnValue
  public <P> AbstractListAssert<?, List<? extends P>, P, ObjectAssert<P>> extractingResultOf(String method,
                                                                                             Class<P> extractedType) {
    @SuppressWarnings("unchecked")
    List<P> values = (List<P>) FieldsOrPropertiesExtractor.extract(actual, resultOf(method));
    String extractedDescription = extractedDescriptionOfMethod(method);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(values).as(description);
  }

  /**
   * Extract the values of given field or property from the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test a property/field of the the Iterable's elements instead of testing the elements themselves,
   * which can be much less work !
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * List&lt;TolkienCharacter&gt; fellowshipOfTheRing = new ArrayList&lt;TolkienCharacter&gt;();
   *
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN);
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Boromir&quot;, 37, MAN));
   *
   * // let's verify the names of TolkienCharacter in fellowshipOfTheRing :
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, String.class)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // you can extract nested property/field like the name of Race :
   * assertThat(fellowshipOfTheRing).extracting(&quot;race.name&quot;, String.class)
   *                                .contains(&quot;Hobbit&quot;, &quot;Elf&quot;)
   *                                .doesNotContain(&quot;Orc&quot;);</code></pre>
   *
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field does not exist an {@link IntrospectionError} is thrown, by default private fields are read but
   * you can change this with {@link Assertions#setAllowComparingPrivateFields(boolean)}, trying to read a private field
   * when it's not allowed leads to an {@link IntrospectionError}.
   * <p>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   * <hr>
   * <p>
   * Extracting also support maps, that is, instead of extracting values from an Object, it extract maps values
   * corresponding to the given keys.
   * <p>
   * Example:
   * <pre><code class='java'> Employee yoda = new Employee(1L, new Name("Yoda"), 800);
   * Employee luke = new Employee(2L, new Name("Luke"), 22);
   * Employee han = new Employee(3L, new Name("Han"), 31);
   *
   * // build two maps
   * Map&lt;String, Employee&gt; map1 = new HashMap&lt;&gt;();
   * map1.put("key1", yoda);
   * map1.put("key2", luke);
   *
   * Map&lt;String, Employee&gt; map2 = new HashMap&lt;&gt;();
   * map2.put("key1", yoda);
   * map2.put("key2", han);
   *
   * // instead of a list of objects, we have a list of maps
   * List&lt;Map&lt;String, Employee&gt;&gt; maps = asList(map1, map2);
   *
   * // extracting a property in that case = get values from maps using property as a key
   * assertThat(maps).extracting(key2, Employee.class).containsExactly(luke, han);
   *
   * // non type safe version
   * assertThat(maps).extracting("key2").containsExactly(luke, han);
   * assertThat(maps).extracting("key1").containsExactly(yoda, yoda);
   *
   * // it works with several keys, extracted values being wrapped in a Tuple
   * assertThat(maps).extracting("key1", "key2").containsExactly(tuple(yoda, luke), tuple(yoda, han));
   *
   * // unknown keys leads to null (map behavior)
   * assertThat(maps).extracting("bad key").containsExactly(null, null);</code></pre>
   *
   * @param <P> the type of elements extracted.
   * @param propertyOrField the property/field to extract from the Iterable under test
   * @param extractingType type to return
   * @return a new assertion object whose object under test is the list of extracted property/field values.
   * @throws IntrospectionError if no field or property exists with the given name in one of the initial
   *         Iterable's element.
   */
  @CheckReturnValue
  public <P> AbstractListAssert<?, List<? extends P>, P, ObjectAssert<P>> extracting(String propertyOrField,
                                                                                     Class<P> extractingType) {
    @SuppressWarnings("unchecked")
    List<P> values = (List<P>) FieldsOrPropertiesExtractor.extract(actual, byName(propertyOrField));
    String extractedDescription = extractedDescriptionOf(propertyOrField);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(values).as(description);
  }

  /**
   * Extract the values of the given fields/properties from the Iterable's elements under test into a new Iterable composed
   * of Tuples (a simple data structure), this new Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test fields/properties of the the Iterable's elements instead of testing the elements themselves,
   * which can be much less work!
   * <p>
   * The Tuple data corresponds to the extracted values of the given fields/properties, for instance if you ask to
   * extract "id", "name" and "email" then each Tuple data will be composed of id, name and email extracted from the
   * element of the initial Iterable (the Tuple's data order is the same as the given fields/properties order).
   * <p>
   * Let's take an example to make things clearer :
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * List&lt;TolkienCharacter&gt; fellowshipOfTheRing = new ArrayList&lt;TolkienCharacter&gt;();
   *
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN);
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Boromir&quot;, 37, MAN));
   *
   * // let's verify 'name' and 'age' of some TolkienCharacter in fellowshipOfTheRing :
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, &quot;age&quot;)
   *                                .contains(tuple(&quot;Boromir&quot;, 37),
   *                                          tuple(&quot;Sam&quot;, 38),
   *                                          tuple(&quot;Legolas&quot;, 1000));
   *
   *
   * // extract 'name', 'age' and Race name values :
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, &quot;age&quot;, &quot;race.name&quot;)
   *                                .contains(tuple(&quot;Boromir&quot;, 37, &quot;Man&quot;),
   *                                          tuple(&quot;Sam&quot;, 38, &quot;Hobbit&quot;),
   *                                          tuple(&quot;Legolas&quot;, 1000, &quot;Elf&quot;));</code></pre>
   *
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field does not exist an {@link IntrospectionError} is thrown, by default private fields are read but
   * you can change this with {@link Assertions#setAllowComparingPrivateFields(boolean)}, trying to read a private field
   * when it's not allowed leads to an {@link IntrospectionError}.
   * <p>
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   * <hr>
   * <p>
   * Extracting also support maps, that is, instead of extracting values from an Object, it extract maps values
   * corresponding to the given keys.
   * <p>
   * Example:
   * <pre><code class='java'> Employee yoda = new Employee(1L, new Name("Yoda"), 800);
   * Employee luke = new Employee(2L, new Name("Luke"), 22);
   * Employee han = new Employee(3L, new Name("Han"), 31);
   *
   * // build two maps
   * Map&lt;String, Employee&gt; map1 = new HashMap&lt;&gt;();
   * map1.put("key1", yoda);
   * map1.put("key2", luke);
   *
   * Map&lt;String, Employee&gt; map2 = new HashMap&lt;&gt;();
   * map2.put("key1", yoda);
   * map2.put("key2", han);
   *
   * // instead of a list of objects, we have a list of maps
   * List&lt;Map&lt;String, Employee&gt;&gt; maps = asList(map1, map2);
   *
   * // extracting a property in that case = get values from maps using property as a key
   * assertThat(maps).extracting("key2").containsExactly(luke, han);
   * assertThat(maps).extracting("key1").containsExactly(yoda, yoda);
   *
   * // it works with several keys, extracted values being wrapped in a Tuple
   * assertThat(maps).extracting("key1", "key2").containsExactly(tuple(yoda, luke), tuple(yoda, han));
   *
   * // unknown keys leads to null (map behavior)
   * assertThat(maps).extracting("bad key").containsExactly(null, null);</code></pre>
   *
   * @param propertiesOrFields the properties/fields to extract from the elements of the Iterable under test
   * @return a new assertion object whose object under test is the list of Tuple with extracted properties/fields values
   *         as data.
   * @throws IntrospectionError if one of the given name does not match a field or property in one of the initial
   *         Iterable's element.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extracting(String... propertiesOrFields) {
    List<Tuple> values = FieldsOrPropertiesExtractor.extract(actual, byName(propertiesOrFields));
    String extractedDescription = extractedDescriptionOf(propertiesOrFields);
    String description = mostRelevantDescription(info.description(), extractedDescription);
    return newListAssertInstance(values).as(description);
  }

  /**
   * Extract the values from Iterable's elements under test by applying an extracting function on them. The returned
   * iterable becomes a new object under test.
   * <p>
   * It allows to test values from the elements in more safe way than by using {@link #extracting(String)}, as it
   * doesn't utilize introspection.
   * <p>
   * Let's have a look at an example :
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * List&lt;TolkienCharacter&gt; fellowshipOfTheRing = new ArrayList&lt;TolkienCharacter&gt;();
   *
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN);
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Boromir&quot;, 37, MAN));
   *
   * // this extracts the race
   * Extractor&lt;TolkienCharacter, Race&gt; race = new Extractor&lt;TolkienCharacter, Race&gt;() {
   *    {@literal @}Override
   *    public Race extract(TolkienCharacter input) {
   *        return input.getRace();
   *    }
   * }
   *
   * // fellowship has hobbitses, right, my presioussss?
   * assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getRace).contains(HOBBIT);</code></pre>
   *
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   *
   * @param <V> the type of elements extracted.
   * @param extractor the object transforming input object to desired one
   * @return a new assertion object whose object under test is the list of values extracted
   */
  @CheckReturnValue
  public <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> extracting(Extractor<? super ELEMENT, V> extractor) {
    List<V> values = FieldsOrPropertiesExtractor.extract(actual, extractor);
    return newListAssertInstance(values);
  }

  /**
   * Extract the values from Iterable's elements under test by applying an extracting function (which might throw an
   * exception) on them. The returned iterable becomes a new object under test.
   * <p>
   * Any checked exception raised in the extractor is rethrown wrapped in a {@link RuntimeException}.
   * <p>
   * It allows to test values from the elements in more safe way than by using {@link #extracting(String)}, as it
   * doesn't utilize introspection.
   * <p>
   * Let's have a look at an example :
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * List&lt;TolkienCharacter&gt; fellowshipOfTheRing = new ArrayList&lt;TolkienCharacter&gt;();
   *
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN);
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Boromir&quot;, 37, MAN));
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
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param <V> the type of elements extracted.
   * @param extractor the object transforming input object to desired one
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.7.0
   */
  @CheckReturnValue
  public <V, EXCEPTION extends Exception> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> extracting(ThrowingExtractor<? super ELEMENT, V, EXCEPTION> extractor) {
    List<V> values = FieldsOrPropertiesExtractor.extract(actual, extractor);
    return newListAssertInstance(values);
  }

  /**
   * Extract the Iterable values from Iterable's elements under test by applying an Iterable extracting function on them
   * and concatenating the result lists. The returned iterable becomes a new object under test.
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
   * Extractor&lt;CartoonCharacter, List&lt;CartoonCharacter&gt;&gt; childrenOf = new Extractor&lt;CartoonChildren, List&lt;CartoonChildren&gt;&gt;() {
   *    {@literal @}Override
   *    public List&lt;CartoonChildren&gt; extract(CartoonCharacter input) {
   *        return input.getChildren();
   *    }
   * }
   *
   * List&lt;CartoonCharacter&gt; parents = newArrayList(homer, fred);
   * // check children
   * assertThat(parent).flatExtracting(CartoonCharacter::getChildren)
   *                   .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The order of extracted values is consistent with both the order of the collection itself, as well as the extracted
   * collections.
   *
   * @param <V> the type of elements extracted.
   * @param extractor the object transforming input object to an {@code Iterable} of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @throws NullPointerException if one of the {@code Iterable}'s element is null.
   */
  @CheckReturnValue
  public <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> flatExtracting(Extractor<? super ELEMENT, ? extends Collection<V>> extractor) {
    return doFlatExtracting(extractor);
  }

  /**
   * Extract the Iterable values from Iterable's elements under test by applying an Iterable extracting function (which
   * might throw an exception) on them and concatenating the result lists. The returned iterable becomes a new object
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
   * List&lt;CartoonCharacter&gt; parents = newArrayList(homer, fred);
   * // check children
   * assertThat(parent).flatExtracting((ThrowingExtractor&lt;CartoonCharacter, List&lt;CartoonCharacter&gt;, Exception&gt;)input -&gt; {
   *   if (input.getChildren().size() == 0) {
   *     throw new Exception("no children");
   *   }
   *   return input.getChildren();
   * }).containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The order of extracted values is consistent with both the order of the collection itself, as well as the extracted
   * collections.
   *
   * @param <V> the type of elements extracted.
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param extractor the object transforming input object to an {@code Iterable} of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @throws NullPointerException if one of the {@code Iterable}'s element is null.
   * @since 3.7.0
   */
  @CheckReturnValue
  public <V, EXCEPTION extends Exception> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> flatExtracting(ThrowingExtractor<? super ELEMENT, ? extends Collection<V>, EXCEPTION> extractor) {
    return doFlatExtracting(extractor);
  }

  private <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> doFlatExtracting(Extractor<? super ELEMENT, ? extends Collection<V>> extractor) {
    List<V> result = newArrayList();
    final List<? extends Collection<V>> extractedValues = FieldsOrPropertiesExtractor.extract(actual, extractor);

    for (Collection<? extends V> iterable : extractedValues) {
      result.addAll(iterable);
    }

    return newListAssertInstance(result);
  }

  /**
   * Extract multiple values from each {@code Iterable}'s element according to the given {@code Extractor}s
   * and concatenate/flatten the extracted values in a list that is used as the new object under test.
   * <p>
   * If extracted values were not flattened, instead of a simple list like (given 2 extractors) :
   * <pre>element1.value1, element1.value2, element2.value1, element2.value2, ...  </pre>
   * we would get a list of list like :
   * <pre>list(element1.value1, element1.value2), list(element2.value1, element2.value2), ...  </pre>
   * <p>
   * Code example:
   * <pre><code class='java'> // fellowshipOfTheRing is a List&lt;TolkienCharacter&gt;
   *
   * // values are extracted in order and flattened : age1, name1, age2, name2, age3 ...
   * assertThat(fellowshipOfTheRing).flatExtracting(TolkienCharacter::getAge,
   *                                                TolkienCharacter::getName)
   *                                .contains(33 ,"Frodo",
   *                                          1000, "Legolas",
   *                                          87, "Aragorn");</code></pre>
   *
   * The resulting extracted values list is ordered by {@code Iterable}'s element first and then extracted values,
   * this is why is in the example that age values come before names.
   *
   * @param extractors all the extractors to apply on each actual {@code Iterable}'s elements
   * @return a new assertion object whose object under test is a flattened list of all extracted values.
   */
  @CheckReturnValue
  @SafeVarargs
  public final ListAssert<Object> flatExtracting(Extractor<? super ELEMENT, ?>... extractors) {
    Stream<? extends ELEMENT> actualStream = stream(actual.spliterator(), false);
    List<Object> result = actualStream.flatMap(element -> Stream.of(extractors)
                                                                .map(extractor -> extractor.extract(element)))
                                      .collect(Collectors.toList());
    return new ListAssert<>(result);
  }

  /**
   * Extract multiple values from each {@code Iterable}'s element according to the given {@link ThrowingExtractor}s
   * and concatenate/flatten the extracted values in a list that is used as the new object under test.
   * <p>
   * If extracted values were not flattened, instead of a simple list like (given 2 extractors) :
   * <pre>element1.value1, element1.value2, element2.value1, element2.value2, ...  </pre>
   * we would get a list of list like :
   * <pre>list(element1.value1, element1.value2), list(element2.value1, element2.value2), ...  </pre>
   * <p>
   * Code example:
   * <pre><code class='java'> // fellowshipOfTheRing is a List&lt;TolkienCharacter&gt;
   *
   * // values are extracted in order and flattened : age1, name1, age2, name2, age3 ...
   * assertThat(fellowshipOfTheRing).flatExtracting(input -&gt; {
   *   if (input.getAge() &lt; 20) {
   *     throw new Exception("age &lt; 20");
   *   }
   *   return input.getName();
   * }, input2 -&gt; {
   *   if (input2.getAge() &lt; 20) {
   *     throw new Exception("age &lt; 20");
   *   }
   *   return input2.getAge();
   * }).contains(33 ,"Frodo",
   *     1000, "Legolas",
   *     87, "Aragorn");</code></pre>
   *
   * The resulting extracted values list is ordered by {@code Iterable}'s element first and then extracted values,
   * this is why is in the example that age values come before names.
   *
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param extractors all the extractors to apply on each actual {@code Iterable}'s elements
   * @return a new assertion object whose object under test is a flattened list of all extracted values.
   * @since 3.7.0
   */
  @CheckReturnValue
  @SafeVarargs
  public final <EXCEPTION extends Exception> ListAssert<Object> flatExtracting(ThrowingExtractor<? super ELEMENT, ?, EXCEPTION>... extractors) {
    Stream<? extends ELEMENT> actualStream = stream(actual.spliterator(), false);
    List<Object> result = actualStream.flatMap(element -> Stream.of(extractors)
                                                                .map(extractor -> extractor.extract(element)))
                                      .collect(Collectors.toList());
    return new ListAssert<>(result);
  }

  /**
   * Extract from Iterable's elements the Iterable/Array values corresponding to the given property/field name and
   * concatenate them into a single list becoming the new object under test.
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
   * List&lt;CartoonCharacter&gt; parents = newArrayList(homer, fred);
   * // check children
   * assertThat(parents).flatExtracting("children")
   *                    .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The order of extracted values is consisted with both the order of the collection itself, as well as the extracted
   * collections.
   *
   * @param fieldOrPropertyName the object transforming input object to an Iterable of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @throws IllegalArgumentException if one of the extracted property value was not an array or an iterable.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatExtracting(String fieldOrPropertyName) {
    List<Object> extractedValues = newArrayList();
    List<?> extractedGroups = FieldsOrPropertiesExtractor.extract(actual, byName(fieldOrPropertyName));
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
    return newListAssertInstance(extractedValues);
  }

  /**
   * Use the given {@link Function}s to extract the values from the {@link Iterable}'s elements into a new {@link Iterable}
   * composed of {@link Tuple}s (a simple data structure containing the extracted values), this new {@link Iterable} becoming the
   * object under test.
   * <p>
   * It allows you to test values from the {@link Iterable}'s elements instead of testing the elements themselves, which sometimes can be
   * much less work!
   * <p>
   * The Tuple data corresponds to the extracted values from the Iterable's elements, for instance if you pass functions
   * extracting "id", "name" and "email" values then each Tuple data will be composed of an id, a name and an email
   * extracted from the element of the initial Iterable (the Tuple's data order is the same as the given functions
   * order).
   * <p>
   * Let's take a look at an example to make things clearer :
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
   * // they can be public field or properties, both can be extracted.
   * List&lt;TolkienCharacter&gt; fellowshipOfTheRing = new ArrayList&lt;TolkienCharacter&gt;();
   *
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Sam&quot;, 38, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gandalf&quot;, 2020, MAIA));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Legolas&quot;, 1000, ELF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Pippin&quot;, 28, HOBBIT));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Gimli&quot;, 139, DWARF));
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Aragorn&quot;, 87, MAN);
   * fellowshipOfTheRing.add(new TolkienCharacter(&quot;Boromir&quot;, 37, MAN));
   *
   * // let's verify 'name', 'age' and Race of some TolkienCharacter in fellowshipOfTheRing :
   * assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName,
   *                                            character &gt; character.getAge(),
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
   * Note that the order of the extracted tuples list is consistent with the iteration order of the Iterable under test,
   * for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted tuples order.
   *
   * @param extractors the extractor functions to extract a value from an element of the Iterable under test.
   * @return a new assertion object whose object under test is the list of Tuples containing the extracted values.
   */
  @CheckReturnValue
  @SafeVarargs
  public final ListAssert<Tuple> extracting(Function<ELEMENT, ?>... extractors) {
    // combine all extractors into one function
    Function<ELEMENT, Tuple> tupleExtractor = objectToExtractValueFrom -> new Tuple(Stream.of(extractors)
                                                                                          .map(extractor -> extractor.apply(objectToExtractValueFrom))
                                                                                          .toArray());
    List<Tuple> tuples = stream(actual.spliterator(), false).map(tupleExtractor)
                                                            .collect(toList());
    return new ListAssert<>(tuples);
  }

  /**
   * Extract the given property/field values from each {@code Iterable}'s element and
   * flatten the extracted values in a list that is used as the new object under test.
   * <p>
   * Given 2 properties, if the extracted values were not flattened, instead having a simple list like :
   * <pre>element1.value1, element1.value2, element2.value1, element2.value2, ...  </pre>
   * ... we would get a list of list :
   * <pre>list(element1.value1, element1.value2), list(element2.value1, element2.value2), ...  </pre>
   * <p>
   * Code example:
   * <pre><code class='java'> // fellowshipOfTheRing is a List&lt;TolkienCharacter&gt;
   *
   * // values are extracted in order and flattened : age1, name1, age2, name2, age3 ...
   * assertThat(fellowshipOfTheRing).flatExtracting("age", "name")
   *                                .contains(33 ,"Frodo",
   *                                          1000, "Legolas",
   *                                          87, "Aragorn");</code></pre>
   *
   * @param fieldOrPropertyNames the field and/or property names to extract from each actual {@code Iterable}'s element
   * @return a new assertion object whose object under test is a flattened list of all extracted values.
   * @throws IllegalArgumentException if fieldOrPropertyNames vararg is null or empty
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatExtracting(String... fieldOrPropertyNames) {
    List<Object> extractedValues = newArrayList();
    for (Tuple tuple : FieldsOrPropertiesExtractor.extract(actual, Extractors.byName(fieldOrPropertyNames))) {
      extractedValues.addAll(tuple.toList());
    }
    return newListAssertInstance(extractedValues);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsExactlyElementsOf(Iterable<? extends ELEMENT> iterable) {
    return containsExactly(toArray(iterable));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsOnlyElementsOf(Iterable<? extends ELEMENT> iterable) {
    return containsOnly(toArray(iterable));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF hasSameElementsAs(Iterable<? extends ELEMENT> iterable) {
    return containsOnlyElementsOf(iterable);
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
   *
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
   * // assertions will pass
   * assertThat(asList(frodo)).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                          .usingFieldByFieldElementComparator()
   *                          .contains(tallerFrodo);
   *
   * assertThat(asList(frodo)).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                          .usingElementComparatorOnFields(&quot;height&quot;)
   *                          .contains(tallerFrodo);
   *
   * assertThat(asList(frodo)).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                          .usingElementComparatorIgnoringFields(&quot;name&quot;)
   *                          .contains(tallerFrodo);
   *
   * assertThat(asList(frodo)).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                          .usingRecursiveFieldByFieldElementComparator()
   *                          .contains(tallerFrodo);
   *
   * // assertion will fail
   * assertThat(asList(frodo)).usingComparatorForElementFieldsWithNames(closeEnough, &quot;height&quot;)
   *                          .usingFieldByFieldElementComparator()
   *                          .containsExactly(reallyTallFrodo);</code></pre>
   *
   * @param <T> the type of elements to compare.
   * @param comparator the {@link java.util.Comparator} to use
   * @param elementPropertyOrFieldNames the names of the properties and/or fields of the elements the comparator should be used for
   * @return {@code this} assertions object
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public <T> SELF usingComparatorForElementFieldsWithNames(Comparator<T> comparator,
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
   * // assertions will pass
   * assertThat(Arrays.asList(frodo)).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                                 .usingFieldByFieldElementComparator()
   *                                 .contains(tallerFrodo);
   *
   * assertThat(Arrays.asList(frodo)).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                                 .usingElementComparatorOnFields(&quot;height&quot;)
   *                                 .contains(tallerFrodo);
   *
   * assertThat(Arrays.asList(frodo)).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                                 .usingElementComparatorIgnoringFields(&quot;name&quot;)
   *                                 .contains(tallerFrodo);
   *
   * assertThat(Arrays.asList(frodo)).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                                 .usingRecursiveFieldByFieldElementComparator()
   *                                 .contains(tallerFrodo);
   *
   * // assertion will fail
   * assertThat(Arrays.asList(frodo)).usingComparatorForElementFieldsWithType(closeEnough, Double.class)
   *                                 .usingFieldByFieldElementComparator()
   *                                 .contains(reallyTallFrodo);</code></pre>
   *
   * @param <T> the type of elements to compare.
   * @param comparator the {@link java.util.Comparator} to use
   * @param type the {@link java.lang.Class} of the type of the element fields the comparator should be used for
   * @return {@code this} assertions object
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public <T> SELF usingComparatorForElementFieldsWithType(Comparator<T> comparator, Class<T> type) {
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
   * <pre><code class='java'>
   *     // assertion will pass
   *     assertThat(asList("some", new BigDecimal("4.2")))
   *         .usingComparatorForType(BIG_DECIMAL_COMPARATOR, BigDecimal.class)
   *         .contains(new BigDecimal("4.20"));
   * </code></pre>
   *
   * @param <T> the type of elements to compare.
   * @param comparator the {@link java.util.Comparator} to use
   * @param type the {@link java.lang.Class} of the type of the element or element fields the comparator should be used for
   * @return {@code this} assertions object
   * @since 2.9.0 / 3.9.0
   */
  @CheckReturnValue
  public <T> SELF usingComparatorForType(Comparator<T> comparator, Class<T> type) {
    if (iterables.getComparator() == null) {
      usingElementComparator(new ExtendedByTypesComparator(comparatorsByType));
    }

    comparatorsForElementPropertyOrFieldTypes.put(type, comparator);
    comparatorsByType.put(type, comparator);

    return myself;
  }

  /**
   * Use field/property by field/property comparison (including inherited fields/properties) instead of relying on
   * actual type A <code>equals</code> method to compare group elements for incoming assertion checks. Private fields
   * are included but this can be disabled using {@link Assertions#setAllowExtractingPrivateFields(boolean)}.
   * <p>
   * This can be handy if <code>equals</code> method of the objects to compare does not suit you.
   * <p>
   * Note that the comparison is <b>not</b> recursive, if one of the fields/properties is an Object, it will be compared
   * to the other field/property using its <code>equals</code> method.
   * <p>
   * You can specify a custom comparator per name or type of element field with
   * {@link #usingComparatorForElementFieldsWithNames(Comparator, String...)}
   * and {@link #usingComparatorForElementFieldsWithType(Comparator, Class)}.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   *
   * // Fail if equals has not been overridden in TolkienCharacter as equals default implementation only compares references
   * assertThat(newArrayList(frodo)).contains(frodoClone);
   *
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(newArrayList(frodo)).usingFieldByFieldElementComparator().contains(frodoClone);</code></pre>
   *
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public SELF usingFieldByFieldElementComparator() {
    return usingExtendedByTypesElementComparator(new FieldByFieldComparator(comparatorsForElementPropertyOrFieldNames,
                                                                            comparatorsForElementPropertyOrFieldTypes));
  }

  /**
   * Use a recursive field/property by field/property comparison (including inherited fields/properties)
   * instead of relying on actual type <code>equals</code> method to compare group elements for incoming
   * assertion checks. This can be useful if actual's {@code equals} implementation does not suit you.
   * <p>
   * The recursive property/field comparison is <b>not</b> applied on fields having a custom {@code equals}
   * implementation, i.e. the overridden {@code equals} method will be used instead of a field/property by field/property
   * comparison.
   * <p>
   * The recursive comparison handles cycles.
   * <p>
   * You can specify a custom comparator per (nested) name or type of element field with
   * {@link #usingComparatorForElementFieldsWithNames(Comparator, String...) usingComparatorForElementFieldsWithNames}
   * and {@link #usingComparatorForElementFieldsWithType(Comparator, Class) usingComparatorForElementFieldsWithType}.
   * <p>
   * The objects to compare can be of different types but must have the same properties/fields. For example if actual
   * object has a {@code name} String field, the other object must also have one.
   * <p>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p>
   * Example:
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
   * List&lt;TolkienCharacter&gt; hobbits = Arrays.asList(frodo, pippin);
   *
   * // fails if equals has not been overridden in TolkienCharacter as it would compares object references
   * assertThat(hobbits).contains(frodoClone, pippinClone);
   *
   * // frodo/frodoClone and pippin/pippinClone are equals when doing a recursive property/field by property/field comparison
   * assertThat(hobbits).usingRecursiveFieldByFieldElementComparator()
   *                    .contains(frodoClone, pippinClone);</code>
   * </pre>
   *
   * @return {@code this} assertion object.
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public SELF usingRecursiveFieldByFieldElementComparator() {
    return usingExtendedByTypesElementComparator(new RecursiveFieldByFieldComparator(comparatorsForElementPropertyOrFieldNames,
                                                                                     comparatorsForElementPropertyOrFieldTypes));
  }

  /**
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
   * </p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   *
   * // frodo and sam both are hobbits, so they are equals when comparing only race
   * assertThat(newArrayList(frodo)).usingElementComparatorOnFields("race").contains(sam); // OK
   *
   * // ... but not when comparing both name and race
   * assertThat(newArrayList(frodo)).usingElementComparatorOnFields("name", "race").contains(sam); // FAIL</code></pre>
   *
   * @param fields the fields/properties to compare using element comparators
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public SELF usingElementComparatorOnFields(String... fields) {
    return usingExtendedByTypesElementComparator(new OnFieldsComparator(comparatorsForElementPropertyOrFieldNames,
                                                                        comparatorsForElementPropertyOrFieldTypes,
                                                                        fields));
  }

  protected SELF usingComparisonStrategy(ComparisonStrategy comparisonStrategy) {
    iterables = new Iterables(comparisonStrategy);
    return myself;
  }

  /**
   * Use field/property by field/property comparison on all fields/properties <b>except</b> the given ones (including inherited
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
   * </p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   *
   * // frodo and sam both are hobbits, so they are equals when comparing only race (i.e. ignoring all other fields)
   * assertThat(newArrayList(frodo)).usingElementComparatorIgnoringFields("name", "age").contains(sam); // OK
   *
   * // ... but not when comparing both name and race
   * assertThat(newArrayList(frodo)).usingElementComparatorIgnoringFields("age").contains(sam); // FAIL</code></pre>
   *
   * @param fields the fields/properties to compare using element comparators
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public SELF usingElementComparatorIgnoringFields(String... fields) {
    return usingExtendedByTypesElementComparator(new IgnoringFieldsComparator(comparatorsForElementPropertyOrFieldNames,
                                                                              comparatorsForElementPropertyOrFieldTypes,
                                                                              fields));
  }

  /**
   * Enable hexadecimal representation of Iterable elements instead of standard representation in error messages.
   * <p>
   * It can be useful to better understand what the error was with a more meaningful error message.
   * <p>
   * Example
   * <pre><code class='java'> final List&lt;Byte&gt; bytes = newArrayList((byte) 0x10, (byte) 0x20);</code></pre>
   *
   * With standard error message:
   * <pre><code class='java'> assertThat(bytes).contains((byte) 0x30);
   *
   * Expecting:
   *  &lt;[16, 32]&gt;
   * to contain:
   *  &lt;[48]&gt;
   * but could not find:
   *  &lt;[48]&gt;</code></pre>
   *
   * With Hexadecimal error message:
   * <pre><code class='java'> assertThat(bytes).inHexadecimal().contains((byte) 0x30);
   *
   * Expecting:
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

  /**
   * Enable binary representation of Iterable elements instead of standard representation in error messages.
   * <p>
   * Example:
   * <pre><code class='java'> final List&lt;Byte&gt; bytes = newArrayList((byte) 0x10, (byte) 0x20);</code></pre>
   *
   * With standard error message:
   * <pre><code class='java'> assertThat(bytes).contains((byte) 0x30);
   *
   * Expecting:
   *  &lt;[16, 32]&gt;
   * to contain:
   *  &lt;[48]&gt;
   * but could not find:
   *  &lt;[48]&gt;</code></pre>
   *
   * With binary error message:
   * <pre><code class='java'> assertThat(bytes).inBinary().contains((byte) 0x30);
   *
   * Expecting:
   *  &lt;[0b00010000, 0b00100000]&gt;
   * to contain:
   *  &lt;[0b00110000]&gt;
   * but could not find:
   *  &lt;[0b00110000]&gt;</code></pre>
   *
   * @return {@code this} assertion object.
   */
  @Override
  @CheckReturnValue
  public SELF inBinary() {
    return super.inBinary();
  }

  /**
   * Filter the iterable under test keeping only elements having a property or field equal to {@code expectedValue}, the
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
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
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
   * An {@link IntrospectionError} is thrown if the given propertyOrFieldName can't be found in one of the iterable
   * elements.
   * <p>
   * You can chain filters:
   * <pre><code class='java'> // fellowshipOfTheRing is a list of TolkienCharacter having race and name fields
   * // 'not' filter is statically imported from Assertions.not
   *
   * assertThat(fellowshipOfTheRing).filteredOn("race.name", "Man")
   *                                .filteredOn("name", not("Boromir"))
   *                                .containsOnly(aragorn);</code></pre>
   *
   * If you need more complex filter, use {@link #filteredOn(Predicate)} or {@link #filteredOn(Condition)}.
   *
   * @param propertyOrFieldName the name of the property or field to read
   * @param expectedValue the value to compare element's property or field with
   * @return a new assertion object with the filtered iterable under test
   * @throws IllegalArgumentException if the given propertyOrFieldName is {@code null} or empty.
   * @throws IntrospectionError if the given propertyOrFieldName can't be found in one of the iterable elements.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> filteredOn(String propertyOrFieldName,
                                                                                                   Object expectedValue) {
    Filters<? extends ELEMENT> filter = filter((Iterable<? extends ELEMENT>) actual);
    Iterable<? extends ELEMENT> filteredIterable = filter.with(propertyOrFieldName, expectedValue).get();
    return newListAssertInstance(newArrayList(filteredIterable));
  }

  /**
   * Filter the iterable under test keeping only elements whose property or field specified by
   * {@code propertyOrFieldName} is null.
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
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
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
   * An {@link IntrospectionError} is thrown if the given propertyOrFieldName can't be found in one of the iterable
   * elements.
   * <p>
   * If you need more complex filter, use {@link #filteredOn(Predicate)} or {@link #filteredOn(Condition)}.
   *
   * @param propertyOrFieldName the name of the property or field to read
   * @return a new assertion object with the filtered iterable under test
   * @throws IntrospectionError if the given propertyOrFieldName can't be found in one of the iterable elements.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> filteredOnNull(String propertyOrFieldName) {
    // need to cast nulll to Object otherwise it calls :
    // filteredOn(String propertyOrFieldName, FilterOperation<?> filterOperation)
    return filteredOn(propertyOrFieldName, (Object) null);
  }

  /**
   * Filter the iterable under test keeping only elements having a property or field matching the filter expressed with
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
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
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
   * An {@link IntrospectionError} is thrown if the given propertyOrFieldName can't be found in one of the iterable
   * elements.
   * <p>
   * Note that combining filter operators is not supported, thus the following code is not correct:
   * <pre><code class='java'> // Combining filter operators like not(in(800)) is NOT supported
   * // -&gt; throws UnsupportedOperationException
   * assertThat(employees).filteredOn("age", not(in(800)))
   *                      .contains(luke);</code></pre>
   * <p>
   * You can chain filters:
   * <pre><code class='java'> // fellowshipOfTheRing is a list of TolkienCharacter having race and name fields
   * // 'not' filter is statically imported from Assertions.not
   *
   * assertThat(fellowshipOfTheRing).filteredOn("race.name", "Man")
   *                                .filteredOn("name", not("Boromir"))
   *                                .containsOnly(aragorn);</code></pre>
   *
   * If you need more complex filter, use {@link #filteredOn(Predicate)} or {@link #filteredOn(Condition)}.
   *
   * @param propertyOrFieldName the name of the property or field to read
   * @param filterOperator the filter operator to apply
   * @return a new assertion object with the filtered iterable under test
   * @throws IllegalArgumentException if the given propertyOrFieldName is {@code null} or empty.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> filteredOn(String propertyOrFieldName,
                                                                                                   FilterOperator<?> filterOperator) {
    checkNotNull(filterOperator);
    Filters<? extends ELEMENT> filter = filter((Iterable<? extends ELEMENT>) actual).with(propertyOrFieldName);
    filterOperator.applyOn(filter);
    return newListAssertInstance(newArrayList(filter.get()));
  }

  /**
   * Filter the iterable under test keeping only elements matching the given {@link Condition}.
   * <p>
   * If you prefer {@link Predicate} over {@link Condition}, use {@link #filteredOn(Predicate)}.
   * <p>
   * Example : check old employees whose age &gt; 100:
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
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
   * @return a new assertion object with the filtered iterable under test
   * @throws IllegalArgumentException if the given condition is {@code null}.
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> filteredOn(Condition<? super ELEMENT> condition) {
    Filters<? extends ELEMENT> filter = filter((Iterable<? extends ELEMENT>) actual);
    Iterable<? extends ELEMENT> filteredIterable = filter.being(condition).get();
    return newListAssertInstance(newArrayList(filteredIterable));
  }

  // navigable assertions

  /**
   * Navigate and allow to perform assertions on the first element of the {@link Iterable} under test.
   * <p>
   * By default available assertions after {@code first()} are {@code Object} assertions, it is possible though to
   * get more specific assertions if you create {@code IterableAssert} with either:
   * <ul>
   * <li>the element assert class, see: {@link Assertions#assertThat(Iterable, Class) assertThat(Iterable, element assert class)}</li>
   * <li>an assert factory used that knows how to create elements assertion, see: {@link Assertions#assertThat(Iterable, AssertFactory) assertThat(Iterable, element assert factory)}</li>
   * </ul>
   * <p>
   * Example: default {@code Object} assertions
   * <pre><code class='java'> // default iterable assert =&gt; element assert is ObjectAssert
   * Iterable&lt;TolkienCharacter&gt; hobbits = newArrayList(frodo, sam, pippin);
   *
   * // assertion succeeds, only Object assertions are available after first()
   * assertThat(hobbits).first()
   *                    .isEqualTo(frodo);
   *
   * // assertion fails
   * assertThat(hobbits).first()
   *                    .isEqualTo(pippin);</code></pre>
   * <p>
   * If you have created the Iterable assertion using an {@link AssertFactory} or the element assert class,
   * you will be able to chain {@code first()} with more specific typed assertion.
   * <p>
   * Example: use of {@code String} assertions after {@code first()}
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newArrayList("frodo", "sam", "pippin");
   *
   * // assertion succeeds
   * // String assertions are available after first()
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("fro")
   *                                        .endsWith("do");
   * // assertion fails
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("pip");</code></pre>
   *
   * @return the assertion on the first element
   * @throws AssertionError if the actual {@link Iterable} is empty.
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public ELEMENT_ASSERT first() {
    isNotEmpty();
    return toAssert(actual.iterator().next(), navigationDescription("check first element")); // TOD better description
  }

  /**
   * Navigate and allow to perform assertions on the first element of the {@link Iterable} under test.
   * <p>
   * By default available assertions after {@code last()} are {@code Object} assertions, it is possible though to
   * get more specific assertions if you create {@code IterableAssert} with either:
   * <ul>
   * <li>the element assert class, see: {@link Assertions#assertThat(Iterable, Class) assertThat(Iterable, element assert class)}</li>
   * <li>an assert factory used that knows how to create elements assertion, see: {@link Assertions#assertThat(Iterable, AssertFactory) assertThat(Iterable, element assert factory)}</li>
   * </ul>
   * <p>
   * Example: default {@code Object} assertions
   * <pre><code class='java'> // default iterable assert =&gt; element assert is ObjectAssert
   * Iterable&lt;TolkienCharacter&gt; hobbits = newArrayList(frodo, sam, pippin);
   *
   * // assertion succeeds, only Object assertions are available after last()
   * assertThat(hobbits).last()
   *                    .isEqualTo(pippin);
   *
   * // assertion fails
   * assertThat(hobbits).last()
   *                    .isEqualTo(frodo);</code></pre>
   * <p>
   * If you have created the Iterable assertion using an {@link AssertFactory} or the element assert class,
   * you will be able to chain {@code last()} with more specific typed assertion.
   * <p>
   * Example: use of {@code String} assertions after {@code last()}
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newArrayList("frodo", "sam", "pippin");
   *
   * // assertion succeeds
   * // String assertions are available after last()
   * assertThat(hobbits, StringAssert.class).last()
   *                                        .startsWith("pi")
   *                                        .endsWith("in");
   * // assertion fails
   * assertThat(hobbits, StringAssert.class).last()
   *                                        .startsWith("fro");</code></pre>
   *
   * @return the assertion on the first element
   * @throws AssertionError if the actual {@link Iterable} is empty.
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public ELEMENT_ASSERT last() {
    isNotEmpty();
    return toAssert(lastElement(), navigationDescription("check last element"));
  }

  private ELEMENT lastElement() {
    if (actual instanceof List) {
      List<? extends ELEMENT> list = (List<? extends ELEMENT>) actual;
      return list.get(list.size() - 1);
    }
    Iterator<? extends ELEMENT> actualIterator = actual.iterator();
    ELEMENT last = actualIterator.next();
    while (actualIterator.hasNext()) {
      last = actualIterator.next();
    }
    return last;
  }

  /**
   * Navigate and allow to perform assertions on the chosen element of the {@link Iterable} under test.
   * <p>
   * By default available assertions after {@code element(index)} are {@code Object} assertions, it is possible though to
   * get more specific assertions if you create {@code IterableAssert} with either:
   * <ul>
   * <li>the element assert class, see: {@link Assertions#assertThat(Iterable, Class) assertThat(Iterable, element assert class)}</li>
   * <li>an assert factory used that knows how to create elements assertion, see: {@link Assertions#assertThat(Iterable, AssertFactory) assertThat(Iterable, element assert factory)}</li>
   * </ul>
   * <p>
   * Example: default {@code Object} assertions
   * <pre><code class='java'> // default iterable assert =&gt; element assert is ObjectAssert
   * Iterable&lt;TolkienCharacter&gt; hobbits = newArrayList(frodo, sam, pippin);
   *
   * // assertion succeeds, only Object assertions are available after element(index)
   * assertThat(hobbits).element(1)
   *                    .isEqualTo(sam);
   *
   * // assertion fails
   * assertThat(hobbits).element(1)
   *                    .isEqualTo(pippin);</code></pre>
   * <p>
   * If you have created the Iterable assertion using an {@link AssertFactory} or the element assert class,
   * you will be able to chain {@code element(index)} with more specific typed assertion.
   * <p>
   * Example: use of {@code String} assertions after {@code element(index)}
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newArrayList("frodo", "sam", "pippin");
   *
   * // assertion succeeds
   * // String assertions are available after element(index)
   * assertThat(hobbits, StringAssert.class).element(1)
   *                                        .startsWith("sa")
   *                                        .endsWith("am");
   * // assertion fails
   * assertThat(hobbits, StringAssert.class).element(1)
   *                                        .startsWith("fro");</code></pre>
   *
   * @param index the element's index
   * @return the assertion on the given element
   * @throws AssertionError if the given index is out of bound.
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public ELEMENT_ASSERT element(int index) {
    isNotEmpty();
    assertThat(index).describedAs(navigationDescription("check index validity"))
                     .isBetween(0, IterableUtil.sizeOf(actual) - 1);
    ELEMENT elementAtIndex = null;
    if (actual instanceof List) {
      List<? extends ELEMENT> list = (List<? extends ELEMENT>) actual;
      elementAtIndex = list.get(index);
    } else {
      Iterator<? extends ELEMENT> actualIterator = actual.iterator();
      for (int i = 0; i < index; i++) {
        actualIterator.next();
      }
      elementAtIndex = actualIterator.next();
    }

    return toAssert(elementAtIndex, navigationDescription("element at index " + index));
  }

  protected abstract ELEMENT_ASSERT toAssert(ELEMENT value, String description);

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

  /**
   * Filter the iterable under test keeping only elements matching the given {@link Predicate}.
   * <p>
   * Example : check old employees whose age &gt; 100:
   *
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   *
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan);
   *
   * assertThat(employees).filteredOn(employee -&gt; employee.getAge() &gt; 100)
   *                      .containsOnly(yoda, obiwan);</code></pre>
   *
   * @param predicate the filter predicate
   * @return a new assertion object with the filtered iterable under test
   * @throws IllegalArgumentException if the given predicate is {@code null}.
   */
  public AbstractListAssert<?, List<? extends ELEMENT>, ELEMENT, ObjectAssert<ELEMENT>> filteredOn(Predicate<? super ELEMENT> predicate) {
    checkArgument(predicate != null, "The filter predicate should not be null");
    return new ListAssert<>(stream(actual.spliterator(), false).filter(predicate).collect(toList()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF allMatch(Predicate<? super ELEMENT> predicate) {
    iterables.assertAllMatch(info, actual, predicate, PredicateDescription.GIVEN);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF allMatch(Predicate<? super ELEMENT> predicate, String predicateDescription) {
    iterables.assertAllMatch(info, actual, predicate, new PredicateDescription(predicateDescription));
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF allSatisfy(Consumer<? super ELEMENT> requirements) {
    iterables.assertAllSatisfy(info, actual, requirements);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF anySatisfy(Consumer<? super ELEMENT> requirements) {
    iterables.assertAnySatisfy(info, actual, requirements);
    return myself;
  }

  // override methods to avoid compilation error when chaining an AbstractAssert method with a AbstractIterableAssert
  // one on raw types.

  @Override
  @CheckReturnValue
  public SELF as(String description, Object... args) {
    return super.as(description, args);
  }

  @Override
  @CheckReturnValue
  public SELF as(Description description) {
    return super.as(description);
  }

  @Override
  @CheckReturnValue
  public SELF describedAs(Description description) {
    return super.describedAs(description);
  }

  @Override
  @CheckReturnValue
  public SELF describedAs(String description, Object... args) {
    return super.describedAs(description, args);
  }

  @Override
  public SELF doesNotHave(Condition<? super ACTUAL> condition) {
    return super.doesNotHave(condition);
  }

  @Override
  public SELF doesNotHaveSameClassAs(Object other) {
    return super.doesNotHaveSameClassAs(other);
  }

  @Override
  public SELF has(Condition<? super ACTUAL> condition) {
    return super.has(condition);
  }

  @Override
  public SELF hasSameClassAs(Object other) {
    return super.hasSameClassAs(other);
  }

  @Override
  public SELF hasToString(String expectedToString) {
    return super.hasToString(expectedToString);
  }

  @Override
  public SELF is(Condition<? super ACTUAL> condition) {
    return super.is(condition);
  }

  @Override
  public SELF isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }

  @Override
  public SELF isExactlyInstanceOf(Class<?> type) {
    return super.isExactlyInstanceOf(type);
  }

  @Override
  public SELF isIn(Iterable<?> values) {
    return super.isIn(values);
  }

  @Override
  public SELF isIn(Object... values) {
    return super.isIn(values);
  }

  @Override
  public SELF isInstanceOf(Class<?> type) {
    return super.isInstanceOf(type);
  }

  @Override
  public SELF isInstanceOfAny(Class<?>... types) {
    return super.isInstanceOfAny(types);
  }

  @Override
  public SELF isNot(Condition<? super ACTUAL> condition) {
    return super.isNot(condition);
  }

  @Override
  public SELF isNotEqualTo(Object other) {
    return super.isNotEqualTo(other);
  }

  @Override
  public SELF isNotExactlyInstanceOf(Class<?> type) {
    return super.isNotExactlyInstanceOf(type);
  }

  @Override
  public SELF isNotIn(Iterable<?> values) {
    return super.isNotIn(values);
  }

  @Override
  public SELF isNotIn(Object... values) {
    return super.isNotIn(values);
  }

  @Override
  public SELF isNotInstanceOf(Class<?> type) {
    return super.isNotInstanceOf(type);
  }

  @Override
  public SELF isNotInstanceOfAny(Class<?>... types) {
    return super.isNotInstanceOfAny(types);
  }

  @Override
  public SELF isNotOfAnyClassIn(Class<?>... types) {
    return super.isNotOfAnyClassIn(types);
  }

  @Override
  public SELF isNotNull() {
    return super.isNotNull();
  }

  @Override
  public SELF isNotSameAs(Object other) {
    return super.isNotSameAs(other);
  }

  @Override
  public SELF isOfAnyClassIn(Class<?>... types) {
    return super.isOfAnyClassIn(types);
  }

  @Override
  public SELF isSameAs(Object expected) {
    return super.isSameAs(expected);
  }

  @Override
  @CheckReturnValue
  public SELF overridingErrorMessage(String newErrorMessage, Object... args) {
    return super.overridingErrorMessage(newErrorMessage, args);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    return super.usingDefaultComparator();
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator) {
    return super.usingComparator(customComparator);
  }

  @Override
  @CheckReturnValue
  public SELF withFailMessage(String newErrorMessage, Object... args) {
    return super.withFailMessage(newErrorMessage, args);
  }

  @Override
  @CheckReturnValue
  public SELF withThreadDumpOnError() {
    return super.withThreadDumpOnError();
  }

  /**
   * Returns an {@code Assert} object that allows performing assertions on the size of the {@link Iterable} under test.
   * <p>
   * Once this method is called, the object under test is no longer the {@link Iterable} but its size,
   * to perform assertions on the {@link Iterable}, call {@link AbstractIterableSizeAssert#returnToIterable()}.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   *
   * // assertion will pass:
   * assertThat(elvesRings).size().isGreaterThan(1)
   *                              .isLessThanOrEqualTo(3)
   *                       .returnToIterable().contains(narya)
   *                                          .doesNotContain(oneRing);
   *
   * // assertion will fail:
   * assertThat(elvesRings).size().isGreaterThan(3);</code></pre>
   *
   * @return AbstractIterableSizeAssert built with the {@code Iterable}'s size.
   * @throws NullPointerException if the given {@code Iterable} is {@code null}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @CheckReturnValue
  public AbstractIterableSizeAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> size() {
    Preconditions.checkNotNull(actual, "Can not perform assertions on the size of a null iterable.");
    return new IterableSizeAssert(this, IterableUtil.sizeOf(actual));
  }
}
