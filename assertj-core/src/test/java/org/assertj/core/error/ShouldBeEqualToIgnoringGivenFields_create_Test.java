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
import static org.assertj.core.error.ShouldBeEqualToIgnoringFields.shouldBeEqualToIgnoringGivenFields;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeEqualToIgnoringFields#create(Description)}</code>.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
class ShouldBeEqualToIgnoringGivenFields_create_Test {

  private ErrorMessageFactory factory;

  @Test
  void should_create_error_message_with_all_fields_differences() {
    // GIVEN
    factory = shouldBeEqualToIgnoringGivenFields(new Jedi("Yoda", "blue"), list("name", "lightSaberColor"),
                                                 list("Yoda", "blue"), list("Yoda", "green"),
                                                 list("someIgnoredField"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting values:%n" +
                                   "  [\"Yoda\", \"green\"]%n" +
                                   "in fields:%n" +
                                   "  [\"name\", \"lightSaberColor\"]%n" +
                                   "but were:%n" +
                                   "  [\"Yoda\", \"blue\"]%n" +
                                   "in Yoda the Jedi.%n" +
                                   "Comparison was performed on all fields but [\"someIgnoredField\"]"));
  }

  @Test
  void should_create_error_message_with_single_field_difference() {
    // GIVEN
    factory = shouldBeEqualToIgnoringGivenFields(new Jedi("Yoda", "blue"), list("lightSaberColor"),
                                                 list("blue"), list("green"),
                                                 list("someIgnoredField"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting value \"green\" in field \"lightSaberColor\" " +
                                   "but was \"blue\" in Yoda the Jedi.%n" +
                                   "Comparison was performed on all fields but [\"someIgnoredField\"]"));
  }

  @Test
  void should_create_error_message_with_all_fields_differences_without_ignored_fields() {
    // GIVEN
    List<String> ignoredFields = list();
    factory = shouldBeEqualToIgnoringGivenFields(new Jedi("Yoda", "blue"), list("name", "lightSaberColor"),
                                                 list("Yoda", "blue"), list("Yoda", "green"), ignoredFields);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting values:%n" +
                                   "  [\"Yoda\", \"green\"]%n" +
                                   "in fields:%n" +
                                   "  [\"name\", \"lightSaberColor\"]%n" +
                                   "but were:%n" +
                                   "  [\"Yoda\", \"blue\"]%n" +
                                   "in Yoda the Jedi.%n" +
                                   "Comparison was performed on all fields"));
  }

  @Test
  void should_create_error_message_with_single_field_difference_without_ignored_fields() {
    // GIVEN
    List<String> ignoredFields = list();
    factory = shouldBeEqualToIgnoringGivenFields(new Jedi("Yoda", "blue"), list("lightSaberColor"),
                                                 list("blue"), list("green"),
                                                 ignoredFields);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting value \"green\" " +
                                   "in field \"lightSaberColor\" " +
                                   "but was \"blue\" in Yoda the Jedi.%n" +
                                   "Comparison was performed on all fields"));
  }

}
