package org.assertj.core.api.soft;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.assertj.core.annotation.Beta;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AbstractOptionalAssert;
import org.assertj.core.api.Assert;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.Condition;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.OptionalAssert;
import org.assertj.core.api.RecursiveAssertionAssert;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;

@Beta
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class SoftOptionalAssert<VALUE> implements SoftAssert {
  private final AssertionErrorCollector errorCollector;

  private final OptionalAssert<VALUE> optionalAssert;

  public SoftOptionalAssert(Optional<VALUE> actual, AssertionErrorCollector errorCollector) {
    this.optionalAssert = assertThat(actual);
    this.errorCollector = errorCollector;
  }

  /**
   * {@inheritDoc}
   */
  public Optional<VALUE> actual() {
    return optionalAssert.actual();
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
  public SoftOptionalAssert<VALUE> as(String description, Object... args) {
    optionalAssert.as(description,args);
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
  public SoftOptionalAssert<VALUE> as(Supplier<String> descriptionSupplier) {
    optionalAssert.as(descriptionSupplier);
    return this;
  }

  /**
   * {@inheritDoc}
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
  public SoftOptionalAssert<VALUE> as(Description description) {
    optionalAssert.as(description);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftStringAssert asString() {
    return new SoftStringAssert(actual().toString(), errorCollector);
  }

  public SoftOptionalAssert<VALUE> contains(VALUE expectedValue) {
    try {
      optionalAssert.contains(expectedValue);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains a value that is an instance of the argument.
   * <p>
   * Assertions succeeds:
   *
   * <pre><code class='java'> assertThat(Optional.of("something")).containsInstanceOf(String.class)
   *                                     .containsInstanceOf(Object.class);
   *
   * assertThat(Optional.of(10)).containsInstanceOf(Integer.class);</code></pre>
   *
   * Assertion fails:
   *
   * <pre><code class='java'> assertThat(Optional.of("something")).containsInstanceOf(Integer.class);</code></pre>
   *
   * @param clazz the expected class of the value inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public SoftOptionalAssert<VALUE> containsInstanceOf(Class<?> clazz) {
    try {
      optionalAssert.containsInstanceOf(clazz);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> containsSame(VALUE expectedValue) {
    try {
      optionalAssert.containsSame(expectedValue);
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
  public SoftOptionalAssert<VALUE> describedAs(String description, Object... args) {
    optionalAssert.describedAs(description,args);
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
  public SoftOptionalAssert<VALUE> describedAs(Supplier<String> descriptionSupplier) {
    optionalAssert.describedAs(descriptionSupplier);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> describedAs(Description description) {
    optionalAssert.describedAs(description);
    return this;
  }

  /**
   * The description of this assertion set with {@link #describedAs(String, Object...)} or
   * {@link #describedAs(Description)}.
   *
   * @return the description String representation of this assertion.
   */
  public SoftOptionalAssert<VALUE> descriptionText() {
    optionalAssert.descriptionText();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> doesNotHave(Condition<? super Optional<VALUE>> condition) {
    try {
      optionalAssert.doesNotHave(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> doesNotHaveSameClassAs(Object other) {
    try {
      optionalAssert.doesNotHaveSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> doesNotHaveSameHashCodeAs(Object other) {
    try {
      optionalAssert.doesNotHaveSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> doesNotHaveToString(String otherToString) {
    try {
      optionalAssert.doesNotHaveToString(otherToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> doesNotHaveToString(String expectedStringTemplate,
      Object... args) {
    try {
      optionalAssert.doesNotHaveToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> doesNotHaveValue(VALUE expectedValue) {
    try {
      optionalAssert.doesNotHaveValue(expectedValue);
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
  public SoftOptionalAssert<VALUE> doesNotMatch(Predicate<? super Optional<VALUE>> predicate) {
    try {
      optionalAssert.doesNotMatch(predicate);
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
  public SoftOptionalAssert<VALUE> doesNotMatch(Predicate<? super Optional<VALUE>> predicate,
      String predicateDescription) {
    try {
      optionalAssert.doesNotMatch(predicate,predicateDescription);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   *
   * @throws UnsupportedOperationException if this method is called.
   * @deprecated use {@link #isEqualTo} instead
   */
  @Deprecated(
      since = "3"
  )
  public boolean equals(Object obj) {
    return optionalAssert.equals(obj);
  }

  /**
   * Call {@link Optional#flatMap(Function) flatMap} on the {@code Optional} under test, assertions chained afterward are performed on the {@code Optional} resulting from the flatMap call.
   * <p>
   * Examples:
   * <pre><code class='java'> Function&lt;String, Optional&lt;String&gt;&gt; UPPER_CASE_OPTIONAL_STRING =
   *       s -&gt; s == null ? Optional.empty() : Optional.of(s.toUpperCase());
   *
   * // assertions succeed
   * assertThat(Optional.of("something")).contains("something")
   *                                     .flatMap(UPPER_CASE_OPTIONAL_STRING)
   *                                     .contains("SOMETHING");
   *
   * assertThat(Optional.&lt;String&gt;empty()).flatMap(UPPER_CASE_OPTIONAL_STRING)
   *                                     .isEmpty();
   *
   * assertThat(Optional.&lt;String&gt;ofNullable(null)).flatMap(UPPER_CASE_OPTIONAL_STRING)
   *                                              .isEmpty();
   *
   * // assertion fails
   * assertThat(Optional.of("something")).flatMap(UPPER_CASE_OPTIONAL_STRING)
   *                                     .contains("something");</code></pre>
   *
   * @param <U> the type wrapped in the {@link java.util.Optional} after the {@link Optional#flatMap(Function) flatMap} operation.
   * @param mapper the {@link Function} to use in the {@link Optional#flatMap(Function) flatMap} operation.
   * @return a new {@link AbstractOptionalAssert} for assertions chaining on the flatMap of the Optional.
   * @throws AssertionError if the actual {@link java.util.Optional} is null.
   * @since 3.6.0
   */
  public <U> SoftOptionalAssert<U> flatMap(Function<? super VALUE, Optional<U>> mapper) {
    return new SoftOptionalAssert<>(optionalAssert.flatMap(mapper).actual(), errorCollector);
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is not {@code null} and not empty and returns an Object assertion
   * that allows chaining (object) assertions on the optional value.
   * <p>
   * Note that it is only possible to return Object assertions after calling this method due to java generics limitations.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, null);
   *
   * // assertion succeeds since all frodo's fields are set
   * assertThat(Optional.of(frodo)).get().hasNoNullFieldsOrProperties();
   *
   * // assertion does not succeed because sam does not have its race set
   * assertThat(Optional.of(sam)).get().hasNoNullFieldsOrProperties();</code></pre>
   *
   * @return a new {@link AbstractObjectAssert} for assertions chaining on the value of the Optional.
   * @throws AssertionError if the actual {@link java.util.Optional} is null.
   * @throws AssertionError if the actual {@link java.util.Optional} is empty.
   * @since 3.9.0
   * @see #get(DefaultSoftAssertFactory)
   */
  public SoftObjectAssert<VALUE> get() {
    return new SoftObjectAssert<>(optionalAssert.get().actual(), errorCollector);
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is not {@code null} and not empty and returns a new assertion instance
   * to chain assertions on the optional value.
   * <p>
   * The {@code softAssertFactory} parameter allows to specify an {@link DefaultSoftAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Wrapping the given {@link DefaultSoftAssertFactory} with {@link org.assertj.core.api.Assertions#as(InstanceOfAssertFactory)} makes the
   * assertion more readable.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(Optional.of("frodo")).get(as(InstanceOfAssertFactories.STRING)).startsWith("fro");
   *
   * // assertion does not succeed because frodo is not an Integer
   * assertThat(Optional.of("frodo")).get(as(InstanceOfAssertFactories.INTEGER)).isZero();</code></pre>
   *
   * @param <SOFT_ASSERT> the type of the resulting {@code SoftAssert}
   * @param softAssertFactory the factory which verifies the type and creates the new {@code SoftAssert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the value of the Optional
   * @throws NullPointerException if the given factory is {@code null}
   * @throws AssertionError if the actual {@link java.util.Optional} is null
   * @throws AssertionError if the actual {@link java.util.Optional} is empty
   * @since 3.14.0
   */
  public <SOFT_ASSERT extends SoftAssert> SOFT_ASSERT get(
      DefaultSoftAssertFactory<?, SOFT_ASSERT> softAssertFactory) {
    return softAssertFactory.createSoftAssert(actual().get(), errorCollector);
  }

  /**
   * Exposes the {@link WritableAssertionInfo} used in the current assertion for better extensibility.<br> When writing
   * your own assertion class, you can use the returned {@link WritableAssertionInfo} to change the error message and
   * still keep the description set by the assertion user.
   *
   * @return the {@link WritableAssertionInfo} used in the current assertion
   */
  public WritableAssertionInfo getWritableAssertionInfo() {
    return optionalAssert.getWritableAssertionInfo();
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> has(Condition<? super Optional<VALUE>> condition) {
    try {
      optionalAssert.has(condition);
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
    return optionalAssert.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> hasSameClassAs(Object other) {
    try {
      optionalAssert.hasSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> hasSameHashCodeAs(Object other) {
    try {
      optionalAssert.hasSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> hasToString(String expectedToString) {
    try {
      optionalAssert.hasToString(expectedToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> hasToString(String expectedStringTemplate, Object... args) {
    try {
      optionalAssert.hasToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasValue(VALUE expectedValue) {
    try {
      optionalAssert.hasValue(expectedValue);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the value in the actual {@link java.util.Optional} matches the given predicate.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(Optional.of("something")).hasValueMatching(s -&gt; s.startsWith("some"));
   *
   * // assertions fail:
   * assertThat(Optional.of("something")).hasValueMatching(s -&gt; s.startsWith("else"));
   * assertThat(Optional.empty()).hasValueMatching(s -&gt; true);</code></pre>
   *
   * @param predicate the predicate to match.
   * @return this assertion object.
   */
  public SoftOptionalAssert<VALUE> hasValueMatching(Predicate<? super VALUE> predicate) {
    try {
      optionalAssert.hasValueMatching(predicate);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the value in the actual {@link java.util.Optional} matches the given predicate.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(Optional.of("something")).hasValueMatching(s -&gt; s.startsWith("some"), "starts with 'some'");
   *
   * // assertions fail:
   * assertThat(Optional.of("something")).hasValueMatching(s -&gt; s.startsWith("else"), "starts with 'else'");
   * assertThat(Optional.empty()).hasValueMatching(s -&gt; true, "any");</code></pre>
   *
   * @param predicate the predicate to match.
   * @param description the description of the predicate.
   * @return this assertion object.
   */
  public SoftOptionalAssert<VALUE> hasValueMatching(Predicate<? super VALUE> predicate,
      String description) {
    try {
      optionalAssert.hasValueMatching(predicate,description);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains a value and gives this value to the given
   * {@link java.util.function.Consumer} for further assertions. Should be used as a way of deeper asserting on the
   * containing object, as further requirement(s) for the value.
   * <p>
   * Assertions succeeds:
   * <pre><code class='java'> // one requirement
   * assertThat(Optional.of(10)).hasValueSatisfying(i -&gt; { assertThat(i).isGreaterThan(9); });
   *
   * // multiple requirements
   * assertThat(Optional.of(someString)).hasValueSatisfying(s -&gt; {
   *   assertThat(s).isEqualTo("something");
   *   assertThat(s).startsWith("some");
   *   assertThat(s).endsWith("thing");
   * }); </code></pre>
   *
   * Assertions will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).hasValueSatisfying(s -&gt; {
   *     assertThat(s).isEqualTo("something else");
   *   });
   *
   * // fail because optional is empty, there is no value to perform assertion on
   * assertThat(Optional.empty()).hasValueSatisfying(o -&gt; {});</code></pre>
   *
   * @param requirement to further assert on the object contained inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public SoftOptionalAssert<VALUE> hasValueSatisfying(Consumer<VALUE> requirement) {
    try {
      optionalAssert.hasValueSatisfying(requirement);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains a value which satisfies the given {@link Condition}.
   * <p>
   * Examples:
   * <pre><code class='java'> Condition&lt;TolkienCharacter&gt; isAnElf = new Condition&lt;&gt;(character -&gt; character.getRace() == ELF, "an elf");
   *
   * TolkienCharacter legolas = new TolkienCharacter("Legolas", 1000, ELF);
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   *
   * // assertion succeeds
   * assertThat(Optional.of(legolas)).hasValueSatisfying(isAnElf);
   *
   * // assertion fails
   * assertThat(Optional.of(frodo)).hasValueSatisfying(isAnElf);</code></pre>
   *
   * @param condition the given condition.
   * @return this assertion object.
   * @throws AssertionError if the actual {@link java.util.Optional} is null or empty.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value does not satisfy the given condition.
   * @since 3.6.0
   */
  public SoftOptionalAssert<VALUE> hasValueSatisfying(Condition<? super VALUE> condition) {
    try {
      optionalAssert.hasValueSatisfying(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains a value and gives this value to the given
   * {@link ThrowingConsumer} for further assertions.
   * <p>
   * This is a same assertion as {@link #hasValueSatisfying(Condition)}
   * except that a {@link ThrowingConsumer} rethrows checked exceptions as {@link RuntimeException}.
   *
   * @param requirement to further assert on the object contained inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public SoftOptionalAssert<VALUE> hasValueSatisfying(ThrowingConsumer<VALUE> requirement) {
    try {
      optionalAssert.hasValueSatisfying(requirement);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> is(Condition<? super Optional<VALUE>> condition) {
    try {
      optionalAssert.is(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is empty.
   * <p>
   * Assertion succeeds:
   * <pre><code class='java'> assertThat(Optional.empty()).isEmpty();</code></pre>
   *
   * Assertion fails:
   * <pre><code class='java'> assertThat(Optional.of("something")).isEmpty();</code></pre>
   *
   * @return this assertion object.
   */
  public SoftOptionalAssert<VALUE> isEmpty() {
    try {
      optionalAssert.isEmpty();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isEqualTo(Object expected) {
    try {
      optionalAssert.isEqualTo(expected);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isExactlyInstanceOf(Class<?> type) {
    try {
      optionalAssert.isExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isIn(Iterable<?> values) {
    try {
      optionalAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isIn(Object... values) {
    try {
      optionalAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isInstanceOf(Class<?> type) {
    try {
      optionalAssert.isInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isInstanceOfAny(Class<?>... types) {
    try {
      optionalAssert.isInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public <T> SoftOptionalAssert<VALUE> isInstanceOfSatisfying(Class<T> type,
      Consumer<T> requirements) {
    try {
      optionalAssert.isInstanceOfSatisfying(type,requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNot(Condition<? super Optional<VALUE>> condition) {
    try {
      optionalAssert.isNot(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.Optional}, it's an alias of {@link #isPresent()}.
   * <p>
   * Assertion succeeds:
   * <pre><code class='java'> assertThat(Optional.of("something")).isNotEmpty();</code></pre>
   *
   * Assertion fails:
   * <pre><code class='java'> assertThat(Optional.empty()).isNotEmpty();</code></pre>
   *
   * @return this assertion object.
   */
  public SoftOptionalAssert<VALUE> isNotEmpty() {
    try {
      optionalAssert.isNotEmpty();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNotEqualTo(Object other) {
    try {
      optionalAssert.isNotEqualTo(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNotExactlyInstanceOf(Class<?> type) {
    try {
      optionalAssert.isNotExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNotIn(Iterable<?> values) {
    try {
      optionalAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNotIn(Object... values) {
    try {
      optionalAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNotInstanceOf(Class<?> type) {
    try {
      optionalAssert.isNotInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNotInstanceOfAny(Class<?>... types) {
    try {
      optionalAssert.isNotInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNotNull() {
    try {
      optionalAssert.isNotNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNotOfAnyClassIn(Class<?>... types) {
    try {
      optionalAssert.isNotOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is empty (alias of {@link #isEmpty()}).
   * <p>
   * Assertion succeeds:
   * <pre><code class='java'> assertThat(Optional.empty()).isNotPresent();</code></pre>
   *
   * Assertion fails:
   * <pre><code class='java'> assertThat(Optional.of("something")).isNotPresent();</code></pre>
   *
   * @return this assertion object.
   */
  public SoftOptionalAssert<VALUE> isNotPresent() {
    try {
      optionalAssert.isNotPresent();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNotSameAs(Object other) {
    try {
      optionalAssert.isNotSameAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isNull() {
    try {
      optionalAssert.isNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isOfAnyClassIn(Class<?>... types) {
    try {
      optionalAssert.isOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.Optional}.
   * <p>
   * Assertion succeeds:
   * <pre><code class='java'> assertThat(Optional.of("something")).isPresent();</code></pre>
   *
   * Assertion fails:
   * <pre><code class='java'> assertThat(Optional.empty()).isPresent();</code></pre>
   *
   * @return this assertion object.
   */
  public SoftOptionalAssert<VALUE> isPresent() {
    try {
      optionalAssert.isPresent();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> isSameAs(Object expected) {
    try {
      optionalAssert.isSameAs(expected);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Call {@link Optional#map(Function) map} on the {@code Optional} under test, assertions chained afterward are performed on the {@code Optional} resulting from the map call.
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(Optional.&lt;String&gt;empty()).map(String::length)
   *                                     .isEmpty();
   *
   * assertThat(Optional.of("42")).contains("42")
   *                              .map(String::length)
   *                              .contains(2);
   *
   * // assertion fails
   * assertThat(Optional.of("42")).map(String::length)
   *                              .contains(3);</code></pre>
   *
   * @param <U> the type wrapped in the {@link java.util.Optional} after the {@link Optional#map(Function) map} operation.
   * @param mapper the {@link Function} to use in the {@link Optional#map(Function) map} operation.
   * @return a new {@link AbstractOptionalAssert} for assertions chaining on the map of the Optional.
   * @throws AssertionError if the actual {@link java.util.Optional} is null.
   * @since 3.6.0
   */
  public <U> SoftOptionalAssert<U> map(Function<? super VALUE, ? extends U> mapper) {
    return new SoftOptionalAssert(optionalAssert.map(mapper).actual(), errorCollector);
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
  public SoftOptionalAssert<VALUE> matches(Predicate<? super Optional<VALUE>> predicate) {
    try {
      optionalAssert.matches(predicate);
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
  public SoftOptionalAssert<VALUE> matches(Predicate<? super Optional<VALUE>> predicate,
      String predicateDescription) {
    try {
      optionalAssert.matches(predicate,predicateDescription);
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
  public SoftOptionalAssert<VALUE> overridingErrorMessage(String newErrorMessage, Object... args) {
    optionalAssert.overridingErrorMessage(newErrorMessage,args);
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
  public SoftOptionalAssert<VALUE> overridingErrorMessage(Supplier<String> supplier) {
    optionalAssert.overridingErrorMessage(supplier);
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
  public final SoftOptionalAssert<VALUE> satisfies(
      Consumer<? super Optional<VALUE>>... requirements) {
    try {
      optionalAssert.satisfies(requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> satisfies(Condition<? super Optional<VALUE>> condition) {
    try {
      optionalAssert.satisfies(condition);
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
  public final SoftOptionalAssert<VALUE> satisfies(
      ThrowingConsumer<? super Optional<VALUE>>... assertions) {
    try {
      optionalAssert.satisfies(assertions);
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
  public final SoftOptionalAssert<VALUE> satisfiesAnyOf(
      Consumer<? super Optional<VALUE>>... assertions) {
    try {
      optionalAssert.satisfiesAnyOf(assertions);
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
  public final SoftOptionalAssert<VALUE> satisfiesAnyOf(
      ThrowingConsumer<? super Optional<VALUE>>... assertions) {
    try {
      optionalAssert.satisfiesAnyOf(assertions);
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
  public SoftOptionalAssert<VALUE> usingComparator(
      Comparator<? super Optional<VALUE>> customComparator) {
    optionalAssert.usingComparator(customComparator);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> usingComparator(
      Comparator<? super Optional<VALUE>> customComparator, String customComparatorDescription) {
    optionalAssert.usingComparator(customComparator,customComparatorDescription);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> usingDefaultComparator() {
    optionalAssert.usingDefaultComparator();
    return this;
  }

  /**
   * Revert to standard comparison for incoming assertion {@link java.util.Optional} value checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling
   * {@link #usingValueComparator(Comparator)}.
   *
   * @return {@code this} assertion object.
   */
  public SoftOptionalAssert<VALUE> usingDefaultValueComparator() {
    try {
      optionalAssert.usingDefaultValueComparator();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
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
  public SoftOptionalAssert<VALUE> usingEquals(
      BiPredicate<? super Optional<VALUE>, ? super Optional<VALUE>> predicate) {
    optionalAssert.usingEquals(predicate);
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
  public SoftOptionalAssert<VALUE> usingEquals(
      BiPredicate<? super Optional<VALUE>, ? super Optional<VALUE>> predicate,
      String customEqualsDescription) {
    optionalAssert.usingEquals(predicate,customEqualsDescription);
    return this;
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
   * <p><strong>Example</strong></p>
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
   * assertThat(Optional.of(pramodSadalage)).usingRecursiveAssertion()
   *                                        .allFieldsSatisfy(field -> field != null); </code></pre>
   *
   * <p>In case one or more fields in the object graph fails the predicate test, the entire assertion will fail. Failing fields
   * will be listed in the failure report using a JSON path-ish notation.</p>
   *
   * @return A new instance of {@link RecursiveAssertionAssert} built with a default {@link RecursiveAssertionConfiguration}.
   */
  public SoftOptionalAssert<VALUE> usingRecursiveAssertion() {
    try {
      optionalAssert.usingRecursiveAssertion();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * <p>The same as {@link #usingRecursiveAssertion()}, but this method allows the developer to pass in an explicit recursion
   * configuration. This configuration gives fine-grained control over what to include in the recursion, such as:</p>
   *
   * <ul>
   *   <li>Exclusion of fields that are null</li>
   *   <li>Exclusion of fields by path</li>
   *   <li>Exclusion of fields by type</li>
   *   <li>Exclusion of primitive fields</li>
   *   <li>Inclusion of Java Class Library types in the recursive execution</li>
   *   <li>Treatment of {@link java.util.Collection} and array objects</li>
   *   <li>Treatment of {@link java.util.Map} objects</li>
   *   <li>Treatment of Optional and primitive Optional objects</li>
   * </ul>
   *
   * <p>Please refer to the documentation of {@link RecursiveAssertionConfiguration.Builder} for more details.</p>
   *
   * @param recursiveAssertionConfiguration The recursion configuration described above.
   * @return A new instance of {@link RecursiveAssertionAssert} built with a default {@link RecursiveAssertionConfiguration}.
   */
  public SoftOptionalAssert<VALUE> usingRecursiveAssertion(
      RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    try {
      optionalAssert.usingRecursiveAssertion(recursiveAssertionConfiguration);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Enable using a recursive field by field comparison strategy when calling the chained {@link SoftRecursiveComparisonAssert},
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   boolean hasPhd;
   * }
   *
   * public class Doctor {
   *  String name;
   *  boolean hasPhd;
   * }
   *
   * Doctor drSheldon = new Doctor("Sheldon Cooper", true);
   * Person sheldon = new Person("Sheldon Cooper", true);
   *
   * Optional&lt;Doctor&gt; doctor = Optional.of(drSheldon);
   * Optional&lt;Person&gt; person = Optional.of(sheldon);
   *
   * // assertion succeeds as both maps contains equivalent items.
   * assertThat(doctor).usingRecursiveComparison()
   *                   .isEqualTo(person);
   *
   * // assertion fails because leonard names are different.
   * drSheldon.setName("Sheldon Kooper");
   * assertThat(doctor).usingRecursiveComparison()
   *                   .isEqualTo(person);</code></pre>
   *
   * A detailed documentation for the recursive comparison is available here: <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>.
   * <p>
   * The default recursive comparison behavior is {@link RecursiveComparisonConfiguration configured} as follows:
   * <ul>
   *   <li> different types of iterable can be compared by default as in the example, this can be turned off by calling {@link SoftRecursiveComparisonAssert#withStrictTypeChecking() withStrictTypeChecking}.</li>
   *   <li>overridden equals methods are used in the comparison (unless stated otherwise - see <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals">https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals</a>)</li>
   *   <li>the following types are compared with these comparators:
   *     <ul>
   *       <li>{@code java.lang.Double}: {@code DoubleComparator} with precision of 1.0E-15</li>
   *       <li>{@code java.lang.Float}: {@code FloatComparator }with precision of 1.0E-6</li>
   *       <li>any comparators previously registered with {@link AbstractIterableAssert#usingComparatorForType(Comparator, Class)} </li>
   *     </ul>
   *   </li>
   * </ul>
   *
   * @return a new {@link SoftRecursiveComparisonAssert} instance
   * @see RecursiveComparisonConfiguration RecursiveComparisonConfiguration
   */
  public SoftRecursiveComparisonAssert<?> usingRecursiveComparison() {
    return new SoftRecursiveComparisonAssert<>(optionalAssert.usingRecursiveComparison(), errorCollector);
  }

  /**
   * Same as {@link #usingRecursiveComparison()} but allows to specify your own {@link RecursiveComparisonConfiguration}.
   *
   * @param recursiveComparisonConfiguration the {@link RecursiveComparisonConfiguration} used in the chained {@link SoftRecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion.
   * @return a new {@link SoftRecursiveComparisonAssert} instance built with the given {@link RecursiveComparisonConfiguration}.
   */
  public SoftRecursiveComparisonAssert<?> usingRecursiveComparison(
      RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    return new SoftRecursiveComparisonAssert<>(optionalAssert.usingRecursiveComparison(recursiveComparisonConfiguration), errorCollector);
  }

  /**
   * Use given custom comparator instead of relying on actual type A <code>equals</code> method to compare the
   * {@link java.util.Optional} value's object for incoming assertion checks.
   * <p>
   * Custom comparator is bound to assertion instance, meaning that if a new assertion is created, it will use default
   * comparison strategy.
   * <p>
   * Examples :
   *
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   *
   * // Fail if equals has not been overridden in TolkienCharacter as equals default implementation only compares references
   * assertThat(Optional.of(frodo)).contains(frodoClone);
   *
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(Optional.of(frodo)).usingValueComparator(new FieldByFieldComparator()).contains(frodoClone);</code></pre>
   *
   * @param customComparator the comparator to use for incoming assertion checks.
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
   */
  public SoftOptionalAssert<VALUE> usingValueComparator(
      Comparator<? super VALUE> customComparator) {
    optionalAssert.usingValueComparator(customComparator);
    return this;
  }

  /**
   * Alternative method for {@link AbstractAssert#overridingErrorMessage}
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
  public SoftOptionalAssert<VALUE> withFailMessage(String newErrorMessage, Object... args) {
    optionalAssert.withFailMessage(newErrorMessage,args);
    return this;
  }

  /**
   * Alternative method for {@link AbstractAssert#overridingErrorMessage}
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
  public SoftOptionalAssert<VALUE> withFailMessage(Supplier<String> supplier) {
    optionalAssert.withFailMessage(supplier);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> withRepresentation(Representation representation) {
    optionalAssert.withRepresentation(representation);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftOptionalAssert<VALUE> withThreadDumpOnError() {
    optionalAssert.withThreadDumpOnError();
    return this;
  }
}
