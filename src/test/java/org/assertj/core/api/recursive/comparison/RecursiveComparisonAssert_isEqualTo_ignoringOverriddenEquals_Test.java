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

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.FieldLocation.fielLocation;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.AlwaysDifferentAddress;
import org.assertj.core.internal.objects.data.AlwaysDifferentPerson;
import org.assertj.core.internal.objects.data.AlwaysEqualAddress;
import org.assertj.core.internal.objects.data.AlwaysEqualPerson;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonAssert_isEqualTo_ignoringOverriddenEquals_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest implements PersonData {

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("comparison_ignores_all_fields_overridden_equals_methods_data")
  public void should_pass_when_comparison_ignores_all_fields_overridden_equals_methods(Object actual,
                                                                                       Object expected,
                                                                                       String testDescription) {
    assertThat(actual).usingRecursiveComparison()
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
  public void should_pass_when_comparison_ignores_overridden_equals_methods_by_regexes(Object actual,
                                                                                       Object expected,
                                                                                       String testDescription,
                                                                                       List<String> regexes) {
    assertThat(actual).usingRecursiveComparison()
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
                                  list("org.assertj.core.internal.objects.data.*")),
                     arguments(person3, person4,
                                  "AlwaysDifferentPerson neighbour and AlwaysDifferentAddress identical field by field",
                                  list(".*AlwaysDifferent.*")),
                     arguments(person3, person4, "Several regexes",
                                  list(".*AlwaysDifferentPerson", ".*AlwaysDifferentAddress")));
  }

  @Test
  public void should_fail_when_actual_differs_from_expected_as_some_overridden_equals_methods_are_ignored_by_regexes() {
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

    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes(".*AlwaysEqualPerson",
                                                                                    ".*AlwaysEqualAddress");

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference neighbourNameDifference = diff("neighbour.name", actual.neighbour.name, expected.neighbour.name);
    ComparisonDifference numberDifference = diff("neighbour.home.address.number",
                                                 actual.neighbour.home.address.number,
                                                 expected.neighbour.home.address.number);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, numberDifference, neighbourNameDifference);
  }

  @Test
  public void ignoring_overriden_equals_with_regexes_does_not_replace_previous_regexes() {
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
  public void should_pass_when_comparison_ignores_overridden_equals_methods_by_types(Object actual,
                                                                                     Object expected,
                                                                                     String testDescription,
                                                                                     List<Class<?>> types) {
    assertThat(actual).usingRecursiveComparison()
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
  public void should_fail_when_actual_differs_from_expected_as_some_overridden_equals_methods_are_ignored_by_types() {
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

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference neighbourNameDifference = diff("neighbour.name", actual.neighbour.name, expected.neighbour.name);
    ComparisonDifference numberDifference = diff("neighbour.home.address.number",
                                                 actual.neighbour.home.address.number,
                                                 expected.neighbour.home.address.number);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, numberDifference, neighbourNameDifference);
  }

  @Test
  public void ignoring_overriden_equals_by_types_does_not_replace_previous_types() {
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
  public void should_pass_when_comparison_ignores_overridden_equals_methods_by_fields(Object actual,
                                                                                      Object expected,
                                                                                      String testDescription,
                                                                                      List<String> fields) {
    assertThat(actual).usingRecursiveComparison()
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
  public void should_fail_when_actual_differs_from_expected_as_some_overridden_equals_methods_are_ignored_by_fields() {
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

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference neighbourNameDifference = diff("neighbour.name", actual.neighbour.name, expected.neighbour.name);
    ComparisonDifference numberDifference = diff("neighbour.home.address.number",
                                                 actual.neighbour.home.address.number,
                                                 expected.neighbour.home.address.number);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, numberDifference, neighbourNameDifference);
  }

  @Test
  public void overridden_equals_is_not_used_on_the_object_under_test_itself() {
    // GIVEN
    AlwaysEqualPerson actual = new AlwaysEqualPerson();
    actual.name = "John";
    AlwaysEqualPerson expected = new AlwaysEqualPerson();
    expected.name = "Jack";
    // THEN
    // would have succeeded if we had used AlwaysEqualPerson equals method
    compareRecursivelyFailsAsExpected(actual, expected);
  }

  @Test
  public void ignoring_overriden_equals_for_fields_does_not_replace_previous_fields() {
    // WHEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("foo");
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("bar", "baz");
    // THEN
    List<FieldLocation> ignoredOverriddenEqualsFields = recursiveComparisonConfiguration.getIgnoredOverriddenEqualsForFields();
    assertThat(ignoredOverriddenEqualsFields).containsExactly(fielLocation("foo"), fielLocation("bar"), fielLocation("baz"));
  }

  @ParameterizedTest(name = "actual {0} / expected {1}")
  @MethodSource("container_values")
  void should_pass_as_Person_overridden_equals_is_ignored(Object actual, Object expected) {
    assertThat(actual).usingRecursiveComparison()
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
