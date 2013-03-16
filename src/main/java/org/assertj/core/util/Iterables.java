/*
 * Created on Aug 23, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.assertj.core.util;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Utility methods related to {@code Iterable}s.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
// TODO(alexRuiz): Get rid of this class.
public final class Iterables {
  /**
   * Indicates whether the given {@link Iterable} is {@code null} or empty.
   * 
   * @param iterable the given {@code Iterable} to check.
   * @return {@code true} if the given {@code Iterable} is {@code null} or empty, otherwise {@code false}.
   */
  public static boolean isNullOrEmpty(Iterable<?> iterable) {
    if (iterable == null) {
      return true;
    }
    if (iterable instanceof Collection && ((Collection<?>) iterable).isEmpty()) {
      return true;
    }
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
    if (iterable == null) {
      throw new NullPointerException("Iterable must not be null");
    }
    if (iterable instanceof Collection) {
      return ((Collection<?>) iterable).size();
    }
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
   *        {@code Iterable} is {@code null}.
   * @since 1.1.3
   */
  public static <T> List<T> nonNullElementsIn(Iterable<T> i) {
    if (isNullOrEmpty(i)) {
      return emptyList();
    }
    List<T> nonNull = new ArrayList<T>();
    for (T element : i) {
      if (element != null) {
        nonNull.add(element);
      }
    }
    return nonNull;
  }

  private Iterables() {}
}
