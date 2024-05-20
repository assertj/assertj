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
import static org.assertj.tests.core.testkit.AlwaysDifferentComparator.alwaysDifferent;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_TIMESTAMP;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.alwaysEqual;
import static org.assertj.tests.core.testkit.NeverEqualComparator.NEVER_EQUALS;
import static org.assertj.tests.core.testkit.SymmetricDateComparator.SYMMETRIC_DATE_COMPARATOR;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.tests.core.api.recursive.data.AlwaysEqualPerson;
import org.assertj.tests.core.api.recursive.data.Giant;
import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.testkit.AtPrecisionComparator;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.assertj.tests.core.testkit.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_withFieldComparators_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest(name = "{4}: actual={0} / expected={1} - comparators {2} - fields {3}")
  @MethodSource("recursivelyEqualObjectsWhenUsingFieldComparators")
  void should_pass_for_objects_with_the_same_data_when_using_registered_comparators_by_fields(Object actual,
                                                                                              Object expected,
                                                                                              Comparator<Object> comparator,
                                                                                              String[] fields,
                                                                                              String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForFields(comparator, fields)
                      .isEqualTo(expected);
  }

  @ParameterizedTest(name = "{4}: actual={0} / expected={1} - comparators {2} - fields {3}")
  @MethodSource("recursivelyEqualObjectsWhenUsingFieldComparators")
  void should_pass_for_objects_with_the_same_data_when_using_registered_BiPredicate_by_fields(Object actual,
                                                                                              Object expected,
                                                                                              Comparator<Object> comparator,
                                                                                              String[] fields,
                                                                                              String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .withEqualsForFields(asBiPredicate(comparator), fields)
                      .isEqualTo(expected);
  }

  @SuppressWarnings("unchecked")
  private static BiPredicate<Object, Object> asBiPredicate(@SuppressWarnings("rawtypes") Comparator comparator) {
    return (Object o1, Object o2) -> comparator.compare(o1, o2) == 0;
  }

  private static Stream<Arguments> recursivelyEqualObjectsWhenUsingFieldComparators() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;
    Person person2 = new Person("JoHN");
    person2.home.address.number = 1;

    Person person3 = new Person("John");
    person3.home.address.number = 1;
    Person person4 = new Person("Jack");
    person4.home.address.number = 2;

    Person person5 = new Person("John");
    person5.home.address.number = 1;
    person5.dateOfBirth = new Date(123);
    person5.neighbour = new Person("Jack");
    person5.neighbour.home.address.number = 123;
    Person person6 = new Person("John");
    person6.home.address.number = 1;
    person6.dateOfBirth = new Date(123);
    person6.neighbour = new Person("Jim");
    person6.neighbour.home.address.number = 456;

    return Stream.of(arguments(person1, person2, CaseInsensitiveStringComparator.INSTANCE, array("name"),
                               "same data except int fields and case for strings"),
                     arguments(person3, person4, alwaysEqual(), array("name", "home.address.number"),
                               "same data except for int fields"),
                     // any neighbour differences should be ignored as we compare persons with AlwaysEqualComparator
                     arguments(person5, person6, alwaysEqual(), array("neighbour"),
                               "same data except for persons, person's fields should not be compared recursively except at the root level"));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_when_using_field_comparators() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jack");
    actual.neighbour.home.address.number = 123;
    // actually a clone of actual
    Person expected = new Person("John");
    expected.home.address.number = 1;
    expected.dateOfBirth = new Date(123);
    expected.neighbour = new Person("Jack");
    expected.neighbour.home.address.number = 123;
    // register comparators for some fields that will fail the comparison
    recursiveComparisonConfiguration.registerComparatorForFields(alwaysDifferent(), "dateOfBirth", "neighbour.home.address");

    // WHEN/THEN
    ComparisonDifference dateOfBirthDifference = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference neighbourAddressDifference = diff("neighbour.home.address",
                                                           actual.neighbour.home.address,
                                                           expected.neighbour.home.address);
    compareRecursivelyFailsWithDifferences(actual, expected, dateOfBirthDifference, neighbourAddressDifference);
  }

  @Test
  void should_fail_when_actual_differs_from_expected_when_using_field_bipredicate_comparators() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jack");
    actual.neighbour.home.address.number = 123;
    // actually a clone of actual
    Person expected = new Person("John");
    expected.home.address.number = 1;
    expected.dateOfBirth = new Date(123);
    expected.neighbour = new Person("Jack");
    expected.neighbour.home.address.number = 123;
    // register some equals methods for some fields that will fail the comparison
    recursiveComparisonConfiguration.registerEqualsForFields((Object o1, Object o2) -> false,
                                                             "dateOfBirth", "neighbour.home.address");
    // WHEN/THEN
    ComparisonDifference dateOfBirthDifference = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference neighbourAddressDifference = diff("neighbour.home.address",
                                                           actual.neighbour.home.address,
                                                           expected.neighbour.home.address);
    compareRecursivelyFailsWithDifferences(actual, expected, dateOfBirthDifference, neighbourAddressDifference);
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
                 .withComparatorForFields(new AtPrecisionComparator<>(0.2), "height")
                 .isEqualTo(goliathTwin);
    then(goliath).usingRecursiveComparison()
                 .withEqualsForFields((Double d1, Double d2) -> Math.abs(d1 - d2) <= 0.2, "height")
                 .isEqualTo(goliathTwin);
  }

  @Test
  void should_be_able_to_compare_objects_recursively_using_given_comparator_for_specified_nested_field() {
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
                 .withComparatorForFields(new AtPrecisionComparator<>(0.2), "height")
                 .withComparatorForFields(new AtPrecisionComparator<>(10), "home.address.number")
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
                 .withEqualsForFields((Double d1, Double d2) -> Math.abs(d1 - d2) <= 0.2, "height")
                 .withEqualsForFields((Integer i1, Integer i2) -> Math.abs(i1 - i2) <= 10, "home.address.number")
                 .isEqualTo(goliathTwin);
  }

  @Test
  void should_handle_null_field_with_field_comparator() {
    // GIVEN
    Patient actual = new Patient(null);
    Patient expected = new Patient(new Timestamp(3L));
    // THEN
    then(actual).usingRecursiveComparison()
                .withComparatorForFields(ALWAYS_EQUALS_TIMESTAMP, "dateOfBirth")
                .isEqualTo(expected);
    then(actual).usingRecursiveComparison()
                .withEqualsForFields((o1, o2) -> true, "dateOfBirth")
                .isEqualTo(expected);
  }

  @Test
  void should_treat_timestamp_as_equal_to_date_when_registering_a_date_symmetric_comparator() {
    // GIVEN
    Person actual = new Person("Fred");
    actual.dateOfBirth = new Timestamp(1000L);
    Person other = new Person(actual.name);
    other.dateOfBirth = new Date(1000L);
    // THEN
    then(actual).usingRecursiveComparison()
                .withComparatorForFields(SYMMETRIC_DATE_COMPARATOR, "dateOfBirth")
                .isEqualTo(other);
    then(other).usingRecursiveComparison()
               .withEqualsForFields(asBiPredicate(SYMMETRIC_DATE_COMPARATOR), "dateOfBirth")
               .isEqualTo(actual);
  }

  @Test
  void field_comparator_should_take_precedence_over_type_comparator_whatever_their_order_of_registration() {
    // GIVEN
    Patient actual = new Patient(new Timestamp(1L));
    Patient expected = new Patient(new Timestamp(3L));
    // THEN
    then(actual).usingRecursiveComparison()
                .withComparatorForType(NEVER_EQUALS, Timestamp.class)
                .withComparatorForFields(ALWAYS_EQUALS_TIMESTAMP, "dateOfBirth")
                .isEqualTo(expected);
    then(actual).usingRecursiveComparison()
                .withComparatorForFields(ALWAYS_EQUALS_TIMESTAMP, "dateOfBirth")
                .withComparatorForType(NEVER_EQUALS, Timestamp.class)
                .isEqualTo(expected);
    then(actual).usingRecursiveComparison()
                .withEqualsForType((o1, o2) -> false, Timestamp.class)
                .withEqualsForFields((o1, o2) -> true, "dateOfBirth")
                .isEqualTo(expected);
    then(actual).usingRecursiveComparison()
                .withEqualsForFields((o1, o2) -> true, "dateOfBirth")
                .withEqualsForType((o1, o2) -> false, Timestamp.class)
                .isEqualTo(expected);
  }

  @Test
  void ignoringOverriddenEquals_should_not_interfere_with_field_comparators() {
    // GIVEN
    Person actual = new Person("Fred");
    actual.neighbour = new AlwaysEqualPerson();
    actual.neighbour.name = "Omar";
    Person expected = new Person("Fred");
    expected.neighbour = new AlwaysEqualPerson();
    expected.neighbour.name = "Omar2";
    // THEN
    then(actual).usingRecursiveComparison()
                .withComparatorForFields(ALWAYS_EQUALS, "neighbour") // fails if commented
                .ignoringOverriddenEqualsForFields("neighbour")
                .isEqualTo(expected);
    then(actual).usingRecursiveComparison()
                .withEqualsForFields((o1, o2) -> true, "neighbour") // fails if commented
                .ignoringOverriddenEqualsForFields("neighbour")
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
                                                                                 .withEqualsForFields(greaterThan, "bar")
                                                                                 .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContainingAll("- these fields were compared with the following comparators:", "  - bar -> ");
  }

  @Test
  void should_use_custom_comparator_over_reference_comparison() {
    // GIVEN
    Foo actual = new Foo(1);
    Foo expected = new Foo(1);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .withComparatorForFields(NEVER_EQUALS, "bar")
                                                                                 .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContainingAll("- these fields were compared with the following comparators:", "  - bar -> ");
  }

  private record Foo(@SuppressWarnings("unused") Integer bar) {

    private Foo(Integer bar) {
      this.bar = bar;
    }
  }
}
