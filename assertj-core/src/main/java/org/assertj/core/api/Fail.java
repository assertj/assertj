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

import org.assertj.core.internal.Failures;
import org.assertj.core.util.CanIgnoreReturnValue;

/**
 * Common failures.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public final class Fail {

  /**
   * Sets whether we remove elements related to AssertJ from assertion error stack trace.
   *
   * @param removeAssertJRelatedElementsFromStackTrace flag.
   */
  public static void setRemoveAssertJRelatedElementsFromStackTrace(boolean removeAssertJRelatedElementsFromStackTrace) {
    Failures.instance().setRemoveAssertJRelatedElementsFromStackTrace(removeAssertJRelatedElementsFromStackTrace);
  }

  /**
   * Throws an {@link AssertionError} with the given message.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail("boom")));}.
   * @throws AssertionError with the given message.
   */
  @CanIgnoreReturnValue
  public static <T> T fail(String failureMessage) {
    throw Failures.instance().failure(failureMessage);
  }

  /**
   * Throws an {@link AssertionError} with an empty message to be used in code like:
   * <pre><code class='java'> doSomething(optional.orElseGet(() -> fail()));</code></pre>
   *
   * @param <T> dummy return value type
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail()));}.
   * @throws AssertionError with an empty message.
   * @since 3.26.0
   */
  @CanIgnoreReturnValue
  public static <T> T fail() {
    // pass an empty string because passing null results in a "null" error message.
    return fail("");
  }

  /**
   * Throws an {@link AssertionError} with the given message built as {@link String#format(String, Object...)}.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @param args Arguments referenced by the format specifiers in the format string.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail("b%s", ""oom)));}.
   * @throws AssertionError with the given built message.
   */
  @CanIgnoreReturnValue
  public static <T> T fail(String failureMessage, Object... args) {
    return fail(String.format(failureMessage, args));
  }

  /**
   * Throws an {@link AssertionError} with the given message and with the {@link Throwable} that caused the failure.
   *
   * @param <T> dummy return value type
   * @param failureMessage the description of the failed assertion. It can be {@code null}.
   * @param realCause cause of the error.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail("boom", cause)));}.
   * @throws AssertionError with the given message and with the {@link Throwable} that caused the failure.
   */
  @CanIgnoreReturnValue
  public static <T> T fail(String failureMessage, Throwable realCause) {
    AssertionError error = Failures.instance().failure(failureMessage);
    error.initCause(realCause);
    throw error;
  }

  /**
   * Throws an {@link AssertionError} with the {@link Throwable} that caused the failure.
   *
   * @param <T> dummy return value type
   * @param realCause cause of the error.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> fail(cause)));}.
   * @throws AssertionError with the {@link Throwable} that caused the failure.
   */
  @CanIgnoreReturnValue
  public static <T> T fail(Throwable realCause) {
    return fail(null, realCause);
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   *
   * @param <T> dummy return value type
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> failBecauseExceptionWasNotThrown(IOException.class)));}.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   *
   * {@link Fail#shouldHaveThrown(Class)} can be used as a replacement.
   */
  @CanIgnoreReturnValue
  public static <T> T failBecauseExceptionWasNotThrown(Class<? extends Throwable> throwableClass) {
    return shouldHaveThrown(throwableClass);
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   *
   * @param <T> dummy return value type
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> shouldHaveThrown(IOException.class)));}.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   */
  @CanIgnoreReturnValue
  public static <T> T shouldHaveThrown(Class<? extends Throwable> throwableClass) {
    throw Failures.instance().expectedThrowableNotThrown(throwableClass);
  }

  /**
   * Since all its methods are static and the class is final, there is no point on creating a new instance of it.
   */
  private Fail() {}
}
