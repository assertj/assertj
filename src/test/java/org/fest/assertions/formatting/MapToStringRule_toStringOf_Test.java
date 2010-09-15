/*
 * Created on Sep 10, 2010
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

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.*;

/**
 * Tests for <code>{@link MapToStringRule#toStringOf(Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class MapToStringRule_toStringOf_Test {

  private static MapToStringRule rule;

  @BeforeClass public static void setUpOnce() {
    rule = new MapToStringRule();
  }

  @Test public void should_return_formatted_Map() {
    Map<String, String> map = new LinkedHashMap<String, String>();
    map.put("name", "Luke");
    map.put("role", "Jedi");
    String s = rule.toStringOf(map);
    assertEquals("{'name'='Luke', 'role'='Jedi'}", s);
  }
}
