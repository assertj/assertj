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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Maps#assertContainsKeys(AssertionInfo, Map, Object[])}</code>.
 *
 * @author William Delanoue
 */
class Maps_assertContainsKeys_Test extends MapsBaseTest {

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = mapOf(entry("name", "Yoda"), entry("color", "green"), entry(null, null));
  }

  @Test
  void should_pass_if_actual_contains_given_key() {
    maps.assertContainsKeys(someInfo(), actual, array("name"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsKey(someInfo(), null, array("name")));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_success_if_key_is_null() {
    maps.assertContainsKeys(someInfo(), actual, new String[] { null });
  }

  @Test
  void should_fail_if_actual_does_not_contain_key() {
    // GIVEN
    AssertionInfo info = someInfo();
    String key = "power";
    // WHEN
    expectAssertionError(() -> maps.assertContainsKeys(info, actual, array(key)));
    // THEN
    verify(failures).failure(info, shouldContainKeys(actual, newLinkedHashSet(key)));
  }

  @Test
  void should_fail_if_actual_does_not_contain_keys() {
    // GIVEN
    AssertionInfo info = someInfo();
    String key1 = "power";
    String key2 = "rangers";
    // WHEN
    expectAssertionError(() -> maps.assertContainsKeys(info, actual, array(key1, key2)));
    // THEN
    verify(failures).failure(info, shouldContainKeys(actual, newLinkedHashSet(key1, key2)));
  }
}
