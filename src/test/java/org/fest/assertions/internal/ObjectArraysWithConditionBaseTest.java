/*
 * Created on Oct 19, 2010
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
package org.fest.assertions.internal;

import static org.mockito.Mockito.spy;

import org.junit.Before;

import org.fest.assertions.condition.JediCondition;
import org.fest.assertions.condition.JediPowerCondition;
import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;

/**
 * Base class for testing <code>{@link ObjectArrays}</code> with {@link Conditions}.
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link ObjectArrays#conditions} appropriately.
 * 
 * @author Joel Costigliola
 */
public class ObjectArraysWithConditionBaseTest extends ObjectArraysBaseTest {

  protected Conditions conditions;
  protected Condition<String> jediPower;
  protected TestCondition<Object> testCondition;
  protected Condition<String> jedi;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    conditions = spy(new Conditions());
    arrays.conditions = conditions;
    jediPower = new JediPowerCondition();
    jedi = new JediCondition();
    testCondition = new TestCondition<Object>();
  }

}