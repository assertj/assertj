/*
 * Created on Oct 21, 2010
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
package org.fest.assertions.api;

import java.util.Comparator;

import org.fest.assertions.internal.Booleans;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for bytes.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Boolean)}</code> or
 * <code>{@link Assertions#assertThat(boolean)}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Mikhail Mazursky
 */
public class BooleanAssert extends AbstractAssert<BooleanAssert, Boolean> {

  @VisibleForTesting
  Booleans booleans = Booleans.instance();

  protected BooleanAssert(Boolean actual) {
    super(actual, BooleanAssert.class);
  }

  /**
   * Verifies that the actual value is {@code true}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not {@code true}.
   */
  public BooleanAssert isTrue() {
    return isEqualTo(true);
  }

  /**
   * Verifies that the actual value is {@code false}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not {@code false}.
   */
  public BooleanAssert isFalse() {
    return isEqualTo(false);
  }

  /**
   * Verifies that the actual value is equal to the given one.
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  public BooleanAssert isEqualTo(boolean expected) {
    booleans.assertEqual(info, actual, expected);
    return this;
  }

  /**
   * Verifies that the actual value is not equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  public BooleanAssert isNotEqualTo(boolean other) {
    booleans.assertNotEqual(info, actual, other);
    return this;
  }

  @Override
  public BooleanAssert usingComparator(Comparator<? super Boolean> customComparator) {
    throw new UnsupportedOperationException("custom Comparator is not supported for Boolean comparison");
  }
}
