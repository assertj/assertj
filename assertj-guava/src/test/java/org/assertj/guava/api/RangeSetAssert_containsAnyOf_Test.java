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
package org.assertj.guava.api;

import static com.google.common.collect.Range.closed;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;

/**
 * @author Ilya Koshaleu
 */
class RangeSetAssert_containsAnyOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).containsAnyOf(1));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Integer[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).containsAnyOf(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("values").create());
  }

  @Test
  void should_fail_if_values_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 1));
    Integer[] values = {};
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).containsAnyOf(values));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Expecting values not to be empty");
  }

  @Test
  void should_fail_if_actual_does_not_contain_values() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 3));
    Integer[] values = array(4, 5);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).containsAnyOf(values));
    // THEN
    then(error).hasMessage(shouldContainAnyOf(actual, values).create());
  }

  @Test
  void should_pass_if_both_actual_and_values_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Integer[] values = {};
    // WHEN/THEN
    assertThat(actual).containsAnyOf(values);
  }

  @Test
  void should_pass_if_actual_contains_any_values() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(1, 10));
    // WHEN/THEN
    assertThat(actual).containsAnyOf(0, 1, 2, 3, 11, 12, 13);
  }

}
