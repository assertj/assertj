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
import static org.assertj.core.error.NoElementsShouldMatch.noElementsShouldMatch;
import static org.assertj.core.error.ShouldNotAccept.shouldNotAccept;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.function.LongPredicate;
import java.util.function.Predicate;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

/// @author Filip Hrisafov
class LongPredicateAssert_rejects_Test {

  @Test
  void should_fail_when_predicate_is_null() {
    // GIVEN
    LongPredicate actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).rejects(1L, 2L, 3L));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_when_no_values_are_given() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    // WHEN/THEN
    then(predicate).rejects();
  }

  @Test
  void should_pass_when_predicate_does_not_accept_value() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    // WHEN/THEN
    then(predicate).rejects(3);
  }

  @Test
  void should_fail_when_predicate_accepts_value() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    Predicate<Long> wrapPredicate = predicate::test;
    long expectedValue = 2;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).rejects(expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldNotAccept(wrapPredicate, expectedValue, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_fail_when_predicate_accepts_value_with_description() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    Predicate<Long> wrapPredicate = predicate::test;
    long expectedValue = 2;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).as(new TextDescription("test"))
                                                                         .rejects(expectedValue));
    // THEN
    then(assertionError).hasMessage("[test] " + shouldNotAccept(wrapPredicate, expectedValue,
                                                                PredicateDescription.GIVEN).create());
  }

  @Test
  void should_fail_when_predicate_accepts_some_value() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(predicate).rejects(3L, 2L, 1L));
    // THEN
    then(assertionError).hasMessage(noElementsShouldMatch(list(3L, 2L, 1L), 2L, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_no_value() {
    // GIVEN
    LongPredicate predicate = value -> value <= 2;
    // WHEN/THEN
    then(predicate).rejects(3L, 4L, 5L);
  }

}
