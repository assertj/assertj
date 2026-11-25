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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class IterableAssert_noneSatisfy_Test {

  private final List<String> actual = List.of("Luke", "Leia", "Yoda");

  @Test
  void should_pass_when_no_elements_satisfy_the_given_single_restriction() {
    // GIVEN
    Consumer<String> restriction = name -> assertThat(name).hasSize(5);
    // WHEN/THEN
    then(actual).noneSatisfy(restriction);
  }

  @Test
  void should_pass_when_no_elements_satisfy_the_given_restrictions() {
    // GIVEN
    Consumer<String> restrictions = name -> {
      assertThat(name).hasSize(5);
      assertThat(name).contains("V");
    };
    // WHEN/THEN
    then(actual).noneSatisfy(restrictions);
  }

  @Test
  void should_pass_for_empty_whatever_the_given_restrictions_are() {
    // GIVEN
    List<String> actual = List.of();
    Consumer<String> restriction = name -> assertThat(name).hasSize(5);
    // WHEN/THEN
    then(actual).noneSatisfy(restriction);
  }

  @Test
  void should_fail_when_one_elements_satisfy_the_given_restrictions() {
    // GIVEN
    Consumer<String> restrictions = name -> assertThat(name).startsWith("Y");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).noneSatisfy(restrictions));
    // THEN
    then(assertionError).hasMessage(noElementsShouldSatisfy(actual, List.of("Yoda")).create());
  }

  @Test
  void should_fail_when_two_elements_satisfy_the_given_restrictions() {
    // GIVEN
    Consumer<String> restrictions = name -> assertThat(name).startsWith("L");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).noneSatisfy(restrictions));
    // THEN
    then(assertionError).hasMessage(noElementsShouldSatisfy(actual, List.of("Luke", "Leia")).create());
  }

  @Test
  void should_throw_error_if_consumer_restrictions_is_null() {
    // GIVEN
    Consumer<String> restrictions = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).noneSatisfy(restrictions));
    // THEN
    then(npe).hasMessage("The Consumer<T> expressing the restrictions must not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    Consumer<String> restrictions = name -> assertThat(name).startsWith("Y");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).noneSatisfy(restrictions));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    Consumer<String> restrictions = s -> assertThat(s).startsWith("Luk");
    // WHEN
    softly.assertThat(List.of("Luke", "Yoda"))
          .overridingErrorMessage("error message")
          .noneSatisfy(restrictions);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
