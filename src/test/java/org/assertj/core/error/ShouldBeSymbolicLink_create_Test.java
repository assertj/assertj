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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.error.ShouldBeSymbolicLink.SHOULD_BE_SYMBOLIC_LINK;
import static org.assertj.core.error.ShouldBeSymbolicLink.shouldBeSymbolicLink;
import static org.mockito.Mockito.mock;

public class ShouldBeSymbolicLink_create_Test
{
  private TestDescription description;
  private StandardRepresentation representation;

  private ErrorMessageFactory factory;
  private String actualMessage;
  private String expectedMessage;

  @Before
  public void setUp()
  {
    description = new TestDescription("Test");
    representation = new StandardRepresentation();
  }

  @Test
  public void should_create_error_message()
  {
    final Path actual = mock(Path.class);

    factory = shouldBeSymbolicLink(actual);
    actualMessage = factory.create(description, representation);

    expectedMessage = String.format("[Test] " + SHOULD_BE_SYMBOLIC_LINK,
        actual);

    assertEquals(expectedMessage, actualMessage);
  }
}
