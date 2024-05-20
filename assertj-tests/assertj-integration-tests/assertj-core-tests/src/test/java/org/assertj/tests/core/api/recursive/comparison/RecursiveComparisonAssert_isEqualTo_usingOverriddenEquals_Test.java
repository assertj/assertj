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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.tests.core.api.recursive.data.AlwaysDifferentPerson;
import org.assertj.tests.core.api.recursive.data.AlwaysEqualPerson;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonAssert_isEqualTo_usingOverriddenEquals_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest implements PersonData {

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource
  void should_pass_when_using_overridden_equals(Object actual, Object expected, String testDescription) {
    then(actual).usingRecursiveComparison()
                .usingOverriddenEquals()
                .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_when_using_overridden_equals() {
    Person neighbourJohn = new Person();
    neighbourJohn.neighbour = new AlwaysEqualPerson("John");
    Person neighbourJack = new Person();
    neighbourJack.neighbour = new AlwaysEqualPerson("Jack");

    WithObject withPerson1 = new WithObject(new PersonComparedByName("John", "green", new Pet("Ducky", "Duck")));
    WithObject withPerson2 = new WithObject(new PersonComparedByName("John", "blue", new Pet("Mia", "Duck")));

    NeverEquals neverEquals1 = new NeverEquals(new PersonComparedByName("John", "green", null));
    NeverEquals neverEquals2 = new NeverEquals(new PersonComparedByName("John", "red", new Pet("Mia", "Duck")));

    return Stream.of(arguments(withPerson1, withPerson2,
                               "different pets and colors but equals ignore them"),
                     arguments(list(withPerson1), list(withPerson2),
                               "list: different pets and colors but equals ignore them"),
                     arguments(array(withPerson1), array(withPerson2),
                               "array: different pets and colors but equals ignore them"),
                     arguments(newHashMap("person", withPerson1), newHashMap("person", withPerson2),
                               "maps: different pets and colors but equals ignore them"),
                     arguments(Optional.of(withPerson1), Optional.of(withPerson2),
                               "Optional: different pets and colors but equals ignore them"),
                     arguments(neighbourJohn, neighbourJack,
                               "neighbour type equals is always true"),
                     arguments(neverEquals1, neverEquals2,
                               "root objects are never compared with equals, their fields are"));
  }

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource
  void should_fail_when_using_overridden_equals(Object actual, Object expected, String testDescription) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .usingOverriddenEquals()
                                                                                 .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining("- equals methods were used in the comparison");
  }

  private static Stream<Arguments> should_fail_when_using_overridden_equals() {
    Person neighbourJohn = new Person();
    neighbourJohn.neighbour = new AlwaysDifferentPerson();
    neighbourJohn.neighbour.name = "John";
    Person differentNeighbourJohn = new Person();
    differentNeighbourJohn.neighbour = new AlwaysDifferentPerson();
    differentNeighbourJohn.neighbour.name = "John";
    return Stream.of(arguments(neighbourJohn, differentNeighbourJohn,
                               "neighbour type is AlwaysDifferentPerson"),
                     arguments(list(neighbourJohn), list(differentNeighbourJohn),
                               "list: neighbour type is AlwaysDifferentPerson"),
                     arguments(array(neighbourJohn), array(differentNeighbourJohn),
                               "array: neighbour type is AlwaysDifferentPerson"),
                     arguments(newHashMap("person", neighbourJohn), newHashMap("person", differentNeighbourJohn),
                               "maps: neighbour type is AlwaysDifferentPerson"),
                     arguments(Optional.of(neighbourJohn), Optional.of(differentNeighbourJohn),
                               "Optional: neighbour type is AlwaysDifferentPerson"));
  }

  static class PersonComparedByName {
    String name;
    String color;
    Pet pet;

    public PersonComparedByName(String name, String color, Pet pet) {
      this.name = name; // only name is used in equals
      this.color = color;
      this.pet = pet;
    }

    @Override
    public boolean equals(Object o) {
      PersonComparedByName person = (PersonComparedByName) o;
      return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, color);
    }

    @Override
    public String toString() {
      return format("Person [name=%s, color=%s]", name, color);
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

  @Test
  void should_use_equals_on_compared_field_only() {
    // GIVEN
    WithObject actual = new WithObject(new A("abc", new NeverEquals("never"), new AlwaysEquals("always")));
    WithObject expected = new WithObject(new A("abc", new NeverEquals("never"), new AlwaysEquals("always")));
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .usingOverriddenEquals()
                .comparingOnlyFields("group.name", "group.neverEquals.name")
                .isEqualTo(expected);
  }

  @Test
  void should_fail_since_the_compared_field_equals_returns_false_even_if_the_outer_field_equals_returns_true() {
    // GIVEN
    WithObject actual = new WithObject(new A("abc", new NeverEquals("never"), new AlwaysEquals("always")));
    WithObject expected = new WithObject(new A("abc", new NeverEquals("never"), new AlwaysEquals("always")));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .usingOverriddenEquals()
                                                                                 .comparingOnlyFields("group.name",
                                                                                                      "group.neverEquals")
                                                                                 .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining("- equals methods were used in the comparison");
  }

  private static class A {
    private final String name;
    private final NeverEquals neverEquals;
    private final AlwaysEquals alwaysEquals;

    public A(String name, NeverEquals neverEquals, AlwaysEquals alwaysEquals) {
      this.name = name;
      this.neverEquals = neverEquals;
      this.alwaysEquals = alwaysEquals;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof A)) return false;
      A a = (A) o;
      return Objects.equals(name, a.name)
             && Objects.equals(neverEquals, a.neverEquals)
             && Objects.equals(alwaysEquals, a.alwaysEquals);
    }

    @Override
    public String toString() {
      return format("A[name=%s, neverEquals=%s, alwaysEquals=%s]", this.name, this.neverEquals, this.alwaysEquals);
    }
  }

  private static class NeverEquals {
    final Object name;

    public NeverEquals(Object name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      return false;
    }

    @Override
    public String toString() {
      return format("NeverEquals[name=%s]", this.name);
    }
  }

  private static class AlwaysEquals {
    final Object name;

    public AlwaysEquals(Object name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      return true;
    }

    @Override
    public String toString() {
      return format("AlwaysEquals[name=%s]", this.name);
    }
  }
}
