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
import static org.assertj.core.error.ShouldBeEqualIgnoringWhitespace.shouldBeEqualIgnoringWhitespace;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/// @author Alex Ruiz
/// @author Joel Costigliola
/// @author Alexander Bischof
/// @author Dan Corder
class CharSequenceAssert_isEqualToIgnoringWhitespace_Test {

  @Test
  void should_fail_if_actual_is_null_and_expected_is_not() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToIgnoringWhitespace("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualIgnoringWhitespace(null, "Luke").create());
  }

  @Test
  void should_fail_if_actual_is_not_null_and_expected_is_null() {
    // GIVEN
    String actual = "Luke";
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).isEqualToIgnoringWhitespace(null));
    // THEN
    then(npe).hasMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_both_Strings_are_not_equal_ignoring_whitespace() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToIgnoringWhitespace("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualIgnoringWhitespace(actual, "Luke").create());
  }

  @ParameterizedTest
  @MethodSource
  void should_pass_if_both_Strings_are_equal_ignoring_whitespace(String actual, String expected) {
    assertThat(actual).isEqualToIgnoringWhitespace(expected);
  }

  static Stream<Arguments> should_pass_if_both_Strings_are_equal_ignoring_whitespace() {
    return Stream.of(arguments("myfoobar", "my foo bar"),
                     arguments("my foo bar", "myfoobar"),
                     arguments("my   foo bar", "my foo bar"),
                     arguments("  my foo bar  ", "my foo bar"),
                     arguments(" my\tfoo bar ", " my foo bar"),
                     arguments(" my foo    bar ", "my foo bar"),
                     arguments(" my foo    bar ", "  my foo bar   "),
                     arguments("       ", " "),
                     arguments(" my\tfoo bar ", new String(new char[] { ' ', 'm', 'y', ' ', 'f', 'o', 'o', ' ', 'b', 'a', 'r' })),
                     arguments(" my\tfoo bar ", " my\tfoo bar "), // same
                     arguments(null, null), // null
                     arguments(" \t \t", " "),
                     arguments(" abc", "abc "));
  }

}
