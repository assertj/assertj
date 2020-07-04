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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.StringAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link IterableAssert#singleElement()}</code>.
 * 
 * @author Michael Grafl
 */
@DisplayName("IterableAssert singleElement")
class IterableAssert_singleElement_Test {

  private final Iterable<String> babySimpsons = list("Maggie");
  private final Iterable<String> simpsons = list("Homer", "Marge", "Lisa", "Bart", "Maggie");

  @Test
  void should_fail_if_iterable_is_empty() {
    // GIVEN
    Iterable<String> iterable = emptyList();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(iterable).singleElement());
    // THEN
    then(assertionError).hasMessageContaining("Expected size:<1> but was:<0>");
  }

  @Test
  void should_pass_allowing_object_assertions_if_iterable_contains_exactly_one_element() {
    // WHEN
    ObjectAssert<String> result = assertThat(babySimpsons).singleElement();
    // THEN
    result.isEqualTo("Maggie");
  }

  @Test
  void should_fail_if_iterable_has_more_than_one_element() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(simpsons).singleElement());
    // THEN
    then(assertionError).hasMessageContaining("Expected size:<1> but was:<5>");
  }

  @Test
  void should_pass_allowing_type_narrowed_assertions_if_assertion_was_created_with_assert_class() {
    // GIVEN
    Class<StringAssert> assertClass = StringAssert.class;
    // WHEN
    StringAssert result = assertThat(babySimpsons, assertClass).singleElement();
    // THEN
    result.startsWith("Mag");
  }

  @Test
  void should_pass_allowing_type_narrowed_assertions_if_assertion_was_created_with_assert_factory() {
    // GIVEN
    AssertFactory<String, StringAssert> assertFactory = StringAssert::new;
    // WHEN
    StringAssert result = assertThat(babySimpsons, assertFactory).singleElement();
    // THEN
    result.startsWith("Mag");
  }

}
