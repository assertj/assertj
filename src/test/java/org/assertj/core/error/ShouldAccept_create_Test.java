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
package org.assertj.core.error;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.test.ExpectedException.none;

public class ShouldAccept_create_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_create_error_message_with_default_predicate_description() {
    ErrorMessageFactory factory = shouldAccept(color -> color.equals("green"), "Yoda", PredicateDescription.GIVEN);
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message)
      .isEqualTo(format("[Test] %nExpecting:%n  <given predicate>%nto accept <\"Yoda\"> but it did not."));
  }

  @Test
  public void should_create_error_message_with_predicate_description() {
    ErrorMessageFactory factory = shouldAccept((String color) -> color.equals("green"), "Yoda",
                                               new PredicateDescription("green light saber"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(
      format("[Test] %nExpecting:%n  <'green light saber' predicate>%nto accept <\"Yoda\"> but it did not."));
  }

  @Test
  public void should_fail_if_predicate_description_is_null() {
    // then
    thrown.expectNullPointerException("The predicate description must not be null");
    // when
    shouldAccept(color -> color.equals("green"), "Yoda", null);
  }

}
