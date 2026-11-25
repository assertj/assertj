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
import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

/// @author Filip Hrisafov
class PredicateAssert_accepts_Test {

  @Test
  void should_fail_when_predicate_is_null() {
    // GIVEN
    Predicate<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).accepts("first", "second"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_when_no_values_are_given() {
    // GIVEN
    Predicate<String> actual = sport -> sport.contains("ball");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).accepts());
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_one_of_the_values() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ballSportPredicate).accepts("football", "basketball", "curling"));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(list("football", "basketball", "curling"), "curling",
                                                        PredicateDescription.GIVEN).create());
  }

  @Test
  void should_report_all_values_not_accepted_by_the_predicate() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ballSportPredicate).accepts("football", "judo", "curling"));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(list("football", "judo", "curling"), list("judo", "curling"),
                                                        PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_all_values() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    // WHEN/THEN
    then(ballSportPredicate).accepts("football", "basketball", "handball");
  }

  @Test
  void should_fail_when_predicate_does_not_accept_value() {
    // GIVEN
    Predicate<String> predicate = value -> value.equals("something");
    String expectedValue = "something else";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).accepts(expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldAccept(predicate, expectedValue, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_value_with_string_description() {
    // GIVEN
    Predicate<String> predicate = value -> value.equals("something");
    String expectedValue = "something else";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).as("test").accepts(expectedValue));
    // THEN
    then(assertionError).hasMessage("[test] " + shouldAccept(predicate, expectedValue, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_value() {
    // GIVEN
    Predicate<String> predicate = value -> value.equals("something");
    // WHEN/THEN
    then(predicate).accepts("something");
  }

  @Test
  void should_pass_and_only_invoke_predicate_once_for_single_value() {
    // GIVEN
    AtomicInteger invocations = new AtomicInteger();
    Predicate<String> predicate = value -> {
      invocations.incrementAndGet();
      return value.equals("something");
    };
    // WHEN
    assertThat(predicate).accepts("something");
    // THEN
    then(invocations).hasValue(1);
  }

}
