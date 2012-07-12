/*
 * Created on Feb 7, 2011
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
package org.fest.assertions.condition;

import static junit.framework.Assert.*;
import static org.fest.assertions.condition.AllOf.allOf;

import org.fest.assertions.core.*;
import org.junit.*;

/**
 * Tests for <code>{@link AllOf#matches(Object)}</code>.
 * 
 * @author Yvonne Wang
 */
public class AllOf_matches_Test {

  private TestCondition<Object> condition1;
  private TestCondition<Object> condition2;
  private Condition<Object> allOf;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    condition1 = new TestCondition<Object>();
    condition2 = new TestCondition<Object>();
    allOf = allOf(condition1, condition2);
  }

  @Test
  public void should_match_if_all_Condition_match() {
    condition1.shouldMatch(true);
    condition2.shouldMatch(true);
    assertTrue(allOf.matches("Yoda"));
  }

  @Test
  public void should_not_match_if_at_least_one_Condition_does_not_match() {
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    assertFalse(allOf.matches("Yoda"));
  }
}
