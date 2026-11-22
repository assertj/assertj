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
import static org.assertj.core.error.ShouldMatchPattern.shouldMatch;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.regexPatternIsNull;
import static org.assertj.tests.core.testkit.TestData.matchAnything;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.regex.Pattern;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Joel Costigliola
class CharSequenceAssert_matches_Pattern_Test {

  private static final String ACTUAL = "Yoda";

  @Test
  void should_throw_error_if_Pattern_is_null() {
    // GIVEN
    Pattern pattern = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(ACTUAL).matches(pattern));
    // THEN
    then(npe).hasMessage(regexPatternIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).matches(matchAnything()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_match_Pattern() {
    // GIVEN
    Pattern pattern = Pattern.compile("Luke");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).matches(pattern));
    // THEN
    then(assertionError).hasMessage(shouldMatch(ACTUAL, "Luke").create());
  }

  @Test
  void should_pass_if_actual_matches_Pattern() {
    assertThat(ACTUAL).matches(Pattern.compile("Yod.*"));
  }

  @Test
  void should_throw_error_if_Pattern_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Pattern pattern = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                .matches(pattern));
    // THEN
    then(npe).hasMessage(regexPatternIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .matches(matchAnything()));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_match_Pattern_whatever_custom_comparison_strategy_is() {
    // GIVEN
    Pattern pattern = Pattern.compile("Luke");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .matches(pattern));
    // THEN
    then(assertionError).hasMessage(shouldMatch(ACTUAL, "Luke").create());
  }

  @Test
  void should_pass_if_actual_matches_Pattern_whatever_custom_comparison_strategy_is() {
    assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE).matches(Pattern.compile("Yod.*"));
  }

}
