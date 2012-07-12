/*
 * Created on Jan 26, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldContainString.shouldContain;
import static org.fest.assertions.error.ShouldEndWith.shouldEndWith;
import static org.fest.assertions.error.ShouldHaveMessage.shouldHaveMessage;
import static org.fest.assertions.error.ShouldHaveNoCause.shouldHaveNoCause;
import static org.fest.assertions.error.ShouldStartWith.shouldStartWith;
import static org.fest.util.Objects.areEqual;

import org.fest.assertions.core.AssertionInfo;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Throwable}</code>s.
 * 
 * @author Joel Costigliola
 */
public class Throwables {

  private static final Throwables INSTANCE = new Throwables();

  /**
   * Returns the singleton instance of this class.
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
  Throwables() {}

  /**
   * Asserts that the given actual {@code Throwable} message is equal to the given one.
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

  private static void assertNotNull(AssertionInfo info, Throwable actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
