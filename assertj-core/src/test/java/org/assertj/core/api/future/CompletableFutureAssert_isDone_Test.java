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
package org.assertj.core.api.future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.future.ShouldBeDone.shouldBeDone;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class CompletableFutureAssert_isDone_Test {

  @Test
  void should_pass_if_completable_future_is_done() {
    // GIVEN
    CompletableFuture<String> future = CompletableFuture.completedFuture("done");
    // THEN
    assertThat(future).isDone();
  }

  @Test
  void should_fail_when_completable_future_is_null() {
    // GIVEN
    CompletableFuture<String> future = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(future).isDone();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_completable_future_is_not_done() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    // WHEN
    ThrowingCallable code = () -> assertThat(future).isDone();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(shouldBeDone(future).create());
  }
}
