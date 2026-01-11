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
package org.example.test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SoftAssertionsErrorDescriptionTest {

  private int initialMaxStackTraceElementsDisplayedValue;

  @BeforeEach
  public void beforeTest() {
    initialMaxStackTraceElementsDisplayedValue = StandardRepresentation.getMaxStackTraceElementsDisplayed();
  }

  @AfterEach
  public void afterTest() {
    StandardRepresentation.setMaxStackTraceElementsDisplayed(initialMaxStackTraceElementsDisplayedValue);
  }

  @Test
  public void should_display_the_error_cause_and_the_cause_first_stack_trace_elements() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    softly.fail("failure", throwRuntimeException());
    // WHEN
    var error = expectAssertionError(softly::assertAll);
    // THEN
    then(error).hasMessageContainingAll("cause message: abc",
                                        "cause first 3 stack trace elements:",
                                        "SoftAssertionsErrorDescriptionTest.throwRuntimeException(SoftAssertionsErrorDescriptionTest.java:55)");
  }

  protected static RuntimeException throwRuntimeException() {
    return new RuntimeException("abc");
  }
}
