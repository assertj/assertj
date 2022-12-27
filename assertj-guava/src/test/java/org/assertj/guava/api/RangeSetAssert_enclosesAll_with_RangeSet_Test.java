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
import static com.google.common.collect.Range.open;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.RangeSetShouldEnclose.shouldEnclose;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;

class RangeSetAssert_enclosesAll_with_RangeSet_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    RangeSet<Integer> rangeSet = ImmutableRangeSet.of(closed(0, 1));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).enclosesAll(rangeSet));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_rangeSet_is_null() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    RangeSet<Integer> rangeSet = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).enclosesAll(rangeSet));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("rangeSet").create());
  }

  @Test
  void should_fail_if_rangeSet_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 1));
    RangeSet<Integer> rangeSet = ImmutableRangeSet.of();
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).enclosesAll(rangeSet));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Expecting rangeSet not to be empty");
  }

  @Test
  void should_fail_if_actual_does_not_enclose_rangeSet() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 100));
    RangeSet<Integer> expected = ImmutableRangeSet.<Integer> builder()
                                                  .add(closed(50, 70))
                                                  .add(closed(120, 150))
                                                  .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).enclosesAll(expected));
    // THEN
    then(error).hasMessage(shouldEnclose(actual, expected, list(closed(120, 150))).create());
  }

  @Test
  void should_pass_if_both_actual_and_rangeSet_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    RangeSet<Integer> rangeSet = ImmutableRangeSet.of();
    // WHEN/THEN
    assertThat(actual).enclosesAll(rangeSet);
  }

  @Test
  void should_pass_if_actual_encloses_rangeSet() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 100));
    RangeSet<Integer> rangeSet = ImmutableRangeSet.<Integer> builder()
                                                  .add(closed(0, 10))
                                                  .add(open(50, 60))
                                                  .add(open(90, 100))
                                                  .build();
    // WHEN/THEN
    assertThat(actual).enclosesAll(rangeSet);
  }

}
