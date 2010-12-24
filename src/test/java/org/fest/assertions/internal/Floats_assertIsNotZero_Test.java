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
package org.fest.assertions.internal;

import static org.fest.assertions.test.TestData.someInfo;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link Floats#assertIsNotZero(AssertionInfo, Float)}</code>.
 *
 * @author Alex Ruiz
 */
public class Floats_assertIsNotZero_Test {

  private Comparables comparables;
  private Floats floats;

  @Before public void setUp() {
    comparables = mock(Comparables.class);
    floats = new Floats();
    floats.comparables = comparables;
  }

  @Test public void should_verify_that_actual_is_not_equal_to_zero() {
    AssertionInfo someInfo = someInfo();
    floats.assertIsNotZero(someInfo, 6f);
    verify(comparables).assertNotEqual(someInfo, 6f, 0f);
  }
}
