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
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.tests.core.api.recursive.data.Address;
import org.assertj.tests.core.api.recursive.data.Giant;
import org.assertj.tests.core.api.recursive.data.Human;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("unused")
class RecursiveComparisonAssert_isEqualTo_ignoringFields_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("recursivelyEqualObjectsIgnoringActualNullValues")
  void should_pass_when_actual_null_fields_are_ignored(Object actual, Object expected,
                                                       @SuppressWarnings("unused") String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringActualNullFields()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> recursivelyEqualObjectsIgnoringActualNullValues() {
    Person person1 = new Person(null);
    person1.home.address.number = 1;

    Person person2 = new Person("John");
    person2.home.address.number = 1;

    Person person3 = new Person(null);
    person3.home = null;

    Human person4 = new Human();
    person4.name = "John";
    person4.home.address.number = 1;

    Human person5 = new Human();
    person5.home.address.number = 1;

    Person person6 = new Person();
    person6.name = "John";
    person6.neighbour = null;
    person6.dateOfBirth = null;
    person6.home.address = null;

    Person person7 = new Person();
    person7.name = "John";
    person7.neighbour = new Person("Jack");
    person7.neighbour.home = null;
    person7.neighbour.neighbour = new Person("James");
    person7.neighbour.neighbour.home.address = null;
    person7.home.address = null;

    Person person8 = new Person();
    person8.name = "John";
    person8.neighbour = new Person("Jack");
    person8.neighbour.home.address.number = 123;
    person8.neighbour.neighbour = new Person("James");
    person8.neighbour.neighbour.home.address.number = 456;

    return Stream.of(arguments(person1, person2, "same data, same type, except for actual null fields"),
                     arguments(person3, person1, "all actual fields are null, should be equal to anything"),
                     arguments(person3, person2, "all actual fields are null, should be equal to anything"),
                     arguments(person3, person3, "all actual fields are null, should be equal to anything"),
                     arguments(person3, person4, "same data, different type, actual has null fields"),
                     arguments(person5, person2, "same data, different type, actual has null fields"),
                     arguments(person6, person2, "same data, different type, actual has only non null name"),
                     arguments(person6, person7, "same data, actual has only non null name"),
                     arguments(person6, person8, "same data, actual has only non null name"),
                     arguments(person7, person8, "same data, actual has null fields deep in its graph"));
  }

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("recursivelyEqualObjectsIgnoringActualOptionalEmptyValues")
  void should_pass_when_actual_empty_optional_fields_are_ignored(Object actual,
                                                                 Object expected,
                                                                 @SuppressWarnings("unused") String testDescription) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringActualEmptyOptionalFields()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> recursivelyEqualObjectsIgnoringActualOptionalEmptyValues() {
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

    return Stream.of(arguments(person1, person2, "same data and same type except for actual empty optional fields"),
                     arguments(person1, person3, "same data, same type, both actual and expected have empty optional fields"),
                     arguments(person1, person4, "same data and same type except for ignored optional fields"));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_all_empty_optional_actual_fields_are_ignored() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.phone = Optional.empty();
    actual.age = OptionalInt.empty();
    actual.id = OptionalLong.empty();
    actual.weight = OptionalDouble.empty();

    Person expected = new Person("John");
    expected.home.address.number = 2;
    expected.phone = Optional.of("123-456-789");
    expected.age = OptionalInt.of(33);
    expected.id = OptionalLong.of(123456);
    expected.weight = OptionalDouble.of(1.80);

    recursiveComparisonConfiguration.setIgnoreAllActualEmptyOptionalFields(true);

    // WHEN/THEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list("home.address.number"), 1, 2));
    compareRecursivelyFailsWithDifferences(actual, expected, comparisonDifference);
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_all_null_actual_fields_are_ignored() {
    // GIVEN
    Person actual = new Person(null);
    actual.home.address.number = 1;
    actual.dateOfBirth = null;
    actual.neighbour = null;
    Person expected = new Person("John");
    expected.home.address.number = 2;
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    // WHEN/THEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list("home.address.number"), 1, 2));
    compareRecursivelyFailsWithDifferences(actual, expected, comparisonDifference);
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_all_null_expected_fields_are_ignored() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date();
    actual.neighbour = new Person("Jack");
    Person expected = new Person(null);
    expected.home.address.number = 2;
    expected.dateOfBirth = null;
    expected.neighbour = null;
    recursiveComparisonConfiguration.setIgnoreAllExpectedNullFields(true);
    // WHEN/THEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list("home.address.number"), 1, 2));
    compareRecursivelyFailsWithDifferences(actual, expected, comparisonDifference);
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored fields={3}")
  @MethodSource("recursivelyEqualObjectsIgnoringGivenFields")
  void should_pass_for_objects_with_the_same_data_when_given_fields_are_ignored(Object actual,
                                                                                Object expected,
                                                                                String testDescription,
                                                                                String[] ignoredFields) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringFields(ignoredFields)
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> recursivelyEqualObjectsIgnoringGivenFields() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;

    Person giant1 = new Giant();
    giant1.name = "Giant John";
    ((Giant) giant1).height = 3.1;
    giant1.home.address.number = 1;

    Person person2 = new Person("Jack");
    person2.home.address.number = 1;

    Person person3 = new Person("John");
    person3.home.address.number = 123;

    Human person4 = new Human();
    person4.name = "Jack";
    person4.home.address.number = 456;

    Person person5 = new Person();
    person5.home.address.number = 1;

    Person person6 = new Person();
    person6.home.address.number = 2;

    Person person7 = new Person("John");
    person7.neighbour = new Person("Jack");
    person7.neighbour.home.address.number = 123;
    person7.neighbour.neighbour = new Person("James");
    person7.neighbour.neighbour.home.address.number = 124;

    Person person8 = new Person("John");
    person8.neighbour = new Person("Jim");
    person8.neighbour.home.address.number = 123;
    person8.neighbour.neighbour = new Person("James");
    person8.neighbour.neighbour.home.address.number = 457;

    return Stream.of(arguments(person1, person2, "same data and type, except for one ignored field",
                               array("name")),
                     arguments(list(person1), list(person2), "list of same data and type, except for one ignored field",
                               array("name")),
                     arguments(array(person1), array(person2), "array of same data and type, except for one ignored field",
                               array("name")),
                     arguments(list(person1, giant1), list(person2, person1),
                               "list of same data except name and height which is not even a field from person1",
                               array("name", "height")),
                     arguments(array(person1, giant1), array(person2, person1),
                               "list of same data except name and height which is not even a field from person1",
                               array("name", "height")),
                     arguments(list(person3, person7), list(person4, person8),
                               "list of same data except name and height which is not even a field from person1",
                               array("name", "home.address.number", "neighbour.neighbour.home.address.number", "neighbour.name")),
                     arguments(array(person3, person7), array(person4, person8),
                               "array of same data except name and height which is not even a field from person1",
                               array("name", "home.address.number", "neighbour.neighbour.home.address.number", "neighbour.name")),
                     arguments(giant1, person1,
                               "different type, same data except name and height which is not even a field from person1",
                               array("name", "height")),
                     arguments(person3, person4, "same data, different type, except for several ignored fields",
                               array("name", "home.address.number")),
                     arguments(person5, person6, "same data except for one subfield of an ignored field",
                               array("home")),
                     arguments(person7, person8, "same data except for one subfield of an ignored field",
                               array("neighbour.neighbour.home.address.number", "neighbour.name")));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_some_fields_are_ignored() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jack");
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.home.address.number = 124;

    Person expected = new Person("Jack");
    expected.home.address.number = 2;
    expected.dateOfBirth = new Date(456);
    expected.neighbour = new Person("Jim");
    expected.neighbour.home.address.number = 123;
    expected.neighbour.neighbour = new Person("James");
    expected.neighbour.neighbour.home.address.number = 457;

    recursiveComparisonConfiguration.ignoreFields("name", "home.address.number");

    // WHEN/THEN
    ComparisonDifference dateOfBirthDifference = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference neighbourNameDifference = diff("neighbour.name", actual.neighbour.name, expected.neighbour.name);
    ComparisonDifference numberDifference = diff("neighbour.neighbour.home.address.number",
                                                 actual.neighbour.neighbour.home.address.number,
                                                 expected.neighbour.neighbour.home.address.number);
    compareRecursivelyFailsWithDifferences(actual, expected, dateOfBirthDifference, neighbourNameDifference, numberDifference);
  }

  @SuppressWarnings("unused")
  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored fields regex={3}")
  @MethodSource("recursivelyEqualObjectsWhenFieldsMatchingGivenRegexesAreIgnored")
  void should_pass_when_fields_matching_given_regexes_are_ignored(Object actual,
                                                                  Object expected,
                                                                  String testDescription,
                                                                  List<String> ignoredFieldRegexes) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringFieldsMatchingRegexes(arrayOf(ignoredFieldRegexes))
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> recursivelyEqualObjectsWhenFieldsMatchingGivenRegexesAreIgnored() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;

    Person giant1 = new Giant();
    giant1.name = "Giant John";
    ((Giant) giant1).height = 3.1;
    giant1.home.address.number = 1;

    Person person2 = new Person("Jack");
    person2.home.address.number = 1;

    Person person3 = new Person("John");
    person3.home.address.number = 123;

    Human person4 = new Human();
    person4.name = "Jack";
    person4.home.address.number = 456;

    Person person5 = new Person();
    person5.home.address.number = 1;

    Person person6 = new Person();
    person6.home.address.number = 2;

    Person person7 = new Person("John");
    person7.neighbour = new Person("Jack");
    person7.neighbour.home.address.number = 123;
    person7.neighbour.neighbour = new Person("James");
    person7.neighbour.neighbour.home.address.number = 124;

    Person person8 = new Person("John");
    person8.neighbour = new Person("Jim");
    person8.neighbour.home.address.number = 123;
    person8.neighbour.neighbour = new Person("James");
    person8.neighbour.neighbour.home.address.number = 457;

    // @format:off
    return Stream.of(arguments(person1, person2, "same data and type, except for one ignored field", list("nam.")),
                     arguments(giant1, person1, "different type, same data except name and height which is not even a field from person1", list(".am.", "height")),
                     arguments(person3, person4, "same data, different type, except for name and home.address.number", list(".*n.m.*")),
                     arguments(person5, person6, "same data except for one subfield of an ignored field", list("home.*")),
                     arguments(person7, person8, "same data except for one subfield of an ignored field", list("neighbour.*")),
                     arguments(person7, person8, "should not stack overflow with regexes", list(".*neighbour[\\D]+", ".*update[\\D]+")));
    // @format:on
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_some_fields_are_ignored_by_regexes() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    actual.dateOfBirth = new Date(123);
    actual.neighbour = new Person("Jack");
    actual.neighbour.dateOfBirth = new Date(123);
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.home.address.number = 124;

    Person expected = new Person("Jack");
    expected.home.address.number = 2;
    expected.dateOfBirth = new Date(456);
    expected.neighbour = new Person("Jim");
    expected.neighbour.dateOfBirth = new Date(456);
    expected.neighbour.home.address.number = 234;
    expected.neighbour.neighbour = new Person("James");
    expected.neighbour.neighbour.home.address.number = 457;

    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(".*name", ".*home.*number");

    // WHEN/THEN
    ComparisonDifference dateOfBirthDifference = diff("dateOfBirth", actual.dateOfBirth, expected.dateOfBirth);
    ComparisonDifference neighbourDateOfBirthDifference = diff("neighbour.dateOfBirth",
                                                               actual.neighbour.dateOfBirth,
                                                               expected.neighbour.dateOfBirth);
    compareRecursivelyFailsWithDifferences(actual, expected,
                                           dateOfBirthDifference, neighbourDateOfBirthDifference);
  }

  @ParameterizedTest(name = "{2}: actual={0} / expected={1} / ignored types={3}")
  @MethodSource("recursivelyEqualObjectsIgnoringGivenTypes")
  void should_pass_when_fields_with_given_types_are_ignored(Object actual,
                                                            Object expected,
                                                            @SuppressWarnings("unused") String testDescription,
                                                            List<Class<?>> ignoredTypes) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringFieldsOfTypes(ignoredTypes.toArray(new Class<?>[0]))
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> recursivelyEqualObjectsIgnoringGivenTypes() {
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

    Person person7 = new Person("John");
    person7.neighbour = new Person("Jack");
    person7.neighbour.home.address.number = 123;

    Person person8 = new Person("John");
    person8.neighbour = new Person("Jim");
    person8.neighbour.home.address.number = 456;

    return Stream.of(arguments(person1, person2, "same data and type, except for one ignored type", list(String.class)),
                     arguments(person3, person4, "same data, different type, except for several ignored types",
                               list(String.class, Date.class)),
                     arguments(person5, person6, "same data except for one subfield of an ignored type", list(Address.class)),
                     arguments(person7, person8,
                               "same data except for several subfields of ignored types, including a primitive type",
                               list(Integer.class, String.class)));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_some_fields_are_ignored_for_types() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address = null;
    actual.neighbour = new Person("Jack");
    actual.neighbour.home.address.number = 123;
    actual.neighbour.neighbour = new Person("James");
    actual.neighbour.neighbour.dateOfBirth = new Date(123);

    Person expected = new Person("Jack");
    expected.home.address.number = 2;
    expected.neighbour = new Person("Jim");
    expected.neighbour.home.address.number = 456;
    expected.neighbour.neighbour = new Person("James");
    expected.neighbour.neighbour.dateOfBirth = new Date(456);

    recursiveComparisonConfiguration.ignoreFieldsOfTypes(String.class, Address.class);

    // WHEN/THEN
    ComparisonDifference addressDifference = diff("home.address", actual.home.address, expected.home.address);
    ComparisonDifference neighbourDateOfBirthDifference = diff("neighbour.neighbour.dateOfBirth",
                                                               actual.neighbour.neighbour.dateOfBirth,
                                                               expected.neighbour.neighbour.dateOfBirth);
    compareRecursivelyFailsWithDifferences(actual, expected, addressDifference, neighbourDateOfBirthDifference);
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
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(NumberHolder.class);
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
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(NumberHolder.class);
    // WHEN/THEN
    then(new WithNumberHolderMap(holdersA)).usingRecursiveComparison(recursiveComparisonConfiguration)
                                           .isEqualTo(new WithNumberHolderMap(holdersB));
  }

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("recursivelyEqualObjectsIgnoringExpectedNullFields")
  void should_pass_when_expected_null_fields_are_ignored(Object actual, Object expected,
                                                         @SuppressWarnings("unused") String testDescription) {

    then(actual).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
  }

  private static Stream<Arguments> recursivelyEqualObjectsIgnoringExpectedNullFields() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;

    Person person2 = new Person(null);

    Person person3 = new Person("John");
    person3.home.address = null;

    Person person4 = new Person(null);
    person3.home.address = null;

    return Stream.of(arguments(person1, person2, "first level expected null field"),
                     arguments(person1, person3, "nested expected null field"),
                     arguments(person1, person4, "multiple expected null fields"));
  }

  private static String[] arrayOf(List<String> list) {
    return list.toArray(new String[0]);
  }

  @Test
  void should_honor_ignored_fields() {
    // GIVEN
    Data actual = new Data(new Data.InnerData("match", "nonMatch"), null);
    Data expected = new Data(new Data.InnerData("match", "hctaMnon"), null);
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .ignoringFields("innerData.field2")
                .isEqualTo(expected);
  }

  @Test
  void should_honor_ignored_fields_regex_in_inner_list() {
    // GIVEN
    Data actual = new Data(null, list(new Data.InnerData("match", "nonMatch")));
    Data expected = new Data(null, list(new Data.InnerData("match", "hctaMnon")));
    RecursiveComparisonConfiguration conf = new RecursiveComparisonConfiguration();
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*field2")
                .isEqualTo(expected);
  }

  @Test
  void should_honor_ignored_fields_regex() {
    // GIVEN
    Data actual = new Data(new Data.InnerData("match", "nonMatch"), null);
    Data expected = new Data(new Data.InnerData("match", "hctaMnon"), null);
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*field2")
                .isEqualTo(expected);
  }

  static class Data {
    private final InnerData innerData;
    private final List<InnerData> innerDataList;

    public Data(InnerData innerData, List<InnerData> innerDataList) {
      this.innerData = innerData;
      this.innerDataList = innerDataList;
    }

    public InnerData getInnerData() {
      return innerData;
    }

    public List<InnerData> getInnerDataList() {
      return innerDataList;
    }

    static class InnerData {
      private final String field1;
      private final String field2;

      public InnerData(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
      }

      public String getField2() {
        return field2;
      }

      public String getField1() {
        return field1;
      }
    }
  }
}
