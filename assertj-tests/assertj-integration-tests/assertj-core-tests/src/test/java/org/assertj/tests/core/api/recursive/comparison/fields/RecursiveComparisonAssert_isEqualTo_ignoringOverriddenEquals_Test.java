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
package org.assertj.tests.core.api.recursive.comparison.fields;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.tests.core.api.recursive.data.AlwaysDifferentAddress;
import org.assertj.tests.core.api.recursive.data.AlwaysDifferentPerson;
import org.assertj.tests.core.api.recursive.data.AlwaysEqualAddress;
import org.assertj.tests.core.api.recursive.data.AlwaysEqualPerson;
import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.PersonDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_ignoringOverriddenEquals_Test
    extends WithComparingFieldsIntrospectionStrategyBaseTest {

  private static final Person sheldon = new Person("Sheldon");
  private static final PersonDto sheldonDto = new PersonDto("Sheldon");

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("comparison_ignores_all_fields_overridden_equals_methods_data")
  void should_pass_when_comparison_ignores_all_fields_overridden_equals_methods(Object actual,
                                                                                Object expected,
                                                                                String testDescription) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringAllOverriddenEquals()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> comparison_ignores_all_fields_overridden_equals_methods_data() {
    Person person1 = new Person();
    person1.neighbour = new AlwaysDifferentPerson();
    Person person2 = new Person();
    person2.neighbour = new AlwaysDifferentPerson();

    Person person3 = new Person();
    person3.home.address = new AlwaysDifferentAddress();
    person3.neighbour = new AlwaysDifferentPerson();
    Person person4 = new Person();
    person4.home.address = new AlwaysDifferentAddress();
    person4.neighbour = new AlwaysDifferentPerson();

    return Stream.of(arguments(person1, person2, "AlwaysDifferentPerson neighbour identical field by field"),
                     arguments(person3, person4,
                               "AlwaysDifferentPerson neighbour and AlwaysDifferentAddress identical field by field"));
  }

  // ignoringOverriddenEqualsForFieldsMatchingRegexes tests

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored overridden equals regexes={3}")
  @MethodSource("comparison_ignores_overridden_equals_methods_by_regexes_data")
  void should_pass_when_comparison_ignores_overridden_equals_methods_by_regexes(Object actual,
                                                                                Object expected,
                                                                                String testDescription,
                                                                                List<String> regexes) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringOverriddenEqualsForFieldsMatchingRegexes(regexes.toArray(new String[0]))
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> comparison_ignores_overridden_equals_methods_by_regexes_data() {
    Person person1 = new Person();
    person1.neighbour = new AlwaysDifferentPerson();
    Person person2 = new Person();
    person2.neighbour = new AlwaysDifferentPerson();

    Person person3 = new Person();
    person3.home.address = new AlwaysDifferentAddress();
    person3.neighbour = new AlwaysDifferentPerson();
    Person person4 = new Person();
    person4.home.address = new AlwaysDifferentAddress();
    person4.neighbour = new AlwaysDifferentPerson();

    return Stream.of(arguments(person1, person2, "AlwaysDifferentPerson neighbour identical field by field",
                               list("org.assertj.tests.core.api.recursive.data.*")),
                     arguments(person3, person4,
                               "AlwaysDifferentPerson neighbour and AlwaysDifferentAddress identical field by field",
                               list(".*AlwaysDifferent.*")),
                     arguments(person3, person4, "Several regexes",
                               list(".*AlwaysDifferentPerson", ".*AlwaysDifferentAddress")));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_as_some_overridden_equals_methods_are_ignored_by_regexes() {
    // GIVEN
    Person actual = new Person();
    actual.neighbour = new AlwaysEqualPerson();
    actual.neighbour.name = "Jack";
    actual.neighbour.home.address = new AlwaysEqualAddress();
    actual.neighbour.home.address.number = 123;

    Person expected = new Person();
    expected.neighbour = new AlwaysEqualPerson();
    expected.neighbour.name = "Jim";
    expected.neighbour.home.address = new AlwaysEqualAddress();
    expected.neighbour.home.address.number = 234;

    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes("neighb..r", ".*address");

    // WHEN/THEN
    ComparisonDifference neighbourNameDifference = javaTypeDiff("neighbour.name",
                                                                actual.neighbour.name,
                                                                expected.neighbour.name);
    ComparisonDifference numberDifference = javaTypeDiff("neighbour.home.address.number",
                                                         actual.neighbour.home.address.number,
                                                         expected.neighbour.home.address.number);
    compareRecursivelyFailsWithDifferences(actual, expected, numberDifference, neighbourNameDifference);
  }

  @Test
  void ignoring_overriden_equals_with_regexes_does_not_replace_previous_regexes() {
    // WHEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes("foo");
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes("bar", "baz");
    // THEN
    List<Pattern> ignoredOverriddenEqualsRegexes = recursiveComparisonConfiguration.getIgnoredOverriddenEqualsForFieldsMatchingRegexes();
    assertThat(ignoredOverriddenEqualsRegexes).extracting(Pattern::pattern)
                                              .containsExactlyInAnyOrder("foo", "bar", "baz");
  }

  // ignoreOverriddenEqualsForTypes tests

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored overridden equals types={3}")
  @MethodSource("comparison_ignores_overridden_equals_methods_by_types_data")
  void should_pass_when_comparison_ignores_overridden_equals_methods_by_types(Object actual,
                                                                              Object expected,
                                                                              String testDescription,
                                                                              List<Class<?>> types) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringOverriddenEqualsForTypes(types.toArray(new Class[0]))
                      .isEqualTo(expected);

  }

  private static Stream<Arguments> comparison_ignores_overridden_equals_methods_by_types_data() {
    Person person1 = new Person();
    person1.neighbour = new AlwaysDifferentPerson();
    Person person2 = new Person();
    person2.neighbour = new AlwaysDifferentPerson();

    Person person3 = new Person();
    person3.home.address = new AlwaysDifferentAddress();
    person3.neighbour = new AlwaysDifferentPerson();
    Person person4 = new Person();
    person4.home.address = new AlwaysDifferentAddress();
    person4.neighbour = new AlwaysDifferentPerson();

    return Stream.of(arguments(person1, person2, "AlwaysDifferentPerson neighbour identical field by field",
                               list(AlwaysDifferentPerson.class)),
                     arguments(person3, person4,
                               "AlwaysDifferentPerson neighbour and AlwaysDifferentAddress identical field by field",
                               list(AlwaysDifferentPerson.class, AlwaysDifferentAddress.class)));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_as_some_overridden_equals_methods_are_ignored_by_types() {
    // GIVEN
    Person actual = new Person();
    actual.neighbour = new AlwaysEqualPerson();
    actual.neighbour.name = "Jack";
    actual.neighbour.home.address = new AlwaysEqualAddress();
    actual.neighbour.home.address.number = 123;

    Person expected = new Person();
    expected.neighbour = new AlwaysEqualPerson();
    expected.neighbour.name = "Jim";
    expected.neighbour.home.address = new AlwaysEqualAddress();
    expected.neighbour.home.address.number = 234;

    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(AlwaysEqualPerson.class, AlwaysEqualAddress.class);

    // WHEN/THEN
    ComparisonDifference neighbourNameDifference = javaTypeDiff("neighbour.name",
                                                                actual.neighbour.name,
                                                                expected.neighbour.name);
    ComparisonDifference numberDifference = javaTypeDiff("neighbour.home.address.number",
                                                         actual.neighbour.home.address.number,
                                                         expected.neighbour.home.address.number);
    compareRecursivelyFailsWithDifferences(actual, expected, numberDifference, neighbourNameDifference);
  }

  @Test
  void ignoring_overriden_equals_by_types_does_not_replace_previous_types() {
    // WHEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(String.class);
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(Date.class);
    // THEN
    assertThat(recursiveComparisonConfiguration.getIgnoredOverriddenEqualsForTypes()).containsExactly(String.class, Date.class);
  }

  // ignoreOverriddenEqualsForFields tests

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored overridden equals fields={3}")
  @MethodSource("comparison_ignores_overridden_equals_methods_by_fields_data")
  void should_pass_when_comparison_ignores_overridden_equals_methods_by_fields(Object actual,
                                                                               Object expected,
                                                                               String testDescription,
                                                                               List<String> fields) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringOverriddenEqualsForFields(fields.toArray(new String[0]))
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> comparison_ignores_overridden_equals_methods_by_fields_data() {
    Person person1 = new Person();
    person1.neighbour = new AlwaysDifferentPerson();
    Person person2 = new Person();
    person2.neighbour = new AlwaysDifferentPerson();

    Person person3 = new Person();
    person3.home.address = new AlwaysDifferentAddress();
    person3.neighbour = new AlwaysDifferentPerson();
    Person person4 = new Person();
    person4.home.address = new AlwaysDifferentAddress();
    person4.neighbour = new AlwaysDifferentPerson();

    return Stream.of(arguments(person1, person2, "AlwaysDifferentPerson neighbour identical field by field",
                               list("neighbour")),
                     arguments(person3, person4,
                               "AlwaysDifferentPerson neighbour and AlwaysDifferentAddress identical field by field",
                               list("neighbour", "home.address")));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_as_some_overridden_equals_methods_are_ignored_by_fields() {
    // GIVEN
    Person actual = new Person();
    actual.neighbour = new AlwaysEqualPerson();
    actual.neighbour.name = "Jack";
    actual.neighbour.home.address = new AlwaysEqualAddress();
    actual.neighbour.home.address.number = 123;

    Person expected = new Person();
    expected.neighbour = new AlwaysEqualPerson();
    expected.neighbour.name = "Jim";
    expected.neighbour.home.address = new AlwaysEqualAddress();
    expected.neighbour.home.address.number = 234;

    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("neighbour", "neighbour.home.address");

    // WHEN/THEN
    ComparisonDifference neighbourNameDifference = javaTypeDiff("neighbour.name",
                                                                actual.neighbour.name,
                                                                expected.neighbour.name);
    ComparisonDifference numberDifference = javaTypeDiff("neighbour.home.address.number",
                                                         actual.neighbour.home.address.number,
                                                         expected.neighbour.home.address.number);
    compareRecursivelyFailsWithDifferences(actual, expected, numberDifference, neighbourNameDifference);
  }

  @Test
  void overridden_equals_is_not_used_on_the_object_under_test_itself() {
    // GIVEN
    AlwaysEqualPerson actual = new AlwaysEqualPerson();
    actual.name = "John";
    AlwaysEqualPerson expected = new AlwaysEqualPerson();
    expected.name = "Jack";
    // THEN
    // would have succeeded if we had used AlwaysEqualPerson equals method
    expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected));
  }

  @Test
  void ignoring_overriden_equals_for_fields_does_not_replace_previous_fields() {
    // WHEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("foo");
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("bar", "baz");
    // THEN
    List<String> ignoredOverriddenEqualsFields = recursiveComparisonConfiguration.getIgnoredOverriddenEqualsForFields();
    then(ignoredOverriddenEqualsFields).containsExactly("foo", "bar", "baz");
  }

  @ParameterizedTest(name = "actual {0} / expected {1}")
  @MethodSource("container_values")
  void should_pass_as_Person_overridden_equals_is_ignored(Object actual, Object expected) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringAllOverriddenEquals()
                      .isEqualTo(expected);
  }

  static Stream<Arguments> container_values() {
    // sheldon type is Person which overrides equals!
    return Stream.of(Arguments.of(newHashSet(sheldon), newHashSet(sheldonDto)),
                     Arguments.of(array(sheldon), array(sheldonDto)),
                     Arguments.of(Optional.of(sheldon), Optional.of(sheldonDto)),
                     Arguments.of(newHashMap("sheldon", sheldon), newHashMap("sheldon", sheldonDto)));
  }

}
