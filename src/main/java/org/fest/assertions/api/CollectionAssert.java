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

import java.util.Collection;

import org.fest.assertions.core.*;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for collections. To create an instance of this class, use the factory method
 * <code>{@link Assertions#assertThat(Collection)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class CollectionAssert implements ObjectGroupAssert, Assert<Collection<?>> {

  @VisibleForTesting Objects objects = Objects.instance();
  @VisibleForTesting Collections collections = Collections.instance();
  @VisibleForTesting Conditions conditions = Conditions.instance();

  @VisibleForTesting final Collection<?> actual;
  @VisibleForTesting final WritableAssertionInfo info;

  protected CollectionAssert(Collection<?> actual) {
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /** {@inheritDoc} */
  public CollectionAssert as(String description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public CollectionAssert as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public CollectionAssert describedAs(String description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert describedAs(Description description) {
    info.description(description);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert isEqualTo(Collection<?> expected) {
    objects.assertEqual(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert isNotEqualTo(Collection<?> other) {
    objects.assertNotEqual(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  public CollectionAssert isNotNull() {
    objects.assertNotNull(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert isSameAs(Collection<?> expected) {
    objects.assertSame(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert isNotSameAs(Collection<?> other) {
    objects.assertNotSame(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public void isNullOrEmpty() {
    collections.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public void isEmpty() {
    collections.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public CollectionAssert isNotEmpty() {
    collections.assertNotEmpty(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert hasSize(int expected) {
    collections.assertHasSize(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert contains(Object... values) {
    collections.assertContains(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert containsExclusively(Object... values) {
    collections.assertContainsExclusively(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert doesNotContain(Object... values) {
    collections.assertDoesNotContain(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert doesNotHaveDuplicates() {
    collections.assertDoesHaveDuplicates(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert satisfies(Condition<Collection<?>> condition) {
    conditions.assertSatisfies(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert doesNotSatisfy(Condition<Collection<?>> condition) {
    conditions.assertDoesNotSatisfy(info, actual, condition);
    return this;
  }

  /** {@inheritDoc} */
  public CollectionAssert is(Condition<Collection<?>> condition) {
    return satisfies(condition);
  }

  /** {@inheritDoc} */
  public CollectionAssert isNot(Condition<Collection<?>> condition) {
    return doesNotSatisfy(condition);
  }

  @VisibleForTesting final String descriptionText() {
    return info.descriptionText();
  }
}
