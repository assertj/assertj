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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.ComparingFields.COMPARING_FIELDS;
import static org.assertj.core.api.recursive.comparison.ComparingProperties.COMPARING_PROPERTIES;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON_NORMALIZED;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.Test;

import com.google.common.base.CaseFormat;

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

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference phoneDifference = diff("phone.value", actual.phone.get(), expected.phone.get());
    ComparisonDifference neighbourDateOfBirthDifference = diff("neighbour.dateOfBirth",
                                                               actual.neighbour.dateOfBirth,
                                                               expected.neighbour.dateOfBirth);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, neighbourDateOfBirthDifference, phoneDifference);
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
    Author martinFowler = new Author("Martin", "Fowler", 58, "+447975777666", "+441611234567");
    Book refactoring = new Book("Refactoring", martinFowler);
    AuthorDto martinFowlerDto = new AuthorDto("Martin", "Fowler", 58, "+447975777666", "+441611234567");
    BookDto refactoringDto = new BookDto("Refactoring", martinFowlerDto);
    RecursiveComparisonIntrospectionStrategy comparingSnakeOrCamelCaseFields = new ComparingSnakeOrCamelCaseFields();

    // WHEN/THEN
    then(refactoring).usingRecursiveComparison()
                     .withIntrospectionStrategy(comparingSnakeOrCamelCaseFields)
                     .isEqualTo(refactoringDto);
  }

  static class Author {
    String firstName;
    String lastName;
    int age;
    String phoneNumber1;
    String phoneNumber2;

    Author(String firstName, String lastName, int age, String phoneNumber1, String phoneNumber2) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.age = age;
      this.phoneNumber1 = phoneNumber1;
      this.phoneNumber2 = phoneNumber2;
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

    AuthorDto(String firstName, String lastName, int age, String phoneNumber1, String phoneNumber2) {
      this.first_name = firstName;
      this.last_name = lastName;
      this._age = age;
      this.phone_number_1 = phoneNumber1;
      this.phone_number_2 = phoneNumber2;
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
  static class ComparingSnakeOrCamelCaseFields implements RecursiveComparisonIntrospectionStrategy {

    @Override
    public Set<String> getChildrenNodeNamesOf(Object node) {
      if (node == null) return new HashSet<>();
      Set<String> fieldsNames = Objects.getFieldsNames(node.getClass());
      return fieldsNames.stream().map(ComparingSnakeOrCamelCaseFields::toCamelCase).collect(toSet());
    }

    static String toCamelCase(String name) {
      if (!name.contains("_")) {
        return name;
      }
      if (name.startsWith("_")) {
        return toCamelCase(name.substring(1));  // avoid _last_name -> LastName
      }
      return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name);
    }

    @Override
    public Object getChildNodeValue(String childNodeName, Object instance) {
      return COMPARISON_NORMALIZED.getSimpleValue(childNodeName, instance);
    }

    @Override
    public String getDescription() {
      return "comparing camel case and snake case fields";
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
    String getTemplate();

    boolean isEmpty();
  }
  class HelloMessage implements Message {
    public final String getTemplate() {
      return "hello";
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

  }

  class GenericMessage implements Message {
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

  class Bean {
    private String string = null;

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
    private String email;

    UserDTO(User user) {
      this.email = user.getEmail();
    }

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

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference valuesDifference = diff("values[1]", "B", "C");
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, valuesDifference);

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
}
