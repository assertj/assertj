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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldContainOnlyNulls.shouldContainOnlyNulls;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldContainOnlyNulls#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * @author Billy Yuan
 */
public class ShouldContainOnlyNulls_create_Test {
  @Test
  public void should_create_error_message_with_unexpected_element() {
    ErrorMessageFactory factory = shouldContainOnlyNulls(newArrayList("person", null),
                                                                                newArrayList("person"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

    assertThat(message).isEqualTo(format("[Test] %n"
                                         + "Expecting:%n"
                                         + "  <[\"person\", null]>%n"
                                         + "to contain only null elements but some elements were not:%n"
                                         + "  <[\"person\"]>"));
  }

  @Test
  public void should_create_error_message_with_no_any_element() {
    ErrorMessageFactory factory = shouldContainOnlyNulls(newArrayList());
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

    assertThat(message).isEqualTo(format("[Test] %n"
                                         + "Expecting:%n"
                                         + "  <[]>%n"
                                         + "to contain only null elements but it was empty"));
  }
}
