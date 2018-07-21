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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainPattern.shouldContainPattern;
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
 * Tests for <code>{@link Strings#assertContainsPattern(AssertionInfo, CharSequence, CharSequence)}</code>.
 * 
 * @author Pierre Templier
 */
public class Strings_assertContainsPattern_CharSequence_Test extends StringsBaseTest {

  private static final String CONTAINED_PATTERN = "dark";
  private String actual = "Fear is the path to the dark side. Fear leads to anger. Anger leads to hate. Hate… leads to suffering.";

  @Test
  public void should_throw_error_if_regular_expression_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      String regex = null;
      strings.assertContainsPattern(someInfo(), actual, regex);
    }).withMessage(regexPatternIsNull());
  }

  @Test
  public void should_throw_error_if_syntax_of_regular_expression_is_invalid() {
    assertThatExceptionOfType(PatternSyntaxException.class).isThrownBy(() -> strings.assertContainsPattern(someInfo(),
                                                                                                           actual,
                                                                                                           "*..."));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsPattern(someInfo(), null, matchAnything().pattern()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_regular_expression() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsPattern(someInfo(), actual, "Luke"))
                                                   .withMessage(shouldContainPattern(actual, "Luke").create());
  }

  @Test
  public void should_pass_if_actual_contains_pattern() {
    strings.assertContainsPattern(someInfo(), actual, CONTAINED_PATTERN);
  }

  @Test
  public void should_throw_error_if_regular_expression_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> {
      String regex = null;
      stringsWithCaseInsensitiveComparisonStrategy.assertContainsPattern(someInfo(), actual, regex);
    }).withMessage(regexPatternIsNull());
  }

  @Test
  public void should_throw_error_if_syntax_of_regular_expression_is_invalid_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(PatternSyntaxException.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsPattern(someInfo(),
                                                                                                                                                actual,
                                                                                                                                                "*..."));
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsPattern(someInfo(), null, matchAnything().pattern()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_regular_expression_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsPattern(someInfo(), actual, "Luke"))
                                                   .withMessage(shouldContainPattern(actual, "Luke").create());
  }

  @Test
  public void should_pass_if_actual_contains_pattern_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsPattern(someInfo(), actual, CONTAINED_PATTERN);
  }
}
