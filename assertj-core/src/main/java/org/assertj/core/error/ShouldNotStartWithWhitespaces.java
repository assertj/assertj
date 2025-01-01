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
package org.assertj.core.error;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link CharSequence}
 * does not start with whitespace characters.
 */
public class ShouldNotStartWithWhitespaces extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotStartWithWhitespaces}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotStartWithWhitespaces(CharSequence actual) {
    return new ShouldNotStartWithWhitespaces(actual);
  }

  private ShouldNotStartWithWhitespaces(Object actual) {
    super("%n" +
          "Expecting string not to start with whitespaces but found one, string was:%n" +
          "  %s", actual);
  }
}
