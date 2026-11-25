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
import static org.assertj.core.error.NoElementsShouldMatch.noElementsShouldMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

/// @author Filip Hrisafov
class PredicateAssert_rejectsAll_Test {

  @Test
  void should_fail_when_predicate_is_null() {
    // GIVEN
    Predicate<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).rejectsAll(list("first", "second")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_when_values_are_null() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    List<String> values = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ballSportPredicate).rejectsAll(values));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_when_values_are_empty() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    // WHEN/THEN
    then(ballSportPredicate).rejectsAll(list());
  }

  @Test
  void should_fail_when_predicate_accepts_some_value() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    List<String> values = list("curling", "judo", "football");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ballSportPredicate).rejectsAll(values));
    // THEN
    then(assertionError).hasMessage(noElementsShouldMatch(values, "football", PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_no_value() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    // WHEN/THEN
    then(ballSportPredicate).rejectsAll(list("curling", "judo", "marathon"));
  }

}
