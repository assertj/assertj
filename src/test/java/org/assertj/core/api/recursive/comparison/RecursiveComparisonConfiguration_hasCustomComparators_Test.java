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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecursiveComparisonConfiguration_hasCustomComparators_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  public void has_custom_comparator_due_to_default_type_comparators() {
    assertThat(recursiveComparisonConfiguration.hasCustomComparators()).isTrue();
  }

  @Test
  public void has_no_custom_comparator_when_clearing_type_comparators() {
    // WHEN
    recursiveComparisonConfiguration.getTypeComparators().clear();
    // THEN
    assertThat(recursiveComparisonConfiguration.hasCustomComparators()).isFalse();
  }

  @Test
  public void has_custom_comparator_when_registering_a_field_comparator() {
    // GIVEN
    recursiveComparisonConfiguration.getTypeComparators().clear();
    // WHEN
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAY_EQUALS, "foo");
    // THEN
    assertThat(recursiveComparisonConfiguration.hasCustomComparators()).isTrue();
  }

}
