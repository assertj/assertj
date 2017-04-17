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
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.internal.ErrorMessages.entryToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;


/**
 * Tests for <code>{@link Maps#assertContains(AssertionInfo, Map, MapEntry[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Maps_assertContains_Test extends MapsBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_entries() {
    maps.assertContains(someInfo(), actual, array(entry("name", "Yoda")));
  }

  @Test
  public void should_pass_if_actual_contains_given_entries_in_different_order() {
    maps.assertContains(someInfo(), actual, array(entry("color", "green"), entry("name", "Yoda")));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_entries() {
    maps.assertContains(someInfo(), actual, array(entry("name", "Yoda"), entry("color", "green")));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_if_actual_and_given_entries_are_empty() {
    actual = new HashMap<>();
    maps.assertContains(someInfo(), actual, new MapEntry[0]);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_throw_error_if_array_of_entries_to_look_for_is_empty() {
    thrown.expectAssertionError();
    maps.assertContains(someInfo(), actual, new MapEntry[0]);
  }

  @Test
  public void should_throw_error_if_array_of_entries_to_look_for_is_null() {
    thrown.expectNullPointerException(entriesToLookForIsNull());
    maps.assertContains(someInfo(), actual, null);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_throw_error_if_entry_is_null() {
    MapEntry<String, String>[] entries = new MapEntry[]{null};
    thrown.expectNullPointerException(entryToLookForIsNull());
    maps.assertContains(someInfo(), actual, entries);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertContains(someInfo(), null, array(entry("name", "Yoda")));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_entries() {
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("job", "Jedi"));
    try {
      maps.assertContains(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet(entry("job", "Jedi"))));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
