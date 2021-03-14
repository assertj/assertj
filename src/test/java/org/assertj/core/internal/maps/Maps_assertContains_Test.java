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

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.internal.ErrorMessages.entryToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
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
    maps.assertContains(someInfo(), actual, array(entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_given_entries_in_different_order() {
    maps.assertContains(someInfo(), actual, array(entry("color", "green"), entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_all_given_entries() {
    maps.assertContains(someInfo(), actual, array(entry("name", "Yoda"), entry("color", "green")));
  }

  @SuppressWarnings("unchecked")
  @Test
  void should_pass_if_actual_and_given_entries_are_empty() {
    actual = new HashMap<>();
    maps.assertContains(someInfo(), actual, emptyEntries());
  }

  @Test
  void should_fail_if_array_of_entries_to_look_for_is_empty_and_the_map_under_test_is_not() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = emptyEntries();
    // WHEN
    expectAssertionError(() -> maps.assertContains(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldBeEmpty(actual));
  }

  @Test
  void should_throw_error_if_array_of_entries_to_look_for_is_null() {
    // GIVEN
    MapEntry<String, String>[] entries = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> maps.assertContains(someInfo(), actual, entries))
                                    .withMessage(entriesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_entry_is_null() {
    // GIVEN
    MapEntry<String, String> nullEntry = null;
    MapEntry<String, String>[] entries = array(nullEntry);
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> maps.assertContains(someInfo(), actual, entries))
                                    .withMessage(entryToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContains(someInfo(), actual, expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_entries() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("job", "Jedi"));
    // WHEN
    expectAssertionError(() -> maps.assertContains(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet(entry("job", "Jedi"))));
  }
}
