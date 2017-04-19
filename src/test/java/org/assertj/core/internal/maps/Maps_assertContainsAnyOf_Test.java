/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.internal.ErrorMessages.entryToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.HashMap;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.Test;

public class Maps_assertContainsAnyOf_Test extends MapsBaseTest {

  @Test
  public void should_pass_if_actual_contains_one_of_the_given_entries_but_not_the_others() {
    maps.assertContainsAnyOf(someInfo(), actual, array(entry("name", "Yoda"), entry("name", "Vador")));
  }

  @Test
  public void should_pass_if_actual_contains_one_of_the_given_entries() {
    maps.assertContainsAnyOf(someInfo(), actual, array(entry("name", "Yoda")));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_entries() {
    maps.assertContainsAnyOf(someInfo(), actual, array(entry("name", "Yoda"), entry("color", "green")));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_if_actual_and_given_entries_are_empty() {
    actual = new HashMap<>();
    maps.assertContainsAnyOf(someInfo(), actual, new MapEntry[0]);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_throw_error_if_array_of_entries_to_look_for_is_empty_and_the_map_under_test_is_not() {
    thrown.expectAssertionError();
    maps.assertContainsAnyOf(someInfo(), actual, new MapEntry[0]);
  }

  @Test
  public void should_throw_error_if_array_of_entries_to_look_for_is_null() {
    thrown.expectNullPointerException(entriesToLookForIsNull());
    maps.assertContainsAnyOf(someInfo(), actual, null);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_throw_error_if_entry_is_null() {
    MapEntry<String, String>[] entries = new MapEntry[] { null };
    thrown.expectNullPointerException(entryToLookForIsNull());
    maps.assertContainsAnyOf(someInfo(), actual, entries);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertContainsAnyOf(someInfo(), null, array(entry("name", "Yoda")));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_any_of_the_given_entries() {
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Vador"), entry("job", "Jedi"));
    try {
      maps.assertContainsAnyOf(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainAnyOf(actual, expected));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
