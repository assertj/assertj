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
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

public class NoElementsShouldSatisfy_create_Test {

  @Test
  public void should_create_error_message_any() {
    // GIVEN
    ErrorMessageFactory factory = noElementsShouldSatisfy(newArrayList("Luke", "Yoda"), "Vader");
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting no elements of:%n" +
                                         "  <[\"Luke\", \"Yoda\"]>%n" +
                                         "to satisfy the given assertions requirements but this one did:%n" +
                                         "  <\"Vader\">"));
  }
}
