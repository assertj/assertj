/*
 * Created on Feb 22, 2011
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.util.Collections.*;
import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.util.Collections.list;

import java.util.*;

import org.fest.assertions.test.*;
import org.fest.util.IntrospectionError;
import org.junit.*;

/**
 * Tests for <code>{@link PropertySupport#propertyValues(String, Collection)}</code>.
 *
 * @author Yvonne Wang
 */
public class PropertySupport_propertyValues_Test {

  private static Employee yoda;
  private static Employee luke;
  private static List<Employee> employees;
  private static PropertySupport propertySupport;

  @BeforeClass public static void setUpOnce() {
    yoda = new Employee(6000L, new Name("Yoda"), 800);
    luke = new Employee(8000L, new Name("Luke", "Skywalker"), 26);
    employees = list(yoda, luke);
    propertySupport = new PropertySupport();
  }

  @Rule public ExpectedException thrown = none();

  @Test public void should_throw_error_if_property_name_is_null() {
    thrown.expectNullPointerException("The name of the property to read should not be null");
    propertySupport.propertyValues(null, employees);
  }

  @Test public void should_throw_error_if_property_name_is_empty() {
    thrown.expectIllegalArgumentException("The name of the property to read should not be empty");
    propertySupport.propertyValues("", employees);
  }

  @Test public void should_return_empty_List_if_given_Collection_is_null() {
    List<Object> ids = propertySupport.propertyValues("ids", null);
    assertEquals(emptyList(), ids);
  }

  @Test public void should_return_empty_List_if_given_Collection_is_empty() {
    List<Object> ids = propertySupport.propertyValues("ids", emptySet());
    assertEquals(emptyList(), ids);
  }

  @Test public void should_return_empty_List_if_given_Collection_contains_only_nulls() {
    List<Object> ids = propertySupport.propertyValues("ids", list(null, null));
    assertEquals(emptyList(), ids);
  }

  @Test public void should_remove_null_values_from_given_Collection() {
    List<Employee> anotherList = list(yoda, null, luke, null);
    List<Object> ids = propertySupport.propertyValues("id", anotherList);
    assertEquals(list(6000L, 8000L), ids);
  }

  @Test public void should_return_values_of_simple_property() {
    List<Object> ids = propertySupport.propertyValues("id", employees);
    assertEquals(list(6000L, 8000L), ids);
  }

  @Test public void should_return_values_of_nested_property() {
    List<Object> firstNames = propertySupport.propertyValues("name.first", employees);
    assertEquals(list("Yoda", "Luke"), firstNames);
  }

  @Test public void should_throw_error_if_property_not_found() {
    thrown.expect(IntrospectionError.class);
    propertySupport.propertyValues("id.", employees);
  }
}
