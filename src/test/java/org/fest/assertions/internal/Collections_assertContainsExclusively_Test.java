/*
 * Created on Oct 3, 2010
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
import static org.fest.assertions.error.DoesNotContainExclusively.doesNotContainExclusively;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.*;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.*;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.*;

import org.fest.assertions.core.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertContainsExclusively(AssertionInfo, Collection, Object[])}</code>.
 *
 * @author Alex Ruiz
 */
public class Collections_assertContainsExclusively_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private List<String> actual;
  private Failures failures;
  private Collections collections;


  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    actual = list("Luke", "Yoda", "Leia");
    failures = spy(Failures.instance());
    collections = new Collections(failures);
  }

  @Test public void should_pass_if_actual_contains_given_values_exclusively() {
    collections.assertContainsExclusively(info, actual, array("Luke", "Yoda", "Leia"));
  }

  @Test public void should_pass_if_actual_contains_given_values_exclusively_in_different_order() {
    collections.assertContainsExclusively(info, actual, array("Leia", "Yoda", "Luke"));
  }

  @Test public void should_pass_if_actual_contains_given_values_exclusively_more_than_once() {
    actual.addAll(list("Luke", "Luke"));
    collections.assertContainsExclusively(info, actual, array("Luke", "Yoda", "Leia"));
  }

  @Test public void should_pass_if_actual_contains_given_values_exclusively_even_if_duplicated() {
    collections.assertContainsExclusively(info, actual, array("Luke", "Luke", "Luke", "Yoda", "Leia"));
  }

  @Test public void should_throw_error_if_array_of_values_is_empty() {
    thrown.expectIllegalArgumentException(arrayIsEmpty());
    collections.assertContainsExclusively(info, actual, array());
  }

  @Test public void should_throw_error_if_array_of_values_is_null() {
    thrown.expectNullPointerException(arrayIsNull());
    collections.assertContainsExclusively(info, emptyList(), null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertContainsExclusively(info, null, array("Yoda"));
  }

  @Test public void should_fail_if_actual_does_not_contain_given_values_exclusively() {
    Object[] expected = { "Luke", "Yoda", "Han" };
    try {
      collections.assertContainsExclusively(info, actual, expected);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, doesNotContainExclusively(actual, expected, set("Leia"), set("Han")));
  }
}
