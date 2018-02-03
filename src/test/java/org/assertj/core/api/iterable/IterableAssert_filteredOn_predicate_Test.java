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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.function.Predicate;

import org.assertj.core.test.Employee;
import org.junit.Test;

public class IterableAssert_filteredOn_predicate_Test extends IterableAssert_filtered_baseTest {

  @Test
  public void should_filter_iterable_under_test_on_predicate() {
    assertThat(employees).filteredOn(employee -> employee.getAge() > 100).containsOnly(yoda, obiwan);
    assertThat(newHashSet(employees)).filteredOn(employee -> employee.getAge() > 100).containsOnly(yoda, obiwan);
  }

  @Test
  public void should_fail_if_given_predicate_is_null() {
    thrown.expectIllegalArgumentException("The filter predicate should not be null");
    Predicate<? super Employee> predicate = null;
    assertThat(employees).filteredOn(predicate);
  }

}
