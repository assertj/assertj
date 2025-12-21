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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.AssertionErrorCreator;
import org.assertj.core.error.MultipleAssertionsError;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

// this is not in an assertj package as we want to check the stack trace, and we filter the element in assertj
class MultipleAssertionsError_getMessage_Test {

  private static final int DEFAULT_MAX_STACK_TRACE_ELEMENTS = StandardRepresentation.getMaxStackTraceElementsDisplayed();

  @AfterEach
  void afterEach() {
    // restore default value
    StandardRepresentation.setMaxStackTraceElementsDisplayed(DEFAULT_MAX_STACK_TRACE_ELEMENTS);
  }

  @Test
  void should_include_stack_trace_allowing_to_navigate_to_the_failing_assertion_line_in_satisfies_assertion() {
    // WHEN
    var error = expectAssertionError(() -> then("abc").satisfies(value -> then(list(value)).isEmpty(),
                                                                 value -> then(value).as("isEmpty string").isEmpty(),
                                                                 value -> then(value).isEqualTo("bcd")));
    // THEN
    then(error).isInstanceOf(MultipleAssertionsError.class)
               .hasMessageContainingAll("MultipleAssertionsError_getMessage_Test.java:47)",
                                        "MultipleAssertionsError_getMessage_Test.java:48)",
                                        "MultipleAssertionsError_getMessage_Test.java:49)");
  }

  @Test
  void should_show_description_and_object_under_test() {
    // GIVEN
    var assertionError = new AssertionError("boom");
    var error = new MultipleAssertionsError(new TextDescription("desc"), new Person("tim"), list(assertionError));
    // WHEN
    String message = error.getMessage();
    // THEN
    then(message).startsWith("""
        [desc]\s
        1 assertion error for: Person[name='tim']

        -- error 1 --
        boom
        first 3 stack trace elements:
        """.replaceAll("\\n", System.lineSeparator()));
  }

  @Test
  void should_honor_description() {
    // GIVEN
    var error = new MultipleAssertionsError(new TextDescription("desc"), null, list(new AssertionError("boom")));
    // WHEN
    String message = error.getMessage();
    // THEN
    then(message).startsWith("""
        [desc]\s
        1 assertion error:

        -- error 1 --
        boom
        first 3 stack trace elements:
        """.replaceAll("\\n", System.lineSeparator()));
  }

  @Test
  void should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line() {
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
    //@format:off
    then(error).hasMessageContainingAll("10 assertion errors",
                                        """

                                        -- error 1 --
                                        Expecting empty but was: [""]
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:96)",
                                        """

                                        -- error 2 --
                                        [isEmpty list]\s
                                        Expecting empty but was: ["a", "b", "c"]
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:97)",
                                        """

                                        -- error 3 --
                                        Expecting empty but was: "abc"
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:98)",
                                        """

                                        -- error 4 --
                                        [isEmpty string]\s
                                        Expecting empty but was: "abc"
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:99)",
                                        """

                                        -- error 5 --
                                        expected: "bcd"
                                         but was: "abc"
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:100)",
                                        """

                                        -- error 6 --
                                        [isEqualTo]\s
                                        expected: "bcd"
                                         but was: "abc"
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:101)",
                                        """

                                        -- error 7 --
                                        [contains]\s
                                        Expecting ArrayList:
                                          ["a", "b", "c"]
                                        to contain:
                                          ["e"]
                                        but could not find the following element(s):
                                          ["e"]

                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:102)",
                                        """

                                        -- error 8 --
                                        [contains]\s
                                        Expecting
                                          ["a", "b", "c"]
                                        not to contain
                                          ["a"]
                                        but found
                                          ["a"]

                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:102)",
                                        """

                                        -- error 9 --
                                        Expecting ArrayList:
                                          ["a", "b", "c"]
                                        to contain:
                                          ["e"]
                                        but could not find the following element(s):
                                          ["e"]

                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:103)",
                                        """

                                        -- error 10 --
                                        Expecting
                                          ["a", "b", "c"]
                                        not to contain
                                          ["a"]
                                        but found
                                          ["a"]

                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_include_errors_count_and_allow_to_navigate_to_the_failing_soft_assertion_line(MultipleAssertionsError_getMessage_Test.java:103)");
    //@format:on
  }

  @Test
  void should_honor_maxStackTraceElementsDisplayed_setting_in_each_assertion_error() {
    // GIVEN
    var assertionError1 = new AssertionError("boom");
    var assertionError2 = new AssertionError("bam");
    StandardRepresentation.setMaxStackTraceElementsDisplayed(10);
    // WHEN
    var error = new AssertionErrorCreator().multipleAssertionsError(List.of(assertionError1, assertionError2));
    // THEN
    then(error).message().contains("first 10 stack trace elements:");
  }

  @Test
  void should_show_object_under_test() {
    // GIVEN
    var assertionError = new AssertionError("boom");
    var error = new MultipleAssertionsError(null, new Person("tim"), list(assertionError));
    // WHEN
    String message = error.getMessage();
    // THEN
    then(message).startsWith("""
        1 assertion error for: Person[name='tim']

        -- error 1 --
        boom
        first 3 stack trace elements:
        """.replaceAll("\\n", System.lineSeparator()));
  }

  @Test
  void satisfies_should_include_object_under_test_in_error_message() {
    // WHEN
    var error = expectAssertionError(() -> then("abc").satisfies(value -> then(value).as("isEmpty string").isEmpty(),
                                                                 value -> then(value).isEqualTo("bcd")));
    // THEN
    then(error).isInstanceOf(MultipleAssertionsError.class)
               .hasMessageContainingAll("2 assertion errors for: abc", "[isEmpty string]");
  }

}
