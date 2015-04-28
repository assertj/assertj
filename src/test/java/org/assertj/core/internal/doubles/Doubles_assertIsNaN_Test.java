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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.doubles;

import static org.assertj.core.test.TestData.someInfo;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Doubles;
import org.assertj.core.internal.DoublesBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Doubles#assertIsNaN(AssertionInfo, Double)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Doubles_assertIsNaN_Test extends DoublesBaseTest {

  @Test
  public void should_succeed_since_actual_is_equal_to_NaN() {
    doubles.assertIsNaN(someInfo(), Double.NaN);
  }

  @Test
  public void should_fail_since_actual_is_not_equal_to_NaN() {
    try {
      doubles.assertIsNaN(someInfo(), 6d);
    } catch (AssertionError e) {
      assertThat(e.getMessage()).isEqualTo("expected:<[NaN]> but was:<[6.0]>");
    }
  }

  @Test
  public void should_succeed_since_actual_is_equal_to_NaN_whatever_custom_comparison_strategy_is() {
    doublesWithAbsValueComparisonStrategy.assertIsNaN(someInfo(), Double.NaN);
  }

  @Test
  public void should_fail_since_actual_is_not_equal_to_NaN_whatever_custom_comparison_strategy_is() {
    try {
      doublesWithAbsValueComparisonStrategy.assertIsNaN(someInfo(), 6d);
    } catch (AssertionError e) {
      assertThat(e.getMessage()).isEqualTo("expected:<[NaN]> but was:<[6.0]>");
    }
  }
}