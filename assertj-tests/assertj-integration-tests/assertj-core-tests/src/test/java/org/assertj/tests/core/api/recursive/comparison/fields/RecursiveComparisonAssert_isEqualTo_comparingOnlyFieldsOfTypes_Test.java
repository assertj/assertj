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
package org.assertj.tests.core.api.recursive.comparison.fields;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.tests.core.api.recursive.data.Giant;
import org.assertj.tests.core.api.recursive.data.Home;
import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.WithObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_comparingOnlyFieldsOfTypes_Test
    extends WithComparingFieldsIntrospectionStrategyBaseTest {

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
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .comparingOnlyFieldsOfTypes(typesToCompare)
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> passComparingFieldsOfTypes() {
    return Stream.of(arguments(billie, jill, types(OptionalInt.class), "same age"),
                     arguments(billie, john, types(OptionalInt.class, OptionalDouble.class), "same age and weight"),
                     arguments(billie, anotherBillie, types(String.class), "same name"),
                     arguments(tim, tom, types(Home.class), "same home"),
                     arguments(tim, tom, types(Home.class), "same home"));
  }

  private static Class<?>[] types(Class<?>... classes) {
    return classes;
  }

  @ParameterizedTest(name = "{3}: actual={0} / expected={1}, compared types: ")
  @MethodSource
  void should_fail_when_actual_differs_from_expected_on_compared_fields_of_types(Object actual, Object expected,
                                                                                 Class<?>[] typesToCompare,
                                                                                 @SuppressWarnings("unused") String testDescription,
                                                                                 ComparisonDifference[] differences) {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(typesToCompare);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, differences);
  }

  private static Stream<Arguments> should_fail_when_actual_differs_from_expected_on_compared_fields_of_types() {
    return Stream.of(arguments(billie, john, types(String.class), "different name",
                               array(javaTypeDiff("name", billie.name, john.name))),
                     arguments(billie, anotherBillie, types(OptionalInt.class, OptionalDouble.class), "different age and weight",
                               array(javaTypeDiff("age", billie.age, anotherBillie.age),
                                     javaTypeDiff("weight", billie.weight, anotherBillie.weight))));
  }

  @Test
  void should_pass_when_combined_with_comparingOnlyFields() {
    assertThat(billie).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .comparingOnlyFieldsOfTypes(OptionalInt.class) // age field
                      .comparingOnlyFields("weight")
                      .isEqualTo(john);
  }

  @Test
  void should_fail_when_combined_with_comparingOnlyFields() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(OptionalInt.class);
    recursiveComparisonConfiguration.compareOnlyFields("weight");
    // WHEN/THEN
    ComparisonDifference ageDifference = javaTypeDiff("age", billie.age, anotherBillie.age);
    ComparisonDifference weightDifference = javaTypeDiff("weight", billie.weight, anotherBillie.weight);
    compareRecursivelyFailsWithDifferences(billie, anotherBillie, ageDifference, weightDifference);
  }

  @Test
  void should_pass_when_combined_with_ignoringFields() {
    // john and jill have the same age, that's the only field they have in common
    assertThat(john).usingRecursiveComparison(recursiveComparisonConfiguration)
                    .comparingOnlyFieldsOfTypes(String.class, OptionalInt.class) // name and age fields
                    .ignoringFieldsMatchingRegexes("name", "neighbour")
                    .isEqualTo(jill);
  }

  @Test
  void should_fail_when_combined_with_ignoringFields() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class, OptionalLong.class); // name and id fields
    recursiveComparisonConfiguration.ignoreFields("id", "neighbour");
    // WHEN/THEN
    ComparisonDifference idDifference = javaTypeDiff("name", john.name, jill.name);
    compareRecursivelyFailsWithDifferences(john, jill, idDifference);
  }

  @Test
  void should_pass_when_combined_with_ignoringFieldsMatchingRegexes() {
    assertThat(john).usingRecursiveComparison(recursiveComparisonConfiguration)
                    .comparingOnlyFieldsOfTypes(String.class, OptionalInt.class) // name and age fields
                    .ignoringFieldsMatchingRegexes(".*ame", ".*age")
                    .isEqualTo(jill);
  }

  @Test
  void should_fail_when_combined_with_ignoringFieldsMatchingRegexes() {
    // GIVEN
    // name, age and weight fields
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class, OptionalInt.class, OptionalDouble.class);
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("w..ght", "neighbour");
    // WHEN/THEN
    ComparisonDifference nameDifference = javaTypeDiff("name", john.name, jill.name);
    compareRecursivelyFailsWithDifferences(john, jill, nameDifference);
  }

  @Test
  void should_pass_when_combined_with_ignoringFieldsOfTypes() {
    // john and jill have the same age, that's the only field they have in common
    assertThat(john).usingRecursiveComparison(recursiveComparisonConfiguration)
                    .comparingOnlyFieldsOfTypes(String.class, OptionalInt.class) // name and age fields
                    .ignoringFieldsOfTypes(String.class)
                    .ignoringFields("neighbour")
                    .isEqualTo(jill);
  }

  @Test
  void should_fail_when_combined_with_ignoringFieldsOfTypes() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(OptionalInt.class, OptionalDouble.class);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(OptionalInt.class);
    // WHEN/THEN
    ComparisonDifference weightDifference = javaTypeDiff("weight", billie.weight, anotherBillie.weight);
    compareRecursivelyFailsWithDifferences(billie, anotherBillie, weightDifference);
  }

  @Test
  void succeeding_javadoc_example_should_pass() {
    // GIVEN
    Person sherlock = new Person("Sherlock");
    sherlock.home.address.number = 221;
    Person moriarty = new Person("Moriarty");
    moriarty.home.address.number = 221;
    // WHEN/THEN
    then(sherlock).usingRecursiveComparison(recursiveComparisonConfiguration)
                  .comparingOnlyFieldsOfTypes(Double.class, Integer.class)
                  .isEqualTo(moriarty);
  }

  @Test
  void failing_javadoc_example_should_fail() {
    // GIVEN
    Person sherlock = new Person("Sherlock");
    sherlock.home.address.number = 221;
    Person moriarty = new Person("Moriarty");
    moriarty.home.address.number = 222;
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(Home.class);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(sherlock, moriarty, javaTypeDiff("home.address.number", 221, 222));
  }

  @Test
  void should_pass_when_actual_compared_fields_are_specified_and_expected_has_extra_fields() {
    // GIVEN
    var actual = WithObject.of(new Person("joe"));
    var expected = WithObject.of(new Giant("joe", 3.0));
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .comparingOnlyFieldsOfTypes(String.class)
                .isEqualTo(expected);
  }

  @Test
  void should_compare_fields_of_specified_compared_types_even_if_parent_field_is_not_of_any_compared_types() {
    // GIVEN
    Dog lassie = new Dog(new Breed("Collie"));
    Dog snoopy = new Dog(new Breed("Beagle"));
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class);
    // WHEN/THEN
    ComparisonDifference weightDifference = javaTypeDiff("breed.name", lassie.breed.name, snoopy.breed.name);
    compareRecursivelyFailsWithDifferences(lassie, snoopy, weightDifference);
  }

  @Test
  void should_treat_primitive_types_as_their_wrapper_types() {
    // GIVEN
    Person actual = new Person("Sherlock");
    actual.home.address.number = 221;
    Person expected = new Person("Moriarty");
    expected.home.address.number = 222;
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(int.class);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, javaTypeDiff("home.address.number", 221, 222));
  }

  @Test
  void should_pass_when_no_field_has_one_of_the_compared_types_even_if_other_fields_differ() {
    assertThat(billie).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .comparingOnlyFieldsOfTypes(Boolean.class)
                      .isEqualTo(john);
  }

  @Test
  void should_fail_when_actual_field_is_null_and_expected_field_is_of_compared_type() {
    // GIVEN
    var actual = WithObject.of(null);
    var expected = WithObject.of("foo");
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, diff("value", null, "foo"));
  }

  @Test
  void should_fail_when_expected_field_is_null_and_actual_field_is_of_compared_type() {
    // GIVEN
    var actual = WithObject.of("foo");
    var expected = WithObject.of(null);
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, diff("value", "foo", null));
  }

  @Test
  void should_pass_when_both_fields_are_null() {
    // GIVEN
    var actual = WithObject.of(null);
    var expected = WithObject.of(null);
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .comparingOnlyFieldsOfTypes(String.class)
                .isEqualTo(expected);
  }

  // TODO maybe we should do it ?
  @Test
  void should_not_compare_fields_whose_type_is_a_subtype_of_a_compared_type() {
    // GIVEN
    var actual = WithObject.of("foo");
    var expected = WithObject.of("bar");
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .comparingOnlyFieldsOfTypes(CharSequence.class)
                .isEqualTo(expected);
  }

  @Test
  void should_compare_elements_of_compared_type_in_collections() {
    // GIVEN
    var actual = WithObject.of(List.of("a", "b"));
    var expected = WithObject.of(List.of("a", "c"));
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, javaTypeDiff("value[1]", "b", "c"));
  }

  @Test
  void should_compare_elements_of_compared_type_in_arrays() {
    // GIVEN
    var actual = WithObject.of(new String[] { "a", "b" });
    var expected = WithObject.of(new String[] { "a", "c" });
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, javaTypeDiff("value[1]", "b", "c"));
  }

  @Test
  void should_ignore_elements_not_of_compared_type_in_collections() {
    // GIVEN
    var actual = WithObject.of(List.of(1, 2));
    var expected = WithObject.of(List.of(1, 3));
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .comparingOnlyFieldsOfTypes(String.class)
                .isEqualTo(expected);
  }

  @Test
  void should_use_registered_equals_for_compared_type_fields() {
    // GIVEN
    Person actual = new Person("JOHN");
    Person expected = new Person("john");
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .comparingOnlyFieldsOfTypes(String.class)
                .withEqualsForType(String::equalsIgnoreCase, String.class)
                .isEqualTo(expected);
  }

  @Test
  void should_fail_when_map_values_of_compared_type_differ() {
    // GIVEN
    var actual = WithObject.of(Map.of("k", "a"));
    var expected = WithObject.of(Map.of("k", "b"));
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, javaTypeDiff("value.k", "a", "b"));
  }

  @Test
  void should_fail_when_map_values_of_compared_type_differ_multiple() {
    // GIVEN
    var actual = WithObject.of(Map.of("k1", "a1", "k2", "a2", "k3", "a3"));
    var expected = WithObject.of(Map.of("k1", "b1", "k2", "b2", "k3", "a3"));
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected,
                                           javaTypeDiff("value.k1", "a1", "b1"),
                                           javaTypeDiff("value.k2", "a2", "b2"));
  }

  @Test
  void should_fail_when_compared_type_fields_differ_and_actual_and_expected_have_different_types() {
    // GIVEN
    var actual = WithObject.of(new Person("joe"));
    var expected = WithObject.of(new Giant("jim", 3.0));
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(String.class);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, javaTypeDiff("value.name", "joe", "jim"));
  }

  static class Dog {
    Breed breed;

    Dog(Breed breed) {
      this.breed = breed;
    }
  }

  static class Breed {
    String name;

    Breed(String name) {
      this.name = name;
    }
  }

}
