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
package org.assertj.core.api.future;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.future.FutureTestHelper.futureCompletingAfter;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

class CompletableFutureAssert_completesExceptionallyWithin_Test extends AbstractFutureTest {

  private static final Duration ONE_SECOND = Duration.ofSeconds(1);

  @Test
  void should_fail_when_future_does_not_complete_within_timeout_Duration() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(ONE_SECOND, executorService);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).completesExceptionallyWithin(Duration.ofMillis(50)));
    // THEN
    then(assertionError).hasMessageContainingAll("Incomplete", "to have completed exceptionally within 0.05S.");
  }

  @Test
  void should_fail_when_future_does_not_complete_within_timeout() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(ONE_SECOND, executorService);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).completesExceptionallyWithin(50, MILLISECONDS));
    // THEN
    then(assertionError).hasMessageContainingAll("Incomplete", "to have completed exceptionally within 50L MILLISECONDS.");
  }

  @Test
  void should_fail_if_future_completes_within_given_timeout() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(Duration.ofMillis(10), executorService);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).completesExceptionallyWithin(500,
                                                                                                               MILLISECONDS));
    // THEN
    then(assertionError).hasMessageContainingAll("Completed", "to have completed exceptionally within 500L MILLISECONDS.");
  }

  @Test
  void should_fail_if_future_completes_within_given_timeout_Duration() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(Duration.ofMillis(10), executorService);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).completesExceptionallyWithin(Duration.ofMillis(500)));
    // THEN
    then(assertionError).hasMessageContainingAll("Completed", "to have completed exceptionally within 0.5S.");
  }

  @Test
  void should_pass_if_future_is_cancelled() {
    // GIVEN
    CompletableFuture<Void> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN/THEN
    then(future).completesExceptionallyWithin(1, MILLISECONDS);
    then(future).completesExceptionallyWithin(Duration.ofMillis(1));
  }

  @Test
  void should_pass_if_future_execution_fails() {
    // GIVEN
    CompletableFuture<Void> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException("boom!"));
    // WHEN/THEN
    then(future).completesExceptionallyWithin(1, MILLISECONDS)
                .withThrowableOfType(ExecutionException.class)
                .withMessageContaining("boom!");
    then(future).completesExceptionallyWithin(Duration.ofMillis(1))
                .withThrowableThat()
                .isInstanceOf(ExecutionException.class)
                .withMessageContaining("boom!");
  }

  @Test
  void should_fail_when_wait_is_interrupted() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(ONE_SECOND, executorService);
    // WHEN
    Thread.currentThread().interrupt();
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).completesExceptionallyWithin(2, SECONDS));
    // THEN
    then(assertionError).hasMessageContainingAll("Incomplete", "to have completed exceptionally within 2L SECONDS.");
  }

  @Test
  void should_fail_when_future_is_null() {
    // GIVEN
    CompletableFuture<Void> future = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).completesExceptionallyWithin(1, MILLISECONDS));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
