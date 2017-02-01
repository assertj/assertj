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
 * Creates an error message indicating that an assertion that verifies that a {@code CharSequence} matches a pattern failed.
 * 
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 */
public class ShouldMatchPattern extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldMatchPattern}</code>.
   * @param actual the actual value in the failed assertion.
   * @param pattern a regular expression pattern.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldMatch(CharSequence actual, CharSequence pattern) {
    return new ShouldMatchPattern(actual, pattern);
  }

  private ShouldMatchPattern(CharSequence actual, CharSequence pattern) {
    super("%nExpecting:%n %s%nto match pattern:%n %s", actual, pattern);
  }
}
