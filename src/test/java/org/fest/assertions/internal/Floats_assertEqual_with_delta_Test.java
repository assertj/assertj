/*
 * Created on Oct 24, 2010
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

import static org.fest.assertions.data.Delta.delta;
import static org.fest.assertions.error.IsNotEqualWithDelta.isNotEqual;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.deltaIsNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.*;
import org.fest.assertions.data.Delta;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Floats#assertEqual(AssertionInfo, Float, Float, Delta)}</code>.
 *
 * @author Alex Ruiz
 */
public class Floats_assertEqual_with_delta_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Floats floats;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    floats = new Floats();
    floats.failures = failures;
  }

  @Test public void should_throw_error_if_delta_is_null() {
    thrown.expectNullPointerException(deltaIsNull());
    floats.assertEqual(info, new Float(8f), new Float(8f), null);
  }

  @Test public void should_pass_if_floats_are_equal() {
    floats.assertEqual(info, new Float(8f), new Float(8f), delta(1f));
  }

  @Test public void should_pass_if_floats_are_equal_within_delta() {
    floats.assertEqual(info, new Float(6f), new Float(8f), delta(2f));
  }

  @Test public void should_fail_if_floats_are_not_equal_within_delta() {
    Delta<Float> delta = delta(1f);
    try {
      floats.assertEqual(info, new Float(6f), new Float(8f), delta);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isNotEqual(6f, 8f, delta));
  }
}
