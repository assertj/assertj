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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.integers;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.IntegersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Integers#assertIsEven(AssertionInfo, Integer)}</code>.
 *
 * @author Cal027
 */
public class Integers_assertIsEven_Test extends IntegersBaseTest {

  @Test
  public void should_succeed_since_actual_is_even() {
    integers.assertIsEven(someInfo(), 2);
    integers.assertIsEven(someInfo(), -2);
  }

  @Test
  public void should_fail_since_actual_is_not_even() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> integers.assertIsEven(someInfo(), 3))
                                                   .withMessage(format("%nExpecting:%n <1>%nto be equal to:%n <0>%nbut was not."));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> integers.assertIsEven(someInfo(), -3))
                                                   .withMessage(format("%nExpecting:%n <1>%nto be equal to:%n <0>%nbut was not."));
  }

  @Test
  public void should_succeed_since_actual_is_even_whatever_custom_comparison_strategy_is() {
    integersWithAbsValueComparisonStrategy.assertIsEven(someInfo(), 4);
    integersWithAbsValueComparisonStrategy.assertIsEven(someInfo(), -4);
  }

  @Test
  public void should_fail_since_actual_is_not_even_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> integersWithAbsValueComparisonStrategy.assertIsEven(someInfo(),
                                                                                                                         5))
                                                   .withMessage(format("%nExpecting:%n <1>%nto be equal to:%n <0>%nbut was not."));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> integersWithAbsValueComparisonStrategy.assertIsEven(someInfo(),
                                                                                                                         -5))
                                                   .withMessage(format("%nExpecting:%n <1>%nto be equal to:%n <0>%nbut was not."));
  }

}
