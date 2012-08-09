/*
 * Created on Oct 21, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Ungreater required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.floats;

import static org.fest.assertions.test.TestData.someInfo;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Floats;
import org.fest.assertions.internal.FloatsBaseTest;

/**
 * Tests for <code>{@link Floats#assertIsPositive(AssertionInfo, Float)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Floats_assertIsPositive_Test extends FloatsBaseTest {

  @Test
  public void should_succeed_since_actual_is_positive() {
    floats.assertIsPositive(someInfo(), 6.0f);
  }

  @Test
  public void should_fail_since_actual_is_not_positive() {
    thrown.expectAssertionError("expected:<-6.0f> to be greater than:<0.0f>");
    floats.assertIsPositive(someInfo(), -6.0f);
  }

  @Test
  public void should_succeed_since_actual_is_positive_according_to_absolute_value_comparison_strategy() {
    floatsWithAbsValueComparisonStrategy.assertIsPositive(someInfo(), (float) 6);
  }

  @Test
  public void should_succeed_since_actual_is_positive_according_to_absolute_value_comparison_strategy2() {
    floatsWithAbsValueComparisonStrategy.assertIsPositive(someInfo(), -6.0f);
  }

}
