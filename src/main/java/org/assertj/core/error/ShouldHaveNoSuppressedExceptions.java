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
package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Throwable} has no suppressed exceptions failed.
 */
public class ShouldHaveNoSuppressedExceptions extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveNoSuppressedExceptions}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveNoSuppressedExceptions(Object actual) {
    return new ShouldHaveNoSuppressedExceptions(actual);
  }

  private ShouldHaveNoSuppressedExceptions(Object actual) {
    super("%nExpecting no suppressed exceptions but found: <%s>", actual);
  }
}
