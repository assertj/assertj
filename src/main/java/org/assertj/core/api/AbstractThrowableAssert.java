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

import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Throwables;
import org.assertj.core.util.VisibleForTesting;

import java.util.IllegalFormatException;

/**
 * Base class for all implementations of assertions for {@link Throwable}s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author David DIDIER
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public abstract class AbstractThrowableAssert<S extends AbstractThrowableAssert<S, A>, A extends Throwable>
    extends AbstractObjectAssert<S, A> {

  @VisibleForTesting
  Throwables throwables = Throwables.instance();

  public AbstractThrowableAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  protected S hasBeenThrown() {
    if (actual == null) throw Failures.instance().failure("Expecting code to raise a throwable.");
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
  public S hasMessage(String message) {
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
   * assertThat(null).hasMessage("%s is not a valid input", "foo");
   * </p>
   *
   * @param message a format string representing the expected message
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   */
  public S hasMessage(String message, Object... parameters) {
    return hasMessage(String.format(message, parameters));
  }

  /**
   * Verifies that the actual {@code Throwable} has a cause similar to the given one, that is with the same type and message
   * (it does not use the {@link Throwable#equals(Object) equals} method for comparison).
   *
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
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has not the given cause.
   */
  public S hasCause(Throwable cause) {
    throwables.assertHasCause(info, actual, cause);
    return myself;
  }

  /**
   * Verifies that the actual {@code Throwable} does not have a cause.
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has a cause.
   */
  public S hasNoCause() {
    throwables.assertHasNoCause(info, actual);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} starts with the given description.
   *
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   */
  public S hasMessageStartingWith(String description) {
    throwables.assertHasMessageStartingWith(info, actual, description);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains the given description.
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   */
  public S hasMessageContaining(String description) {
    throwables.assertHasMessageContaining(info, actual, description);
    return myself;
  }

  /**
   * Verifies that the stack trace of the actual {@code Throwable} contains the given description.
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s stack trace.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the stack trace of the actual {@code Throwable} does not contain the given description.
   */
  public S hasStackTraceContaining(String description) {
    throwables.assertHasStackTraceContaining(info, actual, description);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} matches the given regular expression.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable throwable = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThat(throwable).hasMessageMatching("wrong amount [0-9]*");
   *
   * // assertion will fail
   * assertThat(throwable).hasMessageMatching("wrong amount [0-9]* euros");</code></pre>
   *
   * @param regex the regular expression of value expected to be matched the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not match the given regular expression.
   * @throws NullPointerException if the regex is null
   */
  public S hasMessageMatching(String regex) {
    throwables.assertHasMessageMatching(info, actual, regex);
    return myself;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} ends with the given description.
   *
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   */
  public S hasMessageEndingWith(String description) {
    throwables.assertHasMessageEndingWith(info, actual, description);
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
  public S hasCauseInstanceOf(Class<? extends Throwable> type) {
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
   * </p>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the given
   *           type.
   */
  public S hasCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    throwables.assertHasCauseExactlyInstanceOf(info, actual, type);
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
   * </p>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   */
  public S hasRootCauseInstanceOf(Class<? extends Throwable> type) {
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
   * </p>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the root cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the
   *           given type.
   */
  public S hasRootCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    throwables.assertHasRootCauseExactlyInstanceOf(info, actual, type);
    return myself;
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
   * </p>
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has any suppressed exceptions.
   * @since 2.6.0 / 3.6.0
   */
  public S hasNoSuppressedExceptions() {
    throwables.assertHasNoSuppressedExceptions(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual {@code Throwable} has a suppressed exception similar to the given one, that is with the same type and message
   * (it does not use the {@link Throwable#equals(Object) equals} method for comparison).
   *
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
  public S hasSuppressedException(Throwable suppressedException) {
    throwables.assertHasSuppressedException(info, actual, suppressedException);
    return myself;
  }
}
