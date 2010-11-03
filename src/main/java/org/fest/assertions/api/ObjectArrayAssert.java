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
package org.fest.assertions.api;

import org.fest.assertions.core.*;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for arrays of objects. To create an instance of this class, use the factory method
 * <code>{@link Assertions#assertThat(Object[])}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ObjectArrayAssert implements ObjectGroupAssert, Assert<Object[]> {

  @VisibleForTesting Objects objects = Objects.instance();
  @VisibleForTesting ObjectArrays arrays = ObjectArrays.instance();
  @VisibleForTesting Conditions conditions = Conditions.instance();

  @VisibleForTesting final Object[] actual;
  @VisibleForTesting final WritableAssertionInfo info;

  protected ObjectArrayAssert(Object[] actual) {
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert as(String description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert describedAs(String description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert describedAs(Description description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert isEqualTo(Object[] expected) {
    objects.assertEqual(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert isNotEqualTo(Object[] other) {
    objects.assertNotEqual(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert isNotNull() {
    objects.assertNotNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert isSameAs(Object[] expected) {
    objects.assertSame(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert isNotSameAs(Object[] other) {
    objects.assertNotSame(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public void isNullOrEmpty() {
    arrays.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public void isEmpty() {
    arrays.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert isNotEmpty() {
    arrays.assertNotEmpty(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert hasSize(int expected) {
    arrays.assertHasSize(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert contains(Object... values) {
    arrays.assertContains(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert containsExclusively(Object... values) {
    arrays.assertContainsExclusively(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert doesNotContain(Object... values) {
    arrays.assertDoesNotContain(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert doesNotHaveDuplicates() {
    arrays.assertDoesHaveDuplicates(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert satisfies(Condition<Object[]> condition) {
    conditions.assertSatisfies(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert doesNotSatisfy(Condition<Object[]> condition) {
    conditions.assertDoesNotSatisfy(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert is(Condition<Object[]> condition) {
    return satisfies(condition);
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert isNot(Condition<Object[]> condition) {
    return doesNotSatisfy(condition);
  }

  @VisibleForTesting final String descriptionText() {
    return info.descriptionText();
  }
}
