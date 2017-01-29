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
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectAssert#extracting(String[])}</code>.
 */
public class ObjectAssert_extracting_Test {

  @Test
  public void should_allow_assertions_on_array_of_property_values_extracted_from_given_object() {
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
      
    assertThat(luke).extracting("id", "name").doesNotContainNull();
    assertThat(luke).extracting("name.first", "name.last").containsExactly("Luke", "Skywalker");
  }
}
