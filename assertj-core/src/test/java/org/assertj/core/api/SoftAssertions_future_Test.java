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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Soft assertions on futures")
class SoftAssertions_future_Test extends BaseAssertionsTest {

  private static final Duration TEN_MILLIS = Duration.ofMillis(10);
  private SoftAssertions softly;

  @BeforeEach
  void setup() {
    softly = new SoftAssertions();
  }

  @SuppressWarnings("deprecation")
  @Test
  void should_work_with_CompletionStage() {
    // GIVEN
    CompletionStage<String> completionStage = completedFuture("done");
    // WHEN
    softly.assertThat(completionStage).isDone();
    softly.assertThat(completionStage).hasNotFailed();
    softly.assertThat(completionStage).isCancelled();
    completionStage = null;
    softly.assertThat(completionStage).isNull();
    // THEN
    assertThat(softly.errorsCollected()).singleElement(as(THROWABLE))
                                        .hasMessageContaining("cancelled");
  }

  @Test
  void should_not_collect_AssertionError_from_CompletableFuture_succeedsWithin() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).succeedsWithin(10, MILLISECONDS));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(assertionError).hasMessageContaining("Cancelled");
  }

  @Test
  void should_not_collect_AssertionError_from_CompletableFuture_succeedsWithin_asString() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).succeedsWithin(10, MILLISECONDS,
                                                                                                        as(STRING)));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(assertionError).hasMessageContaining("Cancelled");
  }

  @Test
  void should_not_collect_AssertionError_from_CompletableFuture_succeedsWithin_Duration() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).succeedsWithin(TEN_MILLIS));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(assertionError).hasMessageContaining("Cancelled");
  }

  @Test
  void should_not_collect_AssertionError_from_CompletableFuture_succeedsWithin_Duration_asString() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).succeedsWithin(TEN_MILLIS,
                                                                                                        as(STRING)));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(assertionError).hasMessageContaining("Cancelled");
  }

  @Test
  void should_not_collect_AssertionError_from_Future_succeedsWithin() {
    // GIVEN
    Future<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).succeedsWithin(10, HOURS));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(assertionError).hasMessageContaining("Cancelled");
  }

  @Test
  void should_not_collect_AssertionError_from_Future_succeedsWithin_asString() {
    // GIVEN
    Future<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).succeedsWithin(10, HOURS, as(STRING)));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(assertionError).hasMessageContaining("Cancelled");
  }

  @Test
  void should_not_collect_AssertionError_from_Future_succeedsWithin_Duration() {
    // GIVEN
    Future<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).succeedsWithin(TEN_MILLIS));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(assertionError).hasMessageContaining("Cancelled");
  }

  @Test
  void should_not_collect_AssertionError_from_Future_succeedsWithin_Duration_asString() {
    // GIVEN
    Future<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).succeedsWithin(TEN_MILLIS, as(STRING)));
    // THEN
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(assertionError).hasMessageContaining("Cancelled");
  }

  @Test
  void should_not_collect_AssertionError_from_Future_failsWithin() {
    // GIVEN
    Future<String> future = completedFuture("done");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).failsWithin(10, MILLISECONDS));
    // THEN
    then(softly.errorsCollected()).isEmpty();
    then(assertionError).hasMessageContaining("to have failed within 10L MILLISECONDS.");
  }

  @Test
  void should_not_collect_AssertionError_from_Future_failsWithin_Duration() {
    // GIVEN
    Future<String> future = completedFuture("done");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).failsWithin(TEN_MILLIS));
    // THEN
    then(softly.errorsCollected()).isEmpty();
    then(assertionError).hasMessageContaining("to have failed within 0.01S.");
  }

  @Test
  void should_not_collect_AssertionError_from_CompletableFuture_failsWithin() {
    // GIVEN
    CompletableFuture<String> future = completedFuture("done");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).failsWithin(10, MILLISECONDS));
    // THEN
    then(softly.errorsCollected()).isEmpty();
    then(assertionError).hasMessageContaining("to have failed within 10L MILLISECONDS.");
  }

  @Test
  void should_not_collect_AssertionError_from_CompletableFuture_failsWithin_Duration() {
    // GIVEN
    CompletableFuture<String> future = completedFuture("done");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> softly.assertThat(future).failsWithin(TEN_MILLIS));
    // THEN
    then(softly.errorsCollected()).isEmpty();
    then(assertionError).hasMessageContaining("to have failed within 0.01S.");
  }

  @Test
  void should_only_collect_error_from_chained_assertions_performed_after_CompletableFuture_succeedsWithin() {
    // GIVEN
    CompletableFuture<String> completableFuture = completedFuture("done");
    // WHEN
    softly.assertThat(completableFuture)
          .succeedsWithin(20, MILLISECONDS)
          .isEqualTo("not done 20ms")
          .isEqualTo("not ok 20ms");
    softly.assertThat(completableFuture)
          .succeedsWithin(20, MILLISECONDS, as(STRING))
          .contains("not 20ms")
          .containsIgnoringCase("NOT 20ms");
    softly.assertThat(completableFuture)
          .succeedsWithin(TEN_MILLIS)
          .isEqualTo("not done 10ms")
          .isEqualTo("not ok 10ms");
    softly.assertThat(completableFuture)
          .succeedsWithin(TEN_MILLIS, as(STRING))
          .contains("not 10ms")
          .containsIgnoringCase("NOT 10ms");
    // THEN
    assertThat(softly.errorsCollected()).hasSize(8);
    assertThat(softly.errorsCollected().get(0)).hasMessageContaining("not done 20ms");
    assertThat(softly.errorsCollected().get(1)).hasMessageContaining("not ok 20ms");
    assertThat(softly.errorsCollected().get(2)).hasMessageContaining("not 20ms");
    assertThat(softly.errorsCollected().get(3)).hasMessageContaining("NOT 20ms");
    assertThat(softly.errorsCollected().get(4)).hasMessageContaining("not done 10ms");
    assertThat(softly.errorsCollected().get(5)).hasMessageContaining("not ok 10ms");
    assertThat(softly.errorsCollected().get(6)).hasMessageContaining("not 10ms");
    assertThat(softly.errorsCollected().get(7)).hasMessageContaining("NOT 10ms");
  }

  @Test
  void should_only_collect_error_from_chained_assertions_performed_after_Future_succeedsWithin() {
    // GIVEN
    Future<String> future = completedFuture("done");
    // WHEN
    softly.assertThat(future)
          .succeedsWithin(20, MILLISECONDS)
          .isEqualTo("not done 20ms")
          .isEqualTo("not ok 20ms");
    softly.assertThat(future)
          .succeedsWithin(20, MILLISECONDS, as(STRING))
          .contains("not 20ms")
          .containsIgnoringCase("NOT 20ms");
    softly.assertThat(future)
          .succeedsWithin(TEN_MILLIS)
          .isEqualTo("not done 10ms")
          .isEqualTo("not ok 10ms");
    softly.assertThat(future)
          .succeedsWithin(TEN_MILLIS, as(STRING))
          .contains("not 10ms")
          .containsIgnoringCase("NOT 10ms");
    // THEN
    assertThat(softly.errorsCollected()).hasSize(8);
    assertThat(softly.errorsCollected().get(0)).hasMessageContaining("not done 20ms");
    assertThat(softly.errorsCollected().get(1)).hasMessageContaining("not ok 20ms");
    assertThat(softly.errorsCollected().get(2)).hasMessageContaining("not 20ms");
    assertThat(softly.errorsCollected().get(3)).hasMessageContaining("NOT 20ms");
    assertThat(softly.errorsCollected().get(4)).hasMessageContaining("not done 10ms");
    assertThat(softly.errorsCollected().get(5)).hasMessageContaining("not ok 10ms");
    assertThat(softly.errorsCollected().get(6)).hasMessageContaining("not 10ms");
    assertThat(softly.errorsCollected().get(7)).hasMessageContaining("NOT 10ms");
  }

  @Test
  void should_propagate_AssertionError_from_nested_proxied_calls() {
    // nested proxied call to isCompleted
    softly.assertThat(new CompletableFuture<String>()).isCompletedWithValue("done");
    // it must be caught by softly.assertAll()
    assertThat(softly.errorsCollected()).hasSize(1);
  }

}
