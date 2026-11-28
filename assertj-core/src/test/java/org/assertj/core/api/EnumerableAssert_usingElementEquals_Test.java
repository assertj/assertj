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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.BiPredicate;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link EnumerableAssert#usingElementEquals(BiPredicate)}</code>.
 */
class EnumerableAssert_usingElementEquals_Test {

  // a comparison weaker than equals: both elements only need to share their first letter
  private static final BiPredicate<String, String> SAME_FIRST_LETTER = (actual, other) -> actual.charAt(0) == other.charAt(0);

  @Test
  void should_compare_elements_with_the_given_bi_predicate() {
    // GIVEN
    List<String> actual = List.of("Luke", "Yoda");
    // WHEN/THEN
    then(actual).usingElementEquals(SAME_FIRST_LETTER)
                .contains("Leia", "Yoda");
  }

  @Test
  void should_fail_if_no_element_matches_the_given_bi_predicate() {
    // GIVEN
    List<String> actual = List.of("Luke", "Yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementEquals(SAME_FIRST_LETTER)
                                                                      .contains("Anakin"));
    // THEN
    then(assertionError).hasMessageContaining("Anakin");
  }

  @Test
  void should_apply_the_given_bi_predicate_to_doesNotContain() {
    // GIVEN
    List<String> actual = List.of("Luke", "Yoda");
    // WHEN/THEN
    then(actual).usingElementEquals(SAME_FIRST_LETTER)
                .doesNotContain("Han");
  }

  @Test
  void should_fail_if_doesNotContain_element_matches_the_given_bi_predicate() {
    // GIVEN
    List<String> actual = List.of("Luke", "Yoda");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementEquals(SAME_FIRST_LETTER)
                                                                      .doesNotContain("Leia"));
    // THEN
    then(assertionError).hasMessageContaining("Leia");
  }

  @Test
  void should_fail_if_the_given_bi_predicate_is_null() {
    // GIVEN
    List<String> actual = List.of("Luke");
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).usingElementEquals(null))
                                    .withMessage("The element bi-predicate must not be null");
  }

}
