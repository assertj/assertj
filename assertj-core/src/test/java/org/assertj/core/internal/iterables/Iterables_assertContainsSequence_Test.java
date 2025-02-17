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
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainSequence.shouldContainSequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsSequence(AssertionInfo, Collection, Object[])}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Iterables_assertContainsSequence_Test extends IterablesBaseTest {

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = newArrayList("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      Object[] nullArray = null;
      iterables.assertContainsSequence(someInfo(), actual, nullArray);
    }).withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    actual.clear();
    iterables.assertContainsSequence(someInfo(), actual, array());
  }

  @Test
  void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertContainsSequence(someInfo(), actual,
                                                                                                      emptyArray()));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertContainsSequence(someInfo(), null,
                                                                                                      array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };

    Throwable error = catchThrowable(() -> iterables.assertContainsSequence(info, actual, sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenSequenceNotFound(info, sequence);
  }

  @Test
  void should_fail_if_actual_does_not_contain_whole_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };

    Throwable error = catchThrowable(() -> iterables.assertContainsSequence(info, actual, sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenSequenceNotFound(info, sequence);
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_sequence_but_not_whole_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Luke", "Leia", "Han" };

    Throwable error = catchThrowable(() -> iterables.assertContainsSequence(info, actual, sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenSequenceNotFound(info, sequence);
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo info, Object[] sequence) {
    verify(failures).failure(info, shouldContainSequence(actual, sequence));
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    iterables.assertContainsSequence(someInfo(), actual, array("Luke", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    iterables.assertContainsSequence(someInfo(), actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_contains_both_partial_and_complete_sequence() {
    actual = newArrayList("Yoda", "Luke", "Yoda", "Obi-Wan");
    iterables.assertContainsSequence(someInfo(), actual, array("Yoda", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_contains_sequence_that_specifies_multiple_times_the_same_value_bug_544() {
    actual = newArrayList("a", "-", "b", "-", "c");
    iterables.assertContainsSequence(someInfo(), actual, array("a", "-", "b", "-", "c"));
  }

  @Test
  void should_pass_if_actual_is_an_infinite_sequence_and_contains_sequence() {
    Iterable<String> actual = com.google.common.collect.Iterables.cycle("Leia", "Luke", "Yoda", "Obi-Wan");
    iterables.assertContainsSequence(someInfo(), actual, array("Luke", "Yoda", "Obi-Wan", "Leia"));
    iterables.assertContainsSequence(someInfo(), actual, array("Luke", "Yoda"));
    iterables.assertContainsSequence(someInfo(), actual, array("Luke", "Yoda", "Obi-Wan", "Leia", "Luke", "Yoda"));
  }

  @Test
  void should_pass_if_actual_is_a_singly_traversable_sequence_and_contains_sequence() {
    Iterable<String> actual = SinglyIterableFactory.createSinglyIterable(list("Leia", "Luke", "Yoda", "Obi-Wan"));
    iterables.assertContainsSequence(someInfo(), actual, array("Leia", "Luke", "Yoda", "Obi-Wan"));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_fail_if_actual_does_not_contain_whole_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };

    Throwable error = catchThrowable(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSequence(info, actual,
                                                                                                                 sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_sequence_but_not_whole_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Luke", "Leia", "Han" };

    Throwable error = catchThrowable(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSequence(info, actual,
                                                                                                                 sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContainSequence(actual, sequence, comparisonStrategy));
  }

  @Test
  void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), actual, array("LUKe", "leia"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSequence(someInfo(), actual,
                                                                          array("YODA", "luke", "lEIA", "Obi-wan"));
  }

}
