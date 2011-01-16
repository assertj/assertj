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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.Longs;
import org.junit.*;

/**
 * Tests for <code>{@link LongAssert#isLessThanOrEqualTo(long)}</code>.
 *
 * @author Alex Ruiz
 */
public class LongAssert_isLessThanOrEqualTo_long_Test {

  private Longs longs;
  private LongAssert assertions;

  @Before public void setUp() {
    longs = mock(Longs.class);
    assertions = new LongAssert(6L);
    assertions.longs = longs;
  }

  @Test public void should_verify_that_actual_is_less_than_expected() {
    assertions.isLessThanOrEqualTo(8L);
    verify(longs).assertLessThanOrEqualTo(assertions.info, assertions.actual, 8L);
  }

  @Test public void should_return_this() {
    LongAssert returned = assertions.isLessThanOrEqualTo(8L);
    assertSame(assertions, returned);
  }
}
