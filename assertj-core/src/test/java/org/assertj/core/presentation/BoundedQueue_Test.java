package org.assertj.core.presentation;

import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class BoundedQueue_Test {
  static Stream<Arguments> should_only_be_able_to_offer_up_to_capacity_source() {
    return Stream.of(
      Arguments.of(0, ImmutableList.of(1), ImmutableList.of()),
      Arguments.of(1, ImmutableList.of(1), ImmutableList.of(1)),
      Arguments.of(2, ImmutableList.of(1), ImmutableList.of(1)),
      Arguments.of(2, ImmutableList.of(1, 2), ImmutableList.of(1, 2)),
      Arguments.of(2, ImmutableList.of(1, 2, 3), ImmutableList.of(1, 2)),
      Arguments.of(3, ImmutableList.of(1, 2, 3, 4, 5), ImmutableList.of(1, 2, 3)));
  }

  @ParameterizedTest
  @MethodSource("should_only_be_able_to_offer_up_to_capacity_source")
  void should_only_be_able_to_offer_up_to_capacity(
    int capacity, List<Integer> toAdd, List<Integer> expected) {
    // GIVEN
    Queue<Integer> bounded = new BoundedQueue<>(capacity);
    // WHEN
    for (int i : toAdd) {
      bounded.offer(i);
    }
    // THEN
    then(bounded).containsExactlyElementsOf(expected);
    then(bounded.size()).isEqualTo(expected.size());
  }

  @ParameterizedTest
  @MethodSource("should_only_be_able_to_offer_up_to_capacity_source")
  void should_only_be_able_to_add_up_to_capacity(
    int capacity, List<Integer> toAdd, List<Integer> expected) {
    // GIVEN
    Queue<Integer> bounded = new BoundedQueue<>(capacity);
    // WHEN
    for (int i : toAdd) {
      try {
        bounded.add(i);
      } catch (IllegalStateException e) {
        // expected if the queue is full
      }
    }
    // THEN
    then(bounded).containsExactlyElementsOf(expected);
    then(bounded.size()).isEqualTo(expected.size());
  }

  @Test
  void should_allow_null() {
    // GIVEN
    Queue<Integer> bounded = new BoundedQueue<>(3);
    // WHEN
    bounded.offer(null);
    // THEN
    then(bounded).containsExactly((Integer) null);
    then(bounded.size()).isEqualTo(1);
  }

  @Test
  void should_not_be_able_to_add_over_capacity() {
    // GIVEN
    Queue<Integer> bounded = new BoundedQueue<>(1);
    bounded.add(RandomUtils.nextInt());
    // WHEN
    IllegalStateException exception =
      catchThrowableOfType(() -> bounded.add(RandomUtils.nextInt()), IllegalStateException.class);
    // THEN
    then(exception).hasMessageContaining("full");
    then(bounded.size()).isEqualTo(1);
  }

  @Test
  void should_be_able_to_peek_at_empty_queue() {
    // GIVEN
    Queue<Integer> bounded = new BoundedQueue<>(1);
    // WHEN
    Integer actual = bounded.peek();
    // THEN
    then(actual).isNull();
  }

  @Test
  void should_be_able_to_peek_at_non_empty_queue() {
    // GIVEN
    Queue<Integer> bounded = new BoundedQueue<>(3);
    Integer first = RandomUtils.nextInt();
    bounded.add(first);
    bounded.add(RandomUtils.nextInt());
    // WHEN
    Integer actual = bounded.peek();
    // THEN
    then(actual).isEqualTo(first);
  }

  @Test
  void should_be_able_to_poll_empty_queue() {
    // GIVEN
    Queue<Integer> bounded = new BoundedQueue<>(1);
    // WHEN
    Integer actual = bounded.poll();
    // THEN
    then(actual).isNull();
  }

  @Test
  void should_be_able_to_poll_non_empty_queue() {
    // GIVEN
    Queue<Integer> bounded = new BoundedQueue<>(3);
    Integer first = RandomUtils.nextInt();
    bounded.add(first);
    Integer second = RandomUtils.nextInt();
    bounded.add(second);
    // WHEN
    Integer actual = bounded.poll();
    // THEN
    then(actual).isEqualTo(first);
    then(bounded).containsExactly(second);
  }

  @Test
  void should_not_be_able_to_be_created_with_a_negative_capacity() {
    // WHEN
    IllegalArgumentException exception = catchIllegalArgumentException(() -> new BoundedQueue<>(-1));
    // THEN
    then(exception).hasMessageContainingAll("negative", "-1");
  }
}
