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
 * Creates an error message indicating that an assertion that verifies that a class is (or not) an interface.
 * 
 * @author William Delanoue
 */
public class ShouldBeInterface extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeInterface}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeInterface(Class<?> actual) {
    return new ShouldBeInterface(actual, true);
  }

  /**
   * Creates a new <code>{@link ShouldBeInterface}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeInterface(Class<?> actual) {
    return new ShouldBeInterface(actual, false);
  }

  private ShouldBeInterface(Class<?> actual, boolean toBeOrNotToBe) {
    super("%nExpecting%n  <%s>%n" + (toBeOrNotToBe ? "" : " not ") + "to be an interface", actual);
  }
}
