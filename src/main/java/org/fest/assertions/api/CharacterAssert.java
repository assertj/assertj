/*
 * Created on Oct 23, 2010
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
 * Assertion methods for characters. To create an instance of this class, use the factory methods
 * <code>{@link Assertions#assertThat(Character)}</code> or <code>{@link Assertions#assertThat(char)}</code>.
 *
 * @author Yvonne Wang
 * @author David DIDIER
 * @author Ansgar Konermann
 * @author Alex Ruiz
 */
public class CharacterAssert implements Assert<Character>, ComparableAssert<Character> {

  @VisibleForTesting Objects objects = Objects.instance();
  @VisibleForTesting Comparables comparables = Comparables.instance();
  @VisibleForTesting Characters characters = Characters.instance();
  @VisibleForTesting Conditions conditions = Conditions.instance();

  @VisibleForTesting final Character actual;
  @VisibleForTesting final WritableAssertionInfo info;

  protected CharacterAssert(Character actual) {
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /** {@inheritDoc} */
  public CharacterAssert as(String description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public CharacterAssert as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public CharacterAssert describedAs(String description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert describedAs(Description description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  public CharacterAssert isNotNull() {
    objects.assertNotNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert isEqualTo(Character expected) {
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
  public CharacterAssert isEqualTo(char expected) {
    characters.assertEqual(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert isNotEqualTo(Character other) {
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
  public CharacterAssert isNotEqualTo(char other) {
    characters.assertNotEqual(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert isSameAs(Character expected) {
    objects.assertSame(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert isNotSameAs(Character other) {
    objects.assertNotSame(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert isLessThan(Character other) {
    comparables.assertLessThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is less than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  public CharacterAssert isLessThan(char other) {
    characters.assertLessThan(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert isLessThanOrEqualTo(Character other) {
    comparables.assertLessThanOrEqualTo(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  public CharacterAssert isLessThanOrEqualTo(char other) {
    characters.assertLessThanOrEqualTo(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert isGreaterThan(Character other) {
    comparables.assertGreaterThan(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  public CharacterAssert isGreaterThan(char other) {
    characters.assertGreaterThan(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert isGreaterThanOrEqualTo(Character other) {
    comparables.assertGreaterThanOrEqualTo(info, actual, other);
    return this;
  }

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is less than the given one.
   */
  public CharacterAssert isGreaterThanOrEqualTo(char other) {
    characters.assertGreaterThanOrEqualTo(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert satisfies(Condition<Character> condition) {
    conditions.assertSatisfies(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert doesNotSatisfy(Condition<Character> condition) {
    conditions.assertDoesNotSatisfy(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public CharacterAssert is(Condition<Character> condition) {
    return satisfies(condition);
  }

  /** {@inheritDoc} */
  public CharacterAssert isNot(Condition<Character> condition) {
    return doesNotSatisfy(condition);
  }

  @VisibleForTesting final String descriptionText() {
    return info.descriptionText();
  }
}
