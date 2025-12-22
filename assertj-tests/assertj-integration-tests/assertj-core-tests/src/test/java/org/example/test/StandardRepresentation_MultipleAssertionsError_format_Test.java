/*
 * Copyright 2012-2025 the original author or authors.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.description.TextDescription;
import org.assertj.core.error.MultipleAssertionsError;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * this is not in a org.assertj package to avoid assertj packages in the stack trace that is handled specifically and
 * does not reflect real users code.
 */
class StandardRepresentation_MultipleAssertionsError_format_Test {

  private static final StandardRepresentation REPRESENTATION = new StandardRepresentation();
  private static final List<AssertionError> throwables = List.of(expectAssertionError(() -> assertThat("foo").startsWith("bar")),
                                                                 expectAssertionError(() -> assertThat("baz").isEqualTo("bar")));
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
  void should_not_display_stacktrace_if_maxStackTraceElementsDisplayed_is_zero() {
    // GIVEN
    StandardRepresentation.setMaxStackTraceElementsDisplayed(0);
    var multipleAssertionsError = new MultipleAssertionsError(null, null, throwables);
    // WHEN
    String toString = REPRESENTATION.toStringOf(multipleAssertionsError);
    // THEN
    //@format:off
    then(toString).isEqualTo("""
                               2 assertion errors:
                               
                               -- error 1 --
                               Expecting actual:
                                 "foo"
                               to start with:
                                 "bar"
                               
                               -- error 2 --
                               expected: "bar"
                                but was: "baz\"""".replaceAll("\\n", System.lineSeparator()));
    //@format:on
  }

  @Test
  void should_display_the_configured_number_of_stacktrace_elements() {
    // GIVEN
    StandardRepresentation.setMaxStackTraceElementsDisplayed(2);
    var multipleAssertionsError = new MultipleAssertionsError(null, null, throwables);
    // WHEN
    String toString = REPRESENTATION.toStringOf(multipleAssertionsError);
    // THEN
    then(toString).containsSubsequence("2 assertion errors:",
                                       "-- error 1 --",
                                       "first 2 stack trace elements:",
                                       "-- error 2 --",
                                       "first 2 stack trace elements:");
  }

  @Test
  void should_display_the_full_stacktrace() {
    // GIVEN
    StandardRepresentation.setMaxStackTraceElementsDisplayed(100);
    var multipleAssertionsError = new MultipleAssertionsError(null, null, throwables);
    // WHEN
    String toString = REPRESENTATION.toStringOf(multipleAssertionsError);
    // THEN
    then(toString).containsSubsequence("2 assertion errors:",
                                       "-- error 1 --",
                                       "first 100 stack trace elements:")
                  .doesNotContain("remaining lines not displayed");
  }

  @Test
  void should_display_toString_when_null_stack() {
    // GIVEN
    Throwable throwable = mock();
    when(throwable.toString()).thenReturn("throwable string");
    // WHEN
    String actual = REPRESENTATION.toStringOf(throwable);
    // THEN
    then(actual).isEqualTo("throwable string");
  }

  @Test
  void should_honor_description() {
    // GIVEN
    String description = "assertions description";
    var multipleAssertionsError = new MultipleAssertionsError(new TextDescription(description), null, throwables);
    // WHEN
    String errorRepresentation = REPRESENTATION.toStringOf(multipleAssertionsError);
    // THEN
    then(errorRepresentation).containsSubsequence("[assertions description]",
                                                  "2 assertion errors:");
  }
}
