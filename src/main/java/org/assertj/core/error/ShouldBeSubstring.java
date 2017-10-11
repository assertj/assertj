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

import org.assertj.core.internal.ComparisonStrategy;

public class ShouldBeSubstring extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link org.assertj.core.error.ShouldBeSubstring}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param comparisonStrategy the {@link ComparisonStrategy} used
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeSubstring(CharSequence actual, CharSequence expected,
                                                      ComparisonStrategy comparisonStrategy) {
    return new ShouldBeSubstring(actual, expected, comparisonStrategy);
  }

  private ShouldBeSubstring(CharSequence actual, CharSequence expected, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n  <%s>%nto be a substring of:%n  <%s>%n%s", actual, expected, comparisonStrategy);
  }
}
