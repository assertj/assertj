/*
 * Created on Nov 20, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.util.Collections.emptyList;
import static org.fest.assertions.data.Index.atIndex;
import static org.fest.assertions.error.DoesNotContainAtIndex.doesNotContainAtIndex;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.util.Collections.list;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.List;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Index;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Lists#assertContains(AssertionInfo, List, Object, Index)}</code>.
 *
 * @author Alex Ruiz
 */
public class Lists_assertContains_Test {

  private static List<String> actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Lists lists;

  @BeforeClass public static void setUpOnce() {
    actual = list("Yoda", "Luke", "Leia");
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    lists = new Lists(failures);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    lists.assertContains(someInfo(), null, "Yoda", atIndex(0));
  }

  @Test public void should_fail_if_actual_is_empty() {
    thrown.expectAssertionError(unexpectedEmpty());
    lists.assertContains(someInfo(), emptyList(), "Yoda", atIndex(0));
  }

  @Test public void should_throw_error_if_Index_is_null() {
    thrown.expectNullPointerException("Index should not be null");
    lists.assertContains(someInfo(), actual, "Yoda", null);
  }

  @Test public void should_throw_error_if_Index_is_out_of_bounds() {
    thrown.expectIndexOutOfBoundsException("Index should be between <0> and <2> (inclusive,) but was <6>");
    lists.assertContains(someInfo(), actual, "Yoda", atIndex(6));
  }

  @Test public void should_fail_if_actual_does_not_contain_value_at_index() {
    AssertionInfo info = someInfo();
    try {
      lists.assertContains(info, actual, "Han", atIndex(1));
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, doesNotContainAtIndex(actual, "Han", atIndex(1)));
  }

  @Test public void should_pass_if_actual_contains_value_at_index() {
    lists.assertContains(someInfo(), actual, "Luke", atIndex(1));
  }
}
