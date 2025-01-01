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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.description.Description;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertion class checking {@link Throwable} type.
 * <p>
 * The class itself does not do much, it delegates the work to {@link ThrowableAssertAlternative} after calling {@link #isThrownBy(ThrowingCallable)}.
 *
 * @param <T> type of throwable to be thrown.
 * @see NotThrownAssert
 */
public class ThrowableTypeAssert<T extends Throwable> implements Descriptable<ThrowableTypeAssert<T>> {

  @VisibleForTesting
  protected final Class<? extends T> expectedThrowableType;

  protected Description description;

  /**
   * Default constructor.
   *
   * @param throwableType class representing the target (expected) exception.
   */
  public ThrowableTypeAssert(final Class<? extends T> throwableType) {
    this.expectedThrowableType = requireNonNull(throwableType, "exceptionType");
  }

  /**
   * Assert that an exception of type T is thrown by the {@code throwingCallable}
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
    Throwable throwable = ThrowableAssert.catchThrowable(throwingCallable);
    checkThrowableType(throwable);
    @SuppressWarnings("unchecked")
    T castThrowable = (T) throwable;
    return buildThrowableTypeAssert(castThrowable).as(description);
  }

  protected void checkThrowableType(Throwable throwable) {
    assertThat(throwable).as(description).hasBeenThrown().isInstanceOf(expectedThrowableType);
  }

  protected ThrowableAssertAlternative<T> buildThrowableTypeAssert(T throwable) {
    return new ThrowableAssertAlternative<>(throwable);
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public ThrowableTypeAssert<T> describedAs(Description description) {
    this.description = description;
    return this;
  }

}
