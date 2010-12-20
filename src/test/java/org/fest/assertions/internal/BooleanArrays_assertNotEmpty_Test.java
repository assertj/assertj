/*
 * Created on Dec 15, 2010
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

import static org.fest.assertions.error.IsEmpty.isEmpty;
import static org.fest.assertions.test.ArrayFactory.arrayOfBooleans;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link BooleanArrays#assertNotEmpty(AssertionInfo, boolean[])}</code>.
 *
 * @author Alex Ruiz
 */
public class BooleanArrays_assertNotEmpty_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private BooleanArrays arrays;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    arrays = new BooleanArrays(failures);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    arrays.assertNotEmpty(info, null);
  }

  @Test public void should_fail_if_actual_is_empty() {
    boolean[] actual = new boolean[0];
    try {
      arrays.assertNotEmpty(info, actual);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isEmpty());
  }

  @Test public void should_pass_if_actual_is_not_empty() {
    arrays.assertNotEmpty(info, arrayOfBooleans(true));
  }
}
