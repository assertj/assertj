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
package org.fest.assertions.internal.bytes;

import static org.fest.assertions.test.TestData.someInfo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Bytes;
import org.fest.assertions.internal.BytesBaseTest;

/**
 * Tests for <code>{@link Bytes#assertIsNotZero(AssertionInfo, Byte)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Bytes_assertIsNotZero_Test extends BytesBaseTest {

  @Test
  public void should_succeed_since_actual_is_not_zero() {
    bytes.assertIsNotZero(someInfo(), (byte) 2);
  }

  @Test
  public void should_fail_since_actual_is_zero() {
    thrown.expectAssertionError("<0> should not be equal to:<0>");
    bytes.assertIsNotZero(someInfo(), (byte) 0);
  }

  @Test
  public void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    bytesWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), (byte) 1);
  }

  @Test
  public void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    try {
      bytesWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), (byte) 0);
    } catch (AssertionError e) {
      assertEquals(e.getMessage(), "<0> should not be equal to:<0>");
    }
  }

}
