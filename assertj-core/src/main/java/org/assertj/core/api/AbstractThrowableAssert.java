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
import static org.assertj.core.error.ShouldNotHaveThrown.shouldNotHaveThrown;
import static org.assertj.core.error.ShouldNotHaveThrownExcept.shouldNotHaveThrownExcept;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.regex.Pattern;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Throwables;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Throwable}s.
 *
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Jack Gough
 * @author Mike Gilchrist
 * @author Paweł Baczyński
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 */
public abstract class AbstractThrowableAssert<SELF extends AbstractThrowableAssert<SELF, ACTUAL>, ACTUAL extends Throwable>
    extends AbstractObjectAssert<SELF, ACTUAL> {

  @VisibleForTesting
  Throwables throwables = Throwables.instance();

  protected AbstractThrowableAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  protected SELF hasBeenThrown() {
    if (actual == null) {
      throw Failures.instance().failure(info, new BasicErrorMessageFactory("%nExpecting code to raise a throwable."));
    }
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} is equal to the given one.
   *
   * @param message the expected message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   */
  public SELF hasMessage(String message) {
    throwables.assertHasMessage(info, actual, message);
    return myself;
  }

  /**
   * Verifies that the message of the actual (@code Throwable) is equal to the given one, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable invalidArgException = new IllegalArgumentException("foo is not a valid input");
   * Throwable throwable = new Throwable(invalidArgException);
   *
   * // This assertion succeeds:
   * assertThat(throwable).hasMessage("%s is not a valid input", "foo");
   *
   * // These assertions fail:
   * assertThat(throwable).hasMessage("%s is not a valid input", "bar");
   * assertThat(throwable).hasMessage("%s is not a valid input", 12);
   * assertThat(null).hasMessage("%s is not a valid input", "foo");</code></pre>
   *
   * @param message a format string representing the expected message
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public SELF hasMessage(String message, Object... parameters) {
    return hasMessage(format(message, parameters));
  }

  /**
   * Verifies that the actual {@code Throwable} has a cause similar to the given one, that is with the same type and message
   * (it does not use the {@link Throwable#equals(Object) equals} method for comparison).
   * <p>
   * Example:
   * <pre><code class='java'> Throwable invalidArgException = new IllegalArgumentException("invalid arg");
   * Throwable throwable = new Throwable(invalidArgException);
   *
   * // This assertion succeeds:
   * assertThat(throwable).hasCause(invalidArgException);
   *
   * // These assertions fail:
   * assertThat(throwable).hasCause(new IllegalArgumentException("bad arg"));
   * assertThat(throwable).hasCause(new NullPointerException());
   * assertThat(throwable).hasCause(null); // prefer hasNoCause()</code></pre>
   *
   * @param cause the expected cause
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has not the given cause.
   */
  public SELF hasCause(Throwable cause) {
    throwables.assertHasCause(info, actual, cause);
    return myself;
  }

  /**
   * Verifies that the actual {@code Throwable} has a cause that refers to the given one, i.e. using == comparison
   * <p>
   * Example:
   * <pre><code class='java'> Throwable invalidArgException = new IllegalArgumentException("invalid arg");
   * Throwable throwable = new Throwable(invalidArgException);
   *
   * // This assertion succeeds:
   * assertThat(throwable).hasCauseReference(invalidArgException);
   *
   * // These assertions fail:
   * assertThat(throwable).hasCauseReference(new IllegalArgumentException("invalid arg"));
   * assertThat(throwable).hasCauseReference(new NullPointerException());
   * assertThat(throwable).hasCauseReference(null); // prefer hasNoCause()</code></pre>
   *
   * @param expected the expected cause
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has a cause that does not refer to the given (i.e. actual.getCause() != cause)
   */
  public SELF hasCauseReference(Throwable expected) {
    throwables.assertHasCauseReference(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the actual {@code Throwable} does not have a cause.
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has a cause.
   */
  public SELF hasNoCause() {
    throwables.assertHasNoCause(info, actual);
    return myself;
  }

  /**
   * Returns a new assertion object that uses the cause of the current Throwable as the actual Throwable under test.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable cause =  new IllegalArgumentException("wrong amount 123");
   * Throwable exception = new Exception("boom!", cause);
   *
   * // typical use:
   * assertThat(throwableWithMessage).cause()
   *                                 .hasMessageStartingWith("wrong amount");</code></pre>
   *
   * @return a new assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have a cause.
   *
   * @since 3.23.0
   */
  public AbstractThrowableAssert<?, ?> cause() {
    throwables.assertHasCause(info, actual);
    return new ThrowableAssert<>(actual.getCause()).withAssertionState(myself);
  }

  /**
   * <p>
   * Returns a new assertion object that uses the cause of the current Throwable as the actual Throwable under test.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable cause =  new IllegalArgumentException("wrong amount 123");
   * Throwable exception = new Exception("boom!", cause);
   *
   * // typical use:
   * assertThat(throwableWithMessage).getCause()
   *                                 .hasMessageStartingWith("wrong amount");</code></pre>
   *
   * @return a new assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have a cause.
   *
   * @since 3.16.0
   * @deprecated use {@link #cause()} instead.
   */
  @Deprecated
  public AbstractThrowableAssert<?, ?> getCause() {
    throwables.assertHasCause(info, actual);
    return new ThrowableAssert<>(actual.getCause()).withAssertionState(myself);
  }

  /**
   * Returns a new assertion object that uses the root cause of the current Throwable as the actual Throwable under test.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable rootCause =  new JdbcException("invalid query");
   * Throwable cause =  new RuntimeException(rootCause);
   * Throwable exception = new Exception("boom!", cause);
   *
   * // typical use:
   * assertThat(throwableWithMessage).rootCause()
   *                                 .hasMessageStartingWith("invalid");</code></pre>
   *
   * @return a new assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have a root cause.
   *
   * @since 3.23.0
   */
  public AbstractThrowableAssert<?, ?> rootCause() {
    throwables.assertHasRootCause(info, actual);
    return new ThrowableAssert<>(org.assertj.core.util.Throwables.getRootCause(actual)).withAssertionState(myself);
  }

  /**
   * <p>
   * Returns a new assertion object that uses the root cause of the current Throwable as the actual Throwable under test.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable rootCause =  new JdbcException("invalid query");
   * Throwable cause =  new RuntimeException(rootCause);
   * Throwable exception = new Exception("boom!", cause);
   *
   * // typical use:
   * assertThat(throwableWithMessage).getRootCause()
   *                                 .hasMessageStartingWith("invalid");</code></pre>
   *
   * @return a new assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have a root cause.
   *
   * @since 3.16.0
   * @deprecated use {@link #rootCause()} instead.
   */
  @Deprecated
  public AbstractThrowableAssert<?, ?> getRootCause() {
    throwables.assertHasRootCause(info, actual);
    return new ThrowableAssert<>(org.assertj.core.util.Throwables.getRootCause(actual)).withAssertionState(myself);
  }

  /**
   * Verifies that the message of the actual {@code Throwable} starts with the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass:
   * assertThat(throwableWithMessage).hasMessageStartingWith("wrong amount");
   *
   * // assertions will fail:
   * assertThat(throwableWithMessage).hasMessageStartingWith("right amount"); </code></pre>
   *
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   */
  public SELF hasMessageStartingWith(String description) {
    throwables.assertHasMessageStartingWith(info, actual, description);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} starts with the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass:
   * assertThat(throwableWithMessage).hasMessageStartingWith("%s amount", "wrong");
   *
   * // assertions will fail:
   * assertThat(throwableWithMessage).hasMessageStartingWith("%s amount", "right"); </code></pre>
   *
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public SELF hasMessageStartingWith(String description, Object... parameters) {
    throwables.assertHasMessageStartingWith(info, actual, format(description, parameters));
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   * Throwable throwableWithoutMessage = new IllegalArgumentException();
   *
   * // assertion will pass:
   * assertThat(throwableWithMessage).hasMessageContaining("123");
   *
   * // assertions will fail:
   * assertThat(throwableWithoutMessage).hasMessageContaining("123");
   * assertThat(throwableWithMessage).hasMessageContaining("234"); </code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   */
  public SELF hasMessageContaining(String description) {
    throwables.assertHasMessageContaining(info, actual, description);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   * Throwable throwableWithoutMessage = new IllegalArgumentException();
   *
   * // assertion will pass:
   * assertThat(throwableWithMessage).hasMessageContaining("amount %d", 123);
   *
   * // assertions will fail:
   * assertThat(throwableWithoutMessage).hasMessageContaining("amount %d", 123);
   * assertThat(throwableWithMessage).hasMessageContaining("%s amount", "right"); </code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public SELF hasMessageContaining(String description, Object... parameters) {
    throwables.assertHasMessageContaining(info, actual, format(description, parameters));
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains all the given values.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   * Throwable throwableWithoutMessage = new IllegalArgumentException();
   *
   * // assertion will pass:
   * assertThat(throwableWithMessage).hasMessageContainingAll("amount", "123");
   *
   * // assertions will fail:
   * assertThat(throwableWithoutMessage).hasMessageContainingAll("123");
   * assertThat(throwableWithMessage).hasMessageContainingAll("234"); </code></pre>
   *
   * @param values the Strings expected to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain all the given values.
   */
  public SELF hasMessageContainingAll(CharSequence... values) {
    throwables.assertHasMessageContainingAll(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} does not contain the given content or is {@code null}.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   * Throwable throwableWithoutMessage = new IllegalArgumentException();
   *
   * // assertions will pass:
   * assertThat(throwableWithMessage).hasMessageNotContaining("234");
   * assertThat(throwableWithoutMessage).hasMessageNotContaining("foo");
   *
   * // assertion will fail:
   * assertThat(throwableWithMessage).hasMessageNotContaining("amount");</code></pre>
   *
   * @param content the content expected not to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} contains the given content.
   * @since 3.12.0
   */
  public SELF hasMessageNotContaining(String content) {
    throwables.assertHasMessageNotContaining(info, actual, content);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} does not contain any of the given values or is {@code null}.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   * Throwable throwableWithoutMessage = new IllegalArgumentException();
   *
   * // assertions will pass:
   * assertThat(throwableWithMessage).hasMessageNotContainingAny("234");
   * assertThat(throwableWithoutMessage).hasMessageNotContainingAny("foo");
   *
   * // assertion will fail:
   * assertThat(throwableWithMessage).hasMessageNotContainingAny("foo", "amount");</code></pre>
   *
   * @param values the contents expected to not be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} contains any of the given values.
   * @since 3.12.0
   */
  public SELF hasMessageNotContainingAny(CharSequence... values) {
    throwables.assertHasMessageNotContainingAny(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the stack trace of the actual {@code Throwable} contains the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThat(throwableWithMessage).hasStackTraceContaining("amount 123");
   *
   * // assertion will fail
   * assertThat(throwableWithMessage).hasStackTraceContaining("456");</code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s stack trace.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the stack trace of the actual {@code Throwable} does not contain the given description.
   */
  public SELF hasStackTraceContaining(String description) {
    throwables.assertHasStackTraceContaining(info, actual, description);
    return myself;
  }

  /**
   * Verifies that the stack trace of the actual {@code Throwable} contains the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion succeeds
   * assertThat(throwableWithMessage).hasStackTraceContaining("%s", amount);
   *
   * // assertion fails
   * assertThat(throwableWithMessage).hasStackTraceContaining("%d", 456);</code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s stack trace.
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the stack trace of the actual {@code Throwable} does not contain the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public SELF hasStackTraceContaining(String description, Object... parameters) {
    throwables.assertHasStackTraceContaining(info, actual, format(description, parameters));
    return myself;
  }

  /**
   * Verifies that the message of the {@code Throwable} under test matches the given regular expression.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwable = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion succeeds
   * assertThat(throwable).hasMessageMatching("wrong amount [0-9]*");
   *
   * // assertion fails
   * assertThat(throwable).hasMessageMatching("wrong amount [0-9]* euros");</code></pre>
   *
   * @param regex the regular expression of value expected to be matched the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not match the given regular expression.
   * @throws NullPointerException if the regex is null
   */
  public SELF hasMessageMatching(String regex) {
    throwables.assertHasMessageMatching(info, actual, regex);
    return myself;
  }

  /**
   * Verifies that the message of the {@code Throwable} under test matches the given regular expression {@link Pattern}.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwable = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion succeeds
   * assertThat(throwable).hasMessageMatching(Pattern.compile("wrong amount [0-9]*"));
   *
   * // assertion fails
   * assertThat(throwable).hasMessageMatching(Pattern.compile("wrong amount [0-9]* euros"));</code></pre>
   *
   * @param regex the regular expression of value expected to be matched the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not match the given regular expression.
   * @throws NullPointerException if the regex is null
   */
  public SELF hasMessageMatching(Pattern regex) {
    throwables.assertHasMessageMatching(info, actual, regex);
    return myself;
  }

  /**
   * Verifies that a sequence of the message of the actual {@code Throwable} matches with
   * the given regular expression (see {@link java.util.regex.Matcher#find()}).<br>
   * The {@link Pattern} used under the hood enables the {@link Pattern#DOTALL} mode.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwable = new IllegalArgumentException(&quot;Dear John,\n&quot; +
   *                                                    &quot;it' s a wrong amount&quot;);
   * // assertion will pass
   * assertThat(throwable).hasMessageFindingMatch(&quot;wrong amount&quot;);
   * assertThat(throwable).hasMessageFindingMatch(&quot;Dear John&quot;);
   * assertThat(throwable).hasMessageFindingMatch(&quot;wrong amount$&quot;);
   *
   * // assertion will fail
   * assertThat(throwable).hasMessageFindingMatch(&quot;Dear John$&quot;);</code></pre>
   *
   * @param regex the regular expression expected to be found in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} doesn't contain any sequence matching with the given regular expression
   * @throws NullPointerException if the regex is null
   * @since 3.12.0
   */
  public SELF hasMessageFindingMatch(String regex) {
    throwables.assertHasMessageFindingMatch(info, actual, regex);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} ends with the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThat(throwableWithMessage).hasMessageEndingWith("123");
   *
   * // assertion will fail
   * assertThat(throwableWithMessage).hasMessageEndingWith("456");</code></pre>
   *
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   */
  public SELF hasMessageEndingWith(String description) {
    throwables.assertHasMessageEndingWith(info, actual, description);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} ends with the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwableWithMessage = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThat(throwableWithMessage).hasMessageEndingWith("%s 123", "amount");
   *
   * // assertion will fail
   * assertThat(throwableWithMessage).hasMessageEndingWith("amount %d", 456);</code></pre>
   *
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public SELF hasMessageEndingWith(String description, Object... parameters) {
    throwables.assertHasMessageEndingWith(info, actual, format(description, parameters));
    return myself;
  }

  /**
   * Verifies that the cause of the actual {@code Throwable} is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new NullPointerException());
   *
   * // assertions will pass
   * assertThat(throwable).hasCauseInstanceOf(NullPointerException.class);
   * assertThat(throwable).hasCauseInstanceOf(RuntimeException.class);
   *
   * // assertion will fail
   * assertThat(throwable).hasCauseInstanceOf(IllegalArgumentException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   */
  public SELF hasCauseInstanceOf(Class<? extends Throwable> type) {
    throwables.assertHasCauseInstanceOf(info, actual, type);
    return myself;
  }

  /**
   * Verifies that the cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new NullPointerException());
   *
   * // assertion will pass
   * assertThat(throwable).hasCauseExactlyInstanceOf(NullPointerException.class);
   *
   * // assertions will fail (even if NullPointerException is a RuntimeException since we want an exact match)
   * assertThat(throwable).hasCauseExactlyInstanceOf(RuntimeException.class);
   * assertThat(throwable).hasCauseExactlyInstanceOf(IllegalArgumentException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the given
   *           type.
   */
  public SELF hasCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    throwables.assertHasCauseExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /**
   * Verifies that the actual {@code Throwable} has a root cause similar to the given one, that is with the same type and message
   * (it does not use the {@link Throwable#equals(Object) equals} method for comparison).
   * <p>
   * Example:
   * <pre><code class='java'> Throwable invalidArgException = new IllegalArgumentException("invalid arg");
   * Throwable throwable = new Throwable(new RuntimeException(invalidArgException));
   *
   * // This assertion succeeds:
   * assertThat(throwable).hasRootCause(invalidArgException);
   *
   * // These assertions fail:
   * assertThat(throwable).hasRootCause(new IllegalArgumentException("bad arg"));
   * assertThat(throwable).hasRootCause(new RuntimeException());
   * assertThat(throwable).hasRootCause(null); // prefer hasNoCause()</code></pre>
   *
   * @param cause the expected root cause
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has not the given cause.
   * @since 3.12.0
   */
  public SELF hasRootCause(Throwable cause) {
    throwables.assertHasRootCause(info, actual, cause);
    return myself;
  }

  /**
   * Verifies that the root cause of the actual {@code Throwable} is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException()));
   *
   * // assertions will pass
   * assertThat(throwable).hasRootCauseInstanceOf(NullPointerException.class);
   * assertThat(throwable).hasRootCauseInstanceOf(RuntimeException.class);
   *
   * // assertion will fail
   * assertThat(throwable).hasRootCauseInstanceOf(IllegalStateException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   */
  public SELF hasRootCauseInstanceOf(Class<? extends Throwable> type) {
    throwables.assertHasRootCauseInstanceOf(info, actual, type);
    return myself;
  }

  /**
   * Verifies that the root cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException()));
   *
   * // assertion will pass
   * assertThat(throwable).hasRootCauseExactlyInstanceOf(NullPointerException.class);
   *
   * // assertions will fail (even if NullPointerException is a RuntimeException since we want an exact match)
   * assertThat(throwable).hasRootCauseExactlyInstanceOf(RuntimeException.class);
   * assertThat(throwable).hasRootCauseExactlyInstanceOf(IllegalStateException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the root cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the
   *           given type.
   */
  public SELF hasRootCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    throwables.assertHasRootCauseExactlyInstanceOf(info, actual, type);
    return myself;
  }

  /**
   * Verifies that the message of the root cause of the actual {@code Throwable} is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException("object")));
   *
   * // assertion will pass
   * assertThat(throwable).hasRootCauseMessage("object");
   *
   * // assertions will fail
   * assertThat((Throwable) null).hasRootCauseMessage("object");
   * assertThat(throwable).hasRootCauseMessage("another object");
   * assertThat(new Throwable()).hasRootCauseMessage("object");
   * assertThat(new Throwable(new NullPointerException())).hasRootCauseMessage("object");</code></pre>
   *
   * @param message the expected root cause message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the root cause of the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the root cause of the actual {@code Throwable} is not equal to
   *                        the given one.
   * @since 3.14.0
   */
  public SELF hasRootCauseMessage(String message) {
    throwables.assertHasRootCauseMessage(info, actual, message);
    return myself;
  }

  /**
   * Verifies that the message of the root cause of the actual {@code Throwable} is equal to the given one, after
   * being formatted using {@link String#format(String, Object...)} method.
   * <p>
   * Example:
   * <pre><code class='java'>Throwable throwable = new Throwable(new IllegalStateException(new NullPointerException("expected message")));
   *
   * // assertion will pass
   * assertThat(throwable).hasRootCauseMessage("%s %s", "expected", "message");
   *
   * // assertions will fail
   * assertThat((Throwable) null).hasRootCauseMessage("%s %s", "expected", "message");
   * assertThat(throwable).hasRootCauseMessage("%s", "message");
   * assertThat(new Throwable()).hasRootCauseMessage("%s %s", "expected", "message");
   * assertThat(new Throwable(new NullPointerException())).hasRootCauseMessage("%s %s", "expected", "message");</code></pre>
   *
   * @param message the expected root cause message.
   * @param parameters argument referenced by the format specifiers in the format string.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the root cause of the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the root cause of the actual {@code Throwable} is not equal to the given one.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   * @since 3.14.0
   */
  public SELF hasRootCauseMessage(String message, Object... parameters) {
    return hasRootCauseMessage(format(message, parameters));
  }

  /**
   * Verifies that the actual {@code Throwable} has no suppressed exceptions.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new Throwable()).hasNoSuppressedExceptions();
   *
   * // assertion will fail
   * Throwable throwableWithSuppressedException = new Throwable();
   * throwableWithSuppressedException.addSuppressed(new IllegalArgumentException());
   * assertThat(throwableWithSuppressedException).hasNoSuppressedExceptions();</code></pre>
   *
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has any suppressed exceptions.
   * @since 2.6.0 / 3.6.0
   */
  public SELF hasNoSuppressedExceptions() {
    throwables.assertHasNoSuppressedExceptions(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Throwable} has a suppressed exception similar to the given one, that is with the same type and message
   * (it does not use the {@link Throwable#equals(Object) equals} method for comparison).
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable();
   * Throwable invalidArgException = new IllegalArgumentException("invalid argument");
   * throwable.addSuppressed(invalidArgException);
   *
   * // These assertions succeed:
   * assertThat(throwable).hasSuppressedException(invalidArgException);
   * assertThat(throwable).hasSuppressedException(new IllegalArgumentException("invalid argument"));
   *
   * // These assertions fail:
   * assertThat(throwable).hasSuppressedException(new IllegalArgumentException("invalid parameter"));
   * assertThat(throwable).hasSuppressedException(new NullPointerException());</code></pre>
   *
   * @param suppressedException the expected suppressed exception
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} does not have the given suppressed exception.
   * @since 2.6.0 / 3.6.0
   */
  public SELF hasSuppressedException(Throwable suppressedException) {
    throwables.assertHasSuppressedException(info, actual, suppressedException);
    return myself;
  }

  /**
   * Verifies that the {@link org.assertj.core.api.ThrowableAssert.ThrowingCallable} didn't raise a throwable.
   * <p>
   * Example :
   * <pre><code class='java'> assertThatCode(() -&gt; foo.bar()).doesNotThrowAnyException();</code></pre>
   *
   * @throws AssertionError if the actual statement raised a {@code Throwable}.
   * @since 3.7.0
   */
  public void doesNotThrowAnyException() {
    if (actual != null) throw Failures.instance().failure(info, shouldNotHaveThrown(actual));
  }

  /**
   * Verifies that the {@link org.assertj.core.api.ThrowableAssert.ThrowingCallable} didn't raise a throwable
   * except matching the provided type(s).
   * <p>
   * Example:
   * <pre><code class='java'> void foo() {
   *   throw new IllegalArgumentException();
   * }
   *
   * void bar() {
   * }
   *
   * // assertions succeed:
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(IllegalArgumentException.class);
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(RuntimeException.class);
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(IllegalArgumentException.class, IllegalStateException.class);
   * assertThatCode(() -&gt; bar()).doesNotThrowAnyExceptionExcept();
   *
   * // assertions fails:
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept();
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(NumberFormatException.class);
   * assertThatCode(() -&gt; foo()).doesNotThrowAnyExceptionExcept(NumberFormatException.class, IOException.class);</code></pre>
   *
   * @param ignoredExceptionTypes types allowed to be thrown.
   * @throws AssertionError if the actual statement raised a {@code Throwable} with type other than provided one(s).
   * @since 3.26.0
   */
  @SafeVarargs
  public final void doesNotThrowAnyExceptionExcept(Class<? extends Throwable>... ignoredExceptionTypes) {
    if (isNotAnInstanceOfAny(ignoredExceptionTypes))
      throwAssertionError(shouldNotHaveThrownExcept(actual, ignoredExceptionTypes));
  }

  private boolean isNotAnInstanceOfAny(Class<? extends Throwable>[] exceptionTypes) {
    if (actual == null) return false;
    return Arrays.stream(exceptionTypes).noneMatch(ex -> ex.isAssignableFrom(actual.getClass()));
  }

  /**
   * A shortcut for <code>extracting(Throwable::getMessage, as(InstanceOfAssertFactories.STRING))</code> which allows 
   * to extract a throwable's message and then execute assertions on it.
   * <p>
   * Note that once you have navigated to the throwable's message you can't navigate back to the throwable.
   * <p>
   * Example :
   * <pre><code class='java'> Throwable throwable = new Throwable("boom!");
   *
   * // assertions succeed:
   * assertThat(throwable).message().startsWith("boo")
   *                                .endsWith("!");
   *                                
   * // assertion fails:
   * assertThat(throwable).message().isEmpty();</code></pre>
   *
   * @return the created {@link StringAssert} on the throwable message.
   * @since 3.22.0
   */
  public AbstractStringAssert<?> message() {
    objects.assertNotNull(info, actual);
    return new StringAssert(actual.getMessage());
  }

}
