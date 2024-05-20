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
import static org.assertj.core.util.Arrays.isArrayEmpty;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class Arrays_isArrayEmpty_Test {

  @ParameterizedTest
  @MethodSource
  void should_return_true_if_value_is_an_empty_array(Object value) {
    // WHEN
    boolean isArrayEmpty = isArrayEmpty(value);
    // THEN
    then(isArrayEmpty).as("%s should be an empty array ", value).isTrue();
  }

  static Stream<Object> should_return_true_if_value_is_an_empty_array() {
    // junit fails if given an empty object array
    return Stream.of(new int[0], new long[0], new char[0], new byte[0], new float[0], new double[0], new char[0]);
  }

  @Test
  void should_return_true_if_value_is_an_empty_object_array() {
    // WHEN
    boolean isArrayEmpty = isArrayEmpty(new Object[0]);
    // THEN
    then(isArrayEmpty).as("should be an empty object array").isTrue();
  }

  @ParameterizedTest
  @MethodSource
  void should_throw_an_IllegalArgumentException_if_value_is_not_array(Object value) {
    // WHEN
    Throwable thrown = catchThrowable(() -> isArrayEmpty(value));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("expecting %s to be an array", value);
  }

  static Stream<Object> should_throw_an_IllegalArgumentException_if_value_is_not_array() {
    return Stream.of("not an array", new Object(), new ArrayList<>(), null);
  }
}
