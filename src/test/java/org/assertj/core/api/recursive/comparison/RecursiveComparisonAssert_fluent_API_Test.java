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
import static org.assertj.core.test.AlwaysDifferentComparator.alwaysDifferent;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;

import org.assertj.core.internal.TypeComparators;
import org.assertj.core.test.AlwaysDifferentComparator;
import org.assertj.core.test.AlwaysEqualComparator;
import org.junit.jupiter.api.Test;

public class RecursiveComparisonAssert_fluent_API_Test {

  private final static Object ACTUAL = "";

  @Test
  public void usingRecursiveComparison_should_set_a_default_RecursiveComparisonConfiguration() {
    // WHEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                                          .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(recursiveComparisonConfiguration.isInStrictTypeCheckingMode()).isFalse();
    assertThat(recursiveComparisonConfiguration.getTypeComparators()).isEqualTo(TypeComparators.defaultTypeComparators());
    assertThat(recursiveComparisonConfiguration.getFieldComparators().isEmpty()).isTrue();
    assertThat(recursiveComparisonConfiguration.getIgnoreAllActualNullFields()).isFalse();
    assertThat(recursiveComparisonConfiguration.getIgnoredFields()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoredFieldsRegexes()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoredOverriddenEqualsForFields()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoredOverriddenEqualsForTypes()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoredOverriddenEqualsRegexes()).isEmpty();
    // TODO assertThat(recursiveComparisonConfiguration.hasNoCustomComparators()).isTrue();
  }

  @Test
  public void should_allow_to_use_its_own_RecursiveComparisonConfiguration() {
    // GIVEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration).isSameAs(recursiveComparisonConfiguration);
  }

  @Test
  public void should_allow_to_override_field_comparator() {
    // GIVEN
    String field = "foo.bar";
    AlwaysEqualComparator<Object> alwaysEqualComparator = alwaysEqual();
    AlwaysDifferentComparator<Object> alwaysDifferentComparator = alwaysDifferent();
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForField(field, alwaysEqualComparator)
                                                                       .withComparatorForField(field, alwaysDifferentComparator)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getComparatorForField(field)).isSameAs(alwaysDifferentComparator);
  }

  @Test
  public void should_allow_to_override_type_comparator() {
    // GIVEN
    Class<?> type = String.class;
    AlwaysEqualComparator<Object> alwaysEqualComparator = alwaysEqual();
    AlwaysDifferentComparator<Object> alwaysDifferentComparator = alwaysDifferent();
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForType(type, alwaysEqualComparator)
                                                                       .withComparatorForType(type, alwaysDifferentComparator)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getComparatorForType(type)).isSameAs(alwaysDifferentComparator);
  }

}
