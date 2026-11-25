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
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

class AtomicReferenceArrayAssert_anySatisfy_Test {

  private static final Consumer<String> STARTS_WITH_L = name -> assertThat(name).startsWith("L");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).anySatisfy(STARTS_WITH_L));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_consumer_is_null() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Luke", "Leia"));
    Consumer<String> requirements = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).anySatisfy(requirements));
    // THEN
    then(npe).hasMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(new String[0]);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).anySatisfy(STARTS_WITH_L));
    // THEN
    then(assertionError).hasMessage(elementsShouldSatisfyAny(list(), list(), someInfo()).create());
  }

  @Test
  void should_pass_if_one_element_satisfies_the_requirements() {
    assertThat(new AtomicReferenceArray<>(array("Yoda", "Luke"))).anySatisfy(STARTS_WITH_L);
  }

  @Test
  void should_fail_if_no_element_satisfies_the_requirements() {
    // GIVEN
    AtomicReferenceArray<String> actual = new AtomicReferenceArray<>(array("Yoda", "Mace"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).anySatisfy(STARTS_WITH_L));
    // THEN
    // can't build the exact error message due to the stack traces of the element errors
    then(assertionError).hasMessageStartingWith(format("%n" +
                                                       "Expecting any element of:%n" +
                                                       "  [\"Yoda\", \"Mace\"]%n" +
                                                       "to satisfy the given assertions requirements but none did:%n%n"))
                        .hasMessageContainingAll(shouldStartWith("Yoda", "L").create(),
                                                 shouldStartWith("Mace", "L").create());
  }

}
