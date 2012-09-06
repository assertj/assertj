/*
 * Created on Feb 22, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.util.Lists.newArrayList;

import java.util.Collection;
import java.util.List;

import org.fest.assertions.test.ExpectedException;
import org.fest.test.Employee;
import org.fest.test.Name;
import org.fest.util.IntrospectionError;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link PropertySupport#propertyValues(String, Collection)}</code>.
 * 
 * @author Yvonne Wang
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Florent Biville
 */
public class PropertySupport_propertyValues_Test {

  private static Employee yoda;
  private static Employee luke;
  private static Iterable<Employee> employees;
  private static PropertySupport propertySupport;

  @BeforeClass
  public static void setUpOnce() {
    yoda = new Employee(6000L, new Name("Yoda"), 800);
    luke = new Employee(8000L, new Name("Luke", "Skywalker"), 26);
    employees = newArrayList(yoda, luke);
    propertySupport = new PropertySupport();
  }

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_return_empty_List_if_given_Iterable_is_null() {
    Iterable<Long> ids = propertySupport.propertyValues("ids", Long.class, null);
    assertEquals(emptyList(), ids);
  }

  @Test
  public void should_return_empty_List_if_given_Iterable_is_empty() {
    Iterable<Long> ids = propertySupport.propertyValues("ids", Long.class, emptySet());
    assertEquals(emptyList(), ids);
  }

  @Test
  public void should_return_empty_List_if_given_Iterable_contains_only_nulls() {
    Iterable<Long> ids = propertySupport.propertyValues("ids", Long.class, newArrayList(null, null));
    assertEquals(emptyList(), ids);
  }

  @Test
  public void should_remove_null_values_from_given_Iterable() {
    List<Employee> anotherList = newArrayList(yoda, null, luke, null);
    Iterable<Long> ids = propertySupport.propertyValues("id", Long.class, anotherList);
    assertEquals(newArrayList(6000L, 8000L), ids);
  }

  @Test
  public void should_return_values_of_simple_property() {
    Iterable<Long> ids = propertySupport.propertyValues("id", Long.class, employees);
    assertEquals(newArrayList(6000L, 8000L), ids);
  }

  @Test
  public void should_return_values_of_nested_property() {
    Iterable<String> firstNames = propertySupport.propertyValues("name.first", String.class, employees);
    assertEquals(newArrayList("Yoda", "Luke"), firstNames);
  }

  @Test
  public void should_throw_error_if_property_not_found() {
    thrown.expect(IntrospectionError.class);
    propertySupport.propertyValues("id.", Long.class, employees);
  }

  @Test
  public void should_extract_property() {
    Long id = propertySupport.propertyValue("id", Long.class, yoda);
    assertEquals(Long.valueOf(6000L), id);
  }
}
