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

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_anySatisfy_Test {

  @Test
  void should_pass_if_one_element_satisfies_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda", "Obiwan" };
    Consumer<String> requirements = s -> {
      assertThat(s).hasSize(4);
      assertThat(s).doesNotContain("L");
    };
    // WHEN/THEN
    then(actual).anySatisfy(requirements);
  }

  @Test
  void should_pass_if_several_elements_satisfy_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda", "Obiwan" };
    Consumer<String> requirements = s -> assertThat(s).contains("L");
    // WHEN/THEN
    then(actual).anySatisfy(requirements);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] actual = null;
    Consumer<String> requirements = s -> assertThat(s).isNotNull();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).anySatisfy(requirements));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_consumer_is_null() {
    // GIVEN
    String[] actual = { "Luke", "Yoda" };
    Consumer<String> requirements = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).anySatisfy(requirements));
    // THEN
    then(exception).hasMessage("The Consumer<T> expressing the assertions requirements must not be null");
  }

  @Test
  void should_fail_if_no_element_satisfies_requirements() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    Consumer<String> requirements = s -> assertThat(s).contains("W");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).anySatisfy(requirements));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(assertionError).hasMessageStartingWith("%nExpecting any element of:%n  [\"Luke\", \"Leia\", \"Yoda\"]%nto satisfy the given assertions requirements but none did:%n%n".formatted())
                        .hasMessageContainingAll(shouldContain("Luke", "W").create(),
                                                 shouldContain("Leia", "W").create(),
                                                 shouldContain("Yoda", "W").create());
  }

  @Test
  void should_fail_if_actual_is_empty_whatever_the_requirements_are() {
    // GIVEN
    String[] actual = {};
    Consumer<String> requirements = s -> {};
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).anySatisfy(requirements));
    // THEN
    then(assertionError).hasMessage(elementsShouldSatisfyAny(List.of(), emptyList(), someInfo()).create());
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    Consumer<String> requirements = s -> assertThat(s).startsWith("Darth");
    // WHEN
    softly.assertThat(new String[] { "Luke", "Yoda" })
          .overridingErrorMessage("error message")
          .anySatisfy(requirements);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
