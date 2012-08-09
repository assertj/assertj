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
package org.fest.assertions.internal.doubles;

import static org.fest.assertions.test.TestData.someInfo;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Doubles;
import org.fest.assertions.internal.DoublesBaseTest;

/**
 * Tests for <code>{@link Doubles#assertIsNegative(AssertionInfo, Double)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Doubles_assertIsNegative_Test extends DoublesBaseTest {

  @Test
  public void should_succeed_since_actual_is_negative() {
    doubles.assertIsNegative(someInfo(), -6.0d);
  }

  @Test
  public void should_fail_since_actual_is_not_negative() {
    thrown.expectAssertionError("expected:<6.0> to be less than:<0.0>");
    doubles.assertIsNegative(someInfo(), 6.0d);
  }

  @Test
  public void should_fail_since_actual_is_not_negative_according_to_absolute_value_comparison_strategy() {
    thrown.expectAssertionError("expected:<-6.0> to be less than:<0.0> according to 'AbsValueComparator' comparator");
    doublesWithAbsValueComparisonStrategy.assertIsNegative(someInfo(), -6.0d);
  }

  @Test
  public void should_fail_since_actual_is_not_negative_according_to_absolute_value_comparison_strategy2() {
    thrown.expectAssertionError("expected:<6.0> to be less than:<0.0> according to 'AbsValueComparator' comparator");
    doublesWithAbsValueComparisonStrategy.assertIsNegative(someInfo(), 6.0d);
  }

}
