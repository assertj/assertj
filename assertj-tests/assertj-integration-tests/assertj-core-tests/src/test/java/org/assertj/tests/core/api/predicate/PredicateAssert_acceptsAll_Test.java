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
package org.assertj.tests.core.api.predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

/// @author Filip Hrisafov
class PredicateAssert_acceptsAll_Test {

  @Test
  void should_fail_when_predicate_is_null() {
    // GIVEN
    Predicate<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).acceptsAll(list("first", "second")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_when_values_are_empty() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    List<String> values = list();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ballSportPredicate).acceptsAll(values));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_fail_when_values_are_null() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    List<String> values = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ballSportPredicate).acceptsAll(values));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_one_of_the_values() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    List<String> values = list("football", "basketball", "curling");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ballSportPredicate).acceptsAll(values));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(values, "curling", PredicateDescription.GIVEN).create());
  }

  @Test
  void should_report_all_values_not_accepted_by_the_predicate() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    List<String> values = list("football", "judo", "curling");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ballSportPredicate).acceptsAll(values));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(values, list("judo", "curling"), PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_all_values() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    // WHEN/THEN
    then(ballSportPredicate).acceptsAll(list("football", "basketball", "handball"));
  }

}
