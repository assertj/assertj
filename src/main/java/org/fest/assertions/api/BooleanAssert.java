/*
 * Created on Oct 21, 2010
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
package org.fest.assertions.api;

import org.fest.assertions.core.*;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for bytes. To create an instance of this class, use the factory methods
 * <code>{@link Assertions#assertThat(Boolean)}</code> or <code>{@link Assertions#assertThat(boolean)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 */
public class BooleanAssert implements Assert<Boolean> {

  @VisibleForTesting Conditions conditions = Conditions.instance();
  @VisibleForTesting Objects objects = Objects.instance();
  @VisibleForTesting Booleans booleans = Booleans.instance();

  @VisibleForTesting final Boolean actual;
  @VisibleForTesting final WritableAssertionInfo info;

  protected BooleanAssert(Boolean actual) {
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /** {@inheritDoc} */
  public BooleanAssert as(String description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public BooleanAssert as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public BooleanAssert describedAs(String description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public BooleanAssert describedAs(Description description) {
    info.description(description);
    return this;
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

  /** {@inheritDoc} */
  public BooleanAssert isEqualTo(Boolean expected) {
    objects.assertEqual(info, actual, expected);
    return this;
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

  /** {@inheritDoc} */
  public BooleanAssert isNotEqualTo(Boolean other) {
    objects.assertNotEqual(info, actual, other);
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

  /** {@inheritDoc} */
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  public BooleanAssert isNotNull() {
    objects.assertNotNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public BooleanAssert isSameAs(Boolean expected) {
    objects.assertSame(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public BooleanAssert isNotSameAs(Boolean other) {
    objects.assertNotSame(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public BooleanAssert satisfies(Condition<Boolean> condition) {
    conditions.assertSatisfies(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public BooleanAssert doesNotSatisfy(Condition<Boolean> condition) {
    conditions.assertDoesNotSatisfy(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public BooleanAssert is(Condition<Boolean> condition) {
    return satisfies(condition);
  }

  /** {@inheritDoc} */
  public BooleanAssert isNot(Condition<Boolean> condition) {
    return doesNotSatisfy(condition);
  }

  @VisibleForTesting String descriptionText() {
    return info.descriptionText();
  }
}
