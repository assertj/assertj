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

import java.math.BigDecimal;

import org.assertj.core.annotation.CheckReturnValue;

/**
 * Assertions for a {@link BigDecimal} scale.
 *
 * @param <T> retained for source compatibility
 */
public class BigDecimalScaleAssert<T> extends AbstractBigDecimalScaleAssert<BigDecimalAssert> {

  /**
   * Creates a scale assertion from the given origin assertion.
   *
   * @param originAssert the origin decimal assertion
   */
  public BigDecimalScaleAssert(AbstractBigDecimalAssert<BigDecimalAssert> originAssert) {
    super(originAssert);
  }

  @Override
  @CheckReturnValue
  public AbstractBigDecimalAssert<BigDecimalAssert> returnToBigDecimal() {
    return super.returnToBigDecimal();
  }

  /**
   * Creates a scale assertion used for null navigation.
   *
   * @return the null-navigation scale assertion
   */
  public static BigDecimalScaleAssert<?> nullBigDecimalScaleAssert() {
    return new BigDecimalScaleAssert<>(new BigDecimalAssert(BigDecimal.ZERO));
  }

  @Override
  BigDecimalScaleAssert<T> withAssertionState(AbstractAssert assertInstance) {
    return (BigDecimalScaleAssert<T>) super.withAssertionState(assertInstance);
  }

}
