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
package org.assertj.core.presentation;

import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class HeadTailAccumulator_Test {
  static Stream<Arguments> should_retain_the_expected_elements_source() {
    return Stream.of(
      // don't keep anything
      Arguments.of(0, 0, ImmutableList.of(1, 2), ImmutableList.of()),

      // head only
      Arguments.of(2, 0, ImmutableList.of(1, 2, 3, 4), ImmutableList.of(1, 2)),

      // tail only
      Arguments.of(0, 2, ImmutableList.of(1, 2, 3, 4), ImmutableList.of(3, 4)),

      // both head and tail
      Arguments.of(1, 1, ImmutableList.of(1, 2, 3), ImmutableList.of(1, 3)),
      Arguments.of(2, 2, ImmutableList.of(1, 2, 3, 4, 5, 6), ImmutableList.of(1, 2, 5, 6)),
      Arguments.of(1, 3, ImmutableList.of(1, 2, 3, 4, 5, 6), ImmutableList.of(1, 4, 5, 6)),

      // keep everything if the sum of the capacities is at least the number of elements
      Arguments.of(3, 3, ImmutableList.of(1, 2, 3, 4, 5, 6), ImmutableList.of(1, 2, 3, 4, 5, 6)),
      Arguments.of(2, 6, ImmutableList.of(1, 2, 3, 4, 5, 6), ImmutableList.of(1, 2, 3, 4, 5, 6)));
  }

  @ParameterizedTest
  @MethodSource("should_retain_the_expected_elements_source")
  void should_retain_the_expected_elements(
    int headCapacity, int tailCapacity, List<Integer> toAdd, List<Integer> expected) {
    // GIVEN
    HeadTailAccumulator<Integer> accumulator =
      new HeadTailAccumulator<>(headCapacity, tailCapacity);
    // WHEN
    for (int i : toAdd) {
      accumulator.add(i);
    }
    // THEN
    then(accumulator.toList()).containsExactlyElementsOf(expected);
  }

  @Test
  void should_allow_null() {
    // GIVEN
    HeadTailAccumulator<Integer> accumulator =
      new HeadTailAccumulator<>(1, 1);
    // WHEN
    accumulator.add(null);
    accumulator.add(null);
    // THEN
    then(accumulator.toList()).containsExactly(null, null);
  }

  @Test
  void should_not_be_able_to_be_created_with_a_negative_head_capacity() {
    // WHEN
    IllegalArgumentException exception =
      catchIllegalArgumentException(() -> new HeadTailAccumulator<>(-1, 1));
    // THEN
    then(exception).hasMessageContainingAll("negative", "head", "-1");
  }

  @Test
  void should_not_be_able_to_be_created_with_a_negative_tail_capacity() {
    // WHEN
    IllegalArgumentException exception =
      catchIllegalArgumentException(() -> new HeadTailAccumulator<>(1, -1));
    // THEN
    then(exception).hasMessageContainingAll("negative", "tail", "-1");
  }
}
