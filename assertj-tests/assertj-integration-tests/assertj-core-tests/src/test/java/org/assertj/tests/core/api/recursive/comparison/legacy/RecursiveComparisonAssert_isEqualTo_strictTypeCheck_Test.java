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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.recursive.comparison.legacy;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.api.recursive.data.Color.GREEN;
import static org.assertj.tests.core.api.recursive.data.Color.RED;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Date;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.tests.core.api.recursive.data.ColorDto;
import org.assertj.tests.core.api.recursive.data.Giant;
import org.assertj.tests.core.api.recursive.data.Light;
import org.assertj.tests.core.api.recursive.data.LightDto;
import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.PersonDto;
import org.assertj.tests.core.api.recursive.data.PersonDtoWithPersonNeighbour;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_strictTypeCheck_Test
    extends WithLegacyIntrospectionStrategyBaseTest {

  @Test
  void should_pass_by_default_when_objects_data_are_equals_whatever_their_types_are() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jack");
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.home.address.number = 124;

    PersonDto expected = new PersonDto("John");
    expected.home.address.number = 1;
    expected.dateOfBirth = new Date(123);
    expected.neighbour = new PersonDto("Jack");
    expected.neighbour.home.address.number = 123;
    expected.neighbour.neighbour = new PersonDto("James");
    expected.neighbour.neighbour.home.address.number = 124;

    // THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .isEqualTo(expected);
  }

  @Test
  void should_pass_in_strict_type_checking_mode_when_actual_and_expected_have_the_same_data_and_types() {
    // GIVEN
    Giant actual = new Giant("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Giant("Jack");
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.home.address.number = 124;

    Giant expected = new Giant("John");
    expected.home.address.number = 1;
    expected.dateOfBirth = new Date(123);
    expected.neighbour = new Giant("Jack");
    expected.neighbour.home.address.number = 123;
    expected.neighbour.neighbour = new Person("James");
    expected.neighbour.neighbour.home.address.number = 124;

    // WHEN
    recursiveComparisonConfiguration.strictTypeChecking(true);

    // THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .isEqualTo(expected);
  }

  @Test
  void should_fail_in_strict_type_checking_mode_when_actual_and_expected_have_the_same_data_but_different_types() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jack");
    actual.neighbour.home.address.number = 123;

    PersonDtoWithPersonNeighbour expected = new PersonDtoWithPersonNeighbour("John");
    expected.home.address.number = 1;
    expected.dateOfBirth = new Date(123);
    expected.neighbour = new Person("Jack");
    expected.neighbour.home.address.number = 123;

    recursiveComparisonConfiguration.strictTypeChecking(true);

    // WHEN/THEN
    ComparisonDifference difference = diff("", actual, expected,
                                           "the compared values are considered different since the recursive comparison enforces strict type checking and the actual value type org.assertj.tests.core.api.recursive.data.Person is not equal to the expected value type org.assertj.tests.core.api.recursive.data.PersonDtoWithPersonNeighbour");

    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_in_strict_type_checking_mode_when_actual_and_expected_fields_have_the_same_data_but_incompatible_types() {
    // GIVEN
    Something withA = new Something(new A(10));
    Something withB = new Something(new B(10));
    recursiveComparisonConfiguration.strictTypeChecking(true);

    // WHEN/THEN
    // inner comparison fails as the fields have different types
    ComparisonDifference valueDifference = diff("inner", withA.inner, withB.inner,
                                                "the compared values are considered different since the recursive comparison enforces strict type checking and the actual value type org.assertj.tests.core.api.recursive.comparison.legacy.RecursiveComparisonAssert_isEqualTo_strictTypeCheck_Test.A is not equal to the expected value type org.assertj.tests.core.api.recursive.comparison.legacy.RecursiveComparisonAssert_isEqualTo_strictTypeCheck_Test.B");
    compareRecursivelyFailsWithDifferences(withA, withB, valueDifference);
  }

  @ParameterizedTest
  @MethodSource
  void should_fail_in_strict_type_checking_mode_when_actual_and_expected_have_the_same_type_but_different_data(Object actual,
                                                                                                               Object expected,
                                                                                                               ComparisonDifference difference) {
    // GIVEN
    recursiveComparisonConfiguration.strictTypeChecking(true);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  public static Stream<Arguments> should_fail_in_strict_type_checking_mode_when_actual_and_expected_have_the_same_type_but_different_data() {
    Person actual1 = new Person("John");
    actual1.neighbour = new Person("Jack");
    Person expected1 = new Person("John");
    expected1.neighbour = new Person("Jim");

    Person actual2 = new Person("John");
    actual2.neighbour = new Person("Jack");
    Person expected2 = new Person("John");
    expected2.neighbour = null;

    Person actual3 = new Person("John");
    actual3.neighbour = null;
    Person expected3 = new Person("John");
    expected3.neighbour = new Person("Jim");

    Light actual4 = new Light(GREEN);
    Light expected4 = new Light(RED);
    return Stream.of(arguments(actual1, expected1,
                               javaTypeDiff("neighbour.name", actual1.neighbour.name, expected1.neighbour.name)),
                     arguments(actual2, expected2, diff("neighbour", actual2.neighbour, expected2.neighbour)),
                     arguments(actual3, expected3, diff("neighbour", actual3.neighbour, expected3.neighbour)),
                     arguments(actual4, expected4, diff("color", actual4.color, expected4.color)));

  }

  @Test
  void should_pass_when_enums_have_same_value_and_types() {
    // GIVEN
    Light actual = new Light(GREEN);
    Light expected = new Light(GREEN);
    // WHEN-THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .withStrictTypeChecking()
                .isEqualTo(expected);
  }

  @Test
  void should_fail_when_enums_have_same_value_but_different_types() {
    // GIVEN
    Light actual = new Light(GREEN);
    LightDto expected = new LightDto(ColorDto.RED);
    // WHEN/THEN
    ComparisonDifference difference = diff("color", GREEN, ColorDto.RED);
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  private static class Something {
    Inner inner;  // can be A or B

    public Something(Inner inner) {
      this.inner = inner;
    }
  }

  private static class Inner {
    @SuppressWarnings("unused")
    int value;
  }

  private static class A extends Inner {

    public A(int value) {
      this.value = value;
    }

  }

  private static class B extends Inner {
    public B(int value) {
      this.value = value;
    }
  }

}
