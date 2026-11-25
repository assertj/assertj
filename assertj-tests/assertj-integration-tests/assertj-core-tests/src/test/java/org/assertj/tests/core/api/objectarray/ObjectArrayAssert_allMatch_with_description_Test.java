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
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.presentation.PredicateDescription;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_allMatch_with_description_Test {

  @Test
  void should_pass_if_each_element_matches_predicate() {
    assertThat(new String[] { "123", "1234", "12345" }).allMatch(s -> s.length() >= 3, "length >= 3");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(s -> s.length() >= 3, "length >= 3"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_predicate_is_null() {
    // GIVEN
    String[] actual = { "Luke", "Yoda" };
    Predicate<String> predicate = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).allMatch(predicate, "custom"));
    // THEN
    then(exception).hasMessage("The predicate to evaluate should not be null");
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    String[] emptyArray = {};
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(emptyArray).allMatch(e -> e.contains("error"), "custom"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_fail_with_custom_description_if_one_element_does_not_match_predicate() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(s -> s.startsWith("L"), "custom"));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(List.of(actual), "Yoda", new PredicateDescription("custom")).create());
  }

  @Test
  void should_report_all_elements_that_do_not_match_predicate_with_custom_description() {
    // GIVEN
    String[] actual = { "123", "1234", "12345" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).allMatch(s -> s.length() <= 3, "custom"));
    // THEN
    then(assertionError).hasMessage(elementsShouldMatch(List.of(actual), List.of("1234", "12345"),
                                                        new PredicateDescription("custom")).create());
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat(new String[] { "Luke", "Yoda" })
          .overridingErrorMessage("error message")
          .allMatch(obj -> false, "custom");
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
