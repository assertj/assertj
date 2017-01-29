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
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;
import static org.assertj.core.error.ShouldHaveCauseExactlyInstance.shouldHaveCauseExactlyInstance;
import static org.assertj.core.error.ShouldHaveCauseInstance.shouldHaveCauseInstance;
import static org.assertj.core.error.ShouldHaveMessage.shouldHaveMessage;
import static org.assertj.core.error.ShouldHaveMessageMatchingRegex.shouldHaveMessageMatchingRegex;
import static org.assertj.core.error.ShouldHaveNoCause.shouldHaveNoCause;
import static org.assertj.core.error.ShouldHaveNoSuppressedExceptions.shouldHaveNoSuppressedExceptions;
import static org.assertj.core.error.ShouldHaveRootCauseExactlyInstance.shouldHaveRootCauseExactlyInstance;
import static org.assertj.core.error.ShouldHaveRootCauseInstance.shouldHaveRootCauseInstance;
import static org.assertj.core.error.ShouldHaveSuppressedException.shouldHaveSuppressedException;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.internal.CommonValidations.checkTypeIsNotNull;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Preconditions.checkNotNull;
import static org.assertj.core.util.Throwables.getRootCause;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Throwable}</code>s.
 * 
 * @author Joel Costigliola
 * @author Libor Ondrusek
 */
public class Throwables {

  private static final Throwables INSTANCE = new Throwables();

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static Throwables instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Throwables() {}

  /**
   * Asserts that the given actual {@code Throwable} message is equal to the given one.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param message the expected message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} is not equal to the given one.
   */
  public void assertHasMessage(AssertionInfo info, Throwable actual, String message) {
    assertNotNull(info, actual);
    if (areEqual(actual.getMessage(), message)) return;
    throw failures.failure(info, shouldHaveMessage(actual, message));
  }

  public void assertHasCause(AssertionInfo info, Throwable actual, Throwable expectedCause) {
    assertNotNull(info, actual);
    Throwable actualCause = actual.getCause();
    if (actualCause == expectedCause) return;
    if (null == expectedCause) {
      assertHasNoCause(info, actual);
      return;
    }
    if (actualCause == null) throw failures.failure(info, shouldHaveCause(actualCause, expectedCause));
    if (!compareThrowable(actualCause, expectedCause))
      throw failures.failure(info, shouldHaveCause(actualCause, expectedCause));
  }

  /**
   * Asserts that the actual {@code Throwable} does not have a cause.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has a cause.
   */
  public void assertHasNoCause(AssertionInfo info, Throwable actual) {
    assertNotNull(info, actual);
    Throwable actualCause = actual.getCause();
    if (actualCause == null) return;
    throw failures.failure(info, shouldHaveNoCause(actual));
  }

  /**
   * Asserts that the message of the actual {@code Throwable} starts with the given description.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param description the description expected to start the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not start with the given description.
   */
  public void assertHasMessageStartingWith(AssertionInfo info, Throwable actual, String description) {
    assertNotNull(info, actual);
    if (actual.getMessage() != null && actual.getMessage().startsWith(description)) return;
    throw failures.failure(info, shouldStartWith(actual.getMessage(), description));
  }

  /**
   * Asserts that the message of the actual {@code Throwable} contains with the given description.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param description the description expected to be contained in the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not contain the given description.
   */
  public void assertHasMessageContaining(AssertionInfo info, Throwable actual, String description) {
    assertNotNull(info, actual);
    if (actual.getMessage() != null && actual.getMessage().contains(description)) return;
    throw failures.failure(info, shouldContain(actual.getMessage(), description));
  }

  /**
   * Asserts that the stack trace of the actual {@code Throwable} contains with the given description.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param description the description expected to be contained in the actual {@code Throwable}'s stack trace.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the stack trace of the actual {@code Throwable} does not contain the given description.
   */
  public void assertHasStackTraceContaining(AssertionInfo info, Throwable actual, String description) {
    assertNotNull(info, actual);
    String stackTrace = org.assertj.core.util.Throwables.getStackTrace(actual);
    if (stackTrace != null && stackTrace.contains(description)) return;
    throw failures.failure(info, shouldContain(stackTrace, description));
  }

  /**
   * Asserts that the message of the actual {@code Throwable} matches with the given regular expression.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param regex the regular expression of value expected to be matched the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not match the given regular expression.
   * @throws NullPointerException if the regex is null
   */
  public void assertHasMessageMatching(AssertionInfo info, Throwable actual, String regex) {
    checkNotNull(regex, "regex must not be null");
    assertNotNull(info, actual);
    if (actual.getMessage() != null && actual.getMessage().matches(regex)) return;
    throw failures.failure(info, shouldHaveMessageMatchingRegex(actual, regex));
  }

  /**
   * Asserts that the message of the actual {@code Throwable} ends with the given description.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param description the description expected to end the actual {@code Throwable}'s message.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the message of the actual {@code Throwable} does not end with the given description.
   */
  public void assertHasMessageEndingWith(AssertionInfo info, Throwable actual, String description) {
    assertNotNull(info, actual);
    if (actual.getMessage() != null && actual.getMessage().endsWith(description)) return;
    throw failures.failure(info, shouldEndWith(actual.getMessage(), description));
  }

  /**
   * Assert that the cause of actual {@code Throwable} is an instance of the given type.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param type the expected cause type.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   */
  public void assertHasCauseInstanceOf(AssertionInfo info, Throwable actual, Class<? extends Throwable> type) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(type);
    if (type.isInstance(actual.getCause())) return;
    throw failures.failure(info, shouldHaveCauseInstance(actual, type));
  }

  /**
   * Assert that the cause of actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param type the expected cause type.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the given
   *           type.
   */
  public void assertHasCauseExactlyInstanceOf(AssertionInfo info, Throwable actual, Class<? extends Throwable> type) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(type);
    Throwable cause = actual.getCause();
    if (cause != null && type.equals(cause.getClass())) return;
    throw failures.failure(info, shouldHaveCauseExactlyInstance(actual, type));
  }

  /**
   * Assert that the root cause of actual {@code Throwable} is an instance of the given type.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param type the expected cause type.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the cause of the actual {@code Throwable} is not an instance of the given type.
   */
  public void assertHasRootCauseInstanceOf(AssertionInfo info, Throwable actual, Class<? extends Throwable> type) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(type);
    if (type.isInstance(getRootCause(actual))) return;
    throw failures.failure(info, shouldHaveRootCauseInstance(actual, type));
  }

  /**
   * Assert that the root cause of actual {@code Throwable} is <b>exactly</b> an instance of the given type.
   * 
   * @param info contains information about the assertion.
   * @param actual the given {@code Throwable}.
   * @param type the expected cause type.
   * @throws NullPointerException if given type is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} is {@code null}.
   * @throws AssertionError if the actual {@code Throwable} has no cause.
   * @throws AssertionError if the root cause of the actual {@code Throwable} is not <b>exactly</b> an instance of the
   *           given type.
   */
  public void assertHasRootCauseExactlyInstanceOf(AssertionInfo info, Throwable actual,
                                                  Class<? extends Throwable> type) {
    assertNotNull(info, actual);
    checkTypeIsNotNull(type);
    Throwable rootCause = getRootCause(actual);
    if (rootCause != null && type.equals(rootCause.getClass())) return;
    throw failures.failure(info, shouldHaveRootCauseExactlyInstance(actual, type));
  }

  public void assertHasNoSuppressedExceptions(AssertionInfo info, Throwable actual) {
    assertNotNull(info, actual);
    Throwable[] suppressed = actual.getSuppressed();
    if (suppressed.length != 0) throw failures.failure(info, shouldHaveNoSuppressedExceptions(suppressed));
  }

  public void assertHasSuppressedException(AssertionInfo info, Throwable actual,
                                           Throwable expectedSuppressedException) {
    assertNotNull(info, actual);
    checkNotNull(expectedSuppressedException, "The expected suppressed exception should not be null");
    Throwable[] suppressed = actual.getSuppressed();
    for (int i = 0; i < suppressed.length; i++) {
      if (compareThrowable(suppressed[i], expectedSuppressedException)) return;
    }
    throw failures.failure(info, shouldHaveSuppressedException(actual, expectedSuppressedException));
  }

  private static void assertNotNull(AssertionInfo info, Throwable actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private static boolean compareThrowable(Throwable actual, Throwable expected) {
    return areEqual(actual.getMessage(), expected.getMessage())
           && areEqual(actual.getClass(), expected.getClass());
  }

}
