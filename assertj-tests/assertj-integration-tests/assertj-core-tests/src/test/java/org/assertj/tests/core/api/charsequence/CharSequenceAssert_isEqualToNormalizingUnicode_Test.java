/*
 * Copyright 2012-2026 the original author or authors.
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
package org.assertj.tests.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEqualNormalizingUnicode.shouldBeEqualNormalizingUnicode;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/// @author Julieta Navarro
class CharSequenceAssert_isEqualToNormalizingUnicode_Test {

  @Test
  void should_fail_if_actual_is_not_null_and_expected_is_null() {
    // GIVEN
    String actual = "A";
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isEqualToNormalizingUnicode(null));
    // THEN
    then(npe).hasMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_both_Strings_are_not_equal_after_unicode_is_normalized() {
    // GIVEN
    String actual = "Ä";
    String expected = "b";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToNormalizingUnicode(expected));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualNormalizingUnicode(actual, expected, "Ä", expected).create());
  }

  @ParameterizedTest
  @MethodSource
  void should_pass_if_both_Strings_are_equal_after_unicode_is_normalized(String actual, String expected) {
    assertThat(actual).isEqualToNormalizingUnicode(expected);
  }

  static Stream<Arguments> should_pass_if_both_Strings_are_equal_after_unicode_is_normalized() {
    return Stream.of(arguments("A", "A"),
                     arguments("", ""),
                     // Ä, Ä
                     arguments("Ä", "Ä"),
                     // Amélie, Amélie
                     arguments("Amélie", "Amélie"),
                     // ñ, ñ
                     arguments("ñ", "ñ"),
                     arguments("Zoë", "Zoë"),
                     arguments("sabiá", "sabiá"),
                     // ﬃ, ﬃ
                     arguments("ﬃ", "ﬃ"),
                     // schön, schön
                     arguments("schön", "schön"));
  }

}
