/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.data.Index;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.util.Preconditions.checkArgument;

public class FactoryBasedNavigableCollectionAssert <SELF extends FactoryBasedNavigableCollectionAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>,
  ACTUAL extends Collection<? extends ELEMENT>,
  ELEMENT,
  ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
  extends AbstractCollectionAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {

  private AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory;


  protected FactoryBasedNavigableCollectionAssert(ACTUAL elements, Class<?> selfType, AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    super(elements, selfType);
    this.assertFactory = assertFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ELEMENT_ASSERT toAssert(ELEMENT value, String description) {
    return assertFactory.createAssert(value).as(description);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  protected SELF newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    checkArgument(iterable instanceof Collection, "Expecting %s to be a Collection", iterable);
    return (SELF) new FactoryBasedNavigableCollectionAssert<>((Collection<? extends ELEMENT>) iterable,
      FactoryBasedNavigableCollectionAssert.class,
      assertFactory);
  }
}
