/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static org.mockito.Mockito.verify;


import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.api.TestCondition;
import org.junit.BeforeClass;

/**
 * Tests for <code>{@link AbstractIterableAssert#haveAtLeast(Condition, int)}</code>.
 * 
 * @author Nicolas François
 */
public class IterableAssert_haveAtLeast_Test extends IterableAssertBaseTest {

  private static Condition<Object> condition;

  @BeforeClass
  public static void beforeOnce() {
    condition = new TestCondition<>();
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.haveAtLeast(2, condition);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertHaveAtLeast(getInfo(assertions), getActual(assertions), 2, condition);
  }
}
