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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.strings;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.assertj.core.error.ShouldNotContainSequenceOfCharSequence.shouldNotContainSequence;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

/**
 * Tests for {@link Strings#assertDoesNotContainSequence(AssertionInfo, CharSequence, CharSequence[])}.
 */
class Strings_assertDoesNotContainSequence_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_does_not_contain_all_sequence_items() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequence = { "A Game of ", "Shadows" };

    strings.assertDoesNotContainSequence(someInfo(), actual, sequence);
  }

  @Test
  void should_pass_if_actual_contains_all_sequence_items_but_not_in_the_correct_order() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequence = { "Thrones", " of ", "Game" };

    strings.assertDoesNotContainSequence(someInfo(), actual, sequence);
  }

  @Test
  void should_pass_if_actual_contains_all_sequence_items_but_with_characters_between() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequence = { "A Game of ", "George Martin" };

    strings.assertDoesNotContainSequence(someInfo(), actual, sequence);
  }

  @Test
  void should_pass_if_actual_is_empty_and_sequence_contains_non_empty_strings() {
    String actual = "";
    String[] sequence = { "A Game of ", "Thrones" };

    strings.assertDoesNotContainSequence(someInfo(), actual, sequence);
  }

  @Test
  void should_fail_if_actual_contains_sequence() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequence = { "A Game of ", "Thrones" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSequence(someInfo(), actual,
                                                                                                    sequence));
    then(assertionError).hasMessage(shouldNotContainSequence(actual, sequence, 11).create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_according_to_the_custom_strategy() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequence = { "A GAME OF ", "THRONES" };

    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSequence(someInfo(),
                                                                                                                                         actual,
                                                                                                                                         sequence));
    then(assertionError).hasMessage(shouldNotContainSequence(actual, sequence, 11, comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_is_not_empty_and_sequence_contains_only_empty_strings() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequence = { "", "", "" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSequence(someInfo(), actual,
                                                                                                    sequence));
    then(assertionError).hasMessage(shouldNotContainSequence(actual, sequence, 0).create());
  }

  @Test
  void should_fail_if_actual_is_empty_and_sequence_contains_only_empty_strings() {
    String actual = "";
    String[] sequence = { "", "" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSequence(someInfo(), actual,
                                                                                                    sequence));
    then(assertionError).hasMessage(shouldNotContainSequence(actual, sequence, 0).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    String actual = null;
    String[] sequence = { "A Game of Thrones" };

    AssertionError assertionError = expectAssertionError(() -> strings.assertDoesNotContainSequence(someInfo(), actual,
                                                                                                    sequence));
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequence = null;

    ThrowingCallable assertionCall = () -> strings.assertDoesNotContainSequence(someInfo(), actual, sequence);
    thenThrownBy(assertionCall).isInstanceOf(NullPointerException.class)
                               .hasMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_sequence_is_empty() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequence = {};

    ThrowingCallable assertionCall = () -> strings.assertDoesNotContainSequence(someInfo(), actual, sequence);
    thenThrownBy(assertionCall).isInstanceOf(IllegalArgumentException.class)
                               .hasMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_sequence_has_null_item() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequence = { "A Game of ", null, "Thrones" };

    ThrowingCallable assertionCall = () -> strings.assertDoesNotContainSequence(someInfo(), actual, sequence);
    thenThrownBy(assertionCall).isInstanceOf(NullPointerException.class)
                               .hasMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }
}
