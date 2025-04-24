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

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/** A queue with a fixed maximum size. Once the queue is full, no more elements can be added until an element is removed. */
final class BoundedQueue<T> extends AbstractQueue<T> {
  /** Queue that never holds more than {@code capacity} elements. */
  private final Queue<T> data;

  /** The maximum number of elements that can be present. */
  private final int capacity;

  /**
   * Creates a new {@link BoundedQueue}.
   *
   * @param capacity the maximum number of elements the queue can hold
   * @throws IllegalArgumentException if the capacity is negative
   */
  BoundedQueue(int capacity) {
    checkArgument(capacity >= 0, "capacity must be non-negative but was %d", capacity);
    this.capacity = capacity;
    this.data = new LinkedList<>();
  }

  @Override
  public Iterator<T> iterator() {
    return data.iterator();
  }

  @Override
  public int size() {
    return data.size();
  }

  @Override
  public boolean offer(T element) {
    return data.size() < capacity && data.offer(element);
  }

  @Override
  public T poll() {
    return data.poll();
  }

  @Override
  public T peek() {
    return data.peek();
  }
}
