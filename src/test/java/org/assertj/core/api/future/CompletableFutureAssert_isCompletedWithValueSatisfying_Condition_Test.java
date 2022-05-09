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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.future;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBe.shouldBe;
import static org.assertj.core.error.future.ShouldBeCompleted.shouldBeCompleted;
import static org.assertj.core.error.future.Warning.WARNING;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.assertj.core.error.ShouldBe;
import org.junit.jupiter.api.Test;

class CompletableFutureAssert_isCompletedWithValueSatisfying_Condition_Test {

  private final Condition<String> passingCondition = new TestCondition<>(true);
  private final Condition<String> notPassingCondition = new TestCondition<>();

  @Test
  void should_fail_when_completable_future_is_null() {
    // GIVEN
    CompletableFuture<String> future = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfying(passingCondition));
    // THEN
    assertThat(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_completable_future_is_completed_and_condition_satisfied() {
    // GIVEN
    CompletableFuture<String> future = completedFuture("done");
    // THEN
    assertThat(future).isCompletedWithValueSatisfying(passingCondition);
  }

  @Test
  void should_fail_if_condition_not_satisfied() {
    // GIVEN
    CompletableFuture<String> future = CompletableFuture.completedFuture("done");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfying(notPassingCondition));
    // THEN
    assertThat(assertionError).hasMessage(shouldBe("done", notPassingCondition).create());
  }

  @Test
  void should_fail_if_completable_future_is_incomplete() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfying(passingCondition));
    // THEN
    assertThat(assertionError).hasMessage(shouldBeCompleted(future).create());
  }

  @Test
  void should_fail_if_completable_future_has_failed() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException());
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfying(passingCondition));
    // THEN
    assertThat(assertionError).hasMessageStartingWith(format("%nExpecting%n  <CompletableFuture[Failed with the following stack trace:%njava.lang.RuntimeException"))
                              .hasMessageEndingWith("to be completed.%n%s", WARNING);

  }

  @Test
  void should_fail_if_completable_future_was_cancelled() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.cancel(true);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfying(passingCondition));
    // THEN
    assertThat(assertionError).hasMessage(shouldBeCompleted(future).create());
  }
}
