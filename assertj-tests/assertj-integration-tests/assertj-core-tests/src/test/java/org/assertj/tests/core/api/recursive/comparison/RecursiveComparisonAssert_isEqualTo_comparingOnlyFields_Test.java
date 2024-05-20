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

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.set;
import static org.assertj.tests.core.api.recursive.data.FriendlyPerson.friend;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.tests.core.api.recursive.data.FriendlyPerson;
import org.assertj.tests.core.api.recursive.data.Human;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_comparingOnlyFields_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource
  void should_only_compare_given_fields(Object actual, Object expected, String[] fieldNamesToCompare) {

    then(actual).usingRecursiveComparison()
                .comparingOnlyFields(fieldNamesToCompare)
                .isEqualTo(expected);
  }

  private static Stream<Arguments> should_only_compare_given_fields() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;

    Person person2 = new Person("John");
    person2.home.address.number = 2;

    Person john = new Person("John");
    john.home.address.number = 1;
    john.dateOfBirth = new Date(123);
    john.neighbour = new Person("Jim");
    john.neighbour.home.address.number = 123;
    john.neighbour.neighbour = new Person("James");
    john.neighbour.neighbour.home.address.number = 124;

    Person jack = new Person("Jack");
    jack.home.address.number = 1;
    jack.dateOfBirth = new Date(456);
    jack.neighbour = new Person("Jack");
    jack.neighbour.home.address.number = 456;
    jack.neighbour.neighbour = new Person("James");
    jack.neighbour.neighbour.home.address.number = 124;

    Human person4 = new Human();
    person4.name = "John";
    person4.home.address.number = 1;

    Human person5 = new Human();
    person5.home.address.number = 1;

    return Stream.of(arguments(person1, person2, array("name")),
                     arguments(person1, person4, array("name")),
                     arguments(person1, person5, array("home")),
                     arguments(person1, person5, array("home.address")),
                     arguments(person1, person5, array("home.address.number")),
                     arguments(john, jack, array("home", "neighbour.neighbour")),
                     arguments(john, jack, array("home.address", "neighbour.neighbour")),
                     arguments(john, jack, array("home.address.number", "neighbour.neighbour")),
                     arguments(john, jack, array("home", "neighbour.neighbour.home")),
                     arguments(john, jack, array("home.address", "neighbour.neighbour")));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_on_compared_fields() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jim");
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.home.address.number = 124;

    Person expected = new Person("John");
    expected.home.address.number = 1;
    expected.dateOfBirth = new Date(456);
    expected.neighbour = new Person("Jack");
    expected.neighbour.home.address.number = 123;
    expected.neighbour.neighbour = new Person("James");
    expected.neighbour.neighbour.home.address.number = 125;

    recursiveComparisonConfiguration.compareOnlyFields("name", "home", "dateOfBirth", "neighbour");

    // WHEN/THEN
    ComparisonDifference dateOfBirthDifference = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference neighbourNameDifference = diff("neighbour.name", actual.neighbour.name, expected.neighbour.name);
    ComparisonDifference numberDifference = diff("neighbour.neighbour.home.address.number",
                                                 actual.neighbour.neighbour.home.address.number,
                                                 expected.neighbour.neighbour.home.address.number);
    compareRecursivelyFailsWithDifferences(actual, expected, dateOfBirthDifference, neighbourNameDifference, numberDifference);
  }

  @Test
  void can_be_combined_with_ignoringFields() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jim");
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.home.address.number = 124;

    Person expected = new Person(actual.name);
    expected.home.address.number = 2;
    expected.dateOfBirth = new Date(456);
    expected.neighbour = new Person("Jack");
    expected.neighbour.home.address.number = actual.neighbour.home.address.number;
    expected.neighbour.neighbour = new Person(actual.neighbour.neighbour.name);
    expected.neighbour.neighbour.home.address.number = 125;

    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .comparingOnlyFields("name", "home", "neighbour")
                // ignores all different fields from the compared fields
                .ignoringFields("home.address.number", "neighbour.name", "neighbour.neighbour.home.address.number")
                .isEqualTo(expected);
    then(actual).usingRecursiveComparison()
                // ignores all different fields from the compared fields
                .ignoringFields("home.address.number", "neighbour.name", "neighbour.neighbour.home.address.number")
                .comparingOnlyFields("name", "home", "neighbour")
                .isEqualTo(expected);
  }

  @SuppressWarnings("unused")
  static class Staff {

    private Boolean deleted;
    private ZonedDateTime deletedAt;
    private String defaultRole;
    private String defaultRoleName;

    void setDeleted(Boolean deleted) {
      this.deleted = deleted;
    }

    void setDeletedAt(ZonedDateTime deletedAt) {
      this.deletedAt = deletedAt;
    }

    void setDefaultRole(String defaultRole) {
      this.defaultRole = defaultRole;
    }

    void setDefaultRoleName(String defaultRoleName) {
      this.defaultRoleName = defaultRoleName;
    }

  }

  // #2359
  @Test
  void should_not_compare_given_fields_starting_with_given_name_but_fully_matching_name() {
    // GIVEN
    Staff actual = new Staff();
    actual.setDeleted(true);
    actual.setDeletedAt(ZonedDateTime.parse("2021-10-05T04:21:05.863+00:00"));
    actual.setDefaultRole("MANAGER");
    actual.setDefaultRoleName("MANAGER");
    Staff expected = new Staff();
    expected.setDeleted(true);
    expected.setDeletedAt(null);
    expected.setDefaultRole("MANAGER");
    expected.setDefaultRoleName("UX MANAGER");
    // WHEN/THEN
    // defaultRoleName or deletedAt should not be compared.
    then(actual).usingRecursiveComparison()
                .comparingOnlyFields("defaultRole", "deleted")
                .isEqualTo(expected);
  }

  static class StaffWithLessFields {
    Boolean deleted;
  }

  @Test
  public void should_fail_when_actual_differs_from_expected_on_compared_fields_independent_of_object_order() {
    // GIVEN
    Staff staff = new Staff();
    StaffWithLessFields staffWithLessFields = new StaffWithLessFields();
    staff.setDeleted(Boolean.TRUE);
    staffWithLessFields.deleted = Boolean.FALSE;
    // WHEN
    recursiveComparisonConfiguration.compareOnlyFields("deleted");
    // THEN
    compareRecursivelyFailsWithDifferences(staffWithLessFields, staff,
                                           diff("deleted", staffWithLessFields.deleted, staff.deleted));
    compareRecursivelyFailsWithDifferences(staff, staffWithLessFields,
                                           diff("deleted", staff.deleted, staffWithLessFields.deleted));
  }

  // https://github.com/assertj/assertj/issues/2610
  static class A1 {
    final int a, b;

    A1(int a, int b) {
      this.a = a;
      this.b = b;
    }
  }
  static class A2 {
    final int a;

    A2(int a) {
      this.a = a;
    }
  }

  @Test
  public void should_fix_2610() {
    // GIVEN
    A1 actual = new A1(1, 2);
    A2 expected = new A2(2);
    recursiveComparisonConfiguration.compareOnlyFields("a");
    // WHEN/THEN
    ComparisonDifference difference = diff("a", actual.a, expected.a);
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @ParameterizedTest(name = "actual={0} / expected={1}")
  @MethodSource
  void should_fail_when_actual_is_a_container_whose_elements_differs_from_expected_on_compared_fields(Object actual,
                                                                                                      Object expected,
                                                                                                      ComparisonDifference difference) {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFields("name", "subject");
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  private static Stream<Arguments> should_fail_when_actual_is_a_container_whose_elements_differs_from_expected_on_compared_fields() {
    Student john1 = new Student("John", "math", 1);
    Student john2 = new Student("John", "math", 1);
    Student rohit = new Student("Rohit", "english", 2);
    Student rohyt = new Student("Rohyt", "english", 2);
    ComparisonDifference difference = diff("[1].name", "Rohit", "Rohyt");
    return Stream.of(arguments(list(john1, rohit), list(john2, rohyt), difference),
                     arguments(array(john1, rohit), array(john2, rohyt), difference),
                     arguments(set(john1, rohit), set(john2, rohyt), difference));

  }

  // #3129
  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource
  void should_fail_when_non_existent_fields_specified(Object actual, Object expected, String[] fieldNamesToCompare,
                                                      String unknownFields) {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFields(fieldNamesToCompare);
    // WHEN
    IllegalArgumentException iae = catchIllegalArgumentException(() -> assertThat(actual).usingRecursiveComparison()
                                                                                         .comparingOnlyFields(fieldNamesToCompare)
                                                                                         .isEqualTo(expected));
    // THEN
    then(iae).hasMessage("The following fields don't exist: " + unknownFields);
  }

  private static Stream<Arguments> should_fail_when_non_existent_fields_specified() {
    Person john = new Person("John");
    Person alice = new Person("Alice");
    Person jack = new Person("Jack");
    Person joan = new Person("Joan");
    john.neighbour = jack;
    alice.neighbour = joan;
    jack.neighbour = john;
    joan.neighbour = alice;

    FriendlyPerson sherlockHolmes = friend("Sherlock Holmes");
    FriendlyPerson drWatson = friend("Dr. John Watson");
    FriendlyPerson mollyHooper = friend("Molly Hooper");
    sherlockHolmes.friends.add(drWatson);
    sherlockHolmes.friends.add(mollyHooper);
    drWatson.friends.add(mollyHooper);
    drWatson.friends.add(sherlockHolmes);

    return Stream.of(arguments(john, alice, array("naame"), "{naame}"),
                     arguments(john, alice, array("name", "neighbour", "number"), "{number}"),
                     arguments(john, alice, array("neighbor"), "{neighbor}"),
                     arguments(john, alice, array("neighbour.neighbor.name"), "{neighbor in <neighbour.neighbor.name>}"),
                     // TODO for https://github.com/assertj/assertj/issues/3354
                     // arguments(sherlockHolmes, drWatson, array("friends.other"), "{other in <friends.other>}"),
                     arguments(john, alice, array("neighbour.neighbour.name", "neighbour.neighbour.number"),
                               "{number in <neighbour.neighbour.number>}"));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_lists_on_compared_fields() {
    // GIVEN
    Person john = new Person("John");
    Person alice = new Person("Alice");
    Person jack = new Person("Jack");
    Person joan = new Person("Joan");
    john.neighbour = jack;
    alice.neighbour = joan;
    jack.neighbour = john;
    joan.neighbour = alice;
    List<Person> actual = list(john, alice);
    List<Person> expected = list(jack, joan);

    recursiveComparisonConfiguration.compareOnlyFields("neighbour.neighbour.name");

    // WHEN/THEN
    ComparisonDifference difference1 = diff("[0].neighbour.neighbour.name", "John", "Jack");
    ComparisonDifference difference2 = diff("[1].neighbour.neighbour.name", "Alice", "Joan");
    compareRecursivelyFailsWithDifferences(actual, expected, difference1, difference2);
  }

  // #3129
  @Test
  void should_pass_when_fields_are_nested() {
    // GIVEN
    Person john = new Person("John");
    Person alice = new Person("Alice");
    Person jack = new Person("Jack");
    Person joan = new Person("Joan");
    john.neighbour = jack;
    alice.neighbour = joan;
    jack.neighbour = jack;
    joan.neighbour = jack;
    // WHEN/THEN
    then(john).usingRecursiveComparison()
              .comparingOnlyFields("neighbour.neighbour.name")
              .isEqualTo(alice);
  }

  @Test
  void should_pass_with_cycles() {
    // GIVEN
    Person john = new Person("John");
    Person alice = new Person("Alice");
    Person jack = new Person("Jack");
    Person joan = new Person("Joan");
    john.neighbour = jack;
    alice.neighbour = joan;
    jack.neighbour = jack;
    joan.neighbour = jack;
    // WHEN/THEN
    then(john).usingRecursiveComparison()
              .comparingOnlyFields("neighbour.neighbour.neighbour.neighbour")
              .isEqualTo(alice);
  }

  @Test
  void cannot_report_unknown_compared_fields_if_parent_object_is_null() {
    // GIVEN
    Person john = new Person("John");
    Person alice = new Person("Alice");
    // WHEN/THEN
    // badField is not detected as an unknown field since john.neighbour is null
    // neighbour fields are compared and match since they are both null
    then(john).usingRecursiveComparison()
              .comparingOnlyFields("neighbour.badField")
              .isEqualTo(alice);
  }

  static class Student {
    String name;
    String subject;
    int rollNo;

    Student(String name, String subject, int rollNo) {
      this.name = name;
      this.subject = subject;
      this.rollNo = rollNo;
    }

    @Override
    public String toString() {
      return String.format("Student[name=%s, subject=%s, rollNo=%s]", this.name, this.subject, this.rollNo);
    }
  }

  @Test
  void should_only_check_compared_fields_existence_at_the_root_level() {
    // GIVEN
    Collection<Name> names = newHashSet(new Name("john", "doe"), new Name("jane", "smith"));
    WithNames actual = new WithNames(newHashSet(names));
    WithNames expected = new WithNames(newHashSet(names));
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .comparingOnlyFields("names")
                .isEqualTo(expected);
  }

  static class WithNames {
    Collection<Name> names;

    public WithNames(Collection<Name> names) {
      this.names = names;
    }
  }

  static class Name {
    String first;
    String last;

    public Name(String first, String last) {
      this.first = first;
      this.last = last;
    }
  }

  // https://github.com/assertj/assertj/issues/3354
  @Test
  void checking_compared_fields_existence_should_skip_containers_in_field_location() {
    // GIVEN
    FriendlyPerson sherlock1 = new FriendlyPerson("Sherlock Holmes");
    sherlock1.friends.add(new FriendlyPerson("Dr. John Watson"));
    FriendlyPerson sherlock2 = new FriendlyPerson("Sherlock Holmes");
    sherlock2.friends.add(new FriendlyPerson("Dr. John Watson"));
    // WHEN/THEN
    then(sherlock1).usingRecursiveComparison()
                   .comparingOnlyFields("friends.name")
                   .isEqualTo(sherlock2);
  }
}
