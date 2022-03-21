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

import static java.lang.System.lineSeparator;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveLineCount.shouldHaveLinesCount;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertHasLineCount(org.assertj.core.api.AssertionInfo, CharSequence, int)}</code>.
 *
 * @author Mariusz Smykula
 */
class Strings_assertHasLinesCount_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertHasLineCount(someInfo(), null, 3))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_lines_count_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    String actual = "Begin" + lineSeparator() + "End";

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertHasLineCount(info, actual, 6))
                                                   .withMessage(shouldHaveLinesCount(actual, 2, 6).create());
  }

  @Test
  void should_pass_if_lines_count_of_actual_is_equal_to_expected_lines_count() {
    strings.assertHasLineCount(someInfo(), String.format("Begin" + lineSeparator() + "Middle%nEnd"), 3);
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertHasLineCount(someInfo(),
                                                                                                                                     null,
                                                                                                                                     3))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_lines_count_of_actual_is_not_equal_to_expected_lines_count_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    String actual = "Begin" + lineSeparator() + "End";

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertHasLineCount(info,
                                                                                                                                     actual,
                                                                                                                                     3))
                                                   .withMessage(shouldHaveLinesCount(actual, 2, 3).create());
  }

  @Test
  void should_pass_if_lines_count_of_actual_is_equal_to_expected_lines_count_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertHasLineCount(someInfo(), "Begin" + lineSeparator() + "End", 2);
  }
}
