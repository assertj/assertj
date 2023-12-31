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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnlyValues.shouldContainOnlyValues;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldContainOnlyValues#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 *
 * @author Ilya Koshaleu
 */
class ShouldContainOnlyValues_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnlyValues(mapOf(entry("name", "Yoda"), entry("color", "green")),
                                                          list("jedi", "green"),
                                                          singleton("jedi"),
                                                          singleton("Yoda"));
    // WHEN
    String message = factory.create(new TestDescription("Test"), StandardRepresentation.STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(String.format("[Test] %n"
                                          + "Expecting actual:%n"
                                          + "  {\"color\"=\"green\", \"name\"=\"Yoda\"}%n"
                                          + "to contain only following values:%n"
                                          + "  [\"jedi\", \"green\"]%n"
                                          + "values not found:%n"
                                          + "  [\"jedi\"]%n"
                                          + "and values not expected:%n"
                                          + "  [\"Yoda\"]%n"));
  }

  @Test
  void should_create_error_message_without_unexpected_elements() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnlyValues(singletonMap("color", "green"),
                                                          list("jedi", "green"),
                                                          singleton("jedi"),
                                                          emptySet());
    // WHEN
    String message = factory.create(new TestDescription("Test"), StandardRepresentation.STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(String.format("[Test] %n"
                                          + "Expecting actual:%n"
                                          + "  {\"color\"=\"green\"}%n"
                                          + "to contain only following values:%n"
                                          + "  [\"jedi\", \"green\"]%n"
                                          + "but could not find the following values:%n"
                                          + "  [\"jedi\"]%n"));
  }
}
