/*
 * Created on Dec 16, 2010
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
import static org.fest.assertions.test.ArrayFactory.arrayOfBooleans;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.BooleanArrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link BooleanArrayAssert#contains(boolean...)}</code>.
 *
 * @author Alex Ruiz
 */
public class BooleanArrayAssert_contains_Test {

  private BooleanArrays arrays;
  private BooleanArrayAssert assertions;

  @Before public void setUp() {
    arrays = mock(BooleanArrays.class);
    assertions = new BooleanArrayAssert(new boolean[0]);
    assertions.arrays = arrays;
  }

  @Test public void should_verify_that_actual_contains_given_values() {
    assertions.contains(true, false);
    verify(arrays).assertContains(assertions.info, assertions.actual, arrayOfBooleans(true, false));
  }

  @Test public void should_return_this() {
    BooleanArrayAssert returned = assertions.contains(true);
    assertSame(assertions, returned);
  }
}
