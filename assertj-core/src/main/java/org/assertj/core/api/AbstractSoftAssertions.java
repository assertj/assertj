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

import static java.lang.String.format;

import java.util.List;

import org.assertj.core.error.AssertionErrorCreator;
import org.assertj.core.internal.Failures;
import org.assertj.core.util.CanIgnoreReturnValue;

public abstract class AbstractSoftAssertions extends DefaultAssertionErrorCollector
    implements SoftAssertionsProvider, InstanceOfAssertFactories {

  final SoftProxies proxies;

  protected AbstractSoftAssertions() {
    // pass itself as an AssertionErrorCollector instance
    proxies = new SoftProxies(this);
  }

  private static final AssertionErrorCreator ASSERTION_ERROR_CREATOR = new AssertionErrorCreator();

  public static void assertAll(AssertionErrorCollector collector) {
    List<AssertionError> errors = collector.assertionErrorsCollected();
    if (!errors.isEmpty()) throw ASSERTION_ERROR_CREATOR.multipleSoftAssertionsError(errors);
  }

  @Override
  public void assertAll() {
    assertAll(this);
  }

  @Override
  public <SELF extends Assert<? extends SELF, ? extends ACTUAL>, ACTUAL> SELF proxy(Class<SELF> assertClass,
                                                                                    Class<ACTUAL> actualClass, ACTUAL actual) {
    return proxies.createSoftAssertionProxy(assertClass, actualClass, actual);
  }

  /**
   * Fails with the given message.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> softly.fail("boom")));}.
   * @since 2.6.0 / 3.6.0
   */
  @CanIgnoreReturnValue
  public <T> T fail(String failureMessage) {
    AssertionError error = Failures.instance().failure(failureMessage);
    collectAssertionError(error);
    return null;
  }

  /**
   * Fails with an empty message to be used in code like:
   * <pre><code class='java'> doSomething(optional.orElseGet(() -> softly.fail()));</code></pre>
   *
   * @param <T> dummy return value type
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> softly.fail()));}.
   * @since 3.26.0
   */
  @CanIgnoreReturnValue
  public <T> T fail() {
    // pass an empty string because passing null results in a "null" error message.
    return fail("");
  }

  /**
   * Fails with the given message built like {@link String#format(String, Object...)}.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @param args Arguments referenced by the format specifiers in the format string.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> softly.fail("boom")));}.
   * @since 2.6.0 / 3.6.0
   */
  @CanIgnoreReturnValue
  public <T> T fail(String failureMessage, Object... args) {
    return fail(format(failureMessage, args));
  }

  /**
   * Fails with the given message and with the {@link Throwable} that caused the failure.
   *
   * @param <T> dummy return value type
   * @param failureMessage error message.
   * @param realCause cause of the error.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> softly.fail("boom")));}.
   * @since 2.6.0 / 3.6.0
   */
  @CanIgnoreReturnValue
  public <T> T fail(String failureMessage, Throwable realCause) {
    AssertionError error = Failures.instance().failure(failureMessage);
    error.initCause(realCause);
    collectAssertionError(error);
    return null;
  }

  /**
   * Fails with the {@link Throwable} that caused the failure and an empty message.
   * <p>
   * Example:
   * <pre><code class='java'> doSomething(optional.orElseGet(() -> softly.fail(cause)));</code></pre>
   *
   * @param <T> dummy return value type
   * @param realCause cause of the error.
   * @return nothing, it's just to be used in {@code doSomething(optional.orElseGet(() -> softly.fail(cause)));}.
   * @since 3.26.0
   */
  @CanIgnoreReturnValue
  public <T> T fail(Throwable realCause) {
    return fail("", realCause);
  }

  /**
   * Fails with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   *
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   * @since 2.6.0 / 3.6.0
   *
   * {@link Fail#shouldHaveThrown(Class)} can be used as a replacement.
   */
  public void failBecauseExceptionWasNotThrown(Class<? extends Throwable> throwableClass) {
    shouldHaveThrown(throwableClass);
  }

  /**
   * Fails with a message explaining that a {@link Throwable} of given class was expected to be thrown
   * but had not been.
   *
   * @param throwableClass the Throwable class that was expected to be thrown.
   * @throws AssertionError with a message explaining that a {@link Throwable} of given class was expected to be thrown but had
   *           not been.
   * @since 2.6.0 / 3.6.0
   */
  public void shouldHaveThrown(Class<? extends Throwable> throwableClass) {
    AssertionError error = Failures.instance().expectedThrowableNotThrown(throwableClass);
    collectAssertionError(error);
  }

  /**
   * Returns a copy of list of soft assertions collected errors.
   * @return a copy of list of soft assertions collected errors.
   */
  public List<Throwable> errorsCollected() {
    return decorateErrorsCollected(super.assertionErrorsCollected());
  }

}
