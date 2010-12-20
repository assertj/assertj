/*
 * Created on Oct 28, 2010
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

import static org.fest.assertions.error.IsNotLessThan.isNotLessThan;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Doubles#assertLessThan(AssertionInfo, Double, double)}</code>.
 *
 * @author Alex Ruiz
 */
public class Doubles_assertLessThan_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Doubles doubles;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    doubles = new Doubles();
    doubles.failures = failures;
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    doubles.assertLessThan(info, null, 8d);
  }

  @Test public void should_pass_if_actual_is_less_than_other() {
    doubles.assertLessThan(info, 6d, 8d);
  }

  @Test public void should_fail_if_actual_is_equal_to_other() {
    try {
      doubles.assertLessThan(info, 6d, 6d);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isNotLessThan(6d, 6d));
  }

  @Test public void should_fail_if_actual_is_greater_than_other() {
    try {
      doubles.assertLessThan(info, 8d, 6d);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isNotLessThan(8d, 6d));
  }
}
