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

import java.util.Comparator;

/**
 * A comparator for {@link DualClass}, allowing comparison based on class of the actual and expected components.
 *
 * @author Alessandro Modolo
 */
public class DualClassComparator extends NullSafeComparator<DualClass<?, ?>> {
  private static final int NOT_EQUAL = -1;

  private final Comparator<Class<?>> actualComparator;
  private final Comparator<Class<?>> expectedComparator;

  /**
   * Constructs a {@link DualClassComparator} with the specified comparators for actual and expected components class.
   *
   * @param actualComparator  Comparator for the actual component
   * @param expectedComparator Comparator for the expected component
   */
  public DualClassComparator(Comparator<Class<?>> actualComparator, Comparator<Class<?>> expectedComparator) {
    this.actualComparator = actualComparator;
    this.expectedComparator = expectedComparator;
  }

  @Override
  protected int compareNonNull(DualClass<?, ?> o1, DualClass<?, ?> o2) {
    int result = compare(o1.actual(), o2.actual(), actualComparator);
    return result == 0 ? compare(o1.expected(), o2.expected(), expectedComparator) : result;
  }

  private int compare(Class<?> o1, Class<?> o2, Comparator<Class<?>> comparator) {
    if (o1 == o2) return 0;
    if (o1 == null) return -1;
    if (o2 == null) return 1;

    return comparator != null ? comparator.compare(o1, o2) : NOT_EQUAL;
  }
}
