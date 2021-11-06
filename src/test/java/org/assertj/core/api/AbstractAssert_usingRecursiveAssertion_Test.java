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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.*;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

public class AbstractAssert_usingRecursiveAssertion_Test {

  @Test
  void should_pass_when_asserting_not_null_over_graph_without_null_values() {
    // GIVEN
    Object testObject = objectGraphNoNulls();
    // WHEN/THEN
    thenNoException().isThrownBy(() -> assertThat(testObject)
        .usingRecursiveAssertion().
        allFieldsAreNotNull());
  }

  @Test
  void should_fail_when_asserting_not_null_over_graph_with_null_values() {
    // GIVEN
    Object testObject = objectGraphWithNullValue();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(testObject)
      .usingRecursiveAssertion().
      allFieldsAreNotNull());
    // THEN
    then(error).hasMessageContaining("books.[0].authors.[1].books.[1].authors.[1].email");
  }

  Object objectGraphNoNulls() {
    Author root = (Author) objectGraphWithNullValue();
    root.books.get(0).authors[1].books.get(1).authors[1].email = "k.beck@recursive.test";
    return root;
  }

  Object objectGraphWithNullValue() {
    Author root = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
    Author another = new Author("Martin Fowler", "m.fowler@recursive.test");
    Author last = new Author("Kent Beck", null);

    Book firstbook = new Book("NoSql Distilled", new Author[]{root, another});
    root.books.add(firstbook);
    another.books.add(firstbook);
    Book otherbook = new Book("Refactoring", new Author[] {another, last});
    another.books.add(otherbook);
    last.books.add(otherbook);

    return root;
  }

  class Author {
    String name;
    String email;
    List<Book> books = new ArrayList<>();

    Author(String name, String email) {
      this.name = name;
      this.email = email;
    }
  }

  class Book {
    String title;
    Author[] authors;

    Book(String title, Author[] authors) {
      this.title = title;
      this.authors = authors;
    }
  }

}
