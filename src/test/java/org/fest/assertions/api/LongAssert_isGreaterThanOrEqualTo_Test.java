/*
 * Created on Oct 20, 2010
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

import org.fest.assertions.internal.Comparables;
import org.junit.*;

/**
 * Tests for <code>{@link LongAssert#isGreaterThanOrEqualTo(Long)}</code>.
 *
 * @author Alex Ruiz
 */
public class LongAssert_isGreaterThanOrEqualTo_Test {

  private Comparables comparables;
  private LongAssert assertions;

  @Before public void setUp() {
    comparables = mock(Comparables.class);
    assertions = new LongAssert(8L);
    assertions.comparables = comparables;
  }

  @Test public void should_verify_that_actual_is_greater_than_expected() {
    assertions.isGreaterThanOrEqualTo(new Long(6));
    verify(comparables).assertGreaterThanOrEqualTo(assertions.info, assertions.actual, 6L);
  }

  @Test public void should_return_this() {
    LongAssert returned = assertions.isGreaterThanOrEqualTo(new Long(6));
    assertSame(assertions, returned);
  }
}
