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

import org.assertj.core.internal.Failures;


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
   * @return nothing, it's just to be used in doSomething(optional.orElse(() -&gt; fail("boom")));.
   * @throws AssertionError with the given message.
   */
  public static <T> T fail(String failureMessage) {
    throw Failures.instance().failure(failureMessage);
  }

  /**
   * Throws an {@link AssertionError} with the given message built as {@link String#format(String, Object...)}.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @param args Arguments referenced by the format specifiers in the format string.
   * @return nothing, it's just to be used in doSomething(optional.orElse(() -&gt; fail("b%s", ""oom)));.
   * @throws AssertionError with the given built message.
   */
  public static <T> T fail(String failureMessage, Object... args) {
    return fail(String.format(failureMessage, args));
  }

  /**
   * Throws an {@link AssertionError} with the given message and with the {@link Throwable} that caused the failure.
   *
   * @param <T> dummy return value type
   * @param failureMessage the description of the failed assertion. It can be {@code null}.
   * @param realCause cause of the error.
   * @return nothing, it's just to be used in doSomething(optional.orElse(() -&gt; fail("boom", cause)));.
   * @throws AssertionError with the given message and with the {@link Throwable} that caused the failure.
   */
  public static <T> T fail(String failureMessage, Throwable realCause) {
    AssertionError error = Failures.instance().failure(failureMessage);
    error.initCause(realCause);
    throw error;
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   *
   * @param <T> dummy return value type
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @return nothing, it's just to be used in doSomething(optional.orElse(() -&gt; failBecauseExceptionWasNotThrown(IOException.class)));.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   *
   * {@link Fail#shouldHaveThrown(Class)} can be used as a replacement.
   */
  public static <T> T failBecauseExceptionWasNotThrown(Class<? extends Throwable> throwableClass) {
    return shouldHaveThrown(throwableClass);
  }

  /**
   * Throws an {@link AssertionError} with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   *
   * @param <T> dummy return value type
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @return nothing, it's just to be used in doSomething(optional.orElse(() -&gt; shouldHaveThrown(IOException.class)));.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   */
  public static <T> T shouldHaveThrown(Class<? extends Throwable> throwableClass) {
    throw Failures.instance().expectedThrowableNotThrown(throwableClass);
  }

  /**
   * This constructor is protected to make it possible to subclass this class. Since all its methods are static, there is no point
   * on creating a new instance of it.
   */
  protected Fail() {}
}
