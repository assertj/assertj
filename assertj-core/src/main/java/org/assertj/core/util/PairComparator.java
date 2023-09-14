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
import java.util.Objects;

/**
 * A comparator for {@link Pair}, allowing comparison based on the left and right components.
 *
 * @param <L> Type of the left component
 * @param <R> Type of the right component
 * @author Alessandro Modolo
 */
public class PairComparator<L, R> extends NullSafeComparator<Pair<L, R>> {
  private static final int NOT_EQUAL = -1;

  private final Comparator<? super L> leftComparator;
  private final Comparator<? super R> rightComparator;

  /**
   * Constructs a PairComparator with the specified comparators for left and right components.
   *
   * @param leftComparator  Comparator for the left component
   * @param rightComparator Comparator for the right component
   */
  public PairComparator(Comparator<? super L> leftComparator, Comparator<? super R> rightComparator) {
    this.leftComparator = leftComparator;
    this.rightComparator = rightComparator;
  }

  @Override
  protected int compareNonNull(Pair<L, R> o1, Pair<L, R> o2) {
    int result = compare(o1.left(), o2.left(), leftComparator);
    return result == 0 ? compare(o1.right(), o2.right(), rightComparator) : result;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private <K> int compare(K o1, K o2, Comparator<K> comparator) {
    if (o1 == o2) return 0;
    if (o1 == null) return -1;
    if (o2 == null) return 1;

    if (comparator != null) return comparator.compare(o1, o2);
    else if (o1 instanceof Comparable) return ((Comparable) o1).compareTo(o2);
    else return o1.equals(o2) ? 0 : NOT_EQUAL;
  }
}
