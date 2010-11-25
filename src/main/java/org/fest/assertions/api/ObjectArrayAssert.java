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

import org.fest.assertions.core.ObjectEnumerableAssert;
import org.fest.assertions.internal.ObjectArrays;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for arrays of objects. To create an instance of this class, use the factory method
 * <code>{@link Assertions#assertThat(Object[])}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ObjectArrayAssert extends GenericAssert<ObjectArrayAssert, Object[]> implements ObjectEnumerableAssert<ObjectArrayAssert> {

  // TODO test!

  @VisibleForTesting ObjectArrays arrays = ObjectArrays.instance();

  protected ObjectArrayAssert(Object[] actual) {
    super(actual, ObjectArrayAssert.class);
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
  public ObjectArrayAssert containsOnly(Object... values) {
    arrays.assertContainsOnly(info, actual, values);
    return this;
  }

  /** {@inheritDoc} */
  public ObjectArrayAssert containsSequence(Object... sequence) {
    // TODO Auto-generated method stub
    return null;
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
}
