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
package org.assertj.tests.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.junit.jupiter.api.Test;

/// @author Nicolas François
class CharSequenceAssert_hasSameSizeAs_with_Iterable_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameSizeAs(list("Solo", "Leia")));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    // GIVEN
    String actual = "Han";
    List<String> other = list("Solo", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameSizeAs(other));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(actual, other, actual.length(), other.size()).create());
  }

  @Test
  void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    assertThat("Han").hasSameSizeAs(list("Solo", "Leia", "Yoda"));
  }

}
