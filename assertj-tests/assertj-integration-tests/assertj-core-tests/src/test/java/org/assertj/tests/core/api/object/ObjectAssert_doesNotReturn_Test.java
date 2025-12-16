/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.api.object;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

class ObjectAssert_doesNotReturn_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Jedi actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotReturn("Yoda", from(Jedi::getName)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_from_is_null() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    // WHEN
    var nullPointerException = catchNullPointerException(() -> assertThat(actual).doesNotReturn("Yoda", null));
    // THEN
    then(nullPointerException).hasMessage("The given getter method/Function must not be null");
  }

  @Test
  void should_pass() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    // WHEN/THEN
    then(actual).doesNotReturn("Luke", from(Jedi::getName))
                .doesNotReturn("Luke", Jedi::getName);
  }

  @Test
  void should_pass_if_expected_is_null() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    // WHEN/THEN
    then(actual).doesNotReturn(null, from(Jedi::getName));
  }

  @Test
  void should_honor_custom_type_comparator() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparatorForType(CASE_INSENSITIVE_ORDER,
                                                                                              String.class)
                                                                      .doesNotReturn("YODA", from(Jedi::getName)));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqual("Yoda", "YODA",
                                                     new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

}
