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
import static com.google.common.collect.TreeRangeSet.create;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.common.collect.RangeSet;

/**
 * Tests for <code>{@link RangeSetAssert#doesNotContainAll(Iterable)}</code>.
 *
 * @author Ilya Koshaleu
 */
@DisplayName("RangeSetAssert doesNotContainAll")
class RangeSetAssert_doesNotContainAll_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainAll(asList(1, 2)));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_values_are_null() {
    // GIVEN
    RangeSet<Integer> actual = create();
    Iterable<Integer> elements = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainAll(elements));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_pass_if_expected_range_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = create();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainAll(emptySet()));
    // THEN
    assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                         .hasMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_the_given_range_set_does_not_contain_element() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    // THEN
    assertThat(actual).doesNotContainAll(asList(11, 12, 13, 14));
  }

  @Test
  void should_fail_if_the_given_range_set_contains_contains_element() {
    // GIVEN
    RangeSet<Integer> actual = of(closed(1, 10));
    List<Integer> expected = asList(0, 2, 3, 4);
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(actual).doesNotContainAll(expected));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldNotContain(actual, expected, asList(2, 3, 4)).create());
  }
}
