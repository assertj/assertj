/*
 * Created on Dec 14, 2010
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
package org.fest.assertions.internal.bytearrays;

import static org.fest.assertions.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ByteArrays.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ByteArrays;
import org.fest.assertions.internal.ByteArraysBaseTest;

/**
 * Tests for <code>{@link ByteArrays#assertNotEmpty(AssertionInfo, byte[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ByteArrays_assertNotEmpty_Test extends ByteArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertNotEmpty(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    AssertionInfo info = someInfo();
    try {
      arrays.assertNotEmpty(info, emptyArray());
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEmpty());
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_is_not_empty() {
    arrays.assertNotEmpty(someInfo(), arrayOf(8));
  }
}
