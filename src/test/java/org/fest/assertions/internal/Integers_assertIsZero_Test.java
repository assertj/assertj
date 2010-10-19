/*
 * Created on Oct 18, 2010
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
package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedException.none;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Integers#assertIsZero(AssertionInfo, Integer)}</code>.
 *
 * @author Alex Ruiz
 */
public class Integers_assertIsZero_Test {

  private static WritableAssertionInfo info;

  @Rule public ExpectedException thrown = none();

  private Comparables comparables;
  private Integers integers;

  @BeforeClass public static void setUpOnce() {
    info = new WritableAssertionInfo();
  }

  @Before public void setUp() {
    comparables = mock(Comparables.class);
    integers = new Integers();
    integers.comparables = comparables;
  }

  @Test public void should_verify_that_actual_is_equal_to_zero() {
    integers.assertIsZero(info, 6);
    verify(comparables).assertEqual(info, 6, 0);
  }
}
