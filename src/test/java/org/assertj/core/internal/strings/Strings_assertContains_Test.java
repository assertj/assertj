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
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Strings_assertContains_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_does_not_contain_sequence() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContains(someInfo(), "Yoda", "Luke"))
                                                   .withMessage(shouldContain("Yoda", "Luke").create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_but_in_different_case() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContains(someInfo(), "Yoda", "yo"))
                                                   .withMessage(shouldContain("Yoda", "yo").create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertContains(someInfo(), "Yoda", (String) null))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContains(someInfo(), null, "Yoda"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    strings.assertContains(someInfo(), "Yoda", "Yo");
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContains(someInfo(), "Yoda", "Yo", "da",
                                                                                            "Han"))
                                                   .withMessage(shouldContain("Yoda", array("Yo", "da", "Han"),
                                                                              newLinkedHashSet("Han")).create());
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings() {
    strings.assertContains(someInfo(), "Yoda", "Yo", "da");
  }

  @Test
  void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", "Yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", "yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", "YO");
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), "Yoda", "YO", "dA");
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(),
                                                                                                                                 "Yoda",
                                                                                                                                 "Luke"))
                                                   .withMessage(shouldContain("Yoda", "Luke", comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(),
                                                                                                                                 "Yoda",
                                                                                                                                 "Yo",
                                                                                                                                 "da",
                                                                                                                                 "Han"))
                                                   .withMessage(shouldContain("Yoda", array("Yo", "da",
                                                                                            "Han"),
                                                                              newLinkedHashSet("Han"), comparisonStrategy)
                                                                                                                          .create());
  }

}
