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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldNotBeEqualNormalizingWhitespace.shouldNotBeEqualNormalizingWhitespace;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.CharArrays.arrayOf;
import static org.assertj.core.test.TestData.someInfo;

import java.util.stream.Stream;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertNotEqualsNormalizingWhitespace(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Dan Corder
 */
public class Strings_assertNotEqualsNormalizingWhitespace_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_is_not_null_and_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertNotEqualsNormalizingWhitespace(someInfo(), "Luke",
                                                                                                   null))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @ParameterizedTest
  @MethodSource("notEqualNormalizingWhitespaceGenerator")
  public void should_pass_if_both_Strings_are_not_equal_after_whitespace_is_normalized(String actual, String expected) {
    strings.assertNotEqualsNormalizingWhitespace(someInfo(), actual, expected);
  }

  public static Stream<Arguments> notEqualNormalizingWhitespaceGenerator() {
    return Stream.of(Arguments.of("foo", "bar"),
                     Arguments.of("my foo", "myfoo"),
                     Arguments.of("foo", new String(arrayOf('b', 'a', 'r'))),
                     Arguments.of(null, "bar"));
  }

  @ParameterizedTest
  @MethodSource("equalNormalizingWhitespaceGenerator")
  public void should_fail_if_both_Strings_are_equal_after_whitespace_is_normalized(String actual, String expected) {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertNotEqualsNormalizingWhitespace(someInfo(),
                                                                                                                  actual,
                                                                                                                  expected))
                                                   .withMessage(format(shouldNotBeEqualNormalizingWhitespace(actual,
                                                                                                             expected).create()));
  }

  public static Stream<Arguments> equalNormalizingWhitespaceGenerator() {
    return Stream.of(Arguments.of("my   foo bar", "my foo bar"),
                     Arguments.of("  my foo bar  ", "my foo bar"),
                     Arguments.of(" my\tfoo bar ", " my foo bar"),
                     Arguments.of(" my foo    bar ", "my foo bar"),
                     Arguments.of(" my foo    bar ", "  my foo bar   "),
                     Arguments.of("       ", " "),
                     Arguments.of(" my\tfoo bar ", new String(arrayOf(' ', 'm', 'y', ' ', 'f', 'o', 'o', ' ', 'b', 'a', 'r'))),
                     Arguments.of(" my\tfoo bar ", " my\tfoo bar "),   // same
                     Arguments.of(null, null),   // null
                     Arguments.of(" \t \t", " "),
                     Arguments.of(" abc", "abc "));
  }
}
