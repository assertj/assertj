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
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.DefaultLocale;

/// @author Alex Ruiz
/// @author Joel Costigliola
class CharSequenceAssert_containsIgnoringCase_CharSequence_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsIgnoringCase("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").containsIgnoringCase(null));
    // THEN
    then(exception).hasMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").containsIgnoringCase("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringCase("Yoda", "Luke").create());
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    assertThat("Yoda").containsIgnoringCase("Yo");
  }

  @Test
  void should_pass_if_actual_contains_sequence_in_different_case() {
    assertThat("Yoda").containsIgnoringCase("yo");
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsIgnoringCase("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsIgnoringCase(null));
    // THEN
    then(exception).hasMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_whatever_custom_comparison_strategy_is() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsIgnoringCase("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringCase("Yoda", "Luke").create());
  }

  @Test
  void should_pass_if_actual_contains_sequence_whatever_custom_comparison_strategy_is() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsIgnoringCase("Yo");
  }

  @Test
  void should_pass_if_actual_contains_sequence_in_different_case_whatever_custom_comparison_strategy_is() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsIgnoringCase("yo");
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_pass_with_Turkish_default_locale() {
    assertThat("Leia").containsIgnoringCase("IA");
  }

}
