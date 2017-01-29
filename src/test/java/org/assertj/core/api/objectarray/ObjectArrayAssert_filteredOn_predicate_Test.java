/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.objectarray;

/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Predicate;

import org.assertj.core.test.Employee;
import org.junit.Test;

public class ObjectArrayAssert_filteredOn_predicate_Test extends ObjectArrayAssert_filtered_baseTest {

  @Test
  public void should_filter_iterable_under_test_on_predicate() {
    assertThat(employees).filteredOn(employee -> employee.getAge() > 100).containsOnly(yoda, obiwan);
  }

  @Test
  public void should_fail_if_given_predicate_is_null() {
    thrown.expectIllegalArgumentException("The filter predicate should not be null");
    Predicate<? super Employee> predicate = null;
    assertThat(employees).filteredOn(predicate);
  }

}
