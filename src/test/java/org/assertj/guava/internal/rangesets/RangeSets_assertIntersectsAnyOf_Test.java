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
package org.assertj.guava.internal.rangesets;

import static com.google.common.collect.Range.closed;
import static com.google.common.collect.Range.open;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.error.RangeSetShouldIntersectAnyOf.shouldIntersectAnyOf;

import org.assertj.guava.internal.RangeSetsBaseTest;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;

class RangeSets_assertIntersectsAnyOf_Test extends RangeSetsBaseTest {

  @Test
  void should_pass_if_actual_intersects_all_ranges() {
    rangeSets.assertIntersectsAnyOf(someInfo(), actual, array(closed(-10, 10), open(12, 25)));
    // Order does not matter
    rangeSets.assertIntersectsAnyOf(someInfo(), actual, array(open(12, 25), closed(-10, 10)));
  }

  @Test
  void should_pass_if_actual_intersects_any_range() {
    rangeSets.assertIntersectsAnyOf(someInfo(), actual, array(closed(-10, 10),
                                                              closed(11, 14),
                                                              open(12, 25),
                                                              open(50, 60)));
  }

  @Test
  void should_throw_error_if_array_of_ranges_to_look_for_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertIntersectsAnyOf(someInfo(), actual, null))
                                        .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertIntersectsAnyOf(someInfo(),
                                                                                                     null,
                                                                                                     array(closed(1, 10))))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_array_of_ranges_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertIntersectsAnyOf(someInfo(), actual, array()))
                                        .withMessage(valuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_empty() {
    actual.clear();
    rangeSets.assertIntersectsAnyOf(someInfo(), actual, array());
  }

  @Test
  void should_fail_if_actual_does_not_intersect_range() {
    Range<Integer>[] expected = array(closed(-100, 0), open(100, 200));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertIntersectsAnyOf(someInfo(),
                                                                                                     actual,
                                                                                                     expected))
                                                   .withMessage(shouldIntersectAnyOf(actual, expected).create());
  }
}
