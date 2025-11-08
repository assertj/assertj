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
package org.assertj.core.condition;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.internal.ErrorMessages.predicateIsNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.Objects;

import org.assertj.core.api.WithAssertions;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoesNotMatchPredicateTest implements WithAssertions {

  private Jedi yoda;

  @BeforeEach
  public void setup() {
    yoda = new Jedi("Yoda", "Green");
  }

  @Test
  void should_not_match_predicate() {
    assertThat(yoda).doesNotMatch(x -> x.lightSaberColor.equals("Red"));
  }

  @Test
  void should_not_match_predicate_with_description_() {
    assertThat(yoda).doesNotMatch(x -> x.lightSaberColor.equals("Red"), "does not have a red light saber");
  }

  @Test
  void should_fail_if_object_matches_predicate() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(yoda).doesNotMatch(x -> x.lightSaberColor.equals("Green")));
    // THEN
    then(assertionError).hasMessage(format("%n" +
                                           "Expecting actual:%n" +
                                           "  Yoda the Jedi%n" +
                                           "not to match given predicate.%n" +
                                           "%n" +
                                           "You can use 'doesNotMatch(Predicate p, String description)' to have a better error message%n"
                                           +
                                           "For example:%n" +
                                           "  assertThat(player).doesNotMatch(p -> p.isRookie(), \"is not rookie\");%n"
                                           +
                                           "will give an error message looking like:%n" +
                                           "%n" +
                                           "Expecting actual:%n" +
                                           "  player%n" +
                                           "not to match 'is not rookie' predicate"));
  }

  @Test
  void should_fail_if_object_matches_predicate_and_use_predicate_description_in_error_message() {
    var assertionError = expectAssertionError(() -> assertThat(yoda).as("check light saber")
                                                                    .doesNotMatch(x -> x.lightSaberColor.equals("Green"),
                                                                                  "does not have green light saber"));
    // THEN
    then(assertionError).hasMessage(format("[check light saber] %n" +
                                           "Expecting actual:%n" +
                                           "  Yoda the Jedi%n" +
                                           "not to match 'does not have green light saber' predicate."));
  }

  @Test
  void should_fail_if_given_predicate_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(yoda).doesNotMatch(null))
                                    .withMessage(predicateIsNull());
  }

  @Test
  void should_fail_if_given_predicate_with_description_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(yoda).doesNotMatch(null, "whatever ..."))
                                    .withMessage(predicateIsNull());
  }

  @Test
  void should_fail_if_given_predicate_description_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(yoda).doesNotMatch(Objects::isNull, null))
                                    .withMessage("The predicate description must not be null");
  }
}
