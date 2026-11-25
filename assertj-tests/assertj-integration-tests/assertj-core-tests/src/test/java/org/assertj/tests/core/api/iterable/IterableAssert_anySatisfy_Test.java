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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class IterableAssert_anySatisfy_Test {

  private final List<String> actual = List.of("Luke", "Leia", "Yoda", "Obiwan");

  @Test
  void must_not_check_all_elements() {
    // GIVEN
    AtomicInteger evaluatedElements = new AtomicInteger();
    // first element does not match, 2nd element does match
    Consumer<String> requirements = s -> {
      evaluatedElements.incrementAndGet();
      assertThat(s).isEqualTo("Leia");
    };
    // WHEN
    assertThat(actual).anySatisfy(requirements);
    // THEN
    // make sure that we only evaluated 2 out of 4 elements
    then(evaluatedElements).hasValue(2);
  }

  @Test
  void should_pass_when_one_element_satisfies_the_single_assertion_requirement() {
    // GIVEN
    Consumer<String> requirement = s -> assertThat(s).hasSize(6);
    // WHEN/THEN
    then(actual).anySatisfy(requirement);
  }

  @Test
  void should_pass_when_one_element_satisfies_all_the_assertion_requirements() {
    // GIVEN
    Consumer<String> requirements = s -> {
      assertThat(s).hasSize(4);
      assertThat(s).doesNotContain("L");
    };
    // WHEN/THEN
    then(actual).anySatisfy(requirements);
  }

  @Test
  void should_pass_when_several_elements_satisfy_all_the_assertion_requirements() {
    // GIVEN
    Consumer<String> requirements = s -> {
      assertThat(s).hasSize(4);
      assertThat(s).contains("L");
    };
    // WHEN/THEN
    then(actual).anySatisfy(requirements);
  }

  @Test
  void should_fail_if_no_elements_satisfy_the_assertions_requirements() {
    // GIVEN
    Consumer<String> requirements = s -> {
      assertThat(s).hasSize(4);
      assertThat(s).contains("W");
    };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).anySatisfy(requirements));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(assertionError).hasMessageContainingAll(format("%n" +
                                                        "Expecting actual:%n" +
                                                        "  \"Luke\"%n" +
                                                        "to contain:%n" +
                                                        "  \"W\" "),
                                                 format("%n" +
                                                        "Expecting actual:%n" +
                                                        "  \"Leia\"%n" +
                                                        "to contain:%n" +
                                                        "  \"W\" "),
                                                 format("%n" +
                                                        "Expecting actual:%n" +
                                                        "  \"Yoda\"%n" +
                                                        "to contain:%n" +
                                                        "  \"W\" "),
                                                 format("%n" +
                                                        "Expected size: 4 but was: 6 in:%n" +
                                                        "\"Obiwan\""));
  }

  @Test
  void should_fail_if_the_iterable_under_test_is_empty_whatever_the_assertions_requirements_are() {
    // GIVEN
    List<String> actual = List.of();
    Consumer<String> requirements = s -> assertThat(true).isTrue();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).anySatisfy(requirements));
    // THEN
    then(assertionError).hasMessage(elementsShouldSatisfyAny(actual, List.of(), someInfo()).create());
  }

  @Test
  void should_fail_if_consumer_is_null() {
    // GIVEN
    Consumer<String> requirements = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).anySatisfy(requirements));
    // THEN
    then(npe).hasMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    Consumer<String> requirements = s -> {};
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).anySatisfy(requirements));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    Consumer<String> requirements = s -> assertThat(s).startsWith("Darth");
    // WHEN
    softly.assertThat(List.of("Luke", "Yoda"))
          .overridingErrorMessage("error message")
          .anySatisfy(requirements);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
