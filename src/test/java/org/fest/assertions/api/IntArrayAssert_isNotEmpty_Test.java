/*
 * Created on Dec 14, 2010
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
import static org.fest.assertions.test.IntArrayFactory.emptyArray;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.IntArrays;
import org.junit.*;

/**
 * Tests for <code>{@link IntArrayAssert#isNotEmpty()}</code>.
 *
 * @author Alex Ruiz
 */
public class IntArrayAssert_isNotEmpty_Test {

  private IntArrays arrays;
  private IntArrayAssert assertions;

  @Before public void setUp() {
    arrays = mock(IntArrays.class);
    assertions = new IntArrayAssert(emptyArray());
    assertions.arrays = arrays;
  }

  @Test public void should_verify_that_actual_is_not_empty() {
    assertions.isNotEmpty();
    verify(arrays).assertNotEmpty(assertions.info, assertions.actual);
  }

  @Test public void should_return_this() {
    IntArrayAssert returned = assertions.isNotEmpty();
    assertSame(assertions, returned);
  }
}
