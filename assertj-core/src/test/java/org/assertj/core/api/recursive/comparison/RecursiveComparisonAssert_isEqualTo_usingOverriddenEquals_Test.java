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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.AlwaysDifferentPerson;
import org.assertj.core.internal.objects.data.AlwaysEqualPerson;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonAssert_isEqualTo_usingOverriddenEquals_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest implements PersonData {

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource
  void should_fail_when_using_overridden_equals(Object actual, Object expected, String testDescription) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .usingOverriddenEquals()
                                                                                 .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining("- overridden equals methods were used in the comparison");
  }

  private static Stream<Arguments> should_fail_when_using_overridden_equals() {
    Person person1 = new AlwaysDifferentPerson();
    person1.neighbour = new Person("John");
    Person person2 = new AlwaysDifferentPerson();
    person2.neighbour = new Person("John");

    Person person3 = new Person();
    person3.neighbour = new AlwaysDifferentPerson();
    person3.neighbour.name = "John";
    Person person4 = new Person();
    person4.neighbour = new AlwaysDifferentPerson();
    person4.neighbour.name = "John";

    return Stream.of(arguments(person1, person2, "root Person is AlwaysDifferentPerson"),
                     arguments(person3, person4, "neighbour Person is AlwaysDifferentPerson"));
  }

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource
  void should_pass_when_using_overridden_equals(Object actual, Object expected, String testDescription) {
    then(actual).usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_when_using_overridden_equals() {
    Person person1 = new AlwaysEqualPerson();
    person1.neighbour = new Person("John");
    Person person2 = new AlwaysEqualPerson();
    person2.neighbour = new Person("Jack");

    Person person3 = new Person();
    person3.neighbour = new AlwaysEqualPerson();
    person3.neighbour.name = "John";
    Person person4 = new Person();
    person4.neighbour = new AlwaysEqualPerson();
    person4.neighbour.name = "Jack";

    return Stream.of(arguments(person1, person2, "root Person is AlwaysEqualPerson"),
                     arguments(person3, person4, "neighbour Person is AlwaysEqualPerson"));
  }

  static class PersonWithOverriddenEquals {
    String name;
    String color;
    Pet pet;

    public PersonWithOverriddenEquals(String name, String color, Pet pet) {
      this.name = name; // only name is used in equals
      this.color = color;
      this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
      PersonWithOverriddenEquals person = (PersonWithOverriddenEquals) o;
      return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, color);
    }

    @Override
    public String toString() {
      return String.format("Person [name=%s, color=%s]", name, color);
    }
  }

  public static class Pet {
    String name;
    String type; // only type is used in equals

    public Pet(String name, String type) {
      this.name = name;
      this.type = type;
    }

    @Override
    public boolean equals(Object o) {
      Pet pet = (Pet) o;
      return type.equals(pet.type);
    }

    @Override
    public int hashCode() {
      return Objects.hash(type);
    }
  }

  static class PersonWrapper {
    PersonWithOverriddenEquals person;

    public PersonWrapper(PersonWithOverriddenEquals person) {
      this.person = person;
    }

  }

  @Test
  void should_pass_when_comparison_using_overriden_equals_on_root_objects() {
    // GIVEN
    PersonWithOverriddenEquals person1 = new PersonWithOverriddenEquals("John", "green", new Pet("Ducky", "Duck"));
    PersonWithOverriddenEquals person2 = new PersonWithOverriddenEquals("John", "blue", new Pet("Mia", "Duck"));
    // WHEN/THEN
    then(person1).usingRecursiveComparison()
                 .usingOverriddenEquals()
                 .isEqualTo(person2);
  }

  @Test
  void should_pass_when_comparison_using_overriden_equals_on_fields() {
    // GIVEN
    Optional<PersonWithOverriddenEquals> person1 = Optional.of(new PersonWithOverriddenEquals("John", "green",
                                                                                              new Pet("Ducky", "Duck")));
    Optional<PersonWithOverriddenEquals> person2 = Optional.of(new PersonWithOverriddenEquals("John", "green",
                                                                                              new Pet("Mia", "Duck")));
    // WHEN/THEN
    then(person1).usingRecursiveComparison()
                 .usingOverriddenEquals()
                 .isEqualTo(person2);
  }

  @Test
  void should_pass_when_comparison_using_overriden_equals_on_person_wrapper() {
    // GIVEN
    PersonWrapper person1 = new PersonWrapper(new PersonWithOverriddenEquals("John", "green", new Pet("Ducky", "Duck")));
    PersonWrapper person2 = new PersonWrapper(new PersonWithOverriddenEquals("John", "green", new Pet("Mia", "Duck")));
    // WHEN/THEN
    then(person1).usingRecursiveComparison()
                 .usingOverriddenEquals()
                 .isEqualTo(person2);
  }
}
