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
 * Creates an error message indicating that an assertion that verifies elements of a group satisfies at least n times a
 * {@code Condition} A group of elements can be a collection, an array.<br>
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
public class ElementsShouldBeAtLeast extends BasicErrorMessageFactory {

  /**
   * Creates a new </code>{@link ElementsShouldBeAtLeast}</code>.
   * @param actual the actual value in the failed assertion.
   * @param times least time the condition should be verify.
   * @param condition the {@code Condition}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory elementsShouldBeAtLeast(Object actual, int times, Condition<?> condition) {
    return new ElementsShouldBeAtLeast(actual, times, condition);
  }

  private ElementsShouldBeAtLeast(Object actual, int times, Condition<?> condition) {
    super("%nExpecting elements:%n<%s>%n to be at least %s times <%s>", actual, times, condition);
  }
}
