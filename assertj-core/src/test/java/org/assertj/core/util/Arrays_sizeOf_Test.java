/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.sizeOf;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Arrays_sizeOf_Test {

  @ParameterizedTest
  @MethodSource
  void should_return_array_size(Object value, int expectedSize) {
    // WHEN
    int size = sizeOf(value);
    // THEN
    then(size).isEqualTo(expectedSize);
  }

  static Stream<Arguments> should_return_array_size() {
    return Stream.of(
                     Arguments.of(new int[] { 1, 2 }, 2),
                     Arguments.of(new char[] { 'a', 'b' }, 2),
                     Arguments.of(new byte[] { 1, 2 }, 2),
                     Arguments.of(new long[] { 1, 2, 3 }, 3),
                     Arguments.of(new float[] { 1, 2, 3 }, 3),
                     Arguments.of(new double[] { 1, 2, 3 }, 3),
                     Arguments.of(new String[] { "1", "2", "3" }, 3));
  }

  @ParameterizedTest
  @MethodSource
  void should_throw_an_IllegalArgumentException_if_value_is_not_array(Object value) {
    // WHEN
    Throwable thrown = catchThrowable(() -> sizeOf(value));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("expecting %s to be an array", value);
  }

  static Stream<Object> should_throw_an_IllegalArgumentException_if_value_is_not_array() {
    return Stream.of("not an array", new Object(), new ArrayList<>(), null);
  }
}
