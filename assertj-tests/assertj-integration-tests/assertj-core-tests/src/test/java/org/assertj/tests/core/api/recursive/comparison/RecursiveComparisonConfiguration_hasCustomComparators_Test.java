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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS;

import java.util.function.BiPredicate;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursiveComparisonConfiguration_hasCustomComparators_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  void has_custom_comparator_due_to_default_type_comparators() {
    assertThat(recursiveComparisonConfiguration.hasCustomComparators()).isTrue();
  }

  @Test
  void has_no_custom_comparator_when_clearing_type_comparators() {
    // WHEN
    recursiveComparisonConfiguration.getTypeComparators().clear();
    // THEN
    then(recursiveComparisonConfiguration.hasCustomComparators()).isFalse();
  }

  @Test
  void has_custom_comparator_when_registering_a_field_comparator() {
    // GIVEN
    recursiveComparisonConfiguration.getTypeComparators().clear();
    // WHEN
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAYS_EQUALS, "foo");
    // THEN
    then(recursiveComparisonConfiguration.hasCustomComparators()).isTrue();
  }

  @Test
  void has_custom_comparator_when_registering_a_field_bipredicate_equals_comparator() {
    // GIVEN
    recursiveComparisonConfiguration.getTypeComparators().clear();
    BiPredicate<String, String> stringEquals = String::equalsIgnoreCase;
    // WHEN
    recursiveComparisonConfiguration.registerEqualsForFields(stringEquals, "foo");
    // THEN
    then(recursiveComparisonConfiguration.hasCustomComparators()).isTrue();
  }

}
