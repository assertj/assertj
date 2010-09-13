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
import org.fest.assertions.description.Description;
import org.fest.assertions.error.AssertionErrorFactory;

/**
 * Failure actions.
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
   * @param assertionErrorFactory knows how to create {@code AssertionError}s.
   * @return the created <code>{@link AssertionError}</code>.
   */
  public AssertionError failure(AssertionInfo info, AssertionErrorFactory assertionErrorFactory) {
    String overridingErrorMessage = info.overridingErrorMessage();
    if (!isEmpty(overridingErrorMessage)) return failure(overridingErrorMessage);
    return failure(info.description(), assertionErrorFactory);
  }

  /**
   * Returns the <code>{@link AssertionError}</code> created by the given <code>{@link AssertionErrorFactory}</code>.
   * @param description the description of the failed assertion, to be included at the beginning of the error message.
   * @param assertionErrorFactory knows how to create {@code AssertionError}s.
   * @return the created <code>{@link AssertionError}</code>.
   */
  AssertionError failure(Description description, AssertionErrorFactory assertionErrorFactory) {
    return assertionErrorFactory.newAssertionError(description);
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
