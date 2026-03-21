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

import org.assertj.core.annotation.CheckReturnValue;
import org.assertj.core.util.IterableUtil;

//@format:off
public abstract class AbstractIterableSizeAssert<SELF extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>, 
                                                 ACTUAL extends Iterable<? extends ELEMENT>, 
                                                 ELEMENT, 
                                                 ELEMENT_ASSERT extends AbstractAssert<? extends ELEMENT_ASSERT, ELEMENT>>
    extends AbstractIntegerAssert<AbstractIterableSizeAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>> {
//@format:on

  private final AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> originAssert;

  /**
   * Creates a new instance from an origin {@link AbstractIterableAssert} instance.
   *
   * @param originAssert the origin {@link AbstractIterableAssert} that initiated the navigation.
   * @since 3.28.0
   */
  protected AbstractIterableSizeAssert(AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> originAssert) {
    super(IterableUtil.sizeOf(originAssert.actual), AbstractIterableSizeAssert.class);
    this.originAssert = originAssert;
  }

  /**
   * @deprecated use {@link #AbstractIterableSizeAssert(AbstractIterableAssert)} instead.
   */
  @Deprecated
  protected AbstractIterableSizeAssert(Integer actual, Class<?> selfType) {
    super(actual, selfType);
    this.originAssert = null;
  }

  /**
   * Returns to the origin {@link AbstractIterableAssert} instance that initiated the navigation.
   *
   * @return the origin {@link AbstractIterableAssert} instance.
   */
  @CheckReturnValue
  public AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT> returnToIterable() {
    if (originAssert == null) {
      throw new IllegalStateException("No origin available. Was this assert created from its deprecated constructor?");
    }
    return originAssert;
  }

}
