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
 * Copyright 2012-2022 the original author or authors.
 */

package org.assertj.core.internal.doubles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.assertj.core.internal.Doubles;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static java.lang.Double.*;

class Doubles_isNanOrInfinite_Test extends DoublesBaseTest {

  @Test
  void check_double_isNanOrInfinite_method_Nan(){
    assertThat(doubles.isNanOrInfinite(NaN())).isTrue();
  }

  @Test
  void check_double_isNanOrInfinite_method_POSITIVE_Infinite(){
    assertThat(doubles.isNanOrInfinite(POSITIVE_INFINITY)).isTrue();
  }

  @Test
  void check_double_isNanOrInfinite_method_NEGATIVE_Infinite(){
    assertThat(doubles.isNanOrInfinite(NEGATIVE_INFINITY)).isTrue();
  }

  @ParameterizedTest
  @CsvSource({"1.0", "0.0", "-1.0"})
  void check_double_isNanOrInfinite_method_none_above(Double actual){
    assertThat(doubles.isNanOrInfinite(actual)).isFalse();
  }
}
