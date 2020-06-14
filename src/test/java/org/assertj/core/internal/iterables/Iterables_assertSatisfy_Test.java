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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertSatisfy(AssertionInfo, Iterable, Consumer[])}</code>.
 *
 * @author Ting Sun
 */
@DisplayName("Iterables assertSatisfy")
public class Iterables_assertSatisfy_Test extends IterablesBaseTest {

  private List<String> actual = newArrayList("Luke", "Leia", "Yoda");

  @Test
  public void should_pass_if_there_is_only_one_consumer_and_can_be_satisfied() {
    // GIVEN
    Consumer<String> consumer = s -> assertThat(s).hasSize(4);
    // WHEN/THEN
    iterables.assertSatisfy(info, actual, consumer);
  }

  @Test
  public void should_pass_if_there_are_multiple_consumers_and_can_be_satisfied() {
    // GIVEN
    Consumer<String> consumer1 = s -> {
      assertThat(s).hasSize(4);
      assertThat(s).doesNotContain("L");
    };
    Consumer<String> consumer2 = s -> assertThat(s).doesNotContain("a");

    // WHEN/THEN
    iterables.assertSatisfy(info, actual, consumer1, consumer2);
  }

  @Test
  public void should_pass_there_are_multiple_consumers_and_can_be_satisfied_in_any_order() {
    // GIVEN
    Consumer<String> consumer1 = s -> assertThat(s).contains("L"); // Matches "Luke" and "Leia"
    Consumer<String> consumer2 = s -> assertThat(s).doesNotContain("a"); // Matches "Luke"

    // WHEN/THEN
    iterables.assertSatisfy(info, actual, consumer1, consumer2);
  }

  @Test
  public void should_pass_if_iterable_contains_multiple_equal_elements() {
    // GIVEN
    List<String> names = newArrayList("Luke", "Luke");
    Consumer<String> consumer1 = s -> assertThat(s).contains("L");
    Consumer<String> consumer2 = s -> assertThat(s).contains("u");

    // WHEN/THEN
    iterables.assertSatisfy(info, names, consumer1, consumer2);
  }

  @Test
  public void should_pass_if_there_is_no_consumer() {
    // WHEN/THEN
    iterables.assertSatisfy(info, actual);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    Consumer<String> consumer = s -> assertThat(s).hasSize(4);

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).satisfy(consumer));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_throw_error_if_consumer_array_is_null() {
    // GIVEN
    Consumer<String>[] consumers = null;
    String message = "The Consumer<? super E>... expressing the assertions consumers must not be null";

    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).satisfy(consumers))
                                    .withMessage(message);
  }

  @Test
  public void should_fail_if_consumer_var_arg_is_null() {
    // GIVEN
    Consumer<String> consumer = null;
    String message = "The element in consumers must not be null";

    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(actual).satisfy(consumer))
                                    .withMessage(message);
  }

  @Test
  public void should_fail_if_there_is_only_one_consumer_and_cannot_be_satisfied() {
    // GIVEN
    Consumer<String> consumer = s -> assertThat(s).hasSize(5);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfy(info, actual, consumer));
    // THEN
    then(assertionError).hasMessage(shouldSatisfy(actual).create());
  }

  @Test
  public void should_fail_if_there_are_multiple_consumers_and_cannot_be_all_satisfied() {
    // GIVEN
    Consumer<String> consumer1 = s -> assertThat(s).startsWith("Y");
    Consumer<String> consumer2 = s -> assertThat(s).contains("o");

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfy(info, actual, consumer1, consumer2));
    // THEN
    then(assertionError).hasMessage(shouldSatisfy(actual).create());
  }

  @Test
  public void should_fail_if_consumers_are_more_than_elements() {
    // GIVEN
    Consumer<String> consumer = s -> assertThat(s).doesNotContain("z");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfy(info, actual, consumer, consumer, consumer,
                                                                                       consumer));
    // THEN
    then(assertionError).hasMessage(shouldSatisfy(actual).create());
  }

}
