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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal.maps;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainKeys.shouldNotContainKeys;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.set;

import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Maps#assertDoesNotContainKeys(AssertionInfo, Map, Object[])}</code>.
 *
 * @author dorzey
 */
@DisplayName("Maps assertDoesNotContainKeys")
class Maps_assertDoesNotContainKeys_Test extends MapsBaseTest {

  private static final String ARRAY_OF_KEYS = "array of keys";

  @Override
  @BeforeEach
  protected void setUp() {
    super.setUp();
    actual.put(null, null);
  }

  @Test
  void should_pass_if_actual_does_not_contain_given_keys() {
    maps.assertDoesNotContainKeys(someInfo(), actual, array("age"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] keys = { "name" };
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertDoesNotContainKeys(someInfo(), null, keys));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_keys_array_is_null() {
    // GIVEN
    String[] keys = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertDoesNotContainKeys(someInfo(), actual, keys));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(keysToLookForIsNull(ARRAY_OF_KEYS));
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = { "name", "color" })
  void should_fail_if_actual_contains_key(String key) {
    // GIVEN
    String[] keys = { key };
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertDoesNotContainKeys(someInfo(), actual, keys));
    // THEN
    then(error).hasMessage(shouldNotContainKeys(actual, set(key)).create());
  }

  @Test
  void should_fail_if_actual_contains_keys() {
    // GIVEN
    String[] keys = { "name", "color" };
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertDoesNotContainKeys(someInfo(), actual, keys));
    // THEN
    then(error).hasMessage(shouldNotContainKeys(actual, set("name", "color")).create());
  }

}
