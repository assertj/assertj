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

import static org.fest.assertions.error.ShouldContain.shouldContain;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ObjectArrayFactory.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertContains(AssertionInfo, Collection, Object[])}</code>.
 *
 * @author Alex Ruiz
 */
public class Collections_assertContains_Test {

  @Rule public ExpectedException thrown = none();

  private List<String> actual;
  private Failures failures;
  private Collections collections;

  @Before public void setUp() {
    actual = list("Luke", "Yoda", "Leia");
    failures = spy(new Failures());
    collections = new Collections();
    collections.failures = failures;
  }

  @Test public void should_pass_if_actual_contains_given_values() {
    collections.assertContains(someInfo(), actual, array("Luke"));
  }

  @Test public void should_pass_if_actual_contains_given_values_in_different_order() {
    collections.assertContains(someInfo(), actual, array("Leia", "Yoda"));
  }

  @Test public void should_pass_if_actual_contains_all_given_values() {
    collections.assertContains(someInfo(), actual, array("Luke", "Yoda"));
  }

  @Test public void should_pass_if_actual_contains_given_values_more_than_once() {
    actual.addAll(list("Luke", "Luke"));
    collections.assertContains(someInfo(), actual, array("Luke"));
  }

  @Test public void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    collections.assertContains(someInfo(), actual, array("Luke", "Luke"));
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    collections.assertContains(someInfo(), actual, emptyArray());
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    collections.assertContains(someInfo(), actual, null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    collections.assertContains(someInfo(), null, array("Yoda"));
  }

  @Test public void should_fail_if_actual_does_not_contain_values() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "Luke" };
    try {
      collections.assertContains(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected, set("Han")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
