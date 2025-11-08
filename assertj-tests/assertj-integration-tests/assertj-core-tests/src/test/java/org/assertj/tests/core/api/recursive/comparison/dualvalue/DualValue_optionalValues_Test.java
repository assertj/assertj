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
package org.assertj.tests.core.api.recursive.comparison.dualvalue;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DualValue_optionalValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @Test
  void isActualAnOptional_should_return_true_when_actual_is_an_optional() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Optional.empty(), "abc");
    // WHEN
    boolean actualIsAnOptional = dualValue.isActualAnOptional();
    // THEN
    then(actualIsAnOptional).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isActualAnOptional_should_return_false_when_actual_is_not_an_optional(Object actualField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), actualField);
    // WHEN
    boolean actualIsAnOptional = dualValue.isActualAnOptional();
    // THEN
    then(actualIsAnOptional).isFalse();
  }

  @Test
  void isActualAnOptionalInt_should_return_true_when_actual_is_an_optionalInt() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, OptionalInt.empty(), "abc");
    // WHEN
    boolean actualIsAnOptionalInt = dualValue.isActualAnOptionalInt();
    // THEN
    then(actualIsAnOptionalInt).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isActualAnOptionalInt_should_return_false_when_actual_is_not_an_optionalInt(Object actualField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), actualField);
    // WHEN
    boolean actualIsAnOptionalInt = dualValue.isActualAnOptionalInt();
    // THEN
    then(actualIsAnOptionalInt).isFalse();
  }

  @Test
  void isActualAnOptionalLong_should_return_true_when_actual_is_an_optionalLong() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, OptionalLong.empty(), "abc");
    // WHEN
    boolean actualIsAnOptionalLong = dualValue.isActualAnOptionalLong();
    // THEN
    then(actualIsAnOptionalLong).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isActualAnOptionalLong_should_return_false_when_actual_is_not_an_optionalLong(Object actualField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), actualField);
    // WHEN
    boolean actualIsAnOptionalLong = dualValue.isActualAnOptionalLong();
    // THEN
    then(actualIsAnOptionalLong).isFalse();
  }

  @Test
  void isActualAnOptionalDouble_should_return_true_when_actual_is_an_optionalDouble() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, OptionalDouble.empty(), "abc");
    // WHEN
    boolean actualIsAnOptionalDouble = dualValue.isActualAnOptionalDouble();
    // THEN
    then(actualIsAnOptionalDouble).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isActualAnOptionalDouble_should_return_false_when_actual_is_not_an_optionalDouble(Object actualField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), actualField);
    // WHEN
    boolean actualIsAnOptionalDouble = dualValue.isActualAnOptionalDouble();
    // THEN
    then(actualIsAnOptionalDouble).isFalse();
  }

  @Test
  void isExpectedAnOptional_should_return_true_when_expected_is_an_optional() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "abc", Optional.of(""));
    // WHEN
    boolean expectedIsOptional = dualValue.isExpectedAnOptional();
    // THEN
    then(expectedIsOptional).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  void isExpectedAnOptional_should_return_false_when_expected_is_not_an_optional(Object expectedField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), expectedField);
    // WHEN
    boolean expectedIsAnOptional = dualValue.isExpectedAnOptional();
    // THEN
    then(expectedIsAnOptional).isFalse();
  }

  private static Stream<Object> nonOptional() {
    return Stream.of(123, null, "abc");
  }

  @ParameterizedTest
  @MethodSource("emptyOptionals")
  void isActualAnEmptyOptionalOfAnyType_should_return_true_when_actual_is_an_empty_optional(Object optional) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, optional, "abc");
    // WHEN
    boolean actualIsAnOptional = dualValue.isActualAnEmptyOptionalOfAnyType();
    // THEN
    then(actualIsAnOptional).isTrue();
  }

  @ParameterizedTest
  @MethodSource("populatedOptionals")
  void isActualAnEmptyOptionalOfAnyType_should_return_false_when_actual_is_a_populated_optional(Object optional) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, optional, "abc");
    // WHEN
    boolean actualIsAnOptional = dualValue.isActualAnEmptyOptionalOfAnyType();
    // THEN
    then(actualIsAnOptional).isFalse();
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
