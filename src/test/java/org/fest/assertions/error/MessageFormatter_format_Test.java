/*
 * Created on Jan 11, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.error;

import static org.fest.assertions.test.ExpectedException.none;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link MessageFormatter#format(String, Description, Object...)}</code>.
 *
 * @author Alex Ruiz
 */
public class MessageFormatter_format_Test {

  @Rule public ExpectedException thrown = none();

  private DescriptionFormatter descriptionFormatter;
  private MessageFormatter messageFormatter;

  @Before public void setUp() {
    descriptionFormatter = spy(new DescriptionFormatter());
    messageFormatter = new MessageFormatter();
    messageFormatter.descriptionFormatter = descriptionFormatter;
  }

  @Test public void should_throw_error_if_format_string_is_null() {
    thrown.expectNullPointerException("The format string should not be null");
    messageFormatter.format(null, null);
  }

  @Test public void should_throw_error_if_args_array_is_null() {
    thrown.expectNullPointerException("The array of arguments should not be null");
    Object[] args = null;
    messageFormatter.format("", null, args);
  }

  @Test public void should_format_message() {
    Description description = new TextDescription("Test");
    String s = messageFormatter.format("Hello %s", description, "World");
    assertEquals("[Test] Hello 'World'", s);
    verify(descriptionFormatter).format(description);
  }
}
