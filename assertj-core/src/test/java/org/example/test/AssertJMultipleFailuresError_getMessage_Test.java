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

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.error.AssertJMultipleFailuresError;
import org.assertj.core.testkit.Person;
import org.junit.jupiter.api.Test;

// this is not in an assertj package as we want to check the stack trace, and we filter the element in assertj
class AssertJMultipleFailuresError_getMessage_Test {

  @Test
  void should_include_line_numbers() {
    // GIVEN
    var assertionError = new AssertionError("boom");
    // WHEN
    var error = new AssertJMultipleFailuresError("", list(assertionError));
    // THEN
    then(error).hasStackTraceContaining("AssertJMultipleFailuresError_getMessage_Test.java:35");
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
    var error = expectAssertionError(softly::assertAll);
    // THEN
    // @format:off
    then(error).isInstanceOf(AssertJMultipleFailuresError.class)
               .hasMessage(format("%nMultiple Failures (3 failures)%n" +
                                    "-- failure 1 --%n" +
                                    "Expecting empty but was: [\"\"]%n" +
                                    "at AssertJMultipleFailuresError_getMessage_Test.should_include_stack_trace_allowing_to_navigate_to_the_failing_test_assertion_line_in_soft_assertions_context(AssertJMultipleFailuresError_getMessage_Test.java:48)%n" +
                                    "-- failure 2 --%n" +
                                    "[isEmpty string] %n" +
                                    "Expecting empty but was: \"abc\"%n" +
                                    "at AssertJMultipleFailuresError_getMessage_Test.should_include_stack_trace_allowing_to_navigate_to_the_failing_test_assertion_line_in_soft_assertions_context(AssertJMultipleFailuresError_getMessage_Test.java:49)%n" +
                                    "-- failure 3 --"
                                    + shouldBeEqualMessage("\"abc\"", "\"bcd\"") + "%n" +
                                    "at AssertJMultipleFailuresError_getMessage_Test.should_include_stack_trace_allowing_to_navigate_to_the_failing_test_assertion_line_in_soft_assertions_context(AssertJMultipleFailuresError_getMessage_Test.java:50)"));
    // @format:on
  }

  @Test
  void should_include_stack_trace_allowing_to_navigate_to_the_failing_test_assertion_line_in_satisfies_assertion() {
    // WHEN
    var error = expectAssertionError(() -> then("abc").satisfies(value -> then(list(value)).isEmpty(),
                                                                 value -> then(value).as("isEmpty string").isEmpty(),
                                                                 value -> then(value).isEqualTo("bcd")));
    // THEN
    then(error).isInstanceOf(AssertJMultipleFailuresError.class)
               .hasMessageContainingAll("AssertJMultipleFailuresError_getMessage_Test.java:73)",
                                        "AssertJMultipleFailuresError_getMessage_Test.java:74)",
                                        "AssertJMultipleFailuresError_getMessage_Test.java:75)");
  }

  @Test
  void should_honor_description_and_show_root_object() {
    // GIVEN
    var assertionError = new AssertionError("%nboom".formatted());
    var error = new AssertJMultipleFailuresError("desc", new Person("tim"), list(assertionError));
    // WHEN
    String message = error.getMessage();
    // THEN
    then(message).startsWith(format("%n" +
                                    "For Person[name='tim'],%n" +
                                    "desc (1 failure)%n" +
                                    "-- failure 1 --%n" +
                                    "boom%n" +
                                    "at AssertJMultipleFailuresError_getMessage_Test"));
  }

  @Test
  void should_honor_description() {
    // GIVEN
    String description = "desc";
    var error = new AssertJMultipleFailuresError(description, list(new AssertionError("boom")));
    // WHEN
    String message = error.getMessage();
    // THEN
    then(message).startsWith("%n%s".formatted(description));
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
    var error = expectAssertionError(softly::assertAll);
    // THEN
    then(error).hasMessageContainingAll("%nMultiple Failures (10 failures)%n".formatted(),
                                        "-- failure 1 --%n".formatted(),
                                        "Expecting empty but was: [\"\"]%n".formatted(),
                                        "-- failure 2 --%n".formatted(),
                                        "[isEmpty list] %n".formatted(),
                                        "Expecting empty but was: [\"a\", \"b\", \"c\"]%n".formatted(),
                                        "-- failure 3 --%n".formatted(),
                                        "Expecting empty but was: \"abc\"%n".formatted(),
                                        "-- failure 4 --%n".formatted(),
                                        "[isEmpty string] %n".formatted(),
                                        "Expecting empty but was: \"abc\"%n".formatted(),
                                        "-- failure 5 --%n".formatted(),
                                        format(shouldBeEqualMessage("\"abc\"", "\"bcd\"") + "%n"),
                                        "-- failure 6 --%n".formatted(),
                                        format(shouldBeEqualMessage("isEqualTo", "\"abc\"", "\"bcd\"") + "%n"),
                                        "-- failure 7 --%n".formatted(),
                                        "[contains] %n".formatted(),
                                        "Expecting ArrayList:%n".formatted(),
                                        "  [\"a\", \"b\", \"c\"]%n".formatted(),
                                        "to contain:%n".formatted(),
                                        "  [\"e\"]%n".formatted(),
                                        "but could not find the following element(s):%n".formatted(),
                                        "  [\"e\"]%n".formatted(),
                                        "%n".formatted(),
                                        "-- failure 8 --%n".formatted(),
                                        "[contains] %n".formatted(),
                                        "Expecting%n".formatted(),
                                        "  [\"a\", \"b\", \"c\"]%n".formatted(),
                                        "not to contain%n".formatted(),
                                        "  [\"a\"]%n".formatted(),
                                        "but found%n".formatted(),
                                        "  [\"a\"]%n".formatted(),
                                        "%n".formatted(),
                                        "-- failure 9 --%n".formatted(),
                                        "Expecting ArrayList:%n".formatted(),
                                        "  [\"a\", \"b\", \"c\"]%n".formatted(),
                                        "to contain:%n".formatted(),
                                        "  [\"e\"]%n".formatted(),
                                        "but could not find the following element(s):%n".formatted(),
                                        "  [\"e\"]%n".formatted(),
                                        "%n".formatted(),
                                        "-- failure 10 --%n".formatted(),
                                        "Expecting%n".formatted(),
                                        "  [\"a\", \"b\", \"c\"]%n".formatted(),
                                        "not to contain%n".formatted(),
                                        "  [\"a\"]%n".formatted(),
                                        "but found%n".formatted(),
                                        "  [\"a\"]%n".formatted());
  }

}
