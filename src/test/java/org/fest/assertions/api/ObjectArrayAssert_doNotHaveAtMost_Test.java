/*
 * Created on Mar 17, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.ObjectArrayFactory.emptyArray;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.fest.assertions.internal.ObjectArrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectArrayAssert#doNotHaveAtMost(Condition, int)}</code>.
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ObjectArrayAssert_doNotHaveAtMost_Test {

  private ObjectArrays arrays;
  private ObjectArrayAssert<Object> assertions;
  private static Condition<Object> condition;

  @Before
  public void setUp() {
    arrays = mock(ObjectArrays.class);
    assertions = new ObjectArrayAssert<Object>(emptyArray());
    assertions.arrays = arrays;
    condition = new TestCondition<Object>();
  }

  @Test
  public void should_verify_that_elements_not_have_at_most() {
    assertions.doNotHaveAtMost(2, condition);
    verify(arrays).assertDoNotHaveAtMost(assertions.info, assertions.actual, 2, condition);
  }

  @Test
  public void should_return_this() {
    assertSame(assertions, assertions.doNotHaveAtMost(2, condition));
  }

}
