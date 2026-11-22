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

import static java.lang.System.lineSeparator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveLineCount.shouldHaveLinesCount;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Mariusz Smykula
class CharSequenceAssert_hasLineCount_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasLineCount(3));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_lines_count_of_actual_is_not_equal_to_expected_size() {
    // GIVEN
    String actual = "Begin" + lineSeparator() + "End";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasLineCount(6));
    // THEN
    then(assertionError).hasMessage(shouldHaveLinesCount(actual, 2, 6).create());
  }

  @Test
  void should_pass_if_lines_count_of_actual_is_equal_to_expected_lines_count() {
    // GIVEN
    String actual = "Begin" + lineSeparator() + "Middle%nEnd".formatted();
    // WHEN/THEN
    then(actual).hasLineCount(3);
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .hasLineCount(3));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_lines_count_of_actual_is_not_equal_to_expected_lines_count_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = "Begin" + lineSeparator() + "End";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .hasLineCount(3));
    // THEN
    then(assertionError).hasMessage(shouldHaveLinesCount(actual, 2, 3).create());
  }

  @Test
  void should_pass_if_lines_count_of_actual_is_equal_to_expected_lines_count_whatever_custom_comparison_strategy_is() {
    // GIVEN
    String actual = "Begin" + lineSeparator() + "End";
    // WHEN/THEN
    then(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE).hasLineCount(2);
  }

}
