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
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.Maps;
import org.assertj.core.test.Person;
import org.junit.Test;

import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.util.Sets.newTreeSet;

/**
 * Tests for <code>{@link ShouldHaveMethods}</code>
 */
public class ShouldHaveMethods_create_Test {

  @Test
  public void should_create_error_message_for_methods() {
    ErrorMessageFactory factory = ShouldHaveMethods.shouldHaveMethods(Person.class, false, newTreeSet("getName", "getAddress"), newTreeSet("getAddress"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
        + "Expecting%n"
        + "  <org.assertj.core.test.Person>%n"
        + "to have methods:%n"
        + "  <[\"getAddress\", \"getName\"]>%n"
        + "but it doesn't have:%n"
        + "  <[\"getAddress\"]>"));
  }

  @Test
  public void should_create_error_message_for_declared_methods() {
    ErrorMessageFactory factory = ShouldHaveMethods.shouldHaveMethods(Person.class, true, newTreeSet("getName", "getAddress"), newTreeSet("getAddress"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
        + "Expecting%n"
        + "  <org.assertj.core.test.Person>%n"
        + "to have declared methods:%n"
        + "  <[\"getAddress\", \"getName\"]>%n"
        + "but it doesn't have:%n"
        + "  <[\"getAddress\"]>"));
  }

  @Test
  public void should_create_error_message_for_shouldNotHavePublicDeclaredMethods() {
    ErrorMessageFactory factory = ShouldHaveMethods.shouldNotHaveMethods(Person.class, Modifier.toString(Modifier.PUBLIC),true, newTreeSet("getName"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
        + "Expecting%n"
        + "  <org.assertj.core.test.Person>%n"
        + "to have no declared public methods but have the following:%n"
        + "  <[\"getName\"]>"));
  }

  @Test
  public void should_create_error_message_for_shouldNotHavePublicMethods() {
    ErrorMessageFactory factory = ShouldHaveMethods.shouldNotHaveMethods(Person.class, Modifier.toString(Modifier.PUBLIC),false, newTreeSet("getName"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
        + "Expecting%n"
        + "  <org.assertj.core.test.Person>%n"
        + "to have no public methods but have the following:%n"
        + "  <[\"getName\"]>"));
  }

  @Test
  public void should_create_error_message_for_shouldNotHaveDeclaredMethods() {
    ErrorMessageFactory factory = ShouldHaveMethods.shouldNotHaveMethods(Person.class, true, newTreeSet("getName"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
        + "Expecting%n"
        + "  <org.assertj.core.test.Person>%n"
        + "to have no declared methods but have the following:%n"
        + "  <[\"getName\"]>"));
  }

  @Test
  public void should_create_error_message_for_shouldNotHaveMethods() {
    ErrorMessageFactory factory = ShouldHaveMethods.shouldNotHaveMethods(Person.class, false, newTreeSet("getName"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
        + "Expecting%n"
        + "  <org.assertj.core.test.Person>%n"
        + "to have no methods but have the following:%n"
        + "  <[\"getName\"]>"));
  }

  @Test
  public void should_create_error_message_for_shouldHaveMethodsNonMatchingModifier() {
    ErrorMessageFactory factory = ShouldHaveMethods.shouldHaveMethods(Person.class, false,
      newTreeSet("finalize"), Modifier.toString(Modifier.PUBLIC),
      Maps.mapOf(entry("finalize", Modifier.toString(Modifier.PROTECTED))));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
      "[Test] %n"
        + "Expecting%n"
        + "  <org.assertj.core.test.Person>%n"
        + "to have public methods:%n"
        + "  <[\"finalize\"]>%n"
        +  "but the following are not public:%n"
        + "  <{\"finalize\"=\"protected\"}>"));
  }

}
