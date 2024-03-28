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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.internal.objects.SymmetricDateComparator.SYMMETRIC_DATE_COMPARATOR;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_TIMESTAMP;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.NeverEqualComparator.NEVER_EQUALS;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.AtPrecisionComparator;
import org.assertj.core.internal.objects.data.*;
import org.assertj.core.test.AlwaysDifferentComparator;
import org.assertj.core.test.AlwaysEqualComparator;
import org.assertj.core.test.CaseInsensitiveStringComparator;
import org.assertj.core.test.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_withTypeComparators_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest(name = "{3}: actual={0} / expected={1} - comparatorsByType: {2}")
  @MethodSource("recursivelyEqualObjectsWhenUsingTypeComparators")
  void should_pass_for_objects_with_the_same_data_when_using_registered_comparator_by_types(Object actual,
                                                                                            Object expected,
                                                                                            Map<Class<?>, Comparator<Object>> comparatorByTypes,
                                                                                            String testDescription) {
    // GIVEN
    comparatorByTypes.entrySet().stream()
                     .forEach(entry -> recursiveComparisonConfiguration.registerComparatorForType(entry.getValue(),
                                                                                                  entry.getKey()));
    // THEN
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .isEqualTo(expected);
  }

  @ParameterizedTest(name = "{3}: actual={0} / expected={1} - comparatorsByType: {2}")
  @MethodSource("recursivelyEqualObjectsWhenUsingTypeComparators")
  void should_pass_for_objects_with_the_same_data_when_using_registered_equals_by_types(Object actual,
                                                                                        Object expected,
                                                                                        Map<Class<?>, Comparator<Object>> comparatorByTypes,
                                                                                        String testDescription) {
    // GIVEN
    comparatorByTypes.entrySet().stream()
                     .forEach(entry -> recursiveComparisonConfiguration.registerEqualsForType(asBiPredicate(entry.getValue()),
                                                                                              entry.getKey()));
    // THEN
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .isEqualTo(expected);
  }

  private static BiPredicate<Object, Object> asBiPredicate(Comparator<Object> comparator) {
    return (Object o1, Object o2) -> comparator.compare(o1, o2) == 0;
  }

  private static Stream<Arguments> recursivelyEqualObjectsWhenUsingTypeComparators() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;
    Person person2 = new Person("JoHN");
    person2.home.address.number = 2;

    Person person3 = new Person("John");
    person3.home.address.number = 1;
    Person person4 = new Person("John");
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

    MapEntry<Class<?>, Comparator<?>> stringComparator = entry(String.class, CaseInsensitiveStringComparator.INSTANCE);
    MapEntry<Class<?>, Comparator<?>> intComparator = entry(Integer.class, new AlwaysEqualComparator<Integer>());
    MapEntry<Class<?>, Comparator<?>> personComparator = entry(Person.class, new AlwaysEqualComparator<Person>());
    return Stream.of(arguments(person1, person2, mapOf(stringComparator, intComparator),
                               "same data except int fields and case for strings"),
                     arguments(person3, person4, mapOf(intComparator), "same data except for int fields"),
                     // any neighbour differences should be ignored as we compare persons with AlwaysEqualComparator
                     arguments(person5, person6, mapOf(personComparator),
                               "same data except for persons, person's fields should not be compared recursively except at the root level"));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_when_using_comparators_by_type() {
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
    // register comparators for some type that will fail the comparison
    recursiveComparisonConfiguration.registerComparatorForType(new AlwaysDifferentComparator<>(), Date.class);
    recursiveComparisonConfiguration.registerEqualsForType((Address a1, Address a2) -> false, Address.class);

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference dateOfBirthDifference = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference addressDifference = diff("home.address", actual.home.address, expected.home.address);
    ComparisonDifference neighbourAddressDifference = diff("neighbour.home.address", actual.neighbour.home.address,
                                                           expected.neighbour.home.address);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, dateOfBirthDifference,
                                                              addressDifference, neighbourAddressDifference);
  }

  @Test
  void should_be_able_to_compare_objects_recursively_using_some_precision_for_numerical_types() {
    // GIVEN
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;

    // THEN
    assertThat(goliath).usingRecursiveComparison()
                       .withComparatorForType(new AtPrecisionComparator<>(0.2), Double.class)
                       .isEqualTo(goliathTwin);
    assertThat(goliath).usingRecursiveComparison()
                       .withEqualsForType((d1, d2) -> Math.abs(d1 - d2) < 0.2, Double.class)
                       .isEqualTo(goliathTwin);
  }

  @Test
  void should_handle_null_field_with_type_comparator() {
    // GIVEN
    Patient actual = new Patient(null);
    Patient expected = new Patient(new Timestamp(3L));
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForType(ALWAYS_EQUALS_TIMESTAMP, Timestamp.class)
                      .isEqualTo(expected);
    assertThat(actual).usingRecursiveComparison()
                      .withEqualsForType((o1, o2) -> true, Timestamp.class)
                      .isEqualTo(expected);
  }

  @Test
  void should_use_custom_comparator_over_reference_comparison() {
    // GIVEN
    Timestamp dateOfBirth = new Timestamp(3L);
    Patient actual = new Patient(dateOfBirth);
    Patient expected = new Patient(dateOfBirth);
    // THEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .withComparatorForType(NEVER_EQUALS,
                                                                                                        Timestamp.class)
                                                                                 .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining("- java.sql.Timestamp -> org.assertj.core.test.NeverEqualComparator");
  }

  @Test
  void should_use_custom_equal_over_reference_comparison() {
    // GIVEN
    Timestamp dateOfBirth = new Timestamp(3L);
    Patient actual = new Patient(dateOfBirth);
    Patient expected = new Patient(dateOfBirth);
    // THEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .withEqualsForType((o1, o2) -> false,
                                                                                                    Timestamp.class)
                                                                                 .isEqualTo(expected));
    then(assertionError).hasMessageContaining("- java.sql.Timestamp -> ");
    // THEN
  }

  @Test
  void should_treat_timestamp_as_equal_to_date_when_registering_a_Date_symmetric_comparator() {
    // GIVEN
    Person actual = new Person("Fred");
    actual.dateOfBirth = new Timestamp(1000L);
    Person expected = new Person(actual.name);
    expected.dateOfBirth = new Date(1000L);
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForType(SYMMETRIC_DATE_COMPARATOR, Timestamp.class)
                      .isEqualTo(expected);
    assertThat(expected).usingRecursiveComparison()
                        .withComparatorForType(SYMMETRIC_DATE_COMPARATOR, Timestamp.class)
                        .isEqualTo(actual);
  }

  @Test
  void ignoringOverriddenEquals_should_not_interfere_with_comparators_by_type() {
    // GIVEN
    Person actual = new Person("Fred");
    actual.neighbour = new AlwaysEqualPerson();
    actual.neighbour.name = "Omar";
    Person expected = new Person("Fred");
    expected.neighbour = new AlwaysEqualPerson();
    expected.neighbour.name = "Omar2";
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForType(ALWAYS_EQUALS, AlwaysEqualPerson.class) // fails if commented
                      .ignoringOverriddenEqualsForFields("neighbour")
                      .isEqualTo(expected);
    assertThat(actual).usingRecursiveComparison()
                      .withEqualsForType((o1, o2) -> true, AlwaysEqualPerson.class) // fails if commented
                      .ignoringOverriddenEqualsForFields("neighbour")
                      .isEqualTo(expected);
  }

  @Test
  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  void should_pass_using_a_BiPredicate_to_compare_fields_with_different_types_but_same_values() {
    // GIVEN
    TimeOffset actual = new TimeOffset();
    actual.time = LocalTime.now();
    TimeOffsetDto expected = new TimeOffsetDto();
    expected.time = actual.time.toString();

    // WHEN
    assertThat(actual).usingRecursiveComparison()
                      .withEqualsForTypes((t, s) -> LocalTime.parse(s).equals(t), LocalTime.class, String.class)
                      // THEN
                      .isEqualTo(expected);
  }

  @Test
  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  void should_pass_using_two_BiPredicates_that_matches_to_compare_fields_with_different_types_but_same_values() {
    // GIVEN
    TimeOffset actual = new TimeOffset();
    actual.time = LocalTime.now();
    actual.offset = ZoneOffset.UTC;
    TimeOffsetDto expected = new TimeOffsetDto();
    expected.time = actual.time.toString();
    expected.offset = actual.offset.getId();
    // WHEN
    assertThat(actual).usingRecursiveComparison()
                      .withEqualsForTypes((z, s) -> ZoneOffset.of(s).equals(z), ZoneOffset.class, String.class)
                      .withEqualsForTypes((t, s) -> LocalTime.parse(s).equals(t), LocalTime.class, String.class)
                      // THEN
                      .isEqualTo(expected);
  }

  @Test
  @SuppressWarnings("AssertBetweenInconvertibleTypes")
  void should_pass_having_two_BiPredicate_with_same_left_type_but_same_values_and_every_one_match() {
    // GIVEN
    LocalTime now = LocalTime.now();
    TimeOffsetDto actual = new TimeOffsetDto();
    actual.time = now.toString();
    actual.offset = "Z";
    TimeOffset expected = new TimeOffset();
    expected.time = now;
    expected.offset = ZoneOffset.UTC;
    // WHEN
    assertThat(actual).usingRecursiveComparison()
                      .withEqualsForTypes((s, z) -> ZoneOffset.of(s).equals(z), String.class, ZoneOffset.class)
                      .withEqualsForTypes((s, t) -> LocalTime.parse(s).equals(t), String.class, LocalTime.class)
                      // THEN
                      .isEqualTo(expected);
  }

}
