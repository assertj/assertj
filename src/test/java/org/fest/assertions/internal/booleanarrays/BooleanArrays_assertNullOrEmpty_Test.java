/*
 * Created on Dec 15, 2010
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
package org.fest.assertions.internal.booleanarrays;

import static org.fest.assertions.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.fest.assertions.test.BooleanArrays.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.BooleanArrays;
import org.fest.assertions.internal.BooleanArraysBaseTest;

/**
 * Tests for <code>{@link BooleanArrays#assertNullOrEmpty(AssertionInfo, boolean[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class BooleanArrays_assertNullOrEmpty_Test extends BooleanArraysBaseTest {

  @Test
  public void should_fail_if_array_is_not_null_and_is_not_empty() {
    AssertionInfo info = someInfo();
    boolean[] actual = { true, false };
    try {
      arrays.assertNullOrEmpty(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeNullOrEmpty(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_array_is_null() {
    arrays.assertNullOrEmpty(someInfo(), null);
  }

  @Test
  public void should_pass_if_array_is_empty() {
    arrays.assertNullOrEmpty(someInfo(), emptyArray());
  }
}
