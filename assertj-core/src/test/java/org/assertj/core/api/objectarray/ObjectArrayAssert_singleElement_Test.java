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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_singleElement_Test {

  private final String[] babySimpsons = { "Maggie" };
  private final String[] simpsons = { "Homer", "Marge", "Lisa", "Bart", "Maggie" };

  @Test
  void should_fail_if_iterable_is_empty() {
    // GIVEN
    String[] array = new String[0];
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(array).singleElement());
    // THEN
    then(assertionError).hasMessageContaining("Expected size: 1 but was: 0");
  }

  @Test
  void should_pass_allowing_object_assertions_if_iterable_contains_exactly_one_element() {
    // WHEN
    ObjectAssert<String> result = assertThat(babySimpsons).singleElement();
    // THEN
    result.isEqualTo("Maggie");
  }

  @Test
  void should_fail_with_an_error_describing_thenavigation() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(babySimpsons).singleElement().isEqualTo("Bart"));
    // THEN
    then(assertionError).hasMessageStartingWith("[ObjectArray check single element]");
  }

  @Test
  void should_fail_if_iterable_has_more_than_one_element() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(simpsons).singleElement());
    // THEN
    then(assertionError).hasMessageContaining("Expected size: 1 but was: 5");
  }

}
