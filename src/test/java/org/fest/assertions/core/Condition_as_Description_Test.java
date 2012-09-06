/*
 * Created on Jan 15, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.core;

import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.TestData.someTextDescription;
import static org.junit.Assert.assertSame;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Condition#as(Description)}</code>.
 * 
 * @author Yvonne Wang
 */
public class Condition_as_Description_Test {

  @Rule
  public ExpectedException thrown = none();

  private static Description description;

  @BeforeClass
  public static void setUpOnce() {
    description = new TestDescription(someTextDescription());
  }

  private Condition<Object> condition;

  @Before
  public void setUp() {
    condition = new TestCondition<Object>();
  }

  @Test
  public void should_set_description() {
    condition.as(description);
    assertSame(description, condition.description());
  }

  @Test
  public void should_throw_error_of_description_is_null() {
    thrown.expectNullPointerException("The description to set should not be null");
    condition.as((Description) null);
  }

  @Test
  public void should_return_same_condition() {
    Condition<Object> returnedCondition = condition.as(description);
    assertSame(condition, returnedCondition);
  }
}
