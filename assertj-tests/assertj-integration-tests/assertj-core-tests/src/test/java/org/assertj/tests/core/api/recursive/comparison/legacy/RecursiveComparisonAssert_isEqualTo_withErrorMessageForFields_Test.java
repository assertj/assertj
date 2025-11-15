/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.api.recursive.comparison.legacy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.OptionalInt;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_withErrorMessageForFields_Test extends WithLegacyIntrospectionStrategyBaseTest {

  private static final String ERROR_MESSAGE_DESCRIPTION_FOR_FIELDS = "- these fields had overridden error messages:";
  private static final String ERROR_MESSAGE_PRECEDENCE_DESCRIPTION_FOR_FIELDS = "- field custom messages take precedence over type messages.";

  @Test
  void should_be_able_to_set_custom_error_message_for_specific_fields() {
    // GIVEN
    Person actual = new Person("Valera");
    actual.age = OptionalInt.of(15);
    Person expected = new Person("John");
    expected.age = OptionalInt.of(16);
    String message = "Name must be the same";
    String field = "name";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .withErrorMessageForFields(message, field)
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContainingAll(message, ERROR_MESSAGE_DESCRIPTION_FOR_FIELDS, "- " + field);
  }

  @Test
  void field_message_should_take_precedence_over_type_message() {
    // GIVEN
    Person actual = new Person("Valera");
    actual.age = OptionalInt.of(15);
    Person expected = new Person("John");
    expected.age = OptionalInt.of(15);
    String fieldMessage = "Name must be the same";
    String fieldLocation = "name";
    String typeMessage = "Type message has to be ignored";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .withErrorMessageForFields(fieldMessage,
                                                                                                 fieldLocation)
                                                                      .withErrorMessageForType(typeMessage,
                                                                                               String.class)
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContainingAll(fieldMessage,
                                                 ERROR_MESSAGE_DESCRIPTION_FOR_FIELDS,
                                                 "- " + fieldLocation,
                                                 ERROR_MESSAGE_PRECEDENCE_DESCRIPTION_FOR_FIELDS)
                        .hasMessageNotContainingAny(typeMessage);
  }

  @ParameterizedTest
  @MethodSource
  void should_use_message_for_type_when_possible(final Person actual, final Person expected) {
    // GIVEN
    String customMessage = "custom message 1234";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .withErrorMessageForType(customMessage,
                                                                                               String.class)
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContainingAll(customMessage, "name");
  }

  static Stream<Arguments> should_use_message_for_type_when_possible() {
    // Use the custom message whenever either the expected or the actual name is not null.
    return Stream.of(arguments(new Person("Alice"), new Person("Bob")),
                     arguments(new Person(null), new Person("Bob")),
                     arguments(new Person("Alice"), new Person(null)));
  }

  @Test
  void should_use_default_message_when_expected_and_actual_fields_are_both_null() {
    // GIVEN
    Person actual = new Person(null);
    Person expected = new Person(null);
    BiPredicate<String, String> alwaysFalse = (n1, n2) -> false;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .withErrorMessageForType("custom message not used",
                                                                                               String.class)
                                                                      .withEqualsForFieldsMatchingRegexes(alwaysFalse,
                                                                                                          "name")
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContainingAll("field/property 'name' differ");
  }
}
