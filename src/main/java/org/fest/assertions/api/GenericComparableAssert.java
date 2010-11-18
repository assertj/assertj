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

import org.fest.assertions.core.ComparableAssert;
import org.fest.assertions.internal.Comparables;
import org.fest.util.VisibleForTesting;

/**
 * Base class for all implementations of <code>{@link ComparableAssert}</code>.
 * @param <S> the "self" type of this assertion class. Please read
 * &quot;<a href="http://bit.ly/anMa4g" target="_blank">Emulating 'self types' using Java Generics to simplify fluent
 * API implementation</a>&quot; for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author Alex Ruiz
 */
public abstract class GenericComparableAssert<S, A extends Comparable<A>> extends GenericAssert<S, A> implements ComparableAssert<S, A> {

  @VisibleForTesting Comparables comparables = Comparables.instance();

  protected GenericComparableAssert(A actual) {
    super(actual);
  }

  /** {@inheritDoc} */
  public final S isLessThan(A other) {
    comparables.assertLessThan(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public final S isLessThanOrEqualTo(A other) {
    comparables.assertLessThanOrEqualTo(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public final S isGreaterThan(A other) {
    comparables.assertGreaterThan(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  public final S isGreaterThanOrEqualTo(A other) {
    comparables.assertGreaterThanOrEqualTo(info, actual, other);
    return myself;
  }
}
