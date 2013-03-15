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
package org.assert4j.core.assertions.condition;

import static junit.framework.Assert.assertEquals;
import static org.assert4j.core.assertions.test.ExpectedException.none;
import static org.assert4j.core.util.Arrays.array;
import static org.assert4j.core.util.Lists.newArrayList;



import org.assert4j.core.assertions.condition.AllOf;
import org.assert4j.core.assertions.core.*;
import org.assert4j.core.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link AllOf#allOf(Condition...)}</code>.
 * 
 * @author Yvonne Wang
 */
public class AllOf_allOf_with_array_Test {

  @Rule
  public ExpectedException thrown = none();

  @SuppressWarnings("unchecked")
  @Test
  public void should_create_new_AllOf_with_passed_Conditions() {
    Condition<Object>[] conditions = array(new TestCondition<Object>(), new TestCondition<Object>());
    Condition<Object> created = AllOf.allOf(conditions);
    assertEquals(AllOf.class, created.getClass());
    AllOf<Object> allOf = (AllOf<Object>) created;
    assertEquals(newArrayList(conditions), allOf.conditions);
  }
}
