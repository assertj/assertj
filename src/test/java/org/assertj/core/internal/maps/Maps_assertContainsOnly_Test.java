/*
 * Created on Nov 2, 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2013 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.test.ErrorMessages.entriesToLookForIsEmpty;
import static org.assertj.core.test.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.Maps;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Maps#assertContainsOnly(org.assertj.core.api.AssertionInfo, java.util.Map, org.assertj.core.data.MapEntry...)}</code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
public class Maps_assertContainsOnly_Test extends MapsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());
    maps.assertContainsOnly(someInfo(), null, entry("name", "Yoda"));
  }

  @Test
  public void should_fail_if_given_entries_array_is_null() throws Exception {
    thrown.expectNullPointerException(entriesToLookForIsNull());
    maps.assertContainsOnly(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_given_entries_array_is_empty() throws Exception {
    thrown.expectIllegalArgumentException(entriesToLookForIsEmpty());
    maps.assertContainsOnly(someInfo(), actual, emptyEntries());
  }

  @Test
  public void should_pass_if_actual_and_entries_are_empty() throws Exception {
    maps.assertContainsOnly(someInfo(), emptyMap(), emptyEntries());
  }

  @Test
  public void should_pass_if_actual_contains_only_expected_entries() throws Exception {
    maps.assertContainsOnly(someInfo(), actual, entry("name", "Yoda"), entry("color", "green"));
  }

  @Test
  public void should_fail_if_actual_contains_unexpected_entry() throws Exception {
    AssertionInfo info = someInfo();
    MapEntry[] expected = { entry("name", "Yoda") };
    try {
      maps.assertContainsOnly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainOnly(actual, expected, emptySet(), newHashSet(entry("color", "green"))));
      return;
    }
    failBecauseExceptionWasNotThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_every_expected_entries() throws Exception {
    AssertionInfo info = someInfo();
    MapEntry[] expected = { entry("name", "Yoda"), entry("color", "green") };
    Map<?, ?> underTest = Maps.mapOf(entry("name", "Yoda"));
    try {
      maps.assertContainsOnly(info, underTest, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainOnly(underTest, expected, newHashSet(entry("color", "green")), emptySet()));
      return;
    }
    failBecauseExceptionWasNotThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_every_expected_entries_and_contains_unexpected_one()
      throws Exception {
    AssertionInfo info = someInfo();
    MapEntry[] expected = { entry("name", "Yoda"), entry("color", "green") };
    Map<?, ?> underTest = Maps.mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
    try {
      maps.assertContainsOnly(info, underTest, expected);
    } catch (AssertionError e) {
      verify(failures)
          .failure(
              info,
              shouldContainOnly(underTest, expected, newHashSet(entry("color", "green")),
                  newHashSet(entry("job", "Jedi"))));
      return;
    }
    failBecauseExceptionWasNotThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_contains_entry_key_with_different_value() throws Exception {

    AssertionInfo info = someInfo();
    MapEntry[] expectedEntries = { entry("name", "Yoda"), entry("color", "yellow") };
    try {
      maps.assertContainsOnly(info, actual, expectedEntries);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainOnly(actual, expectedEntries, newHashSet(entry("color", "yellow")),
              newHashSet(entry("color", "green"))));
      return;
    }
    failBecauseExceptionWasNotThrown(AssertionError.class);
  }

  private static HashSet<MapEntry> newHashSet(MapEntry entry) {
    HashSet<MapEntry> notExpected = new HashSet<MapEntry>();
    notExpected.add(entry);
    return notExpected;
  }

}
