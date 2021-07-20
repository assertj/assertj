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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.error.AnyElementShouldMatch.anyElementShouldMatch;
import static org.assertj.core.error.ConditionAndGroupGenericParameterTypeShouldBeTheSame.shouldBeSameGenericBetweenIterableAndCondition;
import static org.assertj.core.error.ElementsShouldBe.elementsShouldBe;
import static org.assertj.core.error.ElementsShouldBeAtLeast.elementsShouldBeAtLeast;
import static org.assertj.core.error.ElementsShouldBeAtMost.elementsShouldBeAtMost;
import static org.assertj.core.error.ElementsShouldBeExactly.elementsShouldBeExactly;
import static org.assertj.core.error.ElementsShouldHave.elementsShouldHave;
import static org.assertj.core.error.ElementsShouldHaveAtLeast.elementsShouldHaveAtLeast;
import static org.assertj.core.error.ElementsShouldHaveAtMost.elementsShouldHaveAtMost;
import static org.assertj.core.error.ElementsShouldHaveExactly.elementsShouldHaveExactly;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ElementsShouldNotBe.elementsShouldNotBe;
import static org.assertj.core.error.ElementsShouldNotHave.elementsShouldNotHave;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfy;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyExactly;
import static org.assertj.core.error.NoElementsShouldMatch.noElementsShouldMatch;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.error.ShouldContainNull.shouldContainNull;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.error.ShouldContainOnlyNulls.shouldContainOnlyNulls;
import static org.assertj.core.error.ShouldContainSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldContainSubsequence.shouldContainSubsequence;
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainNull.shouldNotContainNull;
import static org.assertj.core.error.ShouldNotContainSequence.shouldNotContainSequence;
import static org.assertj.core.error.ShouldNotContainSubsequence.shouldNotContainSubsequence;
import static org.assertj.core.error.ShouldNotHaveDuplicates.shouldNotHaveDuplicates;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfyExactlyInAnyOrder;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.error.ZippedElementsShouldSatisfy.zippedElementsShouldSatisfy;
import static org.assertj.core.internal.Arrays.assertIsArray;
import static org.assertj.core.internal.CommonValidations.checkIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkIsNotNullAndNotEmpty;
import static org.assertj.core.internal.CommonValidations.checkIterableIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkSizeBetween;
import static org.assertj.core.internal.CommonValidations.checkSizeGreaterThan;
import static org.assertj.core.internal.CommonValidations.checkSizeGreaterThanOrEqualTo;
import static org.assertj.core.internal.CommonValidations.checkSizeLessThan;
import static org.assertj.core.internal.CommonValidations.checkSizeLessThanOrEqualTo;
import static org.assertj.core.internal.CommonValidations.checkSizes;
import static org.assertj.core.internal.CommonValidations.failIfEmptySinceActualIsNotEmpty;
import static org.assertj.core.internal.CommonValidations.hasSameSizeAsCheck;
import static org.assertj.core.internal.ErrorMessages.emptySequence;
import static org.assertj.core.internal.ErrorMessages.emptySubsequence;
import static org.assertj.core.internal.ErrorMessages.nullSequence;
import static org.assertj.core.internal.ErrorMessages.nullSubsequence;
import static org.assertj.core.internal.IterableDiff.diff;
import static org.assertj.core.util.Arrays.prepend;
import static org.assertj.core.util.IterableUtil.isNullOrEmpty;
import static org.assertj.core.util.IterableUtil.sizeOf;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Streams.stream;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.error.UnsatisfiedRequirement;
import org.assertj.core.error.ZippedElementsShouldSatisfy.ZipSatisfyError;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Iterable}</code>s.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Maciej Jaskowski
 * @author Nicolas François
 * @author Joel Costigliola
 * @author Florent Biville
 */
public class Iterables {

  private static final Iterables INSTANCE = new Iterables();
  private final ComparisonStrategy comparisonStrategy;
  @VisibleForTesting
  Failures failures = Failures.instance();
  @VisibleForTesting
  Conditions conditions = Conditions.instance();
  @VisibleForTesting
  Predicates predicates = Predicates.instance();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   *
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Iterables instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Iterables() {
    this(StandardComparisonStrategy.instance());
  }

  public Iterables(ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy) {
      return ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator();
    }
    return null;
  }

  @VisibleForTesting
  public ComparisonStrategy getComparisonStrategy() {
    return comparisonStrategy;
  }

  /**
   * Asserts that the given <code>{@link Iterable}</code> is {@code null} or empty.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @throws AssertionError if the given {@code Iterable} is not {@code null} *and* contains one or more elements.
   */
  public void assertNullOrEmpty(AssertionInfo info, Iterable<?> actual) {
    if (!isNullOrEmpty(actual)) throw failures.failure(info, shouldBeNullOrEmpty(actual));
  }

  /**
   * Asserts that the given {@code Iterable} is empty.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} is not empty.
   */
  public void assertEmpty(AssertionInfo info, Iterable<?> actual) {
    assertNotNull(info, actual);
    if (!isNullOrEmpty(actual)) throw failures.failure(info, shouldBeEmpty(actual));
  }

  /**
   * Asserts that the given {@code Iterable} is not empty.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} is empty.
   */
  public void assertNotEmpty(AssertionInfo info, Iterable<?> actual) {
    assertNotNull(info, actual);
    if (isNullOrEmpty(actual)) throw failures.failure(info, shouldNotBeEmpty());
  }

  /**
   * Asserts that the number of elements in the given {@code Iterable} is equal to the expected one.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param expectedSize the expected size of {@code actual}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the number of elements in the given {@code Iterable} is different than the expected one.
   */
  public void assertHasSize(AssertionInfo info, Iterable<?> actual, int expectedSize) {
    assertNotNull(info, actual);
    checkSizes(actual, sizeOf(actual), expectedSize, info);
  }

  /**
   * Asserts that the unique element of the {@link Iterable} satisfies the given assertions expressed as a {@link Consumer},
   *
   * @param <T> the type of elements in actual.
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param consumer the given requirements.
   * @throws AssertionError if the {@link Iterable} does not have a unique element.
   * @throws AssertionError if the {@link Iterable}'s unique element does not satisfies the given assertions.
   */
  public <T> void assertHasOnlyOneElementSatisfying(AssertionInfo info, Iterable<? extends T> actual,
                                                    Consumer<? super T> consumer) {
    assertHasSize(info, actual, 1);
    consumer.accept(actual.iterator().next());
  }

  /**
   * Asserts that the number of elements in the given {@code Iterable} is greater than the boundary.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param boundary  the given value to compare the size of {@code actual} to.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the number of elements in the given {@code Iterable} is greater than the boundary.
   */
  public void assertHasSizeGreaterThan(AssertionInfo info, Iterable<?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeGreaterThan(actual, boundary, sizeOf(actual), info);
  }

  /**
   * Asserts that the number of elements in the given {@code Iterable} is greater than or equal to the boundary.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param boundary  the given value to compare the size of {@code actual} to.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the number of elements in the given {@code Iterable} is greater than or equal to the boundary.
   */
  public void assertHasSizeGreaterThanOrEqualTo(AssertionInfo info, Iterable<?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeGreaterThanOrEqualTo(actual, boundary, sizeOf(actual), info);
  }

  /**
   * Asserts that the number of elements in the given {@code Iterable} is less than the boundary.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param boundary  the given value to compare the size of {@code actual} to.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the number of elements in the given {@code Iterable} is less than the expected one.
   */
  public void assertHasSizeLessThan(AssertionInfo info, Iterable<?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeLessThan(actual, boundary, sizeOf(actual), info);
  }

  /**
   * Asserts that the number of elements in the given {@code Iterable} is less than or equal to the boundary.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param boundary  the given value to compare the size of {@code actual} to.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the number of elements in the given {@code Iterable} is less than or equal to the boundary.
   */
  public void assertHasSizeLessThanOrEqualTo(AssertionInfo info, Iterable<?> actual, int boundary) {
    assertNotNull(info, actual);
    checkSizeLessThanOrEqualTo(actual, boundary, sizeOf(actual), info);
  }

  /**
   * Asserts that the number of elements in the given {@code Iterable} is between the given lower and higher boundary (inclusive).
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of elements in the given array is not between the boundaries.
   */
  public void assertHasSizeBetween(AssertionInfo info, Iterable<?> actual, int lowerBoundary, int higherBoundary) {
    assertNotNull(info, actual);
    checkSizeBetween(actual, lowerBoundary, higherBoundary, sizeOf(actual), info);
  }

  /**
   * Assert that the actual {@code Iterable} has the same size as the other array.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param other the given array to compare.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other group is {@code null}.
   * @throws AssertionError if actual {@code Iterable} and other array don't have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, Iterable<?> actual, Object other) {
    assertNotNull(info, actual);
    assertIsArray(info, other);
    hasSameSizeAsCheck(info, actual, other, sizeOf(actual));
  }

  /**
   * Assert that the actual {@code Iterable} has the same size as the other {@code Iterable}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param other the given {@code Iterable}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other group is {@code null}.
   * @throws AssertionError if actual and other {@code Iterable} don't have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, Iterable<?> actual, Iterable<?> other) {
    assertNotNull(info, actual);
    hasSameSizeAsCheck(info, actual, other, sizeOf(actual));
  }

  /**
   * Asserts that the given {@code Iterable} contains the given values, in any order.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param values the values that are expected to be in the given {@code Iterable}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not contain the given values.
   */
  public void assertContains(AssertionInfo info, Iterable<?> actual, Object[] values) {
    final List<?> actualAsList = newArrayList(actual);
    if (commonCheckThatIterableAssertionSucceeds(info, actualAsList, values)) return;
    // check for elements in values that are missing in actual.
    assertIterableContainsGivenValues(actual.getClass(), actualAsList, values, info);
  }

  private void assertIterableContainsGivenValues(@SuppressWarnings("rawtypes") Class<? extends Iterable> clazz,
                                                 Iterable<?> actual, Object[] values, AssertionInfo info) {
    Set<Object> notFound = stream(values).filter(value -> !iterableContains(actual, value))
                                         .collect(toCollection(LinkedHashSet::new));
    if (notFound.isEmpty())
      return;
    throw failures.failure(info, shouldContain(clazz, actual, values, notFound, comparisonStrategy));
  }

  private boolean iterableContains(Iterable<?> actual, Object value) {
    return comparisonStrategy.iterableContains(actual, value);
  }

  private void iterablesRemoveFirst(Iterable<?> actual, Object value) {
    comparisonStrategy.iterablesRemoveFirst(actual, value);
  }

  private void iterablesRemove(Iterable<?> actual, Object value) {
    comparisonStrategy.iterableRemoves(actual, value);
  }

  /**
   * Asserts that the given {@code Iterable} contains only the given values and nothing else, in any order.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param expectedValues the values that are expected to be in the given {@code Iterable}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not contain the given values or if the given
   *           {@code Iterable} contains values that are not in the given array.
   */
  public void assertContainsOnly(AssertionInfo info, Iterable<?> actual, Object[] expectedValues) {
    final List<?> actualAsList = newArrayList(actual);
    checkNotNullIterables(info, actualAsList, expectedValues);
    // if both actual and values are empty, then assertion passes.
    if (actualAsList.isEmpty() && expectedValues.length == 0) return;

    // after the for loop, unexpected = expectedValues - actual
    List<Object> unexpectedValues = newArrayList(actualAsList);
    // after the for loop, missing = actual - expectedValues
    List<Object> missingValues = newArrayList(expectedValues);
    for (Object expected : expectedValues) {
      if (iterableContains(actualAsList, expected)) {
        // since expected was found in actual:
        // -- it does not belong to the missing elements
        iterablesRemove(missingValues, expected);
        // -- it does not belong to the unexpected elements
        iterablesRemove(unexpectedValues, expected);
      }
    }

    if (!unexpectedValues.isEmpty() || !missingValues.isEmpty()) {
      throw failures.failure(info, shouldContainOnly(actualAsList, expectedValues,
                                                     missingValues, unexpectedValues,
                                                     comparisonStrategy));
    }
  }

  /**
   * Asserts that the given {@code Iterable} contains the given values and only once.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param values the values that are expected to be in the given {@code Iterable}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not contain the given values or if the given
   *           {@code Iterable} contains values that are not in the given array.
   */
  public void assertContainsOnlyOnce(AssertionInfo info, Iterable<?> actual, Object[] values) {
    if (commonCheckThatIterableAssertionSucceeds(info, actual, values)) return;
    // check for elements in values that are missing in actual.
    Set<Object> notFound = new LinkedHashSet<>();
    Set<Object> notOnlyOnce = new LinkedHashSet<>();
    Iterable<?> actualDuplicates = comparisonStrategy.duplicatesFrom(actual);
    for (Object expectedOnlyOnce : values) {
      if (!iterableContains(actual, expectedOnlyOnce)) {
        notFound.add(expectedOnlyOnce);
      } else if (iterableContains(actualDuplicates, expectedOnlyOnce)) {
        notOnlyOnce.add(expectedOnlyOnce);
      }
    }
    if (!notFound.isEmpty() || !notOnlyOnce.isEmpty())
      throw failures.failure(info, shouldContainsOnlyOnce(actual, values, notFound, notOnlyOnce, comparisonStrategy));
    // assertion succeeded
  }

  /**
   * Asserts that the given {@code Iterable} contains only null elements and nothing else.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not contain at least a null element or if the given
   *           {@code Iterable} contains values that are not null elements.
   */
  public void assertContainsOnlyNulls(AssertionInfo info, Iterable<?> actual) {
    assertNotNull(info, actual);
    // empty => no null elements => failure
    if (sizeOf(actual) == 0) throw failures.failure(info, shouldContainOnlyNulls(actual));
    // look for any non null elements
    List<Object> nonNullElements = stream(actual).filter(java.util.Objects::nonNull).collect(toList());
    if (nonNullElements.size() > 0) throw failures.failure(info, shouldContainOnlyNulls(actual, nonNullElements));
  }

  /**
   * Verifies that the given <code>{@link Iterable}</code> contains the given sequence of objects, without any other
   * objects between them.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param sequence the sequence of objects to look for.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws IllegalArgumentException if the given sequence is empty.
   * @throws AssertionError if the given {@code Iterable} does not contain the given sequence of objects.
   */
  public void assertContainsSequence(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    // perform the checks that would have been done in commonCheckThatIterableAssertionSucceeds but do them explicitly without
    // having to create a new iterator on actual - which would break if actual were only singly-traversable.
    checkNotNullIterables(info, actual, sequence);
    // store the elements from actual that have been visited (because we don't know we can look ahead - the 'actual'
    // might be singly-traversable) in a fixed-length LIFO having what is in effect a sliding window.
    // So we store each element and slide for each new element until a match is found or until the 'actual' is
    // exhausted. Of course if 'actual' really is infinite then this could take a while :-D
    final Iterator<?> actualIterator = actual.iterator();
    if (!actualIterator.hasNext() && sequence.length == 0) return;
    failIfEmptySinceActualIsNotEmpty(sequence);
    // we only store sequence.length entries from actual in the LIFO, no need for more.
    Lifo lifo = new Lifo(sequence.length);
    while (actualIterator.hasNext()) {
      lifo.add(actualIterator.next());
      if (lifo.matchesExactly(sequence)) return;
    }
    throw actualDoesNotContainSequence(info, actual, sequence);
  }

  private class Lifo {
    private int maxSize;
    private LinkedList<Object> stack;

    Lifo(int maxSize) {
      this.maxSize = maxSize;
      stack = new LinkedList<>();
    }

    void add(final Object element) {
      if (stack.size() == maxSize) stack.removeFirst();
      stack.addLast(element);
    }

    boolean matchesExactly(Object[] sequence) {
      if (stack.size() != sequence.length) return false;
      for (int i = 0; i < sequence.length; i++) {
        if (!areEqual(stack.get(i), sequence[i])) return false;
      }
      return true;
    }
  }

  /**
   * Verifies that the given <code>{@link Iterable}</code> does not contain the given sequence of objects in order.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param sequence the sequence of objects to look for.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws IllegalArgumentException if the given sequence is empty.
   * @throws AssertionError if the given {@code Iterable} does contain the given sequence of objects.
   */
  public void assertDoesNotContainSequence(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    requireNonNull(sequence, nullSequence());
    checkIsNotEmptySequence(sequence);
    assertNotNull(info, actual);
    // check for elements in values that are missing in actual.
    List<?> actualAsList = newArrayList(actual);
    for (int index = 0; index < actualAsList.size(); index++) {
      // look for given sequence in actual starting from current index (i)
      if (containsSequenceAtGivenIndex(actualAsList, sequence, index)) {
        throw actualDoesContainSequence(info, actual, sequence, index);
      }
    }
  }

  /**
   * Verifies that the given <code>{@link Iterable}</code> contains the given subsequence of objects (possibly with
   * other values between them).
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param subsequence the subsequence of objects to look for.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws IllegalArgumentException if the given subsequence is empty.
   * @throws AssertionError if the given {@code Iterable} does not contain the given subsequence of objects.
   */
  public void assertContainsSubsequence(AssertionInfo info, Iterable<?> actual, Object[] subsequence) {
    if (commonCheckThatIterableAssertionSucceeds(info, actual, subsequence)) return;

    Iterator<?> actualIterator = actual.iterator();
    int subsequenceIndex = 0;
    while (actualIterator.hasNext() && subsequenceIndex < subsequence.length) {
      Object actualNext = actualIterator.next();
      Object subsequenceNext = subsequence[subsequenceIndex];
      if (areEqual(actualNext, subsequenceNext)) subsequenceIndex++;
    }

    if (subsequenceIndex < subsequence.length) throw actualDoesNotContainSubsequence(info, actual, subsequence);
  }

  public void assertContainsSubsequence(AssertionInfo info, Iterable<?> actual, List<?> subsequence) {
    checkIsNotNull(subsequence);
    assertContainsSubsequence(info, actual, subsequence.toArray());
  }

  /**
   * Verifies that the given <code>{@link Iterable}</code> does not contain the given subsequence of objects (possibly
   * with other values between them).
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param subsequence the subsequence of objects to look for.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws IllegalArgumentException if the given subsequence is empty.
   * @throws AssertionError if the given {@code Iterable} contains the given subsequence of objects.
   */
  public void assertDoesNotContainSubsequence(AssertionInfo info, Iterable<?> actual, Object[] subsequence) {
    requireNonNull(subsequence, nullSubsequence());
    checkIsNotEmptySubsequence(subsequence);
    assertNotNull(info, actual);

    int subsequenceIndex = 0;
    int subsequenceStartIndex = 0;

    List<?> actualAsList = newArrayList(actual);
    for (int index = 0; index < actualAsList.size(); index++) {
      Object actualNext = actualAsList.get(index);
      Object subsequenceNext = subsequence[subsequenceIndex];
      if (areEqual(actualNext, subsequenceNext)) {
        if (subsequenceIndex == 0) subsequenceStartIndex = index;
        subsequenceIndex++;
      }
      if (subsequenceIndex == subsequence.length) {
        throw actualContainsSubsequence(info, actual, subsequence, subsequenceStartIndex);
      }
    }
  }

  /**
   * Verifies that the actual <code>Iterable</code> is a subset of values <code>Iterable</code>. <br>
   * Both actual and given iterable are treated as sets, therefore duplicates on either of them are ignored.
   *
   * @param info contains information about the assertion.
   * @param actual the actual {@code Iterable}.
   * @param values the {@code Iterable} that should contain all actual elements.
   * @throws AssertionError if the actual {@code Iterable} is {@code null}.
   * @throws NullPointerException if the given Iterable is {@code null}.
   * @throws AssertionError if the actual {@code Iterable} is not subset of set <code>{@link Iterable}</code>
   */
  public void assertIsSubsetOf(AssertionInfo info, Iterable<?> actual, Iterable<?> values) {
    assertNotNull(info, actual);
    checkIterableIsNotNull(values);
    List<Object> extra = stream(actual).filter(actualElement -> !iterableContains(values, actualElement))
                                       .collect(toList());
    if (extra.size() > 0) throw failures.failure(info, shouldBeSubsetOf(actual, values, extra, comparisonStrategy));
  }

  /**
   * Return true if actualAsList contains exactly the given sequence at given starting index, false otherwise.
   *
   * @param actualAsList the list to look sequence in
   * @param sequence the sequence to look for
   * @param startingIndex the index of actual list at which we start looking for sequence.
   * @return true if actualAsList contains exactly the given sequence at given starting index, false otherwise.
   */
  private boolean containsSequenceAtGivenIndex(List<?> actualAsList, Object[] sequence, int startingIndex) {
    // check that, starting from given index, actualAsList has enough remaining elements to contain sequence
    if (actualAsList.size() - startingIndex < sequence.length) return false;
    for (int i = 0; i < sequence.length; i++) {
      if (!areEqual(actualAsList.get(startingIndex + i), sequence[i])) return false;
    }
    return true;
  }

  private boolean areEqual(Object actual, Object other) {
    return comparisonStrategy.areEqual(actual, other);
  }

  private AssertionError actualDoesNotContainSequence(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    return failures.failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
  }

  private AssertionError actualDoesContainSequence(AssertionInfo info, Iterable<?> actual, Object[] sequence,
                                                   int index) {
    return failures.failure(info, shouldNotContainSequence(actual, sequence, index, comparisonStrategy));
  }

  private AssertionError actualDoesNotContainSubsequence(AssertionInfo info, Iterable<?> actual, Object[] subsequence) {
    return failures.failure(info, shouldContainSubsequence(actual, subsequence, comparisonStrategy));
  }

  private AssertionError actualContainsSubsequence(AssertionInfo info, Iterable<?> actual, Object[] subsequence,
                                                   int index) {
    return failures.failure(info, shouldNotContainSubsequence(actual, subsequence, comparisonStrategy, index));
  }

  /**
   * Asserts that the given {@code Iterable} does not contain the given values.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param values the values that are expected not to be in the given {@code Iterable}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} contains any of given values.
   */
  public void assertDoesNotContain(AssertionInfo info, Iterable<?> actual, Object[] values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, actual);
    Set<Object> found = new LinkedHashSet<>();
    for (Object o : values) {
      if (iterableContains(actual, o)) found.add(o);
    }
    if (!found.isEmpty()) throw failures.failure(info, shouldNotContain(actual, values, found, comparisonStrategy));
  }

  /**
   * Asserts that the given {@code Iterable} does not contain the given values.
   *
   * @param <T> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param iterable the values that are expected not to be in the given {@code Iterable}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} contains any of given values.
   */
  public <T> void assertDoesNotContainAnyElementsOf(AssertionInfo info, Iterable<? extends T> actual,
                                                    Iterable<? extends T> iterable) {
    checkIsNotNullAndNotEmpty(iterable);
    List<T> values = newArrayList(iterable);
    assertDoesNotContain(info, actual, values.toArray());
  }

  /**
   * Asserts that the given {@code Iterable} does not have duplicate values.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} contains duplicate values.
   */
  public void assertDoesNotHaveDuplicates(AssertionInfo info, Iterable<?> actual) {
    assertNotNull(info, actual);
    Iterable<?> duplicates = comparisonStrategy.duplicatesFrom(actual);
    if (!isNullOrEmpty(duplicates))
      throw failures.failure(info, shouldNotHaveDuplicates(actual, duplicates, comparisonStrategy));
  }

  /**
   * Verifies that the given {@code Iterable} starts with the given sequence of objects, without any other objects
   * between them. Similar to <code>{@link #assertContainsSequence(AssertionInfo, Iterable, Object[])}</code>, but it
   * also verifies that the first element in the sequence is also the first element of the given {@code Iterable}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param sequence the sequence of objects to look for.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not start with the given sequence of objects.
   */
  public void assertStartsWith(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    if (commonCheckThatIterableAssertionSucceeds(info, actual, sequence)) return;
    int i = 0;
    for (Object actualCurrentElement : actual) {
      if (i >= sequence.length) break;
      if (areEqual(actualCurrentElement, sequence[i++])) continue;
      throw actualDoesNotStartWithSequence(info, actual, sequence);
    }
    if (sequence.length > i) {
      // sequence has more elements than actual
      throw actualDoesNotStartWithSequence(info, actual, sequence);
    }
  }

  private AssertionError actualDoesNotStartWithSequence(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    return failures.failure(info, shouldStartWith(actual, sequence, comparisonStrategy));
  }

  /**
   * Verifies that the given {@code Iterable} ends with the given sequence of objects, without any other objects between
   * them. Similar to <code>{@link #assertContainsSequence(AssertionInfo, Iterable, Object[])}</code>, but it also
   * verifies that the last element in the sequence is also the last element of the given {@code Iterable}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param first the first element of the end sequence.
   * @param rest the optional next elements of the end sequence.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not end with the given sequence of objects.
   */
  public void assertEndsWith(AssertionInfo info, Iterable<?> actual, Object first, Object[] rest) {
    Object[] sequence = prepend(first, rest);
    assertEndsWith(info, actual, sequence);
  }

  /**
   * Verifies that the given {@code Iterable} ends with the given sequence of objects, without any other objects between
   * them. Similar to <code>{@link #assertContainsSequence(AssertionInfo, Iterable, Object[])}</code>, but it also
   * verifies that the last element in the sequence is also the last element of the given {@code Iterable}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param sequence the sequence of objects to look for.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not end with the given sequence of objects.
   */
  public void assertEndsWith(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    checkNotNullIterables(info, actual, sequence);

    int sizeOfActual = sizeOf(actual);
    if (sizeOfActual < sequence.length) throw actualDoesNotEndWithSequence(info, actual, sequence);

    int start = sizeOfActual - sequence.length;
    int sequenceIndex = 0, indexOfActual = 0;
    for (Object actualElement : actual) {
      if (indexOfActual++ < start) continue;
      if (areEqual(actualElement, sequence[sequenceIndex++])) continue;
      throw actualDoesNotEndWithSequence(info, actual, sequence);
    }
  }

  private boolean commonCheckThatIterableAssertionSucceeds(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    checkNotNullIterables(info, actual, sequence);
    // if both actual and values are empty, then assertion passes.
    if (!actual.iterator().hasNext() && sequence.length == 0) return true;
    failIfEmptySinceActualIsNotEmpty(sequence);
    return false;
  }

  private void checkNotNullIterables(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    checkIsNotNull(sequence);
    assertNotNull(info, actual);
  }

  /**
   * Asserts that the given {@code Iterable} contains at least a null element.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not contain at least a null element.
   */
  public void assertContainsNull(AssertionInfo info, Iterable<?> actual) {
    assertNotNull(info, actual);
    if (!iterableContains(actual, null)) throw failures.failure(info, shouldContainNull(actual));
  }

  /**
   * Asserts that the given {@code Iterable} does not contain null elements.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} contains a null element.
   */
  public void assertDoesNotContainNull(AssertionInfo info, Iterable<?> actual) {
    assertNotNull(info, actual);
    if (iterableContains(actual, null)) throw failures.failure(info, shouldNotContainNull(actual));
  }

  /**
   * Assert that each element of given {@code Iterable} satisfies the given condition.
   *
   * @param <T> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to T.
   * @throws AssertionError if one or more elements do not satisfy the given condition.
   */
  public <T> void assertAre(AssertionInfo info, Iterable<? extends T> actual, Condition<? super T> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<T> notSatisfiesCondition = notSatisfyingCondition(actual, condition);
      if (!notSatisfiesCondition.isEmpty())
        throw failures.failure(info, elementsShouldBe(actual, notSatisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that each element of given {@code Iterable} not satisfies the given condition.
   *
   * @param <E> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to E.
   * @throws AssertionError if one or more elements satisfy the given condition.
   */
  public <E> void assertAreNot(AssertionInfo info, Iterable<? extends E> actual, Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (!satisfiesCondition.isEmpty())
        throw failures.failure(info, elementsShouldNotBe(actual, satisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that each element of given {@code Iterable} satisfies the given condition.
   *
   * @param <E> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to E.
   * @throws AssertionError if one or more elements do not satisfy the given condition.
   */
  public <E> void assertHave(AssertionInfo info, Iterable<? extends E> actual, Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfyingCondition(actual, condition);
      if (!notSatisfiesCondition.isEmpty())
        throw failures.failure(info, elementsShouldHave(actual, notSatisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that each element of given {@code Iterable} not satisfies the given condition.
   *
   * @param <E> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to E.
   * @throws AssertionError if one or more elements satisfy the given condition.
   */
  public <E> void assertDoNotHave(AssertionInfo info, Iterable<? extends E> actual, Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (!satisfiesCondition.isEmpty())
        throw failures.failure(info, elementsShouldNotHave(actual, satisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that there are <b>at least</b> <i>n</i> elements in the actual {@code Iterable} satisfying the given
   * condition.
   *
   * @param <E> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param times the minimum number of times the condition should be verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &lt; n.
   */
  public <E> void assertAreAtLeast(AssertionInfo info, Iterable<? extends E> actual, int times,
                                   Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      if (!conditionIsSatisfiedAtLeastNTimes(actual, times, condition))
        throw failures.failure(info, elementsShouldBeAtLeast(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  private <E> boolean conditionIsSatisfiedAtLeastNTimes(Iterable<? extends E> actual, int n,
                                                        Condition<? super E> condition) {
    List<E> satisfiesCondition = satisfiesCondition(actual, condition);
    return satisfiesCondition.size() >= n;
  }

  /**
   * Assert that there are <b>at most</b> <i>n</i> elements in the actual {@code Iterable} satisfying the given
   * condition.
   *
   * @param <E> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param n the number of times the condition should be at most verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  public <E> void assertAreAtMost(AssertionInfo info, Iterable<? extends E> actual, int n,
                                  Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      if (!conditionIsSatisfiedAtMostNTimes(actual, condition, n))
        throw failures.failure(info, elementsShouldBeAtMost(actual, n, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  private <E> boolean conditionIsSatisfiedAtMostNTimes(Iterable<? extends E> actual, Condition<? super E> condition,
                                                       int n) {
    List<E> satisfiesCondition = satisfiesCondition(actual, condition);
    return satisfiesCondition.size() <= n;
  }

  /**
   * Verifies that there are <b>exactly</b> <i>n</i> elements in the actual {@code Iterable} satisfying the given
   * condition.
   *
   * @param <E> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param times the exact number of times the condition should be verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  public <E> void assertAreExactly(AssertionInfo info, Iterable<? extends E> actual, int times,
                                   Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      if (!conditionIsSatisfiedNTimes(actual, condition, times))
        throw failures.failure(info, elementsShouldBeExactly(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  private <E> boolean conditionIsSatisfiedNTimes(Iterable<? extends E> actual, Condition<? super E> condition,
                                                 int times) {
    List<E> satisfiesCondition = satisfiesCondition(actual, condition);
    return satisfiesCondition.size() == times;
  }

  /**
   * An alias method of {@link #assertAreAtLeast(AssertionInfo, Iterable, int, Condition)} to provide a richer fluent
   * api (same logic, only error message differs).
   *
   * @param <E> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param times the minimum number of times the condition should be verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &lt; n.
   */
  public <E> void assertHaveAtLeast(AssertionInfo info, Iterable<? extends E> actual, int times,
                                    Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      if (!conditionIsSatisfiedAtLeastNTimes(actual, times, condition))
        throw failures.failure(info, elementsShouldHaveAtLeast(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * An alias method of {@link #assertAreAtMost(AssertionInfo, Iterable, int, Condition)} to provide a richer fluent api
   * (same logic, only error message differs).
   *
   * @param <E> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param times the number of times the condition should be at most verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  public <E> void assertHaveAtMost(AssertionInfo info, Iterable<? extends E> actual, int times,
                                   Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      if (!conditionIsSatisfiedAtMostNTimes(actual, condition, times))
        throw failures.failure(info, elementsShouldHaveAtMost(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * An alias method of {@link #assertAreExactly(AssertionInfo, Iterable, int, Condition)} to provide a richer fluent
   * api (same logic, only error message differs).
   *
   * @param <E> the type of actual elements
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param times the exact number of times the condition should be verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  public <E> void assertHaveExactly(AssertionInfo info, Iterable<? extends E> actual, int times,
                                    Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      if (!conditionIsSatisfiedNTimes(actual, condition, times))
        throw failures.failure(info, elementsShouldHaveExactly(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Asserts that the given {@code Iterable} contains all the elements of the other {@code Iterable}, in any order.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param other the other {@code Iterable}.
   * @throws NullPointerException if {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not contain all the elements of the other
   *           {@code Iterable}, in any order.
   */
  public void assertContainsAll(AssertionInfo info, Iterable<?> actual, Iterable<?> other) {
    final List<?> actualAsList = newArrayList(actual);
    checkIterableIsNotNull(other);
    assertNotNull(info, actualAsList);
    Object[] values = newArrayList(other).toArray();
    assertIterableContainsGivenValues(actual.getClass(), actualAsList, values, info);
  }

  /**
   * Asserts that the given {@code Iterable} contains exactly the given values and nothing else, <b>in order</b>.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param values the values that are expected to be in the given {@code Iterable} in order.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not contain the given values or if the given
   *           {@code Iterable} contains values that are not in the given array, in order.
   */
  public void assertContainsExactly(AssertionInfo info, Iterable<?> actual, Object[] values) {
    checkIsNotNull(values);
    assertNotNull(info, actual);

    List<Object> actualAsList = newArrayList(actual);
    IterableDiff diff = diff(actualAsList, asList(values), comparisonStrategy);
    if (!diff.differencesFound()) {
      // actual and values have the same elements but are they in the same order ?
      int i = 0;
      // use actualAsList instead of actual in case actual is a singly-passable iterable
      for (Object elementFromActual : actualAsList) {
        if (!areEqual(elementFromActual, values[i])) {
          throw failures.failure(info, elementsDifferAtIndex(elementFromActual, values[i], i, comparisonStrategy));
        }
        i++;
      }
      return;
    }
    throw failures.failure(info,
                           shouldContainExactly(actual, asList(values), diff.missing, diff.unexpected,
                                                comparisonStrategy));
  }

  public <E> void assertAllSatisfy(AssertionInfo info, Iterable<? extends E> actual, Consumer<? super E> requirements) {
    assertNotNull(info, actual);
    requireNonNull(requirements, "The Consumer<T> expressing the assertions requirements must not be null");

    List<UnsatisfiedRequirement> unsatisfiedRequirements = stream(actual).map(element -> failsRequirements(requirements, element))
                                                                         .filter(Optional::isPresent)
                                                                         .map(Optional::get)
                                                                         .collect(toList());
    if (!unsatisfiedRequirements.isEmpty())
      throw failures.failure(info, elementsShouldSatisfy(actual, unsatisfiedRequirements, info));
  }

  private static <E> Optional<UnsatisfiedRequirement> failsRequirements(Consumer<? super E> requirements, E element) {
    try {
      requirements.accept(element);
    } catch (AssertionError ex) {
      return Optional.of(new UnsatisfiedRequirement(element, ex.getMessage()));
    }
    return Optional.empty();
  }

  public <E> void assertSatisfiesExactly(AssertionInfo info, Iterable<? extends E> actual,
                                         Consumer<? super E>[] allRequirements) {
    assertNotNull(info, actual);
    assertHasSameSizeAs(info, actual, allRequirements); // TODO
    List<E> actualAsList = newArrayList(actual);
    Map<Integer, UnsatisfiedRequirement> unsatisfiedRequirements = new HashMap<>();
    for (int i = 0; i < allRequirements.length; i++) {
      final int index = i;
      E elementAtIndex = actualAsList.get(index);
      Consumer<? super E> requirementsAtIndex = allRequirements[index];
      failsRequirements(requirementsAtIndex, elementAtIndex).ifPresent(req -> unsatisfiedRequirements.put(index, req));
    }
    if (!unsatisfiedRequirements.isEmpty())
      throw failures.failure(info, elementsShouldSatisfyExactly(actual, unsatisfiedRequirements, info));

  }

  public <E> void assertSatisfiesExactlyInAnyOrder(AssertionInfo info, Iterable<? extends E> actual,
                                                   Consumer<? super E>[] consumers) {
    assertNotNull(info, actual);
    requireNonNull(consumers, "The Consumer<? super E>... expressing the assertions must not be null");
    for (Consumer<? super E> consumer : consumers)
      requireNonNull(consumer, "Elements in the Consumer<? super E>... expressing the assertions must not be null");

    checkSizes(actual, sizeOf(actual), consumers.length, info);
    Deque<ElementsSatisfyingConsumer<E>> satisfiedElementsPerConsumer = satisfiedElementsPerConsumer(actual, consumers);
    // fail fast check
    boolean someRequirementsAreNotMet = satisfiedElementsPerConsumer.stream().anyMatch(e -> e.getElements().isEmpty());
    if (someRequirementsAreNotMet) throw failures.failure(info, shouldSatisfyExactlyInAnyOrder(actual));

    if (!areAllConsumersSatisfied(satisfiedElementsPerConsumer))
      throw failures.failure(info, shouldSatisfyExactlyInAnyOrder(actual));
  }

  @SafeVarargs
  private static <E> Deque<ElementsSatisfyingConsumer<E>> satisfiedElementsPerConsumer(Iterable<? extends E> actual,
                                                                                       Consumer<? super E>... consumers) {
    return stream(consumers).map(consumer -> new ElementsSatisfyingConsumer<E>(actual, consumer))
                            .collect(toCollection(ArrayDeque::new));
  }

  private static <E> boolean areAllConsumersSatisfied(Queue<ElementsSatisfyingConsumer<E>> satisfiedElementsPerConsumer) {
    // recursively test whether we can find any specific matching permutation that can meet the requirements
    if (satisfiedElementsPerConsumer.isEmpty()) return true; // all consumers have been satisfied

    // pop the head (i.e, elements satisfying the current consumer), process the tail (i.e., remaining consumers)...
    ElementsSatisfyingConsumer<E> head = satisfiedElementsPerConsumer.remove();
    List<E> elementsSatisfyingCurrentConsumer = head.getElements();
    if (elementsSatisfyingCurrentConsumer.isEmpty()) return false;   // no element satisfies current consumer
    // if we remove an element satisfying the current consumer from all remaining consumers, will other elements still satisfy
    // the remaining consumers?
    return elementsSatisfyingCurrentConsumer.stream()
                                            .map(element -> removeElement(satisfiedElementsPerConsumer, element))
                                            .anyMatch(Iterables::areAllConsumersSatisfied);
  }

  private static <E> Queue<ElementsSatisfyingConsumer<E>> removeElement(Queue<ElementsSatisfyingConsumer<E>> satisfiedElementsPerConsumer,
                                                                        E element) {
    // new Queue of ElementsSatisfyingConsumer without the given element, original ElementsSatisfyingConsumer are not modified.
    return satisfiedElementsPerConsumer.stream()
                                       .map(elementsSatisfyingConsumer -> elementsSatisfyingConsumer.withoutElement(element))
                                       .collect(toCollection(ArrayDeque::new));
  }

  public <ACTUAL_ELEMENT, OTHER_ELEMENT> void assertZipSatisfy(AssertionInfo info,
                                                               Iterable<? extends ACTUAL_ELEMENT> actual,
                                                               Iterable<OTHER_ELEMENT> other,
                                                               BiConsumer<? super ACTUAL_ELEMENT, OTHER_ELEMENT> zipRequirements) {
    assertNotNull(info, actual);
    requireNonNull(zipRequirements, "The BiConsumer expressing the assertions requirements must not be null");
    requireNonNull(other, "The iterable to zip actual with must not be null");
    assertHasSameSizeAs(info, actual, other);
    Iterator<OTHER_ELEMENT> otherIterator = other.iterator();

    List<ZipSatisfyError> errors = stream(actual).map(actualElement -> failsZipRequirements(actualElement, otherIterator.next(),
                                                                                            zipRequirements))
                                                 .filter(Optional::isPresent)
                                                 .map(Optional::get)
                                                 .collect(toList());
    if (!errors.isEmpty()) throw failures.failure(info, zippedElementsShouldSatisfy(info, actual, other, errors));
  }

  private <ACTUAL_ELEMENT, OTHER_ELEMENT> Optional<ZipSatisfyError> failsZipRequirements(ACTUAL_ELEMENT actualElement,
                                                                                         OTHER_ELEMENT otherElement,
                                                                                         BiConsumer<ACTUAL_ELEMENT, OTHER_ELEMENT> zipRequirements) {
    try {
      zipRequirements.accept(actualElement, otherElement);
      return Optional.empty();
    } catch (AssertionError ex) {
      return Optional.of(new ZipSatisfyError(actualElement, otherElement, ex.getMessage()));
    }
  }

  public <E> void assertAnySatisfy(AssertionInfo info, Iterable<? extends E> actual, Consumer<? super E> requirements) {
    assertNotNull(info, actual);
    requireNonNull(requirements, "The Consumer<T> expressing the assertions requirements must not be null");

    List<UnsatisfiedRequirement> unsatisfiedRequirements = new ArrayList<>();
    for (E element : actual) {
      Optional<UnsatisfiedRequirement> result = failsRequirements(requirements, element);
      if (!result.isPresent()) return; // element satisfied the requirements
      unsatisfiedRequirements.add(result.get());
    }

    throw failures.failure(info, elementsShouldSatisfyAny(actual, unsatisfiedRequirements, info));
  }

  public <E> void assertAllMatch(AssertionInfo info, Iterable<? extends E> actual, Predicate<? super E> predicate,
                                 PredicateDescription predicateDescription) {
    assertNotNull(info, actual);
    predicates.assertIsNotNull(predicate);
    List<? extends E> nonMatches = stream(actual).filter(predicate.negate()).collect(toList());

    if (!nonMatches.isEmpty()) {
      throw failures.failure(info, elementsShouldMatch(actual,
                                                       nonMatches.size() == 1 ? nonMatches.get(0) : nonMatches,
                                                       predicateDescription));
    }
  }

  public <E> void assertNoneSatisfy(AssertionInfo info, Iterable<? extends E> actual, Consumer<? super E> restrictions) {
    assertNotNull(info, actual);
    requireNonNull(restrictions, "The Consumer<T> expressing the restrictions must not be null");
    List<E> erroneousElements = stream(actual).map(element -> failsRestrictions(element, restrictions))
                                              .filter(Optional::isPresent)
                                              .map(Optional::get)
                                              .collect(toList());
    if (erroneousElements.size() > 0) throw failures.failure(info, noElementsShouldSatisfy(actual, erroneousElements));
  }

  private <E> Optional<E> failsRestrictions(E element, Consumer<? super E> restrictions) {
    try {
      restrictions.accept(element);
    } catch (AssertionError e) {
      // element is supposed not to meet the given restrictions
      return Optional.empty();
    }
    // element meets the given restrictions!
    return Optional.of(element);
  }

  public <E> void assertAnyMatch(AssertionInfo info, Iterable<? extends E> actual, Predicate<? super E> predicate,
                                 PredicateDescription predicateDescription) {
    assertNotNull(info, actual);
    predicates.assertIsNotNull(predicate);
    stream(actual).filter(predicate)
                  .findFirst()
                  .orElseThrow(() -> failures.failure(info, anyElementShouldMatch(actual, predicateDescription)));
  }

  public <E> void assertNoneMatch(AssertionInfo info, Iterable<? extends E> actual, Predicate<? super E> predicate,
                                  PredicateDescription predicateDescription) {
    assertNotNull(info, actual);
    predicates.assertIsNotNull(predicate);
    stream(actual).filter(predicate)
                  .findFirst()
                  .ifPresent(e -> {
                    throw failures.failure(info, noElementsShouldMatch(actual, e,
                                                                       predicateDescription));
                  });
  }

  /**
   * Asserts that the given {@code Iterable} contains at least one of the given {@code values}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param values the values that, at least one of which is expected to be in the given {@code Iterable}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty and given {@code Iterable} is not empty.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not contain any of given {@code values}.
   */
  public void assertContainsAnyOf(AssertionInfo info, Iterable<?> actual, Object[] values) {
    if (commonCheckThatIterableAssertionSucceeds(info, actual, values))
      return;

    Iterable<Object> valuesToSearchFor = newArrayList(values);
    for (Object element : actual) {
      if (iterableContains(valuesToSearchFor, element)) return;
    }
    throw failures.failure(info, shouldContainAnyOf(actual, values, comparisonStrategy));
  }

  public void assertContainsExactlyInAnyOrder(AssertionInfo info, Iterable<?> actual, Object[] values) {
    checkIsNotNull(values);
    assertNotNull(info, actual);
    List<Object> notExpected = newArrayList(actual);
    List<Object> notFound = newArrayList(values);

    for (Object value : values) {
      if (iterableContains(notExpected, value)) {
        iterablesRemoveFirst(notExpected, value);
        iterablesRemoveFirst(notFound, value);
      }
    }

    if (notExpected.isEmpty() && notFound.isEmpty()) return;

    throw failures.failure(info,
                           shouldContainExactlyInAnyOrder(actual, values, notFound, notExpected, comparisonStrategy));
  }

  void assertNotNull(AssertionInfo info, Iterable<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private AssertionError actualDoesNotEndWithSequence(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    return failures.failure(info, shouldEndWith(actual, sequence, comparisonStrategy));
  }

  private <E> List<E> notSatisfyingCondition(Iterable<? extends E> actual, Condition<? super E> condition) {
    return stream(actual).filter(o -> !condition.matches(o)).collect(toList());
  }

  private <E> List<E> satisfiesCondition(Iterable<? extends E> actual, Condition<? super E> condition) {
    return stream(actual).filter(o -> condition.matches(o)).collect(toList());
  }

  public static <T> Predicate<T> byPassingAssertions(Consumer<? super T> assertions) {
    return objectToTest -> {
      try {
        assertions.accept(objectToTest);
        return true;
      } catch (AssertionError e) {
        return false;
      }
    };
  }

  private static void checkIsNotEmptySequence(Object[] sequence) {
    if (sequence.length == 0) throw new IllegalArgumentException(emptySequence());
  }

  private static void checkIsNotEmptySubsequence(Object[] subsequence) {
    if (subsequence.length == 0) throw new IllegalArgumentException(emptySubsequence());
  }

}
