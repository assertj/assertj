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
package org.assertj.tests.core.api.recursive.assertion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

class RecursiveAssertionAssert_allFieldsSatisfy_Test {

  @Test
  void object_recursive_assertion_should_pass_when_asserting_not_null_over_graph_without_null_values() {
    // GIVEN
    Object testObject = objectGraphNoNulls();
    // WHEN/THEN
    then(testObject).usingRecursiveAssertion().allFieldsSatisfy(Objects::nonNull);
  }

  @Test
  void iterable_recursive_assertion_should_pass_when_asserting_not_null_over_graph_without_null_values() {
    // GIVEN
    List<Object> testObject = List.of(objectGraphNoNulls(), objectGraphNoNulls());
    // WHEN/THEN
    then(testObject).usingRecursiveAssertion().allFieldsSatisfy(Objects::nonNull);
  }

  @Test
  void array_recursive_assertion_should_pass_when_asserting_not_null_over_graph_without_null_values() {
    // GIVEN
    Object[] testObject = array(objectGraphNoNulls(), objectGraphNoNulls());
    // WHEN/THEN
    then(testObject).usingRecursiveAssertion().allFieldsSatisfy(Objects::nonNull);
  }

  @Test
  void map_recursive_assertion_should_pass_when_asserting_not_null_over_graph_without_null_values() {
    // GIVEN
    Map<String, Object> testObject = Map.of("k1", objectGraphNoNulls(), "k2", objectGraphNoNulls());
    // WHEN/THEN
    then(testObject).usingRecursiveAssertion().allFieldsSatisfy(Objects::nonNull);
  }

  @Test
  void optional_recursive_assertion_should_pass_when_asserting_not_null_over_graph_without_null_values() {
    // GIVEN
    Optional<Object> testObject = Optional.of(objectGraphNoNulls());
    // WHEN/THEN
    then(testObject).usingRecursiveAssertion().allFieldsSatisfy(Objects::nonNull);
  }

  @Test
  void should_not_evaluate_root_object_but_only_its_fields() {
    // GIVEN
    Object address = new Address("Paris", "Tolbiac");
    Predicate<Object> isString = field -> field instanceof String;
    // WHEN/THEN
    then(address).usingRecursiveAssertion().allFieldsSatisfy(isString);
  }

  @Test
  void should_fail_when_asserting_not_null_over_graph_with_null_values() {
    // GIVEN
    Object testObject = objectGraphWithNullValue();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(testObject).usingRecursiveAssertion()
                                                                            .allFieldsSatisfy(Objects::nonNull));
    // THEN
    then(error).hasMessageContaining("books.[0].authors.[1].books.[1].authors.[1].email");
  }

  Object objectGraphNoNulls() {
    Author root = (Author) objectGraphWithNullValue();
    root.books.get(0).authors[1].books.get(1).authors[1].email = "k.beck@recursive.test";
    return root;
  }

  Object objectGraphWithNullValue() {
    Author pramodSadalage = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
    Author martinFowler = new Author("Martin Fowler", "m.fowler@recursive.test");
    Author kentBeck = new Author("Kent Beck", null);

    Book noSqlDistilled = new Book("NoSql Distilled", array(pramodSadalage, martinFowler));
    pramodSadalage.books.add(noSqlDistilled);
    martinFowler.books.add(noSqlDistilled);

    Book refactoring = new Book("Refactoring", array(martinFowler, kentBeck));
    martinFowler.books.add(refactoring);
    kentBeck.books.add(refactoring);

    return pramodSadalage;
  }

  static class Author {
    String name;
    String email;
    List<Book> books = new ArrayList<>();

    Author(String name, String email) {
      this.name = name;
      this.email = email;
    }
  }

  static class Book {
    String title;
    Author[] authors;

    Book(String title, Author[] authors) {
      this.title = title;
      this.authors = authors;
    }
  }

  static class Address {
    String town;
    String street;

    Address(String town, String street) {
      this.town = town;
      this.street = street;
    }
  }
}
