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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link MessageFormatter#format(Description, String, Object...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class MessageFormatter_format_Test {

  private DescriptionFormatter descriptionFormatter;
  private MessageFormatter messageFormatter;

  @BeforeEach
  public void setUp() {
    descriptionFormatter = spy(new DescriptionFormatter());
    messageFormatter = new MessageFormatter();
    messageFormatter.descriptionFormatter = descriptionFormatter;
  }

  @Test
  public void should_throw_error_if_format_string_is_null() {
    assertThatNullPointerException().isThrownBy(() -> messageFormatter.format(null, null, null));
  }

  @Test
  public void should_throw_error_if_args_array_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      Object[] args = null;
      messageFormatter.format(null, null, "", args);
    });
  }

  @Test
  public void should_format_message() {
    Description description = new TextDescription("Test");
    String s = messageFormatter.format(description, STANDARD_REPRESENTATION, "Hello %s", "World");
    assertThat(s).isEqualTo("[Test] Hello \"World\"");
    verify(descriptionFormatter).format(description);
  }

  @ParameterizedTest
  @MethodSource("messages")
  public void should_format_message_and_correctly_escape_percentage(String input, String formatted) {
    // GIVEN
    Description description = new TextDescription("Test");
    // WHEN
    String finalMessage = messageFormatter.format(description, STANDARD_REPRESENTATION, input);
    // THEN
    assertThat(finalMessage).isEqualTo("[Test] " + formatted);
  }

  public static Stream<Arguments> messages() {
    return Stream.of(Arguments.of("%E", "%E"),
                     Arguments.of("%%E", "%%E"),
                     Arguments.of("%%%E", "%%%E"),
                     Arguments.of("%n", format("%n")),
                     Arguments.of("%%%n%E", "%%" + format("%n") + "%E"),
                     Arguments.of("%%n", "%" + format("%n")));
  }
}
