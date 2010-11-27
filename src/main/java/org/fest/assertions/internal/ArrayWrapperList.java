/*
 * Created on Nov 26, 2010
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

import java.lang.reflect.Array;
import java.util.AbstractList;

/**
 * An list-like wrapper for arrays. This class does not provide type-safety in order to handle both arrays
 * of objects and arrays of primitives.
 *
 * @author Alex Ruiz
 */
class ArrayWrapperList extends AbstractList<Object> {

  static ArrayWrapperList wrap(Object array) {
    if (array == null) return null;
    return new ArrayWrapperList(array);
  }

  private final Object array;

  private ArrayWrapperList(Object array) {
    this.array = array;
  }

  @Override public Object get(int index) {
    validateInRange(index);
    return Array.get(array, index);
  }

  private void validateInRange(int index) {
    int size = size();
    if (index < size()) return;
    throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, size));
  }

  @Override public int size() {
    return Array.getLength(array);
  }

  Object array() { return array; }
}
