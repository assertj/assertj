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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link FieldSupport#fieldValues(String, Class, Iterable)}</code>.
 * 
 * @author Joel Costigliola
 */
public class FieldSupport_fieldValues_Test {

  private Employee yoda;
  private Employee luke;
  private List<Employee> employees;
  private FieldSupport fieldSupport = FieldSupport.extraction();

  @Before
  public void setUp() {
	yoda = new Employee(1L, new Name("Yoda"), 800);
	luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
	employees = newArrayList(yoda, luke);
  }

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_return_empty_List_if_given_Iterable_is_null() {
	Iterable<Long> ids = fieldSupport.fieldValues("ids", Long.class, (Iterable<Long>) null);
	assertThat(ids).isEqualTo(emptyList());
  }

  @Test
  public void should_return_empty_List_if_given_Iterable_is_empty() {
	Iterable<Long> ids = fieldSupport.fieldValues("ids", Long.class, emptySet());
	assertThat(ids).isEqualTo(emptyList());
  }

  @Test
  public void should_return_null_elements_for_null_field_value() {
	List<Employee> list = newArrayList(null, null);
	Iterable<Long> ages = fieldSupport.fieldValues("id", Long.class, list);
	assertThat(ages).containsExactly(null, null);

	luke.setName(null);
	list = newArrayList(yoda, luke, null, null);
	Iterable<Name> names = fieldSupport.fieldValues("name", Name.class, list);
	assertThat(names).containsExactly(new Name("Yoda"), null, null, null);
	Iterable<String> firstNames = fieldSupport.fieldValues("name.first", String.class, list);
	assertThat(firstNames).containsExactly("Yoda", null, null, null);
  }

  @Test
  public void should_return_values_of_simple_field() {
	Iterable<Long> ids = fieldSupport.fieldValues("id", Long.class, employees);
	assertThat(ids).isEqualTo(newArrayList(1L, 2L));
  }

  @Test
  public void should_return_values_of_nested_field() {
	Iterable<String> firstNames = fieldSupport.fieldValues("name.first", String.class, employees);
	assertThat(firstNames).isEqualTo(newArrayList("Yoda", "Luke"));
  }

  @Test
  public void should_throw_error_if_field_not_found() {
	thrown.expect(IntrospectionError.class,
	              "Unable to obtain the value of the field <'id.'> from <Employee[id=1, name=Name[first='Yoda', last='null'], age=800]>");
	fieldSupport.fieldValues("id.", Long.class, employees);
  }

  @Test
  public void should_return_values_of_private_field() {
	List<Integer> ages = fieldSupport.fieldValues("age", Integer.class, employees);
	assertThat(ages).isEqualTo(newArrayList(800, 26));
  }

  @Test
  public void should_throw_error_if_field_is_not_public_and_allowExtractingPrivateFields_set_to_false() {
	FieldSupport.EXTRACTION.setAllowUsingPrivateFields(false);
	try {
	  thrown.expect(IntrospectionError.class,
		            "Unable to obtain the value of the field <'age'> from <Employee[id=1, name=Name[first='Yoda', last='null'], age=800]>, check that field is public.");
	  fieldSupport.fieldValues("age", Integer.class, employees);
	} finally { // back to default value
	  FieldSupport.EXTRACTION.setAllowUsingPrivateFields(true);
	}
  }

  @Test
  public void should_extract_field() {
	Long id = fieldSupport.fieldValue("id", Long.class, yoda);
	assertThat(id).isEqualTo(1L);
	Object idObject = fieldSupport.fieldValue("id", Object.class, yoda);
	assertThat(idObject).isInstanceOf(Long.class).isEqualTo(1L);
  }

  @Test
  public void should_extract_nested_field() {
	String firstName = fieldSupport.fieldValue("name.first", String.class, yoda);
	assertThat(firstName).isEqualTo("Yoda");

	yoda.name.first = null;
	firstName = fieldSupport.fieldValue("name.first", String.class, yoda);
	assertThat(firstName).isNull();

	yoda.name = null;
	firstName = fieldSupport.fieldValue("name.first", String.class, yoda);
	assertThat(firstName).isNull();
  }

  @Test
  public void should_handle_array_as_iterable() {
	List<Long> fieldValuesFromIterable = fieldSupport.fieldValues("id", Long.class, employees);
	List<Long> fieldValuesFromArray = fieldSupport.fieldValues("id", Long.class, employees.toArray(new Employee[0]));
	assertThat(fieldValuesFromArray).isEqualTo(fieldValuesFromIterable);
  }
}
