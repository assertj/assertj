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
package org.assertj.core.util.introspection;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Collection;
import java.util.List;

import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.assertj.core.test.VehicleFactory;
import org.junit.Before;
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

  private Employee yoda;
  private Employee luke;
  private Iterable<Employee> employees;

  @Before
  public void setUpOnce() {
    yoda = new Employee(6000L, new Name("Yoda"), 800);
    luke = new Employee(8000L, new Name("Luke", "Skywalker"), 26);
    employees = newArrayList(yoda, luke);
  }

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_return_empty_List_if_given_Iterable_is_null() {
    Iterable<Integer> ages = PropertySupport.instance().propertyValues("ages", Integer.class, null);
    assertThat(ages).isEmpty();
  }

  @Test
  public void should_return_empty_List_if_given_Iterable_is_empty() {
    Iterable<Integer> ages = PropertySupport.instance().propertyValues("ages", Integer.class, emptySet());
    assertThat(ages).isEmpty();
  }

  @Test
  public void should_return_null_elements_for_null_property_value() {
    List<Employee> list = newArrayList(null, null);
    Iterable<Integer> ages = PropertySupport.instance().propertyValues("ages", Integer.class, list);
    assertThat(ages).containsExactly(null, null);
    
    list = newArrayList(yoda, luke, null, null);
    ages = PropertySupport.instance().propertyValues("age", Integer.class, list);
    assertThat(ages).containsExactly(800, 26,null, null);
  }

  @Test
  public void should_return_values_of_simple_property() {
    Iterable<Integer> ages = PropertySupport.instance().propertyValues("age", Integer.class, employees);
    assertThat(ages).containsExactly(800, 26);
  }

  @Test
  public void should_return_values_of_simple_property_as_objects() {
    Iterable<Integer> ages = PropertySupport.instance().propertyValues("age", Integer.class, employees);
    Iterable<Object> agesAsObjects = PropertySupport.instance().propertyValues("age", employees);
    assertThat(ages).isEqualTo(agesAsObjects);
    Iterable<String> firstNames = PropertySupport.instance().propertyValues("name.first", String.class, employees);
    Iterable<Object> firstNamesAsObjects = PropertySupport.instance().propertyValues("name.first", employees);
    assertThat(firstNames).isEqualTo(firstNamesAsObjects);
  }

  @Test
  public void should_return_values_of_nested_property() {
    Iterable<String> firstNames = PropertySupport.instance().propertyValues("name.first", String.class, employees);
    assertThat(firstNames).containsExactly("Yoda", "Luke");
  }

  @Test
  public void should_throw_error_if_property_not_found() {
    thrown.expectIntrospectionError();
    PropertySupport.instance().propertyValues("foo", Integer.class, employees);
  }

  @Test
  public void should_throw_error_if_property_name_is_null() {
    thrown.expectIllegalArgumentException();
    PropertySupport.instance().propertyValueOf(null, Integer.class, employees);
  }

  @Test
  public void should_extract_property() {
    Integer age = PropertySupport.instance().propertyValue("age", Integer.class, yoda);
    assertThat(age).isEqualTo(800);
  }

  @Test
  public void should_extract_nested_property() {
    String firstName = PropertySupport.instance().propertyValueOf("name.first", String.class, yoda);
    assertThat(firstName).isEqualTo("Yoda");
    
    yoda.name.first = null;
    firstName = PropertySupport.instance().propertyValueOf("name.first", String.class, yoda);
    assertThat(firstName).isNull();
    
    yoda.name = null;
    firstName = PropertySupport.instance().propertyValueOf("name.first", String.class, yoda);
    assertThat(firstName).isNull();
  }
  
  @Test
  public void should_return_properties_of_inner_class() {
    VehicleFactory vehicleFactory = new VehicleFactory();
    List<String> names = PropertySupport.instance().propertyValues("name", String.class, vehicleFactory.getVehicles());
    assertThat(names).containsExactly("Toyota", "Honda", "Audi");
  }

  @Test
  public void should_return_property_from_superclass() {
    assertThat(PropertySupport.instance().propertyValues("class", Class.class, employees)).containsExactly(Employee.class,
                                                                                                           Employee.class);
  }

}
