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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContainEntries.shouldContainEntries;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.internal.ErrorMessages.entryToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.set;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.Map.Entry;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Maps#assertContains(AssertionInfo, Map, Map.Entry[])}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Maps_assertContains_Test extends MapsBaseTest {

  @Test
  void should_pass_if_actual_contains_given_entries() {
    maps.assertContains(info, actual, array(entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_given_entries_in_different_order() {
    maps.assertContains(info, actual, array(entry("color", "green"), entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_all_given_entries() {
    maps.assertContains(info, actual, array(entry("name", "Yoda"), entry("color", "green")));
  }

  @Test
  void should_pass_if_actual_and_given_entries_are_empty() {
    maps.assertContains(info, emptyMap(), emptyEntries());
  }

  @Test
  void should_fail_if_array_of_entries_to_look_for_is_empty_and_the_map_under_test_is_not() {
    // GIVEN
    Entry<String, String>[] expected = emptyEntries();
    // WHEN
    expectAssertionError(() -> maps.assertContains(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldBeEmpty(actual));
  }

  @Test
  void should_throw_error_if_array_of_entries_to_look_for_is_null() {
    // GIVEN
    Entry<String, String>[] entries = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> maps.assertContains(info, actual, entries))
                                    .withMessage(entriesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_entry_is_null() {
    // GIVEN
    Entry<String, String> nullEntry = null;
    Entry<String, String>[] entries = array(nullEntry);
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> maps.assertContains(info, actual, entries))
                                    .withMessage(entryToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    Entry<String, String>[] expected = array(entry("name", "Yoda"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContains(info, actual, expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_empty_and_expected_entries_is_not() {
    // GIVEN
    Entry<String, String>[] expected = array(entry("a", "1"));
    // WHEN
    expectAssertionError(() -> maps.assertContains(info, emptyMap(), expected));
    // THEN
    verify(failures).failure(info,
                             shouldContainEntries(emptyMap(), expected, emptySet(), set(entry("a", "1")), info.representation()));
  }

  @Test
  void should_fail_if_actual_does_not_contain_entries() {
    // GIVEN
    Entry<String, String>[] expected = array(entry("name", "Yoda"), entry("job", "Jedi"));
    // WHEN
    expectAssertionError(() -> maps.assertContains(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainEntries(actual, expected, emptySet(), set(entry("job", "Jedi")),
                                                        info.representation()));
  }

  @Test
  void should_fail_if_actual_does_not_contain_entries_because_values_differ_for_the_same_key() {
    // GIVEN
    Entry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "red"));
    // WHEN
    expectAssertionError(() -> maps.assertContains(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainEntries(actual, expected, set(entry("color", "red")), emptySet(),
                                                        info.representation()));
  }

  @Test
  void should_fail_if_actual_does_not_contain_entries_because_of_some_missing_keys_and_some_values_difference() {
    // GIVEN
    Entry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "red"), entry("job", "Jedi"));
    // WHEN
    expectAssertionError(() -> maps.assertContains(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainEntries(actual, expected, set(entry("color", "red")), set(entry("job", "Jedi")),
                                                        info.representation()));
  }
}
