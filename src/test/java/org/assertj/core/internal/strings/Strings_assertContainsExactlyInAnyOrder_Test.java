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
 * Copyright 2012-2017 the original author or authors.
 */

package org.assertj.core.internal.strings;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.*;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.TestData.*;
import static org.assertj.core.util.FailureMessages.*;
import static org.assertj.core.util.Lists.newArrayList;

/**
 * Tests for <code>{@link Strings#assertContainsExactlyInAnyOrder(AssertionInfo, CharSequence, CharSequence[])}</code>.
 *
 * @author Billy Yuan
 */
public class Strings_assertContainsExactlyInAnyOrder_Test extends StringsBaseTest {
  String actual = "Practice makes perfect!";

  @Test
  public void should_pass_if_actual_exactly_contains_values_in_order() {
    String[] values = { "Practice ", "makes", " ", "perfect!" };
    strings.assertContainsExactlyInAnyOrder(someInfo(), actual, values);
  }

  @Test
  public void should_pass_if_actual_exactly_contains_values_without_order() {
    String[] values = { " makes ", "Practice", "!", "perfect" };
    strings.assertContainsExactlyInAnyOrder(someInfo(), actual, values);
  }

  @Test
  public void should_pass_if_actual_exactly_contains_values_according_to_custom_comparison_strategy() {
    String[] values = { "practice ", "MAKES ", "!", "PerFECT" };
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, values);
  }

  @Test
  public void should_throw_error_if_actual_is_null() {
    String[] values = { "hey", "there" };
    thrown.expectAssertionError(actualIsNull());
    strings.assertContainsExactlyInAnyOrder(someInfo(), null, values);
  }

  @Test
  public void should_throw_exception_if_the_array_of_values_is_null() {
    thrown.expectNullPointerException(arrayOfValuesToLookForIsNull());
    strings.assertContainsExactlyInAnyOrder(someInfo(), actual, null);
  }

  @Test
  public void should_throw_exception_if_one_of_values_is_null() {
    String[] values = { null, "Practice makes perfect", "!" };
    thrown.expectNullPointerException("Expecting CharSequence elements not to be null but found one at index 0");
    strings.assertContainsExactlyInAnyOrder(someInfo(), actual, values);
  }

  @Test
  public void should_throw_exception_if_the_array_of_values_is_empty() {
    String[] values = {};
    thrown.expectIllegalArgumentException(arrayOfValuesToLookForIsEmpty());
    strings.assertContainsExactlyInAnyOrder(someInfo(), actual, values);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values() {
    String actual = "Yoda";
    String[] values = { "Lu", "ke" };
    thrown.expectAssertionError(
      shouldContainExactlyInAnyOrder(actual, values, Arrays.asList("L", "u", "k", "e"),
                                     Arrays.asList("Y", "o", "d", "a"),
                                     StandardComparisonStrategy.instance()));
    strings.assertContainsExactlyInAnyOrder(someInfo(), actual, values);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_values_according_to_custom_comparison_strategy() {
    String actual = "YODA";
    String[] values = { "lu", "ke" };
    thrown.expectAssertionError(
      shouldContainExactlyInAnyOrder(actual, values, Arrays.asList("l", "u", "k", "e"),
                                     Arrays.asList("Y", "O", "D", "A"),
                                     comparisonStrategy));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsExactlyInAnyOrder(someInfo(), actual, values);
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    String actual = "LukeLeiaLuke";
    String[] values = { "Luke", "Leia" };
    thrown.expectAssertionError(
      shouldContainExactlyInAnyOrder(actual, values, newArrayList(),
                                     newArrayList("L", "u", "k", "e"),
                                     StandardComparisonStrategy.instance()));
    strings.assertContainsExactlyInAnyOrder(someInfo(), actual, values);
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    String actual = "LukeLeia";
    String[] values = { "Luke", "Leia", "Luke" };
    thrown.expectAssertionError(
      shouldContainExactlyInAnyOrder(actual, values, newArrayList("L", "u", "k", "e"),
                                     newArrayList(),
                                     StandardComparisonStrategy.instance()));
    strings.assertContainsExactlyInAnyOrder(someInfo(), actual, values);
  }
}
