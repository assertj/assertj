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
package org.assertj.core.api.object;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.testkit.Jedi;
import org.assertj.core.testkit.Person;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectAssert#hasNoNullFieldsOrPropertiesExcept(String...)}</code>.
 *
 * @author Johannes Brodwall
 * @author Vladimir Chernikov
 */
class ObjectAssert_hasNoNullFieldsOrProperties_Test extends ObjectAssertBaseTest {

  private static final String FIELD_NAME = "color"; // field in org.assertj.core.testkit.Person

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.hasNoNullFieldsOrPropertiesExcept(FIELD_NAME);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertHasNoNullFieldsOrPropertiesExcept(getInfo(assertions), getActual(assertions), FIELD_NAME);
  }

  @Test
  void should_fail_if_a_public_field_is_null() {
    // GIVEN
    Jedi jedi = new Jedi("Yoda", null);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(jedi).hasNoNullFieldsOrPropertiesExcept("name",
                                                                                                       "strangeNotReadablePrivateField"));
    // THEN
    then(assertionError).hasMessage(format("%n"
                                           + "Expecting%n"
                                           + "  Yoda the Jedi%n"
                                           + "not to have any null property or field, but \"lightSaberColor\" was null.%n"
                                           + "Check was performed on all fields/properties except: [\"name\", \"strangeNotReadablePrivateField\"]"));
  }

  @Test
  void should_fail_if_a_private_field_is_null() {
    // GIVEN
    Jedi jedi = new Jedi("Yoda", "Blue");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(jedi).hasNoNullFieldsOrProperties());
    // THEN
    then(assertionError).hasMessage(format("%n"
                                           + "Expecting%n"
                                           + "  Yoda the Jedi%n"
                                           + "not to have any null property or field, but \"strangeNotReadablePrivateField\" was null.%n"
                                           + "Check was performed on all fields/properties."));
  }

  @Test
  void should_fail_if_all_fields_or_properties_are_not_set() {
    // GIVEN
    Jedi jedi = new Jedi(null, null);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(jedi).hasNoNullFieldsOrProperties());
    // THEN
    then(assertionError).hasMessage(format("%n"
                                           + "Expecting%n"
                                           + "  null the Jedi%n"
                                           + "to have a property or a field named [\"lightSaberColor\", \"strangeNotReadablePrivateField\", \"name\"].%n"
                                           + "Check was performed on all fields/properties."));
  }

  @Test
  void should_fail_if_a_property_is_null() {
    // GIVEN
    Person nobody = new Person(null);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(nobody).hasNoNullFieldsOrProperties());
    // THEN
    then(assertionError).hasMessage(format("%n"
                                           + "Expecting%n"
                                           + "  Person[name='null']%n"
                                           + "not to have any null property or field, but \"name\" was null.%n"
                                           + "Check was performed on all fields/properties."));
  }

  @Test
  void should_pass_if_all_fields_or_properties_but_the_ignored_ones_are_set() {
    // GIVEN
    Jedi jedi = new Jedi("Yoda", null);
    // WHEN/THEN
    then(jedi).hasNoNullFieldsOrPropertiesExcept("lightSaberColor", "strangeNotReadablePrivateField");
  }

  @Test
  public void should_fail_if_a_field_does_not_exist() {
    // GIVEN
    Jedi jedi = new Jedi("Yoda", "Blue");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(jedi).hasNoNullFieldsOrPropertiesExcept("father"));
    // THEN
    then(assertionError).hasMessage(format("[ignored fields existence check] %n"
                                           + "Expecting%n"
                                           + "  org.assertj.core.testkit.Jedi%n"
                                           + "to have the following declared fields:%n"
                                           + "  [\"father\"]%n"
                                           + "but it doesn't have:%n"
                                           + "  [\"father\"]"));
  }

  @Test
  public void should_fail_if_a_field_does_not_exist_honoring_current_description() {
    // GIVEN
    Jedi jedi = new Jedi("Yoda", "Blue");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(jedi).as("my description")
                                                                    .hasNoNullFieldsOrPropertiesExcept("father"));
    // THEN
    then(assertionError).hasMessage(format("[my description - ignored fields existence check] %n"
                                           + "Expecting%n"
                                           + "  org.assertj.core.testkit.Jedi%n"
                                           + "to have the following declared fields:%n"
                                           + "  [\"father\"]%n"
                                           + "but it doesn't have:%n"
                                           + "  [\"father\"]"));
  }
}
