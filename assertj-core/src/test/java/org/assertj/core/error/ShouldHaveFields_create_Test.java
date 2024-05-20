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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveFields.shouldHaveDeclaredFields;
import static org.assertj.core.error.ShouldHaveFields.shouldHaveFields;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.Person;
import org.junit.jupiter.api.Test;

class ShouldHaveFields_create_Test {

  @Test
  void should_create_error_message_for_fields() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveFields(Person.class, newLinkedHashSet("name", "address"),
                                                   newLinkedHashSet("address"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting%n"
                                   + "  org.assertj.core.testkit.Person%n"
                                   + "to have the following public accessible fields:%n"
                                   + "  [\"name\", \"address\"]%n"
                                   + "but it doesn't have:%n"
                                   + "  [\"address\"]"));
  }

  @Test
  void should_create_error_message_for_declared_fields() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveDeclaredFields(Person.class, newLinkedHashSet("name", "address"),
                                                           newLinkedHashSet("address"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting%n"
                                   + "  org.assertj.core.testkit.Person%n"
                                   + "to have the following declared fields:%n"
                                   + "  [\"name\", \"address\"]%n"
                                   + "but it doesn't have:%n"
                                   + "  [\"address\"]"));
  }
}
