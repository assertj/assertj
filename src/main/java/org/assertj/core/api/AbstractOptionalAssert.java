/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
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

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.FieldByFieldComparator;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.util.CheckReturnValue;

/**
 * Assertions for {@link java.util.Optional}.
 *
 * @param <SELF> the "self" type of this assertion class.
 * @param <VALUE> type of the value contained in the {@link java.util.Optional}.
 *
 * @author Jean-Christophe Gay
 * @author Nicolai Parlog
 * @author Grzegorz Piwowarek
 */
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
      throwAssertionError(shouldContain(actual, expectedValue));
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
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError       if the actual value does not satisfy the given condition.
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
   * checks. Private fields are included but this can be disabled using
   * {@link Assertions#setAllowExtractingPrivateFields(boolean)}.
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
   */
  @CheckReturnValue
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
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
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
   * Verifies that the actual {@link Optional} is not {@code null} and not empty,
   * and returns an Object assertion, to allow chaining of object-specific
   * assertions from this call.
   * <p>
   * Note that it is only possible to return Object assertions after calling this method due to java generics limitations.  
   * <p>
   * Example:
   * <pre><code class='java'> TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter sam = new TolkienCharacter("Sam", 38, null);
   *
   * // assertion succeeds since all frodo's fields are set
   * assertThat(Optional.of(frodo)).get().hasNoNullFields();
   *
   * // assertion does not succeed because sam does not have its race set
   * assertThat(Optional.of(sam)).get().hasNoNullFields();</code></pre>
   *
   * @return a new {@link AbstractObjectAssert} for assertions chaining on the value of the Optional.
   * @throws AssertionError if the actual {@link Optional} is null.
   * @throws AssertionError if the actual {@link Optional} is empty.
   * @since 3.9.0
   */
  @CheckReturnValue
  public AbstractObjectAssert<?, VALUE> get() {
    isPresent();
    return assertThat(actual.get());
  }

  private void checkNotNull(Object expectedValue) {
    checkArgument(expectedValue != null, "The expected value should not be <null>.");
  }

  private void assertValueIsPresent() {
    isNotNull();
    if (!actual.isPresent()) throwAssertionError(shouldBePresent(actual));
  }

}
