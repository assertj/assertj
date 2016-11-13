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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.internal;

import java.util.Comparator;
import java.util.Map;

import static org.assertj.core.internal.ComparatorBasedComparisonStrategy.NOT_EQUAL;
import static org.assertj.core.util.Objects.areEqual;

/**
 * Compares objects by passed or standard default comparator extended with comparators by type.
 */
public class ExtendedByTypeComparator implements Comparator<Object> {

  private final Comparator<Object> comparator;
  private final Map<Class<?>, Comparator<?>> comparatorsByType;

  public ExtendedByTypeComparator(Map<Class<?>, Comparator<?>> comparatorsByType) {
    this(
      new Comparator<Object>() {
        @Override
        public int compare(Object actual, Object other) {
          return areEqual(actual, other) ? 0 : NOT_EQUAL;
        }
      }, comparatorsByType);
  }

  public ExtendedByTypeComparator(Comparator<Object> comparator, Map<Class<?>, Comparator<?>> comparatorsByType) {
    this.comparator = comparator;
    this.comparatorsByType = comparatorsByType;
  }

  @Override
  @SuppressWarnings("unchecked")
  public int compare(Object actual, Object other) {
    // value returned is not relevant for ordering if objects are not equal.
    if (actual == null && other == null) return 0;
    if (actual == null || other == null) return NOT_EQUAL;

    Comparator comparatorByType = comparatorsByType == null ? null : comparatorsByType.get(actual.getClass());
    if (comparatorByType != null) {
      return other.getClass().isInstance(actual) ? comparatorByType.compare(actual, other) : NOT_EQUAL;
    }

    return comparator.compare(actual, other);
  }

  public Comparator<Object> getComparator() {
    return comparator;
  }

  @Override
  public String toString() {
    return "extended " + comparator.toString() + " for types " + comparatorsByType.keySet();
  }
}
