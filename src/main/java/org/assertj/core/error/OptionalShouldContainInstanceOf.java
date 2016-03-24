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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;

/**
 * Build an error message when a value should be instance of a specific class.
 *
 * @author Alban Dericbourg
 */
public class OptionalShouldContainInstanceOf extends BasicErrorMessageFactory {

  private OptionalShouldContainInstanceOf(Object optional, Class<?> clazz) {
    super(format("%nExpecting %s to contain a value of type %s.", optional.getClass().getSimpleName(), clazz.getName()));
  }

  /**
   * Indicates that a value should be present in an empty {@link java.util.Optional}.
   *
   * @param optional Optional to be checked.
   * @return an error message factory.
   * @throws java.lang.NullPointerException if optional is null.
   */
  public static OptionalShouldContainInstanceOf shouldContainInstanceOf(Object optional, Class<?> clazz) {
    return new OptionalShouldContainInstanceOf(optional, clazz);
  }
}
