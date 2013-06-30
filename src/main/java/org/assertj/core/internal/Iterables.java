/*
 * Created on Sep 17, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.error.ConditionAndGroupGenericParameterTypeShouldBeTheSame.shouldBeSameGenericBetweenIterableAndCondition;
import static org.assertj.core.error.ElementsShouldBe.elementsShouldBe;
import static org.assertj.core.error.ElementsShouldBeAtLeast.elementsShouldBeAtLeast;
import static org.assertj.core.error.ElementsShouldBeExactly.elementsShouldBeExactly;
import static org.assertj.core.error.ElementsShouldHave.elementsShouldHave;
import static org.assertj.core.error.ElementsShouldHaveAtLeast.elementsShouldHaveAtLeast;
import static org.assertj.core.error.ElementsShouldHaveAtMost.elementsShouldHaveAtMost;
import static org.assertj.core.error.ElementsShouldHaveExactly.elementsShouldHaveExactly;
import static org.assertj.core.error.ElementsShouldNotBe.elementsShouldNotBe;
import static org.assertj.core.error.ElementsShouldNotBeAtLeast.elementsShouldNotBeAtLeast;
import static org.assertj.core.error.ElementsShouldNotBeAtMost.elementsShouldNotBeAtMost;
import static org.assertj.core.error.ElementsShouldNotBeExactly.elementsShouldNotBeExactly;
import static org.assertj.core.error.ElementsShouldNotHave.elementsShouldNotHave;
import static org.assertj.core.error.ElementsShouldNotHaveAtLeast.elementsShouldNotHaveAtLeast;
import static org.assertj.core.error.ElementsShouldNotHaveAtMost.elementsShouldNotHaveAtMost;
import static org.assertj.core.error.ElementsShouldNotHaveExactly.elementsShouldNotHaveExactly;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldContainNull.shouldContainNull;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.error.ShouldContainSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainNull.shouldNotContainNull;
import static org.assertj.core.error.ShouldNotHaveDuplicates.shouldNotHaveDuplicates;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.CommonValidations.checkIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkIsNotNullAndNotEmpty;
import static org.assertj.core.internal.CommonValidations.failIfEmptySinceActualIsNotEmpty;
import static org.assertj.core.util.Iterables.isNullOrEmpty;
import static org.assertj.core.util.Iterables.sizeOf;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Iterable}</code>s.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Maciej Jaskowski
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Iterables {

  private static final Iterables INSTANCE = new Iterables();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * 
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Iterables instance() {
    return INSTANCE;
  }

  private final ComparisonStrategy comparisonStrategy;

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Conditions conditions = Conditions.instance();

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

  /**
   * Asserts that the given <code>{@link Iterable}</code> is {@code null} or empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @throws AssertionError if the given {@code Iterable} is not {@code null} *and* contains one or more elements.
   */
  public void assertNullOrEmpty(AssertionInfo info, Iterable<?> actual) {
    if (actual == null || isNullOrEmpty(actual)) {
      return;
    }
    throw failures.failure(info, shouldBeNullOrEmpty(actual));
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
    if (isNullOrEmpty(actual)) {
      return;
    }
    throw failures.failure(info, shouldBeEmpty(actual));
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
    if (!isNullOrEmpty(actual)) {
      return;
    }
    throw failures.failure(info, shouldNotBeEmpty());
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
    int sizeOfActual = sizeOf(actual);
    if (sizeOfActual == expectedSize) {
      return;
    }
    throw failures.failure(info, shouldHaveSize(actual, sizeOfActual, expectedSize));
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
  public void assertHasSameSizeAs(AssertionInfo info, Iterable<?> actual, Object[] other) {
    assertNotNull(info, actual);
    checkIsNotNull(other);
    int sizeOfActual = sizeOf(actual);
    int sizeOfOther = other.length;
    if (sizeOfActual == sizeOfOther) {
      return;
    }
    throw failures.failure(info, shouldHaveSameSizeAs(actual, sizeOfActual, sizeOfOther));
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
    checkNotNull(info, other);
    int sizeOfActual = sizeOf(actual);
    int sizeOfOther = sizeOf(other);
    if (sizeOfActual == sizeOfOther) {
      return;
    }
    throw failures.failure(info, shouldHaveSameSizeAs(actual, sizeOfActual, sizeOfOther));
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
    checkIsNotNull(values);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (!actual.iterator().hasNext() && values.length == 0)
      return;
    failIfEmptySinceActualIsNotEmpty(values);
    // check for elements in values that are missing in actual.
    Set<Object> notFound = new LinkedHashSet<Object>();
    for (Object value : values) {
      if (!iterableContains(actual, value)) {
        notFound.add(value);
      }
    }
    if (notFound.isEmpty())
      return;
    throw failures.failure(info, shouldContain(actual, values, notFound, comparisonStrategy));
  }

  /**
   * Delegates to {@link ComparisonStrategy#iterableContains(Iterable, Object)}
   */
  private boolean iterableContains(Iterable<?> actual, Object value) {
    return comparisonStrategy.iterableContains(actual, value);
  }

  /**
   * Delegates to {@link ComparisonStrategy#iterableRemoves(Iterable, Object)}
   */
  private void iterableRemoves(Iterable<?> actual, Object value) {
    comparisonStrategy.iterableRemoves(actual, value);
  }

  /**
   * Asserts that the given {@code Iterable} contains only the given values and nothing else, in any order.
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
  public void assertContainsOnly(AssertionInfo info, Iterable<?> actual, Object[] values) {
    checkIsNotNull(values);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (!actual.iterator().hasNext() && values.length == 0)
      return;
    failIfEmptySinceActualIsNotEmpty(values);
    // check for elements in values that are missing in actual.
    Set<Object> notExpected = setFromIterable(actual);
    Set<Object> notFound = containsOnly(notExpected, values);
    if (notExpected.isEmpty() && notFound.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContainOnly(actual, values, notFound, notExpected, comparisonStrategy));
  }

  private Set<Object> containsOnly(Set<Object> actual, Object[] values) {
    Set<Object> notFound = new LinkedHashSet<Object>();
    for (Object o : set(values)) {
      if (iterableContains(actual, o)) {
        iterableRemoves(actual, o);
      } else {
        notFound.add(o);
      }
    }
    return notFound;
  }

  /**
   * build a Set with that avoid duplicates <b>according to given comparison strategy</b>
   * 
   * @param elements to feed the Set we want to build
   * @return a Set without duplicates <b>according to given comparison strategy</b>
   */
  private Set<Object> set(Object... elements) {
    if (elements == null) {
      return null;
    }
    Set<Object> set = new HashSet<Object>();
    for (Object e : elements) {
      // only add is not already there
      if (!iterableContains(set, e)) {
        set.add(e);
      }
    }
    return set;
  }

  /**
   * build a Set with that avoid duplicates <b>according to given comparison strategy</b>
   * 
   * @param iterable to feed the Set we want to build
   * @return a Set without duplicates <b>according to given comparison strategy</b>
   */
  private Set<Object> setFromIterable(Iterable<?> iterable) {
    if (iterable == null) {
      return null;
    }
    Set<Object> set = new HashSet<Object>();
    for (Object e : iterable) {
      // only add is not already there
      if (!iterableContains(set, e)) {
        set.add(e);
      }
    }
    return set;
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
    checkIsNotNull(values);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (!actual.iterator().hasNext() && values.length == 0)
      return;
    failIfEmptySinceActualIsNotEmpty(values);
    // check for elements in values that are missing in actual.
    Set<?> expected = asTreeSetWithoutDuplicatesAccordingToComparisonStrategy(Arrays.asList(values));
    Set<?> actualList = asTreeSetWithoutDuplicatesAccordingToComparisonStrategy(actual);
    Iterable<?> duplicates = comparisonStrategy.duplicatesFrom(actual);
    Set<Object> notFound = new LinkedHashSet<Object>();
    Set<Object> notOnlyOnce = new LinkedHashSet<Object>();
    for (Object element : expected) {
      if (!actualList.contains(element)) {
        notFound.add(element);
      } else if (iterableContains(duplicates, element)) {
        notOnlyOnce.add(element);
      }
    }
    if (notFound.isEmpty() && notOnlyOnce.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContainsOnlyOnce(actual, values, notFound, notOnlyOnce, comparisonStrategy));
  }

  /**
   * build a TreeSet with that avoid duplicates <b>according to given comparison strategy</b>
   * 
   * @param iterable to feed the Set we want to build
   * @return a Set without duplicates <b>according to given comparison strategy</b> and with {@code .contains} who use
   *         the given comparison strategy.
   */
  private <T> Set<T> asTreeSetWithoutDuplicatesAccordingToComparisonStrategy(Iterable<T> iterable) {
    Set<T> set = new TreeSet<T>(getComparatorFromComparisonStrategy());
    for (T element : iterable) {
      set.add(element);
    }
    return set;
  }

  private Comparator<Object> getComparatorFromComparisonStrategy() {
    @SuppressWarnings("unchecked")
    Comparator<Object> comparator = (Comparator<Object>) getComparator();
    if (comparator == null) {
      comparator = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
          if (comparisonStrategy.areEqual(o1, o2))
            return 0;
          if (o1 == null) {
            return -1;
          }
          if (o2 == null) {
            return 1;
          }
          if (comparisonStrategy.isGreaterThan(o1, o2))
            return 1;
          return -1;
        }
      };
    }
    return comparator;
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
    checkIsNotNull(sequence);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (!actual.iterator().hasNext() && sequence.length == 0)
      return;
    failIfEmptySinceActualIsNotEmpty(sequence);
    // check for elements in values that are missing in actual.
    List<?> actualAsList = newArrayList(actual);
    for (int i = 0; i < actualAsList.size(); i++) {
      // look for given sequence in actual starting from current index (i)
      if (containsSequenceAtGivenIndex(actualAsList, sequence, i)) {
        return;
      }
    }
    throw actualDoesNotContainSequence(info, actual, sequence);
  }

  /**
   * Verifies that the actual <code>Iterable</code> is a subset of values <code>Iterable</code>. <br/>
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
    checkNotNull(info, values);
    List<Object> extra = newArrayList();
    for (Object actualElement : actual) {
      if (!iterableContains(values, actualElement)) {
        extra.add(actualElement);
      }
    }
    if (extra.size() > 0) {
      throw actualIsNotSubsetOfSet(info, actual, values, extra);
    }
  }

  private static void checkNotNull(AssertionInfo info, Iterable<?> set) {
    if (set == null) {
      throw iterableToLookForIsNull();
    }
  }

  private AssertionError actualIsNotSubsetOfSet(AssertionInfo info, Object actual, Iterable<?> set, Iterable<?> extra) {
    return failures.failure(info, shouldBeSubsetOf(actual, set, extra, comparisonStrategy));
  }

  /**
   * Return true if actualAsList contains exactly the given sequence at given starting index, false otherwise.
   * 
   * @param actualAsList the list to look sequance in
   * @param sequence the sequence to look for
   * @param startingIndex the index of actual list at which we start looking for sequence.
   * @return
   */
  private boolean containsSequenceAtGivenIndex(List<?> actualAsList, Object[] sequence, int startingIndex) {
    // check that, starting from given index, actualAsList has enough remaining elements to contain sequence
    if (actualAsList.size() - startingIndex < sequence.length) {
      return false;
    }
    for (int i = 0; i < sequence.length; i++) {
      if (!areEqual(actualAsList.get(startingIndex + i), sequence[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Delegates to {@link ComparisonStrategy#areEqual(Object, Object)}
   */
  private boolean areEqual(Object actual, Object other) {
    return comparisonStrategy.areEqual(actual, other);
  }

  private AssertionError actualDoesNotContainSequence(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    return failures.failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
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
    Set<Object> found = new LinkedHashSet<Object>();
    for (Object o : values) {
      if (iterableContains(actual, o)) {
        found.add(o);
      }
    }
    if (found.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldNotContain(actual, values, found, comparisonStrategy));
  }

  /**
   * Asserts that the given {@code Iterable} does not contain the given values.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param iterable the values that are expected not to be in the given {@code Iterable}.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} contains any of given values.
   */
  public <T> void assertDoesNotContainAnyElementsOf(AssertionInfo info, Iterable<T> actual,
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
    if (isNullOrEmpty(duplicates)) {
      return;
    }
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
    checkIsNotNull(sequence);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (!actual.iterator().hasNext() && sequence.length == 0)
      return;
    failIfEmptySinceActualIsNotEmpty(sequence);
    int sequenceSize = sequence.length;
    int i = 0;
    for (Object actualCurrentElement : actual) {
      if (i >= sequenceSize) {
        break;
      }
      if (areEqual(actualCurrentElement, sequence[i++])) {
        continue;
      }
      throw actualDoesNotStartWithSequence(info, actual, sequence);
    }
    if (sequenceSize > i) {
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
   * @param sequence the sequence of objects to look for.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not end with the given sequence of objects.
   */
  public void assertEndsWith(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    checkIsNotNull(sequence);
    assertNotNull(info, actual);
    // if both actual and values are empty, then assertion passes.
    if (!actual.iterator().hasNext() && sequence.length == 0)
      return;
    failIfEmptySinceActualIsNotEmpty(sequence);
    int sequenceSize = sequence.length;
    int sizeOfActual = sizeOf(actual);
    if (sizeOfActual < sequenceSize) {
      throw actualDoesNotEndWithSequence(info, actual, sequence);
    }
    int start = sizeOfActual - sequenceSize;
    int sequenceIndex = 0, indexOfActual = 0;
    for (Object o : actual) {
      if (indexOfActual++ < start) {
        continue;
      }
      if (areEqual(o, sequence[sequenceIndex++])) {
        continue;
      }
      throw actualDoesNotEndWithSequence(info, actual, sequence);
    }
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
    if (!iterableContains(actual, null)) {
      throw failures.failure(info, shouldContainNull(actual));
    }
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
    if (iterableContains(actual, null)) {
      throw failures.failure(info, shouldNotContainNull(actual));
    }
  }

  /**
   * Assert that each element of given {@code Iterable} satisfies the given condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  public <E> void assertAre(AssertionInfo info, Iterable<? extends E> actual, Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(actual, condition);
      if (notSatisfiesCondition.isEmpty()) {
        return;
      }
      throw failures.failure(info, elementsShouldBe(actual, notSatisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that each element of given {@code Iterable} not satisfies the given condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  public <E> void assertAreNot(AssertionInfo info, Iterable<? extends E> actual, Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (satisfiesCondition.isEmpty()) {
        return;
      }
      throw failures.failure(info, elementsShouldNotBe(actual, satisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that each element of given {@code Iterable} satisfies the given condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  public <E> void assertHave(AssertionInfo info, Iterable<? extends E> actual, Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(actual, condition);
      if (notSatisfiesCondition.isEmpty()) {
        return;
      }
      throw failures.failure(info, elementsShouldHave(actual, notSatisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that each element of given {@code Iterable} not satisfies the given condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  public <E> void assertDoNotHave(AssertionInfo info, Iterable<? extends E> actual, Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (satisfiesCondition.isEmpty()) {
        return;
      }
      throw failures.failure(info, elementsShouldNotHave(actual, satisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that there is <b>at least</b> <i>n</i> elements in the actual {@code Iterable} satisfying the given
   * condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param n the minimum number of times the condition should be verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &lt; n.
   */
  public <E> void assertAreAtLeast(AssertionInfo info, Iterable<? extends E> actual, int n,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (satisfiesCondition.size() >= n) {
        return;
      }
      throw failures.failure(info, elementsShouldBeAtLeast(actual, n, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that there is <b>at least</b> <i>n</i> elements in the actual {@code Iterable} <b>not</b> satisfying the
   * given condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param n the number of times the condition should not be verified at least.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements not satisfying the given condition is &lt; n.
   */
  public <E> void assertAreNotAtLeast(AssertionInfo info, Iterable<? extends E> actual, int n,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(actual, condition);
      if (notSatisfiesCondition.size() >= n) {
        return;
      }
      throw failures.failure(info, elementsShouldNotBeAtLeast(actual, n, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Assert that there is <b>at most</b> <i>n</i> elements in the actual {@code Iterable} satisfying the given
   * condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param n the number of times the condition should be at most verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  public <E> void assertAreAtMost(AssertionInfo info, Iterable<? extends E> actual, int n,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (satisfiesCondition.size() <= n) {
        return;
      }
      throw failures.failure(info, elementsShouldNotBeAtMost(actual, n, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Verifies that there is <b>at most</b> <i>n</i> elements in the actual {@code Iterable} <b>not</b> satisfying the
   * given condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param n the number of times the condition should not be verified at most.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements not satisfying the given condition is &gt; n.
   */
  public <E> void assertAreNotAtMost(AssertionInfo info, Iterable<? extends E> actual, int n,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(actual, condition);
      if (notSatisfiesCondition.size() <= n) {
        return;
      }
      throw failures.failure(info, elementsShouldNotBeAtMost(actual, n, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Verifies that there is <b>exactly</b> <i>n</i> elements in the actual {@code Iterable} satisfying the given
   * condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param n the exact number of times the condition should be verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  public <E> void assertAreExactly(AssertionInfo info, Iterable<? extends E> actual, int n,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (satisfiesCondition.size() == n) {
        return;
      }
      throw failures.failure(info, elementsShouldBeExactly(actual, n, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * Verifies that there is <b>exactly</b> <i>n</i> elements in the actual {@code Iterable} <b>not</b> satisfying the
   * given condition.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Iterable}.
   * @param n the exact number of times the condition should not be verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements not satisfying the given condition is &ne; n.
   */
  public <E> void assertAreNotExactly(AssertionInfo info, Iterable<? extends E> actual, int n,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(actual, condition);
      if (notSatisfiesCondition.size() == n) {
        return;
      }
      throw failures.failure(info, elementsShouldNotBeExactly(actual, n, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * An alias method of {@link #assertAreAtLeast(AssertionInfo, Iterable, int, Condition)} to provide a richer fluent
   * api (same logic, only error message differs).
   */
  public <E> void assertHaveAtLeast(AssertionInfo info, Iterable<? extends E> actual, int times,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (satisfiesCondition.size() >= times) {
        return;
      }
      throw failures.failure(info, elementsShouldHaveAtLeast(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * An alias method of {@link #assertAreNotAtLeast(AssertionInfo, Iterable, int, Condition)} to provide a richer fluent
   * api (same logic, only error message differs).
   */
  public <E> void assertDoNotHaveAtLeast(AssertionInfo info, Iterable<? extends E> actual, int times,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(actual, condition);
      if (notSatisfiesCondition.size() >= times) {
        return;
      }
      throw failures.failure(info, elementsShouldNotHaveAtLeast(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * An alias method of {@link #assertAreAtMost(AssertionInfo, Iterable, int, Condition)} to provide a richer fluent api
   * (same logic, only error message differs).
   */
  public <E> void assertHaveAtMost(AssertionInfo info, Iterable<? extends E> actual, int times,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (satisfiesCondition.size() <= times) {
        return;
      }
      throw failures.failure(info, elementsShouldHaveAtMost(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * An alias method of {@link #assertAreNotAtMost(AssertionInfo, Iterable, int, Condition)} to provide a richer fluent
   * api (same logic, only error message differs).
   */
  public <E> void assertDoNotHaveAtMost(AssertionInfo info, Iterable<? extends E> actual, int times,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(actual, condition);
      if (notSatisfiesCondition.size() <= times) {
        return;
      }
      throw failures.failure(info, elementsShouldNotHaveAtMost(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * An alias method of {@link #assertAreExactly(AssertionInfo, Iterable, int, Condition)} to provide a richer fluent
   * api (same logic, only error message differs).
   */
  public <E> void assertHaveExactly(AssertionInfo info, Iterable<? extends E> actual, int times,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(actual, condition);
      if (satisfiesCondition.size() == times) {
        return;
      }
      throw failures.failure(info, elementsShouldHaveExactly(actual, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(actual, condition));
    }
  }

  /**
   * An alias method of {@link #assertAreNotExactly(AssertionInfo, Iterable, int, Condition)} to provide a richer fluent
   * api (same logic, only error message differs).
   */
  public <E> void assertDoNotHaveExactly(AssertionInfo info, Iterable<? extends E> actual, int times,
      Condition<? super E> condition) {
    assertNotNull(info, actual);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(actual, condition);
      if (notSatisfiesCondition.size() == times) {
        return;
      }
      throw failures.failure(info, elementsShouldNotHaveExactly(actual, times, condition));
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
    if (other == null) {
      throw iterableToLookForIsNull();
    }
    assertNotNull(info, actual);
    Object[] values = newArrayList(other).toArray();
    Set<Object> notFound = new LinkedHashSet<Object>();
    for (Object value : values) {
      if (!iterableContains(actual, value)) {
        notFound.add(value);
      }
    }
    if (notFound.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContain(actual, values, notFound, comparisonStrategy));
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
    assertHasSameSizeAs(info, actual, values); // include check that actual is not null
    Set<Object> notExpected = setFromIterable(actual);
    Set<Object> notFound = containsOnly(notExpected, values);
    if (notExpected.isEmpty() && notFound.isEmpty()) {
      // actual and values have the same elements but are they in the same order.
      int i = 0;
      for (Object elementFromActual : actual) {
        if (!areEqual(elementFromActual, values[i])) {
          throw failures.failure(info, shouldContainExactly(elementFromActual, values[i], i, comparisonStrategy));
        }
        i++;
      }
      return;
    }
    throw failures.failure(info, shouldContainExactly(actual, values, notFound, notExpected, comparisonStrategy));
  }

  private void assertNotNull(AssertionInfo info, Iterable<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private AssertionError actualDoesNotEndWithSequence(AssertionInfo info, Iterable<?> actual, Object[] sequence) {
    return failures.failure(info, shouldEndWith(actual, sequence, comparisonStrategy));
  }

  private <E> List<E> notSatisfiesCondition(Iterable<? extends E> actual, Condition<? super E> condition) {
    List<E> notSatisfiesCondition = new LinkedList<E>();
    for (E o : actual) {
      if (!condition.matches(o)) {
        notSatisfiesCondition.add(o);
      }
    }
    return notSatisfiesCondition;
  }

  private <E> List<E> satisfiesCondition(Iterable<? extends E> actual, Condition<? super E> condition) {
    List<E> satisfiesCondition = new LinkedList<E>();
    for (E o : actual) {
      if (condition.matches(o)) {
        satisfiesCondition.add(o);
      }
    }
    return satisfiesCondition;
  }

  static public NullPointerException iterableToLookForIsNull() {
    return new NullPointerException("The iterable to look for should not be null");
  }

  static public IllegalArgumentException iterableToLookForIsEmpty() {
    return new IllegalArgumentException("The iterable to look for should not be empty");
  }

}
