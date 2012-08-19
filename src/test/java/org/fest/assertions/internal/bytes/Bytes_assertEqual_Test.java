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

import static org.fest.assertions.error.ShouldBeEqual.shouldBeEqual;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Bytes;
import org.fest.assertions.internal.BytesBaseTest;

/**
 * Tests for <code>{@link Bytes#assertEqual(AssertionInfo, Byte, byte)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Bytes_assertEqual_Test extends BytesBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    bytes.assertEqual(someInfo(), null, (byte) 8);
  }

  @Test
  public void should_pass_if_bytes_are_equal() {
    bytes.assertEqual(someInfo(), (byte) 8, (byte) 8);
  }

  @Test
  public void should_fail_if_bytes_are_not_equal() {
    AssertionInfo info = someInfo();
    try {
      bytes.assertEqual(info, (byte) 6, (byte) 8);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual((byte) 6, (byte) 8));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    bytesWithAbsValueComparisonStrategy.assertEqual(someInfo(), null, (byte) 8);
  }

  @Test
  public void should_pass_if_bytes_are_equal_according_to_custom_comparison_strategy() {
    bytesWithAbsValueComparisonStrategy.assertEqual(someInfo(), (byte) 8, (byte) -8);
  }

  @Test
  public void should_fail_if_bytes_are_not_equal_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      bytesWithAbsValueComparisonStrategy.assertEqual(info, (byte) 6, (byte) 8);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual((byte) 6, (byte) 8, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
