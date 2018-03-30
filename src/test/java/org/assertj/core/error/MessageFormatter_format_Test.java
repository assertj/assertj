/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static com.tngtech.junit.dataprovider.DataProviders.$;
import static com.tngtech.junit.dataprovider.DataProviders.$$;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.test.ExpectedException.none;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * Tests for <code>{@link MessageFormatter#format(Description, String, Object...)}</code>.
 * 
 * @author Alex Ruiz
 */
@RunWith(DataProviderRunner.class)
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
    String s = messageFormatter.format(description, STANDARD_REPRESENTATION, "Hello %s", "World");
    assertThat(s).isEqualTo("[Test] Hello \"World\"");
    verify(descriptionFormatter).format(description);
  }

  @Test
  @UseDataProvider("messages")
  public void should_format_message_and_correctly_escape_percentage(String input, String formatted) {
    // GIVEN
    Description description = new TextDescription("Test");
    // WHEN
    String finalMessage = messageFormatter.format(description, STANDARD_REPRESENTATION, input);
    // THEN
    assertThat(finalMessage).isEqualTo("[Test] " + formatted);
  }

  @DataProvider
  public static Object[][] messages() {
    return $$($("%E", "%E"),
              $("%%E", "%%E"),
              $("%%%E", "%%%E"),
              $("%n", format("%n")),
              $("%%%n%E", "%%" + format("%n") + "%E"),
              $("%%n", "%" + format("%n")));
  }
}
