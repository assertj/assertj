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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.future;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.util.AssertionsUtil;
import org.junit.jupiter.api.Test;

public class CompletableFutureAssert_willCompleteWithValueMatching_Test extends AbstractFutureTest {

  @Test
  void should_pass_on_already_completed_future_with_matching_result() {

    // GIVEN
    CompletableFuture<String> future = CompletableFuture.completedFuture("string");

    // WHEN/THEN
    Assertions.assertThat(future).willCompleteWithValueMatching(Duration.ofSeconds(1L), s -> s.length() == 6);
  }

  @Test
  void should_fail_on_already_exceptionally_completed_future() {

    // GIVEN
    CompletableFuture<String> future = new CompletableFuture<>();
    future.completeExceptionally(new Exception());

    // WHEN
    ThrowingCallable t = () -> Assertions.assertThat(future).willCompleteWithValueMatching(Duration.ofSeconds(1L),
                                                                                           s -> s.length() == 5);

    // THEN
    AssertionsUtil.assertThatAssertionErrorIsThrownBy(t);
  }

  @Test
  void should_fail_on_already_completed_future_with_non_matching_result() {

    // GIVEN
    CompletableFuture<String> future = CompletableFuture.completedFuture("string");

    // WHEN
    ThrowingCallable t = () -> Assertions.assertThat(future).willCompleteWithValueMatching(Duration.ofSeconds(1L),
                                                                                           s -> s.length() == 5);

    // THEN
    AssertionsUtil.assertThatAssertionErrorIsThrownBy(t);
  }

  @Test
  void should_fail_on_pending_future_that_will_not_complete_in_provided_period() {

    // GIVEN
    CompletableFuture<String> future = completedFutureAfter("string", Duration.ofSeconds(1), executorService);

    // WHEN
    ThrowingCallable t = () -> Assertions.assertThat(future).willCompleteWithValueMatching(Duration.ofMillis(10),
                                                                                           s -> s.length() == 6);

    // THEN
    AssertionsUtil.assertThatAssertionErrorIsThrownBy(t);
  }

  @Test
  void should_fail_on_pending_future_that_will_complete_in_provided_period_but_with_wrong_value() {

    // GIVEN
    CompletableFuture<String> future = completedFutureAfter("string", Duration.ofMillis(10), executorService);

    // WHEN
    ThrowingCallable t = () -> Assertions.assertThat(future).willCompleteWithValueMatching(Duration.ofMillis(100),
                                                                                           s -> s.length() == 5);

    // THEN
    AssertionsUtil.assertThatAssertionErrorIsThrownBy(t);
  }

  @Test
  void should_pass_on_pending_future_that_will_complete_in_provided_period_with_satisfying_value() {

    // GIVEN
    CompletableFuture<String> future = completedFutureAfter("string", Duration.ofMillis(10), executorService);

    // WHEN/THEN
    Assertions.assertThat(future).willCompleteWithValueMatching(Duration.ofMillis(100), s -> s.length() == 6);
  }
}
