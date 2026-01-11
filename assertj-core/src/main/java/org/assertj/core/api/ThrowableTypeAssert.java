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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Throwables.catchThrowable;

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.description.Description;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;

/**
 * Assertion class checking {@link Throwable} type.
 * <p>
 * The class itself does not do much, it delegates the work to {@link ThrowableAssertAlternative} after calling {@link #isThrownBy(ThrowingCallable)}.
 *
 * @param <T> expected type of throwable to assert.
 * @see NotThrownAssert
 */
public class ThrowableTypeAssert<T extends Throwable> implements Descriptable<ThrowableTypeAssert<T>> {

  protected final Class<? extends T> expectedThrowableType;

  protected Description description;

  /**
   * Default constructor.
   *
   * @param throwableType class representing the target (expected) exception.
   */
  public ThrowableTypeAssert(final Class<? extends T> throwableType) {
    this.expectedThrowableType = requireNonNull(throwableType, "throwableType");
  }

  /**
   * Assert that an exception of type {@link T} is thrown by the {@code throwingCallable}
   * and allow to chain assertions on the thrown exception.
   * <p>
   * Example:
   * <pre><code class='java'> assertThatExceptionOfType(IOException.class).isThrownBy(() -&gt; { throw new IOException("boom!"); })
   *                                       .withMessage("boom!"); </code></pre>
   *
   * @param throwingCallable code throwing the exception of expected type
   * @return return a {@link ThrowableAssertAlternative}.
   */
  public ThrowableAssertAlternative<T> isThrownBy(final ThrowingCallable throwingCallable) {
    @SuppressWarnings("unchecked")
    T castThrowable = (T) checkThrowableType(catchThrowable(throwingCallable));
    return buildThrowableTypeAssert(castThrowable).as(description);
  }

  protected Throwable checkThrowableType(Throwable throwable) {
    if (throwable == null) {
      throwAssertionError("%nExpecting code to throw a " + expectedThrowableType.getName() + ", but no throwable was thrown.");
    }
    var info = new WritableAssertionInfo();
    info.description(description);
    Objects.instance().assertIsInstanceOf(info, throwable, expectedThrowableType);
    return throwable;
  }

  /**
   * Asserts that the {@code throwingCallable} does not throw an exception of type {@link T} or does not throw any exception.
   * <p>
   * Example:
   * <pre><code class='java'> assertThatExceptionOfType(IllegalArgumentException.class).isNotThrownBy(() -&gt; { throw new IllegalStateException("boom!"); });</code></pre>
   *
   * @param throwingCallable code will not throw the exception of expected type
   */
  @SuppressWarnings("CatchMayIgnoreException")
  public void isNotThrownBy(final ThrowingCallable throwingCallable) {
    try {
      throwingCallable.call();
    } catch (Throwable throwable) {
      if (expectedThrowableType.isInstance(throwable)) {
        throwAssertionError("Expecting code not to raise a " + expectedThrowableType.getName());
      }
    }
    // if nothing was thrown, the assertion succeeds
  }

  private void throwAssertionError(String message) {
    var info = new WritableAssertionInfo();
    info.description(description);
    throw Failures.instance().failure(info, new BasicErrorMessageFactory(message));
  }

  protected ThrowableAssertAlternative<T> buildThrowableTypeAssert(T throwable) {
    return new ThrowableAssertAlternative<>(throwable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @CheckReturnValue
  public ThrowableTypeAssert<T> describedAs(Description description) {
    this.description = description;
    return this;
  }

}
