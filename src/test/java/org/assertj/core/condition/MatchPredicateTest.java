/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.condition;

import static org.assertj.core.internal.ErrorMessages.predicateIsNull;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.api.WithAssertions;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Jedi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MatchPredicateTest implements WithAssertions {

  @Rule
  public ExpectedException thrown = none();
  private Jedi yoda;

  @Before
  public void setup() {
	yoda = new Jedi("Yoda", "Green");
  }

  @Test
  public void should_match_predicate() {
	assertThat(yoda).matches(x -> x.lightSaberColor.equals("Green"));
  }

  @Test
  public void should_match_predicate_with_description_() {
	assertThat(yoda).matches(x -> x.lightSaberColor.equals("Green"), "has green light saber");
  }

  @Test
  public void should_fail_if_object_does_not_match_predicate() {
    thrown.expectAssertionError("%n" +
                                "Expecting:%n" +
                                "  <Yoda the Jedi>%n" +
                                "to match given predicate.%n" +
                                "%n" +
                                "You can use 'matches(Predicate p, String description)' to have a better error message%n" +
                                "For example:%n" +
                                "  assertThat(player).matches(p -> p.isRookie(), \"is rookie\");%n" +
                                "will give an error message looking like:%n" +
                                "%n" +
                                "Expecting:%n" +
                                "  <player>%n" +
                                "to match 'is rookie' predicate");
    assertThat(yoda).matches(x -> x.lightSaberColor.equals("Red"));
  }

  @Test
  public void should_fail_if_object_does_not_match_predicate_and_use_predicate_description_in_error_message() {
    thrown.expectAssertionError("[check light saber] %n" +
                                "Expecting:%n" +
                                "  <Yoda the Jedi>%n" +
                                "to match 'has red light saber' predicate.");
    assertThat(yoda).as("check light saber").matches(x -> x.lightSaberColor.equals("Red"), "has red light saber");
  }

  @Test
  public void should_fail_if_given_predicate_is_null() {
	// then
	thrown.expectNullPointerException(predicateIsNull());
	// when
	assertThat(yoda).matches(null);
  }

  @Test
  public void should_fail_if_given_predicate_with_description_is_null() {
	// then
	thrown.expectNullPointerException(predicateIsNull());
	// when
	assertThat(yoda).matches(null, "whatever ...");
  }
  
  @Test
  public void should_fail_if_given_predicate_description_is_null() {
	// then
	thrown.expectNullPointerException("The predicate description must not be null");
	// when
	assertThat(yoda).matches(x -> x.lightSaberColor.equals("Green"), null);
  }
}
