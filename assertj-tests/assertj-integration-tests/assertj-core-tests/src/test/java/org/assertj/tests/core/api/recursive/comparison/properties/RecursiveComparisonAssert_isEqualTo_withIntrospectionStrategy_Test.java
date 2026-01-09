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
package org.assertj.tests.core.api.recursive.comparison.properties;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.ComparingProperties.COMPARING_PROPERTIES;
import static org.assertj.core.util.introspection.PropertyOrFieldSupport.COMPARISON;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.recursive.comparison.AbstractRecursiveComparisonIntrospectionStrategy;
import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonIntrospectionStrategy;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.introspection.IntrospectionError;
import org.assertj.tests.core.api.recursive.comparison.RecursiveComparisonAssert_BaseTest;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_isEqualTo_withIntrospectionStrategy_Test
    extends RecursiveComparisonAssert_BaseTest {

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

  // related to https://github.com/assertj/assertj/issues/2314 and https://github.com/assertj/assertj/issues/2108

  @Test
  void should_pass_with_property_based_introspection_for_2314() {
    // GIVEN
    Message actual = new HelloMessage();
    Message expected = new GenericMessage("hello");
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .withIntrospectionStrategy(COMPARING_PROPERTIES)
                .isEqualTo(expected);

    // note that the following assertion succeeds because the default behavior is to look for actual fields but not properties and
    // HelloMessage does not have fields
    // then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
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
    // WHEN
    Throwable throwable = catchThrowable(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                           .withIntrospectionStrategy(COMPARING_PROPERTIES)
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
    then(user).usingRecursiveComparison(recursiveComparisonConfiguration)
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

}
