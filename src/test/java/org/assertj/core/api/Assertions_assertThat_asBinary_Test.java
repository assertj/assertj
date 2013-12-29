/*
 * Created on Dec 21, 2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2009-2013 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tests for {@link org.assertj.core.presentation.BinaryRepresentation#toStringOf(Object)}.
 *
 * @author Mariusz Smykula
 */
public class Assertions_assertThat_asBinary_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_assert_byte_as_binary() {
    thrown.expectMessage("expected:<0b0000001[1]> but was:<0b0000001[0]>");
    assertThat((byte) 2).asBinary().isEqualTo((byte) 3);
  }

  @Test
  public void should_assert_signed_byte_as_binary() {
    thrown.expectMessage("expected:<0b[00000011]> but was:<0b[11111110]>");
    assertThat((byte) -2).asBinary().isEqualTo((byte) 3);
  }

  @Test
  public void should_assert_bytes_as_binary() {
    thrown.expectMessage("expected:<[0b000000[0]1]> but was:<[0b000000[10, 0b0000001]1]>");
    assertThat(new byte[]{2, 3}).asBinary().isEqualTo(new byte[]{1});
  }

  @Test
  public void should_assert_short_as_binary() {
    thrown.expectMessage("expected:<0b00000000_0000001[1]> but was:<0b00000000_0000001[0]>");
    assertThat((short) 2).asBinary().isEqualTo((short) 3);
  }

  @Test
  public void should_assert_signed_short_as_binary() {
    thrown.expectMessage("expected:<0b[00000000_000000]11> but was:<0b[11111111_111111]11>");
    assertThat((short) -1).asBinary().isEqualTo((short) 3);
  }

  @Test
  public void should_assert_integer_as_binary() {
    thrown.expectMessage("expected:<...000_00000000_0000001[1]> but was:<...000_00000000_0000001[0]>");
    assertThat(2).asBinary().isEqualTo(3);
  }

  @Test
  public void should_assert_negative_integer_as_binary() {
    thrown.expectMessage(
        "expected:<0b[11111111_11111111_11111111_1111110]1> but was:<0b[00000000_00000000_00000000_0000001]1>");
    assertThat(3).asBinary().isEqualTo(-3);
  }

  @Test
  public void should_assert_long_as_binary() {
    thrown.expectMessage("expected:<...000_00000000_0000001[1]> but was:<...000_00000000_0000001[0]>");
    assertThat((long) 2).asBinary().isEqualTo((long) 3);
  }

  @Test
  public void should_assert_negative_long_as_binary() {
    thrown.expectMessage("expected:<0b[00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000011]> " +
        "but was:<0b[11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111110]>");
    assertThat((long) -2).asBinary().isEqualTo((long) 3);
  }

  @Test
  public void should_assert_float_as_binary() {
    thrown.expectMessage(
        "expected:<0b01000000_0[1000000_00000000_0000000]0> but was:<0b01000000_0[0000110_01100110_0110011]0>");
    assertThat(2.1f).asBinary().isEqualTo(3f);
  }

  @Test
  public void should_assert_double_as_binary() {
    thrown.expectMessage("expected:<0b01000000_0000[1000_00000000_00000000_00000000_00000000_00000000_00000000]> " +
        "but was:<0b01000000_0000[0000_11001100_11001100_11001100_11001100_11001100_11001101]>");
    assertThat(2.1d).asBinary().isEqualTo(3d);
  }

  @Test
  public void should_assert_String_as_binary() {
    thrown.expectMessage(
        "expected:<...0001', '0b00000000_0[01101]10']\"> but was:<...0001', '0b00000000_0[11000]10']\">");
    assertThat("ab").asBinary().isEqualTo("a6");
  }

  @Test
  public void should_assert_Date_as_hex() throws ParseException {
    thrown.expectMessage("expected:<...000, 0b00000000, 0b1[1001011, 0b00100111, 0b11010001, 0b011]10111, 0b00000000, 0...> " +
        "but was:<...000, 0b00000000, 0b1[0110101, 0b00011011, 0b10010111, 0b100]10111, 0b00000000, 0...>");
    assertThat(toDate("26/08/1994")).asBinary().isEqualTo(toDate("26/08/1997"));
  }

  private Date toDate(String source) throws ParseException {
    return new SimpleDateFormat("dd/MM/yyyy").parse(source);
  }
}
