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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstanceButWasNull;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.io.File;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeInstance#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ShouldBeInstance_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeInstance("Yoda", File.class);
    // WHEN
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to be an instance of:%n" +
                                   "  java.io.File%n" +
                                   "but was instance of:%n" +
                                   "  java.lang.String"));
  }

  @Test
  void should_create_error_message_with_stack_trace_for_throwable() {
    // GIVEN
    IllegalArgumentException actual = new IllegalArgumentException("Not a file");
    // WHEN
    String message = shouldBeInstance(actual, File.class).create();
    // THEN
    then(message).isEqualTo(format("%nExpecting actual throwable to be an instance of:%n" +
                                   "  java.io.File%n" +
                                   "but was:%n" +
                                   "  %s",
                                   STANDARD_REPRESENTATION.toStringOf(actual)));
  }

  @Test
  void should_create_shouldBeInstanceButWasNull_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeInstanceButWasNull("other", File.class);
    // WHEN
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting object:%n" +
                                   "  \"other\"%n" +
                                   "to be an instance of:%n" +
                                   "  <java.io.File>%n" +
                                   "but was null"));
  }
}
