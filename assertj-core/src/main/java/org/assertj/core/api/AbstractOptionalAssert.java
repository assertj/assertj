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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;
import static org.assertj.core.error.OptionalShouldContain.shouldContainSame;
import static org.assertj.core.error.OptionalShouldContainInstanceOf.shouldContainInstanceOf;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.FieldByFieldComparator;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;

/**
 * Assertions for {@link java.util.Optional}.
 *
 * @author Jean-Christophe Gay
 * @author Nicolai Parlog
 * @author Grzegorz Piwowarek
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <VALUE> type of the value contained in the {@link java.util.Optional}.
 */
// Deprecation is raised by JDK-17. IntelliJ thinks this is redundant when it is not.
@SuppressWarnings({ "deprecation", "RedundantSuppression" })
public abstract class AbstractOptionalAssert<SELF extends AbstractOptionalAssert<SELF, VALUE>, VALUE> extends
    AbstractAssert<SELF, Optional<VALUE>> {

  private ComparisonStrategy optionalValueComparisonStrategy;

  protected AbstractOptionalAssert(Optional<VALUE> actual, Class<?> selfType) {
    super(actual, selfType);
    this.optionalValueComparisonStrategy = StandardComparisonStrategy.instance();
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.Optional}.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.of("something")).isPresent();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.empty()).isPresent();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF isPresent() {
    assertValueIsPresent();
    return myself;
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.Optional}, it's an alias of {@link #isPresent()}.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.of("something")).isNotEmpty();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.empty()).isNotEmpty();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF isNotEmpty() {
    return isPresent();
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is empty.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.empty()).isEmpty();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).isEmpty();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF isEmpty() {
    isNotNull();
    if (actual.isPresent()) throwAssertionError(shouldBeEmpty(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is empty (alias of {@link #isEmpty()}).
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.empty()).isNotPresent();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).isNotPresent();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF isNotPresent() {
    return isEmpty();
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains the given value (alias of {@link #hasValue(Object)}).
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.of("something")).contains("something");
   * assertThat(Optional.of(10)).contains(10);</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).contains("something else");
   * assertThat(Optional.of(20)).contains(10);</code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public SELF contains(VALUE expectedValue) {
    isNotNull();
    checkNotNull(expectedValue);
    if (!actual.isPresent()) throwAssertionError(shouldContain(expectedValue));
    if (!optionalValueComparisonStrategy.areEqual(actual.get(), expectedValue))
      throw Failures.instance().failure(info, shouldContain(actual, expectedValue), actual.get(), expectedValue);
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains a value and gives this value to the given
   * {@link java.util.function.Consumer} for further assertions. Should be used as a way of deeper asserting on the
   * containing object, as further requirement(s) for the value.
   * <p>
   * Assertions will pass :
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
  public SELF hasValueSatisfying(Consumer<VALUE> requirement) {
    assertValueIsPresent();
    requirement.accept(actual.get());
    return myself;
  }

  /**
   * Verifies that the actual {@link Optional} contains a value which satisfies the given {@link Condition}.
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
   * @throws AssertionError       if the actual {@link Optional} is null or empty.
   * @throws AssertionError       if the actual value does not satisfy the given condition.
   * @throws NullPointerException if the given condition is {@code null}.
   * @since 3.6.0
   */
  public SELF hasValueSatisfying(Condition<? super VALUE> condition) {
    assertValueIsPresent();
    conditions.assertIs(info, actual.get(), condition);
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains the given value (alias of {@link #contains(Object)}).
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(Optional.of("something")).hasValue("something");
   * assertThat(Optional.of(10)).contains(10);</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(Optional.of("something")).hasValue("something else");
   * assertThat(Optional.of(20)).contains(10);</code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public SELF hasValue(VALUE expectedValue) {
    return contains(expectedValue);
  }

  /**
   * Verifies that the actual {@link Optional} contains a value that is an instance of the argument.
   * <p>
   * Assertions will pass:
   *
   * <pre><code class='java'> assertThat(Optional.of("something")).containsInstanceOf(String.class)
   *                                     .containsInstanceOf(Object.class);
   *
   * assertThat(Optional.of(10)).containsInstanceOf(Integer.class);</code></pre>
   *
   * Assertion will fail:
   *
   * <pre><code class='java'> assertThat(Optional.of("something")).containsInstanceOf(Integer.class);</code></pre>
   *
   * @param clazz the expected class of the value inside the {@link Optional}.
   * @return this assertion object.
   */
  public SELF containsInstanceOf(Class<?> clazz) {
    assertValueIsPresent();
    if (!clazz.isInstance(actual.get())) throwAssertionError(shouldContainInstanceOf(actual, clazz));
    return myself;
  }

  /**
   * Use field/property by field/property comparison (including inherited fields/properties) instead of relying on
   * actual type A <code>equals</code> method to compare the {@link Optional} value's object for incoming assertion
   * checks. Private fields are included but this can be disabled using {@link Assertions#setAllowExtractingPrivateFields(boolean)}.
   * <p>
   * This can be handy if <code>equals</code> method of the {@link Optional} value's object to compare does not suit
   * you.
   * </p>
   * Note that the comparison is <b>not</b> recursive, if one of the fields/properties is an Object, it will be
   * compared to the other field/property using its <code>equals</code> method.
   * <p>
   * Example:
   *
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   *
   * // Fail if equals has not been overridden in TolkienCharacter as equals default implementation only compares references
   * assertThat(Optional.of(frodo)).contains(frodoClone);
   *
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(Optional.of(frodo)).usingFieldByFieldValueComparator().contains(frodoClone);</code></pre>
   *
   * @return {@code this} assertion object.
   * @deprecated This method is deprecated because it performs a <b>shallow</b> field by field comparison, i.e. elements are compared
   * field by field but the fields are compared with equals, use {@link #get()} chained with {@link AbstractObjectAssert#usingRecursiveComparison()} instead.
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter frodoClone = new TolkienCharacter("Frodo", 33, HOBBIT);
   *
   * // frodo and frodoClone are equals when doing a field by field comparison.
   * assertThat(Optional.of(frodo)).get().usingRecursiveComparison()
   *                               .isEqualTo(frodoClone);</code></pre>
   * <br>See <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>
   */
  @Deprecated
  @CheckReturnValue
  @SuppressWarnings({ "DeprecatedIsStillUsed", "deprecation" })
  public SELF usingFieldByFieldValueComparator() {
    return usingValueComparator(new FieldByFieldComparator());
  }

  /**
   * Use given custom comparator instead of relying on actual type A <code>equals</code> method to compare the
   * {@link Optional} value's object for incoming assertion checks.
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
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  @CheckReturnValue
  public SELF usingValueComparator(Comparator<? super VALUE> customComparator) {
    optionalValueComparisonStrategy = new ComparatorBasedComparisonStrategy(customComparator);
    return myself;
  }

  /**
   * Revert to standard comparison for incoming assertion {@link Optional} value checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling
   * {@link #usingValueComparator(Comparator)}.
   *
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  public SELF usingDefaultValueComparator() {
    // fall back to default strategy to compare actual with other objects.
    optionalValueComparisonStrategy = StandardComparisonStrategy.instance();
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} contains the instance given as an argument (i.e. it must be the
   * same instance).
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'> String someString = "something";
   * assertThat(Optional.of(someString)).containsSame(someString);
   *
   * // Java will create the same 'Integer' instance when boxing small ints
   * assertThat(Optional.of(10)).containsSame(10);</code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'> // not even equal:
   * assertThat(Optional.of("something")).containsSame("something else");
   * assertThat(Optional.of(20)).containsSame(10);
   *
   * // equal but not the same:
   * assertThat(Optional.of(new String("something"))).containsSame(new String("something"));
   * assertThat(Optional.of(new Integer(10))).containsSame(new Integer(10));</code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.Optional}.
   * @return this assertion object.
   */
  public SELF containsSame(VALUE expectedValue) {
    isNotNull();
    checkNotNull(expectedValue);
    if (!actual.isPresent()) throwAssertionError(shouldContain(expectedValue));
    if (actual.get() != expectedValue) throwAssertionError(shouldContainSame(actual, expectedValue));
    return myself;
  }

  /**
   * Call {@link Optional#flatMap(Function) flatMap} on the {@code Optional} under test, assertions chained afterwards are performed on the {@code Optional} resulting from the flatMap call.
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
   * @param <U> the type wrapped in the {@link Optional} after the {@link Optional#flatMap(Function) flatMap} operation.
   * @param mapper the {@link Function} to use in the {@link Optional#flatMap(Function) flatMap} operation.
   * @return a new {@link AbstractOptionalAssert} for assertions chaining on the flatMap of the Optional.
   * @throws AssertionError if the actual {@link Optional} is null.
   * @since 3.6.0
   */
  @CheckReturnValue
  public <U> AbstractOptionalAssert<?, U> flatMap(Function<? super VALUE, Optional<U>> mapper) {
    isNotNull();
    return assertThat(actual.flatMap(mapper));
  }

  /**
   * Call {@link Optional#map(Function) map} on the {@code Optional} under test, assertions chained afterwards are performed on the {@code Optional} resulting from the map call.
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
   * @param <U> the type wrapped in the {@link Optional} after the {@link Optional#map(Function) map} operation.
   * @param mapper the {@link Function} to use in the {@link Optional#map(Function) map} operation.
   * @return a new {@link AbstractOptionalAssert} for assertions chaining on the map of the Optional.
   * @throws AssertionError if the actual {@link Optional} is null.
   * @since 3.6.0
   */
  @CheckReturnValue
  public <U> AbstractOptionalAssert<?, U> map(Function<? super VALUE, ? extends U> mapper) {
    isNotNull();
    return assertThat(actual.map(mapper));
  }

  /**
   * Verifies that the actual {@link Optional} is not {@code null} and not empty and returns an Object assertion
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
   * @throws AssertionError if the actual {@link Optional} is null.
   * @throws AssertionError if the actual {@link Optional} is empty.
   * @see #get(InstanceOfAssertFactory)
   * @since 3.9.0
   */
  @CheckReturnValue
  public AbstractObjectAssert<?, VALUE> get() {
    return internalGet();
  }

  /**
   * Verifies that the actual {@link Optional} is not {@code null} and not empty and returns an new assertion instance
   * to chain assertions on the optional value.
   * <p>
   * The {@code assertFactory} parameter allows to specify an {@link InstanceOfAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Wrapping the given {@link InstanceOfAssertFactory} with {@link Assertions#as(InstanceOfAssertFactory)} makes the
   * assertion more readable.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(Optional.of("frodo")).get(as(InstanceOfAssertFactories.STRING)).startsWith("fro");
   *
   * // assertion does not succeed because frodo is not an Integer
   * assertThat(Optional.of("frodo")).get(as(InstanceOfAssertFactories.INTEGER)).isZero();</code></pre>
   *
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the value of the Optional
   * @throws AssertionError if the actual {@link Optional} is null
   * @throws AssertionError if the actual {@link Optional} is empty
   * @throws NullPointerException if the given factory is {@code null}
   * @since 3.14.0
   */
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT get(InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return internalGet().asInstanceOf(assertFactory);
  }

  /**
   * Enable using a recursive field by field comparison strategy when calling the chained {@link RecursiveComparisonAssert},
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
   *   <li> different types of iterable can be compared by default as in the example, this can be turned off by calling {@link RecursiveComparisonAssert#withStrictTypeChecking() withStrictTypeChecking}.</li>
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
   * @return a new {@link RecursiveComparisonAssert} instance
   * @see RecursiveComparisonConfiguration RecursiveComparisonConfiguration
   */
  @Override
  @Beta
  public RecursiveComparisonAssert<?> usingRecursiveComparison() {
    // overridden for javadoc and to make this method public
    return super.usingRecursiveComparison();
  }

  /**
   * Same as {@link #usingRecursiveComparison()} but allows to specify your own {@link RecursiveComparisonConfiguration}.
   * @param recursiveComparisonConfiguration the {@link RecursiveComparisonConfiguration} used in the chained {@link RecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion.
   *
   * @return a new {@link RecursiveComparisonAssert} instance built with the given {@link RecursiveComparisonConfiguration}.
   */
  @Override
  public RecursiveComparisonAssert<?> usingRecursiveComparison(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    // overridden for javadoc and to make this method public
    return super.usingRecursiveComparison(recursiveComparisonConfiguration);
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
  @Override
  public RecursiveAssertionAssert usingRecursiveAssertion() {
    return super.usingRecursiveAssertion();
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
  @Override
  public RecursiveAssertionAssert usingRecursiveAssertion(RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    return super.usingRecursiveAssertion(recursiveAssertionConfiguration);
  }

  private AbstractObjectAssert<?, VALUE> internalGet() {
    isPresent();
    return assertThat(actual.get()).withAssertionState(myself);
  }

  private void checkNotNull(Object expectedValue) {
    checkArgument(expectedValue != null, "The expected value should not be <null>.");
  }

  private void assertValueIsPresent() {
    isNotNull();
    if (!actual.isPresent()) throwAssertionError(shouldBePresent(actual));
  }

}
