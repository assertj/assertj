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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringWhitespaces;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Strings#assertContainsIgnoringWhitespaces(AssertionInfo, CharSequence, CharSequence...)} </code>.
 *
 * @author Johannes Becker
 */
public class Strings_assertContainsIgnoringWhitespaces_Test extends StringsBaseTest {

  private static final WritableAssertionInfo INFO = someInfo();

  @ParameterizedTest
  @ValueSource(strings = { "Yo", "a n dLuke", "YodaandLuke", "Yoda\tand\nLuke" })
  void should_pass_if_actual_contains_value_when_whitespaces_are_ignored(String value) {
    // GIVEN
    String actual = "Yoda and Luke";
    // WHEN / THEN
    strings.assertContainsIgnoringWhitespaces(INFO, actual, value);
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings() {
    // GIVEN
    String actual = "Yoda and Luke";
    String[] values = array("Yo", "da", "a n d", "L u k    e");
    // WHEN / THEN
    strings.assertContainsIgnoringWhitespaces(INFO, actual, values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_value() {
    // GIVEN
    String actual = "Yoda";
    String value = "Luke";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringWhitespaces(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces("Yoda", "Luke",
                                                                     StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_contains_value_but_in_different_case() {
    // GIVEN
    String actual = "Yoda";
    String value = "yo";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringWhitespaces(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces("Yoda", "yo",
                                                                     StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_contains_value_with_whitespaces_but_in_different_case() {
    // GIVEN
    String actual = "Yoda and Luke";
    String value = "a n dluke";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringWhitespaces(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces("Yoda and Luke", "a n dluke",
                                                                     StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_throw_error_if_value_is_null() {
    // GIVEN
    String actual = "Yoda";
    String value = null;
    // WHEN / THEN
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsIgnoringWhitespaces(INFO, actual, value))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    String value = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringWhitespaces(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // GIVEN
    String actual = "Yoda";
    String[] values = array("Yo", "da", "Han");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringWhitespaces(INFO, actual, values));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces(actual, values, newLinkedHashSet("Han"),
                                                                     StandardComparisonStrategy.instance()).create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "Yo", "yo", "YO", "a n dluke", "A N Dluke", "and L u   k" })
  void should_pass_if_actual_contains_value_according_to_custom_comparison_strategy(String value) {
    // GIVEN
    String actual = "Yoda and Luke";
    // WHEN / THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespaces(INFO, actual, value);
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda and Luke";
    String[] values = array("YO", "dA", "Aa", " n d l");
    // WHEN / THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespaces(INFO, actual, values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    String value = "Luke";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespaces(INFO,
                                                                                                                                              actual,
                                                                                                                                              value));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces("Yoda", "Luke", comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    String[] values = array("Yo", "da", "Han");

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespaces(INFO,
                                                                                                                                              actual,
                                                                                                                                              values));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespaces(actual, values, newLinkedHashSet("Han"),
                                                                     comparisonStrategy).create());
  }
}
