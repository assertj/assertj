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
package org.assertj.core.util;

import static java.util.Collections.emptyList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Utility methods related to arrays.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Florent Biville
 */
public class Arrays {

  /**
   * Indicates whether the given object is not {@code null} and is an array.
   * 
   * @param o the given object.
   * @return {@code true} if the given object is not {@code null} and is an array, otherwise {@code false}.
   */
  public static boolean isArray(Object o) {
    return o != null && o.getClass().isArray();
  }

  /**
   * Indicates whether the given array is {@code null} or empty.
   * 
   * @param <T> the type of elements of the array.
   * @param array the array to check.
   * @return {@code true} if the given array is {@code null} or empty, otherwise {@code false}.
   */
  public static <T> boolean isNullOrEmpty(T[] array) {
    return array == null || isEmpty(array);
  }

  /**
   * Returns an array containing the given arguments.
   * 
   * @param <T> the type of the array to return.
   * @param values the values to store in the array.
   * @return an array containing the given arguments.
   */
  @SafeVarargs
  public static <T> T[] array(T... values) {
    return values;
  }

  /** 
   * Returns an int[] from the {@link AtomicIntegerArray}, null if the given atomic array is null.
   * 
   * @param atomicIntegerArray the {@link AtomicIntegerArray} to convert to int[].
   * @return an int[].
   */
  public static int[] array(AtomicIntegerArray atomicIntegerArray) {
    if (atomicIntegerArray == null) return null;
    int[] array = new int[atomicIntegerArray.length()];
    for (int i = 0; i < array.length; i++) {
      array[i] = atomicIntegerArray.get(i);
    }
    return array;
  }

  /** 
   * Returns an long[] from the {@link AtomicLongArray}, null if the given atomic array is null.
   * 
   * @param atomicLongArray the {@link AtomicLongArray} to convert to long[].
   * @return an long[].
   */
  public static long[] array(AtomicLongArray atomicLongArray) {
    if (atomicLongArray == null) return null;
    long[] array = new long[atomicLongArray.length()];
    for (int i = 0; i < array.length; i++) {
      array[i] = atomicLongArray.get(i);
    }
    return array;
  }

  /** 
   * Returns an T[] from the {@link AtomicReferenceArray}, null if the given atomic array is null.
   * 
   * @param <T> the type of elements of the array.
   * @param atomicReferenceArray the {@link AtomicReferenceArray} to convert to T[].
   * @return an T[].
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] array(AtomicReferenceArray<T> atomicReferenceArray) {
    if (atomicReferenceArray == null) return null;
    int length = atomicReferenceArray.length();
    if (length == 0) return array();
    List<T> list = newArrayList();
    for (int i = 0; i < length; i++) {
      list.add(atomicReferenceArray.get(i));
    }
    return list.toArray((T[]) Array.newInstance(Object.class, length));
  }

  /**
   * Returns all the non-{@code null} elements in the given array.
   * 
   * @param <T> the type of elements of the array.
   * @param array the given array.
   * @return all the non-{@code null} elements in the given array. An empty list is returned if the given array is
   *         {@code null}.
   */
  public static <T> List<T> nonNullElementsIn(T[] array) {
    if (array == null) return emptyList();
    List<T> nonNullElements = new ArrayList<>();
    for (T o : array) {
      if (o != null) nonNullElements.add(o);
    }
    return nonNullElements;
  }

  /**
   * Returns {@code true} if the given array has only {@code null} elements, {@code false} otherwise. If given array is
   * empty, this method returns {@code true}.
   * 
   * @param <T> the type of elements of the array.
   * @param array the given array. <b>It must not be null</b>.
   * @return {@code true} if the given array has only {@code null} elements or is empty, {@code false} otherwise.
   * @throws NullPointerException if the given array is {@code null}.
   */
  public static <T> boolean hasOnlyNullElements(T[] array) {
    checkNotNull(array);
    if (isEmpty(array)) return false;
    for (T o : array) {
      if (o != null) return false;
    }
    return true;
  }

  private static <T> boolean isEmpty(T[] array) {
    return array.length == 0;
  }

  public static boolean isObjectArray(Object o) {
    return isArray(o) && !isArrayTypePrimitive(o);
  }

  public static boolean isArrayTypePrimitive(Object o) {
    return o != null && o.getClass().getComponentType().isPrimitive();
  }

  public static IllegalArgumentException notAnArrayOfPrimitives(Object o) {
    return new IllegalArgumentException(String.format("<%s> is not an array of primitives", o));
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] prepend(T first, T... rest) {
    T[] result = (T[]) new Object[1 + rest.length];
    result[0] = first;
    System.arraycopy(rest, 0, result, 1, rest.length);
    return result;
  }

  private Arrays() {}

}
