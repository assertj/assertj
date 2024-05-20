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

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;

public class CompletableFutureAssert_isCompletedWithValueMatchingWithin_Test extends AbstractFutureTest {

  private final Duration ONE_SECOND = Duration.ofSeconds(1L);
  private final Duration TEN_MS = Duration.ofMillis(10L);
  private final Duration FIFTY_MS = Duration.ofMillis(50L);

  @Test
  void should_pass_on_already_completed_future_with_matching_result() {
    // GIVEN
    CompletableFuture<String> future = completedFuture("string");
    // WHEN/THEN
    then(future).isCompletedWithValueMatchingWithin(s -> s.length() == 6, ONE_SECOND);
  }

  @Test
  void should_pass_on_pending_future_that_will_complete_in_provided_period_with_satisfying_value() {
    // GIVEN
    CompletableFuture<String> future = completedFutureAfter("string", TEN_MS, executorService);
    // WHEN/THEN
    then(future).isCompletedWithValueMatchingWithin(s -> s.length() == 6, Duration.ofMillis(100));
  }

  @Test
  void should_fail_on_already_exceptionally_completed_future() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.completeExceptionally(new Exception());
    // WHEN/THEN
    expectAssertionError(() -> assertThat(future).isCompletedWithValueMatchingWithin(s -> s.length() == 5, ONE_SECOND));
  }

  @Test
  void should_fail_on_already_completed_future_with_non_matching_result() {
    // GIVEN
    CompletableFuture<String> future = completedFuture("string");
    // WHEN/THEN
    expectAssertionError(() -> assertThat(future).isCompletedWithValueMatchingWithin(s -> s.length() == 5, ONE_SECOND));
  }

  @Test
  void should_fail_on_pending_future_that_will_not_complete_in_provided_period() {
    // GIVEN
    CompletableFuture<String> future = completedFutureAfter("123456", Duration.ofSeconds(1), executorService);
    // WHEN/THEN
    expectAssertionError(() -> assertThat(future).isCompletedWithValueMatchingWithin(s -> s.length() == 6, TEN_MS));
  }

  @Test
  void should_fail_on_pending_future_that_will_complete_in_provided_period_but_with_wrong_value() {
    // GIVEN
    CompletableFuture<String> future = completedFutureAfter("string", TEN_MS, executorService);
    // WHEN/THEN
    expectAssertionError(() -> assertThat(future).isCompletedWithValueMatchingWithin(s -> s.length() == 5, FIFTY_MS));
  }

  @Test
  void should_fail_when_future_is_null() {
    // GIVEN
    CompletableFuture<String> future = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueMatchingWithin(s -> s.length() == 6,
                                                                                                                     TEN_MS));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }
}
