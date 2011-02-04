/*
 * Created on Nov 18, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import java.util.Collection;

import org.fest.assertions.core.*;
import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Base class for all assertions.
 * @param <S> the "self" type of this assertion class. Please read
 * &quot;<a href="http://bit.ly/anMa4g" target="_blank">Emulating 'self types' using Java Generics to simplify fluent
 * API implementation</a>&quot; for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author Alex Ruiz
 */
public abstract class AbstractAssert<S, A> implements Assert<S, A> {

  @VisibleForTesting Objects objects = Objects.instance();
  @VisibleForTesting Conditions conditions = Conditions.instance();

  @VisibleForTesting final WritableAssertionInfo info;
  @VisibleForTesting final A actual;

  protected final S myself;

  protected AbstractAssert(A actual, Class<S> selfType) {
    myself = selfType.cast(this);
    this.actual = actual;
    info = new WritableAssertionInfo();
  }

  /** {@inheritDoc} */
  public final S as(String description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public final S as(Description description) {
    return describedAs(description);
  }

  /** {@inheritDoc} */
  public final S describedAs(String description) {
    info.description(description);
    return myself;
  }

  /** {@inheritDoc} */
  public final S describedAs(Description description) {
    info.description(description);
    return myself;
  }

  /** {@inheritDoc} */
  public S isEqualTo(A expected) {
    objects.assertEqual(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotEqualTo(A other) {
    objects.assertNotEqual(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public final void isNull() {
    objects.assertNull(info, actual);
  }

  /** {@inheritDoc} */
  public final S isNotNull() {
    objects.assertNotNull(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public final S isSameAs(A expected) {
    objects.assertSame(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  public final S isNotSameAs(A other) {
    objects.assertNotSame(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public final S isIn(Object... values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public final S isIn(Collection<?> values) {
    objects.assertIsIn(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public final S is(Condition<A> condition) {
    conditions.assertIs(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public final S isNot(Condition<A> condition) {
    conditions.assertIsNot(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public final S has(Condition<A> condition) {
    conditions.assertHas(info, actual, condition);
    return myself;
  }

  /** {@inheritDoc} */
  public final S doesNotHave(Condition<A> condition) {
    conditions.assertDoesNotHave(info, actual, condition);
    return myself;
  }

  @VisibleForTesting final String descriptionText() {
    return info.descriptionText();
  }
}
