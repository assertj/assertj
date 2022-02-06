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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_TUPLE;

import java.util.Comparator;
import java.util.function.BiPredicate;

import org.assertj.core.util.AbsValueComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursiveComparisonConfiguration_fieldComparators_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  void should_register_given_field_comparators() {
    // GIVEN
    AbsValueComparator<Integer> integerComparator = new AbsValueComparator<>();
    BiPredicate<Double, Double> doubleEquals = (Double d1, Double d2) -> Math.abs(d1 - d2) <= 0.01;
    BiPredicate<String, String> stringEquals = (String s1, String s2) -> s1.equalsIgnoreCase(s2);
    assertThat(recursiveComparisonConfiguration.getComparatorForField("temperature")).isNull();
    assertThat(recursiveComparisonConfiguration.getComparatorForField("name")).isNull();
    // WHEN
    recursiveComparisonConfiguration.registerComparatorForFields(integerComparator, "height");
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAYS_EQUALS_TUPLE, "weight");
    recursiveComparisonConfiguration.registerEqualsForFields(doubleEquals, "temperature");
    recursiveComparisonConfiguration.registerEqualsForFields(stringEquals, "name");
    // THEN
    assertThat(recursiveComparisonConfiguration.getComparatorForField("height")).isSameAs(integerComparator);
    assertThat(recursiveComparisonConfiguration.getComparatorForField("weight")).isSameAs(ALWAYS_EQUALS_TUPLE);
    assertThat(recursiveComparisonConfiguration.getComparatorForField("temperature")).isNotNull();
    assertThat(recursiveComparisonConfiguration.getComparatorForField("name")).isNotNull();
  }

  @Test
  void should_replace_a_registered_field_comparator() {
    // GIVEN
    recursiveComparisonConfiguration.registerComparatorForFields(new AbsValueComparator<>(), "height");
    BiPredicate<String, String> stringEquals = (String s1, String s2) -> s1.equalsIgnoreCase(s2);
    recursiveComparisonConfiguration.registerEqualsForFields(stringEquals, "name");
    Comparator<?> firstComparator = recursiveComparisonConfiguration.getComparatorForField("name");
    // WHEN
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAYS_EQUALS_TUPLE, "height");
    recursiveComparisonConfiguration.registerEqualsForFields(stringEquals, "name");
    // THEN
    assertThat(recursiveComparisonConfiguration.getComparatorForField("name")).isNotSameAs(firstComparator);
    assertThat(recursiveComparisonConfiguration.getComparatorForField("height")).isSameAs(ALWAYS_EQUALS_TUPLE);
  }

  @Test
  void bipredicate_should_replace_a_registered_field_comparator() {
    // GIVEN
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAYS_EQUALS_TUPLE, "weight");
    Comparator<?> firstComparator = recursiveComparisonConfiguration.getComparatorForField("weight");
    // WHEN
    recursiveComparisonConfiguration.registerEqualsForFields((Double d1, Double d2) -> Math.abs(d1 - d2) <= 0.01, "weight");
    // THEN
    assertThat(recursiveComparisonConfiguration.getComparatorForField("weight")).isNotSameAs(firstComparator);
  }

  @Test
  void should_throw_NPE_if_given_comparator_is_null() {
    // GIVEN
    Comparator<Integer> integerComparator = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> recursiveComparisonConfiguration.registerComparatorForFields(integerComparator,
                                                                                                            "age"));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessage("Expecting a non null Comparator");
  }

  @Test
  void should_throw_NPE_if_given_BiPredicate_is_null() {
    // GIVEN
    BiPredicate<Double, Double> doubleEquals = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> recursiveComparisonConfiguration.registerEqualsForFields(doubleEquals, "height"));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessage("Expecting a non null BiPredicate");
  }

}
