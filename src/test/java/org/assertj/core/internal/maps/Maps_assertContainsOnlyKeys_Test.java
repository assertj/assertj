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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.test.ErrorMessages.keysToLookForIsEmpty;
import static org.assertj.core.test.ErrorMessages.keysToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.Maps;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Maps#assertContainsOnlyKeys(org.assertj.core.api.AssertionInfo, java.util.Map, java.lang.Object...)}</code>
 * .
 * 
 * @author Christopher Arnott
 */
public class Maps_assertContainsOnlyKeys_Test extends MapsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());
    maps.assertContainsOnlyKeys(someInfo(), null, "name");
  }

  @Test
  public void should_fail_if_given_keys_array_is_null() throws Exception {
    thrown.expectNullPointerException(keysToLookForIsNull());
    maps.assertContainsOnlyKeys(someInfo(), actual, (String[]) null);
  }

  @Test
  public void should_fail_if_given_keys_array_is_empty() throws Exception {
    thrown.expectIllegalArgumentException(keysToLookForIsEmpty());
    maps.assertContainsOnlyKeys(someInfo(), actual, emptyKeys());
  }

  @Test
  public void should_pass_if_actual_and_entries_are_empty() throws Exception {
    maps.assertContainsOnlyKeys(someInfo(), emptyMap(), (Object[]) emptyKeys());
  }

  @Test
  public void should_pass_if_actual_contains_only_expected_entries() throws Exception {
    maps.assertContainsOnlyKeys(someInfo(), actual, "color", "name");
  }

  @Test
  public void should_fail_if_actual_contains_unexpected_entry() throws Exception {
    AssertionInfo info = someInfo();
    String[] expectedKeys = { "name" };
    try {
      maps.assertContainsOnlyKeys(info, actual, expectedKeys);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyKeys(actual, expectedKeys, emptySet(), newHashSet("color")));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_every_expected_entries() throws Exception {
    AssertionInfo info = someInfo();
    String[] expectedKeys = { "name", "color" };
    @SuppressWarnings("unchecked")
    Map<String, String> underTest = (Map<String, String>) Maps.mapOf(entry("name", "Yoda"));
    try {
      maps.assertContainsOnlyKeys(info, underTest, expectedKeys);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyKeys(underTest, expectedKeys, newHashSet("color"), emptySet()));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_every_expected_entries_and_contains_unexpected_one()
      throws Exception {
    AssertionInfo info = someInfo();
    String[] expectedKeys = { "name", "color" };
    @SuppressWarnings("unchecked")
    Map<String, String> underTest = (Map<String, String>) Maps.mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
    try {
      maps.assertContainsOnlyKeys(info, underTest, expectedKeys);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyKeys(underTest, expectedKeys, newHashSet("color"), newHashSet("job")));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  private static HashSet<String> newHashSet(String entry) {
    HashSet<String> notExpected = new HashSet<String>();
    notExpected.add(entry);
    return notExpected;
  }
}
