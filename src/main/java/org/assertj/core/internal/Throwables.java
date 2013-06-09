/*
 * Created on Jan 26, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.error.ShouldHaveCauseExactlyInstance.shouldHaveCauseExactlyInstance;
import static org.assertj.core.error.ShouldHaveCauseInstance.shouldHaveCauseInstance;
import static org.assertj.core.error.ShouldHaveMessage.shouldHaveMessage;
import static org.assertj.core.error.ShouldHaveNoCause.shouldHaveNoCause;
import static org.assertj.core.error.ShouldHaveRootCauseExactlyInstance.shouldHaveRootCauseExactlyInstance;
import static org.assertj.core.error.ShouldHaveRootCauseInstance.shouldHaveRootCauseInstance;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Throwables.getRootCause;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Throwable}</code>s.
 * 
 * @author Joel Costigliola
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
  Diff diff = new Diff();
  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Throwables() {
  }

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
    // TODO unit test with null exception message
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
    if (type == null) throw new NullPointerException("The given type should not be null");
    assertNotNull(info, actual);
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
    if (type == null) throw new NullPointerException("The given type should not be null");
    assertNotNull(info, actual);
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
    if (type == null) throw new NullPointerException("The given type should not be null");
    assertNotNull(info, actual);
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
  public void assertHasRootCauseExactlyInstanceOf(AssertionInfo info, Throwable actual, Class<? extends Throwable> type) {
    if (type == null) throw new NullPointerException("The given type should not be null");
    assertNotNull(info, actual);
    Throwable rootCause = getRootCause(actual);
    if (rootCause != null && type.equals(rootCause.getClass())) return;
    throw failures.failure(info, shouldHaveRootCauseExactlyInstance(actual, type));
  }

  private static void assertNotNull(AssertionInfo info, Throwable actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
