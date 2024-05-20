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
package org.example.test;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.error.AssertJMultipleFailuresError;
import org.junit.jupiter.api.Test;

// this is not in an assertj package as we want to check the stack trace and we filter the element in assertj
class AssertJMultipleFailuresError_getMessage_Test {

  @Test
  void should_honor_description() {
    // GIVEN
    String description = "desc";
    AssertJMultipleFailuresError error = new AssertJMultipleFailuresError(description, list(new AssertionError("boom")));
    // WHEN
    String message = error.getMessage();
    // THEN
    then(message).startsWith(format("%n%s", description));
  }

  @Test
  void should_include_errors_count_and_clearly_separate_error_messages_in_soft_assertions_context() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(list("")).isEmpty();
    softly.assertThat(list("a", "b", "c")).as("isEmpty list").isEmpty();
    softly.assertThat("abc").isEmpty();
    softly.assertThat("abc").as("isEmpty string").isEmpty();
    softly.assertThat("abc").isEqualTo("bcd");
    softly.assertThat("abc").as("isEqualTo").isEqualTo("bcd");
    softly.assertThat(list("a", "b", "c")).as("contains").contains("e").doesNotContain("a");
    softly.assertThat(list("a", "b", "c")).contains("e").doesNotContain("a");
    // WHEN
    AssertionError error = expectAssertionError(() -> softly.assertAll());
    // THEN
    then(error).hasMessageContainingAll(format("%nMultiple Failures (10 failures)%n"),
                                        format("-- failure 1 --%n"),
                                        format("Expecting empty but was: [\"\"]%n"),
                                        format("-- failure 2 --%n"),
                                        format("[isEmpty list] %n"),
                                        format("Expecting empty but was: [\"a\", \"b\", \"c\"]%n"),
                                        format("-- failure 3 --%n"),
                                        format("Expecting empty but was: \"abc\"%n"),
                                        format("-- failure 4 --%n"),
                                        format("[isEmpty string] %n"),
                                        format("Expecting empty but was: \"abc\"%n"),
                                        format("-- failure 5 --"),
                                        format(shouldBeEqualMessage("\"abc\"", "\"bcd\"") + "%n"),
                                        format("-- failure 6 --%n"),
                                        format(shouldBeEqualMessage("isEqualTo", "\"abc\"", "\"bcd\"") + "%n"),
                                        format("-- failure 7 --%n"),
                                        format("[contains] %n"),
                                        format("Expecting ArrayList:%n"),
                                        format("  [\"a\", \"b\", \"c\"]%n"),
                                        format("to contain:%n"),
                                        format("  [\"e\"]%n"),
                                        format("but could not find the following element(s):%n"),
                                        format("  [\"e\"]%n"),
                                        format("%n"),
                                        format("-- failure 8 --%n"),
                                        format("[contains] %n"),
                                        format("Expecting%n"),
                                        format("  [\"a\", \"b\", \"c\"]%n"),
                                        format("not to contain%n"),
                                        format("  [\"a\"]%n"),
                                        format("but found%n"),
                                        format("  [\"a\"]%n"),
                                        format("%n"),
                                        format("-- failure 9 --%n"),
                                        format("Expecting ArrayList:%n"),
                                        format("  [\"a\", \"b\", \"c\"]%n"),
                                        format("to contain:%n"),
                                        format("  [\"e\"]%n"),
                                        format("but could not find the following element(s):%n"),
                                        format("  [\"e\"]%n"),
                                        format("%n"),
                                        format("-- failure 10 --%n"),
                                        format("Expecting%n"),
                                        format("  [\"a\", \"b\", \"c\"]%n"),
                                        format("not to contain%n"),
                                        format("  [\"a\"]%n"),
                                        format("but found%n"),
                                        format("  [\"a\"]%n"));
  }

  // also verifies that we don't add stack trace line numbers twice (in soft assertion
  // DefaultAssertionErrorCollector.decorateErrorsCollected and AssertJMultipleFailuresError
  @Test
  void should_include_stack_trace_allowing_to_navigate_to_the_failing_test_assertion_line_in_soft_assertions_context() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(list("")).isEmpty();
    softly.assertThat("abc").as("isEmpty string").isEmpty();
    softly.assertThat("abc").isEqualTo("bcd");
    // WHEN
    AssertionError error = expectAssertionError(() -> softly.assertAll());
    // THEN
    // @format:off
    then(error).isInstanceOf(AssertJMultipleFailuresError.class)
               .hasMessage(format("%nMultiple Failures (3 failures)%n" +
                                  "-- failure 1 --%n" +
                                  "Expecting empty but was: [\"\"]%n" +
                                  "at AssertJMultipleFailuresError_getMessage_Test.should_include_stack_trace_allowing_to_navigate_to_the_failing_test_assertion_line_in_soft_assertions_context(AssertJMultipleFailuresError_getMessage_Test.java:113)%n" +
                                  "-- failure 2 --%n" +
                                  "[isEmpty string] %n" +
                                  "Expecting empty but was: \"abc\"%n" +
                                  "at AssertJMultipleFailuresError_getMessage_Test.should_include_stack_trace_allowing_to_navigate_to_the_failing_test_assertion_line_in_soft_assertions_context(AssertJMultipleFailuresError_getMessage_Test.java:114)%n" +
                                  "-- failure 3 --"
                                  + shouldBeEqualMessage("\"abc\"", "\"bcd\"") + "%n" +
                                  "at AssertJMultipleFailuresError_getMessage_Test.should_include_stack_trace_allowing_to_navigate_to_the_failing_test_assertion_line_in_soft_assertions_context(AssertJMultipleFailuresError_getMessage_Test.java:115)"));
    // @format:on
  }

  @Test
  void should_include_stack_trace_allowing_to_navigate_to_the_failing_test_assertion_line_in_satisfies_assertion() {
    // WHEN
    AssertionError error = expectAssertionError(() -> then("abc").satisfies(value -> then(list(value)).isEmpty(),
                                                                            value -> then(value).as("isEmpty string").isEmpty(),
                                                                            value -> then(value).isEqualTo("bcd")));
    // THEN
    // @format:off
    then(error).isInstanceOf(AssertJMultipleFailuresError.class)
               .hasMessageContainingAll("AssertJMultipleFailuresError_getMessage_Test.java:138)",
                                        "AssertJMultipleFailuresError_getMessage_Test.java:139)",
                                        "AssertJMultipleFailuresError_getMessage_Test.java:140)");
    // @format:on
  }

  @Test
  void should_include_line_numbers() {
    // GIVEN
    AssertionError assertionError = new AssertionError("boom");
    // WHEN
    AssertJMultipleFailuresError error = new AssertJMultipleFailuresError("", list(assertionError));
    // THEN
    then(error).hasStackTraceContaining("AssertJMultipleFailuresError_getMessage_Test.java:153");
  }

}
