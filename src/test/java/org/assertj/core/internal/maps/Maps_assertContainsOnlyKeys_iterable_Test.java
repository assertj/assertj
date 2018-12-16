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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.internal.ErrorMessages.*;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.assertj.core.util.Lists.list;

import java.util.HashSet;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.Maps;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Maps#assertContainsOnlyKeys(AssertionInfo, Map, Iterable<Object>)}</code>
 * .
 *
 * @author Sebastian Kempken
 */
public class Maps_assertContainsOnlyKeys_iterable_Test extends MapsBaseTest {

  private final String KEYS_ITERABLE = "keys iterable";

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> maps.assertContainsOnlyKeys(someInfo(), null,
      list("name")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_given_keys_iterable_is_null() {
    assertThatNullPointerException().isThrownBy(() -> maps.assertContainsOnlyKeys(someInfo(), actual,
                                                                                  (Iterable<String>) null))
                                    .withMessage(keysToLookForIsNull(KEYS_ITERABLE));
  }

  @Test
  public void should_fail_if_given_keys_iterable_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> maps.assertContainsOnlyKeys(someInfo(), actual,
                                                                                      list()))
                                        .withMessage(keysToLookForIsEmpty(KEYS_ITERABLE));
  }

  @Test
  public void should_pass_if_actual_and_given_keys_are_empty() {
    maps.assertContainsOnlyKeys(someInfo(), emptyMap(), list());
  }

  @Test
  public void should_pass_if_actual_contains_only_expected_keys() {
    maps.assertContainsOnlyKeys(someInfo(), actual, list("color", "name"));
  }

  @Test
  public void should_fail_if_actual_contains_an_unexpected_key() {
    AssertionInfo info = someInfo();
    Iterable<String> expectedKeys = list("name");
    try {
      maps.assertContainsOnlyKeys(info, actual, expectedKeys);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyKeys(actual, convertToArray(expectedKeys), emptySet(),
                                                           newHashSet("color")));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_all_expected_keys() {
    AssertionInfo info = someInfo();
    Iterable<String> expectedKeys = list("name", "color");
    Map<String, String> underTest = mapOf(entry("name", "Yoda"));
    try {
      maps.assertContainsOnlyKeys(info, underTest, expectedKeys);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyKeys(underTest, convertToArray(expectedKeys),
                                                           newHashSet("color"), emptySet()));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  @Test
  public void should_fail_if_actual_does_not_contains_all_expected_keys_and_contains_unexpected_one() {
    AssertionInfo info = someInfo();
    Iterable<String> expectedKeys = list("name", "color");
    Map<String, String> underTest = mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
    try {
      maps.assertContainsOnlyKeys(info, underTest, expectedKeys);
    } catch (AssertionError e) {
      verify(failures).failure(info,
                               shouldContainOnlyKeys(underTest, convertToArray(expectedKeys), newHashSet("color"),
                                                     newHashSet("job")));
      return;
    }
    shouldHaveThrown(AssertionError.class);
  }

  private static HashSet<String> newHashSet(String entry) {
    HashSet<String> notExpected = new HashSet<>();
    notExpected.add(entry);
    return notExpected;
  }

  private static String[] convertToArray(Iterable<String> iterable) {
    return Streams.stream(iterable).toArray(String[]::new);
  }

}
