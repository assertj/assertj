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
package org.assertj.tests.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.function.Consumer;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

/// @author Stefan Bratanov
class ObjectArrayAssert_satisfiesOnlyOnce_Test {

  private static final Consumer<String> IS_LUKE = value -> assertThat(value).isEqualTo("Luke");

  @Test
  void should_pass_if_only_one_element_satisfies_requirements() {
    assertThat(new String[] { "Luke", "Leia", "Yoda" }).satisfiesOnlyOnce(IS_LUKE);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesOnlyOnce(IS_LUKE));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_consumer_is_null() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    Consumer<String> requirements = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).satisfiesOnlyOnce(requirements));
    // THEN
    then(exception).hasMessage("The Consumer<? super E> expressing the requirements must not be null");
  }

  @Test
  void should_fail_if_several_elements_satisfy_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda", "Luke" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesOnlyOnce(IS_LUKE));
    // THEN
    then(assertionError).hasMessageContainingAll("Expecting exactly one element of actual:",
                                                 "to satisfy the requirements but these 2 elements did:",
                                                 "[\"Luke\", \"Luke\"]");
  }

  @Test
  void should_fail_if_no_element_satisfies_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    Consumer<String> requirements = value -> assertThat(value).isEqualTo("Vader");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesOnlyOnce(requirements));
    // THEN
    then(assertionError).hasMessageContainingAll("Expecting exactly one element of actual:",
                                                 "to satisfy the requirements but none did",
                                                 "\"Luke\"", "\"Leia\"", "\"Yoda\"");
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    String[] actual = {};
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesOnlyOnce(IS_LUKE));
    // THEN
    then(assertionError).hasMessageContainingAll("Expecting exactly one element of actual:",
                                                 "to satisfy the requirements but none did");
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    Consumer<Object> requirements = element -> assertThat(element).isNotNull();
    // WHEN
    softly.assertThat(new String[] { "Luke", "Yoda" })
          .overridingErrorMessage("error message")
          .satisfiesOnlyOnce(requirements);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
