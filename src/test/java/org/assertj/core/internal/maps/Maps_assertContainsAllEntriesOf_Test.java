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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.mapOfEntriesToLookForIsNull;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.Test;

class Maps_assertContainsAllEntriesOf_Test extends MapsBaseTest {

  @Test
  void should_pass_if_actual_contains_given_map_entries() {
    maps.assertContainsAllEntriesOf(someInfo(), actual, mapOf(entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_given_map_entries_in_different_order() {
    maps.assertContainsAllEntriesOf(someInfo(), actual, mapOf(entry("color", "green"), entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_all_given_map_entries() {
    maps.assertContainsAllEntriesOf(someInfo(), actual, mapOf(entry("name", "Yoda"), entry("color", "green")));
  }

  @Test
  void should_pass_if_actual_and_given_map_are_empty() {
    actual = emptyMap();
    maps.assertContainsAllEntriesOf(someInfo(), actual, mapOf());
  }

  @Test
  void should_pass_if_actual_is_not_empty_and_given_map_is_empty() {
    maps.assertContainsAllEntriesOf(someInfo(), actual, emptyMap());
  }

  @Test
  void should_throw_error_if_map_of_entries_to_look_for_is_null() {
    // GIVEN
    Map<String, String> other = null;
    // WHEN
    NullPointerException npe = catchThrowableOfType(() -> maps.assertContainsAllEntriesOf(someInfo(), actual, other),
                                                    NullPointerException.class);
    // THEN
    then(npe).isNotNull()
             .hasMessage(mapOfEntriesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Map<String, String> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsAllEntriesOf(someInfo(), actual,
                                                                                               mapOf(entry("name", "Yoda"))));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_map_entries() {
    // GIVEN
    LinkedHashMap<String, String> other = mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsAllEntriesOf(info, actual, other));
    // THEN
    then(assertionError).hasMessage(shouldContain(actual, other.entrySet(), mapOf(entry("job", "Jedi")).entrySet()).create());
  }
}
