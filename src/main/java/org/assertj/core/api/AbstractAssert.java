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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Strings.formatIfArgs;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all assertions.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractAssert<SELF extends AbstractAssert<SELF, ACTUAL>, ACTUAL> implements Assert<SELF, ACTUAL> {

  private static final String ORG_ASSERTJ = "org.assert";

  @VisibleForTesting
  Objects objects = Objects.instance();

  @VisibleForTesting
  Conditions conditions = Conditions.instance();

  @VisibleForTesting
  public final WritableAssertionInfo info;

  // visibility is protected to allow us write custom assertions that need access to actual
  @VisibleForTesting
  protected final ACTUAL actual;
  protected final SELF myself;

  private static Representation customRepresentation = null;

  // we prefer not to use Class<? extends S> selfType because it would force inherited
  // constructor to cast with a compiler warning
  // let's keep compiler warning internal (when we can) and not expose them to our end users.
  @SuppressWarnings("unchecked")
  public AbstractAssert(ACTUAL actual, Class<?> selfType) {
    myself = (SELF) selfType.cast(this);
    this.actual = actual;
    info = new WritableAssertionInfo(customRepresentation);
  }

  /**
   * Exposes the {@link WritableAssertionInfo} used in the current assertion for better extensibility.<br> When writing
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
    AssertionError assertionError = Failures.instance().failureIfErrorMessageIsOverridden(info);
    if (assertionError == null) {
      // error message was not overridden, build it.
      String description = MessageFormatter.instance().format(info.description(), info.representation(), "");
      assertionError = new AssertionError(description + String.format(errorMessage, arguments));
    }
    Failures.instance().removeAssertJRelatedElementsFromStackTraceIfNeeded(assertionError);
    removeCustomAssertRelatedElementsFromStackTraceIfNeeded(assertionError);
    throw assertionError;
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
   * @throws AssertionError with a message corresponding to the given {@link BasicErrorMessageFactory}.
   */
  protected void throwAssertionError(ErrorMessageFactory errorMessageFactory) {
    AssertionError failure = Failures.instance().failure(info, errorMessageFactory);
    removeCustomAssertRelatedElementsFromStackTraceIfNeeded(failure);
    throw failure;
  }

  private void removeCustomAssertRelatedElementsFromStackTraceIfNeeded(AssertionError assertionError) {
    if (!Failures.instance().isRemoveAssertJRelatedElementsFromStackTrace()) return;
    if (isAssertjAssertClass()) return;

    List<StackTraceElement> filtered = newArrayList(assertionError.getStackTrace());
    for (StackTraceElement element : assertionError.getStackTrace()) {
      if (isElementOfCustomAssert(element)) {
        filtered.remove(element);
      }
    }
    StackTraceElement[] newStackTrace = filtered.toArray(new StackTraceElement[filtered.size()]);
    assertionError.setStackTrace(newStackTrace);
  }

  private boolean isAssertjAssertClass() {
    return getClass().getName().startsWith(ORG_ASSERTJ);
  }

  private boolean isElementOfCustomAssert(StackTraceElement stackTraceElement) {
    Class<?> currentAssertClass = getClass();
    while (currentAssertClass != AbstractAssert.class) {
      if (stackTraceElement.getClassName().equals(currentAssertClass.getName())) {
        return true;
      }
      currentAssertClass = currentAssertClass.getSuperclass();
    }
    return false;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF as(String description, Object... args) {
    return describedAs(description, args);
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF as(Description description) {
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
  @CheckReturnValue
  protected SELF inHexadecimal() {
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
  @CheckReturnValue
  protected SELF inBinary() {
    info.useBinaryRepresentation();
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF describedAs(String description, Object... args) {
    info.description(description, args);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF describedAs(Description description) {
    info.description(description);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isEqualTo(Object expected) {
    objects.assertEqual(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotEqualTo(Object other) {
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
  public SELF isNotNull() {
    objects.assertNotNull(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isSameAs(Object expected) {
    objects.assertSame(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotSameAs(Object other) {
    objects.assertNotSame(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isIn(Object... values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotIn(Object... values) {
    objects.assertIsNotIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isIn(Iterable<?> values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotIn(Iterable<?> values) {
    objects.assertIsNotIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF is(Condition<? super ACTUAL> condition) {
    conditions.assertIs(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNot(Condition<? super ACTUAL> condition) {
    conditions.assertIsNot(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF has(Condition<? super ACTUAL> condition) {
    conditions.assertHas(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF doesNotHave(Condition<? super ACTUAL> condition) {
    conditions.assertDoesNotHave(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isInstanceOf(Class<?> type) {
    objects.assertIsInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  public <T> SELF isInstanceOfSatisfying(Class<T> type, Consumer<T> requirements) {
    objects.assertIsInstanceOf(info, actual, type);
    requireNonNull(requirements, "The Consumer<T> expressing the assertions requirements must not be null");
    requirements.accept((T) actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isInstanceOfAny(Class<?>... types) {
    objects.assertIsInstanceOfAny(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotInstanceOf(Class<?> type) {
    objects.assertIsNotInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotInstanceOfAny(Class<?>... types) {
    objects.assertIsNotInstanceOfAny(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasSameClassAs(Object other) {
    objects.assertHasSameClassAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasToString(String expectedToString) {
    objects.assertHasToString(info, actual, expectedToString);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF doesNotHaveSameClassAs(Object other) {
    objects.assertDoesNotHaveSameClassAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isExactlyInstanceOf(Class<?> type) {
    objects.assertIsExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotExactlyInstanceOf(Class<?> type) {
    objects.assertIsNotExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isOfAnyClassIn(Class<?>... types) {
    objects.assertIsOfAnyClassIn(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotOfAnyClassIn(Class<?>... types) {
    objects.assertIsNotOfAnyClassIn(info, actual, types);
    return myself;
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  @CheckReturnValue
  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> asList() {    objects.assertIsInstanceOf(info, actual, List.class);
    return new ListAssert<>((List<Object>) actual);
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public AbstractCharSequenceAssert<?, String> asString() {
    objects.assertIsInstanceOf(info, actual, String.class);
    return Assertions.assertThat((String) actual);
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
   */
  @CheckReturnValue
  public SELF overridingErrorMessage(String newErrorMessage, Object... args) {
    info.overridingErrorMessage(formatIfArgs(newErrorMessage, args));
    return myself;
  }

  /**
   * Alternative method for {@link AbstractAssert#overridingErrorMessage}
   *
   * @param newErrorMessage the error message that will replace the default one provided by Assertj.
   * @param args the args used to fill error message as in {@link String#format(String, Object...)}.
   * @return this assertion object.
   */
  @CheckReturnValue
  public SELF withFailMessage(String newErrorMessage, Object... args) {
    overridingErrorMessage(newErrorMessage, args);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator) {
    // using a specific strategy to compare actual with other objects.
    this.objects = new Objects(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    // fall back to default strategy to compare actual with other objects.
    this.objects = Objects.instance();
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF withThreadDumpOnError() {
    Failures.instance().enablePrintThreadDump();
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF withRepresentation(Representation representation) {
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
   * <pre><code class='java'> assertThat(player).matches(p -&gt; p.isRookie());</code></pre>
   *
   * @param predicate the {@link Predicate} to match
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual does not match the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   */
  public SELF matches(Predicate<? super ACTUAL> predicate) {
    // use default PredicateDescription
    return matches(predicate, PredicateDescription.GIVEN);
  }

  /**
   * Verifies that the actual object matches the given predicate, the predicate description is used to get an
   * informative error message.
   * <p>
   * Example :
   *
   * <pre><code class='java'> assertThat(player).matches(p -&gt; p.isRookie(), "is rookie");</code></pre>
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
  public SELF matches(Predicate<? super ACTUAL> predicate, String predicateDescription) {
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
   * Consumer&lt;Jedi&gt; jediRequirements = jedi -&gt; {
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
   * assertThat(team.getPlayers().get(0).getStats()).satisfies(stats -&gt; {
   *   assertThat(stats.pointPerGame).isGreaterThan(25.7);
   *   assertThat(stats.assistsPerGame).isGreaterThan(7.2);
   *   assertThat(stats.reboundsPerGame).isBetween(9, 12);
   * };</code></pre>
   *
   * @param requirements to assert on the actual object - must not be null.
   * @return this assertion object.
   * 
   * @throws NullPointerException if given Consumer is null 
   */
  public SELF satisfies(Consumer<ACTUAL> requirements) {
    requireNonNull(requirements, "The Consumer<T> expressing the assertions requirements must not be null");
    requirements.accept(actual);
    return myself;
  }

  private SELF matches(Predicate<? super ACTUAL> predicate, PredicateDescription predicateDescription) {
    requireNonNull(predicate, "The predicate must not be null");
    if (predicate.test(actual)) return myself;
    throw Failures.instance().failure(info, shouldMatch(actual, predicate, predicateDescription));
  }

  public static void setCustomRepresentation(Representation customRepresentation) {
    AbstractAssert.customRepresentation = customRepresentation;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasSameHashCodeAs(Object other) {
    objects.assertHasSameHashCodeAs(info, actual, other);
    return myself;
  }
}
