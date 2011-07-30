/*
 * Created on Dec 24, 2010
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

import org.junit.*;

import org.fest.assertions.internal.Throwables;

/**
 * Tests for <code>{@link ThrowableAssert#hasNoCause()}</code>.
 *
 * @author Joel Costigliola
 */
public class ThrowableAssert_hasNoCause_Test {

  private Throwables throwables;
  private ThrowableAssert assertions;

  @Before public void setUp() {
    throwables = mock(Throwables.class);
    assertions = new ThrowableAssert(new Throwable("throwable message"));
    assertions.throwables = throwables;
  }

  @Test public void should_verify_that_actual_contains_sequence() {
    assertions.hasNoCause();
    verify(throwables).assertHasNoCause(assertions.info, assertions.actual);
  }

  @Test public void should_return_this() {
    ThrowableAssert returned = assertions.hasNoCause();
    assertSame(assertions, returned);
  }
}
