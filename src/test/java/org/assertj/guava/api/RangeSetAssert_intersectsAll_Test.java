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
import static com.google.common.collect.Range.closedOpen;
import static com.google.common.collect.Range.open;
import static com.google.common.collect.TreeRangeSet.create;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.RangeSetShouldIntersect.shouldIntersect;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsEmpty;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

/**
 * Tests for <code>{@link RangeSetAssert#intersectsAll(RangeSet)}</code> and
 * <code>{@link RangeSetAssert#intersectsAll(Iterable)}</code>.
 *
 * @author Ilya Koshaleu
 */
@DisplayName("RangeSetAssert intersectsAll")
class RangeSetAssert_intersectsAll_Test {

  @Test
  void should_fail_if_the_given_set_if_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAll(emptySet()));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_range_set_is_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    RangeSet<Integer> expected = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAll(expected));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(rangeSetValuesToLookForIsNull());
  }

  @Test
  void should_fail_if_range_is_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    Iterable<Range<Integer>> expected = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAll(expected));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_pass_if_both_expected_range_and_actual_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // THEN
    assertThat(actual).intersectsAll(emptySet());
  }

  @Test
  void should_pass_if_both_expected_range_set_and_actual_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // THEN
    assertThat(actual).intersectsAll(of());
  }

  @Test
  void should_fail_if_expected_range_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 1));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAll(emptySet()));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_fail_if_expected_range_set_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 1));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAll(of()));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(rangeSetValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_the_given_set_intersects_range() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 100));
    // THEN
    assertThat(actual).intersectsAll(asList(closed(10, 15), open(30, 45)));
  }

  @Test
  void should_pass_if_the_given_set_intersects_range_set() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 100));
    // THEN
    assertThat(actual).intersectsAll(of(closed(50, 200)));
  }

  @Test
  void should_fail_if_the_given_set_does_not_intersect_range_set() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 100));
    RangeSet<Integer> expected = ImmutableRangeSet.<Integer> builder()
                                                  .add(closed(101, 120))
                                                  .add(closedOpen(-100, 0)).build();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAll(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldIntersect(actual, expected, asList(closedOpen(-100, 0),
                                                                              closed(101, 120))).create());
  }

  @Test
  void should_fail_if_the_given_set_does_not_intersect_range() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(0, 100));
    List<Range<Integer>> expected = asList(closed(50, 75),
                                           open(105, 120),
                                           closedOpen(-100, 0));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).intersectsAll(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldIntersect(actual, expected, asList(open(105, 120),
                                                                              closedOpen(-100, 0))).create());
  }
}
