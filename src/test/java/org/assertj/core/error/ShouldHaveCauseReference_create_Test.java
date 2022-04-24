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
import static org.assertj.core.error.ShouldHaveCauseReference.shouldHaveCauseReference;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHaveCauseReference#shouldHaveCauseReference(Throwable, Throwable)}</code>.
 *
 * @author weiyilei
 */
class ShouldHaveCauseReference_create_Test {

  private static final TestDescription DESCRIPTION = new TestDescription("TEST");
  /**
   * Test1 for null actual cause reference
   */
  @Test
  void should_create_error_message_for_expected_without_actual1() {
    // GIVEN
    Throwable actualCause = new RuntimeException().getCause();
    Throwable expectedCause = new IllegalStateException();
    // WHEN
    String message = shouldHaveCauseReference(actualCause, expectedCause).create();
    // THEN
    then(message).isEqualTo(format("Expecting actual cause reference to be:%n" +
        "  java.lang.IllegalStateException%n" +
        "but was:%n" +
        "  null"));
  }
  /**
   * Test2 for null actual cause reference
   */
  @Test
  void should_create_error_message_for_expected_without_actual2(){
    // GIVEN
    Throwable actualCause = new RuntimeException().getCause();
    Throwable expectedCause = new OutOfMemoryError();
    // WHEN
    String message = shouldHaveCauseReference(actualCause, expectedCause).create();
    then(message).isEqualTo(format("Expecting actual cause reference to be:%n" +
      "  java.lang.OutOfMemoryError%n" +
      "but was:%n" +
      "  null"));
  }
  /**
   * Test1 for not null actual cause reference
   */
  @Test
  void should_create_error_message_for_expected_with_actual1() {
    // GIVEN
    Throwable expectedCause = new IllegalStateException();
    Throwable actualCause = new IllegalAccessError("oops...");
    Throwable actual = new RuntimeException(actualCause);
    // WHEN
    String message = shouldHaveCauseReference(actualCause, expectedCause).create();
    // THEN
    then(message).isEqualTo(format("Expecting actual cause reference to be:%n" +
      "  java.lang.IllegalStateException%n" +
      "but was:%n" +
      "  java.lang.IllegalAccessError: oops...%n" +
      "Throwable that failed the check:%n" +
      "%s",getStackTrace(actualCause)));
  }
  /**
   * Test2 for not null actual cause reference
   */
  @Test
  void should_create_error_message_for_expected_with_actual2(){
    // GIVEN
    Throwable expectedCause = new OutOfMemoryError();
    Throwable actualCause = new IllegalAccessError("Test");
    Throwable actual = new RuntimeException(actualCause);
    // WHEN
    then(message).isEqualTo(format("Expecting actual cause reference to be:%n" +
      "  java.lang.OutOfMemoryError%n" +
      "but was:%n" +
      "  java.lang.IllegalAccessError: Test%n" +
      "Throwable that failed the check:%n" +
      "%s",getStackTrace(actualCause));
  }
}
