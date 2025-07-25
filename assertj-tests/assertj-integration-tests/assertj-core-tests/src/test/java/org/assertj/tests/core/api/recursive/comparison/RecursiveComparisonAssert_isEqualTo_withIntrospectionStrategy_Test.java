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
package org.assertj.tests.core.api.recursive.comparison;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.ComparingFields.COMPARING_FIELDS;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.recursive.comparison.AbstractRecursiveComparisonIntrospectionStrategy;
import org.assertj.core.api.recursive.comparison.ComparingNormalizedFields;
import org.assertj.core.api.recursive.comparison.ComparingSnakeOrCamelCaseFields;
import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonIntrospectionStrategy;
import org.assertj.core.internal.Objects;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_isEqualTo_withIntrospectionStrategy_Test extends RecursiveComparisonAssert_BaseTest {

  RecursiveComparisonIntrospectionStrategy comparingFieldsNameContaining_o = new ComparingFieldsNameContaining_o();

  @Test
  void should_pass_with_the_specified_comparison_strategy() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("Steve");
    expected.home.address.number = 2;
    // compared fields
    actual.dateOfBirth = new Date(123);
    expected.dateOfBirth = new Date(123);
    actual.phone = Optional.of("6677889900");
    expected.phone = Optional.of("6677889900");
    actual.neighbour = new Person("John neighbour"); // names are not compared
    expected.neighbour = new Person("Steve neighbour");
    actual.neighbour.dateOfBirth = new Date(456);
    expected.neighbour.dateOfBirth = new Date(456);
    actual.neighbour.phone = Optional.of("1122334455");
    expected.neighbour.phone = Optional.of("1122334455");
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .withIntrospectionStrategy(comparingFieldsNameContaining_o)
                .isEqualTo(expected);
  }

  @Test
  void should_report_differences_with_the_specified_comparison_strategy() {
    // GIVEN
    Person actual = new Person("John");
    actual.home.address.number = 1;
    Person expected = new Person("Steve");
    expected.home.address.number = 2;
    // compared fields
    actual.dateOfBirth = new Date(123);
    expected.dateOfBirth = new Date(123);
    actual.phone = Optional.of("123");
    expected.phone = Optional.of("456");
    actual.neighbour = new Person("John neighbour"); // names are not compared
    expected.neighbour = new Person("Steve neighbour");
    actual.neighbour.dateOfBirth = new Date(456);
    expected.neighbour.dateOfBirth = new Date(789);
    actual.neighbour.phone = Optional.of("1122334455");
    expected.neighbour.phone = Optional.of("1122334455");

    recursiveComparisonConfiguration.setIntrospectionStrategy(comparingFieldsNameContaining_o);

    // WHEN/THEN
    ComparisonDifference phoneDifference = javaTypeDiff("phone.value", actual.phone.get(), expected.phone.get());
    ComparisonDifference neighbourDateOfBirthDifference = javaTypeDiff("neighbour.dateOfBirth",
                                                                       actual.neighbour.dateOfBirth,
                                                                       expected.neighbour.dateOfBirth);
    compareRecursivelyFailsWithDifferences(actual, expected, neighbourDateOfBirthDifference, phoneDifference);
  }

  static class ComparingFieldsNameContaining_o extends AbstractRecursiveComparisonIntrospectionStrategy {

    @Override
    public Set<String> getChildrenNodeNamesOf(Object node) {
      if (node == null) return new HashSet<>();
      Set<String> fieldsNames = Objects.getFieldsNames(node.getClass());
      return fieldsNames.stream().filter(name -> name.toLowerCase().contains("o")).collect(toSet());
    }

    @Override
    public Object getChildNodeValue(String childNodeName, Object instance) {
      return COMPARISON.getSimpleValue(childNodeName, instance);
    }

    @Override
    public String getDescription() {
      return "comparing fields containing o";
    }
  }

  // addresses https://github.com/assertj/assertj/issues/2554

  @Test
  void should_pass_with_the_snake_case_matching_camel_case_fields() {
    // GIVEN
    Author martinFowler = new Author("Martin", "Fowler", 58, "+447975777666", "+441611234567",
                                     "https://www.thoughtworks.com/profiles/leaders/martin-fowler");
    Book refactoring = new Book("Refactoring", martinFowler);
    AuthorDto martinFowlerDto = new AuthorDto("Martin", "Fowler", 58, "+447975777666", "+441611234567",
                                              "https://www.thoughtworks.com/profiles/leaders/martin-fowler");
    BookDto refactoringDto = new BookDto("Refactoring", martinFowlerDto);
    RecursiveComparisonIntrospectionStrategy comparingSnakeOrCamelCaseFields = new ComparingSnakeOrCamelCaseFields();

    // WHEN/THEN
    then(refactoring).usingRecursiveComparison(recursiveComparisonConfiguration)
                     .withIntrospectionStrategy(comparingSnakeOrCamelCaseFields)
                     .isEqualTo(refactoringDto);
    then(refactoringDto).usingRecursiveComparison(recursiveComparisonConfiguration)
                        .withIntrospectionStrategy(comparingSnakeOrCamelCaseFields)
                        .isEqualTo(refactoring);
  }

  static class Author {
    String firstName;
    String lastName;
    int age;
    String phoneNumber1;
    String phoneNumber2;
    String profileURL;

    Author(String firstName, String lastName, int age, String phoneNumber1, String phoneNumber2, String profileUrl) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.age = age;
      this.phoneNumber1 = phoneNumber1;
      this.phoneNumber2 = phoneNumber2;
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
    String phone_number_1;
    String phone_number_2;
    String profile_url;

    AuthorDto(String firstName, String lastName, int age, String phoneNumber1, String phoneNumber2, String profileUrl) {
      this.first_name = firstName;
      this.last_name = lastName;
      this._age = age;
      this.phone_number_1 = phoneNumber1;
      this.phone_number_2 = phoneNumber2;
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

  // https://github.com/assertj/assertj/issues/2149

  @Test
  void should_fail_with_field_based_introspection() {
    // GIVEN
    Values actual = new Values("A", "B");
    Values expected = new Values("A", "C");
    recursiveComparisonConfiguration.setIntrospectionStrategy(COMPARING_FIELDS);

    // WHEN/THEN
    ComparisonDifference valuesDifference = javaTypeDiff("values[1]", "B", "C");
    compareRecursivelyFailsWithDifferences(actual, expected, valuesDifference);

    // Note that this succeeds when it should not:
    // then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
    // rationale is by default we get value by property first and field second which means that we call getValues(),
    // which returns the first element "A" of both actual and expected.
  }

  static class Values {
    Collection<String> values;

    public Values(String... values) {
      this.values = asList(values);
    }

    public String getValues() {
      return values == null ? null : values.iterator().next();
    }
  }

  static class ComparingLowercaseNormalizedFields extends ComparingNormalizedFields {

    static final ComparingLowercaseNormalizedFields INSTANCE = new ComparingLowercaseNormalizedFields();

    @Override
    public String normalizeFieldName(String name) {
      return name.toLowerCase();
    }

  }

  @Test
  void should_pass_with_lowercase_compared_fields() {
    // GIVEN
    Animal fox = new Animal("fox");
    AnimalDto foxDto = new AnimalDto("fox");
    // WHEN/THEN
    then(fox).usingRecursiveComparison(recursiveComparisonConfiguration)
             .withIntrospectionStrategy(ComparingLowercaseNormalizedFields.INSTANCE)
             .isEqualTo(foxDto);
    then(foxDto).usingRecursiveComparison(recursiveComparisonConfiguration)
                .withIntrospectionStrategy(ComparingLowercaseNormalizedFields.INSTANCE)
                .isEqualTo(fox);
  }

  @Test
  void should_pass_with_lowercase_compared_fields_several_times() {
    // GIVEN
    Animal fox1 = new Animal("fox");
    AnimalDto foxDto1 = new AnimalDto("fox");
    Animal fox2 = new Animal("fox");
    AnimalDto foxDto2 = new AnimalDto("fox");
    // WHEN/THEN
    then(fox1).usingRecursiveComparison(recursiveComparisonConfiguration)
              .withIntrospectionStrategy(ComparingLowercaseNormalizedFields.INSTANCE)
              .isEqualTo(foxDto1);
    then(fox2).usingRecursiveComparison(recursiveComparisonConfiguration)
              .withIntrospectionStrategy(ComparingLowercaseNormalizedFields.INSTANCE)
              .isEqualTo(foxDto2);
  }

  static class Animal {
    String raceName;

    Animal(String raceName) {
      this.raceName = raceName;
    }
  }

  static class AnimalDto {
    String racename;

    AnimalDto(String racename) {
      this.racename = racename;
    }
  }
}
