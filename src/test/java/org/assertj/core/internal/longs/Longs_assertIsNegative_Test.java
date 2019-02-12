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
 * Copyright 2012-2019 the original author or authors.
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
 * Tests for <code>{@link Longs#assertIsNegative(AssertionInfo, Long)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Longs_assertIsNegative_Test extends LongsBaseTest {

  @Test
  public void should_succeed_since_actual_is_negative() {
    longs.assertIsNegative(someInfo(), -6L);
  }

  @Test
  public void should_fail_since_actual_is_not_negative() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertIsNegative(someInfo(), 6L))
                                                   .withMessage(format("%nExpecting:%n <6L>%nto be less than:%n <0L> "));
  }

  @Test
  public void should_fail_since_actual_can_not_be_negative_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longsWithAbsValueComparisonStrategy.assertIsNegative(someInfo(), -1L))
                                                   .withMessage(format("%nExpecting:%n <-1L>%nto be less than:%n <0L> when comparing values using AbsValueComparator"));
  }

  @Test
  public void should_fail_since_actual_is_not_negative_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longsWithAbsValueComparisonStrategy.assertIsNegative(someInfo(), 1L))
                                                   .withMessage(format("%nExpecting:%n <1L>%nto be less than:%n <0L> when comparing values using AbsValueComparator"));
  }

}
