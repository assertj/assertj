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

import org.fest.assertions.data.Offset;

/**
 * Creates an error message indicating that an assertion that verifies that two numbers are equal within a positive
 * offset failed.
 *
 * @author Alex Ruiz
 */
public class IsNotEqualWithOffset extends BasicErrorMessage {

  /**
   * Creates a new <code>{@link IsNotEqualWithOffset}</code>.
   * @param <T> guarantees that the values used in this factory have the same type.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param offset the given positive offset.
   * @return the created {@code ErrorMessage}.
   */
  public static <T extends Number> ErrorMessage isNotEqual(T actual, T expected, Offset<T> offset) {
    return new IsNotEqualWithOffset(actual, expected, offset);
  }

  private IsNotEqualWithOffset(Number actual, Number expected, Offset<?> offset) {
    super("%sexpected:<%s> but was:<%s> with offset:<%s>", expected, actual, offset.value());
  }
}
