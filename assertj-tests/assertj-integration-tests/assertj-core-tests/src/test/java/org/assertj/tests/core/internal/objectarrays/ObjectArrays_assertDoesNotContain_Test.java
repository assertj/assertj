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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ObjectArrays_assertDoesNotContain_Test extends ObjectArraysBaseTest {

  @Test
  void should_pass_if_actual_does_not_contain_given_values() {
    arrays.assertDoesNotContain(INFO, actual, array("Han"));
  }

  @Test
  void should_pass_if_actual_does_not_contain_given_values_even_if_duplicated() {
    arrays.assertDoesNotContain(INFO, actual, array("Han", "Han", "Anakin"));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> arrays.assertDoesNotContain(INFO, actual, emptyArray()))
                                        .withMessage(valuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertDoesNotContain(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertDoesNotContain(INFO, null, array("Yoda")));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_contains_given_values() {
    // GIVEN
    Object[] expected = { "Luke", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> arrays.assertDoesNotContain(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO, shouldNotContain(actual, expected, newLinkedHashSet("Luke", "Yoda")));
  }

  @Test
  void should_pass_if_actual_does_not_contain_given_values_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(INFO, actual, array("Han"));
  }

  @Test
  void should_pass_if_actual_does_not_contain_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertDoesNotContain(INFO, actual, array("Han", "HAn", "Anakin"));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null_whatever_custom_comparison_strategy_is() {
    assertThatNullPointerException().isThrownBy(() -> arraysWithCustomComparisonStrategy.assertDoesNotContain(INFO,
                                                                                                              actual,
                                                                                                              null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    // GIVEN
    Object[] expected = { "LUKE", "Yoda", "Han" };
    // WHEN
    expectAssertionError(() -> arraysWithCustomComparisonStrategy.assertDoesNotContain(INFO, actual, expected));
    // THEN
    verify(failures).failure(INFO,
                             shouldNotContain(actual, expected, newLinkedHashSet("LUKE", "Yoda"),
                                              caseInsensitiveStringComparisonStrategy));
  }
}
