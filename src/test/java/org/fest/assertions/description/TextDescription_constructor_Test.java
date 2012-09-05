/*
 * Created on Jul 20, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.description;

import static java.util.UUID.randomUUID;
import static junit.framework.Assert.assertEquals;
import static org.junit.rules.ExpectedException.none;

import org.junit.*;
import org.junit.rules.ExpectedException;

/**
 * Tests for <code>{@link TextDescription#TextDescription(String)}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class TextDescription_constructor_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_set_value() {
    String value = randomText();
    TextDescription description = new TextDescription(value);
    assertEquals(value, description.value);
  }

  private static String randomText() {
    return randomUUID().toString();
  }

  @Test
  public void should_throw_error_if_value_is_null() {
    thrown.expect(NullPointerException.class);
    new TextDescription(null);
  }
}
