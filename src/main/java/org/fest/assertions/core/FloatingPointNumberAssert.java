/*
 * Created on Oct 23, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.core;

import org.fest.assertions.data.Offset;

/**
 * Assertion methods applicable to floating-point <code>{@link Number}</code>s.
 * @param <T> the type of the "actual" value.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public interface FloatingPointNumberAssert<T extends Number> extends NumberAssert<T> {

  /**
   * Verifies that the actual value is equal to the given one, within a positive offset.
   * @param expected the given value to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  FloatingPointNumberAssert<T> isEqualTo(T expected, Offset<T> offset);

  /**
   * Verifies that the actual value is equal to {@code NaN}.
   * @return this assertion object.
   * @throws AssertionError if the actual value is not equal to {@code NaN}.
   */
  FloatingPointNumberAssert<T> isNaN();

  /**
   * Verifies that the actual value is not equal to {@code NaN}.
   * @return this assertion object.
   * @throws AssertionError if the actual value is equal to {@code NaN}.
   */
  FloatingPointNumberAssert<T> isNotNaN();
}
