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

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_TUPLE;
import static org.assertj.tests.core.testkit.BiPredicates.DOUBLE_EQUALS;
import static org.assertj.tests.core.testkit.BiPredicates.STRING_EQUALS;

import java.util.Comparator;
import java.util.function.BiPredicate;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.tests.core.testkit.AbsValueComparator;
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
    assumeThat(recursiveComparisonConfiguration.getComparatorForField("temperature")).isNull();
    assumeThat(recursiveComparisonConfiguration.getComparatorForField("name")).isNull();
    // WHEN
    recursiveComparisonConfiguration.registerComparatorForFields(integerComparator, "height");
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAYS_EQUALS_TUPLE, "weight");
    recursiveComparisonConfiguration.registerEqualsForFields(DOUBLE_EQUALS, "temperature");
    recursiveComparisonConfiguration.registerEqualsForFields(STRING_EQUALS, "name");
    // THEN
    then(recursiveComparisonConfiguration.getComparatorForField("height")).isSameAs(integerComparator);
    then(recursiveComparisonConfiguration.getComparatorForField("weight")).isSameAs(ALWAYS_EQUALS_TUPLE);
    then(recursiveComparisonConfiguration.getComparatorForField("temperature")).isNotNull();
    then(recursiveComparisonConfiguration.getComparatorForField("name")).isNotNull();
  }

  @Test
  void should_replace_a_registered_field_comparator() {
    // GIVEN
    recursiveComparisonConfiguration.registerComparatorForFields(new AbsValueComparator<>(), "height");
    recursiveComparisonConfiguration.registerEqualsForFields(STRING_EQUALS, "name");
    Comparator<?> firstComparator = recursiveComparisonConfiguration.getComparatorForField("name");
    // WHEN
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAYS_EQUALS_TUPLE, "height");
    recursiveComparisonConfiguration.registerEqualsForFields(STRING_EQUALS, "name");
    // THEN
    then(recursiveComparisonConfiguration.getComparatorForField("name")).isNotSameAs(firstComparator);
    then(recursiveComparisonConfiguration.getComparatorForField("height")).isSameAs(ALWAYS_EQUALS_TUPLE);
  }

  @Test
  void bipredicate_should_replace_a_registered_field_comparator() {
    // GIVEN
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAYS_EQUALS_TUPLE, "weight");
    Comparator<?> firstComparator = recursiveComparisonConfiguration.getComparatorForField("weight");
    // WHEN
    recursiveComparisonConfiguration.registerEqualsForFields((Double d1, Double d2) -> Math.abs(d1 - d2) <= 0.01, "weight");
    // THEN
    then(recursiveComparisonConfiguration.getComparatorForField("weight")).isNotSameAs(firstComparator);
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

  @Test
  void should_register_bipredicate_for_fields_matching_regexes() {
    // GIVEN
    assumeThat(recursiveComparisonConfiguration.getComparatorForField("height")).isNull();
    assumeThat(recursiveComparisonConfiguration.getComparatorForField("weight")).isNull();
    assumeThat(recursiveComparisonConfiguration.getComparatorForField("temperature")).isNull();
    assumeThat(recursiveComparisonConfiguration.getComparatorForField("firstname")).isNull();
    assumeThat(recursiveComparisonConfiguration.getComparatorForField("lastname")).isNull();
    // WHEN
    recursiveComparisonConfiguration.registerEqualsForFieldsMatchingRegexes(DOUBLE_EQUALS, ".eight", "temp.*");
    recursiveComparisonConfiguration.registerEqualsForFieldsMatchingRegexes(STRING_EQUALS, ".*name");
    // THEN
    then(recursiveComparisonConfiguration.getComparatorForField("height")).isNotNull();
    then(recursiveComparisonConfiguration.getComparatorForField("weight")).isNotNull();
    then(recursiveComparisonConfiguration.getComparatorForField("temperature")).isNotNull();
    then(recursiveComparisonConfiguration.getComparatorForField("firstname")).isNotNull();
    then(recursiveComparisonConfiguration.getComparatorForField("lastname")).isNotNull();
  }

  @Test
  void latest_field_regex_matching_comparator_should_take_precedence_over_previous_ones() {
    // GIVEN
    recursiveComparisonConfiguration.registerEqualsForFieldsMatchingRegexes(DOUBLE_EQUALS, ".eight");
    Comparator<?> firstWeightComparator = recursiveComparisonConfiguration.getComparatorForField("weight");
    // WHEN
    recursiveComparisonConfiguration.registerEqualsForFieldsMatchingRegexes(STRING_EQUALS, "weigh.");
    // THEN
    Comparator<?> lastComparator = recursiveComparisonConfiguration.getComparatorForField("weight");
    then(recursiveComparisonConfiguration.getComparatorForField("weight")).isNotSameAs(firstWeightComparator);
  }

  @Test
  void exact_field_comparator_should_take_precedence_over_field_regex_matching_comparator() {
    // GIVEN
    recursiveComparisonConfiguration.registerEqualsForFields(DOUBLE_EQUALS, "weight");
    Comparator<?> weightComparator = recursiveComparisonConfiguration.getComparatorForField("weight");
    // WHEN
    recursiveComparisonConfiguration.registerEqualsForFieldsMatchingRegexes(STRING_EQUALS, "weigh.");
    // THEN
    then(recursiveComparisonConfiguration.getComparatorForField("weight")).isSameAs(weightComparator);
  }

}
