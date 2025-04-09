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

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.description.Description.mostRelevantDescription;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOf;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.description.Description;
import org.assertj.core.groups.Tuple;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Base class for all implementations of assertions for {@link Object}s.
 *
 * @param <SELF>   the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *                 target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *                 for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 * @author Libor Ondrusek
 */
// suppression of deprecation works in Eclipse to hide warning for the deprecated classes in the imports
public abstract class AbstractObjectAssert<SELF extends AbstractObjectAssert<SELF, ACTUAL>, ACTUAL>
    extends AbstractAssert<SELF, ACTUAL> {

  private Map<String, Comparator<?>> comparatorsByPropertyOrField = new TreeMap<>();
  private TypeComparators comparatorsByType;

  public AbstractObjectAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  @Override
  @CheckReturnValue
  public SELF as(Description description) {
    return super.as(description);
  }

  @Override
  @CheckReturnValue
  public SELF as(String description, Object... args) {
    return super.as(description, args);
  }

  /**
   * Asserts that the actual object has no null fields or properties (inherited ones are taken into account).
   * <p>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p>
   * Private fields are checked, but this can be disabled using {@link Assertions#setAllowComparingPrivateFields(boolean)},
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
  public SELF hasNoNullFieldsOrProperties() {
    objects.assertHasNoNullFieldsOrPropertiesExcept(info, actual);
    return myself;
  }

  /**
   * Asserts that the actual object has only null fields or properties.
   * <p>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p>
   * Private fields are checked, but this can be disabled using {@link Assertions#setAllowComparingPrivateFields(boolean)},
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
  public SELF hasAllNullFieldsOrProperties() {
    objects.assertHasAllNullFieldsOrPropertiesExcept(info, actual);
    return myself;
  }

  /**
   * Asserts that the actual object has no null fields or properties <b>except for the given ones</b>
   * (inherited ones are taken into account).
   * <p>
   * If an object has a field and a property with the same name, the property value will be used over the field.
   * <p>
   * Private fields are checked, but this can be disabled using {@link Assertions#setAllowComparingPrivateFields(boolean)},
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
   * @since 2.5.0 / 3.5.0
   */
  public SELF hasNoNullFieldsOrPropertiesExcept(String... propertiesOrFieldsToIgnore) {
    objects.assertHasNoNullFieldsOrPropertiesExcept(info, actual, propertiesOrFieldsToIgnore);
    return myself;
  }

  /**
   * Asserts that the actual object has only null fields or properties <b>except for the given ones</b>
   * (inherited ones are taken into account).
   * <p>
   * If an object has a field and a property with the same name, the property value will be user over the field.
   * <p>
   * Private fields are checked, but this can be disabled using {@link Assertions#setAllowComparingPrivateFields(boolean)},
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
  public SELF hasAllNullFieldsOrPropertiesExcept(String... propertiesOrFieldsToIgnore) {
    objects.assertHasAllNullFieldsOrPropertiesExcept(info, actual, propertiesOrFieldsToIgnore);
    return myself;
  }

  // lazy init TypeComparators
  protected TypeComparators getComparatorsByType() {
    if (comparatorsByType == null) comparatorsByType = defaultTypeComparators();
    return comparatorsByType;
  }

  /**
   * Allows to set a specific comparator to compare properties or fields with the given type.
   * A typical usage is for comparing fields of numeric type at a given precision.
   * <p>
   * The comparators specified by this method are used in the {@link #usingRecursiveComparison() recursive comparison},
   * the {@link #returns(Object, Function)} and {@link #doesNotReturn(Object, Function)} assertions.
   * <p>
   * Note that for the recursive comparison, it is more idiomatic to use {@link RecursiveComparisonAssert#withComparatorForType(Comparator, Class)} or {@link RecursiveComparisonAssert#withEqualsForType(BiPredicate, Class)}.
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
   * @param type       the {@link Class} of the type the comparator should be used for
   * @param <T>        the type of objects that the comparator should be used for
   * @return {@code this} assertions object
   * @see #returns(Object, Function)
   * @see #doesNotReturn(Object, Function)
   */
  @CheckReturnValue
  public <T> SELF usingComparatorForType(Comparator<? super T> comparator, Class<T> type) {
    getComparatorsByType().registerComparator(type, comparator);
    return myself;
  }

  /**
   * Asserts that the actual object has the specified field or property. Static and synthetic fields are ignored since 3.19.0.
   * <p>
   * Private fields are matched by default but this can be changed by calling {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
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
   * @throws AssertionError           if the actual object is {@code null}.
   * @throws IllegalArgumentException if name is {@code null}.
   * @throws AssertionError           if the actual object does not have the given field/property
   */
  public SELF hasFieldOrProperty(String name) {
    objects.assertHasFieldOrProperty(info, actual, name);
    return myself;
  }

  /**
   * Asserts that the actual object has the specified field or property with the given value. Static and synthetic fields are ignored since 3.19.0.
   * <p>
   * Private fields are matched by default but this can be changed by calling {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
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
   * @param name  the field/property name to check
   * @param value the field/property expected value
   * @return {@code this} assertion object.
   * @throws AssertionError           if the actual object is {@code null}.
   * @throws IllegalArgumentException if name is {@code null}.
   * @throws AssertionError           if the actual object does not have the given field/property
   * @throws AssertionError           if the actual object has the given field/property but not with the expected value
   * @see AbstractObjectAssert#hasFieldOrProperty(java.lang.String)
   */
  public SELF hasFieldOrPropertyWithValue(String name, Object value) {
    objects.assertHasFieldOrPropertyWithValue(info, actual, name, value);
    return myself;
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
   * @throws AssertionError           if the actual object is {@code null}.
   * @throws IllegalArgumentException if expectedFieldNames is {@code null}.
   * @throws AssertionError           if the actual object does not have the expected fields (without extra ones)
   * @since 3.19.0
   */
  public SELF hasOnlyFields(String... expectedFieldNames) {
    objects.assertHasOnlyFields(info, actual, expectedFieldNames);
    return myself;
  }

  /**
   * Extracts the values of given fields/properties from the object under test into a list, this new list becoming
   * the object under test.
   * <p>
   * If you extract "id", "name" and "email" fields/properties then the list will contain the id, name and email values
   * of the object under test, you can then perform list assertions on the extracted values.
   * <p>
   * If the object under test is a {@link Map} with {@link String} keys, extracting will extract values matching the given fields/properties.
   * <p>
   * Nested fields/properties are supported, specifying "address.street.number" is equivalent to:
   * <pre><code class='java'> // "address.street.number" corresponding to pojo properties
   * actual.getAddress().getStreet().getNumber();</code></pre>
   * or if address is a {@link Map}:
   * <pre><code class='java'> // "address" is a Map property (that is getAddress() returns a Map)
   * actual.getAddress().get("street").getNumber();</code></pre>
   * <p>
   * Private fields can be extracted unless you call {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
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
   * @throws IntrospectionError if one of the given name does not match a field or property
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> extracting(String... propertiesOrFields) {
    Tuple values = byName(propertiesOrFields).apply(actual);
    String extractedPropertiesOrFieldsDescription = extractedDescriptionOf(propertiesOrFields);
    String description = mostRelevantDescription(info.description(), extractedPropertiesOrFieldsDescription);
    return newListAssertInstance(values.toList()).withAssertionState(myself).as(description);
  }

  /**
   * Extracts the value of given field/property from the object under test, the extracted value becoming the new object under test.
   * <p>
   * If the object under test is a {@link Map}, the {@code propertyOrField} parameter is used as a key to the map.
   * <p>
   * Nested fields/properties are supported, specifying "address.street.number" is equivalent to:
   * <pre><code class='java'> // "address.street.number" corresponding to pojo properties
   * actual.getAddress().getStreet().getNumber();</code></pre>
   * or if address is a {@link Map}:
   * <pre><code class='java'> // "address" is a Map property (that is getAddress() returns a Map)
   * actual.getAddress().get("street").getNumber();</code></pre>
   * <p>
   * Private field can be extracted unless you call {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
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
   * // To get String assertions, use {@link #extracting(String, InstanceOfAssertFactory)}:
   * assertThat(frodo).extracting(&quot;name&quot;, as(InstanceOfAssertFactories.STRING))
   *                  .startsWith(&quot;Fro&quot;);</code></pre>
   * <p>
   * A property with the given name is looked for first, if it doesn't exist then a field with the given name is looked
   * for, if the field is not accessible (i.e. does not exist) an {@link IntrospectionError} is thrown.
   *
   * @param propertyOrField the property/field to extract from the initial object under test
   * @return a new {@link ObjectAssert} instance whose object under test is the extracted property/field value
   * @throws IntrospectionError if one of the given name does not match a field or property
   * @see #extracting(String, InstanceOfAssertFactory)
   * @since 3.13.0
   */
  @CheckReturnValue
  public AbstractObjectAssert<?, ?> extracting(String propertyOrField) {
    return super.extracting(propertyOrField, this::newObjectAssert);
  }

  /**
   * Extracts the value of given field/property from the object under test, the extracted value becoming the new object under test.
   * <p>
   * If the object under test is a {@link Map}, the {@code propertyOrField} parameter is used as a key to the map.
   * <p>
   * Nested field/property is supported, specifying "address.street.number" is equivalent to get the value
   * corresponding to actual.getAddress().getStreet().getNumber()
   * <p>
   * Private field can be extracted unless you call {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   * <p>
   * The {@code assertFactory} parameter allows to specify an {@link InstanceOfAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Wrapping the given {@link InstanceOfAssertFactory} with {@link Assertions#as(InstanceOfAssertFactory)} makes the
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
   * @param <ASSERT>        the type of the resulting {@code Assert}
   * @param propertyOrField the property/field to extract from the initial object under test
   * @param assertFactory   the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance whose object under test is the extracted property/field value
   * @throws NullPointerException if the given factory is {@code null}
   * @throws IntrospectionError   if one of the given name does not match a field or property
   * @since 3.14.0
   */
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(String propertyOrField,
                                                                 InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return super.extracting(propertyOrField, this::newObjectAssert).asInstanceOf(assertFactory);
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
  @CheckReturnValue
  @SafeVarargs
  public final <T> AbstractListAssert<?, List<? extends T>, T, ObjectAssert<T>> extracting(Function<? super ACTUAL, ? extends T>... extractors) {
    return extractingForProxy(extractors);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected <T> AbstractListAssert<?, List<? extends T>, T, ObjectAssert<T>> extractingForProxy(Function<? super ACTUAL, ? extends T>[] extractors) {
    requireNonNull(extractors, shouldNotBeNull("extractors")::create);
    List<T> values = Stream.of(extractors)
                           .map(extractor -> extractor.apply(actual))
                           .collect(toList());
    return newListAssertInstance(values).withAssertionState(myself);
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
   * // To get String assertions, use {@link #extracting(Function, InstanceOfAssertFactory)}:
   * assertThat(frodo).extracting(TolkienCharacter::getName, as(InstanceOfAssertFactories.STRING))
   *                  .startsWith(&quot;Fro&quot;);</code></pre>
   *
   * @param <T>       the expected extracted value type.
   * @param extractor the extractor function used to extract the value from the object under test.
   * @return a new {@link ObjectAssert} instance whose object under test is the extracted value
   * @see #extracting(Function, InstanceOfAssertFactory)
   * @since 3.11.0
   */
  @CheckReturnValue
  public <T> AbstractObjectAssert<?, T> extracting(Function<? super ACTUAL, T> extractor) {
    return super.extracting(extractor, this::newObjectAssert);
  }

  /**
   * Uses the given {@link Function} to extract a value from the object under test, the extracted value becoming the new object under test.
   * <p>
   * Note that since the value is extracted as an Object, only Object assertions can be chained after extracting.
   * <p>
   * The {@code assertFactory} parameter allows to specify an {@link InstanceOfAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Wrapping the given {@link InstanceOfAssertFactory} with {@link Assertions#as(InstanceOfAssertFactory)} makes the
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
   * @param <T>           the expected extracted value type
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @param extractor     the extractor function used to extract the value from the object under test
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance whose object under test is the extracted value
   * @throws NullPointerException if the given factory is {@code null}
   * @since 3.14.0
   */
  @CheckReturnValue
  public <T, ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(Function<? super ACTUAL, T> extractor,
                                                                    InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return super.extracting(extractor, this::newObjectAssert).asInstanceOf(assertFactory);
  }

  /**
   * Verifies that the object under test returns the given expected value from the given {@link Function},
   * a typical usage is to pass a method reference to assert object's property.
   * <p>
   * Wrapping the given {@link Function} with {@link Assertions#from(Function)} makes the assertion more readable.
   * <p>
   * The assertion supports custom comparators, configurable with {@link #usingComparatorForType(Comparator, Class)}.
   * <p>
   * Example:
   * <pre><code class="java"> // from is not mandatory but it makes the assertions more readable
   * assertThat(frodo).returns("Frodo", from(TolkienCharacter::getName))
   *                  .returns("Frodo", TolkienCharacter::getName) // no from :(
   *                  .returns(HOBBIT, from(TolkienCharacter::getRace));</code></pre>
   *
   * @param expected the value the object under test method's call should return.
   * @param from     {@link Function} used to acquire the value to test from the object under test. Must not be {@code null}
   * @param <T>      the expected value type the given {@code method} returns.
   * @return {@code this} assertion object.
   * @throws AssertionError       if {@code actual} is {@code null}
   * @throws NullPointerException if given {@code from} function is null
   * @see #usingComparatorForType(Comparator, Class)
   */
  public <T> SELF returns(T expected, Function<ACTUAL, T> from) {
    requireNonNull(from, "The given getter method/Function must not be null");
    isNotNull();
    Objects objects = getComparatorBasedObjectAssertions(expected);
    objects.assertEqual(info, from.apply(actual), expected);
    return myself;
  }

  /**
   * Verifies that the object under test does not return the given expected value from the given {@link Function},
   * a typical usage is to pass a method reference to assert object's property.
   * <p>
   * Wrapping the given {@link Function} with {@link Assertions#from(Function)} makes the assertion more readable.
   * <p>
   * The assertion supports custom comparators, configurable with {@link #usingComparatorForType(Comparator, Class)}.
   * <p>
   * Example:
   * <pre><code class="java"> // from is not mandatory but it makes the assertions more readable
   * assertThat(frodo).doesNotReturn("Bilbo", from(TolkienCharacter::getName))
   *                  .doesNotReturn("Bilbo", TolkienCharacter::getName) // no from :(
   *                  .doesNotReturn(null, from(TolkienCharacter::getRace));</code></pre>
   *
   * @param expected the value the object under test method's call should not return.
   * @param from     {@link Function} used to acquire the value to test from the object under test. Must not be {@code null}
   * @param <T>      the expected value type the given {@code method} returns.
   * @return {@code this} assertion object.
   * @throws AssertionError       if {@code actual} is {@code null}
   * @throws NullPointerException if given {@code from} function is null
   * @see #usingComparatorForType(Comparator, Class)
   * @since 3.22.0
   */
  public <T> SELF doesNotReturn(T expected, Function<ACTUAL, T> from) {
    requireNonNull(from, "The given getter method/Function must not be null");
    isNotNull();
    Objects objects = getComparatorBasedObjectAssertions(expected);
    objects.assertNotEqual(info, from.apply(actual), expected);
    return myself;
  }

  private Objects getComparatorBasedObjectAssertions(Object value) {
    if (value == null) return objects;
    Class<?> type = value.getClass();
    TypeComparators comparatorsByType = getComparatorsByType();
    if (comparatorsByType.hasComparatorForType(type)) {
      return new Objects(new ComparatorBasedComparisonStrategy(comparatorsByType.getComparatorForType(type)));
    }
    return objects;
  }

  /**
   * Enable using a recursive field by field comparison strategy when calling the chained {@link RecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion.
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
   * <li>actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call {@link RecursiveComparisonAssert#withStrictTypeChecking() withStrictTypeChecking()} to change that behavior). </li>
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
   * @return a new {@link RecursiveComparisonAssert} instance
   */
  @Override
  public RecursiveComparisonAssert<?> usingRecursiveComparison() {
    // overridden for javadoc
    return super.usingRecursiveComparison();
  }

  /**
   * Same as {@link #usingRecursiveComparison()} but allows to specify your own {@link RecursiveComparisonConfiguration}.
   *
   * @param recursiveComparisonConfiguration the {@link RecursiveComparisonConfiguration} used in the chained {@link RecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion.
   * @return a new {@link RecursiveComparisonAssert} instance built with the given {@link RecursiveComparisonConfiguration}.
   */
  @Override
  public RecursiveComparisonAssert<?> usingRecursiveComparison(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    return super.usingRecursiveComparison(recursiveComparisonConfiguration).withTypeComparators(comparatorsByType);
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
  @Override
  public RecursiveAssertionAssert usingRecursiveAssertion() {
    return super.usingRecursiveAssertion();
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
  @Override
  public RecursiveAssertionAssert usingRecursiveAssertion(RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    return super.usingRecursiveAssertion(recursiveAssertionConfiguration);
  }

  // override for proxyable friendly AbstractObjectAssert
  protected <T> AbstractObjectAssert<?, T> newObjectAssert(T objectUnderTest) {
    return new ObjectAssert<>(objectUnderTest);
  }

  @SuppressWarnings({ "rawtypes" })
  @Override
  SELF withAssertionState(AbstractAssert assertInstance) {
    if (assertInstance instanceof AbstractObjectAssert objectAssert) {
      return (SELF) super.withAssertionState(assertInstance).withTypeComparator(objectAssert.comparatorsByType)
                                                            .withComparatorByPropertyOrField(objectAssert.comparatorsByPropertyOrField);
    }
    return super.withAssertionState(assertInstance);
  }

  SELF withTypeComparator(TypeComparators comparatorsByType) {
    this.comparatorsByType = comparatorsByType;
    return myself;
  }

  SELF withComparatorByPropertyOrField(Map<String, Comparator<?>> comparatorsToPropagate) {
    this.comparatorsByPropertyOrField = comparatorsToPropagate;
    return myself;
  }

}
