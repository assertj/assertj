/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldContainOnlyKeys#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Joel Costigliola.
 */
class ShouldContainOnlyKeys_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnlyKeys(mapOf(entry("name", "Yoda"), entry("color", "green")),
                                                        list("jedi", "color"), newLinkedHashSet("jedi"),
                                                        newLinkedHashSet("name"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(String.format("[Test] %n"
                                          + "Expecting actual:%n"
                                          + "  {\"color\"=\"green\", \"name\"=\"Yoda\"}%n"
                                          + "to contain only following keys:%n"
                                          + "  [\"jedi\", \"color\"]%n"
                                          + "keys not found:%n"
                                          + "  [\"jedi\"]%n"
                                          + "and keys not expected:%n"
                                          + "  [\"name\"]%n"));
  }

  @Test
  void should_not_display_unexpected_elements_when_there_are_none() {
    // GIVEN
    ErrorMessageFactory factory = shouldContainOnlyKeys(mapOf(entry("color", "green")),
                                                        list("jedi", "color"), newLinkedHashSet("jedi"),
                                                        emptySet());
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(String.format("[Test] %n"
                                          + "Expecting actual:%n"
                                          + "  {\"color\"=\"green\"}%n"
                                          + "to contain only following keys:%n"
                                          + "  [\"jedi\", \"color\"]%n"
                                          + "but could not find the following keys:%n"
                                          + "  [\"jedi\"]%n"));
  }

}
