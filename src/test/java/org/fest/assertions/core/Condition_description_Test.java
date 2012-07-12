/*
 * Created on Jul 15, 2010
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
package org.fest.assertions.core;

import static org.junit.Assert.assertEquals;

import org.fest.assertions.description.TextDescription;
import org.junit.*;

/**
 * Tests for <code>{@link Condition#description()}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Condition_description_Test {

  private Condition<Object> condition;

  @Before
  public void setUp() {
    condition = new TestCondition<Object>();
  }

  @Test
  public void should_return_description() {
    String description = "do or do not, there is not try";
    condition.description = new TextDescription(description);
    assertEquals(description, condition.description().value());
  }
}
