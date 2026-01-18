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
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.Optional;
import java.util.function.Predicate;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

/**
 * The assertions classes have to be in a package other than org.assertj to test
 * the behavior of line numbers for assertions defined outside the assertj package
 */
class SoftAssertionsLineNumberTest {

  @Test
  void should_print_line_numbers_of_failed_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(1)
          .isLessThan(0)
          .isLessThan(1);
    // WHEN
    var error = expectAssertionError(softly::assertAll);
    // THEN
    //@format:off
    then(error).hasMessageContainingAll("""
                                        Expecting actual:
                                          1
                                        to be less than:
                                          0\s
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_print_line_numbers_of_failed_assertions(SoftAssertionsLineNumberTest.java:38)",
                                        """
                                        Expecting actual:
                                          1
                                        to be less than:
                                          1\s
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_print_line_numbers_of_failed_assertions(SoftAssertionsLineNumberTest.java:39)");
    //@format:on
  }

  @Test
  void should_print_line_numbers_of_failed_assertions_even_if_it_came_from_nested_calls() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(Optional.empty()).contains("Foo");
    // nested proxied call to isNotNull
    softly.assertThat((Predicate<String>) null).accepts("a", "b", "c");
    Predicate<String> lowercasePredicate = s -> s.equals(s.toLowerCase());
    softly.assertThat(lowercasePredicate).accepts("a", "b", "C");
    // WHEN
    var error = expectAssertionError(softly::assertAll);
    // THEN
    //@format:off
    then(error).hasMessageContainingAll("""
                                        3 assertion errors:

                                        -- error 1 --
                                        Expecting Optional to contain:
                                          "Foo"
                                        but was empty.
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_print_line_numbers_of_failed_assertions_even_if_it_came_from_nested_calls(SoftAssertionsLineNumberTest.java:67)",
                                        """
                                        
                                        -- error 2 --
                                        Expecting actual not to be null
                                        first 3 stack trace elements:
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_print_line_numbers_of_failed_assertions_even_if_it_came_from_nested_calls(SoftAssertionsLineNumberTest.java:69)",
                                        """
                                        
                                        -- error 3 --
                                        Expecting all elements of:
                                          ["a", "b", "C"]
                                        to match given predicate but this element did not:
                                          "C"
                                        """.replaceAll("\\n", System.lineSeparator()),
                                        "should_print_line_numbers_of_failed_assertions_even_if_it_came_from_nested_calls(SoftAssertionsLineNumberTest.java:71)");
    //@format:on
  }

}
