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

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursive;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.core.internal.objects.SymmetricDateComparator.SYMMETRIC_DATE_COMPARATOR;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TIMESTAMP;
import static org.assertj.core.test.NeverEqualComparator.NEVER_EQUALS;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.AtPrecisionComparator;
import org.assertj.core.internal.DeepDifference.Difference;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.internal.objects.data.AlwaysEqualPerson;
import org.assertj.core.internal.objects.data.FriendlyPerson;
import org.assertj.core.internal.objects.data.Giant;
import org.assertj.core.internal.objects.data.Human;
import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.test.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonAssert_isEqualTo_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @Test
  public void should_pass_when_actual_and_expected_are_null() {
    // GIVEN
    Person actual = null;
    Person expected = null;
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  @Test
  public void should_fail_when_actual_is_null_and_expected_is_not() {
    // GIVEN
    Person actual = null;
    Person expected = new Person();
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verify(failures).failure(info, shouldNotBeNull());
  }

  @Test
  public void should_fail_when_actual_is_not_null_and_expected_is() {
    // GIVEN
    Person actual = new Person();
    Person expected = null;
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verify(failures).failure(info, shouldBeEqual(actual, null, objects.getComparisonStrategy(), info.representation()));
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("recursivelyEqualObjects")
  public void should_pass_for_objects_with_the_same_data_when_using_the_default_recursive_comparison(Object actual,
                                                                                                     Object expected,
                                                                                                     String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);

  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> recursivelyEqualObjects() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;
    Person person2 = new Person("John");
    person2.home.address.number = 1;

    Person person3 = new Person("John");
    person3.home.address.number = 1;
    Human person4 = new Human();
    person4.name = "John";
    person4.home.address.number = 1;

    return Stream.of(arguments(person1, person2, "same data, same type"),
                     arguments(person2, person1, "same data, same type reversed"),
                     arguments(person3, person4, "same data, different type"),
                     arguments(person4, person3, "same data, different type"));
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("recursivelyEqualObjectsIgnoringNullValues")
  public void should_pass_for_objects_with_the_same_data_when_all_null_fields_are_ignored(Object actual,
                                                                                          Object expected,
                                                                                          String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringActualNullFields()
                      .isEqualTo(expected);
  }

  @Test
  public void should_fail_when_actual_differs_from_expected_even_when_all_null_fields_are_ignored() {
    // GIVEN
    Person actual = new Person(null);
    actual.home.address.number = 1;
    actual.dateOfBirth = null;
    actual.neighbour = null;
    Person expected = new Person("John");
    expected.home.address.number = 2;
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(list("home.address.number"), 1, 2);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, comparisonDifference);
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> recursivelyEqualObjectsIgnoringNullValues() {
    Person person1 = new Person(null);
    person1.home.address.number = 1;

    Person person2 = new Person("John");
    person2.home.address.number = 1;

    Person person3 = new Person(null);
    person3.home = null;

    Human person4 = new Human();
    person4.name = "John";
    person4.home.address.number = 1;

    Human person5 = new Human();
    person5.home.address.number = 1;

    Person person6 = new Person();
    person6.name = "John";
    person6.neighbour = null;
    person6.dateOfBirth = null;
    person6.home.address = null;

    Person person7 = new Person();
    person7.name = "John";
    person7.neighbour = new Person("Jack");
    person7.neighbour.home = null;
    person7.neighbour.neighbour = new Person("James");
    person7.neighbour.neighbour.home.address = null;
    person7.home.address = null;

    Person person8 = new Person();
    person8.name = "John";
    person8.neighbour = new Person("Jack");
    person8.neighbour.home.address.number = 123;
    person8.neighbour.neighbour = new Person("James");
    person8.neighbour.neighbour.home.address.number = 456;

    return Stream.of(arguments(person1, person2, "same data, same type, except for actual null fields"),
                     arguments(person3, person1, "all actual fields are null, should be equal to anything"),
                     arguments(person3, person2, "all actual fields are null, should be equal to anything"),
                     arguments(person3, person3, "all actual fields are null, should be equal to anything"),
                     arguments(person3, person4, "same data, different type, actual has null fields"),
                     arguments(person5, person2, "same data, different type, actual has null fields"),
                     arguments(person6, person2, "same data, different type, actual has only non null name"),
                     arguments(person6, person7, "same data, actual has only non null name"),
                     arguments(person6, person8, "same data, actual has only non null name"),
                     arguments(person7, person8, "same data, actual has null fields deep in its graph"));
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored fields={3}")
  @MethodSource("recursivelyEqualObjectsIgnoringGivenFields")
  public void should_pass_for_objects_with_the_same_data_when_given_fields_are_ignored(Object actual,
                                                                                       Object expected,
                                                                                       String testDescription,
                                                                                       List<String> ignoredFields) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringFields(ignoredFields.toArray(new String[0]))
                      .isEqualTo(expected);
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> recursivelyEqualObjectsIgnoringGivenFields() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;

    Person giant1 = new Giant();
    giant1.name = "Giant John";
    ((Giant) giant1).height = 3.1;
    giant1.home.address.number = 1;

    Person person2 = new Person("Jack");
    person2.home.address.number = 1;

    Person person3 = new Person("John");
    person3.home.address.number = 123;

    Human person4 = new Human();
    person4.name = "Jack";
    person4.home.address.number = 456;

    Person person5 = new Person();
    person5.home.address.number = 1;

    Person person6 = new Person();
    person6.home.address.number = 2;

    Person person7 = new Person("John");
    person7.neighbour = new Person("Jack");
    person7.neighbour.home.address.number = 123;
    person7.neighbour.neighbour = new Person("James");
    person7.neighbour.neighbour.home.address.number = 124;

    Person person8 = new Person("John");
    person8.neighbour = new Person("Jim");
    person8.neighbour.home.address.number = 123;
    person8.neighbour.neighbour = new Person("James");
    person8.neighbour.neighbour.home.address.number = 457;

    return Stream.of(arguments(person1, person2, "same data and type, except for one ignored field",
                               list("name")),
                     arguments(giant1, person1,
                               "different type, same data except name and height which is not even a field from person1",
                               list("name", "height")),
                     arguments(person3, person4, "same data, different type, except for several ignored fields",
                               list("name", "home.address.number")),
                     arguments(person5, person6, "same data except for one subfield of an ignored field",
                               list("home")),
                     arguments(person7, person8, "same data except for one subfield of an ignored field",
                               list("neighbour.neighbour.home.address.number", "neighbour.name")));
  }

  @Test
  public void should_fail_when_actual_differs_from_expected_even_when_some_fields_are_ignored() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jack");
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.home.address.number = 124;

    Person expected = new Person("Jack");
    expected.home.address.number = 2;
    expected.dateOfBirth = new Date(456);
    expected.neighbour = new Person("Jim");
    expected.neighbour.home.address.number = 123;
    expected.neighbour.neighbour = new Person("James");
    expected.neighbour.neighbour.home.address.number = 457;

    recursiveComparisonConfiguration.ignoreFields("name", "home.address.number");

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference dateOfBirthDifference = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference neighbourNameDifference = diff("neighbour.name", actual.neighbour.name, expected.neighbour.name);
    ComparisonDifference numberDifference = diff("neighbour.neighbour.home.address.number",
                                                 actual.neighbour.neighbour.home.address.number,
                                                 expected.neighbour.neighbour.home.address.number);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected,
                                                              dateOfBirthDifference, neighbourNameDifference, numberDifference);
  }

  @Test
  public void should_fail_when_actual_differs_from_expected_even_when_some_fields_are_ignored_by_regexes() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jack");
    actual.neighbour.dateOfBirth = new Date(123);
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.home.address.number = 124;

    Person expected = new Person("Jack");
    expected.home.address.number = 2;
    expected.dateOfBirth = new Date(456);
    expected.neighbour = new Person("Jim");
    expected.neighbour.dateOfBirth = new Date(456);
    expected.neighbour.home.address.number = 234;
    expected.neighbour.neighbour = new Person("James");
    expected.neighbour.neighbour.home.address.number = 457;

    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(".*name", ".*home.*number");

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference dateOfBirthDifference = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference neighbourdateOfBirthDifference = diff("neighbour.dateOfBirth",
                                                               actual.neighbour.dateOfBirth,
                                                               expected.neighbour.dateOfBirth);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected,
                                                              dateOfBirthDifference, neighbourdateOfBirthDifference);
  }

  // old tests

  @Test
  public void should_be_able_to_compare_objects_recursively_using_some_precision_for_numerical_types() {
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;

    assertThat(goliath).usingComparatorForType(new AtPrecisionComparator<>(0.2), Double.class)
                       .isEqualToComparingFieldByFieldRecursively(goliathTwin);
  }

  @Test
  public void should_be_able_to_compare_objects_recursively_using_given_comparator_for_specified_field() {
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;

    assertThat(goliath).usingComparatorForFields(new AtPrecisionComparator<>(0.2), "height")
                       .isEqualToComparingFieldByFieldRecursively(goliathTwin);
  }

  @Test
  public void should_be_able_to_compare_objects_recursively_using_given_comparator_for_specified_nested_field() {
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;
    goliath.home.address.number = 1;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;
    goliathTwin.home.address.number = 5;

    assertThat(goliath).usingComparatorForFields(new AtPrecisionComparator<>(0.2), "height")
                       .usingComparatorForFields(new AtPrecisionComparator<>(10), "home.address.number")
                       .isEqualToComparingFieldByFieldRecursively(goliathTwin);
  }

  @Test
  public void should_fail_when_fields_differ() {
    Person actual = new Person();
    actual.name = "John";

    Person other = new Person();
    other.name = "Jack";

    try {
      objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, noFieldComparators(),
                                                              defaultTypeComparators());
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeEqualByComparingFieldByFieldRecursive(actual, other,
                                                                                   asList(new Difference(asList("name"),
                                                                                                         "John",
                                                                                                         "Jack")),
                                                                                   CONFIGURATION_PROVIDER.representation()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_fields_of_child_objects_differ() {
    Person actual = new Person();
    actual.name = "John";
    actual.home.address.number = 1;

    Person other = new Person();
    other.name = "John";
    other.home.address.number = 2;

    try {
      objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, noFieldComparators(),
                                                              defaultTypeComparators());
    } catch (AssertionError err) {
      verify(failures).failure(info,
                               shouldBeEqualByComparingFieldByFieldRecursive(actual, other,
                                                                             asList(new Difference(asList("home.address.number"),
                                                                                                   1,
                                                                                                   2)),
                                                                             CONFIGURATION_PROVIDER.representation()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_have_error_message_with_differences_and_path_to_differences() {
    Person actual = new Person();
    actual.name = "Jack";
    actual.home.address.number = 1;

    Person other = new Person();
    other.name = "John";
    other.home.address.number = 2;

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).isEqualToComparingFieldByFieldRecursively(other))
                                                   .withMessage(format("%nExpecting:%n  <%s>%nto be equal to:%n  <%s>%n"
                                                                       +
                                                                       "when recursively comparing field by field, but found the following difference(s):%n%n"
                                                                       +
                                                                       "Path to difference: <home.address.number>%n" +
                                                                       "- actual  : <1>%n" +
                                                                       "- expected: <2>%n%n" +
                                                                       "Path to difference: <name>%n" +
                                                                       "- actual  : <\"Jack\">%n" +
                                                                       "- expected: <\"John\">", actual, other));
  }

  @Test
  public void should_have_error_message_with_path_to_difference_when_difference_is_in_collection() {
    FriendlyPerson actual = new FriendlyPerson();
    FriendlyPerson friendOfActual = new FriendlyPerson();
    friendOfActual.home.address.number = 99;
    actual.friends = Arrays.asList(friendOfActual);

    FriendlyPerson other = new FriendlyPerson();
    FriendlyPerson friendOfOther = new FriendlyPerson();
    friendOfOther.home.address.number = 10;
    other.friends = Arrays.asList(friendOfOther);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(actual).isEqualToComparingFieldByFieldRecursively(other))
                                                   .withMessage(format("%nExpecting:%n  <%s>%nto be equal to:%n  <%s>%n"
                                                                       +
                                                                       "when recursively comparing field by field, but found the following difference(s):%n%n"
                                                                       +
                                                                       "Path to difference: <friends.home.address.number>%n"
                                                                       +
                                                                       "- actual  : <99>%n" +
                                                                       "- expected: <10>", actual, other));
  }

  @Test
  public void should_not_use_equal_implementation_of_objects_to_compare() {
    AlwaysEqualPerson actual = new AlwaysEqualPerson();
    actual.name = "John";
    actual.home.address.number = 1;

    AlwaysEqualPerson other = new AlwaysEqualPerson();
    other.name = "John";
    other.home.address.number = 2;

    try {
      objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, noFieldComparators(),
                                                              defaultTypeComparators());
    } catch (AssertionError err) {
      verify(failures).failure(info,
                               shouldBeEqualByComparingFieldByFieldRecursive(actual, other,
                                                                             asList(new Difference(asList("home.address.number"),
                                                                                                   1, 2)),
                                                                             CONFIGURATION_PROVIDER.representation()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_comparing_unsorted_with_sorted_set() {
    WithCollection<String> actual = new WithCollection<>(new LinkedHashSet<String>());
    actual.collection.add("bar");
    actual.collection.add("foo");
    WithCollection<String> expected = new WithCollection<>(new TreeSet<String>());
    expected.collection.add("bar");
    expected.collection.add("foo");

    try {
      assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    } catch (AssertionError err) {
      assertThat(err).hasMessageContaining(format("Path to difference: <collection>%n"));
      assertThat(err).hasMessageContaining(format("- actual  : <[\"bar\", \"foo\"] (LinkedHashSet@"));
      assertThat(err).hasMessageContaining(format("- expected: <[\"bar\", \"foo\"] (TreeSet@"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_comparing_sorted_with_unsorted_set() {
    WithCollection<String> actual = new WithCollection<>(new TreeSet<String>());
    actual.collection.add("bar");
    actual.collection.add("foo");
    WithCollection<String> expected = new WithCollection<>(new LinkedHashSet<String>());
    expected.collection.add("bar");
    expected.collection.add("foo");

    try {
      assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    } catch (AssertionError err) {
      assertThat(err).hasMessageContaining(format("Path to difference: <collection>%n"));
      assertThat(err).hasMessageContaining(format("- actual  : <[\"bar\", \"foo\"] (TreeSet@"));
      assertThat(err).hasMessageContaining(format("- expected: <[\"bar\", \"foo\"] (LinkedHashSet@"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_comparing_unsorted_with_sorted_map() {
    WithMap<Long, Boolean> actual = new WithMap<>(new LinkedHashMap<>());
    actual.map.put(1L, true);
    actual.map.put(2L, false);
    WithMap<Long, Boolean> expected = new WithMap<>(new TreeMap<>());
    expected.map.put(2L, false);
    expected.map.put(1L, true);

    try {
      assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    } catch (AssertionError err) {
      assertThat(err).hasMessageContaining(format("Path to difference: <map>%n"));
      assertThat(err).hasMessageContaining(format("- actual  : <{1L=true, 2L=false} (LinkedHashMap@"));
      assertThat(err).hasMessageContaining(format("- expected: <{1L=true, 2L=false} (TreeMap@"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_comparing_sorted_with_unsorted_map() {
    WithMap<Long, Boolean> actual = new WithMap<>(new TreeMap<Long, Boolean>());
    actual.map.put(1L, true);
    actual.map.put(2L, false);
    WithMap<Long, Boolean> expected = new WithMap<>(new LinkedHashMap<Long, Boolean>());
    expected.map.put(2L, false);
    expected.map.put(1L, true);

    try {
      assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    } catch (AssertionError err) {
      assertThat(err).hasMessageContaining(format("Path to difference: <map>%n"));
      assertThat(err).hasMessageContaining(format("- actual  : <{1L=true, 2L=false} (TreeMap@"));
      assertThat(err).hasMessageContaining(format("- expected: <{1L=true, 2L=false} (LinkedHashMap@"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_handle_null_field_with_field_comparator() {
    // GIVEN
    Patient adam = new Patient(null);
    Patient eve = new Patient(new Timestamp(3L));
    // THEN
    assertThat(adam).usingComparatorForFields(ALWAY_EQUALS, "dateOfBirth", "health")
                    .isEqualToComparingFieldByFieldRecursively(eve);
  }

  @Test
  public void should_handle_null_field_with_type_comparator() {
    // GIVEN
    Patient adam = new Patient(null);
    Patient eve = new Patient(new Timestamp(3L));
    // THEN
    assertThat(adam).usingComparatorForType(ALWAY_EQUALS_TIMESTAMP, Timestamp.class)
                    .isEqualToComparingFieldByFieldRecursively(eve);
  }

  @Test
  public void should_not_bother_with_comparators_when_fields_are_the_same() {
    // GIVEN
    Timestamp dateOfBirth = new Timestamp(3L);
    Patient adam = new Patient(dateOfBirth);
    Patient eve = new Patient(dateOfBirth);
    // THEN
    assertThat(adam).usingComparatorForFields(NEVER_EQUALS, "dateOfBirth")
                    .isEqualToComparingFieldByFieldRecursively(eve);
  }

  @Test
  public void should_treat_date_as_equal_to_timestamp() {
    Person actual = new Person();
    actual.name = "Fred";
    actual.dateOfBirth = new Date(1000L);

    Person other = new Person();
    other.name = "Fred";
    other.dateOfBirth = new Timestamp(1000L);

    objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, noFieldComparators(),
                                                            defaultTypeComparators());
  }

  @Test
  public void should_treat_timestamp_as_equal_to_date_when_registering_a_Date_symmetric_comparator() {
    Person actual = new Person();
    actual.name = "Fred";
    actual.dateOfBirth = new Timestamp(1000L);

    Person other = new Person();
    other.name = "Fred";
    other.dateOfBirth = new Date(1000L);

    TypeComparators typeComparators = new TypeComparators();
    typeComparators.put(Timestamp.class, SYMMETRIC_DATE_COMPARATOR);

    objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, noFieldComparators(),
                                                            typeComparators);
    objects.assertIsEqualToComparingFieldByFieldRecursively(info, other, actual, noFieldComparators(),
                                                            typeComparators);
  }

  @Test
  public void should_treat_timestamp_as_equal_to_date_when_registering_a_Date_symmetric_comparator_for_field() {
    Person actual = new Person();
    actual.name = "Fred";
    actual.dateOfBirth = new Timestamp(1000L);

    Person other = new Person();
    other.name = "Fred";
    other.dateOfBirth = new Date(1000L);

    Map<String, Comparator<?>> fieldComparators = new HashMap<>();
    fieldComparators.put("dateOfBirth", SYMMETRIC_DATE_COMPARATOR);
    objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, fieldComparators,
                                                            defaultTypeComparators());
  }

  @Test
  public void should_be_able_to_compare_objects_with_percentages() {
    Person actual = new Person();
    actual.name = "foo";

    Person other = new Person();
    other.name = "%foo";

    try {
      objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, noFieldComparators(),
                                                              defaultTypeComparators());
    } catch (AssertionError err) {
      assertThat(err).hasMessageContaining("Path to difference: <name>")
                     .hasMessageContaining("- expected: <\"%foo\">")
                     .hasMessageContaining("- actual  : <\"foo\">");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_report_missing_property() {
    // GIVEN
    Human joe = new Human();
    joe.name = "joe";
    Giant goliath = new Giant();
    goliath.name = "joe";
    goliath.height = 3.0;
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(goliath).isEqualToComparingFieldByFieldRecursively(joe));
    // THEN
    assertThat(error).hasMessageContaining("Human does not declare all Giant fields")
                     .hasMessageContaining("[height]");
  }

  @Test
  public void should_be_able_to_compare_objects_with_cycles_recursively() {
    // GIVEN
    FriendlyPerson actual = new FriendlyPerson();
    actual.name = "John";
    actual.home.address.number = 1;

    FriendlyPerson expected = new FriendlyPerson();
    expected.name = "John";
    expected.home.address.number = 1;

    // neighbour
    expected.neighbour = actual;
    actual.neighbour = expected;

    // friends
    FriendlyPerson sherlock = new FriendlyPerson();
    sherlock.name = "Sherlock";
    sherlock.home.address.number = 221;
    actual.friends.add(sherlock);
    actual.friends.add(expected);
    expected.friends.add(sherlock);
    expected.friends.add(actual);

    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  @Test
  public void should_honor_test_description() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("John");
    expected.home.address.number = 2;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).as("test description")
                                                                        .usingRecursiveComparison()
                                                                        .isEqualTo(expected));
    // THEN
    assertThat(error).hasMessageContaining("[test description]");
  }

  public static class WithMap<K, V> {
    public Map<K, V> map;

    public WithMap(Map<K, V> map) {
      this.map = map;
    }

    @Override
    public String toString() {
      return format("WithMap [map=%s]", map);
    }

  }

  public static class WithCollection<E> {
    public Collection<E> collection;

    public WithCollection(Collection<E> collection) {
      this.collection = collection;
    }

    @Override
    public String toString() {
      return format("WithCollection [collection=%s]", collection);
    }

  }

}
