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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.ComparingFields.COMPARING_FIELDS;
import static org.assertj.core.api.recursive.comparison.ComparingProperties.COMPARING_PROPERTIES;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.recursive.comparison.ComparingNormalizedFields;
import org.assertj.core.api.recursive.comparison.ComparingProperties;
import org.assertj.core.api.recursive.comparison.ComparingSnakeOrCamelCaseFields;
import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonIntrospectionStrategy;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.introspection.IntrospectionError;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_isEqualTo_withIntrospectionStrategy_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest {

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
    then(actual).usingRecursiveComparison()
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
    ComparisonDifference phoneDifference = diff("phone.value", actual.phone.get(), expected.phone.get());
    ComparisonDifference neighbourDateOfBirthDifference = diff("neighbour.dateOfBirth",
                                                               actual.neighbour.dateOfBirth,
                                                               expected.neighbour.dateOfBirth);
    compareRecursivelyFailsWithDifferences(actual, expected, neighbourDateOfBirthDifference, phoneDifference);
  }

  static class ComparingFieldsNameContaining_o implements RecursiveComparisonIntrospectionStrategy {

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
    then(refactoring).usingRecursiveComparison()
                     .withIntrospectionStrategy(comparingSnakeOrCamelCaseFields)
                     .isEqualTo(refactoringDto);
    then(refactoringDto).usingRecursiveComparison()
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

  // related to https://github.com/assertj/assertj/issues/2314 and https://github.com/assertj/assertj/issues/2108

  @Test
  void should_pass_with_property_based_introspection_for_2314() {
    // GIVEN
    Message actual = new HelloMessage();
    Message expected = new GenericMessage("hello");
    // WHEN/THEN
    then(actual).usingRecursiveComparison()
                .withIntrospectionStrategy(COMPARING_PROPERTIES)
                .isEqualTo(expected);

    // note that the following assertion succeeds because the default behavior is to look for actual fields but not properties and
    // HelloMessage does not have fields
    // then(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  interface Message {
    @SuppressWarnings("unused")
    String getTemplate();

    boolean isEmpty();
  }
  static class HelloMessage implements Message {
    public final String getTemplate() {
      return "hello";
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

  }

  static class GenericMessage implements Message {
    String template;
    boolean empty;

    public GenericMessage(String template) {
      this.template = template;
      this.empty = template == null || template.isEmpty();
    }

    @Override
    public String getTemplate() {
      return template;
    }

    @Override
    public boolean isEmpty() {
      return empty;
    }
  }

  // https://github.com/assertj/assertj/issues/2108

  @Test
  void should_detect_badly_set_optional_2108() {
    // GIVEN
    Bean actual = new Bean();
    Bean expected = new Bean();
    ComparingProperties comparingProperties = new ComparingProperties();
    // WHEN
    Throwable throwable = catchThrowable(() -> then(actual).usingRecursiveComparison()
                                                           .withIntrospectionStrategy(comparingProperties)
                                                           .isEqualTo(expected));
    // THEN fails due to getString failing as it tries to build an optional for a null value.
    then(throwable).isInstanceOf(IntrospectionError.class);
  }

  static class Bean {
    private final String string = null;

    public Optional<String> getString() {
      return Optional.of(string);
    } // coding error here
  }

  // https://github.com/assertj/assertj/issues/2108#issuecomment-1088830619 with getEmail() added to UserDto

  @Test
  void should_pass_with_property_based_introspection_for_2108() {
    // GIVEN
    User user = new User();
    UserDTO userDto = new UserDTO(user);
    // WHEN/THEN
    then(user).usingRecursiveComparison()
              .withIntrospectionStrategy(COMPARING_PROPERTIES)
              .isEqualTo(userDto);
  }

  static class User {

    public String getEmail() {
      // Some complicated logic for email retrieval
      return "a@example.com";
    }
  }

  static class UserDTO {
    private final String email;

    UserDTO(User user) {
      this.email = user.getEmail();
    }

    @SuppressWarnings("unused")
    public String getEmail() {
      return email;
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
    ComparisonDifference valuesDifference = diff("values[1]", "B", "C");
    compareRecursivelyFailsWithDifferences(actual, expected, valuesDifference);

    // Note that this succeeds when it should not:
    // then(actual).usingRecursiveComparison().isEqualTo(expected);
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
    then(fox).usingRecursiveComparison()
             .withIntrospectionStrategy(ComparingLowercaseNormalizedFields.INSTANCE)
             .isEqualTo(foxDto);
    then(foxDto).usingRecursiveComparison()
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
    then(fox1).usingRecursiveComparison()
              .withIntrospectionStrategy(ComparingLowercaseNormalizedFields.INSTANCE)
              .isEqualTo(foxDto1);
    then(fox2).usingRecursiveComparison()
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
