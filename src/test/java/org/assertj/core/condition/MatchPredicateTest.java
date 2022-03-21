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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.condition;

import static java.lang.String.format;
import static org.assertj.core.internal.ErrorMessages.predicateIsNull;

import org.assertj.core.api.WithAssertions;
import org.assertj.core.test.Jedi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchPredicateTest implements WithAssertions {

  private Jedi yoda;

  @BeforeEach
  public void setup() {
    yoda = new Jedi("Yoda", "Green");
  }

  @Test
  void should_match_predicate() {
    assertThat(yoda).matches(x -> x.lightSaberColor.equals("Green"));
  }

  @Test
  void should_match_predicate_with_description_() {
    assertThat(yoda).matches(x -> x.lightSaberColor.equals("Green"), "has green light saber");
  }

  @Test
  void should_fail_if_object_does_not_match_predicate() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(yoda).matches(x -> x.lightSaberColor.equals("Red")))
                                                   .withMessage(format("%n" +
                                                                       "Expecting actual:%n" +
                                                                       "  Yoda the Jedi%n" +
                                                                       "to match given predicate.%n" +
                                                                       "%n" +
                                                                       "You can use 'matches(Predicate p, String description)' to have a better error message%n"
                                                                       +
                                                                       "For example:%n" +
                                                                       "  assertThat(player).matches(p -> p.isRookie(), \"is rookie\");%n"
                                                                       +
                                                                       "will give an error message looking like:%n" +
                                                                       "%n" +
                                                                       "Expecting actual:%n" +
                                                                       "  player%n" +
                                                                       "to match 'is rookie' predicate"));
  }

  @Test
  void should_fail_if_object_does_not_match_predicate_and_use_predicate_description_in_error_message() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(yoda).as("check light saber")
                                                                                     .matches(x -> x.lightSaberColor.equals("Red"),
                                                                                              "has red light saber"))
                                                   .withMessage(format("[check light saber] %n" +
                                                                       "Expecting actual:%n" +
                                                                       "  Yoda the Jedi%n" +
                                                                       "to match 'has red light saber' predicate."));
  }

  @Test
  void should_fail_if_given_predicate_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(yoda).matches(null))
                                    .withMessage(predicateIsNull());
  }

  @Test
  void should_fail_if_given_predicate_with_description_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(yoda).matches(null,
                                                                               "whatever ..."))
                                    .withMessage(predicateIsNull());
  }

  @Test
  void should_fail_if_given_predicate_description_is_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(yoda).matches(x -> x.lightSaberColor.equals("Green"),
                                                                               null))
                                    .withMessage("The predicate description must not be null");
  }
}
