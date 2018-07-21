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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.internal.ErrorMessages.percentageValueIsInRange;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Percentage#withPercentage(Number)}.
 *
 * @author Alexander Bischof
 */
public class Percentage_withPercentage_with_Double_Test {

  @SuppressWarnings("null")
  @Test
  public void should_throw_error_if_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> {
      Double value = null;
      withPercentage(value);
    });
  }

  @Test
  public void should_throw_error_if_value_is_negative() {
    double negative = -1d;
    assertThatIllegalArgumentException().isThrownBy(() -> withPercentage(negative))
                                        .withMessage(percentageValueIsInRange(negative));
  }

  @Test
  public void should_create_Percentage() {
    Double value = 0.8d;
    assertThat(withPercentage(value).value).isEqualTo(value);
    value = 200d;
    assertThat(withPercentage(value).value).isEqualTo(value);
  }
}
