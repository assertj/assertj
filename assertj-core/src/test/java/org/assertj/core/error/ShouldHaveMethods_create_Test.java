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
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldHaveMethods.shouldHaveMethods;
import static org.assertj.core.error.ShouldHaveMethods.shouldNotHaveMethods;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.Sets.newTreeSet;

import java.lang.reflect.Modifier;
import org.assertj.core.description.TextDescription;
import org.assertj.core.testkit.Person;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHaveMethods}</code>
 */
class ShouldHaveMethods_create_Test {

  @Test
  void should_create_error_message_for_methods() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveMethods(Person.class, false,
                                                    newTreeSet("getName", "getAddress"),
                                                    newTreeSet("getAddress"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.testkit.Person%n" +
                                   "to have methods:%n" +
                                   "  [\"getAddress\", \"getName\"]%n" +
                                   "but could not find:%n" +
                                   "  [\"getAddress\"]"));
  }

  @Test
  void should_create_error_message_for_declared_methods() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveMethods(Person.class, true,
                                                    newTreeSet("getName", "getAddress"),
                                                    newTreeSet("getAddress"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.testkit.Person%n" +
                                   "to have declared methods:%n" +
                                   "  [\"getAddress\", \"getName\"]%n" +
                                   "but could not find:%n" +
                                   "  [\"getAddress\"]"));
  }

  @Test
  void should_create_error_message_for_shouldNotHave_PublicDeclared_Methods() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotHaveMethods(Person.class,
                                                       Modifier.toString(Modifier.PUBLIC), true,
                                                       newTreeSet("getName"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.testkit.Person%n" +
                                   "not to have any declared public methods but it has the following:%n" +
                                   "  [\"getName\"]"));
  }

  @Test
  void should_create_error_message_for_shouldNotHave_Public_Methods() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotHaveMethods(Person.class,
                                                       Modifier.toString(Modifier.PUBLIC), false,
                                                       newTreeSet("getName"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.testkit.Person%n" +
                                   "not to have any public methods but it has the following:%n" +
                                   "  [\"getName\"]"));
  }

  @Test
  void should_create_error_message_for_shouldNotHave_Declared_Methods() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotHaveMethods(Person.class, true, newTreeSet("getName"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.testkit.Person%n" +
                                   "not to have any declared methods but it has the following:%n" +
                                   "  [\"getName\"]"));
  }

  @Test
  void should_create_error_message_for_shouldNotHaveMethods() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotHaveMethods(Person.class, false, newTreeSet("getName"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.testkit.Person%n" +
                                   "not to have any methods but it has the following:%n" +
                                   "  [\"getName\"]"));
  }

  @Test
  void should_create_error_message_for_shouldHaveMethods_with_non_matching_modifier() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveMethods(Person.class, false,
                                                    newTreeSet("finalize"),
                                                    Modifier.toString(Modifier.PUBLIC),
                                                    mapOf(entry("finalize", Modifier.toString(Modifier.PROTECTED))));
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  org.assertj.core.testkit.Person%n" +
                                   "to have public methods:%n" +
                                   "  [\"finalize\"]%n" +
                                   "but the following are not public:%n" +
                                   "  {\"finalize\"=\"protected\"}"));
  }

}
