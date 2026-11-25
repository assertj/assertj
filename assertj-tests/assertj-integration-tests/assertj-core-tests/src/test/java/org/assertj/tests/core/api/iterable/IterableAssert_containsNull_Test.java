/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.iterable;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainNull.shouldContainNull;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.junit.jupiter.api.Test;

/// @author Joel Costigliola
class IterableAssert_containsNull_Test {

  @Test
  void should_pass_if_actual_contains_null() {
    assertThat(list("Luke", "Yoda", null)).containsNull();
  }

  @Test
  void should_pass_if_actual_contains_only_null_values() {
    assertThat(list((String) null, null)).containsNull();
  }

  @Test
  void should_pass_if_actual_contains_null_more_than_once() {
    assertThat(list("Luke", "Yoda", null, null)).containsNull();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsNull());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_null() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsNull());
    // THEN
    then(assertionError).hasMessage(shouldContainNull(actual).create());
  }

  @Test
  void should_pass_if_actual_contains_null_whatever_custom_comparison_strategy_is() {
    assertThat(list("Luke", "Yoda", null)).usingElementComparator(CASE_INSENSITIVE_ORDER).containsNull();
  }

  @Test
  void should_pass_if_actual_contains_only_null_values_whatever_custom_comparison_strategy_is() {
    assertThat(list((String) null, null)).usingElementComparator(CASE_INSENSITIVE_ORDER).containsNull();
  }

  @Test
  void should_pass_if_actual_contains_null_more_than_once_whatever_custom_comparison_strategy_is() {
    assertThat(list("Luke", "Yoda", null, null)).usingElementComparator(CASE_INSENSITIVE_ORDER).containsNull();
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsNull());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .containsNull());
    // THEN
    then(assertionError).hasMessage(shouldContainNull(actual).create());
  }

}
