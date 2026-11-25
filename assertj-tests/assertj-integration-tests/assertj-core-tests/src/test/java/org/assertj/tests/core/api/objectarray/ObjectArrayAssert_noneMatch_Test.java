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
import static org.assertj.core.error.NoElementsShouldMatch.noElementsShouldMatch;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.presentation.PredicateDescription.GIVEN;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.Predicate;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_noneMatch_Test {

  @Test
  void should_pass_if_no_element_matches_predicate() {
    assertThat(new String[] { "123", "1234", "12345" }).noneMatch(s -> s.length() < 3);
  }

  @Test
  void should_pass_if_actual_is_empty() {
    assertThat(new String[0]).noneMatch(s -> true);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).noneMatch(String::isEmpty));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_predicate_is_null() {
    // GIVEN
    String[] actual = { "Luke", "Yoda" };
    Predicate<String> predicate = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).noneMatch(predicate));
    // THEN
    then(exception).hasMessage("The predicate to evaluate should not be null");
  }

  @Test
  void should_fail_if_an_element_matches_predicate() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).noneMatch(s -> s.startsWith("L")));
    // THEN
    then(assertionError).hasMessage(noElementsShouldMatch(List.of(actual), "Luke", GIVEN).create());
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    // WHEN
    softly.assertThat(new String[] { "Luke", "Yoda" })
          .overridingErrorMessage("error message")
          .noneMatch(obj -> true);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
