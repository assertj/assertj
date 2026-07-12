/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

/**
 * Provides helper methods for navigating a list property in a generated assertion class so we can chain assertions
 * through deeply nested models more easily.
 *
 * @param <SELF>           the "self" type of this assertion class. Please read &quot;<a href="https://bit.ly/1IZIRcY"
 *                         target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *                         for more details.
 * @param <ACTUAL>         the type of the "actual" value.
 * @param <ELEMENT>        the type of elements of the "actual" value.
 * @param <ELEMENT_ASSERT> used for navigational assertions to return the right assert type.
 * @since 2.5.0 / 3.5.0
 * @deprecated Use {@link AbstractIterableAssert#withElementAssert(AssertFactory)} instead.
 */
//@format:off
@Deprecated
public class FactoryBasedNavigableIterableAssert<SELF extends FactoryBasedNavigableIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>,
                                                 ACTUAL extends Iterable<? extends ELEMENT>, 
                                                 ELEMENT, 
                                                 ELEMENT_ASSERT extends AbstractAssert<? extends ELEMENT_ASSERT, ELEMENT>>
       extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {
// @format:on

  private final AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory;

  /**
   * Creates a navigable iterable assertion using the given element assertion factory.
   *
   * @param actual the actual iterable
   * @param selfType the type of the concrete assertion
   * @param assertFactory the element assertion factory
   */
  public FactoryBasedNavigableIterableAssert(ACTUAL actual, Class<?> selfType,
                                             AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    super(actual, selfType);
    this.assertFactory = assertFactory;
  }

  @Override
  public ELEMENT_ASSERT toAssert(ELEMENT value) {
    return assertFactory.createAssert(value);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected SELF newAbstractIterableAssert(Iterable<? extends ELEMENT> iterable) {
    return (SELF) new FactoryBasedNavigableIterableAssert<>(iterable, FactoryBasedNavigableIterableAssert.class, assertFactory);
  }
}
