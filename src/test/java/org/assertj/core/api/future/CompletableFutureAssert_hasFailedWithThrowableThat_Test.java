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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.assertj.core.error.future.ShouldHaveFailed.shouldHaveFailed;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.BaseTest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("CompletableFutureAssert hasFailedWithThrowableThat")
public class CompletableFutureAssert_hasFailedWithThrowableThat_Test extends BaseTest {

  @Test
  public void should_pass_if_completable_future_has_failed() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    // WHEN
    future.completeExceptionally(new RuntimeException("some random error"));
    // THEN
    assertThat(future).hasFailedWithThrowableThat()
                      .isInstanceOf(RuntimeException.class)
                      .hasMessage("some random error");
  }

  @Test
  public void should_fail_when_completable_future_is_null() {
    // GIVEN
    CompletableFuture<String> future = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(future).hasFailedWithThrowableThat();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_completable_future_has_failed_with_wrong_throwable() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    RuntimeException exception = new RuntimeException("some random error");
    future.completeExceptionally(exception);
    // WHEN
    ThrowingCallable code = () -> assertThat(future).hasFailedWithThrowableThat().isInstanceOf(IllegalArgumentException.class);
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeInstance(exception, IllegalArgumentException.class).create());
  }

  @Test
  public void should_fail_if_completable_future_is_incomplete() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    // WHEN
    ThrowingCallable code = () -> assertThat(future).hasFailedWithThrowableThat();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldHaveFailed(future).create());
  }

  @Test
  public void should_fail_if_completable_future_is_completed() {
    // GIVEN
    CompletableFuture<String> future = CompletableFuture.completedFuture("done");
    // WHEN
    ThrowingCallable code = () -> assertThat(future).hasFailedWithThrowableThat();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldHaveFailed(future).create());
  }

  @Test
  public void should_fail_if_completable_future_was_cancelled() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.cancel(true);
    // WHEN
    ThrowingCallable code = () -> assertThat(future).hasFailedWithThrowableThat();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldHaveFailed(future).create());
  }
}
