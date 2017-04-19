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

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
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

  @SuppressWarnings("unchecked")
  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertContainsOnly(someInfo(), null, entry("name", "Yoda"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_fail_if_given_entries_array_is_null() {
    thrown.expectNullPointerException(entriesToLookForIsNull());
    maps.assertContainsOnly(someInfo(), actual, (MapEntry[])null);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_fail_if_given_entries_array_is_empty() {
    thrown.expectIllegalArgumentException(entriesToLookForIsEmpty());
    maps.assertContainsOnly(someInfo(), actual, emptyEntries());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_if_actual_and_entries_are_empty() {
    maps.assertContainsOnly(someInfo(), emptyMap(), emptyEntries());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_if_actual_contains_only_expected_entries() {
    maps.assertContainsOnly(someInfo(), actual, entry("name", "Yoda"), entry("color", "green"));
  }

  @Test
  public void should_fail_if_actual_contains_unexpected_entry() {
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"));
    try {
      maps.assertContainsOnly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainOnly(actual, expected, emptySet(), newHashSet(entry("color", "green"))));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_every_expected_entries() {
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    Map<String, String> underTest = Maps.mapOf(entry("name", "Yoda"));
    try {
      maps.assertContainsOnly(info, underTest, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainOnly(underTest, expected, newHashSet(entry("color", "green")), emptySet()));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_every_expected_entries_and_contains_unexpected_one()
       {
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    Map<String, String> underTest = Maps.mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
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
    shouldHaveThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_contains_entry_key_with_different_value() {

    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expectedEntries = array(entry("name", "Yoda"), entry("color", "yellow"));
    try {
      maps.assertContainsOnly(info, actual, expectedEntries);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainOnly(actual, expectedEntries, newHashSet(entry("color", "yellow")),
              newHashSet(entry("color", "green"))));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  private static <K, V> HashSet<MapEntry<K, V>> newHashSet(MapEntry<K, V> entry) {
    HashSet<MapEntry<K, V>> notExpected = new HashSet<>();
    notExpected.add(entry);
    return notExpected;
  }

}
