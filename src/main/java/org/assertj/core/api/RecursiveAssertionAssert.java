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

import org.assertj.core.api.recursive.FieldLocation;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionDriver;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.assertj.core.error.ShouldNotBeEqualComparingFieldByFieldRecursively.shouldNotBeEqualComparingFieldByFieldRecursively;
import static org.assertj.core.error.ShouldNotSatisfyPredicateRecursively.shouldNotSatisfyRecursively;

/**
 * <p>An assertion that supports asserting a {@link Predicate} over all the fields of an object graph. Cycle avoidance is used,
 * so a graph that has cyclic references is essentially reduced to a tree by this class (the actual object graph is not changed
 * of course, it is treated as an immutable value).</p>
 *
 * <p>This class is <em>absolutely not</em> thread safe!! When using this class, care must be taken to ensure that its instances
 * are thread-bound. However, it <em>can</em> be re-used for multiple assertions over the same object graph.</p>
 * @param <SELF>
 */
public class RecursiveAssertionAssert<SELF extends RecursiveAssertionAssert<SELF>> extends AbstractAssert<SELF, Object> {

  private final RecursiveAssertionConfiguration recursiveAssertionConfiguration;
  private final RecursiveAssertionDriver recursiveAssertionDriver;

  public RecursiveAssertionAssert(Object o, RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    super(o, RecursiveAssertionAssert.class);
    this.recursiveAssertionConfiguration = recursiveAssertionConfiguration;
    this.recursiveAssertionDriver = new RecursiveAssertionDriver(recursiveAssertionConfiguration);
  }

  /**
   * <p>Asserts that for all fields of the object actual and all fields of the object tree starting with actual, a given predicate
   * returns true.</p>
   *
   * <p><strong>Example</strong></p>
   * <pre><code style='java'> class Author {
   *     String name;
   *     String email;
   *     List<Book> books = new ArrayList<>();
   *
   *     Author(String name, String email) {
   *       this.name = name;
   *       this.email = email;
   *     }
   *   }
   *
   *   class Book {
   *     String title;
   *     Author[] authors;
   *
   *     Book(String title, Author[] authors) {
   *       this.title = title;
   *       this.authors = authors;
   *     }
   *   }
   *  ...
   *
   *   Author root = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
   *   Author another = new Author("Martin Fowler", "m.fowler@recursive.test");
   *   Author last = new Author("Kent Beck", "k.beck@recursive.test");
   *
   *   Book firstbook = new Book("NoSql Distilled", new Author[]{root, another});
   *   root.books.add(firstbook);
   *   another.books.add(firstbook);
   *   Book otherbook = new Book("Refactoring", new Author[] {another, last});
   *   another.books.add(otherbook);
   *   last.books.add(otherbook);
   *   ...
   *
   *   assertThat(root).withRecursiveAssertion().allFieldsSatisfy(theField -> theField != null); </code>
   * </pre>
   *
   *
   *
   * @param toBeAssertedRecursively The predicate that is recursively applied to all the fields in the object tree of which actual
   *                                is the root.
   * @return {@code this} assertions object
   * @throws AssertionError if one or more fields as described above fail the predicate test.
   * @since 3.22.0
   */
  public RecursiveAssertionAssert<?> allFieldsSatisfy(Predicate<Object> toBeAssertedRecursively) {
    // Reset the driver in case this is not the first predicate being run over actual.
    recursiveAssertionDriver.reset();

    List<FieldLocation> failedFields = recursiveAssertionDriver.assertOverObjectGraph(toBeAssertedRecursively, actual);
    if (!failedFields.isEmpty())
      throw objects.getFailures()
                   .failure(info, shouldNotSatisfyRecursively(recursiveAssertionConfiguration, failedFields));
    return this;
  }

  /**
   * <p></p>Asserts that for all fields of the object actual and all fields of the object tree starting with actual, the fields are not
   * null. This is a convenience method for a common test and it is equivalent to {@code allFieldsSatisfy(fld -> fld != null)}.</p>
   *
   * <p><strong>Example</strong></p>
   * <pre><code style='java'> class Author {
   *     String name;
   *     String email;
   *     List<Book> books = new ArrayList<>();
   *
   *     Author(String name, String email) {
   *       this.name = name;
   *       this.email = email;
   *     }
   *   }
   *
   *   class Book {
   *     String title;
   *     Author[] authors;
   *
   *     Book(String title, Author[] authors) {
   *       this.title = title;
   *       this.authors = authors;
   *     }
   *   }
   *  ...
   *
   *   Author root = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
   *   Author another = new Author("Martin Fowler", "m.fowler@recursive.test");
   *   Author last = new Author("Kent Beck", "k.beck@recursive.test");
   *
   *   Book firstbook = new Book("NoSql Distilled", new Author[]{root, another});
   *   root.books.add(firstbook);
   *   another.books.add(firstbook);
   *   Book otherbook = new Book("Refactoring", new Author[] {another, last});
   *   another.books.add(otherbook);
   *   last.books.add(otherbook);
   *   ...
   *
   *   assertThat(root).withRecursiveAssertion().allFieldsAreNotNull(); </code>
   * </pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if one or more fields as described above are null.
   * @since 3.22.0
   */
  public RecursiveAssertionAssert<?> allFieldsAreNotNull() {
    return allFieldsSatisfy(Objects::nonNull);
  }
}
