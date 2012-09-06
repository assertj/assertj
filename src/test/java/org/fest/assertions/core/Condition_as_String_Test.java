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

import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.TestData.someTextDescription;
import static org.junit.Assert.*;

import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Condition#as(String)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Condition_as_String_Test {

  @Rule
  public ExpectedException thrown = none();

  private Condition<Object> condition;

  @Before
  public void setUp() {
    condition = new TestCondition<Object>();
  }

  @Test
  public void should_set_description() {
    String description = someTextDescription();
    condition.as(description);
    assertEquals(description, condition.description.value());
  }

  @Test
  public void should_throw_error_of_description_is_null() {
    thrown.expectNullPointerException("The description to set should not be null");
    String description = null;
    condition.as(description);
  }

  @Test
  public void should_return_same_condition() {
    Condition<Object> returnedCondition = condition.as(someTextDescription());
    assertSame(condition, returnedCondition);
  }
}
