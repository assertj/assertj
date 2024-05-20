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

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldNotContainKeys.shouldNotContainKeys;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Map;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.error.ShouldNotContainKeys#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author dorzey
 */
class ShouldNotContainKeys_create_Test {

  private Map<?, ?> map;

  @BeforeEach
  public void setUp() {
    map = mapOf(entry("name", "Yoda"), entry("color", "green"));
  }

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContainKeys(map, newLinkedHashSet("age"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  {\"color\"=\"green\", \"name\"=\"Yoda\"}%n" +
                                   "not to contain key:%n" +
                                   "  \"age\""));
  }

  @Test
  void should_create_error_message_with_multiple_keys() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContainKeys(map, newLinkedHashSet("name", "color"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  {\"color\"=\"green\", \"name\"=\"Yoda\"}%n" +
                                   "not to contain keys:%n" +
                                   "  [\"name\", \"color\"]"));
  }

}
