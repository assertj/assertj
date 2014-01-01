/*
 * Created on Oct 20, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.api;

/**
 * Base class for all implementations of assertions for {@link Number}s without {@link java.math.BigDecimal}.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 *
 * @author Mariusz Smykula
 */
public abstract class AbstractNumberAssert<S extends AbstractNumberAssert<S, A>, A extends Number & Comparable<? super A>>
    extends AbstractComparableAssert<S, A> implements NumberAssert<S, A> {

  protected AbstractNumberAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public S asHexadecimal() {
    return super.asHexadecimal();
  }

  /** {@inheritDoc} */
  @Override
  public S asBinary() {
    return super.asBinary();
  }

}
