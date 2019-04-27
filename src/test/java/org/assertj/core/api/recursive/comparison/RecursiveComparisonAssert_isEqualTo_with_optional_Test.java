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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonAssert_isEqualTo_with_optional_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest
  @MethodSource("sameBooks")
  public void should_pass_when_comparing_optional_fields_recursively_and_not_using_optional_equals(BookWithOptionalCoAuthor actual,
                                                                                                   BookWithOptionalCoAuthor expected) {
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  static Stream<Arguments> sameBooks() {
    return Stream.of(Arguments.of(new BookWithOptionalCoAuthor(new Author("test")),
                                  new BookWithOptionalCoAuthor(new Author("test"))),
                     // empty optional coAuthor
                     Arguments.of(new BookWithOptionalCoAuthor(null), new BookWithOptionalCoAuthor(null)),
                     // null coAuthor
                     Arguments.of(new BookWithOptionalCoAuthor(), new BookWithOptionalCoAuthor()));
  }

  @ParameterizedTest(name = "author 1 {0} / author 2 {1} / path {2} / value 1 {3}/ value 2 {4}")
  @MethodSource("differentOptionalCoAuthors")
  public void should_fail_when_comparing_different_optional_fields(Author author1, Author author2, String path, Object value1,
                                                                   Object value2) {
    // GIVEN
    BookWithOptionalCoAuthor actual = new BookWithOptionalCoAuthor(author1);
    BookWithOptionalCoAuthor expected = new BookWithOptionalCoAuthor(author2);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, diff(path, value1, value2));
  }

  static Stream<Arguments> differentOptionalCoAuthors() {
    Author pratchett = new Author("Terry Pratchett");
    return Stream.of(Arguments.of(pratchett, new Author("George Martin"), "coAuthor.value.name", "Terry Pratchett",
                                  "George Martin"),
                     Arguments.of(pratchett, null, "coAuthor", Optional.of(pratchett), Optional.empty()),
                     Arguments.of(null, pratchett, "coAuthor", Optional.empty(), Optional.of(pratchett)));
  }

  @Test
  public void should_fail_when_comparing_non_optional_expected_field_with_optional_actual_field() {
    // GIVEN
    Author pratchett = new Author("Terry Pratchett");
    BookWithOptionalCoAuthor actual = new BookWithOptionalCoAuthor(pratchett);
    BookWithCoAuthor expected = new BookWithCoAuthor(pratchett);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected,
                                                              diff("coAuthor", Optional.of(pratchett), pratchett));
  }

  @Test
  public void should_fail_when_comparing_optional_expected_field_with_non_optional_actual_field() {
    // GIVEN
    Author pratchett = new Author("Terry Pratchett");
    BookWithCoAuthor actual = new BookWithCoAuthor(pratchett);
    BookWithOptionalCoAuthor expected = new BookWithOptionalCoAuthor(pratchett);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected,
                                                              diff("coAuthor", pratchett, Optional.of(pratchett),
                                                                   "expected field is an Optional but actual field is not (org.assertj.core.api.recursive.comparison.Author)"));
  }

  static class BookWithOptionalCoAuthor {

    Optional<Author> coAuthor;

    public BookWithOptionalCoAuthor(Author author) {
      this.coAuthor = Optional.ofNullable(author);
    }

    public BookWithOptionalCoAuthor() {
      this.coAuthor = null;
    }

    public Optional<Author> getCoAuthor() {
      return coAuthor;
    }
  }

  static class BookWithCoAuthor {

    Author coAuthor;

    public BookWithCoAuthor(Author author) {
      this.coAuthor = author;
    }

    public Author getCoAuthor() {
      return coAuthor;
    }
  }
}
