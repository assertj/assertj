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
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.function.Predicate;

import org.assertj.core.testkit.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AtomicReferenceArrayAssert filteredOn predicate")
class AtomicReferenceArrayAssert_filteredOn_predicate_Test extends AtomicReferenceArrayAssert_filtered_baseTest {

  @Test
  void should_filter_iterable_under_test_on_predicate() {
    assertThat(employees).filteredOn(employee -> employee.getAge() > 100)
                         .containsOnly(yoda, obiwan);
  }

  @Test
  void should_fail_if_given_predicate_is_null() {
    Predicate<? super Employee> predicate = null;
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(employees).filteredOn(predicate))
                                        .withMessage("The filter predicate should not be null");
  }

}
