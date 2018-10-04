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

import static org.assertj.core.error.ShouldHaveAllNullFields.shouldHaveAllNullFields;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.ObjectsBaseTest;
import org.assertj.core.test.Jedi;
import org.assertj.core.test.Person;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Objects#assertHasAllNullFieldsOrPropertiesExcept(AssertionInfo, Object, String...)}</code>.
 *
 * @author Vladimir Chernikov
 */
class Objects_assertHasAllNullFieldsOrPropertiesExcept_Test extends ObjectsBaseTest {

  private static final AssertionInfo INFO = someInfo();

  @Test
  void should_pass_if_all_fields_or_properties_are_null_and_no_ignored_fields_are_specified() {
    // GIVEN
    Jedi jedi = new Jedi(null, null);
    // THEN
    objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), jedi);
  }

  @Test
  void should_pass_if_all_fields_or_properties_are_null_except_for_the_ones_to_ignore() {
    // GIVEN
    Jedi jedi = new Jedi("Kenobi", null);
    // THEN
    objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), jedi, "name");
  }

  @Test
  void should_success_if_private_field_is_not_null_but_ignored() {
    // GIVEN
    PersonWithPrivateField person = new PersonWithPrivateField(null, "value");
    // THEN
    objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), person, "privateField");
  }

  @Test
  void should_fail_if_one_of_the_field_or_property_is_not_null() {
    // GIVEN
    Jedi jedi = new Jedi("Kenobi", null);
    // WHEN
    expectAssertionError(() -> objects.assertHasAllNullFieldsOrPropertiesExcept(INFO, jedi));
    // THEN
    verify(failures).failure(INFO, shouldHaveAllNullFields(jedi, list("name"), emptyList()));
  }

  @Test
  void should_fail_if_both_public_field_or_property_are_set() {
    // GIVEN
    Jedi jedi = new Jedi("Kenobi", "blue");
    // WHEN
    expectAssertionError(() -> objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), jedi));
    // THEN
    verify(failures).failure(INFO, shouldHaveAllNullFields(jedi, list("lightSaberColor", "name"), emptyList()));
  }

  @Test
  void should_fail_if_one_field_or_property_is_set_even_if_another_is_ignored() {
    // GIVEN
    Jedi jedi = new Jedi("Kenobi", "blue");
    // WHEN
    expectAssertionError(() -> objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), jedi, "name"));
    // THEN
    verify(failures).failure(INFO, shouldHaveAllNullFields(jedi, list("lightSaberColor"), list("name")));
  }

  @Test
  void should_fail_if_private_field_is_not_null() {
    // GIVEN
    PersonWithPrivateField person = new PersonWithPrivateField(null, "value");
    // WHEN
    expectAssertionError(() -> objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), person));
    // THEN
    verify(failures).failure(INFO, shouldHaveAllNullFields(person, list("privateField"), emptyList()));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    ThrowingCallable code = () -> objects.assertHasAllNullFieldsOrPropertiesExcept(someInfo(), null);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldNotBeNull().create());
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
