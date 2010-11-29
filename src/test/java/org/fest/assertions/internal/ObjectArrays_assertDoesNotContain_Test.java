/*
 * Created on Nov 29, 2010
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

import static org.fest.assertions.error.Contains.contains;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.*;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.fest.util.Arrays.array;
import static org.fest.util.Collections.set;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link ObjectArrays#assertDoesNotContain(AssertionInfo, Object[], Object[])}</code>.
 *
 * @author Alex Ruiz
 */
public class ObjectArrays_assertDoesNotContain_Test {

  private static WritableAssertionInfo info;
  private static Object[] actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private ObjectArrays arrays;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
    actual = array("Luke", "Yoda", "Leia");
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    arrays = new ObjectArrays(failures);
  }

  @Test public void should_pass_if_actual_does_not_contain_given_values() {
    arrays.assertDoesNotContain(info, actual, array("Han"));
  }

  @Test public void should_pass_if_actual_does_not_contain_given_values_even_if_duplicated() {
    arrays.assertDoesNotContain(info, actual, array("Han", "Han", "Anakin"));
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(arrayToLookForIsEmpty());
    arrays.assertDoesNotContain(info, actual, new Object[0]);
  }

  @Test public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(arrayToLookForIsNull());
    arrays.assertDoesNotContain(info, actual, null);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    arrays.assertDoesNotContain(info, null, array("Yoda"));
  }

  @Test public void should_fail_if_actual_contains_given_values() {
    Object[] expected = { "Luke", "Yoda", "Han" };
    try {
      arrays.assertDoesNotContain(info, actual, expected);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, contains(wrap(actual), wrap(expected), set("Luke", "Yoda")));
  }
}
