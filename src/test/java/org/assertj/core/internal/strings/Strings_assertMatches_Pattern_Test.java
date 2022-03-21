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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldMatchPattern.shouldMatch;
import static org.assertj.core.internal.ErrorMessages.regexPatternIsNull;
import static org.assertj.core.test.TestData.matchAnything;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.regex.Pattern;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertMatches(AssertionInfo, CharSequence, Pattern)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Strings_assertMatches_Pattern_Test extends StringsBaseTest {

  private String actual = "Yoda";

  @Test
  void should_throw_error_if_Pattern_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      Pattern pattern = null;
      strings.assertMatches(someInfo(), actual, pattern);
    }).withMessage(regexPatternIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMatches(someInfo(), null, matchAnything()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_match_Pattern() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMatches(someInfo(), actual,
                                                                                           Pattern.compile("Luke")))
                                                   .withMessage(shouldMatch(actual, "Luke").create());
  }

  @Test
  void should_pass_if_actual_matches_Pattern() {
    strings.assertMatches(someInfo(), actual, Pattern.compile("Yod.*"));
  }

  @Test
  void should_throw_error_if_Pattern_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> {
      Pattern pattern = null;
      stringsWithCaseInsensitiveComparisonStrategy.assertMatches(someInfo(), actual, pattern);
    }).withMessage(regexPatternIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertMatches(someInfo(),
                                                                                                                                null,
                                                                                                                                matchAnything()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_match_Pattern_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> stringsWithCaseInsensitiveComparisonStrategy.assertMatches(info, actual,
                                                                                                      Pattern.compile("Luke")));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldMatch(actual, "Luke"));
  }

  @Test
  void should_pass_if_actual_matches_Pattern_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertMatches(someInfo(), actual, Pattern.compile("Yod.*"));
  }
}
