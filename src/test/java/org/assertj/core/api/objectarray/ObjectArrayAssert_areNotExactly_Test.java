/*
 * Created on Mar 17, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.assertj.core.api.objectarray;

import static org.mockito.Mockito.verify;

import org.assertj.core.api.Condition;
import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.assertj.core.api.TestCondition;
import org.junit.Before;

/**
 * Tests for <code>{@link ObjectArrayAssert#areNotExactly(Condition, int)}</code>.
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ObjectArrayAssert_areNotExactly_Test extends ObjectArrayAssertBaseTest {

  private static Condition<Object> condition;

  @Before
  public void before() {
    condition = new TestCondition<Object>();
  }

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.areNotExactly(2, condition);
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertAreNotExactly(getInfo(assertions), getActual(assertions), 2, condition);
  }
}
