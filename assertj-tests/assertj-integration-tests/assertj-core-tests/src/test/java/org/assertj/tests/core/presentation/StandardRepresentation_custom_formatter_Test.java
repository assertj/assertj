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
package org.assertj.tests.core.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.BinaryRepresentation.BINARY_REPRESENTATION;
import static org.assertj.core.presentation.HexadecimalRepresentation.HEXA_REPRESENTATION;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;

import org.assertj.core.api.Assertions;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class StandardRepresentation_custom_formatter_Test {

  @AfterEach
  public void afterTest() {
    StandardRepresentation.removeAllRegisteredFormatters();
  }

  @Test
  void should_use_registered_formatter_for_type() {
    // GIVEN
    Object longNumber = 123L; // need to declare as an Object otherwise toStringOf(Long) is used
    assertThat(STANDARD_REPRESENTATION.toStringOf(longNumber)).isEqualTo("123L");
    assertThat(HEXA_REPRESENTATION.toStringOf(longNumber)).isEqualTo("0x0000_0000_0000_007B");
    assertThat(BINARY_REPRESENTATION.toStringOf(longNumber)).isEqualTo("0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_01111011");
    assertThat(UNICODE_REPRESENTATION.toStringOf(longNumber)).isEqualTo("123L");
    // WHEN
    Assertions.registerFormatterForType(Long.class, value -> "$" + value + "$");
    // THEN
    then(STANDARD_REPRESENTATION.toStringOf(longNumber)).isEqualTo("$123$");
    then(HEXA_REPRESENTATION.toStringOf(longNumber)).isEqualTo("$123$");
    then(BINARY_REPRESENTATION.toStringOf(longNumber)).isEqualTo("$123$");
    then(UNICODE_REPRESENTATION.toStringOf(longNumber)).isEqualTo("$123$");
  }

  @Test
  void should_remove_all_registered_formatters_after_resetting_to_default() {
    // GIVEN
    StandardRepresentation.registerFormatterForType(String.class, value -> "'" + value + "'");
    StandardRepresentation.registerFormatterForType(Integer.class, value -> "int(" + Integer.toBinaryString(value) + ")");
    StandardRepresentation.registerFormatterForType(Integer.class, value -> "int(" + Integer.toBinaryString(value) + ")");
    Object string = "abc";
    Object intNumber = 8;
    assertThat(STANDARD_REPRESENTATION.toStringOf(string)).isEqualTo("'abc'");
    assertThat(STANDARD_REPRESENTATION.toStringOf(intNumber)).isEqualTo("int(1000)");
    // WHEN
    StandardRepresentation.removeAllRegisteredFormatters();
    // THEN
    then(STANDARD_REPRESENTATION.toStringOf(string)).isEqualTo("\"abc\"");
    then(STANDARD_REPRESENTATION.toStringOf(intNumber)).isEqualTo("8");
  }

  @Test
  void should_return_null_when_registered_formatter_returns_null() {
    // GIVEN
    StandardRepresentation.registerFormatterForType(String.class, value -> null);
    Object string = "abc";
    // WHEN
    String actual = STANDARD_REPRESENTATION.toStringOf(string);
    // THEN
    then(actual).isNull();
  }
}
