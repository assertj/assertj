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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.util;

/**
 * A comparison function among two different types.
 *
 * @param <T> the type of first objects that may be compared by this comparator
 * @param <U> the type of second objects that may be compared by this comparator
 * @author Alessandro Modolo
 */
@FunctionalInterface
public interface BiComparator<T, U> {

  /**
   * Compares two instance of different types.
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return A negative integer, zero, or a positive integer as the first element is less than, equal to, or greater
   *         than the second.
   * @throws NullPointerException if an argument is null and this comparator does not permit null arguments
   * @throws ClassCastException if the arguments' types prevent them from being compared by this comparator.
   * @see java.util.Comparator
   */
  int compare(T o1, U o2);
}
