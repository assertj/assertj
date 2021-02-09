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
import static org.assertj.guava.error.RangeSetShouldNotEnclose.shouldNotEnclose;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsEmpty;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsNull;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.guava.internal.RangeSetsBaseTest;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

class RangeSets_assertDoesNotEncloseAll_Test extends RangeSetsBaseTest {

  @Test
  void should_pass_if_actual_does_not_enclose_iterable_of_ranges() {
    rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual, iterable(open(-10, 1),
                                                                     open(11, 20),
                                                                     closed(40, 60)));
  }

  @Test
  void should_pass_if_actual_does_not_enclose_range_set() {
    rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual, rangeSet(open(-10, 1),
                                                                     open(11, 20),
                                                                     closed(40, 60)));
  }

  @Test
  void should_pass_if_actual_does_not_enclose_ranges_even_if_duplicated() {
    rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual, iterable(open(-10, 1),
                                                                     open(-10, 1),
                                                                     open(11, 20),
                                                                     open(11, 20),
                                                                     closed(40, 60),
                                                                     closed(40, 60)));
  }

  @Test
  void should_throw_error_if_iterable_of_ranges_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual,
                                                                                              iterable()))
                                        .withMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_range_set_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual,
                                                                                              rangeSet()))
                                        .withMessage(rangeSetValuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_iterable_of_ranges_to_look_for_is_null() {
    Iterable<Range<Integer>> expected = null;
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual, expected))
                                        .withMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_range_set_to_look_for_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual,
                                                                                              (RangeSet<Integer>) null))
                                        .withMessage(rangeSetValuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_for_iterable() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), null,
                                                                                                         iterable(closed(-10,
                                                                                                                         0))))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_null_for_range_set() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), null,
                                                                                                         rangeSet(closed(-10,
                                                                                                                         0))))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_encloses_all_values_of_iterable() {
    Iterable<Range<Integer>> expected = iterable(closed(1, 5), open(16, 19), open(32, 34));

    ErrorMessageFactory expectedMessage = shouldNotEnclose(actual, expected, expected);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual,
                                                                                                         expected))
                                                   .withMessage(expectedMessage.create());
  }

  @Test
  void should_fail_if_actual_encloses_all_values_of_range_set() {
    RangeSet<Integer> expected = rangeSet(closed(1, 5), open(16, 19), open(32, 34));

    ErrorMessageFactory expectedMessage = shouldNotEnclose(actual, expected, expected.asRanges());

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual,
                                                                                                         expected))
                                                   .withMessage(expectedMessage.create());
  }

  @Test
  void should_fail_if_actual_encloses_any_values_of_iterable() {
    Iterable<Range<Integer>> expected = iterable(closed(-10, 0), open(16, 19), open(35, 40));

    ErrorMessageFactory expectedMessage = shouldNotEnclose(actual, expected, iterable(open(16, 19)));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual,
                                                                                                         expected))
                                                   .withMessage(expectedMessage.create());
  }

  @Test
  void should_fail_if_actual_encloses_any_values_range_set() {
    RangeSet<Integer> expected = rangeSet(closed(-10, 0), open(16, 19), open(35, 40));

    ErrorMessageFactory expectedMessage = shouldNotEnclose(actual, expected, iterable(open(16, 19)));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.doesNotEncloseAnyRangesOf(someInfo(), actual,
                                                                                                         expected))
                                                   .withMessage(expectedMessage.create());
  }
}
