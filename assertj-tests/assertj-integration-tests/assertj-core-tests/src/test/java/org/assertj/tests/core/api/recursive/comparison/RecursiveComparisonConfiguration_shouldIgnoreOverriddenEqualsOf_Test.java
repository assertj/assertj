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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.tests.core.api.recursive.data.Human;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonConfiguration_shouldIgnoreOverriddenEqualsOf_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  void should_ignore_all_overridden_equals_for_non_java_types() {
    // GIVEN
    DualValue dualValue = new DualValue(list("foo"), new Person(), new Person());
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreAllOverriddenEquals();
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(dualValue);
    // THEN
    then(ignored).as("All overridden equals should be ignored").isTrue();
  }

  @ParameterizedTest
  @MethodSource("ignoringAllOverriddenEqualsExceptBasicTypes")
  void should_ignore_all_overridden_equals_except_basic_types(Object value) {
    // GIVEN
    DualValue dualValue = new DualValue(list("foo"), value, value);
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreAllOverriddenEquals();
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(dualValue);
    // THEN
    then(ignored).as("overridden equals should not be ignored for %s", value.getClass()).isFalse();
  }

  private static Stream<Object> ignoringAllOverriddenEqualsExceptBasicTypes() {
    return Stream.of("foo", 23, 2.0, 123L, true, Byte.MIN_VALUE, new Object(), new Date(), Color.BLUE);
  }

  @ParameterizedTest(name = "{0} overridden equals should be ignored with these regexes {1}")
  @MethodSource("ignoringOverriddenEqualsByRegexesSource")
  void should_ignore_overridden_equals_for_fields_matching_regexes(DualValue dualValue, String[] fieldRegexes) {
    // GIVEN
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes(fieldRegexes);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(dualValue);
    // THEN
    then(ignored).as("%s overridden equals should be ignored for fields matching these regexes %s", dualValue, fieldRegexes)
                 .isTrue();
  }

  private static Stream<Arguments> ignoringOverriddenEqualsByRegexesSource() {
    return Stream.of(arguments(dualValueWithPath("phone"), array("phon.")),
                     arguments(dualValueWithPath("name"), array("foo", "na..", "foo")),
                     arguments(dualValueWithPath("name", "first"), array("name\\.fi.st")),
                     arguments(dualValueWithPath("father", "dob"), array("father.*")),
                     arguments(dualValueWithPath("father", "nickname", "first"), array("father\\.(.*)\\.first")),
                     arguments(dualValueWithPath("father", "name", "first"), array("father\\.(.*)\\.first")));
  }

  @ParameterizedTest(name = "{0} overridden equals should be ignored for these types {1}")
  @MethodSource("ignoringOverriddenEqualsForTypesSource")
  void should_ignore_overridden_equals_by_types(Class<?> clazz, List<Class<?>> types) {
    // GIVEN
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(types.toArray(new Class[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(clazz);
    // THEN
    then(ignored).as("%s overridden equals should be ignored for these types %s", clazz, types).isTrue();
  }

  private static Stream<Arguments> ignoringOverriddenEqualsForTypesSource() {
    return Stream.of(arguments(Person.class, list(Human.class, Person.class, String.class)),
                     arguments(Human.class, list(Human.class)));
  }

  @ParameterizedTest(name = "{0} overridden equals should be ignored for these fields {1}")
  @MethodSource("ignoringOverriddenEqualsForFieldsSource")
  void should_ignore_overridden_equals_by_fields(DualValue dualValue, String[] fields) {
    // GIVEN
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields(fields);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreOverriddenEqualsOf(dualValue);
    // THEN
    then(ignored).as("%s overridden equals should be ignored for these fields %s", dualValue, fields).isTrue();
  }

  private static Stream<Arguments> ignoringOverriddenEqualsForFieldsSource() {
    return Stream.of(arguments(dualValueWithPath("name"), array("name")),
                     arguments(dualValueWithPath("name"), array("foo", "name", "foo")),
                     arguments(dualValueWithPath("name", "first"), array("name.first")),
                     arguments(dualValueWithPath("father", "name", "first"), array("father", "name.first", "father.name.first")));
  }

  private static DualValue dualValueWithPath(String... pathElements) {
    return new DualValue(list(pathElements), new Person(), new Person());
  }

}
