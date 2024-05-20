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

import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContainIgnoringCase;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.set;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultLocale;

/**
 * @author Brummolix
 */
class Strings_assertDoesNotContainIgnoringCase_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_does_not_contain_value_ignoring_case() {
    // WHEN/THEN
    strings.assertDoesNotContainIgnoringCase(someInfo(), "Yoda", "no");
  }

  @Test
  void should_pass_if_actual_does_not_contain_values_ignoring_case() {
    // WHEN/THEN
    strings.assertDoesNotContainIgnoringCase(someInfo(), "Yoda", "no", "also no");
  }

  @Test
  void should_fail_if_actual_contains_value() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainIgnoringCase(someInfo(), actual,
                                                                                                        "od"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, "od").create());
  }

  @Test
  void should_fail_if_actual_contains_value_with_different_case() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainIgnoringCase(someInfo(), actual,
                                                                                                        "OD"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, "OD").create());
  }

  @Test
  void should_fail_if_actual_contains_one_of_several_values() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainIgnoringCase(someInfo(), actual, "od",
                                                                                                        "Yo", "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, array("od", "Yo", "Luke"), set("od", "Yo")).create());
  }

  @Test
  void should_fail_if_actual_contains_one_of_several_values_with_different_case() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainIgnoringCase(someInfo(), actual, "OD",
                                                                                                        "yo", "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, array("OD", "yo", "Luke"), set("OD", "yo")).create());
  }

  @Test
  void should_fail_if_values_are_null() {
    // GIVEN
    CharSequence[] values = null;
    // WHEN
    NullPointerException exception = catchNullPointerException(() -> strings.assertDoesNotContainIgnoringCase(someInfo(), "Yoda",
                                                                                                              values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainIgnoringCase(someInfo(), actual,
                                                                                                        "Yoda"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_values_are_empty() {
    thenIllegalArgumentException().isThrownBy(() -> strings.assertDoesNotContainIgnoringCase(someInfo(), "Yoda"))
                                  .withMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_values_contains_null() {
    // GIVEN
    CharSequence[] values = new CharSequence[] { "1", null };
    // WHEN
    NullPointerException exception = catchNullPointerException(() -> strings.assertDoesNotContainIgnoringCase(someInfo(), "Yoda",
                                                                                                              values));
    // THEN
    then(exception).hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_fail_with_Turkish_default_locale() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainIgnoringCase(INFO, "Leia", "EI"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase("Leia", "EI").create());
  }

}
