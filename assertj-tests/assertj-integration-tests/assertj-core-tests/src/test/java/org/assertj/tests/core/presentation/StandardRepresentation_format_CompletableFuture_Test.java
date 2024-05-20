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
package org.assertj.tests.core.presentation;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

class StandardRepresentation_format_CompletableFuture_Test {

  @Test
  void should_format_incomplete_future() {
    // GIVEN
    CompletableFuture<Object> future = new CompletableFuture<>();
    // WHEN
    String futureStandardRepresentation = STANDARD_REPRESENTATION.toStringOf(future);
    // THEN
    then(futureStandardRepresentation).isEqualTo("CompletableFuture[Incomplete]");
  }

  @Test
  void should_format_complete_future() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(completedFuture("done"))).isEqualTo("CompletableFuture[Completed: \"done\"]");
    assertThat(STANDARD_REPRESENTATION.toStringOf(completedFuture(42))).isEqualTo("CompletableFuture[Completed: 42]");
    assertThat(STANDARD_REPRESENTATION.toStringOf(completedFuture(null))).isEqualTo("CompletableFuture[Completed: null]");
  }

  @Test
  void should_format_failed_future() {
    // GIVEN
    CompletableFuture<Object> future = new CompletableFuture<>();
    // WHEN
    future.completeExceptionally(new RuntimeException("some random error"));
    // THEN
    then(STANDARD_REPRESENTATION.toStringOf(future)).startsWith(format("CompletableFuture[Failed with the following stack trace:%njava.lang.RuntimeException: some random error"));
  }

  @Test
  void should_format_cancelled_future() {
    // GIVEN
    CompletableFuture<Object> future = new CompletableFuture<>();
    // WHEN
    future.cancel(true);
    // THEN
    then(STANDARD_REPRESENTATION.toStringOf(future)).isEqualTo("CompletableFuture[Cancelled]");
  }

  @Test
  void should_not_stack_overflow_when_formatting_future_completed_with_itself() {
    // GIVEN
    CompletableFuture<CompletableFuture<?>> future = new CompletableFuture<>();
    // WHEN
    future.complete(future);
    // THEN
    then(STANDARD_REPRESENTATION.toStringOf(future)).isEqualTo("CompletableFuture[Completed: " + future + "]");
  }

  @Test
  void should_not_stack_overflow_when_formatting_future_with_reference_cycle() {
    // GIVEN
    CompletableFuture<CompletableFuture<?>> future1 = new CompletableFuture<>();
    CompletableFuture<CompletableFuture<?>> future2 = new CompletableFuture<>();
    // WHEN
    future1.complete(future2);
    future2.complete(future1);
    // THEN
    then(STANDARD_REPRESENTATION.toStringOf(future1)).isEqualTo("CompletableFuture[Completed: " + future2 + "]");
    then(STANDARD_REPRESENTATION.toStringOf(future2)).isEqualTo("CompletableFuture[Completed: " + future1 + "]");
  }
}
