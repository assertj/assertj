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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.util.Lists.newArrayList;

import java.io.File;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.HexadecimalRepresentation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHaveSize#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ShouldHaveSize_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  public void setUp() {
    factory = shouldHaveSize(newArrayList('a', 'b'), 4, 2);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpected size:<2> but was:<4> in:%n<['a', 'b']>"));
  }

  @Test
  public void should_create_error_message_with_hexadecimal_representation() {
    String message = factory.create(new TextDescription("Test"), new HexadecimalRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpected size:<2> but was:<4> in:%n<['0x0061', '0x0062']>"));
  }

  @Test
  public void should_create_error_message_for_file_not_has_size() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveSize(new FakeFile("abc"), 3L);
    String expectedErrorMessage = format("[Test] %n"
                                         + "Expecting file%n"
                                         + "  <abc>%n"
                                         + "to have size of:%n"
                                         + "  3L bytes%n"
                                         + "but had:%n"
                                         + "  0L bytes");
    //WHEN
    String actualErrorMessage = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
  }
}
