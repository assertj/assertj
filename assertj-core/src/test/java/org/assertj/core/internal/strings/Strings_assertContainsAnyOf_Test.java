/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

class Strings_assertContainsAnyOf_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    CharSequence actual = null;
    CharSequence[] values = array("Yoda", "Luke");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsAnyOf(someInfo(), actual, values));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    CharSequence actual = "Master Yoda";
    CharSequence[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> strings.assertContainsAnyOf(someInfo(), actual, values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The array of values to look for should not be null");
  }

  @Test
  void should_fail_if_values_is_empty() {
    // GIVEN
    CharSequence actual = "Master Yoda";
    CharSequence[] values = array();
    // WHEN
    Throwable thrown = catchThrowable(() -> strings.assertContainsAnyOf(someInfo(), actual, values));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The array of values to look for should not be empty");
  }

  @Test
  void should_fail_if_values_contains_null() {
    // GIVEN
    CharSequence actual = "Master Yoda";
    CharSequence[] values = array("Yoda", "Luke", null);
    // WHEN
    Throwable thrown = catchThrowable(() -> strings.assertContainsAnyOf(someInfo(), actual, values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("Expecting CharSequence elements not to be null but found one at index 2");
  }

  @Test
  void should_fail_if_actual_does_not_contain_any_value() {
    // GIVEN
    CharSequence actual = "Leia";
    CharSequence[] values = array("Yoda", "Luke");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsAnyOf(someInfo(), actual, values));
    // THEN
    then(assertionError).hasMessage(shouldContainAnyOf(actual, values, StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_contains_any_value() {
    // GIVEN
    CharSequence actual = "Master Yoda";
    CharSequence[] values = array("Yoda", "Luke");
    // WHEN/THEN
    strings.assertContainsAnyOf(someInfo(), actual, values);
  }

  @Test
  void should_pass_if_actual_contains_any_value_according_to_custom_comparison_strategy() {
    // GIVEN
    CharSequence actual = "Master Yoda";
    CharSequence[] values = array("YODA", "LUKE");
    // WHEN/THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsAnyOf(someInfo(), actual, values);
  }

}
