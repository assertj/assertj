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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.tests.core.api.recursive.comparison;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RecursiveComparisonAssert_isEqualTo_with_optional_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest
  @MethodSource("sameBooks")
  void should_pass_when_comparing_optional_fields_recursively_and_not_using_optional_equals(BookWithOptionalCoAuthor actual,
                                                                                            BookWithOptionalCoAuthor expected) {
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  static Stream<Arguments> sameBooks() {
    return Stream.of(Arguments.of(new BookWithOptionalCoAuthor("test"), new BookWithOptionalCoAuthor("test")),
                     // empty optional coAuthor
                     Arguments.of(new BookWithOptionalCoAuthor(null), new BookWithOptionalCoAuthor(null)),
                     // null coAuthor
                     Arguments.of(new BookWithOptionalCoAuthor(), new BookWithOptionalCoAuthor()));
  }

  @ParameterizedTest(name = "author 1 {0} / author 2 {1} / diff {2}")
  @MethodSource("differentBookWithOptionalCoAuthors")
  void should_fail_when_comparing_different_optional_fields(BookWithOptionalCoAuthor actual,
                                                            BookWithOptionalCoAuthor expected,
                                                            ComparisonDifference diff) {
    compareRecursivelyFailsWithDifferences(actual, expected, diff);
  }

  private static Stream<Arguments> differentBookWithOptionalCoAuthors() {
    BookWithOptionalCoAuthor pratchett = new BookWithOptionalCoAuthor("Terry Pratchett");
    BookWithOptionalCoAuthor georgeMartin = new BookWithOptionalCoAuthor("George Martin");

    return Stream.of(Arguments.of(pratchett, georgeMartin,
                                  javaTypeDiff("coAuthor.value.name", "Terry Pratchett", "George Martin")),
                     Arguments.of(pratchett, new BookWithOptionalCoAuthor(null),
                                  diff("coAuthor", Optional.of(new Author("Terry Pratchett")), Optional.empty())),
                     Arguments.of(new BookWithOptionalCoAuthor(null), pratchett,
                                  diff("coAuthor", Optional.empty(), Optional.of(new Author("Terry Pratchett")))),
                     Arguments.of(new BookWithOptionalCoAuthor("Terry Pratchett", 1, 2L, 3.0),
                                  new BookWithOptionalCoAuthor("Terry Pratchett", 2, 2L, 3.0),
                                  javaTypeDiff("numberOfPages", OptionalInt.of(1), OptionalInt.of(2))),
                     Arguments.of(new BookWithOptionalCoAuthor("Terry Pratchett", 1, 2L, 3.0),
                                  new BookWithOptionalCoAuthor("Terry Pratchett", 1, 4L, 3.0),
                                  javaTypeDiff("bookId", OptionalLong.of(2L), OptionalLong.of(4L))),
                     Arguments.of(new BookWithOptionalCoAuthor("Terry Pratchett", 1, 2L, 3.0),
                                  new BookWithOptionalCoAuthor("Terry Pratchett", 1, 2L, 6.0),
                                  javaTypeDiff("price", OptionalDouble.of(3.0), OptionalDouble.of(6.0))));
  }

  @Test
  void should_fail_when_comparing_non_optional_expected_field_with_optional_actual_field() {
    // GIVEN
    Author pratchett = new Author("Terry Pratchett");
    BookWithOptionalCoAuthor actual = new BookWithOptionalCoAuthor(pratchett.name);
    BookWithCoAuthor expected = new BookWithCoAuthor(pratchett);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected,
                                           diff("bookId", null, 0l),
                                           javaTypeDiff("coAuthor", Optional.of(pratchett), pratchett),
                                           diff("numberOfPages", null, 0),
                                           diff("price", null, 0.0));
  }

  @Test
  void should_fail_when_comparing_optional_expected_field_with_non_optional_actual_field() {
    // GIVEN
    Author pratchett = new Author("Terry Pratchett");
    BookWithCoAuthor actual = new BookWithCoAuthor(pratchett);
    BookWithOptionalCoAuthor expected = new BookWithOptionalCoAuthor(pratchett.name);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected,
                                           diff("bookId", 0l, null),
                                           diff("coAuthor", pratchett, Optional.of(pratchett),
                                                "expected field is an Optional but actual field is not (org.assertj.tests.core.api.recursive.comparison.Author)"),
                                           diff("numberOfPages", 0, null),
                                           diff("price", 0.0, null));
  }

  static class BookWithOptionalCoAuthor {

    Optional<Author> coAuthor;
    OptionalInt numberOfPages;
    OptionalLong bookId;
    OptionalDouble price;

    public BookWithOptionalCoAuthor(String coAuthor) {
      this.coAuthor = Optional.ofNullable(coAuthor == null ? null : new Author(coAuthor));
    }

    public BookWithOptionalCoAuthor() {
      this.coAuthor = null;
    }

    public BookWithOptionalCoAuthor(String coAuthor, int numberOfPages, long bookId, double price) {
      this.coAuthor = Optional.ofNullable(coAuthor == null ? null : new Author(coAuthor));
      this.numberOfPages = OptionalInt.of(numberOfPages);
      this.bookId = OptionalLong.of(bookId);
      this.price = OptionalDouble.of(price);
    }

    public Optional<Author> getCoAuthor() {
      return coAuthor;
    }
  }

  static class BookWithCoAuthor {

    Author coAuthor;
    int numberOfPages;
    long bookId;
    double price;

    public BookWithCoAuthor(Author author) {
      this.coAuthor = author;
    }

    public Author getCoAuthor() {
      return coAuthor;
    }
  }
}
