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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import java.util.IllegalFormatException;

import org.assertj.core.description.Description;
import org.assertj.core.util.CheckReturnValue;

/**
 * Assertion methods for {@link java.lang.Throwable} similar to {@link ThrowableAssert} but with assertions methods named
 * differently to make testing code fluent (ex : <code>withMessage</code> instead of <code>hasMessage</code>.
 * <pre><code class='java'> assertThatExceptionOfType(IOException.class)
 *           .isThrownBy(() -&gt; { throw new IOException("boom! tcha!"); });
 *           .withMessage("boom! %s", "tcha!"); </code></pre>
 * This class is linked with the {@link ThrowableTypeAssert} and allow to check that an exception
 * type is thrown by a lambda.
 */
public class ThrowableAssertAlternative<T extends Throwable> extends AbstractAssert<ThrowableAssertAlternative<T>, T> {

  private ThrowableAssert delegate;

  public ThrowableAssertAlternative(final T actual) {
    super(actual, ThrowableAssertAlternative.class);
    delegate = new ThrowableAssert(actual);
  }

  /**
   * Verifies that the message of the actual {@code Throwable} is equal to the given one.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessage("wrong amount 123");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessage("wrong amount 123 euros");</code></pre>
   *
   * @param message the expected message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   * @see AbstractThrowableAssert#hasMessage(String)
   */
  public ThrowableAssertAlternative<T> withMessage(String message) {
    delegate.hasMessage(message);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} is equal to the given one built using {@link String#format(String, Object...)} syntax.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessage("wrong amount %s, "123");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessage("wrong amount 123 euros");</code></pre>
   *
   * @param message a format string representing the expected message
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   * @see AbstractThrowableAssert#hasMessage(String)
   */
  public ThrowableAssertAlternative<T> withMessage(String message, Object... parameters) {
    delegate.hasMessage(message, parameters);
    return this;
  }

  /**
   * Verifies that the actual {@code Throwable} has a cause similar to the given one, that is with same type and message
   * (it does not use {@link Throwable#equals(Object) equals} method for comparison).
   * <p>
   * Example:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("invalid arg");
   * Throwable wrappingException = new Throwable(illegalArgumentException);
   *
   * // This assertion succeeds:
   *
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw wrappingException;})
   *           .withCause(illegalArgumentException);
   *
   * // These assertions fail:
   *
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw wrappingException;})
   *           .withCause(new IllegalArgumentException("bad arg"));
   *
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw wrappingException;})
   *           .withCause(new NullPointerException());
   *
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw wrappingException;})
   *           .withCause(null);</code></pre>
   *
   * @param cause the expected cause.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has not the given cause.
   * @see AbstractThrowableAssert#hasCause(Throwable)
   */
  public ThrowableAssertAlternative<T> withCause(Throwable cause) {
    delegate.hasCause(cause);
    return this;
  }

  /**
   * Verifies that the actual {@code Throwable} does not have a cause.
   * <p>
   * Example:
   * <pre><code class='java'> IllegalArgumentException exception = new IllegalArgumentException();
   *
   * // This assertion succeeds:
   * assertThatExceptionOfType(IllegalArgumentException.class)
   *           .isThrownBy(() -&gt; {throw exception;})
   *           .withNoCause();
   *
   * // These assertion fails:
   * Throwable illegalArgumentException = new Throwable(exception);
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withNoCause();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has a cause.
   * @see AbstractThrowableAssert#hasNoCause()
   */
  public ThrowableAssertAlternative<T> withNoCause() {
    delegate.hasNoCause();
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} starts with the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageStartingWith("wrong amount");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageStartingWith("right amount");</code></pre>
   *
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   * @see AbstractThrowableAssert#hasMessageStartingWith(String)
   */
  public ThrowableAssertAlternative<T> withMessageStartingWith(String description) {
    delegate.hasMessageStartingWith(description);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} starts with the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageStartingWith("%s amount", "wrong");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageStartingWith("%s amount", "right");</code></pre>
   *
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   * @see AbstractThrowableAssert#hasMessageStartingWith(String, Object...)
   */
  public ThrowableAssertAlternative<T> withMessageStartingWith(String description, Object... parameters) {
    delegate.hasMessageStartingWith(description, parameters);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageContaining("amount");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageContaining("456");</code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   * @see AbstractThrowableAssert#hasMessageContaining(String)
   */
  public ThrowableAssertAlternative<T> withMessageContaining(String description) {
    delegate.hasMessageContaining(description);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageContaining("%s", amount);
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageContaining("%d", 456);</code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   * @see AbstractThrowableAssert#hasMessageContaining(String, Object...)
   */
  public ThrowableAssertAlternative<T> withMessageContaining(String description, Object... parameters) {
    delegate.hasMessageContaining(description, parameters);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} contains all the given values.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageContainingAll("amount", "123");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageContainingAll("456");</code></pre>
   *
   * @param values the Strings expected to be contained in the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain all the given values.
   * @see AbstractThrowableAssert#hasMessageContainingAll(CharSequence...)
   */
  public ThrowableAssertAlternative<T> withMessageContainingAll(CharSequence... values) {
    delegate.hasMessageContainingAll(values);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} does not contain the given content or is null.
   * <p>
   * Examples:
   * <pre><code class='java'> //assertions will pass
   * assertThatExceptionOfType(Exception.class)
   *           .isThrownBy(codeThrowing(new Exception("boom")))
   *           .withMessageNotContaining("bam");
   *
   * assertThatExceptionOfType(Exception.class)
   *           .isThrownBy(codeThrowing(new Exception()))
   *           .withMessageNotContaining("bam");
   *
   * //assertion will fail
   * assertThatExceptionOfType(Exception.class)
   *           .isThrownBy(codeThrowing(new Exception("boom")))
   *           .withMessageNotContaining("boom");</code></pre>
   *
   * @param content the content expected to not be contained in the actual {@code Throwable}'s message.
   * @return this assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} contains the given content.
   * @see AbstractThrowableAssert#hasMessageNotContaining(String)
   */
  public ThrowableAssertAlternative<T> withMessageNotContaining(String content) {
    delegate.hasMessageNotContaining(content);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} does not contain any of the given values or is {@code null}.
   * <p>
   * Examples:
   * <pre><code class='java'> //assertions will pass
   * assertThatExceptionOfType(Exception.class)
   *           .isThrownBy(codeThrowing(new Exception("boom")))
   *           .withMessageNotContainingAny("bam");
   *
   * assertThatExceptionOfType(Exception.class)
   *           .isThrownBy(codeThrowing(new Exception()))
   *           .withMessageNotContainingAny("bam");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Exception.class)
   *           .isThrownBy(codeThrowing(new Exception("boom")))
   *           .withMessageNotContainingAny("bam", "boom");</code></pre>
   *
   * @param values the contents expected to not be contained in the actual {@code Throwable}'s message.
   * @return this assertion object
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} contains any of the given values.
   * @see AbstractThrowableAssert#hasMessageNotContainingAny(CharSequence...)
   */
  public ThrowableAssertAlternative<T> withMessageNotContainingAny(CharSequence... values) {
    delegate.hasMessageNotContainingAny(values);
    return this;
  }

  /**
   * Verifies that the stack trace of the actual {@code Throwable} contains with the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withStackTraceContaining("amount");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withStackTraceContaining("456");</code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s stack trace.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the stack trace of the actual {@code Throwable} does not contain the given description.
   * @see AbstractThrowableAssert#hasStackTraceContaining(String)
   */
  public ThrowableAssertAlternative<T> withStackTraceContaining(String description) {
    delegate.hasStackTraceContaining(description);
    return this;
  }

  /**
   * Verifies that the stack trace of the actual {@code Throwable} contains with the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withStackTraceContaining("%s", amount);
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withStackTraceContaining("%d", 456);</code></pre>
   *
   * @param description the description expected to be contained in the actual {@code Throwable}'s stack trace.
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the stack trace of the actual {@code Throwable} does not contain the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   * @see AbstractThrowableAssert#hasStackTraceContaining(String, Object...)
   */
  public ThrowableAssertAlternative<T> withStackTraceContaining(String description, Object... parameters) {
    delegate.hasStackTraceContaining(description, parameters);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} matches with the given regular expression.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageMatching("wrong amount [0-9]*");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageMatching("wrong amount [0-9]* euros");</code></pre>
   *
   * @param regex the regular expression of value expected to be matched the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not match the given regular expression.
   * @throws NullPointerException if the regex is null
   * @see AbstractThrowableAssert#hasMessageMatching(String)
   */
  public ThrowableAssertAlternative<T> withMessageMatching(String regex) {
    delegate.hasMessageMatching(regex);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} ends with the given description.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageEndingWith("123");
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageEndingWith("456");</code></pre>
   *
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   * @see AbstractThrowableAssert#hasMessageEndingWith(String)
   */
  public ThrowableAssertAlternative<T> withMessageEndingWith(String description) {
    delegate.hasMessageEndingWith(description);
    return this;
  }

  /**
   * Verifies that the message of the actual {@code Throwable} ends with the given description, after being formatted using
   * the {@link String#format} method.
   * <p>
   * Examples:
   * <pre><code class='java'> Throwable illegalArgumentException = new IllegalArgumentException("wrong amount 123");
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageEndingWith("%d", 123);
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw illegalArgumentException;})
   *           .withMessageEndingWith("%d", 456);</code></pre>
   *
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @param parameters argument referenced by the format specifiers in the format string
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   * @throws IllegalFormatException if the message contains an illegal syntax according to {@link String#format(String, Object...)}.
   * @see AbstractThrowableAssert#hasMessageEndingWith(String, Object...)
   */
  public ThrowableAssertAlternative<T> withMessageEndingWith(String description, Object... parameters) {
    delegate.hasMessageEndingWith(description, parameters);
    return this;
  }

  /**
   * Verifies that the cause of the actual {@code Throwable} is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new NullPointerException());
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withCauseInstanceOf(NullPointerException.class);
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withCauseInstanceOf(RuntimeException.class);
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withCauseInstanceOf(IllegalArgumentException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   * @see AbstractThrowableAssert#hasCauseInstanceOf(Class)
   */
  public ThrowableAssertAlternative<T> withCauseInstanceOf(Class<? extends Throwable> type) {
    delegate.hasCauseInstanceOf(type);
    return this;
  }

  /**
   * Verifies that the cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(new NullPointerException());
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withCauseExactlyInstanceOf(NullPointerException.class);
   *
   * // assertions will fail (even if NullPointerException is a RuntimeException since we want an exact match)
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withCauseExactlyInstanceOf(RuntimeException.class);
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withCauseExactlyInstanceOf(IllegalArgumentException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the given
   *           type.
   * @see AbstractThrowableAssert#hasCauseExactlyInstanceOf(Class)
   */
  public ThrowableAssertAlternative<T> withCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    delegate.hasCauseExactlyInstanceOf(type);
    return this;
  }

  /**
   * Verifies that the root cause of the actual {@code Throwable} is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(
   *                            new IllegalStateException(
   *                                new NullPointerException()));
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withRootCauseInstanceOf(NullPointerException.class);
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withRootCauseInstanceOf(RuntimeException.class);
   *
   * // assertion will fail
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withRootCauseInstanceOf(IllegalStateException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   * @see AbstractThrowableAssert#hasRootCauseInstanceOf(Class)
   */
  public ThrowableAssertAlternative<T> withRootCauseInstanceOf(Class<? extends Throwable> type) {
    delegate.hasRootCauseInstanceOf(type);
    return this;
  }

  /**
   * Verifies that the root cause of the actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> Throwable throwable = new Throwable(
   *                            new IllegalStateException(
   *                                new NullPointerException()));
   *
   * // assertion will pass
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withRootCauseExactlyInstanceOf(NullPointerException.class);
   *
   * // assertion will fail (even if NullPointerException is a RuntimeException since we want an exact match)
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withRootCauseExactlyInstanceOf(RuntimeException.class);
   * assertThatExceptionOfType(Throwable.class)
   *           .isThrownBy(() -&gt; {throw throwable;})
   *           .withRootCauseExactlyInstanceOf(IllegalStateException.class);</code></pre>
   *
   * @param type the expected cause type.
   * @return this assertion object.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the root cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the
   *           given type.
   * @see AbstractThrowableAssert#hasRootCauseExactlyInstanceOf(Class)
   */
  public ThrowableAssertAlternative<T> withRootCauseExactlyInstanceOf(Class<? extends Throwable> type) {
    delegate.hasRootCauseExactlyInstanceOf(type);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public ThrowableAssertAlternative<T> describedAs(String description, Object... args) {
    delegate.describedAs(description, args);
    return super.describedAs(description, args);
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public ThrowableAssertAlternative<T> describedAs(Description description) {
    delegate.describedAs(description);
    return super.describedAs(description);
  }

  /**
   * Checks if the actual {@link Throwable} has a cause and returns a new assertion object where the
   * cause becomes the actual Throwable in order to further assert properties of the cause {@link Throwable}
   *
   * @return a new assertion object with the cause of the current actual becoming the new actual
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   *
   * @since 3.16.0
   */
  public ThrowableAssertAlternative<?> havingCause() {
    AbstractThrowableAssert<?,?> causeAssert = delegate.getCause();
    return new ThrowableAssertAlternative<>(causeAssert.actual);
  }

  /**
   * Checks if the actual {@link Throwable} has a root cause and returns a new assertion object where the
   * root cause becomes the actual Throwable in order to further assert properties of the cause {@link Throwable}
   *
   * @return a new assertion object with the root cause of the current actual becoming the new actual
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no root cause.
   *
   * @since 3.16.0
   */
  public ThrowableAssertAlternative<?> havingRootCause() {
    AbstractThrowableAssert<?, ?> rootCauseAssert = delegate.getRootCause();
    return new ThrowableAssertAlternative<>(rootCauseAssert.actual);
  }

}
