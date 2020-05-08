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
package org.assertj.core.internal.longs;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Longs#assertIsOdd(AssertionInfo, Long)}</code>.
 *
 * @author Cal027
 */
public class Longs_assertIsOdd_Test extends LongsBaseTest {

  @Test
  public void should_succeed_since_actual_is_odd() {
    longs.assertIsOdd(someInfo(), 3L);
    longs.assertIsOdd(someInfo(), -3L);
  }

  @Test
  public void should_fail_since_actual_is_not_odd() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertIsOdd(someInfo(), 4L))
                                                   .withMessage(format("%nExpecting:%n <0L>%nnot to be equal to:%n <0L>%n"));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertIsOdd(someInfo(), -4L))
                                                   .withMessage(format("%nExpecting:%n <0L>%nnot to be equal to:%n <0L>%n"));
  }

  @Test
  public void should_succeed_since_actual_is_odd_whatever_custom_comparison_strategy_is() {
    longsWithAbsValueComparisonStrategy.assertIsOdd(someInfo(), 5L);
    longsWithAbsValueComparisonStrategy.assertIsOdd(someInfo(), -5L);
  }

  @Test
  public void should_fail_since_actual_is_not_odd_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longsWithAbsValueComparisonStrategy.assertIsOdd(someInfo(),
                                                                                                                     6L))
                                                   .withMessage(format("%nExpecting:%n <0L>%nnot to be equal to:%n <0L>%n"));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longsWithAbsValueComparisonStrategy.assertIsOdd(someInfo(),
                                                                                                                     -6L))
                                                   .withMessage(format("%nExpecting:%n <0L>%nnot to be equal to:%n <0L>%n"));
  }

}
