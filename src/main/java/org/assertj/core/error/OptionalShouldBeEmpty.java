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

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Build error message when an {@link java.util.Optional} should be empty.
 *
 * @author Jean-Christophe Gay
 */
public class OptionalShouldBeEmpty extends BasicErrorMessageFactory {

  private OptionalShouldBeEmpty(Class<?> optionalClass, Object optionalValue) {
    super("%nExpecting an empty " + optionalClass.getSimpleName() + " but was containing value: <%s>.", optionalValue);
  }

  /**
   * Indicates that the provided {@link java.util.Optional} should be empty.
   *
   * @param optional the actual {@link Optional} to test.
   * @param <VALUE> the type of the value contained in the {@link java.util.Optional}.
   * @return a error message factory.
   */
  public static <VALUE> OptionalShouldBeEmpty shouldBeEmpty(Optional<VALUE> optional) {
    return new OptionalShouldBeEmpty(optional.getClass(), optional.get());
  }

  /**
   * Indicates that the provided {@link java.util.OptionalDouble} should be empty.
   *
   * @param optional the actual {@link OptionalDouble} to test.
   * @return a error message factory.
   */
  public static OptionalShouldBeEmpty shouldBeEmpty(OptionalDouble optional) {
    return new OptionalShouldBeEmpty(optional.getClass(), optional.getAsDouble());
  }

  /**
   * Indicates that the provided {@link java.util.OptionalInt} should be empty.
   *
   * @param optional the actual {@link OptionalInt} to test.
   * @return a error message factory.
   */
  public static OptionalShouldBeEmpty shouldBeEmpty(OptionalInt optional) {
    return new OptionalShouldBeEmpty(optional.getClass(), optional.getAsInt());
  }

  /**
   * Indicates that the provided {@link java.util.OptionalLong} should be empty.
   *
   * @param optional the actual {@link OptionalLong} to test.
   * @return a error message factory.
   */
  public static OptionalShouldBeEmpty shouldBeEmpty(OptionalLong optional) {
    return new OptionalShouldBeEmpty(optional.getClass(), optional.getAsLong());
  }
}
