/*
 * Created on Sep 30, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ShouldBeSorted.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Collections.list;

import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;

/**
 * Tests for <code>{@link Collections#assertDoesNotContainNull(AssertionInfo, Collection)}</code>.
 *
 * @author Joel Costigliola
 */
public class Lists_assertIsSorted_Test {

  @Rule public ExpectedException thrown = none();

  private List<String> actual;
  private Failures failures;
  private Lists lists;

  @Before public void setUp() {
    actual = list("Leia", "Luke", "Luke", "Vador", "Yoda");
    failures = spy(new Failures());
    lists = new Lists();
    lists.failures = failures;
  }

  @Test
  public void should_pass_if_actual_is_sorted_in_ascending_order() {
    lists.assertIsSorted(someInfo(), actual);
  }

  @Test
  public void should_pass_if_actual_is_empty() {
    lists.assertIsSorted(someInfo(), list());
  }

  @Test
  public void should_pass_if_actual_contains_only_one_comparable_element() {
    lists.assertIsSorted(someInfo(), list("Obiwan"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    lists.assertIsSorted(someInfo(), null);
  }

  @Test
  public void should_fail_if_actual_is_not_sorted_in_ascending_order() {
    AssertionInfo info = someInfo();
    actual = list("Luke", "Yoda", "Leia");
    try {
      lists.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeSorted(1, actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_only_one_non_comparable_element() {
    AssertionInfo info = someInfo();
    List<Object> actual = list(new Object());
    try {
      lists.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveMutuallyComparableElements(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_non_comparable_elements() {
    AssertionInfo info = someInfo();
    List<Object> actual = list("bar", new Object(), "foo");
    try {
      lists.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveMutuallyComparableElements(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_has_some_not_mutually_comparable_elements() {
    AssertionInfo info = someInfo();
    List<Object> actual = list();
    actual.add("bar");
    actual.add(new Integer(5));
    actual.add("foo" );
    try {
      lists.assertIsSorted(info, actual);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveMutuallyComparableElements(actual));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
