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

import java.util.Arrays;

import org.junit.Test;

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
                                                   .withMessage("expected:<0x0[3]> but was:<0x0[2]>");
  }

  @Test
  public void should_assert_signed_byte_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((byte) -2).inHexadecimal()
                                                                                          .isEqualTo((byte) 3))
                                                   .withMessage("expected:<0x[03]> but was:<0x[FE]>");
  }

  @Test
  public void should_assert_bytes_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new byte[] { 2,
        3 }).inHexadecimal().isEqualTo(new byte[] { 1 })).withMessage("expected:<[0x0[1]]> but was:<[0x0[2, 0x03]]>");
  }

  @Test
  public void should_assert_bytes_contains_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new byte[] { 2,
        3 }).inHexadecimal().contains(new byte[] { 1 })).withMessage(format("%nExpecting:%n" +
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
                                                   .withMessage("expected:<0x000[3]> but was:<0x000[2]>");
  }

  @Test
  public void should_assert_signed_short_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat((short) -2).inHexadecimal()
                                                                                           .isEqualTo((short) 3))
                                                   .withMessage("expected:<0x[0003]> but was:<0x[FFFE]>");
  }

  @Test
  public void should_assert_shorts_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new short[] { (short) 1,
        (short) 2 }).inHexadecimal().isEqualTo(new short[] { (short) 3 }))
                                                   .withMessage("expected:<[0x000[3]]> but was:<[0x000[1, 0x0002]]>");
  }

  @Test
  public void should_assert_integer_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(2).inHexadecimal().isEqualTo(3))
                                                   .withMessage("expected:<0x0000_000[3]> but was:<0x0000_000[2]>");
  }

  @Test
  public void should_assert_integers_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new int[] { 1,
        2 }).inHexadecimal().isEqualTo(new int[] {
            2 })).withMessage("expected:<[0x0000_000[]2]> but was:<[0x0000_000[1, 0x0000_000]2]>");
  }

  @Test
  public void should_assert_long_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(Long.MAX_VALUE).inHexadecimal()
                                                                                               .isEqualTo(Long.MIN_VALUE))
                                                   .withMessage("expected:<0x[8000_0000_0000_0000]> but was:<0x[7FFF_FFFF_FFFF_FFFF]>");
  }

  @Test
  public void should_assert_signed_long_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(-2L).inHexadecimal().isEqualTo(2L))
                                                   .withMessage("expected:<0x[0000_0000_0000_0002]> but was:<0x[FFFF_FFFF_FFFF_FFFE]>");
  }

  @Test
  public void should_assert_longs_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new long[] { -1L,
        2L }).inHexadecimal().isEqualTo(new long[] {
            3L })).withMessage("expected:<[0x[0000_0000_0000_0003]]> but was:<[0x[FFFF_FFFF_FFFF_FFFF, 0x0000_0000_0000_0002]]>");
  }

  @Test
  public void should_assert_float_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(4.3f).inHexadecimal().isEqualTo(2.3f))
                                                   .withMessage("expected:<0x40[13_3333]> but was:<0x40[89_999A]>");
  }

  @Test
  public void should_assert_floats_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new float[] { 4.3f,
        -2f }).inHexadecimal().isEqualTo(new float[] {
            4.1f })).withMessage("expected:<[0x408[3_3333]]> but was:<[0x408[9_999A, 0xC000_0000]]>");
  }

  @Test
  public void should_assert_double_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(4.3d).inHexadecimal().isEqualTo(2.3d))
                                                   .withMessage("expected:<0x40[02_6666_6666_6666]> but was:<0x40[11_3333_3333_3333]>");
  }

  @Test
  public void should_assert_doubles_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(new double[] { 1d,
        2d }).inHexadecimal().isEqualTo(new double[] {
            3d })).withMessage("expected:<[0x[4008]_0000_0000_0000]> but was:<[0x[3FF0_0000_0000_0000, 0x4000]_0000_0000_0000]>");
  }

  @Test
  public void should_assert_collections_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(Arrays.asList(1,
                                                                                              2)).inHexadecimal()
                                                                                                 .isEqualTo(Arrays.asList(3)))
                                                   .withMessage("expected:<[0x0000_000[3]]> but was:<[0x0000_000[1, 0x0000_0002]]>");
  }

  @Test
  public void should_assert_Character_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat('a').inHexadecimal().isEqualTo('b'))
                                                   .withMessage("expected:<'0x006[2]'> but was:<'0x006[1]'>");
  }

  @Test
  public void should_assert_String_in_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat("a6c").inHexadecimal().isEqualTo("abc"))
                                                   .withMessage("expected:<\"['0x0061', '0x00[62]', '0x0063']\"> but was:<\"['0x0061', '0x00[36]', '0x0063']\">");
  }

}
