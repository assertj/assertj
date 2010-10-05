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
import static org.fest.assertions.error.WhenDoesNotContainOnlyErrorFactory.errorWhenDoesNotContainOnly;
import static org.fest.assertions.test.Exceptions.assertionFailingOnPurpose;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.*;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.*;

import java.util.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.error.AssertionErrorFactory;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Collections#assertContainsOnly(AssertionInfo, Collection, Object[])}</code>.
 *
 * @author Alex Ruiz
 */
public class Collections_assertContainsOnly_Test {

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
    failures = mock(Failures.class);
    collections = new Collections(failures);
  }


  @Test public void should_pass_if_actual_contains_only_given_values() {
    collections.assertContainsOnly(info, actual, array("Luke"));
  }

  @Test public void should_pass_if_actual_contains_only_given_values_in_different_order() {
    collections.assertContainsOnly(info, actual, array("Leia", "Yoda"));
  }

  @Test public void should_pass_if_actual_contains_only_given_values_more_than_once() {
    actual.addAll(list("Luke", "Luke"));
    collections.assertContainsOnly(info, actual, array("Luke"));
  }

  @Test public void should_pass_if_actual_contains_only_given_values_even_if_duplicated() {
    collections.assertContainsOnly(info, actual, array("Luke", "Luke"));
  }

  @Test public void should_throw_error_if_array_of_values_is_empty() {
    thrown.expectIllegalArgumentException(arrayIsEmpty());
    collections.assertContainsOnly(info, actual, array());
  }

  @Test public void should_throw_error_if_array_of_values_is_null() {
    thrown.expectNullPointerException(arrayIsNull());
    collections.assertContainsOnly(info, emptyList(), null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertContainsOnly(info, null, array("Yoda"));
  }

  @Test public void should_fail_if_actual_does_not_contain_only_given_values() {
    AssertionError expectedError = assertionFailingOnPurpose();
    Object[] expected = { "Luke", "Yoda", "Han" };
    AssertionErrorFactory errorFactory = errorWhenDoesNotContainOnly(expected, expected, set("Han"), list("Leia"));
    when(failures.failure(info, errorFactory)).thenReturn(expectedError);
    thrown.expect(expectedError);
    collections.assertContainsOnly(info, actual, expected);
  }

  private static <T> Set<T> set(T...elements) {
    return new LinkedHashSet<T>(list(elements));
  }
}
