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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import static java.lang.reflect.Array.getLength;
import static java.util.Collections.emptyList;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.presentation.Representation;

/**
 * Utility methods related to arrays.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Arrays extends GroupFormatUtil {

  private static final String NULL = "null";

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

  /**
   * Returns the {@code String} representation of the given array, or {@code null} if the given object is either
   * {@code null} or not an array. This method supports arrays having other arrays as elements.
   *
   * @param representation
   * @param array the object that is expected to be an array.
   * @return the {@code String} representation of the given array.
   */
  public static String format(Representation representation, Object o) {
    if (!isArray(o)) return null;
    return isObjectArray(o) ? smartFormat(representation, (Object[]) o) : formatPrimitiveArray(representation, o);
  }

  private static <T> boolean isEmpty(T[] array) {
    return array.length == 0;
  }

  private static boolean isObjectArray(Object o) {
    return isArray(o) && !isArrayTypePrimitive(o);
  }

  private static boolean isArrayTypePrimitive(Object o) {
    return o != null && o.getClass().getComponentType().isPrimitive();
  }

  static IllegalArgumentException notAnArrayOfPrimitives(Object o) {
    return new IllegalArgumentException(String.format("<%s> is not an array of primitives", o));
  }

  private static String smartFormat(Representation representation, Object[] iterable) {
    Set<Object[]> alreadyFormatted = new HashSet<>();
    String singleLineDescription = singleLineFormat(representation, iterable, DEFAULT_START, DEFAULT_END,
                                                    alreadyFormatted);
    return doesDescriptionFitOnSingleLine(singleLineDescription) ?
        singleLineDescription : multiLineFormat(representation, iterable, alreadyFormatted);
  }

  private static String singleLineFormat(Representation representation, Object[] iterable, String start, String end,
                                        Set<Object[]> alreadyFormatted) {
    return format(representation, iterable, ELEMENT_SEPARATOR, INDENTATION_FOR_SINGLE_LINE, alreadyFormatted);
  }

  private static String multiLineFormat(Representation representation, Object[] iterable, Set<Object[]> alreadyFormatted) {
    return format(representation, iterable, ELEMENT_SEPARATOR_WITH_NEWLINE, INDENTATION_AFTER_NEWLINE, alreadyFormatted);
  }

  private static boolean doesDescriptionFitOnSingleLine(String singleLineDescription) {
    return singleLineDescription == null || singleLineDescription.length() < maxLengthForSingleLineDescription;
  }

  private static String format(Representation representation, Object[] array, String elementSeparator,
                               String indentation, Set<Object[]> alreadyFormatted) {
    if (array == null) return null;
    if (array.length == 0) return DEFAULT_START + DEFAULT_END;
    // iterable has some elements
    StringBuilder desc = new StringBuilder();
    desc.append(DEFAULT_START);
    alreadyFormatted.add(array); // used to avoid infinite recursion when array contains itself
    int i = 0;
    while (true) {
      Object element = array[i];
      // do not indent first element
      if (i != 0) desc.append(indentation);
      // add element representation
      if (!isArray(element)) desc.append(element == null ? NULL : representation.toStringOf(element));
      else if (isArrayTypePrimitive(element)) desc.append(formatPrimitiveArray(representation, element));
      else if (alreadyFormatted.contains(element)) desc.append("(this array)");
      else desc.append(format(representation, (Object[]) element, elementSeparator, indentation, alreadyFormatted));
      // manage end description
      if (i == array.length - 1) {
        alreadyFormatted.remove(array);
        return desc.append(DEFAULT_END).toString();
      }
      // there are still elements to be describe
      desc.append(elementSeparator);
      i++;
    }
  }

  private static String formatPrimitiveArray(Representation representation, Object o) {
    if (!isArray(o)) return null;
    if (!isArrayTypePrimitive(o)) throw Arrays.notAnArrayOfPrimitives(o);
    int size = getLength(o);
    if (size == 0) return DEFAULT_START + DEFAULT_END;
    StringBuilder buffer = new StringBuilder();
    buffer.append(DEFAULT_START);
    buffer.append(representation.toStringOf(Array.get(o, 0)));
    for (int i = 1; i < size; i++) {
      buffer.append(ELEMENT_SEPARATOR)
            .append(INDENTATION_FOR_SINGLE_LINE)
            .append(representation.toStringOf(Array.get(o, i)));
    }
    buffer.append(DEFAULT_END);
    return buffer.toString();
  }

  private Arrays() {}

}
