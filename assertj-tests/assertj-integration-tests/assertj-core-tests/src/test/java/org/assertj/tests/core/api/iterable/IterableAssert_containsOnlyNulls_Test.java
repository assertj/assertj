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
package org.assertj.tests.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainOnlyNulls.shouldContainOnlyNulls;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/// @author Billy Yuan
class IterableAssert_containsOnlyNulls_Test {

  @Test
  void should_pass_if_actual_contains_null_once() {
    assertThat(list((String) null)).containsOnlyNulls();
  }

  @Test
  void should_pass_if_actual_contains_null_more_than_once() {
    assertThat(list((String) null, null, null)).containsOnlyNulls();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyNulls());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    List<String> actual = new ArrayList<>();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyNulls());
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyNulls(actual).create());
  }

  @Test
  void should_fail_if_actual_contains_null_and_non_null_elements() {
    // GIVEN
    List<String> actual = list(null, null, "person");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyNulls());
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyNulls(actual, list("person")).create());
  }

  @Test
  void should_fail_if_actual_contains_non_null_elements_only() {
    // GIVEN
    List<String> actual = list("person", "person2");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyNulls());
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyNulls(actual, list("person", "person2")).create());
  }

}
