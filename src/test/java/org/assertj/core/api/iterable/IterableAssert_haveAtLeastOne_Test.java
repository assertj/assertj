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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static org.mockito.Mockito.*;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.api.TestCondition;

/**
 * Tests for <code>{@link org.assertj.core.api.AbstractIterableAssert#haveAtLeastOne(org.assertj.core.api.Condition)}</code>.
 *
 * @author Adam Ruka
 */
public class IterableAssert_haveAtLeastOne_Test extends IterableAssertBaseTest {
  private static final Condition<Object> condition = new TestCondition<>();

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.haveAtLeastOne(condition);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertHaveAtLeast(getInfo(assertions), getActual(assertions), 1, condition);
  }
}
