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
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.RangeSetShouldIntersect.shouldIntersect;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

/**
 * @author Ilya Koshaleu
 */
class RangeSetAssert_intersectsAll_with_Iterable_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    Iterable<Range<Integer>> ranges = list(closed(0, 10));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).intersectsAll(ranges));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_ranges_is_null() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Iterable<Range<Integer>> ranges = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).intersectsAll(ranges));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("ranges").create());
  }

  @Test
  void should_fail_if_ranges_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 1));
    Iterable<Range<Integer>> ranges = emptySet();
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).intersectsAll(ranges));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Expecting ranges not to be empty");
  }

  @Test
  void should_fail_if_actual_does_not_intersect_ranges() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 100));
    Iterable<Range<Integer>> ranges = list(closed(50, 70), closed(120, 150));
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).intersectsAll(ranges));
    // THEN
    then(error).hasMessage(shouldIntersect(actual, ranges, list(closed(120, 150))).create());
  }

  @Test
  void should_pass_if_both_actual_and_ranges_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Iterable<Range<Integer>> ranges = emptySet();
    // WHEN/THEN
    assertThat(actual).intersectsAll(ranges);
  }

  @Test
  void should_pass_if_actual_intersects_ranges() {
    // GIVEN
    RangeSet<Integer> rangeSet = ImmutableRangeSet.of(closed(0, 100));
    Iterable<Range<Integer>> ranges = list(closed(-10, 10),
                                       open(50, 60),
                                       open(90, 170));
    // WHEN/THEN
    assertThat(rangeSet).intersectsAll(ranges);
  }

}
