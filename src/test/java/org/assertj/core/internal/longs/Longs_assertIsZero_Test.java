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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.longs;

import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Longs#assertIsZero(AssertionInfo, Long)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Longs_assertIsZero_Test extends LongsBaseTest {

  @Test
  public void should_succeed_since_actual_is_zero() {
    longs.assertIsZero(someInfo(), 0L);
  }

  @Test
  public void should_fail_since_actual_is_not_zero() {
    thrown.expectAssertionError("expected:<[0]L> but was:<[2]L>");
    longs.assertIsZero(someInfo(), 2L);
  }

  @Test
  public void should_succeed_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    longsWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), 1L);
  }

  @Test
  public void should_fail_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError("%nExpecting:%n <0L>%nnot to be equal to:%n <0L>%n");
    longsWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), 0L);
  }

}
