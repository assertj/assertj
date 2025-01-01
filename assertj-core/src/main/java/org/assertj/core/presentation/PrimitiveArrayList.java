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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.presentation;

import static java.lang.reflect.Array.getLength;
import static org.assertj.core.util.Arrays.isArrayTypePrimitive;
import static org.assertj.core.util.Arrays.isObjectArray;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.lang.reflect.Array;
import java.util.AbstractList;

/**
 * Provides a read-only view of an array as a list.
 *
 * <p>This is different from {@link java.util.ArrayList} because the array may be an array of primitives. Arrays of non-primitives
 * also work, but {@link java.util.ArrayList} would probably be a better choice for these arrays.
 */
final class PrimitiveArrayList extends AbstractList<Object> {
  /** The array to provide a view of. */
  private final Object array;

  /**
   * Creates a new {@link PrimitiveArrayList}.
   *
   * @param array primitive or object array
   */
  PrimitiveArrayList(final Object array) {
    checkArgument(isObjectArray(array) || isArrayTypePrimitive(array), "input must be an array");
    this.array = array;
  }

  @Override
  public Object get(final int index) {
    return Array.get(array, index);
  }

  @Override
  public int size() {
    return getLength(array);
  }
}
