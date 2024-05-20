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
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainSubsequenceOfCharSequence.shouldContainSubsequence;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.Arrays.array;

import java.util.stream.Stream;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for <code>{@link ShouldContainSubsequenceOfCharSequence#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Joel Costigliola
 */
class ShouldContainSubsequenceOfCharSequence_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    String[] sequenceValues = { "{", "author", "title", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    ErrorMessageFactory factory = shouldContainSubsequence(actual, sequenceValues, 1);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n" +
                                   "  \"" + actual + "\"%n" +
                                   "to contain the following CharSequences in this order (possibly with other values between them):%n"
                                   +
                                   "  [\"{\", \"author\", \"title\", \"}\"]%n" +
                                   "but \"title\" was found before \"author\"%n"));
  }

  @Test
  void should_create_error_message_with_custom_comparison_strategy() {
    // GIVEN
    String[] sequenceValues = { "{", "author", "title", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    ErrorMessageFactory factory = shouldContainSubsequence(actual, sequenceValues, 1,
                                                           new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n" +
                                   "  \"" + actual + "\"%n" +
                                   "to contain the following CharSequences in this order (possibly with other values between them):%n"
                                   +
                                   "  [\"{\", \"author\", \"title\", \"}\"]%n" +
                                   "but \"title\" was found before \"author\"%n" +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }

  @ParameterizedTest(name = "Testing {0} occurrence of title")
  @MethodSource
  void should_create_error_message_indicating_duplicate_subsequence(int occurrence, String[] sequenceValues, String actual,
                                                                    String expectedErrorMessage) {
    ErrorMessageFactory factory = shouldContainSubsequence(actual, sequenceValues, mapOf(entry("title", occurrence - 1)),
                                                           new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));

    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

    // THEN
    then(message).isEqualTo(expectedErrorMessage);
  }

  private static Stream<Arguments> should_create_error_message_indicating_duplicate_subsequence() {
    String actual2ndNotFound = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
    String actual3rdNotFound = "{ 'title':'A Game of Thrones', 'author':'George Martin', 'title':'A Clash of Kings', 'author':'George Martin'}";
    String actual4thNotFound = "{ 'title':'A Game of Thrones', 'author':'George Martin', 'title':'A Clash of Kings', 'author':'George Martin', 'title':'A Storm of Swords', 'author':'George Martin'}";
    return Stream.of(Arguments.of(
                                  2,
                                  array("{", "title", "author", "title", "}"),
                                  actual2ndNotFound,
                                  format("[Test] %nExpecting actual:%n" +
                                         "  \"" + actual2ndNotFound + "\"%n" +
                                         "to contain the following CharSequences in this order (possibly with other values between them):%n"
                                         +
                                         "  [\"{\", \"title\", \"author\", \"title\", \"}\"]%n" +
                                         "But the 2nd occurrence of \"title\" was not found%n" +
                                         "when comparing values using CaseInsensitiveStringComparator")),
                     Arguments.of(
                                  3,
                                  array("{", "title", "author", "title", "title", "}"),
                                  actual3rdNotFound,
                                  format("[Test] %nExpecting actual:%n" +
                                         "  \"" + actual3rdNotFound + "\"%n" +
                                         "to contain the following CharSequences in this order (possibly with other values between them):%n"
                                         +
                                         "  [\"{\", \"title\", \"author\", \"title\", \"title\", \"}\"]%n" +
                                         "But the 3rd occurrence of \"title\" was not found%n" +
                                         "when comparing values using CaseInsensitiveStringComparator")),
                     Arguments.of(4,
                                  array("{", "title", "author", "title", "title", "title", "}"),
                                  actual4thNotFound,
                                  format("[Test] %nExpecting actual:%n" +
                                         "  \"" + actual4thNotFound + "\"%n" +
                                         "to contain the following CharSequences in this order (possibly with other values between them):%n"
                                         +
                                         "  [\"{\", \"title\", \"author\", \"title\", \"title\", \"title\", \"}\"]%n" +
                                         "But the 4th occurrence of \"title\" was not found%n" +
                                         "when comparing values using CaseInsensitiveStringComparator")));
  }

  @Test
  void should_create_error_message_indicating_multiple_duplicate_subsequence() {
    // GIVEN
    String[] sequenceValues = { "{", "title", "George", "title", "title", "George", "}" };
    String actual = "{ 'title':'A Game of Thrones', 'author':'George Martin', 'title':'The Kingkiller Chronicle', 'author':'Patrick Rothfuss'}";
    ErrorMessageFactory factory = shouldContainSubsequence(actual, sequenceValues, mapOf(entry("title", 2), entry("George", 1)),
                                                           new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n" +
                                   "  \"" + actual + "\"%n" +
                                   "to contain the following CharSequences in this order (possibly with other values between them):%n"
                                   +
                                   "  [\"{\", \"title\", \"George\", \"title\", \"title\", \"George\", \"}\"]%n" +
                                   "But:%n" +
                                   "- the 3rd occurrence of \"title\" was not found%n" +
                                   "- the 2nd occurrence of \"George\" was not found%n" +
                                   "when comparing values using CaseInsensitiveStringComparator"));
  }
}
