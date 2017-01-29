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
package org.assertj.core.internal;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.assertj.core.condition.JediCondition;
import org.assertj.core.condition.JediPowerCondition;
import org.assertj.core.internal.Conditions;
import org.assertj.core.internal.ObjectArrays;
import org.junit.Before;


/**
 * Base class for testing <code>{@link ObjectArrays}</code> with {@link Conditions}.
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link ObjectArrays#conditions} appropriately.
 * 
 * @author Joel Costigliola
 */
public class ObjectArraysWithConditionBaseTest extends ObjectArraysBaseTest {

  protected Condition<String> jediPower;
  protected TestCondition<Object> testCondition;
  protected Condition<String> jedi;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    jediPower = new JediPowerCondition();
    jedi = new JediCondition();
    testCondition = new TestCondition<>();
  }

}