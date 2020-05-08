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
package org.assertj.core.internal.shorts;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Shorts;
import org.assertj.core.internal.ShortsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Shorts#assertIsOdd(AssertionInfo, Short)}</code>.
 *
 * @author Cal027
 */
public class Shorts_assertIsOdd_Test extends ShortsBaseTest {

  @Test
  public void should_succeed_since_actual_is_odd() {
    shorts.assertIsOdd(someInfo(), (short) 3);
    shorts.assertIsOdd(someInfo(), (short) -3);
  }

  @Test
  public void should_fail_since_actual_is_not_odd() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> shorts.assertIsOdd(someInfo(), (short) 4))
                                                   .withMessage(format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> shorts.assertIsOdd(someInfo(), (short) -4))
                                                   .withMessage(format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
  }

  @Test
  public void should_succeed_since_actual_is_odd_whatever_custom_comparison_strategy_is() {
    shortsWithAbsValueComparisonStrategy.assertIsOdd(someInfo(), (short) 5);
    shortsWithAbsValueComparisonStrategy.assertIsOdd(someInfo(), (short) -5);
  }

  @Test
  public void should_fail_since_actual_is_not_odd_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> shortsWithAbsValueComparisonStrategy.assertIsOdd(someInfo(),
                                                                                                                      (short) 6))
                                                   .withMessage(format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> shortsWithAbsValueComparisonStrategy.assertIsOdd(someInfo(),
                                                                                                                      (short) -6))
                                                   .withMessage(format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
  }

}
