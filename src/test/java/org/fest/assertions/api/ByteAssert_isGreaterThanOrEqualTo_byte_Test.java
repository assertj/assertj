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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.Bytes;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ByteAssert#isGreaterThanOrEqualTo(byte)}</code>.
 *
 * @author Alex Ruiz
 */
public class ByteAssert_isGreaterThanOrEqualTo_byte_Test {

  private Bytes bytes;
  private ByteAssert assertions;

  @Before public void setUp() {
    bytes = mock(Bytes.class);
    assertions = new ByteAssert((byte)8);
    assertions.bytes = bytes;
  }

  @Test public void should_verify_that_actual_is_greater_than_expected() {
    assertions.isGreaterThanOrEqualTo((byte)6);
    verify(bytes).assertGreaterThanOrEqualTo(assertions.info, assertions.actual, (byte)6);
  }

  @Test public void should_return_this() {
    ByteAssert returned = assertions.isGreaterThanOrEqualTo((byte)6);
    assertSame(assertions, returned);
  }
}
