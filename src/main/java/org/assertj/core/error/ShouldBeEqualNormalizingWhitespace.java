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
 * Creates an error message indicating that an assertion that verifies that two {@code CharSequence}s are equal, 
 * after the whitespace of both strings has been normalized, failed.
 * 
 * @author Alexander Bischof
 */
public class ShouldBeEqualNormalizingWhitespace extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link org.assertj.core.error.ShouldBeEqualNormalizingWhitespace}</code>.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeEqualNormalizingWhitespace(CharSequence actual, CharSequence expected) {
    return new ShouldBeEqualNormalizingWhitespace(actual, expected);
  }

  private ShouldBeEqualNormalizingWhitespace(CharSequence actual, CharSequence expected) {
    super("%nExpecting:%n  <%s>%nto be equal to:%n  <%s>%nafter whitespace differences are normalized", actual, expected);
  }
}
