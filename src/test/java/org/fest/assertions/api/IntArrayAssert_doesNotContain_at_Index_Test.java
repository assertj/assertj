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
import static org.fest.assertions.test.TestData.someIndex;
import static org.mockito.Mockito.*;

import org.fest.assertions.data.Index;
import org.fest.assertions.internal.IntArrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link IntArrayAssert#doesNotContain(int, Index)}</code>.
 *
 * @author Alex Ruiz
 */
public class IntArrayAssert_doesNotContain_at_Index_Test {

  private IntArrays arrays;
  private IntArrayAssert assertions;

  @Before public void setUp() {
    arrays = mock(IntArrays.class);
    assertions = new IntArrayAssert(emptyArray());
    assertions.arrays = arrays;
  }

  @Test public void should_verify_that_actual_does_not_contain_value_at_index() {
    Index index = someIndex();
    assertions.doesNotContain(8, index);
    verify(arrays).assertDoesNotContain(assertions.info, assertions.actual, 8, index);
  }

  @Test public void should_return_this() {
    IntArrayAssert returned = assertions.doesNotContain(8, someIndex());
    assertSame(assertions, returned);
  }
}
