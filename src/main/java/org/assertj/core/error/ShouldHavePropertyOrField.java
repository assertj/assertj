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
 * Creates an error message indicating that an assertion that verifies that a class has a given field/property.
 * 
 * @author Libor Ondrusek
 */
public class ShouldHavePropertyOrField extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldHavePropertyOrField}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param name expected name of field for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHavePropertyOrField(Object actual, String name) {
    return new ShouldHavePropertyOrField(actual, name);
  }

  private ShouldHavePropertyOrField(Object actual, String name) {
    super("%nExpecting%n  <%s>%nto have a property or a field named <%s>", actual, name);
  }
}
