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
package org.assertj.core.api.iterable;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.api.Assertions.setAllowExtractingPrivateFields;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.Name.lastNameComparator;
import static org.assertj.core.test.Name.name;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.Set;

import org.assertj.core.api.IterableAssert;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacterAssert;
import org.assertj.core.data.TolkienCharacterAssertFactory;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
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
    assertThat(employeeSet).filteredOn("age", 800)
                           .containsOnly(yoda, obiwan);
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
      assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> {
        assertThat(employees).filteredOn("city", "New York").isEmpty();
      });
    } finally {
      setAllowExtractingPrivateFields(true);
    }
  }

  @Test
  public void should_filter_iterator_under_test_on_property_values() {
    assertThat(employees.iterator()).filteredOn("age", 800).containsOnly(yoda, obiwan);
  }

  @Test
  public void should_filter_stream_under_test_on_property_values() {
    assertThat(employees.stream()).filteredOn("age", 800)
                                  .containsOnly(yoda, obiwan);
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
    thrown.expectWithMessageContaining(IllegalArgumentException.class,
                                       "The expected value should not be null.%n"
                                                                       + "If you were trying to filter on a null value, please use filteredOnNull(String propertyOrFieldName) instead");
    assertThat(employees).filteredOn("name", null);
  }

  @Test
  public void should_fail_if_on_of_the_iterable_element_does_not_have_given_property_or_field() {
    thrown.expectIntrospectionErrorWithMessageContaining("Can't find any field or property with name 'secret'");
    assertThat(employees).filteredOn("secret", "???");
  }

  @Test
  public void should_fail_if_filter_operators_are_combined() {
    thrown.expectWithMessageStartingWith(UnsupportedOperationException.class, "Combining operator is not supported");
    assertThat(employees).filteredOn("age", not(in(800))).containsOnly(luke, noname);
  }

  @Test
  public void shoul_honor_AssertFactory_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    TolkienCharacterAssertFactory tolkienCharacterAssertFactory = new TolkienCharacterAssertFactory();
    // THEN
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn("name", "Frodo")
                                                      .first()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn("name", "Frodo")
                                                      .last()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn("name", "Frodo")
                                                      .element(0)
                                                      .hasAge(33);
  }

  @Test
  public void shoul_honor_ClassBased_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    // THEN
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn("name", "Frodo")
                                                     .first()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn("name", "Frodo")
                                                     .last()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn("name", "Frodo")
                                                     .element(0)
                                                     .hasAge(33);
  }

  @Test
  public void should_keep_assertion_state() {
    // GIVEN
    Iterable<Name> names = asList(name("Manu", "Ginobili"), name("Magic", "Johnson"));
    // WHEN
    IterableAssert<Name> assertion = assertThat(names).as("test description")
                                                      .withFailMessage("error message")
                                                      .withRepresentation(UNICODE_REPRESENTATION)
                                                      .usingElementComparator(lastNameComparator)
                                                      .filteredOn("first", "Manu")
                                                      .containsExactly(name("Whoever", "Ginobili"));
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
  }

}
