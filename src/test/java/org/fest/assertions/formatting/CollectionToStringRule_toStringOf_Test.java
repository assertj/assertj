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

import static org.fest.util.Collections.list;
import static org.junit.Assert.assertEquals;

import org.junit.*;

/**
 * Tests for <code>{@link CollectionToStringRule#toStringOf(Object)}</code>.
 * @author Alex Ruiz
 */
public class CollectionToStringRule_toStringOf_Test {

  private static CollectionToStringRule rule;

  @BeforeClass public static void setUpOnce() {
    rule = new CollectionToStringRule();
  }

  @Test public void should_return_formatted_Collection() {
    String s = rule.toStringOf(list("Luke", "Yoda"));
    assertEquals("['Luke', 'Yoda']", s);
  }
}
