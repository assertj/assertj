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
package example.core;

// @formatter:off

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

// not run by the build (class name does not match *Test)
class RecursiveAssertionExample {

  @Test
  void all_fields_satisfy() {
    // tag::user_guide[]
    Author pramodSadalage = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
    Author martinFowler = new Author("Martin Fowler", "m.fowler@recursive.test");
    Author kentBeck = new Author("Kent Beck", "k.beck@recursive.test");

    Book noSqlDistilled = new Book("NoSql Distilled", new Author[] {pramodSadalage, martinFowler});
    pramodSadalage.books.add(noSqlDistilled);
    martinFowler.books.add(noSqlDistilled);

    Book refactoring = new Book("Refactoring", new Author[] {martinFowler, kentBeck});
    martinFowler.books.add(refactoring);
    kentBeck.books.add(refactoring);

    // assertion succeeds
    assertThat(pramodSadalage).usingRecursiveAssertion()
                              .allFieldsSatisfy(field -> field != null);
    // end::user_guide[]
  }
}

// tag::classes[]
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
// end::classes[]
// @formatter:on
