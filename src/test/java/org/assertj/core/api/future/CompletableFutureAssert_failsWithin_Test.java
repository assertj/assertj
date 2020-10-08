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
package org.assertj.core.api.future;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.junit.jupiter.api.condition.OS.MAC;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

@DisplayName("FutureAssert failsWithin")
class CompletableFutureAssert_failsWithin_Test {

  private static final Duration ONE_SECOND = Duration.ofSeconds(1);

  @Test
  void should_pass_when_future_does_not_complete_within_timeout_Duration() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(ONE_SECOND);
    // WHEN/THEN
    assertThat(future).failsWithin(Duration.ofMillis(50));
  }

  @Test
  void should_pass_when_future_does_not_complete_within_timeout() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(ONE_SECOND);
    // WHEN/THEN
    assertThat(future).failsWithin(50, MILLISECONDS);
  }

  @Test
  void should_allow_assertion_on_future_exception_when_future_did_not_complete_within_timeout_Duration() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(ONE_SECOND);
    // WHEN/THEN
    assertThat(future).failsWithin(Duration.ofMillis(50))
                      .withThrowableOfType(TimeoutException.class)
                      .withMessage(null);
  }

  @Test
  void should_allow_assertion_on_future_exception_when_future_did_not_complete_within_timeout() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(ONE_SECOND);
    // WHEN/THEN
    assertThat(future).failsWithin(50, MILLISECONDS)
                      .withThrowableOfType(TimeoutException.class)
                      .withMessage(null);
  }

  @DisabledOnOs(MAC) // Fails only on the Mac runner of GitHub Actions, but succeeds locally
  @Test
  void should_fail_if_future_completes_within_given_timeout() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(Duration.ofMillis(10));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).failsWithin(500, MILLISECONDS));
    // THEN
    then(assertionError).hasMessageContainingAll("Completed", "to have failed within 500L MILLISECONDS.");
  }

  @DisabledOnOs(MAC) // Fails only on the Mac runner of GitHub Actions, but succeeds locally
  @Test
  void should_fail_if_future_completes_within_given_timeout_Duration() {
    // GIVEN
    CompletableFuture<Void> future = futureCompletingAfter(Duration.ofMillis(10));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).failsWithin(Duration.ofMillis(500)));
    // THEN
    then(assertionError).hasMessageContainingAll("Completed", "to have failed within 0.5S.");
  }

  @Test
  void should_pass_if_future_is_cancelled() {
    // GIVEN
    CompletableFuture<Void> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN/THEN
    then(future).failsWithin(1, MILLISECONDS);
    then(future).failsWithin(Duration.ofMillis(1));
  }

  @Test
  void should_pass_if_future_execution_fails() {
    // GIVEN
    CompletableFuture<Void> completableFuture = new CompletableFuture<>();
    completableFuture.completeExceptionally(new RuntimeException("boom!"));
    CompletableFuture<Void> future = completableFuture;
    // WHEN/THEN
    then(future).failsWithin(1, MILLISECONDS)
                .withThrowableOfType(ExecutionException.class)
                .withMessageContaining("boom!");
    then(future).failsWithin(Duration.ofMillis(1))
                .withThrowableOfType(ExecutionException.class)
                .withMessageContaining("boom!");
  }

  @Test
  void should_fail_when_future_is_null() {
    // GIVEN
    CompletableFuture<Void> future = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).failsWithin(1, MILLISECONDS));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  private static CompletableFuture<Void> futureCompletingAfter(Duration duration) {
    return CompletableFuture.runAsync(() -> sleep(duration));
  }

  private static void sleep(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (@SuppressWarnings("unused") InterruptedException e) {
      // do nothing
    }
  }

}
