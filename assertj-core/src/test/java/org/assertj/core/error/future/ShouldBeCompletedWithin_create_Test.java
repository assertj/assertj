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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.future.ShouldBeCompletedWithin.shouldBeCompletedWithin;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldBeCompletedWithin_create_Test {

  @Test
  void should_create_error_message_with_duration() {
    // GIVEN
    CompletableFuture<Object> actual = new CompletableFuture<>();
    Duration duration = Duration.ofMillis(70_001);
    Exception exception = new Exception("boom");
    // WHEN
    String error = shouldBeCompletedWithin(actual, duration, exception).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo("[TEST] %n" + "Expecting%n" + "  <CompletableFuture[Incomplete]>%n"
                          + "to be completed within 1M10.001S.%n" + "%n"
                          + "exception caught while trying to get the future result: %s", getStackTrace(exception));
  }

  @Test
  void should_create_error_message() {
    // GIVEN
    CompletableFuture<Object> actual = new CompletableFuture<>();
    Exception exception = new Exception("boom");
    // WHEN
    String error = shouldBeCompletedWithin(actual, 100, TimeUnit.MILLISECONDS, exception).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo("[TEST] %n" +
                          "Expecting%n" +
                          "  <CompletableFuture[Incomplete]>%n" +
                          "to be completed within 100L Millis.%n" +
                          "%n" +
                          "exception caught while trying to get the future result: %s",
                          getStackTrace(exception));
  }

}
