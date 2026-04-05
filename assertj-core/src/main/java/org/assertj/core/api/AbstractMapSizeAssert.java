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

import java.util.Map;

//@format:off
public abstract class AbstractMapSizeAssert<ORIGIN extends AbstractMapAssert<ORIGIN, MAP, KEY, VALUE>,
                                            MAP extends Map<KEY, VALUE>,
                                            KEY,
                                            VALUE>
    extends AbstractIntegerAssert<AbstractMapSizeAssert<ORIGIN, MAP, KEY, VALUE>> {
//@format:on

  private final AbstractMapAssert<ORIGIN, MAP, KEY, VALUE> originAssert;

  /**
   * Creates a new instance from an origin {@link AbstractMapAssert} instance.
   *
   * @param originAssert the origin {@link AbstractMapAssert} that initiated the navigation.
   * @since 3.28.0
   */
  protected AbstractMapSizeAssert(AbstractMapAssert<ORIGIN, MAP, KEY, VALUE> originAssert) {
    super(originAssert.actual.size(), AbstractMapSizeAssert.class);
    this.originAssert = originAssert;
  }

  /**
   * @deprecated use {@link #AbstractMapSizeAssert(AbstractMapAssert)} instead.
   */
  @Deprecated
  protected AbstractMapSizeAssert(Integer actual, Class<?> selfType) {
    super(actual, selfType);
    this.originAssert = null;
  }

  /**
   * Returns to the origin {@link AbstractMapAssert} instance that initiated the navigation.
   *
   * @return the origin {@link AbstractMapAssert} instance.
   */
  @CheckReturnValue
  public AbstractMapAssert<ORIGIN, MAP, KEY, VALUE> returnToMap() {
    if (originAssert == null) {
      throw new IllegalStateException("No origin available. Was this assert created from its deprecated constructor?");
    }
    return originAssert;
  }

}
