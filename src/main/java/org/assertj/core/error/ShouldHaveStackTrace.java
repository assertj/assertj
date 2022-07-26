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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.error;

/**
 * Creates an error message indicating that a {@link Throwable} was expected to have a stack
 * trace populated.
 *
 * @author Ashley Scopes
 */
public class ShouldHaveStackTrace extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_STACK_TRACE = String.join(
    "%n",
    "",
    "Expecting actual:",
    "  %s",
    "to have a stack trace, but a stack trace was not provided"
  );

  /**
   * Creates a new <code>{@link ShouldHaveStackTrace}</code>.
   *
   * @param actual the throwable that should have a stack trace.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveStackTrace(Throwable actual) {
    return new ShouldHaveStackTrace(actual);
  }

  private ShouldHaveStackTrace(Throwable actual) {
    super(SHOULD_HAVE_STACK_TRACE, actual);
  }
}
