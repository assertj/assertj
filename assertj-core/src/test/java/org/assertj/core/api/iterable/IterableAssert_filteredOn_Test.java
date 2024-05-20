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
package org.assertj.core.api.iterable;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.api.Assertions.setAllowExtractingPrivateFields;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.testkit.Name.lastNameComparator;
import static org.assertj.core.testkit.Name.name;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.Set;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.assertj.core.testkit.TolkienCharacter;
import org.assertj.core.testkit.TolkienCharacterAssert;
import org.assertj.core.testkit.TolkienCharacterAssertFactory;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;

class IterableAssert_filteredOn_Test extends IterableAssert_filtered_baseTest {

  @Test
  void should_filter_iterable_under_test_on_property_values() {
    assertThat(employees).filteredOn("age", 800).containsOnly(yoda, obiwan);
  }

  @Test
  void should_filter_set_under_test_on_property_values() {
    Set<Employee> employeeSet = newHashSet(employees);
    assertThat(employeeSet).filteredOn("age", 800)
                           .containsOnly(yoda, obiwan);
  }

  @Test
  void should_filter_iterable_under_test_on_property_not_backed_by_a_field_values() {
    assertThat(employees).filteredOn("adult", false).containsOnly(noname);
    assertThat(employees).filteredOn("adult", true).containsOnly(yoda, obiwan, luke);
  }

  @Test
  void should_filter_iterable_under_test_on_public_field_values() {
    assertThat(employees).filteredOn("id", 1L).containsOnly(yoda);
  }

  @Test
  void should_filter_iterable_under_test_on_private_field_values() {
    assertThat(employees).filteredOn("city", "New York").containsOnly(yoda, obiwan, luke, noname);
    assertThat(employees).filteredOn("city", "Paris").isEmpty();
  }

  @Test
  void should_fail_if_filter_is_on_private_field_and_reading_private_field_is_disabled() {
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
  void should_filter_stream_under_test_on_property_values() {
    assertThat(employees.stream()).filteredOn("age", 800)
                                  .containsOnly(yoda, obiwan);
  }

  @Test
  void should_filter_iterable_under_test_on_nested_property_values() {
    assertThat(employees).filteredOn("name.first", "Luke").containsOnly(luke);
  }

  @Test
  void should_filter_iterable_under_test_on_nested_mixed_property_and_field_values() {
    assertThat(employees).filteredOn("name.last", "Vader").isEmpty();
    assertThat(employees).filteredOn("name.last", "Skywalker").containsOnly(luke);
  }

  @Test
  void should_fail_if_given_property_or_field_name_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(employees).filteredOn((String) null, 800))
                                        .withMessage("The property/field name to filter on should not be null or empty");
  }

  @Test
  void should_fail_if_given_property_or_field_name_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(employees).filteredOn("", 800))
                                        .withMessage("The property/field name to filter on should not be null or empty");
  }

  @Test
  void should_fail_if_given_expected_value_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(employees).filteredOn("name", null))
                                        .withMessage(format("The expected value should not be null.%n" +
                                                            "If you were trying to filter on a null value, please use filteredOnNull(String propertyOrFieldName) instead"));
  }

  @Test
  void should_fail_if_on_of_the_iterable_element_does_not_have_given_property_or_field() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> assertThat(employees).filteredOn("secret", "???"))
                                                       .withMessageContaining("Can't find any field or property with name 'secret'");
  }

  @Test
  void should_fail_if_filter_operators_are_combined() {
    ThrowingCallable code = () -> assertThat(employees).filteredOn("age", not(in(800))).containsOnly(luke, noname);
    assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(code)
                                                                  .withMessageStartingWith("Combining operator is not supported");
  }

  @Test
  void should_honor_AssertFactory_strongly_typed_navigation_assertions() {
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
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn("name", "Frodo")
                                                      .elements(0)
                                                      .first()
                                                      .hasAge(33);
  }

  @Test
  void should_honor_ClassBased_strongly_typed_navigation_assertions() {
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
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn("name", "Frodo")
                                                     .elements(0)
                                                     .first()
                                                     .hasAge(33);
  }

  @Test
  void should_keep_assertion_state() {
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
