/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

/**
 * Provides helper methods for navigating a list property in a generated assertion class so we can chain assertions
 * through deeply nested models more easily.
 * 
 * @since 2.5.0 / 3.5.0
 */
//@format:off
public class FactoryBasedNavigableIterableAssert<SELF extends FactoryBasedNavigableIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>, 
                                                 ACTUAL extends Iterable<? extends ELEMENT>, 
                                                 ELEMENT, 
                                                 ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {
// @format:on

  private AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory;

  public FactoryBasedNavigableIterableAssert(ACTUAL actual, Class<?> selfType, AssertFactory<ELEMENT, ELEMENT_ASSERT> assertFactory) {
    super(actual, selfType);
    this.assertFactory = assertFactory;
  }

  public ELEMENT_ASSERT toAssert(ELEMENT value, String description) {
    return assertFactory.createAssert(value).as(description);
  }
}
