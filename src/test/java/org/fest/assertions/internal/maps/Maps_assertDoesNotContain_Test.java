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
import static org.fest.assertions.error.ShouldNotContain.shouldNotContain;
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
 * Tests for <code>{@link Maps#assertDoesNotContain(AssertionInfo, Map, MapEntry[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Maps_assertDoesNotContain_Test extends MapsBaseTest {

  @Test
  public void should_pass_if_actual_does_not_contain_given_values() {
    maps.assertDoesNotContain(someInfo(), actual, array(entry("job", "Jedi")));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(entriesToLookForIsEmpty());
    maps.assertDoesNotContain(someInfo(), actual, new MapEntry[0]);
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(entriesToLookForIsNull());
    maps.assertDoesNotContain(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertDoesNotContain(someInfo(), null, array(entry("job", "Jedi")));
  }

  @Test
  public void should_fail_if_actual_contains_given_values() {
    AssertionInfo info = someInfo();
    MapEntry[] expected = { entry("name", "Yoda"), entry("job", "Jedi") };
    try {
      maps.assertDoesNotContain(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContain(actual, expected, newLinkedHashSet(entry("name", "Yoda"))));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
