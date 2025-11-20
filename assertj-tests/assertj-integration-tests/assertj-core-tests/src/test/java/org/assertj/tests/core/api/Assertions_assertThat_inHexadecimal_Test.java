/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author Mariusz Smykula
 */
class Assertions_assertThat_inHexadecimal_Test {

  @Test
  void should_assert_byte_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat((byte) 2).inHexadecimal().isEqualTo((byte) 3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0x02", "0x03"));

  }

  @Test
  void should_assert_signed_byte_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat((byte) -2).inHexadecimal().isEqualTo((byte) 3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0xFE", "0x03"));
  }

  @Test
  void should_assert_bytes_in_hexadecimal() {
    // GIVEN
    byte[] actual = { 2, 3 };
    byte expected = 1;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).inHexadecimal().contains(expected));
    // THEN
    then(assertionError).hasMessage(format("%nExpecting byte[]:%n" +
                                           "  [0x02, 0x03]%n" +
                                           "to contain:%n" +
                                           "  [0x01]%n" +
                                           "but could not find the following byte(s):%n" +
                                           "  [0x01]%n"));
  }

  @Test
  void should_assert_short_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat((short) 2).inHexadecimal().isEqualTo((short) 3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0x0002", "0x0003"));
  }

  @Test
  void should_assert_signed_short_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat((short) -2).inHexadecimal().isEqualTo((short) 3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0xFFFE", "0x0003"));
  }

  @Test
  void should_assert_shorts_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(new short[] { (short) 1,
        (short) 2 }).inHexadecimal().isEqualTo(new short[] { (short) 3 }));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("[0x0001, 0x0002]", "[0x0003]"));
  }

  @Test
  void should_assert_integer_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(2).inHexadecimal().isEqualTo(3));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0x0000_0002", "0x0000_0003"));
  }

  @Test
  void should_assert_integers_in_hexadecimal() {
    // GIVEN
    int[] actual = { 1, 2 };
    int[] expected = { 2 };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).inHexadecimal().isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("[0x0000_0001, 0x0000_0002]", "[0x0000_0002]"));
  }

  @Test
  void should_assert_long_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(Long.MAX_VALUE).inHexadecimal()
                                                                              .isEqualTo(Long.MIN_VALUE));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0x7FFF_FFFF_FFFF_FFFF", "0x8000_0000_0000_0000"));
  }

  @Test
  void should_assert_signed_long_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(-2L).inHexadecimal().isEqualTo(2L));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0xFFFF_FFFF_FFFF_FFFE", "0x0000_0000_0000_0002"));
  }

  @Test
  void should_assert_longs_in_hexadecimal() {
    // GIVEN
    long[] actual = { -1L, 2L };
    long[] expected = { 3L };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).inHexadecimal().isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("[0xFFFF_FFFF_FFFF_FFFF, 0x0000_0000_0000_0002]",
                                                         "[0x0000_0000_0000_0003]"));
  }

  @Test
  void should_assert_float_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(4.3f).inHexadecimal().isEqualTo(2.3f));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0x4089_999A", "0x4013_3333"));
  }

  @Test
  void should_assert_floats_in_hexadecimal() {
    // GIVEN
    float[] actual = { 4.3f, -2f };
    float[] expected = { 4.1f };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).inHexadecimal().isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("[0x4089_999A, 0xC000_0000]", "[0x4083_3333]"));
  }

  @Test
  void should_assert_double_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(4.3d).inHexadecimal().isEqualTo(2.3d));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("0x4011_3333_3333_3333", "0x4002_6666_6666_6666"));
  }

  @Test
  void should_assert_doubles_in_hexadecimal() {
    // GIVEN
    double[] actual = { 1d, 2d };
    double[] expected = { 3d };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).inHexadecimal().isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("[0x3FF0_0000_0000_0000, 0x4000_0000_0000_0000]",
                                                         "[0x4008_0000_0000_0000]"));
  }

  @Test
  void should_assert_collections_in_hexadecimal() {
    // GIVEN
    List<Integer> actual = List.of(1, 2);
    List<Integer> expected = List.of(3);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).inHexadecimal().isEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("[0x0000_0001, 0x0000_0002]", "[0x0000_0003]"));
  }

  @Test
  void should_assert_Character_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat('a').inHexadecimal().isEqualTo('b'));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("'0x0061'", "'0x0062'"));
  }

  @Test
  void should_assert_String_in_hexadecimal() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("a6c").inHexadecimal().isEqualTo("abc"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("\"['0x0061', '0x0036', '0x0063']\"",
                                                         "\"['0x0061', '0x0062', '0x0063']\""));
  }

  @Test
  public void should_keep_existing_description_set_before_calling_inHexadecimal() {
    // GIVEN
    String description = "My description";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("ab").as(description).inHexadecimal().isNull());
    // THEN
    then(assertionError).hasMessageContaining(description);
  }

}
