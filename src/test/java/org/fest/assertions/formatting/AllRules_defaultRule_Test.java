/*
 * Created on Dec 3, 2010
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
package org.fest.assertions.formatting;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link AllRules#defaultRule()}</code>.
 *
 * @author Alex Ruiz
 */
public class AllRules_defaultRule_Test {

  private static AllRules allRules;

  @BeforeClass public static void setUpOnce() {
    allRules = new AllRules();
  }

  @Test public void should_return_ObjectToStringRule_as_default() {
    ToStringRule defaultRule = allRules.defaultRule();
    assertNotNull(defaultRule);
    assertEquals(ObjectToStringRule.class, defaultRule.getClass());
  }
}
