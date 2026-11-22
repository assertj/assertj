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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainPattern.shouldContainPattern;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.regexPatternIsNull;
import static org.assertj.tests.core.testkit.TestData.matchAnything;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.regex.PatternSyntaxException;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Pierre Templier
class CharSequenceAssert_containsPattern_String_Test {

  private static final String CONTAINED_PATTERN = "dark";
  private static final String ACTUAL = "Fear is the path to the dark side. Fear leads to anger. Anger leads to hate. Hate… leads to suffering.";

  @Test
  void should_throw_error_if_regular_expression_is_null() {
    // GIVEN
    CharSequence regex = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(ACTUAL).containsPattern(regex));
    // THEN
    then(npe).hasMessage(regexPatternIsNull());
  }

  @Test
  void should_throw_error_if_syntax_of_regular_expression_is_invalid() {
    // WHEN
    var throwable = catchThrowable(() -> assertThat(ACTUAL).containsPattern("*..."));
    // THEN
    then(throwable).isInstanceOf(PatternSyntaxException.class);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsPattern(matchAnything().pattern()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_regular_expression() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).containsPattern("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldContainPattern(ACTUAL, "Luke").create());
  }

  @Test
  void should_pass_if_actual_contains_pattern() {
    assertThat(ACTUAL).containsPattern(CONTAINED_PATTERN);
  }

  @Test
  void should_throw_error_if_regular_expression_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    CharSequence regex = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                .containsPattern(regex));
    // THEN
    then(npe).hasMessage(regexPatternIsNull());
  }

  @Test
  void should_throw_error_if_syntax_of_regular_expression_is_invalid_whatever_custom_comparison_strategy_is() {
    // WHEN
    var throwable = catchThrowable(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                           .containsPattern("*..."));
    // THEN
    then(throwable).isInstanceOf(PatternSyntaxException.class);
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsPattern(matchAnything().pattern()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_regular_expression_whatever_custom_comparison_strategy_is() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .containsPattern("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldContainPattern(ACTUAL, "Luke").create());
  }

  @Test
  void should_pass_if_actual_contains_pattern_whatever_custom_comparison_strategy_is() {
    assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE).containsPattern(CONTAINED_PATTERN);
  }

}
