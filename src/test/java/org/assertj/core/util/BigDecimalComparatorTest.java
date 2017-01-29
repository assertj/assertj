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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalComparatorTest {

  @Test
  public void should_consider_null_to_be_less_than_non_bull() {
    // GIVEN
    BigDecimal bigDecimal1 = null;
    BigDecimal bigDecimal2 = BigDecimal.ZERO;
    // WHEN
    int result = BIG_DECIMAL_COMPARATOR.compare(bigDecimal1, bigDecimal2);
    // THEN
    assertThat(result).isNegative();
  }

  @Test
  public void should_consider_non_null_to_be_greater_than_null() {
    // GIVEN
    BigDecimal bigDecimal1 = BigDecimal.ZERO;
    BigDecimal bigDecimal2 = null;
    // WHEN
    int result = BIG_DECIMAL_COMPARATOR.compare(bigDecimal1, bigDecimal2);
    // THEN
    assertThat(result).isPositive();
  }

  @Test
  public void should_return_0_when_both_BigDecimal_are_null() {
    // GIVEN
    BigDecimal bigDecimal1 = null;
    BigDecimal bigDecimal2 = null;
    // WHEN
    int result = BIG_DECIMAL_COMPARATOR.compare(bigDecimal1, bigDecimal2);
    // THEN
    assertThat(result).isZero();
  }

  @Test
  public void should_compare_BigDecimal_with_natural_comparator() {
    // GIVEN
    BigDecimal bigDecimal1 = new BigDecimal("0.0");
    BigDecimal bigDecimal2 = new BigDecimal("0.000000");
    // WHEN
    int result = BIG_DECIMAL_COMPARATOR.compare(bigDecimal1, bigDecimal2);
    // THEN
    assertThat(result).isZero();
  }

}
