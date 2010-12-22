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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.Contains.contains;
import static org.fest.assertions.error.ContainsAtIndex.containsAtIndex;
import static org.fest.assertions.error.DoesNotContain.doesNotContain;
import static org.fest.assertions.error.DoesNotContainAtIndex.doesNotContainAtIndex;
import static org.fest.assertions.error.DoesNotContainOnly.doesNotContainOnly;
import static org.fest.assertions.error.DoesNotContainSequence.doesNotContainSequence;
import static org.fest.assertions.error.DoesNotEndWith.doesNotEndWith;
import static org.fest.assertions.error.DoesNotHaveSize.doesNotHaveSize;
import static org.fest.assertions.error.DoesNotStartWith.doesNotStartWith;
import static org.fest.assertions.error.HasDuplicates.hasDuplicates;
import static org.fest.assertions.error.IsEmpty.isEmpty;
import static org.fest.assertions.error.IsNotEmpty.isNotEmpty;
import static org.fest.assertions.error.IsNotNullOrEmpty.isNotNullOrEmpty;
import static org.fest.assertions.internal.CommonErrors.*;
import static org.fest.assertions.internal.CommonValidations.validateIndexValue;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.fest.util.Collections.*;
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
 */
class Arrays {

  private static final Arrays INSTANCE = new Arrays();

  static Arrays instance() {
    return INSTANCE;
  }

  private Arrays() {}

  void assertNullOrEmpty(AssertionInfo info, Failures failures, Object array) {
    if (array == null || isArrayEmpty(array)) return;
    throw failures.failure(info, isNotNullOrEmpty(array));
  }

  void assertEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (isArrayEmpty(array)) return;
    throw failures.failure(info, isNotEmpty(array));
  }

  void assertNotEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (!isArrayEmpty(array)) return;
    throw failures.failure(info, isEmpty());
  }

  void assertHasSize(AssertionInfo info, Failures failures, Object array, int expectedSize) {
    assertNotNull(info, array);
    int sizeOfActual = sizeOf(array);
    if (sizeOfActual == expectedSize) return;
    throw failures.failure(info, doesNotHaveSize(array, sizeOfActual, expectedSize));
  }

  void assertContains(AssertionInfo info, Failures failures, Object array, Object values) {
    isNotEmptyOrNull(values);
    assertNotNull(info, array);
    Set<Object> notFound = new LinkedHashSet<Object>();
    int valueCount = sizeOf(values);
    for (int i = 0; i < valueCount; i++) {
      Object value = Array.get(values, i);
      if (!arrayContains(array, value)) notFound.add(value);
    }
    if (notFound.isEmpty()) return;
    throw failures.failure(info, doesNotContain(array, values, notFound));
  }

  void assertContains(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    assertNotEmpty(info, failures, array);
    validateIndexValue(index, sizeOf(array) - 1);
    Object actualElement = Array.get(array, index.value);
    if (areEqual(actualElement, value)) return;
    throw failures.failure(info, doesNotContainAtIndex(wrap(array), value, index));
  }

  void assertDoesNotContain(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    validateIndexValue(index, Integer.MAX_VALUE);
    int indexValue = index.value;
    if (indexValue >= sizeOf(array)) return;
    Object actualElement = Array.get(array, index.value);
    if (!areEqual(actualElement, value)) return;
    throw failures.failure(info, containsAtIndex(wrap(array), value, index));
  }

  void assertContainsOnly(AssertionInfo info, Failures failures, Object array, Object values) {
    isNotEmptyOrNull(values);
    assertNotNull(info, array);
    Set<Object> notExpected = asSet(array);
    Set<Object> notFound = containsOnly(notExpected, values);
    if (notExpected.isEmpty() && notFound.isEmpty()) return;
    throw failures.failure(info, doesNotContainOnly(wrap(array), wrap(values), notExpected, notFound));
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
    isNotEmptyOrNull(sequence);
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

  private AssertionError arrayDoesNotContainSequence(AssertionInfo info, Failures failures, Object array, Object sequence) {
    return failures.failure(info, doesNotContainSequence(wrap(array), wrap(sequence)));
  }

  void assertDoesNotContain(AssertionInfo info, Failures failures, Object array, Object values) {
    isNotEmptyOrNull(values);
    assertNotNull(info, array);
    Set<Object> found = new LinkedHashSet<Object>();
    int valueCount = sizeOf(values);
    for (int i = 0; i < valueCount; i++) {
      Object value = Array.get(values, i);
      if (arrayContains(array, value)) found.add(value);
    }
    if (found.isEmpty()) return;
    throw failures.failure(info, contains(array, values, found));
  }

  private void isNotEmptyOrNull(Object values) {
    if (values == null) throw arrayOfValuesToLookForIsNull();
    if (isArrayEmpty(values)) throw arrayOfValuesToLookForIsEmpty();
  }

  private boolean isArrayEmpty(Object array) {
    return sizeOf(array) == 0;
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
    if (isEmpty(duplicates)) return;
    throw failures.failure(info, hasDuplicates(wrapped, duplicates));
  }

  void assertStartsWith(AssertionInfo info, Failures failures, Object array, Object sequence) {
    isNotEmptyOrNull(sequence);
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
    return failures.failure(info, doesNotStartWith(wrap(array), wrap(sequence)));
  }

  void assertEndsWith(AssertionInfo info, Failures failures, Object array, Object sequence) {
    isNotEmptyOrNull(sequence);
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

  private AssertionError arrayDoesNotEndWithSequence(AssertionInfo info, Failures failures, Object array,
      Object sequence) {
    return failures.failure(info, doesNotEndWith(wrap(array), wrap(sequence)));
  }

  private void assertNotNull(AssertionInfo info, Object array) {
    Objects.instance().assertNotNull(info, array);
  }

  private int sizeOf(Object array) {
    return Array.getLength(array);
  }
}
