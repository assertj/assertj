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
import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Maps#assertContainsExactly(org.assertj.core.api.AssertionInfo, java.util.Map, org.assertj.core.data.MapEntry...)}</code>
 * .
 *
 * @author Jean-Christophe Gay
 */
public class Maps_assertContainsExactly_Test extends MapsBaseTest {

  private LinkedHashMap<String, String> linkedActual;

  @Before
  public void initLinkedHashMap() {
    linkedActual = new LinkedHashMap<>(2);
    linkedActual.put("name", "Yoda");
    linkedActual.put("color", "green");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    maps.assertContainsExactly(someInfo(), null, entry("name", "Yoda"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_fail_if_given_entries_array_is_null() {
    thrown.expectNullPointerException(entriesToLookForIsNull());
    maps.assertContainsExactly(someInfo(), linkedActual, (MapEntry[])null);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_fail_if_given_entries_array_is_empty() {
    thrown.expectIllegalArgumentException(entriesToLookForIsEmpty());
    maps.assertContainsExactly(someInfo(), linkedActual, emptyEntries());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_if_actual_and_entries_are_empty() {
    maps.assertContainsExactly(someInfo(), emptyMap(), emptyEntries());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_pass_if_actual_contains_given_entries_in_order() {
    maps.assertContainsExactly(someInfo(), linkedActual, entry("name", "Yoda"), entry("color", "green"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void should_fail_if_actual_contains_given_entries_in_disorder() {
    AssertionInfo info = someInfo();
    try {
      maps.assertContainsExactly(info, linkedActual, entry("color", "green"), entry("name", "Yoda"));
    } catch (AssertionError e) {
      verify(failures).failure(info, elementsDifferAtIndex(entry("name", "Yoda"), entry("color", "green"), 0));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_and_expected_entries_have_different_size() {
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"));

    thrown.expectAssertionError(shouldHaveSameSizeAs(linkedActual, linkedActual.size(), expected.length).create());

    maps.assertContainsExactly(info, linkedActual, expected);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_every_expected_entries_and_contains_unexpected_one()
       {
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    Map<String, String> underTest = newLinkedHashMap(entry("name", "Yoda"), entry("job", "Jedi"));
    try {
      maps.assertContainsExactly(info, underTest, expected);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainExactly(underTest, expected, newHashSet(entry("color", "green")),
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
      maps.assertContainsExactly(info, actual, expectedEntries);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainExactly(actual, expectedEntries, newHashSet(entry("color", "yellow")),
              newHashSet(entry("color", "green"))));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  @SafeVarargs
  private static Map<String, String> newLinkedHashMap(MapEntry<String, String>... entries) {
    LinkedHashMap<String, String> result = new LinkedHashMap<>();
    for (MapEntry<String, String> entry : entries) {
      result.put(entry.key, entry.value);
    }
    return result;
  }

  private static <K, V> Set<MapEntry<K, V>> newHashSet(MapEntry<K, V> entry) {
    LinkedHashSet<MapEntry<K, V>> result = new LinkedHashSet<>();
    result.add(entry);
    return result;
  }
}
