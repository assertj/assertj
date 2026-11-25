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
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfyExactlyInAnyOrder;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.tests.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

/// @author Ting Sun
/// @author Michael Grafl
class IterableAssert_satisfiesExactlyInAnyOrder_Test {

  private final List<String> actual = List.of("Luke", "Leia", "Yoda");

  @Test
  void should_pass_if_all_consumers_are_satisfied_by_different_elements_in_order() {
    // GIVEN
    Consumer<String> consumer1 = s -> assertThat(s).contains("Luk");
    Consumer<String> consumer2 = s -> assertThat(s).contains("Lei");
    Consumer<String> consumer3 = s -> {
      assertThat(s).hasSize(4);
      assertThat(s).doesNotContain("L");
    }; // Matches "Yoda"
    // WHEN/THEN
    then(actual).satisfiesExactlyInAnyOrder(consumer1, consumer2, consumer3);
  }

  @Test
  void should_pass_if_all_consumers_are_satisfied_by_different_elements_in_any_order() {
    // GIVEN
    Consumer<String> consumer1 = s -> assertThat(s).contains("Y"); // Matches "Yoda"
    Consumer<String> consumer2 = s -> assertThat(s).contains("L"); // Matches "Luke" and "Leia"
    Consumer<String> consumer3 = s -> assertThat(s).doesNotContain("a"); // Matches "Luke"
    // WHEN/THEN
    then(actual).satisfiesExactlyInAnyOrder(consumer1, consumer2, consumer3)
                .satisfiesExactlyInAnyOrder(consumer1, consumer3, consumer2)
                .satisfiesExactlyInAnyOrder(consumer2, consumer1, consumer3)
                .satisfiesExactlyInAnyOrder(consumer2, consumer3, consumer1)
                .satisfiesExactlyInAnyOrder(consumer3, consumer2, consumer1)
                .satisfiesExactlyInAnyOrder(consumer3, consumer1, consumer2);
  }

  @Test
  void should_fail_if_one_of_the_consumer_cannot_be_satisfied() {
    // GIVEN
    Consumer<String> consumer1 = s -> assertThat(s).hasSize(5);
    Consumer<String> consumer2 = s -> assertThat(s).hasSize(4);
    Consumer<String> consumer3 = s -> assertThat(s).hasSize(4);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer1, consumer2,
                                                                                                  consumer3));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyExactlyInAnyOrder(actual).create());
  }

  @Test
  void should_fail_if_no_combination_of_consumers_can_be_satisfied() {
    // GIVEN
    Consumer<String> consumer1 = s -> assertThat(s).contains("Y"); // Matches "Yoda"
    Consumer<String> consumer2 = s -> assertThat(s).contains("o"); // Matches "Yoda"
    Consumer<String> consumer3 = s -> assertThat(s).contains("L"); // Matches "Luke" or "Leia"
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer1, consumer2,
                                                                                                  consumer3));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyExactlyInAnyOrder(actual).create());
  }

  @Test
  void should_fail_if_one_of_the_requirements_cannot_be_satisfied() {
    // GIVEN
    Consumer<String> consumer1 = s -> assertThat(s).isNotEmpty(); // all elements satisfy this
    Consumer<String> consumer2 = s -> assertThat(s).isNotEmpty(); // all elements satisfy this
    Consumer<String> consumer3 = s -> assertThat(s).isBlank(); // no elements satisfy this
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer1, consumer2,
                                                                                                  consumer3));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyExactlyInAnyOrder(actual).create());
  }

  @Test
  void should_pass_if_iterable_contains_same_elements() {
    // GIVEN
    String luke = "Luke";
    List<String> names = List.of(luke, luke);
    Consumer<String> consumer1 = s -> assertThat(s).contains("L");
    Consumer<String> consumer2 = s -> assertThat(s).contains("u");
    // WHEN/THEN
    then(names).satisfiesExactlyInAnyOrder(consumer1, consumer2);
  }

  @Test
  void should_pass_if_both_are_empty() {
    // GIVEN
    List<String> actual = List.of();
    Consumer<String>[] consumers = array();
    // WHEN/THEN
    then(actual).satisfiesExactlyInAnyOrder(consumers);
  }

  @Test
  void should_fail_if_there_are_too_few_consumers() {
    // GIVEN
    Consumer<String>[] consumers = array();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumers));
    // THEN
    then(assertionError).hasMessage(shouldHaveSize(actual, 3, 0).create());
  }

  @Test
  void should_fail_if_there_are_too_many_consumers() {
    // GIVEN
    Consumer<String> consumer = s -> assertThat(s).doesNotContain("z");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer, consumer,
                                                                                                  consumer, consumer));
    // THEN
    then(assertionError).hasMessage(shouldHaveSize(actual, 3, 4).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    Consumer<String> consumer = s -> assertThat(s).hasSize(4);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_consumer_array_is_null() {
    // GIVEN
    Consumer<String>[] consumers = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumers));
    // THEN
    then(npe).hasMessage("The Consumer<? super E>... expressing the assertions must not be null");
  }

  @Test
  void should_fail_if_consumer_var_arg_is_null() {
    // GIVEN
    Consumer<String> consumer = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(npe).hasMessage("Elements in the Consumer<? super E>... expressing the assertions must not be null");
  }

  @Test
  void should_pass_without_relying_on_elements_equality() {
    // GIVEN
    List<Jedi> actual = List.of(new JediOverridingEquals("Luke", "blue"),
                                new JediOverridingEquals("Luke", "green"),
                                new JediOverridingEquals("Luke", "green"));
    Consumer<Jedi> consumer1 = jedi -> assertThat(jedi.lightSaberColor).isEqualTo("green");
    Consumer<Jedi> consumer2 = jedi -> assertThat(jedi.lightSaberColor).isEqualTo("blue");
    Consumer<Jedi> consumer3 = jedi -> assertThat(jedi.lightSaberColor).isEqualTo("green");
    // WHEN/THEN
    then(actual).satisfiesExactlyInAnyOrder(consumer1, consumer2, consumer3);
  }

  private static class JediOverridingEquals extends Jedi {

    private JediOverridingEquals(String name, String lightSaberColor) {
      super(name, lightSaberColor);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      JediOverridingEquals jedi = (JediOverridingEquals) o;
      return getName().equals(jedi.getName());
    }

    @Override
    public int hashCode() {
      return getName().hashCode();
    }

  }

}
