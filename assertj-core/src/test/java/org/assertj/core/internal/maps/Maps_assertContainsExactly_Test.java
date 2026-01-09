/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.set;
import static org.mockito.Mockito.verify;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
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
    Entry<String, String>[] expected = array(entry("name", "Yoda"));
    // WHEN
    var assertionError = expectAssertionError(() -> maps.assertContainsExactly(INFO, actual, expected, null));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_entries_array_is_null() {
    // GIVEN
    Entry<String, String>[] entries = null;
    // WHEN/THEN
    thenNullPointerException().isThrownBy(() -> maps.assertContainsExactly(INFO, linkedActual, entries, null))
                              .withMessage(entriesToLookForIsNull());
  }

  @Test
  void should_fail_if_given_entries_array_is_empty() {
    // GIVEN
    Entry<String, String>[] expected = emptyEntries();
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(INFO, actual, expected, null));
    // THEN
    verify(failures).failure(INFO, shouldBeEmpty(actual));
  }

  @Test
  void should_pass_if_actual_and_entries_are_empty() {
    maps.assertContainsExactly(INFO, emptyMap(), array(), null);
  }

  @Test
  void should_pass_if_actual_contains_given_entries_in_order() {
    maps.assertContainsExactly(INFO, linkedActual, array(entry("name", "Yoda"), entry("color", "green")), null);
  }

  @Test
  void should_fail_if_actual_contains_given_entries_in_disorder() {
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(INFO, linkedActual,
                                                          array(entry("color", "green"), entry("name", "Yoda")), null));
    // THEN
    verify(failures).failure(INFO, elementsDifferAtIndex(entry("name", "Yoda"), entry("color", "green"), 0));
  }

  @Test
  void should_fail_if_actual_and_expected_entries_have_different_size() {
    // GIVEN
    Entry<String, String>[] expected = array(entry("name", "Yoda"));
    // WHEN
    var assertionError = expectAssertionError(() -> maps.assertContainsExactly(INFO, linkedActual, expected, null));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(linkedActual, expected, linkedActual.size(), expected.length).create());
  }

  @Test
  void should_fail_if_actual_does_not_contains_every_expected_entries_and_contains_unexpected_one() {
    // GIVEN
    Entry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    Map<String, String> underTest = mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(INFO, underTest, expected, null));
    // THEN
    verify(failures).failure(INFO, shouldContainExactly(underTest, list(expected),
                                                        set(entry("color", "green")),
                                                        set(entry("job", "Jedi"))));
  }

  @Test
  void should_fail_if_actual_contains_entry_key_with_different_value() {
    // GIVEN
    Entry<String, String>[] expectedEntries = array(entry("name", "Yoda"), entry("color", "yellow"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(INFO, actual, expectedEntries, null));
    // THEN
    verify(failures).failure(INFO, shouldContainExactly(actual, asList(expectedEntries),
                                                        set(entry("color", "yellow")),
                                                        set(entry("color", "green"))));
  }

  @Test
  void should_pass_with_singleton_map_having_array_value() {
    // GIVEN
    Map<String, String[]> actual = singletonMap("color", array("yellow"));
    Entry<String, String[]>[] expected = array(entry("color", array("yellow")));
    // WHEN/THEN
    maps.assertContainsExactly(INFO, actual, expected, null);
  }

  @Test
  void should_fail_with_singleton_map_having_array_value() {
    // GIVEN
    Map<String, String[]> actual = singletonMap("color", array("yellow"));
    Entry<String, String[]>[] expected = array(entry("color", array("green")));
    // WHEN
    var assertionError = expectAssertionError(() -> maps.assertContainsExactly(INFO, actual, expected, null));
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, asList(expected),
                                                         set(entry("color", array("green"))),
                                                         set(entry("color", array("yellow")))).create());
  }

  @Test
  void should_pass_with_Properties() {
    // GIVEN
    Properties actual = mapOf(Properties::new, entry("name", "Yoda"), entry("job", "Jedi"));
    Entry<Object, Object>[] expected = array(entry("name", "Yoda"), entry("job", "Jedi"));
    // WHEN/THEN
    maps.assertContainsExactly(INFO, actual, expected, null);
  }

  @Test
  void should_fail_with_Properties() {
    // GIVEN
    Properties actual = mapOf(Properties::new, entry("name", "Yoda"), entry("job", "Jedi"));
    Entry<Object, Object>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    // WHEN
    var assertionError = expectAssertionError(() -> maps.assertContainsExactly(INFO, actual, expected, null));
    // THEN
    then(assertionError).hasMessage(shouldContainExactly(actual, asList(expected),
                                                         set(entry("color", "green")),
                                                         set(entry("job", "Jedi"))).create());
  }

}
