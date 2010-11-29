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

import static org.fest.assertions.error.DoesNotContain.doesNotContain;
import static org.fest.assertions.error.DoesNotHaveSize.doesNotHaveSize;
import static org.fest.assertions.error.IsEmpty.isEmpty;
import static org.fest.assertions.error.IsNotEmpty.isNotEmpty;
import static org.fest.assertions.error.IsNotNullOrEmpty.isNotNullOrEmpty;
import static org.fest.assertions.internal.Errors.*;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.fest.util.Objects.areEqual;

import java.lang.reflect.Array;
import java.util.LinkedHashSet;
import java.util.Set;

import org.fest.assertions.core.AssertionInfo;

/**
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
    throw failures.failure(info, isNotNullOrEmpty(wrap(array)));
  }

  void assertEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (isArrayEmpty(array)) return;
    throw failures.failure(info, isNotEmpty(wrap(array)));
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
    throw failures.failure(info, doesNotHaveSize(wrap(array), expectedSize));
  }

  void assertContains(AssertionInfo info, Failures failures, Object array, Object values) {
    isNotEmptyOrNull(values);
    assertNotNull(info, array);
    Set<Object> notFound = new LinkedHashSet<Object>();
    int valueCount = sizeOf(values);
    for (int i = 0; i < valueCount; i++) {
      Object value = Array.get(values, i);
      if (!contains(array, value)) notFound.add(value);
    }
    if (notFound.isEmpty()) return;
    throw failures.failure(info, doesNotContain(wrap(array), wrap(values), notFound));
  }

  private boolean contains(Object array, Object value) {
    int size = sizeOf(array);
    for (int i = 0; i < size; i++) {
      Object element = Array.get(array, i);
      if (areEqual(element, value)) return true;
    }
    return false;
  }

  private void isNotEmptyOrNull(Object values) {
    if (values == null) throw arrayOfValuesToLookForIsNull();
    if (isArrayEmpty(values)) throw arrayOfValuesToLookForIsEmpty();
  }

  private boolean isArrayEmpty(Object array) {
    return sizeOf(array) == 0;
  }

  private int sizeOf(Object array) {
    return Array.getLength(array);
  }

  private void assertNotNull(AssertionInfo info, Object array) {
    Objects.instance().assertNotNull(info, array);
  }
}
