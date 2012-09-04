/*
 * Created on Jun 4, 2012
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.internal.shortarrays;

import static org.fest.assertions.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.fest.assertions.test.ShortArrays.arrayOf;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.*;
import org.junit.*;

/**
 * Tests for <code>{@link ShortArrays#assertHasSameSizeAs(AssertionInfo, boolean[], Object[])}</code>.
 *
 * @author Nicolas François
 */
public class ShortArrays_assertHasSameSizeAs_with_Array_Test extends ShortArraysBaseTest {

  private static short[] actual;

  @BeforeClass
  public static void setUpOnce() {
    // don't use a static import here, it leads to a compilation error with oracle jdk 1.7.0_05 compiler due to the
    // other array static import.
    actual = arrayOf(6, 8);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertHasSameSizeAs(someInfo(), null, array("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    String[] other = array("Solo", "Leia", "Yoda");
    try {
      arrays.assertHasSameSizeAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveSameSizeAs(actual, actual.length, other.length));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    arrays.assertHasSameSizeAs(someInfo(), actual, array("Solo", "Leia"));
  }
}
