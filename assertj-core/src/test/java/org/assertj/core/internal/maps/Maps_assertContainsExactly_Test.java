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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.mockito.Mockito.verify;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Maps#assertContainsExactly(org.assertj.core.api.AssertionInfo, java.util.Map, java.util.Map.Entry...)}</code>
 * .
 *
 * @author Jean-Christophe Gay
 */
class Maps_assertContainsExactly_Test extends MapsBaseTest {

  private LinkedHashMap<String, String> linkedActual;

  @BeforeEach
  void initLinkedHashMap() {
    linkedActual = new LinkedHashMap<>(2);
    linkedActual.put("name", "Yoda");
    linkedActual.put("color", "green");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsExactly(someInfo(), actual, expected));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_entries_array_is_null() {
    // GIVEN
    MapEntry<String, String>[] entries = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> maps.assertContainsExactly(someInfo(), linkedActual, entries))
                                    .withMessage(entriesToLookForIsNull());
  }

  @Test
  void should_fail_if_given_entries_array_is_empty() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = emptyEntries();
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldBeEmpty(actual));
  }

  @Test
  void should_pass_if_actual_and_entries_are_empty() {
    maps.assertContainsExactly(someInfo(), emptyMap(), array());
  }

  @Test
  void should_pass_if_actual_contains_given_entries_in_order() {
    maps.assertContainsExactly(someInfo(), linkedActual, array(entry("name", "Yoda"), entry("color", "green")));
  }

  @Test
  void should_fail_if_actual_contains_given_entries_in_disorder() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(info, linkedActual,
                                                          array(entry("color", "green"), entry("name", "Yoda"))));
    // THEN
    verify(failures).failure(info, elementsDifferAtIndex(entry("name", "Yoda"), entry("color", "green"), 0));
  }

  @Test
  void should_fail_if_actual_and_expected_entries_have_different_size() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"));
    // WHEN
    ThrowingCallable code = () -> maps.assertContainsExactly(info, linkedActual, expected);
    // THEN
    String error = shouldHaveSameSizeAs(linkedActual, expected, linkedActual.size(), expected.length).create();
    assertThatAssertionErrorIsThrownBy(code).withMessage(error);
  }

  @Test
  void should_fail_if_actual_does_not_contains_every_expected_entries_and_contains_unexpected_one() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    Map<String, String> underTest = newLinkedHashMap(entry("name", "Yoda"), entry("job", "Jedi"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(info, underTest, expected));
    // THEN
    verify(failures).failure(info, shouldContainExactly(underTest, list(expected),
                                                        newHashSet(entry("color", "green")),
                                                        newHashSet(entry("job", "Jedi"))));
  }

  @Test
  void should_fail_if_actual_contains_entry_key_with_different_value() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expectedEntries = array(entry("name", "Yoda"), entry("color", "yellow"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(info, actual, expectedEntries));
    // THEN
    verify(failures).failure(info, shouldContainExactly(actual, asList(expectedEntries),
                                                        newHashSet(entry("color", "yellow")),
                                                        newHashSet(entry("color", "green"))));
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
