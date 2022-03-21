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
import static org.assertj.core.error.ShouldNotStartWith.shouldNotStartWith;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertDoesNotStartWith(AssertionInfo, CharSequence, CharSequence)}</code>.
 *
 * @author Michal Kordas
 */
class Strings_assertDoesNotStartWith_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_starts_with_prefix() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertDoesNotStartWith(someInfo(), "Yoda", "Yo"))
                                                   .withMessage(shouldNotStartWith("Yoda", "Yo").create());
  }

  @Test
  void should_throw_error_if_prefix_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertDoesNotStartWith(someInfo(), "Yoda", null))
                                    .withMessage("The given prefix should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertDoesNotStartWith(someInfo(), null, "Yoda"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_does_not_start_with_prefix() {
    strings.assertDoesNotStartWith(someInfo(), "Yoda", "Luke");
    strings.assertDoesNotStartWith(someInfo(), "Yoda", "YO");
  }

  @Test
  void should_pass_if_actual_does_not_start_with_prefix_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotStartWith(someInfo(), "Yoda", "Luke");
  }

  @Test
  void should_fail_if_actual_starts_with_prefix_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotStartWith(someInfo(),
                                                                                                                                         "Yoda",
                                                                                                                                         "yODA"))
                                                   .withMessage(shouldNotStartWith("Yoda", "yODA", comparisonStrategy).create());
  }

}
