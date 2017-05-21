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

import org.assertj.core.api.Condition;

/**
 * Creates an error message indicating that an assertion that verifies a map contains a key..
 */
public class ShouldContainKey extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldContainKey}</code>.
   *
   * @param actual the actual map in the failed assertion.
   * @param keyCondition key condition.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldContainKey(Object actual, Condition<?> keyCondition) {
    return new ShouldContainKey(actual, keyCondition);
  }

  private ShouldContainKey(Object actual, Condition<?> keyCondition) {
    super("%n" +
          "Expecting:%n" +
          "  <%s>%n" +
          "to contain a key satisfying:%n" +
          "  <%s>",
          actual, keyCondition);
  }

}
