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

package org.assertj.core.internal.iterables;

import static org.assertj.core.error.ShouldContainOnlyNulls.shouldContainOnlyNulls;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsOnlyNulls(AssertionInfo, Iterable)}</code>.
 *
 * @author Billy Yuan
 */
public class Iterables_assertContainsOnlyNulls_Test extends IterablesBaseTest {
  private List<String> actual = new ArrayList<>();

  @Test
  public void should_pass_if_actual_contains_null_once() {
    actual.add(null);
    iterables.assertContainsOnlyNulls(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_contains_null_more_than_once() {
    actual = newArrayList(null, null, null);
    iterables.assertContainsOnlyNulls(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    iterables.assertContainsOnlyNulls(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_empty() {
    AssertionInfo info = someInfo();
    try {
      iterables.assertContainsOnlyNulls(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyNulls(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_null_and_non_null_elements() {
    AssertionInfo info = someInfo();
    actual = newArrayList(null, null, "person");
    List<String> nonNulls = newArrayList("person");
    try {
      iterables.assertContainsOnlyNulls(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyNulls(actual, nonNulls));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_non_null_elements_only() {
    AssertionInfo info = someInfo();
    actual = newArrayList("person", "person2");
    List<String> nonNulls = newArrayList("person", "person2");
    try {
      iterables.assertContainsOnlyNulls(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyNulls(actual, nonNulls));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
