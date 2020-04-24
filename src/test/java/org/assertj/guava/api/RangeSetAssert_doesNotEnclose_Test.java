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
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.error.RangeSetShouldNotEnclose.shouldNotEnclose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

@DisplayName("RangeSetAssert doesNotEnclose")
class RangeSetAssert_doesNotEnclose_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotEnclose(closed(0, 10)));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_values_are_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    Range<Integer>[] elements = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotEnclose(elements));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_expected_values_are_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotEnclose());
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(valuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_the_given_range_set_does_not_enclose_element() {
    // GIVEN
    RangeSet<Integer> actual = of(open(1, 10));
    // THEN
    assertThat(actual).doesNotEnclose(closed(1, 5),
                                      closed(5, 12),
                                      closed(14, 17));
  }

  @Test
  void should_fail_if_the_given_range_set_encloses_element() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    Range<Integer>[] expected = array(closed(2, 5),
                                      closed(4, 12));
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotEnclose(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldNotEnclose(actual, expected, singletonList(closed(2, 5))).create());
  }
}
