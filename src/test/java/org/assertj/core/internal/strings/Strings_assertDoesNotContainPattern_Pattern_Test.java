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

import static org.assertj.core.error.ShouldNotContainPattern.shouldNotContainPattern;
import static org.assertj.core.internal.ErrorMessages.regexPatternIsNull;
import static org.assertj.core.test.TestData.matchAnything;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.regex.Pattern;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Strings#assertDoesNotContainPattern(AssertionInfo, CharSequence, Pattern)}</code>.
 */
public class Strings_assertDoesNotContainPattern_Pattern_Test extends StringsBaseTest {

  private static final String CONTAINED_PATTERN = "y.*u?";
  private static final String NOT_CONTAINED_PATTERN = "Y.*U?";
  private static final String ACTUAL = "No soup for you!";

  @Test
  public void should_throw_error_if_pattern_is_null() {
    thrown.expectNullPointerException(regexPatternIsNull());
    Pattern nullPattern = null;
    strings.assertDoesNotContainPattern(someInfo(), ACTUAL, nullPattern);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertDoesNotContainPattern(someInfo(), null, matchAnything());
  }

  @Test
  public void should_fail_if_actual_contains_pattern() {
    thrown.expectAssertionError(shouldNotContainPattern(ACTUAL, CONTAINED_PATTERN));
    strings.assertDoesNotContainPattern(someInfo(), ACTUAL, Pattern.compile(CONTAINED_PATTERN));
  }

  @Test
  public void should_pass_if_actual_does_not_contain_pattern() {
    strings.assertDoesNotContainPattern(someInfo(), ACTUAL, Pattern.compile(NOT_CONTAINED_PATTERN));
  }

  @Test
  public void should_throw_error_if_pattern_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(regexPatternIsNull());
    Pattern nullPattern = null;
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainPattern(someInfo(), ACTUAL, nullPattern);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainPattern(someInfo(), null, matchAnything());
  }

  @Test
  public void should_fail_if_actual_contains_pattern_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(shouldNotContainPattern(ACTUAL, CONTAINED_PATTERN));
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainPattern(someInfo(), ACTUAL,
                                                                             Pattern.compile(CONTAINED_PATTERN));
  }

  @Test
  public void should_pass_if_actual_does_not_contain_pattern_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainPattern(someInfo(), ACTUAL,
                                                                             Pattern.compile(NOT_CONTAINED_PATTERN));
  }
}
