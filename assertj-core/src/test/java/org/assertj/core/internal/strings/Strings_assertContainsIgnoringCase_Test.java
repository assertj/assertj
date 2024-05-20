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

import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultLocale;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Strings_assertContainsIgnoringCase_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_does_not_contain_sequence() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringCase(someInfo(), "Yoda", "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringCase("Yoda", "Luke").create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // WHEN
    NullPointerException exception = catchNullPointerException(() -> strings.assertContainsIgnoringCase(someInfo(), "Yoda",
                                                                                                        null));
    // THEN
    then(exception).hasMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringCase(someInfo(), null, "Yoda"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    // WHEN/THEN
    strings.assertContainsIgnoringCase(someInfo(), "Yoda", "Yo");
  }

  @Test
  void should_pass_if_actual_contains_sequence_in_different_case() {
    // WHEN/THEN
    strings.assertContainsIgnoringCase(someInfo(), "Yoda", "yo");
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_whatever_custom_comparison_strategy_is() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(),
                                                                                                                                       "Yoda",
                                                                                                                                       "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringCase("Yoda", "Luke").create());
  }

  @Test
  void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    // WHEN
    NullPointerException exception = catchNullPointerException(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(),
                                                                                                                                             "Yoda",
                                                                                                                                             null));
    // THEN
    then(exception).hasMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(),
                                                                                                                                       null,
                                                                                                                                       "Yoda"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_sequence_whatever_custom_comparison_strategy_is() {
    // WHEN/THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(), "Yoda", "Yo");
  }

  @Test
  void should_pass_if_actual_contains_sequence_in_different_case_whatever_custom_comparison_strategy_is() {
    // WHEN/THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(), "Yoda", "yo");
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_pass_with_Turkish_default_locale() {
    // WHEN/THEN
    strings.assertContainsIgnoringCase(someInfo(), "Leia", "IA");
  }

}
