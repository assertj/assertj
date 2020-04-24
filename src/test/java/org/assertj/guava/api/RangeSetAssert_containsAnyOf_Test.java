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
package org.assertj.guava.api;

import static com.google.common.collect.ImmutableRangeSet.of;
import static com.google.common.collect.Range.closed;
import static com.google.common.collect.TreeRangeSet.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.RangeSet;

/**
 * Tests for <code>{@link RangeSetAssert#containsAnyOf(Comparable[])}</code>.
 *
 * @author Ilya Koshaleu
 */
@DisplayName("RangeSetAssert containsAnyOf")
class RangeSetAssert_containsAnyOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAnyOf(1));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_values_are_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    Integer[] expected = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAnyOf(expected));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_both_expected_values_and_actual_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // THEN
    assertThat(actual).containsAnyOf();
  }

  @Test
  void should_fail_if_expected_values_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 1));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAnyOf());
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(valuesToLookForIsEmpty());
  }

  @Test
  void should_fail_if_the_given_range_set_does_not_contain_element() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 3));
    Integer[] expected = array(4, 5);
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).containsAnyOf(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldContainAnyOf(actual, expected).create());
  }

  @Test
  void should_pass_if_the_given_range_set_contains_part_of_elements() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    // THEN
    assertThat(actual).containsAnyOf(0, 1, 2, 3, 11, 12, 13);
  }
}
