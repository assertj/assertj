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
package org.assertj.tests.core.api.collection;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Collection;
import java.util.List;

import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CollectionAssert_containsExactlyInAnyOrderElementsOf_Test extends CollectionAssertBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Collection<String> actual = null;
    Iterable<String> values = List.of("foo", "bar");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrderElementsOf(values));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_given_values_are_null() {
    // GIVEN
    Collection<String> actual = List.of("foo");
    Iterable<String> values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).containsExactlyInAnyOrderElementsOf(values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(shouldNotBeNull("values").create());
  }

  @ParameterizedTest
  @MethodSource("setContainingArrayElements")
  void should_fail_with_set_containing_array_elements(Collection<String[]> actual) {
    // GIVEN
    Iterable<String[]> values = List.<String[]> of(array("foo"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsExactlyInAnyOrderElementsOf(values));
    // THEN
    then(assertionError).hasMessage(shouldContainExactlyInAnyOrder(actual, values, values, values,
                                                                   StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_is_empty_and_given_values_are_empty() {
    // GIVEN
    Collection<String> actual = emptyList();
    Iterable<String> values = emptyList();
    // WHEN/THEN
    assertThat(actual).containsExactlyInAnyOrderElementsOf(values);
  }

  @ParameterizedTest
  @MethodSource("caseInsensitiveSets")
  void should_pass_with_case_insensitive_set(Collection<String> actual) {
    // WHEN/THEN
    assertThat(actual).containsExactlyInAnyOrderElementsOf(List.of("fOo", "bar"));
  }

}
