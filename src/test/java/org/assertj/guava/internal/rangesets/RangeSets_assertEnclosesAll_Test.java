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
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.IterableUtil.iterable;
import static org.assertj.guava.error.RangeSetShouldEnclose.shouldEnclose;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsEmpty;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsNull;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.guava.internal.RangeSetsBaseTest;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

class RangeSets_assertEnclosesAll_Test extends RangeSetsBaseTest {

  @Test
  void should_pass_if_actual_encloses_all_ranges() {
    rangeSets.assertEnclosesAll(someInfo(), actual, iterable(closed(2, 5),
                                                             closed(17, 19),
                                                             open(30, 32)));
    // Order does not matter
    rangeSets.assertEnclosesAll(someInfo(), actual, iterable(closed(17, 19),
                                                             closed(2, 5),
                                                             open(30, 32)));
  }

  @Test
  void should_pass_if_actual_encloses_range_set() {
    rangeSets.assertEnclosesAll(someInfo(), actual, rangeSet(closed(2, 5),
                                                             closed(17, 19),
                                                             open(30, 32)));
  }

  @Test
  void should_pass_if_actual_encloses_iterable_with_duplicated_values() {
    rangeSets.assertIntersectsAll(someInfo(), actual, iterable(closed(2, 5),
                                                               closed(2, 5),
                                                               closed(17, 19),
                                                               closed(17, 19),
                                                               open(30, 32)));
  }

  @Test
  void should_throw_error_if_iterable_of_values_to_look_for_is_null() {
    Iterable<Range<Integer>> expected = null;

    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(), actual, expected))
                                        .withMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_range_set_to_look_for_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(), actual,
                                                                                      (RangeSet<Integer>) null))
                                        .withMessage(rangeSetValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_actual_range_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(), null,
                                                                                                 iterable(closed(1, 2))))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_actual_range_set_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(), null,
                                                                                                 rangeSet(closed(1, 2))))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_iterable_of_values_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(), actual, iterable()))
                                        .withMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_range_set_of_values_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(), actual, rangeSet()))
                                        .withMessage(rangeSetValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_both_actual_and_expected_range_are_empty() {
    actual.clear();
    rangeSets.assertEnclosesAll(someInfo(), actual, iterable());
  }

  @Test
  void should_pass_if_both_actual_and_expected_range_set_are_empty() {
    actual.clear();
    rangeSets.assertEnclosesAll(someInfo(), actual, rangeSet());
  }

  @Test
  void should_fail_if_actual_does_not_enclose_all_range_values() {
    Iterable<Range<Integer>> expected = iterable(open(-10, 1),
                                                 closed(11, 14),
                                                 closed(50, 60));

    ErrorMessageFactory expectedMessage = shouldEnclose(actual, expected, expected);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(),
                                                                                                 actual,
                                                                                                 expected))
                                                   .withMessage(expectedMessage.create());
  }

  @Test
  void should_fail_if_actual_does_not_enclose_all_range_set_values() {
    RangeSet<Integer> expected = rangeSet(open(-10, 1),
                                          closed(11, 14),
                                          closed(50, 60));

    ErrorMessageFactory expectedMessage = shouldEnclose(actual, expected, expected.asRanges());

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(),
                                                                                                 actual,
                                                                                                 expected))
                                                   .withMessage(expectedMessage.create());
  }

  @Test
  void should_fail_if_actual_does_not_enclose_part_of_range_values() {
    Iterable<Range<Integer>> expected = iterable(open(-10, 1),
                                                 closed(15, 20),
                                                 closed(50, 60));

    ErrorMessageFactory expectedMessage = shouldEnclose(actual, expected, iterable(open(-10, 1),
                                                                                   closed(50, 60)));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(),
                                                                                                 actual,
                                                                                                 expected))
                                                   .withMessage(expectedMessage.create());
  }

  @Test
  void should_fail_if_actual_does_not_enclose_part_of_range_set_values() {
    RangeSet<Integer> expected = rangeSet(open(-10, 1),
                                          closed(15, 20),
                                          closed(50, 60));

    ErrorMessageFactory expectedMessage = shouldEnclose(actual, expected, iterable(open(-10, 1),
                                                                                   closed(50, 60)));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertEnclosesAll(someInfo(),
                                                                                                 actual,
                                                                                                 expected))
                                                   .withMessage(expectedMessage.create());
  }
}
