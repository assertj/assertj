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

import java.util.regex.PatternSyntaxException;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Strings#assertDoesNotContainPattern(AssertionInfo, CharSequence, CharSequence)}</code>.
 */
public class Strings_assertDoesNotContainPattern_CharSequence_Test extends StringsBaseTest {

  private static final String CONTAINED_PATTERN = "y.*u?";
  private static final String NOT_CONTAINED_PATTERN = "Y.*U?";
  private static final String ACTUAL = "No soup for you!";

  @Test
  public void should_throw_error_if_regular_expression_is_null() {
    thrown.expectNullPointerException(regexPatternIsNull());
    final String nullRegex = null;
    strings.assertDoesNotContainPattern(someInfo(), ACTUAL, nullRegex);
  }

  @Test
  public void should_throw_error_if_syntax_of_regular_expression_is_invalid() {
    thrown.expect(PatternSyntaxException.class);
    strings.assertDoesNotContainPattern(someInfo(), ACTUAL, "*...");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertDoesNotContainPattern(someInfo(), null, matchAnything().pattern());
  }

  @Test
  public void should_fail_if_actual_contains_regular_expression() {
    thrown.expectAssertionError(shouldNotContainPattern(ACTUAL, CONTAINED_PATTERN));
    strings.assertDoesNotContainPattern(someInfo(), ACTUAL, CONTAINED_PATTERN);
  }

  @Test
  public void should_pass_if_actual_does_not_contain_regular_expression() {
    strings.assertDoesNotContainPattern(someInfo(), ACTUAL, NOT_CONTAINED_PATTERN);
  }

  @Test
  public void should_throw_error_if_regular_expression_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(regexPatternIsNull());
    String nullRegex = null;
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainPattern(someInfo(), ACTUAL, nullRegex);
  }

  @Test
  public void should_throw_error_if_syntax_of_regular_expression_is_invalid_whatever_custom_comparison_strategy_is() {
    thrown.expect(PatternSyntaxException.class);
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainPattern(someInfo(), ACTUAL, "*...");
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainPattern(someInfo(), null,
                                                                             matchAnything().pattern());
  }

  @Test
  public void should_fail_if_actual_contains_regular_expression_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(shouldNotContainPattern(ACTUAL, CONTAINED_PATTERN));
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainPattern(someInfo(), ACTUAL, CONTAINED_PATTERN);
  }

  @Test
  public void should_pass_if_actual_does_not_contain_regular_expression_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainPattern(someInfo(), ACTUAL, NOT_CONTAINED_PATTERN);
  }
}
