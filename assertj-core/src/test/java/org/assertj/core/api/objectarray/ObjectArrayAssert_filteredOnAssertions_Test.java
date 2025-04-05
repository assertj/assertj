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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.objectarray;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.function.Consumer;

import org.assertj.core.api.IterableAssert;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.TolkienCharacter;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_filteredOnAssertions_Test extends ObjectArrayAssert_filtered_baseTest {

  private static Consumer<? super TolkienCharacter> nameStartingWithFro = hobbit -> assertThat(hobbit.getName()).startsWith("Fro");

  @Test
  void should_filter_iterable_under_test_verifying_given_assertions() {
    assertThat(employees).filteredOnAssertions(employee -> assertThat(employee.getAge()).isGreaterThan(100))
                         .containsOnly(yoda, obiwan);
    assertThat(newLinkedHashSet(employees)).filteredOnAssertions(employee -> assertThat(employee.getAge()).isGreaterThan(100))
                                           .containsOnly(yoda, obiwan);
  }

  @Test
  void should_fail_if_given_consumer_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> {
      Consumer<? super Employee> consumer = null;
      assertThat(employees).filteredOnAssertions(consumer);
    }).withMessage("The element assertions should not be null");
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
                                                        .filteredOnAssertions(string -> assertThat(string.length()).isEqualTo(4))
                                                        .containsExactly("JOHN", "JANE");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
  }

}
