/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for {@link org.assertj.core.presentation.BinaryRepresentation#toStringOf(Object)}.
 *
 * @author Mariusz Smykula
 */
public class Assertions_assertThat_inBinary_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_assert_byte_in_binary() {
    thrown.expectMessage("expected:<0b0000001[1]> but was:<0b0000001[0]>");
    assertThat((byte) 2).inBinary().isEqualTo((byte) 3);
  }

  @Test
  public void should_assert_signed_byte_in_binary() {
    thrown.expectMessage("expected:<0b[00000011]> but was:<0b[11111110]>");
    assertThat((byte) -2).inBinary().isEqualTo((byte) 3);
  }

  @Test
  public void should_assert_bytes_in_binary() {
    thrown.expectMessage("expected:<[0b000000[0]1]> but was:<[0b000000[10, 0b0000001]1]>");
    assertThat(new byte[]{2, 3}).inBinary().isEqualTo(new byte[]{1});
  }

  @Test
  public void should_assert_short_in_binary() {
    thrown.expectMessage("expected:<0b00000000_0000001[1]> but was:<0b00000000_0000001[0]>");
    assertThat((short) 2).inBinary().isEqualTo((short) 3);
  }

  @Test
  public void should_assert_signed_short_in_binary() {
    thrown.expectMessage("expected:<0b[00000000_000000]11> but was:<0b[11111111_111111]11>");
    assertThat((short) -1).inBinary().isEqualTo((short) 3);
  }

  @Test
  public void should_assert_integer_in_binary() {
    thrown.expectMessage("expected:<...000_00000000_0000001[1]> but was:<...000_00000000_0000001[0]>");
    assertThat(2).inBinary().isEqualTo(3);
  }

  @Test
  public void should_assert_negative_integer_in_binary() {
    thrown.expectMessage(
        "expected:<0b[11111111_11111111_11111111_1111110]1> but was:<0b[00000000_00000000_00000000_0000001]1>");
    assertThat(3).inBinary().isEqualTo(-3);
  }

  @Test
  public void should_assert_long_in_binary() {
    thrown.expectMessage("expected:<...000_00000000_0000001[1]> but was:<...000_00000000_0000001[0]>");
    assertThat((long) 2).inBinary().isEqualTo(3);
  }

  @Test
  public void should_assert_negative_long_in_binary() {
    thrown.expectMessage("expected:<0b[00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000011]> " +
        "but was:<0b[11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111110]>");
    assertThat((long) -2).inBinary().isEqualTo(3);
  }

  @Test
  public void should_assert_float_in_binary() {
    thrown.expectMessage(
        "expected:<0b01000000_0[1000000_00000000_0000000]0> but was:<0b01000000_0[0000110_01100110_0110011]0>");
    assertThat(2.1f).inBinary().isEqualTo(3f);
  }

  @Test
  public void should_assert_double_in_binary() {
    thrown.expectMessage("expected:<0b01000000_0000[1000_00000000_00000000_00000000_00000000_00000000_00000000]> " +
        "but was:<0b01000000_0000[0000_11001100_11001100_11001100_11001100_11001100_11001101]>");
    assertThat(2.1d).inBinary().isEqualTo(3d);
  }

  @Test
  public void should_assert_String_in_binary() {
    thrown.expectMessage(
        "expected:<...0001', '0b00000000_0[01101]10']\"> but was:<...0001', '0b00000000_0[11000]10']\">");
    assertThat("ab").inBinary().isEqualTo("a6");
  }

}
