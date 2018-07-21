/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.mockito.Mockito.verify;


import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractAssertBaseTest;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.junit.jupiter.api.BeforeAll;

/**
 * Tests for <code>{@link AbstractAssert#is(Condition)}</code>.
 * 
 * @author Alex Ruiz
 */
public class AbstractAssert_is_Test extends AbstractAssertBaseTest {

  private static Condition<Object> condition;

  @BeforeAll
  public static void setUpOnce() {
    condition = new TestCondition<>();
  }

  @Override
  protected ConcreteAssert invoke_api_method() {
    return assertions.is(condition);
  }

  @Override
  protected void verify_internal_effects() {
    verify(conditions).assertIs(getInfo(assertions), getActual(assertions), condition);
  }
}
