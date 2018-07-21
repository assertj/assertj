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
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.not;

import org.assertj.core.api.Condition;
import org.assertj.core.test.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AtomicReferenceArrayAssert_filteredOn_condition_Test extends AtomicReferenceArrayAssert_filtered_baseTest {

  protected Condition<Employee> oldEmployees;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    oldEmployees = new Condition<Employee>("old employees") {
      @Override
      public boolean matches(Employee employee) {
        return employee.getAge() > 100;
      }
    };
  }

  @Test
  public void should_filter_object_array_under_test_on_condition() {
    assertThat(employees).filteredOn(oldEmployees).containsOnly(yoda, obiwan);
  }

  @Test
  public void should_filter_object_array_under_test_on_combined_condition() {
    assertThat(employees).filteredOn(not(oldEmployees)).contains(luke, noname);
  }

  @Test
  public void should_fail_if_given_condition_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(employees).filteredOn(null))
                                        .withMessage("The filter condition should not be null");
  }

}
