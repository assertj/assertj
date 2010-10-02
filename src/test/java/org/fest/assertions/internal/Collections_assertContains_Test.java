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

import static java.util.Collections.emptyList;
import static org.fest.assertions.error.WhenDoesNotContainErrorFactory.errorWhenDoesNotContain;
import static org.fest.assertions.test.Exceptions.assertionFailingOnPurpose;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.list;
import static org.mockito.Mockito.*;

import java.util.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
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
    failures = mock(Failures.class);
    collections = new Collections(failures);
  }

  @Test public void should_pass_if_actual_contains_values() {
    collections.assertContains(info, actual, array("Luke"));
  }

  @Test public void should_pass_if_actual_contains_values_in_different_order() {
    collections.assertContains(info, actual, array("Leia", "Yoda"));
  }

  @Test public void should_pass_if_actual_contains_all_values() {
    collections.assertContains(info, actual, array("Luke", "Yoda"));
  }

  @Test public void should_throw_error_if_array_of_values_is_empty() {
    thrown.expectIllegalArgumentException("The array of values to evaluate should not be empty");
    collections.assertContains(info, actual, array());
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    collections.assertContains(info, null, new Object[0]);
  }

  @Test public void should_throw_error_if_array_of_values_is_null() {
    thrown.expectNullPointerException("The array of values to evaluate should not be null");
    collections.assertContains(info, emptyList(), null);
  }

  @Test public void should_fail_if_actual_does_not_contain_values() {
    AssertionError expectedError = assertionFailingOnPurpose();
    Object[] expected = { "Han", "Luke" };
    when(failures.failure(info, errorWhenDoesNotContain(actual, expected, set("Han")))).thenReturn(expectedError);
    thrown.expect(expectedError);
    collections.assertContains(info, actual, expected);
  }

  private static <T> Set<T> set(T...elements) {
    return new LinkedHashSet<T>(list(elements));
  }
}
