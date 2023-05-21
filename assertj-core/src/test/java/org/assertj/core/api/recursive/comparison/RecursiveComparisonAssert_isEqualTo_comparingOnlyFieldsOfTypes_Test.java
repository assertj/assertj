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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.Home;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_comparingOnlyFieldsOfTypes_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  static Person billie;
  static Person anotherBillie;
  static Person john;
  static Person jill;
  static Person tom;
  static Person jess;
  static Person tim;

  @BeforeAll
  static void setupTestData() {
    tom = new Person("Tom");
    tom.age = OptionalInt.of(60);
    tom.home.address.number = 777;

    jess = new Person("Jess");
    jess.age = OptionalInt.of(70);
    jess.home.address.number = 555;

    // same age (OptionalInt) and weight (OptionalDouble) as John
    billie = new Person("Billie");
    billie.id = OptionalLong.of(1000);
    billie.age = OptionalInt.of(25);
    billie.weight = OptionalDouble.of(70.21);

    // same neighbour with another Jill
    anotherBillie = new Person("Billie");
    anotherBillie.id = OptionalLong.of(1001);
    anotherBillie.age = OptionalInt.of(35);
    anotherBillie.weight = OptionalDouble.of(80.21);
    anotherBillie.neighbour = jess;

    // same age (OptionalInt) and weight (OptionalDouble) as Billie
    john = new Person("John");
    john.id = OptionalLong.of(1002);
    john.age = OptionalInt.of(billie.age.getAsInt());
    john.weight = OptionalDouble.of(billie.weight.getAsDouble());
    john.neighbour = tom;
    john.home.address.number = 10;

    // same neighbour with another Billie
    jill = new Person("Jill");
    jill.id = OptionalLong.of(1003);
    jill.age = OptionalInt.of(billie.age.getAsInt());
    jill.weight = OptionalDouble.of(60.22);
    jill.neighbour = jess;
    jill.home.address.number = 150;

    // tom and tim share the same home
    tim = new Person("Tim");
    tim.age = OptionalInt.of(50);
    tim.home.address.number = 777;

  }

  @ParameterizedTest(name = "{3}: actual={0} / expected={1}, compared types: ")
  @MethodSource("passComparingFieldsOfTypes")
  void should_pass_when_comparing_only_fields_of_types(Object actual, Object expected, Class<?>[] typesToCompare,
                                                       @SuppressWarnings("unused") String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .comparingOnlyFieldsOfTypes(typesToCompare)
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> passComparingFieldsOfTypes() {
    return Stream.of(arguments(billie, jill, types(OptionalInt.class), "same age"),
                     arguments(billie, john, types(OptionalInt.class, OptionalDouble.class), "same age and weight"),
                     arguments(billie, anotherBillie, types(String.class), "same name"),
                     arguments(tim, tom, types(Home.class), "same home"),
                     arguments(tim, tom, types(Home.class), "same home"),
                     arguments(anotherBillie, jill, types(Person.class), "same neighbour"));
  }

  private static Class<?>[] types(Class<?>... classes) {
    return classes;
  }

  @ParameterizedTest(name = "{3}: actual={0} / expected={1}, compared types: ")
  @MethodSource("failComparingTopLevelFields")
  void should_fail_when_actual_differs_from_expected_on_compared_fields_of_types(Object actual, Object expected,
                                                                                 Class<?>[] typesToCompare,
                                                                                 @SuppressWarnings("unused") String testDescription,
                                                                                 ComparisonDifference[] differences) {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(typesToCompare);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, differences);
  }

  private static Stream<Arguments> failComparingTopLevelFields() {
    return Stream.of(arguments(billie, john, types(String.class), "different name", array(diff("name", billie.name, john.name))),
                     arguments(billie, anotherBillie, types(OptionalInt.class, OptionalDouble.class), "different age and weight",
                               array(diff("age", billie.age, anotherBillie.age),
                                     diff("weight", billie.weight, anotherBillie.weight))),
                     arguments(john, jill, types(Person.class),
                               "different neighbour.name, neighbour.age and neighbour.home.address.number",
                               array(diff("neighbour.age", john.neighbour.age, jill.neighbour.age),
                                     diff("neighbour.home.address.number", john.neighbour.home.address.number,
                                          jill.neighbour.home.address.number),
                                     diff("neighbour.name", john.neighbour.name, jill.neighbour.name))));
  }

  @Test
  void should_pass_when_combined_with_comparingOnlyFields() {
    assertThat(billie).usingRecursiveComparison()
                      .comparingOnlyFieldsOfTypes(OptionalInt.class) // age field
                      .comparingOnlyFields("weight")
                      .isEqualTo(john);
  }

  @Test
  void should_fail_when_combined_with_comparingOnlyFields() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(OptionalInt.class);
    recursiveComparisonConfiguration.compareOnlyFields("weight");
    // WHEN
    compareRecursivelyFailsAsExpected(billie, anotherBillie);
    // THEN
    ComparisonDifference ageDifference = diff("age", billie.age, anotherBillie.age);
    ComparisonDifference weightDifference = diff("weight", billie.weight, anotherBillie.weight);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(billie, anotherBillie, ageDifference, weightDifference);
  }

  @Test
  void should_pass_when_combined_with_ignoringFields() {
    // john and jill have the same age, that's the only field they have in common
    assertThat(john).usingRecursiveComparison()
                    .comparingOnlyFieldsOfTypes(String.class, OptionalInt.class) // name and age fields
                    .ignoringFields("name")
                    .isEqualTo(jill);
  }

  @Test
  void should_fail_when_combined_with_ignoringFields() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class, OptionalLong.class); // name and id fields
    recursiveComparisonConfiguration.ignoreFields("id");
    // WHEN
    compareRecursivelyFailsAsExpected(john, jill);
    // THEN
    ComparisonDifference idDifference = diff("name", john.name, jill.name);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(john, jill, idDifference);
  }

  @Test
  void should_pass_when_combined_with_ignoringFieldsMatchingRegexes() {
    assertThat(john).usingRecursiveComparison()
                    .comparingOnlyFieldsOfTypes(String.class, OptionalInt.class) // name and age fields
                    .ignoringFieldsMatchingRegexes(".*ame")
                    .isEqualTo(jill);
  }

  @Test
  void should_fail_when_combined_with_ignoringFieldsMatchingRegexes() {
    // GIVEN
    // name, age and weight fields
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class, OptionalInt.class, OptionalDouble.class);
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("w..ght");
    // WHEN
    compareRecursivelyFailsAsExpected(john, jill);
    // THEN
    ComparisonDifference nameDifference = diff("name", john.name, jill.name);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(john, jill, nameDifference);
  }

  @Test
  void should_pass_when_combined_with_ignoringFieldsOfTypes() {
    // john and jill have the same age, that's the only field they have in common
    assertThat(john).usingRecursiveComparison()
                    .comparingOnlyFieldsOfTypes(String.class, OptionalInt.class) // name and age fields
                    .ignoringFieldsOfTypes(String.class)
                    .isEqualTo(jill);
  }

  @Test
  void should_fail_when_combined_with_ignoringFieldsOfTypes() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(OptionalInt.class, OptionalDouble.class);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(OptionalInt.class);
    // WHEN
    compareRecursivelyFailsAsExpected(billie, anotherBillie);
    // THEN
    ComparisonDifference weightDifference = diff("weight", billie.weight, anotherBillie.weight);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(billie, anotherBillie, weightDifference);
  }
}
