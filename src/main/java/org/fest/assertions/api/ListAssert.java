/*
 * Created on Oct 26, 2010
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

import java.util.List;

import org.fest.assertions.core.IndexedObjectEnumerableAssert;
import org.fest.assertions.data.Index;
import org.fest.assertions.internal.Lists;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for <code>{@link List}</code>s. To create an instance of this class, use the factory method
 * <code>{@link Assertions#assertThat(List)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ListAssert extends GenericCollectionAssert<ListAssert, List<?>> implements IndexedObjectEnumerableAssert {

  @VisibleForTesting Lists lists = Lists.instance();

  protected ListAssert(List<?> actual) {
    super(actual, ListAssert.class);
  }

  /** {@inheritDoc} */
  public ListAssert contains(Object value, Index index) {
    lists.assertContains(info, actual, value, index);
    return this;
  }

  /** {@inheritDoc} */
  public ListAssert doesNotContain(Object value, Index index) {
    lists.assertDoesNotContain(info, actual, value, index);
    return this;
  }
}
