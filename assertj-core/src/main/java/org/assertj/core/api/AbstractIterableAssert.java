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
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.filter.Filters.filter;
import static org.assertj.core.description.Description.mostRelevantDescription;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
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
import org.assertj.core.description.Description;
import org.assertj.core.groups.FieldsOrPropertiesExtractor;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.CommonErrors;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.ConfigurableRecursiveFieldByFieldComparator;
import org.assertj.core.internal.ExtendedByTypesComparator;
import org.assertj.core.internal.FieldByFieldComparator;
import org.assertj.core.internal.IgnoringFieldsComparator;
import org.assertj.core.internal.IterableElementComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.OnFieldsComparator;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.IterableUtil;
import org.assertj.core.util.Strings;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Base class for implementations of <code>{@link ObjectEnumerableAssert}</code> whose actual value type is
 * <code>{@link Collection}</code>.
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
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * @param <ELEMENT> the type of elements of the "actual" value.
 * @param <ELEMENT_ASSERT> used for navigational assertions to return the right assert type.
 */
//@format:off
// suppression of deprecation works in Eclipse to hide warning for the deprecated classes in the imports
@SuppressWarnings("deprecation")
public abstract class AbstractIterableAssert<SELF extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>,
                                             ACTUAL extends Iterable<? extends ELEMENT>,
                                             ELEMENT,
                                             ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractAssert<SELF, ACTUAL>
       implements ObjectEnumerableAssert<SELF, ELEMENT> {
//@format:on

  private static final String ASSERT = "Assert";

  private TypeComparators comparatorsByType;
  private Map<String, Comparator<?>> comparatorsForElementPropertyOrFieldNames = new TreeMap<>();
  private TypeComparators comparatorsForElementPropertyOrFieldTypes;

  protected Iterables iterables = Iterables.instance();

  protected AbstractIterableAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);

    if (actual instanceof SortedSet) {
      @SuppressWarnings("unchecked")
      SortedSet<ELEMENT> sortedSet = (SortedSet<ELEMENT>) actual;
      Comparator<? super ELEMENT> comparator = sortedSet.comparator();
      if (comparator != null) usingElementComparator(sortedSet.comparator());
    }
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
   * Verifies that the number of values in the actual iterable is greater than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeGreaterThan(2);
   *
   * // assertion will fail
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeGreaterThan(3);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual iterable is not greater than the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThan(int boundary) {
    iterables.assertHasSizeGreaterThan(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual iterable is greater than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeGreaterThanOrEqualTo(1)
   *                                   .hasSizeGreaterThanOrEqualTo(3);
   *
   * // assertion will fail
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeGreaterThanOrEqualTo(4);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual iterable is not greater than or equal to the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThanOrEqualTo(int boundary) {
    iterables.assertHasSizeGreaterThanOrEqualTo(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual iterable is less than the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeLessThan(4);
   *
   * // assertion will fail
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeLessThan(3);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual iterable is not less than the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThan(int boundary) {
    iterables.assertHasSizeLessThan(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual iterable is less than or equal to the given boundary.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeLessThanOrEqualTo(5)
   *                                   .hasSizeLessThanOrEqualTo(3);
   *
   * // assertion will fail
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeLessThanOrEqualTo(2);</code></pre>
   *
   * @param boundary the given value to compare the actual size to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual iterable is not less than or equal to the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThanOrEqualTo(int boundary) {
    iterables.assertHasSizeLessThanOrEqualTo(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the actual iterable is between the given boundaries (inclusive).
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeBetween(2, 3)
   *                                   .hasSizeBetween(3, 4)
   *                                   .hasSizeBetween(3, 3);
   *
   * // assertion will fail
   * assertThat(Arrays.asList(1, 2, 3)).hasSizeBetween(4, 6);</code></pre>
   *
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the actual iterable is not between the boundaries.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeBetween(int lowerBoundary, int higherBoundary) {
    iterables.assertHasSizeBetween(info, actual, lowerBoundary, higherBoundary);
    return myself;
  }

  /**
   * Verifies that the unique element of the {@link Iterable} satisfies the given assertions expressed as a {@link Consumer},
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
   * @throws AssertionError if the {@link Iterable}'s unique element does not satisfy the given assertions.
   *
   * @since 3.5.0
   * @deprecated use {@link #singleElement()} instead
   */
  @Deprecated
  @Override
  public SELF hasOnlyOneElementSatisfying(Consumer<? super ELEMENT> elementAssertions) {
    iterables.assertHasSize(info, actual, 1);
    elementAssertions.accept(actual.iterator().next());
    return myself;
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
  @SafeVarargs
  public final SELF contains(ELEMENT... values) {
    return containsForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsForProxy(ELEMENT[] values) {
    iterables.assertContains(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
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
    iterables.assertContainsOnly(info, actual, values);
    return myself;
  }

  /**
   * {@inheritDoc}
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
  @SafeVarargs
  public final SELF containsExactly(ELEMENT... values) {
    return containsExactlyForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsExactlyForProxy(ELEMENT[] values) {
    iterables.assertContainsExactly(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @SafeVarargs
  public final SELF containsExactlyInAnyOrder(ELEMENT... values) {
    return containsExactlyInAnyOrderForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsExactlyInAnyOrderForProxy(ELEMENT[] values) {
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
  @SafeVarargs
  public final SELF isSubsetOf(ELEMENT... values) {
    return isSubsetOfForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF isSubsetOfForProxy(ELEMENT[] values) {
    iterables.assertIsSubsetOf(info, actual, Arrays.asList(values));
    return myself;
  }

  /**
   * {@inheritDoc}
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
  @SafeVarargs
  public final SELF doesNotContainSequence(ELEMENT... sequence) {
    return doesNotContainSequenceForProxy(sequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF doesNotContainSequenceForProxy(ELEMENT[] sequence) {
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
  @SafeVarargs
  public final SELF containsSubsequence(ELEMENT... subsequence) {
    return containsSubsequenceForProxy(subsequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsSubsequenceForProxy(ELEMENT[] subsequence) {
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
  @SafeVarargs
  public final SELF doesNotContainSubsequence(ELEMENT... subsequence) {
    return doesNotContainSubsequenceForProxy(subsequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF doesNotContainSubsequenceForProxy(ELEMENT[] subsequence) {
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
  @SafeVarargs
  public final SELF doesNotContain(ELEMENT... values) {
    return doesNotContainForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF doesNotContainForProxy(ELEMENT[] values) {
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
  @SafeVarargs
  public final SELF startsWith(ELEMENT... sequence) {
    return startsWithForProxy(sequence);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF startsWithForProxy(ELEMENT[] sequence) {
    iterables.assertStartsWith(info, actual, sequence);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SafeVarargs
  public final SELF endsWith(ELEMENT first, ELEMENT... rest) {
    return endsWithForProxy(first, rest);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF endsWithForProxy(ELEMENT first, ELEMENT[] rest) {
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
   * Verifies that all elements in the actual {@code Iterable} do not have the specified types (including subclasses).
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

  /** {@inheritDoc} */
  @Override
  public SELF hasExactlyElementsOfTypes(Class<?>... types) {
    ObjectArrays.instance().assertHasExactlyElementsOfTypes(info, toArray(actual), types);
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
    return usingElementComparator(new ExtendedByTypesComparator(elementComparator, getComparatorsByType()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    this.iterables = Iterables.instance();
    return usingDefaultComparator();
  }

  /**
   * Verifies that the actual {@link Iterable} contains at least one of the given values.
   * <p>
   * Example:
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
  @SafeVarargs
  public final SELF containsAnyOf(ELEMENT... values) {
    return containsAnyOfForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsAnyOfForProxy(ELEMENT[] values) {
    iterables.assertContainsAnyOf(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the {@link Iterable} under test contains at least one of the given {@link Iterable} elements.
   * <p>
   * Example:
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
  public SELF containsAnyElementsOf(Iterable<? extends ELEMENT> iterable) {
    return containsAnyOf(toArray(iterable));
  }

  /**
   * Extract the values of the given field or property from the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test a property/field of the Iterable's elements instead of testing the elements themselves, which
   * can be be much less work!
   * <p>
   * Let's take a look at an example to make things clearer:
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
   * // let's verify the names of the TolkienCharacters in fellowshipOfTheRing:
   *
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // you can extract nested properties/fields like the name of the race:
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
    return newListAssertInstanceForMethodsChangingElementType(values).as(description);
  }

  /**
   * Extract the result of given method invocation on the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test the method results of the Iterable's elements instead of testing the elements themselves. This
   * is especially useful for classes that do not conform to the Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take a look at an example to make things clearer:
   * <pre><code class='java'> // Build an array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
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
    return newListAssertInstanceForMethodsChangingElementType(values).as(description);
  }

  /**
   * Extract the result of given method invocation on the Iterable's elements under test into a new list of the given
   * class, this new List becoming the object under test.
   * <p>
   * It allows you to test the method results of the Iterable's elements instead of testing the elements themselves, it
   * is especially useful for classes that do not conform to the Java Bean's getter specification (i.e. public String
   * toString() or public String status() instead of public String getStatus()).
   * <p>
   * Let's take an example to make things clearer:
   * <pre><code class='java'> // Build an array of WesterosHouse, a WesterosHouse has a method: public String sayTheWords()
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
    return newListAssertInstanceForMethodsChangingElementType(values).as(description);
  }

  /**
   * Extract the values of given field or property from the Iterable's elements under test into a new Iterable, this new
   * Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test a property/field of the Iterable's elements instead of testing the elements themselves,
   * which can be much less work!
   * <p>
   * Let's take an example to make things clearer:
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
   * // let's verify the names of TolkienCharacter in fellowshipOfTheRing:
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, String.class)
   *           .contains(&quot;Boromir&quot;, &quot;Gandalf&quot;, &quot;Frodo&quot;)
   *           .doesNotContain(&quot;Sauron&quot;, &quot;Elrond&quot;);
   *
   * // you can extract nested property/field like the name of Race:
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
    return newListAssertInstanceForMethodsChangingElementType(values).as(description);
  }

  /**
   * Extract the values of the given fields/properties from the Iterable's elements under test into a new Iterable composed
   * of Tuples (a simple data structure), this new Iterable becoming the Iterable under test.
   * <p>
   * It allows you to test fields/properties of the Iterable's elements instead of testing the elements themselves,
   * which can be much less work!
   * <p>
   * The Tuple data corresponds to the extracted values of the given fields/properties, for instance if you ask to
   * extract "id", "name" and "email" then each Tuple data will be composed of id, name and email extracted from the
   * element of the initial Iterable (the Tuple's data order is the same as the given fields/properties order).
   * <p>
   * Let's take an example to make things clearer:
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
   * // let's verify 'name' and 'age' of some TolkienCharacter in fellowshipOfTheRing:
   * assertThat(fellowshipOfTheRing).extracting(&quot;name&quot;, &quot;age&quot;)
   *                                .contains(tuple(&quot;Boromir&quot;, 37),
   *                                          tuple(&quot;Sam&quot;, 38),
   *                                          tuple(&quot;Legolas&quot;, 1000));
   *
   *
   * // extract 'name', 'age' and Race name values:
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
    return newListAssertInstanceForMethodsChangingElementType(values).as(description);
  }

  /**
   * Extract the values from Iterable's elements under test by applying an extracting function on them. The returned
   * iterable becomes the instance under test.
   * <p>
   * It allows to test values from the elements more safely than by using {@link #extracting(String)}, as it
   * doesn't utilize introspection.
   * <p>
   * Let's have a look at an example:
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
   * // fellowship has hobbitses, right, my presioussss?
   * assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getRace).contains(HOBBIT);</code></pre>
   *
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values order.
   *
   * @param <V> the type of elements extracted.
   * @param extractor the object transforming input object to desired one
   * @return a new assertion object whose object under test is the list of values extracted
   */
  @CheckReturnValue
  public <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> extracting(Function<? super ELEMENT, V> extractor) {
    return internalExtracting(extractor);
  }

  private <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> internalExtracting(Function<? super ELEMENT, V> extractor) {
    if (actual == null) throwAssertionError(shouldNotBeNull());
    List<V> values = FieldsOrPropertiesExtractor.extract(actual, extractor);
    return newListAssertInstanceForMethodsChangingElementType(values);
  }

  /**
   * Maps the Iterable's elements under test by applying a mapping function, the resulting list becomes the instance under test.
   * <p>
   * This allows to test values from the elements more safely than by using {@link #extracting(String)}.
   * <p>
   * Let's have a look at an example:
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
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
   * // fellowship has hobbitses, right, my precioussss?
   * assertThat(fellowshipOfTheRing).map(TolkienCharacter::getRace)
   *                                .contains(HOBBIT);</code></pre>
   *
   * Note that the order of mapped values is consistent with the order of the Iterable under test, for example if
   * it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values order.
   *
   * @param <V> the type of elements resulting of the map operation.
   * @param mapper the {@link Function} transforming input object to desired one
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.19.0
   */
  public <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> map(Function<? super ELEMENT, V> mapper) {
    return internalExtracting(mapper);
  }

  /**
   * Extract the values from Iterable's elements under test by applying an extracting function (which might throw an
   * exception) on them. The returned iterable becomes the instance under test.
   * <p>
   * Any checked exception raised in the extractor is rethrown wrapped in a {@link RuntimeException}.
   * <p>
   * It allows to test values from the elements more safely than by using {@link #extracting(String)}, as it
   * doesn't utilize introspection.
   * <p>
   * Let's have a look at an example:
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
    return internalExtracting(extractor);
  }

  /**
   * Maps the Iterable's elements by applying the given mapping function (which might throw an exception), the returned list
   * becomes the instance under test.
   * <p>
   * Any checked exception raised in the function is rethrown wrapped in a {@link RuntimeException}.
   * <p>
   * This allows to test values from the elements more safely than by using {@link #extracting(String)}.
   * <p>
   * Let's have a look at an example:
   * <pre><code class='java'> // Build a list of TolkienCharacter, a TolkienCharacter has a name, and age and a Race (a specific class)
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
   * assertThat(fellowshipOfTheRing).map(input -&gt; {
   *   if (input.getAge() &lt; 20) {
   *     throw new Exception("age &lt; 20");
   *   }
   *   return input.getName();
   * }).contains("Frodo");</code></pre>
   *
   * Note that the order of mapped values is consistent with the order of the Iterable under test, for example if it's a
   * {@link HashSet}, you won't be able to make any assumptions on the extracted values order.
   *
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param <V> the type of elements extracted.
   * @param mapper the function transforming input object to desired one
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.19.0
   */
  @CheckReturnValue
  public <V, EXCEPTION extends Exception> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> map(ThrowingExtractor<? super ELEMENT, V, EXCEPTION> mapper) {
    return internalExtracting(mapper);
  }

  /*
   * Should be used after any methods changing the elements type like {@link #extracting(Function)} as it will propagate the
   * correct assertions state, that is everything but the element comparator (since the element type has changed).
   */
  private <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> newListAssertInstanceForMethodsChangingElementType(List<V> values) {
    if (actual instanceof SortedSet) {
      // Reset the natural element comparator set when building an iterable assert instance for a SortedSet as it is likely not
      // compatible with extracted values type, example with a SortedSet<Person> using a comparator on the Person's age, after
      // extracting names we get a List<String> which is mot suitable for the age comparator
      usingDefaultElementComparator();
    }
    return newListAssertInstance(values).withAssertionState(myself);
  }

  /**
   * Extracts Iterable elements values by applying a function and concatenates the result into a list that becomes the instance
   * under test.
   * <p>
   * Example:
   * <pre><code class='java'> CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   *
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.getChildren().add(bart);
   * homer.getChildren().add(lisa);
   * homer.getChildren().add(maggie);
   *
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   *
   * List&lt;CartoonCharacter&gt; parents = list(homer, fred);
   *
   * // check children property which is a List&lt;CartoonCharacter&gt;
   * assertThat(parents).flatExtracting(CartoonCharacter::getChildren)
   *                    .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The extracted values order is consistent with both the order of the iterable itself as well as the extracted collections.
   *
   * @param <V> the type of extracted elements.
   * @param extractor the {@link Function} transforming input object to an {@code Iterable} of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   */
  @CheckReturnValue
  public <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> flatExtracting(Function<? super ELEMENT, ? extends Collection<V>> extractor) {
    return doFlatExtracting(extractor);
  }

  /**
   * Maps the Iterable's elements under test by applying the given {@link Function} and flattens the resulting collections in a
   * list becoming the object under test.
   * <p>
   * Example:
   * <pre><code class='java'> CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   *
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.getChildren().add(bart);
   * homer.getChildren().add(lisa);
   * homer.getChildren().add(maggie);
   *
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   *
   * List&lt;CartoonCharacter&gt; parents = list(homer, fred);
   *
   * // check children property which is a List&lt;CartoonCharacter&gt;
   * assertThat(parents).flatMap(CartoonCharacter::getChildren)
   *                    .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The mapped values order is consistent with both the order of the iterable itself as well as the mapped collections.
   *
   * @param <V> the type of mapped elements.
   * @param mapper the {@link Function} transforming input object to an {@code Iterable} of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.19.0
   */
  @CheckReturnValue
  public <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> flatMap(Function<? super ELEMENT, ? extends Collection<V>> mapper) {
    return doFlatExtracting(mapper);
  }

  /**
   * Extracts Iterable elements values by applying a function (which might throw a checked exception) on them and
   * concatenates/flattens the result into a single list that becomes the instance under test.
   * <p>
   * Example:
   * <pre><code class='java'> CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   *
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.getChildren().add(bart);
   * homer.getChildren().add(lisa);
   * homer.getChildren().add(maggie);
   *
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   *
   * List&lt;CartoonCharacter&gt; parents = list(homer, fred);
   *
   * // check children property where getChildren() can throw an Exception!
   * assertThat(parents).flatExtracting(CartoonCharacter::getChildren)
   *                    .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The extracted values order is consistent with both the order of the iterable itself as well as the extracted collections.
   *
   * @param <V> the type of extracted values.
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param extractor the object transforming input object to an {@code Iterable} of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.7.0
   */
  @CheckReturnValue
  public <V, EXCEPTION extends Exception> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> flatExtracting(ThrowingExtractor<? super ELEMENT, ? extends Collection<V>, EXCEPTION> extractor) {
    return doFlatExtracting(extractor);
  }

  /**
   * Maps the Iterable's elements under test by applying a mapping function (which might throw a checked exception) and
   * concatenates/flattens the result into a single list that becomes the instance under test.
   * <p>
   * Example:
   * <pre><code class='java'> CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   *
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.getChildren().add(bart);
   * homer.getChildren().add(lisa);
   * homer.getChildren().add(maggie);
   *
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   *
   * List&lt;CartoonCharacter&gt; parents = list(homer, fred);
   *
   * // check children property where getChildren() can throw an Exception!
   * assertThat(parents).flatMap(CartoonCharacter::getChildren)
   *                    .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The mapped values order is consistent with both the order of the iterable itself as well as the mapped collections.
   *
   * @param <V> the type of mapped values.
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param mapper the object transforming input object to an {@code Iterable} of desired ones
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.19.0
   */
  @CheckReturnValue
  public <V, EXCEPTION extends Exception> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> flatMap(ThrowingExtractor<? super ELEMENT, ? extends Collection<V>, EXCEPTION> mapper) {
    return doFlatExtracting(mapper);
  }

  private <V> AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> doFlatExtracting(Function<? super ELEMENT, ? extends Collection<V>> extractor) {
    List<V> result = FieldsOrPropertiesExtractor.extract(actual, extractor).stream()
                                                .flatMap(Collection::stream)
                                                .collect(toList());
    return newListAssertInstanceForMethodsChangingElementType(result);
  }

  /**
   * Extracts multiple values from each {@code Iterable}'s element according to the given {@code Function}s and
   * concatenates/flattens them in a list that becomes the instance under test.
   * <p>
   * If extracted values were not flattened, instead of a simple list like (given 2 extractors):
   * <pre>  element1.value1, element1.value2, element2.value1, element2.value2, ...  </pre>
   * we would get a list of list like:
   * <pre>  list(element1.value1, element1.value2), list(element2.value1, element2.value2), ...  </pre>
   * <p>
   * Example:
   * <pre><code class='java'> // fellowshipOfTheRing is a List&lt;TolkienCharacter&gt;
   *
   * // values are extracted in order and flattened: age1, name1, age2, name2, age3 ...
   * assertThat(fellowshipOfTheRing).flatExtracting(TolkienCharacter::getAge,
   *                                                TolkienCharacter::getName)
   *                                .contains(33 ,"Frodo",
   *                                          1000, "Legolas",
   *                                          87, "Aragorn");</code></pre>
   *
   * The resulting extracted values list is ordered by {@code Iterable}'s element first and then extracted values,
   * this is why is in the example age values come before names.
   *
   * @param extractors all the extractors to apply on each actual {@code Iterable}'s elements
   * @return a new assertion object whose object under test is a flattened list of all extracted values.
   */
  @CheckReturnValue
  @SafeVarargs
  public final AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatExtracting(Function<? super ELEMENT, ?>... extractors) {
    return flatExtractingForProxy(extractors);
  }

  /**
   * Maps multiple values from each {@code Iterable}'s element according to the given {@code Function}s
   * and concatenates/flattens them in a list that becomes the instance under test.
   * <p>
   * If mapped values were not flattened, instead of a simple list like (given 2 extractors):
   * <pre>  element1.value1, element1.value2, element2.value1, element2.value2, ...  </pre>
   * we would get a list of list like:
   * <pre>  list(element1.value1, element1.value2), list(element2.value1, element2.value2), ...  </pre>
   * <p>
   * Example:
   * <pre><code class='java'> // fellowshipOfTheRing is a List&lt;TolkienCharacter&gt;
   *
   * // values are extracted in order and flattened: age1, name1, age2, name2, age3 ...
   * assertThat(fellowshipOfTheRing).flatMap(TolkienCharacter::getAge,
   *                                         TolkienCharacter::getName)
   *                                .contains(33 ,"Frodo",
   *                                          1000, "Legolas",
   *                                          87, "Aragorn");</code></pre>
   *
   * The resulting mapped values list is ordered by {@code Iterable}'s element first and then mapped values, this is why is
   * in the example age values come before names.
   *
   * @param mappers all the mappers to apply on each actual {@code Iterable}'s elements
   * @return a new assertion object whose object under test is a flattened list of all mapped values.
   * @since 3.19.0
   */
  @CheckReturnValue
  @SafeVarargs
  public final AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatMap(Function<? super ELEMENT, ?>... mappers) {
    return flatExtractingForProxy(mappers);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatExtractingForProxy(Function<? super ELEMENT, ?>[] extractors) {
    if (actual == null) throwAssertionError(shouldNotBeNull());
    Stream<? extends ELEMENT> actualStream = stream(actual.spliterator(), false);
    List<Object> result = actualStream.flatMap(element -> Stream.of(extractors).map(extractor -> extractor.apply(element)))
                                      .collect(toList());
    return newListAssertInstanceForMethodsChangingElementType(result);
  }

  /**
   * Extracts multiple values from each {@code Iterable}'s element according to the given {@link ThrowingExtractor}s
   * and concatenates/flattens them in a list that becomes the object under test.
   * <p>
   * If extracted values were not flattened, instead of a simple list like (given 2 extractors):
   * <pre>  element1.value1, element1.value2, element2.value1, element2.value2, ...  </pre>
   * we would get a list of list like:
   * <pre>  list(element1.value1, element1.value2), list(element2.value1, element2.value2), ...  </pre>
   * <p>
   * Example:
   * <pre><code class='java'> // fellowshipOfTheRing is a List&lt;TolkienCharacter&gt;
   *
   * // values are extracted in order and flattened: age1, name1, age2, name2, age3 ...
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
   * this is why is in the example age values come before names.
   *
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param extractors all the extractors to apply on each actual {@code Iterable}'s elements
   * @return a new assertion object whose object under test is a flattened list of all extracted values.
   * @since 3.7.0
   */
  @CheckReturnValue
  @SafeVarargs
  public final <EXCEPTION extends Exception> AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatExtracting(ThrowingExtractor<? super ELEMENT, ?, EXCEPTION>... extractors) {
    return flatExtractingForProxy(extractors);
  }

  /**
   * Maps multiple values from each {@code Iterable}'s element according to the given {@link ThrowingExtractor}s and
   * concatenates/flattens them in a list that becomes the object under test.
   * <p>
   * If mapped values were not flattened, instead of a simple list like (given 2 mappers):
   * <pre>  element1.value1, element1.value2, element2.value1, element2.value2, ...  </pre>
   * we would get a list of list like:
   * <pre>  list(element1.value1, element1.value2), list(element2.value1, element2.value2), ...  </pre>
   * <p>
   * Example:
   * <pre><code class='java'> // fellowshipOfTheRing is a List&lt;TolkienCharacter&gt;
   *
   * // values are extracted in order and flattened: age1, name1, age2, name2, age3 ...
   * assertThat(fellowshipOfTheRing).flatMap(input -&gt; {
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
   * The resulting mapped values list is ordered by {@code Iterable}'s element first and then mapped values, this is why is in
   * the example age values come before names.
   *
   * @param <EXCEPTION> the exception type of {@link ThrowingExtractor}
   * @param mappers all the mappers to apply on each actual {@code Iterable}'s elements
   * @return a new assertion object whose object under test is a flattened list of all extracted values.
   * @since 3.19.0
   */
  @CheckReturnValue
  @SafeVarargs
  public final <EXCEPTION extends Exception> AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> flatMap(ThrowingExtractor<? super ELEMENT, ?, EXCEPTION>... mappers) {
    return flatExtractingForProxy(mappers);
  }

  /**
   * Extract Iterable's elements values corresponding to the given property/field name and concatenates them into a list becoming
   * the new instance under test.
   * <p>
   * This allows testing the elements extracted values that are iterables or arrays.
   * <p>
   * For example:
   * <pre><code class='java'> CartoonCharacter bart = new CartoonCharacter("Bart Simpson");
   * CartoonCharacter lisa = new CartoonCharacter("Lisa Simpson");
   * CartoonCharacter maggie = new CartoonCharacter("Maggie Simpson");
   * CartoonCharacter homer = new CartoonCharacter("Homer Simpson");
   * homer.getChildren().add(bart);
   * homer.getChildren().add(lisa);
   * homer.getChildren().add(maggie);
   *
   * CartoonCharacter pebbles = new CartoonCharacter("Pebbles Flintstone");
   * CartoonCharacter fred = new CartoonCharacter("Fred Flintstone");
   * fred.getChildren().add(pebbles);
   *
   * List&lt;CartoonCharacter&gt; parents = list(homer, fred);
   *
   * // check children which is a List&lt;CartoonCharacter&gt;
   * assertThat(parents).flatExtracting("children")
   *                    .containsOnly(bart, lisa, maggie, pebbles);</code></pre>
   *
   * The order of extracted values is consisted with both the order of the iterable itself as well as the extracted collections.
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
    return newListAssertInstanceForMethodsChangingElementType(extractedValues);
  }

  /**
   * Use the given {@link Function}s to extract the values from the {@link Iterable}'s elements into a new {@link Iterable}
   * composed of {@link Tuple}s (a simple data structure containing the extracted values), this new {@link Iterable} becoming the
   * object under test.
   * <p>
   * It allows you to test values from the {@link Iterable}'s elements instead of testing the elements themselves, which sometimes can be
   * much less work!
   * <p>
   * The {@link Tuple} data correspond to the extracted values from the Iterable's elements, for instance if you pass functions
   * extracting "id", "name" and "email" values then each Tuple data will be composed of an id, a name and an email
   * extracted from the element of the initial Iterable (the Tuple's data order is the same as the given functions order).
   * <p>
   * Let's take a look at an example to make things clearer:
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
   * // let's verify 'name', 'age' and Race of some TolkienCharacter in fellowshipOfTheRing:
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
   * Note that the order of the extracted tuples list is consistent with the iteration order of the Iterable under test,
   * for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted tuples order.
   *
   * @param extractors the extractor functions to extract a value from an element of the Iterable under test.
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
    if (actual == null) throwAssertionError(shouldNotBeNull());
    // combine all extractors into one function
    Function<ELEMENT, Tuple> tupleExtractor = objectToExtractValueFrom -> new Tuple(Stream.of(extractors)
                                                                                          .map(extractor -> extractor.apply(objectToExtractValueFrom))
                                                                                          .toArray());
    List<Tuple> tuples = stream(actual.spliterator(), false).map(tupleExtractor).collect(toList());
    return newListAssertInstanceForMethodsChangingElementType(tuples);
  }

  /**
   * Use the given {@link Function}s to map the {@link Iterable}'s elements into a {@link List} of {@link Tuple}s
   * (a simple data structure containing the mapped values), this new list becoming the object under test.
   * <p>
   * This allows you to test values from the {@link Iterable}'s elements instead of testing the elements themselves, which
   * sometimes can be much less work!
   * <p>
   * The {@link Tuple} data correspond to the extracted values from the Iterable's elements, for instance if you pass functions
   * mapping "id", "name" and "email" values then each {@code Tuple} data will be composed of an id, a name and an email
   * mapped from the element of the initial Iterable (the Tuple's data order is the same as the given functions order).
   * <p>
   * Let's take a look at an example to make things clearer:
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
   * // let's verify 'name', 'age' and Race of some TolkienCharacter in fellowshipOfTheRing:
   * assertThat(fellowshipOfTheRing).map(TolkienCharacter::getName,
   *                                     character -&gt; character.getAge(),
   *                                     TolkienCharacter::getRace)
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
   * @param mappers the mapper functions to extract a value from an element of the Iterable under test.
   * @return a new assertion object whose object under test is the list of Tuples containing the extracted values.
   * @since 3.19.0
   */
  @CheckReturnValue
  @SafeVarargs
  public final AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> map(Function<? super ELEMENT, ?>... mappers) {
    return extractingForProxy(mappers);
  }

  /**
   * Extract the given property/field values from each {@code Iterable}'s element and
   * flatten the extracted values in a list that is used as the new object under test.
   * <p>
   * Given 2 properties, if the extracted values were not flattened, instead having a simple list like:
   * <pre>  element1.value1, element1.value2, element2.value1, element2.value2, ...  </pre>
   * ... we would get a list of list:
   * <pre>  list(element1.value1, element1.value2), list(element2.value1, element2.value2), ...  </pre>
   * <p>
   * Example:
   * <pre><code class='java'> // fellowshipOfTheRing is a List&lt;TolkienCharacter&gt;
   *
   * // values are extracted in order and flattened: age1, name1, age2, name2, age3 ...
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
    List<Object> extractedValues = FieldsOrPropertiesExtractor.extract(actual, byName(fieldOrPropertyNames)).stream()
                                                              .flatMap(tuple -> tuple.toList().stream())
                                                              .collect(toList());
    return newListAssertInstanceForMethodsChangingElementType(extractedValues);
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
  @Deprecated
  @Override
  public SELF containsOnlyElementsOf(Iterable<? extends ELEMENT> iterable) {
    return containsOnly(toArray(iterable));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF containsOnlyOnceElementsOf(Iterable<? extends ELEMENT> iterable) {
    return containsOnlyOnce(toArray(iterable));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF hasSameElementsAs(Iterable<? extends ELEMENT> iterable) {
    // containsOnlyElementsOf is deprecated so we use its implementation
    return containsOnly(toArray(iterable));
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
  public <T> SELF usingComparatorForElementFieldsWithNames(Comparator<T> comparator,
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
   * <li>{@link #usingFieldByFieldElementComparator}</li>
   * <li>{@link #usingElementComparatorOnFields}</li>
   * <li>{@link #usingElementComparatorIgnoringFields}</li>
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
  public <T> SELF usingComparatorForElementFieldsWithType(Comparator<T> comparator, Class<T> type) {
    getComparatorsForElementPropertyOrFieldTypes().registerComparator(type, comparator);
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
   * @deprecated This method is deprecated because it performs a <b>shallow</b> field by field comparison, i.e. elements are compared
   * field by field but the fields are compared with equals, use {@link #usingRecursiveFieldByFieldElementComparator()}
   * or {@link #usingRecursiveComparison()} instead to perform a true recursive comparison.
   * <br>See <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   */
  @CheckReturnValue
  @Deprecated
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
   * List&lt;Doctor&gt; doctors = list(drSheldon, drLeonard, drRaj);
   * List&lt;Person&gt; people = list(sheldon, leonard, raj);
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
   * List&lt;Doctor&gt; doctors = list(drSheldon, drLeonard, drRaj);
   * List&lt;Person&gt; people = list(sheldon, leonard, raj);
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
   * The default recursive comparison behavior is {@link RecursiveComparisonConfiguration configured} as follows:
   * <ul>
   *   <li> different types of iterable can be compared by default, this allows to compare for example an {@code List<Person>} and a {@code LinkedHashSet<PersonDto>}.<br>
   *        This behavior can be turned off by calling {@link RecursiveComparisonAssert#withStrictTypeChecking() withStrictTypeChecking}.</li>
   *   <li>overridden equals methods are used in the comparison (unless stated otherwise - see <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals">https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals</a>)</li>
   *   <li>the following types are compared with these comparators:
   *     <ul>
   *       <li>{@code java.lang.Double}: {@code DoubleComparator} with precision of 1.0E-15</li>
   *       <li>{@code java.lang.Float}: {@code FloatComparator }with precision of 1.0E-6</li>
   *     </ul>
   *   </li>
   * </ul>
   * <p>
   * Another point worth mentioning: <b>elements order does matter if the expected iterable is ordered</b>, for example comparing a {@code Set<Person>} to a {@code List<Person>} fails as {@code List} is ordered and {@code Set} is not.<br>
   * The ordering can be ignored by calling {@link RecursiveComparisonAssert#ignoringCollectionOrder ignoringCollectionOrder} allowing ordered/unordered iterable comparison, note that {@link RecursiveComparisonAssert#ignoringCollectionOrder ignoringCollectionOrder} is applied recursively on any nested iterable fields, if this behavior is too generic,
   * use the more fine grained {@link RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...) ignoringCollectionOrderInFields} or
   * {@link RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...) ignoringCollectionOrderInFieldsMatchingRegexes}.
   *
   * @param configuration the recursive comparison configuration.
   *
   * @return {@code this} assertion object.
   * @see RecursiveComparisonConfiguration
   * @since 3.17.0
   */
  public SELF usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration configuration) {
    return usingElementComparator(new ConfigurableRecursiveFieldByFieldComparator(configuration));
  }

  /**
   * Enable using a recursive field by field comparison strategy when calling the chained {@link RecursiveComparisonAssert},
   * <p>
   * There are differences between this approach and {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)}:
   * <ul>
   *   <li>you can only chain {@link RecursiveComparisonAssert} assertions (basically {@link RecursiveComparisonAssert#isEqualTo(Object) isEqualTo}) and (basically {@link RecursiveComparisonAssert#isNotEqualTo(Object) isNotEqualTo}), no iterable assertions.</li>
   *   <li>{@link RecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion error will report all field differences (very detailed).</li>
   *   <li>no comparators registered with {@link AbstractIterableAssert#usingComparatorForType(Comparator, Class)} will be used, you need to register them in chained call like {@link RecursiveComparisonAssert#withComparatorForType(Comparator, Class)}.</li>
   * </ul>
   * <p>
   * If you need to chain iterable assertions using recursive comparisons call {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)} instead.
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
   * List&lt;Doctor&gt; doctors = Arrays.asList(drSheldon, drLeonard, drRaj);
   * List&lt;Person&gt; people = Arrays.asList(sheldon, leonard, raj);
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
   * people = Arrays.asList(howard, sheldon, raj)
   * assertThat(doctors).usingRecursiveComparison()
   *                    .isEqualTo(people);</code></pre>
   *
   * A detailed documentation for the recursive comparison is available here: <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>.
   * <p>
   * The default recursive comparison behavior is {@link RecursiveComparisonConfiguration configured} as follows:
   * <ul>
   *   <li> different types of iterable can be compared by default, this allows to compare for example an {@code List<Person>} and a {@code LinkedHashSet<PersonDto>}.<br>
   *        This behavior can be turned off by calling {@link RecursiveComparisonAssert#withStrictTypeChecking() withStrictTypeChecking}.</li>
   *   <li>overridden equals methods are used in the comparison (unless stated otherwise - see <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals">https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals</a>)</li>
   *   <li>the following types are compared with these comparators:
   *     <ul>
   *       <li>{@code java.lang.Double}: {@code DoubleComparator} with precision of 1.0E-15</li>
   *       <li>{@code java.lang.Float}: {@code FloatComparator }with precision of 1.0E-6</li>
   *     </ul>
   *   </li>
   * </ul>
   * <p>
   * Another point worth mentioning: <b>elements order does matter if the expected iterable is ordered</b>, for example comparing a {@code Set<Person>} to a {@code List<Person>} fails as {@code List} is ordered and {@code Set} is not.<br>
   * The ordering can be ignored by calling {@link RecursiveComparisonAssert#ignoringCollectionOrder ignoringCollectionOrder} allowing ordered/unordered iterable comparison, note that {@link RecursiveComparisonAssert#ignoringCollectionOrder ignoringCollectionOrder} is applied recursively on any nested iterable fields, if this behavior is too generic,
   * use the more fine grained {@link RecursiveComparisonAssert#ignoringCollectionOrderInFields(String...) ignoringCollectionOrderInFields} or
   * {@link RecursiveComparisonAssert#ignoringCollectionOrderInFieldsMatchingRegexes(String...) ignoringCollectionOrderInFieldsMatchingRegexes}.
   * <p>
   * At the moment, only `isEqualTo` can be chained after this method but there are plans to provide assertions.
   *
   * @return a new {@link RecursiveComparisonAssert} instance
   * @see RecursiveComparisonConfiguration RecursiveComparisonConfiguration
   * @since 3.15.0
   */
  @Beta
  @Override
  public RecursiveComparisonAssert<?> usingRecursiveComparison() {
    // overridden for javadoc and to make this method public
    return super.usingRecursiveComparison();
  }

  /**
   * Same as {@link #usingRecursiveComparison()} but allows to specify your own {@link RecursiveComparisonConfiguration}.
   * <p>
   * Another difference is that any comparators previously registered with {@link AbstractIterableAssert#usingComparatorForType(Comparator, Class)} will be used in the comparison.
   *
   * @param recursiveComparisonConfiguration the {@link RecursiveComparisonConfiguration} used in the chained {@link RecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion.
   *
   * @return a new {@link RecursiveComparisonAssert} instance built with the given {@link RecursiveComparisonConfiguration}.
   * @since 3.15.0
   */
  @Beta
  @Override
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
   * List&lt;Author&gt; authors = Arrays.asList(pramodSadalage, kentBeck);
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
   * assertThat(list(derrickRose)).usingRecursiveFieldByFieldElementComparatorOnFields("name.last", "team", "nickname.first")
   *                              .contains(jalenRose);
   *
   * // assertion fails, name.first values differ
   * assertThat(list(derrickRose)).usingRecursiveFieldByFieldElementComparatorOnFields("name")
   *                              .contains(jalenRose);</code></pre>
   * <p>
   * This method is actually a shortcut of {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)}
   * with a configuration comparing only the given fields, the previous example can be written as:
   * <pre><code class='java'> RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
   *                                                                                  .withComparedFields("name.last", "team", "nickname.first")
   *                                                                                  .build();
   *
   * assertThat(list(derrickRose)).usingRecursiveFieldByFieldElementComparator(configuration)
   *                              .contains(jalenRose);</code></pre>
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

  protected SELF usingComparisonStrategy(ComparisonStrategy comparisonStrategy) {
    iterables = new Iterables(comparisonStrategy);
    return myself;
  }

  /**
   * <b><u>Deprecated javadoc</u></b>
   * <p>
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
   * @param fields the field names to exclude in the elements comparison.
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
   * assertThat(list(derrickRose)).usingRecursiveFieldByFieldElementComparatorIgnoringFields("name.first", "nickname.last")
   *                              .contains(jalenRose);
   *
   * // assertion fails, names are ignored but nicknames are not and nickname.last values differ
   * assertThat(list(derrickRose)).usingRecursiveFieldByFieldElementComparatorIgnoringFields("name")
   *                              .contains(jalenRose);</code></pre>
   * <p>
   * This method is actually a shortcut of {@link #usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration)}
   * with a configuration ignoring the given fields, the previous example can be written as:
   * <pre><code class='java'> RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
   *                                                                                  .withIgnoredFields("name.first", "nickname.last")
   *                                                                                  .build();
   *
   * assertThat(list(derrickRose)).usingRecursiveFieldByFieldElementComparator(configuration)
   *                              .contains(jalenRose);</code></pre>
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
   * Filters the iterable under test keeping only elements having a property or field equal to {@code expectedValue}, the
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
  public SELF filteredOn(String propertyOrFieldName, Object expectedValue) {
    Filters<? extends ELEMENT> filter = filter((Iterable<? extends ELEMENT>) actual);
    Iterable<? extends ELEMENT> filteredIterable = filter.with(propertyOrFieldName, expectedValue).get();
    return newAbstractIterableAssert(filteredIterable).withAssertionState(myself);
  }

  /**
   * Filters the iterable under test keeping only elements whose property or field specified by
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
  public SELF filteredOnNull(String propertyOrFieldName) {
    // can't call filteredOn(String propertyOrFieldName, null) as it does not work with soft assertions proxying
    // mechanism, it would lead to double proxying which is not handle properly (improvements needed in our proxy mechanism)
    Filters<? extends ELEMENT> filter = filter((Iterable<? extends ELEMENT>) actual);
    Iterable<? extends ELEMENT> filteredIterable = filter.with(propertyOrFieldName, null).get();
    return newAbstractIterableAssert(filteredIterable).withAssertionState(myself);
  }

  /**
   * Filters the iterable under test keeping only elements having a property or field matching the filter expressed with
   * the {@link FilterOperator}, the property/field is specified by {@code propertyOrFieldName} parameter.
   * <p>
   * The existing filters are:
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
   * As an example, let's check stuff on some special employees:
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
  public SELF filteredOn(String propertyOrFieldName, FilterOperator<?> filterOperator) {
    checkNotNull(filterOperator);
    Filters<? extends ELEMENT> filter = filter((Iterable<? extends ELEMENT>) actual).with(propertyOrFieldName);
    filterOperator.applyOn(filter);
    return newAbstractIterableAssert(filter.get()).withAssertionState(myself);
  }

  /**
   * Filters the iterable under test keeping only elements matching the given {@link Condition}.
   * <p>
   * If you prefer {@link Predicate} over {@link Condition}, use {@link #filteredOn(Predicate)}.
   * <p>
   * Example: check old employees whose age &gt; 100:
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
  public SELF filteredOn(Condition<? super ELEMENT> condition) {
    Filters<? extends ELEMENT> filter = filter((Iterable<? extends ELEMENT>) actual);
    Iterable<? extends ELEMENT> filteredIterable = filter.being(condition).get();
    return newAbstractIterableAssert(filteredIterable).withAssertionState(myself);
  }

  /**
   * Filters the iterable under test keeping only elements for which the result of the {@code function} is equal to {@code expectedValue}.
   * <p>
   * It allows to filter elements more safely than by using {@link #filteredOn(String, Object)} as it doesn't utilize introspection.
   * <p>
   * As an example, let's check all employees 800 years old (yes, special employees):
   * <pre><code class='java'> Employee yoda   = new Employee(1L, new Name("Yoda"), 800);
   * Employee obiwan = new Employee(2L, new Name("Obiwan"), 800);
   * Employee luke   = new Employee(3L, new Name("Luke", "Skywalker"), 26);
   * Employee noname = new Employee(4L, null, 50);
   *
   * List&lt;Employee&gt; employees = newArrayList(yoda, luke, obiwan, noname);
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
   * @return a new assertion object with the filtered iterable under test
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
   * Filters the iterable under test keeping only elements matching the given assertions specified with a {@link Consumer}.
   * <p>
   * Example: check young hobbits whose age &lt; 34:
   *
   * <pre><code class='java'> TolkienCharacter pippin = new TolkienCharacter("Pippin", 28, HOBBIT);
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter merry = new TolkienCharacter("Merry", 36, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   *
   * List&lt;TolkienCharacter&gt; hobbits = list(frodo, sam, merry, pippin);
   *
   * assertThat(hobbits).filteredOnAssertions(hobbit -&gt; assertThat(hobbit.age).isLessThan(34))
   *                    .containsOnly(frodo, pippin);</code></pre>
   *
   * @param elementAssertions containing AssertJ assertions to filter on
   * @return a new assertion object with the filtered iterable under test
   * @throws IllegalArgumentException if the given {@link Consumer} is {@code null}.
   * @since 3.11.0
   */
  public SELF filteredOnAssertions(Consumer<? super ELEMENT> elementAssertions) {
    return internalFilteredOnAssertions(elementAssertions);
  }

  /**
   * Filters the iterable under test keeping only elements matching the given assertions specified with a {@link ThrowingConsumer}.
   * <p>
   * This is the same assertion as {@link #filteredOnAssertions(Consumer)} but the given consumer can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}. 
   * <p>
   * Example: check young hobbits whose age &lt; 34:
   *
   * <pre><code class='java'> TolkienCharacter pippin = new TolkienCharacter("Pippin", 28, HOBBIT);
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter merry = new TolkienCharacter("Merry", 36, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, HOBBIT);
   *
   * List&lt;TolkienCharacter&gt; hobbits = list(frodo, sam, merry, pippin);
   *
   * // the code would compile even if getAge() threw a checked exception
   * assertThat(hobbits).filteredOnAssertions(hobbit -&gt; assertThat(hobbit.getAge()).isLessThan(34))
   *                    .containsOnly(frodo, pippin);</code></pre>
   *
   * @param elementAssertions containing AssertJ assertions to filter on
   * @return a new assertion object with the filtered iterable under test
   * @throws IllegalArgumentException if the given {@link ThrowingConsumer} is {@code null}.
   * @since 3.21.0
   */
  public SELF filteredOnAssertions(ThrowingConsumer<? super ELEMENT> elementAssertions) {
    return internalFilteredOnAssertions(elementAssertions);
  }

  private SELF internalFilteredOnAssertions(Consumer<? super ELEMENT> elementAssertions) {
    checkArgument(elementAssertions != null, "The element assertions should not be null");
    List<? extends ELEMENT> filteredIterable = stream(actual.spliterator(), false).filter(byPassingAssertions(elementAssertions))
                                                                                  .collect(toList());
    return newAbstractIterableAssert(filteredIterable).withAssertionState(myself);
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
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newArrayList("Frodo", "Sam", "Pippin");
   *
   * // assertion succeeds
   * // String assertions are available after first()
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("Fro")
   *                                        .endsWith("do");
   * // assertion fails
   * assertThat(hobbits, StringAssert.class).first()
   *                                        .startsWith("Pip");</code></pre>
   *
   * @return the assertion on the first element
   * @throws AssertionError if the actual {@link Iterable} is empty.
   * @see #first(InstanceOfAssertFactory)
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public ELEMENT_ASSERT first() {
    return internalFirst();
  }

  /**
   * Navigate and allow to perform assertions on the first element of the {@link Iterable} under test.
   * <p>
   * The {@code assertFactory} parameter allows to specify an {@link InstanceOfAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Example: use of {@code String} assertions after {@code first(as(InstanceOfAssertFactories.STRING)}
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newArrayList("Frodo", "Sam", "Pippin");
   *
   * // assertion succeeds
   * assertThat(hobbits).first(as(InstanceOfAssertFactories.STRING))
   *                    .startsWith("Fro")
   *                    .endsWith("do");
   * // assertion fails
   * assertThat(hobbits).first(as(InstanceOfAssertFactories.STRING))
   *                    .startsWith("Pip");
   * // assertion fails because of wrong factory type
   * assertThat(hobbits).first(as(InstanceOfAssertFactories.INTEGER))
   *                    .isZero();</code></pre>
   *
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the first element
   * @throws AssertionError if the actual {@link Iterable} is empty.
   * @throws NullPointerException if the given factory is {@code null}
   * @since 3.14.0
   */
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT first(InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return internalFirst().asInstanceOf(assertFactory);
  }

  private ELEMENT_ASSERT internalFirst() {
    isNotEmpty();
    return toAssert(actual.iterator().next(), navigationDescription("check first element"));
  }

  /**
   * Navigate and allow to perform assertions on the last element of the {@link Iterable} under test.
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
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newArrayList("Frodo", "Sam", "Pippin");
   *
   * // assertion succeeds
   * // String assertions are available after last()
   * assertThat(hobbits, StringAssert.class).last()
   *                                        .startsWith("Pi")
   *                                        .endsWith("in");
   * // assertion fails
   * assertThat(hobbits, StringAssert.class).last()
   *                                        .startsWith("Fro");</code></pre>
   *
   * @return the assertion on the last element
   * @throws AssertionError if the actual {@link Iterable} is empty.
   * @see #last(InstanceOfAssertFactory)
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public ELEMENT_ASSERT last() {
    return internalLast();
  }

  /**
   * Navigate and allow to perform assertions on the last element of the {@link Iterable} under test.
   * <p>
   * The {@code assertFactory} parameter allows to specify an {@link InstanceOfAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Example: use of {@code String} assertions after {@code last(as(InstanceOfAssertFactories.STRING)}
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newArrayList("Frodo", "Sam", "Pippin");
   *
   * // assertion succeeds
   * assertThat(hobbits).last(as(InstanceOfAssertFactories.STRING))
   *                    .startsWith("Pip")
   *                    .endsWith("pin");
   * // assertion fails
   * assertThat(hobbits).last(as(InstanceOfAssertFactories.STRING))
   *                    .startsWith("Fro");
   * // assertion fails because of wrong factory type
   * assertThat(hobbits).last(as(InstanceOfAssertFactories.INTEGER))
   *                    .isZero();</code></pre>
   *
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the last element
   * @throws AssertionError if the actual {@link Iterable} is empty.
   * @throws NullPointerException if the given factory is {@code null}
   * @since 3.14.0
   */
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT last(InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return internalLast().asInstanceOf(assertFactory);
  }

  private ELEMENT_ASSERT internalLast() {
    isNotEmpty();
    return toAssert(lastElement(), navigationDescription("check last element"));
  }

  private ELEMENT lastElement() {
    if (actual instanceof List) {
      @SuppressWarnings("unchecked")
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
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newArrayList("Frodo", "Sam", "Pippin");
   *
   * // assertion succeeds
   * // String assertions are available after element(index)
   * assertThat(hobbits, StringAssert.class).element(1)
   *                                        .startsWith("Sa")
   *                                        .endsWith("am");
   * // assertion fails
   * assertThat(hobbits, StringAssert.class).element(1)
   *                                        .startsWith("Fro");</code></pre>
   *
   * @param index the element's index
   * @return the assertion on the given element
   * @throws AssertionError if the given index is out of bound.
   * @see #element(int, InstanceOfAssertFactory)
   * @since 2.5.0 / 3.5.0
   */
  @CheckReturnValue
  public ELEMENT_ASSERT element(int index) {
    return internalElement(index);
  }

  /**
   * Allow to perform assertions on the elements corresponding to the given indices 
   * (the iterable {@link Iterable} under test is changed to an iterable with the selected elements).  
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;TolkienCharacter&gt; hobbits = newArrayList(frodo, sam, pippin);
   *
   * // assertion succeeds
   * assertThat(hobbits).elements(1, 2)
   *                    .hasSize(2)
   *                    .containsExactly(sam, pippin);
   *
   * // assertion fails
   * assertThat(hobbits).element(1, 2)
   *                    .containsExactly(frodo, pippin);</code></pre>
   * <p>
   *
   * @param indices the elements indices
   * @return the assertion on the given elements
   * @throws IllegalArgumentException if indices array is null or empty
   * @throws AssertionError if one of the given indices is out of bound or if the actual is empty
   * @since 3.20
   */
  @CheckReturnValue
  public SELF elements(int... indices) {
    isNotEmpty();
    assertIndicesIsNotNull(indices);
    assertIndicesIsNotEmpty(indices);

    List<ELEMENT> indexedActual = newArrayList(actual);

    List<ELEMENT> filteredIterable = Arrays.stream(indices)
                                           .peek(index -> checkIndexValidity(index, indexedActual))
                                           .mapToObj(indexedActual::get)
                                           .collect(toList());
    // For soft assertions/assumptions, this must return a proxied iterable assert but we can't put "elements" in
    // SoftProxies.METHODS_CHANGING_THE_OBJECT_UNDER_TEST because these methods are not proxied.
    // We want to proxy elements(int... indices) to capture isNotEmpty and checkIndexValidity assertion errors.
    // The solution is to introduce newAbstractIterableAssertForProxy which is going to be proxied as newAbstractIterableAssert
    // was added to SoftProxies.METHODS_CHANGING_THE_OBJECT_UNDER_TEST list and SoftProxies.methodsChangingTheObjectUnderTestNamed
    // will select newAbstractIterableAssertForProxy to be proxied.
    return newAbstractIterableAssertForProxy(filteredIterable);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  protected SELF newAbstractIterableAssertForProxy(List<ELEMENT> filteredIterable) {
    return newAbstractIterableAssert(filteredIterable).withAssertionState(myself);
  }

  private static void assertIndicesIsNotNull(int[] indices) {
    if (indices == null) throw new IllegalArgumentException("indices must not be null");
  }

  private static void assertIndicesIsNotEmpty(int[] indices) {
    if (indices.length == 0) throw new IllegalArgumentException("indices must not be empty");
  }

  private void checkIndexValidity(int index, List<ELEMENT> indexedActual) {
    assertThat(indexedActual).describedAs("check actual size is enough to get element[" + index + "]")
                             .hasSizeGreaterThan(index);
  }

  /**
   * Navigate and allow to perform assertions on the chosen element of the {@link Iterable} under test.
   * <p>
   * The {@code assertFactory} parameter allows to specify an {@link InstanceOfAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Example: use of {@code String} assertions after {@code element(index, as(InstanceOfAssertFactories.STRING)}
   * <pre><code class='java'> Iterable&lt;String&gt; hobbits = newArrayList("Frodo", "Sam", "Pippin");
   *
   * // assertion succeeds
   * assertThat(hobbits).element(1, as(InstanceOfAssertFactories.STRING))
   *                    .startsWith("Sa")
   *                    .endsWith("am");
   * // assertion fails
   * assertThat(hobbits).element(1, as(InstanceOfAssertFactories.STRING))
   *                    .startsWith("Fro");
   * // assertion fails because of wrong factory type
   * assertThat(hobbits).element(1, as(InstanceOfAssertFactories.INTEGER))
   *                    .isZero();</code></pre>
   *
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @param index         the element's index
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the element at the given index
   * @throws AssertionError if the given index is out of bound.
   * @throws NullPointerException if the given factory is {@code null}
   * @since 3.14.0
   */
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT element(int index, InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return internalElement(index).asInstanceOf(assertFactory);
  }

  private ELEMENT_ASSERT internalElement(int index) {
    isNotEmpty();
    assertThat(index).describedAs(navigationDescription("check index validity"))
                     .isBetween(0, IterableUtil.sizeOf(actual) - 1);
    ELEMENT elementAtIndex;
    if (actual instanceof List) {
      @SuppressWarnings("unchecked")
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

  /**
   * Verifies that the {@link Iterable} under test contains a single element and allows to perform assertions on that element.
   * <p>
   * This is a shorthand for <code>hasSize(1).first()</code>.
   * <p>
   * By default available assertions after {@code singleElement()} are {@code Object} assertions, it is possible though to
   * get more specific assertions if you create {@code IterableAssert} with either:
   * <ul>
   * <li>the element assert class, see: {@link Assertions#assertThat(Iterable, Class) assertThat(Iterable, element assert class)}</li>
   * <li>an assert factory used that knows how to create elements assertion, see: {@link Assertions#assertThat(Iterable, AssertFactory) assertThat(Iterable, element assert factory)}</li>
   * <li>the general <code>assertThat(Iterable)</code> and narrow down the single element with an assert factory, see: {@link #singleElement(InstanceOfAssertFactory) singleElement(element assert factory)}</li>
   * </ul>
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;String&gt; babySimpsons = list("Maggie");
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
   * List&lt;String&gt; simpsons = list("Homer", "Marge", "Lisa", "Bart", "Maggie");
   * assertThat(simpsons).singleElement();</code></pre>
   * <p>
   * If you have created the Iterable assertion using an {@link AssertFactory} or the element assert class,
   * you will be able to chain {@code singleElement()} with more specific typed assertion.
   * <p>
   * Example: use of {@code String} assertions after {@code singleElement()}
   * <pre><code class='java'> List&lt;String&gt; babySimpsons = list("Maggie");
   *
   * // assertion succeeds
   * // String assertions are available after singleElement()
   * assertThat(babySimpsons, StringAssert.class).singleElement()
   *                                             .startsWith("Mag");
   *
   * // InstanceOfAssertFactories.STRING is an AssertFactory for String assertions
   * assertThat(babySimpsons, InstanceOfAssertFactories.STRING).singleElement()
   *                                                           .startsWith("Mag");
   * // better readability with import static InstanceOfAssertFactories.STRING and Assertions.as
   * assertThat(babySimpsons, as(STRING)).singleElement()
   *                                     .startsWith("Mag");
   *
   * // assertions fail
   * assertThat(babySimpsons, StringAssert.class).singleElement()
   *                                             .startsWith("Lis");
   * // failure as the single element is not an int/Integer
   * assertThat(babySimpsons, IntegerAssert.class).singleElement()
   *                                              .startsWith("Lis");</code></pre>
   *
   * @return the assertion on the first element
   * @throws AssertionError if the actual {@link Iterable} does not contain exactly one element.
   * @see #singleElement(InstanceOfAssertFactory)
   * @since 3.17.0
   */
  @CheckReturnValue
  public ELEMENT_ASSERT singleElement() {
    return internalSingleElement();
  }

  /**
   * Verifies that the {@link Iterable} under test contains a single element and allows to perform assertions on that element, 
   * the assertions are strongly typed according to the given {@link AssertFactory} parameter.
   * <p>
   * This is a shorthand for <code>hasSize(1).first(assertFactory)</code>.
   * <p>
   * Example: use of {@code String} assertions after {@code singleElement(as(STRING))}
   * <pre><code class='java'> import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
   * import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
   * import static org.assertj.core.api.Assertions.as; // syntactic sugar
   *
   * List&lt;String&gt; babySimpsons = list("Maggie");
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
   * List&lt;String&gt; simpsons = list("Homer", "Marge", "Lisa", "Bart", "Maggie");
   * assertThat(simpsons).singleElement(as(STRING));</code></pre>
   *
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the single element
   * @throws AssertionError if the actual {@link Iterable} does not contain exactly one element.
   * @throws NullPointerException if the given factory is {@code null}.
   * @since 3.17.0
   */
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT singleElement(InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return internalSingleElement().asInstanceOf(assertFactory);
  }

  private ELEMENT_ASSERT internalSingleElement() {
    iterables.assertHasSize(info, actual, 1);
    return toAssert(actual.iterator().next(), navigationDescription("check single element"));
  }

  /**
   * This method is used in navigating assertions like {@link #first()}, {@link #last()} and {@link #element(int)} to build the
   * assertion for the given element navigated to.
   * <p>
   * Typical implementation is returning an {@link ObjectAssert} but it is possible to return a more specialized assertions
   * should you know what type of elements the iterables contain.
   *
   * @param value the element value
   * @param description describes the element, ex: "check first element" for {@link #first()}, used in assertion description.
   * @return the assertion for the given element
   */
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
   * Filters the iterable under test keeping only elements matching the given {@link Predicate}.
   * <p>
   * Example: check old employees whose age &gt; 100:
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
  public SELF filteredOn(Predicate<? super ELEMENT> predicate) {
    return internalFilteredOn(predicate);
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
    return internalAllSatisfy(requirements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF allSatisfy(ThrowingConsumer<? super ELEMENT> requirements) {
    return internalAllSatisfy(requirements);
  }

  private SELF internalAllSatisfy(Consumer<? super ELEMENT> requirements) {
    iterables.assertAllSatisfy(info, actual, requirements);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF anyMatch(Predicate<? super ELEMENT> predicate) {
    iterables.assertAnyMatch(info, actual, predicate, PredicateDescription.GIVEN);
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
   * <pre><code class='java'> List&lt;Adress&gt; addressModels = findGoodRestaurants();
   * List&lt;AdressView&gt; addressViews = convertToView(addressModels);
   *
   * // compare addressViews and addressModels respective paired elements.
   * assertThat(addressViews).zipSatisfy(addressModels, (AdressView view, Adress model) -&gt; {
   *    assertThat(view.getZipcode() + ' ' + view.getCity()).isEqualTo(model.getCityLine());
   *    assertThat(view.getStreet()).isEqualTo(model.getStreet().toUpperCase());
   * });</code></pre>
   *
   * @param <OTHER_ELEMENT> the type of the other iterable elements.
   * @param other the iterable to zip actual with.
   * @param zipRequirements the given requirements that each pair must satisfy.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given zipRequirements {@link BiConsumer} is {@code null}.
   * @throws NullPointerException if the other iterable to zip actual with is {@code null}.
   * @throws AssertionError if the {@code Iterable} under test is {@code null}.
   * @throws AssertionError if actual and other don't have the same size.
   * @throws AssertionError if one or more pairs don't satisfy the given requirements.
   * @since 3.9.0
   */
  public <OTHER_ELEMENT> SELF zipSatisfy(Iterable<OTHER_ELEMENT> other,
                                         BiConsumer<? super ELEMENT, OTHER_ELEMENT> zipRequirements) {
    iterables.assertZipSatisfy(info, actual, other, zipRequirements);
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
   * {@inheritDoc}
   */
  @Override
  public SELF anySatisfy(ThrowingConsumer<? super ELEMENT> requirements) {
    return internalAnySatisfy(requirements);
  }

  private SELF internalAnySatisfy(Consumer<? super ELEMENT> requirements) {
    iterables.assertAnySatisfy(info, actual, requirements);
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
   * {@inheritDoc}
   */
  @Override
  public SELF noneSatisfy(ThrowingConsumer<? super ELEMENT> restrictions) {
    return internalNoneSatisfy(restrictions);
  }

  private SELF internalNoneSatisfy(Consumer<? super ELEMENT> restrictions) {
    iterables.assertNoneSatisfy(info, actual, restrictions);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SafeVarargs
  public final SELF satisfiesExactly(Consumer<? super ELEMENT>... requirements) {
    return satisfiesExactlyForProxy(requirements);
  }

  /**
   * {@inheritDoc}
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
    iterables.assertSatisfiesExactly(info, actual, requirements);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SafeVarargs
  public final SELF satisfiesExactlyInAnyOrder(Consumer<? super ELEMENT>... requirements) {
    return satisfiesExactlyInAnyOrderForProxy(requirements);
  }

  /**
   * {@inheritDoc}
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
    iterables.assertSatisfiesExactlyInAnyOrder(info, actual, requirements);
    return myself;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF satisfiesOnlyOnce(Consumer<? super ELEMENT> requirements) {
    return satisfiesOnlyOnceForProxy(requirements);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF satisfiesOnlyOnce(ThrowingConsumer<? super ELEMENT> requirements) {
    return satisfiesOnlyOnceForProxy(requirements);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF satisfiesOnlyOnceForProxy(Consumer<? super ELEMENT> requirements) {
    iterables.assertSatisfiesOnlyOnce(info, actual, requirements);
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
  public SELF noneMatch(Predicate<? super ELEMENT> predicate) {
    iterables.assertNoneMatch(info, actual, predicate, PredicateDescription.GIVEN);
    return myself;
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
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator, String customComparatorDescription) {
    return super.usingComparator(customComparator, customComparatorDescription);
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
    requireNonNull(actual, "Can not perform assertions on the size of a null iterable.");
    return new IterableSizeAssert(this, IterableUtil.sizeOf(actual));
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

  /**
   * This methods is needed to build a new concrete instance of AbstractIterableAssert after a filtering operation is executed.
   * <p>
   * If you create your own subclass of AbstractIterableAssert, simply returns an instance of it in this method.
   *
   * @param iterable the iterable used to build the concrete instance of AbstractIterableAssert
   * @return concrete instance of AbstractIterableAssert
   */
  protected abstract SELF newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable);

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  SELF withAssertionState(AbstractAssert assertInstance) {
    if (assertInstance instanceof AbstractIterableAssert) {
      AbstractIterableAssert iterableAssert = (AbstractIterableAssert) assertInstance;
      return (SELF) super.withAssertionState(assertInstance).withIterables(iterableAssert.iterables)
                                                            .withTypeComparators(iterableAssert.comparatorsByType)
                                                            .withComparatorsForElementPropertyOrFieldNames(iterableAssert.comparatorsForElementPropertyOrFieldNames)
                                                            .withComparatorsForElementPropertyOrFieldTypes(iterableAssert.comparatorsForElementPropertyOrFieldTypes);
    }
    // we can go from ObjectArrayAssert -> IterableAssert when using extracting on an object array
    if (assertInstance instanceof AbstractObjectArrayAssert) {
      AbstractObjectArrayAssert objectArrayAssert = (AbstractObjectArrayAssert) assertInstance;
      return (SELF) super.withAssertionState(assertInstance).withIterables(objectArrayAssert.iterables)
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

  private SELF internalFilteredOn(Predicate<? super ELEMENT> predicate) {
    checkArgument(predicate != null, "The filter predicate should not be null");
    List<? extends ELEMENT> filteredIterable = stream(actual.spliterator(), false).filter(predicate).collect(toList());
    return newAbstractIterableAssert(filteredIterable).withAssertionState(myself);
  }
}
