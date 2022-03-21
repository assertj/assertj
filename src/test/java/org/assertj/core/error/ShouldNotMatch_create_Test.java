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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.assertj.core.error.ShouldNotMatch.shouldNotMatch;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
class ShouldNotMatch_create_Test {

  @Test
  void should_create_error_message_with_default_predicate_description() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotMatch("Yoda", color -> color.equals("green"), PredicateDescription.GIVEN);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n  \"Yoda\"%nnot to match given predicate."
                                   + ShouldNotMatch.ADVICE));
  }

  @Test
  void should_create_error_message_with_predicate_description() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotMatch("Yoda", (String color) -> color.equals("green"),
                                                 new PredicateDescription("green light saber"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n  \"Yoda\"%nnot to match 'green light saber' predicate."));
  }

  @Test
  void should_fail_if_predicate_description_is_null() {
    thenNullPointerException().isThrownBy(() -> shouldNotMatch("Yoda", color -> color.equals("green"), null))
                              .withMessage("The predicate description must not be null");
  }
}
