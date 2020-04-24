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
import static com.google.common.collect.Range.open;
import static com.google.common.collect.TreeRangeSet.create;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.RangeSetShouldNotIntersect.shouldNotIntersects;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsEmpty;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsNull;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

@DisplayName("RangeSetAssert doesNotIntersectAnyRangeFrom")
class RangeSetAssert_doesNotIntersectAll_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotIntersectAnyRangeFrom(singleton(closed(1, 3))));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_range_is_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    Iterable<Range<Integer>> expected = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotIntersectAnyRangeFrom(expected));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_fail_if_expected_range_set_is_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    RangeSet<Integer> expected = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotIntersectAnyRangeFrom(expected));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(rangeSetValuesToLookForIsNull());
  }

  @Test
  void should_fail_if_expected_range_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotIntersectAnyRangeFrom(emptySet()));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_fail_if_expected_range_set_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotIntersectAnyRangeFrom(of()));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(rangeSetValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_the_given_range_set_does_not_intersect_range() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    // THEN
    assertThat(actual).doesNotIntersectAnyRangeFrom(asList(closed(11, 14),
                                                           open(15, 19),
                                                           open(-10, 0)));
  }

  @Test
  void should_pass_if_the_given_range_set_does_not_intersect_range_set() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    RangeSet<Integer> expected = ImmutableRangeSet.<Integer> builder()
                                                  .add(closed(11, 14))
                                                  .add(open(15, 19))
                                                  .add(open(-10, 0))
                                                  .build();
    // THEN
    assertThat(actual).doesNotIntersectAnyRangeFrom(expected);
  }

  @Test
  void should_fail_if_the_given_range_set_intersects_range() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    List<Range<Integer>> expected = asList(closed(-10, 0),
                                           open(3, 12),
                                           open(12, 15));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotIntersectAnyRangeFrom(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldNotIntersects(actual, expected, singletonList(open(3, 12)))
                                                                                                      .create());
  }

  @Test
  void should_fail_if_the_given_range_set_intersects_range_set() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    RangeSet<Integer> expected = ImmutableRangeSet.<Integer> builder()
                                                  .add(closed(-10, 0))
                                                  .add(open(3, 12))
                                                  .add(open(12, 15))
                                                  .build();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotIntersectAnyRangeFrom(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldNotIntersects(actual, expected, singletonList(open(3, 12)))
                                                                                                      .create());
  }
}
