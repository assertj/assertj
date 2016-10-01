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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.util.Strings.formatIfArgs;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.Map;

import org.assertj.core.description.Description;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.MessageFormatter;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all assertions.
 *
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractAssert<S extends AbstractAssert<S, A>, A> implements Assert<S, A> {

  @VisibleForTesting
  Objects objects = Objects.instance();

  @VisibleForTesting
  Conditions conditions = Conditions.instance();

  @VisibleForTesting
  public final WritableAssertionInfo info;

  // visibility is protected to allow us write custom assertions that need access to actual
  @VisibleForTesting
  protected final A actual;
  protected final S myself;

  private static Representation customRepresentation = null;

  // we prefer not to use Class<? extends S> selfType because it would force inherited
  // constructor to cast with a compiler warning
  // let's keep compiler warning internal (when we can) and not expose them to our end users.
  @SuppressWarnings("unchecked")
  public AbstractAssert(A actual, Class<?> selfType) {
    myself = (S) selfType.cast(this);
    this.actual = actual;
    info = new WritableAssertionInfo(customRepresentation);
  }

  /**
   * Exposes the {@link WritableAssertionInfo} used in the current assertion for better extensibility.</br> When writing
   * your own assertion class, you can use the returned {@link WritableAssertionInfo} to change the error message and
   * still keep the description set by the assertion user.
   *
   * @return the {@link WritableAssertionInfo} used in the current assertion
   */
  public WritableAssertionInfo getWritableAssertionInfo() {
    return info;
  }

  /**
   * Utility method to ease writing custom assertions classes using {@link String#format(String, Object...)} specifiers
   * in error message.
   * <p>
   * Moreover, this method honors any description set with {@link #as(String, Object...)} or overridden error message
   * defined by the user with {@link #overridingErrorMessage(String, Object...)}.
   * <p>
   * Example :
   * <pre><code class='java'> public TolkienCharacterAssert hasName(String name) {
   *   // check that actual TolkienCharacter we want to make assertions on is not null.
   *   isNotNull();
   *
   *   // check condition
   *   if (!actual.getName().equals(name)) {
   *     failWithMessage(&quot;Expected character's name to be &lt;%s&gt; but was &lt;%s&gt;&quot;, name, actual.getName());
   *   }
   *
   *   // return the current assertion for method chaining
   *   return this;
   * }</code></pre>
   *
   * @param errorMessage the error message to format
   * @param arguments the arguments referenced by the format specifiers in the errorMessage string.
   */
  protected void failWithMessage(String errorMessage, Object... arguments) {
    AssertionError failureWithOverriddenErrorMessage = Failures.instance().failureIfErrorMessageIsOverridden(info);
    if (failureWithOverriddenErrorMessage != null) throw failureWithOverriddenErrorMessage;
    String description = MessageFormatter.instance().format(info.description(), info.representation(), "");
    throw new AssertionError(description + String.format(errorMessage, arguments));
  }

  /**
   * Utility method to throw an {@link AssertionError} given a {@link BasicErrorMessageFactory}.
   * <p>
   * Instead of writing ...
   *
   * <pre><code class='java'> throw Failures.instance().failure(info, ShouldBePresent.shouldBePresent());</code></pre>
   * ... you can simply write :
   *
   * <pre><code class='java'> throwAssertionError(info, ShouldBePresent.shouldBePresent());</code></pre>
   *
   * @param errorMessageFactory used to define the error message.
   * @return an {@link AssertionError} with a message corresponding to the given {@link BasicErrorMessageFactory}.
   */
  protected void throwAssertionError(ErrorMessageFactory errorMessageFactory) {
    throw Failures.instance().failure(info, errorMessageFactory);
  }

  /** {@inheritDoc} */
  @Override
  public S as(String description, Object... args) {
    return describedAs(description, args);
  }

  /** {@inheritDoc} */
  @Override
  public S as(Description description) {
    return describedAs(description);
  }

  /**
   * Use hexadecimal object representation instead of standard representation in error messages.
   * <p>
   * It can be useful when comparing UNICODE characters - many unicode chars have duplicate characters assigned, it is
   * thus impossible to find differences from the standard error message:
   * <p>
   * With standard message:
   * <pre><code class='java'> assertThat("µµµ").contains("μμμ");
   *
   * java.lang.AssertionError:
   * Expecting:
   *   &lt;"µµµ"&gt;
   * to contain:
   *   &lt;"μμμ"&gt;</code></pre>
   *
   * With Hexadecimal message:
   * <pre><code class='java'> assertThat("µµµ").inHexadecimal().contains("μμμ");
   *
   * java.lang.AssertionError:
   * Expecting:
   *   &lt;"['00B5', '00B5', '00B5']"&gt;
   * to contain:
   *   &lt;"['03BC', '03BC', '03BC']"&gt;</code></pre>
   *
   * @return {@code this} assertion object.
   */
  protected S inHexadecimal() {
    info.useHexadecimalRepresentation();
    return myself;
  }

  /**
   * Use binary object representation instead of standard representation in error messages.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(1).inBinary().isEqualTo(2);
   *
   * org.junit.ComparisonFailure:
   * Expected :0b00000000_00000000_00000000_00000010
   * Actual   :0b00000000_00000000_00000000_00000001</code></pre>
   *
   * @return {@code this} assertion object.
   */
  protected S inBinary() {
    info.useBinaryRepresentation();
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S describedAs(String description, Object... args) {
    info.description(description, args);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S describedAs(Description description) {
    info.description(description);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isEqualTo(Object expected) {
    objects.assertEqual(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotEqualTo(Object other) {
    objects.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public S isNotNull() {
    objects.assertNotNull(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isSameAs(Object expected) {
    objects.assertSame(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotSameAs(Object other) {
    objects.assertNotSame(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isIn(Object... values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotIn(Object... values) {
    objects.assertIsNotIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isIn(Iterable<?> values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotIn(Iterable<?> values) {
    objects.assertIsNotIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S is(Condition<? super A> condition) {
    conditions.assertIs(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNot(Condition<? super A> condition) {
    conditions.assertIsNot(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S has(Condition<? super A> condition) {
    conditions.assertHas(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S doesNotHave(Condition<? super A> condition) {
    conditions.assertDoesNotHave(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isInstanceOf(Class<?> type) {
    objects.assertIsInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isInstanceOfAny(Class<?>... types) {
    objects.assertIsInstanceOfAny(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotInstanceOf(Class<?> type) {
    objects.assertIsNotInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotInstanceOfAny(Class<?>... types) {
    objects.assertIsNotInstanceOfAny(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasSameClassAs(Object other) {
    objects.assertHasSameClassAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasToString(String expectedToString) {
    objects.assertHasToString(info, actual, expectedToString);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S doesNotHaveSameClassAs(Object other) {
    objects.assertDoesNotHaveSameClassAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isExactlyInstanceOf(Class<?> type) {
    objects.assertIsExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotExactlyInstanceOf(Class<?> type) {
    objects.assertIsNotExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isOfAnyClassIn(Class<?>... types) {
    objects.assertIsOfAnyClassIn(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S isNotOfAnyClassIn(Class<?>... types) {
    objects.assertIsNotOfAnyClassIn(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public AbstractBigDecimalAssert<?> asBigDecimal() {
    objects.assertIsInstanceOf(info, actual, BigDecimal.class);
    return Assertions.assertThat((BigDecimal) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractBooleanAssert<?> asBoolean() {
    objects.assertIsInstanceOf(info, actual, Boolean.class);
    return Assertions.assertThat((Boolean) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractBooleanArrayAssert<?> asBooleanArray() {
    objects.assertIsInstanceOf(info, actual, boolean[].class);
    return Assertions.assertThat((boolean[]) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractByteAssert<?> asByte() {
    objects.assertIsInstanceOf(info, actual, Byte.class);
    return Assertions.assertThat((Byte) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractByteArrayAssert<?> asByteArray() {
    objects.assertIsInstanceOf(info, actual, byte[].class);
    return Assertions.assertThat((byte[]) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractCharacterAssert<?> asCharacter() {
    objects.assertIsInstanceOf(info, actual, Character.class);
    return Assertions.assertThat((Character) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractCharArrayAssert<?> asCharArray() {
    objects.assertIsInstanceOf(info, actual, char[].class);
    return Assertions.assertThat((char[]) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractClassAssert<?> asClass() {
    objects.assertIsInstanceOf(info, actual, Class.class);
    return Assertions.assertThat((Class<?>) actual);
  }

  /** {@inheritDoc} */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public <T extends Comparable<? super T>> AbstractComparableAssert<?, T> asComparable(Class<T> comparableClass) {
    objects.assertIsInstanceOf(info, actual, Comparable.class);
    return new GenericComparableAssert((Comparable<T>) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractDateAssert<?> asDate() {
    objects.assertIsInstanceOf(info, actual, Date.class);
    return Assertions.assertThat((Date) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractDoubleAssert<?> asDouble() {
    objects.assertIsInstanceOf(info, actual, Double.class);
    return Assertions.assertThat((Double) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractDoubleArrayAssert<?> asDoubleArray() {
    objects.assertIsInstanceOf(info, actual, double[].class);
    return Assertions.assertThat((double[]) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractFileAssert<?> asFile() {
    objects.assertIsInstanceOf(info, actual, File.class);
    return Assertions.assertThat((File) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractFloatAssert<?> asFloat() {
    objects.assertIsInstanceOf(info, actual, Float.class);
    return Assertions.assertThat((Float) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractFloatArrayAssert<?> asFloatArray() {
    objects.assertIsInstanceOf(info, actual, float[].class);
    return Assertions.assertThat((float[]) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractInputStreamAssert<?, ?> asInputStream() {
    objects.assertIsInstanceOf(info, actual, InputStream.class);
    return Assertions.assertThat((InputStream) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractIntegerAssert<?> asInteger() {
    objects.assertIsInstanceOf(info, actual, Integer.class);
    return Assertions.assertThat((Integer) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractIntArrayAssert<?> asIntArray() {
    objects.assertIsInstanceOf(info, actual, int[].class);
    return Assertions.assertThat((int[]) actual);
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public AbstractIterableAssert<?, Iterable<? extends Object>, Object, ObjectAssert<Object>> asIterable() {
    objects.assertIsInstanceOf(info, actual, Iterable.class);
    return Assertions.assertThat((Iterable<Object>) actual);
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public AbstractIterableAssert<?, Iterable<? extends Object>, Object, ObjectAssert<Object>> asIterator() {
    objects.assertIsInstanceOf(info, actual, Iterator.class);
    return Assertions.assertThat((Iterator<Object>) actual);
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> asList() {
    objects.assertIsInstanceOf(info, actual, List.class);
    return new ListAssert<>((List<Object>) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractLongAssert<?> asLong() {
    objects.assertIsInstanceOf(info, actual, Long.class);
    return Assertions.assertThat((Long) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractLongArrayAssert<?> asLongArray() {
    objects.assertIsInstanceOf(info, actual, long[].class);
    return Assertions.assertThat((long[]) actual);
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public MapAssert<Object, Object> asMap() {
    objects.assertIsInstanceOf(info, actual, Map.class);
    return Assertions.assertThat((Map<Object, Object>) actual);
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public <T> AbstractObjectArrayAssert<?, T> asArrayOf(Class<T> elementType) {
    objects.assertIsInstanceOf(info, actual, Object[].class);
    return Assertions.assertThat((T[]) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractPathAssert<?> asPath() {
    objects.assertIsInstanceOf(info, actual, Path.class);
    return Assertions.assertThat((Path) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractShortAssert<?> asShort() {
    objects.assertIsInstanceOf(info, actual, Short.class);
    return Assertions.assertThat((Short) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractShortArrayAssert<?> asShortArray() {
    objects.assertIsInstanceOf(info, actual, short[].class);
    return Assertions.assertThat((short[]) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractCharSequenceAssert<?, String> asString() {
    objects.assertIsInstanceOf(info, actual, String.class);
    return Assertions.assertThat((String) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractThrowableAssert<?, ? extends Throwable> asThrowable() {
    objects.assertIsInstanceOf(info, actual, Throwable.class);
    return Assertions.assertThat((Throwable) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractUriAssert<?> asUri() {
    objects.assertIsInstanceOf(info, actual, URI.class);
    return Assertions.assertThat((URI) actual);
  }

  /** {@inheritDoc} */
  @Override
  public AbstractUrlAssert<?> asUrl() {
    objects.assertIsInstanceOf(info, actual, URL.class);
    return Assertions.assertThat((URL) actual);
  }

  /**
   * The description of this assertion set with {@link #describedAs(String, Object...)} or
   * {@link #describedAs(Description)}.
   *
   * @return the description String representation of this assertion.
   */
  public String descriptionText() {
    return info.descriptionText();
  }

  /**
   * Overrides AssertJ default error message by the given one.
   * <p>
   * The new error message is built using {@link String#format(String, Object...)} if you provide args parameter (if you
   * don't, the error message is taken as it is).
   * <p>
   * Example :
   * <pre><code class='java'>assertThat(player.isRookie()).overridingErrorMessage(&quot;Expecting Player &lt;%s&gt; to be a rookie but was not.&quot;, player)
   *                              .isTrue();</code></pre>
   *
   * @param newErrorMessage the error message that will replace the default one provided by Assertj.
   * @param args the args used to fill error message as in {@link String#format(String, Object...)}.
   * @return this assertion object.
   * @throws Exception see {@link String#format(String, Object...)} exception clause.
   */
  public S overridingErrorMessage(String newErrorMessage, Object... args) {
    info.overridingErrorMessage(formatIfArgs(newErrorMessage, args));
    return myself;
  }

  /**
   * Alternative method for {@link AbstractAssert#overridingErrorMessage}
   *
   * @param newErrorMessage the error message that will replace the default one provided by Assertj.
   * @param args the args used to fill error message as in {@link String#format(String, Object...)}.
   * @return this assertion object.
   * @throws Exception see {@link String#format(String, Object...)} exception clause.
   */
  public S withFailMessage(String newErrorMessage, Object... args) {
    overridingErrorMessage(newErrorMessage, args);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingComparator(Comparator<? super A> customComparator) {
    // using a specific strategy to compare actual with other objects.
    this.objects = new Objects(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S usingDefaultComparator() {
    // fall back to default strategy to compare actual with other objects.
    this.objects = Objects.instance();
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S withThreadDumpOnError() {
    Failures.instance().enablePrintThreadDump();
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S withRepresentation(Representation representation) {
    info.useRepresentation(representation);
    return myself;
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #isEqualTo} instead
   *
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public boolean equals(Object obj) {
    throw new UnsupportedOperationException("'equals' is not supported...maybe you intended to call 'isEqualTo'");
  }

  /**
   * Always returns 1.
   *
   * @return 1.
   */
  @Override
  public int hashCode() {
    return 1;
  }

  /**
   * Verifies that the actual object matches the given predicate.
   * <p>
   * Example :
   *
   * <pre><code class='java'> assertThat(player).matches(p -> p.isRookie());</code></pre>
   *
   * @param predicate the {@link Predicate} to match
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual does not match the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   */
  public S matches(Predicate<? super A> predicate) {
    // use default PredicateDescription
    return matches(predicate, PredicateDescription.GIVEN);
  }

  /**
   * Verifies that the actual object matches the given predicate, the predicate description is used to get an
   * informative error message.
   * <p>
   * Example :
   *
   * <pre><code class='java'> assertThat(player).matches(p -> p.isRookie(), "is rookie");</code></pre>
   *
   * The error message contains the predicate description, if the previous assertion fails, it will be:
   *
   * <pre><code class='java'> Expecting:
   *   &lt;player&gt;
   * to match 'is rookie' predicate.</code></pre>
   *
   * @param predicate the {@link Predicate} to match
   * @param predicateDescription a description of the {@link Predicate} used in the error message
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual does not match the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   * @throws NullPointerException if given predicateDescription is null.
   */
  public S matches(Predicate<? super A> predicate, String predicateDescription) {
    return matches(predicate, new PredicateDescription(predicateDescription));
  }

  /**
   * Verifies that the actual object satisfied the given requirements expressed as a {@link Consumer}.
   * <p>
   * This is useful to perform a group of assertions on a single object.
   * <p>
   * Grouping assertions example :
   * <pre><code class='java'> // second constructor parameter is the light saber color
   * Jedi yoda = new Jedi("Yoda", "Green");
   * Jedi luke = new Jedi("Luke Skywalker", "Green");
   *
   * Consumer&lt;Jedi&gt; jediRequirements = jedi -> {
   *   assertThat(jedi.getLightSaberColor()).isEqualTo("Green");
   *   assertThat(jedi.getName()).doesNotContain("Dark");
   * };
   *
   * // assertions succeed:
   * assertThat(yoda).satisfies(jediRequirements);
   * assertThat(luke).satisfies(jediRequirements);
   *
   * // assertions fails:
   * Jedi vader = new Jedi("Vader", "Red");
   * assertThat(vader).satisfies(jediRequirements);</code></pre>
   * <p>
   * In the following example, {@code satisfies} prevents the need of define a local variable in order to run multiple assertions:
   * <pre><code class='java'> // no need to define team.getPlayers().get(0).getStats() as a local variable
   * assertThat(team.getPlayers().get(0).getStats()).satisfies(stats -> {
   *   assertThat(stats.pointPerGame).isGreaterThan(25.7);
   *   assertThat(stats.assistsPerGame).isGreaterThan(7.2);
   *   assertThat(stats.reboundsPerGame).isBetween(9, 12);
   * };</code></pre>
   *
   * @param requirements to assert on the actual object.
   * @return this assertion object.
   */
  public S satisfies(Consumer<A> requirements) {
    requireNonNull(requirements, "The Consumer<T> expressing the assertions requirements must not be null");
    requirements.accept(actual);
    return myself;
  }

  private S matches(Predicate<? super A> predicate, PredicateDescription predicateDescription) {
    requireNonNull(predicate, "The predicate must not be null");
    if (predicate.test(actual)) return myself;
    throw Failures.instance().failure(info, shouldMatch(actual, predicate, predicateDescription));
  }

  public static void setCustomRepresentation(Representation customRepresentation) {
    AbstractAssert.customRepresentation = customRepresentation;
  }
}
