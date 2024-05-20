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
import static org.assertj.core.error.ShouldBeEqualByComparingOnlyGivenFields.shouldBeEqualComparingOnlyGivenFields;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeEqualToIgnoringFields#create(Description)}</code>.
 *
 * @author Nicolas François
 */
class ShouldBeEqualByComparingOnlyGivenFields_create_Test {

  private ErrorMessageFactory factory;

  @Test
  void should_create_error_message_with_all_fields_differences() {
    // GIVEN
    factory = shouldBeEqualComparingOnlyGivenFields(new Jedi("Luke", "blue"), list("name", "lightSaberColor"),
                                                    list("Luke", "blue"), list("Yoda", "green"),
                                                    list("name", "lightSaberColor"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting values:%n" +
                                   "  [\"Yoda\", \"green\"]%n" +
                                   "in fields:%n" +
                                   "  [\"name\", \"lightSaberColor\"]%n" +
                                   "but were:%n" +
                                   "  [\"Luke\", \"blue\"]%n" +
                                   "in Luke the Jedi.%n" +
                                   "Comparison was performed on fields:%n" +
                                   "  [\"name\", \"lightSaberColor\"]"));
  }

  @Test
  void should_create_error_message_with_single_field_difference() {
    // GIVEN
    factory = shouldBeEqualComparingOnlyGivenFields(new Jedi("Yoda", "green"), list("lightSaberColor"),
                                                    list("green"), list("blue"),
                                                    list("lightSaberColor"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting value \"blue\"" +
                                   " in field \"lightSaberColor\"" +
                                   " but was \"green\"" +
                                   " in Yoda the Jedi"));
  }

}
