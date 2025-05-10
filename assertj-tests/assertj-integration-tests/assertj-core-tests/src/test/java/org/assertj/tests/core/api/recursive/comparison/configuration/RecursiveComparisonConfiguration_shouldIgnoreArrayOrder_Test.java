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
package org.assertj.tests.core.api.recursive.comparison.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RecursiveComparisonConfiguration_shouldIgnoreArrayOrder_Test {

  @ParameterizedTest(name = "{0} array order should be ignored")
  @ValueSource(booleans = { true, false })
  void should_ignore_array_order(boolean ignoreArrayOrder) {
    // GIVEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    recursiveComparisonConfiguration.ignoreArrayOrder(ignoreArrayOrder);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreArrayOrder();
    // THEN
    assertThat(ignored).as("array order should be ignored").isEqualTo(ignoreArrayOrder);
  }

}
