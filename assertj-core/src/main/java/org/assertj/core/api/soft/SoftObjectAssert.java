package org.assertj.core.api.soft;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.assertj.core.annotation.Beta;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assert;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.Condition;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.RecursiveAssertionAssert;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.introspection.IntrospectionError;

@Beta
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class SoftObjectAssert<ACTUAL> implements SoftAssert {
  private final AssertionErrorCollector errorCollector;

  private final ObjectAssert<ACTUAL> objectAssert;

  public SoftObjectAssert(ACTUAL actual, AssertionErrorCollector errorCollector) {
    this.objectAssert = assertThat(actual);
    this.errorCollector = errorCollector;
  }

  /**
   * {@inheritDoc}
   */
  public ACTUAL actual() {
    return objectAssert.actual();
  }

  public SoftObjectAssert<ACTUAL> as(String description, Object... args) {
    objectAssert.as(description,args);
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
  public SoftObjectAssert<ACTUAL> as(Supplier<String> descriptionSupplier) {
    objectAssert.as(descriptionSupplier);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public <SOFT_ASSERT extends SoftAssert> SOFT_ASSERT asInstanceOf(
      DefaultSoftAssertFactory<?, SOFT_ASSERT> softAssertFactory) {
    return softAssertFactory.createSoftAssert(actual(), errorCollector);
  }

  public SoftObjectAssert<ACTUAL> as(Description description) {
    objectAssert.as(description);
    return this;
  }

  /**
   * {@inheritDoc}
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
  public SoftObjectAssert<ACTUAL> describedAs(String description, Object... args) {
    objectAssert.describedAs(description,args);
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
  public SoftObjectAssert<ACTUAL> describedAs(Supplier<String> descriptionSupplier) {
    objectAssert.describedAs(descriptionSupplier);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> describedAs(Description description) {
    objectAssert.describedAs(description);
    return this;
  }

  /**
   * The description of this assertion set with {@link #describedAs(String, Object...)} or
   * {@link #describedAs(Description)}.
   *
   * @return the description String representation of this assertion.
   */
  public SoftObjectAssert<ACTUAL> descriptionText() {
    objectAssert.descriptionText();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> doesNotHave(Condition<? super ACTUAL> condition) {
    try {
      objectAssert.doesNotHave(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> doesNotHaveSameClassAs(Object other) {
    try {
      objectAssert.doesNotHaveSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> doesNotHaveSameHashCodeAs(Object other) {
    try {
      objectAssert.doesNotHaveSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> doesNotHaveToString(String otherToString) {
    try {
      objectAssert.doesNotHaveToString(otherToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> doesNotHaveToString(String expectedStringTemplate,
      Object... args) {
    try {
      objectAssert.doesNotHaveToString(expectedStringTemplate,args);
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
  public SoftObjectAssert<ACTUAL> doesNotMatch(Predicate<? super ACTUAL> predicate) {
    try {
      objectAssert.doesNotMatch(predicate);
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
  public SoftObjectAssert<ACTUAL> doesNotMatch(Predicate<? super ACTUAL> predicate,
      String predicateDescription) {
    try {
      objectAssert.doesNotMatch(predicate,predicateDescription);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public <T> SoftObjectAssert<ACTUAL> doesNotReturn(T expected, Function<ACTUAL, T> from) {
    try {
      objectAssert.doesNotReturn(expected,from);
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
    return objectAssert.equals(obj);
  }

  /**
   * Extracts the value of given field/property from the object under test, the extracted value becoming the new object under test.
   * <p>
   * If the object under test is a {@link java.util.Map}, the {@code propertyOrField} parameter is used as a key to the map.
   * <p>
   * Nested fields/properties are supported, specifying "address.street.number" is equivalent to:
   * <pre><code class='java'> // "address.street.number" corresponding to pojo properties
   * actual.getAddress().getStreet().getNumber();</code></pre>
   * or if address is a {@link java.util.Map}:
   * <pre><code class='java'> // "address" is a Map property (that is getAddress() returns a Map)
   * actual.getAddress().get("street").getNumber();</code></pre>
   * <p>
   * Private field can be extracted unless you call {@link org.assertj.core.api.Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * Note that since the value is extracted as an Object, only Object assertions can be chained after extracting.
   * <p>
   * Example:
   * <pre><code class='java'> // Create frodo, setting its name, age and Race (Race having a name property)
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   *
   * // let's extract and verify Frodo's name:
   * assertThat(frodo).extracting(&quot;name&quot;)
   *                  .isEqualTo(&quot;Frodo&quot;);
   * // or its race name:
   * assertThat(frodo).extracting(&quot;race.name&quot;)
   *                  .isEqualTo(&quot;Hobbit&quot;);
   *
   * // The extracted value being a String, we would like to use String assertions but we can't due to Java generics limitations.
   * // The following assertion does NOT compile:
   * assertThat(frodo).extracting(&quot;name&quot;)
   *                  .startsWith(&quot;Fro&quot;);
   *
   * // To get String assertions, use {@link #extracting(String, DefaultSoftAssertFactory)}:
   * assertThat(frodo).extracting(&quot;name&quot;, as(InstanceOfAssertFactories.STRING))
   *                  .startsWith(&quot;Fro&quot;);</code></pre>
   * <p>
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field is not accessible (i.e. does not exist) an {@link IntrospectionError} is thrown.
   *
   * @param propertyOrField the property/field to extract from the initial object under test
   * @return a new {@link ObjectAssert} instance whose object under test is the extracted property/field value
   * @throws org.assertj.core.util.introspection.IntrospectionError if one of the given name does not match a field or property
   * @see #extracting(String, DefaultSoftAssertFactory)
   * @since 3.13.0
   */
  public SoftObjectAssert<?> extracting(String propertyOrField) {
    return new SoftObjectAssert<>(objectAssert.extracting(propertyOrField).actual(), errorCollector);
  }

  /**
   * Extracts the value of given field/property from the object under test, the extracted value becoming the new object under test.
   * <p>
   * If the object under test is a {@link java.util.Map}, the {@code propertyOrField} parameter is used as a key to the map.
   * <p>
   * Nested field/property is supported, specifying "address.street.number" is equivalent to get the value
   * corresponding to actual.getAddress().getStreet().getNumber()
   * <p>
   * Private field can be extracted unless you call {@link org.assertj.core.api.Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * The {@code softAssertFactory} parameter allows to specify an {@link DefaultSoftAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Wrapping the given {@link DefaultSoftAssertFactory} with {@link org.assertj.core.api.Assertions#as(InstanceOfAssertFactory)} makes the
   * assertion more readable.
   * <p>
   * Example:
   * <pre><code class='java'> // Create frodo, setting its name, age and Race (Race having a name property)
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   *
   * // let's extract and verify Frodo's name:
   * assertThat(frodo).extracting(&quot;name&quot;, as(InstanceOfAssertFactories.STRING))
   *                  .startsWith(&quot;Fro&quot;);
   *
   * // The following assertion will fail as Frodo's name is not an Integer:
   * assertThat(frodo).extracting(&quot;name&quot;, as(InstanceOfAssertFactories.INTEGER))
   *                  .isZero();</code></pre>
   * <p>
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field is not accessible (i.e. does not exist) an {@link IntrospectionError} is thrown.
   *
   * @param <SOFT_ASSERT> the type of the resulting {@code SoftAssert}
   * @param propertyOrField the property/field to extract from the initial object under test
   * @param softAssertFactory the factory which verifies the type and creates the new {@code SoftAssert}
   * @return a new narrowed {@link Assert} instance whose object under test is the extracted property/field value
   * @throws NullPointerException if the given factory is {@code null}
   * @throws org.assertj.core.util.introspection.IntrospectionError if one of the given name does not match a field or property
   * @since 3.14.0
   */
  public <SOFT_ASSERT extends SoftAssert> SOFT_ASSERT extracting(String propertyOrField,
      DefaultSoftAssertFactory<?, SOFT_ASSERT> softAssertFactory) {
    return extracting(propertyOrField).asInstanceOf(softAssertFactory);
  }

  /**
   * Extracts the values of given fields/properties from the object under test into a list, this new list becoming
   * the object under test.
   * <p>
   * If you extract "id", "name" and "email" fields/properties then the list will contain the id, name and email values
   * of the object under test, you can then perform list assertions on the extracted values.
   * <p>
   * If the object under test is a {@link java.util.Map} with {@link String} keys, extracting will extract values matching the given fields/properties.
   * <p>
   * Nested fields/properties are supported, specifying "address.street.number" is equivalent to:
   * <pre><code class='java'> // "address.street.number" corresponding to pojo properties
   * actual.getAddress().getStreet().getNumber();</code></pre>
   * or if address is a {@link java.util.Map}:
   * <pre><code class='java'> // "address" is a Map property (that is getAddress() returns a Map)
   * actual.getAddress().get("street").getNumber();</code></pre>
   * <p>
   * Private fields can be extracted unless you call {@link org.assertj.core.api.Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * Example:
   * <pre><code class='java'> // Create frodo, setting its name, age and Race (Race having a name property)
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   *
   * // let's verify Frodo's name, age and race name:
   * assertThat(frodo).extracting(&quot;name&quot;, &quot;age&quot;, &quot;race.name&quot;)
   *                  .containsExactly(&quot;Frodo&quot;, 33, &quot;Hobbit&quot;);</code></pre>
   * <p>
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field is not accessible (i.e. does not exist) an {@link IntrospectionError} is thrown.
   * <p>
   * Note that the order of extracted values is consistent with the order of the given property/field.
   *
   * @param propertiesOrFields the properties/fields to extract from the initial object under test
   * @return a new assertion object whose object under test is the list containing the extracted properties/fields values
   * @throws org.assertj.core.util.introspection.IntrospectionError if one of the given name does not match a field or property
   */
  public SoftListAssert<Object> extracting(String... propertiesOrFields) {
    return new SoftListAssert(objectAssert.extracting(propertiesOrFields).actual(), errorCollector);
  }

  /**
   * Uses the given {@link Function} to extract a value from the object under test, the extracted value becoming the new object under test.
   * <p>
   * Note that since the value is extracted as an Object, only Object assertions can be chained after extracting.
   * <p>
   * Example:
   * <pre><code class='java'> // Create frodo, setting its name, age and Race
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   *
   * // let's extract and verify Frodo's name:
   * assertThat(frodo).extracting(TolkienCharacter::getName)
   *                  .isEqualTo(&quot;Frodo&quot;);
   *
   * // The extracted value being a String, we would like to use String assertions but we can't due to Java generics limitations.
   * // The following assertion does NOT compile:
   * assertThat(frodo).extracting(TolkienCharacter::getName)
   *                  .startsWith(&quot;Fro&quot;);
   *
   * // To get String assertions, use {@link #extracting(Function, DefaultSoftAssertFactory)}:
   * assertThat(frodo).extracting(TolkienCharacter::getName, as(InstanceOfAssertFactories.STRING))
   *                  .startsWith(&quot;Fro&quot;);</code></pre>
   *
   * @param <T> the expected extracted value type.
   * @param extractor the extractor function used to extract the value from the object under test.
   * @return a new {@link ObjectAssert} instance whose object under test is the extracted value
   * @see #extracting(Function, DefaultSoftAssertFactory)
   * @since 3.11.0
   */
  public <T> SoftObjectAssert<T> extracting(Function<? super ACTUAL, T> extractor) {
    return new SoftObjectAssert<>(extractor.apply(actual()), errorCollector);
  }

  /**
   * Uses the given {@link Function} to extract a value from the object under test, the extracted value becoming the new object under test.
   * <p>
   * Note that since the value is extracted as an Object, only Object assertions can be chained after extracting.
   * <p>
   * The {@code softAssertFactory} parameter allows to specify an {@link DefaultSoftAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Wrapping the given {@link DefaultSoftAssertFactory} with {@link org.assertj.core.api.Assertions#as(InstanceOfAssertFactory)} makes the
   * assertion more readable.
   * <p>
   * Example:
   * <pre><code class='java'> // Create frodo, setting its name, age and Race
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   *
   * // let's extract and verify Frodo's name:
   * assertThat(frodo).extracting(TolkienCharacter::getName, as(InstanceOfAssertFactories.STRING))
   *                  .startsWith(&quot;Fro&quot;);
   *
   * // The following assertion will fail as Frodo's name is not an Integer:
   * assertThat(frodo).extracting(TolkienCharacter::getName, as(InstanceOfAssertFactories.INTEGER))
   *                  .isZero();</code></pre>
   *
   * @param <T> the expected extracted value type
   * @param <SOFT_ASSERT> the type of the resulting {@code SoftAssert}
   * @param extractor the extractor function used to extract the value from the object under test
   * @param softAssertFactory the factory which verifies the type and creates the new {@code SoftAssert}
   * @return a new narrowed {@link Assert} instance whose object under test is the extracted value
   * @throws NullPointerException if the given factory is {@code null}
   * @since 3.14.0
   */
  public <T, SOFT_ASSERT extends SoftAssert> SOFT_ASSERT extracting(
      Function<? super ACTUAL, T> extractor,
      DefaultSoftAssertFactory<?, SOFT_ASSERT> softAssertFactory) {
    return extracting(extractor).asInstanceOf(softAssertFactory);
  }

  /**
   * Uses the given {@link Function}s to extract the values from the object under test into a list, this new list becoming
   * the object under test.
   * <p>
   * If the given {@link Function}s extract the id, name and email values then the list will contain the id, name and email values
   * of the object under test, you can then perform list assertions on the extracted values.
   * <p>
   * Example:
   * <pre><code class='java'> // Create frodo, setting its name, age and Race (Race having a name property)
   * TolkienCharacter frodo = new TolkienCharacter(&quot;Frodo&quot;, 33, HOBBIT);
   *
   * // let's verify Frodo's name, age and race name:
   * assertThat(frodo).extracting(TolkienCharacter::getName,
   *                              character -&gt; character.age, // public field
   *                              character -&gt; character.getRace().getName())
   *                  .containsExactly(&quot;Frodo&quot;, 33, "Hobbit");</code></pre>
   * <p>
   * Note that the order of extracted values is consistent with the order of given extractor functions.
   *
   * @param extractors the extractor functions to extract values from the Object under test.
   * @return a new assertion object whose object under test is the list containing the extracted values
   */
  @SafeVarargs
  public final SoftListAssert<Object> extracting(Function<? super ACTUAL, ?>... extractors) {
    return new SoftListAssert(objectAssert.extracting(extractors).actual(), errorCollector);
  }

  /**
   * Exposes the {@link WritableAssertionInfo} used in the current assertion for better extensibility.<br> When writing
   * your own assertion class, you can use the returned {@link WritableAssertionInfo} to change the error message and
   * still keep the description set by the assertion user.
   *
   * @return the {@link WritableAssertionInfo} used in the current assertion
   */
  public WritableAssertionInfo getWritableAssertionInfo() {
    return objectAssert.getWritableAssertionInfo();
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> has(Condition<? super ACTUAL> condition) {
    try {
      objectAssert.has(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Asserts that the actual object has only null fields or properties.
   * <p>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p>
   * Private fields are checked, but this can be disabled using {@link org.assertj.core.api.Assertions#setAllowComparingPrivateFields(boolean)},
   * if disable only <b>accessible</b> fields values are checked,
   * accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter(null, null, null);
   * TolkienCharacter sam = new TolkienCharacter("sam", null, null);
   *
   * // assertion succeeds since all frodo's fields are null
   * assertThat(frodo).hasAllNullFieldsOrProperties();
   *
   * // assertion fails because sam has its name set
   * assertThat(sam).hasAllNullFieldsOrProperties();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws AssertionError if some field or properties of the actual object are not null.
   * @since 3.12.0
   */
  public SoftObjectAssert<ACTUAL> hasAllNullFieldsOrProperties() {
    try {
      objectAssert.hasAllNullFieldsOrProperties();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Asserts that the actual object has only null fields or properties <b>except for the given ones</b>
   * (inherited ones are taken into account).
   * <p>
   * If an object has a field and a property with the same name, the property value will be user over the field.
   * <p>
   * Private fields are checked, but this can be disabled using {@link org.assertj.core.api.Assertions#setAllowComparingPrivateFields(boolean)},
   * if disabled only <b>accessible</b> fields values are checked,
   * accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", null, null);
   *
   * // assertion succeeds since frodo has only non null field is name
   * assertThat(frodo).hasAllNullFieldsOrPropertiesExcept("name");
   *
   * // ... but if we specify any field other than name, the assertion fails
   * assertThat(frodo).hasAllNullFieldsOrPropertiesExcept("race");</code></pre>
   *
   * @param propertiesOrFieldsToIgnore properties/fields that won't be checked for not being null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws AssertionError if some (non ignored) fields or properties of the actual object are not null.
   * @since 3.12.0
   */
  public SoftObjectAssert<ACTUAL> hasAllNullFieldsOrPropertiesExcept(
      String... propertiesOrFieldsToIgnore) {
    try {
      objectAssert.hasAllNullFieldsOrPropertiesExcept(propertiesOrFieldsToIgnore);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Asserts that the actual object has the specified field or property. Static and synthetic fields are ignored since 3.19.0.
   * <p>
   * Private fields are matched by default but this can be changed by calling {@link org.assertj.core.api.Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *
   *   private String name;
   *   private int age;
   *   // constructor omitted
   *
   *   public String getName() {
   *     return this.name;
   *   }
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33);
   *
   * // assertions will pass :
   * assertThat(frodo).hasFieldOrProperty("name")
   *                  .hasFieldOrProperty("age"); // private field are matched by default
   *
   * // assertions will fail :
   * assertThat(frodo).hasFieldOrProperty("not_exists");
   * assertThat(frodo).hasFieldOrProperty(null);
   * // disable looking for private fields
   * Assertions.setAllowExtractingPrivateFields(false);
   * assertThat(frodo).hasFieldOrProperty("age"); </code></pre>
   *
   * @param name the field/property name to check
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws IllegalArgumentException if name is {@code null}.
   * @throws AssertionError if the actual object does not have the given field/property
   */
  public SoftObjectAssert<ACTUAL> hasFieldOrProperty(String name) {
    try {
      objectAssert.hasFieldOrProperty(name);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Asserts that the actual object has the specified field or property with the given value. Static and synthetic fields are ignored since 3.19.0.
   * <p>
   * Private fields are matched by default but this can be changed by calling {@link org.assertj.core.api.Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * If you are looking to chain multiple assertions on different properties in a type safe way, consider chaining
   * {@link #returns(Object, Function)} and {@link #doesNotReturn(Object, Function)} calls.
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *   private String name;
   *   private int age;
   *   // constructor omitted
   *
   *   public String getName() {
   *     return this.name;
   *   }
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33);
   * TolkienCharacter noname = new TolkienCharacter(null, 33);
   *
   * // assertions will pass :
   * assertThat(frodo).hasFieldOrPropertyWithValue("name", "Frodo");
   * assertThat(frodo).hasFieldOrPropertyWithValue("age", 33);
   * assertThat(noname).hasFieldOrPropertyWithValue("name", null);
   *
   * // assertions will fail :
   * assertThat(frodo).hasFieldOrPropertyWithValue("name", "not_equals");
   * assertThat(frodo).hasFieldOrPropertyWithValue(null, 33);
   * assertThat(frodo).hasFieldOrPropertyWithValue("age", null);
   * assertThat(noname).hasFieldOrPropertyWithValue("name", "Frodo");
   * // disable extracting private fields
   * Assertions.setAllowExtractingPrivateFields(false);
   * assertThat(frodo).hasFieldOrPropertyWithValue("age", 33); </code></pre>
   *
   * @param name the field/property name to check
   * @param value the field/property expected value
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws IllegalArgumentException if name is {@code null}.
   * @throws AssertionError if the actual object does not have the given field/property
   * @throws AssertionError if the actual object has the given field/property but not with the expected value
   * @see AbstractObjectAssert#hasFieldOrProperty(java.lang.String)
   */
  public SoftObjectAssert<ACTUAL> hasFieldOrPropertyWithValue(String name, Object value) {
    try {
      objectAssert.hasFieldOrPropertyWithValue(name,value);
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
    return objectAssert.hashCode();
  }

  /**
   * Asserts that the actual object has no null fields or properties (inherited ones are taken into account).
   * <p>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p>
   * Private fields are checked, but this can be disabled using {@link org.assertj.core.api.Assertions#setAllowComparingPrivateFields(boolean)},
   * if disabled only <b>accessible</b> fields values are
   * checked, accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, null);
   *
   * // assertion succeeds since all frodo's fields are set
   * assertThat(frodo).hasNoNullFieldsOrProperties();
   *
   * // assertion fails because sam does not have its race set
   * assertThat(sam).hasNoNullFieldsOrProperties();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws AssertionError if some fields or properties of the actual object are null.
   * @since 2.5.0 / 3.5.0
   */
  public SoftObjectAssert<ACTUAL> hasNoNullFieldsOrProperties() {
    try {
      objectAssert.hasNoNullFieldsOrProperties();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Asserts that the actual object has no null fields or properties <b>except for the given ones</b>
   * (inherited ones are taken into account).
   * <p>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p>
   * Private fields are checked, but this can be disabled using {@link org.assertj.core.api.Assertions#setAllowComparingPrivateFields(boolean)},
   * if disabled only <b>accessible</b> fields values are checked,
   * accessible fields include directly accessible fields (e.g. public) or fields with an accessible getter.
   * <p>
   * Example:
   * <pre><code class='java'>TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, null);
   *
   * // assertion succeeds since frodo has only null field is race
   * assertThat(frodo).hasNoNullFieldsOrPropertiesExcept("race");
   *
   * // ... but if we require the race field, the assertion fails
   * assertThat(frodo).hasNoNullFieldsOrPropertiesExcept("name", "age");</code></pre>
   *
   * @param propertiesOrFieldsToIgnore properties/fields that won't be checked for null.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws AssertionError if some (non ignored) fields or properties of the actual object are null.
   * @throws AssertionError if any of the properties or fields to ignore doesn't exist.
   * @since 2.5.0 / 3.5.0
   */
  public SoftObjectAssert<ACTUAL> hasNoNullFieldsOrPropertiesExcept(
      String... propertiesOrFieldsToIgnore) {
    try {
      objectAssert.hasNoNullFieldsOrPropertiesExcept(propertiesOrFieldsToIgnore);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Asserts that the actual object has only the specified fields and nothing else.
   * <p>
   * The assertion only checks declared fields (inherited fields are not checked) that are not static or synthetic.
   * <p>
   * By default, private fields are included in the check, this can be disabled with {@code Assertions.setAllowExtractingPrivateFields(false);}
   * but be mindful this has a global effect on all field introspection in AssertJ.
   * <p>
   * Example:
   * <pre><code class='java'> public class TolkienCharacter {
   *
   *   private String name;
   *   public int age;
   *
   *   public String getName() {
   *     return this.name;
   *   }
   * }
   *
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33);
   *
   * // assertion succeeds:
   * assertThat(frodo).hasOnlyFields("name", "age");
   *
   * // assertions fail:
   * assertThat(frodo).hasOnlyFields("name");
   * assertThat(frodo).hasOnlyFields("not_exists");
   * assertThat(frodo).hasOnlyFields(null);</code></pre>
   *
   * @param expectedFieldNames the expected field names actual should have
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual object is {@code null}.
   * @throws IllegalArgumentException if expectedFieldNames is {@code null}.
   * @throws AssertionError if the actual object does not have the expected fields (without extra ones)
   * @since 3.19.0
   */
  public SoftObjectAssert<ACTUAL> hasOnlyFields(String... expectedFieldNames) {
    try {
      objectAssert.hasOnlyFields(expectedFieldNames);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> hasSameClassAs(Object other) {
    try {
      objectAssert.hasSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> hasSameHashCodeAs(Object other) {
    try {
      objectAssert.hasSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> hasToString(String expectedToString) {
    try {
      objectAssert.hasToString(expectedToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> hasToString(String expectedStringTemplate, Object... args) {
    try {
      objectAssert.hasToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> is(Condition<? super ACTUAL> condition) {
    try {
      objectAssert.is(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isEqualTo(Object expected) {
    try {
      objectAssert.isEqualTo(expected);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isExactlyInstanceOf(Class<?> type) {
    try {
      objectAssert.isExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isIn(Iterable<?> values) {
    try {
      objectAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isIn(Object... values) {
    try {
      objectAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isInstanceOf(Class<?> type) {
    try {
      objectAssert.isInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isInstanceOfAny(Class<?>... types) {
    try {
      objectAssert.isInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public <T> SoftObjectAssert<ACTUAL> isInstanceOfSatisfying(Class<T> type,
      Consumer<T> requirements) {
    try {
      objectAssert.isInstanceOfSatisfying(type,requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNot(Condition<? super ACTUAL> condition) {
    try {
      objectAssert.isNot(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNotEqualTo(Object other) {
    try {
      objectAssert.isNotEqualTo(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNotExactlyInstanceOf(Class<?> type) {
    try {
      objectAssert.isNotExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNotIn(Iterable<?> values) {
    try {
      objectAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNotIn(Object... values) {
    try {
      objectAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNotInstanceOf(Class<?> type) {
    try {
      objectAssert.isNotInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNotInstanceOfAny(Class<?>... types) {
    try {
      objectAssert.isNotInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNotNull() {
    try {
      objectAssert.isNotNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNotOfAnyClassIn(Class<?>... types) {
    try {
      objectAssert.isNotOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNotSameAs(Object other) {
    try {
      objectAssert.isNotSameAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isNull() {
    try {
      objectAssert.isNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isOfAnyClassIn(Class<?>... types) {
    try {
      objectAssert.isOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> isSameAs(Object expected) {
    try {
      objectAssert.isSameAs(expected);
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
  public SoftObjectAssert<ACTUAL> matches(Predicate<? super ACTUAL> predicate) {
    try {
      objectAssert.matches(predicate);
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
  public SoftObjectAssert<ACTUAL> matches(Predicate<? super ACTUAL> predicate,
      String predicateDescription) {
    try {
      objectAssert.matches(predicate,predicateDescription);
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
  public SoftObjectAssert<ACTUAL> overridingErrorMessage(String newErrorMessage, Object... args) {
    objectAssert.overridingErrorMessage(newErrorMessage,args);
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
  public SoftObjectAssert<ACTUAL> overridingErrorMessage(Supplier<String> supplier) {
    objectAssert.overridingErrorMessage(supplier);
    return this;
  }

  public <T> SoftObjectAssert<ACTUAL> returns(T expected, Function<ACTUAL, T> from) {
    try {
      objectAssert.returns(expected,from);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public <T> SoftObjectAssert<ACTUAL> returns(T expected, Function<ACTUAL, T> from,
      String description) {
    try {
      objectAssert.returns(expected,from,description);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
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
  public final SoftObjectAssert<ACTUAL> satisfies(Consumer<? super ACTUAL>... requirements) {
    try {
      objectAssert.satisfies(requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> satisfies(Condition<? super ACTUAL> condition) {
    try {
      objectAssert.satisfies(condition);
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
  public final SoftObjectAssert<ACTUAL> satisfies(ThrowingConsumer<? super ACTUAL>... assertions) {
    try {
      objectAssert.satisfies(assertions);
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
  public final SoftObjectAssert<ACTUAL> satisfiesAnyOf(Consumer<? super ACTUAL>... assertions) {
    try {
      objectAssert.satisfiesAnyOf(assertions);
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
  public final SoftObjectAssert<ACTUAL> satisfiesAnyOf(
      ThrowingConsumer<? super ACTUAL>... assertions) {
    try {
      objectAssert.satisfiesAnyOf(assertions);
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
  public SoftObjectAssert<ACTUAL> usingComparator(Comparator<? super ACTUAL> customComparator) {
    objectAssert.usingComparator(customComparator);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> usingComparator(Comparator<? super ACTUAL> customComparator,
      String customComparatorDescription) {
    objectAssert.usingComparator(customComparator,customComparatorDescription);
    return this;
  }

  /**
   * Allows to set a specific comparator to compare properties or fields with the given type.
   * A typical usage is for comparing fields of numeric type at a given precision.
   * <p>
   * The comparators specified by this method are used in the {@link #usingRecursiveComparison() recursive comparison},
   * the {@link #returns(Object, Function)} and {@link #doesNotReturn(Object, Function)} assertions.
   * <p>
   * Note that for the recursive comparison, it is more idiomatic to use {@link SoftRecursiveComparisonAssert#withComparatorForType(Comparator, Class)} or {@link SoftRecursiveComparisonAssert#withEqualsForType(BiPredicate, Class)}.
   * <p>
   * If multiple compatible comparators have been registered for a given {@code type}, the closest in the inheritance
   * chain to the given {@code type} is chosen in the following order:
   * <ol>
   * <li>The comparator for the exact given {@code type}</li>
   * <li>The comparator of a superclass of the given {@code type}</li>
   * <li>The comparator of an interface implemented by the given {@code type}</li>
   * </ol>
   * <p>
   * Example:
   * <pre><code class='java'> import static org.assertj.core.api.Assertions.from;
   *
   * Jedi yoda = new Jedi("Yoda");
   *
   * // assertion succeeds
   * assertThat(yoda).usingComparatorForType(String.CASE_INSENSITIVE_ORDER, String.class)
   *                 .returns("YODA", from(Jedi::getName));
   *
   * // assertion will fail
   * assertThat(yoda).usingComparatorForType(String.CASE_INSENSITIVE_ORDER, String.class)
   *                 .returns("LUKE", from(Jedi::getName));</code></pre>
   *
   * @param comparator the {@link Comparator} to use
   * @param type the {@link Class} of the type the comparator should be used for
   * @param <T> the type of objects that the comparator should be used for
   * @return {@code this} assertions object
   * @see #returns(Object, Function)
   * @see #doesNotReturn(Object, Function)
   */
  public <T> SoftObjectAssert<ACTUAL> usingComparatorForType(Comparator<? super T> comparator,
      Class<T> type) {
    try {
      objectAssert.usingComparatorForType(comparator,type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> usingDefaultComparator() {
    objectAssert.usingDefaultComparator();
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
  public SoftObjectAssert<ACTUAL> usingEquals(
      BiPredicate<? super ACTUAL, ? super ACTUAL> predicate) {
    objectAssert.usingEquals(predicate);
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
  public SoftObjectAssert<ACTUAL> usingEquals(BiPredicate<? super ACTUAL, ? super ACTUAL> predicate,
      String customEqualsDescription) {
    objectAssert.usingEquals(predicate,customEqualsDescription);
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
   * <p>Example:</p>
   * <pre><code style='java'> class Author {
   *   String name;
   *   String email;
   *   List&lt;Book&gt; books = new ArrayList();
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
   * <p>In case one or more fields in the object graph fails the predicate test, the entire assertion will fail. Failing fields
   * will be listed in the failure report using a JSON path-ish notation.</p>
   *
   * @return A new instance of {@link RecursiveAssertionAssert} built with a default {@link RecursiveAssertionConfiguration}.
   */
  public SoftObjectAssert<ACTUAL> usingRecursiveAssertion() {
    try {
      objectAssert.usingRecursiveAssertion();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * <p>The same as {@link #usingRecursiveAssertion()}, but this method allows the developer to pass in an explicit recursion
   * configuration. This configuration gives fine-grained control over what to include in the recursion, such as:</p>
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
  public SoftObjectAssert<ACTUAL> usingRecursiveAssertion(
      RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    try {
      objectAssert.usingRecursiveAssertion(recursiveAssertionConfiguration);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  /**
   * Enable using a recursive field by field comparison strategy when calling the chained {@link SoftRecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion.
   * <p>
   * The detailed documentation is available here: <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>.
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   double height;
   *   Home home = new Home();
   * }
   *
   * public class Home {
   *   Address address = new Address();
   *   Date ownedSince;
   * }
   *
   * public static class Address {
   *   int number;
   *   String street;
   * }
   *
   * Person sherlock = new Person("Sherlock", 1.80);
   * sherlock.home.ownedSince = new Date(123);
   * sherlock.home.address.street = "Baker Street";
   * sherlock.home.address.number = 221;
   *
   * Person sherlock2 = new Person("Sherlock", 1.80);
   * sherlock2.home.ownedSince = new Date(123);
   * sherlock2.home.address.street = "Baker Street";
   * sherlock2.home.address.number = 221;
   *
   * // assertion succeeds as the data of both objects are the same.
   * assertThat(sherlock).usingRecursiveComparison()
   *                     .isEqualTo(sherlock2);
   *
   * // assertion fails because sherlock.equals(sherlock2) is false.
   * assertThat(sherlock).isEqualTo(sherlock2);</code></pre>
   * <p>
   * The recursive comparison is performed according to the default {@link RecursiveComparisonConfiguration} that is:
   * <ul>
   * <li>actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call {@link SoftRecursiveComparisonAssert#withStrictTypeChecking() withStrictTypeChecking()} to change that behavior). </li>
   * <li>overridden equals methods were used in the comparison (unless stated otherwise)</li>
   * <li>these types were compared with the following comparators:
   *   <ul>
   *   <li>java.lang.Double -&gt; DoubleComparator[precision=1.0E-15] </li>
   *   <li>java.lang.Float -&gt; FloatComparator[precision=1.0E-6] </li>
   *   <li>any comparators previously registered with {@link AbstractObjectAssert#usingComparatorForType(Comparator, Class)} </li>
   *   </ul>
   * </li>
   * </ul>
   * <p>
   * It is possible to change the comparison behavior, among things what you can is:
   * <ul>
   *   <li>Choosing a strict or lenient recursive comparison (lenient being the default which allows to compare different types like {@code Book} and {@code BookDto} </li>
   *   <li>Ignoring fields in the comparison </li>
   *   <li>Specifying comparators to use in the comparison per fields and types</li>
   *   <li>Forcing recursive comparison on classes that have redefined equals (by default overridden equals are used)</li>
   * </ul>
   * <p>
   * Please consult the detailed documentation available here: <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   *
   * @return a new {@link SoftRecursiveComparisonAssert} instance
   */
  public SoftRecursiveComparisonAssert<?> usingRecursiveComparison() {
    return new SoftRecursiveComparisonAssert<>(objectAssert.usingRecursiveComparison(), errorCollector);
  }

  /**
   * Same as {@link #usingRecursiveComparison()} but allows specifying your own {@link RecursiveComparisonConfiguration}.
   *
   * @param recursiveComparisonConfiguration the {@link RecursiveComparisonConfiguration} used in the chained {@link SoftRecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion.
   * @return a new {@link SoftRecursiveComparisonAssert} instance built with the given {@link RecursiveComparisonConfiguration}.
   */
  public SoftRecursiveComparisonAssert<?> usingRecursiveComparison(
      RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    return new SoftRecursiveComparisonAssert<>(objectAssert.usingRecursiveComparison(recursiveComparisonConfiguration), errorCollector);
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
  public SoftObjectAssert<ACTUAL> withFailMessage(String newErrorMessage, Object... args) {
    objectAssert.withFailMessage(newErrorMessage,args);
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
  public SoftObjectAssert<ACTUAL> withFailMessage(Supplier<String> supplier) {
    objectAssert.withFailMessage(supplier);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> withRepresentation(Representation representation) {
    objectAssert.withRepresentation(representation);
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public SoftObjectAssert<ACTUAL> withThreadDumpOnError() {
    objectAssert.withThreadDumpOnError();
    return this;
  }
}
