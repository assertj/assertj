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
import static org.assertj.core.error.ElementsShouldHaveAtLeast.elementsShouldHaveAtLeast;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.core.api.Condition;
import org.assertj.tests.core.testkit.JediPowerCondition;
import org.junit.jupiter.api.Test;

/// @author Nicolas François
/// @author Mikhail Mazursky
/// @author Joel Costigliola
class IterableAssert_haveAtLeast_Test {

  private static final JediPowerCondition JEDI_POWER = new JediPowerCondition();

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).haveAtLeast(2, JEDI_POWER));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_condition_is_null() {
    // GIVEN
    List<String> actual = List.of("Yoda", "Luke");
    Condition<String> condition = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).haveAtLeast(2, condition));
    // THEN
    then(npe).hasMessage("The condition to evaluate should not be null");
  }

  @Test
  void should_pass_if_satisfies_at_least_times_condition() {
    assertThat(List.of("Yoda", "Luke", "Leia")).haveAtLeast(2, JEDI_POWER);
  }

  @Test
  void should_pass_if_all_satisfies_condition() {
    assertThat(List.of("Yoda", "Luke", "Obiwan")).haveAtLeast(2, JEDI_POWER);
  }

  @Test
  void should_fail_if_condition_is_not_met_enough() {
    // GIVEN
    List<String> actual = List.of("Yoda", "Solo", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).haveAtLeast(2, JEDI_POWER));
    // THEN
    then(assertionError).hasMessage(elementsShouldHaveAtLeast(actual, 2, JEDI_POWER).create());
  }

}
