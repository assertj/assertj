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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;

import org.assertj.core.api.Condition;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacterAssert;
import org.assertj.core.data.TolkienCharacterAssertFactory;
import org.assertj.core.presentation.Representation;
import org.assertj.core.test.Employee;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IterableAssert_filteredOn_condition_Test extends IterableAssert_filtered_baseTest {

  private Condition<Employee> oldEmployees;
  private Condition<TolkienCharacter> nameStartingWithFro;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    oldEmployees = new Condition<>(employee -> employee.getAge() > 100, "old employees");
    nameStartingWithFro = new Condition<>(hobbit -> hobbit.getName().startsWith("Fro"), "name starts with Fro");
  }

  @Test
  public void should_filter_iterable_under_test_on_condition() {
    assertThat(employees).filteredOn(oldEmployees).containsOnly(yoda, obiwan);
  }

  @Test
  public void should_filter_iterable_under_test_on_combined_condition() {
    assertThat(employees).filteredOn(not(oldEmployees)).contains(luke, noname);
  }

  @Test
  public void should_fail_if_given_condition_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> {
      oldEmployees = null;
      assertThat(employees).filteredOn(oldEmployees);
    }).withMessage("The filter condition should not be null");
  }

  @Test
  public void should_honor_AssertFactory_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    TolkienCharacterAssertFactory tolkienCharacterAssertFactory = new TolkienCharacterAssertFactory();
    // THEN
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn(nameStartingWithFro)
                                                      .first()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn(nameStartingWithFro)
                                                      .last()
                                                      .hasAge(33);
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn(nameStartingWithFro)
                                                      .element(0)
                                                      .hasAge(33);
  }

  @Test
  public void should_honor_ClassBased_strongly_typed_navigation_assertions() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    // THEN
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn(nameStartingWithFro)
                                                     .first()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn(nameStartingWithFro)
                                                     .last()
                                                     .hasAge(33);
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn(nameStartingWithFro)
                                                     .element(0)
                                                     .hasAge(33);
  }

  @Test
  public void should_keep_existing_assertion_contextual_information() {
    // GIVEN
    Iterable<TolkienCharacter> hobbits = hobbits();
    String description = "should find Frodo";
    String errorMessage = "should have found Frodo";
    Representation representation = UNICODE_REPRESENTATION;
    // WHEN
    IterableAssert<TolkienCharacter> filteredAssertion = assertThat(hobbits).as(description)
                                                                            .overridingErrorMessage(errorMessage)
                                                                            .withRepresentation(representation)
                                                                            .filteredOn(nameStartingWithFro);
    // THEN
    assertThat(filteredAssertion.info.descriptionText()).isEqualTo(description);
    assertThat(filteredAssertion.info.overridingErrorMessage()).isEqualTo(errorMessage);
    assertThat(filteredAssertion.info.representation()).isEqualTo(representation);
  }

  @Test
  public void should_keep_assertion_state() {
    // GIVEN
    Iterable<String> names = asList("John", "Doe", "Jane", "Doe");
    // WHEN
    IterableAssert<String> assertion = assertThat(names).as("test description")
                                                        .withFailMessage("error message")
                                                        .withRepresentation(UNICODE_REPRESENTATION)
                                                        .usingElementComparator(CaseInsensitiveStringComparator.instance)
                                                        .filteredOn(new Condition<>(string -> string.length() == 4, "4 letters"))
                                                        .containsExactly("JOHN", "JANE");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
  }
}
