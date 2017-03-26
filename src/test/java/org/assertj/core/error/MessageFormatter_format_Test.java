/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


import org.assertj.core.description.*;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link MessageFormatter#format(Description, String, Object...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class MessageFormatter_format_Test {

  @Rule
  public ExpectedException thrown = none();

  private DescriptionFormatter descriptionFormatter;
  private MessageFormatter messageFormatter;

  @Before
  public void setUp() {
    descriptionFormatter = spy(new DescriptionFormatter());
    messageFormatter = new MessageFormatter();
    messageFormatter.descriptionFormatter = descriptionFormatter;
  }

  @Test
  public void should_throw_error_if_format_string_is_null() {
    thrown.expectNullPointerException();
    messageFormatter.format(null, null, null);
  }

  @Test
  public void should_throw_error_if_args_array_is_null() {
    thrown.expectNullPointerException();
    Object[] args = null;
    messageFormatter.format(null, null, "", args);
  }

  @Test
  public void should_format_message() {
    Description description = new TextDescription("Test");
    Representation representation = new StandardRepresentation();
    String s = messageFormatter.format(description, representation, "Hello %s", "World");
    assertThat(s).isEqualTo("[Test] Hello \"World\"");
    verify(descriptionFormatter).format(description);
  }
}
