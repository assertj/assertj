/*
 * Created on Nov 28, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.lang.reflect.Array.getLength;
import static org.fest.assertions.error.ConditionAndGroupGenericParameterTypeShouldBeTheSame.shouldBeSameGenericBetweenIterableAndCondition;
import static org.fest.assertions.error.ElementsShouldBe.elementsShouldBe;
import static org.fest.assertions.error.ElementsShouldBeAtLeast.elementsShouldBeAtLeast;
import static org.fest.assertions.error.ElementsShouldBeAtMost.elementsShouldBeAtMost;
import static org.fest.assertions.error.ElementsShouldBeExactly.elementsShouldBeExactly;
import static org.fest.assertions.error.ElementsShouldHave.elementsShouldHave;
import static org.fest.assertions.error.ElementsShouldHaveAtLeast.elementsShouldHaveAtLeast;
import static org.fest.assertions.error.ElementsShouldHaveAtMost.elementsShouldHaveAtMost;
import static org.fest.assertions.error.ElementsShouldHaveExactly.elementsShouldHaveExactly;
import static org.fest.assertions.error.ElementsShouldNotBe.elementsShouldNotBe;
import static org.fest.assertions.error.ElementsShouldNotBeAtLeast.elementsShouldNotBeAtLeast;
import static org.fest.assertions.error.ElementsShouldNotBeAtMost.elementsShouldNotBeAtMost;
import static org.fest.assertions.error.ElementsShouldNotHave.elementsShouldNotHave;
import static org.fest.assertions.error.ElementsShouldNotHaveAtLeast.elementsShouldNotHaveAtLeast;
import static org.fest.assertions.error.ElementsShouldNotHaveAtMost.elementsShouldNotHaveAtMost;
import static org.fest.assertions.error.ElementsShouldNotHaveExactly.elementsShouldNotHaveExactly;
import static org.fest.assertions.error.ShouldBeEmpty.shouldBeEmpty;
import static org.fest.assertions.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.fest.assertions.error.ShouldBeSorted.*;
import static org.fest.assertions.error.ShouldContain.shouldContain;
import static org.fest.assertions.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.fest.assertions.error.ShouldContainNull.shouldContainNull;
import static org.fest.assertions.error.ShouldContainOnly.shouldContainOnly;
import static org.fest.assertions.error.ShouldContainSequence.shouldContainSequence;
import static org.fest.assertions.error.ShouldEndWith.shouldEndWith;
import static org.fest.assertions.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.fest.assertions.error.ShouldHaveSize.shouldHaveSize;
import static org.fest.assertions.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.fest.assertions.error.ShouldNotContain.shouldNotContain;
import static org.fest.assertions.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.fest.assertions.error.ShouldNotContainNull.shouldNotContainNull;
import static org.fest.assertions.error.ShouldNotHaveDuplicates.shouldNotHaveDuplicates;
import static org.fest.assertions.error.ShouldStartWith.shouldStartWith;
import static org.fest.assertions.internal.CommonErrors.*;
import static org.fest.assertions.internal.CommonValidations.checkIndexValueIsValid;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.fest.util.Arrays.isArray;
import static org.fest.util.Iterables.isNullOrEmpty;
import static org.fest.util.Lists.newArrayList;

import java.lang.reflect.Array;
import java.util.*;

import org.fest.assertions.core.*;
import org.fest.assertions.data.Index;
import org.fest.assertions.error.ElementsShouldNotBeExactly;
import org.fest.assertions.util.ArrayWrapperList;
import org.fest.util.*;

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
    if (comparisonStrategy instanceof ComparatorBasedComparisonStrategy) { return ((ComparatorBasedComparisonStrategy) comparisonStrategy)
        .getComparator(); }
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
    int sizeOfOther = org.fest.util.Iterables.sizeOf(other);
    if (sizeOfActual == sizeOfOther) {
      return;
    }
    throw failures.failure(info, shouldHaveSameSizeAs(array, sizeOfActual, sizeOfOther));
  }

  void assertHasSameSizeAs(AssertionInfo info, Failures failures, Object array, Object other) {
    assertNotNull(info, array);
    if (other == null) {
      throw arrayOfValuesToLookForIsNull();
    }
    int sizeOfActual = sizeOf(array);
    int sizeOfOther = sizeOf(other);
    if (sizeOfActual == sizeOfOther) {
      return;
    }
    throw failures.failure(info, shouldHaveSameSizeAs(array, sizeOfActual, sizeOfOther));
  }

  void assertContains(AssertionInfo info, Failures failures, Object array, Object values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, array);
    Set<Object> notFound = new LinkedHashSet<Object>();
    int valueCount = sizeOf(values);
    for (int i = 0; i < valueCount; i++) {
      Object value = Array.get(values, i);
      if (!arrayContains(array, value)) {
        notFound.add(value);
      }
    }
    if (notFound.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContain(array, values, notFound, comparisonStrategy));
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
    throw failures.failure(info, shouldContainAtIndex(array, value, index, Array.get(array, index.value), comparisonStrategy));
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

  void assertContainsOnly(AssertionInfo info, Failures failures, Object array, Object values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, array);
    Set<Object> notExpected = asSetWithoutDuplicatesAccordingToComparisonStrategy(array);
    Set<Object> notFound = containsOnly(notExpected, values);
    if (notExpected.isEmpty() && notFound.isEmpty()) {
      return;
    }
    throw failures.failure(info, shouldContainOnly(array, values, notFound, notExpected, comparisonStrategy));
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
   * @param elements to feed the Set we want to build
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
   * Delegates to {@link ComparisonStrategy#iterableContains(Collection, Object)}
   */
  private boolean collectionContains(Collection<?> actual, Object value) {
    return comparisonStrategy.iterableContains(actual, value);
  }

  /**
   * Delegates to {@link ComparisonStrategy#iterableRemoves(Iterable, Object)}
   */
  private void collectionRemoves(Collection<?> actual, Object value) {
    comparisonStrategy.iterableRemoves(actual, value);
  }

  void assertContainsSequence(AssertionInfo info, Failures failures, Object array, Object sequence) {
    checkIsNotNullAndNotEmpty(sequence);
    assertNotNull(info, array);
    boolean firstAlreadyFound = false;
    int i = 0;
    int sequenceSize = sizeOf(sequence);
    int sizeOfActual = sizeOf(array);
    for (int j = 0; j < sizeOfActual; j++) {
      Object o = Array.get(array, j);
      if (i >= sequenceSize) {
        break;
      }
      if (!firstAlreadyFound) {
        if (!areEqual(o, Array.get(sequence, i))) {
          continue;
        }
        firstAlreadyFound = true;
        i++;
        continue;
      }
      if (areEqual(o, Array.get(sequence, i++))) {
        continue;
      }
      throw arrayDoesNotContainSequence(info, failures, array, sequence);
    }
    if (!firstAlreadyFound || i < sequenceSize) {
      throw arrayDoesNotContainSequence(info, failures, array, sequence);
    }
  }

  /**
   * Delegates to {@link ComparisonStrategy#areEqual(Object, Object)}
   */
  private boolean areEqual(Object actual, Object other) {
    return comparisonStrategy.areEqual(actual, other);
  }

  private AssertionError arrayDoesNotContainSequence(AssertionInfo info, Failures failures, Object array, Object sequence) {
    return failures.failure(info, shouldContainSequence(array, sequence, comparisonStrategy));
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

  void assertStartsWith(AssertionInfo info, Failures failures, Object array, Object sequence) {
    checkIsNotNullAndNotEmpty(sequence);
    assertNotNull(info, array);
    int sequenceSize = sizeOf(sequence);
    int arraySize = sizeOf(array);
    if (arraySize < sequenceSize) {
      throw arrayDoesNotStartWithSequence(info, failures, array, sequence);
    }
    for (int i = 0; i < sequenceSize; i++) {
      if (areEqual(Array.get(sequence, i), Array.get(array, i))) {
        continue;
      }
      throw arrayDoesNotStartWithSequence(info, failures, array, sequence);
    }
  }

  private AssertionError arrayDoesNotStartWithSequence(AssertionInfo info, Failures failures, Object array, Object sequence) {
    return failures.failure(info, shouldStartWith(array, sequence, comparisonStrategy));
  }

  void assertEndsWith(AssertionInfo info, Failures failures, Object array, Object sequence) {
    checkIsNotNullAndNotEmpty(sequence);
    assertNotNull(info, array);
    int sequenceSize = sizeOf(sequence);
    int arraySize = sizeOf(array);
    if (arraySize < sequenceSize) {
      throw arrayDoesNotEndWithSequence(info, failures, array, sequence);
    }
    for (int i = 0; i < sequenceSize; i++) {
      int sequenceIndex = sequenceSize - (i + 1);
      int arrayIndex = arraySize - (i + 1);
      if (areEqual(Array.get(sequence, sequenceIndex), Array.get(array, arrayIndex))) {
        continue;
      }
      throw arrayDoesNotEndWithSequence(info, failures, array, sequence);
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

  public <E> void assertAre(AssertionInfo info, Failures failures, Conditions conditions, Object array, Condition<E> condition) {
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

  public <E> void assertAreNot(AssertionInfo info, Failures failures, Conditions conditions, Object array, Condition<E> condition) {
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

  public <E> void assertHave(AssertionInfo info, Failures failures, Conditions conditions, Object array, Condition<E> condition) {
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

  public <E> void assertHaveNot(AssertionInfo info, Failures failures, Conditions conditions, Object array, Condition<E> condition) {
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

  public <E> void assertAreAtLeast(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertAreNotAtLeast(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertAreAtMost(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertAreNotAtMost(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertAreExactly(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertAreNotExactly(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertHaveAtLeast(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertDoNotHaveAtLeast(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertHaveAtMost(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertDoNotHaveAtMost(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertHaveExactly(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  public <E> void assertDoNotHaveExactly(AssertionInfo info, Failures failures, Conditions conditions, Object array, int times,
      Condition<E> condition) {
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

  private void checkIsNotNullAndNotEmpty(Object values) {
    if (values == null) {
      throw arrayOfValuesToLookForIsNull();
    }
    if (isArrayEmpty(values)) {
      throw arrayOfValuesToLookForIsEmpty();
    }
  }

  private boolean isArrayEmpty(Object array) {
    return sizeOf(array) == 0;
  }

  private AssertionError arrayDoesNotEndWithSequence(AssertionInfo info, Failures failures, Object array, Object sequence) {
    return failures.failure(info, shouldEndWith(array, sequence, comparisonStrategy));
  }

  private static void assertNotNull(AssertionInfo info, Object array) {
    Objects.instance().assertNotNull(info, array);
  }

  private int sizeOf(Object array) {
    return Array.getLength(array);
  }

}