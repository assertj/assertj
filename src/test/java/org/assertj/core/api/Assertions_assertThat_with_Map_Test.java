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
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import org.assertj.core.error.ShouldContainKey;
import org.assertj.core.error.ShouldContainKeys;
import org.assertj.core.error.ShouldNotContainKey;
import org.assertj.core.error.ShouldNotContainKeys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(Map)}</code>.
 * 
 * @author Alex Ruiz
 * @author Nicolas François
 */

class Assertions_assertThat_with_Map_Test {
  /**
   * String constant for unit tests
   */
  private final static String TEST_STRING_ONE = "TestString1";
  /**
   * String constant for unit tests
   */
  private final static String TEST_STRING_TWO = "TestString2";
  /**
   * String constant for unit tests
   */  private final static String TEST_STRING_THREE = "TestString3";

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

  /**
   * Tests that AbstractMapAssert returns correct error for shouldContainKey
   * // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2428
   */
  @Test
  void shouldContainKeyShouldFailWithCorrectMessage() { //NOPMD - suppressed JUnitTestContainsTooManyAsserts - Follows the project's convention for AbstractMapAssert tests //NOPMD - suppressed JUnitTestsShouldIncludeAssert - Assert is included.
    // Default access - PMD Warning
    final ConcurrentHashMap<Object, Object> actual = new ConcurrentHashMap<>();
    actual.put(TEST_STRING_ONE, 1);

    final AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = assertThat(actual); //NOPMD - suppressed Dataflow Anomaly Analysis - Follows the project's convention for AbstractMapAssert tests

    final AssertionError assertionError = expectAssertionError(() -> assertions.containsKey(TEST_STRING_TWO).isNotNull());

    then(assertionError).hasMessage(ShouldContainKey.shouldContainKey(actual, new TestCondition<>(TEST_STRING_TWO)).create());
  }

  /**
   * Tests that AbstractMapAssert returns correct error for shouldContainKeys
   * // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2428
   */
  @Test
  void shouldContainKeysShouldFailWithCorrectMessage() { //NOPMD - suppressed JUnitTestContainsTooManyAsserts - Follows the project's convention for AbstractMapAssert tests
    final ConcurrentHashMap<Object, Object> actual = new ConcurrentHashMap<>();
    actual.put(TEST_STRING_ONE, 1);

    final Set<String> helper = new HashSet<>();
    helper.add(TEST_STRING_TWO);
    helper.add(TEST_STRING_THREE);

    final AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = assertThat(actual); //NOPMD - suppressed Dataflow Anomaly Analysis - Follows the project's convention for AbstractMapAssert tests

    final AssertionError assertionError = expectAssertionError(() -> assertThat(assertions.containsKeys(TEST_STRING_TWO, TEST_STRING_THREE)).isNotNull());

    then(assertionError).hasMessage(ShouldContainKeys.shouldContainKeys(actual, helper).create());  //NOPMD - suppressed LawOfDemeter - The nature of unit test library requires chaining.
  }

  /**
   * Tests that AbstractMapAssert returns correct error for shouldNotContainKey
   * // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2428
   */
  @Test
  void shouldNotContainKeyShouldFailWithCorrectMessage() { //NOPMD - suppressed JUnitTestContainsTooManyAsserts - Follows the project's convention for AbstractMapAssert tests //NOPMD - suppressed JUnitTestsShouldIncludeAssert - Assert is included.
    final ConcurrentHashMap<Object, Object> actual = new ConcurrentHashMap<>();
    actual.put(TEST_STRING_ONE, 1);

    final AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = assertThat(actual); //NOPMD - suppressed Dataflow Anomaly Analysis - Follows the project's convention for AbstractMapAssert tests

    final AssertionError assertionError = expectAssertionError(() -> assertThat(assertions.doesNotContainKey(TEST_STRING_ONE)).isNotNull());

    then(assertionError).hasMessage(ShouldNotContainKey.shouldNotContainKey(actual, TEST_STRING_ONE).create()); //NOPMD - suppressed LawOfDemeter - The nature of unit test library requires chaining.
  }

  /**
   * Tests that AbstractMapAssert returns correct error for shouldNotContainKeys
   * // CS427 Issue link: https://github.com/assertj/assertj-core/issues/2428
   */
  @Test
  void shouldNotContainKeysShouldFailWithCorrectMessage() { //NOPMD - suppressed JUnitTestContainsTooManyAsserts - Follows the project's convention for AbstractMapAssert tests //NOPMD - suppressed JUnitTestsShouldIncludeAssert - Assert is included.
    final ConcurrentHashMap<Object, Object> actual = new ConcurrentHashMap<>();
    actual.put(TEST_STRING_ONE, 1);
    actual.put(TEST_STRING_TWO, 1);

    final Set<String> helper = new HashSet<>();
    helper.add(TEST_STRING_TWO);
    helper.add(TEST_STRING_ONE);

    final AbstractMapAssert<?, ? extends Map<Object, Object>, Object, Object> assertions = assertThat(actual); //NOPMD - suppressed Dataflow Anomaly Analysis - Follows the project's convention for AbstractMapAssert tests

    final AssertionError assertionError = expectAssertionError(() -> assertThat(assertions.doesNotContainKeys(TEST_STRING_TWO, TEST_STRING_ONE)).isNotNull());

    then(assertionError).hasMessage(ShouldNotContainKeys.shouldNotContainKeys(actual, helper).create()); //NOPMD - suppressed LawOfDemeter - The nature of unit test library requires chaining.
  }
}
