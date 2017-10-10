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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link BasicErrorMessageFactory#create(Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Yvonne Wang
 */
public class BasicErrorMessageFactory_create_Test {

  private MessageFormatter formatter;
  private BasicErrorMessageFactory factory;

  @Before
  public void setUp() {
    formatter = mock(MessageFormatter.class);
    factory = new BasicErrorMessageFactory("Hello %s", "Yoda");
    factory.formatter = formatter;
  }

  @Test
  public void should_implement_toString() {
    Description description = new TestDescription("Test");
    Representation representation = new StandardRepresentation();
    String formattedMessage = "[Test] Hello Yoda";
    when(formatter.format(description, representation, "Hello %s", "Yoda")).thenReturn(formattedMessage);
    assertThat(factory.create(description, representation)).isEqualTo(formattedMessage);
  }

  @Test
  public void should_create_error_with_configured_representation() {
    Description description = new TestDescription("Test");
    String formattedMessage = "[Test] Hello Yoda";
    when(formatter.format(eq(description), same(CONFIGURATION_PROVIDER.representation()), eq("Hello %s"), eq("Yoda")))
      .thenReturn(formattedMessage);
    assertThat(factory.create(description)).isEqualTo(formattedMessage);
  }

  @Test
  public void should_create_error_with_empty_description_and_configured_representation() {
    Description description = emptyDescription();
    String formattedMessage = "[] Hello Yoda";
    when(formatter.format(eq(description), same(CONFIGURATION_PROVIDER.representation()), eq("Hello %s"), eq("Yoda")))
      .thenReturn(formattedMessage);
    assertThat(factory.create()).isEqualTo(formattedMessage);
  }
}
