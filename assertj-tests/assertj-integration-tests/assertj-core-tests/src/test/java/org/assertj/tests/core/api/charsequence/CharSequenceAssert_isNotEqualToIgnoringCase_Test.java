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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringCase.shouldNotBeEqualIgnoringCase;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.DefaultLocale;

/// @author Alexander Bischof
class CharSequenceAssert_isNotEqualToIgnoringCase_Test {

  @Test
  void should_pass_if_actual_is_null_and_expected_is_not() {
    assertThat((String) null).isNotEqualToIgnoringCase("Luke");
  }

  @Test
  void should_pass_if_actual_is_not_null_and_expected_is() {
    assertThat("Luke").isNotEqualToIgnoringCase(null);
  }

  @Test
  void should_pass_if_both_Strings_are_not_equal_regardless_of_case() {
    assertThat("Yoda").isNotEqualToIgnoringCase("Luke");
  }

  @Test
  void should_fail_if_both_Strings_are_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotEqualToIgnoringCase(null));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase(null, null).create());
  }

  @Test
  void should_fail_if_both_Strings_are_the_same() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotEqualToIgnoringCase(actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase(actual, actual).create());
  }

  @Test
  void should_fail_if_both_Strings_are_equal_but_not_same() {
    // GIVEN
    String actual = "Yoda";
    String expected = new String(new char[] { 'Y', 'o', 'd', 'a' });
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotEqualToIgnoringCase(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase(actual, expected).create());
  }

  @Test
  void should_fail_if_both_Strings_are_equal_ignoring_case() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotEqualToIgnoringCase("YODA"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase(actual, "YODA").create());
  }

  @Test
  void should_pass_if_actual_is_null_and_expected_is_not_whatever_custom_comparison_strategy_is() {
    assertThat((String) null).usingComparator(CaseInsensitiveStringComparator.INSTANCE).isNotEqualToIgnoringCase("Luke");
  }

  @Test
  void should_pass_if_both_Strings_are_not_equal_regardless_of_case_whatever_custom_comparison_strategy_is() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).isNotEqualToIgnoringCase("Luke");
  }

  @Test
  void should_fail_if_both_Strings_are_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .isNotEqualToIgnoringCase(null));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase(null, null).create());
  }

  @Test
  void should_fail_if_both_Strings_are_the_same_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .isNotEqualToIgnoringCase(actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase(actual, actual).create());
  }

  @Test
  void should_fail_if_both_Strings_are_equal_but_not_same_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = "Yoda";
    String expected = new String(new char[] { 'Y', 'o', 'd', 'a' });
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .isNotEqualToIgnoringCase(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase(actual, expected).create());
  }

  @Test
  void should_fail_if_both_Strings_are_equal_ignoring_case_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .isNotEqualToIgnoringCase("YODA"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase(actual, "YODA").create());
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_fail_with_Turkish_default_locale() {
    // GIVEN
    String actual = "Leia";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNotEqualToIgnoringCase("LEIA"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase(actual, "LEIA").create());
  }

}
