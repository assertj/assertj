/*
 * Created on Aug 5, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.util.Strings.isEmpty;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.error.AssertionErrorFactory;
import org.fest.assertions.error.ErrorMessage;

/**
 * Failure actions.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Failures {

  private static final Failures INSTANCE = new Failures();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Failures instance() {
    return INSTANCE;
  }

  private Failures() {}

  /**
   * Creates a <code>{@link AssertionError}</code> following this pattern:
   * <ol>
   * <li>creates a <code>{@link AssertionError}</code> using <code>{@link AssertionInfo#overridingErrorMessage()}</code>
   * as the error message if such value is not {@code null}, or</li>
   * <li>uses the given <code>{@link AssertionErrorFactory}</code> to create an <code>{@link AssertionError}</code>,
   * prepending the value of <code>{@link AssertionInfo#description()}</code> to the error message</li>
   * </ol>
   * @param info contains information about the failed assertion.
   * @param factory knows how to create {@code AssertionError}s.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(AssertionInfo info, AssertionErrorFactory factory) {
    AssertionError error = failureIfErrorMessageIsOverriden(info);
    if (error != null) return error;
    return factory.newAssertionError(info.description());
  }

  /**
   * Creates a <code>{@link AssertionError}</code> following this pattern:
   * <ol>
   * <li>creates a <code>{@link AssertionError}</code> using <code>{@link AssertionInfo#overridingErrorMessage()}</code>
   * as the error message if such value is not {@code null}, or</li>
   * <li>uses the given <code>{@link ErrorMessage}</code> to create the detail message of the
   * <code>{@link AssertionError}</code>, prepending the value of <code>{@link AssertionInfo#description()}</code> to
   * the error message</li>
   * </ol>
   * @param info contains information about the failed assertion.
   * @param message knows how to create detail messages for {@code AssertionError}s.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(AssertionInfo info, ErrorMessage message) {
    AssertionError error = failureIfErrorMessageIsOverriden(info);
    if (error != null) return error;
    return new AssertionError(message.create(info.description()));
  }

  private AssertionError failureIfErrorMessageIsOverriden(AssertionInfo info) {
    String overridingErrorMessage = info.overridingErrorMessage();
    if (!isEmpty(overridingErrorMessage)) return failure(overridingErrorMessage);
    return null;
  }

  /**
   * Creates a <code>{@link AssertionError}</code> using the given {@code String} as message.
   * @param message the message of the {@code AssertionError} to create.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(String message) {
    return new AssertionError(message);
  }
}
