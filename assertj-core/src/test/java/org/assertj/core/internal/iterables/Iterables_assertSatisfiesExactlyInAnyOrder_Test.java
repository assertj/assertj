/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfyExactlyInAnyOrder;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.internal.IterablesBaseTest;
import org.assertj.core.testkit.Jedi;
import org.junit.jupiter.api.Test;

/**
 * @author Ting Sun
 */
class Iterables_assertSatisfiesExactlyInAnyOrder_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Leia", "Yoda");

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
    iterables.assertSatisfiesExactlyInAnyOrder(info, actual, array(consumer1, consumer2, consumer3));
  }

  @Test
  void should_pass_if_all_consumers_are_satisfied_by_different_elements_in_any_order() {
    // GIVEN
    Consumer<String> consumer1 = s -> assertThat(s).contains("Y"); // Matches "Yoda"
    Consumer<String> consumer2 = s -> assertThat(s).contains("L"); // Matches "Luke" and "Leia"
    Consumer<String> consumer3 = s -> assertThat(s).doesNotContain("a"); // Matches "Luke"
    // WHEN/THEN
    iterables.assertSatisfiesExactlyInAnyOrder(info, actual, array(consumer1, consumer2, consumer3));
    iterables.assertSatisfiesExactlyInAnyOrder(info, actual, array(consumer1, consumer3, consumer2));
    iterables.assertSatisfiesExactlyInAnyOrder(info, actual, array(consumer2, consumer1, consumer3));
    iterables.assertSatisfiesExactlyInAnyOrder(info, actual, array(consumer2, consumer3, consumer1));
    iterables.assertSatisfiesExactlyInAnyOrder(info, actual, array(consumer3, consumer2, consumer1));
    iterables.assertSatisfiesExactlyInAnyOrder(info, actual, array(consumer3, consumer1, consumer2));
  }

  @Test
  void should_fail_if_one_of_the_consumer_cannot_be_satisfied() {
    // GIVEN
    Consumer<String> consumer1 = s -> assertThat(s).hasSize(5);
    Consumer<String> consumer2 = s -> assertThat(s).hasSize(4);
    Consumer<String> consumer3 = s -> assertThat(s).hasSize(4);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesExactlyInAnyOrder(info, actual,
                                                                                                          array(consumer1,
                                                                                                                consumer2,
                                                                                                                consumer3)));
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
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesExactlyInAnyOrder(info, actual,
                                                                                                          array(consumer1,
                                                                                                                consumer2,
                                                                                                                consumer3)));
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
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesExactlyInAnyOrder(info, actual,
                                                                                                          array(consumer1,
                                                                                                                consumer2,
                                                                                                                consumer3)));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyExactlyInAnyOrder(actual).create());
  }

  @Test
  void should_pass_if_iterable_contains_same_elements() {
    // GIVEN
    String luke = "Luke";
    List<String> names = newArrayList(luke, luke);
    Consumer<String> consumer1 = s -> assertThat(s).contains("L");
    Consumer<String> consumer2 = s -> assertThat(s).contains("u");
    // WHEN/THEN
    iterables.assertSatisfiesExactlyInAnyOrder(info, names, array(consumer1, consumer2));
  }

  @Test
  void should_pass_if_both_are_empty() {
    // WHEN/THEN
    iterables.assertSatisfiesExactlyInAnyOrder(info, newArrayList(), array());
  }

  @Test
  void should_fail_if_there_are_too_few_consumers() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesExactlyInAnyOrder(info, actual, array()));
    // THEN
    then(assertionError).hasMessage(shouldHaveSize(actual, 3, 0).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    Consumer<String> consumer = s -> assertThat(s).hasSize(4);

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_consumer_array_is_null() {
    // GIVEN
    Consumer<String>[] consumers = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumers))
                                    .withMessage("The Consumer<? super E>... expressing the assertions must not be null");
  }

  @Test
  void should_fail_if_consumer_var_arg_is_null() {
    // GIVEN
    Consumer<String> consumer = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer))
                                    .withMessage("Elements in the Consumer<? super E>... expressing the assertions must not be null");
  }

  @Test
  void should_fail_if_there_are_too_many_consumers() {
    // GIVEN
    Consumer<String> consumer = s -> assertThat(s).doesNotContain("z");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesExactlyInAnyOrder(info, actual,
                                                                                                          array(consumer,
                                                                                                                consumer,
                                                                                                                consumer,
                                                                                                                consumer)));
    // THEN
    then(assertionError).hasMessage(shouldHaveSize(actual, 3, 4).create());
  }

  @Test
  void should_pass_without_relying_on_elements_equality() {
    // GIVEN
    List<Jedi> actual = newArrayList(new JediOverridingEquals("Luke", "blue"),
                                     new JediOverridingEquals("Luke", "green"),
                                     new JediOverridingEquals("Luke", "green"));
    Consumer<Jedi>[] consumers = array(jedi -> assertThat(jedi.lightSaberColor).isEqualTo("green"),
                                       jedi -> assertThat(jedi.lightSaberColor).isEqualTo("blue"),
                                       jedi -> assertThat(jedi.lightSaberColor).isEqualTo("green"));
    // WHEN/THEN
    iterables.assertSatisfiesExactlyInAnyOrder(info, actual, consumers);
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
