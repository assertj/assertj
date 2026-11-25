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
package org.assertj.tests.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

class AtomicReferenceArrayAssert_allMatch_with_description_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(name -> name.startsWith("L"), "starts with L"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_predicate_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Leia"));
    Predicate<String> predicate = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).allMatch(predicate, "starts with L"));
    // THEN
    then(npe).hasMessage("The predicate to evaluate should not be null");
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(new String[0]);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(name -> name.startsWith("L"), "starts with L"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_pass_if_all_elements_match_the_predicate() {
    assertThat(new AtomicReferenceArray<>(array("Luke", "Leia"))).allMatch(name -> name.startsWith("L"), "starts with L");
  }

  @Test
  void should_fail_with_custom_description_if_one_element_does_not_match_the_predicate() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Leia", "Yoda"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(name -> name.startsWith("L"), "starts with L"));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(list("Luke", "Leia", "Yoda"), "Yoda",
                                                        new PredicateDescription("starts with L")).create());
  }

}
