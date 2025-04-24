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
import static org.assertj.core.error.ShouldSatisfyOnlyOnce.shouldSatisfyOnlyOnce;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertSatisfiesOnlyOnce(AssertionInfo, Iterable, Consumer)}</code>.
 *
 * @author Stefan Bratanov
 */
class Iterables_assertSatisfiesOnlyOnce_Test extends IterablesBaseTest {

  private static final Consumer<String> REQUIREMENTS = value -> assertThat(value).isEqualTo("Luke");

  @Test
  void should_pass_if_only_one_actual_elements_satisfies_the_requirements() {
    iterables.assertSatisfiesOnlyOnce(INFO, actual, REQUIREMENTS);
  }

  @Test
  void should_fail_if_more_than_once_actual_elements_satisfy_the_requirements() {
    // GIVEN
    actual.add("Luke");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesOnlyOnce(INFO, actual, REQUIREMENTS));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyOnlyOnce(actual, List.of("Luke", "Luke")).create());
  }

  @Test
  void should_fail_if_no_actual_elements_satisfy_the_requirements() {
    // GIVEN
    Consumer<String> requirements = value -> assertThat(value).isEqualTo("Vader");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesOnlyOnce(INFO, actual, requirements));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyOnlyOnce(actual, List.of()).create());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    actual = List.of();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesOnlyOnce(INFO, actual, REQUIREMENTS));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyOnlyOnce(List.of(), List.of()).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertSatisfiesOnlyOnce(INFO, actual, REQUIREMENTS));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_consumer_is_null() {
    // GIVEN
    Consumer<String> requirements = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> iterables.assertSatisfiesOnlyOnce(INFO, actual, requirements))
                                    .withMessage("The Consumer<? super E> expressing the requirements must not be null");
  }

}
