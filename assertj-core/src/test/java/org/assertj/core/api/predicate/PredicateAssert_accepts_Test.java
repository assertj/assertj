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
package org.assertj.core.api.predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Predicate;
import org.assertj.core.api.PredicateAssert;
import org.assertj.core.api.PredicateAssertBaseTest;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Filip Hrisafov
 */
@ExtendWith(MockitoExtension.class)
class PredicateAssert_accepts_Test extends PredicateAssertBaseTest {

  @Override
  protected PredicateAssert<Boolean> invoke_api_method() {
    return assertions.accepts(true, true);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllMatch(getInfo(assertions), newArrayList(true, true), getActual(assertions),
                                     PredicateDescription.GIVEN);
  }

  @Test
  void should_fail_when_predicate_is_null() {
    // GIVEN
    String[] values = { "first", "second" };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat((Predicate<String>) null).accepts(values));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_values() {
    // GIVEN
    String[] values = { "football", "basketball", "curling" };
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(ballSportPredicate).accepts(values));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(values, "curling", PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_all_values() {
    // GIVEN
    Predicate<String> ballSportPredicate = sport -> sport.contains("ball");
    // WHEN/THEN
    assertThat(ballSportPredicate).accepts("football", "basketball", "handball");
  }

  @Test
  void should_fail_when_predicate_does_not_accept_value() {
    // GIVEN
    Predicate<String> predicate = val -> val.equals("something");
    String expectedValue = "something else";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(predicate).accepts(expectedValue));
    // THEN
    then(assertionError).hasMessage(shouldAccept(predicate, expectedValue, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_fail_when_predicate_does_not_accept_value_with_string_description() {
    // GIVEN
    Predicate<String> predicate = val -> val.equals("something");
    String expectedValue = "something else";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(predicate).as("test").accepts(expectedValue));
    // THEN
    then(assertionError).hasMessage("[test] " + shouldAccept(predicate, expectedValue, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_pass_when_predicate_accepts_value() {
    // GIVEN
    Predicate<String> predicate = val -> val.equals("something");
    // WHEN/THEN
    assertThat(predicate).accepts("something");
  }

  @Test
  void should_pass_and_only_invoke_predicate_once_for_single_value(@Mock Predicate<Object> predicate) {
    // GIVEN
    when(predicate.test(any())).thenReturn(true);
    // WHEN
    assertThat(predicate).accepts("something");
    // THEN
    verify(predicate).test("something");
  }

}
