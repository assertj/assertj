/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.object;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

class ObjectAssert_doesNotReturn_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Jedi actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).doesNotReturn("Yoda", from(Jedi::getName)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_from_is_null() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).doesNotReturn("Yoda", null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage("The given getter method/Function must not be null");
  }

  @Test
  void should_pass() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    // WHEN/THEN
    assertThat(actual).doesNotReturn("Luke", from(Jedi::getName))
                      .doesNotReturn("Luke", Jedi::getName);
  }

  @Test
  void should_pass_if_expected_is_null() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    // WHEN/THEN
    assertThat(actual).doesNotReturn(null, from(Jedi::getName));
  }

  @Test
  void should_honor_custom_type_comparator() {
    // GIVEN
    Jedi actual = new Jedi("Yoda", "Green");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).usingComparatorForType(CASE_INSENSITIVE_ORDER,
                                                                                                         String.class)
                                                                                 .doesNotReturn("YODA", from(Jedi::getName)));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqual("Yoda", "YODA",
                                                     new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER)).create());
  }

}
