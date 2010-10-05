/*
 * Created on Jul 26, 2010
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
package org.fest.assertions.core;

/**
 * Assertions methods applicable to groups of objects (e.g. arrays or collections.)
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public interface ObjectGroupAssert extends GroupAssert {

  /**
   * Verifies that the actual group contains the given values, in any order.
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values.
   */
  ObjectGroupAssert contains(Object... values);

  /**
   * Verifies that the actual group contains only the given values, in any order.
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the given group contains some
   * or none of the given values, or the actual group contains more values than the given ones.
   */
  ObjectGroupAssert containsOnly(Object... values);

  /**
   * Verifies that the actual group does not contain the given values.
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains the given values.
   */
  ObjectGroupAssert doesNotContain(Object... values);

  /**
   * Verifies that the actual group does not contain duplicates.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains duplicates.
   */
  ObjectGroupAssert doesNotHaveDuplicates();
}
