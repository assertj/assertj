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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.objects;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Jedi;
import org.assertj.core.test.Person;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someInfo;

/**
 * Tests for <code>{@link Objects#assertHasAllNullFieldsOrPropertiesExcept(AssertionInfo, Object, String...)}</code>.
 *
 * @author Vladimir Chernikov
 */
class Objects_assertHasAllNullFieldsOrPropertiesExcept_Test extends ObjectsBaseTest {

  private Jedi jedi;

  @Test
  void should_pass_if_all_fields_or_properties_are_null_and_the_ignored_fields_are_not_set() {
    jedi = new Jedi(null, null);
    objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), jedi);
  }

  @Test
  void should_pass_if_all_fields_or_properties_are_null_but_the_ignored_ones_are_set() {
    jedi = new Jedi("Kenobi", null);
    objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), jedi, "name");
  }

  @Test
  void should_success_if_private_field_is_not_null_but_ignored() {
    PersonWithPrivateField person = new PersonWithPrivateField(null, "value");
    objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), person, "privateField");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->
      objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), null)
    );
  }

  @Test
  void should_fail_if_one_field_or_property_is_set() {
    jedi = new Jedi("Kenobi", null);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->
      objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), jedi)
    ).withMessage(format("%n"
      + "Expecting%n"
      + "  <Kenobi the Jedi>%n"
      + "to only have null property or field, but <\"name\"> was not null.%n"
      + "Check was performed on all fields/properties.")
    );
  }

  @Test
  void should_fail_if_both_public_field_or_property_are_set() {
    jedi = new Jedi("Kenobi", "blue");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->
      objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), jedi)
    ).withMessage(format("%n"
      + "Expecting%n"
      + "  <Kenobi the Jedi>%n"
      + "to only have null properties or fields but these were not null:%n"
      + " <[\"lightSaberColor\", \"name\"]> are null.%n"
      + "Check was performed on all fields/properties.")
    );
  }

  @Test
  void should_fail_if_one_field_or_property_is_set_but_another_is_ignored() {
    jedi = new Jedi("Kenobi", "blue");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->
      objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), jedi, "name")
    ).withMessage(format("%n"
      + "Expecting%n"
      + "  <Kenobi the Jedi>%n"
      + "to only have null property or field, but <\"lightSaberColor\"> was not null.%n"
      + "Check was performed on all fields/properties except: <[\"name\"]>.")
    );
  }

  @Test
  void should_fail_if_private_field_is_not_null() {
    PersonWithPrivateField person = new PersonWithPrivateField(null, "value");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->
      objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), person)
    ).withMessage(format("%n"
      + "Expecting%n"
      + "  <Person[name='null']>%n"
      + "to only have null property or field, but <\"privateField\"> was not null.%n"
      + "Check was performed on all fields/properties.")
    );
  }

  private class PersonWithPrivateField extends Person {

    private Object privateField;

    PersonWithPrivateField(String name, Object privateFieldValue) {
      super(name);
      privateField = privateFieldValue;
    }

    @SuppressWarnings("unused")
    private Object getPrivateField() {
      return privateField;
    }
  }
}
