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
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.testkit.AlwaysDifferentComparator.ALWAY_DIFFERENT;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS;

import org.assertj.core.api.recursive.comparison.FieldComparators;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FieldComparators_Test {

  private FieldComparators fieldComparators;

  @BeforeEach
  void setup() {
    fieldComparators = new FieldComparators();
  }

  @Test
  void should_register_comparator_for_an_exact_field_location() {
    // GIVEN
    String fooLocation = "foo";
    // WHEN
    fieldComparators.registerComparator(fooLocation, ALWAYS_EQUALS);
    // THEN
    then(fieldComparators.hasComparatorForField(fooLocation)).isTrue();
    then(fieldComparators.getComparatorForField(fooLocation)).isSameAs(ALWAYS_EQUALS);
  }

  @Test
  void should_override_registered_comparator_for_an_exact_field_location() {
    // GIVEN
    String fooLocation = "foo";
    fieldComparators.registerComparator(fooLocation, ALWAYS_EQUALS);
    then(fieldComparators.hasComparatorForField(fooLocation)).isTrue();
    then(fieldComparators.getComparatorForField(fooLocation)).isSameAs(ALWAYS_EQUALS);
    // WHEN
    fieldComparators.registerComparator(fooLocation, ALWAY_DIFFERENT);
    // THEN
    then(fieldComparators.hasComparatorForField(fooLocation)).isTrue();
    then(fieldComparators.getComparatorForField(fooLocation)).isNotSameAs(ALWAYS_EQUALS);
    then(fieldComparators.getComparatorForField(fooLocation)).isSameAs(ALWAY_DIFFERENT);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "foo",
      "com.foo",
      "bar",
      "bar.baz",
      "com.bar.baz",
  })
  void should_register_comparator_for_regexes_field_location(String fieldLocation) {
    // WHEN
    fieldComparators.registerComparatorForFieldsMatchingRegexes(array(".*foo", ".*bar.*"), ALWAYS_EQUALS);
    // THEN
    then(fieldComparators.hasComparatorForField(fieldLocation)).isTrue();
    then(fieldComparators.getComparatorForField(fieldLocation)).isSameAs(ALWAYS_EQUALS);
  }

  @Test
  void should_use_latest_registered_comparator_when_several_regexes_match_field_location() {
    // GIVEN
    fieldComparators.registerComparatorForFieldsMatchingRegexes(array(".*foo", ".*bar.*"), ALWAYS_EQUALS);
    then(fieldComparators.hasComparatorForField("com.foo")).isTrue();
    then(fieldComparators.getComparatorForField("com.foo")).isSameAs(ALWAYS_EQUALS);
    // WHEN
    fieldComparators.registerComparatorForFieldsMatchingRegexes(array(".*foo.*", ".*baz.*"), ALWAY_DIFFERENT);
    // THEN
    then(fieldComparators.hasComparatorForField("com.foo")).isTrue();
    then(fieldComparators.getComparatorForField("com.foo")).isNotSameAs(ALWAYS_EQUALS);
    then(fieldComparators.getComparatorForField("com.foo")).isSameAs(ALWAY_DIFFERENT);
  }

  @Test
  void should_prefer_exact_field_location_comparator_over_regex_one() {
    // GIVEN
    fieldComparators.registerComparator("foo", ALWAYS_EQUALS);
    fieldComparators.registerComparatorForFieldsMatchingRegexes(array(".*foo.*", ".*baz.*"), ALWAY_DIFFERENT);
    // THEN
    then(fieldComparators.hasComparatorForField("foo")).isTrue();
    then(fieldComparators.getComparatorForField("foo")).isSameAs(ALWAYS_EQUALS);
    then(fieldComparators.getComparatorForField("com.foo")).isSameAs(ALWAY_DIFFERENT);
  }

  @Test
  void hasComparatorForField_should_return_false_for_field_location_without_comparator() {
    // GIVEN
    String fooLocation = "foo";
    // THEN
    then(fieldComparators.hasComparatorForField(fooLocation)).isFalse();
  }

  @Test
  void getComparatorForField_should_return_null_for_field_location_without_comparator() {
    // GIVEN
    String fooLocation = "foo";
    // THEN
    then(fieldComparators.getComparatorForField(fooLocation)).isNull();
  }

  @Test
  void isEmpty_should_return_true_if_no_field_or_field_regexes_comparators_are_registered() {
    assertThat(fieldComparators.isEmpty()).isTrue();
  }

  @Test
  void isEmpty_should_return_false_if_a_field_comparators_is_registered() {
    // GIVEN
    fieldComparators.registerComparator("foo", ALWAYS_EQUALS);
    // WHEN/THEN
    then(fieldComparators.isEmpty()).isFalse();
    then(fieldComparators.hasFieldComparators()).isTrue();
  }

  @Test
  void isEmpty_should_return_false_if_a_regex_field_comparators_is_registered() {
    // GIVEN
    fieldComparators.registerComparatorForFieldsMatchingRegexes(array(".*foo.*"), ALWAYS_EQUALS);
    // WHEN/THEN
    then(fieldComparators.isEmpty()).isFalse();
    then(fieldComparators.hasRegexFieldComparators()).isTrue();
  }

}
