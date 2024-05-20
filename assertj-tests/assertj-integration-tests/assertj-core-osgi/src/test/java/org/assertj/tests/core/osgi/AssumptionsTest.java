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
package org.assertj.tests.core.osgi;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.Assumptions.setPreferredAssumptionException;
import static org.assertj.core.configuration.PreferredAssumptionException.JUNIT5;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;

import org.assertj.core.api.Assumptions;
import org.assertj.core.configuration.PreferredAssumptionException;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;

class AssumptionsTest {

  @Test
  void should_ignore_test_when_one_of_the_assumption_fails() {
    setPreferredAssumptionException(JUNIT5);
    assumeThat("foo").isNotEmpty();
    assertThatThrownBy(() -> assumeThat("bar").isEmpty()).isInstanceOf(TestAbortedException.class);
  }

  @Test
  void should_run_test_when_all_assumptions_are_met() {
    assertThatNoException().isThrownBy(() -> {
      assumeThat("foo").isNotNull()
                       .isNotEmpty()
                       .isEqualTo("foo");
      assumeThat("bar").contains("ar")
                       .isNotBlank();
      assumeThat(asList("John", "Doe", "Jane", "Doe")).as("test description")
                                                      .withFailMessage("error message")
                                                      .withRepresentation(UNICODE_REPRESENTATION)
                                                      .usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                      .filteredOn(string -> string.length() == 4)
                                                      .containsExactly("JOHN", "JANE");
    });
  }

}
