/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.bigdecimals;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class BigDecimalAssert_scale_Test {

  @Test
  void should_be_able_to_use_scale_assertions_on_big_decimal_scale() throws Exception {
    // GIVEN
    BigDecimal threeDecimal = new BigDecimal("1.111");
    // THEN
    then(threeDecimal).scale()
                      .isLessThan(4)
                      .isGreaterThan(2)
                      .isPositive()
                      .returnToBigDecimal()
                      .hasScaleOf(3);
  }

  @Test
  void should_have_a_helpful_error_message_when_scale_assertion_is_used_on_a_null_big_decimal() {
    // GIVEN
    BigDecimal nullBigDecimal = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(nullBigDecimal).scale().isBetween(2, 3))
                                    .withMessage("Can not perform assertions on the scale of a null BigDecimal");
  }

}
