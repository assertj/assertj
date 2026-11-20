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
package org.assertj.tests.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.IndentWrapper;
import org.assertj.core.error.MessageFormatter;
import org.assertj.core.presentation.CompositeRepresentation;
import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Alex Ruiz
 */
class MessageFormatter_format_Test {

  private final MessageFormatter messageFormatter = MessageFormatter.instance();

  @Test
  void should_throw_error_if_format_string_is_null() {
    thenNullPointerException().isThrownBy(() -> messageFormatter.format(null, null, null));
  }

  @Test
  void should_throw_error_if_args_array_is_null() {
    Object[] args = null;
    thenNullPointerException().isThrownBy(() -> messageFormatter.format(null, null, "", args));
  }

  @Test
  void should_format_message() {
    // GIVEN
    Description description = new TextDescription("Test");
    // WHEN
    String s = messageFormatter.format(description, STANDARD_REPRESENTATION, "Hello %s", "World");
    // THEN
    then(s).isEqualTo("[Test] Hello \"World\"");
  }

  @ParameterizedTest
  @MethodSource("messages")
  void should_format_message_and_correctly_escape_percentage(String input, String formatted) {
    // GIVEN
    Description description = new TextDescription("Test");
    // WHEN
    String finalMessage = messageFormatter.format(description, STANDARD_REPRESENTATION, input);
    // THEN
    then(finalMessage).isEqualTo("[Test] " + formatted);
  }

  public static Stream<Arguments> messages() {
    return Stream.of(arguments("%E", "%E"),
                     arguments("%%E", "%%E"),
                     arguments("%%%E", "%%%E"),
                     arguments("%n", "%n".formatted()),
                     arguments("%%%n%E", "%%" + "%n".formatted() + "%E"),
                     arguments("%%n", "%" + "%n".formatted()));
  }

  @Test
  void should_disambiguate_enums_in_indented_arguments() {
    // GIVEN
    var actual = IndentWrapper.of(List.of(First.VALUE));
    var expected = List.of(Second.VALUE);
    // WHEN
    var message = messageFormatter.format(null, STANDARD_REPRESENTATION, "%s != %s", actual, expected);
    // THEN
    then(message).contains("VALUE (" + First.class.getName() + "@")
                 .contains("VALUE (" + Second.class.getName() + "@");
  }

  @Test
  void should_preserve_custom_representations_in_composite() {
    // GIVEN
    Representation customRepresentation = value -> "custom group";
    var representation = new CompositeRepresentation(List.of(customRepresentation));
    // WHEN
    var message = messageFormatter.format(null, representation, "%s != %s", List.of(First.VALUE), List.of(Second.VALUE));
    // THEN
    then(message).isEqualTo("custom group != custom group");
  }

  private enum First {
    VALUE
  }

  private enum Second {
    VALUE
  }
}
