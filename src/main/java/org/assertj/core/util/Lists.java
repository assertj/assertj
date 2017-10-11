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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Utility methods related to {@code java.util.List}s.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public final class Lists {
  /**
   * Creates a <em>mutable</em> {@link ArrayList} containing the given elements.
   * 
   * @param <T> the generic type of the {@code ArrayList} to create.
   * @param elements the elements to store in the {@code ArrayList}.
   * @return the created {@code ArrayList}, of {@code null} if the given array of elements is {@code null}.
   */
  @SafeVarargs
  public static <T> ArrayList<T> newArrayList(T... elements) {
    if (elements == null) {
      return null;
    }
    ArrayList<T> list = newArrayList();
    java.util.Collections.addAll(list, elements);
    return list;
  }

  /**
   * Creates a <em>mutable</em> {@link ArrayList} containing the given elements.
   * 
   * @param <T> the generic type of the {@code ArrayList} to create.
   * @param elements the elements to store in the {@code ArrayList}.
   * @return the created {@code ArrayList}, or {@code null} if the given {@code Iterable} is {@code null}.
   */
  public static <T> ArrayList<T> newArrayList(Iterable<? extends T> elements) {
    if (elements == null) {
      return null;
    }
    ArrayList<T> list = newArrayList();
    for (T e : elements) {
      list.add(e);
    }
    return list;
  }

  /**
   * Creates a <em>mutable</em> {@link ArrayList} containing the given elements.
   * 
   * @param <T> the generic type of the {@code ArrayList} to create.
   * @param elements the elements to store in the {@code ArrayList}.
   * @return the created {@code ArrayList}, or {@code null} if the given {@code Iterator} is {@code null}.
   */
  public static <T> ArrayList<T> newArrayList(Iterator<? extends T> elements) {
    if (elements == null) {
      return null;
    }
    ArrayList<T> list = newArrayList();
    while (elements.hasNext()) {
      list.add(elements.next());
    }
    return list;
  }

  /**
   * Creates a <em>mutable</em> {@link ArrayList}.
   * 
   * @param <T> the generic type of the {@code ArrayList} to create.
   * @return the created {@code ArrayList}, of {@code null} if the given array of elements is {@code null}.
   */
  public static <T> ArrayList<T> newArrayList() {
    return new ArrayList<>();
  }

  /**
   * @param <T> the generic type of the {@code List}.
   * @return an empty, <em>immutable</em> {@code List}.
   */
  public static <T> List<T> emptyList() {
    return Collections.emptyList();
  }

  private Lists() {
  }
}
