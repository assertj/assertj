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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeLowerCase#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 */
public class ShouldBeLowerCase_create_Test {

  @Test
  public void should_create_error_message_for_character() {
    String message = shouldBeLowerCase('A').create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    assertThat(message).isEqualTo(format("[Test] %nExpecting <'A'> to be a lowercase"));
  }

  @Test
  public void should_create_error_message_for_string() {
    String message = shouldBeLowerCase("ABC").create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    assertThat(message).isEqualTo(format("[Test] %nExpecting <\"ABC\"> to be a lowercase"));
  }
}
