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
package org.assertj.tests.core.data;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.internal.ErrorMessages.offsetValueIsNotPositive;
import static org.assertj.core.internal.ErrorMessages.strictOffsetValueIsNotStrictlyPositive;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link Offset}.
 *
 * @author Alex Ruiz
 */
class Offset_Test {

  @Test
  void should_honor_equals_contract() {
    // WHEN/THEN
    EqualsVerifier.forClass(Offset.class)
                  .verify();
  }

  @Test
  void offset_should_fail_if_value_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> Offset.offset(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class);
  }

  @Test
  void offset_should_fail_if_value_is_negative() {
    // GIVEN
    Number value = -0.1;
    // WHEN
    Throwable thrown = catchThrowable(() -> Offset.offset(value));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(offsetValueIsNotPositive());
  }

  @ParameterizedTest
  @ValueSource(doubles = { 0.0, 0.1 })
  void offset_should_succeed_if_value_is_non_negative(Number value) {
    // WHEN
    Offset<Number> offset = Offset.offset(value);
    // THEN
    then(offset.value).isSameAs(value);
  }

  @Test
  void strict_offset_should_fail_if_value_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> Offset.strictOffset(null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class);
  }

  @ParameterizedTest
  @ValueSource(doubles = { -0.1, 0.0 })
  void strict_offset_should_fail_if_value_is_non_negative(Number value) {
    // WHEN
    Throwable thrown = catchThrowable(() -> Offset.strictOffset(value));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(strictOffsetValueIsNotStrictlyPositive());
  }

  @Test
  void strict_offset_should_succeed_if_value_is_positive() {
    // GIVEN
    Number value = 0.1;
    // WHEN
    Offset<Number> offset = Offset.strictOffset(value);
    // THEN
    then(offset.value).isSameAs(value);
  }

  @Test
  void should_implement_toString() {
    // GIVEN
    Offset<Number> underTest = Offset.offset(0.0);
    // WHEN
    String result = underTest.toString();
    // THEN
    then(result).isEqualTo("Offset[value=0.0]");
  }

  @Test
  void toString_should_describe_strict_offset() {
    // GIVEN
    Offset<Number> underTest = Offset.strictOffset(1.0);
    // WHEN
    String result = underTest.toString();
    // THEN
    then(result).isEqualTo("strict Offset[value=1.0]");
  }

}
