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

import static org.fest.util.Strings.quote;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectToStringRule#toStringOf(Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class ObjectToStringRule_toStringOf_Test {

  private static ObjectToStringRule rule;

  @BeforeClass public static void setUpOnce() {
    rule = new ObjectToStringRule();
  }

  @Test public void should_return_toString() {
    Person person = new Person("Leia");
    String s = rule.toStringOf(person);
    assertEquals("Person[name='Leia']", s);
  }

  @Test public void should_return_null_if_object_is_null() {
    assertNull(rule.toStringOf(null));
  }

  private static class Person {
    private final String name;

    Person(String name) {
      this.name = name;
    }

    @Override public String toString() {
      return String.format("Person[name=%s]", quote(name));
    }
  }
}
