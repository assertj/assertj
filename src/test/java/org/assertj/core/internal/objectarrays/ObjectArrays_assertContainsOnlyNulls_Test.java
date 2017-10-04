/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */

package org.assertj.core.internal.objectarrays;

import static org.assertj.core.error.ShouldContainOnlyNulls.shouldContainOnlyNulls;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ObjectArrays;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectArrays#assertContainsOnlyNulls(AssertionInfo, Object[])}</code>.
 *
 * @author Billy Yuan
 */
public class ObjectArrays_assertContainsOnlyNulls_Test extends ObjectArraysBaseTest {
  Object[] actual = new Object[] {};

  @Test
  public void should_pass_if_actual_contains_null_only_once() {
    actual = new Object[] { null };
    arrays.assertContainsOnlyNulls(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_null_more_than_once() {
    actual = array(null, null, null);
    arrays.assertContainsOnlyNulls(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    arrays.assertContainsOnlyNulls(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_contains_no_any_element() {
    AssertionInfo info = someInfo();
    List<String> unexpectedList = newArrayList();
    try {
      arrays.assertContainsOnlyNulls(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyNulls(actual, unexpectedList));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_null_elements_and_unexpected_elements() {
    AssertionInfo info = someInfo();
    actual = array(null, null, "person");
    List<String> unexpectedList = newArrayList("person");
    try {
      arrays.assertContainsOnlyNulls(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyNulls(actual, unexpectedList));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_unexpected_elements_only() {
    AssertionInfo info = someInfo();
    actual = array("person", "person2");
    List<String> unexpectedList = newArrayList("person", "person2");
    try {
      arrays.assertContainsOnlyNulls(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyNulls(actual, unexpectedList));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
