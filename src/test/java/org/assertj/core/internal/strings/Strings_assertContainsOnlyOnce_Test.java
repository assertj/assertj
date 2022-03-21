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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainCharSequenceOnlyOnce.shouldContainOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertContainsOnlyOnce(AssertionInfo, CharSequence, CharSequence)}</code>.
 */
class Strings_assertContainsOnlyOnce_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_contains_given_string_only_once() {
    strings.assertContainsOnlyOnce(someInfo(), "Yoda", "Yo");
  }

  @Test
  void should_fail_if_actual_contains_given_string_more_than_once() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsOnlyOnce(someInfo(), "Yodayoda",
                                                                                                    "oda"))
                                                   .withMessage(shouldContainOnlyOnce("Yodayoda", "oda", 2).create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_only_once_but_in_different_case() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsOnlyOnce(someInfo(), "Yoda", "yo"))
                                                   .withMessage(shouldContainOnlyOnce("Yoda", "yo", 0).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_given_string() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsOnlyOnce(someInfo(), "Yoda", "Luke"))
                                                   .withMessage(shouldContainOnlyOnce("Yoda", "Luke", 0).create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsOnlyOnce(someInfo(), "Yoda", null))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsOnlyOnce(someInfo(), null, "Yoda"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_sequence_only_once_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), "Yoda", "Yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), "Yoda", "yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), "Yoda", "YO");
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_only_once_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(),
                                                                                                                                         "Yoda",
                                                                                                                                         "Luke"))
                                                   .withMessage(shouldContainOnlyOnce("Yoda", "Luke", 0,
                                                                                      comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_several_times_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(),
                                                                                                                                         "Yoda",
                                                                                                                                         "Luke"))
                                                   .withMessage(shouldContainOnlyOnce("Yoda", "Luke", 0,
                                                                                      comparisonStrategy).create());
  }

}
