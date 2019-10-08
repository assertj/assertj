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

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.future.Warning.WARNING;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.BaseTest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

public class CompletableFutureAssert_isNotCompletedExceptionally_Test extends BaseTest {

  @Test
  public void should_pass_if_completable_future_is_not_completed_exceptionally() {
    // GIVEN
    CompletableFuture<String> completedFuture = completedFuture("done");
    // THEN
    assertThat(completedFuture).isNotCompletedExceptionally();
  }

  @Test
  public void should_fail_when_completable_future_is_null() {
    // GIVEN
    CompletableFuture<String> future = null;
    // WHEN
    ThrowingCallable code = () -> assertThat(future).isNotCompletedExceptionally();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessageStartingWith(actualIsNull());
  }

  @Test
  public void should_fail_if_completable_future_is_completed_exceptionally() {
    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException());
    // WHEN
    ThrowingCallable code = () -> assertThat(future).isNotCompletedExceptionally();
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessageStartingWith(format("%nExpecting%n  <CompletableFuture[Failed: java.lang.RuntimeException]%n"))
                                            .withMessageContaining("Caused by: java.lang.RuntimeException")
                                            .withMessageEndingWith("to not be completed exceptionally.%n%s", WARNING);
  }
}
