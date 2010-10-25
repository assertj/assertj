/*
 * Created on Oct 24, 2010
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
import static org.fest.assertions.data.Delta.delta;
import static org.fest.assertions.test.ExpectedException.none;
import static org.mockito.Mockito.*;

import org.fest.assertions.data.Delta;
import org.fest.assertions.internal.Floats;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link FloatAssert#isEqualTo(Float, Delta)}</code>.
 *
 * @author Alex Ruiz
 */
public class FloatAssert_isEqualTo_with_delta_Test {

  @Rule public ExpectedException thrown = none();

  private Floats floats;
  private FloatAssert assertions;
  private Delta<Float> delta;

  @Before public void setUp() {
    floats = mock(Floats.class);
    assertions = new FloatAssert(6f);
    assertions.floats = floats;
    delta = delta(5f);
  }

  @Test public void should_verify_that_actual_is_equal_to_expected() {
    Float expected = new Float(8f);
    assertions.isEqualTo(expected, delta);
    verify(floats).assertEqual(assertions.info, assertions.actual, expected, delta);
  }

  @Test public void should_return_this() {
    FloatAssert returned = assertions.isEqualTo(new Float(8f), delta);
    assertSame(assertions, returned);
  }
}
