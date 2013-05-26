/*
 * Created on Nov 28, 2010
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

import static java.lang.reflect.Array.getLength;
import static org.assertj.core.error.ConditionAndGroupGenericParameterTypeShouldBeTheSame.shouldBeSameGenericBetweenIterableAndCondition;
import static org.assertj.core.error.ElementsShouldBe.elementsShouldBe;
import static org.assertj.core.error.ElementsShouldBeAtLeast.elementsShouldBeAtLeast;
import static org.assertj.core.error.ElementsShouldBeAtMost.elementsShouldBeAtMost;
import static org.assertj.core.error.ElementsShouldBeExactly.elementsShouldBeExactly;
import static org.assertj.core.error.ElementsShouldHave.elementsShouldHave;
import static org.assertj.core.error.ElementsShouldHaveAtLeast.elementsShouldHaveAtLeast;
import static org.assertj.core.error.ElementsShouldHaveAtMost.elementsShouldHaveAtMost;
import static org.assertj.core.error.ElementsShouldHaveExactly.elementsShouldHaveExactly;
import static org.assertj.core.error.ElementsShouldNotBe.elementsShouldNotBe;
import static org.assertj.core.error.ElementsShouldNotBeAtLeast.elementsShouldNotBeAtLeast;
import static org.assertj.core.error.ElementsShouldNotBeAtMost.elementsShouldNotBeAtMost;
import static org.assertj.core.error.ElementsShouldNotHave.elementsShouldNotHave;
import static org.assertj.core.error.ElementsShouldNotHaveAtLeast.elementsShouldNotHaveAtLeast;
import static org.assertj.core.error.ElementsShouldNotHaveAtMost.elementsShouldNotHaveAtMost;
import static org.assertj.core.error.ElementsShouldNotHaveExactly.elementsShouldNotHaveExactly;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSorted;
import static org.assertj.core.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.assertj.core.error.ShouldBeSorted.shouldHaveComparableElementsAccordingToGivenComparator;
import static org.assertj.core.error.ShouldBeSorted.shouldHaveMutuallyComparableElements;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.error.ShouldContainNull.shouldContainNull;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.error.ShouldContainSequence.shouldContainSequence;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.assertj.core.error.ShouldNotContainNull.shouldNotContainNull;
import static org.assertj.core.error.ShouldNotHaveDuplicates.shouldNotHaveDuplicates;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsNull;
import static org.assertj.core.internal.CommonErrors.iterableToLookForIsNull;
import static org.assertj.core.internal.CommonValidations.checkIndexValueIsValid;
import static org.assertj.core.util.ArrayWrapperList.wrap;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.Iterables.isNullOrEmpty;
import static org.assertj.core.util.Lists.newArrayList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Index;
import org.assertj.core.error.ElementsShouldNotBeExactly;
import org.assertj.core.util.ArrayWrapperList;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for object and primitive arrays. It trades off performance for DRY.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 */
class Arrays {

  private static final Arrays INSTANCE = new Arrays();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   * 
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  static Arrays instance() {
    return INSTANCE;
  }

  private final ComparisonStrategy comparisonStrategy;

  private Arrays() {
    this(StandardComparisonStrategy.instance());
  }

  public Arrays(ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy) {
      return ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator();
    }
    return null;
  }

  void assertNullOrEmpty(AssertionInfo info, Failures failures, Object array) {
    if (array == null || isArrayEmpty(array)) {
      return;
    }
    throw failures.failure(info, shouldBeNullOrEmpty(array));
  }

  void assertEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (isArrayEmpty(array)) {
      return;
    }
    throw failures.failure(info, shouldBeEmpty(array));
  }

  void assertHasSize(AssertionInfo info, Failures failures, Object array, int expectedSize) {
    assertNotNull(info, array);
    int sizeOfActual = sizeOf(array);
    if (sizeOfActual == expectedSize) {
      return;
    }
    throw failures.failure(info, shouldHaveSize(array, sizeOfActual, expectedSize));
  }

  void assertHasSameSizeAs(AssertionInfo info, Failures failures, Object array, Iterable<?> other) {
    assertNotNull(info, array);
    if (other == null) {
      throw new NullPointerException("The iterable to look for should not be null");
    }
    int sizeOfActual = sizeOf(array);
    int sizeOfOther = org.assertj.core.util.Iterables.sizeOf(other);
    if (sizeOfActual == sizeOfOther) {
      return;
    }
    throw failures.failure(info, shouldHaveSameSizeAs(array, sizeOfActual, sizeOfOther));
  }

  void assertHasSameSizeAs(AssertionInfo info, Failures failures, Object array, Object other) {
    assertNotNull(info, array);
    checkIsNotNull(other);
    int sizeOfActual = sizeOf(array);
    int sizeOfOther = sizeOf(other);
    if (sizeOfActual == sizeOfOther) {
      return;
    }
    throw failures.failure(info, shouldHaveSameSizeAs(array, sizeOfActual, sizeOfOther));
  }

  void assertContains(AssertionInfo info, Failures failures, Object actual, Object values) {
    checkIsNotNull(values);
    assertNotNull(info, actual);
    // if both actual and values are empty arrays, then assertion passes.
    if (isArrayEmpty(actual) && isArrayEmpty(values))
      return;
    failIfEmptySinceActualIsNotEmpty(values);
    Set<Object> notFound = new LinkedHashSet<Object>();
    int valueCount = sizeOf(values);
    for (int i = 0; i < valueCount; i++) {
      Object value = Array.get(values, i);
      if (!arrayContains(actual, value)) {
        notFound.add(value);
      }
    }
    if (notFound.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContain(actual, values, notFound, comparisonStrategy));
  }

  void assertcontainsAll(AssertionInfo info, Failures failures, Object array, Iterable<?> iterable) {
    if (iterable == null) {
      throw iterableToLookForIsNull();
    }
    assertNotNull(info, array);
    Object[] values = newArrayList(iterable).toArray();
    Set<Object> notFound = new LinkedHashSet<Object>();
    for (Object value : values) {
      if (!arrayContains(array, value)) {
        notFound.add(value);
      }
    }
    if (notFound.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContain(array, values, notFound, comparisonStrategy));
  }

  void assertContains(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    assertNotEmpty(info, failures, array);
    checkIndexValueIsValid(index, sizeOf(array) - 1);
    Object actualElement = Array.get(array, index.value);
    if (areEqual(actualElement, value)) {
      return;
    }
    throw failures.failure(info,
        shouldContainAtIndex(array, value, index, Array.get(array, index.value), comparisonStrategy));
  }

  void assertNotEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (!isArrayEmpty(array)) {
      return;
    }
    throw failures.failure(info, shouldNotBeEmpty());
  }

  void assertDoesNotContain(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    checkIndexValueIsValid(index, Integer.MAX_VALUE);
    int indexValue = index.value;
    if (indexValue >= sizeOf(array)) {
      return;
    }
    Object actualElement = Array.get(array, index.value);
    if (!areEqual(actualElement, value)) {
      return;
    }
    throw failures.failure(info, shouldNotContainAtIndex(array, value, index, comparisonStrategy));
  }

  void assertContainsOnly(AssertionInfo info, Failures failures, Object actual, Object values) {
    checkIsNotNull(values);
    assertNotNull(info, actual);
    // if both actual and values are empty arrays, then assertion passes.
    if (isArrayEmpty(actual) && isArrayEmpty(values))
      return;
    failIfEmptySinceActualIsNotEmpty(values);
    Set<Object> notExpected = asSetWithoutDuplicatesAccordingToComparisonStrategy(actual);
    Set<Object> notFound = containsOnly(notExpected, values);
    if (notExpected.isEmpty() && notFound.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContainOnly(actual, values, notFound, notExpected, comparisonStrategy));
  }

  void assertContainsOnlyOnce(AssertionInfo info, Failures failures, Object actual, Object values) {
    checkIsNotNull(values);
    assertNotNull(info, actual);
    // if both actual and values are empty arrays, then assertion passes.
    if (isArrayEmpty(actual) && isArrayEmpty(values))
      return;
    failIfEmptySinceActualIsNotEmpty(values);
    Set<?> expected = asTreeSetWithoutDuplicatesAccordingToComparisonStrategy(asList(values));
    List<Object> actualList = asList(actual);
    Set<?> actualSet = asTreeSetWithoutDuplicatesAccordingToComparisonStrategy(actualList);
    Iterable<?> duplicates = comparisonStrategy.duplicatesFrom(actualList);
    Set<Object> notFound = new LinkedHashSet<Object>();
    Set<Object> notOnlyOnce = new LinkedHashSet<Object>();
    for (Object element : expected) {
      if (!actualSet.contains(element)) {
        notFound.add(element);
      } else if (collectionContains(duplicates, element)) {
        notOnlyOnce.add(element);
      }
    }
    if (notFound.isEmpty() && notOnlyOnce.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContainsOnlyOnce(actual, values, notFound, notOnlyOnce, comparisonStrategy));
  }

  private Set<Object> containsOnly(Set<Object> actual, Object values) {
    Set<Object> notFound = new LinkedHashSet<Object>();
    for (Object o : asSetWithoutDuplicatesAccordingToComparisonStrategy(values)) {
      if (collectionContains(actual, o)) {
        collectionRemoves(actual, o);
      } else {
        notFound.add(o);
      }
    }
    return notFound;
  }

  /**
   * build a Set with that avoid duplicates <b>according to given comparison strategy</b>
   * 
   * @param array to feed the Set we want to build
   * @return a Set without duplicates <b>according to given comparison strategy</b>
   */
  private Set<Object> asSetWithoutDuplicatesAccordingToComparisonStrategy(Object array) {
    Set<Object> set = new LinkedHashSet<Object>();
    int size = sizeOf(array);
    for (int i = 0; i < size; i++) {
      Object element = Array.get(array, i);
      if (!collectionContains(set, element)) {
        set.add(element);
      }
    }
    return set;
  }

  /**
   * build a TreeSet with that avoid duplicates <b>according to given comparison strategy</b>
   * 
   * @param collection to feed the Set we want to build
   * @return a Set without duplicates <b>according to given comparison strategy</b> and with {@code .contains} who use
   *         the given comparison strategy.
   */
  private Set<Object> asTreeSetWithoutDuplicatesAccordingToComparisonStrategy(Iterable<Object> iterable) {
    Set<Object> set = new TreeSet<Object>(getComparatorFromComparisonStrategy());
    for (Object element : iterable) {
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
          if (comparisonStrategy.isGreaterThan(o1, o2))
            return 1;
          return -1;
        }
      };
    }
    return comparator;
  }

  /**
   * Delegates to {@link ComparisonStrategy#iterableContains(Iterable, Object)}
   */
  private boolean collectionContains(Iterable<?> actual, Object value) {
    return comparisonStrategy.iterableContains(actual, value);
  }

  /**
   * Delegates to {@link ComparisonStrategy#iterableRemoves(Iterable, Object)}
   */
  private void collectionRemoves(Collection<?> actual, Object value) {
    comparisonStrategy.iterableRemoves(actual, value);
  }

  void assertContainsSequence(AssertionInfo info, Failures failures, Object actual, Object sequence) {
    checkIsNotNull(sequence);
    assertNotNull(info, actual);
    // if both actual and values are empty arrays, then assertion passes.
    if (isArrayEmpty(actual) && isArrayEmpty(sequence))
      return;
    failIfEmptySinceActualIsNotEmpty(sequence);
    // look for given sequence, stop check when there is not enough elements remaining in actual to contain sequence
    int lastIndexWhereSequeceCanBeFound = sizeOf(actual) - sizeOf(sequence);
    for (int actualIndex = 0; actualIndex <= lastIndexWhereSequeceCanBeFound; actualIndex++) {
      if (containsSequenceAtGivenIndex(actualIndex, actual, sequence))
        return;
    }
    throw failures.failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
  }

  /**
   * Return true if actualArray contains exactly the given sequence at given starting index, false otherwise.
   * 
   * 
   * @param actualStartIndex the index to start looking for sequence in actualArray
   * @param actualArray the actual array to search sequence in
   * @param sequence the sequence to look for
   * @return true if actualArray contains exactly the given sequence at given starting index, false otherwise.
   */
  private boolean containsSequenceAtGivenIndex(int actualStartIndex, Object actualArray, Object sequence) {
    int sequenceSize = sizeOf(sequence);
    for (int i = 0; i < sequenceSize; i++) {
      if (areEqual(Array.get(sequence, i), Array.get(actualArray, i + actualStartIndex)))
        continue;
      return false;
    }
    return true;
  }

  /**
   * Delegates to {@link ComparisonStrategy#areEqual(Object, Object)}
   */
  private boolean areEqual(Object actual, Object other) {
    return comparisonStrategy.areEqual(actual, other);
  }

  void assertDoesNotContain(AssertionInfo info, Failures failures, Object array, Object values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, array);
    Set<Object> found = new LinkedHashSet<Object>();
    for (int i = 0; i < sizeOf(values); i++) {
      Object value = Array.get(values, i);
      if (arrayContains(array, value)) {
        found.add(value);
      }
    }
    if (found.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldNotContain(array, values, found, comparisonStrategy));
  }

  /**
   * Delegates to {@link ComparisonStrategy#arrayContains(Object, Object)}
   */
  private boolean arrayContains(Object array, Object value) {
    return comparisonStrategy.arrayContains(array, value);
  }

  void assertDoesNotHaveDuplicates(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    ArrayWrapperList wrapped = wrap(array);
    Iterable<?> duplicates = comparisonStrategy.duplicatesFrom(wrapped);
    if (isNullOrEmpty(duplicates)) {
      return;
    }
    throw failures.failure(info, shouldNotHaveDuplicates(array, duplicates, comparisonStrategy));
  }

  void assertStartsWith(AssertionInfo info, Failures failures, Object actual, Object sequence) {
    checkIsNotNull(sequence);
    assertNotNull(info, actual);
    // if both actual and values are empty arrays, then assertion passes.
    if (isArrayEmpty(actual) && isArrayEmpty(sequence))
      return;
    failIfEmptySinceActualIsNotEmpty(sequence);
    int sequenceSize = sizeOf(sequence);
    int arraySize = sizeOf(actual);
    if (arraySize < sequenceSize) {
      throw arrayDoesNotStartWithSequence(info, failures, actual, sequence);
    }
    for (int i = 0; i < sequenceSize; i++) {
      if (areEqual(Array.get(sequence, i), Array.get(actual, i))) {
        continue;
      }
      throw arrayDoesNotStartWithSequence(info, failures, actual, sequence);
    }
  }

  private AssertionError arrayDoesNotStartWithSequence(AssertionInfo info, Failures failures, Object array,
      Object sequence) {
    return failures.failure(info, shouldStartWith(array, sequence, comparisonStrategy));
  }

  void assertEndsWith(AssertionInfo info, Failures failures, Object actual, Object sequence) {
    checkIsNotNull(sequence);
    assertNotNull(info, actual);
    // if both actual and values are empty arrays, then assertion passes.
    if (isArrayEmpty(actual) && isArrayEmpty(sequence))
      return;
    failIfEmptySinceActualIsNotEmpty(sequence);
    int sequenceSize = sizeOf(sequence);
    int arraySize = sizeOf(actual);
    if (arraySize < sequenceSize) {
      throw arrayDoesNotEndWithSequence(info, failures, actual, sequence);
    }
    for (int i = 0; i < sequenceSize; i++) {
      int sequenceIndex = sequenceSize - (i + 1);
      int arrayIndex = arraySize - (i + 1);
      if (areEqual(Array.get(sequence, sequenceIndex), Array.get(actual, arrayIndex))) {
        continue;
      }
      throw arrayDoesNotEndWithSequence(info, failures, actual, sequence);
    }
  }

  void assertContainsNull(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (!arrayContains(array, null)) {
      throw failures.failure(info, shouldContainNull(array));
    }
  }

  void assertDoesNotContainNull(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (arrayContains(array, null)) {
      throw failures.failure(info, shouldNotContainNull(array));
    }
  }

  public <E> void assertAre(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(array, condition);
      if (notSatisfiesCondition.isEmpty()) {
        return;
      }
      throw failures.failure(info, elementsShouldBe(array, notSatisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertAreNot(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(array, condition);
      if (satisfiesCondition.isEmpty()) {
        return;
      }
      throw failures.failure(info, elementsShouldNotBe(array, satisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertHave(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(array, condition);
      if (notSatisfiesCondition.isEmpty()) {
        return;
      }
      throw failures.failure(info, elementsShouldHave(array, notSatisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertHaveNot(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(array, condition);
      if (satisfiesCondition.isEmpty()) {
        return;
      }
      throw failures.failure(info, elementsShouldNotHave(array, satisfiesCondition, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertAreAtLeast(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(array, condition);
      if (satisfiesCondition.size() >= times) {
        return;
      }
      throw failures.failure(info, elementsShouldBeAtLeast(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertAreNotAtLeast(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(array, condition);
      if (notSatisfiesCondition.size() >= times) {
        return;
      }
      throw failures.failure(info, elementsShouldNotBeAtLeast(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertAreAtMost(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(array, condition);
      if (satisfiesCondition.size() <= times) {
        return;
      }
      throw failures.failure(info, elementsShouldBeAtMost(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertAreNotAtMost(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(array, condition);
      if (notSatisfiesCondition.size() <= times) {
        return;
      }
      throw failures.failure(info, elementsShouldNotBeAtMost(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertAreExactly(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(array, condition);
      if (satisfiesCondition.size() == times) {
        return;
      }
      throw failures.failure(info, elementsShouldBeExactly(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertAreNotExactly(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(array, condition);
      if (notSatisfiesCondition.size() == times) {
        return;
      }
      throw failures.failure(info, ElementsShouldNotBeExactly.elementsShouldNotBeExactly(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertHaveAtLeast(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(array, condition);
      if (satisfiesCondition.size() >= times) {
        return;
      }
      throw failures.failure(info, elementsShouldHaveAtLeast(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertDoNotHaveAtLeast(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(array, condition);
      if (notSatisfiesCondition.size() >= times) {
        return;
      }
      throw failures.failure(info, elementsShouldNotHaveAtLeast(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertHaveAtMost(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(array, condition);
      if (satisfiesCondition.size() <= times) {
        return;
      }
      throw failures.failure(info, elementsShouldHaveAtMost(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertDoNotHaveAtMost(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(array, condition);
      if (notSatisfiesCondition.size() <= times) {
        return;
      }
      throw failures.failure(info, elementsShouldNotHaveAtMost(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertHaveExactly(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> satisfiesCondition = satisfiesCondition(array, condition);
      if (satisfiesCondition.size() == times) {
        return;
      }
      throw failures.failure(info, elementsShouldHaveExactly(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  public <E> void assertDoNotHaveExactly(AssertionInfo info, Failures failures, Conditions conditions, Object array,
      int times, Condition<E> condition) {
    assertNotNull(info, array);
    conditions.assertIsNotNull(condition);
    try {
      List<E> notSatisfiesCondition = notSatisfiesCondition(array, condition);
      if (notSatisfiesCondition.size() == times) {
        return;
      }
      throw failures.failure(info, elementsShouldNotHaveExactly(array, times, condition));
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldBeSameGenericBetweenIterableAndCondition(array, condition));
    }
  }

  @SuppressWarnings("unchecked")
  private <E> List<E> notSatisfiesCondition(Object array, Condition<E> condition) {
    List<E> notSatisfiesCondition = new LinkedList<E>();
    int arraySize = sizeOf(array);
    for (int i = 0; i < arraySize; i++) {
      Object o = Array.get(array, i);
      if (!condition.matches((E) o)) {
        notSatisfiesCondition.add((E) o);
      }
    }
    return notSatisfiesCondition;
  }

  @SuppressWarnings("unchecked")
  private <E> List<E> satisfiesCondition(Object array, Condition<E> condition) {
    List<E> notSatisfiesCondition = new LinkedList<E>();
    int arraySize = sizeOf(array);
    for (int i = 0; i < arraySize; i++) {
      Object o = Array.get(array, i);
      if (condition.matches((E) o)) {
        notSatisfiesCondition.add((E) o);
      }
    }
    return notSatisfiesCondition;
  }

  void assertIsSorted(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy) {
      // instead of comparing array elements with their natural comparator, use the one set by client.
      Comparator<?> comparator = ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator();
      assertIsSortedAccordingToComparator(info, failures, array, comparator);
      return;
    }
    // empty arrays are considered sorted even if component type is not sortable.
    if (sizeOf(array) == 0) {
      return;
    }
    assertThatArrayComponentTypeIsSortable(info, failures, array);
    try {
      // sorted assertion is only relevant if array elements are Comparable
      // => we should be able to build a Comparable array
      Comparable<Object>[] comparableArray = arrayOfComparableItems(array);
      // array with 0 or 1 element are considered sorted.
      if (comparableArray.length <= 1) {
        return;
      }
      for (int i = 0; i < comparableArray.length - 1; i++) {
        // array is sorted in ascending order iif element i is less or equal than element i+1
        if (comparableArray[i].compareTo(comparableArray[i + 1]) > 0) {
          throw failures.failure(info, shouldBeSorted(i, array));
        }
      }
    } catch (ClassCastException e) {
      // elements are either not Comparable or not mutually Comparable (e.g. array with String and Integer)
      throw failures.failure(info, shouldHaveMutuallyComparableElements(array));
    }
  }

  // is static to avoid "generify" Arrays
  static <T> void assertIsSortedAccordingToComparator(AssertionInfo info, Failures failures, Object array,
      Comparator<T> comparator) {
    assertNotNull(info, array);
    if (comparator == null) {
      throw new NullPointerException("The given comparator should not be null");
    }
    try {
      List<T> arrayAsList = asList(array);
      // empty arrays are considered sorted even if comparator can't be applied to <T>.
      if (arrayAsList.size() == 0) {
        return;
      }
      if (arrayAsList.size() == 1) {
        // call compare to see if unique element is compatible with comparator.
        comparator.compare(arrayAsList.get(0), arrayAsList.get(0));
        return;
      }
      for (int i = 0; i < arrayAsList.size() - 1; i++) {
        // array is sorted in comparator defined order iif element i is less or equal than element i+1
        if (comparator.compare(arrayAsList.get(i), arrayAsList.get(i + 1)) > 0) {
          throw failures.failure(info, shouldBeSortedAccordingToGivenComparator(i, array, comparator));
        }
      }
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldHaveComparableElementsAccordingToGivenComparator(array, comparator));
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> List<T> asList(Object array) {
    if (array == null) {
      return null;
    }
    if (!isArray(array)) {
      throw new IllegalArgumentException("The object should be an array");
    }
    int length = getLength(array);
    List<T> list = new ArrayList<T>(length);
    for (int i = 0; i < length; i++) {
      list.add((T) Array.get(array, i));
    }
    return list;
  }

  @SuppressWarnings("unchecked")
  private static Comparable<Object>[] arrayOfComparableItems(Object array) {
    ArrayWrapperList arrayWrapperList = wrap(array);
    Comparable<Object>[] arrayOfComparableItems = new Comparable[arrayWrapperList.size()];
    for (int i = 0; i < arrayWrapperList.size(); i++) {
      arrayOfComparableItems[i] = (Comparable<Object>) arrayWrapperList.get(i);
    }
    return arrayOfComparableItems;
  }

  private static void assertThatArrayComponentTypeIsSortable(AssertionInfo info, Failures failures, Object array) {
    ArrayWrapperList arrayAsList = wrap(array);
    Class<?> arrayComponentType = arrayAsList.getComponentType();
    if (arrayComponentType.isPrimitive()) {
      return;
    }
    if (!Comparable.class.isAssignableFrom(arrayComponentType)) {
      throw failures.failure(info, shouldHaveMutuallyComparableElements(array));
    }
  }

  // TODO manage empty values + empty actual
  private static void checkIsNotNullAndNotEmpty(Object values) {
    checkIsNotNull(values);
    if (isArrayEmpty(values)) {
      throw arrayOfValuesToLookForIsEmpty();
    }
  }

  /**
   * @param values
   */
  private static void checkIsNotNull(Object values) {
    if (values == null) {
      throw arrayOfValuesToLookForIsNull();
    }
  }

  private static boolean isArrayEmpty(Object array) {
    return sizeOf(array) == 0;
  }

  private AssertionError arrayDoesNotEndWithSequence(AssertionInfo info, Failures failures, Object array,
      Object sequence) {
    return failures.failure(info, shouldEndWith(array, sequence, comparisonStrategy));
  }

  private static void assertNotNull(AssertionInfo info, Object array) {
    Objects.instance().assertNotNull(info, array);
  }

  private static int sizeOf(Object array) {
    return Array.getLength(array);
  }

  private static void failIfEmptySinceActualIsNotEmpty(Object values) {
    if (isArrayEmpty(values))
      throw new AssertionError("actual is not empty while group of values to look for is.");
  }

}