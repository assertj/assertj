/*
 * Created on Oct 20, 2010
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

import static org.fest.assertions.error.IsNotEqual.isNotEqual;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Shorts#assertEqual(AssertionInfo, Short, short)}</code>.
 *
 * @author Alex Ruiz
 */
public class Shorts_assertEqual_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Shorts shorts;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    shorts = new Shorts();
    shorts.failures = failures;
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    shorts.assertEqual(info, null, (short)8);
  }

  @Test public void should_pass_if_shorts_are_equal() {
    shorts.assertEqual(info, (short)8, (short)8);
  }

  @Test public void should_fail_if_shorts_are_not_equal() {
    try {
      shorts.assertEqual(info, (short)6, (short)8);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isNotEqual((short)6, (short)8));
  }
}
