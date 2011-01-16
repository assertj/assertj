/*
 * Created on Dec 20, 2010
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
import static org.fest.assertions.test.DoubleArrayFactory.emptyArray;
import static org.fest.assertions.test.TestData.someIndex;
import static org.mockito.Mockito.*;

import org.fest.assertions.data.Index;
import org.fest.assertions.internal.DoubleArrays;
import org.junit.*;

/**
 * Tests for <code>{@link DoubleArrayAssert#contains(double, Index)}</code>.
 *
 * @author Alex Ruiz
 */
public class DoubleArrayAssert_contains_at_Index_Test {

  private DoubleArrays arrays;
  private DoubleArrayAssert assertions;

  @Before public void setUp() {
    arrays = mock(DoubleArrays.class);
    assertions = new DoubleArrayAssert(emptyArray());
    assertions.arrays = arrays;
  }

  @Test public void should_verify_that_actual_contains_value_at_index() {
    Index index = someIndex();
    assertions.contains(8d, index);
    verify(arrays).assertContains(assertions.info, assertions.actual, 8d, index);
  }

  @Test public void should_return_this() {
    DoubleArrayAssert returned = assertions.contains(8d, someIndex());
    assertSame(assertions, returned);
  }
}
