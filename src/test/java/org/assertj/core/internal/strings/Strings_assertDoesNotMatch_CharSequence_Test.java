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
import static org.assertj.core.error.ShouldNotMatchPattern.shouldNotMatch;
import static org.assertj.core.internal.ErrorMessages.regexPatternIsNull;
import static org.assertj.core.test.TestData.matchAnything;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.regex.PatternSyntaxException;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertDoesNotMatch(AssertionInfo, CharSequence, CharSequence)}</code>.
 * 
 * @author Alex Ruiz
 */
class Strings_assertDoesNotMatch_CharSequence_Test extends StringsBaseTest {

  private String actual = "Yoda";

  @Test
  void should_throw_error_if_regular_expression_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      String pattern = null;
      strings.assertDoesNotMatch(someInfo(), actual, pattern);
    }).withMessage(regexPatternIsNull());
  }

  @Test
  void should_throw_error_if_syntax_of_regular_expression_is_invalid() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> strings.assertDoesNotMatch(someInfo(), actual,
                                                                                                  "*..."));
  }

  @Test
  void should_fail_if_actual_matches_regular_expression() {
    String regex = matchAnything().pattern();
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertDoesNotMatch(someInfo(), actual, regex))
                                                   .withMessage(shouldNotMatch(actual, regex).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertDoesNotMatch(someInfo(), null,
                                                                                                matchAnything().pattern()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_does_not_match_regular_expression() {
    strings.assertDoesNotMatch(someInfo(), actual, "Luke");
  }

  @Test
  void should_throw_error_if_regular_expression_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> {
      String pattern = null;
      stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotMatch(someInfo(), actual, pattern);
    }).withMessage(regexPatternIsNull());
  }

  @Test
  void should_throw_error_if_syntax_of_regular_expression_is_invalid_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(PatternSyntaxException.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotMatch(someInfo(),
                                                                                                                                             actual,
                                                                                                                                             "*..."));
  }

  @Test
  void should_fail_if_actual_matches_regular_expression_whatever_custom_comparison_strategy_is() {
    String regex = matchAnything().pattern();
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotMatch(someInfo(),
                                                                                                                                     actual,
                                                                                                                                     regex))
                                                   .withMessage(shouldNotMatch(actual, regex).create());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotMatch(someInfo(),
                                                                                                                                     null,
                                                                                                                                     matchAnything().pattern()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_does_not_match_regular_expression_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotMatch(someInfo(), actual, "Luke");
  }
}
