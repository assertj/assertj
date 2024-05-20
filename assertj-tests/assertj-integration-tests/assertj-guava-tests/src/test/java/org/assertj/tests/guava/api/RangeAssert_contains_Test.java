/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.guava.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.tests.guava.testkit.AssertionErrors.expectAssertionError;

import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;

/**
 * @author Marcin Kwaczyński
 */
class RangeAssert_contains_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Range<Integer> actual = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).contains(1));
    // THEN
    then(thrown).isInstanceOf(AssertionError.class)
                .hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_values_are_null() {
    // GIVEN
    Range<Integer> actual = Range.closedOpen(1, 10);
    Integer[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).contains(values));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The values to look for should not be null");
  }

  @Test
  void should_fail_if_expected_values_group_is_empty_and_actual_is_not() {
    // GIVEN
    Range<Integer> actual = Range.openClosed(1, 2);
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).contains());
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The values to look for should not be empty");
  }

  @Test
  void should_pass_if_both_actual_and_expected_values_are_empty() {
    // GIVEN
    Range<Integer> actual = Range.openClosed(1, 1);
    // WHEN/THEN
    assertThat(actual).contains();
  }

  @Test
  void should_fail_when_range_does_not_contain_expected_values() {
    // GIVEN
    final Range<Integer> actual = Range.closedOpen(1, 10);
    // when
    AssertionError error = expectAssertionError(() -> assertThat(actual).contains(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    // THEN
    then(error).hasMessage(format("%nExpecting Range:%n" +
                                  "  [1..10)%n" +
                                  "to contain:%n" +
                                  "  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]%n" +
                                  "but could not find the following element(s):%n" +
                                  "  [10]%n"));
  }

  @Test
  void should_pass_if_range_contains_values() {
    // GIVEN
    Range<Integer> actual = Range.closed(1, 10);
    // WHEN/THEN
    assertThat(actual).contains(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }

}
