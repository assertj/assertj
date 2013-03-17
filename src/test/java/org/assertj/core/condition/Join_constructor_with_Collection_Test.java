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
package org.assertj.core.condition;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.test.ExpectedException.none;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link Join#Join(Condition...)}</code>.
 * 
 * @author Yvonne Wang
 */
public class Join_constructor_with_Collection_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_Collection_is_null() {
    thrown.expectNullPointerException("The given conditions should not be null");
    Collection<Condition<Object>> conditions = null;
    new ConcreteJoin(conditions);
  }

  @Test
  public void should_throw_error_if_Collection_contains_nulls() {
    thrown.expectNullPointerException("The given conditions should not have null entries");
    Collection<Condition<Object>> conditions = new ArrayList<Condition<Object>>();
    conditions.add(new TestCondition<Object>());
    conditions.add(null);
    new ConcreteJoin(conditions);
  }

  @Test
  public void should_create_new_Join_with_passed_Conditions() {
    Collection<Condition<Object>> conditions = new ArrayList<Condition<Object>>();
    conditions.add(new TestCondition<Object>());
    Join<Object> join = new ConcreteJoin(conditions);
    assertEquals(conditions, join.conditions);
  }
}
