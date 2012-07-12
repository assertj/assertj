/*
 * Created on Mar 22, 2012
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

import static org.fest.assertions.condition.DoesNotHave.doesNotHave;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.core.Condition;
import org.fest.assertions.core.TestCondition;

/**
 * Tests for <code>{@link Not#matches(Object)}</code>.
 * 
 * @author Nicolas François
 */
public class DoesNotHave_matches_Test {

  private TestCondition<Object> condition;
  private Condition<Object> doesNotHave;

  @Before
  public void setUp() {
    condition = new TestCondition<Object>();
    doesNotHave = doesNotHave(condition);
  }

  @Test
  public void should_match_if_Condition_not_match() {
    condition.shouldMatch(false);
    assertTrue(doesNotHave.matches("Yoda"));
  }

  @Test
  public void should_not_match_Conditions_match() {
    condition.shouldMatch(true);
    assertFalse(doesNotHave.matches("Yoda"));
  }

}
