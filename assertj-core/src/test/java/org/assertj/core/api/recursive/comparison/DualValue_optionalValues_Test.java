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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DualValue_optionalValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @Test
  void isActualFieldAnOptional_should_return_true_when_actual_is_an_optional() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Optional.empty(), "abc");
    // WHEN
    boolean actualFieldIsOptional = dualValue.isActualFieldAnOptional();
    // THEN
    assertThat(actualFieldIsOptional).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isActualFieldAnOptional_should_return_false_when_actual_is_not_an_optional(Object actualField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), actualField);
    // WHEN
    boolean actualFieldIsOptional = dualValue.isActualFieldAnOptional();
    // THEN
    assertThat(actualFieldIsOptional).isFalse();
  }

  @Test
  void isActualFieldAnOptionalInt_should_return_true_when_actual_is_an_optionalInt() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, OptionalInt.empty(), "abc");
    // WHEN
    boolean actualFieldIsOptionalInt = dualValue.isActualFieldAnOptionalInt();
    // THEN
    assertThat(actualFieldIsOptionalInt).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isActualFieldAnOptionalInt_should_return_false_when_actual_is_not_an_optionalInt(Object actualField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), actualField);
    // WHEN
    boolean actualFieldIsOptionalInt = dualValue.isActualFieldAnOptionalInt();
    // THEN
    assertThat(actualFieldIsOptionalInt).isFalse();
  }

  @Test
  void isActualFieldAnOptionalLong_should_return_true_when_actual_is_an_optionalLong() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, OptionalLong.empty(), "abc");
    // WHEN
    boolean actualFieldIsOptionalLong = dualValue.isActualFieldAnOptionalLong();
    // THEN
    assertThat(actualFieldIsOptionalLong).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isActualFieldAnOptionalLong_should_return_false_when_actual_is_not_an_optionalLong(Object actualField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), actualField);
    // WHEN
    boolean actualFieldIsOptionalLong = dualValue.isActualFieldAnOptionalLong();
    // THEN
    assertThat(actualFieldIsOptionalLong).isFalse();
  }

  @Test
  void isActualFieldAnOptionalDouble_should_return_true_when_actual_is_an_optionalDouble() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, OptionalDouble.empty(), "abc");
    // WHEN
    boolean actualFieldIsOptionalDouble = dualValue.isActualFieldAnOptionalDouble();
    // THEN
    assertThat(actualFieldIsOptionalDouble).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isActualFieldAnOptionalDouble_should_return_false_when_actual_is_not_an_optionalDouble(Object actualField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), actualField);
    // WHEN
    boolean actualFieldIsOptionalDouble = dualValue.isActualFieldAnOptionalDouble();
    // THEN
    assertThat(actualFieldIsOptionalDouble).isFalse();
  }

  @Test
  void isExpectedFieldAnOptional_should_return_true_when_expected_is_an_optional() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "abc", Optional.of(""));
    // WHEN
    boolean expectedFieldIsOptional = dualValue.isExpectedFieldAnOptional();
    // THEN
    assertThat(expectedFieldIsOptional).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isExpectedFieldAnOptional_should_return_false_when_expected_is_not_an_optional(Object expectedField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), expectedField);
    // WHEN
    boolean expectedFieldIsOptional = dualValue.isExpectedFieldAnOptional();
    // THEN
    assertThat(expectedFieldIsOptional).isFalse();
  }

  private static Stream<Object> nonOptional() {
    return Stream.of(123, null, "abc");
  }

  @ParameterizedTest
  @MethodSource("emptyOptionals")
  void isActualFieldAnEmptyOptionalOfAnyType_should_return_true_when_actual_is_an_empty_optional(Object optional) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, optional, "abc");
    // WHEN
    boolean actualFieldIsOptional = dualValue.isActualFieldAnEmptyOptionalOfAnyType();
    // THEN
    assertThat(actualFieldIsOptional).isTrue();
  }

  @ParameterizedTest
  @MethodSource("populatedOptionals")
  void isActualFieldAnEmptyOptionalOfAnyType_should_return_false_when_actual_is_a_populated_optional(Object optional) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, optional, "abc");
    // WHEN
    boolean actualFieldIsOptional = dualValue.isActualFieldAnEmptyOptionalOfAnyType();
    // THEN
    assertThat(actualFieldIsOptional).isFalse();
  }

  private static Stream<Object> populatedOptionals() {
    return Stream.of(Optional.of("123"),
                     OptionalInt.of(123),
                     OptionalLong.of(123L),
                     OptionalDouble.of(123.0));
  }

  private static Stream<Object> emptyOptionals() {
    return Stream.of(Optional.empty(),
                     OptionalInt.empty(),
                     OptionalLong.empty(),
                     OptionalDouble.empty());
  }
}
