/*
 * Created on Sep 30, 2010
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

import org.fest.assertions.core.*;
import org.fest.assertions.internal.Conditions;
import org.junit.*;

/**
 * Tests for <code>{@link IntegerAssert#isNot(Condition)}</code>.
 *
 * @author Alex Ruiz
 */
public class IntegerAssert_isNot_Test {

  private static Condition<Integer> condition;

  private Conditions conditions;
  private IntegerAssert assertions;

  @BeforeClass public static void setUpOnce() {
    condition = new TestCondition<Integer>();
  }

  @Before public void setUp() {
    conditions = mock(Conditions.class);
    assertions = new IntegerAssert(8);
    assertions.conditions = conditions;
  }

  @Test public void should_verify_that_actual_does_not_satisfy_Condition() {
    assertions.isNot(condition);
    verify(conditions).assertDoesNotSatisfy(assertions.info, assertions.actual, condition);
  }

  @Test public void should_return_this() {
    IntegerAssert returned = assertions.isNot(condition);
    assertSame(assertions, returned);
  }
}
