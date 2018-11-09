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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.ErrorMessagesForTest.shouldBeEqualMessage;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.core.presentation.BinaryRepresentation#toStringOf(Object)}.
 *
 * @author Mariusz Smykula
 */
public class Assertions_assertThat_inBinary_Test {

  @Test
  public void should_assert_byte_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((byte) 2).inBinary()
                                                                                         .isEqualTo((byte) 3))
                                                   .withMessage(shouldBeEqualMessage("0b00000011", "0b00000010"));
  }

  @Test
  public void should_assert_signed_byte_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((byte) -2).inBinary()
                                                                                          .isEqualTo((byte) 3))
                                                   .withMessage(shouldBeEqualMessage("0b00000011", "0b11111110"));
  }

  @Test
  public void should_assert_bytes_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new byte[] { 2, 3 }).inBinary()
                                                                                                    .isEqualTo(new byte[] { 1 }))
                                                   .withMessage(shouldBeEqualMessage("[0b00000001]", "[0b00000010, 0b00000011]"));
  }

  @Test
  public void should_assert_short_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((short) 2).inBinary()
                                                                                          .isEqualTo((short) 3))
                                                   .withMessage(shouldBeEqualMessage("0b00000000_00000011",
                                                                                     "0b00000000_00000010"));
  }

  @Test
  public void should_assert_signed_short_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((short) -1).inBinary()
                                                                                           .isEqualTo((short) 3))
                                                   .withMessage(shouldBeEqualMessage("0b00000000_00000011",
                                                                                     "0b11111111_11111111"));
  }

  @Test
  public void should_assert_integer_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(2).inBinary().isEqualTo(3))
                                                   .withMessage(shouldBeEqualMessage("0b00000000_00000000_00000000_00000011",
                                                                                     "0b00000000_00000000_00000000_00000010"));
  }

  @Test
  public void should_assert_negative_integer_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(3).inBinary().isEqualTo(-3))
                                                   .withMessage(shouldBeEqualMessage("0b11111111_11111111_11111111_11111101",
                                                                                     "0b00000000_00000000_00000000_00000011"));
  }

  @Test
  public void should_assert_long_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((long) 2).inBinary().isEqualTo(3))
                                                   .withMessage(shouldBeEqualMessage("0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000011",
                                                                                     "0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000010"));
  }

  @Test
  public void should_assert_negative_long_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((long) -2).inBinary().isEqualTo(3))
                                                   .withMessage(shouldBeEqualMessage("0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000011",
                                                                                     "0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111110"));
  }

  @Test
  public void should_assert_float_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(2.1f).inBinary().isEqualTo(3f))
                                                   .withMessage(shouldBeEqualMessage("0b01000000_01000000_00000000_00000000",
                                                                                     "0b01000000_00000110_01100110_01100110"));
  }

  @Test
  public void should_assert_double_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(2.1d).inBinary().isEqualTo(3d))
                                                   .withMessage(shouldBeEqualMessage("0b01000000_00001000_00000000_00000000_00000000_00000000_00000000_00000000",
                                                                                     "0b01000000_00000000_11001100_11001100_11001100_11001100_11001100_11001101"));
  }

  @Test
  public void should_assert_String_in_binary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat("ab").inBinary().isEqualTo("a6"))
                                                   .withMessage(shouldBeEqualMessage("\"['0b00000000_01100001', '0b00000000_00110110']\"",
                                                                                     "\"['0b00000000_01100001', '0b00000000_01100010']\""));
  }

}
