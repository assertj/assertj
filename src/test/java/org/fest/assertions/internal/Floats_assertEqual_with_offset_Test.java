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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.data.Offset.offset;
import static org.fest.assertions.error.IsNotEqualWithOffset.isNotEqual;
import static org.fest.assertions.test.ErrorMessages.offsetIsNull;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.Offset;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Floats#assertEqual(AssertionInfo, Float, Float, Offset)}</code>.
 *
 * @author Alex Ruiz
 */
public class Floats_assertEqual_with_offset_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Floats floats;

  @Before public void setUp() {
    failures = spy(new Failures());
    floats = new Floats();
    floats.failures = failures;
  }

  @Test public void should_throw_error_if_offset_is_null() {
    thrown.expectNullPointerException(offsetIsNull());
    floats.assertEqual(someInfo(), new Float(8f), new Float(8f), null);
  }

  @Test public void should_pass_if_floats_are_equal() {
    floats.assertEqual(someInfo(), new Float(8f), new Float(8f), offset(1f));
  }

  @Test public void should_pass_if_floats_are_equal_within_offset() {
    floats.assertEqual(someInfo(), new Float(6f), new Float(8f), offset(2f));
  }

  @Test public void should_fail_if_floats_are_not_equal_within_offset() {
    AssertionInfo info = someInfo();
    Offset<Float> offset = offset(1f);
    try {
      floats.assertEqual(info, new Float(6f), new Float(8f), offset);
    } catch (AssertionError e) {
      verify(failures).failure(info, isNotEqual(6f, 8f, offset));
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }
}
