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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;

import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.test.AlwaysEqualComparator;
import org.assertj.core.util.AbsValueComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecursiveComparisonConfiguration_comparatorByType_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  public void should_have_default_comparator_by_types() {
    // WHEN
    TypeComparators typeComparators = recursiveComparisonConfiguration.getComparatorForTypes();
    // THEN
    assertThat(typeComparators).isEqualTo(defaultTypeComparators());
  }

  @Test
  public void should_register_given_comparator_per_types() {
    // GIVEN
    AbsValueComparator<Integer> integerComparator = new AbsValueComparator<>();
    recursiveComparisonConfiguration.registerComparatorForType(Integer.class, integerComparator);
    recursiveComparisonConfiguration.registerComparatorForType(Tuple.class, AlwaysEqualComparator.ALWAY_EQUALS_TUPLE);
    // THEN
    assertThat(recursiveComparisonConfiguration.getComparatorForType(Integer.class)).isSameAs(integerComparator);
    assertThat(recursiveComparisonConfiguration.getComparatorForType(Tuple.class)).isSameAs(AlwaysEqualComparator.ALWAY_EQUALS_TUPLE);
  }

}
