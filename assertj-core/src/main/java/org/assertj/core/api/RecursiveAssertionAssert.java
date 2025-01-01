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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldNotSatisfyPredicateRecursively.shouldNotSatisfyRecursively;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.recursive.assertion.DefaultRecursiveAssertionIntrospectionStrategy;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionDriver;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionIntrospectionStrategy;
import org.assertj.core.api.recursive.comparison.FieldLocation;

/**
 * <p>An assertion that supports asserting a {@link Predicate} over all the fields of an object graph. Cycle avoidance is used,
 * so a graph that has cyclic references is essentially reduced to a tree by this class (the actual object graph is not changed
 * of course, it is treated as an immutable value).</p>
 *
 * @since 3.24.0
 */
@Beta
public class RecursiveAssertionAssert extends AbstractAssert<RecursiveAssertionAssert, Object> {

  private final RecursiveAssertionConfiguration recursiveAssertionConfiguration;
  private final RecursiveAssertionDriver recursiveAssertionDriver;

  public RecursiveAssertionAssert(Object o, RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    super(o, RecursiveAssertionAssert.class);
    this.recursiveAssertionConfiguration = recursiveAssertionConfiguration;
    this.recursiveAssertionDriver = new RecursiveAssertionDriver(recursiveAssertionConfiguration);
  }

  /**
   * <p>Asserts that the given predicate is met for all fields of the object under test <b>recursively</b> (but not the object itself).</p>
   *
   * <p>For example if the object under test is an instance of class A, A has a B field and B a C field then the assertion checks A's B field and B's C field and all C's fields.</p>
   *
   * <p>The recursive algorithm employs cycle detection, so object graphs with cyclic references can safely be asserted over without causing looping.</p>
   *
   * <p>This method enables recursive asserting using default configuration, which means all fields of all objects have the  
   * {@link java.util.function.Predicate} applied to them (including primitive fields), no fields are excluded, but:
   * <ul>
   *   <li>The recursion does not enter into Java Class Library types (java.*, javax.*)</li>
   *   <li>The {@link java.util.function.Predicate} is applied to {@link java.util.Collection} and array elements (but not the collection/array itself)</li>
   *   <li>The {@link java.util.function.Predicate} is applied to {@link java.util.Map} values but not the map itself or its keys</li>
   *   <li>The {@link java.util.function.Predicate} is applied to {@link java.util.Optional} and primitive optional values</li>
   * </ul>
   * <p>You can change how the recursive assertion deals with arrays, collections, maps and optionals, see:</p>
   * <ul>
   *   <li>{@link RecursiveAssertionAssert#withCollectionAssertionPolicy(RecursiveAssertionConfiguration.CollectionAssertionPolicy)} for collections and arrays</li>
   *   <li>{@link RecursiveAssertionAssert#withMapAssertionPolicy(RecursiveAssertionConfiguration.MapAssertionPolicy)} for maps</li>
   *   <li>{@link RecursiveAssertionAssert#withOptionalAssertionPolicy(RecursiveAssertionConfiguration.OptionalAssertionPolicy)} for optionals</li>
   * </ul>
   *
   * <p>It is possible to assert several predicates over the object graph in a row.</p>
   *
   * <p>The classes used in recursive asserting are <em>not</em> thread safe. Care must be taken when running tests in parallel
   * not to run assertions over object graphs that are being shared between tests.</p>
   * 
   * <p>Example:</p>
   * <pre><code style='java'> class Author {
   *   String name;
   *   String email;
   *   List&lt;Book&gt; books = new ArrayList&lt;&gt;();
   *
   *   Author(String name, String email) {
   *     this.name = name;
   *     this.email = email;
   *   }
   * }
   *
   * class Book {
   *   String title;
   *   Author[] authors;
   *
   *   Book(String title, Author[] authors) {
   *     this.title = title;
   *     this.authors = authors;
   *   }
   * }
   *
   * Author pramodSadalage = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
   * Author martinFowler = new Author("Martin Fowler", "m.fowler@recursive.test");
   * Author kentBeck = new Author("Kent Beck", "k.beck@recursive.test");
   *
   * Book noSqlDistilled = new Book("NoSql Distilled", new Author[] {pramodSadalage, martinFowler});
   * pramodSadalage.books.add(noSqlDistilled);
   * martinFowler.books.add(noSqlDistilled);
   * 
   * Book refactoring = new Book("Refactoring", new Author[] {martinFowler, kentBeck});
   * martinFowler.books.add(refactoring);
   * kentBeck.books.add(refactoring);
   *
   * // assertion succeeds
   * assertThat(pramodSadalage).usingRecursiveAssertion()
   *                           .allFieldsSatisfy(field -> field != null); </code></pre>
   *
   * @param predicate The predicate that is recursively applied to all the fields in the object tree of which actual is the root.
   * @return {@code this} assertions object
   * @throws AssertionError if one or more fields as described above fail the predicate test.
   */
  public RecursiveAssertionAssert allFieldsSatisfy(Predicate<Object> predicate) {
    // Reset the driver in case this is not the first predicate being run over actual.
    recursiveAssertionDriver.reset();

    List<FieldLocation> failedFields = recursiveAssertionDriver.assertOverObjectGraph(predicate, actual);
    if (!failedFields.isEmpty()) {
      throw objects.getFailures().failure(info, shouldNotSatisfyRecursively(recursiveAssertionConfiguration, failedFields));
    }
    return this;
  }

  /**
   * Asserts that none of the fields of the object under test graph (i.e. recursively getting the fields) are null (but not the object itself).
   * <p>
   * This is a convenience method for a common test, and it is equivalent to {@code allFieldsSatisfy(field -> field != null)}.
   * <p>
   * Example:
   * <pre><code style='java'> class Author {
   *   String name;
   *   String email;
   *   List&lt;Book&gt; books = new ArrayList&lt;&gt;();
   *
   *   Author(String name, String email) {
   *     this.name = name;
   *     this.email = email;
   *   }
   * }
   *
   * class Book {
   *   String title;
   *   Author[] authors;
   *
   *   Book(String title, Author[] authors) {
   *     this.title = title;
   *     this.authors = authors;
   *   }
   * }
   *
   * Author pramodSadalage = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
   * Author martinFowler = new Author("Martin Fowler", "m.fowler@recursive.test");
   * Author kentBeck = new Author("Kent Beck", "k.beck@recursive.test");
   *
   * Book noSqlDistilled = new Book("NoSql Distilled", new Author[]{pramodSadalage, martinFowler});
   * pramodSadalage.books.add(noSqlDistilled);
   * martinFowler.books.add(noSqlDistilled);
   * 
   * Book refactoring = new Book("Refactoring", new Author[] {martinFowler, kentBeck});
   * martinFowler.books.add(refactoring);
   * kentBeck.books.add(refactoring);
   *
   * // assertion succeeds
   * assertThat(pramodSadalage).usingRecursiveAssertion()
   *                           .hasNoNullFields(); </code></pre>
   *
   * @return {@code this} assertions object
   * @throws AssertionError if one or more fields as described above are null.
   */
  public RecursiveAssertionAssert hasNoNullFields() {
    return allFieldsSatisfy(Objects::nonNull);
  }

  /**
   * Makes the recursive assertion to ignore the specified fields in the object under test.
   * <p>
   * When a field is ignored, all its fields are ignored too.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   String occupation;
   *   int age;
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", "Detective", 60);
   * sherlock.address.street = "Baker Street";
   * sherlock.address.number = 221;
   *
   * // assertion succeeds because Person has only String fields except for address and age (address fields are ignored)
   * assertThat(sherlock).usingRecursiveAssertion()
   *                     .ignoringFields("address", "age")
   *                     .allFieldsSatisfy(field -> field instanceof String);
   *
   * // assertion fails because of age, address and address.number fields
   * assertThat(sherlock).usingRecursiveAssertion()
   *                     .allFieldsSatisfy(field -> field instanceof String);</code></pre>
   *
   * @param fieldsToIgnore the fields to ignore in the object under test.
   * @return this {@link RecursiveAssertionAssert} to chain other methods.
   */
  public RecursiveAssertionAssert ignoringFields(String... fieldsToIgnore) {
    recursiveAssertionConfiguration.ignoreFields(fieldsToIgnore);
    return this;
  }

  /**
   * Makes the recursive assertion to ignore the fields matching the specified regexes in the object under test.
   * <p>
   * When a field is ignored, all its fields are ignored too.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   String occupation;
   *   int age;
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", "Detective", 60);
   * sherlock.address.street = "Baker Street";
   * sherlock.address.number = 221;
   *
   * // assertion succeeds because Person has only String fields except for address and age (address fields are ignored)
   * assertThat(sherlock).usingRecursiveAssertion()
   *                     .ignoringFieldsMatchingRegexes("ad.*", "ag.")
   *                     .allFieldsSatisfy(field -> field instanceof String);
   *
   * // assertion fails because of age and address fields (address.number is ignored)
   * assertThat(sherlock).usingRecursiveAssertion()
   *                     .ignoringFieldsMatchingRegexes(".*ber")
   *                     .allFieldsSatisfy(field -> field instanceof String);</code></pre>
   *
   * @param regexes regexes used to ignore fields in the assertion.
   * @return this {@link RecursiveAssertionAssert} to chain other methods.
   */
  public RecursiveAssertionAssert ignoringFieldsMatchingRegexes(String... regexes) {
    recursiveAssertionConfiguration.ignoreFieldsMatchingRegexes(regexes);
    return this;
  }

  /**
   * Makes the recursive assertion to ignore the object under test fields of the given types.
   * The fields are ignored if their types <b>exactly match one of the ignored types</b>, for example if a field is a subtype of an ignored type it is not ignored.
   * <p>
   * If some object under test fields are null it is not possible to evaluate their types and thus these fields are not ignored.
   * <p>
   * When a field is ignored, all its fields are ignored too.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   String occupation;
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", "Detective");
   * sherlock.address.street = "Baker Street";
   * sherlock.address.number = 221;
   *
   * // assertion succeeds because Person has only String fields except for address  (address fields are ignored)
   * assertThat(sherlock).usingRecursiveAssertion()
   *                     .ignoringFieldsOfTypes(Address.class)
   *                     .allFieldsSatisfy(field -> field instanceof String);
   *
   * // assertion fails because of address and address.number fields
   * assertThat(sherlock).usingRecursiveAssertion()
   *                     .allFieldsSatisfy(field -> field instanceof String);</code></pre>
   *
   * @param typesToIgnore the types we want to ignore in the object under test fields.
   * @return this {@link RecursiveAssertionAssert} to chain other methods.
   */
  public RecursiveAssertionAssert ignoringFieldsOfTypes(Class<?>... typesToIgnore) {
    recursiveAssertionConfiguration.ignoreFieldsOfTypes(typesToIgnore);
    return this;
  }

  /**
   * Make the recursive assertion <b>not to run</b> the {@link Predicate} over the primitive fields of an object in an object graph,
   * by default asserting over primitives is <em>enabled</em>.
   * <p>
   * For example, consider the following class:
   * <pre><code class='java'> class Example {
   *    public int primitiveField;
   *    public String objectField;
   *  } </code></pre>
   * <p>
   * By default, the assertion being applied recursively will be applied to <code>primitiveField</code> and to
   * <code>objectField</code>. If ignoring primitives it set to true, the assertion will only be applied to <code>objectField</code>.
   * <p>
   * If you elect to assert over primitives then it is your own responsibility as a developer to ensure that your
   * {@link Predicate} can handle (boxed) primitive arguments.</p>
   *
   * @return this {@link RecursiveAssertionAssert} to chain other methods.
   */
  public RecursiveAssertionAssert ignoringPrimitiveFields() {
    recursiveAssertionConfiguration.ignorePrimitiveFields(true);
    return this;
  }

  /**
   * Makes the recursive assertion to ignore all null fields.
   * <p>
   * <pre><code class='java'> class Person {
   *   String name;
   *   String occupation;
   *   Address address;
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", "Detective");
   * sherlock.address = null;
   *
   * // assertion succeeds as address field is ignored
   * assertThat(noName).usingRecursiveAssertion()
   *                   .ignoringAllNullFields()
   *                   .allFieldsSatisfy(field -> field instanceof String);
   *
   * // assertion fails as address, address.number and address.street fields are not evaluated as String, street because it's null.
   * assertThat(sherlock).usingRecursiveAssertion()
   *                     .allFieldsSatisfy(field -> field instanceof String);</code></pre>
   *
   * @return this {@link RecursiveAssertionAssert} to chain other methods.
   */
  public RecursiveAssertionAssert ignoringAllNullFields() {
    recursiveAssertionConfiguration.ignoreAllNullFields(true);
    return this;
  }

  /**
   * Makes the recursive assertion to use the specified {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy}.
   * <p>
   * See {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy} for the different possible policies, by default
   * {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy#OPTIONAL_VALUE_ONLY} is used.
   *
   * @param optionalAssertionPolicy the {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy} to use.
   * @return this {@link RecursiveAssertionAssert} to chain other methods.
   */
  public RecursiveAssertionAssert withOptionalAssertionPolicy(RecursiveAssertionConfiguration.OptionalAssertionPolicy optionalAssertionPolicy) {
    recursiveAssertionConfiguration.setOptionalAssertionPolicy(optionalAssertionPolicy);
    return this;
  }

  /**
   * Makes the recursive assertion to use the specified {@link RecursiveAssertionConfiguration.CollectionAssertionPolicy}.
   * <p>
   * See {@link RecursiveAssertionConfiguration.CollectionAssertionPolicy} for the different possible policies, by default
   * {@link RecursiveAssertionConfiguration.CollectionAssertionPolicy#ELEMENTS_ONLY} is used.
   *
   * @param collectionAssertionPolicy the {@link RecursiveAssertionConfiguration.CollectionAssertionPolicy} to use.
   * @return this {@link RecursiveAssertionAssert} to chain other methods.
   */
  public RecursiveAssertionAssert withCollectionAssertionPolicy(RecursiveAssertionConfiguration.CollectionAssertionPolicy collectionAssertionPolicy) {
    recursiveAssertionConfiguration.setCollectionAssertionPolicy(collectionAssertionPolicy);
    return this;
  }

  /**
   * Makes the recursive assertion to use the specified {@link RecursiveAssertionConfiguration.MapAssertionPolicy}.
   * <p>
   * See {@link RecursiveAssertionConfiguration.MapAssertionPolicy} for the different possible policies, by default
   * {@link RecursiveAssertionConfiguration.MapAssertionPolicy#MAP_VALUES_ONLY} is used.
   *
   * @param mapAssertionPolicy the {@link RecursiveAssertionConfiguration.MapAssertionPolicy} to use.
   * @return this {@link RecursiveAssertionAssert} to chain other methods.
   */
  public RecursiveAssertionAssert withMapAssertionPolicy(RecursiveAssertionConfiguration.MapAssertionPolicy mapAssertionPolicy) {
    recursiveAssertionConfiguration.setMapAssertionPolicy(mapAssertionPolicy);
    return this;
  }

  /**
   * Defines how objects are introspected in the recursive assertion.
   * <p>
   * Default to {@link DefaultRecursiveAssertionIntrospectionStrategy} that introspects all fields (including inherited ones).
   *
   * @param introspectionStrategy the {@link RecursiveAssertionIntrospectionStrategy} to use
   * @return this {@link RecursiveAssertionAssert} to chain other methods.
   */
  public RecursiveAssertionAssert withIntrospectionStrategy(RecursiveAssertionIntrospectionStrategy introspectionStrategy) {
    recursiveAssertionConfiguration.setIntrospectionStrategy(introspectionStrategy);
    return this;
  }
}
