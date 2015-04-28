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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.*;

import java.util.List;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for <code>{@link ShouldBeEqual#newAssertionError(Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 */
@RunWith(Parameterized.class)
public class ShouldBeEqual_newAssertionError_Test {

  private final String formattedDescription;

  @Parameters
  public static List<Object[]> parameters() {
    return newArrayList(new Object[][] { { "[Jedi]" }, { "[Jedi]  " } });
  }

  public ShouldBeEqual_newAssertionError_Test(String formattedDescription) {
    this.formattedDescription = formattedDescription;
  }

  private Description description;
  private ShouldBeEqual factory;
  private DescriptionFormatter formatter;

  @Before
  public void setUp() {
    description = new TestDescription("Jedi");
    factory = (ShouldBeEqual) shouldBeEqual("Luke", "Yoda", new StandardRepresentation());
    factory.descriptionFormatter = mock(DescriptionFormatter.class);
    formatter = factory.descriptionFormatter;
  }

  @Test
  public void should_create_ComparisonFailure_if_JUnit4_is_present_and_trim_spaces_in_formatted_description() {
    when(formatter.format(description)).thenReturn(formattedDescription);
    AssertionError error = factory.newAssertionError(description, new StandardRepresentation());
    assertThat(error.getClass()).isEqualTo(ComparisonFailure.class);
    assertThat(error.getMessage()).isEqualTo("[Jedi] expected:<\"[Yoda]\"> but was:<\"[Luke]\">");
  }
}
