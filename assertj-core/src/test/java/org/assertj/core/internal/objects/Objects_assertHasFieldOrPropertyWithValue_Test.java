/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.objects;

import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchRuntimeException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePropertyOrField.shouldHavePropertyOrField;
import static org.assertj.core.error.ShouldHavePropertyOrFieldWithValue.shouldHavePropertyOrFieldWithValue;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.jupiter.api.Test;

class Objects_assertHasFieldOrPropertyWithValue_Test extends ObjectsBaseTest {

  @Test
  void should_pass_if_actual_has_expected_field_and_value() {
    // GIVEN
    Object actual = new Data();
    // WHEN/THEN
    objects.assertHasFieldOrPropertyWithValue(INFO, actual, "field1", "foo");
  }

  @Test
  void should_pass_if_actual_has_expected_property_and_value_not_backed_by_field() {
    // GIVEN
    Object actual = new Data();
    // WHEN/THEN
    objects.assertHasFieldOrPropertyWithValue(INFO, actual, "field3", "bar");
  }

  @Test
  void should_pass_if_actual_has_expected_list_field_and_value_with_index() {
    // GIVEN
    Object data = new Data();
    // WHEN/THEN
    objects.assertHasFieldOrPropertyWithValue(INFO, data, "listField", list("bar", "baz"));
    objects.assertHasFieldOrPropertyWithValue(INFO, data, "listField[0]", "bar");
    objects.assertHasFieldOrPropertyWithValue(INFO, data, "listField[1]", "baz");
  }

  @Test
  void should_pass_if_actual_has_expected_nested_list_field_and_value_with_index() {
    // GIVEN
    Wrapper wrapper = new Wrapper(new Data());
    // WHEN/THEN
    objects.assertHasFieldOrPropertyWithValue(INFO, wrapper, "value.listField", list("bar", "baz"));
    objects.assertHasFieldOrPropertyWithValue(INFO, wrapper, "value.listField[0]", "bar");
    objects.assertHasFieldOrPropertyWithValue(INFO, wrapper, "value.listField[1]", "baz");
    objects.assertHasFieldOrPropertyWithValue(INFO, new CompanyWithList(), "addresses[1].street", "butcher street");
  }

  @Test
  void should_pass_if_actual_has_expected_array_field_and_value_with_index() {
    // GIVEN
    Object data = new Data();
    // WHEN/THEN
    objects.assertHasFieldOrPropertyWithValue(INFO, data, "arrayField", array("bar", "baz"));
    objects.assertHasFieldOrPropertyWithValue(INFO, data, "arrayField[0]", "bar");
    objects.assertHasFieldOrPropertyWithValue(INFO, data, "arrayField[1]", "baz");
    objects.assertHasFieldOrPropertyWithValue(INFO, new CompanyWithArray(), "addresses[1].street", "butcher street");
  }

  @Test
  void should_pass_if_actual_has_expected_nested_array_field_and_value_with_index() {
    // GIVEN
    Wrapper wrapper = new Wrapper(new Data());
    // WHEN/THEN
    objects.assertHasFieldOrPropertyWithValue(INFO, wrapper, "value.arrayField", array("bar", "baz"));
    objects.assertHasFieldOrPropertyWithValue(INFO, wrapper, "value.arrayField[0]", "bar");
    objects.assertHasFieldOrPropertyWithValue(INFO, wrapper, "value.arrayField[1]", "baz");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Object actual = null;
    // WHEN
    var error = expectAssertionError(() -> objects.assertHasFieldOrPropertyWithValue(INFO, actual, "field1", 123));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_field_or_property_exists_but_does_not_have_expected_value() {
    // GIVEN
    Object actual = new Data();
    String fieldName = "field1";
    // WHEN
    var error = expectAssertionError(() -> objects.assertHasFieldOrPropertyWithValue(INFO, actual, fieldName, "baz"));
    // THEN
    then(error).hasMessage(shouldHavePropertyOrFieldWithValue(actual, fieldName, "baz", "foo").create());
  }

  @Test
  void should_fail_if_field_or_property_does_not_exists() {
    // GIVEN
    Object actual = new Data();
    String fieldName = "unknown_field";
    // WHEN
    var error = expectAssertionError(() -> objects.assertHasFieldOrPropertyWithValue(INFO, actual, fieldName, "foo"));
    // THEN
    then(error).hasMessage(shouldHavePropertyOrField(actual, fieldName).create());
  }

  @Test
  void should_fail_if_field_exists_but_is_static() {
    // GIVEN
    Object actual = new Data();
    String fieldName = "staticField";
    // WHEN
    var error = expectAssertionError(() -> objects.assertHasFieldOrPropertyWithValue(INFO, actual, fieldName, "foo"));
    // THEN
    then(error).hasMessage(shouldHavePropertyOrField(actual, fieldName).create());
  }

  @Test
  void should_fail_if_given_field_or_property_name_is_null() {
    // GIVEN
    Object actual = new Data();
    String fieldName = null;
    // WHEN
    var exception = catchIllegalArgumentException(() -> objects.assertHasFieldOrPropertyWithValue(INFO, actual, fieldName, 12));
    // THEN
    then(exception).hasMessage("The name of the property/field to read should not be null");
  }

  @Test
  void should_use_field_if_getters_throws_exception() {
    // GIVEN
    Object actual = new Data();
    String fieldName = "fieldWithGetterThrowing";
    // WHEN/THEN
    objects.assertHasFieldOrPropertyWithValue(INFO, actual, fieldName, "dummy");
  }

  @Test
  void should_rethrow_getter_exception_if_field_is_missing() {
    // GIVEN
    Object actual = new Data();
    String fieldName = "unknownFieldWithGetterThrowing";
    // WHEN
    var exception = catchRuntimeException(() -> objects.assertHasFieldOrPropertyWithValue(INFO, actual, fieldName, "foo"));
    // THEN
    then(exception).hasMessage("some dummy exception");
  }

  @SuppressWarnings({ "unused", "FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection" })
  private static class Data {

    private final Object field1 = "foo";
    private Object field2;
    private final List<String> listField = new ArrayList<>(1);
    private final String[] arrayField = new String[] { "bar", "baz" };
    private final Object fieldWithGetterThrowing = "dummy";
    private static Object staticField;

    public Data() {
      listField.add("bar");
      listField.add("baz");
    }

    @Override
    public String toString() {
      return "data";
    }

    public Object getField3() {
      return "bar";
    }

    public Object getUnknownFieldWithGetterThrowing() {
      throw new RuntimeException("some dummy exception");
    }

    public Object getFieldWithGetterThrowing() {
      throw new RuntimeException("some dummy exception");
    }
  }

  static class Wrapper {
    Object value;

    Wrapper(Object value) {
      this.value = value;
    }
  }

  static class CompanyWithArray {
    @SuppressWarnings("unused")
    Address[] addresses = {
        new Address("baker street"),
        new Address("butcher street")
    };
  }

  static class CompanyWithList {
    @SuppressWarnings("unused")
    List<Address> addresses = list(new Address("baker street"), new Address("butcher street"));
  }

  static class Address {
    String street;

    public Address(String street) {
      this.street = street;
    }
  }

}
