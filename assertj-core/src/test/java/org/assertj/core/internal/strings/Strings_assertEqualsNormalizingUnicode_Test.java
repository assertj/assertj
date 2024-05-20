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
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeEqualNormalizingUnicode.shouldBeEqualNormalizingUnicode;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertEqualsToNormalizingUnicode(AssertionInfo, CharSequence, CharSequence)} (org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Julieta Navarro
 */
class Strings_assertEqualsNormalizingUnicode_Test extends StringsBaseTest {
  @Test
  void should_fail_if_actual_is_not_null_and_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertEqualsToNormalizingUnicode(someInfo(), "\u0041", null))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_both_Strings_are_not_equal_after_unicode_is_normalized() {
    // GIVEN
    String actual = "\u00C4";
    String expected = "\u0062";
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> strings.assertEqualsToNormalizingUnicode(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldBeEqualNormalizingUnicode(actual, expected, "Ä", expected), "Ä", expected);
  }

  @ParameterizedTest
  @MethodSource("equalNormalizingUnicodeGenerator")
  void should_pass_if_both_Strings_are_equal_after_unicode_is_normalized(String actual, String expected) {
    strings.assertEqualsToNormalizingUnicode(someInfo(), actual, expected);
  }

  public static Stream<Arguments> equalNormalizingUnicodeGenerator() {
    return Stream.of(
                     Arguments.of("A", "A"),
                     Arguments.of("", ""),
                     // Ä, Ä
                     Arguments.of("\u00C4", "\u0041\u0308"),
                     // Amélie, Amélie
                     Arguments.of("\u0041\u006d\u00e9\u006c\u0069\u0065", "\u0041\u006d\u0065\u0301\u006c\u0069\u0065"),
                     // ñ, ñ
                     Arguments.of("\u00F1", "\u006E\u0303"),
                     Arguments.of("Zoë", "Zoë"),
                     Arguments.of("sabiá", "sabiá"),
                     // ﬃ, ﬃ
                     Arguments.of("ﬃ", "\uFB03"),
                     // schön, schön
                     Arguments.of("schön", "scho\u0308n"));
  }
}
