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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.presentation;

import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * Accumulates the values in a stream or iterable, keeping the first and last elements and discarding everything in between.
 */
final class HeadTailAccumulator<T> {
  /** The first elements seen. */
  private final Queue<T> head;

  /** The elements seen most recently, excluding anything already on {@code head}. */
  private final Queue<T> tail;

  /**
   * Creates a new {@link HeadTailAccumulator}.
   *
   * @param headCapacity the maximum number of elements to retain from the start of the stream
   * @param tailCapacity the maximum number of elements to retain from the end of the stream.
   * @throws IllegalArgumentException if either argument is negative
   */
  HeadTailAccumulator(int headCapacity, int tailCapacity) {
    checkArgument(headCapacity >= 0, "head capacity must be non-negative but was %d", headCapacity);
    checkArgument(tailCapacity >= 0, "tail capacity must be non-negative but was %d", tailCapacity);
    this.head = new BoundedQueue<>(headCapacity);
    this.tail = new RotatingQueue<>(tailCapacity);
  }

  /**
   * Adds an element to the accumulator, possibly displacing an older element.
   *
   * @param element the element to add (may be {@code null})
   */
  void add(final T element) {
    if (!head.offer(element)) tail.offer(element);
  }

  /**
   * Converts the accumulated elements into a stream.
   *
   * @return the head and tail concatenated
   */
  Stream<T> stream() {
    List<T> result = new ArrayList<>(head);
    result.addAll(tail);
    return result.stream();
  }
}
