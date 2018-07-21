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
package org.assertj.core.internal.floats;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Floats;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Floats#assertIsNotNan(AssertionInfo, Float)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Floats_assertIsNotNaN_Test extends FloatsBaseTest {

  @Test
  public void should_succeed_since_actual_is_not_equal_to_NaN() {
    floats.assertIsNotNaN(someInfo(), 6f);
  }

  @Test
  public void should_fail_since_actual_is_equal_to_NaN() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> floats.assertIsNotNaN(someInfo(), Float.NaN))
                                                   .withMessage(format("%nExpecting:%n <NaNf>%nnot to be equal to:%n <NaNf>%n"));
  }

  @Test
  public void should_succeed_since_actual_is_not_equal_to_NaN_whatever_custom_comparison_strategy_is() {
    floatsWithAbsValueComparisonStrategy.assertIsNotNaN(someInfo(), 6f);
  }

  @Test
  public void should_fail_since_actual_is_equal_to_NaN_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> floatsWithAbsValueComparisonStrategy.assertIsNotNaN(someInfo(), Float.NaN))
                                                   .withMessage(format("%nExpecting:%n <NaNf>%nnot to be equal to:%n <NaNf>%n"));
  }
}