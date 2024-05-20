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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newHashSet;
import static org.assertj.tests.core.testkit.ObjectArrays.arrayOf;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.assertj.tests.core.api.recursive.data.Human;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("unused")
class RecursiveComparisonAssert_isEqualTo_ignoringFieldsOfTypesMatchingRegexes_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored types={3}")
  @MethodSource
  void should_pass_when_fields_whose_types_match_given_regexes_are_ignored(Object actual,
                                                                           Object expected,
                                                                           List<String> regexes,
                                                                           @SuppressWarnings("unused") String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringFieldsOfTypesMatchingRegexes(arrayOf(regexes))
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_when_fields_whose_types_match_given_regexes_are_ignored() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;

    Person person2 = new Person("Jack");
    person2.home.address.number = 1;

    Person person3 = new Person("John");
    person3.dateOfBirth = new Date(123);

    Human person4 = new Human();
    person4.name = "Jack";
    person4.dateOfBirth = new Date(456);

    Person person5 = new Person();
    person5.home.address.number = 1;

    Person person6 = new Person();
    person6.home.address.number = 2;

    return Stream.of(arguments(person1, person2, list("java.*String"),
                               "same data and type, except for String"),
                     arguments(person3, person4, list(".*lang\\.String", "java\\.util\\.Date"),
                               "same data, different type, except for String and Date"),
                     arguments(person5, person6, list("org\\.assertj\\.tests\\.core\\.api\\.recursive\\.data.*"),
                               "same data except for one an assertj internal type"),
                     arguments(person5, person6, list(".*Integer"),
                               "primitive types can only be ignored if specifying their corresponding wrapper types"));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_some_fields_are_ignored_for_types() {
    // GIVEN
    Person actual = new Person("John");
    actual.id = OptionalLong.of(123);
    actual.age = OptionalInt.of(30);
    actual.home.address.number = 1;
    actual.neighbour = new Person("Jack");
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.age = OptionalInt.of(40);

    Person expected = new Person("Jack");
    expected.id = OptionalLong.of(456);
    expected.age = OptionalInt.of(50);
    expected.home.address.number = 2;
    expected.neighbour = new Person("Jim");
    expected.neighbour.home.address.number = 456;
    expected.neighbour.neighbour = new Person("James");
    expected.neighbour.neighbour.age = OptionalInt.of(60);

    recursiveComparisonConfiguration.ignoreFieldsOfTypesMatchingRegexes(".*lang\\.String",
                                                                        "org\\.assertj.*data\\.Address",
                                                                        "java\\.util\\.OptionalI.*");
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, diff("id", actual.id, expected.id));
  }

  static class NumberHolder {
    private final Number number;

    NumberHolder(final Number number) {
      this.number = number;
    }

    public Number getNumber() {
      return number;
    }

    @Override
    public String toString() {
      return number.toString();
    }
  }

  @Test
  void should_pass_when_fields_with_given_types_are_ignored_on_unordered_collections() {

    class WithNumberHolderCollection {
      private final Collection<NumberHolder> holders;

      WithNumberHolderCollection(Collection<NumberHolder> holders) {
        this.holders = holders;
      }

      Collection<NumberHolder> getNumberHolders() {
        return holders;
      }
    }

    // GIVEN
    final Number intValue = 12;
    final Double doubleValueA = 12.34;
    final Double doubleValueB = 56.78;
    final List<NumberHolder> holdersA = list(new NumberHolder(intValue), new NumberHolder(doubleValueA));
    final List<NumberHolder> holdersB = list(new NumberHolder(intValue), new NumberHolder(doubleValueB));
    WithNumberHolderCollection actual = new WithNumberHolderCollection(newHashSet(holdersA));

    recursiveComparisonConfiguration.ignoreFieldsOfTypesMatchingRegexes(".*NumberHolder");

    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .isEqualTo(new WithNumberHolderCollection(newHashSet(holdersB)));
    // bonus check also ordered collection
    actual = new WithNumberHolderCollection(new ArrayList<>(holdersA));
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .isEqualTo(new WithNumberHolderCollection(new ArrayList<>(holdersB)));
  }

  @Test
  void should_pass_when_fields_with_given_types_are_ignored_on_unordered_maps() {
    class WithNumberHolderMap {
      private final Map<String, NumberHolder> holders;

      WithNumberHolderMap(NumberHolder... holders) {
        this.holders = new HashMap<>();
        for (int i = 0; i < holders.length; i++) {
          this.holders.put("key " + i, holders[i]);
        }
      }

      Map<String, NumberHolder> getNumberHoldersMap() {
        return holders;
      }
    }

    // GIVEN
    final Number intValue = 12;
    final Double doubleValueA = 12.34;
    final Double doubleValueB = 56.78;
    final NumberHolder[] holdersA = array(new NumberHolder(intValue), new NumberHolder(doubleValueA));
    final NumberHolder[] holdersB = array(new NumberHolder(intValue), new NumberHolder(doubleValueB));
    recursiveComparisonConfiguration.ignoreFieldsOfTypesMatchingRegexes(".*NumberHolder");
    // WHEN/THEN
    then(new WithNumberHolderMap(holdersA)).usingRecursiveComparison(recursiveComparisonConfiguration)
                                           .isEqualTo(new WithNumberHolderMap(holdersB));
  }

  @Test
  void does_not_support_ignoring_primitive_types_but_only_their_wrapper_types() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("John");
    expected.home.address.number = 2;
    recursiveComparisonConfiguration.ignoreFieldsOfTypesMatchingRegexes("int");
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, diff("home.address.number",
                                                                  actual.home.address.number,
                                                                  expected.home.address.number));
  }

  @Test
  void evaluates_expected_when_actual_field_is_null_and_strict_type_checking_is_enabled() {
    // GIVEN
    Person actual = new Person("John");
    actual.home = null;
    Person expected = new Person("John");
    expected.home.address.number = 123;
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .ignoringFieldsOfTypesMatchingRegexes(".*Home")
                .withStrictTypeChecking()
                .isEqualTo(expected);
  }
}
