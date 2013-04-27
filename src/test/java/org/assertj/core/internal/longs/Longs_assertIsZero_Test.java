/*
 * Created on Oct 21, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal.longs;

import static org.assertj.core.test.TestData.someInfo;

import static org.junit.Assert.assertEquals;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Longs#assertIsNegative(AssertionInfo, Comparable)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Longs_assertIsZero_Test extends LongsBaseTest {

  @Test
  public void should_succeed_since_actual_is_zero() {
    longs.assertIsZero(someInfo(), 0l);
  }

  @Test
  public void should_fail_since_actual_is_not_zero() {
    try {
      longs.assertIsZero(someInfo(), 2l);
    } catch (AssertionError e) {
      assertEquals("expected:<[0]L> but was:<[2]L>", e.getMessage());
    }
  }

  @Test
  public void should_succeed_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    longsWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), 1L);
  }

  @Test
  public void should_fail_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    try {
      longsWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), 0L);
    } catch (AssertionError e) {
      assertEquals(e.getMessage(), "\nExpecting:\n <0L>\nnot to be equal to:\n <0L>\n");

    }
  }

}
