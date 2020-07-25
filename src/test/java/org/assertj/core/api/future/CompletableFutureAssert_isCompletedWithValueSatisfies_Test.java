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

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.future.ShouldBeCompleted.shouldBeCompleted;
import static org.assertj.core.error.future.Warning.WARNING;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

class CompletableFutureAssert_isCompletedWithValueSatisfies_Test {

  private static final Consumer<String> IS_DONE = result -> assertThat(result).isEqualTo("done");

  @Test
  void should_pass_if_completable_future_is_completed() {
    // GIVEN
    CompletableFuture<String> future = completedFuture("done");
    // THEN
    assertThat(future).isCompletedWithValueSatisfies(IS_DONE);
  }

  @Test
  void should_fail_when_completable_future_is_null() {
    // GIVEN
    CompletableFuture<String> future = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfies(IS_DONE));
    // THEN
    assertThat(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_result_does_not_match() {
    // GIVEN
    CompletableFuture<String> future = CompletableFuture.completedFuture("foo");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfies(IS_DONE));
    // THEN
    assertThat(assertionError).hasMessageContaining("<\"done\">")
                              .hasMessageContaining("<\"foo\">");
  }

  @Test
  void should_fail_if_completable_future_is_incomplete() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfies(IS_DONE));
    // THEN
    assertThat(assertionError).hasMessage(shouldBeCompleted(future).create());
  }

  @Test
  void should_fail_if_completable_future_has_failed() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException());
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfies(IS_DONE));
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
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).isCompletedWithValueSatisfies(IS_DONE));
    // THEN
    assertThat(assertionError).hasMessage(shouldBeCompleted(future).create());
  }
}
