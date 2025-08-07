/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Optional;
import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.core.api.OptionalAssert#hasValueMatching(java.util.function.Predicate)}.
 *
 * @author JongJun Kim
 */
class OptionalAssert_hasValueMatching_Test {

  private static final Predicate<String> IS_STRING = s -> true;

  @Test
  void should_fail_when_optional_is_null() {
    // GIVEN
    @SuppressWarnings("OptionalAssignedToNull")
    Optional<String> nullActual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(nullActual).hasValueMatching(IS_STRING));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_when_optional_is_empty() {
    // GIVEN
    Optional<String> actual = Optional.empty();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasValueMatching(IS_STRING));
    // THEN
    then(error).hasMessage(shouldBePresent(Optional.empty()).create());
  }

  @Test
  void should_pass_when_predicate_matches() {
    // GIVEN
    Optional<String> optional = Optional.of("something");
    Predicate<String> predicate = s -> s.startsWith("some");
    // WHEN/THEN
    assertThat(optional).hasValueMatching(predicate);
  }

  @Test
  void should_fail_when_predicate_does_not_match() {
    // GIVEN
    Optional<String> optional = Optional.of("something");
    Predicate<String> predicate = s -> s.startsWith("other");
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(optional).hasValueMatching(predicate));
    // THEN
    then(error).hasMessage(shouldMatch("something", predicate, PredicateDescription.GIVEN).create());
  }

  @Test
  void should_fail_with_custom_description_when_predicate_does_not_match() {
    // GIVEN
    Optional<String> optional = Optional.of("something");
    Predicate<String> predicate = s -> s.startsWith("other");
    String description = "starts with 'other'";
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(optional).hasValueMatching(predicate, description));
    // THEN
    then(error).hasMessage(shouldMatch("something", predicate, new PredicateDescription(description)).create());
  }
}
