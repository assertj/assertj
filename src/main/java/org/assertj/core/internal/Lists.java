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
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldBeAtIndex.shouldBeAtIndex;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSorted;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.error.ShouldBeSorted.shouldHaveComparableElementsAccordingToGivenComparator;
import static org.assertj.core.error.ShouldBeSorted.shouldHaveMutuallyComparableElements;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.error.ShouldHaveAtIndex.shouldHaveAtIndex;
import static org.assertj.core.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.assertj.core.internal.CommonValidations.checkIndexValueIsValid;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.assertj.core.util.VisibleForTesting;


/**
 * Reusable assertions for <code>{@link List}</code>s.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
// TODO inherits from Collections to avoid repeating comparisonStrategy ?
public class Lists {

  private static final Lists INSTANCE = new Lists();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Lists instance() {
    return INSTANCE;
  }

  private final ComparisonStrategy comparisonStrategy;

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Lists() {
    this(StandardComparisonStrategy.instance());
  }

  public Lists(ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy) { return ((ComparatorBasedComparisonStrategy) comparisonStrategy)
        .getComparator(); }
    return null;
  }

  /**
   * Verifies that the given {@code List} contains the given object at the given index.
   * @param info contains information about the assertion.
   * @param actual the given {@code List}.
   * @param value the object to look for.
   * @param index the index where the object should be stored in the given {@code List}.
   * @throws AssertionError if the given {@code List} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of the given
   *           {@code List}.
   * @throws AssertionError if the given {@code List} does not contain the given object at the given index.
   */
  public void assertContains(AssertionInfo info, List<?> actual, Object value, Index index) {
    assertNotNull(info, actual);
    Iterables.instance().assertNotEmpty(info, actual);
    checkIndexValueIsValid(index, actual.size() - 1);
    Object actualElement = actual.get(index.value);
    if (areEqual(actualElement, value)) return;
    throw failures.failure(info, shouldContainAtIndex(actual, value, index, actual.get(index.value), comparisonStrategy));
  }

  /**
   * Verifies that the given {@code List} does not contain the given object at the given index.
   * @param info contains information about the assertion.
   * @param actual the given {@code List}.
   * @param value the object to look for.
   * @param index the index where the object should be stored in the given {@code List}.
   * @throws AssertionError if the given {@code List} is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the given {@code List} contains the given object at the given index.
   */
  public void assertDoesNotContain(AssertionInfo info, List<?> actual, Object value, Index index) {
    assertNotNull(info, actual);
    checkIndexValueIsValid(index, Integer.MAX_VALUE);
    int indexValue = index.value;
    if (indexValue >= actual.size()) return;
    Object actualElement = actual.get(index.value);
    if (!areEqual(actualElement, value)) return;
    throw failures.failure(info, shouldNotContainAtIndex(actual, value, index, comparisonStrategy));
  }

  /**
   * Verifies that the actual list is sorted in ascending order according to the natural ordering of its elements.
   * <p>
   * All list elements must implement the {@link Comparable} interface and must be mutually comparable (that is, e1.compareTo(e2)
   * must not throw a ClassCastException for any elements e1 and e2 in the list), examples :
   * <ul>
   * <li>a list composed of {"a1", "a2", "a3"} is ok because the element type (String) is Comparable</li>
   * <li>a list composed of Rectangle {r1, r2, r3} is <b>NOT ok</b> because Rectangle is not Comparable</li>
   * <li>a list composed of {True, "abc", False} is <b>NOT ok</b> because elements are not mutually comparable</li>
   * </ul>
   * Empty lists are considered sorted.<br> Unique element lists are considered sorted unless the element type is not Comparable.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code List}.
   * 
   * @throws AssertionError if the actual list is not sorted in ascending order according to the natural ordering of its
   *           elements.
   * @throws AssertionError if the actual list is <code>null</code>.
   * @throws AssertionError if the actual list element type does not implement {@link Comparable}.
   * @throws AssertionError if the actual list elements are not mutually {@link Comparable}.
   */
  public void assertIsSorted(AssertionInfo info, List<?> actual) {
    assertNotNull(info, actual);
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy) {
      // instead of comparing elements with their natural comparator, use the one set by client.
      Comparator<?> comparator = ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator();
      assertIsSortedAccordingToComparator(info, actual, comparator);
      return;
    }
    try {
      // sorted assertion is only relevant if elements are Comparable, we assume they are
      List<Comparable<Object>> comparableList = listOfComparableElements(actual);
      // array with 0 or 1 element are considered sorted.
      if (comparableList.size() <= 1) return;
      for (int i = 0; i < comparableList.size() - 1; i++) {
        // array is sorted in ascending order iif element i is less or equal than element i+1
        if (comparableList.get(i).compareTo(comparableList.get(i + 1)) > 0)
          throw failures.failure(info, shouldBeSorted(i, actual));
      }
    } catch (ClassCastException e) {
      // elements are either not Comparable or not mutually Comparable (e.g. List<Object> containing String and Integer)
      throw failures.failure(info, shouldHaveMutuallyComparableElements(actual));
    }
  }

  /**
   * Verifies that the actual list is sorted according to the given comparator.<br> Empty lists are considered sorted whatever
   * the comparator is.<br> One element lists are considered sorted if the element is compatible with comparator.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code List}.
   * @param comparator the {@link Comparator} used to compare list elements
   * 
   * @throws AssertionError if the actual list is not sorted according to the given comparator.
   * @throws AssertionError if the actual list is <code>null</code>.
   * @throws NullPointerException if the given comparator is <code>null</code>.
   * @throws AssertionError if the actual list elements are not mutually comparable according to given Comparator.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void assertIsSortedAccordingToComparator(AssertionInfo info, List<?> actual, Comparator<?> comparator) {
    assertNotNull(info, actual);
    checkNotNull(comparator, "The given comparator should not be null");
    try {
      // Empty collections are considered sorted even if comparator can't be applied to their element type
      // We can't verify that point because of erasure type at runtime.
      if (actual.size() == 0) return;
      Comparator rawComparator = comparator;
      if (actual.size() == 1) {
        // Compare unique element with itself to verify that it is compatible with comparator (a ClassCastException is
        // thrown if not). We have to use a raw comparator to compare the unique element of actual ... :(
        rawComparator.compare(actual.get(0), actual.get(0));
        return;
      }
      for (int i = 0; i < actual.size() - 1; i++) {
        // List is sorted in comparator defined order if current element is less or equal than next element
        if (rawComparator.compare(actual.get(i), actual.get(i + 1)) > 0)
          throw failures.failure(info, shouldBeSortedAccordingToGivenComparator(i, actual, comparator));
      }
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldHaveComparableElementsAccordingToGivenComparator(actual, comparator));
    }
  }

  /**
   * Verifies that the given {@code List} satisfies the given <code>{@link Condition}</code> at the given index.
   * @param <T> the type of the actual value and the type of values that given {@code Condition} takes.
   * @param info contains information about the assertion.
   * @param actual the given {@code List}.
   * @param condition the given {@code Condition}.
   * @param index the index where the object should be stored in the given {@code List}.
   * @throws AssertionError if the given {@code List} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of the given
   *           {@code List}.
   * @throws NullPointerException if the given {@code Condition} is {@code null}.
   * @throws AssertionError if the value in the given {@code List} at the given index does not satisfy the given {@code Condition}
   *           .
   */
  public <T> void assertHas(AssertionInfo info, List<? extends T> actual, Condition<? super T> condition, Index index) {
    if (conditionIsMetAtIndex(info, actual, condition, index)) return;
    throw failures.failure(info, shouldHaveAtIndex(actual, condition, index, actual.get(index.value)));
  }

  /**
   * Verifies that the given {@code List} satisfies the given <code>{@link Condition}</code> at the given index.
   * @param <T> the type of the actual value and the type of values that given {@code Condition} takes.
   * @param info contains information about the assertion.
   * @param actual the given {@code List}.
   * @param condition the given {@code Condition}.
   * @param index the index where the object should be stored in the given {@code List}.
   * @throws AssertionError if the given {@code List} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of the given
   *           {@code List}.
   * @throws NullPointerException if the given {@code Condition} is {@code null}.
   * @throws AssertionError if the value in the given {@code List} at the given index does not satisfy the given {@code Condition}
   *           .
   */
  public <T> void assertIs(AssertionInfo info, List<? extends T> actual, Condition<? super T> condition, Index index) {
    if (conditionIsMetAtIndex(info, actual, condition, index)) return;
    throw failures.failure(info, shouldBeAtIndex(actual, condition, index, actual.get(index.value)));
  }

  private <T> boolean conditionIsMetAtIndex(AssertionInfo info, List<T> actual, Condition<? super T> condition, Index index) {
    assertNotNull(info, actual);
    assertNotNull(condition);
    Iterables.instance().assertNotEmpty(info, actual);
    checkIndexValueIsValid(index, actual.size() - 1);
    return condition.matches(actual.get(index.value));
  }

  @SuppressWarnings("unchecked")
  private static List<Comparable<Object>> listOfComparableElements(List<?> collection) {
    List<Comparable<Object>> listOfComparableElements = new ArrayList<>();
    for (Object object : collection) {
      listOfComparableElements.add((Comparable<Object>) object);
    }
    return listOfComparableElements;
  }

  private void assertNotNull(AssertionInfo info, List<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private void assertNotNull(Condition<?> condition) {
    Conditions.instance().assertIsNotNull(condition);
  }

  /**
   * Delegates to {@link ComparisonStrategy#areEqual(Object, Object)}
   */
  private boolean areEqual(Object actual, Object other) {
    return comparisonStrategy.areEqual(actual, other);
  }

    @VisibleForTesting
  public ComparisonStrategy getComparisonStrategy() {
    return comparisonStrategy;
  }
}
