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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ShouldHaveCause create")
class ShouldHaveCause_create_Test {

  @Test
  void should_create_error_message_for_actual_cause() {
    // GIVEN
    Throwable actual = new RuntimeException();
    // WHEN
    String message = shouldHaveCause(actual).create();
    // THEN
    then(message).isEqualTo(format("Expecting actual throwable to have a cause but it did not, actual was:%n%s",
                                   STANDARD_REPRESENTATION.toStringOf(actual)));
  }

  @Test
  void should_create_error_message_for_actual_and_expected_cause() {
    // GIVEN
    Throwable actual = new RuntimeException("Boom");
    Throwable expected = new IllegalStateException("illegal state");
    // WHEN
    String message = shouldHaveCause(actual, expected).create();
    // THEN
    then(message).isEqualTo(format("%n" +
                                   "Expecting a cause with type:%n" +
                                   "  \"java.lang.IllegalStateException\"%n" +
                                   "and message:%n" +
                                   "  \"illegal state\"%n" +
                                   "but type was:%n" +
                                   "  \"java.lang.RuntimeException\"%n" +
                                   "and message was:%n" +
                                   "  \"Boom\"."));
  }

  @Test
  void should_create_error_message_for_actual_and_expected_cause_same_type_different_message() {
    // GIVEN
    Throwable actual = new RuntimeException("Boom");
    Throwable expected = new RuntimeException("something went wrong");
    // WHEN
    String message = shouldHaveCause(actual, expected).create();
    // THEN
    then(message).isEqualTo(format("%n" +
                                   "Expecting a cause with message:%n" +
                                   "  \"something went wrong\"%n" +
                                   "but message was:%n" +
                                   "  \"Boom\"."));
  }

  @Test
  void should_create_error_message_for_actual_and_expected_cause_same_message_different_type() {
    // GIVEN
    Throwable actual = new RuntimeException("Boom");
    Throwable expected = new IllegalStateException("Boom");
    // WHEN
    String message = shouldHaveCause(actual, expected).create();
    // THEN
    then(message).isEqualTo(format("%n" +
                                   "Expecting a cause with type:%n" +
                                   "  \"java.lang.IllegalStateException\"%n" +
                                   "but type was:%n" +
                                   "  \"java.lang.RuntimeException\"."));
  }

  @Test
  void should_create_error_message_for_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    Throwable expected = new IllegalStateException("Boom");
    // WHEN
    String message = shouldHaveCause(actual, expected).create();
    // THEN
    then(message).isEqualTo(format("%n" +
                                   "Expecting a cause with type:%n" +
                                   "  \"java.lang.IllegalStateException\"%n" +
                                   "and message:%n" +
                                   "  \"Boom\"%n" +
                                   "but actualCause had no cause."));
  }
}
