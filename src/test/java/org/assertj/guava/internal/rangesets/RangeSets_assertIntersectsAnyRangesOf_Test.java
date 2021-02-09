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
package org.assertj.guava.internal.rangesets;

import static com.google.common.collect.Range.closed;
import static com.google.common.collect.Range.open;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.IterableUtil.iterable;
import static org.assertj.guava.error.RangeSetShouldIntersectAnyOf.shouldIntersectAnyOf;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsEmpty;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsNull;

import org.assertj.guava.internal.RangeSetsBaseTest;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

class RangeSets_assertIntersectsAnyRangesOf_Test extends RangeSetsBaseTest {

  @Test
  void should_pass_if_actual_intersects_all_elements_of_range() {
    rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual, iterable(closed(-10, 10), open(12, 25)));
    // Order does not matter
    rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual, iterable(open(12, 25), closed(-10, 10)));
  }

  @Test
  void should_pass_if_actual_intersects_all_elements_of_range_set() {
    rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual, rangeSet(closed(-10, 10), open(12, 25)));
  }

  @Test
  void should_pass_if_actual_intersects_any_elements_of_range() {
    rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual, iterable(closed(-10, 0),
                                                                       open(8, 12),
                                                                       open(40, 60)));
  }

  @Test
  void should_pass_if_actual_intersects_any_elements_of_range_set() {
    rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual, rangeSet(closed(-10, 0),
                                                                       open(8, 12),
                                                                       open(40, 60)));
  }

  @Test
  void should_throw_error_if_iterable_of_ranges_to_look_for_is_null() {
    Iterable<Range<Integer>> expected = null;
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual,
                                                                                                expected))
                                        .withMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_range_set_to_look_for_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual,
                                                                                                (RangeSet<Integer>) null))
                                        .withMessage(rangeSetValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertIntersectsAnyRangesOf(someInfo(),
                                                                                                           null,
                                                                                                           iterable(closed(1,
                                                                                                                           10))))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_array_of_ranges_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual,
                                                                                                iterable()))
                                        .withMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_array_of_range_set_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual,
                                                                                                rangeSet()))
                                        .withMessage(rangeSetValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_empty() {
    actual.clear();
    rangeSets.assertIntersectsAnyRangesOf(someInfo(), actual, iterable());
  }

  @Test
  void should_fail_if_actual_does_not_intersect_range() {
    Iterable<Range<Integer>> expected = iterable(closed(-100, 0), open(100, 200));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertIntersectsAnyRangesOf(someInfo(),
                                                                                                           actual,
                                                                                                           expected))
                                                   .withMessage(shouldIntersectAnyOf(actual, expected).create());
  }

  @Test
  void should_fail_if_actual_does_not_intersect_range_set() {
    RangeSet<Integer> expected = rangeSet(closed(-100, 0), open(100, 200));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertIntersectsAnyRangesOf(someInfo(),
                                                                                                           actual,
                                                                                                           expected))
                                                   .withMessage(shouldIntersectAnyOf(actual, expected).create());
  }
}
