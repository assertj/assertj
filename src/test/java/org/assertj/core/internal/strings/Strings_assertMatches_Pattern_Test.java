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

import static org.assertj.core.error.ShouldMatchPattern.shouldMatch;
import static org.assertj.core.internal.ErrorMessages.regexPatternIsNull;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import java.util.regex.Pattern;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Strings#assertMatches(AssertionInfo, CharSequence, Pattern)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Strings_assertMatches_Pattern_Test extends StringsBaseTest {

  private String actual = "Yoda";

  @Test
  public void should_throw_error_if_Pattern_is_null() {
    thrown.expectNullPointerException(regexPatternIsNull());
    Pattern pattern = null;
    strings.assertMatches(someInfo(), actual, pattern);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertMatches(someInfo(), null, matchAnything());
  }

  @Test
  public void should_fail_if_actual_does_not_match_Pattern() {
    thrown.expectAssertionError(shouldMatch(actual, "Luke"));
    strings.assertMatches(someInfo(), actual, Pattern.compile("Luke"));
  }

  @Test
  public void should_pass_if_actual_matches_Pattern() {
    strings.assertMatches(someInfo(), actual, Pattern.compile("Yod.*"));
  }

  @Test
  public void should_throw_error_if_Pattern_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(regexPatternIsNull());
    Pattern pattern = null;
    stringsWithCaseInsensitiveComparisonStrategy.assertMatches(someInfo(), actual, pattern);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    stringsWithCaseInsensitiveComparisonStrategy.assertMatches(someInfo(), null, matchAnything());
  }

  @Test
  public void should_fail_if_actual_does_not_match_Pattern_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertMatches(info, actual, Pattern.compile("Luke"));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldMatch(actual, "Luke"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_matches_Pattern_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertMatches(someInfo(), actual, Pattern.compile("Yod.*"));
  }
}
