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
import java.util.Arrays;
import java.util.Date;

/**
 * Tests for {@link org.assertj.core.presentation.HexadecimalRepresentation#toStringOf(Object)}.
 *
 * @author Mariusz Smykula
 */
public class Assertions_assertThat_asHexadecimal_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_assert_byte_as_hexadecimal() {
    thrown.expectMessage("expected:<0x0[3]> but was:<0x0[2]>");
    assertThat((byte) 2).asHexadecimal().isEqualTo((byte) 3);
  }

  @Test
  public void should_assert_signed_byte_as_hexadecimal() {
    thrown.expectMessage("expected:<0x[03]> but was:<0x[FE]>");
    assertThat((byte) -2).asHexadecimal().isEqualTo((byte) 3);
  }

  @Test
  public void should_assert_bytes_as_hexadecimal() {
    thrown.expectMessage("expected:<[0x0[1]]> but was:<[0x0[2, 0x03]]>");
    assertThat(new byte[]{2, 3}).asHexadecimal().isEqualTo(new byte[]{1});
  }

  @Test
  public void should_assert_bytes_contains_as_hexadecimal() {
    thrown.expectMessage("Expecting:\n" +
        " <[0x02, 0x03]>\n" +
        "to contain:\n" +
        " <[0x01]>\n" +
        "but could not find:\n" +
        " <[0x01]>");
    assertThat(new byte[]{2, 3}).asHexadecimal().contains(new byte[]{1});
  }

  @Test
  public void should_assert_short_as_hexadecimal() {
    thrown.expectMessage("expected:<0x000[3]> but was:<0x000[2]>");
    assertThat((short) 2).asHexadecimal().isEqualTo((short) 3);
  }

  @Test
  public void should_assert_signed_short_as_hexadecimal() {
    thrown.expectMessage("expected:<0x[0003]> but was:<0x[FFFE]>");
    assertThat((short) -2).asHexadecimal().isEqualTo((short) 3);
  }

  @Test
  public void should_assert_shorts_as_hexadecimal() {
    thrown.expectMessage("expected:<[0x000[3]]> but was:<[0x000[1, 0x0002]]>");
    assertThat(new short[]{(short) 1, (short) 2}).asHexadecimal().isEqualTo(new short[]{(short) 3});
  }

  @Test
  public void should_assert_integer_as_hexadecimal() {
    thrown.expectMessage("expected:<0x0000_000[3]> but was:<0x0000_000[2]>");
    assertThat(2).asHexadecimal().isEqualTo(3);
  }

  @Test
  public void should_assert_integers_as_hexadecimal() {
    thrown.expectMessage("expected:<[0x0000_000[]2]> but was:<[0x0000_000[1, 0x0000_000]2]>");
    assertThat(new int[]{1, 2}).asHexadecimal().isEqualTo(new int[]{2});
  }

  @Test
  public void should_assert_long_as_hexadecimal() {
    thrown.expectMessage("expected:<0x[8000_0000_0000_0000]> but was:<0x[7FFF_FFFF_FFFF_FFFF]>");
    assertThat(Long.MAX_VALUE).asHexadecimal().isEqualTo(Long.MIN_VALUE);
  }

  @Test
  public void should_assert_signed_long_as_hexadecimal() {
    thrown.expectMessage("expected:<0x[0000_0000_0000_0002]> but was:<0x[FFFF_FFFF_FFFF_FFFE]>");
    assertThat(-2L).asHexadecimal().isEqualTo(2L);
  }

  @Test
  public void should_assert_longs_as_hexadecimal() {
    thrown.expectMessage("expected:<[0x[0000_0000_0000_0003]]> but was:<[0x[FFFF_FFFF_FFFF_FFFF, 0x0000_0000_0000_0002]]>");
    assertThat(new long[]{-1L, 2L}).asHexadecimal().isEqualTo(new long[]{3L});
  }

  @Test
  public void should_assert_float_as_hexadecimal() {
    thrown.expectMessage("expected:<0x40[13_3333]> but was:<0x40[89_999A]>");
    assertThat(4.3f).asHexadecimal().isEqualTo(2.3f);
  }

  @Test
  public void should_assert_floats_as_hexadecimal() {
    thrown.expectMessage("expected:<[0x408[3_3333]]> but was:<[0x408[9_999A, 0xC000_0000]]>");
    assertThat(new float[]{4.3f, -2f}).asHexadecimal().isEqualTo(new float[]{4.1f});
  }

  @Test
  public void should_assert_double_as_hexadecimal() {
    thrown.expectMessage("expected:<0x40[02_6666_6666_6666]> but was:<0x40[11_3333_3333_3333]>");
    assertThat(4.3d).asHexadecimal().isEqualTo(2.3d);
  }

  @Test
  public void should_assert_doubles_as_hexadecimal() {
    thrown.expectMessage("expected:<[0x[4008]_0000_0000_0000]> but was:<[0x[3FF0_0000_0000_0000, 0x4000]_0000_0000_0000]>");
    assertThat(new double[]{1d, 2d}).asHexadecimal().isEqualTo(new double[]{3d});
  }

  @Test
  public void should_assert_collections_as_hexadecimal() {
    thrown.expectMessage("expected:<[0x0000_000[3]]> but was:<[0x0000_000[1, 0x0000_0002]]>");
    assertThat(Arrays.asList(1, 2)).asHexadecimal().isEqualTo(Arrays.asList(3));
  }

  @Test
  public void should_assert_Character_as_hexadecimal() {
    thrown.expectMessage("expected:<'0x006[2]'> but was:<'0x006[1]'>");
    assertThat('a').asHexadecimal().isEqualTo('b');
  }

  @Test
  public void should_assert_String_as_hexadecimal() {
    thrown.expectMessage("expected:<\"['0x0061', '0x00[62]', '0x0063']\"> but was:<\"['0x0061', '0x00[36]', '0x0063']\">");
    assertThat("a6c").asHexadecimal().isEqualTo("abc");
  }

  @Test
  public void should_assert_Date_as_hexadecimal() throws ParseException {
    thrown.expectMessage("expected:<...0x00, 0x00, 0x00, 0x[CB, 0x27, 0xD1, 0x7]7, 0x00, 0x78]> but was:<...0x00, 0x00, 0x00, 0x[B5, 0x1B, 0x97, 0x9]7, 0x00, 0x78]>");
    assertThat(toDate("26/08/1994")).asHexadecimal().isEqualTo(toDate("26/08/1997"));
  }

  private Date toDate(String source) throws ParseException {
    return new SimpleDateFormat("dd/MM/yyyy").parse(source);
  }
}
