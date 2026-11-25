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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAnArray.shouldBeAnArray;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

class AtomicReferenceArrayAssert_satisfiesExactly_Test {

  private static final Consumer<String> STARTS_WITH_L = name -> assertThat(name).startsWith("L");
  private static final Consumer<String> STARTS_WITH_Y = name -> assertThat(name).startsWith("Y");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(STARTS_WITH_L));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_requirements_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Yoda"));
    Consumer<String>[] requirements = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(requirements));
    // THEN
    then(assertionError).hasMessage(shouldBeAnArray(null).create());
  }

  @Test
  void should_fail_if_actual_and_requirements_have_different_sizes() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Yoda", "Leia"));
    Consumer<String>[] requirements = array(STARTS_WITH_L, STARTS_WITH_Y);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(requirements));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(list("Luke", "Yoda", "Leia"), requirements, 3, 2).create());
  }

  @Test
  void should_pass_if_each_element_satisfies_the_requirements_at_its_index() {
    assertThat(new AtomicReferenceArray<>(array("Luke", "Yoda"))).satisfiesExactly(STARTS_WITH_L, STARTS_WITH_Y);
  }

  @Test
  void should_fail_if_requirements_are_met_but_not_in_the_right_order() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Yoda", "Luke"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(STARTS_WITH_L, STARTS_WITH_Y));
    // THEN
    // can't build the exact error message due to the stack traces of the element errors
    then(assertionError).hasMessageStartingWith(format("%n" +
                                                       "Expecting each element of:%n" +
                                                       "  [\"Yoda\", \"Luke\"]%n" +
                                                       "to satisfy the requirements at its index, but these elements did not:%n%n"))
                        .hasMessageContainingAll(shouldStartWith("Yoda", "L").create(),
                                                 shouldStartWith("Luke", "Y").create());
  }

}
