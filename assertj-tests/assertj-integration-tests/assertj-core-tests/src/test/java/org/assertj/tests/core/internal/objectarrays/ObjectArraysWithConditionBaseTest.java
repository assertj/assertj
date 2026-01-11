/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.internal.objectarrays;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Condition;
import org.assertj.tests.core.testkit.JediCondition;
import org.assertj.tests.core.testkit.JediPowerCondition;
import org.assertj.tests.core.testkit.TestCondition;
import org.assertj.tests.core.testkit.TestData;
import org.junit.jupiter.api.BeforeEach;

public class ObjectArraysWithConditionBaseTest extends ObjectArraysBaseTest {

  protected static final AssertionInfo INFO = TestData.someInfo();

  protected Condition<String> jediPower;
  protected TestCondition<Object> testCondition;
  protected Condition<String> jedi;

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    jediPower = new JediPowerCondition();
    jedi = new JediCondition();
    testCondition = new TestCondition<>();
  }

}
