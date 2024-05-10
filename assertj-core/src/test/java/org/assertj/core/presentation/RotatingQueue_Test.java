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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.presentation;

import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.ImmutableList;

final class RotatingQueue_Test {

  private final Random random = new Random();

  @Test
  void should_not_be_able_to_offer_zero_capacity_queue() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(0);
    // WHEN
    boolean actual = rotating.offer(random.nextInt());
    // THEN
    then(actual).isFalse();
    then(rotating.isEmpty()).isTrue();
  }

  @Test
  void should_not_be_able_to_add_to_zero_capacity_queue() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(0);
    // WHEN
    IllegalStateException exception = catchThrowableOfType(() -> rotating.add(random.nextInt()),
                                                           IllegalStateException.class);
    // THEN
    then(exception).hasMessageContaining("full");
  }

  @ParameterizedTest
  @MethodSource("should_rotate_old_elements_source")
  void should_rotate_old_elements_with_offer(
                                             int capacity, List<Integer> toAdd, List<Integer> expected) {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(capacity);
    // WHEN
    for (int i : toAdd)
      rotating.offer(i);
    // THEN
    then(rotating).containsExactlyElementsOf(expected);
  }

  @ParameterizedTest
  @MethodSource("should_rotate_old_elements_source")
  void should_rotate_old_elements_with_add(
                                           int capacity, List<Integer> toAdd, List<Integer> expected) {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(capacity);
    // WHEN
    for (int i : toAdd) {
      rotating.add(i);
    }
    // THEN
    then(rotating).containsExactlyElementsOf(expected);
  }

  static Stream<Arguments> should_rotate_old_elements_source() {
    return Stream.of(Arguments.of(1, ImmutableList.of(1), ImmutableList.of(1)),
                     Arguments.of(2, ImmutableList.of(1), ImmutableList.of(1)),
                     Arguments.of(2, ImmutableList.of(1, 2), ImmutableList.of(1, 2)),
                     Arguments.of(2, ImmutableList.of(1, 2, 3), ImmutableList.of(2, 3)),
                     Arguments.of(3, ImmutableList.of(1, 2, 3, 4, 5), ImmutableList.of(3, 4, 5)));
  }

  @Test
  void should_allow_null() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(3);
    // WHEN
    rotating.offer(null);
    // THEN
    then(rotating).containsExactly((Integer) null);
  }

  @Test
  void should_be_able_to_peek_at_empty_queue() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(1);
    // WHEN
    Integer actual = rotating.peek();
    // THEN
    then(actual).isNull();
  }

  @Test
  void should_be_able_to_peek_at_non_empty_queue() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(3);
    Integer first = random.nextInt();
    rotating.add(first);
    rotating.add(random.nextInt());
    // WHEN
    Integer actual = rotating.peek();
    // THEN
    then(actual).isEqualTo(first);
  }

  @Test
  void should_be_able_to_poll_empty_queue() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(1);
    // WHEN
    Integer actual = rotating.poll();
    // THEN
    then(actual).isNull();
  }

  @Test
  void should_be_able_to_poll_non_empty_queue() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(3);
    Integer first = random.nextInt();
    rotating.add(first);
    Integer second = random.nextInt();
    rotating.add(second);
    // WHEN
    Integer actual = rotating.poll();
    // THEN
    then(actual).isEqualTo(first);
    then(rotating).containsExactly(second);
  }

  @Test
  void should_not_be_able_to_be_created_with_a_negative_capacity() {
    // WHEN
    IllegalArgumentException exception = catchIllegalArgumentException(() -> new RotatingQueue<>(-1));
    // THEN
    then(exception).hasMessageContainingAll("non-negative", "-1");
  }
}
