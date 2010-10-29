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

import static org.fest.assertions.data.Offset.offset;
import static org.fest.assertions.error.IsNotEqualWithOffset.isNotEqual;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.offsetIsNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.data.Offset;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Doubles#assertEqual(AssertionInfo, Double, Double, Offset)}</code>.
 *
 * @author Alex Ruiz
 */
public class Doubles_assertEqual_with_offset_Test {

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

  @Test public void should_throw_error_if_offset_is_null() {
    thrown.expectNullPointerException(offsetIsNull());
    doubles.assertEqual(info, new Double(8d), new Double(8d), null);
  }

  @Test public void should_pass_if_doubles_are_equal() {
    doubles.assertEqual(info, new Double(8d), new Double(8d), offset(1d));
  }

  @Test public void should_pass_if_doubles_are_equal_within_offset() {
    doubles.assertEqual(info, new Double(6d), new Double(8d), offset(2d));
  }

  @Test public void should_fail_if_doubles_are_not_equal_within_offset() {
    Offset<Double> offset = offset(1d);
    try {
      doubles.assertEqual(info, new Double(6d), new Double(8d), offset);
      fail();
    } catch (AssertionError e) {}
    verify(failures).failure(info, isNotEqual(6d, 8d, offset));
  }
}
