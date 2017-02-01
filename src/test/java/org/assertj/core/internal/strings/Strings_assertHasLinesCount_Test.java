/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldHaveLineCount.shouldHaveLinesCount;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertHasLineCount(org.assertj.core.api.AssertionInfo, CharSequence, int)}</code>.
 *
 * @author Mariusz Smykula
 */
public class Strings_assertHasLinesCount_Test extends StringsBaseTest {

  public static final String LINE_SEPARATOR = System.getProperty("line.separator");

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertHasLineCount(someInfo(), null, 3);
  }

  @Test
  public void should_fail_if_lines_count_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    String actual = "Begin" + LINE_SEPARATOR + "End";

    thrown.expectAssertionError(shouldHaveLinesCount(actual, 2, 6).create());

    strings.assertHasLineCount(info, actual, 6);
  }

  @Test
  public void should_pass_if_lines_count_of_actual_is_equal_to_expected_lines_count() {
    strings.assertHasLineCount(someInfo(), String.format("Begin" + LINE_SEPARATOR + "Middle%nEnd"), 3);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    stringsWithCaseInsensitiveComparisonStrategy.assertHasLineCount(someInfo(), null, 3);
  }

  @Test
  public void should_fail_if_lines_count_of_actual_is_not_equal_to_expected_lines_count_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    String actual = "Begin" + LINE_SEPARATOR + "End";

    thrown.expectAssertionError(shouldHaveLinesCount(actual, 2, 3).create());

    stringsWithCaseInsensitiveComparisonStrategy.assertHasLineCount(info, actual, 3);
  }

  @Test
  public void should_pass_if_lines_count_of_actual_is_equal_to_expected_lines_count_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertHasLineCount(someInfo(), "Begin" + LINE_SEPARATOR + "End", 2);
  }
}
