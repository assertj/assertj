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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.guava.api;

import static com.google.common.collect.ImmutableRangeSet.of;
import static com.google.common.collect.Range.closed;
import static com.google.common.collect.Range.open;
import static com.google.common.collect.Range.openClosed;
import static com.google.common.collect.TreeRangeSet.create;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.RangeSetShouldIntersectAnyOf.shouldIntersectAnyOf;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsEmpty;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

/**
 * Tests for <code>{@link RangeSetAssert#intersectsAnyRangesOf(RangeSet)}</code> and
 * <code>{@link RangeSetAssert#intersectsAnyRangesOf(Iterable)}</code>.
 *
 * @author Ilya Koshaleu
 */
@DisplayName("RangeSetAssert intersectsAnyRangesOf")
class RangeSetAssert_intersectsAnyRangesOf_Test {

  @Test
  void should_fail_if_the_given_set_if_null() {
    // GIVEN
    RangeSet<Integer> rangeSet = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(rangeSet).intersectsAnyRangesOf(emptySet()));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_range_are_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    Iterable<Range<Integer>> range = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAnyRangesOf(range));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_fail_if_range_set_are_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    RangeSet<Integer> rangeSet = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAnyRangesOf(rangeSet));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(rangeSetValuesToLookForIsNull());
  }

  @Test
  void should_pass_if_both_expected_range_and_actual_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // THEN
    assertThat(actual).intersectsAnyRangesOf(emptySet());
  }

  @Test
  void should_pass_if_both_expected_range_set_and_actual_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // THEN
    assertThat(actual).intersectsAnyRangesOf(of());
  }

  @Test
  void should_fail_if_expected_range_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 1));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAnyRangesOf(emptySet()));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_fail_if_expected_range_set_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 1));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAnyRangesOf(of()));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(rangeSetValuesToLookForIsEmpty());
  }

  @Test
  void should_fail_if_the_given_range_set_does_not_intersect_range() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    List<Range<Integer>> expected = asList(open(0, 1),
                                           open(11, 15));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAnyRangesOf(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldIntersectAnyOf(actual, expected).create());
  }

  @Test
  void should_fail_if_the_given_range_set_does_not_intersect_range_set() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    RangeSet<Integer> expected = ImmutableRangeSet.<Integer> builder()
                                                  .add(open(0, 1))
                                                  .add(open(11, 15))
                                                  .build();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAnyRangesOf(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldIntersectAnyOf(actual, expected).create());
  }

  @Test
  void should_pass_if_the_given_range_set_intersects_part_of_range() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    // THEN
    assertThat(actual).intersectsAnyRangesOf(asList(open(0, 1),
                                                    open(1, 4),
                                                    closed(3, 11),
                                                    closed(11, 15)));
  }

  @Test
  void should_pass_if_the_given_range_set_intersects_part_of_range_set() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    RangeSet<Integer> expected = ImmutableRangeSet.<Integer> builder()
                                                  .add(open(0, 1))
                                                  .add(closed(3, 11))
                                                  .add(openClosed(11, 15))
                                                  .build();
    // THEN
    assertThat(actual).intersectsAnyRangesOf(expected);
  }
}
