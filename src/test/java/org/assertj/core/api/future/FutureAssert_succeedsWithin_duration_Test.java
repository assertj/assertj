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

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FutureAssert succeedsWithin(Duration)")
public class FutureAssert_succeedsWithin_duration_Test {

  @Test
  public void should_allow_assertion_on_future_result_when_completed_normally() {
    // GIVEN
    String value = "done";
    Future<String> future = completedFuture(value);
    // WHEN/THEN
    assertThat(future).succeedsWithin(Duration.ofMillis(1))
                      .isEqualTo(value);
  }

  @Test
  public void should_allow_assertion_on_future_result_when_completed_normally_within_timeout() {
    // GIVEN
    String value = "done";
    int sleepDuration = 10;
    Future<String> future = futureAfter(value, sleepDuration);
    // WHEN/THEN
    // using the same duration would fail depending on when the thread executing the future is started
    assertThat(future).succeedsWithin(Duration.ofMillis(sleepDuration + 100))
                      .isEqualTo(value);
  }

  @Test
  public void should_allow_narrowed_assertion_on_future_result() {
    // GIVEN
    String value = "done";
    Future<String> future = completedFuture(value);
    // WHEN/THEN
    assertThat(future).succeedsWithin(Duration.ofMillis(1), as(STRING))
                      .startsWith("don");
  }

  @Test
  public void should_fail_if_future_does_not_succeed_within_given_timeout() {
    // GIVEN
    int sleepDuration = 100_000;
    Future<String> future = futureAfter("ook!", sleepDuration);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).succeedsWithin(Duration.ofMillis(10)));
    // THEN
    then(assertionError).hasMessageStartingWith(format("%nExpecting%n" +
                                                       "  <FutureTask[Incomplete]>%n" +
                                                       "to be completed within 0.01S.%n"));
  }

  @Test
  public void should_fail_if_future_is_cancelled() {
    // GIVEN
    Future<String> future = new CompletableFuture<>();
    future.cancel(false);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).succeedsWithin(Duration.ofMillis(1)));
    // THEN
    then(assertionError).hasMessageStartingWith(format("%nExpecting%n" +
                                                       "  <CompletableFuture[Cancelled]>%n" +
                                                       "to be completed within 0.001S.%n" +
                                                       "%n" +
                                                       "exception caught while trying to get the future result: java.util.concurrent.CancellationException"));
  }

  @Test
  public void should_fail_when_future_is_null() {
    // GIVEN
    Future<String> future = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(future).succeedsWithin(Duration.ofMillis(1)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  private static <U> Future<U> futureAfter(U value, long sleepDuration) {
    return newSingleThreadExecutor().submit(() -> {
      Thread.sleep(sleepDuration);
      return value;
    });
  }
}
