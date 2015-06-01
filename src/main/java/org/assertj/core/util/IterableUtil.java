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

import static java.util.Collections.emptyList;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.newArrayList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.assertj.core.presentation.Representation;

/**
 * Utility methods related to {@code Iterable}s.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public final class IterableUtil {

  private static final String ELEMENT_SEPARATOR = ",";
  private static final String ELEMENT_SEPARATOR_WITH_NEWLINE = ELEMENT_SEPARATOR + System.lineSeparator();
  private static final String DEFAULT_END = "]";
  private static final String DEFAULT_START = "[";
  // 4 spaces indentation : 2 space indentation after new line + '<' + '['
  private static final String INDENTATION_AFTER_NEWLINE = "    ";
  private static final String ONE_LINE_INDENTATION = " ";

  /**
   * Indicates whether the given {@link Iterable} is {@code null} or empty.
   * 
   * @param iterable the given {@code Iterable} to check.
   * @return {@code true} if the given {@code Iterable} is {@code null} or empty, otherwise {@code false}.
   */
  public static boolean isNullOrEmpty(Iterable<?> iterable) {
    if (iterable == null) return true;
    if (iterable instanceof Collection && ((Collection<?>) iterable).isEmpty()) return true;
    return !iterable.iterator().hasNext();
  }

  /**
   * Returns the size of the given {@link Iterable}.
   * 
   * @param iterable the {@link Iterable} to get size.
   * @return the size of the given {@link Iterable}.
   * @throws NullPointerException if given {@link Iterable} is null.
   */
  public static int sizeOf(Iterable<?> iterable) {
    if (iterable == null) throw new NullPointerException("Iterable must not be null");
    if (iterable instanceof Collection) return ((Collection<?>) iterable).size();
    int size = 0;
    Iterator<?> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      size++;
      iterator.next();
    }
    return size;
  }

  /**
   * Returns all the non-{@code null} elements in the given {@link Iterable}.
   * 
   * @param <T> the type of elements of the {@code Iterable}.
   * @param i the given {@code Iterable}.
   * @return all the non-{@code null} elements in the given {@code Iterable}. An empty list is returned if the given
   *         {@code Iterable} is {@code null}.
   * @since 1.1.3
   */
  public static <T> List<T> nonNullElementsIn(Iterable<? extends T> i) {
    if (isNullOrEmpty(i)) return emptyList();
    List<T> nonNull = new ArrayList<>();
    for (T element : i) {
      if (element != null) nonNull.add(element);
    }
    return nonNull;
  }

  /**
   * Create an array from an {@link Iterable}.
   * <p/>
   * Note: this method will return Object[]. If you require a typed array please use {@link #toArray(Iterable, Class)}.
   * It's main usage is to keep the generic type for chaining call like in:
   * 
   * <pre><code class='java'>
   * public S containsOnlyElementsOf(Iterable<? extends T> iterable) {
   *   return containsOnly(toArray(iterable));
   * }
   * </code></pre>
   * 
   * @param iterable an {@link Iterable} to translate in an array.
   * @param <T> the type of elements of the {@code Iterable}.
   * @return all the elements from the given {@link Iterable} in an array. {@code null} if given {@link Iterable} is
   *         null.
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] toArray(Iterable<? extends T> iterable) {
    if (iterable == null) return null;
    return (T[]) newArrayList(iterable).toArray();
  }

  /**
   * Create an typed array from an {@link Iterable}.
   *
   * @param iterable an {@link Iterable} to translate in an array.
   * @param type the type of the resulting array.
   * @param <T> the type of elements of the {@code Iterable}.
   * @return all the elements from the given {@link Iterable} in an array. {@code null} if given {@link Iterable} is
   *         null.
   */
  public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
    if (iterable == null) return null;
    Collection<? extends T> collection = toCollection(iterable);
    T[] array = newArray(type, collection.size());
    return collection.toArray(array);
  }

  private static <T> Collection<T> toCollection(Iterable<T> iterable) {
    return iterable instanceof Collection ? (Collection<T>) iterable : newArrayList(iterable);
  }

  @SuppressWarnings("unchecked")
  private static <T> T[] newArray(Class<T> type, int length) {
    return (T[]) Array.newInstance(type, length);
  }

  private IterableUtil() {}

  /**
   * Returns the {@code String} representation of the given {@code Iterable}, or {@code null} if the given
   * {@code Iterable} is {@code null}.
   * 
   * 
   * @param representation
   * @param iterable the {@code Iterable} to format.
   * @return the {@code String} representation of the given {@code Collection}.
   */
  public static String format(Representation representation, Iterable<?> iterable) {
    return format(representation, iterable, DEFAULT_START, DEFAULT_END);
  }

  /**
   * Returns the {@code String} {@link org.assertj.core.presentation.StandardRepresentation standard representation} of
   * the given {@code Iterable}, or {@code null} if the given {@code Iterable} is {@code null}.
   * 
   * 
   * @param iterable the {@code Iterable} to format.
   * @return the {@code String} representation of the given {@code Iterable}.
   */
  public static String format(Iterable<?> iterable) {
    return format(STANDARD_REPRESENTATION, iterable, DEFAULT_START, DEFAULT_END);
  }

  /**
   * Returns the {@code String} representation of the given {@code Iterable}, or {@code null} if the given
   * {@code Iterable} is {@code null}.
   * 
   * 
   * @param representation
   * @param iterable the {@code Iterable} to format.
   * @return the {@code String} representation of the given {@code Iterable}.
   */
  public static String format(Representation representation, Iterable<?> iterable, String start, String end) {
    return singleLineFormat(representation, iterable, start, end);
  }

  public static String singleLineFormat(Representation representation, Iterable<?> iterable, String start, String end) {
    return format(representation, iterable, start, end, ELEMENT_SEPARATOR, ONE_LINE_INDENTATION);
  }

  public static String multiLineFormat(Representation representation, Iterable<?> iterable) {
    return format(representation, iterable, DEFAULT_START, DEFAULT_END, ELEMENT_SEPARATOR_WITH_NEWLINE,
                  INDENTATION_AFTER_NEWLINE);
  }

  private static String format(Representation representation, Iterable<?> iterable, String start, String end,
                               String elementSeparator, String indentation) {
    if (iterable == null) return null;
    Iterator<?> iterator = iterable.iterator();
    if (!iterator.hasNext()) return start + end;
    // iterable has some elements
    StringBuilder desc = new StringBuilder(start);
    boolean firstElement = true;
    while (true) {
      Object element = iterator.next();
      // do not indent first element
      if (firstElement) firstElement = false;
      else desc.append(indentation);
      // add element representation
      desc.append(element == iterable ? "(this Collection)" : representation.toStringOf(element));
      // manage end description
      if (!iterator.hasNext()) return desc.append(end).toString();
      // there are still elements to be describe
      desc.append(elementSeparator);
    }
  }
}
