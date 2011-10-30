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
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldBeEmpty.shouldBeEmpty;
import static org.fest.assertions.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.fest.assertions.error.ShouldBeSorted.*;
import static org.fest.assertions.error.ShouldContain.shouldContain;
import static org.fest.assertions.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.fest.assertions.error.ShouldContainNull.shouldContainNull;
import static org.fest.assertions.error.ShouldContainOnly.shouldContainOnly;
import static org.fest.assertions.error.ShouldContainSequence.shouldContainSequence;
import static org.fest.assertions.error.ShouldEndWith.shouldEndWith;
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
import static org.fest.util.Collections.duplicatesFrom;
import static org.fest.util.Objects.areEqual;

import java.lang.reflect.Array;
import java.util.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Index;
import org.fest.assertions.util.ArrayWrapperList;

/**
 * Assertions for object and primitive arrays. It trades off performance for DRY.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Arrays {

  private static final Arrays INSTANCE = new Arrays();

  static Arrays instance() {
    return INSTANCE;
  }

  private Arrays() {}

  void assertNullOrEmpty(AssertionInfo info, Failures failures, Object array) {
    if (array == null || isArrayEmpty(array)) return;
    throw failures.failure(info, shouldBeNullOrEmpty(array));
  }

  void assertEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (isArrayEmpty(array)) return;
    throw failures.failure(info, shouldBeEmpty(array));
  }

  void assertHasSize(AssertionInfo info, Failures failures, Object array, int expectedSize) {
    assertNotNull(info, array);
    int sizeOfActual = sizeOf(array);
    if (sizeOfActual == expectedSize) return;
    throw failures.failure(info, shouldHaveSize(array, sizeOfActual, expectedSize));
  }

  void assertContains(AssertionInfo info, Failures failures, Object array, Object values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, array);
    Set<Object> notFound = new LinkedHashSet<Object>();
    int valueCount = sizeOf(values);
    for (int i = 0; i < valueCount; i++) {
      Object value = Array.get(values, i);
      if (!arrayContains(array, value)) notFound.add(value);
    }
    if (notFound.isEmpty()) return;
    throw failures.failure(info, shouldContain(array, values, notFound));
  }

  void assertContains(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    assertNotEmpty(info, failures, array);
    checkIndexValueIsValid(index, sizeOf(array) - 1);
    Object actualElement = Array.get(array, index.value);
    if (areEqual(actualElement, value)) return;
    throw failures.failure(info, shouldContainAtIndex(array, value, index, Array.get(array, index.value)));
  }

  void assertNotEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (!isArrayEmpty(array)) return;
    throw failures.failure(info, shouldNotBeEmpty());
  }

  void assertDoesNotContain(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    checkIndexValueIsValid(index, Integer.MAX_VALUE);
    int indexValue = index.value;
    if (indexValue >= sizeOf(array)) return;
    Object actualElement = Array.get(array, index.value);
    if (!areEqual(actualElement, value)) return;
    throw failures.failure(info, shouldNotContainAtIndex(array, value, index));
  }

  void assertContainsOnly(AssertionInfo info, Failures failures, Object array, Object values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, array);
    Set<Object> notExpected = asSet(array);
    Set<Object> notFound = containsOnly(notExpected, values);
    if (notExpected.isEmpty() && notFound.isEmpty()) return;
    throw failures.failure(info, shouldContainOnly(array, values, notFound, notExpected));
  }

  private Set<Object> containsOnly(Set<Object> actual, Object values) {
    Set<Object> notFound = new LinkedHashSet<Object>();
    for (Object o : asSet(values)) {
      if (actual.contains(o)) actual.remove(o);
      else notFound.add(o);
    }
    return notFound;
  }

  private Set<Object> asSet(Object array) {
    Set<Object> set = new LinkedHashSet<Object>();
    int size = sizeOf(array);
    for (int i = 0; i < size; i++) {
      Object element = Array.get(array, i);
      set.add(element);
    }
    return set;
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
      if (i >= sequenceSize) break;
      if (!firstAlreadyFound) {
        if (!areEqual(o, Array.get(sequence, i))) continue;
        firstAlreadyFound = true;
        i++;
        continue;
      }
      if (areEqual(o, Array.get(sequence, i++))) continue;
      throw arrayDoesNotContainSequence(info, failures, array, sequence);
    }
    if (!firstAlreadyFound || i < sequenceSize) throw arrayDoesNotContainSequence(info, failures, array, sequence);
  }

  private AssertionError arrayDoesNotContainSequence(AssertionInfo info, Failures failures, Object array,
      Object sequence) {
    return failures.failure(info, shouldContainSequence(array, sequence));
  }

  void assertDoesNotContain(AssertionInfo info, Failures failures, Object array, Object values) {
    checkIsNotNullAndNotEmpty(values);
    assertNotNull(info, array);
    Set<Object> found = new LinkedHashSet<Object>();
    int valueCount = sizeOf(values);
    for (int i = 0; i < valueCount; i++) {
      Object value = Array.get(values, i);
      if (arrayContains(array, value)) found.add(value);
    }
    if (found.isEmpty()) return;
    throw failures.failure(info, shouldNotContain(array, values, found));
  }

  private boolean arrayContains(Object array, Object value) {
    int size = sizeOf(array);
    for (int i = 0; i < size; i++) {
      Object element = Array.get(array, i);
      if (areEqual(element, value)) return true;
    }
    return false;
  }

  void assertDoesNotHaveDuplicates(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    ArrayWrapperList wrapped = wrap(array);
    Collection<?> duplicates = duplicatesFrom(wrapped);
    if (duplicates.isEmpty()) return;
    throw failures.failure(info, shouldNotHaveDuplicates(array, duplicates));
  }

  void assertStartsWith(AssertionInfo info, Failures failures, Object array, Object sequence) {
    checkIsNotNullAndNotEmpty(sequence);
    assertNotNull(info, array);
    int sequenceSize = sizeOf(sequence);
    int arraySize = sizeOf(array);
    if (arraySize < sequenceSize) throw arrayDoesNotStartWithSequence(info, failures, array, sequence);
    for (int i = 0; i < sequenceSize; i++) {
      if (areEqual(Array.get(sequence, i), Array.get(array, i))) continue;
      throw arrayDoesNotStartWithSequence(info, failures, array, sequence);
    }
  }

  private AssertionError arrayDoesNotStartWithSequence(AssertionInfo info, Failures failures, Object array,
      Object sequence) {
    return failures.failure(info, shouldStartWith(array, sequence));
  }

  void assertEndsWith(AssertionInfo info, Failures failures, Object array, Object sequence) {
    checkIsNotNullAndNotEmpty(sequence);
    assertNotNull(info, array);
    int sequenceSize = sizeOf(sequence);
    int arraySize = sizeOf(array);
    if (arraySize < sequenceSize) throw arrayDoesNotEndWithSequence(info, failures, array, sequence);
    for (int i = 0; i < sequenceSize; i++) {
      int sequenceIndex = sequenceSize - (i + 1);
      int arrayIndex = arraySize - (i + 1);
      if (areEqual(Array.get(sequence, sequenceIndex), Array.get(array, arrayIndex))) continue;
      throw arrayDoesNotEndWithSequence(info, failures, array, sequence);
    }
  }

  void assertContainsNull(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (!arrayContains(array, null)) throw failures.failure(info, shouldContainNull(array));
  }

  void assertDoesNotContainNull(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (arrayContains(array, null)) throw failures.failure(info, shouldNotContainNull(array));
  }

  void assertIsSorted(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    assertThatArrayComponentTypeIsSortable(info, failures, array);
    try {
      // sorted assertion is only relevant if array elements are Comparable
      // => we should be able to build a Comparable array
      Comparable<Object>[] comparableArray = arrayOfComparableItems(array);
      // array with 0 or 1 element are considered sorted.
      if (comparableArray.length <= 1) return;
      for (int i = 0; i < comparableArray.length - 1; i++) {
        // array is sorted in ascending order iif element i is less or equal than element i+1
        if (comparableArray[i].compareTo(comparableArray[i + 1]) > 0)
          throw failures.failure(info, shouldBeSorted(i, array));
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
    if (comparator == null) throw new NullPointerException("The given comparator should not be null");
    try {
      List<T> arrayAsList = asList(array);
      // empty arrays are considered sorted even if comparator can't be applied to <T>.
      if (arrayAsList.size() == 0) return;
      if (arrayAsList.size() == 1) {
        // call compare to see if unique element is compatible with comparator.
        comparator.compare(arrayAsList.get(0), arrayAsList.get(0));
        return;
      }
      for (int i = 0; i < arrayAsList.size() - 1; i++) {
        // array is sorted in comparator defined order iif element i is less or equal than element i+1
        if (comparator.compare(arrayAsList.get(i), arrayAsList.get(i + 1)) > 0)
          throw failures.failure(info, shouldBeSortedAccordingToGivenComparator(i, array));
      }
    } catch (ClassCastException e) {
      throw failures.failure(info, shouldHaveComparableElementsAccordingToGivenComparator(array));
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> List<T> asList(Object array) {
    if (array == null) return null;
    if (!isArray(array)) throw new IllegalArgumentException("The object should be an array");
    int length = Array.getLength(array);
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
    if (arrayComponentType.isPrimitive()) return;
    if (!Comparable.class.isAssignableFrom(arrayComponentType))
      throw failures.failure(info, shouldHaveMutuallyComparableElements(array));
  }

  
  private void checkIsNotNullAndNotEmpty(Object values) {
    if (values == null) throw arrayOfValuesToLookForIsNull();
    if (isArrayEmpty(values)) throw arrayOfValuesToLookForIsEmpty();
  }

  private boolean isArrayEmpty(Object array) {
    return sizeOf(array) == 0;
  }

  private AssertionError arrayDoesNotEndWithSequence(AssertionInfo info, Failures failures, Object array,
      Object sequence) {
    return failures.failure(info, shouldEndWith(array, sequence));
  }

  private static void assertNotNull(AssertionInfo info, Object array) {
    Objects.instance().assertNotNull(info, array);
  }

  private int sizeOf(Object array) {
    return Array.getLength(array);
  }
}
