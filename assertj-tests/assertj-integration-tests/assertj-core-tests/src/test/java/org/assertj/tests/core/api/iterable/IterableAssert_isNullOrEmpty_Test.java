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
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Yvonne Wang
/// @author Joel Costigliola
class IterableAssert_isNullOrEmpty_Test {

  @Test
  void should_pass_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN/THEN
    then(actual).isNullOrEmpty();
  }

  @Test
  void should_pass_if_actual_is_empty() {
    assertThat(List.of()).isNullOrEmpty();
  }

  @Test
  void should_fail_if_actual_has_elements() {
    // GIVEN
    List<String> actual = List.of("Yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isNullOrEmpty());
    // THEN
    then(assertionError).hasMessage(shouldBeNullOrEmpty(actual).create());
  }

  @Test
  void should_pass_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = null;
    // WHEN/THEN
    then(actual).usingElementComparator(CASE_INSENSITIVE_ORDER).isNullOrEmpty();
  }

  @Test
  void should_pass_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    assertThat(List.<String> of()).usingElementComparator(CASE_INSENSITIVE_ORDER).isNullOrEmpty();
  }

  @Test
  void should_fail_if_actual_has_elements_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = List.of("Yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .isNullOrEmpty());
    // THEN
    then(assertionError).hasMessage(shouldBeNullOrEmpty(actual).create());
  }

}
