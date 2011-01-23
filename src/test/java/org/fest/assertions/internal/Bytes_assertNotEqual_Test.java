/*
 * Created on Oct 21, 2010
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

import static org.fest.assertions.error.IsEqual.isEqual;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.test.TestFailures.expectedAssertionErrorNotThrown;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Bytes#assertNotEqual(AssertionInfo, Byte, byte)}</code>.
 *
 * @author Alex Ruiz
 */
public class Bytes_assertNotEqual_Test {

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private Bytes bytes;

  @Before public void setUp() {
    failures = spy(new Failures());
    bytes = new Bytes();
    bytes.failures = failures;
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    bytes.assertNotEqual(someInfo(), null, (byte)8);
  }

  @Test public void should_pass_if_bytes_are_not_equal() {
    bytes.assertNotEqual(someInfo(), (byte)8, (byte)6);
  }

  @Test public void should_fail_if_bytes_are_equal() {
    AssertionInfo info = someInfo();
    try {
      bytes.assertNotEqual(info, (byte)6, (byte)6);
    } catch (AssertionError e) {
      verify(failures).failure(info, isEqual((byte)6, (byte)6));
      return;
    }
    throw expectedAssertionErrorNotThrown();
  }
}
