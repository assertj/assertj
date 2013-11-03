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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.ErrorMessages.entriesToLookForIsEmpty;
import static org.assertj.core.test.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
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
  public void initLinkedHashMap() throws Exception {
    linkedActual = new LinkedHashMap<String, String>(2);
    linkedActual.put("name", "Yoda");
    linkedActual.put("color", "green");
  }

  @Test
  public void should_fail_if_actual_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());
    maps.assertContainsExactly(someInfo(), null, entry("name", "Yoda"));
  }

  @Test
  public void should_fail_if_given_entries_array_is_null() throws Exception {
    thrown.expectNullPointerException(entriesToLookForIsNull());
    maps.assertContainsExactly(someInfo(), linkedActual, null);
  }

  @Test
  public void should_fail_if_given_entries_array_is_empty() throws Exception {
    thrown.expectIllegalArgumentException(entriesToLookForIsEmpty());
    maps.assertContainsExactly(someInfo(), linkedActual, emptyEntries());
  }

  @Test
  public void should_pass_if_actual_and_entries_are_empty() throws Exception {
    maps.assertContainsExactly(someInfo(), emptyMap(), emptyEntries());
  }

  @Test
  public void should_pass_if_actual_contains_given_entries_in_order() throws Exception {
    maps.assertContainsExactly(someInfo(), linkedActual, entry("name", "Yoda"), entry("color", "green"));
  }

  @Test
  public void should_fail_if_actual_contains_given_entries_in_disorder() throws Exception {
    AssertionInfo info = someInfo();
    try {
      maps.assertContainsExactly(info, linkedActual, entry("color", "green"), entry("name", "Yoda"));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactly(entry("name", "Yoda"), entry("color", "green"), 0));
      return;
    }
    failBecauseExceptionWasNotThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_and_expected_entries_have_different_size() throws Exception {
    AssertionInfo info = someInfo();
    MapEntry[] expected = { entry("name", "Yoda") };
    try {
      maps.assertContainsExactly(info, linkedActual, expected);
    } catch (AssertionError e) {
      assertThat(e).hasMessage(shouldHaveSameSizeAs(linkedActual, linkedActual.size(), expected.length).create());
      return;
    }
    failBecauseExceptionWasNotThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_every_expected_entries_and_contains_unexpected_one()
      throws Exception {
    AssertionInfo info = someInfo();
    MapEntry[] expected = { entry("name", "Yoda"), entry("color", "green") };
    Map<?, ?> underTest = newLinkedHashMap(entry("name", "Yoda"), entry("job", "Jedi"));
    try {
      maps.assertContainsExactly(info, underTest, expected);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainExactly(underTest, expected, newHashSet(entry("color", "green")),
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
      maps.assertContainsExactly(info, actual, expectedEntries);
    } catch (AssertionError e) {
      verify(failures).failure(
          info,
          shouldContainExactly(actual, expectedEntries, newHashSet(entry("color", "yellow")),
              newHashSet(entry("color", "green"))));
      return;
    }
    failBecauseExceptionWasNotThrown(AssertionError.class);
  }

  private static Map<String, String> newLinkedHashMap(MapEntry... entries) {
    LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
    for (MapEntry entry : entries) {
      result.put((String) entry.key, (String) entry.value);
    }
    return result;
  }

  private static Set<MapEntry> newHashSet(MapEntry entry) {
    LinkedHashSet<MapEntry> result = new LinkedHashSet<MapEntry>();
    result.add(entry);
    return result;
  }
}
