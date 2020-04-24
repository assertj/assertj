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
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.IterableUtil.iterable;
import static org.assertj.guava.error.RangeSetShouldIntersect.shouldIntersect;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.guava.internal.RangeSetsBaseTest;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;

class RangeSets_assertIntersects_Test extends RangeSetsBaseTest {

  @Test
  void should_pass_if_actual_intersects_ranges() {
    rangeSets.assertIntersects(someInfo(), actual, array(closed(-10, 10), open(12, 25)));
    // Order does not matter
    rangeSets.assertIntersects(someInfo(), actual, array(open(12, 25), closed(-10, 10)));
  }

  @Test
  void should_pass_if_actual_intersects_array_with_duplicated_values() {
    rangeSets.assertIntersects(someInfo(), actual, array(closed(-10, 10),
                                                         closed(-10, 10),
                                                         open(12, 25),
                                                         open(12, 25)));
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertIntersects(someInfo(), actual, null))
                                        .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertIntersects(someInfo(), null,
                                                                                                array(closed(1, 2))))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertIntersects(someInfo(), actual, array()))
                                        .withMessage(valuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_empty() {
    actual.clear();
    rangeSets.assertIntersects(someInfo(), actual, array());
  }

  @Test
  void should_fail_if_actual_does_not_intersect_all_values() {
    Range<Integer>[] expected = array(open(-10, 1),
                                      closed(11, 14),
                                      closed(50, 60));

    ErrorMessageFactory expectedMessage = shouldIntersect(actual, expected, asList(expected));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertIntersects(someInfo(),
                                                                                                actual,
                                                                                                expected))
                                                   .withMessage(expectedMessage.create());
  }

  @Test
  void should_fail_if_actual_does_not_intersect_part_of_values() {
    Range<Integer>[] expected = array(open(-10, 1),
                                      closed(11, 20),
                                      closed(50, 60));

    ErrorMessageFactory expectedMessage = shouldIntersect(actual, expected, iterable(open(-10, 1),
                                                                                     closed(50, 60)));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertIntersects(someInfo(),
                                                                                                actual,
                                                                                                expected))
                                                   .withMessage(expectedMessage.create());
  }
}
