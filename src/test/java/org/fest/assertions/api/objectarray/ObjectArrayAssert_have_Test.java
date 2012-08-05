/*
 * Created on Mar 5, 2012
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
package org.fest.assertions.api.objectarray;

import static org.mockito.Mockito.verify;

import org.fest.assertions.api.ObjectArrayAssert;
import org.fest.assertions.api.ObjectArrayAssertBaseTest;
import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;
import org.junit.Before;

/**
 * Tests for <code>{@link ObjectArrayAssert#doNotHave(org.fest.assertions.core.Condition)}</code>.
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ObjectArrayAssert_have_Test extends ObjectArrayAssertBaseTest {

  private static Condition<Object> condition;

  @Before
  public void before() {
    condition = new TestCondition<Object>();
  }

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.doNotHave(condition);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertDoNotHave(getInfo(assertions), getActual(assertions), condition);
  }
}
