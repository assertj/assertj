/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.recursive.comparison.ComparingFields;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_defaultIntrospectionStrategy_Test {

  @Test
  void should_be_comparing_fields_introspection_strategy() {
    // GIVEN
    var recursiveComparisonAssert = assertThat(new Person("John")).usingRecursiveComparison();
    // WHEN
    var recursiveComparisonConfiguration = recursiveComparisonAssert.getRecursiveComparisonConfiguration();
    // THEN
    then(recursiveComparisonConfiguration.getIntrospectionStrategy()).isInstanceOf(ComparingFields.class);
  }

}
