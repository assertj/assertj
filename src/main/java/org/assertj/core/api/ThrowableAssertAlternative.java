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

  ThrowableAssertAlternative(final T actual) {
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
   * Verifies that the message of the actual {@code Throwable} contains with the given description.
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
}
