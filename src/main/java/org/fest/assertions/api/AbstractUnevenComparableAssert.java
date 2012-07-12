/*
 * Created on Feb 8, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.api;

import org.fest.assertions.core.UnevenComparableAssert;

/**
 * Base class for all implementations of <code>{@link UnevenComparableAssert}</code>.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g" target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <A> the type of the "actual" value.
 * 
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 */
public abstract class AbstractUnevenComparableAssert<S extends AbstractUnevenComparableAssert<S, A>, A extends Comparable<? super A>>
    extends AbstractComparableAssert<S, A> implements UnevenComparableAssert<S, A> {

  protected AbstractUnevenComparableAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  public S isEqualByComparingTo(A expected) {
    comparables.assertEqualByComparison(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
  public S isNotEqualByComparingTo(A other) {
    comparables.assertNotEqualByComparison(info, actual, other);
    return myself;
  }
}
