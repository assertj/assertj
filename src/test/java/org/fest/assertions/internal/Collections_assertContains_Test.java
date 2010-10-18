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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.ErrorWhenGroupDoesNotContainValues.errorWhenDoesNotContain;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.*;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.fest.assertions.core.*;
import org.fest.assertions.error.AssertionErrorFactory;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertContains(AssertionInfo, Collection, Object[])}</code>.
 *
 * @author Alex Ruiz
 */
public class Collections_assertContains_Test {

  private static WritableAssertionInfo info;
  private static List<String> actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Collections collections;


  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
    actual = list("Luke", "Yoda", "Leia");
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    collections = new Collections(failures);
  }

  @Test public void should_pass_if_actual_contains_given_values() {
    collections.assertContains(info, actual, array("Luke"));
  }

  @Test public void should_pass_if_actual_contains_given_values_in_different_order() {
    collections.assertContains(info, actual, array("Leia", "Yoda"));
  }

  @Test public void should_pass_if_actual_contains_all_given_values() {
    collections.assertContains(info, actual, array("Luke", "Yoda"));
  }

  @Test public void should_pass_if_actual_contains_given_values_more_than_once() {
    actual.addAll(list("Luke", "Luke"));
    collections.assertContains(info, actual, array("Luke"));
  }

  @Test public void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    collections.assertContains(info, actual, array("Luke", "Luke"));
  }

  @Test public void should_throw_error_if_array_of_values_is_empty() {
    thrown.expectIllegalArgumentException(arrayIsEmpty());
    collections.assertContains(info, actual, array());
  }

  @Test public void should_throw_error_if_array_of_values_is_null() {
    thrown.expectNullPointerException(arrayIsNull());
    collections.assertContains(info, actual, null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertContains(info, null, array("Yoda"));
  }

  @Test public void should_fail_if_actual_does_not_contain_values() {
    thrown.expectAssertionErrorButNotFromMockito();
    Object[] expected = { "Han", "Luke" };
    collections.assertContains(info, actual, expected);
    AssertionErrorFactory errorFactory = errorWhenDoesNotContain(actual, expected, set("Han"));
    verify(failures).failure(info, errorFactory);
  }
}
