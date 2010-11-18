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

import org.fest.assertions.core.ObjectGroupAssert;
import org.fest.assertions.internal.Collections;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for collections. To create an instance of this class, use the factory method
 * <code>{@link Assertions#assertThat(Collection)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class CollectionAssert extends GenericAssert<CollectionAssert, Collection<?>> implements ObjectGroupAssert {

  @VisibleForTesting Collections collections = Collections.instance();

  protected CollectionAssert(Collection<?> actual) {
    super(actual);
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
}
