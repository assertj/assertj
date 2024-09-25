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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.tests.core.api.recursive.comparison;

import org.assertj.core.api.recursive.comparison.RecursiveComparator;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class RecursiveComparator_constructor_Test {

  @Test
  void default_constructor_should_use_default_configuration() {
    // WHEN
    RecursiveComparator recursiveComparator = new RecursiveComparator();
    // THEN
    then(recursiveComparator).extracting("recursiveComparisonConfiguration")
                             .isEqualTo(new RecursiveComparisonConfiguration());
  }

  @Test
  void constructor_with_configuration_should_use_given_configuration() {
    // GIVEN
    RecursiveComparisonConfiguration configuration = new RecursiveComparisonConfiguration();
    // WHEN
    RecursiveComparator recursiveComparator = new RecursiveComparator(configuration);
    // THEN
    then(recursiveComparator).extracting("recursiveComparisonConfiguration")
                             .isSameAs(configuration);
  }

}
