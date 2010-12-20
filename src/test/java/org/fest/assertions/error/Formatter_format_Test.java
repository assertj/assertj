/*
 * Created on Aug 8, 2010
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
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link Formatter#format(Description)}</code>.
 *
 * @author Alex Ruiz
 */
public class Formatter_format_Test {

  private static Formatter formatter;

  @BeforeClass public static void setUpOnce() {
    formatter = Formatter.instance();
  }

  @Test public void should_return_empty_String_if_description_is_null() {
    assertEquals("", formatter.format(null));
  }

  @Test public void should_return_empty_String_if_description_value_is_null() {
    assertEquals("", formatter.format(new TestDescription(null)));
  }

  @Test public void should_return_empty_String_if_description_value_is_empty() {
    assertEquals("", formatter.format(new TestDescription("")));
  }

  @Test public void should_format_description_if_value_is_not_empty_or_null() {
    assertEquals("[Leia] ", formatter.format(new TestDescription("Leia")));
  }
}
