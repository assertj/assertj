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
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.RangeSetShouldEncloseAnyOf.shouldEncloseAnyOf;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

class RangeSetAssert_enclosesAnyOf_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).enclosesAnyOf(closed(0, 1)));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_ranges_is_null() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Range<Integer>[] ranges = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).enclosesAnyOf(ranges));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("ranges").create());
  }

  @Test
  void should_fail_if_ranges_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 1));
    Range<Integer>[] ranges = array();
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).enclosesAnyOf(ranges));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Expecting ranges not to be empty");
  }

  @Test
  void should_fail_if_the_given_set_does_not_enclose_ranges() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(open(0, 100));
    Range<Integer>[] ranges = array(closed(0, 70),
                                    closed(120, 150));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).enclosesAnyOf(ranges));
    // THEN
    then(error).hasMessage(shouldEncloseAnyOf(actual, ranges).create());
  }

  @Test
  void should_pass_if_both_actual_and_ranges_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Range<Integer>[] ranges = array();
    // WHEN/THEN
    assertThat(actual).enclosesAnyOf(ranges);
  }

  @Test
  void should_pass_if_the_given_set_encloses_ranges() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 100));
    Range<Integer>[] ranges = array(open(0, 10),
                           open(50, 60),
                           open(90, 110));
    // WHEN/THEN
    assertThat(actual).enclosesAnyOf(ranges);
  }

}
