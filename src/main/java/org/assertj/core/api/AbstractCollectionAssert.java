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

import java.util.Collection;

/**
 * Base class for all implementations of assertions for {@link Collection}s.
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * @param <ELEMENT> the type of elements of the "actual" value.
 * @param <ELEMENT_ASSERT> used for navigational assertions to return the right assert type.
 *
 * @since 3.21.0
 */
//@format:off
public abstract class AbstractCollectionAssert<SELF extends AbstractCollectionAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>,
                                               ACTUAL extends Collection<? extends ELEMENT>,
                                               ELEMENT,
                                               ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
    extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> {
//@format:on

  protected AbstractCollectionAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

}
