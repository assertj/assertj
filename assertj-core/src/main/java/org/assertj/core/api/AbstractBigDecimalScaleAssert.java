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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

/**
 * Base class for BigDecimal scale assertions.
 */
public abstract class AbstractBigDecimalScaleAssert<SELF extends AbstractBigDecimalAssert<SELF>>
    extends AbstractIntegerAssert<AbstractBigDecimalScaleAssert<SELF>> {

  protected AbstractBigDecimalScaleAssert(Integer actualScale, Class<?> selfType) {
    super(actualScale, selfType);
  }

  /**
   * Returns to the BigDecimal on which we ran scale assertions on.
   * <p>
   * Example:
   * <pre><code class='java'> assertThat(new BigDecimal(&quot;2.313&quot;)).scale()
   *                                      .isGreaterThan(1L)
   *                                      .isLessThan(5L)
   *                                    .returnToBigDecimal()
   *                                      .isPositive();</code></pre>
   *
   * @return BigDecimal assertions.
   */
  public abstract AbstractBigDecimalAssert<SELF> returnToBigDecimal();

}
