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
import static org.assertj.core.error.ShouldNotContainSequenceOfCharSequence.shouldNotContainSequence;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

class CharSequenceAssert_doesNotContainSequence_with_var_args_Test {

  private static final String ACTUAL = "{ 'title':'A Game of Thrones', 'author':'George Martin' }";

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSequence("A Game of Thrones"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    // GIVEN
    String[] sequence = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_sequence_is_empty() {
    // GIVEN
    String[] sequence = {};
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(exception).hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_sequence_has_null_item() {
    // GIVEN
    String[] sequence = { "A Game of ", null, "Thrones" };
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(exception).hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  void should_pass_if_actual_does_not_contain_all_sequence_items() {
    assertThat(ACTUAL).doesNotContainSequence("A Game of ", "Shadows");
  }

  @Test
  void should_pass_if_actual_contains_all_sequence_items_but_not_in_the_correct_order() {
    assertThat(ACTUAL).doesNotContainSequence("Thrones", " of ", "Game");
  }

  @Test
  void should_pass_if_actual_contains_all_sequence_items_but_with_characters_between() {
    assertThat(ACTUAL).doesNotContainSequence("A Game of ", "George Martin");
  }

  @Test
  void should_pass_if_actual_is_empty_and_sequence_contains_non_empty_strings() {
    assertThat("").doesNotContainSequence("A Game of ", "Thrones");
  }

  @Test
  void should_fail_if_actual_contains_sequence() {
    // GIVEN
    String[] sequence = { "A Game of ", "Thrones" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(ACTUAL, sequence, 11).create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_according_to_the_custom_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    String[] sequence = { "A GAME OF ", "THRONES" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(ACTUAL, sequence, 11, comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_is_not_empty_and_sequence_contains_only_empty_strings() {
    // GIVEN
    String[] sequence = { "", "", "" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(ACTUAL, sequence, 0).create());
  }

  @Test
  void should_fail_if_actual_is_empty_and_sequence_contains_only_empty_strings() {
    // GIVEN
    String actual = "";
    String[] sequence = { "", "" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContainSequence(sequence));
    // THEN
    then(assertionError).hasMessage(shouldNotContainSequence(actual, sequence, 0).create());
  }

}
