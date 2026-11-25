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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

class IterableAssert_allMatch_Test {

  @Test
  void should_pass_if_each_element_satisfies_predicate() {
    assertThat(List.of("123", "1234", "12345")).allMatch(s -> s.length() >= 3);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(s -> s.length() >= 3));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_predicate_is_null() {
    // GIVEN
    List<String> actual = List.of("Luke", "Yoda", "Leia");
    Predicate<String> predicate = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).allMatch(predicate));
    // THEN
    then(npe).hasMessage("The predicate to evaluate should not be null");
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    List<String> actual = List.of();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(s -> s.contains("error")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_fail_if_predicate_is_not_met() {
    // GIVEN
    List<String> actual = List.of("Luke", "Leia", "Yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(s -> s.startsWith("L")));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(actual, "Yoda", PredicateDescription.GIVEN).create());
  }

  @Test
  void should_report_all_items_that_do_not_match() {
    // GIVEN
    List<String> actual = List.of("123", "1234", "12345");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(s -> s.length() <= 3));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(actual, List.of("1234", "12345"), PredicateDescription.GIVEN).create());
  }

}
