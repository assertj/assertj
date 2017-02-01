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
 * Creates an error message indicating that an assertion that verifies that a {@link Throwable} has a given suppressed exception failed.
 */
public class ShouldHaveSuppressedException extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHaveSuppressedException}</code>.
   * @param actual the throwable to check suppressed exceptions.
   * @param expectedSuppressedException the expected suppressed exception.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveSuppressedException(Throwable actual,
                                                                 Throwable expectedSuppressedException) {
    return new ShouldHaveSuppressedException(actual, expectedSuppressedException);
  }

  private ShouldHaveSuppressedException(Throwable actual, Throwable expectedSuppressedException) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to have a suppressed exception with the following type and message:%n" +
          "  <%s> / <%s>%n" +
          "but could not find any in actual's suppressed exceptions:%n" +
          "  <%s>.",
          actual, expectedSuppressedException.getClass().getName(), expectedSuppressedException.getMessage(),
          actual.getSuppressed());
  }

}
