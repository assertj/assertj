/*
 * Created on Apr 29, 2007
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
 * Copyright @2007-2012 the original author or authors.
 */
package org.assertj.core.util;

import static java.util.Collections.emptyList;
import static org.assertj.core.util.ToString.toStringOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility methods related to {@code Collection}s.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public final class Collections {
  /**
   * Returns any duplicate elements from the given {@code Collection}.
   * 
   * @param <T> the generic type of the given {@code Collection}.
   * @param c the given {@code Collection} that might have duplicate elements.
   * @return a {@code Collection} containing the duplicate elements of the given one. If no duplicates are found, an
   *         empty {@code Collection} is returned.
   */
  public static <T> Collection<T> duplicatesFrom(Collection<T> c) {
    Set<T> duplicates = new LinkedHashSet<T>();
    if (isNullOrEmpty(c)) {
      return duplicates;
    }
    Set<T> noDuplicates = new HashSet<T>();
    for (T e : c) {
      if (noDuplicates.contains(e)) {
        duplicates.add(e);
        continue;
      }
      noDuplicates.add(e);
    }
    return duplicates;
  }

  /**
   * Indicates whether the given {@code Collection} is {@code null} or empty.
   * 
   * @param c the given {@code Collection}.
   * @return {@code true} if the given {@code Collection} is {@code null} or empty, otherwise {@code false}.
   */
  public static boolean isNullOrEmpty(Collection<?> c) {
    return c == null || c.isEmpty();
  }

  /**
   * Returns the {@code String} representation of the given {@code Collection}, or {@code null} if the given
   * {@code Collection} is {@code null}.
   * 
   * @param c the {@code Collection} to format.
   * @return the {@code String} representation of the given {@code Collection}.
   */
  public static String format(Collection<?> c) {
    if (c == null) {
      return null;
    }
    Iterator<?> i = c.iterator();
    if (!i.hasNext()) {
      return "[]";
    }
    StringBuilder b = new StringBuilder();
    b.append('[');
    for (;;) {
      Object e = i.next();
      b.append(e == c ? "(this Collection)" : toStringOf(e));
      if (!i.hasNext()) {
        return b.append(']').toString();
      }
      b.append(", ");
    }
  }

  /**
   * Returns all the non-{@code null} elements in the given {@link Collection}.
   * 
   * @param <T> the type of elements of the {@code Collection}.
   * @param c the given {@code Collection}.
   * @return all the non-{@code null} elements in the given {@code Collection}. An empty list is returned if the
   *         given {@code Collection} is {@code null}.
   * @since 1.1.3
   */
  public static <T> List<T> nonNullElementsIn(Collection<T> c) {
    if (isNullOrEmpty(c)) {
      return emptyList();
    }
    List<T> nonNull = new ArrayList<T>();
    for (T element : c) {
      if (element != null) {
        nonNull.add(element);
      }
    }
    return nonNull;
  }

  private Collections() {}
}
