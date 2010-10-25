/*
 * Created on Oct 24, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.error;

import org.fest.assertions.data.Delta;

/**
 * Creates an error message indicating that an assertion that verifies that two numbers are equal within a positive
 * delta failed.
 *
 * @author Alex Ruiz
 */
public class IsNotEqualWithDelta extends BasicErrorMessage {

  /**
   * Creates a new <code>{@link IsNotEqualWithDelta}</code>.
   * @param <T> guarantees that the values used in this factory have the same type.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param delta the given delta.
   * @return the created {@code ErrorMessage}.
   */
  public static <T extends Number> ErrorMessage isNotEqual(T actual, T expected, Delta<T> delta) {
    return new IsNotEqualWithDelta(actual, expected, delta);
  }

  private IsNotEqualWithDelta(Number actual, Number expected, Delta<?> delta) {
    super("%sexpected:<%s> but was:<%s> with delta:<%s>", expected, actual, delta.value());
  }
}
