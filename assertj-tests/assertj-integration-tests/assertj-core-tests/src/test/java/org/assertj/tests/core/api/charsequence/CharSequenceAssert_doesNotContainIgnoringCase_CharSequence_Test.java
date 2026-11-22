/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContainIgnoringCase;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.set;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.DefaultLocale;

/// @author Brummolix
class CharSequenceAssert_doesNotContainIgnoringCase_CharSequence_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainIgnoringCase("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_values_are_null() {
    // GIVEN
    CharSequence[] values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").doesNotContainIgnoringCase(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_values_are_empty() {
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat("Yoda").doesNotContainIgnoringCase());
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_values_contains_null() {
    // GIVEN
    CharSequence[] values = { "1", null };
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").doesNotContainIgnoringCase(values));
    // THEN
    then(exception).hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  void should_pass_if_actual_does_not_contain_value_ignoring_case() {
    assertThat("Yoda").doesNotContainIgnoringCase("no");
  }

  @Test
  void should_pass_if_actual_does_not_contain_values_ignoring_case() {
    assertThat("Yoda").doesNotContainIgnoringCase("no", "also no");
  }

  @Test
  void should_fail_if_actual_contains_value() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainIgnoringCase("od"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, "od").create());
  }

  @Test
  void should_fail_if_actual_contains_value_with_different_case() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainIgnoringCase("OD"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, "OD").create());
  }

  @Test
  void should_fail_if_actual_contains_one_of_several_values() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainIgnoringCase("od", "Yo", "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, array("od", "Yo", "Luke"), set("od", "Yo")).create());
  }

  @Test
  void should_fail_if_actual_contains_one_of_several_values_with_different_case() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainIgnoringCase("OD", "yo", "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, array("OD", "yo", "Luke"), set("OD", "yo")).create());
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_fail_with_Turkish_default_locale() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Leia").doesNotContainIgnoringCase("EI"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase("Leia", "EI").create());
  }

}
