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
 * Copyright 2012-2019 the original author or authors.
 */
package org.example.test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.Optional;
import java.util.function.Predicate;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

/**
 * This test has to be in a package other than org.assertj because otherwise the
 * line number information will be removed by the assertj filtering of internal lines.
 * {@link org.assertj.core.util.Throwables#removeAssertJRelatedElementsFromStackTrace}
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
    AssertionError error = catchThrowableOfType(softly::assertAll, AssertionError.class);
    // THEN
    assertThat(error).hasMessageContaining(format("%n"
                                                  + "Expecting:%n"
                                                  + " <1>%n"
                                                  + "to be less than:%n"
                                                  + " <0> %n"
                                                  + "at SoftAssertionsLineNumberTest.should_print_line_numbers_of_failed_assertions(SoftAssertionsLineNumberTest.java:37)%n"))
                     .hasMessageContaining(format("%n"
                                                  + "Expecting:%n"
                                                  + " <1>%n"
                                                  + "to be less than:%n"
                                                  + " <1> %n"
                                                  + "at SoftAssertionsLineNumberTest.should_print_line_numbers_of_failed_assertions(SoftAssertionsLineNumberTest.java:38)"));
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
    AssertionError error = catchThrowableOfType(softly::assertAll, AssertionError.class);
    // THEN
    assertThat(error).hasMessageContaining(format("%n"
                                                  + "Expecting Optional to contain:%n"
                                                  + "  <\"Foo\">%n"
                                                  + "but was empty.%n"
                                                  + "at SoftAssertionsLineNumberTest.should_print_line_numbers_of_failed_assertions_even_if_it_came_from_nested_calls(SoftAssertionsLineNumberTest.java:60)%n"))
                     .hasMessageContaining(format("%n"
                                                  + "Expecting actual not to be null%n"
                                                  + "at SoftAssertionsLineNumberTest.should_print_line_numbers_of_failed_assertions_even_if_it_came_from_nested_calls(SoftAssertionsLineNumberTest.java:62)%n"))
                     .hasMessageContaining(format("%n"
                                                  + "Expecting all elements of:%n"
                                                  + "  <[\"a\", \"b\", \"C\"]>%n"
                                                  + "to match given predicate but this element did not:%n"
                                                  + "  <\"C\">%n"
                                                  + "at SoftAssertionsLineNumberTest.should_print_line_numbers_of_failed_assertions_even_if_it_came_from_nested_calls(SoftAssertionsLineNumberTest.java:64)"));
  }

}
