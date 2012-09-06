/*
 * Created on Feb 5, 2011
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

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.util.Arrays.array;
import static org.fest.util.Lists.newArrayList;

import org.fest.assertions.core.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link AnyOf#anyOf(Condition...)}</code>.
 * 
 * @author Yvonne Wang
 */
public class AnyOf_anyOf_with_array_Test {

  @Rule
  public ExpectedException thrown = none();

  @SuppressWarnings("unchecked")
  @Test
  public void should_create_new_AnyOf_with_passed_Conditions() {
    Condition<Object>[] conditions = array(new TestCondition<Object>(), new TestCondition<Object>());
    Condition<Object> created = AnyOf.anyOf(conditions);
    assertEquals(AnyOf.class, created.getClass());
    AnyOf<Object> anyOf = (AnyOf<Object>) created;
    assertEquals(newArrayList(conditions), anyOf.conditions);
  }
}
