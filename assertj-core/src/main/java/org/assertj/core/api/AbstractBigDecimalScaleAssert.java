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

/**
 * Base class for BigDecimal scale assertions.
 */
public abstract class AbstractBigDecimalScaleAssert<ORIGIN extends AbstractBigDecimalAssert<ORIGIN>>
    extends AbstractIntegerAssert<AbstractBigDecimalScaleAssert<ORIGIN>> {

  private final AbstractBigDecimalAssert<ORIGIN> originAssert;

  /**
   * Creates a new instance from an origin {@link AbstractBigDecimalAssert} instance.
   *
   * @param originAssert the origin {@link AbstractBigDecimalAssert} that initiated the navigation.
   * @since 3.28.0
   */
  protected AbstractBigDecimalScaleAssert(AbstractBigDecimalAssert<ORIGIN> originAssert) {
    super(originAssert.actual.scale(), AbstractBigDecimalScaleAssert.class);
    this.originAssert = originAssert;
  }

  /**
   * @deprecated use {@link #AbstractBigDecimalScaleAssert(AbstractBigDecimalAssert)} instead.
   */
  @Deprecated
  protected AbstractBigDecimalScaleAssert(Integer actualScale, Class<?> selfType) {
    super(actualScale, selfType);
    this.originAssert = null;
  }

  /**
   * Returns to the origin {@link AbstractBigDecimalAssert} instance that initiated the navigation.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(new BigDecimal(&quot;2.313&quot;)).scale()
   *                                      .isGreaterThan(1L)
   *                                      .isLessThan(5L)
   *                                    .returnToBigDecimal()
   *                                      .isPositive();</code></pre>
   *
   * @return the origin {@link AbstractBigDecimalAssert} instance.
   */
  @CheckReturnValue
  public AbstractBigDecimalAssert<ORIGIN> returnToBigDecimal() {
    if (originAssert == null) {
      throw new IllegalStateException("No origin available. Was this assert created from its deprecated constructor?");
    }
    return originAssert;
  }

}
