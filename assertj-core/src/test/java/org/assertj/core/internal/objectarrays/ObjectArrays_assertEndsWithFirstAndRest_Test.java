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
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.prepend;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertEndsWith(AssertionInfo, Object[], Object, Object[])}</code>.
 */
class ObjectArrays_assertEndsWithFirstAndRest_Test extends ObjectArraysBaseTest {

  @Override
  protected void initActualArray() {
    actual = array("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertEndsWith(someInfo(), actual, "Luke", null));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertEndsWith(someInfo(), null, "Luke",
                                                                                           array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };

    Throwable error = catchThrowable(() -> arrays.assertEndsWith(info, actual, "Luke", sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenSequenceNotFound(info, "Luke", sequence);
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };

    Throwable error = catchThrowable(() -> arrays.assertEndsWith(info, actual, "Obi-Wan", sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenSequenceNotFound(info, "Obi-Wan", sequence);
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };

    Throwable error = catchThrowable(() -> arrays.assertEndsWith(info, actual, "Luke", sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenSequenceNotFound(info, "Luke", sequence);
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo info, Object first, Object[] sequence) {
    verify(failures).failure(info, shouldEndWith(actual, prepend(first, sequence)));
  }

  @Test
  void should_pass_if_actual_ends_with_first_then_sequence() {
    arrays.assertEndsWith(someInfo(), actual, "Luke", array("Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_and_first_then_sequence_are_equal() {
    arrays.assertEndsWith(someInfo(), actual, "Yoda", array("Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "LUKE", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertEndsWith(info, actual, "Yoda", sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldEndWith(actual, prepend("Yoda", sequence),
                                                 caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_does_not_end_with_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertEndsWith(info, actual, "Yoda", sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldEndWith(actual, prepend("Yoda", sequence),
                                                 caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_fail_if_actual_ends_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };

    Throwable error = catchThrowable(() -> arraysWithCustomComparisonStrategy.assertEndsWith(info, actual, "Luke", sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldEndWith(actual, prepend("Luke", sequence),
                                                 caseInsensitiveStringComparisonStrategy));
  }

  @Test
  void should_pass_if_actual_ends_with_sequence_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, "LUKE", array("Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    arraysWithCustomComparisonStrategy.assertEndsWith(someInfo(), actual, "Yoda", array("LUKE", "Leia", "Obi-Wan"));
  }
}
