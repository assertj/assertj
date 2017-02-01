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
 * Tests for <code>{@link Longs#assertIsPositive(AssertionInfo, Long)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Longs_assertIsPositive_Test extends LongsBaseTest {

  @Test
  public void should_succeed_since_actual_is_positive() {
    longs.assertIsPositive(someInfo(), 6L);
  }

  @Test
  public void should_fail_since_actual_is_not_positive() {
    thrown.expectAssertionError("%nExpecting:%n <-6L>%nto be greater than:%n <0L> ");
    longs.assertIsPositive(someInfo(), -6L);
  }

  @Test
  public void should_succeed_since_actual_is_positive_according_to_custom_comparison_strategy() {
    longsWithAbsValueComparisonStrategy.assertIsPositive(someInfo(), -1L);
  }

  @Test
  public void should_fail_since_actual_is_not_positive_according_to_custom_comparison_strategy() {
    thrown
        .expectAssertionError("%nExpecting:%n <0L>%nto be greater than:%n <0L> when comparing values using 'AbsValueComparator'");
    longsWithAbsValueComparisonStrategy.assertIsPositive(someInfo(), 0L);
  }

}
