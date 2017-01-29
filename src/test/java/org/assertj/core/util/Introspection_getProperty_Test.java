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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.introspection.Introspection.getPropertyGetter;

import java.lang.reflect.Method;

import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class Introspection_getProperty_Test {

  @Rule
  public ExpectedException thrown = none();

  private Employee judy;

  @Before
  public void initData() {
    judy = new Employee(100000.0, 31);
  }

  @Test
  public void get_getter_for_property() {
    Method getter = getPropertyGetter("age", judy);
    assertThat(getter).isNotNull();
  }

  @Test
  public void should_raise_an_error_because_of_missing_getter() {
    thrown.expect(IntrospectionError.class, "No getter for property 'salary' in org.assertj.core.util.Employee");
    getPropertyGetter("salary", judy);
  }

  @Test
  public void should_raise_an_error_because_of_non_public_getter_when_getter_does_not_exists() {
    thrown.expect(IntrospectionError.class,
                  "No public getter for property 'company' in org.assertj.core.util.Employee");
    getPropertyGetter("company", judy);
  }

  @Test
  public void should_raise_an_error_because_of_non_public_getter_when_getter_is_package_private() {
    thrown.expect(IntrospectionError.class,
                  "No public getter for property 'firstJob' in org.assertj.core.util.Employee");
    getPropertyGetter("firstJob", judy);
  }

  @Test
  public void should_raise_an_error_because_of_non_public_getter_when_getter_is_in_superclass() {
    thrown.expect(IntrospectionError.class,
                  "No public getter for property 'name' in org.assertj.core.util.Introspection_getProperty_Test$Example");
    getPropertyGetter("name", new Example());
  }

  public static class Example extends Super {
  }

  public static class Super {
    @SuppressWarnings("unused")
    private String getName() {
      return "a";
    }
  }
}
