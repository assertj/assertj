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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.internal.objects.SymmetricDateComparator.SYMMETRIC_DATE_COMPARATOR;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TIMESTAMP;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.NeverEqualComparator.NEVER_EQUALS;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.AtPrecisionComparator;
import org.assertj.core.internal.CaseInsensitiveStringComparator;
import org.assertj.core.internal.objects.data.Address;
import org.assertj.core.internal.objects.data.Giant;
import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.test.AlwaysDifferentComparator;
import org.assertj.core.test.AlwaysEqualComparator;
import org.assertj.core.test.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonAssert_isEqualTo_withTypeComparators_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{3}: actual={0} / expected={1} - comparatorsByType: {2}")
  @MethodSource("recursivelyEqualObjectsWhenUsingTypeComparators")
  public void should_pass_for_objects_with_the_same_data_when_using_registered_comparator_by_types(Object actual,
                                                                                                   Object expected,
                                                                                                   Map<Class<?>, Comparator<Object>> comparatorByTypes,
                                                                                                   String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForTypes(comparatorByTypes.entrySet().toArray(new Map.Entry[0]))
                      .isEqualTo(expected);

  }

  @SuppressWarnings("unused")
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

    MapEntry<Class<?>, Comparator<?>> stringComparator = entry(String.class, CaseInsensitiveStringComparator.instance);
    MapEntry<Class<?>, Comparator<?>> intComparator = entry(Integer.class, new AlwaysEqualComparator<Integer>());
    MapEntry<Class<?>, Comparator<?>> personComparator = entry(Person.class, new AlwaysEqualComparator<Person>());
    return Stream.of(Arguments.of(person1, person2, mapOf(stringComparator, intComparator),
                                  "same data except int fields and case for strings"),
                     Arguments.of(person3, person4, mapOf(intComparator), "same data except for int fields"),
                     // any neighbour differences should be ignored as we compare persons with AlwaysEqualComparator
                     Arguments.of(person5, person6, mapOf(personComparator),
                                  "same data except for persons, person's fields should not be compared recursively except at the root level"));
  }

  @Test
  public void should_fail_when_actual_differs_from_expected_when_using_comparators_by_type() {
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
    recursiveComparisonConfiguration.registerComparatorForType(Person.class, new AlwaysDifferentComparator<>());
    recursiveComparisonConfiguration.registerComparatorForType(Date.class, new AlwaysDifferentComparator<>());
    recursiveComparisonConfiguration.registerComparatorForType(Address.class, new AlwaysDifferentComparator<>());

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference dateOfBirthDifference = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference addressDifference = diff("home.address", actual.home.address, expected.home.address);
    ComparisonDifference neighbourDifference = diff("neighbour", actual.neighbour, expected.neighbour);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected,
                                                              dateOfBirthDifference, addressDifference, neighbourDifference);
  }

  @Test
  public void should_be_able_to_compare_objects_recursively_using_some_precision_for_numerical_types() {
    // GIVEN
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;

    // THEN
    assertThat(goliath).usingRecursiveComparison()
                       .withComparatorForType(Double.class, new AtPrecisionComparator<>(0.2))
                       .isEqualTo(goliathTwin);
  }

  @Test
  public void should_handle_null_field_with_type_comparator() {
    // GIVEN
    Patient actual = new Patient(null);
    Patient expected = new Patient(new Timestamp(3L));
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForType(Timestamp.class, ALWAY_EQUALS_TIMESTAMP)
                      .isEqualTo(expected);
  }

  @Test
  public void should_ignore_comparators_when_fields_are_the_same() {
    // GIVEN
    Timestamp dateOfBirth = new Timestamp(3L);
    Patient actual = new Patient(dateOfBirth);
    Patient expected = new Patient(dateOfBirth);
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForType(Timestamp.class, NEVER_EQUALS)
                      .isEqualTo(expected);
  }

  @Test
  public void should_treat_timestamp_as_equal_to_date_when_registering_a_Date_symmetric_comparator() {
    // GIVEN
    Person actual = new Person("Fred");
    actual.dateOfBirth = new Timestamp(1000L);
    Person expected = new Person(actual.name);
    expected.dateOfBirth = new Date(1000L);
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .withComparatorForType(Timestamp.class, SYMMETRIC_DATE_COMPARATOR)
                      .isEqualTo(expected);
    assertThat(expected).usingRecursiveComparison()
                        .withComparatorForType(Timestamp.class, SYMMETRIC_DATE_COMPARATOR)
                        .isEqualTo(actual);
  }

}
