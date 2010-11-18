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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.api;

import java.util.Collection;

import org.fest.assertions.core.ObjectGroupAssert;
import org.fest.assertions.internal.Collections;
import org.fest.util.VisibleForTesting;

/**
 * Base class for implementations of <code>{@link ObjectGroupAssert}</code> whose actual value type is
 * <code>{@link Collection}</code>.
 * @param <S> the "self" type of this assertion class. Please read
 * &quot;<a href="http://bit.ly/anMa4g" target="_blank">Emulating 'self types' using Java Generics to simplify fluent
 * API implementation</a>&quot; for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public abstract class GenericCollectionAssert<S, A extends Collection<?>> extends GenericAssert<S, A> implements ObjectGroupAssert<S>  {

  @VisibleForTesting Collections collections = Collections.instance();

  protected GenericCollectionAssert(A actual) {
    super(actual);
  }

  /** {@inheritDoc} */
  public final void isNullOrEmpty() {
    collections.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public final void isEmpty() {
    collections.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public final S isNotEmpty() {
    collections.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  public final S hasSize(int expected) {
    collections.assertHasSize(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  public final S contains(Object... values) {
    collections.assertContains(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public final S containsExclusively(Object... values) {
    collections.assertContainsExclusively(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public final S doesNotContain(Object... values) {
    collections.assertDoesNotContain(info, actual, values);
    return myself;
  }

  /** {@inheritDoc} */
  public final S doesNotHaveDuplicates() {
    collections.assertDoesHaveDuplicates(info, actual);
    return myself;
  }
}
