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

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.description.Description.mostRelevantDescription;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOf;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Strings.formatIfArgs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.description.Description;
import org.assertj.core.error.AssertionErrorCreator;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.MessageFormatter;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
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
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 */
public abstract class AbstractAssert<SELF extends AbstractAssert<SELF, ACTUAL>, ACTUAL> implements Assert<SELF, ACTUAL> {

  // https://github.com/assertj/assertj/issues/1128
  public static boolean throwUnsupportedExceptionOnEquals = true;

  private static final String ORG_ASSERTJ = "org.assert";

  protected Objects objects = Objects.instance();

  @VisibleForTesting
  Conditions conditions = Conditions.instance();

  @VisibleForTesting
  public WritableAssertionInfo info;

  // visibility is protected to allow us write custom assertions that need access to actual
  protected final ACTUAL actual;
  protected final SELF myself;

  @VisibleForTesting
  // = ConfigurationProvider.CONFIGURATION_PROVIDER.representation(); ?
  static Representation customRepresentation = null;

  @VisibleForTesting
  AssertionErrorCreator assertionErrorCreator;

  @VisibleForTesting
  static boolean printAssertionsDescription;

  private static Consumer<Description> descriptionConsumer;

  // we prefer not to use Class<? extends S> selfType because it would force inherited
  // constructor to cast with a compiler warning
  // let's keep compiler warning internal (when we can) and not expose them to our end users.
  @SuppressWarnings("unchecked")
  protected AbstractAssert(ACTUAL actual, Class<?> selfType) {
    myself = (SELF) selfType.cast(this);
    this.actual = actual;
    info = new WritableAssertionInfo(customRepresentation);
    assertionErrorCreator = new AssertionErrorCreator();
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
   * Throw an assertion error based on information in this assertion. Equivalent to:
   * <pre><code class='java'>throw failure(errorMessage, arguments);</code></pre>
   * <p>
   * This method is a thin wrapper around {@link #failure(String, Object...) failure()} - see that method for a more detailed
   * description.
   * <p>
   * Note that generally speaking, using {@link #failure(String, Object...) failure()} directly is preferable to using this
   * wrapper method, as the compiler and other code analysis tools will be able to tell that the statement will never return
   * normally and respond appropriately.
   *
   * @param errorMessage the error message to format
   * @param arguments the arguments referenced by the format specifiers in the errorMessage string.
   * @see #failWithActualExpectedAndMessage(Object, Object, String, Object...)
   * @see #failure(String, Object...)
   */
  protected void failWithMessage(String errorMessage, Object... arguments) {
    throw failure(errorMessage, arguments);
  }

  /**
   * Generate a custom assertion error using the information in this assertion.
   * <p>
   * This is a utility method to ease writing custom assertions classes using {@link String#format(String, Object...)} specifiers
   * in error message.
   * <p>
   * Moreover, this method honors any description set with {@link #as(String, Object...)} or overridden error message
   * defined by the user with {@link #overridingErrorMessage(String, Object...)}.
   * <p>
   * Example:
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
   * @return The generated assertion error.
   * @see #failureWithActualExpected(Object, Object, String, Object...)
   * @see #failWithMessage(String, Object...)
   */
  protected AssertionError failure(String errorMessage, Object... arguments) {
    AssertionError assertionError = Failures.instance().failureIfErrorMessageIsOverridden(info);
    if (assertionError == null) {
      // error message was not overridden, build it.
      String description = MessageFormatter.instance().format(info.description(), info.representation(), "");
      assertionError = new AssertionError(description + format(errorMessage, arguments));
    }
    Failures.instance().removeAssertJRelatedElementsFromStackTraceIfNeeded(assertionError);
    removeCustomAssertRelatedElementsFromStackTraceIfNeeded(assertionError);
    return assertionError;
  }

  /**
   * Throw an assertion error based on information in this assertion. Equivalent to:
   * <pre><code class='java'>throw failureWithActualExpected(actual, expected, errorMessageFormat, arguments);</code></pre>
   * <p>
   * This method is a thin wrapper around {@link #failureWithActualExpected(Object, Object, String, Object...) failureWithActualExpected()} -
   * see that method for a more detailed description. Note that generally speaking, using
   * {@link #failureWithActualExpected(Object, Object, String, Object...) failureWithActualExpected()} directly is
   * preferable to using this wrapper method, as the compiler and other code analysis tools will be able to tell that the
   * statement will never return normally and respond appropriately.
   *
   * @param actual the actual object that was found during the test
   * @param expected the object that was expected
   * @param errorMessageFormat the error message to format
   * @param arguments the arguments referenced by the format specifiers in the errorMessage string.
   * @see #failWithMessage(String, Object...)
   * @see #failureWithActualExpected(Object, Object, String, Object...)
   */
  protected void failWithActualExpectedAndMessage(Object actual, Object expected, String errorMessageFormat,
                                                  Object... arguments) {
    throw failureWithActualExpected(actual, expected, errorMessageFormat, arguments);
  }

  /**
   * Generate a custom assertion error using the information in this assertion, using the given actual and expected values.
   * <p>
   * This is a utility method to ease writing custom assertions classes using {@link String#format(String, Object...)} specifiers
   * in error message with actual and expected values.
   * <p>
   * Moreover, this method honors any description set with {@link #as(String, Object...)} or overridden error message
   * defined by the user with {@link #overridingErrorMessage(String, Object...)}.
   * <p>
   * This method also sets the "actual" and "expected" fields of the assertion if available (eg, if OpenTest4J is on the path).
   * This aids IDEs to produce visual diffs of the resulting values.
   * <p>
   * Example :
   * <pre><code class='java'> public TolkienCharacterAssert hasName(String name) {
   *   // check that actual TolkienCharacter we want to make assertions on is not null.
   *   isNotNull();
   *
   *   // check condition
   *   if (!actual.getName().equals(name)) {
   *     throw failureWithActualExpected(actual.getName(), name, &quot;Expected character's name to be &lt;%s&gt; but was &lt;%s&gt;&quot;, name, actual.getName());
   *   }
   *
   *   // return the current assertion for method chaining
   *   return this;
   * }</code></pre>
   *
   * @param actual the actual object that was found during the test
   * @param expected the object that was expected
   * @param errorMessageFormat the error message to format
   * @param arguments the arguments referenced by the format specifiers in the errorMessage string.
   * @return The generated assertion error.
   * @see #failure(String, Object...)
   * @see #failWithActualExpectedAndMessage(Object, Object, String, Object...)
   */
  protected AssertionError failureWithActualExpected(Object actual, Object expected, String errorMessageFormat,
                                                     Object... arguments) {
    String errorMessage = Optional.ofNullable(info.overridingErrorMessage())
                                  .orElse(format(errorMessageFormat, arguments));
    String description = MessageFormatter.instance().format(info.description(), info.representation(), errorMessage);
    AssertionError assertionError = assertionErrorCreator.assertionError(description, actual, expected, info.representation());
    Failures.instance().removeAssertJRelatedElementsFromStackTraceIfNeeded(assertionError);
    removeCustomAssertRelatedElementsFromStackTraceIfNeeded(assertionError);
    return assertionError;
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
    throw assertionError(errorMessageFactory);
  }

  protected AssertionError assertionError(ErrorMessageFactory errorMessageFactory) {
    AssertionError failure = Failures.instance().failure(info, errorMessageFactory);
    removeCustomAssertRelatedElementsFromStackTraceIfNeeded(failure);
    return failure;
  }

  private void removeCustomAssertRelatedElementsFromStackTraceIfNeeded(AssertionError assertionError) {
    if (!Failures.instance().isRemoveAssertJRelatedElementsFromStackTrace()) return;
    if (isAssertjAssertClass()) return;

    StackTraceElement[] newStackTrace = Arrays.stream(assertionError.getStackTrace())
                                              .filter(element -> !isElementOfCustomAssert(element))
                                              .toArray(StackTraceElement[]::new);
    assertionError.setStackTrace(newStackTrace);
  }

  private boolean isAssertjAssertClass() {
    return getClass().getName().startsWith(ORG_ASSERTJ);
  }

  protected boolean isElementOfCustomAssert(StackTraceElement stackTraceElement) {
    Class<?> currentAssertClass = getClass();
    while (currentAssertClass != AbstractAssert.class) {
      if (stackTraceElement.getClassName().equals(currentAssertClass.getName())) return true;
      currentAssertClass = currentAssertClass.getSuperclass();
    }
    return false;
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
  public SELF describedAs(Description description) {
    info.description(description);
    if (printAssertionsDescription) printDescriptionText();
    if (descriptionConsumer != null) descriptionConsumer.accept(description);
    return myself;
  }

  private void printDescriptionText() {
    String descriptionText = info.descriptionText();
    if (!descriptionText.isEmpty()) System.out.println(descriptionText);
  }

  /** {@inheritDoc} */
  @Override
  public SELF isEqualTo(Object expected) {
    if (actual instanceof AbstractAssert<?, ?> && throwUnsupportedExceptionOnEquals) {
      throw new UnsupportedOperationException("Attempted to compare an assertion object to another object using 'isEqualTo'. "
                                              + "This is not supported. Perhaps you meant 'isSameAs' instead?");
    }

    objects.assertEqual(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF isNotEqualTo(Object other) {
    if (actual instanceof AbstractAssert<?, ?> && throwUnsupportedExceptionOnEquals) {
      throw new UnsupportedOperationException("Attempted to compare an assertion object to another object using 'isNotEqualTo'. "
                                              + "This is not supported. Perhaps you meant 'isNotSameAs' instead?");
    }

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
  public SELF satisfies(Condition<? super ACTUAL> condition) {
    conditions.assertSatisfies(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  @SuppressWarnings("unchecked")
  @Override
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT asInstanceOf(InstanceOfAssertFactory<?, ASSERT> instanceOfAssertFactory) {
    requireNonNull(instanceOfAssertFactory, shouldNotBeNull("instanceOfAssertFactory")::create);
    objects.assertIsInstanceOf(info, actual, instanceOfAssertFactory.getRawClass());
    return (ASSERT) instanceOfAssertFactory.createAssert(actual).withAssertionState(myself);
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
  public SELF hasToString(String expectedStringTemplate, Object... args) {
    requireNonNull(expectedStringTemplate, "The expectedStringTemplate must not be null");
    return hasToString(format(expectedStringTemplate, args));
  }

  /** {@inheritDoc} */
  @Override
  public SELF doesNotHaveToString(String otherToString) {
    objects.assertDoesNotHaveToString(info, actual, otherToString);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF doesNotHaveToString(String expectedStringTemplate, Object... args) {
    requireNonNull(expectedStringTemplate, "The expectedStringTemplate must not be null");
    return doesNotHaveToString(format(expectedStringTemplate, args));
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

  /**
   *  {@inheritDoc}
   *  @deprecated use {@link #asInstanceOf(InstanceOfAssertFactory) asInstanceOf(InstanceOfAssertFactories.LIST)} instead
   */
  @Deprecated
  @Override
  @CheckReturnValue
  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> asList() {
    return asInstanceOf(InstanceOfAssertFactories.LIST);
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public AbstractStringAssert<?> asString() {
    objects.assertNotNull(info, actual);
    return Assertions.assertThat(actual.toString());
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
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the chained call by throwing an AssertionError.
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
   * Overrides AssertJ default error message by the given one.
   * <p>
   * The new error message is only built if the assertion fails (by consuming the given supplier), this is useful if building messages is expensive.
   * <p>
   * You must set the message <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks
   * the call chain by throwing an {@link AssertionError}.
   * <p>
   * Example :
   * <pre><code class='java'>assertThat(player.isRookie()).overridingErrorMessage(() -&gt; &quot;Expecting Player to be a rookie but was not.&quot;)
   *                             .isTrue();</code></pre>
   *
   * @param supplier the supplier supplies error message that will replace the default one provided by Assertj.
   * @return this assertion object.
   */
  @CheckReturnValue
  public SELF overridingErrorMessage(Supplier<String> supplier) {
    info.overridingErrorMessage(supplier);
    return myself;
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
   * @param newErrorMessage the error message that will replace the default one provided by Assertj.
   * @param args the args used to fill error message as in {@link String#format(String, Object...)}.
   * @return this assertion object.
   */
  @CheckReturnValue
  public SELF withFailMessage(String newErrorMessage, Object... args) {
    return overridingErrorMessage(newErrorMessage, args);
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
   * @param supplier the supplier supplies error message that will replace the default one provided by Assertj.
   * @return this assertion object.
   */
  @CheckReturnValue
  public SELF withFailMessage(Supplier<String> supplier) {
    return overridingErrorMessage(supplier);
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator) {
    return usingComparator(customComparator, null);
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator, String customComparatorDescription) {
    // using a specific strategy to compare actual with other objects.
    this.objects = new Objects(new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
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
   * @throws UnsupportedOperationException if this method is called.
   *
   * @deprecated use {@link #isEqualTo} instead
   */
  @Override
  @Deprecated
  public boolean equals(Object obj) {
    if (throwUnsupportedExceptionOnEquals) {
      throw new UnsupportedOperationException("'equals' is not supported... maybe you intended to call 'isEqualTo'");
    }
    return super.equals(obj);
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
   * @throws AssertionError if {@code actual} does not match the given {@link Predicate}.
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
   * @throws AssertionError if {@code actual} does not match the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   * @throws NullPointerException if given predicateDescription is null.
   */
  public SELF matches(Predicate<? super ACTUAL> predicate, String predicateDescription) {
    return matches(predicate, new PredicateDescription(predicateDescription));
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
   *
   * @throws NullPointerException if any given Consumer is null
   */
  @SafeVarargs
  public final SELF satisfies(Consumer<? super ACTUAL>... requirements) {
    return satisfiesForProxy(requirements);
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
   *
   * @throws IllegalArgumentException if any given assertions group is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.
   * @throws AssertionError rethrown as is by the given {@link ThrowingConsumer}
   * @since 3.21.0
   */
  @SafeVarargs
  public final SELF satisfies(ThrowingConsumer<? super ACTUAL>... assertions) {
    return satisfiesForProxy(assertions);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF satisfiesForProxy(Consumer<? super ACTUAL>[] assertionsGroups) throws AssertionError {
    checkArgument(stream(assertionsGroups).allMatch(java.util.Objects::nonNull), "No assertions group should be null");
    List<AssertionError> assertionErrors = stream(assertionsGroups).map(this::catchOptionalAssertionError)
                                                                   .filter(Optional::isPresent)
                                                                   .map(Optional::get)
                                                                   .collect(toList());
    if (!assertionErrors.isEmpty()) {
      throw multipleAssertionsError(assertionErrors);
    }
    return myself;
  }

  private Optional<AssertionError> catchOptionalAssertionError(Consumer<? super ACTUAL> assertions) {
    try {
      assertions.accept(actual);
      return Optional.empty();
    } catch (AssertionError assertionError) {
      return Optional.of(assertionError);
    }
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
   *
   * @throws IllegalArgumentException if any given assertions group is null
   * @since 3.12.0
   */
  @SafeVarargs
  public final SELF satisfiesAnyOf(Consumer<? super ACTUAL>... assertions) {
    return satisfiesAnyOfForProxy(assertions);
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
   *
   * @throws IllegalArgumentException if any given assertions group is null
   * @throws RuntimeException rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.
   * @throws AssertionError rethrown as is by the given {@link ThrowingConsumer}
   * @since 3.21.0
   */
  @SafeVarargs
  public final SELF satisfiesAnyOf(ThrowingConsumer<? super ACTUAL>... assertions) {
    return satisfiesAnyOfForProxy(assertions);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF satisfiesAnyOfForProxy(Consumer<? super ACTUAL>[] assertionsGroups) throws AssertionError {
    checkArgument(stream(assertionsGroups).allMatch(java.util.Objects::nonNull), "No assertions group should be null");
    // use a for loop over stream to return as soon as one assertion is met
    List<AssertionError> assertionErrors = list();
    for (Consumer<? super ACTUAL> assertionsGroup : assertionsGroups) {
      Optional<AssertionError> maybeError = catchOptionalAssertionError(assertionsGroup);
      if (!maybeError.isPresent()) {
        return myself;
      }
      assertionErrors.add(maybeError.get());
    }
    throw multipleAssertionsError(assertionErrors);
  }

  private AssertionError multipleAssertionsError(List<AssertionError> assertionErrors) {
    // we don't allow overriding the error message to avoid loosing all the failed assertions error message.
    return assertionErrorCreator.multipleAssertionsError(info.description(), assertionErrors);
  }

  private boolean satisfiesAssertions(Consumer<? super ACTUAL> assertions) {
    try {
      assertions.accept(actual);
    } catch (@SuppressWarnings("unused") AssertionError e) {
      return false;
    }
    return true;
  }

  private AssertionError catchAssertionError(Consumer<? super ACTUAL> assertions) {
    try {
      assertions.accept(actual);
    } catch (AssertionError assertionError) {
      return assertionError;
    }
    throw new IllegalStateException("Shouldn't arrived here, assertions should have raised an AssertionError (please file a bug)");
  }

  private SELF matches(Predicate<? super ACTUAL> predicate, PredicateDescription predicateDescription) {
    requireNonNull(predicate, "The predicate must not be null");
    if (predicate.test(actual)) return myself;
    throw Failures.instance().failure(info, shouldMatch(actual, predicate, predicateDescription));
  }

  public static void setCustomRepresentation(Representation customRepresentation) {
    ConfigurationProvider.loadRegisteredConfiguration();
    AbstractAssert.customRepresentation = customRepresentation;
  }

  public static void setPrintAssertionsDescription(boolean printAssertionsDescription) {
    ConfigurationProvider.loadRegisteredConfiguration();
    AbstractAssert.printAssertionsDescription = printAssertionsDescription;
  }

  public static void setDescriptionConsumer(Consumer<Description> descriptionConsumer) {
    AbstractAssert.descriptionConsumer = descriptionConsumer;
  }

  /** {@inheritDoc} */
  @Override
  public SELF hasSameHashCodeAs(Object other) {
    objects.assertHasSameHashCodeAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF doesNotHaveSameHashCodeAs(Object other) {
    objects.assertDoesNotHaveSameHashCodeAs(info, actual, other);
    return myself;
  }

  /**
   * Create a {@link AbstractListAssert} from the given list.
   * <p>
   * this method avoids code duplication when features like extracting/asList need to create a new list assertions.
   *
   * @param <E> the type of elements.
   * @param newActual new list under test
   * @return a new {@link AbstractListAssert}.
   */
  protected <E> AbstractListAssert<?, List<? extends E>, E, ObjectAssert<E>> newListAssertInstance(List<? extends E> newActual) {
    return new ListAssert<>(newActual);
  }

  SELF withAssertionState(@SuppressWarnings("rawtypes") AbstractAssert assertInstance) {
    this.objects = assertInstance.objects;
    propagateAssertionInfoFrom(assertInstance);
    return myself;
  }

  private void propagateAssertionInfoFrom(AbstractAssert<?, ?> assertInstance) {
    this.info.useRepresentation(assertInstance.info.representation());
    this.info.description(assertInstance.info.description());
    this.info.overridingErrorMessage(assertInstance.info.overridingErrorMessage());
  }

  // this method is meant to be overridden and made public in subclasses that want to expose it
  // this would avoid duplicating this code in all subclasses
  protected RecursiveComparisonAssert<?> usingRecursiveComparison(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    return new RecursiveComparisonAssert<>(actual, recursiveComparisonConfiguration).withAssertionState(myself);
  }

  // this method is meant to be overridden and made public in subclasses that want to expose it
  // this would avoid duplicating this code in all subclasses
  protected RecursiveComparisonAssert<?> usingRecursiveComparison() {
    return usingRecursiveComparison(new RecursiveComparisonConfiguration());
  }

  // this method is meant to be overridden and made public in subclasses that want to expose it
  // this would avoid duplicating this code in all subclasses
  protected RecursiveAssertionAssert usingRecursiveAssertion(RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    return new RecursiveAssertionAssert(actual, recursiveAssertionConfiguration);
  }

  // this method is meant to be overridden and made public in subclasses that want to expose it
  // this would avoid duplicating this code in all subclasses
  protected RecursiveAssertionAssert usingRecursiveAssertion() {
    return new RecursiveAssertionAssert(actual, RecursiveAssertionConfiguration.builder().build());
  }

  /**
   * Extracts the value of given field/property from the object under test and creates a new assertion object using the
   * given assert factory.
   * <p>
   * If the object under test is a {@link Map}, the {@code propertyOrField} parameter is used as a key to the map.
   * <p>
   * Nested field/property is supported, specifying "address.street.number" is equivalent to get the value
   * corresponding to actual.getAddress().getStreet().getNumber()
   * <p>
   * Private field can be extracted unless you call {@link Assertions#setAllowExtractingPrivateFields(boolean) Assertions.setAllowExtractingPrivateFields(false)}.
   *
   * @param <ASSERT>        the type of the resulting {@code Assert}
   * @param propertyOrField the property/field to extract from the initial object under test
   * @param assertFactory   the factory for the creation of the new {@code Assert}
   * @return the new {@code Assert} instance
   * @throws AssertionError if {@code actual} is {@code null}
   *
   * @see AbstractObjectAssert#extracting(String)
   * @see AbstractObjectAssert#extracting(String, InstanceOfAssertFactory)
   *
   * @since 3.16.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  protected <ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(String propertyOrField,
                                                                    AssertFactory<Object, ASSERT> assertFactory) {
    requireNonNull(propertyOrField, shouldNotBeNull("propertyOrField")::create);
    requireNonNull(assertFactory, shouldNotBeNull("assertFactory")::create);
    isNotNull();
    Object value = byName(propertyOrField).apply(actual);
    String extractedPropertyOrFieldDescription = extractedDescriptionOf(propertyOrField);
    String description = mostRelevantDescription(info.description(), extractedPropertyOrFieldDescription);
    return (ASSERT) assertFactory.createAssert(value).withAssertionState(myself).as(description);
  }

  /**
   * Uses the given {@link Function} to extract a value from the object under test and creates a new assertion object
   * using the given assert factory.
   *
   * @param <T>           the expected extracted value type
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @param extractor     the extractor function used to extract the value from the object under test
   * @param assertFactory the factory for the creation of the new {@code Assert}
   * @return the new {@code Assert} instance
   * @throws AssertionError if {@code actual} is {@code null}
   *
   * @see AbstractObjectAssert#extracting(Function)
   * @see AbstractObjectAssert#extracting(Function, InstanceOfAssertFactory)
   *
   * @since 3.16.0
   */
  @SuppressWarnings("unchecked")
  @CheckReturnValue
  protected <T, ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(Function<? super ACTUAL, ? extends T> extractor,
                                                                       AssertFactory<T, ASSERT> assertFactory) {
    requireNonNull(extractor, shouldNotBeNull("extractor")::create);
    requireNonNull(assertFactory, shouldNotBeNull("assertFactory")::create);
    isNotNull();
    T extractedValue = extractor.apply(actual);
    return (ASSERT) assertFactory.createAssert(extractedValue).withAssertionState(myself);
  }

  /**
   * Returns true if actual and other are equal according to the current comparison strategy.
   *
   * @param actual the object to compare to other
   * @param other the object to compare to actual
   * @return true if actual and other are equal according to the underlying comparison strategy.
   * @since 3.23.0
   *
   * @deprecated {@link ComparisonStrategy} will become part of the public API in the next major release and this method
   * will be removed.
   */
  @Deprecated
  protected boolean areEqual(Object actual, Object other) {
    return objects.getComparisonStrategy().areEqual(actual, other);
  }
}
