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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.core.presentation.BinaryRepresentation#toStringOf(Object)}.
 *
 * @author Mariusz Smykula
 */
class Assertions_assertThat_inBinary_Test {

  @Test
  void should_assert_byte_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat((byte) 2).inBinary().isEqualTo((byte) 3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b00000010", "0b00000011"));
  }

  @Test
  void should_assert_signed_byte_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat((byte) -2).inBinary().isEqualTo((byte) 3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b11111110", "0b00000011"));
  }

  @Test
  void should_assert_bytes_in_binary() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(new byte[] { 2, 3 }).inBinary().isEqualTo(new byte[] { 1 }));
    // THEN
    then(error).hasMessage(shouldBeEqualMessage("[0b00000010, 0b00000011]", "[0b00000001]"));
  }

  @Test
  void should_assert_short_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat((short) 2).inBinary().isEqualTo((short) 3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b00000000_00000010",
                                                         "0b00000000_00000011"));
  }

  @Test
  void should_assert_signed_short_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat((short) -1).inBinary().isEqualTo((short) 3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b11111111_11111111",
                                                         "0b00000000_00000011"));
  }

  @Test
  void should_assert_integer_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(2).inBinary().isEqualTo(3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b00000000_00000000_00000000_00000010",
                                                         "0b00000000_00000000_00000000_00000011"));
  }

  @Test
  void should_assert_negative_integer_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(3).inBinary().isEqualTo(-3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b00000000_00000000_00000000_00000011",
                                                         "0b11111111_11111111_11111111_11111101"));
  }

  @Test
  void should_assert_long_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat((long) 2).inBinary().isEqualTo(3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000010",
                                                         "0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000011"));
  }

  @Test
  void should_assert_negative_long_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat((long) -2).inBinary().isEqualTo(3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111110",
                                                         "0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000011"));
  }

  @Test
  void should_assert_float_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(2.1f).inBinary().isEqualTo(3f));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b01000000_00000110_01100110_01100110",
                                                         "0b01000000_01000000_00000000_00000000"));
  }

  @Test
  void should_assert_double_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(2.1d).inBinary().isEqualTo(3d));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0b01000000_00000000_11001100_11001100_11001100_11001100_11001100_11001101",
                                                         "0b01000000_00001000_00000000_00000000_00000000_00000000_00000000_00000000"));
  }

  @Test
  void should_assert_String_in_binary() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("ab").inBinary().isEqualTo("a6"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("\"['0b00000000_01100001', '0b00000000_01100010']\"",
                                                         "\"['0b00000000_01100001', '0b00000000_00110110']\""));
  }

}
