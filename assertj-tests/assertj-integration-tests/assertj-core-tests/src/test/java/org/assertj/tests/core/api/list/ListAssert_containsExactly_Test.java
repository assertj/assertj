/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class ListAssert_containsExactly_Test {

  @Test
  void should_pass_when_asserting_stream_multiple_times() {
    // GIVEN
    Stream<String> actual = Stream.of("Luke", "Leia");
    // WHEN/THEN
    assertThat(actual).containsExactly("Luke", "Leia")
                      .containsExactly("Luke", "Leia");
  }

  @Test
  void should_pass_when_asserting_int_stream_multiple_times() {
    // GIVEN
    IntStream actual = IntStream.of(823952, 1947230585);
    // WHEN/THEN
    assertThat(actual).containsExactly(823952, 1947230585)
                      .containsExactly(823952, 1947230585);
  }

  @Test
  void should_pass_when_asserting_long_stream_multiple_times() {
    // GIVEN
    LongStream actual = LongStream.of(823952L, 1947230585L);
    // WHEN/THEN
    assertThat(actual).containsExactly(823952L, 1947230585L)
                      .containsExactly(823952L, 1947230585L);
  }

  @Test
  void should_pass_when_asserting_double_stream_multiple_times() {
    // GIVEN
    DoubleStream actual = DoubleStream.of(823952.8, 1947230585.9);
    // WHEN/THEN
    assertThat(actual).containsExactly(823952.8, 1947230585.9)
                      .containsExactly(823952.8, 1947230585.9);
  }

}
