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

import java.util.regex.Pattern;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertDoesNotMatch(AssertionInfo, CharSequence, Pattern)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Strings_assertDoesNotMatch_Pattern_Test extends StringsBaseTest {

  private String actual = "Yoda";

  @Test
  void should_throw_error_if_Pattern_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      Pattern pattern = null;
      strings.assertDoesNotMatch(someInfo(), actual, pattern);
    }).withMessage(regexPatternIsNull());
  }

  @Test
  void should_fail_if_actual_matches_Pattern() {
    Pattern pattern = matchAnything();
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertDoesNotMatch(someInfo(), actual, pattern))
                                                   .withMessage(shouldNotMatch(actual, pattern.pattern()).create());
  }

  @Test
  void should_pass_if_actual_is_null() {
    strings.assertDoesNotMatch(someInfo(), null, matchAnything());
  }

  @Test
  void should_pass_if_actual_does_not_match_Pattern() {
    strings.assertDoesNotMatch(someInfo(), actual, Pattern.compile("Luke"));
  }

  @Test
  void should_throw_error_if_Pattern_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> {
      Pattern pattern = null;
      stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotMatch(someInfo(), actual, pattern);
    }).withMessage(regexPatternIsNull());
  }

  @Test
  void should_fail_if_actual_matches_Pattern_whatever_custom_comparison_strategy_is() {
    Pattern pattern = matchAnything();
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotMatch(someInfo(),
                                                                                                                                     actual,
                                                                                                                                     pattern))
                                                   .withMessage(shouldNotMatch(actual, pattern.pattern()).create());
  }

  @Test
  void should_pass_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotMatch(someInfo(), null, matchAnything());
  }

  @Test
  void should_pass_if_actual_does_not_match_Pattern_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotMatch(someInfo(), actual, Pattern.compile("Luke"));
  }
}
