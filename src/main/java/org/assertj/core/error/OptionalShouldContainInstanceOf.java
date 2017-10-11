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

import static java.lang.String.format;

import java.util.Optional;

/**
 * Build an error message when a value should be instance of a specific class.
 *
 * @author Alban Dericbourg
 */
public class OptionalShouldContainInstanceOf extends BasicErrorMessageFactory {

  private OptionalShouldContainInstanceOf(String message) {
    super(message);
  }

  /**
   * Indicates that a value should be present in an empty {@link java.util.Optional}.
   *
   * @param value Optional to be checked.
   * @param clazz the class to check the optional value against
   * @return an error message factory.
   * @throws java.lang.NullPointerException if optional is null.
   */
  public static OptionalShouldContainInstanceOf shouldContainInstanceOf(Object value, Class<?> clazz) {
    Optional<?> optional = (Optional<?>) value;
    if (optional.isPresent()) {
      return new OptionalShouldContainInstanceOf(format("%nExpecting:%n <%s>%nto contain a value that is an instance of:%n <%s>%nbut did contain an instance of:%n <%s>",
                                                        optional.getClass().getSimpleName(), clazz.getName(),
                                                        optional.get().getClass().getName()));
    }
    return new OptionalShouldContainInstanceOf(format("%nExpecting:%n <%s>%nto contain a value that is an instance of:%n <%s>%nbut was empty",
                                                      optional.getClass().getSimpleName(), clazz.getName()));
  }
}
