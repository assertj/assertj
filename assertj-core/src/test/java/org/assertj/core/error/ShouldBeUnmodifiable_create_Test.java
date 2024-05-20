/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeUnmodifiable.shouldBeUnmodifiable;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldBeUnmodifiable_create_Test {

  @Test
  void should_create_error_message_with_String() {
    // GIVEN
    ErrorMessageFactory underTest = shouldBeUnmodifiable("method()");
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual to be unmodifiable, but invoking%n" +
                                   "  \"method()\"%n" +
                                   "succeeded instead of throwing java.lang.UnsupportedOperationException"));
  }

  @Test
  void should_create_error_message_with_String_and_RuntimeException_without_message() {
    // GIVEN
    ErrorMessageFactory underTest = shouldBeUnmodifiable("method()", new RuntimeException());
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual to be unmodifiable, but invoking%n" +
                                   "  \"method()\"%n" +
                                   "thrown%n" +
                                   "  \"java.lang.RuntimeException\"%n" +
                                   "instead of java.lang.UnsupportedOperationException"));
  }

  @Test
  void should_create_error_message_with_String_and_RuntimeException_with_message() {
    // GIVEN
    ErrorMessageFactory underTest = shouldBeUnmodifiable("method()", new RuntimeException("message"));
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual to be unmodifiable, but invoking%n" +
                                   "  \"method()\"%n" +
                                   "thrown%n" +
                                   "  \"java.lang.RuntimeException: message\"%n" +
                                   "instead of java.lang.UnsupportedOperationException"));
  }

}
