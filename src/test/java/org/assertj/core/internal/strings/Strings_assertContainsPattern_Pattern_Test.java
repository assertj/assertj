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

import static org.assertj.core.error.ShouldContainPattern.shouldContainPattern;
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
 * Tests for <code>{@link Strings#assertContainsPattern(AssertionInfo, CharSequence, Pattern)}</code>.
 * 
 * @author Pierre Templier
 */
public class Strings_assertContainsPattern_Pattern_Test extends StringsBaseTest {

  private static final String CONTAINED_PATTERN = "dark";
  private String actual = "Fear is the path to the dark side. Fear leads to anger. Anger leads to hate. Hate… leads to suffering.";

  @Test
  public void should_throw_error_if_Pattern_is_null() {
    thrown.expectNullPointerException(regexPatternIsNull());
    Pattern pattern = null;
    strings.assertContainsPattern(someInfo(), actual, pattern);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertContainsPattern(someInfo(), null, matchAnything());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_Pattern() {
    thrown.expectAssertionError(shouldContainPattern(actual, "Luke"));
    strings.assertContainsPattern(someInfo(), actual, Pattern.compile("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_Pattern() {
    strings.assertContainsPattern(someInfo(), actual, Pattern.compile(CONTAINED_PATTERN));
  }

  @Test
  public void should_throw_error_if_Pattern_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectNullPointerException(regexPatternIsNull());
    Pattern pattern = null;
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsPattern(someInfo(), actual, pattern);
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsPattern(someInfo(), null, matchAnything());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_Pattern_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(shouldContainPattern(actual, "Luke"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsPattern(someInfo(), actual, Pattern.compile("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_Pattern_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsPattern(someInfo(), actual, Pattern.compile(CONTAINED_PATTERN));
  }
}
