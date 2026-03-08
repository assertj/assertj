package org.assertj.core.api.soft;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.AssertionError;
import java.lang.Class;
import java.lang.Deprecated;
import java.lang.Iterable;
import java.lang.Object;
import java.lang.SafeVarargs;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.assertj.core.annotation.Beta;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.Condition;
import org.assertj.core.api.RecursiveAssertionAssert;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionIntrospectionStrategy;
import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;

@Beta
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class SoftRecursiveAssertionAssert implements SoftAssert {
  private final AssertionErrorCollector errorCollector;

  private final RecursiveAssertionAssert recursiveAssertionAssert;

  public SoftRecursiveAssertionAssert(RecursiveAssertionAssert recursiveAssertionAssert,
      AssertionErrorCollector errorCollector) {
    this.recursiveAssertionAssert = recursiveAssertionAssert;
    this.errorCollector = errorCollector;
  }

  /**
   * Returns actual (the object currently under test).
   * <p>
   * This can be useful if after chaining assertions, the object under test has changed and you want to get it.
   * <p>
   * Examples of method changing actual:
   * {@link org.assertj.core.api.AbstractObjectAssert#extracting(java.util.function.Function) extracting(java.util.function.Function)} or a navigation methods like
   * {@link org.assertj.core.api.AbstractThrowableAssert#rootCause() rootCause()}.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
   *
   * String newActual = assertThat(frodo).extracting(TolkienCharacter::getName).actual();
   *
   * // newActual == frodo.getName()
   * assertThat(newActual).isSameAs(frodo.name);</code></pre>
   *
   * @return actual the object currently under test.
   * @since 3.27.0 in {@link org.assertj.core.api.AbstractAssert} and 4.0.0 in {@link org.assertj.core.api.Assert}
   */
  public Object actual() {
    return recursiveAssertionAssert.actual();
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
   *   <li>{@link SoftRecursiveAssertionAssert#withCollectionAssertionPolicy(RecursiveAssertionConfiguration.CollectionAssertionPolicy)} for collections and arrays</li>
   *   <li>{@link SoftRecursiveAssertionAssert#withMapAssertionPolicy(RecursiveAssertionConfiguration.MapAssertionPolicy)} for maps</li>
   *   <li>{@link SoftRecursiveAssertionAssert#withOptionalAssertionPolicy(RecursiveAssertionConfiguration.OptionalAssertionPolicy)} for optionals</li>
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
  public SoftRecursiveAssertionAssert allFieldsSatisfy(Predicate<Object> predicate) {
    try {
      recursiveAssertionAssert.allFieldsSatisfy(predicate);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Sets the description of the assertion that is going to be called after.
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * The description follows {@link String#format(String, Object...)} syntax.
   * <p>
   * Example :
   * <pre><code class='java'> try {
   *   // set an incorrect age to Mr Frodo which is really 33 years old.
   *   frodo.setAge(50);
   *   // specify a test description (call as() before the assertion !), it supports String format syntax.
   *   assertThat(frodo.getAge()).as(&quot;check %s's age&quot;, frodo.getName()).isEqualTo(33);
   * } catch (AssertionError e) {
   *   assertThat(e).hasMessage(&quot;[check Frodo's age]\n
   *                             expected: 33\n
   *                              but was: 50&quot;);
   * }</code></pre>
   *
   * @param description the new description to set.
   * @param args optional parameter if description is a format String.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   * @see #describedAs(String, Object...)
   */
  public SoftRecursiveAssertionAssert as(String description, Object... args) {
    recursiveAssertionAssert.as(description,args);
    return this;
  }

  /**
   * Lazily specifies the description of the assertion that is going to be called, the given description is <b>not</b> evaluated
   * if the assertion succeeds.
   * <p>
   * The description must be set <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * Example :
   * <pre><code class='java'> // set an incorrect age to Mr Frodo which we all know is 33 years old.
   * frodo.setAge(50);
   *
   * // the lazy test description is <b>not</b> evaluated as the assertion succeeds
   * assertThat(frodo.getAge()).as(() -&gt; &quot;check Frodo's age&quot;).isEqualTo(50);
   *
   * try
   * {
   *   // the lazy test description is evaluated as the assertion fails
   *   assertThat(frodo.getAge()).as(() -&gt; &quot;check Frodo's age&quot;).isEqualTo(33);
   * }
   * catch (AssertionError e)
   * {
   *   assertThat(e).hasMessage(&quot;[check Frodo's age]\n
   *                             expected: 33\n
   *                              but was: 50&quot;);
   * }</code></pre>
   *
   * @param descriptionSupplier the description {@link Supplier}.
   * @return {@code this} object.
   * @throws IllegalStateException if the descriptionSupplier is {@code null} when evaluated.
   */
  public SoftRecursiveAssertionAssert as(Supplier<String> descriptionSupplier) {
    recursiveAssertionAssert.as(descriptionSupplier);
    return this;
  }

  /**
   * Uses an {@link DefaultSoftAssertFactory} to verify that the actual value is an instance of a given type and to produce
   * a new {@link org.assertj.core.api.Assert} narrowed to that type.
   * <p>
   * {@link SoftAssertFactories} provides static factories for all the types supported by {@code Assertions#assertThat}.
   * <p>
   * Additional factories can be created with custom {@code InstanceOfAssertFactory} instances.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeeds
   * Object string = &quot;abc&quot;;
   * assertThat(string).asInstanceOf(InstanceOfAssertFactories.STRING).startsWith(&quot;ab&quot;);
   *
   * Object integer = 1;
   * assertThat(integer).asInstanceOf(InstanceOfAssertFactories.INTEGER).isNotZero();
   *
   * // assertions fails
   * assertThat(&quot;abc&quot;).asInstanceOf(InstanceOfAssertFactories.INTEGER);</code></pre>
   *
   * @param <SOFT_ASSERT> the type of the resulting {@code SoftAssert}.
   * @param softAssertFactory the factory which verifies the type and creates the new {@code SoftAssert}.
   * @return the narrowed {@code SoftAssert} instance.
   * @throws NullPointerException if the given factory is {@code null}.
   * @see DefaultSoftAssertFactory
   * @see SoftAssertFactories
   * @since 3.13.0
   */
  public <SOFT_ASSERT extends SoftAssert> SOFT_ASSERT asInstanceOf(
      DefaultSoftAssertFactory<?, SOFT_ASSERT> softAssertFactory) {
    return softAssertFactory.createSoftAssert(actual(), errorCollector);
  }

  /**
   * Sets the description of the assertion that is going to be called after.
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * This overloaded version of "describedAs" offers more flexibility than the one taking a {@code String} by allowing
   * users to pass their own implementation of a description. For example, a description that creates its value lazily,
   * only when an assertion failure occurs.
   * </p>
   *
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   * @see #describedAs(Description)
   */
  public SoftRecursiveAssertionAssert as(Description description) {
    recursiveAssertionAssert.as(description);
    return this;
  }

  /**
   * Returns a String assertion for the <code>toString()</code> of the actual
   * value, to allow chaining of String-specific assertions from this call.
   * <p>
   * Example :
   * <pre><code class='java'> Object stringAsObject = "hello world";
   *
   * // assertion succeeds
   * assertThat(stringAsObject).asString().contains("hello");
   *
   * // assertions fails
   * assertThat(stringAsObject).asString().contains("holla");</code></pre>
   *
   * @return a string assertion object
   */
  public SoftStringAssert asString() {
    return new SoftStringAssert(actual().toString(), errorCollector);
  }

  /**
   * Sets the description of the assertion that is going to be called after.
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * Alias for <code>{@link #as(String, Object...)}</code> since "as" is a keyword in <a
   * href="https://groovy-lang.org/" target="_blank">Groovy</a>.
   *
   * @param description the new description to set.
   * @param args optional parameter if description is a format String.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   */
  public SoftRecursiveAssertionAssert describedAs(String description, Object... args) {
    recursiveAssertionAssert.describedAs(description,args);
    return this;
  }

  /**
   * Lazily specifies the description of the assertion that is going to be called, the given description is <b>not</b> evaluated
   * if the assertion succeeds.
   * <p>
   * The description must be set <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * Example :
   * <pre><code class='java'> // set an incorrect age to Mr Frodo which we all know is 33 years old.
   * frodo.setAge(50);
   *
   * // the lazy test description is <b>not</b> evaluated as the assertion succeeds
   * assertThat(frodo.getAge()).as(() -&gt; &quot;check Frodo's age&quot;).isEqualTo(50);
   *
   * try
   * {
   *   // the lazy test description is evaluated as the assertion fails
   *   assertThat(frodo.getAge()).as(() -&gt; &quot;check Frodo's age&quot;).isEqualTo(33);
   * }
   * catch (AssertionError e)
   * {
   *   assertThat(e).hasMessage(&quot;[check Frodo's age]\n
   *                             expected: 33\n
   *                              but was: 50&quot;);
   * }</code></pre>
   *
   * @param descriptionSupplier the description {@link Supplier}.
   * @return {@code this} object.
   * @throws IllegalStateException if the descriptionSupplier is {@code null} when evaluated.
   */
  public SoftRecursiveAssertionAssert describedAs(Supplier<String> descriptionSupplier) {
    recursiveAssertionAssert.describedAs(descriptionSupplier);
    return this;
  }

  /**
   * Sets the description of the assertion that is going to be called after.
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * This overloaded version of "describedAs" offers more flexibility than the one taking a {@code String} by allowing
   * users to pass their own implementation of a description. For example, a description that creates its value lazily,
   * only when an assertion failure occurs.
   * </p>
   *
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   */
  public SoftRecursiveAssertionAssert describedAs(Description description) {
    recursiveAssertionAssert.describedAs(description);
    return this;
  }

  /**
   * The description of this assertion set with {@link #describedAs(String, Object...)} or
   * {@link #describedAs(Description)}.
   *
   * @return the description String representation of this assertion.
   */
  public SoftRecursiveAssertionAssert descriptionText() {
    recursiveAssertionAssert.descriptionText();
    return this;
  }

  /**
   * Verifies that the actual value does not satisfy the given condition. This method is an alias for
   * <code>{@link #isNot(Condition)}</code>.
   *
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value satisfies the given condition.
   * @see #isNot(Condition)
   */
  public SoftRecursiveAssertionAssert doesNotHave(Condition<? super Object> condition) {
    try {
      recursiveAssertionAssert.doesNotHave(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value does not have the same class as the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).doesNotHaveSameClassAs(&quot;abc&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).doesNotHaveSameClassAs(new LinkedList&lt;String&gt;());
   *
   * // assertions fail
   * assertThat(1).doesNotHaveSameClassAs(2);
   * assertThat(&quot;abc&quot;).doesNotHaveSameClassAs(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).doesNotHaveSameClassAs(new ArrayList&lt;Integer&gt;());</code></pre>
   *
   * @param other the object to check type against.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} has the same type as the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  public SoftRecursiveAssertionAssert doesNotHaveSameClassAs(Object other) {
    try {
      recursiveAssertionAssert.doesNotHaveSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual object does not have the same hashCode as the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(42L).doesNotHaveSameHashCodeAs(2501L);
   * assertThat(&quot;The Force&quot;).doesNotHaveSameHashCodeAs(&quot;Awakens&quot;);
   *
   * // assertions fail
   * assertThat(42L).doesNotHaveSameHashCodeAs(42L);
   * assertThat(&quot;The Force&quot;).doesNotHaveSameHashCodeAs(&quot;The Force&quot;);
   * assertThat(new Jedi(&quot;Yoda&quot;, &quot;Blue&quot;)).doesNotHaveSameHashCodeAs(new Jedi(&quot;Yoda&quot;, &quot;Blue&quot;)); </code></pre>
   *
   * @param other the object to check hashCode against.
   * @return this assertion object.
   * @throws AssertionError if the actual object is null.
   * @throws NullPointerException if the other object is null.
   * @throws AssertionError if the actual object has the same hashCode as the given object.
   * @since 3.19.0
   */
  public SoftRecursiveAssertionAssert doesNotHaveSameHashCodeAs(Object other) {
    try {
      recursiveAssertionAssert.doesNotHaveSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that actual {@code actual.toString()} is not equal to the given {@code String}.
   * <p>
   * Example :
   * <pre><code class='java'> CartoonCharacter homer = new CartoonCharacter("Homer");
   *
   * // Instead of writing ...
   * assertThat(homer.toString()).isNotEqualTo("Marge");
   * // ... you can simply write:
   * assertThat(homer).doesNotHaveToString("Marge");</code></pre>
   *
   * @param otherToString the String to check against.
   * @return this assertion object.
   * @throws AssertionError if {@code actual.toString()} result is equal to the given {@code String}.
   * @throws AssertionError if actual is {@code null}.
   */
  public SoftRecursiveAssertionAssert doesNotHaveToString(String otherToString) {
    try {
      recursiveAssertionAssert.doesNotHaveToString(otherToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that actual {@code actual.toString()} is not equal to the given {@code String}.
   * <p>
   * Example:
   * <pre><code class='java'> Foo foo = new Foo();
   * Bar bar = new Bar();
   * FooBarWrapper wrapper = new FooBarWrapper(bar);
   *
   * assertThat(wrapper).doesNotHaveToString("FooBarWrapper[%s]", foo);</code></pre>
   *
   * @param expectedStringTemplate the format string to use.
   * @param args the arguments to interpolate into the format string.
   * @return this assertion object.
   * @throws AssertionError if {@code actual.toString()} result is equal to the given {@code String}.
   * @throws AssertionError if actual is {@code null}.
   * @since 3.25.0
   */
  public SoftRecursiveAssertionAssert doesNotHaveToString(String expectedStringTemplate,
      Object... args) {
    try {
      recursiveAssertionAssert.doesNotHaveToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual object does not match the given predicate.
   * <p>
   * Example:
   *
   * <pre><code class='java'> assertThat(player).doesNotMatch(p -&gt; p.isRookie());</code></pre>
   *
   * @param predicate the {@link Predicate} not to match
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} matches the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   */
  public SoftRecursiveAssertionAssert doesNotMatch(Predicate<? super Object> predicate) {
    try {
      recursiveAssertionAssert.doesNotMatch(predicate);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual object does not match the given predicate,
   * the predicate description is used to get an informative error message.
   * <p>
   * Example:
   *
   * <pre><code class='java'> assertThat(player).doesNotMatch(p -&gt; p.isRookie(), "is rookie");</code></pre>
   * <p>
   * The error message contains the predicate description, in our example, it is:
   *
   * <pre><code class='java'> Expecting:
   *   &lt;player&gt;
   * not to match 'is rookie' predicate.</code></pre>
   *
   * @param predicate the {@link Predicate} not to match
   * @param predicateDescription a description of the {@link Predicate} used in the error message
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} matches the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   * @throws NullPointerException if given predicateDescription is null.
   */
  public SoftRecursiveAssertionAssert doesNotMatch(Predicate<? super Object> predicate,
      String predicateDescription) {
    try {
      recursiveAssertionAssert.doesNotMatch(predicate,predicateDescription);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   *
   * @throws UnsupportedOperationException if this method is called.
   * @deprecated Throws <code>{@link UnsupportedOperationException}</code> if called. It is easy to accidentally call
   * <code>equals(Object)</code> instead of <code>{@link #isEqualTo(Object)}</code>.
   */
  @Deprecated(
      since = "3"
  )
  public boolean equals(Object obj) {
    return recursiveAssertionAssert.equals(obj);
  }

  /**
   * Exposes the {@link WritableAssertionInfo} used in the current assertion for better extensibility.<br> When writing
   * your own assertion class, you can use the returned {@link WritableAssertionInfo} to change the error message and
   * still keep the description set by the assertion user.
   *
   * @return the {@link WritableAssertionInfo} used in the current assertion
   */
  public WritableAssertionInfo getWritableAssertionInfo() {
    return recursiveAssertionAssert.getWritableAssertionInfo();
  }

  /**
   * Verifies that the actual value satisfies the given condition. This method is an alias for <code>{@link #is(Condition)}</code>
   * .
   *
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value does not satisfy the given condition.
   * @see #is(Condition)
   */
  public SoftRecursiveAssertionAssert has(Condition<? super Object> condition) {
    try {
      recursiveAssertionAssert.has(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Always returns 1.
   *
   * @return 1.
   */
  public int hashCode() {
    return recursiveAssertionAssert.hashCode();
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
  public SoftRecursiveAssertionAssert hasNoNullFields() {
    try {
      recursiveAssertionAssert.hasNoNullFields();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value has the same class as the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).hasSameClassAs(2);
   * assertThat(&quot;abc&quot;).hasSameClassAs(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).hasSameClassAs(new ArrayList&lt;Integer&gt;());
   *
   * // assertions fail
   * assertThat(1).hasSameClassAs(&quot;abc&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).hasSameClassAs(new LinkedList&lt;String&gt;());</code></pre>
   *
   * @param other the object to check type against.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} has not the same type as the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  public SoftRecursiveAssertionAssert hasSameClassAs(Object other) {
    try {
      recursiveAssertionAssert.hasSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual object has the same hashCode as the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(42L).hasSameHashCodeAs(42L);
   * assertThat(&quot;The Force&quot;).hasSameHashCodeAs(&quot;The Force&quot;);
   * assertThat(new Jedi(&quot;Yoda&quot;, &quot;Blue&quot;)).hasSameHashCodeAs(new Jedi(&quot;Yoda&quot;, &quot;Blue&quot;));
   *
   * // assertions fail
   * assertThat(42L).hasSameHashCodeAs(2501L);
   * assertThat(null).hasSameHashCodeAs(&quot;The Force&quot;);
   * assertThat(&quot;The Force&quot;).hasSameHashCodeAs(&quot;Awakens&quot;);</code></pre>
   *
   * @param other the object to check hashCode against.
   * @return this assertion object.
   * @throws AssertionError if the actual object is null.
   * @throws NullPointerException if the other object is null.
   * @throws AssertionError if the actual object has not the same hashCode as the given object.
   * @since 2.9.0
   */
  public SoftRecursiveAssertionAssert hasSameHashCodeAs(Object other) {
    try {
      recursiveAssertionAssert.hasSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that actual {@code actual.toString()} is equal to the given {@code String}.
   * <p>
   * Example :
   * <pre><code class='java'> CartoonCharacter homer = new CartoonCharacter("Homer");
   *
   * // Instead of writing ...
   * assertThat(homer.toString()).isEqualTo("Homer");
   * // ... you can simply write:
   * assertThat(homer).hasToString("Homer");</code></pre>
   *
   * @param expectedToString the expected String description of actual.
   * @return this assertion object.
   * @throws AssertionError if {@code actual.toString()} result is not equal to the given {@code String}.
   * @throws AssertionError if actual is {@code null}.
   */
  public SoftRecursiveAssertionAssert hasToString(String expectedToString) {
    try {
      recursiveAssertionAssert.hasToString(expectedToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that actual {@code actual.toString()} is equal to the given {@code String} when
   * {@link String#format formatted} with the given arguments.
   * <p>
   * Example:
   * <pre><code class='java'> Foo foo = new Foo();
   * FooWrapper wrapper = new FooWrapper(foo);
   *
   * assertThat(wrapper).hasToString("FooWrapper[foo=%s]", foo); </code></pre>
   *
   * @param expectedStringTemplate the format string to use.
   * @param args the arguments to interpolate into the format string.
   * @return this assertion object.
   * @throws AssertionError if {@code actual.toString()} result is not equal to the given {@code String}.
   * @throws AssertionError if actual is {@code null}.
   * @since 3.25.0
   */
  public SoftRecursiveAssertionAssert hasToString(String expectedStringTemplate, Object... args) {
    try {
      recursiveAssertionAssert.hasToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
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
   * @return this {@link SoftRecursiveAssertionAssert} to chain other methods.
   */
  public SoftRecursiveAssertionAssert ignoringAllNullFields() {
    try {
      recursiveAssertionAssert.ignoringAllNullFields();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
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
   * @return this {@link SoftRecursiveAssertionAssert} to chain other methods.
   */
  public SoftRecursiveAssertionAssert ignoringFields(String... fieldsToIgnore) {
    try {
      recursiveAssertionAssert.ignoringFields(fieldsToIgnore);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
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
   * @return this {@link SoftRecursiveAssertionAssert} to chain other methods.
   */
  public SoftRecursiveAssertionAssert ignoringFieldsMatchingRegexes(String... regexes) {
    try {
      recursiveAssertionAssert.ignoringFieldsMatchingRegexes(regexes);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
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
   * @return this {@link SoftRecursiveAssertionAssert} to chain other methods.
   */
  public SoftRecursiveAssertionAssert ignoringFieldsOfTypes(Class<?>... typesToIgnore) {
    try {
      recursiveAssertionAssert.ignoringFieldsOfTypes(typesToIgnore);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
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
   * @return this {@link SoftRecursiveAssertionAssert} to chain other methods.
   */
  public SoftRecursiveAssertionAssert ignoringPrimitiveFields() {
    try {
      recursiveAssertionAssert.ignoringPrimitiveFields();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value satisfies the given condition. This method is an alias for
   * <code>{@link #has(Condition)}</code>.
   *
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value does not satisfy the given condition.
   * @see #has(Condition)
   */
  public SoftRecursiveAssertionAssert is(Condition<? super Object> condition) {
    try {
      recursiveAssertionAssert.is(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is equal to the expected one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;abc&quot;);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isEqualTo(new HashMap&lt;String, Integer&gt;());
   *
   * // assertions fail
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).isEqualTo(1);</code></pre>
   *
   * <b>Note on testing {@link Object#equals(Object) equals} and {@link Object#hashCode() hashCode}
   * contracts</b>
   * <p>
   * {@code isEqualTo} uses a comparison strategy to determine equality and is <b>not</b> designed
   * to validate the correctness of {@code equals} or {@code hashCode} implementations. By default,
   * the comparison strategy delegates to the object's {@code equals} method (or performs deep
   * equality for arrays).
   * This means {@code isEqualTo} only verifies the <i>outcome</i> of a single equality check;
   * it does not validate that the full {@code equals} contract (reflexivity, symmetry,
   * transitivity, consistency, and null-handling) is satisfied.
   * <p>
   * If the {@code equals} implementation returns an incorrect result, {@code isEqualTo} will
   * accept that result without detecting the bug.
   * <p>
   * Additionally, the comparison strategy can be customized via
   * {@link org.assertj.core.api.AssertWithComparator#usingComparator(Comparator)} or {@link org.assertj.core.api.AssertWithComparator#usingEquals(BiPredicate)}.
   * In these cases, {@code isEqualTo} bypasses {@code equals} entirely in favor of the provided
   * logic.
   * <p>
   * For comprehensive testing of the {@code equals} and {@code hashCode} contracts, consider using
   * dedicated libraries such as:
   *
   *  <ul>
   * <li><a href="https://jqno.nl/equalsverifier/">EqualsVerifier</a></li>
   * <li>Guava's <a href="https://javadoc.io/doc/com.google.guava/guava-testlib/latest/com/google/common/testing/EqualsTester.html">EqualsTester</a></li>
   * </ul>
   *
   * To verify that two objects have the same field values without relying on {@code equals}, use
   * the {@link org.assertj.core.api.AbstractObjectAssert#usingRecursiveComparison() recursive comparison}.
   *
   * @param expected the expected value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public SoftRecursiveAssertionAssert isEqualTo(Object expected) {
    try {
      recursiveAssertionAssert.isEqualTo(expected);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isExactlyInstanceOf(ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isExactlyInstanceOf(HashMap.class);
   *
   * // assertions fail
   * assertThat(1).isExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isExactlyInstanceOf(List.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isExactlyInstanceOf(Map.class);</code></pre>
   *
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual is not <b>exactly</b> an instance of given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  public SoftRecursiveAssertionAssert isExactlyInstanceOf(Class<?> type) {
    try {
      recursiveAssertionAssert.isExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is present in the given iterable.
   * <p>
   * This assertion always fails if the given iterable is empty.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = list(vilya, nenya, narya);
   *
   * // assertion succeeds
   * assertThat(nenya).isIn(elvesRings);
   *
   * // assertions fail:
   * assertThat(oneRing).isIn(elvesRings);
   * assertThat(oneRing).isIn(emptyList());</code></pre>
   *
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given collection is {@code null}.
   * @throws AssertionError if the actual value is not present in the given iterable.
   */
  public SoftRecursiveAssertionAssert isIn(Iterable<?> values) {
    try {
      recursiveAssertionAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is present in the given array of values.
   * <p>
   * This assertion always fails if the given array of values is empty.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = new Ring[] { vilya, nenya, narya };
   *
   * // assertion succeeds
   * assertThat(nenya).isIn(elvesRings);
   *
   * // assertions fail
   * assertThat(oneRing).isIn(elvesRings);
   * assertThat(oneRing).isIn(new Ring[0]);</code></pre>
   *
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws AssertionError if the actual value is not present in the given array.
   */
  public SoftRecursiveAssertionAssert isIn(Object... values) {
    try {
      recursiveAssertionAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isInstanceOf(String.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isInstanceOf(HashMap.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isInstanceOf(Map.class);
   *
   * // assertions fail
   * assertThat(1).isInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isInstanceOf(LinkedList.class);</code></pre>
   *
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not an instance of the given type.
   */
  public SoftRecursiveAssertionAssert isInstanceOf(Class<?> type) {
    try {
      recursiveAssertionAssert.isInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is an instance of any of the given types.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isInstanceOfAny(String.class, Integer.class);
   * assertThat(new ArrayList&lt;String&gt;()).isInstanceOfAny(LinkedList.class, ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isInstanceOfAny(TreeMap.class, Map.class);
   *
   * // assertions fail
   * assertThat(1).isInstanceOfAny(Double.class, Float.class);
   * assertThat(new ArrayList&lt;String&gt;()).isInstanceOfAny(LinkedList.class, Vector.class);</code></pre>
   *
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not an instance of any of the given types.
   * @throws NullPointerException if the given array of types is {@code null}.
   * @throws NullPointerException if the given array of types contains {@code null}s.
   */
  public SoftRecursiveAssertionAssert isInstanceOfAny(Class<?>... types) {
    try {
      recursiveAssertionAssert.isInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is an instance of the given type satisfying the given requirements expressed as a {@link Consumer}.
   * <p>
   * This is useful to perform a group of assertions on a single object after checking its runtime type.
   * <p>
   * Example:
   * <pre><code class='java'> // second constructor parameter is the light saber color
   * Object yoda = new Jedi("Yoda", "Green");
   * Object luke = new Jedi("Luke Skywalker", "Green");
   *
   * Consumer&lt;Jedi&gt; jediRequirements = jedi -&gt; {
   *   assertThat(jedi.getLightSaberColor()).isEqualTo("Green");
   *   assertThat(jedi.getName()).doesNotContain("Dark");
   * };
   *
   * // assertions succeed:
   * assertThat(yoda).isInstanceOfSatisfying(Jedi.class, jediRequirements);
   * assertThat(luke).isInstanceOfSatisfying(Jedi.class, jediRequirements);
   *
   * // assertions fail:
   * Jedi vader = new Jedi("Vader", "Red");
   * assertThat(vader).isInstanceOfSatisfying(Jedi.class, jediRequirements);
   * // not a Jedi !
   * assertThat("foo").isInstanceOfSatisfying(Jedi.class, jediRequirements);</code></pre>
   *
   * @param <T> the generic type to check the actual value against.
   * @param type the type to check the actual value against.
   * @param requirements the requirements expressed as a {@link Consumer}.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws NullPointerException if the given Consumer is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not an instance of the given type.
   */
  public <T> SoftRecursiveAssertionAssert isInstanceOfSatisfying(Class<T> type,
      Consumer<T> requirements) {
    try {
      recursiveAssertionAssert.isInstanceOfSatisfying(type,requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value does not satisfy the given condition. This method is an alias for
   * <code>{@link #doesNotHave(Condition)}</code>.
   *
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value satisfies the given condition.
   * @see #isNot(Condition)
   */
  public SoftRecursiveAssertionAssert isNot(Condition<? super Object> condition) {
    try {
      recursiveAssertionAssert.isNot(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is not equal to the expected one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isNotEqualTo(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).isNotEqualTo(1);
   *
   * // assertions fail
   * assertThat(&quot;abc&quot;).isNotEqualTo(&quot;abc&quot;);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotEqualTo(new HashMap&lt;String, Integer&gt;());</code></pre>
   *
   * @param other the expected value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public SoftRecursiveAssertionAssert isNotEqualTo(Object other) {
    try {
      recursiveAssertionAssert.isNotEqualTo(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is not <b>exactly</b> an instance of given type.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).isNotExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotExactlyInstanceOf(List.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotExactlyInstanceOf(Map.class);
   *
   * // assertions fail
   * assertThat(&quot;abc&quot;).isNotExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotExactlyInstanceOf(ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotExactlyInstanceOf(HashMap.class);</code></pre>
   *
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual is exactly an instance of given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  public SoftRecursiveAssertionAssert isNotExactlyInstanceOf(Class<?> type) {
    try {
      recursiveAssertionAssert.isNotExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is not present in the given iterable.
   * <p>
   * This assertion always succeeds if the given iterable is empty.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = list(vilya, nenya, narya);
   *
   * // assertions succeed:
   * assertThat(oneRing).isNotIn(elvesRings);
   * assertThat(oneRing).isNotIn(emptyList());
   *
   * // assertions fails:
   * assertThat(nenya).isNotIn(elvesRings);</code></pre>
   *
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws AssertionError if the actual value is present in the given iterable.
   */
  public SoftRecursiveAssertionAssert isNotIn(Iterable<?> values) {
    try {
      recursiveAssertionAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is not present in the given array of values.
   * <p>
   * This assertion always succeeds if the given array of values is empty.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = new Ring[] { vilya, nenya, narya };
   *
   * // assertions succeed
   * assertThat(oneRing).isNotIn(elvesRings);
   * assertThat(oneRing).isNotIn(new Ring[0]);
   *
   * // assertions fails:
   * assertThat(nenya).isNotIn(elvesRings);</code></pre>
   *
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws AssertionError if the actual value is present in the given array.
   */
  public SoftRecursiveAssertionAssert isNotIn(Object... values) {
    try {
      recursiveAssertionAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is not an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).isNotInstanceOf(Double.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotInstanceOf(LinkedList.class);
   *
   * // assertions fail
   * assertThat(&quot;abc&quot;).isNotInstanceOf(String.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotInstanceOf(HashMap.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotInstanceOf(Map.class);</code></pre>
   *
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is an instance of the given type.
   */
  public SoftRecursiveAssertionAssert isNotInstanceOf(Class<?> type) {
    try {
      recursiveAssertionAssert.isNotInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is not an instance of any of the given types.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).isNotInstanceOfAny(Double.class, Float.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotInstanceOfAny(LinkedList.class, Vector.class);
   *
   * // assertions fail
   * assertThat(1).isNotInstanceOfAny(Double.class, Integer.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotInstanceOfAny(LinkedList.class, ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotInstanceOfAny(TreeMap.class, Map.class);</code></pre>
   *
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is an instance of any of the given types.
   * @throws NullPointerException if the given array of types is {@code null}.
   * @throws NullPointerException if the given array of types contains {@code null}s.
   */
  public SoftRecursiveAssertionAssert isNotInstanceOfAny(Class<?>... types) {
    try {
      recursiveAssertionAssert.isNotInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is not {@code null}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isNotNull();
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotNull();
   *
   * // assertions fails
   * String value = null;
   * assertThat(value).isNotNull();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   */
  public SoftRecursiveAssertionAssert isNotNull() {
    try {
      recursiveAssertionAssert.isNotNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value type is not in given types.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotOfAnyClassIn(Map.class, TreeMap.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotOfAnyClassIn(LinkedList.class, List.class);
   *
   * // assertions fail
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotOfAnyClassIn(HashMap.class, TreeMap.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotOfAnyClassIn(ArrayList.class, LinkedList.class);</code></pre>
   *
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value type is in given types.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  public SoftRecursiveAssertionAssert isNotOfAnyClassIn(Class<?>... types) {
    try {
      recursiveAssertionAssert.isNotOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is not the same as the given one
   * (reference equality, not {@link Object#equals(Object) equals}).
   * <p>
   * Example:
   * <pre><code class='java'> // Name is a class with first and last fields, two Names are equals if both first and last are equals.
   * Name tyrion = new Name("Tyrion", "Lannister");
   * Name alias  = tyrion;
   * Name clone  = new Name("Tyrion", "Lannister");
   *
   * // assertions succeed:
   * assertThat(clone).isNotSameAs(tyrion)
   *                  .isEqualTo(tyrion);
   *
   * // assertion fails:
   * assertThat(alias).isNotSameAs(tyrion);</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is the same as the given one.
   */
  public SoftRecursiveAssertionAssert isNotSameAs(Object other) {
    try {
      recursiveAssertionAssert.isNotSameAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is {@code null}.
   * <p>
   * Example:
   * <pre><code class='java'> String value = null;
   * // assertion succeeds
   * assertThat(value).isNull();
   *
   * // assertions fail
   * assertThat(&quot;abc&quot;).isNull();
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNull();</code></pre>
   *
   * @throws AssertionError if the actual value is not {@code null}.
   */
  public SoftRecursiveAssertionAssert isNull() {
    try {
      recursiveAssertionAssert.isNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value type is in given types.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(new HashMap&lt;String, Integer&gt;()).isOfAnyClassIn(HashMap.class, TreeMap.class);
   * assertThat(new ArrayList&lt;String&gt;()).isOfAnyClassIn(ArrayList.class, LinkedList.class);
   *
   * // assertions fail
   * assertThat(new HashMap&lt;String, Integer&gt;()).isOfAnyClassIn(TreeMap.class, Map.class);
   * assertThat(new ArrayList&lt;String&gt;()).isOfAnyClassIn(LinkedList.class, List.class);</code></pre>
   *
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value type is not in given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  public SoftRecursiveAssertionAssert isOfAnyClassIn(Class<?>... types) {
    try {
      recursiveAssertionAssert.isOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value is the same as the given one
   * (reference equality, not {@link Object#equals(Object) equals}).
   * <p>
   * Example:
   * <pre><code class='java'> // Name is a class with first and last fields, two Names are equals if both first and last are equals.
   * Name tyrion = new Name("Tyrion", "Lannister");
   * Name alias  = tyrion;
   * Name clone  = new Name("Tyrion", "Lannister");
   *
   * // assertions succeed:
   * assertThat(tyrion).isSameAs(alias)
   *                   .isEqualTo(clone);
   *
   * // assertion fails:
   * assertThat(tyrion).isSameAs(clone);</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not the same as the given one.
   */
  public SoftRecursiveAssertionAssert isSameAs(Object expected) {
    try {
      recursiveAssertionAssert.isSameAs(expected);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual object matches the given predicate.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(player).matches(p -&gt; p.isRookie());</code></pre>
   *
   * @param predicate the {@link Predicate} to match
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} does not match the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   */
  public SoftRecursiveAssertionAssert matches(Predicate<? super Object> predicate) {
    try {
      recursiveAssertionAssert.matches(predicate);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual object matches the given predicate, the predicate description is used to get an
   * informative error message.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(player).matches(p -&gt; p.isRookie(), "is rookie");</code></pre>
   * <p>
   * The error message contains the predicate description, in our example, it is:
   * <pre><code class='java'> Expecting:
   *   &lt;player&gt;
   * to match 'is rookie' predicate.</code></pre>
   *
   * @param predicate the {@link Predicate} to match
   * @param predicateDescription a description of the {@link Predicate} used in the error message
   * @return {@code this} assertion object.
   * @throws AssertionError if {@code actual} does not match the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   * @throws NullPointerException if given predicateDescription is null.
   */
  public SoftRecursiveAssertionAssert matches(Predicate<? super Object> predicate,
      String predicateDescription) {
    try {
      recursiveAssertionAssert.matches(predicate,predicateDescription);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Overrides AssertJ default error message by the given one.
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * The new error message is built using {@link String#format(String, Object...)} if you provide args parameter (if you
   * don't, the error message is taken as it is).
   * <p>
   * Example:
   * <pre><code class='java'>assertThat(player.isRookie()).overridingErrorMessage(&quot;Expecting Player &lt;%s&gt; to be a rookie but was not.&quot;, player)
   *                              .isTrue();</code></pre>
   *
   * @param newErrorMessage the error message that will replace the default one provided by Assertj.
   * @param args the args used to fill the error message as in {@link String#format(String, Object...)}.
   * @return this assertion object.
   */
  public SoftRecursiveAssertionAssert overridingErrorMessage(String newErrorMessage,
      Object... args) {
    recursiveAssertionAssert.overridingErrorMessage(newErrorMessage,args);
    return this;
  }

  /**
   * Overrides AssertJ default error message by the given one.
   * <p>
   * The new error message is only built if the assertion fails (by consuming the given supplier), this is useful if building messages is expensive.
   * <p>
   * You must set the message <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the call chain by throwing an {@link AssertionError}.
   * <p>
   * Example:
   * <pre><code class='java'>assertThat(player.isRookie()).overridingErrorMessage(() -&gt; &quot;Expecting Player to be a rookie but was not.&quot;)
   *                             .isTrue();</code></pre>
   *
   * @param supplier the supplier supplies error message that will replace the default one provided by Assertj.
   * @return this assertion object.
   */
  public SoftRecursiveAssertionAssert overridingErrorMessage(Supplier<String> supplier) {
    recursiveAssertionAssert.overridingErrorMessage(supplier);
    return this;
  }

  /**
   * Verifies that the actual object satisfied the given requirements expressed as {@link Consumer}s.
   * <p>
   * This is useful to perform a group of assertions on a single object, each passed assertion is evaluated and all failures are reported (to be precise each assertion can lead to one failure max).
   * <p>
   * Grouping assertions example :
   * <pre><code class='java'> // second constructor parameter is the light saber color
   * Jedi yoda = new Jedi("Yoda", "Green");
   * Jedi luke = new Jedi("Luke Skywalker", "Green");
   *
   * Consumer&lt;Jedi&gt; redLightSaber = jedi -&gt; assertThat(jedi.getLightSaberColor()).isEqualTo("Red");
   * Consumer&lt;Jedi&gt; greenLightSaber = jedi -&gt; assertThat(jedi.getLightSaberColor()).isEqualTo("Green");
   * Consumer&lt;Jedi&gt; notDarth = jedi -&gt; assertThat(jedi.getName()).doesNotContain("Darth");
   * Consumer&lt;Jedi&gt; darth = jedi -&gt; assertThat(jedi.getName()).contains("Darth");
   *
   * // assertions succeed:
   * assertThat(yoda).satisfies(greenLightSaber);
   * assertThat(luke).satisfies(greenLightSaber, notDarth);
   *
   * // assertions fail:
   * Jedi vader = new Jedi("Darth Vader", "Red");
   * assertThat(vader).satisfies(greenLightSaber);
   * assertThat(vader).satisfies(darth, greenLightSaber);
   * assertThat(vader).satisfies(greenLightSaber, notDarth);</code></pre>
   * <p>
   * In the following example, {@code satisfies} prevents the need to define a local variable in order to run multiple assertions:
   * <pre><code class='java'> // no need to define team.getPlayers().get(0).getStats() as a local variable
   * assertThat(team.getPlayers().get(0).getStats()).satisfies(stats -&gt; assertThat(stats.pointPerGame).isGreaterThan(25.7),
   *                                                           stats -&gt; assertThat(stats.assistsPerGame).isGreaterThan(7.2),
   *                                                           stats -&gt; assertThat(stats.reboundsPerGame).isBetween(9, 12));</code></pre>
   *
   * @param requirements to assert on the actual object - must not be null.
   * @return this assertion object.
   * @throws NullPointerException if any given Consumer is null
   */
  @SafeVarargs
  public final SoftRecursiveAssertionAssert satisfies(Consumer<? super Object>... requirements) {
    try {
      recursiveAssertionAssert.satisfies(requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual value satisfies the given condition. This method is an alias for <code>{@link #is(Condition)}</code>.
   * <p>
   * Example:
   * <pre><code class='java'> // Given
   * Condition&lt;String&gt; fairyTale = new Condition&lt;&gt;(s -&gt; s.startsWith("Once upon a time"), "fairy tale start");
   * // When
   * String littleRedCap = "Once upon a time there was a dear little girl ...";
   * // Then
   * assertThat(littleRedCap).satisfies(fairyTale);</code></pre>
   *
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value does not satisfy the given condition.
   * @see #is(Condition)
   * @since 3.11
   */
  public SoftRecursiveAssertionAssert satisfies(Condition<? super Object> condition) {
    try {
      recursiveAssertionAssert.satisfies(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual object satisfied the given requirements expressed as {@link ThrowingConsumer}s.
   * <p>
   * This is the same assertion as {@link #satisfies(Consumer[])} except that a {@link ThrowingConsumer} rethrows checked exceptions as {@link RuntimeException}.
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}.
   * If each assertion is passed as a separate argument, all of them will be evaluated and assertion error will contain all failures.
   * <p>
   * Example:
   * <pre><code class='java'> // read() throws IOException
   * ThrowingConsumer&lt;Reader&gt; hasReachedEOF = reader -&gt; assertThat(reader.read()).isEqualTo(-1);
   * ThrowingConsumer&lt;Reader&gt; nextCharacterA = reader -&gt; assertThat(reader.read()).isEqualTo('a');
   * ThrowingConsumer&lt;Reader&gt; nextCharacterB = reader -&gt; assertThat(reader.read()).isEqualTo('b');
   * ThrowingConsumer&lt;Reader&gt; nextCharacterZ = reader -&gt; assertThat(reader.read()).isEqualTo('z');
   *
   * // alphabet.txt contains: abcdefghijklmnopqrstuvwxyz
   * // empty.txt is empty
   *
   * // assertion succeeds:
   * assertThat(new FileReader("empty.txt")).satisfies(hasReachedEOF);
   * assertThat(new FileReader("alphabet.txt")).satisfies(nextCharacterA, nextCharacterB);
   *
   * // assertion fails:
   * assertThat(new FileReader("alphabet.txt")).satisfies(nextCharacterA, hasReachedEOF);
   * assertThat(new FileReader("alphabet.txt")).satisfies(nextCharacterB, nextCharacterZ);</code></pre>
   *
   * @param assertions the group of assertions to run against the object under test - must not be null.
   * @return this assertion object.
   * @throws IllegalArgumentException if any given assertions group is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.
   * @throws AssertionError rethrown as is by the given {@link ThrowingConsumer}
   * @since 3.21.0
   */
  @SafeVarargs
  public final SoftRecursiveAssertionAssert satisfies(
      ThrowingConsumer<? super Object>... assertions) {
    try {
      recursiveAssertionAssert.satisfies(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual object under test satisfies at least one of the given assertions group expressed as {@link Consumer}s.
   * <p>
   * This allows users to perform <b>OR like assertions</b> since only one the assertions group has to be met.
   * <p>
   * {@link #overridingErrorMessage(String, Object...) Overriding error message} is not supported as it would prevent from
   * getting the error messages of the failing assertions, these are valuable to figure out what went wrong.<br>
   * Describing the assertion is supported (for example with {@link #as(String, Object...)}).
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", HOBBIT);
   *
   * Consumer&lt;TolkienCharacter&gt; isHobbit = tolkienCharacter -&gt; assertThat(tolkienCharacter.getRace()).isEqualTo(HOBBIT);
   * Consumer&lt;TolkienCharacter&gt; isElf = tolkienCharacter -&gt; assertThat(tolkienCharacter.getRace()).isEqualTo(ELF);
   * Consumer&lt;TolkienCharacter&gt; isDwarf = tolkienCharacter -&gt; assertThat(tolkienCharacter.getRace()).isEqualTo(DWARF);
   *
   * // assertion succeeds:
   * assertThat(frodo).satisfiesAnyOf(isElf, isHobbit, isDwarf);
   *
   * // assertion fails:
   * TolkienCharacter boromir = new TolkienCharacter("Boromir", MAN);
   * assertThat(boromir).satisfiesAnyOf(isHobbit, isElf, isDwarf);</code></pre>
   *
   * @param assertions the group of assertions to run against the object under test - must not be null.
   * @return this assertion object.
   * @throws IllegalArgumentException if any given assertions group is null
   * @since 3.12.0
   */
  @SafeVarargs
  public final SoftRecursiveAssertionAssert satisfiesAnyOf(Consumer<? super Object>... assertions) {
    try {
      recursiveAssertionAssert.satisfiesAnyOf(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual object under test satisfies at least one of the given assertions group expressed as {@link ThrowingConsumer}s.
   * <p>
   * This allows users to perform <b>OR like assertions</b> since only one the assertions group has to be met.
   * <p>
   * This is the same assertion as {@link #satisfiesAnyOf(Consumer...)} but the given consumers can throw checked exceptions.<br>
   * More precisely, {@link RuntimeException} and {@link AssertionError} are rethrown as they are and {@link Throwable} wrapped in a {@link RuntimeException}.
   * <p>
   * {@link #overridingErrorMessage(String, Object...) Overriding error message} is not supported as it would prevent from
   * getting the error messages of the failing assertions, these are valuable to figure out what went wrong.<br>
   * Describing the assertion is supported (for example with {@link #as(String, Object...)}).
   * <p>
   * Example:
   * <pre><code class='java'> // read() throws IOException
   * ThrowingConsumer&lt;Reader&gt; hasReachedEOF = reader -&gt; assertThat(reader.read()).isEqualTo(-1);
   * ThrowingConsumer&lt;Reader&gt; startsWithZ = reader -&gt; assertThat(reader.read()).isEqualTo('Z');
   *
   * // assertion succeeds as the file is empty (note that if hasReachedEOF was declared as a Consumer&lt;Reader&gt; the following line would not compile):
   * assertThat(new FileReader("empty.txt")).satisfiesAnyOf(hasReachedEOF, startsWithZ);
   *
   * // alphabet.txt contains: abcdefghijklmnopqrstuvwxyz
   * // assertion fails as alphabet.txt is not empty and starts with 'a':
   * assertThat(new FileReader("alphabet.txt")).satisfiesAnyOf(hasReachedEOF, startsWithZ);</code></pre>
   *
   * @param assertions the group of assertions to run against the object under test - must not be null.
   * @return this assertion object.
   * @throws IllegalArgumentException if any given assertions group is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.
   * @throws AssertionError rethrown as is by the given {@link ThrowingConsumer}
   * @since 3.21.0
   */
  @SafeVarargs
  public final SoftRecursiveAssertionAssert satisfiesAnyOf(
      ThrowingConsumer<? super Object>... assertions) {
    try {
      recursiveAssertionAssert.satisfiesAnyOf(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Use the given custom comparator instead of relying on actual type A equals method for incoming assertion checks.
   * <p>
   * The custom comparator is bound to assertion instance, meaning that if a new assertion instance is created, the default
   * comparison strategy will be used.
   * <p>
   * Examples :
   * <pre><code class='java'> // frodo and sam are instances of Character with Hobbit race (obviously :).
   * // raceComparator implements Comparator&lt;Character&gt;
   * assertThat(frodo).usingComparator(raceComparator).isEqualTo(sam);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertion checks.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  public SoftRecursiveAssertionAssert usingComparator(Comparator<? super Object> customComparator) {
    recursiveAssertionAssert.usingComparator(customComparator);
    return this;
  }

  /**
   * Use the given custom comparator instead of relying on actual type A equals method for incoming assertion checks.
   * <p>
   * The custom comparator is bound to the current assertion chain, meaning that if a new assertion instance is created, the default
   * comparison strategy will be used.
   * <p>
   * Examples :
   * <pre><code class='java'> // frodo and sam are instances of Character with Hobbit race (obviously :).
   * // raceComparator implements Comparator&lt;Character&gt;
   * assertThat(frodo).usingComparator(raceComparator, "Hobbit Race Comparator").isEqualTo(sam);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertion checks.
   * @param customComparatorDescription comparator description to be used in assertion error messages
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  public SoftRecursiveAssertionAssert usingComparator(Comparator<? super Object> customComparator,
      String customComparatorDescription) {
    recursiveAssertionAssert.usingComparator(customComparator,customComparatorDescription);
    return this;
  }

  /**
   * Revert to standard comparison for the incoming assertion checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling {@link #usingComparator(Comparator) usingComparator}.
   *
   * @return {@code this} assertion object.
   */
  public SoftRecursiveAssertionAssert usingDefaultComparator() {
    recursiveAssertionAssert.usingDefaultComparator();
    return this;
  }

  /**
   * Uses the given custom {@link BiPredicate} instead of relying on actual type A {@code equals} method
   * for incoming assertion checks.
   * <p>
   * The custom equals is bound to the current assertion chain, meaning that if a new assertion instance is created, the default
   * comparison strategy will be used.
   * <p>
   * Examples:
   * <pre><code class='java'> // frodo and sam are instances of Character of Hobbit race (obviously :).
   * assertThat(frodo).usingEquals((f, s) -> f.race() == s.race()).isEqualTo(sam);</code></pre>
   *
   * @param predicate the predicate to use for the incoming assertion checks.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given biPredicate is {@code null}.
   */
  public SoftRecursiveAssertionAssert usingEquals(
      BiPredicate<? super Object, ? super Object> predicate) {
    recursiveAssertionAssert.usingEquals(predicate);
    return this;
  }

  /**
   * Uses the given custom {@link BiPredicate} instead of relying on actual type A {@code equals} method
   * for incoming assertion checks. The given description is present in the assertion error if the assertion fails.
   * <p>
   * The custom equals is bound to the current assertion chain, meaning that if a new assertion instance is created, the default
   * comparison strategy will be used.
   * <p>
   * Examples:
   * <pre><code class='java'> // frodo and sam are instances of Character of Hobbit race (obviously :).
   * assertThat(frodo).usingEquals((f, s) -> f.race() == s.race(), "comparing race").isEqualTo(sam);</code></pre>
   *
   * @param predicate the predicate to use for the incoming assertion checks.
   * @param customEqualsDescription comparator description to be used in assertion error messages
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  public SoftRecursiveAssertionAssert usingEquals(
      BiPredicate<? super Object, ? super Object> predicate, String customEqualsDescription) {
    recursiveAssertionAssert.usingEquals(predicate,customEqualsDescription);
    return this;
  }

  /**
   * Makes the recursive assertion to use the specified {@link RecursiveAssertionConfiguration.CollectionAssertionPolicy}.
   * <p>
   * See {@link RecursiveAssertionConfiguration.CollectionAssertionPolicy} for the different possible policies, by default
   * {@link RecursiveAssertionConfiguration.CollectionAssertionPolicy#ELEMENTS_ONLY} is used.
   *
   * @param collectionAssertionPolicy the {@link RecursiveAssertionConfiguration.CollectionAssertionPolicy} to use.
   * @return this {@link SoftRecursiveAssertionAssert} to chain other methods.
   */
  public SoftRecursiveAssertionAssert withCollectionAssertionPolicy(
      RecursiveAssertionConfiguration.CollectionAssertionPolicy collectionAssertionPolicy) {
    try {
      recursiveAssertionAssert.withCollectionAssertionPolicy(collectionAssertionPolicy);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Alternative method for {@link org.assertj.core.api.AbstractAssert#overridingErrorMessage}
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
   * <p>
   * Example:
   * <pre><code class='java'>assertThat(player.isRookie()).withFailMessage(&quot;Expecting Player &lt;%s&gt; to be a rookie but was not.&quot;, player)
   *                              .isTrue();</code></pre>
   *
   * @param newErrorMessage the error message that will replace the default one provided by Assertj.
   * @param args the args used to fill error message as in {@link String#format(String, Object...)}.
   * @return this assertion object.
   */
  public SoftRecursiveAssertionAssert withFailMessage(String newErrorMessage, Object... args) {
    recursiveAssertionAssert.withFailMessage(newErrorMessage,args);
    return this;
  }

  /**
   * Alternative method for {@link org.assertj.core.api.AbstractAssert#overridingErrorMessage}
   * <p>
   * The new error message is only built if the assertion fails (by consuming the given supplier), this is useful if building messages is expensive.
   * <p>
   * You must set the message <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the call chain by throwing an {@link AssertionError}.
   * <p>
   * Example:
   * <pre><code class='java'>assertThat(player.isRookie()).withFailMessage(() -&gt; &quot;Expecting Player to be a rookie but was not.&quot;)
   *                              .isTrue();</code></pre>
   *
   * @param supplier the supplier supplies error message that will replace the default one provided by Assertj.
   * @return this assertion object.
   */
  public SoftRecursiveAssertionAssert withFailMessage(Supplier<String> supplier) {
    recursiveAssertionAssert.withFailMessage(supplier);
    return this;
  }

  /**
   * Defines how objects are introspected in the recursive assertion.
   * <p>
   * Default to {@link org.assertj.core.api.recursive.assertion.DefaultRecursiveAssertionIntrospectionStrategy} that introspects all fields (including inherited ones).
   *
   * @param introspectionStrategy the {@link RecursiveAssertionIntrospectionStrategy} to use
   * @return this {@link SoftRecursiveAssertionAssert} to chain other methods.
   */
  public SoftRecursiveAssertionAssert withIntrospectionStrategy(
      RecursiveAssertionIntrospectionStrategy introspectionStrategy) {
    try {
      recursiveAssertionAssert.withIntrospectionStrategy(introspectionStrategy);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive assertion to use the specified {@link RecursiveAssertionConfiguration.MapAssertionPolicy}.
   * <p>
   * See {@link RecursiveAssertionConfiguration.MapAssertionPolicy} for the different possible policies, by default
   * {@link RecursiveAssertionConfiguration.MapAssertionPolicy#MAP_VALUES_ONLY} is used.
   *
   * @param mapAssertionPolicy the {@link RecursiveAssertionConfiguration.MapAssertionPolicy} to use.
   * @return this {@link SoftRecursiveAssertionAssert} to chain other methods.
   */
  public SoftRecursiveAssertionAssert withMapAssertionPolicy(
      RecursiveAssertionConfiguration.MapAssertionPolicy mapAssertionPolicy) {
    try {
      recursiveAssertionAssert.withMapAssertionPolicy(mapAssertionPolicy);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive assertion to use the specified {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy}.
   * <p>
   * See {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy} for the different possible policies, by default
   * {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy#OPTIONAL_VALUE_ONLY} is used.
   *
   * @param optionalAssertionPolicy the {@link RecursiveAssertionConfiguration.OptionalAssertionPolicy} to use.
   * @return this {@link SoftRecursiveAssertionAssert} to chain other methods.
   */
  public SoftRecursiveAssertionAssert withOptionalAssertionPolicy(
      RecursiveAssertionConfiguration.OptionalAssertionPolicy optionalAssertionPolicy) {
    try {
      recursiveAssertionAssert.withOptionalAssertionPolicy(optionalAssertionPolicy);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Use the given {@link Representation} to describe/represent values in AssertJ error messages.
   * <p>
   * The usual way to introduce a new {@link Representation} is to extend {@link org.assertj.core.presentation.StandardRepresentation}
   * and override any existing {@code toStringOf} methods that don't suit you. For example you can control
   * {@link java.util.Date} formatting by overriding {@code StandardRepresentation#toStringOf(Date)}).
   * <p>
   * You can also control other types format by overriding {@link org.assertj.core.presentation.StandardRepresentation#toStringOf(Object)})
   * calling your formatting method first and then fall back to the default representation by calling {@code super.toStringOf(Object)}.
   * <p>
   * Example :
   * <pre><code class='java'> private class Example {}
   *
   * private class CustomRepresentation extends StandardRepresentation {
   *
   *   // override needed to hook specific formatting
   *   {@literal @}Override
   *   public String toStringOf(Object o) {
   *     if (o instanceof Example) return "Example";
   *     // fall back to default formatting
   *     return super.toStringOf(o);
   *   }
   *
   *   // change String representation
   *   {@literal @}Override
   *   protected String toStringOf(String s) {
   *     return "&#36;" + s + "&#36;";
   *   }
   * }
   *
   * // next assertion fails with error : "expected:&lt;[null]&gt; but was:&lt;[Example]&gt;"
   * Example example = new Example();
   * assertThat(example).withRepresentation(new CustomRepresentation())
   *                    .isNull(); // example is not null !
   *
   * // next assertion fails ...
   * assertThat("foo").withRepresentation(new CustomRepresentation())
   *                  .startsWith("bar");
   * // ... with error :
   * Expecting:
   *  &lt;&#36;foo&#36;&gt;
   * to start with:
   *  &lt;&#36;bar&#36;&gt;</code></pre>
   *
   * @param representation Describe/represent values in AssertJ error messages.
   * @return this assertion object.
   */
  public SoftRecursiveAssertionAssert withRepresentation(Representation representation) {
    recursiveAssertionAssert.withRepresentation(representation);
    return this;
  }

  /**
   * In case of an assertion error, a thread dump will be printed to {@link System#err}.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat("Messi").withThreadDumpOnError().isEqualTo("Ronaldo");</code></pre>
   * <p>
   * will print a thread dump, something similar to this:
   * <pre>{@code "JDWP Command Reader"
   * 	java.lang.Thread.State: RUNNABLE
   *
   * "JDWP Event Helper Thread"
   * 	java.lang.Thread.State: RUNNABLE
   *
   * "JDWP Transport Listener: dt_socket"
   * 	java.lang.Thread.State: RUNNABLE
   *
   * "Signal Dispatcher"
   * 	java.lang.Thread.State: RUNNABLE
   *
   * "Finalizer"
   * 	java.lang.Thread.State: WAITING
   * 		at java.lang.Object.wait(Native Method)
   * 		at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:135)
   * 		at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:151)
   * 		at java.lang.ref.Finalizer&#36;FinalizerThread.run(Finalizer.java:189)
   *
   * "Reference Handler"
   * 	java.lang.Thread.State: WAITING
   * 		at java.lang.Object.wait(Native Method)
   * 		at java.lang.Object.wait(Object.java:503)
   * 		at java.lang.ref.Reference&#36;ReferenceHandler.run(Reference.java:133)
   *
   * "main"
   * 	java.lang.Thread.State: RUNNABLE
   * 		at sun.management.ThreadImpl.dumpThreads0(Native Method)
   * 		at sun.management.ThreadImpl.dumpAllThreads(ThreadImpl.java:446)
   * 		at org.assertj.core.internal.Failures.threadDumpDescription(Failures.java:193)
   * 		at org.assertj.core.internal.Failures.printThreadDumpIfNeeded(Failures.java:141)
   * 		at org.assertj.core.internal.Failures.failure(Failures.java:91)
   * 		at org.assertj.core.internal.Objects.assertEqual(Objects.java:314)
   * 		at org.assertj.core.api.AbstractAssert.isEqualTo(AbstractAssert.java:198)
   * 		at org.assertj.examples.ThreadDumpOnErrorExample.main(ThreadDumpOnErrorExample.java:28)}</pre>
   *
   * @return this assertion object.
   */
  public SoftRecursiveAssertionAssert withThreadDumpOnError() {
    recursiveAssertionAssert.withThreadDumpOnError();
    return this;
  }
}
