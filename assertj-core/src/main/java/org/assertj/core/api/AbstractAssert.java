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
package org.assertj.core.api;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.description.Description.mostRelevantDescription;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotMatch.shouldNotMatch;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOf;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Preconditions.checkArgument;
import static org.assertj.core.util.Strings.formatIfArgs;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.api.AssertFactory.ValueProvider;
import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.description.Description;
import org.assertj.core.error.AssertionErrorCreator;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.MessageFormatter;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.annotation.Contract;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.presentation.Representation;

/**
 * Base class for all assertions.
 *
 * @param <SELF>   the "self" type of this assertion class. Please read &quot;<a href="https://bit.ly/1IZIRcY"
 *                 target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *                 for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractAssert<SELF extends AbstractAssert<SELF, ACTUAL>, ACTUAL> implements Assert<SELF, ACTUAL> {

  // https://github.com/assertj/assertj/issues/1128
  public static boolean throwUnsupportedExceptionOnEquals = true;

  private static final String ORG_ASSERTJ = "org.assert";

  protected Objects objects = Objects.instance();

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  Conditions conditions = Conditions.instance();

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  public WritableAssertionInfo info;

  // visibility is protected to allow us write custom assertions that need access to actual
  protected final ACTUAL actual;
  protected final SELF myself;

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  // = ConfigurationProvider.CONFIGURATION_PROVIDER.representation(); ?
  static Representation customRepresentation = null;

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  AssertionErrorCreator assertionErrorCreator;

  // TODO reduce the visibility of the fields annotated with @VisibleForTesting
  static boolean printAssertionsDescription;

  private static Consumer<Description> descriptionConsumer;

  // When non-null, assertion errors are handled by this handler instead of being thrown directly.
  // Used by soft assertions (collect errors) and assumptions (convert to assumption exceptions).
  AssertionErrorHandler assertionErrorHandler;

  // Depth counter for nested soft assertion call detection (replaces stack trace scanning)
  private static final ThreadLocal<Integer> SOFT_CALL_DEPTH = ThreadLocal.withInitial(() -> 0);

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
   * Wraps an assertion method body for soft assertion support.
   * When {@code softAssertionCollector} is set, catches {@link AssertionError} and collects it
   * instead of throwing. Uses a depth counter to handle nested calls (e.g., {@code isTrue()}
   * calling {@code isEqualTo(true)}) — only the outermost call catches and collects.
   *
   * @param body the assertion logic to execute
   * @return {@code myself} for fluent chaining
   */
  protected SELF executeAssertion(Runnable body) {
    if (assertionErrorHandler == null) {
      body.run();
      return myself;
    }
    if (assertionErrorHandler.mustSkipChainedAssertions()) {
      return myself;
    }
    int depth = SOFT_CALL_DEPTH.get();
    SOFT_CALL_DEPTH.set(depth + 1);
    try {
      body.run();
      if (depth == 0) assertionErrorHandler.succeeded();
    } catch (AssertionError e) {
      if (depth > 0) throw e;
      assertionErrorHandler.handleError(e);
      if (e instanceof FinalAssertionError) {
        assertionErrorHandler.skipChainedAssertions();
      }
    } finally {
      SOFT_CALL_DEPTH.set(depth);
    }
    return myself;
  }

  /**
   * Wraps a navigation method that returns a different assert type and also performs assertion checks.
   * In soft mode, catches {@link AssertionError} from the assertion guards, collects it,
   * and returns {@code null} (matching the old proxy behavior).
   *
   * @param <T>  the return type of the navigation method
   * @param body the navigation method logic to execute
   * @return the navigation result, or {@code null} if an assertion error was collected in soft mode
   */
  protected <T> T executeAssertionNavigation(Supplier<T> body) {
    if (assertionErrorHandler == null) {
      try {
        return body.get();
      } catch (AssertionError | RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    int depth = SOFT_CALL_DEPTH.get();
    SOFT_CALL_DEPTH.set(depth + 1);
    try {
      T result = body.get();
      if (depth == 0) assertionErrorHandler.succeeded();
      return result;
    } catch (AssertionError e) {
      if (depth > 0) throw e;
      assertionErrorHandler.handleError(e);
      if (e instanceof FinalAssertionError finalAssertionError) {
        assertionErrorHandler.skipChainedAssertions();
        return (T) finalAssertionError.getAssert();
      }
      return null;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      SOFT_CALL_DEPTH.set(depth);
    }
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

  protected ComparisonStrategy getComparisonStrategy() {
    return objects.getComparisonStrategy();
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
   * @param arguments    the arguments referenced by the format specifiers in the errorMessage string.
   * @see #failWithActualExpectedAndMessage(Object, Object, String, Object...)
   * @see #failure(String, Object...)
   */
  @Contract("_, _ -> fail")
  protected void failWithMessage(String errorMessage, Object... arguments) {
    throw failure(errorMessage, arguments);
  }

  /**
   * Generate a custom assertion error using the information in this assertion.
   * <p>
   * This is a utility method to ease writing custom assertions classes using {@link String#format(String, Object...)} specifiers
   * in the error message.
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
   * @param arguments    the arguments referenced by the format specifiers in the errorMessage string.
   * @return The generated assertion error.
   * @see #failureWithActualExpected(Object, Object, String, Object...)
   * @see #failWithMessage(String, Object...)
   */
  protected AssertionError failure(String errorMessage, Object... arguments) {
    AssertionError assertionError = Failures.instance().failureIfErrorMessageIsOverridden(info);
    if (assertionError == null) {
      // error message was not overridden, build it.
      String description = MessageFormatter.instance().format(info.description(), info.representation(), "");
      assertionError = new AssertionError(description + errorMessage.formatted(arguments));
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
   * @param actual             the actual object that was found during the test
   * @param expected           the object that was expected
   * @param errorMessageFormat the error message to format
   * @param arguments          the arguments referenced by the format specifiers in the errorMessage string.
   * @see #failWithMessage(String, Object...)
   * @see #failureWithActualExpected(Object, Object, String, Object...)
   */
  @Contract("_, _, _, _ -> fail")
  protected void failWithActualExpectedAndMessage(Object actual, Object expected, String errorMessageFormat,
                                                  Object... arguments) {
    throw failureWithActualExpected(actual, expected, errorMessageFormat, arguments);
  }

  /**
   * Generate a custom assertion error using the information in this assertion, using the given actual and expected values.
   * <p>
   * This is a utility method to ease writing custom assertions classes using {@link String#format(String, Object...)} specifiers
   * in the error message with actual and expected values.
   * <p>
   * Moreover, this method honors any description set with {@link #as(String, Object...)} or overridden error message
   * defined by the user with {@link #overridingErrorMessage(String, Object...)}.
   * <p>
   * This method also sets the "actual" and "expected" fields of the assertion if available (eg, if OpenTest4J is on the path).
   * This aids IDEs to produce visual diffs of the resulting values.
   * <p>
   * Example:
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
   * @param actual             the actual object that was found during the test
   * @param expected           the object that was expected
   * @param errorMessageFormat the error message to format
   * @param arguments          the arguments referenced by the format specifiers in the errorMessage string.
   * @return The generated assertion error.
   * @see #failure(String, Object...)
   * @see #failWithActualExpectedAndMessage(Object, Object, String, Object...)
   */
  protected AssertionError failureWithActualExpected(Object actual, Object expected, String errorMessageFormat,
                                                     Object... arguments) {
    String errorMessage = Optional.ofNullable(info.overridingErrorMessage())
                                  .orElse(errorMessageFormat.formatted(arguments));
    String description = MessageFormatter.instance().format(info.description(), info.representation(), errorMessage);
    AssertionError assertionError = assertionErrorCreator.assertionError(description, actual, expected, info.representation());
    Failures.instance().removeAssertJRelatedElementsFromStackTraceIfNeeded(assertionError);
    removeCustomAssertRelatedElementsFromStackTraceIfNeeded(assertionError);
    return assertionError;
  }

  /**
   * Utility method to throw an {@link AssertionError} given a {@link org.assertj.core.error.BasicErrorMessageFactory}.
   * <p>
   * Instead of writing ...
   *
   * <pre><code class='java'> throw Failures.instance().failure(info, ShouldBePresent.shouldBePresent());</code></pre>
   * ... you can simply write :
   *
   * <pre><code class='java'> throwAssertionError(info, ShouldBePresent.shouldBePresent());</code></pre>
   *
   * @param errorMessageFactory used to define the error message.
   * @throws AssertionError with a message corresponding to the given {@code BasicErrorMessageFactory}.
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
   * <p>
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
   * org.opentest4j.AssertionFailedError:
   * expected: 0b00000000_00000000_00000000_00000010
   *  but was: 0b00000000_00000000_00000000_00000001</code></pre>
   *
   * @return {@code this} assertion object.
   */
  @CheckReturnValue
  protected SELF inBinary() {
    info.useBinaryRepresentation();
    return myself;
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isEqualTo(Object expected) {
    if (actual instanceof AbstractAssert<?, ?> && throwUnsupportedExceptionOnEquals) {
      throw new UnsupportedOperationException("Attempted to compare an assertion object to another object using 'isEqualTo'. "
                                                + "This is not supported. Perhaps you meant 'isSameAs' instead?");
    }

    return executeAssertion(() -> objects.assertEqual(info, actual, expected));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotEqualTo(Object other) {
    if (actual instanceof AbstractAssert<?, ?> && throwUnsupportedExceptionOnEquals) {
      throw new UnsupportedOperationException("Attempted to compare an assertion object to another object using 'isNotEqualTo'. "
                                                + "This is not supported. Perhaps you meant 'isNotSameAs' instead?");
    }

    return executeAssertion(() -> objects.assertNotEqual(info, actual, other));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void isNull() {
    executeAssertion(() -> objects.assertNull(info, actual));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotNull() {
    return executeAssertion(() -> objects.assertNotNull(info, actual));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isSameAs(Object expected) {
    return executeAssertion(() -> objects.assertSame(info, actual, expected));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotSameAs(Object other) {
    return executeAssertion(() -> objects.assertNotSame(info, actual, other));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isIn(Object... values) {
    return executeAssertion(() -> objects.assertIsIn(info, actual, values));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotIn(Object... values) {
    return executeAssertion(() -> objects.assertIsNotIn(info, actual, values));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isIn(Iterable<?> values) {
    return executeAssertion(() -> objects.assertIsIn(info, actual, values));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotIn(Iterable<?> values) {
    return executeAssertion(() -> objects.assertIsNotIn(info, actual, values));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF is(Condition<? super ACTUAL> condition) {
    return executeAssertion(() -> conditions.assertIs(info, actual, condition));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNot(Condition<? super ACTUAL> condition) {
    return executeAssertion(() -> conditions.assertIsNot(info, actual, condition));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF has(Condition<? super ACTUAL> condition) {
    return executeAssertion(() -> conditions.assertHas(info, actual, condition));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotHave(Condition<? super ACTUAL> condition) {
    return executeAssertion(() -> conditions.assertDoesNotHave(info, actual, condition));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF satisfies(Condition<? super ACTUAL> condition) {
    return executeAssertion(() -> conditions.assertSatisfies(info, actual, condition));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT asInstanceOf(InstanceOfAssertFactory<?, ASSERT> instanceOfAssertFactory) {
    requireNonNull(instanceOfAssertFactory, shouldNotBeNull("instanceOfAssertFactory")::create);

    ValueProvider<?> isInstanceOfValueProvider = type -> {
      if (type instanceof Class<?> clazz) {
        isInstanceOf(clazz);
      } else if (type instanceof ParameterizedType parameterizedType) {
        isInstanceOf((Class<?>) parameterizedType.getRawType());
      }
      return actual;
    };

    return (ASSERT) instanceOfAssertFactory.createAssert(isInstanceOfValueProvider).withAssertionState(myself);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isInstanceOf(Class<?> type) {
    return executeAssertion(() -> objects.assertIsInstanceOf(info, actual, type));
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> SELF isInstanceOfSatisfying(Class<T> type, Consumer<T> requirements) {
    return executeAssertion(() -> {
      objects.assertIsInstanceOf(info, actual, type);
      requireNonNull(requirements, "The Consumer<T> expressing the assertions requirements must not be null");
      requirements.accept((T) actual);
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isInstanceOfAny(Class<?>... types) {
    return executeAssertion(() -> objects.assertIsInstanceOfAny(info, actual, types));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotInstanceOf(Class<?> type) {
    return executeAssertion(() -> objects.assertIsNotInstanceOf(info, actual, type));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotInstanceOfAny(Class<?>... types) {
    return executeAssertion(() -> objects.assertIsNotInstanceOfAny(info, actual, types));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF hasSameClassAs(Object other) {
    return executeAssertion(() -> objects.assertHasSameClassAs(info, actual, other));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF hasToString(String expectedToString) {
    return executeAssertion(() -> objects.assertHasToString(info, actual, expectedToString));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF hasToString(String expectedStringTemplate, Object... args) {
    requireNonNull(expectedStringTemplate, "The expectedStringTemplate must not be null");
    return hasToString(expectedStringTemplate.formatted(args));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotHaveToString(String otherToString) {
    return executeAssertion(() -> objects.assertDoesNotHaveToString(info, actual, otherToString));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotHaveToString(String expectedStringTemplate, Object... args) {
    requireNonNull(expectedStringTemplate, "The expectedStringTemplate must not be null");
    return doesNotHaveToString(expectedStringTemplate.formatted(args));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotHaveSameClassAs(Object other) {
    return executeAssertion(() -> objects.assertDoesNotHaveSameClassAs(info, actual, other));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isExactlyInstanceOf(Class<?> type) {
    return executeAssertion(() -> objects.assertIsExactlyInstanceOf(info, actual, type));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotExactlyInstanceOf(Class<?> type) {
    return executeAssertion(() -> objects.assertIsNotExactlyInstanceOf(info, actual, type));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isOfAnyClassIn(Class<?>... types) {
    return executeAssertion(() -> objects.assertIsOfAnyClassIn(info, actual, types));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF isNotOfAnyClassIn(Class<?>... types) {
    return executeAssertion(() -> objects.assertIsNotOfAnyClassIn(info, actual, types));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @CheckReturnValue
  public AbstractStringAssert<?> asString() {
    return executeAssertionNavigation(() -> {
      objects.assertNotNull(info, actual);
      return Assertions.assertThat(actual.toString()).withAssertionState(myself);
    });
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
   * Example:
   * <pre><code class='java'>assertThat(player.isRookie()).overridingErrorMessage(&quot;Expecting Player &lt;%s&gt; to be a rookie but was not.&quot;, player)
   *                              .isTrue();</code></pre>
   *
   * @param newErrorMessage the error message that will replace the default one provided by Assertj.
   * @param args            the args used to fill the error message as in {@link String#format(String, Object...)}.
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
   * Example:
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
   *
   * @param newErrorMessage the error message that will replace the default one provided by Assertj.
   * @param args            the args used to fill error message as in {@link String#format(String, Object...)}.
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
   *
   * @param supplier the supplier supplies error message that will replace the default one provided by Assertj.
   * @return this assertion object.
   */
  @CheckReturnValue
  public SELF withFailMessage(Supplier<String> supplier) {
    return overridingErrorMessage(supplier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @CheckReturnValue
  public SELF withThreadDumpOnError() {
    Failures.instance().enablePrintThreadDump();
    return myself;
  }

  /**
   * {@inheritDoc}
   */
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
   * @deprecated use {@link #isEqualTo} instead
   */
  @Override
  @Deprecated(since = "3")
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
   * Example:
   * <pre><code class='java'> assertThat(player).matches(p -&gt; p.isRookie());</code></pre>
   *
   * @param predicate the {@link Predicate} to match
   * @return {@code this} assertion object.
   * @throws AssertionError       if {@code actual} does not match the given {@link Predicate}.
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
   * Example:
   * <pre><code class='java'> assertThat(player).matches(p -&gt; p.isRookie(), "is rookie");</code></pre>
   * <p>
   * The error message contains the predicate description, in our example, it is:
   * <pre><code class='java'> Expecting:
   *   &lt;player&gt;
   * to match 'is rookie' predicate.</code></pre>
   *
   * @param predicate            the {@link Predicate} to match
   * @param predicateDescription a description of the {@link Predicate} used in the error message
   * @return {@code this} assertion object.
   * @throws AssertionError       if {@code actual} does not match the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   * @throws NullPointerException if given predicateDescription is null.
   */
  public SELF matches(Predicate<? super ACTUAL> predicate, String predicateDescription) {
    return matches(predicate, new PredicateDescription(predicateDescription));
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
   * @throws AssertionError       if {@code actual} matches the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   */
  public SELF doesNotMatch(Predicate<? super ACTUAL> predicate) {
    // use default PredicateDescription
    return doesNotMatch(predicate, PredicateDescription.GIVEN);
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
   * @param predicate            the {@link Predicate} not to match
   * @param predicateDescription a description of the {@link Predicate} used in the error message
   * @return {@code this} assertion object.
   * @throws AssertionError       if {@code actual} matches the given {@link Predicate}.
   * @throws NullPointerException if given {@link Predicate} is null.
   * @throws NullPointerException if given predicateDescription is null.
   */
  public SELF doesNotMatch(Predicate<? super ACTUAL> predicate, String predicateDescription) {
    return doesNotMatch(predicate, new PredicateDescription(predicateDescription));
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
  public final SELF satisfies(Consumer<? super ACTUAL>... requirements) {
    return executeAssertion(() -> {
      checkArgument(stream(requirements).allMatch(java.util.Objects::nonNull), "No assertions group should be null");
      List<AssertionError> assertionErrors = stream(requirements).map(this::catchOptionalAssertionError)
                                                                 .flatMap(Optional::stream)
                                                                 .collect(toList());
      if (!assertionErrors.isEmpty()) {
        throw multipleAssertionsError(actual, assertionErrors);
      }
    });
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
   * @throws RuntimeException         rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.
   * @throws AssertionError           rethrown as is by the given {@link ThrowingConsumer}
   * @since 3.21.0
   */
  @SafeVarargs
  public final SELF satisfies(ThrowingConsumer<? super ACTUAL>... assertions) {
    return satisfies((Consumer<? super ACTUAL>[]) assertions);
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
   * @throws IllegalArgumentException if any given assertions group is null
   * @since 3.12.0
   */
  @SafeVarargs
  public final SELF satisfiesAnyOf(Consumer<? super ACTUAL>... assertions) {
    return executeAssertion(() -> {
      checkArgument(stream(assertions).allMatch(java.util.Objects::nonNull), "No assertions group should be null");
      // use a for loop over stream to return as soon as one assertion is met
      List<AssertionError> assertionErrors = list();
      for (Consumer<? super ACTUAL> assertionsGroup : assertions) {
        Optional<AssertionError> maybeError = catchOptionalAssertionError(assertionsGroup);
        if (maybeError.isEmpty()) {
          return;
        }
        assertionErrors.add(maybeError.get());
      }
      throw multipleAssertionsError(actual, assertionErrors);
    });
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
   * @throws RuntimeException         rethrown as is by the given {@link ThrowingConsumer} or wrapping any {@link Throwable}.
   * @throws AssertionError           rethrown as is by the given {@link ThrowingConsumer}
   * @since 3.21.0
   */
  @SafeVarargs
  public final SELF satisfiesAnyOf(ThrowingConsumer<? super ACTUAL>... assertions) {
    return satisfiesAnyOf((Consumer<? super ACTUAL>[]) assertions);
  }

  private AssertionError multipleAssertionsError(ACTUAL actual, List<AssertionError> assertionErrors) {
    // we don't allow overriding the error message to avoid loosing all the failed assertions error message.
    return assertionErrorCreator.multipleAssertionsError(info.description(), actual, assertionErrors);
  }

  private SELF matches(Predicate<? super ACTUAL> predicate, PredicateDescription predicateDescription) {
    requireNonNull(predicate, "The predicate must not be null");
    return executeAssertion(() -> {
      if (predicate.test(actual)) {
        return;
      }
      throw Failures.instance().failure(info, shouldMatch(actual, predicate, predicateDescription));
    });
  }

  private SELF doesNotMatch(Predicate<? super ACTUAL> predicate, PredicateDescription predicateDescription) {
    requireNonNull(predicate, "The predicate must not be null");
    return executeAssertion(() -> {
      if (predicate.negate().test(actual)) {
        return;
      }
      throw Failures.instance().failure(info, shouldNotMatch(actual, predicate, predicateDescription));
    });
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

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF hasSameHashCodeAs(Object other) {
    return executeAssertion(() -> objects.assertHasSameHashCodeAs(info, actual, other));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SELF doesNotHaveSameHashCodeAs(Object other) {
    return executeAssertion(() -> objects.assertDoesNotHaveSameHashCodeAs(info, actual, other));
  }

  /**
   * Create a {@link AbstractListAssert} from the given list.
   * <p>
   * this method avoids code duplication when features like extracting/asList need to create a new list assertions.
   *
   * @param <E>       the type of elements.
   * @param newActual new list under test
   * @return a new {@link AbstractListAssert}.
   */
  protected <E> AbstractListAssert<?, List<? extends E>, E, ObjectAssert<E>> newListAssertInstance(List<? extends E> newActual) {
    return new ListAssert<>(newActual);
  }

  SELF withAssertionState(@SuppressWarnings("rawtypes") AbstractAssert assertInstance) {
    this.objects = assertInstance.objects;
    this.assertionErrorHandler = assertInstance.assertionErrorHandler;
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
    return usingRecursiveComparison(new RecursiveComparisonConfiguration(info.representation()));
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
   * If the object under test is a {@link java.util.Map}, the {@code propertyOrField} parameter is used as a key to the map.
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
   * @see AbstractObjectAssert#extracting(String)
   * @see AbstractObjectAssert#extracting(String, InstanceOfAssertFactory)
   * @since 3.16.0
   */
  @CheckReturnValue
  protected <ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(String propertyOrField,
                                                                    AssertFactory<Object, ASSERT> assertFactory) {
    requireNonNull(propertyOrField, shouldNotBeNull("propertyOrField")::create);
    requireNonNull(assertFactory, shouldNotBeNull("assertFactory")::create);
    return executeAssertionNavigation(() -> {
      if (actual == null && assertionErrorHandler != null) {
        ASSERT anAssert = (ASSERT) assertFactory.createAssert((Object) null).withAssertionState(myself);
        throw new FinalAssertionError("can't extract from null", anAssert);
      }
      isNotNull();
      Object value = byName(propertyOrField).apply(actual);
      String extractedPropertyOrFieldDescription = extractedDescriptionOf(propertyOrField);
      String description = mostRelevantDescription(info.description(), extractedPropertyOrFieldDescription);
      //noinspection unchecked
      return (ASSERT) assertFactory.createAssert(value).withAssertionState(myself).as(description);
    });
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
   * @see AbstractObjectAssert#extracting(Function)
   * @see AbstractObjectAssert#extracting(Function, InstanceOfAssertFactory)
   * @since 3.16.0
   */
  @CheckReturnValue
  protected <T, ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(Function<? super ACTUAL, ? extends T> extractor,
                                                                       AssertFactory<T, ASSERT> assertFactory) {
    requireNonNull(extractor, shouldNotBeNull("extractor")::create);
    requireNonNull(assertFactory, shouldNotBeNull("assertFactory")::create);
    isNotNull();
    T extractedValue = extractor.apply(actual);
    @SuppressWarnings("unchecked")
    ASSERT result = (ASSERT) assertFactory.createAssert(extractedValue).withAssertionState(myself);
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ACTUAL actual() {
    return actual;
  }

}
