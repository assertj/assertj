/*
 * Created on Oct 19, 2010
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

import static org.fest.assertions.error.IsNotLessThanOrEqualTo.isNotLessThanOrEqualTo;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Comparables#assertLessThanOrEqualTo(AssertionInfo, Comparable, Comparable)}</code>.
 *
 * @author Alex Ruiz
 */
public class Comparables_assertLessThanOrEqualTo_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Comparables comparables;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    comparables = new Comparables(failures);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    comparables.assertLessThanOrEqualTo(info, null, 8);
  }

  @Test public void should_pass_if_actual_is_less_than_other() {
    comparables.assertLessThanOrEqualTo(info, 6, 8);
  }

  @Test public void should_pass_if_actual_is_equal_to_other() {
    comparables.assertLessThanOrEqualTo(info, 6, 6);
  }

  @Test public void should_fail_if_actual_is_greater_than_other() {
    try {
      comparables.assertLessThanOrEqualTo(info, 8, 6);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isNotLessThanOrEqualTo(8, 6));
  }
}
