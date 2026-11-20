/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

import static example.data.Race.HOBBIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BiPredicate;

import org.assertj.core.api.recursive.comparison.ComparingSnakeOrCamelCaseFields;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonIntrospectionStrategy;
import org.junit.jupiter.api.Test;

import example.data.TolkienCharacter;

// Not run by the build (class name does not match *Test), some examples fail on purpose.
// Each nested class holds the model classes of one documentation section, the constructors
// the documentation omits are kept outside of the tagged regions.
class RecursiveComparisonExample {

  @Test
  void is_not_equal_to() {
    // tag::is_not_equal_to[]
    // equals not overridden in TolkienCharacter
    TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
    TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
    TolkienCharacter youngFrodo = new TolkienCharacter("Frodo", 22, HOBBIT);

    // Pass as equals compares object references
    assertThat(frodo).isNotEqualTo(frodoClone);

    // Fail as frodo and frodoClone are equals when doing a field by field comparison.
    assertThat(frodo).usingRecursiveComparison()
                     .isNotEqualTo(frodoClone);

    // Pass as one the age fields differ between frodo and youngFrodo.
    assertThat(frodo).usingRecursiveComparison()
                     .isNotEqualTo(youngFrodo);
    // end::is_not_equal_to[]
  }

  static class BasicUsage {

    // tag::basic_usage_classes[]
    public class Person {
      String name;
      double height;
      Home home = new Home();
      // end::basic_usage_classes[]

      Person(String name, double height) {
        this.name = name;
        this.height = height;
      }
      // tag::basic_usage_classes[]
    }

    public class Home {
      Address address = new Address();
      Date ownedSince;
    }

    public static class Address {
      int number;
      String street;
    }
    // end::basic_usage_classes[]

    @Test
    void basic_usage() {
      // tag::basic_usage[]
      Person sherlock = new Person("Sherlock", 1.80);
      sherlock.home.ownedSince = new Date(123);
      sherlock.home.address.street = "Baker Street";
      sherlock.home.address.number = 221;

      Person sherlock2 = new Person("Sherlock", 1.80);
      sherlock2.home.ownedSince = new Date(123);
      sherlock2.home.address.street = "Baker Street";
      sherlock2.home.address.number = 221;

      // assertion succeeds as the data of both objects are the same.
      assertThat(sherlock).usingRecursiveComparison()
                          .isEqualTo(sherlock2);

      // assertion fails as Person equals only compares references.
      assertThat(sherlock).isEqualTo(sherlock2);
      // end::basic_usage[]
    }

    @Test
    void ignoring_fields() {
      // tag::ignoring_fields[]
      Person sherlock = new Person("Sherlock", 1.80);
      sherlock.home.address.street = "Baker Street";
      sherlock.home.address.number = 221;

      // strangely moriarty and sherlock have the same height!
      Person moriarty = new Person("Moriarty", 1.80);
      moriarty.home.address.street = "Crime Street";
      moriarty.home.address.number = 221;

      // assertion succeeds as name and home.address.street fields are ignored in the comparison
      assertThat(sherlock).usingRecursiveComparison()
                          .ignoringFields("name", "home.address.street")
                          .isEqualTo(moriarty);

      // assertion succeeds as once a field is ignored, its subfields are too
      assertThat(sherlock).usingRecursiveComparison()
                          .ignoringFields("name", "home")
                          .isEqualTo(moriarty);

      // ignoring fields matching regexes: name and home match .*me
      assertThat(sherlock).usingRecursiveComparison()
                          .ignoringFieldsMatchingRegexes(".*me")
                          .isEqualTo(moriarty);

      // ignore height and address fields by type:
      Person tallSherlock = new Person("Sherlock", 2.10);
      tallSherlock.home.address.street = "Long Baker Street";
      tallSherlock.home.address.number = 222;
      assertThat(sherlock).usingRecursiveComparison()
                          .ignoringFieldsOfTypes(double.class, Address.class)
                          .isEqualTo(tallSherlock);

      // ignoring null fields example:
      sherlock.name = null;
      sherlock.home.address.street = null;
      assertThat(sherlock).usingRecursiveComparison()
                          .ignoringActualNullFields()
                          .isEqualTo(moriarty);
      // end::ignoring_fields[]
    }
  }

  static class StrictTypeChecking {

    // tag::strict_person_class[]
    public class Person {
      String name;
      double height;
      Person bestFriend;
      // end::strict_person_class[]

      Person(String name, double height) {
        this.name = name;
        this.height = height;
      }
      // tag::strict_person_class[]
    }
    // end::strict_person_class[]

    // tag::strict_dto_class[]
    public class PersonDTO {
      String name;
      double height;
      PersonDTO bestFriend;
      // end::strict_dto_class[]

      PersonDTO(String name, double height) {
        this.name = name;
        this.height = height;
      }
      // tag::strict_dto_class[]
    }
    // end::strict_dto_class[]

    // tag::strict_subclass_class[]
    public class Detective extends Person {
      boolean busy;
      // end::strict_subclass_class[]

      Detective(String name, double height) {
        super(name, height);
      }
      // tag::strict_subclass_class[]
    }
    // end::strict_subclass_class[]

    @Test
    void strict_type_checking() {
      // tag::strict_same_type[]
      Person sherlock = new Person("Sherlock", 1.80);
      sherlock.bestFriend = new Person("Watson", 1.70);

      Person sherlockClone = new Person("Sherlock", 1.80);
      sherlockClone.bestFriend = new Person("Watson", 1.70);

      // assertion succeeds as sherlock and sherlockClone have the same data and types
      assertThat(sherlock).usingRecursiveComparison()
                          .withStrictTypeChecking()
                          .isEqualTo(sherlockClone);

      // Let's now define a data structure similar to Person
      // end::strict_same_type[]

      // tag::strict_dto[]
      PersonDTO sherlockDto = new PersonDTO("Sherlock", 1.80);
      sherlockDto.bestFriend = new PersonDTO("Watson", 1.70);

      // assertion fails as Person and PersonDTO are not compatible even though they have the same data
      assertThat(sherlock).usingRecursiveComparison()
                          .withStrictTypeChecking()
                          .isEqualTo(sherlockDto);

      // Let's define a subclass of Person
      // end::strict_dto[]

      // tag::strict_subclass[]
      Detective detectiveSherlock = new Detective("Sherlock", 1.80);
      detectiveSherlock.bestFriend = new Person("Watson", 1.70);
      detectiveSherlock.busy = true;

      // assertion succeeds as Detective inherits from Person and
      // only Person's fields are included into the comparison.
      assertThat(sherlock).usingRecursiveComparison()
                          .withStrictTypeChecking()
                          .isEqualTo(detectiveSherlock);
      // end::strict_subclass[]
    }
  }

  static class OverriddenEquals {

    // tag::overridden_equals_classes[]
    public class Person {
      String name;
      double height;
      Home home = new Home();
      // end::overridden_equals_classes[]

      Person(String name, double height) {
        this.name = name;
        this.height = height;
      }
      // tag::overridden_equals_classes[]
    }

    public class Home {
      Address address = new Address();
    }

    public static class Address {
      int number;
      String street;

      // only compares number, ouch!
      @Override
      public boolean equals(final Object other) {
        if (!(other instanceof Address)) return false;
        Address castOther = (Address) other;
        return Objects.equals(number, castOther.number);
      }
    }
    // end::overridden_equals_classes[]

    @Test
    void overridden_equals() {
      // tag::overridden_equals[]
      Person sherlock = new Person("Sherlock", 1.80);
      sherlock.home.address.street = "Baker Street";
      sherlock.home.address.number = 221;

      Person sherlock2 = new Person("Sherlock", 1.80);
      sherlock2.home.address.street = "Butcher Street";
      sherlock2.home.address.number = 221;

      // assertion succeeds but that's not what we expected since the home.address.street fields differ
      // but the equals implementation in Address does not compare them.
      assertThat(sherlock).usingRecursiveComparison()
                          .usingOverriddenEquals()
                          .isEqualTo(sherlock2);

      // to avoid the previous issue, we force a recursive comparison on the Address type
      // now this assertion fails as expected since the home.address.street fields differ.
      assertThat(sherlock).usingRecursiveComparison()
                          .usingOverriddenEquals()
                          .ignoringOverriddenEqualsForTypes(Address.class)
                          .isEqualTo(sherlock2);
      // end::overridden_equals[]
    }
  }

  static class ExpectedNullFields {

    // tag::expected_null_fields_classes[]
    public class Person {
      String name;
      double height;
      Home home = new Home();
      // end::expected_null_fields_classes[]

      Person(String name, double height) {
        this.name = name;
        this.height = height;
      }
      // tag::expected_null_fields_classes[]
    }
    public class Home {
      Address address = new Address();
    }
    public static class Address {
      int number;
      String street;
    }
    // end::expected_null_fields_classes[]

    @Test
    void ignoring_expected_null_fields() {
      // tag::expected_null_fields[]
      Person sherlock = new Person("Sherlock", 1.80);
      sherlock.home.address.street = "Baker Street";
      sherlock.home.address.number = 221;

      Person noName = new Person(null, 1.80);
      noName.home.address.street = null;
      noName.home.address.number = 221;

      // assertion succeeds as name and home.address.street fields are ignored in the comparison
      assertThat(sherlock).usingRecursiveComparison()
                          .ignoringExpectedNullFields()
                          .isEqualTo(noName);

      // assertion fails as name and home.address.street fields are populated for sherlock but not for noName.
      assertThat(noName).usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(sherlock);
      // end::expected_null_fields[]
    }
  }

  static class ActualEmptyOptionalFields {

    // tag::actual_empty_optional_fields_classes[]
    public class Person {
      String name;
      OptionalInt age;
      OptionalLong id;
      OptionalDouble height;
      Home home = new Home();
      // end::actual_empty_optional_fields_classes[]

      Person(String name) {
        this.name = name;
      }
      // tag::actual_empty_optional_fields_classes[]
    }

    public class Home {
      String address;
      Optional<String> phone;
    }
    // end::actual_empty_optional_fields_classes[]

    @Test
    void ignoring_actual_empty_optional_fields() {
      // tag::actual_empty_optional_fields[]
      Person homerWithoutDetails = new Person("Homer Simpson");
      homerWithoutDetails.home.address = "742 Evergreen Terrace";
      homerWithoutDetails.home.phone = Optional.empty();
      homerWithoutDetails.age = OptionalInt.empty();
      homerWithoutDetails.id = OptionalLong.empty();
      homerWithoutDetails.height = OptionalDouble.empty();

      Person homerWithDetails = new Person("Homer Simpson");
      homerWithDetails.home.address = "742 Evergreen Terrace";
      homerWithDetails.home.phone = Optional.of("(939) 555-0113");
      homerWithDetails.age = OptionalInt.of(39);
      homerWithDetails.id = OptionalLong.of(123456);
      homerWithDetails.height = OptionalDouble.of(1.83);

      // assertion succeeds as phone is ignored in the comparison
      assertThat(homerWithoutDetails).usingRecursiveComparison()
                                     .ignoringActualEmptyOptionalFields()
                                     .isEqualTo(homerWithDetails);

      // assertion fails as phone, age, id and height are not ignored and are populated for homerWithDetails but not for homerWithoutDetails.
      assertThat(homerWithDetails).usingRecursiveComparison()
                                  .ignoringActualEmptyOptionalFields()
                                  .isEqualTo(homerWithoutDetails);
      // end::actual_empty_optional_fields[]
    }
  }

  static class ComparatorsAndErrorMessages {

    // tag::tolkien_character_class[]
    public class TolkienCharacter {
      String name;
      double height;
      // end::tolkien_character_class[]

      TolkienCharacter(String name, double height) {
        this.name = name;
        this.height = height;
      }

      @Override
      public String toString() {
        return String.format("TolkienCharacter [name=%s, height=%s]", name, height);
      }
      // tag::tolkien_character_class[]
    }
    // end::tolkien_character_class[]

    @Test
    void comparators() {
      // tag::comparators[]
      TolkienCharacter frodo = new TolkienCharacter("Frodo", 1.2);
      TolkienCharacter tallerFrodo = new TolkienCharacter("Frodo", 1.3);
      TolkienCharacter reallyTallFrodo = new TolkienCharacter("Frodo", 1.9);

      BiPredicate<Double, Double> closeEnough = (d1, d2) -> Math.abs(d1 - d2) <= 0.5;
      // same comparison expressed with a Comparator:
      // Comparator<Double> closeEnough = (d1, d2) -> Math.abs(d1 - d2) <= 0.5 ? 0 : 1;

      // assertion succeeds
      assertThat(frodo).usingRecursiveComparison()
                       .withEqualsForFields(closeEnough, "height")
                       .isEqualTo(tallerFrodo);

      assertThat(frodo).usingRecursiveComparison()
                       .withEqualsForType(closeEnough, Double.class)
                       .isEqualTo(tallerFrodo);

      // assertions fail
      assertThat(frodo).usingRecursiveComparison()
                       .withEqualsForFields(closeEnough, "height")
                       .isEqualTo(reallyTallFrodo);

      assertThat(frodo).usingRecursiveComparison()
                       .withEqualsForType(closeEnough, Double.class)
                       .isEqualTo(reallyTallFrodo);
      // end::comparators[]
    }

    @Test
    void error_message_for_fields() {
      // tag::error_message_for_fields[]
      TolkienCharacter frodo = new TolkienCharacter("Frodo", 1.2);
      TolkienCharacter tallerFrodo = new TolkienCharacter("Frodon", 1.4);

      String message = "The field 'height' differ.";

      // assertion fails
      assertThat(frodo).usingRecursiveComparison()
                       .withErrorMessageForFields(message, "height")
                       .isEqualTo(tallerFrodo);
      // end::error_message_for_fields[]
    }

    @Test
    void error_message_for_type() {
      TolkienCharacter frodo = new TolkienCharacter("Frodo", 1.2);
      TolkienCharacter tallerFrodo = new TolkienCharacter("Frodon", 1.4);

      // tag::error_message_for_type[]
      String message = "Double field differ";

      // assertion fails
      assertThat(frodo).usingRecursiveComparison()
                       .withErrorMessageForType(message, Double.class)
                       .isEqualTo(tallerFrodo);
      // end::error_message_for_type[]
    }
  }

  static class IterableAssertions {

    // tag::iterable_classes[]
    public class Person {
      String name;
      boolean hasPhd;
      // end::iterable_classes[]

      Person(String name, boolean hasPhd) {
        this.name = name;
        this.hasPhd = hasPhd;
      }

      void setName(String name) {
        this.name = name;
      }
      // tag::iterable_classes[]
    }

    public class Doctor {
      String name;
      boolean hasPhd;
      // end::iterable_classes[]

      Doctor(String name, boolean hasPhd) {
        this.name = name;
        this.hasPhd = hasPhd;
      }
      // tag::iterable_classes[]
    }
    // end::iterable_classes[]

    @Test
    void recursive_field_by_field_element_comparator() {
      // tag::iterable[]
      Doctor drSheldon = new Doctor("Sheldon Cooper", true);
      Doctor drLeonard = new Doctor("Leonard Hofstadter", true);
      Doctor drRaj = new Doctor("Raj Koothrappali", true);

      Person sheldon = new Person("Sheldon Cooper", false);
      Person leonard = new Person("Leonard Hofstadter", false);
      Person raj = new Person("Raj Koothrappali", false);
      Person howard = new Person("Howard Wolowitz", false);

      List<Object> doctors = list(drSheldon, drLeonard, drRaj);
      List<Person> people = list(sheldon, leonard, raj);

      RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                       .withIgnoredFields("hasPhd")
                                                                                       .build();

      // assertion succeeds as both lists contains equivalent items in order.
      assertThat(doctors).usingRecursiveFieldByFieldElementComparator(configuration)
                         .contains(sheldon);

      // assertion fails because leonard names are different.
      leonard.setName("Leonard Ofstater");
      assertThat(doctors).usingRecursiveFieldByFieldElementComparator(configuration)
                         .contains(leonard);

      // assertion fails because howard is missing and leonard is not expected.
      people = list(howard, sheldon, raj);
      assertThat(doctors).usingRecursiveFieldByFieldElementComparator(configuration)
                         .contains(howard);
      // end::iterable[]
    }
  }

  static class IntrospectionStrategy {

    @Test
    void introspection_strategy() {
      // tag::introspection_strategy[]
      Author martinFowler = new Author("Martin", "Fowler", 58, "https://www.thoughtworks.com/profiles/leaders/martin-fowler");
      Book refactoring = new Book("Refactoring", martinFowler);
      AuthorDto martinFowlerDto = new AuthorDto("Martin", "Fowler", 58, "https://www.thoughtworks.com/profiles/leaders/martin-fowler");
      BookDto refactoringDto = new BookDto("Refactoring", martinFowlerDto);

      RecursiveComparisonIntrospectionStrategy comparingSnakeOrCamelCaseFields = new ComparingSnakeOrCamelCaseFields();

      // both assertions succeed
      assertThat(refactoring).usingRecursiveComparison()
                             .withIntrospectionStrategy(comparingSnakeOrCamelCaseFields)
                             .isEqualTo(refactoringDto);
      assertThat(refactoringDto).usingRecursiveComparison()
                                .withIntrospectionStrategy(comparingSnakeOrCamelCaseFields)
                                .isEqualTo(refactoring);
      // end::introspection_strategy[]
    }

    // tag::introspection_strategy_classes[]
    static class Author {
      String firstName;
      String lastName;
      int age;
      String profileURL;

      Author(String firstName, String lastName, int age, String profileUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.profileURL = profileUrl;
      }
    }

    static class Book {
      String title;
      Author mainAuthor;

      Book(String title, Author author) {
        this.title = title;
        this.mainAuthor = author;
      }
    }
    static class AuthorDto {
      String first_name;
      String last_name;
      int _age;
      String profile_url;

      AuthorDto(String firstName, String lastName, int age, String profileUrl) {
        this.first_name = firstName;
        this.last_name = lastName;
        this._age = age;
        this.profile_url = profileUrl;
      }
    }

    static class BookDto {
      String title;
      AuthorDto main_author;

      BookDto(String title, AuthorDto author) {
        this.title = title;
        this.main_author = author;
      }
    }
    // end::introspection_strategy_classes[]
  }
}
// @formatter:on
