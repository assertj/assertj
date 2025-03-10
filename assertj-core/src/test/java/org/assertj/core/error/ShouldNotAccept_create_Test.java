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
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.assertj.core.error.ShouldNotAccept.shouldNotAccept;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

class ShouldNotAccept_create_Test {

  @Test
  void should_create_error_message_with_default_predicate_description() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotAccept(color -> color.equals("red"), "Yoda", PredicateDescription.GIVEN);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[Test] %nExpecting actual:%n  given predicate%nnot to accept \"Yoda\" but it did.".formatted());
  }

  @Test
  void should_create_error_message_with_predicate_description() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotAccept((String color) -> color.equals("red"), "Yoda",
                                                  new PredicateDescription("red light saber"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[Test] %nExpecting actual:%n  'red light saber' predicate%nnot to accept \"Yoda\" but it did.".formatted());
  }

  @Test
  void should_fail_if_predicate_description_is_null() {
    thenNullPointerException().isThrownBy(() -> shouldNotAccept(color -> color.equals("red"), "Yoda", null))
                              .withMessage("The predicate description must not be null");
  }

}
