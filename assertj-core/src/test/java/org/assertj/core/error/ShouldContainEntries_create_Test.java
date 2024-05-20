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
import static java.util.Collections.emptySet;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainEntries.shouldContainEntries;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.set;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

class ShouldContainEntries_create_Test {

  @Test
  void should_create_error_message_when_all_keys_are_found_but_values_differ() {
    // GIVEN
    Map<?, ?> map = mapOf(entry("name", "yoda"), entry("color", "green"));
    Entry<?, ?>[] expectedEntries = array(entry("name", "vador"), entry("color", "red"));
    Set<Entry<?, ?>> keysWithWrongValues = set(entry("name", "vador"), entry("color", "red"));
    Set<Entry<?, ?>> keysNotFound = emptySet();
    ErrorMessageFactory factory = shouldContainEntries(map, expectedEntries, keysWithWrongValues, keysNotFound,
                                                       STANDARD_REPRESENTATION);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting map:%n" +
                                   "  {\"color\"=\"green\", \"name\"=\"yoda\"}%n" +
                                   "to contain entries:%n" +
                                   "  [\"name\"=\"vador\", \"color\"=\"red\"]%n" +
                                   "but the following map entries had different values:%n" +
                                   "  [\"name\"=\"yoda\" (expected: \"vador\"), \"color\"=\"green\" (expected: \"red\")]"));
  }

  @Test
  void should_create_error_message_when_keys_and_values_differ() {
    // GIVEN
    Map<?, ?> map = mapOf(entry("name", "yoda"), entry("color", "green"));
    Entry<?, ?>[] expectedEntries = array(entry("NAME", "vador"), entry("COLOR", "red"));
    Set<Entry<?, ?>> keysWithWrongValues = emptySet();
    Set<Entry<?, ?>> keysNotFound = set(entry("NAME", "vador"), entry("COLOR", "red"));
    ErrorMessageFactory factory = shouldContainEntries(map, expectedEntries, keysWithWrongValues, keysNotFound,
                                                       STANDARD_REPRESENTATION);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting map:%n" +
                                   "  {\"color\"=\"green\", \"name\"=\"yoda\"}%n" +
                                   "to contain entries:%n" +
                                   "  [\"NAME\"=\"vador\", \"COLOR\"=\"red\"]%n" +
                                   "but could not find the following map entries:%n" +
                                   "  [\"NAME\"=\"vador\", \"COLOR\"=\"red\"]"));
  }

  @Test
  void should_create_error_message_when_no_keys_are_found() {
    // GIVEN
    Map<?, ?> map = mapOf(entry("name", "yoda"), entry("color", "green"));
    Entry<?, ?>[] expectedEntries = array(entry("NAME", "yoda"), entry("COLOR", "green"));
    Set<Entry<?, ?>> keysWithWrongValues = emptySet();
    Set<Entry<?, ?>> keysNotFound = set(entry("NAME", "yoda"), entry("COLOR", "green"));
    ErrorMessageFactory factory = shouldContainEntries(map, expectedEntries, keysWithWrongValues, keysNotFound,
                                                       STANDARD_REPRESENTATION);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting map:%n" +
                                   "  {\"color\"=\"green\", \"name\"=\"yoda\"}%n" +
                                   "to contain entries:%n" +
                                   "  [\"NAME\"=\"yoda\", \"COLOR\"=\"green\"]%n" +
                                   "but could not find the following map entries:%n" +
                                   "  [\"NAME\"=\"yoda\", \"COLOR\"=\"green\"]"));
  }

  @Test
  void should_create_error_message_when_some_keys_are_found_but_values_differ() {
    // GIVEN
    Map<?, ?> map = mapOf(entry("name", "yoda"), entry("color", "%d"), entry("power", "99%"));
    Entry<?, ?>[] expectedEntries = array(entry("NAME", "yoda"), entry("color", "red"), entry("power", "%s"));
    Set<Entry<?, ?>> keysWithWrongValues = set(entry("color", "red"), entry("power", "%s"));
    Set<Entry<?, ?>> keysNotFound = set(entry("NAME", "yoda"));
    ErrorMessageFactory factory = shouldContainEntries(map, expectedEntries, keysWithWrongValues, keysNotFound,
                                                       STANDARD_REPRESENTATION);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting map:%n" +
                                   "  {\"color\"=\"%%d\", \"name\"=\"yoda\", \"power\"=\"99%%\"}%n" +
                                   "to contain entries:%n" +
                                   "  [\"NAME\"=\"yoda\", \"color\"=\"red\", \"power\"=\"%%s\"]%n" +
                                   "but could not find the following map entries:%n" +
                                   "  [\"NAME\"=\"yoda\"]%n" +
                                   "and the following map entries had different values:%n" +
                                   "  [\"color\"=\"%%d\" (expected: \"red\"), \"power\"=\"99%%\" (expected: \"%%s\")]"));
  }
}
