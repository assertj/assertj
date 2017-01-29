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
 * Creates an error message indicating that an assertion that verifies that a class is (or is not) final.
 *
 * @author Michal Kordas
 */
public class ShouldBeFinal extends BasicErrorMessageFactory {

  private ShouldBeFinal(Class<?> actual, boolean positive) {
    super("%nExpecting:%n  <%s>%n" + (positive ? "to" : "not to") + " be a final class.", actual);
  }

  /**
   * Creates a new {@link ShouldBeFinal}.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeFinal(Class<?> actual) {
    return new ShouldBeFinal(actual, true);
  }

  /**
   * Creates a new {@link ShouldBeFinal}.
   *
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeFinal(Class<?> actual) {
    return new ShouldBeFinal(actual, false);
  }
}
