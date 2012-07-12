/*
 * Created on Oct 22, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static java.lang.Boolean.TRUE;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.Booleans;
import org.junit.*;

/**
 * Tests for <code>{@link BooleanAssert#isNotEqualTo(boolean)}</code>.
 * 
 * @author Alex Ruiz
 */
public class BooleanAssert_isNotEqualTo_boolean_Test {

  private Booleans booleans;
  private BooleanAssert assertions;

  @Before
  public void setUp() {
    booleans = mock(Booleans.class);
    assertions = new BooleanAssert(TRUE);
    assertions.booleans = booleans;
  }

  @Test
  public void should_verify_that_actual_is_not_equal_to_expected() {
    assertions.isNotEqualTo(false);
    verify(booleans).assertNotEqual(assertions.info, assertions.actual, false);
  }

  @Test
  public void should_return_this() {
    BooleanAssert returned = assertions.isNotEqualTo(false);
    assertSame(assertions, returned);
  }
}
