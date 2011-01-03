/*
 * Created on Oct 17, 2010
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
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.Integers;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link IntegerAssert#isEqualTo(int)}</code>.
 *
 * @author Alex Ruiz
 */
public class IntegerAssert_isEqualTo_int_Test {

  private Integers integers;
  private IntegerAssert assertions;

  @Before public void setUp() {
    integers = mock(Integers.class);
    assertions = new IntegerAssert(6);
    assertions.integers = integers;
  }

  @Test public void should_verify_that_actual_is_equal_to_expected() {
    assertions.isEqualTo(8);
    verify(integers).assertEqual(assertions.info, assertions.actual, 8);
  }

  @Test public void should_return_this() {
    IntegerAssert returned = assertions.isEqualTo(8);
    assertSame(assertions, returned);
  }
}
