package org.assertj.core.api.iterable;

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
 * Copyright 2012-2015 the original author or authors.
 */

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.api.Assertions.setAllowExtractingPrivateFields;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.Set;

import org.assertj.core.test.Employee;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.Test;

public class IterableAssert_filteredOn_Test extends IterableAssert_filtered_baseTest {

  @Test
  public void should_filter_iterable_under_test_on_property_values() {
    assertThat(employees).filteredOn("age", 800).containsOnly(yoda, obiwan);
  }

  @Test
  public void should_filter_set_under_test_on_property_values() {
    Set<Employee> employeeSet = newHashSet(employees);
    assertThat(employeeSet).filteredOn("age", 800).containsOnly(yoda, obiwan);
  }

  @Test
  public void should_filter_iterable_under_test_on_property_not_backed_by_a_field_values() {
    assertThat(employees).filteredOn("adult", false).containsOnly(noname);
    assertThat(employees).filteredOn("adult", true).containsOnly(yoda, obiwan, luke);
  }

  @Test
  public void should_filter_iterable_under_test_on_public_field_values() {
    assertThat(employees).filteredOn("id", 1L).containsOnly(yoda);
  }

  @Test
  public void should_filter_iterable_under_test_on_private_field_values() {
    assertThat(employees).filteredOn("city", "New York").containsOnly(yoda, obiwan, luke, noname);
    assertThat(employees).filteredOn("city", "Paris").isEmpty();
  }

  @Test
  public void should_fail_if_filter_is_on_private_field_and_reading_private_field_is_disabled() {
    setAllowExtractingPrivateFields(false);
    try {
      assertThat(employees).filteredOn("city", "New York").isEmpty();
      failBecauseExceptionWasNotThrown(IntrospectionError.class);
    } catch (IntrospectionError e) {
      // expected
    } finally {
      setAllowExtractingPrivateFields(true);
    }
  }

  @Test
  public void should_filter_iterator_under_test_on_property_values() {
    assertThat(employees.iterator()).filteredOn("age", 800).containsOnly(yoda, obiwan);
  }

  @Test
  public void should_filter_iterable_under_test_on_nested_property_values() {
    assertThat(employees).filteredOn("name.first", "Luke").containsOnly(luke);
  }

  @Test
  public void should_filter_iterable_under_test_on_nested_mixed_property_and_field_values() {
    assertThat(employees).filteredOn("name.last", "Vader").isEmpty();
    assertThat(employees).filteredOn("name.last", "Skywalker").containsOnly(luke);
  }

  @Test
  public void should_fail_if_given_property_or_field_name_is_null() {
    thrown.expectIllegalArgumentException("The property/field name to filter on should not be null or empty");
    assertThat(employees).filteredOn(null, 800);
  }

  @Test
  public void should_fail_if_given_property_or_field_name_is_empty() {
    thrown.expectIllegalArgumentException("The property/field name to filter on should not be null or empty");
    assertThat(employees).filteredOn("", 800);
  }

  @Test
  public void should_fail_if_given_expected_value_is_null() {
    try {
      assertThat(employees).filteredOn("name", null);
      failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage(format("The expected value should not be null.%n"
                                      + "If you were trying to filter on a null value, please use filterOnNull(String propertyOrFieldName) instead"));
    }
  }

  @Test
  public void should_fail_if_on_of_the_iterable_element_does_not_have_given_property_or_field() {
    try {
      assertThat(employees).filteredOn("secret", "???");
      failBecauseExceptionWasNotThrown(IntrospectionError.class);
    } catch (IntrospectionError e) {
      assertThat(e).hasMessageContaining("Can't find any field or property with name 'secret'");
    }
  }

  @Test
  public void should_fail_if_filter_operators_are_combined() {
    try {
      assertThat(employees).filteredOn("age", not(in(800))).containsOnly(luke, noname);
      failBecauseExceptionWasNotThrown(UnsupportedOperationException.class);
    } catch (UnsupportedOperationException e) {
      assertThat(e).hasMessageStartingWith("Combining operator is not supported");
    }
  }
}
