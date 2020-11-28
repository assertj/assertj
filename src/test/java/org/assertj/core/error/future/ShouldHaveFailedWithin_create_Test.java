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
package org.assertj.core.error.future;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.future.ShouldHaveFailedWithin.shouldHaveFailedWithin;
import static org.assertj.core.error.future.Warning.WARNING;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveFailedWithin_create_Test {

  @Test
  void should_create_error_message_with_duration() {
    // GIVEN
    CompletableFuture<Object> actual = completedFuture("ok");
    // WHEN
    String error = shouldHaveFailedWithin(actual, Duration.ofHours(1)).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting%n" +
                                 "  <CompletableFuture[Completed: \"ok\"]>%n" +
                                 "to have failed within 1H.%n%s",
                                 WARNING));
  }

  @Test
  void should_create_error_message_with_time_unit() {
    // GIVEN
    Future<Object> actual = new CompletableFuture<>();
    // WHEN
    String error = shouldHaveFailedWithin(actual, 1, TimeUnit.HOURS).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting%n" +
                                 "  <CompletableFuture[Incomplete]>%n" +
                                 "to have failed within 1L HOURS.%n%s",
                                 WARNING));
  }

}
