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
package org.assertj.core.error.future;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.future.ShouldHaveCompletedExceptionallyWithin.shouldHaveCompletedExceptionallyWithin;
import static org.assertj.core.error.future.Warning.WARNING;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveCompletedExceptionallyWithin_create_Test {

  private final CompletableFuture<Object> future = new CompletableFuture<>();
  private final TestDescription description = new TestDescription("TEST");

  @Test
  void should_create_error_message() {
    // WHEN
    String error = shouldHaveCompletedExceptionallyWithin(future, 1, SECONDS).create(description);
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting%n" +
                                 "  <CompletableFuture[Incomplete]>%n" +
                                 "to have completed exceptionally within 1L SECONDS.%n%s", WARNING));
  }

  @Test
  void should_create_error_message_Duration() {
    // WHEN
    String error = shouldHaveCompletedExceptionallyWithin(future, Duration.ofSeconds(1)).create(description);
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting%n" +
                                 "  <CompletableFuture[Incomplete]>%n" +
                                 "to have completed exceptionally within 1S.%n%s", WARNING));
  }

}
