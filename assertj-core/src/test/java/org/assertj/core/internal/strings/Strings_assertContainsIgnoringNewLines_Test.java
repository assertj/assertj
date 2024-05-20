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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.containsIgnoringNewLines;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Strings#assertContainsIgnoringNewLines(AssertionInfo, CharSequence, CharSequence...)} </code>.
 */
class Strings_assertContainsIgnoringNewLines_Test extends StringsBaseTest {

  private static final StandardComparisonStrategy STANDARD_COMPARISON = StandardComparisonStrategy.instance();
  private static final WritableAssertionInfo INFO = someInfo();

  @ParameterizedTest
  @ValueSource(strings = { "Al", "ice\nandBob", "Alice\nandBob", "Alice\nand\nBob", "ice\r\nandBob", "Alice\r\nandBob",
      "Alice\r\nand\nBob" })
  void should_pass_if_actual_contains_value_when_new_lines_are_ignored(String value) {
    // GIVEN
    String actual = "Alice\nand\nBob";
    // WHEN / THEN
    strings.assertContainsIgnoringNewLines(INFO, actual, value);
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings() {
    // GIVEN
    String actual = "Alice\nand\nBob";
    String[] values = array("Ali", "ce", "\na", "nd", "\r\nBob");
    // WHEN / THEN
    strings.assertContainsIgnoringNewLines(INFO, actual, values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_value() {
    // GIVEN
    String actual = "Al";
    String value = "Bob";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines("Al", array("Bob"), set("Bob"), STANDARD_COMPARISON).create());
  }

  @Test
  void should_fail_if_actual_contains_value_but_in_different_case() {
    // GIVEN
    String actual = "Al";
    String value = "al";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines("Al", array("al"), set("al"), STANDARD_COMPARISON).create());
  }

  @Test
  void should_fail_if_actual_contains_value_with_new_lines_but_in_different_case() {
    // GIVEN
    String actual = "Alice\nand\nbob";
    String value = "and\nBob";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines("Alice\nand\nbob", array("and\nBob"), set("and\nBob"),
                                                             STANDARD_COMPARISON).create());
  }

  @Test
  void should_throw_error_if_value_is_null() {
    // GIVEN
    String actual = "Al";
    String value = null;
    // WHEN / THEN
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    String value = "Bob";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // GIVEN
    String actual = "Alice";
    String[] values = array("Al", "ice", "Bob");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, values));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines(actual, values, set("Bob"), STANDARD_COMPARISON).create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "Al", "al", "AL", "and\nbob", "and\nBob", "and\nB\no\nb" })
  void should_pass_if_actual_contains_value_according_to_custom_comparison_strategy(String value) {
    // GIVEN
    String actual = "Alice\nand\nBob";
    // WHEN / THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringNewLines(INFO, actual, value);
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Alice\nand\nBob";
    String[] values = array("Al", "iCe", "and", "Bob");
    // WHEN / THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringNewLines(INFO, actual, values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Al";
    String value = "Bob";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringNewLines(INFO,
                                                                                                                                           actual,
                                                                                                                                           value));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines("Al", array("Bob"), set("Bob"), comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Alice";
    String[] values = array("Al", "ice", "\r\nBob");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringNewLines(INFO,
                                                                                                                                           actual,
                                                                                                                                           values));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines(actual, values, set("\r\nBob"), comparisonStrategy).create());
  }
}
