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
 * Creates an error message indicating that an assertion that verifies a CharSequence contains only digits failed.
 */
public class ShouldContainOnlyDigits extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainOnlyDigits}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param character first non-digit character found in {@code actual}.
   * @param index index of first non-digit character found in {@code actual}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnlyDigits(CharSequence actual, char character, int index) {
    return new ShouldContainOnlyDigits(actual, character, index);
  }

  /**
   * Creates a new <code>{@link ShouldContainOnlyDigits}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainOnlyDigits(CharSequence actual) {
    return new ShouldContainOnlyDigits(actual);
  }

  private ShouldContainOnlyDigits(CharSequence actual, char character, int index) {
    super("%nExpecting:%n  <%s>%nto contain only digits%nbut found non-digit character <%s> at index <%s>",
          actual, character, index);
  }

  private ShouldContainOnlyDigits(CharSequence actual) {
    super("%nExpecting:%n  <%s>%nto contain only digits%nbut could not found any digits at all", actual);
  }
}
