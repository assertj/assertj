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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldOnlyHaveFields.shouldOnlyHaveDeclaredFields;
import static org.assertj.core.error.ShouldOnlyHaveFields.shouldOnlyHaveFields;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Set;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.Player;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldOnlyHaveFields#create(Description, Representation)}</code>
 *
 * @author Filip Hrisafov
 */
class ShouldOnlyHaveFields_create_Test {

  private static final Set<String> EMPTY_STRING_SET = emptySet();

  @Test
  void should_create_error_message_for_fields() {
    // GIVEN
    ErrorMessageFactory factory = shouldOnlyHaveFields(Player.class,
                                                       newLinkedHashSet("name", "team"),
                                                       newLinkedHashSet("nickname"),
                                                       newLinkedHashSet("address"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.test.Player%n" +
                                   "to only have the following public accessible fields:%n" +
                                   "  [\"name\", \"team\"]%n" +
                                   "fields not found:%n" +
                                   "  [\"nickname\"]%n" +
                                   "and fields not expected:%n" +
                                   "  [\"address\"]"));
  }

  @Test
  void should_not_display_unexpected_fields_when_there_are_none_for_fields() {
    ErrorMessageFactory factory = shouldOnlyHaveFields(Player.class,
                                                       newLinkedHashSet("name", "team"),
                                                       newLinkedHashSet("nickname"),
                                                       EMPTY_STRING_SET);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.test.Player%n" +
                                   "to only have the following public accessible fields:%n" +
                                   "  [\"name\", \"team\"]%n" +
                                   "but could not find the following fields:%n" +
                                   "  [\"nickname\"]"));
  }

  @Test
  void should_not_display_fields_not_found_when_there_are_none_for_fields() {
    // GIVEN
    ErrorMessageFactory factory = shouldOnlyHaveFields(Player.class,
                                                       newLinkedHashSet("name", "team"),
                                                       EMPTY_STRING_SET,
                                                       newLinkedHashSet("address"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.test.Player%n" +
                                   "to only have the following public accessible fields:%n" +
                                   "  [\"name\", \"team\"]%n" +
                                   "but the following fields were unexpected:%n" +
                                   "  [\"address\"]"));
  }

  @Test
  void should_create_error_message_for_declared_fields() {
    // GIVEN
    ErrorMessageFactory factory = shouldOnlyHaveDeclaredFields(Player.class,
                                                               newLinkedHashSet("name", "team"),
                                                               newLinkedHashSet("nickname"),
                                                               newLinkedHashSet("address"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.test.Player%n" +
                                   "to only have the following declared fields:%n" +
                                   "  [\"name\", \"team\"]%n" +
                                   "fields not found:%n" +
                                   "  [\"nickname\"]%n" +
                                   "and fields not expected:%n" +
                                   "  [\"address\"]"));
  }

  @Test
  void should_not_display_unexpected_fields_when_there_are_none_for_declared_fields() {
    // GIVEN
    ErrorMessageFactory factory = shouldOnlyHaveDeclaredFields(Player.class,
                                                               newLinkedHashSet("name", "team"),
                                                               newLinkedHashSet("nickname"),
                                                               EMPTY_STRING_SET);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.test.Player%n" +
                                   "to only have the following declared fields:%n" +
                                   "  [\"name\", \"team\"]%n" +
                                   "but could not find the following fields:%n" +
                                   "  [\"nickname\"]"));
  }

  @Test
  void should_not_display_fields_not_found_when_there_are_none_for_declared_fields() {
    // GIVEN
    ErrorMessageFactory factory = shouldOnlyHaveDeclaredFields(Player.class,
                                                               newLinkedHashSet("name", "team"),
                                                               EMPTY_STRING_SET,
                                                               newLinkedHashSet("address"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(String.format("[Test] %n" +
                                          "Expecting%n" +
                                          "  org.assertj.core.test.Player%n" +
                                          "to only have the following declared fields:%n" +
                                          "  [\"name\", \"team\"]%n" +
                                          "but the following fields were unexpected:%n" +
                                          "  [\"address\"]"));
  }
}
