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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.HexadecimalRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHaveSameSizeAs#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Nicolas François
 */
public class ShouldHaveSameSizeAs_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  public void setUp() {
    factory = shouldHaveSameSizeAs(list('a', 'b'), list('a', 'b', 'c', 'd'), 2, 4);
  }

  @Test
  public void should_create_error_message() {
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Actual and expected should have same size but actual size is:%n" +
                                         " <2>%n" +
                                         "while expected size is:%n" +
                                         " <4>%n" +
                                         "Actual was:%n" +
                                         " ['a', 'b']%n" +
                                         "Expected was:%n" +
                                         " ['a', 'b', 'c', 'd']"));
  }

  @Test
  public void should_create_error_message_with_hexadecimal_representation() {
    // WHEN
    String message = factory.create(new TextDescription("Test"), new HexadecimalRepresentation());
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Actual and expected should have same size but actual size is:%n" +
                                         " <2>%n" +
                                         "while expected size is:%n" +
                                         " <4>%n" +
                                         "Actual was:%n" +
                                         " ['0x0061', '0x0062']%n" +
                                         "Expected was:%n" +
                                         " ['0x0061', '0x0062', '0x0063', '0x0064']"));
  }
}
