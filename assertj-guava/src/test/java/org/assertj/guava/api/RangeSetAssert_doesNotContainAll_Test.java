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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.api;

import static com.google.common.collect.Range.closed;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.testkit.AssertionErrors.expectAssertionError;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.RangeSet;

/**
 * @author Ilya Koshaleu
 */
class RangeSetAssert_doesNotContainAll_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    RangeSet<Integer> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).doesNotContainAll(asList(1, 2)));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_values_is_null() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Iterable<Integer> values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).doesNotContainAll(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("values").create());
  }

  @Test
  void should_fail_if_values_is_empty() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of();
    Iterable<Integer> values = emptySet();
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).doesNotContainAll(values));
    // THEN
    assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                      .hasMessage("Expecting values not to be empty");
  }

  @Test
  void should_fail_if_actual_contains_values() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(1, 10));
    Iterable<Integer> values = list(0, 2, 3, 4);
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).doesNotContainAll(values));
    // THEN
    then(error).hasMessage(shouldNotContain(actual, values, list(2, 3, 4)).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_values() {
    // GIVEN
    RangeSet<Integer> actual = ImmutableRangeSet.of(closed(0, 3));
    // WHEN/THEN
    assertThat(actual).doesNotContainAll(list(4, 5));
  }

}
