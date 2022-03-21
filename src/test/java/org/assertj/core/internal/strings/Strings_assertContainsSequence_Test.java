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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainSequenceOfCharSequence.shouldContainSequence;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertContainsSequence(AssertionInfo, CharSequence, CharSequence[])}</code>.
 *
 * @author Billy Yuan
 */
class Strings_assertContainsSequence_Test extends StringsBaseTest {
  String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";

  @Test
  void should_pass_if_actual_contains_sequence() {
    String[] sequenceValues = { "{ ", "'title':", "'A Game of Thrones'", "," };
    strings.assertContainsSequence(someInfo(), actual, sequenceValues);
  }

  @Test
  void should_fail_if_actual_contains_sequence_with_values_between() {
    String[] sequenceValues = { "{ ", "'author':'George Martin'}" };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsSequence(someInfo(), actual,
                                                                                                    sequenceValues))
                                                   .withMessage(shouldContainSequence(actual, sequenceValues).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    String[] sequenceValues = { "{ ", "'title':", "'A Game of Thrones'", "unexpectedString" };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsSequence(someInfo(), actual,
                                                                                                    sequenceValues))
                                                   .withMessage(shouldContain(actual, sequenceValues,
                                                                              newLinkedHashSet("unexpectedString")).create());
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_the_given_order() {
    String[] sequenceValues = { "'A Game of Thrones'", "'title':" };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsSequence(someInfo(), actual,
                                                                                                    sequenceValues))
                                                   .withMessage(shouldContainSequence(actual, sequenceValues).create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsSequence(someInfo(), actual, null))
                                    .withMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_any_value_of_sequence_is_null() {
    String[] sequenceValues = { "author", null };
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsSequence(someInfo(), actual,
                                                                                     sequenceValues))
                                    .withMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  void should_throw_error_if_sequence_values_are_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> strings.assertContainsSequence(someInfo(), actual,
                                                                                         new String[0]))
                                        .withMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  void should_fail_if_actual_is_null() {
    String[] sequenceValues = { "{ ", "'title':", "'A Game of Thrones'", "," };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsSequence(someInfo(), null,
                                                                                                    sequenceValues))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_sequence_that_specifies_multiple_times_the_same_value() {
    strings.assertContainsSequence(someInfo(), "a-b-c-", array("a", "-", "b", "-", "c"));
  }

  @Test
  void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), "Yoda", array("Yo", "da"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), "Yoda", array("Yo", "DA"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), "Yoda", array("YO", "dA"));
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(),
                                                                                                                                         "Yoda",
                                                                                                                                         array("Yo",
                                                                                                                                               "da",
                                                                                                                                               "Han")))
                                                   .withMessage(shouldContain("Yoda", array("Yo", "da", "Han"),
                                                                              newLinkedHashSet("Han"),
                                                                              comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_contains_values_but_not_in_given_order_according_to_custom_comparison_strategy() {
    String[] sequenceValues = { ", 'author'", "'A Game of Thrones'" };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(),
                                                                                                                                         actual,
                                                                                                                                         sequenceValues))
                                                   .withMessage(shouldContainSequence(actual, sequenceValues,
                                                                                      comparisonStrategy).create());
  }
}
