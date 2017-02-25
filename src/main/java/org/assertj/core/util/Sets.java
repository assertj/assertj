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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Utility methods related to {@link Set}s.
 * 
 * @author alruiz
 */
public final class Sets {
  /**
   * Creates a <em>mutable</em> {@code HashSet}.
   * 
   * @param <T> the generic type of the {@code HashSet} to create.
   * @return the created {@code HashSet}.
   */
  public static <T> HashSet<T> newHashSet() {
    return new HashSet<>();
  }

  /**
   * Creates a <em>mutable</em> {@code HashSet} containing the given elements.
   * 
   * @param <T> the generic type of the {@code HashSet} to create.
   * @param elements the elements to store in the {@code HashSet}.
   * @return the created {@code HashSet}, or {@code null} if the given array of elements is {@code null}.
   */
  public static <T> HashSet<T> newHashSet(Iterable<? extends T> elements) {
    if (elements == null) {
      return null;
    }
    HashSet<T> set = newHashSet();
    for (T e : elements) {
      set.add(e);
    }
    return set;
  }

  /**
   * Creates a <em>mutable</em> {@code LinkedHashSet}.
   * 
   * @param <T> the generic type of the {@code LinkedHashSet} to create.
   * @return the created {@code LinkedHashSet}.
   */
  public static <T> LinkedHashSet<T> newLinkedHashSet() {
    return new LinkedHashSet<>();
  }

  /**
   * Creates a <em>mutable</em> {@link LinkedHashSet} containing the given elements.
   * 
   * @param <T> the generic type of the {@code LinkedHashSet} to create.
   * @param elements the elements to store in the {@code LinkedHashSet}.
   * @return the created {@code LinkedHashSet}, or {@code null} if the given array of elements is {@code null}.
   */
  @SafeVarargs
  public static <T> LinkedHashSet<T> newLinkedHashSet(T... elements) {
    if (elements == null) {
      return null;
    }
    LinkedHashSet<T> set = newLinkedHashSet();
    java.util.Collections.addAll(set, elements);
    return set;
  }

  /**
   * Creates a <em>mutable</em> {@link TreeSet}.
   *
   * @param <T> the generic type of the {@link TreeSet} to create.
   * @return the created {@link TreeSet}.
   */
  public static <T> TreeSet<T> newTreeSet() {
    return new TreeSet<>();
  }

  /**
   * Creates a <em>mutable</em> {@link TreeSet} containing the given elements.
   *
   * @param <T> the generic type of the {@link TreeSet} to create.
   * @param elements the elements to store in the {@link TreeSet}.
   * @return the created {@link TreeSet}, or {@code null} if the given array of elements is {@code null}.
   */
  @SafeVarargs
  public static <T> TreeSet<T> newTreeSet(T... elements) {
    if (elements == null) {
      return null;
    }
    TreeSet<T> set = newTreeSet();
    java.util.Collections.addAll(set, elements);
    return set;
  }

  private Sets() {
  }
}
