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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainSubsequenceOfCharSequence.shouldContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.arrayOfValuesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

public class Strings_assertContainsSubsequence_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_actual_contains_subsequence() {
    strings.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "da"));
  }

  @Test
  public void should_pass_if_actual_contains_subsequence_with_values_between() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequenceValues = { "{", "title", "A Game of Thrones", "}" };
    strings.assertContainsSubsequence(someInfo(), actual, sequenceValues);
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_strings() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "da", "Han")))
                                                   .withMessage(shouldContain("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han")).create());
  }

  @Test
  public void should_fail_if_actual_contains_values_but_not_in_given_order() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequenceValues = { "{", "author", "A Game of Thrones", "}" };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsSubsequence(someInfo(), actual, sequenceValues))
                                                   .withMessage(shouldContainSubsequence(actual, sequenceValues, 1).create());
  }

  @Test
  public void should_throw_error_if_subsequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsSubsequence(someInfo(), "Yoda", null))
                                    .withMessage(arrayOfValuesToLookForIsNull());
  }

  @Test
  public void should_throw_error_if_any_value_of_subsequence_is_null() {
    String[] sequenceValues = { "author", null };
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsSubsequence(someInfo(),
                                                                                        "'author':'George Martin'",
                                                                                        sequenceValues))
                                    .withMessage("Expecting CharSequence elements not to be null but found one at index 1");
  }

  @Test
  public void should_throw_error_if_subsequence_values_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> strings.assertContainsSubsequence(someInfo(), "Yoda",
                                                                                            new String[0]))
                                        .withMessage(arrayOfValuesToLookForIsEmpty());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsSubsequence(someInfo(), null, array("Yo", "da")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_contains_subsequence_that_specifies_multiple_times_the_same_value_bug_544() {
    strings.assertContainsSubsequence(someInfo(), "a-b-c-", array("a", "-", "b", "-", "c"));
    strings.assertContainsSubsequence(someInfo(), "{ 'title':'A Game of Thrones', 'author':'George Martin'}",
                                      array("George", " ", "Martin"));
  }

  @Test
  public void should_pass_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "da"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), "Yoda", array("Yo", "DA"));
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), "Yoda", array("YO", "dA"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_subsequence_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(),
                                                                                                                                            "Yoda",
                                                                                                                                            array("Yo",
                                                                                                                                                  "da",
                                                                                                                                                  "Han")))
                                                   .withMessage(shouldContain("Yoda", array("Yo", "da", "Han"),
                                                                              newLinkedHashSet("Han"),
                                                                              comparisonStrategy).create());
  }

  @Test
  public void should_fail_if_actual_contains_values_but_not_in_given_order_according_to_custom_comparison_strategy() {
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String[] sequenceValues = { "{", "author", "A Game of Thrones", "}" };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), actual, sequenceValues))
                                                   .withMessage(shouldContainSubsequence(actual, sequenceValues, 1, comparisonStrategy).create());
  }

}
