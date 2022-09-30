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
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
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


  @BeforeAll
  static void setupTestData() {
    //GIVEN

    tom = new Person("Tom");
    tom.age = OptionalInt.of(60);
    tom.home.address.number = 777;

    jess = new Person("Jess");
    jess.age = OptionalInt.of(70);
    jess.home.address.number = 555;


    // same age (OptionalInt) and weight (OptionalDouble) with John
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

    // same age (OptionalInt) and weight (OptionalDouble) with Billie
    john = new Person("John");
    john.id = OptionalLong.of(1002);
    john.age = OptionalInt.of(25);
    john.weight = OptionalDouble.of(70.21);
    john.neighbour = tom;
    john.home.address.number = 10;

    // same neighbour with another Billie
    jill = new Person("Jill");
    jill.id = OptionalLong.of(1003);
    jill.age = OptionalInt.of(25);
    jill.weight = OptionalDouble.of( 60.22);
    jill.neighbour = jess;
    jill.home.address.number = 150;

  }



  @ParameterizedTest(name = "{3}: actual={0} / expected={1}, compared types: ")
  @MethodSource("passComparingFieldsOfTypes")
  void should_pass_when_comparing_only_fields_of_types(Object actual, Object expected, Class<?>[] typesToCompare,
                                                                       @SuppressWarnings("unused") String testDescription) {
    // WHEN/THEN
    assertThat(actual).usingRecursiveComparison()
                      .comparingOnlyFieldsOfTypes(typesToCompare)
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> passComparingFieldsOfTypes() {

    return Stream.of(arguments(billie, jill, new Class<?>[]{ OptionalInt.class }, "same \"age\""),
                     arguments(billie, john, new Class<?>[]{ OptionalInt.class, OptionalDouble.class }, "same \"age\" and \"weight\""),
                     arguments(billie, anotherBillie, new Class<?>[]{ String.class }, "same \"name\""),
                     arguments(anotherBillie, jill, new Class<?>[]{ Person.class }, "same \"neighbour\""));
  }

  @ParameterizedTest(name = "{3}: actual={0} / expected={1}, compared types: ")
  @MethodSource("failComparingTopLevelFields")
  void should_fail_when_actual_differs_from_expected_on_compared_fields_of_types(Object actual, Object expected, Class<?>[] typesToCompare,
                                                                 @SuppressWarnings("unused") String testDescription, ComparisonDifference[] differences) {
    // WHEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(typesToCompare);
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected,
                                                              differences);
  }

  private static Stream<Arguments> failComparingTopLevelFields() {
    return Stream.of(arguments(billie, john, new Class<?>[] { String.class }, "different \"name\"",
                               new ComparisonDifference[] { diff("name", billie.name, john.name) }),
                     arguments(billie, anotherBillie, new Class<?>[] { OptionalInt.class, OptionalDouble.class }, "different \"age\" and \"weight\"",
                               new ComparisonDifference[] { diff("age", billie.age, anotherBillie.age),
                                                            diff("weight", billie.weight, anotherBillie.weight)
                     }),
                     arguments(john, jill, new Class<?>[] { Person.class }, "different \"neighbour.name\", \"neighbour.age\" and \"neighbour.home.address.number\"",
                               new ComparisonDifference[] { diff("neighbour.age", john.neighbour.age, jill.neighbour.age),
                                                            diff("neighbour.home.address.number", john.neighbour.home.address.number, jill.neighbour.home.address.number),
                                                            diff("neighbour.name", john.neighbour.name, jill.neighbour.name)
                     }));
  }


  @Test
  void should_pass_when_combined_with_comparingOnlyFields() {
    // WHEN/THEN
    assertThat(billie).usingRecursiveComparison()
                    .comparingOnlyFieldsOfTypes(OptionalInt.class)
                    .comparingOnlyFields("weight")
                    .isEqualTo(john);
  }

  @Test
  void should_fail_when_combined_with_comparingOnlyFields() {
    // WHEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(OptionalInt.class);
    recursiveComparisonConfiguration.compareOnlyFields("weight");
    compareRecursivelyFailsAsExpected(billie, anotherBillie);

    ComparisonDifference ageDifference = diff("age", billie.age, anotherBillie.age);
    ComparisonDifference weightDifference = diff("weight", billie.weight, anotherBillie.weight);

    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(billie, anotherBillie, ageDifference, weightDifference);
  }

  @Test
  void should_pass_when_combined_with_ignoringFields() {
    // WHEN/THEN
    assertThat(john).usingRecursiveComparison()
                    .comparingOnlyFieldsOfTypes(String.class, OptionalInt.class)
                    .ignoringFields("name")
                    .isEqualTo(jill);
  }

  @Test
  void should_fail_when_combined_with_ignoringFields() {
    // WHEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class, OptionalLong.class);
    recursiveComparisonConfiguration.ignoreFields("id");
    compareRecursivelyFailsAsExpected(john, jill);

    ComparisonDifference idDifference = diff("name", john.name, jill.name);

    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(john, jill, idDifference);
  }

  @Test
  void should_pass_when_combined_with_ignoringFieldsMatchingRegexes() {
    // WHEN/THEN
    assertThat(john).usingRecursiveComparison()
                    .comparingOnlyFieldsOfTypes(String.class, OptionalInt.class)
                    .ignoringFieldsMatchingRegexes(".*ame")
                    .isEqualTo(jill);
  }

  @Test
  void should_fail_when_combined_with_ignoringFieldsMatchingRegexes() {
    // WHEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class, OptionalInt.class);
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes(".*ame");
    compareRecursivelyFailsAsExpected(john, jill);

    ComparisonDifference nameDifference = diff("name", john.name, jill.name);

    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(john, jill, nameDifference);
  }

  @Test
  void should_pass_when_combined_with_ignoringFieldsOfTypes() {
    // WHEN/THEN
    assertThat(john).usingRecursiveComparison()
                    .comparingOnlyFieldsOfTypes(String.class, OptionalInt.class)
                    .ignoringFieldsOfTypes(String.class)
                    .isEqualTo(jill);
  }

  @Test
  void should_fail_when_combined_with_ignoringFieldsOfTypes() {
    // WHEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(OptionalInt.class, OptionalDouble.class);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(OptionalInt.class);
    compareRecursivelyFailsAsExpected(billie, anotherBillie);

    ComparisonDifference weightDifference = diff("weight", billie.weight, anotherBillie.weight);

    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(billie, anotherBillie, weightDifference);
  }
}
