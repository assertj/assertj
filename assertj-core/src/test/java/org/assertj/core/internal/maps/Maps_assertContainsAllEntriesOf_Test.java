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
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainEntries.shouldContainEntries;
import static org.assertj.core.internal.ErrorMessages.mapOfEntriesToLookForIsNull;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.set;

import java.util.Map;
import java.util.Map.Entry;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.Test;

class Maps_assertContainsAllEntriesOf_Test extends MapsBaseTest {

  @Test
  void should_pass_if_actual_contains_given_map_entries() {
    maps.assertContainsAllEntriesOf(info, actual, mapOf(entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_given_map_entries_in_different_order() {
    maps.assertContainsAllEntriesOf(info, actual, mapOf(entry("color", "green"), entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_all_given_map_entries() {
    maps.assertContainsAllEntriesOf(info, actual, mapOf(entry("name", "Yoda"), entry("color", "green")));
  }

  @Test
  void should_pass_if_actual_and_given_map_are_empty() {
    actual = emptyMap();
    maps.assertContainsAllEntriesOf(info, actual, mapOf());
  }

  @Test
  void should_pass_if_actual_is_not_empty_and_given_map_is_empty() {
    maps.assertContainsAllEntriesOf(info, actual, emptyMap());
  }

  @Test
  void should_throw_error_if_map_of_entries_to_look_for_is_null() {
    // GIVEN
    Map<String, String> other = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> maps.assertContainsAllEntriesOf(info, actual, other),
                                                    NullPointerException.class);
    // THEN
    then(npe).hasMessage(mapOfEntriesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Map<String, String> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsAllEntriesOf(info, actual,
                                                                                               mapOf(entry("name", "Yoda"))));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_map_entries() {
    // GIVEN
    Map<String, String> expected = mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsAllEntriesOf(info, actual, expected));
    // THEN
    then(assertionError).hasMessage(shouldContainEntries(actual, asEntriesArray(expected), emptySet(), set(entry("job", "Jedi")),
                                                         info.representation()).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_entries_because_values_differ_for_the_same_key() {
    // GIVEN
    Map<String, String> expected = mapOf(entry("name", "Yoda"), entry("color", "red"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsAllEntriesOf(info, actual, expected));
    // THEN
    then(assertionError).hasMessage(shouldContainEntries(actual, asEntriesArray(expected), set(entry("color", "red")), emptySet(),
                                                         info.representation()).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_entries_because_of_some_missing_keys_and_some_values_difference() {
    // GIVEN
    Map<String, String> expected = mapOf(entry("name", "Yoda"), entry("color", "red"), entry("job", "Jedi"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsAllEntriesOf(info, actual, expected));
    // THEN
    then(assertionError).hasMessage(shouldContainEntries(actual, asEntriesArray(expected), set(entry("color", "red")),
                                                         set(entry("job", "Jedi")), info.representation()).create());
  }

  @SuppressWarnings("unchecked")
  private static Entry<String, String>[] asEntriesArray(Map<String, String> expected) {
    return expected.entrySet().toArray(new Entry[0]);
  }

}
