/*
 * Created on Jul 15, 2010
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
package org.fest.assertions.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link Condition#as(String)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Condition_as_String_Test {

  private Condition<Object> condition;

  @Before public void setUp() {
    condition = new TestCondition<Object>();
  }

  @Test public void should_set_description() {
    String description = "truly wonderful, the mind of a child is";
    condition.as(description);
    assertEquals(description, condition.description.value());
  }

  @Test public void should_return_same_condition() {
    Condition<Object> returnedCondition = condition.as("I find your lack of faith disturbing");
    assertSame(condition, returnedCondition);
  }
}
