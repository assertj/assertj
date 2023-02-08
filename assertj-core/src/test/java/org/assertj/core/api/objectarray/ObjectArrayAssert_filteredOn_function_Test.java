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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.objectarray;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.Name.lastNameComparator;
import static org.assertj.core.test.Name.name;

import java.util.function.Function;

import org.assertj.core.api.IterableAssert;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ObjectArrayAssert filteredOn function")
class ObjectArrayAssert_filteredOn_function_Test extends ObjectArrayAssert_filtered_baseTest {

  @Test
  void should_filter_object_array_under_test_on_function_result_equals_to_given_value() {
    assertThat(employees).filteredOn(Employee::getAge, 800)
                         .containsOnly(yoda, obiwan);
  }

  @Test
  void should_filter_object_array_under_test_on_function_result_equals_to_null() {
    assertThat(employees).filteredOn(Employee::getName, null)
                         .containsOnly(noname);
  }

  @Test
  void should_fail_if_given_function_is_null() {
    // GIVEN
    Function<? super Employee, String> function = null;
    // WHEN/THEN
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(employees).filteredOn(function, "Yoda"))
                                        .withMessage("The filter function should not be null");
  }

  @Test
  void should_pass_keep_assertion_state() {
    // GIVEN
    Iterable<Name> names = asList(name("Manu", "Ginobili"), name("Magic", "Johnson"));
    // WHEN
    IterableAssert<Name> assertion = assertThat(names).as("test description")
                                                      .withFailMessage("error message")
                                                      .withRepresentation(UNICODE_REPRESENTATION)
                                                      .usingElementComparator(lastNameComparator)
                                                      .filteredOn(Name::getFirst, "Manu")
                                                      .containsExactly(name("Whoever", "Ginobili"));
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
  }

}
