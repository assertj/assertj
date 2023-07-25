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

final class RotatingQueue_Test {
  @Test
  void should_not_be_able_to_offer_zero_capacity_queue() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(0);
    // WHEN
    boolean actual = rotating.offer(RandomUtils.nextInt());
    // THEN
    then(actual).isFalse();
    then(rotating.isEmpty()).isTrue();
  }

  @Test
  void should_not_be_able_to_add_to_zero_capacity_queue() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(0);
    // WHEN
    IllegalStateException exception = catchThrowableOfType(() -> rotating.add(RandomUtils.nextInt()), IllegalStateException.class);
    // THEN
    then(exception).hasMessageContaining("full");
  }

  static Stream<Arguments> should_rotate_old_elements_source() {
    return Stream.of(
      Arguments.of(1, ImmutableList.of(1), ImmutableList.of(1)),
      Arguments.of(2, ImmutableList.of(1), ImmutableList.of(1)),
      Arguments.of(2, ImmutableList.of(1, 2), ImmutableList.of(1, 2)),
      Arguments.of(2, ImmutableList.of(1, 2, 3), ImmutableList.of(2, 3)),
      Arguments.of(3, ImmutableList.of(1, 2, 3, 4, 5), ImmutableList.of(3, 4, 5)));
  }

  @ParameterizedTest
  @MethodSource("should_rotate_old_elements_source")
  void should_rotate_old_elements_with_offer(
    int capacity, List<Integer> toAdd, List<Integer> expected) {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(capacity);
    // WHEN
    for (int i : toAdd) {
      rotating.offer(i);
    }
    // THEN
    then(rotating).containsExactlyElementsOf(expected);
    then(rotating.size()).isEqualTo(expected.size());
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
    then(rotating.size()).isEqualTo(expected.size());
  }

  @Test
  void should_allow_null() {
    // GIVEN
    Queue<Integer> rotating = new RotatingQueue<>(3);
    // WHEN
    rotating.offer(null);
    // THEN
    then(rotating).containsExactly((Integer) null);
    then(rotating.size()).isEqualTo(1);
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
    Integer first = RandomUtils.nextInt();
    rotating.add(first);
    rotating.add(RandomUtils.nextInt());
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
    Integer first = RandomUtils.nextInt();
    rotating.add(first);
    Integer second = RandomUtils.nextInt();
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
