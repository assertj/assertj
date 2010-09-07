/*
 * Created on Aug 5, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.internal.DescriptionFormatter.format;
import static org.fest.util.Strings.isEmpty;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.description.Description;
import org.fest.assertions.error.AssertionErrorFactory;

/**
 * Failure actions.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public final class Failures {

  /**
   * Throws an <code>{@link AssertionError}</code>. The method creates the exception to throw as follows:
   * <ol>
   * <li>creates a <code>{@link AssertionError}</code> using <code>{@link AssertionInfo#overridingErrorMessage()}</code>
   * as the error message if such value is not {@code null}, or</li>
   * <li>uses the given <code>{@link AssertionErrorFactory}</code> to create an <code>{@link AssertionError}</code>,
   * prepending the value of <code>{@link AssertionInfo#description()}</code> to the error message</li>
   * </ol>
   * @param info contains information about the failed assertion.
   * @param assertionErrorFactory knows how to create {@code AssertionError}s.
   * @throws AssertionError indicating an assertion failure.
   */
  public static void fail(AssertionInfo info, AssertionErrorFactory assertionErrorFactory) {
    String overridingErrorMessage = info.overridingErrorMessage();
    if (!isEmpty(overridingErrorMessage)) throw failure(overridingErrorMessage);
    fail(info.description(), assertionErrorFactory);
  }

  /**
   * Throws an <code>{@link AssertionError}</code>. The exception to throw is created by the given
   * <code>{@link AssertionErrorFactory}</code>.
   * @param description the description of the failed assertion, to be included at the beginning of the error message.
   * @param assertionErrorFactory knows how to create {@code AssertionError}s.
   * @throws AssertionError indicating an assertion failure.
   */
  public static void fail(Description description, AssertionErrorFactory assertionErrorFactory) {
    throw assertionErrorFactory.newAssertionError(format(description));
  }

  /**
   * Creates a <code>{@link AssertionError}</code> using the given {@code String} as message.
   * @param message the message of the {@code AssertionError} to create.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public static AssertionError failure(String message) {
    return new AssertionError(message);
  }

  private Failures() {}
}
