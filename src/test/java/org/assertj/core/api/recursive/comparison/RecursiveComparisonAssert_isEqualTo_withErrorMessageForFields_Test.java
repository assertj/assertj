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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.OptionalInt;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.Person;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_isEqualTo_withErrorMessageForFields_Test extends
    RecursiveComparisonAssert_isEqualTo_BaseTest {

  private static final String ERROR_MESSAGE_DESCRIPTION_FOR_FIELDS = "- these fields had overridden error messages:";
  private static final String ERROR_MESSAGE_PRECEDENCE_DESCRIPTION_FOR_FIELDS = "- field custom messages take "
                                                                                + "precedence over type messages.";

  @Test
  void should_be_able_to_set_custom_error_message_for_specific_fields() {
    // GIVEN
    Person actual = new Person("Valera");
    actual.age = OptionalInt.of(15);

    Person expected = new Person("John");
    expected.age = OptionalInt.of(15);

    String message = "Name must be the same";
    String fieldLocation = "name";

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .withErrorMessageForFields(message,
                                                                                                            fieldLocation)
                                                                                 .isEqualTo(expected));

    // THEN
    then(assertionError).hasMessageContainingAll(message, ERROR_MESSAGE_DESCRIPTION_FOR_FIELDS, "- " + fieldLocation);
  }

  @Test
  void should_be_able_to_set_custom_error_message_with_arguments_for_specific_fields() {
    // GIVEN
    String actualName = "Valera";
    String expectedName = "John";

    Person actual = new Person(actualName);
    actual.age = OptionalInt.of(15);

    Person expected = new Person(expectedName);
    expected.age = OptionalInt.of(15);

    String fieldLocation = "name";
    String message = "Name must be the same:%n - Actual name is %s%n - Expected name is %s";
    List<Object> args = list(actualName, expectedName);

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .withErrorMessageForFields(message,
                                                                                                            args,
                                                                                                            fieldLocation)
                                                                                 .isEqualTo(expected));

    // THEN
    then(assertionError).hasMessageContainingAll(format(message, args.toArray()), ERROR_MESSAGE_DESCRIPTION_FOR_FIELDS,
                                                 "- " + fieldLocation);
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
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .withErrorMessageForFields(
                                                                                     fieldMessage,
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

  @Test
  void field_message_with_arguments_should_take_precedence_over_type_message() {
    // GIVEN
    String actualName = "Valera";
    String expectedName = "John";

    Person actual = new Person(actualName);
    actual.age = OptionalInt.of(15);

    Person expected = new Person(expectedName);
    expected.age = OptionalInt.of(15);

    String fieldLocation = "name";
    String fieldMessage = "Name must be the same:%n - Actual name is %s%n - Expected name is %s";
    List<Object> args = list(actualName, expectedName);

    String typeMessage = "Type message has to be ignored";

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison()
                                                                                 .withErrorMessageForFields(
                                                                                     fieldMessage,
                                                                                     args,
                                                                                     fieldLocation)
                                                                                 .withErrorMessageForType(typeMessage,
                                                                                                          String.class)
                                                                                 .isEqualTo(expected));

    // THEN
    then(assertionError).hasMessageContainingAll(format(fieldMessage, args.toArray()),
                                                 ERROR_MESSAGE_DESCRIPTION_FOR_FIELDS,
                                                 "- " + fieldLocation,
                                                 ERROR_MESSAGE_PRECEDENCE_DESCRIPTION_FOR_FIELDS)
                        .hasMessageNotContainingAny(typeMessage);
  }
}
