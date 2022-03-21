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
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.Iterator;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertStartsWith(AssertionInfo, Collection, Object[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Iterables_assertStartsWith_Test extends IterablesBaseTest {

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = newArrayList("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> iterables.assertStartsWith(someInfo(), actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_sequence_are_empty() {
    actual.clear();
    iterables.assertStartsWith(someInfo(), actual, emptyArray());
  }

  @Test
  void should_fail_if_sequence_to_look_for_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertStartsWith(someInfo(), actual,
                                                                                                emptyArray()));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertStartsWith(someInfo(), null, array("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };

    Throwable error = catchThrowable(() -> iterables.assertStartsWith(info, actual, sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenSequenceNotFound(info, sequence);
  }

  @Test
  void should_fail_if_actual_does_not_start_with_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };

    Throwable error = catchThrowable(() -> iterables.assertStartsWith(info, actual, sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenSequenceNotFound(info, sequence);
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };

    Throwable error = catchThrowable(() -> iterables.assertStartsWith(info, actual, sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verifyFailureThrownWhenSequenceNotFound(info, sequence);
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo info, Object[] sequence) {
    verify(failures).failure(info, shouldStartWith(actual, sequence));
  }

  @Test
  void should_pass_if_actual_starts_with_sequence() {
    iterables.assertStartsWith(someInfo(), actual, array("Yoda", "Luke", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal() {
    iterables.assertStartsWith(someInfo(), actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  void should_pass_if_infinite_iterable_starts_with_given_sequence() {
    iterables.assertStartsWith(someInfo(), infiniteListOfNumbers(), array(1, 2, 3, 4, 5));
  }

  private Iterable<Integer> infiniteListOfNumbers() {

    return new Iterable<Integer>() {

      int number = 1;

      @Override
      public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

          @Override
          public boolean hasNext() {
            return true;
          }

          @Override
          public Integer next() {
            return number++;
          }

          @Override
          public void remove() {}
        };
      }
    };
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_fail_if_actual_does_not_start_with_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };

    Throwable error = catchThrowable(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertStartsWith(info, actual,
                                                                                                           sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldStartWith(actual, sequence, comparisonStrategy));
  }

  @Test
  void should_fail_if_actual_starts_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "YODA", "luke", "Leia", "Obi-Wan", "Han" };

    Throwable error = catchThrowable(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertStartsWith(info, actual,
                                                                                                           sequence));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldStartWith(actual, sequence, comparisonStrategy));
  }

  @Test
  void should_pass_if_actual_starts_with_sequence_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertStartsWith(someInfo(), actual, array("YODA", "luke", "Leia"));
  }

  @Test
  void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertStartsWith(someInfo(), actual, array("Yoda", "LUke", "LeIA", "oBi-WaN"));
  }

}
