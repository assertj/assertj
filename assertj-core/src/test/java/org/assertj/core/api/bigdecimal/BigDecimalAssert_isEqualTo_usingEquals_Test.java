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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.bigdecimal;

import org.assertj.core.api.BigDecimalAssert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.function.BiPredicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.BiPredicates.ALWAYS_DIFFERENT;
import static org.assertj.core.testkit.BiPredicates.ALWAYS_EQUALS;

/**
 * Tests for <code>{@link BigDecimalAssert#usingEquals(BiPredicate)}</code>.
 */
class BigDecimalAssert_isEqualTo_usingEquals_Test {

  @Test
  void should_be_able_to_use_a_custom_equals() {
    BigDecimal one = BigDecimal.ONE;
    BigDecimal two = new BigDecimal("2.00");

    assertThat(one)
                   .usingEquals(ALWAYS_EQUALS)
                   .isEqualTo(two);
    assertThat(one)
                   .usingEquals(ALWAYS_EQUALS, "name")
                   .isEqualTo(two);
    assertThat(one)
                   .usingEquals(ALWAYS_DIFFERENT)
                   .isNotEqualTo(two);
    assertThat(one)
                   .usingEquals(ALWAYS_DIFFERENT, "name")
                   .isNotEqualTo(two);
  }

  @Test
  void should_reset_custom_equals_to_default() {
    BigDecimal one = BigDecimal.ONE;
    BigDecimal two = new BigDecimal("2.00");

    assertThat(one)
                   .usingEquals(ALWAYS_EQUALS)
                   .usingDefaultComparator()
                   .isEqualTo(new BigDecimal(1));
    assertThat(one)
                   .usingEquals(ALWAYS_EQUALS, "name")
                   .usingDefaultComparator()
                   .isEqualTo(new BigDecimal(1));
    assertThat(one)
                   .usingEquals(ALWAYS_DIFFERENT)
                   .usingDefaultComparator()
                   .isNotEqualTo(two);
    assertThat(one)
                   .usingEquals(ALWAYS_DIFFERENT, "name")
                   .usingDefaultComparator()
                   .isNotEqualTo(two);
  }

}
