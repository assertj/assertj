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
package org.assertj.tests.core.api.longpredicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.function.LongPredicate;
import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

/// @author Filip Hrisafov
class LongPredicateAssert_accepts_Test {

  @Test
  void should_fail_when_predicate_is_null() {
    // GIVEN
    LongPredicate actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).accepts(1L, 2L));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_when_no_values_are_given() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).accepts());
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_value() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    Predicate<Long> wrapPredicate = predicate::test;
    long expectedValue = 3;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).accepts(expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldAccept(wrapPredicate, expectedValue, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_value_with_string_description() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    Predicate<Long> wrapPredicate = predicate::test;
    long expectedValue = 3;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).as("test").accepts(expectedValue));
    // THEN
    then(assertionError).hasMessage("[test] " + shouldAccept(wrapPredicate, expectedValue, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_value() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    // WHEN/THEN
    then(predicate).accepts(1);
  }

  @Test
  void should_fail_when_predicate_does_not_accept_one_of_the_values() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).accepts(1L, 2L, 3L));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(list(1L, 2L, 3L), 3L, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_report_all_values_not_accepted_by_the_predicate() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).accepts(1L, 3L, 4L));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(list(1L, 3L, 4L), list(3L, 4L), PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_all_values() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    // WHEN/THEN
    then(predicate).accepts(1L, 2L);
  }

}
