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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringWhitespace.shouldNotBeEqualIgnoringWhitespace;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.tests.core.testkit.CharArrays.arrayOf;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.assertj.core.api.AssertionInfo;
import org.assertj.tests.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertNotEqualsIgnoringWhitespace(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Dan Corder
 */
class Strings_assertNotEqualsIgnoringWhitespace_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_is_not_null_and_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertNotEqualsIgnoringWhitespace(someInfo(), "Luke",
                                                                                                null))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @ParameterizedTest
  @MethodSource("notEqualIgnoringWhitespaceGenerator")
  void should_pass_if_both_Strings_are_not_equal_ignoring_whitespace(String actual, String expected) {
    strings.assertNotEqualsIgnoringWhitespace(someInfo(), actual, expected);
  }

  public static Stream<Arguments> notEqualIgnoringWhitespaceGenerator() {
    return Stream.of(Arguments.of("foo", "bar"),
                     Arguments.of("foo", new String(arrayOf('b', 'a', 'r'))),
                     Arguments.of(null, "bar"));
  }

  @ParameterizedTest
  @MethodSource("equalIgnoringWhitespaceGenerator")
  void should_fail_if_both_Strings_are_equal_ignoring_whitespace(String actual, String expected) {
    Throwable error = catchThrowable(() -> strings.assertNotEqualsIgnoringWhitespace(someInfo(), actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenStringsAreEqualIgnoringWhitespace(someInfo(), actual, expected);
  }

  public static Stream<Arguments> equalIgnoringWhitespaceGenerator() {
    return Stream.of(Arguments.of("my foo", "myfoo"),
                     Arguments.of("myfoo", "my foo"),
                     Arguments.of("my   foo bar", "my foo bar"),
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

  private void verifyFailureThrownWhenStringsAreEqualIgnoringWhitespace(AssertionInfo info, String actual,
                                                                        String expected) {
    verify(failures).failure(info, shouldNotBeEqualIgnoringWhitespace(actual, expected));
  }
}
