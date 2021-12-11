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
package org.assertj.core.api;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainKey.shouldContainKey;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.error.ShouldNotContainKey.shouldNotContainKey;
import static org.assertj.core.error.ShouldNotContainKeys.shouldNotContainKeys;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(Map)}</code>.
 * 
 * @author Alex Ruiz
 * @author Nicolas François
 */
class Assertions_assertThat_with_Map_Test {

  @Test
  void should_create_Assert() {
    AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = Assertions.assertThat(emptyMap());
    assertThat(assertions).isNotNull();
  }

  @Test
  void should_pass_actual() {
    Map<Object, Object> actual = new HashMap<>();
    AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = Assertions.assertThat(actual);
    assertThat(assertions.actual).isSameAs(actual);
  }

  // Use parameterized tests https://github.com/assertj/assertj-core/blob/9eb27d7005ce807d01d0180df794625db278c61e/src/test/java/org/assertj/core/api/map/MapAssert_containsValue_Test.java

  /**
   * Tests that AbstractMapAssert returns correct error for shouldContainKey
   * // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2428
   */
  @Test
  void should_contain_key_should_fail_with_correct_message() {
    Map<Object, Object> actual = new HashMap<>();
    actual.put("TestString", 1);

    AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = Assertions.assertThat(actual);

    AssertionError assertionError = expectAssertionError(() -> assertThat(assertions.containsKey("TestString2")).isNotNull());

    then(assertionError).hasMessage(shouldContainKey(actual, new TestCondition<>("TestString2")).create());
  }

  /**
   * Tests that AbstractMapAssert returns correct error for shouldContainKeys
   * // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2428
   */
  @Test
  void should_contain_keys_should_fail_with_correct_message() {
    Map<Object, Object> actual = new HashMap<>();
    actual.put("TestString", 1);

    Set<String> helper = new HashSet<>();
    helper.add("TestString2");
    helper.add("TestString3");

    AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = Assertions.assertThat(actual);

    AssertionError assertionError = expectAssertionError(() -> assertThat(assertions.containsKeys("TestString2", "TestString3")).isNotNull());

    then(assertionError).hasMessage(shouldContainKeys(actual, helper).create());
  }

  /**
   * Tests that AbstractMapAssert returns correct error for shouldNotContainKey
   * // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2428
   */
  @Test
  void should_not_contain_key_should_fail_with_correct_message() {
    Map<Object, Object> actual = new HashMap<>();
    actual.put("TestString", 1);

    AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = Assertions.assertThat(actual);

    AssertionError assertionError = expectAssertionError(() -> assertThat(assertions.doesNotContainKey("TestString")).isNotNull());

    then(assertionError).hasMessage(shouldNotContainKey(actual, "TestString").create());
  }

  /**
   * Tests that AbstractMapAssert returns correct error for shouldNotContainKeys
   * // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2428
   */
  @Test
  void should_not_contain_keys_should_fail_with_correct_message() {
    Map<Object, Object> actual = new HashMap<>();
    actual.put("TestString", 1);
    actual.put("TestString2", 1);

    Set<String> helper = new HashSet<>();
    helper.add("TestString2");
    helper.add("TestString");

    AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = Assertions.assertThat(actual);

    AssertionError assertionError = expectAssertionError(() -> assertThat(assertions.doesNotContainKeys("TestString2", "TestString")).isNotNull());

    then(assertionError).hasMessage(shouldNotContainKeys(actual, helper).create());
  }
}
