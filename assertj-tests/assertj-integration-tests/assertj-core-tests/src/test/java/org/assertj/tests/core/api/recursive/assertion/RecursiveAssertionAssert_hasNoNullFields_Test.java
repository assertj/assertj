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
package org.assertj.tests.core.api.recursive.assertion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNoException;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.CollectionAssertionPolicy.COLLECTION_OBJECT_AND_ELEMENTS;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.MapAssertionPolicy.MAP_OBJECT_AND_ENTRIES;
import static org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration.OptionalAssertionPolicy.OPTIONAL_OBJECT_AND_VALUE;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.tests.core.testkit.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveAssertionAssert_hasNoNullFields_Test {

  @Test
  void should_pass_when_asserting_not_null_over_graph_without_null_values() {
    // GIVEN
    Object testObject = objectGraphNoNulls();
    // WHEN/THEN
    thenNoException().isThrownBy(() -> assertThat(testObject).usingRecursiveAssertion().hasNoNullFields());
  }

  @Test
  void should_fail_when_asserting_not_null_over_graph_with_null_values() {
    // GIVEN
    Object testObject = objectGraphWithNullValue();
    // WHEN
    var error = expectAssertionError(() -> assertThat(testObject).usingRecursiveAssertion().hasNoNullFields());
    // THEN
    then(error).hasMessageContaining("books.[0].authors.[1].books.[1].authors.[1].email");
  }

  Object objectGraphNoNulls() {
    Author root = (Author) objectGraphWithNullValue();
    root.books.getFirst().authors[1].books.get(1).authors[1].email = "k.beck@recursive.test";
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

  @SuppressWarnings("unused")
  static class OuterWithArray {
    InnerWithArray inner = new InnerWithArray();
    byte[] arrayOuter = null;
  }

  @SuppressWarnings("unused")
  static class InnerWithArray {
    byte[] array = null;
  }

  @Test
  public void should_report_null_arrays() {
    // GIVEN
    Object testObject = new OuterWithArray();
    // WHEN
    var error = expectAssertionError(() -> assertThat(testObject).usingRecursiveAssertion()
                                                                 .withCollectionAssertionPolicy(COLLECTION_OBJECT_AND_ELEMENTS)
                                                                 .hasNoNullFields());
    // THEN
    then(error).hasMessageContainingAll("arrayOuter", "inner.array");
  }

  @SuppressWarnings("unused")
  static class OuterWithCollection {
    InnerWithCollection inner = new InnerWithCollection();
    Collection<String> collectionOuter = null;
  }

  @SuppressWarnings("unused")
  static class InnerWithCollection {
    Collection<String> collectionInner = null;
  }

  @Test
  public void should_report_null_collections() {
    // GIVEN
    Object testObject = new OuterWithCollection();
    // WHEN
    var error = expectAssertionError(() -> assertThat(testObject).usingRecursiveAssertion()
                                                                 .withCollectionAssertionPolicy(COLLECTION_OBJECT_AND_ELEMENTS)
                                                                 .hasNoNullFields());
    // THEN
    then(error).hasMessageContainingAll("collectionOuter", "inner.collection");
  }

  @SuppressWarnings("unused")
  static class OuterWithMap {
    InnerWithMap inner = new InnerWithMap();
    Map<String, String> mapOuter = null;
  }

  @SuppressWarnings("unused")
  static class InnerWithMap {
    Map<String, String> mapInner = null;
  }

  @Test
  public void should_report_null_maps() {
    // GIVEN
    Object testObject = new OuterWithMap();
    // WHEN
    var error = expectAssertionError(() -> assertThat(testObject).usingRecursiveAssertion()
                                                                 .withMapAssertionPolicy(MAP_OBJECT_AND_ENTRIES)
                                                                 .hasNoNullFields());
    // THEN
    then(error).hasMessageContainingAll("mapOuter", "inner.mapInner");
  }

  @SuppressWarnings({ "unused", "OptionalUsedAsFieldOrParameterType" })
  static class OuterWithOptional {
    InnerWithOptional inner = new InnerWithOptional();
    Optional<String> optionalOuter = null;
  }

  @SuppressWarnings({ "unused", "OptionalUsedAsFieldOrParameterType" })
  static class InnerWithOptional {
    Optional<String> optionalInner = null;
  }

  @Test
  public void should_report_null_optionals() {
    // GIVEN
    Object testObject = new OuterWithOptional();
    // WHEN
    var error = expectAssertionError(() -> assertThat(testObject).usingRecursiveAssertion()
                                                                 .withOptionalAssertionPolicy(OPTIONAL_OBJECT_AND_VALUE)
                                                                 .hasNoNullFields());
    // THEN
    then(error).hasMessageContainingAll("optionalOuter", "inner.optional");
  }

  interface I {
  }

  static class O {
    public I i;
  }

  @Test
  public void should_support_anonymous_classes() {
    // GIVEN
    O o = new O();
    o.i = new I() {}; // anonymous class
    // WHEN/THEN
    then(o).usingRecursiveAssertion()
           .hasNoNullFields();
  }

  @Test
  public void should_support_local_classes() {
    // GIVEN
    class PhoneNumber {
    }
    class WithPhoneNumber {
      PhoneNumber phoneNumber = new PhoneNumber();
    }
    WithPhoneNumber withPhoneNumber = new WithPhoneNumber();
    withPhoneNumber.phoneNumber = new PhoneNumber();
    // WHEN/THEN
    then(withPhoneNumber).usingRecursiveAssertion()
                         .hasNoNullFields();
  }

  public record WithContainer(Object container, String string) {
  }

  @ParameterizedTest
  @MethodSource
  void should_report_container_with_null_values(WithContainer withContainer) {
    // WHEN
    var error = expectAssertionError(() -> assertThat(withContainer).usingRecursiveAssertion().hasNoNullFields());
    // THEN
    then(error).hasMessageContainingAll("The following fields did not satisfy the predicate", "container");
  }

  private static Stream<WithContainer> should_report_container_with_null_values() {
    List<String> list = list(null, "value");
    String[] array = new String[] { null, null, "value" };
    Map<String, String> map = Maps.mapOf(entry("key", "value"), entry("key2", null));
    return Stream.of(new WithContainer(list, "list"), new WithContainer(array, "array"), new WithContainer(map, "map"));
  }
}
