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
 * Creates an error message indicating that an assertion that verifies that a class has a field/property with a value.
 * 
 * @author Libor Ondrusek
 */
public class ShouldHavePropertyOrFieldWithValue extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHavePropertyOrFieldWithValue}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param name expected name of the field of this class
   * @param expectedValue expected value of the field of the class
   * @param actualValue actual value of the field of the class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHavePropertyOrFieldWithValue(Object actual, String name, Object expectedValue,
                                                                       Object actualValue) {
    return new ShouldHavePropertyOrFieldWithValue(actual, name, expectedValue, actualValue);
  }

  private ShouldHavePropertyOrFieldWithValue(Object actual, String name, Object expectedValue, Object actualValue) {
    super("%nExpecting%n  <%s>%nto have a property or a field named <%s> with value%n  <%s>%nbut value was:%n  <%s>",
          actual, name, expectedValue, actualValue);
  }
}
