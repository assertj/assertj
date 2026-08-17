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

import org.junit.jupiter.api.Test;

class ListAssert_isNotEmpty_Test {

  @Test
  void should_pass_with_non_empty_int_stream() {
    // GIVEN
    IntStream actual = IntStream.of(123, 5674, 363);
    // WHEN/THEN
    assertThat(actual).isNotEmpty();
  }

  @Test
  void should_pass_with_non_empty_long_stream() {
    // GIVEN
    LongStream actual = LongStream.of(123L, 5674L, 363L);
    // WHEN/THEN
    assertThat(actual).isNotEmpty();
  }

  @Test
  void should_pass_with_non_empty_double_stream() {
    // GIVEN
    DoubleStream actual = DoubleStream.of(123.3, 5674.5, 363.4);
    // WHEN/THEN
    assertThat(actual).isNotEmpty();
  }

}
