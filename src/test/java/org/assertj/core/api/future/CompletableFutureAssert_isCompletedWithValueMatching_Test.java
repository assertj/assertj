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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.future;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.error.future.ShouldBeCompleted.shouldBeCompleted;
import static org.assertj.core.error.future.Warning.WARNING;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.BaseTest;
import org.junit.jupiter.api.Test;

public class CompletableFutureAssert_isCompletedWithValueMatching_Test extends BaseTest {

  @Test
  public void should_pass_if_completable_future_is_completed() {
    // GIVEN
    CompletableFuture<String> future = completedFuture("done");
    // THEN
    assertThat(future).isCompletedWithValueMatching(result -> result.equals("done"));
  }

  @Test
  public void should_fail_when_completable_future_is_null() {
    // GIVEN
    CompletableFuture<String> future = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(future).isCompletedWithValueMatching(result -> result.equals("done")));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(format(actualIsNull()));
  }

  @Test
  public void should_fail_if_result_does_not_match() {
    // GIVEN
    CompletableFuture<String> future = CompletableFuture.completedFuture("done");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(future).isCompletedWithValueMatching(result -> result.equals("foo"),
                                                                                               "is foo"));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessageContaining("<\"done\">")
                         .hasMessageContaining("to match 'is foo' predicate");
  }

  @Test
  public void should_print_advice_without_description() {
    // GIVEN
    CompletableFuture<String> future = CompletableFuture.completedFuture("done");
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(future).isCompletedWithValueMatching(result -> result.equals("foo")));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessageContaining("<\"done\">")
                         .hasMessageContaining("to match given predicate")
                         .hasMessageContaining("a better error message");
  }

  @Test
  public void should_fail_if_completable_future_is_incomplete() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(future).isCompletedWithValueMatching(result -> result.equals("done")));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldBeCompleted(future).create());
  }

  @Test
  public void should_fail_if_completable_future_has_failed() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException());
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(future).isCompletedWithValueMatching(result -> result.equals("done")));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessageStartingWith(format("%nExpecting%n  <CompletableFuture[Failed: java.lang.RuntimeException]%n"))
                         .hasMessageContaining("Caused by: java.lang.RuntimeException")
                         .hasMessageEndingWith(format("to be completed.%n%s", WARNING));

  }

  @Test
  public void should_fail_if_completable_future_was_cancelled() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.cancel(true);
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(future).isCompletedWithValueMatching(result -> result.equals("done")));
    // THEN
    assertThat(throwable).isInstanceOf(AssertionError.class)
                         .hasMessage(shouldBeCompleted(future).create());
  }
}
