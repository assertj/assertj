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
import org.fest.assertions.internal.Objects;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for collections. To create an instance of this class, use the factory method
 * <code>{@link Assertions#assertThat(Collection)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class CollectionAssert implements GroupAssert, Assert<Collection<?>> {

  @VisibleForTesting final WritableAssertionInfo info = new WritableAssertionInfo();

  private final Collection<?> actual;
  private final Objects objects;

  protected CollectionAssert(Collection<?> actual) {
    this(actual, Objects.instance());
  }

  @VisibleForTesting CollectionAssert(Collection<?> actual, Objects objects) {
    this.actual = actual;
    this.objects = objects;
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
    // TODO Auto-generated method stub

  }

  /** {@inheritDoc} */
  public CollectionAssert isNotNull() {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public CollectionAssert isSameAs(Collection<?> expected) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public CollectionAssert isNotSameAs(Collection<?> other) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public CollectionAssert satisfies(Condition<Collection<?>> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public CollectionAssert doesNotSatisfy(Condition<Collection<?>> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public CollectionAssert is(Condition<Collection<?>> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public CollectionAssert isNot(Condition<Collection<?>> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public void isNullOrEmpty() {
    // TODO Auto-generated method stub

  }

  /** {@inheritDoc} */
  public void isEmpty() {
    // TODO Auto-generated method stub

  }

  /** {@inheritDoc} */
  public CollectionAssert isNotEmpty() {
    // TODO Auto-generated method stub
    return null;
  }

  /** {@inheritDoc} */
  public CollectionAssert hasSize(int expected) {
    // TODO Auto-generated method stub
    return null;
  }

  @VisibleForTesting final String descriptionText() {
    return info.descriptionText();
  }
}
