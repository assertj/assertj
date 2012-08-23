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
package org.fest.assertions.internal.bytearrays;

import static org.fest.assertions.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Lists.newArrayList;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.ByteArrays;
import org.fest.assertions.internal.ByteArraysBaseTest;

/**
 * Tests for <code>{@link ByteArrays#assertHasSameSizeAs(AssertionInfo, Object[], Object[])}</code>.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class ByteArrays_assertHasSameSizeAs_with_Iterable_Test extends ByteArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertHasSameSizeAs(someInfo(), null, newArrayList("Solo", "Leia"));
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    AssertionInfo info = someInfo();
    List<String> other = newArrayList("Solo", "Leia");
    try {
      arrays.assertHasSameSizeAs(info, actual, other);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveSameSizeAs(actual, actual.length, other.size()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    arrays.assertHasSameSizeAs(someInfo(), actual, newArrayList("Solo", "Leia", "Luke"));
  }
}
