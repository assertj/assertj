/*
 * Created on Jan 14, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.internal.floats;

import static org.fest.assertions.test.TestData.someInfo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Floats;
import org.fest.assertions.internal.FloatsBaseTest;

/**
 * Tests for <code>{@link Floats#assertIsNaN(AssertionInfo, Float)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class Floats_assertIsNaN_Test extends FloatsBaseTest {

  @Test
  public void should_succeed_since_actual_is_equal_to_NaN() {
    floats.assertIsNaN(someInfo(), Float.NaN);
  }

  @Test
  public void should_fail_since_actual_is_not_equal_to_NaN() {
    try {
      floats.assertIsNaN(someInfo(), 6.0f);
    } catch (AssertionError e) {
      assertEquals(e.getMessage(), "expected:<[NaN]f> but was:<[6.0]f>");
    }
  }

  @Test
  public void should_succeed_since_actual_is_equal_to_NaN_whatever_custom_comparison_strategy_is() {
    floatsWithAbsValueComparisonStrategy.assertIsNaN(someInfo(), Float.NaN);
  }

  @Test
  public void should_fail_since_actual_is_not_equal_to_NaN_whatever_custom_comparison_strategy_is() {
    try {
      floatsWithAbsValueComparisonStrategy.assertIsNaN(someInfo(), 6.0f);
    } catch (AssertionError e) {
      assertEquals(e.getMessage(), "expected:<[NaN]f> but was:<[6.0]f>");
    }
  }
}