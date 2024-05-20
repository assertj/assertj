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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.introspection.Introspection.getPropertyGetter;

import java.lang.reflect.Method;

import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;

class Introspection_getProperty_Test {

  private final Employee judy = new Employee(100000.0, 31);

  @Test
  void should_succeed() {
    // WHEN
    Method getter = getPropertyGetter("age", judy);
    // THEN
    then(getter).isNotNull();
  }

  @Test
  void should_raise_an_error_because_of_missing_getter() {
    // WHEN
    Throwable thrown = catchThrowable(() -> getPropertyGetter("salary", judy));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class)
                .hasMessage("No getter for property 'salary' in org.assertj.core.util.Employee");
  }

  @Test
  void should_raise_an_error_because_of_non_public_getter_when_getter_does_not_exists() {
    // WHEN
    Throwable thrown = catchThrowable(() -> getPropertyGetter("company", judy));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class)
                .hasMessage("No public getter for property 'company' in org.assertj.core.util.Employee");
  }

  @Test
  void should_raise_an_error_because_of_non_public_getter_when_getter_is_package_private() {
    // WHEN
    Throwable thrown = catchThrowable(() -> getPropertyGetter("firstJob", judy));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class)
                .hasMessage("No public getter for property 'firstJob' in org.assertj.core.util.Employee");
  }

  @Test
  void should_raise_an_error_because_of_non_public_getter_when_getter_is_in_superclass() {
    // WHEN
    Throwable thrown = catchThrowable(() -> getPropertyGetter("name", new Example()));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class)
                .hasMessage("No public getter for property 'name' in org.assertj.core.util.Introspection_getProperty_Test$Example");
  }

  @Test
  void should_raise_an_error_because_of_getter_with_void_return_type() {
    // WHEN
    Throwable thrown = catchThrowable(() -> getPropertyGetter("surname", new VoidGetter()));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class)
                .hasMessage("No getter for property 'surname' in org.assertj.core.util.Introspection_getProperty_Test$VoidGetter");
  }

  static class Example extends Super {
  }

  static class Super {
    @SuppressWarnings("unused")
    private String getName() {
      return "a";
    }
  }

  static class VoidGetter {
    public void getSurname() {}
  }

}
