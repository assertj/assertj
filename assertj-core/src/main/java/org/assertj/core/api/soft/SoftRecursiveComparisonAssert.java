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
import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonIntrospectionStrategy;
import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;

@Beta
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class SoftRecursiveComparisonAssert<SELF> implements SoftAssert {
  private final AssertionErrorCollector errorCollector;

  private final RecursiveComparisonAssert<?> recursiveComparisonAssert;

  public SoftRecursiveComparisonAssert(RecursiveComparisonAssert<?> recursiveComparisonAssert,
      AssertionErrorCollector errorCollector) {
    this.recursiveComparisonAssert = recursiveComparisonAssert;
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
    return recursiveComparisonAssert.actual();
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
  public SoftRecursiveComparisonAssert<SELF> as(String description, Object... args) {
    recursiveComparisonAssert.as(description,args);
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
  public SoftRecursiveComparisonAssert<SELF> as(Supplier<String> descriptionSupplier) {
    recursiveComparisonAssert.as(descriptionSupplier);
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
  public SoftRecursiveComparisonAssert<SELF> as(Description description) {
    recursiveComparisonAssert.as(description);
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
   * Makes the recursive comparison to only compare given actual fields and their subfields (no other fields will be compared).
   * <p>
   * Specifying a field will make all its subfields to be compared, for example specifying {@code person} will lead to compare
   * {@code person.name}, {@code person.address} and all other Person fields.<br>
   * On the other hand if you specify {@code person.name}, {@code person} won't be compared but {@code person.name} will be.
   * <p>
   * The fields are specified by name, not by value, for example you can specify {@code person.name} but not {@code "Jack"}
   * as {@code "Jack"} is not a field value.
   * <p>
   * {@code comparingOnlyFields} can be combined with ignoring fields methods to restrict further the fields actually compared,
   * the resulting compared fields = {specified compared fields} {@code -} {specified ignored fields}.<br>For example if the specified compared fields = {"foo", "bar", "baz"}
   * and the ignored fields = {"bar"} then only {"foo", "baz"} fields will be compared.
   * <p>
   * Usage example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person moriarty = new Person("Moriarty", 1.80);
   * moriarty.home.address.street = "Butcher Street";
   * moriarty.home.address.number = 221;
   *
   * // assertion succeeds as name and home.address.street fields are not compared.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .comparingOnlyFields("height", "home.address.number")
   *                     .isEqualTo(moriarty);
   *
   * // assertion fails as home.address.street fields differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .comparingOnlyFields("height", "home")
   *                     .isEqualTo(moriarty);</code></pre>
   *
   * @param fieldNamesToCompare the fields of the object under test to compare in the comparison.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> comparingOnlyFields(String... fieldNamesToCompare) {
    try {
      recursiveComparisonAssert.comparingOnlyFields(fieldNamesToCompare);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to only compare given actual fields of the specified types and their subfields (no other fields will be compared).
   * <p>
   * Specifying a compared type will make any fields of this type and its subfields to be compared, for example specifying the {@code Person} type will
   * lead to compare {@code Person.name}, {@code Person.address} and all other {@code Person} fields.<br>
   * In case actual's field is null, expected's field type will be checked to match one of the given types (we assume actual and expected fields have the same type).
   * <p>
   * {@code comparingOnlyFieldsOfTypes} can be combined with {@link #comparingOnlyFields(String...)} to compare fields of the given types <b>or</b> names (union of both sets of fields).
   * <p>
   * {@code comparingOnlyFieldsOfTypes} can be also combined with ignoring fields to restrict further the fields actually compared,
   * the resulting compared fields = {specified compared fields of types} {@code -} {specified ignored fields}.<br>
   * For example, we specify the following compared types: {@code {String.class, Integer.class, Double.class}}, and the
   * object to compare has fields {@code String foo}, {@code Integer baz} and {@code Double bar},
   * if we ignore the {"bar"} field with {@link SoftRecursiveComparisonAssert#ignoringFields(String...)} the comparison will only report differences on {@code {foo, baz}} fields.
   * <p>
   * Usage example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person moriarty = new Person("Moriarty", 1.80);
   * moriarty.home.address.street = "Butcher Street";
   * moriarty.home.address.number = 221;
   *
   *
   * // assertion succeeds as it only compared fields height and home.address.number since their types match compared types
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .comparingOnlyFieldsOfTypes(Integer.class, Double.class)
   *                     .isEqualTo(moriarty);
   *
   * // assertion fails as home.address.street fields differ (Home fields and its subfields were compared)
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .comparingOnlyFieldsOfTypes(Home.class)
   *                     .isEqualTo(moriarty);</code></pre>
   * <p>
   * Note that the recursive comparison checks whether the fields actually exist and throws an {@link IllegalArgumentException} if some of them don't,
   * this is done to catch typos.
   *
   * @param typesToCompare the types to compare in the recursive comparison.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> comparingOnlyFieldsOfTypes(
      Class<?>... typesToCompare) {
    try {
      recursiveComparisonAssert.comparingOnlyFieldsOfTypes(typesToCompare);
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
   * Alias for <code>{@link #as(String, Object...)}</code> since "as" is a keyword in <a
   * href="https://groovy-lang.org/" target="_blank">Groovy</a>.
   *
   * @param description the new description to set.
   * @param args optional parameter if description is a format String.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   */
  public SoftRecursiveComparisonAssert<SELF> describedAs(String description, Object... args) {
    recursiveComparisonAssert.describedAs(description,args);
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
  public SoftRecursiveComparisonAssert<SELF> describedAs(Supplier<String> descriptionSupplier) {
    recursiveComparisonAssert.describedAs(descriptionSupplier);
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
  public SoftRecursiveComparisonAssert<SELF> describedAs(Description description) {
    recursiveComparisonAssert.describedAs(description);
    return this;
  }

  /**
   * The description of this assertion set with {@link #describedAs(String, Object...)} or
   * {@link #describedAs(Description)}.
   *
   * @return the description String representation of this assertion.
   */
  public SoftRecursiveComparisonAssert<SELF> descriptionText() {
    recursiveComparisonAssert.descriptionText();
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
  public SoftRecursiveComparisonAssert<SELF> doesNotHave(Condition<? super Object> condition) {
    try {
      recursiveComparisonAssert.doesNotHave(condition);
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
  public SoftRecursiveComparisonAssert<SELF> doesNotHaveSameClassAs(Object other) {
    try {
      recursiveComparisonAssert.doesNotHaveSameClassAs(other);
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
  public SoftRecursiveComparisonAssert<SELF> doesNotHaveSameHashCodeAs(Object other) {
    try {
      recursiveComparisonAssert.doesNotHaveSameHashCodeAs(other);
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
  public SoftRecursiveComparisonAssert<SELF> doesNotHaveToString(String otherToString) {
    try {
      recursiveComparisonAssert.doesNotHaveToString(otherToString);
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
  public SoftRecursiveComparisonAssert<SELF> doesNotHaveToString(String expectedStringTemplate,
      Object... args) {
    try {
      recursiveComparisonAssert.doesNotHaveToString(expectedStringTemplate,args);
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
  public SoftRecursiveComparisonAssert<SELF> doesNotMatch(Predicate<? super Object> predicate) {
    try {
      recursiveComparisonAssert.doesNotMatch(predicate);
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
  public SoftRecursiveComparisonAssert<SELF> doesNotMatch(Predicate<? super Object> predicate,
      String predicateDescription) {
    try {
      recursiveComparisonAssert.doesNotMatch(predicate,predicateDescription);
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
    return recursiveComparisonAssert.equals(obj);
  }

  /**
   * Returns the {@link RecursiveComparisonConfiguration} currently used.
   *
   * @return the {@link RecursiveComparisonConfiguration} currently used
   */
  public RecursiveComparisonConfiguration getRecursiveComparisonConfiguration() {
    return recursiveComparisonAssert.getRecursiveComparisonConfiguration();
  }

  /**
   * Exposes the {@link WritableAssertionInfo} used in the current assertion for better extensibility.<br> When writing
   * your own assertion class, you can use the returned {@link WritableAssertionInfo} to change the error message and
   * still keep the description set by the assertion user.
   *
   * @return the {@link WritableAssertionInfo} used in the current assertion
   */
  public WritableAssertionInfo getWritableAssertionInfo() {
    return recursiveComparisonAssert.getWritableAssertionInfo();
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
  public SoftRecursiveComparisonAssert<SELF> has(Condition<? super Object> condition) {
    try {
      recursiveComparisonAssert.has(condition);
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
    return recursiveComparisonAssert.hashCode();
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
  public SoftRecursiveComparisonAssert<SELF> hasSameClassAs(Object other) {
    try {
      recursiveComparisonAssert.hasSameClassAs(other);
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
  public SoftRecursiveComparisonAssert<SELF> hasSameHashCodeAs(Object other) {
    try {
      recursiveComparisonAssert.hasSameHashCodeAs(other);
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
  public SoftRecursiveComparisonAssert<SELF> hasToString(String expectedToString) {
    try {
      recursiveComparisonAssert.hasToString(expectedToString);
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
  public SoftRecursiveComparisonAssert<SELF> hasToString(String expectedStringTemplate,
      Object... args) {
    try {
      recursiveComparisonAssert.hasToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore all <b>actual empty optional fields</b> (including {@link java.util.Optional}, {@link java.util.OptionalInt}, {@link java.util.OptionalLong} and {@link java.util.OptionalDouble}),
   * note that the expected object empty optional fields are not ignored, this only applies to actual's fields.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   OptionalInt age;
   *   OptionalLong id;
   *   OptionalDouble height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   String address;
   *   Optional&lt;String&gt; phone;
   * }
   *
   * Person homerWithoutDetails = new Person("Homer Simpson");
   * homerWithoutDetails.home.address.street = "Evergreen Terrace";
   * homerWithoutDetails.home.address.number = 742;
   * homerWithoutDetails.home.phone = Optional.empty();
   * homerWithoutDetails.age = OptionalInt.empty();
   * homerWithoutDetails.id = OptionalLong.empty();
   * homerWithoutDetails.height = OptionalDouble.empty();
   *
   * Person homerWithDetails = new Person("Homer Simpson");
   * homerWithDetails.home.address.street = "Evergreen Terrace";
   * homerWithDetails.home.address.number = 742;
   * homerWithDetails.home.phone = Optional.of("(939) 555-0113");
   * homerWithDetails.age = OptionalInt.of(39);
   * homerWithDetails.id = OptionalLong.of(123456);
   * homerWithDetails.height = OptionalDouble.of(1.83);
   *
   * // assertion succeeds as phone is ignored in the comparison
   * assertThat(homerWithoutDetails).usingRecursiveComparison()
   *                                .ignoringActualEmptyOptionalFields()
   *                                .isEqualTo(homerWithDetails);
   *
   * // assertion fails as phone, age, id and height are not ignored and are populated for homerWithDetails but not for homerWithoutDetails.
   * assertThat(homerWithDetails).usingRecursiveComparison()
   *                             .ignoringActualEmptyOptionalFields()
   *                             .isEqualTo(homerWithoutDetails);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringActualEmptyOptionalFields() {
    try {
      recursiveComparisonAssert.ignoringActualEmptyOptionalFields();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore all <b>actual null fields</b> (but note that the expected object null fields are used in the comparison).
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person noName = new Person(null, 1.80);
   * noName.home.address.street = null;
   * noName.home.address.number = 221;
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * // assertion succeeds as name and home.address.street fields are ignored in the comparison
   * assertThat(noName).usingRecursiveComparison()
   *                   .ignoringActualNullFields()
   *                   .isEqualTo(sherlock);
   *
   * // assertion fails as name and home.address.street fields are populated for sherlock but not for noName.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringActualNullFields()
   *                     .isEqualTo(noName);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringActualNullFields() {
    try {
      recursiveComparisonAssert.ignoringActualNullFields();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * This method instructs the recursive comparison to compare recursively all fields including the one whose type have overridden equals,
   * <b>except fields with java types</b> (at some point we need to compare something!).
   * <p>
   * Since 3.17.0 this is the default behavior for recursive comparisons, to revert to the previous behavior call {@link #usingOverriddenEquals()}.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number, ouch!
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // Assertion succeeds because:
   * // - overridden equals are used
   * // - Address has overridden equals and does not compare street fields.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // To avoid using Address overridden equals, don't call usingOverriddenEquals() or call ignoringAllOverriddenEquals()
   * // (calling ignoringAllOverriddenEquals() is actually not required as this is the default behavior).
   * // This assertion fails as it will compare home.address.street fields which differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                      //.ignoringAllOverriddenEquals() // not needed as this is the default
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringAllOverriddenEquals() {
    try {
      recursiveComparisonAssert.ignoringAllOverriddenEquals();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore array order in all fields of the object under test.
   * <p>
   * <b>Important:</b> ignoring array order has a high performance cost because each element of the actual array must
   * be compared to each element of the expected array which is an O(n&sup2;) operation. For example with a array of 100
   * elements, the number of comparisons is 100x100 = 10 000!
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   Person[] friends = new Person[0];
   *   void add(FriendlyPerson newFriend) {
   *     // ... add newFriend to friends array
   *   }
   * }
   *
   * Person sherlock1 = new Person("Sherlock Holmes");
   * sherlock1.add(new Person("Dr. John Watson"));
   * sherlock1.add(new Person("Molly Hooper"));
   *
   * Person sherlock2 = new Person("Sherlock Holmes");
   * sherlock2.add(new Person("Molly Hooper"));
   * sherlock2.add(new Person("Dr. John Watson"));
   *
   * // assertion succeeds as all fields array order is ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringArrayOrder()
   *                      .isEqualTo(sherlock2);
   *
   * // assertion fails as fields array order is not ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .isEqualTo(sherlock2);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringArrayOrder() {
    try {
      recursiveComparisonAssert.ignoringArrayOrder();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore collection order in all fields of the object under test.
   * <p>
   * <b>Important:</b> ignoring collection order has a high performance cost because each element of the actual collection must
   * be compared to each element of the expected collection which is an O(n&sup2;) operation. For example with a collection of 100
   * elements, the number of comparisons is 100x100 = 10 000!
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   List&lt;Person&gt; friends = new ArrayList&lt;&gt;();
   * }
   *
   * Person sherlock1 = new Person("Sherlock Holmes");
   * sherlock1.friends.add(new Person("Dr. John Watson"));
   * sherlock1.friends.add(new Person("Molly Hooper"));
   *
   * Person sherlock2 = new Person("Sherlock Holmes");
   * sherlock2.friends.add(new Person("Molly Hooper"));
   * sherlock2.friends.add(new Person("Dr. John Watson"));
   *
   * // assertion succeeds as all fields collection order is ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrder()
   *                      .isEqualTo(sherlock2);
   *
   * // assertion fails as fields collection order is not ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .isEqualTo(sherlock2);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringCollectionOrder() {
    try {
      recursiveComparisonAssert.ignoringCollectionOrder();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore collection order in the object under test specified fields. Nested fields can be specified like this: {@code home.address.street}.
   * <p>
   * <b>Important:</b> ignoring collection order has a high performance cost because each element of the actual collection must
   * be compared to each element of the expected collection which is a O(n&sup2;) operation. For example with a collection of 100
   * elements, the number of comparisons is 100x100 = 10 000!
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   List&lt;Person&gt; friends = new ArrayList&lt;&gt;();
   *   List&lt;Person&gt; enemies = new ArrayList&lt;&gt;();
   * }
   *
   * Person sherlock1 = new Person("Sherlock Holmes");
   * sherlock1.friends.add(new Person("Dr. John Watson"));
   * sherlock1.friends.add(new Person("Molly Hooper"));
   * sherlock1.enemies.add(new Person("Jim Moriarty"));
   * sherlock1.enemies.add(new Person("Irene Adler"));
   *
   * Person sherlock2 = new Person("Sherlock Holmes");
   * sherlock2.friends.add(new Person("Molly Hooper"));
   * sherlock2.friends.add(new Person("Dr. John Watson"));
   * sherlock2.enemies.add(new Person("Irene Adler"));
   * sherlock2.enemies.add(new Person("Jim Moriarty"));
   *
   * // assertion succeeds as friends and enemies fields collection order is ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrderInFields("friends", "enemies")
   *                      .isEqualTo(sherlock2);
   *
   * // assertion fails as enemies field collection order differ and it is not ignored
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrderInFields("friends")
   *                      .isEqualTo(sherlock2);</code></pre>
   *
   * @param fieldsToIgnoreCollectionOrder the fields of the object under test to ignore collection order in the comparison.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringCollectionOrderInFields(
      String... fieldsToIgnoreCollectionOrder) {
    try {
      recursiveComparisonAssert.ignoringCollectionOrderInFields(fieldsToIgnoreCollectionOrder);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore collection order in the object under test fields matching the specified regexes.
   * <p>
   * Nested fields can be specified by using dots like this: {@code home\.address\.street} ({@code \} is used to escape
   * dots since they have a special meaning in regexes).
   * <p>
   * <b>Important:</b> ignoring collection order has a high performance cost because each element of the actual collection must
   * be compared to each element of the expected collection which is a O(n&sup2;) operation. For example with a collection of 100
   * elements, the number of comparisons is 100x100 = 10 000!
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   List&lt;Person&gt; friends = new ArrayList&lt;&gt;();
   *   List&lt;Person&gt; enemies = new ArrayList&lt;&gt;();
   * }
   *
   * Person sherlock1 = new Person("Sherlock Holmes");
   * sherlock1.friends.add(new Person("Dr. John Watson"));
   * sherlock1.friends.add(new Person("Molly Hooper"));
   * sherlock1.enemies.add(new Person("Jim Moriarty"));
   * sherlock1.enemies.add(new Person("Irene Adler"));
   *
   * Person sherlock2 = new Person("Sherlock Holmes");
   * sherlock2.friends.add(new Person("Molly Hooper"));
   * sherlock2.friends.add(new Person("Dr. John Watson"));
   * sherlock2.enemies.add(new Person("Irene Adler"));
   * sherlock2.enemies.add(new Person("Jim Moriarty"));
   *
   * // assertion succeeds as friends and enemies fields collection order is ignored in the comparison
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrderInFieldsMatchingRegexes("friend.", "enemie.")
   *                      .isEqualTo(sherlock2);
   *
   * // assertion fails as enemies field collection order differ and it is not ignored
   * assertThat(sherlock1).usingRecursiveComparison()
   *                      .ignoringCollectionOrderInFields("friend.")
   *                      .isEqualTo(sherlock2);</code></pre>
   *
   * @param regexes regexes used to find the object under test fields to ignore collection order in the comparison.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringCollectionOrderInFieldsMatchingRegexes(
      String... regexes) {
    try {
      recursiveComparisonAssert.ignoringCollectionOrderInFieldsMatchingRegexes(regexes);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore all <b>expected null fields</b>.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person noName = new Person(null, 1.80);
   * noName.home.address.street = null;
   * noName.home.address.number = 221;
   *
   * // assertion succeeds as name and home.address.street fields are ignored in the comparison
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringExpectedNullFields()
   *                     .isEqualTo(noName);
   *
   * // assertion fails as name and home.address.street fields are populated for sherlock but not for noName.
   * assertThat(noName).usingRecursiveComparison()
   *                   .ignoringExpectedNullFields()
   *                   .isEqualTo(sherlock);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringExpectedNullFields() {
    try {
      recursiveComparisonAssert.ignoringExpectedNullFields();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore the given object under test fields. Nested fields can be specified like this: {@code home.address.street}.
   * <p>
   * The given fieldNamesToIgnore are matched against field names, not field values.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person noName = new Person(null, 1.80);
   * noName.home.address.street = null;
   * noName.home.address.number = 221;
   *
   * // assertion succeeds as name and home.address.street fields are ignored in the comparison
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFields("name", "home.address.street")
   *                     .isEqualTo(noName);
   *
   * // assertion fails as home.address.street fields differ and is not ignored.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFields("name")
   *                     .isEqualTo(noName);</code></pre>
   *
   * @param fieldNamesToIgnore the field names of the object under test to ignore in the comparison.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringFields(String... fieldNamesToIgnore) {
    try {
      recursiveComparisonAssert.ignoringFields(fieldNamesToIgnore);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore the object under test fields whose name matches the given regexes.
   * <p>
   * Nested fields can be specified by using dots like this: {@code home\.address\.street} ({@code \} is used to escape
   * dots since they have a special meaning in regexes).
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person noName = new Person(null, 1.80);
   * noName.home.address.street = "Butcher Street";
   * noName.home.address.number = 222;
   *
   * // assertion succeeds as name and all home fields are ignored in the comparison
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFieldsMatchingRegexes("n.me", "home.*")
   *                     .isEqualTo(noName);
   *
   * // although home fields are ignored, assertion fails as name fields differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFieldsMatchingRegexes("home.*")
   *                     .isEqualTo(noName);</code></pre>
   *
   * @param regexes regexes used to ignore fields in the comparison.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringFieldsMatchingRegexes(String... regexes) {
    try {
      recursiveComparisonAssert.ignoringFieldsMatchingRegexes(regexes);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore the object under test fields of the given types.
   * <p>
   * The fields are ignored if their types <b>exactly match one of the ignored types</b>, for example,
   * if a field is a subtype of an ignored type it is not ignored.
   * <p>
   * If {@code strictTypeChecking} mode is disabled then null fields are ignored since their types cannot be known.
   * <p>
   * If {@code strictTypeChecking} mode is enabled and a field of the object under test is null, the recursive
   * comparison evaluates the corresponding expected field's type.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.90);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // assertion succeeds as we ignore Address and height
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFieldsOfTypes(double.class, Address.class)
   *                     .isEqualTo(sherlock2);
   *
   * // now this assertion fails as expected since the home.address.street fields and height differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param typesToIgnore the types we want to ignore in the object under test fields.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringFieldsOfTypes(Class<?>... typesToIgnore) {
    try {
      recursiveComparisonAssert.ignoringFieldsOfTypes(typesToIgnore);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore the fields of the object under test having types matching one of the given regexes.
   * The fields are ignored if their types <b>exactly match one of the regexes</b>, if a field is a subtype of a matched type it is not ignored.
   * <p>
   * One use case of this method is to ignore types that can't be introspected.
   * <p>
   * If {@code strictTypeChecking} mode is enabled and a field of the object under test is null, the recursive
   * comparison evaluates the corresponding expected field's type (if not null), if it is disabled then the field is evaluated as
   * usual (i.e. it is not ignored).
   * <p>
   * <b>Warning</b>: primitive types are not directly supported because under the hood they are converted to their
   * corresponding wrapping types, for example {@code int} to {@code java.lang.Integer}. The preferred way to ignore
   * primitive types is to use {@link #ignoringFieldsOfTypes(Class[])}.
   * Another way is to ignore the wrapping type, for example ignoring {@code java.lang.Integer} ignores both
   * {@code java.lang.Integer} and {@code int} fields.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person cherlock = new Person("Cherlock", 1.80);
   * cherlock.home.address.street = "Butcher Street";
   * cherlock.home.address.number = 221;
   *
   * // assertion succeeds as we ignore Address and height
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoringFieldsOfTypes(".*Address", "java\\.util\\.String")
   *                     .isEqualTo(cherlock);
   *
   * // now this assertion fails as expected since the home.address.street fields and name differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(cherlock);</code></pre>
   *
   * @param regexes regexes specifying the types to ignore.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringFieldsOfTypesMatchingRegexes(
      String... regexes) {
    try {
      recursiveComparisonAssert.ignoringFieldsOfTypesMatchingRegexes(regexes);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * In the recursive comparison, allows to ignore non-existent fields in the object under test when using
   * {@link #comparingOnlyFields(String...)}.
   * <p>
   * This is useful when comparing polymorphic objects where some fields might not exist in all subtypes.
   * <p>
   * Example:
   * <pre><code class='java'> class BaseClass {
   *   String commonField = "common";
   * }
   *
   * class SubType1 extends BaseClass {
   *   // No 'inSubType2' field
   *   String inSubType1 = "type1";
   * }
   *
   * class SubType2 extends SubType1 {
   *   String inSubType2 = "type2";
   * }
   *
   * SubType1 actual = new SubType1();
   * SubType2 expected = new SubType2();
   *
   * // this fails due to field existence check with an IllegalArgumentException
   * // indicating that 'inSubType2' field doesn't exist in SubType1.
   * assertThat(actual).usingRecursiveComparison()
   *                   .comparingOnlyFields("common", "inSubType1", "inSubType2")
   *                   .isEqualTo(expected);
   *
   * // Using ignoringNonExistentComparedFields() makes it pass
   * assertThat(actual).usingRecursiveComparison()
   *                   .comparingOnlyFields("common", "inSubType1", "inSubType2")
   *                   .ignoringNonExistentComparedFields()
   *                   .isEqualTo(expected); </code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to allow fluent chaining.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringNonExistentComparedFields() {
    try {
      recursiveComparisonAssert.ignoringNonExistentComparedFields();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * In case you have instructed the recursive to use overridden {@code equals} with {@link #usingOverriddenEquals()},
   * this method allows ignoring overridden {@code equals} for the given fields (it adds them to the already registered ones).
   * <p>
   * Since 3.17.0 all overridden {@code equals} so this method is only relevant if you have called {@link #usingOverriddenEquals()} before.
   * <p>
   * Nested fields can be specified by using dots like this: {@code home.address.street}
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // Assertion succeeds because:
   * // - overridden equals are used
   * // - Address has overridden equals and does not compare street fields.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // ignoringOverriddenEqualsForFields force a recursive comparison on the given field
   * // Assertion fails because:
   * // - Address equals is not used.
   * // - street fields are compared and differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .ignoringOverriddenEqualsForFields("home.address")
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param fields the fields we want to force a recursive comparison on.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringOverriddenEqualsForFields(String... fields) {
    try {
      recursiveComparisonAssert.ignoringOverriddenEqualsForFields(fields);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * In case you have instructed the recursive comparison to use overridden {@code equals} with {@link #usingOverriddenEquals()},
   * this method allows forcing a recursive comparison for the fields matching the given regexes (it adds them to the already registered ones).
   * <p>
   * Since 3.17.0 all overridden {@code equals} so this method is only relevant if you have called {@link #usingOverriddenEquals()} before.
   * <p>
   * Nested fields can be specified by using dots like: {@code home\.address\.street} ({@code \} is used to escape
   * dots since they have a special meaning in regexes).
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number, ouch!
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // assertion succeeds because overridden equals are used and thus street fields are mot compared
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // ignoringOverriddenEqualsForFields force a recursive comparison on the field matching the regex
   * // now this assertion fails as we expect since the home.address.street fields differ
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .ignoringOverriddenEqualsForFieldsMatchingRegexes("home.*")
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param regexes regexes used to specify the fields we want to force a recursive comparison on.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringOverriddenEqualsForFieldsMatchingRegexes(
      String... regexes) {
    try {
      recursiveComparisonAssert.ignoringOverriddenEqualsForFieldsMatchingRegexes(regexes);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * By default, the recursive comparison uses overridden {@code equals} methods to compare fields,
   * this method allows forcing a recursive comparison for all fields of the given types (it adds them to the already registered ones).
   * <p>
   * Since 3.17.0 all overridden {@code equals} so this method is only relevant if you have called {@link #usingOverriddenEquals()} before.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number, ouch!
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // Assertion succeeds because:
   * // - overridden equals are used
   * // - Address has overridden equals and does not compare street fields.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // ignoringOverriddenEqualsForTypes force a recursive comparison on the given types.
   * // Assertion fails because:
   * // - Address equals is not used.
   * // - street fields are compared and differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .ignoringOverriddenEqualsForTypes(Address.class)
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @param types the types we want to force a recursive comparison on.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringOverriddenEqualsForTypes(Class<?>... types) {
    try {
      recursiveComparisonAssert.ignoringOverriddenEqualsForTypes(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to ignore <a href="https://docs.oracle.com/javase/specs/jvms/se6/html/Concepts.doc.html#18858">transient</a> fields.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   transient double height;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * Person cherlock = new Person("Sherlock", 1.81);
   *
   * // assertion succeeds as we ignore the height transient field:
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .ignoreTransientFields()
   *                     .isEqualTo(cherlock);
   *
   * // now this assertion fails as expected because the height field differs:
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(cherlock);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> ignoringTransientFields() {
    try {
      recursiveComparisonAssert.ignoringTransientFields();
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
  public SoftRecursiveComparisonAssert<SELF> is(Condition<? super Object> condition) {
    try {
      recursiveComparisonAssert.is(condition);
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
  public SoftRecursiveComparisonAssert<SELF> isEqualTo(Object expected) {
    try {
      recursiveComparisonAssert.isEqualTo(expected);
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
  public SoftRecursiveComparisonAssert<SELF> isExactlyInstanceOf(Class<?> type) {
    try {
      recursiveComparisonAssert.isExactlyInstanceOf(type);
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
  public SoftRecursiveComparisonAssert<SELF> isIn(Iterable<?> values) {
    try {
      recursiveComparisonAssert.isIn(values);
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
  public SoftRecursiveComparisonAssert<SELF> isIn(Object... values) {
    try {
      recursiveComparisonAssert.isIn(values);
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
  public SoftRecursiveComparisonAssert<SELF> isInstanceOf(Class<?> type) {
    try {
      recursiveComparisonAssert.isInstanceOf(type);
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
  public SoftRecursiveComparisonAssert<SELF> isInstanceOfAny(Class<?>... types) {
    try {
      recursiveComparisonAssert.isInstanceOfAny(types);
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
  public <T> SoftRecursiveComparisonAssert<SELF> isInstanceOfSatisfying(Class<T> type,
      Consumer<T> requirements) {
    try {
      recursiveComparisonAssert.isInstanceOfSatisfying(type,requirements);
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
  public SoftRecursiveComparisonAssert<SELF> isNot(Condition<? super Object> condition) {
    try {
      recursiveComparisonAssert.isNot(condition);
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
  public SoftRecursiveComparisonAssert<SELF> isNotEqualTo(Object other) {
    try {
      recursiveComparisonAssert.isNotEqualTo(other);
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
  public SoftRecursiveComparisonAssert<SELF> isNotExactlyInstanceOf(Class<?> type) {
    try {
      recursiveComparisonAssert.isNotExactlyInstanceOf(type);
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
  public SoftRecursiveComparisonAssert<SELF> isNotIn(Iterable<?> values) {
    try {
      recursiveComparisonAssert.isNotIn(values);
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
  public SoftRecursiveComparisonAssert<SELF> isNotIn(Object... values) {
    try {
      recursiveComparisonAssert.isNotIn(values);
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
  public SoftRecursiveComparisonAssert<SELF> isNotInstanceOf(Class<?> type) {
    try {
      recursiveComparisonAssert.isNotInstanceOf(type);
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
  public SoftRecursiveComparisonAssert<SELF> isNotInstanceOfAny(Class<?>... types) {
    try {
      recursiveComparisonAssert.isNotInstanceOfAny(types);
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
  public SoftRecursiveComparisonAssert<SELF> isNotNull() {
    try {
      recursiveComparisonAssert.isNotNull();
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
  public SoftRecursiveComparisonAssert<SELF> isNotOfAnyClassIn(Class<?>... types) {
    try {
      recursiveComparisonAssert.isNotOfAnyClassIn(types);
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
  public SoftRecursiveComparisonAssert<SELF> isNotSameAs(Object other) {
    try {
      recursiveComparisonAssert.isNotSameAs(other);
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
  public SoftRecursiveComparisonAssert<SELF> isNull() {
    try {
      recursiveComparisonAssert.isNull();
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
  public SoftRecursiveComparisonAssert<SELF> isOfAnyClassIn(Class<?>... types) {
    try {
      recursiveComparisonAssert.isOfAnyClassIn(types);
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
  public SoftRecursiveComparisonAssert<SELF> isSameAs(Object expected) {
    try {
      recursiveComparisonAssert.isSameAs(expected);
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
  public SoftRecursiveComparisonAssert<SELF> matches(Predicate<? super Object> predicate) {
    try {
      recursiveComparisonAssert.matches(predicate);
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
  public SoftRecursiveComparisonAssert<SELF> matches(Predicate<? super Object> predicate,
      String predicateDescription) {
    try {
      recursiveComparisonAssert.matches(predicate,predicateDescription);
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
  public SoftRecursiveComparisonAssert<SELF> overridingErrorMessage(String newErrorMessage,
      Object... args) {
    recursiveComparisonAssert.overridingErrorMessage(newErrorMessage,args);
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
  public SoftRecursiveComparisonAssert<SELF> overridingErrorMessage(Supplier<String> supplier) {
    recursiveComparisonAssert.overridingErrorMessage(supplier);
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
  public final SoftRecursiveComparisonAssert<SELF> satisfies(
      Consumer<? super Object>... requirements) {
    try {
      recursiveComparisonAssert.satisfies(requirements);
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
  public SoftRecursiveComparisonAssert<SELF> satisfies(Condition<? super Object> condition) {
    try {
      recursiveComparisonAssert.satisfies(condition);
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
  public final SoftRecursiveComparisonAssert<SELF> satisfies(
      ThrowingConsumer<? super Object>... assertions) {
    try {
      recursiveComparisonAssert.satisfies(assertions);
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
  public final SoftRecursiveComparisonAssert<SELF> satisfiesAnyOf(
      Consumer<? super Object>... assertions) {
    try {
      recursiveComparisonAssert.satisfiesAnyOf(assertions);
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
  public final SoftRecursiveComparisonAssert<SELF> satisfiesAnyOf(
      ThrowingConsumer<? super Object>... assertions) {
    try {
      recursiveComparisonAssert.satisfiesAnyOf(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Makes the recursive comparison to consider empty and null iterables as equal.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   List&lt;Person&gt; friends;
   * }
   *
   * Person person1 = new Person("John Doe");
   * person1.friends = null;
   * Person person2 = new Person("John Doe");
   * person2.friends = new ArrayList&lt;&gt;();
   *
   * // assertion succeeds as the friend field values (null vs empty list) are considered equal
   * assertThat(person1).usingRecursiveComparison()
   *                    .treatingNullAndEmptyIterablesAsEqual()
   *                    .isEqualTo(person2);
   *
   * // assertion fails as the friend field values are different (null vs empty list)
   * assertThat(person1).usingRecursiveComparison()
   *                    .isEqualTo(person2);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> treatingNullAndEmptyIterablesAsEqual() {
    try {
      recursiveComparisonAssert.treatingNullAndEmptyIterablesAsEqual();
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
  public SoftRecursiveComparisonAssert<SELF> usingComparator(
      Comparator<? super Object> customComparator) {
    recursiveComparisonAssert.usingComparator(customComparator);
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
  public SoftRecursiveComparisonAssert<SELF> usingComparator(
      Comparator<? super Object> customComparator, String customComparatorDescription) {
    recursiveComparisonAssert.usingComparator(customComparator,customComparatorDescription);
    return this;
  }

  /**
   * Revert to standard comparison for the incoming assertion checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling {@link #usingComparator(Comparator) usingComparator}.
   *
   * @return {@code this} assertion object.
   */
  public SoftRecursiveComparisonAssert<SELF> usingDefaultComparator() {
    recursiveComparisonAssert.usingDefaultComparator();
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
  public SoftRecursiveComparisonAssert<SELF> usingEquals(
      BiPredicate<? super Object, ? super Object> predicate) {
    recursiveComparisonAssert.usingEquals(predicate);
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
  public SoftRecursiveComparisonAssert<SELF> usingEquals(
      BiPredicate<? super Object, ? super Object> predicate, String customEqualsDescription) {
    recursiveComparisonAssert.usingEquals(predicate,customEqualsDescription);
    return this;
  }

  /**
   * By default, the recursive comparison compare recursively all fields including the ones whose type have overridden equals
   * <b>except fields with java types</b> (at some point we need to compare something!).
   * <p>
   * This method instructs the recursive comparison to use overridden equals.
   * <p>
   * Example:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * class Home {
   *   Address address = new Address();
   * }
   *
   * class Address {
   *   int number;
   *   String street;
   *
   *   // only compares number!
   *   {@literal @}Override
   *   public boolean equals(final Object other) {
   *     if (!(other instanceof Address)) return false;
   *     Address castOther = (Address) other;
   *     return Objects.equals(number, castOther.number);
   *   }
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.address.street = "Butcher Street";
   * sherlock2.home.address.number = 221;
   *
   * // assertion succeeds because Address equals does not compare street fields.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .usingOverriddenEquals()
   *                     .isEqualTo(sherlock2);
   *
   * // Assertion fails because:
   * // - Address equals is not used.
   * // - street fields are compared and differ.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   * @since 3.17.0
   */
  public SoftRecursiveComparisonAssert<SELF> usingOverriddenEquals() {
    try {
      recursiveComparisonAssert.usingOverriddenEquals();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Allows registering a comparator to compare fields with the given locations.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * Comparators registered with this method have precedence over comparators registered with {@link #withComparatorForType(Comparator, Class)}
   * or {@link BiPredicate} registered with {@link #withEqualsForType(BiPredicate, Class)}.
   * <p>
   * The field locations must be specified from the root object,
   * for example if {@code Foo} has a {@code Bar} field which has an {@code id}, one can register to a comparator for Bar's {@code id} by calling:
   * <pre><code class='java'> withComparatorForFields(idComparator, "foo.id", "foo.bar.id")</code></pre>
   * <p>
   * Note that registering a {@link Comparator} for a given field will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * Complete example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * Comparator&lt;Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5 ? 0 : 1;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withComparatorForFields(closeEnough, &quot;height&quot;)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withComparatorForFields(closeEnough, &quot;height&quot;)
   *                  .isEqualTo(reallyTallFrodo);</code></pre>
   *
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given fields
   * @param fieldLocations the location from the root object of the fields the comparator should be used for
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given comparator is null.
   */
  public SoftRecursiveComparisonAssert<SELF> withComparatorForFields(Comparator<?> comparator,
      String... fieldLocations) {
    try {
      recursiveComparisonAssert.withComparatorForFields(comparator,fieldLocations);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Allows registering a comparator to compare the fields with the given type.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * Comparators registered with this method have less precedence than comparators registered with {@link #withComparatorForFields(Comparator, String...) withComparatorForFields(Comparator, String...)}
   * or BiPredicate registered with {@link #withEqualsForFields(BiPredicate, String...) withEqualsForFields(BiPredicate, String...)}.
   * <p>
   * Note that registering a {@link Comparator} for a given type will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * Comparator&lt;Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5 ? 0 : 1;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withComparatorForType(closeEnough, Double.class)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withComparatorForType(closeEnough, Double.class)
   *                  .isEqualTo(reallyTallFrodo);</code></pre>
   *
   * @param <T> the class type to register a comparator for
   * @param comparator the {@link java.util.Comparator Comparator} to use to compare the given fields
   * @param type the type to be compared with the given comparator.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given comparator is null.
   */
  public <T> SoftRecursiveComparisonAssert<SELF> withComparatorForType(
      Comparator<? super T> comparator, Class<T> type) {
    try {
      recursiveComparisonAssert.withComparatorForType(comparator,type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Allows the recursive comparison to compare an enum field against a String field and vice versa.
   * <p>
   * Example:
   * <pre><code class='java'> LightString actual = new LightString("GREEN");
   * Light expected = new Light(GREEN);
   *
   * // compares "GREEN" to GREEN
   * assertThat(actual).usingRecursiveComparison()
   *                   .withEnumStringComparison()
   *                   .isEqualTo(expected);
   *
   * // compares GREEN to "GREEN"
   * assertThat(expected).usingRecursiveComparison()
   *                     .withEnumStringComparison()
   *                     .isEqualTo(actual);</code></pre>
   * where {@code Light} and {@code LightString} are defined as:
   * <pre><code class='java'> class Light {
   *   Color color;
   *   Light(Color value) {
   *     this.color = value;
   *   }
   * }
   *
   * class LightString {
   *   String color;
   *   LightString(String value) {
   *     this.color = value;
   *   }
   * }</code></pre>
   */
  public SoftRecursiveComparisonAssert<SELF> withEnumStringComparison() {
    try {
      recursiveComparisonAssert.withEnumStringComparison();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Allows registering a {@link BiPredicate} to compare fields with the given locations.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * BiPredicates specified with this method have precedence over the ones registered with {@link #withEqualsForType(BiPredicate, Class)}
   * or the comparators registered with {@link #withComparatorForType(Comparator, Class)}.
   * <p>
   * Note that registering a {@link BiPredicate} for a given field will override the previously registered one (if any).
   * <p>
   * The field locations must be specified from the root object,
   * for example if {@code Foo} has a {@code Bar} field which has an {@code id}, one can register to a comparator for Bar's {@code id} by calling:
   * <pre><code class='java'> withEqualsForFields(idBiPredicate, "foo.id", "foo.bar.id")</code></pre>
   * <p>
   * Complete example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * BiPredicate&lt;Double, Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForFields(closeEnough, &quot;height&quot;)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForFields(closeEnough, &quot;height&quot;)
   *                  .isEqualTo(reallyTallFrodo);</code></pre>
   *
   * @param equals the {@link BiPredicate} to use to compare the given fields
   * @param fieldLocations the location from the root object of the fields the BiPredicate should be used for
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given BiPredicate is null.
   */
  public SoftRecursiveComparisonAssert<SELF> withEqualsForFields(BiPredicate<?, ?> equals,
      String... fieldLocations) {
    try {
      recursiveComparisonAssert.withEqualsForFields(equals,fieldLocations);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Allows registering a {@link BiPredicate} to compare fields whose location matches the given regexes.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * The fields are evaluated from the root object, for example if {@code Foo} has a {@code Bar} field and both have an {@code id} field,
   * one can register a BiPredicate for Foo and Bar's {@code id} by calling:
   * <pre><code class='java'> withEqualsForFieldsMatchingRegexes(idBiPredicate, ".*id")</code></pre>
   * or
   * <pre><code class='java'> withEqualsForFieldsMatchingRegexes(idBiPredicate, "foo.*id")</code></pre>
   * <p>
   * BiPredicates registered with this method have precedence over the ones registered with {@link #withEqualsForType(BiPredicate, Class)}
   * or the comparators registered with {@link #withComparatorForType(Comparator, Class)} but don't have precedence over the
   * ones registered with exact location match: {@link #withEqualsForFields(BiPredicate, String...)} or {@link #withComparatorForFields(Comparator, String...)}
   * <p>
   * If registered regexes for different {@link BiPredicate} match a given field, the latest registered regexes {@link BiPredicate} wins.
   * <p>
   * Example:
   * <pre><code class='java'> class TolkienCharacter {
   *   String name;
   *   double height;
   *   double weight;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2, 40);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3, 40.5);
   * TolkienCharacter hugeFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9, 45);
   *
   * BiPredicate&lt;Double, Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForFieldsMatchingRegexes(closeEnough, &quot;.eight&quot;)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForFieldsMatchingRegexes(closeEnough, &quot;.eight&quot;)
   *                  .isEqualTo(hugeFrodo);</code></pre>
   *
   * @param equals the {@link BiPredicate} to use to compare the fields matching the given regexes
   * @param regexes the regexes from the root object of the field locations the BiPredicate should be used for
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given BiPredicate is null.
   * @since 3.24.0
   */
  public SoftRecursiveComparisonAssert<SELF> withEqualsForFieldsMatchingRegexes(
      BiPredicate<?, ?> equals, String... regexes) {
    try {
      recursiveComparisonAssert.withEqualsForFieldsMatchingRegexes(equals,regexes);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Allows registering a {@link BiPredicate} to compare the fields with the given type.
   * A typical usage is to compare double/float fields with a given precision.
   * <p>
   * BiPredicates registered with this method have less precedence than the one registered  with {@link #withEqualsForTypes(BiPredicate, Class, Class) withEqualsForTypes(BiPredicate, Class, Class)},
   * {@link #withEqualsForFields(BiPredicate, String...) withEqualsForFields(BiPredicate, String...)}
   * or comparators registered with {@link #withComparatorForFields(Comparator, String...) withComparatorForFields(Comparator, String...)}.
   * <p>
   * Note that registering a {@link BiPredicate} for a given type will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.3);
   * TolkienCharacter reallyTallFrodo = new TolkienCharacter(&quot;Frodo&quot;, 1.9);
   *
   * BiPredicate&lt;Double, Double&gt; closeEnough = (d1, d2) -&gt; Math.abs(d1 - d2) &lt;= 0.5;
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForType(closeEnough, Double.class)
   *                  .isEqualTo(tallerFrodo);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForType(closeEnough, Double.class)
   *                  .isEqualTo(reallyTallFrodo);</code></pre>
   *
   * @param <T> the class type to register a BiPredicate for
   * @param equals the {@link BiPredicate} to use to compare the given fields
   * @param type the type to be compared with the given comparator.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given BiPredicate is null.
   */
  public <T> SoftRecursiveComparisonAssert<SELF> withEqualsForType(
      BiPredicate<? super T, ? super T> equals, Class<T> type) {
    try {
      recursiveComparisonAssert.withEqualsForType(equals,type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Allows to register a {@link BiPredicate} to compare the fields with the given types.
   * A typical usage is to compare fields belonging to different types.
   * <p>
   * BiPredicates registered with this method have less precedence than the one registered  with {@link #withEqualsForFields(BiPredicate, String...) withEqualsForFields(BiPredicate, String...)}
   * or comparators registered with {@link #withComparatorForFields(Comparator, String...) withComparatorForFields(Comparator, String...)}.
   * <p>
   * Note that registering a {@link BiPredicate} for a given type will override the previously registered BiPredicate/Comparator (if any).
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   LocalDate birthday;
   * }
   * public class TolkienCharacterDto {
   *    String birthday;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(LocalDate.of(2968, Month.SEPTEMBER, 22));
   * TolkienCharacterDto frodoDto = new TolkienCharacterDto("2968-09-22");
   * TolkienCharacterDto bilboDto = new TolkienCharacterDto("2890-09-22");
   *
   * BiPredicate&lt;LocalDate, String&gt; sameDate = (d, s) -&gt; LocalDate.parse(s).equals(d);
   *
   * // assertion succeeds
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForTypes(sameDate, LocalDate.class, String.class)
   *                  .isEqualTo(frodoDto);
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withEqualsForTypes(sameDate, LocalDate.class, String.class)
   *                  .isEqualTo(bilboDto);</code></pre>
   *
   * @param <T> the left element's class type to register a BiPredicate for
   * @param <U> the right element's class type to register a BiPredicate for
   * @param equals the {@link BiPredicate} to use to compare the given fields
   * @param type the type of the left element to be compared with the given comparator.
   * @param otherType the type of the right element to be compared with the given comparator.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   * @throws NullPointerException if the given BiPredicate is null.
   */
  public <T, U> SoftRecursiveComparisonAssert<SELF> withEqualsForTypes(
      BiPredicate<? super T, ? super U> equals, Class<T> type, Class<U> otherType) {
    try {
      recursiveComparisonAssert.withEqualsForTypes(equals,type,otherType);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Overrides an error message which would be shown when differences in the given fields while comparison occurred
   * with the giving error message.
   * <p>
   * The fields must be specified from the root object, for example if {@code Foo} has a {@code Bar} field and both
   * have an {@code id} field, one can register a message for Foo and Bar's {@code id} by calling:
   * <pre><code class='java'> withErrorMessageForFields("some message", "foo.id", "foo.bar.id")</code></pre>
   * <p>
   * Messages registered with this method have precedence over the ones registered with {@link #withErrorMessageForType(String, Class)}.
   * <p>
   * In case of {@code null} as message the default error message will be used (See
   * {@link org.assertj.core.api.recursive.comparison.ComparisonDifference#DEFAULT_TEMPLATE}).
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodon&quot;, 1.4);
   *
   * String message = &quot;The field 'height' differ.&quot;;
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withErrorMessageForFields(message, "height")
   *                  .isEqualTo(tallerFrodo);</code></pre>
   * and the error will report the height field with the given overridden message instead of the one computed by AssertJ as with the name error:
   * <pre><code>
   * Expecting actual:
   *   TolkienCharacter [name=Frodo, height=1.2]
   * to be equal to:
   *   TolkienCharacter [name=Frodon, height=1.4]
   * when recursively comparing field by field, but found the following 2 differences:
   *
   * The field 'height' differ.
   *
   * field/property 'name' differ:
   * - actual value  : "Frodo"
   * - expected value: "Frodon"
   *
   * The recursive comparison was performed with this configuration:
   * - no overridden equals methods were used in the comparison (except for java types)
   * - these types were compared with the following comparators:
   *   - java.lang.Double -&gt; DoubleComparator[precision=1.0E-15]
   *   - java.lang.Float -&gt; FloatComparator[precision=1.0E-6]
   *   - java.nio.file.Path -&gt; lexicographic comparator (Path natural order)
   * - actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call strictTypeChecking(true) to change that behavior).
   * - these fields had overridden error messages:
   *   - height</code></pre>
   *
   * @param message the error message that will be thrown when comparison error occurred.
   * @param fieldLocations the fields the error message should be used for.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> withErrorMessageForFields(String message,
      String... fieldLocations) {
    try {
      recursiveComparisonAssert.withErrorMessageForFields(message,fieldLocations);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Overrides an error message which would be shown when differences for the giving type while comparison occurred with
   * the giving error message.
   * <p>
   * Message registered with this method have less precedence than the ones registered with {@link #withErrorMessageForFields(String, String...)}.
   * <p>
   * In case of {@code null} as message the default error message will be used (See
   * {@link org.assertj.core.api.recursive.comparison.ComparisonDifference#DEFAULT_TEMPLATE}).
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   String name;
   *   double height;
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 1.2);
   * TolkienCharacter tallerFrodo = new TolkienCharacter(&quot;Frodon&quot;, 1.4);
   *
   * String message = &quot;Double field differ.&quot;;
   *
   * // assertion fails
   * assertThat(frodo).usingRecursiveComparison()
   *                  .withErrorMessageForType(message, Double.class)
   *                  .isEqualTo(tallerFrodo);</code></pre>
   * and the error will report the height field with the given overridden message instead of the one computed by AssertJ as with the name error:
   * <pre><code>
   * Expecting actual:
   *   TolkienCharacter [name=Frodo, height=1.2]
   * to be equal to:
   *   TolkienCharacter [name=Frodon, height=1.4]
   * when recursively comparing field by field, but found the following 2 differences:
   *
   * Double field differ.
   *
   * field/property 'name' differ:
   * - actual value  : "Frodo"
   * - expected value: "Frodon"
   *
   * The recursive comparison was performed with this configuration:
   * - no overridden equals methods were used in the comparison (except for java types)
   * - these types were compared with the following comparators:
   *   - java.lang.Double -&gt; DoubleComparator[precision=1.0E-15]
   *   - java.lang.Float -&gt; FloatComparator[precision=1.0E-6]
   *   - java.nio.file.Path -&gt; lexicographic comparator (Path natural order)
   * - actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call strictTypeChecking(true) to change that behavior).
   * - these types had overridden error messages:
   *   - height</code></pre>
   *
   * @param message the error message that will be thrown when comparison error occurred.
   * @param type the type the error message should be used for.
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> withErrorMessageForType(String message,
      Class<?> type) {
    try {
      recursiveComparisonAssert.withErrorMessageForType(message,type);
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
  public SoftRecursiveComparisonAssert<SELF> withFailMessage(String newErrorMessage,
      Object... args) {
    recursiveComparisonAssert.withFailMessage(newErrorMessage,args);
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
  public SoftRecursiveComparisonAssert<SELF> withFailMessage(Supplier<String> supplier) {
    recursiveComparisonAssert.withFailMessage(supplier);
    return this;
  }

  /**
   * Defines how objects are introspected in the recursive comparison, that is:
   * <ul>
   *  <li>how to traverse the graph of nodes to compare</li>
   *  <li>how to get a node value</li>
   *  </ul>
   * <p>
   * Default to {@link RecursiveComparisonConfiguration#DEFAULT_RECURSIVE_COMPARISON_INTROSPECTION_STRATEGY} that introspects all fields (including inherited ones).
   *
   * @param introspectionStrategy the {@link RecursiveComparisonIntrospectionStrategy} to use
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> withIntrospectionStrategy(
      RecursiveComparisonIntrospectionStrategy introspectionStrategy) {
    try {
      recursiveComparisonAssert.withIntrospectionStrategy(introspectionStrategy);
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
  public SoftRecursiveComparisonAssert<SELF> withRepresentation(Representation representation) {
    recursiveComparisonAssert.withRepresentation(representation);
    return this;
  }

  /**
   * Makes the recursive comparison to check that actual's type is compatible with expected's type (and do the same for each field). <br>
   * Compatible means that the expected's type is the same or a subclass of actual's type.
   * <p>
   * Examples:
   * <pre><code class='java'> class Person {
   *   String name;
   *   double height;
   *   Person bestFriend;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.bestFriend = new Person("Watson", 1.70);
   *
   * Person sherlockClone = new Person("Sherlock", 1.80);
   * sherlockClone.bestFriend = new Person("Watson", 1.70);
   *
   * // assertion succeeds as sherlock and sherlockClone have the same data and types
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .withStrictTypeChecking()
   *                     .isEqualTo(sherlockClone);
   *
   * // Let's now define a data structure similar to Person
   *
   * public class PersonDTO {
   *   String name;
   *   double height;
   *   PersonDTO bestFriend;
   * }
   *
   * PersonDTO sherlockDto = new PersonDTO("Sherlock", 1.80);
   * sherlockDto.bestFriend = new PersonDTO("Watson", 1.70);
   *
   * // assertion fails as Person and PersonDTO are not compatible even though they have the same data
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .withStrictTypeChecking()
   *                     .isEqualTo(noName);
   *
   * // Let's define a subclass of Person
   *
   * public class Detective extends Person {
   *   boolean busy;
   * }
   *
   * Detective detectiveSherlock = new Detective("Sherlock", 1.80);
   * detectiveSherlock.bestFriend = new Person("Watson", 1.70);
   * detectiveSherlock.busy = true;
   *
   * // assertion succeeds as Detective inherits from Person and
   * // only Person's fields are included into the comparison.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .withStrictTypeChecking()
   *                     .isEqualTo(detectiveSherlock);</code></pre>
   *
   * @return this {@link SoftRecursiveComparisonAssert} to chain other methods.
   */
  public SoftRecursiveComparisonAssert<SELF> withStrictTypeChecking() {
    try {
      recursiveComparisonAssert.withStrictTypeChecking();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
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
  public SoftRecursiveComparisonAssert<SELF> withThreadDumpOnError() {
    recursiveComparisonAssert.withThreadDumpOnError();
    return this;
  }
}
