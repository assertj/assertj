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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.Lists.list;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.core.presentation.HexadecimalRepresentation#toStringOf(Object)}.
 *
 * @author Mariusz Smykula
 */
public class Assertions_assertThat_inHexadecimal_Test {

  @Test
  public void should_assert_byte_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((byte) 2).inHexadecimal()
                                                                                         .isEqualTo((byte) 3))
                                                   .withMessage(shouldBeEqualMessage("0x03", "0x02"));

  }

  @Test
  public void should_assert_signed_byte_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((byte) -2).inHexadecimal()
                                                                                          .isEqualTo((byte) 3))
                                                   .withMessage(shouldBeEqualMessage("0x03", "0xFE"));
  }

  @Test
  public void should_assert_bytes_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new byte[] { 2, 3 }).inHexadecimal()
                                                                                                    .isEqualTo(new byte[] { 1 }))
                                                   .withMessage(shouldBeEqualMessage("[0x01]", "[0x02, 0x03]"));
  }

  @Test
  public void should_assert_bytes_contains_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new byte[] { 2, 3 }).inHexadecimal()
                                                                                                    .contains(new byte[] { 1 }))
                                                   .withMessage(format("%nExpecting:%n" +
                                                                       " <[0x02, 0x03]>%n"
                                                                       + "to contain:%n" +
                                                                       " <[0x01]>%n" +
                                                                       "but could not find:%n" +
                                                                       " <[0x01]>%n"));
  }

  @Test
  public void should_assert_short_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((short) 2).inHexadecimal()
                                                                                          .isEqualTo((short) 3))
                                                   .withMessage(shouldBeEqualMessage("0x0003", "0x0002"));
  }

  @Test
  public void should_assert_signed_short_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((short) -2).inHexadecimal()
                                                                                           .isEqualTo((short) 3))
                                                   .withMessage(shouldBeEqualMessage("0x0003", "0xFFFE"));
  }

  @Test
  public void should_assert_shorts_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new short[] { (short) 1,
        (short) 2 }).inHexadecimal().isEqualTo(new short[] { (short) 3 }))
                                                   .withMessage(shouldBeEqualMessage("[0x0003]",
                                                                                     "[0x0001, 0x0002]"));
  }

  @Test
  public void should_assert_integer_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(2).inHexadecimal().isEqualTo(3))
                                                   .withMessage(shouldBeEqualMessage("0x0000_0003", "0x0000_0002"));
  }

  @Test
  public void should_assert_integers_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new int[] { 1, 2 }).inHexadecimal()
                                                                                                   .isEqualTo(new int[] { 2 }))
                                                   .withMessage(shouldBeEqualMessage("[0x0000_0002]",
                                                                                     "[0x0000_0001, 0x0000_0002]"));
  }

  @Test
  public void should_assert_long_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(Long.MAX_VALUE).inHexadecimal()
                                                                                               .isEqualTo(Long.MIN_VALUE))
                                                   .withMessage(shouldBeEqualMessage("0x8000_0000_0000_0000",
                                                                                     "0x7FFF_FFFF_FFFF_FFFF"));
  }

  @Test
  public void should_assert_signed_long_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(-2L).inHexadecimal().isEqualTo(2L))
                                                   .withMessage(shouldBeEqualMessage("0x0000_0000_0000_0002",
                                                                                     "0xFFFF_FFFF_FFFF_FFFE"));
  }

  @Test
  public void should_assert_longs_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new long[] { -1L,
        2L }).inHexadecimal().isEqualTo(new long[] { 3L }))
                                                   .withMessage(shouldBeEqualMessage("[0x0000_0000_0000_0003]",
                                                                                     "[0xFFFF_FFFF_FFFF_FFFF, 0x0000_0000_0000_0002]"));
  }

  @Test
  public void should_assert_float_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(4.3f).inHexadecimal().isEqualTo(2.3f))
                                                   .withMessage(shouldBeEqualMessage("0x4013_3333", "0x4089_999A"));
  }

  @Test
  public void should_assert_floats_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new float[] { 4.3f, -2f }).inHexadecimal()
                                                                                                          .isEqualTo(new float[] {
                                                                                                              4.1f }))
                                                   .withMessage(shouldBeEqualMessage("[0x4083_3333]",
                                                                                     "[0x4089_999A, 0xC000_0000]"));
  }

  @Test
  public void should_assert_double_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(4.3d).inHexadecimal().isEqualTo(2.3d))
                                                   .withMessage(shouldBeEqualMessage("0x4002_6666_6666_6666",
                                                                                     "0x4011_3333_3333_3333"));
  }

  @Test
  public void should_assert_doubles_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new double[] { 1d, 2d }).inHexadecimal()
                                                                                                        .isEqualTo(new double[] {
                                                                                                            3d }))
                                                   .withMessage(shouldBeEqualMessage("[0x4008_0000_0000_0000]",
                                                                                     "[0x3FF0_0000_0000_0000, 0x4000_0000_0000_0000]"));
  }

  @Test
  public void should_assert_collections_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(list(1, 2)).inHexadecimal().isEqualTo(list(3)))
                                                   .withMessage(shouldBeEqualMessage("[0x0000_0003]",
                                                                                     "[0x0000_0001, 0x0000_0002]"));
  }

  @Test
  public void should_assert_Character_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat('a').inHexadecimal().isEqualTo('b'))
                                                   .withMessage(shouldBeEqualMessage("'0x0062'", "'0x0061'"));
  }

  @Test
  public void should_assert_String_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat("a6c").inHexadecimal().isEqualTo("abc"))
                                                   .withMessage(shouldBeEqualMessage("\"['0x0061', '0x0062', '0x0063']\"",
                                                                                     "\"['0x0061', '0x0036', '0x0063']\""));
  }

}
