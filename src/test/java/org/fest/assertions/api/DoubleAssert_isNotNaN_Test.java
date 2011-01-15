/*
 * Created on Jan 14, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.Doubles;
import org.junit.*;

/**
 * Tests for <code>{@link DoubleAssert#isNotNaN()}</code>.
 *
 * @author Yvonne Wang
 */
public class DoubleAssert_isNotNaN_Test {

  private Doubles doubles;
  private DoubleAssert assertions;

  @Before public void setUp() {
    doubles = mock(Doubles.class);
    assertions = new DoubleAssert(8d);
    assertions.doubles = doubles;
  }

  @Test public void should_verify_that_actual_is_not_NaN() {
    assertions.isNotNaN();
    verify(doubles).assertIsNotNaN(assertions.info, assertions.actual);
  }

  @Test public void should_return_this() {
    DoubleAssert returned = assertions.isNotNaN();
    assertSame(assertions, returned);
  }
}
