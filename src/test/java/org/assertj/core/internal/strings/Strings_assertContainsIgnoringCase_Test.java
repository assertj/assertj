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
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertContainsIgnoringCase(AssertionInfo, CharSequence, CharSequence)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Strings_assertContainsIgnoringCase_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_does_not_contain_sequence() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsIgnoringCase(someInfo(), "Yoda",
                                                                                                        "Luke"))
                                                   .withMessage(shouldContainIgnoringCase("Yoda", "Luke").create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsIgnoringCase(someInfo(), "Yoda", null))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsIgnoringCase(someInfo(), null, "Yoda"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    strings.assertContainsIgnoringCase(someInfo(), "Yoda", "Yo");
  }

  @Test
  void should_pass_if_actual_contains_sequence_in_different_case() {
    strings.assertContainsIgnoringCase(someInfo(), "Yoda", "yo");
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(),
                                                                                                                                             "Yoda",
                                                                                                                                             "Luke"))
                                                   .withMessage(shouldContainIgnoringCase("Yoda", "Luke").create());
  }

  @Test
  void should_throw_error_if_sequence_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(),
                                                                                                                              "Yoda",
                                                                                                                              null))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(),
                                                                                                                                             null,
                                                                                                                                             "Yoda"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_sequence_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(), "Yoda", "Yo");
  }

  @Test
  void should_pass_if_actual_contains_sequence_in_different_case_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringCase(someInfo(), "Yoda", "yo");
  }
}
