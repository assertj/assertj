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
import static org.assertj.core.error.ShouldBeEqualIgnoringCase.shouldBeEqual;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.DefaultLocale;

/// @author Alex Ruiz
/// @author Joel Costigliola
class CharSequenceAssert_isEqualToIgnoringCase_Test {

  @Test
  void should_fail_if_actual_is_null_and_expected_is_not() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToIgnoringCase("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqual(null, "Luke").create());
  }

  @Test
  void should_fail_if_actual_is_not_null_and_expected_is() {
    // GIVEN
    String actual = "Luke";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToIgnoringCase(null));
    // THEN
    then(assertionError).hasMessage(shouldBeEqual(actual, null).create());
  }

  @Test
  void should_fail_if_both_Strings_are_not_equal_regardless_of_case() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isEqualToIgnoringCase("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqual(actual, "Luke").create());
  }

  @Test
  void should_pass_if_both_Strings_are_null() {
    assertThat((String) null).isEqualToIgnoringCase(null);
  }

  @Test
  void should_pass_if_both_Strings_are_the_same() {
    // GIVEN
    String actual = "Yoda";
    // WHEN/THEN
    then(actual).isEqualToIgnoringCase(actual);
  }

  @Test
  void should_pass_if_both_Strings_are_equal_but_not_same() {
    assertThat("Yoda").isEqualToIgnoringCase(new String(new char[] { 'Y', 'o', 'd', 'a' }));
  }

  @Test
  void should_pass_if_both_Strings_are_equal_ignoring_case() {
    assertThat("Yoda").isEqualToIgnoringCase("YODA");
  }

  @Test
  void should_fail_if_actual_is_null_and_expected_is_not_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .isEqualToIgnoringCase("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqual(null, "Luke").create());
  }

  @Test
  void should_fail_if_both_Strings_are_not_equal_regardless_of_case_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .isEqualToIgnoringCase("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqual(actual, "Luke").create());
  }

  @Test
  void should_pass_if_both_Strings_are_null_whatever_custom_comparison_strategy_is() {
    assertThat((String) null).usingComparator(CaseInsensitiveStringComparator.INSTANCE).isEqualToIgnoringCase(null);
  }

  @Test
  void should_pass_if_both_Strings_are_the_same_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = "Yoda";
    // WHEN/THEN
    then(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE).isEqualToIgnoringCase(actual);
  }

  @Test
  void should_pass_if_both_Strings_are_equal_but_not_same_whatever_custom_comparison_strategy_is() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                      .isEqualToIgnoringCase(new String(new char[] { 'Y', 'o', 'd', 'a' }));
  }

  @Test
  void should_pass_if_both_Strings_are_equal_ignoring_case_whatever_custom_comparison_strategy_is() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).isEqualToIgnoringCase("YODA");
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_pass_with_Turkish_default_locale() {
    assertThat("Leia").isEqualToIgnoringCase("LEIA");
  }

}
