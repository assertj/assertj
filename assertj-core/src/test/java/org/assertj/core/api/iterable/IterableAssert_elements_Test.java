/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.iterable;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsEmpty;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.IterableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link IterableAssert#elements(int...)}</code>.
 */
@DisplayName("IterableAssert elements(int...)")
class IterableAssert_elements_Test {

  private final Iterable<String> iterable = list("Homer", "Marge", "Lisa", "Bart", "Maggie");

  @Test
  void should_fail_if_iterable_is_empty() {
    // GIVEN
    Iterable<String> iterable = emptyList();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(iterable).elements(1));
    // THEN
    then(assertionError).hasMessage(actualIsEmpty());
  }

  @Test
  void should_pass_allowing_object_assertions_if_iterable_contains_enough_elements() {
    // WHEN
    IterableAssert<String> result = assertThat(iterable).elements(1);
    // THEN
    result.containsExactly("Marge");
  }

  @Test
  void should_pass_allowing_assertions_for_several_elements() {
    // WHEN
    IterableAssert<String> result = assertThat(iterable).elements(1, 2);
    // THEN
    result.containsExactly("Marge", "Lisa");
  }

  @Test
  void should_pass_allowing_assertions_for_several_unordered_elements() {
    // WHEN
    IterableAssert<String> result = assertThat(iterable).elements(2, 1);
    // THEN
    result.containsExactly("Lisa", "Marge");
  }

  @Test
  void should_pass_allowing_assertions_for_repeating_elements() {
    // WHEN
    IterableAssert<String> result = assertThat(iterable).elements(2, 1, 2);
    // THEN
    result.containsExactly("Lisa", "Marge", "Lisa");
  }

  @Test
  void should_fail_if_index_out_of_range() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(iterable).elements(5));
    // THEN
    then(assertionError).hasMessageContainingAll("check actual size is enough to get element[5]",
                                                 "Expecting size of:",
                                                 "to be greater than 5 but was 5");
  }

  @Test
  void should_fail_if_indices_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(iterable).elements())
                                        .withMessageContaining("indices must not be empty");
  }

  @Test
  void should_fail_if_indices_is_empty_2() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(iterable).elements(new int[0]))
                                        .withMessageContaining("indices must not be empty");
  }

  @Test
  void should_fail_if_indices_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(iterable).elements((int[]) null))
                                        .withMessageContaining("indices must not be null");
  }

  @Test
  void should_fail_if_iterable_is_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat((Iterable<?>) null).elements(1));
    // THEN
    then(assertionError).hasMessageContaining("Expecting actual not to be null");
  }
}
