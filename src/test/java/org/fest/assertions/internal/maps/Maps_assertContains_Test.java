/*
 * Created on Dec 21, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.maps;

import static org.fest.assertions.data.MapEntry.entry;
import static org.fest.assertions.error.ShouldContain.shouldContain;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.Sets.newLinkedHashSet;

import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.data.MapEntry;
import org.fest.assertions.internal.Maps;
import org.fest.assertions.internal.MapsBaseTest;

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

  @Test
  public void should_throw_error_if_array_of_entries_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(entriesToLookForIsEmpty());
    maps.assertContains(someInfo(), actual, new MapEntry[0]);
  }

  @Test
  public void should_throw_error_if_array_of_entries_to_look_for_is_null() {
    thrown.expectNullPointerException(entriesToLookForIsNull());
    maps.assertContains(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_entry_is_null() {
    MapEntry[] entries = { null };
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
    MapEntry[] expected = { entry("name", "Yoda"), entry("job", "Jedi") };
    try {
      maps.assertContains(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet(entry("job", "Jedi"))));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
