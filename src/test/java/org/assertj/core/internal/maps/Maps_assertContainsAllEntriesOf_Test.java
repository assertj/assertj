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

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Maps.newHashMapOfEntries;
import static org.mockito.Mockito.verify;


/**
 * Tests for <code>{@link Maps#assertContains(AssertionInfo, Map, Map.Entry[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Maps_assertContainsAllEntriesOf_Test extends MapsBaseTest {

  @Test
  void should_pass_if_actual_contains_given_map_entries() {
    maps.assertContainsAllEntriesOf(someInfo(), actual, newHashMapOfEntries(entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_given_map_entries_in_different_order() {
    maps.assertContainsAllEntriesOf(someInfo(), actual, newHashMapOfEntries(entry("color", "green"), entry("name", "Yoda")));
  }

  @Test
  void should_pass_if_actual_contains_all_given_map_entries() {
    maps.assertContainsAllEntriesOf(someInfo(), actual, newHashMapOfEntries(entry("name", "Yoda"), entry("color", "green")));
  }

  @SuppressWarnings("unchecked")
  @Test
  void should_pass_if_actual_and_given_map_are_empty() {
    actual = new HashMap<>();
    maps.assertContainsAllEntriesOf(someInfo(), actual, newHashMapOfEntries());
  }

  @SuppressWarnings("unchecked")
  @Test
  void should_pass_if_given_map_is_empty() {
    maps.assertContainsAllEntriesOf(someInfo(), actual, newHashMapOfEntries());
  }

  @Test
  void should_throw_error_if_map_of_entries_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> maps.assertContainsAllEntriesOf(someInfo(), actual, null))
                                    .withMessage(mapOfEntriesToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> maps.assertContainsAllEntriesOf(someInfo(), null, newHashMapOfEntries(entry("name", "Yoda"))))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_map_entries() {
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("job", "Jedi"));

    Throwable error = catchThrowable(() -> maps.assertContainsAllEntriesOf(info, actual, newHashMapOfEntries(expected)));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldContain(actual, newHashMapOfEntries(expected).entrySet(), newHashMapOfEntries(entry("job", "Jedi")).entrySet()));
  }
}
