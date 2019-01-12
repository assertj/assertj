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
package org.assertj.core.internal.objects;

import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.internal.objects.data.AlwaysDifferentAddress;
import org.assertj.core.internal.objects.data.AlwaysDifferentPerson;
import org.assertj.core.internal.objects.data.AlwaysEqualAddress;
import org.assertj.core.internal.objects.data.AlwaysEqualPerson;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Objects_assertIsEqualToUsingRecursiveComparison_ignoreOverriddenEquals_Test
    extends Objects_assertIsEqualToUsingRecursiveComparison_BaseTest {

  // ignoreOverriddenEqualsByRegexes tests

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored overridden equals regexes={3}")
  @MethodSource("comparison_ignores_overridden_equals_methods_by_regexes_data")
  public void should_pass_when_comparison_ignores_overridden_equals_methods_by_regexes(Object actual,
                                                                                       Object expected,
                                                                                       String testDescription,
                                                                                       List<String> regexes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsByRegexes(regexes.toArray(new String[0]));
    // THEN
    areEqualsByRecursiveComparison(actual, expected, recursiveComparisonConfiguration);
  }

  @SuppressWarnings("unused")
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

    return Stream.of(Arguments.of(person1, person2, "AlwaysDifferentPerson neighbour identical field by field",
                                  list("org.assertj.core.internal.objects.data.*")),
                     Arguments.of(person3, person4,
                                  "AlwaysDifferentPerson neighbour and AlwaysDifferentAddress identical field by field",
                                  list(".*AlwaysDifferent.*")),
                     Arguments.of(person3, person4, "Several regexes",
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

    recursiveComparisonConfiguration.ignoreOverriddenEqualsByRegexes(".*AlwaysEqualPerson", ".*AlwaysEqualAddress");

    // WHEN
    expectAssertionError(() -> areEqualsByRecursiveComparison(actual, expected, recursiveComparisonConfiguration));

    // THEN
    ComparisonDifference neighbourNameDifference = diff("neighbour.name", actual.neighbour.name, expected.neighbour.name);
    ComparisonDifference numberDifference = diff("neighbour.home.address.number",
                                                 actual.neighbour.home.address.number,
                                                 expected.neighbour.home.address.number);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, numberDifference, neighbourNameDifference);
  }

  // ignoreOverriddenEqualsForTypes tests

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored overridden equals types={3}")
  @MethodSource("comparison_ignores_overridden_equals_methods_by_types_data")
  public void should_pass_when_comparison_ignores_overridden_equals_methods_by_types(Object actual,
                                                                                     Object expected,
                                                                                     String testDescription,
                                                                                     List<Class<?>> types) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(types.toArray(new Class[0]));
    // THEN
    areEqualsByRecursiveComparison(actual, expected, recursiveComparisonConfiguration);
  }

  @SuppressWarnings("unused")
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

    return Stream.of(Arguments.of(person1, person2, "AlwaysDifferentPerson neighbour identical field by field",
                                  list(AlwaysDifferentPerson.class)),
                     Arguments.of(person3, person4,
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
    expectAssertionError(() -> areEqualsByRecursiveComparison(actual, expected, recursiveComparisonConfiguration));

    // THEN
    ComparisonDifference neighbourNameDifference = diff("neighbour.name", actual.neighbour.name, expected.neighbour.name);
    ComparisonDifference numberDifference = diff("neighbour.home.address.number",
                                                 actual.neighbour.home.address.number,
                                                 expected.neighbour.home.address.number);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, numberDifference, neighbourNameDifference);
  }

  // ignoreOverriddenEqualsForFields tests

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored overridden equals fields={3}")
  @MethodSource("comparison_ignores_overridden_equals_methods_by_fields_data")
  public void should_pass_when_comparison_ignores_overridden_equals_methods_by_fields(Object actual,
                                                                                      Object expected,
                                                                                      String testDescription,
                                                                                      List<String> fields) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields(fields.toArray(new String[0]));
    // THEN
    areEqualsByRecursiveComparison(actual, expected, recursiveComparisonConfiguration);
  }

  @SuppressWarnings("unused")
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

    return Stream.of(Arguments.of(person1, person2, "AlwaysDifferentPerson neighbour identical field by field",
                                  list("neighbour")),
                     Arguments.of(person3, person4,
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
    expectAssertionError(() -> areEqualsByRecursiveComparison(actual, expected, recursiveComparisonConfiguration));

    // THEN
    ComparisonDifference neighbourNameDifference = diff("neighbour.name", actual.neighbour.name, expected.neighbour.name);
    ComparisonDifference numberDifference = diff("neighbour.home.address.number",
                                                 actual.neighbour.home.address.number,
                                                 expected.neighbour.home.address.number);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, numberDifference, neighbourNameDifference);
  }

  @Test
  public void ignoring_overridden_equals_methods_only_applies_on_fields_but_not_on_the_object_under_test_itself() {
    // GIVEN
    AlwaysEqualPerson actual = new AlwaysEqualPerson();
    actual.name = "John";
    AlwaysEqualPerson expected = new AlwaysEqualPerson();
    expected.name = "Jack";
    // THEN
    // would have succeeded if we had used AlwaysEqualPerson equals method
    expectAssertionError(() -> areEqualsByRecursiveComparison(actual, expected, recursiveComparisonConfiguration));
  }

}
