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
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_ignoringActualEmptyOptionalFields_Test
    extends WithComparingFieldsIntrospectionStrategyBaseTest {

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource
  void should_pass_when_actual_empty_optional_fields_are_ignored(Object actual,
                                                                 Object expected,
                                                                 String testDescription) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringActualEmptyOptionalFields()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_when_actual_empty_optional_fields_are_ignored() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;
    person1.phone = Optional.empty();
    person1.age = OptionalInt.empty();
    person1.id = OptionalLong.empty();
    person1.weight = OptionalDouble.empty();

    Person person2 = new Person("John");
    person2.home.address.number = 1;
    person2.phone = Optional.of("456");
    person2.age = OptionalInt.of(33);
    person2.id = OptionalLong.of(123456);
    person2.weight = OptionalDouble.of(1.80);

    Person person3 = new Person("John");
    person3.home.address.number = 1;
    person3.phone = Optional.empty();
    person3.age = OptionalInt.empty();
    person3.id = OptionalLong.empty();
    person3.weight = OptionalDouble.empty();

    Person person4 = new Person("John");
    person4.home.address.number = 1;

    Person neighbour1 = new Person("Ned");
    neighbour1.phone = Optional.empty();
    neighbour1.age = OptionalInt.empty();
    Person person5 = new Person("John");
    person5.home.address.number = 1;
    person5.phone = Optional.empty();
    person5.neighbour = neighbour1;

    Person neighbour2 = new Person("Ned");
    neighbour2.phone = Optional.of("789");
    neighbour2.age = OptionalInt.of(60);
    Person person6 = new Person("John");
    person6.home.address.number = 1;
    person6.phone = Optional.of("456");
    person6.neighbour = neighbour2;

    return Stream.of(arguments(person1, person2, "same data and same type except for actual empty optional fields"),
                     arguments(person1, person3, "same data, same type, both actual and expected have empty optional fields"),
                     arguments(person1, person4, "same data and same type except for ignored optional fields"),
                     arguments(person5, person6, "same data except for nested actual empty optional fields"));
  }

  @Test
  void should_fail_if_expected_has_empty_optionals_but_actual_is_populated() {
    // GIVEN
    Person actual = new Person("Homer");
    actual.phone = Optional.of("555");
    actual.age = OptionalInt.of(39);
    actual.id = OptionalLong.of(1L);
    actual.weight = OptionalDouble.of(1.83);
    actual.neighbour = new Person("Ned");
    actual.neighbour.phone = Optional.of("789");
    Person expected = new Person("Homer");
    expected.phone = Optional.empty();
    expected.age = OptionalInt.empty();
    expected.id = OptionalLong.empty();
    expected.weight = OptionalDouble.empty();
    expected.neighbour = new Person("Ned");
    expected.neighbour.phone = Optional.empty();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .ignoringActualEmptyOptionalFields()
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContainingAll("field/property 'phone'",
                                                 "field/property 'age'",
                                                 "field/property 'id'",
                                                 "field/property 'weight'",
                                                 "field/property 'neighbour.phone'");
  }

  @Test
  void should_not_ignore_other_differences_when_ignoring_actual_empty_optionals() {
    // GIVEN
    Person actual = new Person("Homer");
    actual.phone = Optional.empty();
    Person expected = new Person("Marge");
    expected.phone = Optional.of("555");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .ignoringActualEmptyOptionalFields()
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining("field/property 'name'")
                        .hasMessageNotContaining("field/property 'phone'");
  }

  @Test
  void should_not_ignore_actual_null_optional_fields() {
    // GIVEN
    Person actual = new Person("Homer");
    actual.phone = null;
    Person expected = new Person("Homer");
    expected.phone = Optional.of("555");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .ignoringActualEmptyOptionalFields()
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining("field/property 'phone'");
  }

}
