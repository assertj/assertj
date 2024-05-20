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
import static org.assertj.tests.core.testkit.BiPredicates.ALWAYS_DIFFERENT;
import static org.assertj.tests.core.testkit.BiPredicates.ALWAYS_EQUALS;
import static org.assertj.tests.core.testkit.BiPredicates.STRING_EQUALS;
import static org.assertj.tests.core.testkit.NeverEqualComparator.NEVER_EQUALS;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.tests.core.api.recursive.data.Giant;
import org.assertj.tests.core.api.recursive.data.Home;
import org.assertj.tests.core.testkit.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_withComparatorsForFieldMatchingRegexes_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest(name = "{4}: actual={0} / expected={1} - fieldRegexes {3}")
  @MethodSource("recursivelyEqualObjectsWhenUsingFieldComparators")
  void should_pass_with_registered_BiPredicates_by_fields_matching_regexes(Object actual, Object expected,
                                                                           BiPredicate<Object, Object> equals,
                                                                           String[] fieldRegexes, String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .withEqualsForFieldsMatchingRegexes(equals, fieldRegexes)
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> recursivelyEqualObjectsWhenUsingFieldComparators() {
    Person person1 = new Person("John", "Doe");
    person1.home.address.number = 1;
    Person person2 = new Person("JoHN", "DoE");
    person2.home.address.number = 1;

    Person person3 = new Person("John", "Doe");
    person3.home.address.number = 1;
    Person person4 = new Person("Jack", "Doe");
    person4.home.address.number = 2;

    Person person5 = new Person("John", "Doe");
    person5.home.address.number = 1;
    person5.dateOfBirth = new Date(123);
    person5.neighbour = new Person("John", "Doe");
    person5.neighbour.home.address.number = 123;
    Person person6 = new Person("John", "Doe");
    person6.home.address.number = 1;
    person6.dateOfBirth = new Date(123);
    person6.neighbour = new Person("Jim", "Doe");
    person6.neighbour.home.address.number = 456;

    Person person7 = new Person("John", "Doe");
    person7.title = "Sir";
    Person person8 = new Person("Jack", "Dough");
    person8.title = "Mr";

    return Stream.of(arguments(person1, person2, STRING_EQUALS, array("name.*name"), "same data except case for strings"),
                     arguments(person3, person4, ALWAYS_EQUALS, array(".*first..me", "home.address.number"),
                               "same data except for address number and first name"),
                     // any neighbour differences should be ignored as we compare persons with ALWAYS_EQUALS
                     arguments(person5, person6, ALWAYS_EQUALS, array("neigh.*"), "same data except for neighbour"),
                     arguments(person7, person8, ALWAYS_EQUALS, array(".*stname", "t.tle"), "same data except for strings"));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_when_using_BiPredicates_by_fields_matching_regexes() {
    // GIVEN
    Person actual = new Person("John", "Doe");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jack", "Doe");
    actual.neighbour.home.address.number = 123;
    // actually a clone of actual
    Person expected = new Person("John", "Doe");
    expected.home.address.number = 1;
    expected.dateOfBirth = new Date(123);
    expected.neighbour = new Person("Jack", "Doe");
    expected.neighbour.home.address.number = 123;
    // register BiPredicates for some fields that will fail the comparison
    recursiveComparisonConfiguration.registerEqualsForFieldsMatchingRegexes(ALWAYS_DIFFERENT, "dateOf.*", "neighbour.ho.*");

    // WHEN/THEN
    ComparisonDifference dateOfBirthDiff = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference neighbourAddressDiff = diff("neighbour.home", actual.neighbour.home, expected.neighbour.home);
    compareRecursivelyFailsWithDifferences(actual, expected, dateOfBirthDiff, neighbourAddressDiff);
  }

  @Test
  void should_be_able_to_compare_objects_recursively_using_some_precision_for_numerical_fields() {
    // GIVEN
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;

    // THEN
    then(goliath).usingRecursiveComparison()
                 .withEqualsForFieldsMatchingRegexes((Double d1, Double d2) -> Math.abs(d1 - d2) <= 0.2, "hei...")
                 .isEqualTo(goliathTwin);
  }

  @Test
  void should_be_able_to_compare_objects_recursively_using_given_BiPredicate_for_specified_nested_field() {
    // GIVEN
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;
    goliath.home.address.number = 1;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;
    goliathTwin.home.address.number = 5;

    // THEN
    then(goliath).usingRecursiveComparison()
                 .withEqualsForFieldsMatchingRegexes((Double d1, Double d2) -> Math.abs(d1 - d2) <= 0.2, "height")
                 .withEqualsForFieldsMatchingRegexes((Integer d1, Integer d2) -> d1 - d2 <= 10, "home.address.number")
                 .isEqualTo(goliathTwin);
  }

  @Test
  void should_handle_null_field_with_BiPredicates_by_fields_matching_regexes() {
    // GIVEN
    Patient actual = new Patient(null);
    Patient expected = new Patient(new Timestamp(3L));
    // THEN
    then(actual).usingRecursiveComparison()
                .withEqualsForFieldsMatchingRegexes(ALWAYS_EQUALS, "dateOfBirth")
                .isEqualTo(expected);
  }

  @Test
  void field_BiPredicate_should_take_precedence_over_type_comparator_whatever_their_order_of_registration() {
    // GIVEN
    Patient actual = new Patient(new Timestamp(1L));
    Patient expected = new Patient(new Timestamp(3L));
    // THEN
    then(actual).usingRecursiveComparison()
                .withComparatorForType(NEVER_EQUALS, Timestamp.class)
                .withEqualsForFieldsMatchingRegexes(ALWAYS_EQUALS, "dateOfBirth")
                .isEqualTo(expected);
    then(actual).usingRecursiveComparison()
                .withEqualsForFieldsMatchingRegexes(ALWAYS_EQUALS, "dateOfBirth")
                .withComparatorForType(NEVER_EQUALS, Timestamp.class)
                .isEqualTo(expected);
  }

  @Test
  void exact_field_location_comparators_should_take_precedence_over_regexes_BiPredicates_matching_field_location_whatever_their_order_of_registration() {
    // GIVEN
    Patient actual = new Patient(new Timestamp(1L));
    Patient expected = new Patient(new Timestamp(3L));
    // THEN
    then(actual).usingRecursiveComparison()
                .withEqualsForFields(ALWAYS_EQUALS, "dateOfBirth")
                .withEqualsForFieldsMatchingRegexes(ALWAYS_DIFFERENT, "dateOfB.*")
                .isEqualTo(expected);
    then(actual).usingRecursiveComparison()
                .withEqualsForFieldsMatchingRegexes(ALWAYS_DIFFERENT, "dateOfBi.*")
                .withEqualsForFields(ALWAYS_EQUALS, "dateOfBirth")
                .isEqualTo(expected);
  }

  @Test
  void biPredicates_matching_field_location_take_precedence_over_overridden_equals() {
    // GIVEN
    Person actual = new Person("Fred", "Flint");
    actual.neighbour = new AlwaysEqualPerson();
    actual.neighbour.name = new Name("Omar", "Sy");
    Person expected = new Person("Fred", "Flint");
    expected.neighbour = new AlwaysEqualPerson();
    expected.neighbour.name = new Name("Omar2", "Sy");
    // THEN
    then(actual).usingRecursiveComparison()
                .withEqualsForFieldsMatchingRegexes(ALWAYS_EQUALS, "neighbour") // fails if commented
                .usingOverriddenEquals()
                .isEqualTo(expected);
  }

  @Test
  void should_use_custom_equal_over_reference_comparison() {
    // GIVEN
    Foo actual = new Foo(1);
    Foo expected = new Foo(1);
    BiPredicate<Integer, Integer> greaterThan = (i1, i2) -> Objects.equals(i1, i2 + 1);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .withEqualsForFieldsMatchingRegexes(greaterThan,
                                                                                                                     "b..")
                                                                                 .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContainingAll("- the fields matching these regexes were compared with the following comparators",
                                                 "  - [b..] -> ");
  }

  private static class Foo {

    @SuppressWarnings("unused")
    private final Integer bar;

    private Foo(Integer bar) {
      this.bar = bar;
    }
  }

  static class Person {
    Date dateOfBirth;
    Name name;
    String title;
    double weight;
    double height;
    Home home = new Home();
    Person neighbour;

    public Person(String firstname, String lastname) {
      this.name = new Name(firstname, lastname);
    }

    public Person() {}
  }

  static class Name {
    final String firstname;
    final String lastname;

    public Name(String firstname, String lastname) {
      this.firstname = firstname;
      this.lastname = lastname;
    }

    @Override
    public String toString() {
      return String.format("Name[firstname=%s, lastname=%s]", this.firstname, this.lastname);
    }
  }

  static class AlwaysEqualPerson extends Person {

    @Override
    public boolean equals(Object o) {
      return true;
    }
  }
}
