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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.function.Predicate;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.TolkienCharacter;
import org.assertj.core.testkit.TolkienCharacterAssert;
import org.assertj.core.testkit.TolkienCharacterAssertFactory;
import org.junit.jupiter.api.Test;

class IterableAssert_filteredOn_predicate_Test extends IterableAssert_filtered_baseTest {

  private static Predicate<? super TolkienCharacter> nameStartingWithFro = hobbit -> hobbit.getName().startsWith("Fro");

  @Test
  void should_filter_iterable_under_test_on_predicate() {
    assertThat(employees).filteredOn(employee -> employee.getAge() > 100).containsOnly(yoda, obiwan);
    assertThat(newHashSet(employees)).filteredOn(employee -> employee.getAge() > 100).containsOnly(yoda, obiwan);
  }

  @Test
  void should_fail_if_given_predicate_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> {
      Predicate<? super Employee> predicate = null;
      assertThat(employees).filteredOn(predicate);
    }).withMessage("The filter predicate should not be null");
  }

  @Test
  void should_honor_AssertFactory_strongly_typed_navigation_assertions() {
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
    assertThat(hobbits, tolkienCharacterAssertFactory).filteredOn(nameStartingWithFro)
                                                      .elements(0)
                                                      .first()
                                                      .hasAge(33);
  }

  @Test
  void should_honor_ClassBased_strongly_typed_navigation_assertions() {
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
    assertThat(hobbits, TolkienCharacterAssert.class).filteredOn(nameStartingWithFro)
                                                     .elements(0)
                                                     .first()
                                                     .hasAge(33);
  }

  @Test
  void should_keep_assertion_state() {
    // GIVEN
    Iterable<String> names = asList("John", "Doe", "Jane", "Doe");
    // WHEN
    IterableAssert<String> assertion = assertThat(names).as("test description")
                                                        .withFailMessage("error message")
                                                        .withRepresentation(UNICODE_REPRESENTATION)
                                                        .usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                        .filteredOn(string -> string.length() == 4)
                                                        .containsExactly("JOHN", "JANE");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
  }

}
