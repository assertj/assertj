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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.extractor;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.Rule;
import org.junit.Test;

public class ByNameSingleExtractorTest {
  private static final Employee yoda = new Employee(1L, new Name("Yoda"), 800);

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_extract_field_values_even_if_property_exist() {
	Object extractedValues = idExtractor().extract(yoda);

	assertThat(extractedValues).isEqualTo(1L);
  }

  @Test
  public void should_extract_property_values_when_no_public_field_match_given_name() {
	Object extractedValues = ageExtractor().extract(yoda);

	assertThat(extractedValues).isEqualTo(800);
  }

  @Test
  public void should_extract_pure_property_values() {
	Object extractedValues = adultExtractor().extract(yoda);

	assertThat(extractedValues).isEqualTo(true);
  }

  @Test
  public void should_throw_error_when_no_property_nor_public_field_match_given_name() {
	thrown.expect(IntrospectionError.class);

	new ByNameSingleExtractor<Employee>("unknown").extract(yoda);
  }

  @Test
  public void should_throw_exception_when_given_name_is_null() {
	thrown.expectIllegalArgumentException("The name of the field/property to read should not be null");

	new ByNameSingleExtractor<Employee>(null).extract(yoda);
  }

  @Test
  public void should_throw_exception_when_given_name_is_empty() {
	thrown.expectIllegalArgumentException("The name of the field/property to read should not be empty");

	new ByNameSingleExtractor<Employee>("").extract(yoda);
  }

  @Test
  public void should_fallback_to_field_if_exception_has_been_thrown_on_property_access() throws Exception {
	Object extractedValue = nameExtractor().extract(employeeWithBrokenName("Name"));

	assertThat(extractedValue).isEqualTo(new Name("Name"));
  }

  @Test
  public void should_prefer_properties_over_fields() throws Exception {
	Object extractedValue = nameExtractor().extract(employeeWithOverridenName("Overriden Name"));

	assertThat(extractedValue).isEqualTo(new Name("Overriden Name"));
  }

  @Test
  public void should_throw_exception_if_property_cannot_be_extracted_due_to_runtime_exception_during_property_access()
	  throws Exception {
	thrown.expect(IntrospectionError.class);

	Employee employee = brokenEmployee();
	adultExtractor().extract(employee);
  }

  @Test
  public void should_throw_exception_if_no_object_is_given() throws Exception {
	thrown.expect(IllegalArgumentException.class);

	idExtractor().extract(null);
  }

  @Test
  public void should_extract_single_value_from_maps_by_key() {
	String key1 = "key1";
	String key2 = "key2";
	Map<String, Employee> map1 = new HashMap<>();
	map1.put(key1, yoda);
	Employee luke = new Employee(2L, new Name("Luke"), 22);
	map1.put(key2, luke);

	Map<String, Employee> map2 = new HashMap<>();
	map2.put(key1, yoda);
	Employee han = new Employee(3L, new Name("Han"), 31);
	map2.put(key2, han);

	List<Map<String, Employee>> maps = asList(map1, map2);
	assertThat(maps).extracting(key2).containsExactly(luke, han);
	assertThat(maps).extracting(key2, Employee.class).containsExactly(luke, han);
	assertThat(maps).extracting(key1).containsExactly(yoda, yoda);
	assertThat(maps).extracting("bad key").containsExactly(null, null);
  }

  private Employee employeeWithBrokenName(String name) {
	return new Employee(1L, new Name(name), 0) {
	  @Override
	  public Name getName() {
		throw new IllegalStateException();
	  }
	};
  }

  private Employee employeeWithOverridenName(final String overridenName) {
	return new Employee(1L, new Name("Name"), 0) {
	  @Override
	  public Name getName() {
		return new Name(overridenName);
	  }
	};
  }

  private Employee brokenEmployee() {
	return new Employee() {
	  @Override
	  public boolean isAdult() {
		throw new IllegalStateException();
	  }
	};
  }

  private ByNameSingleExtractor<Employee> idExtractor() {
	return new ByNameSingleExtractor<>("id");
  }

  private ByNameSingleExtractor<Employee> ageExtractor() {
	return new ByNameSingleExtractor<>("age");
  }

  private ByNameSingleExtractor<Employee> adultExtractor() {
	return new ByNameSingleExtractor<>("adult");
  }

  private ByNameSingleExtractor<Employee> nameExtractor() {
	return new ByNameSingleExtractor<>("name");
  }

}
