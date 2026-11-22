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
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContainPattern.shouldNotContainPattern;
import static org.assertj.core.internal.ErrorMessages.regexPatternIsNull;
import static org.assertj.tests.core.testkit.TestData.matchAnything;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.regex.Pattern;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

class CharSequenceAssert_doesNotContainPattern_Pattern_Test {

  private static final String CONTAINED_PATTERN = "y.*u?";
  private static final String NOT_CONTAINED_PATTERN = "Y.*U?";
  private static final String ACTUAL = "No soup for you!";

  @Test
  void should_throw_error_if_pattern_is_null() {
    // GIVEN
    Pattern pattern = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainPattern(pattern));
    // THEN
    then(npe).hasMessage(regexPatternIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainPattern(matchAnything()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_contains_pattern() {
    // GIVEN
    Pattern pattern = Pattern.compile(CONTAINED_PATTERN);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainPattern(pattern));
    // THEN
    then(assertionError).hasMessage(shouldNotContainPattern(ACTUAL, CONTAINED_PATTERN).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_pattern() {
    assertThat(ACTUAL).doesNotContainPattern(Pattern.compile(NOT_CONTAINED_PATTERN));
  }

  @Test
  void should_throw_error_if_pattern_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Pattern pattern = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                .doesNotContainPattern(pattern));
    // THEN
    then(npe).hasMessage(regexPatternIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotContainPattern(matchAnything()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_contains_pattern_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Pattern pattern = Pattern.compile(CONTAINED_PATTERN);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotContainPattern(pattern));
    // THEN
    then(assertionError).hasMessage(shouldNotContainPattern(ACTUAL, CONTAINED_PATTERN).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_pattern_whatever_custom_comparison_strategy_is() {
    assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                      .doesNotContainPattern(Pattern.compile(NOT_CONTAINED_PATTERN));
  }

}
