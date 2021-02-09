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
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.IterableUtil.iterable;
import static org.assertj.guava.error.RangeSetShouldNotEnclose.shouldNotEnclose;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.guava.internal.RangeSetsBaseTest;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;

class RangeSets_assertDoesNotEnclose_Test extends RangeSetsBaseTest {

  @Test
  void should_pass_if_actual_does_not_enclose_ranges() {
    rangeSets.assertDoesNotEnclose(someInfo(), actual, array(open(-10, 1),
                                                             open(11, 20),
                                                             closed(40, 60)));
  }

  @Test
  void should_pass_if_actual_does_not_enclose_ranges_even_if_duplicated() {
    rangeSets.assertDoesNotEnclose(someInfo(), actual, array(open(-10, 1),
                                                             open(-10, 1),
                                                             open(11, 20),
                                                             open(11, 20),
                                                             closed(40, 60),
                                                             closed(40, 60)));
  }

  @Test
  void should_throw_error_if_array_of_ranges_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertDoesNotEnclose(someInfo(), actual, array()))
                                        .withMessage(valuesToLookForIsEmpty());
  }

  @Test
  void should_throw_error_if_array_of_ranges_to_look_for_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertDoesNotEnclose(someInfo(), actual, null))
                                        .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertDoesNotEnclose(someInfo(), null,
                                                                                                    array(closed(-10, 0))))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_encloses_all_values() {
    Range<Integer>[] expected = array(closed(1, 5), open(16, 19), open(32, 34));

    ErrorMessageFactory expectedMessage = shouldNotEnclose(actual, expected, iterable(expected));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertDoesNotEnclose(someInfo(), actual,
                                                                                                    expected))
                                                   .withMessage(expectedMessage.create());
  }

  @Test
  void should_fail_if_actual_encloses_any_values() {
    Range<Integer>[] expected = array(closed(-10, 0), open(16, 19), open(35, 40));

    ErrorMessageFactory expectedMessage = shouldNotEnclose(actual, expected, iterable(open(16, 19)));

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertDoesNotEnclose(someInfo(), actual,
                                                                                                    expected))
                                                   .withMessage(expectedMessage.create());
  }
}
