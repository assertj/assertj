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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveExactlyFields.shouldHaveExactlyDeclaredFields;
import static org.assertj.core.error.ShouldHaveExactlyFields.shouldHaveExactlyFields;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.Player;
import org.assertj.core.util.Sets;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link ShouldHaveExactlyFields#create(Description, Representation)}</code>
 *
 * @author Filip Hrisafov
 */
public class ShouldHaveExactlyFields_create_Test {

  @Test
  public void should_create_error_message_for_fields() {
    ErrorMessageFactory factory = shouldHaveExactlyFields(Player.class, newLinkedHashSet("name", "team"),
                                                          newLinkedHashSet("nickname"),
                                                          newLinkedHashSet("address"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
      + "Expecting%n"
      + "  <org.assertj.core.test.Player>%n"
      + "to have exactly fields:%n"
      + "  <[\"name\", \"team\"]>%n"
      + "fields not found:%n"
      + "  <[\"nickname\"]>%n"
      + "and fields not expected:%n"
      + "  <[\"address\"]>"));
  }

  @Test
  public void should_not_display_unexpected_fields_when_there_are_none_for_fields() {
    ErrorMessageFactory factory = shouldHaveExactlyFields(Player.class, newLinkedHashSet("name", "team"),
                                                          newLinkedHashSet("nickname"),
                                                          Sets.<String>newLinkedHashSet());
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
      + "Expecting%n"
      + "  <org.assertj.core.test.Player>%n"
      + "to have exactly fields:%n"
      + "  <[\"name\", \"team\"]>%n"
      + "but could not find the following fields:%n"
      + "  <[\"nickname\"]>"));
  }

  @Test
  public void should_not_display_fields_not_found_when_there_are_none_for_fields() {
    ErrorMessageFactory factory = shouldHaveExactlyFields(Player.class, newLinkedHashSet("name", "team"),
                                                          Sets.<String>newLinkedHashSet(),
                                                          newLinkedHashSet("address"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
      + "Expecting%n"
      + "  <org.assertj.core.test.Player>%n"
      + "to have exactly fields:%n"
      + "  <[\"name\", \"team\"]>%n"
      + "but the following fields were unexpected:%n"
      + "  <[\"address\"]>"));
  }

  @Test
  public void should_create_error_message_for_declared_fields() {
    ErrorMessageFactory factory = shouldHaveExactlyDeclaredFields(Player.class, newLinkedHashSet("name", "team"),
                                                          newLinkedHashSet("nickname"),
                                                          newLinkedHashSet("address"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
      + "Expecting%n"
      + "  <org.assertj.core.test.Player>%n"
      + "to have exactly declared fields:%n"
      + "  <[\"name\", \"team\"]>%n"
      + "fields not found:%n"
      + "  <[\"nickname\"]>%n"
      + "and fields not expected:%n"
      + "  <[\"address\"]>"));
  }

  @Test
  public void should_not_display_unexpected_fields_when_there_are_none_for_declared_fields() {
    ErrorMessageFactory factory = shouldHaveExactlyDeclaredFields(Player.class, newLinkedHashSet("name", "team"),
                                                                  newLinkedHashSet("nickname"),
                                                                  Sets.<String>newLinkedHashSet());
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
      + "Expecting%n"
      + "  <org.assertj.core.test.Player>%n"
      + "to have exactly declared fields:%n"
      + "  <[\"name\", \"team\"]>%n"
      + "but could not find the following fields:%n"
      + "  <[\"nickname\"]>"));
  }

  @Test
  public void should_not_display_fields_not_found_when_there_are_none_for_declared_fields() {
    ErrorMessageFactory factory = shouldHaveExactlyDeclaredFields(Player.class, newLinkedHashSet("name", "team"),
                                                                  Sets.<String>newLinkedHashSet(),
                                                                  newLinkedHashSet("address"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
      + "Expecting%n"
      + "  <org.assertj.core.test.Player>%n"
      + "to have exactly declared fields:%n"
      + "  <[\"name\", \"team\"]>%n"
      + "but the following fields were unexpected:%n"
      + "  <[\"address\"]>"));
  }
}
