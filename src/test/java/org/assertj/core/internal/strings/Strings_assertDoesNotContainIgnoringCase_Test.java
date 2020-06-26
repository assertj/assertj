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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContainIgnoringCase;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.internal.util.collections.Sets.newSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertDoesNotContainIgnoringCase(AssertionInfo, CharSequence, CharSequence...)}</code>.
 *
 * @author Brummolix
 */
@DisplayName("Strings assertDoesNotContainIgnoringCase")
public class Strings_assertDoesNotContainIgnoringCase_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_actual_does_not_contain_value_ignoring_case() {
    assertDoesNotContainIgnoringCase("Yoda", "no");
  }

  @Test
  public void should_pass_if_actual_does_not_contain_values_ignoring_case() {
    assertDoesNotContainIgnoringCase("Yoda", "no", "also no");
  }

  @Test
  public void should_fail_if_actual_contains_value() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertDoesNotContainIgnoringCase(actual, "od"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, "od").create());
  }

  @Test
  public void should_fail_if_actual_contains_value_with_different_case() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertDoesNotContainIgnoringCase(actual, "OD"));
    // THEN
    then(assertionError).hasMessage(shouldNotContainIgnoringCase(actual, "OD").create());
  }

  @Test
  public void should_fail_if_actual_contains_one_of_several_values() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertDoesNotContainIgnoringCase(actual, "od", "Yo", "Luke"));
    // THEN
    String message = shouldNotContainIgnoringCase(actual, new CharSequence[] { "od", "Yo", "Luke" }, newSet("od", "Yo")).create();
    then(assertionError).hasMessage(message);
  }

  @Test
  public void should_fail_if_actual_contains_one_of_several_values_with_different_case() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertDoesNotContainIgnoringCase(actual, "OD", "yo", "Luke"));
    // THEN
    String message = shouldNotContainIgnoringCase(actual, new CharSequence[] { "OD", "yo", "Luke" }, newSet("OD", "yo")).create();
    then(assertionError).hasMessage(message);
  }

  @Test
  public void should_fail_if_values_are_null() {
    // GIVEN
    CharSequence[] values = null;
    // WHEN
    Throwable npe = catchThrowable(() -> assertDoesNotContainIgnoringCase("Yoda", values));
    // THEN
    then(npe).isInstanceOf(NullPointerException.class)
             .hasMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertDoesNotContainIgnoringCase(actual, "Yoda"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_throw_error_if_values_are_empty() {
    thenIllegalArgumentException().isThrownBy(() -> assertDoesNotContainIgnoringCase("Yoda"))
                                  .withMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  public void should_throw_error_if_values_contains_null() {
    // GIVEN
    CharSequence[] values = new CharSequence[] { "1", null };
    // WHEN
    Throwable npe = catchThrowable(() -> assertDoesNotContainIgnoringCase("Yoda", values));
    // THEN
    then(npe).isInstanceOf(NullPointerException.class)
             .hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  private void assertDoesNotContainIgnoringCase(CharSequence actual, CharSequence... values) {
    strings.assertDoesNotContainIgnoringCase(someInfo(), actual, values);
  }
}
