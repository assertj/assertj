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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldContainOnlyDigits.shouldContainOnlyDigits;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.error.ShouldContainOnlyDigits#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 */
public class ShouldContainOnlyDigits_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  public void setUp() {
    factory = shouldContainOnlyDigits("10$", '$', 2);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %n" +
                                  "Expecting:%n" +
                                  "  <\"10$\">%n" +
                                  "to contain only digits%n" +
                                  "but found non-digit character <'$'> at index <2>"));
  }

  @Test
  public void should_create_error_message_for_empty_string() {
    factory = shouldContainOnlyDigits("");
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %n" +
                                  "Expecting:%n" +
                                  "  <\"\">%n" +
                                  "to contain only digits%n" +
                                  "but could not found any digits at all"));
  }
}
