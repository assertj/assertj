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

import static org.assertj.core.error.ShouldBeSubstring.shouldBeSubstring;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

public class Strings_assertIsSubstringOf_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_actual_is_a_substring_of_given_string() {
    strings.assertIsSubstringOf(someInfo(), "Yo", "Yoda");
  }

  @Test
  public void should_pass_if_actual_is_equal_to_given_string() {
    strings.assertIsSubstringOf(someInfo(), "Yoda", "Yoda");
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    strings.assertIsSubstringOf(someInfo(), "", "Yoda");
    strings.assertIsSubstringOf(someInfo(), "", "");
  }

  @Test
  public void should_fail_if_actual_contains_given_string() {
    thrown.expectAssertionError(shouldBeSubstring("Yoda", "oda", StandardComparisonStrategy.instance()));
    strings.assertIsSubstringOf(someInfo(), "Yoda", "oda");
  }

  @Test
  public void should_fail_if_actual_completely_different_from_given_string() {
    thrown.expectAssertionError(shouldBeSubstring("Yoda", "Luke", StandardComparisonStrategy.instance()));
    strings.assertIsSubstringOf(someInfo(), "Yoda", "Luke");
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException("Expecting CharSequence not to be null");
    strings.assertIsSubstringOf(someInfo(), "Yoda", null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertIsSubstringOf(someInfo(), null, "Yoda");
  }

  @Test
  public void should_pass_if_actual_is_a_part_of_sequence_only_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertIsSubstringOf(someInfo(), "Yo", "Yoda");
    stringsWithCaseInsensitiveComparisonStrategy.assertIsSubstringOf(someInfo(), "yo", "Yoda");
    stringsWithCaseInsensitiveComparisonStrategy.assertIsSubstringOf(someInfo(), "YO", "Yoda");
  }

  @Test
  public void should_fail_if_actual_is_not_a_substring_of_sequence_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError(shouldBeSubstring("Yoda", "Luke", comparisonStrategy));
    stringsWithCaseInsensitiveComparisonStrategy.assertIsSubstringOf(someInfo(), "Yoda", "Luke");
  }

}
