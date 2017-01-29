/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.presentation;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

public class StandardRepresentation_format_CompletableFuture_Test {

  @Test
  public void should_format_incomplete_future() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new CompletableFuture<>())).isEqualTo("CompletableFuture[Incomplete]");
  }

  @Test
  public void should_format_complete_future() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(completedFuture("done"))).isEqualTo("CompletableFuture[Completed: \"done\"]");
    assertThat(STANDARD_REPRESENTATION.toStringOf(completedFuture(42))).isEqualTo("CompletableFuture[Completed: 42]");
    assertThat(STANDARD_REPRESENTATION.toStringOf(completedFuture(null))).isEqualTo("CompletableFuture[Completed: null]");
  }

  @Test
  public void should_format_failed_future() {
    CompletableFuture<Object> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException("some random error"));
    assertThat(STANDARD_REPRESENTATION.toStringOf(future)).isEqualTo("CompletableFuture[Failed: java.lang.RuntimeException: some random error]");
  }

  @Test
  public void should_format_cancelled_future() {
    CompletableFuture<Object> future = new CompletableFuture<>();
    future.cancel(true);
    assertThat(STANDARD_REPRESENTATION.toStringOf(future)).isEqualTo("CompletableFuture[Cancelled]");
  }

  @Test
  public void should_not_stack_overflow_when_formatting_future_completed_with_itself() {
    CompletableFuture<CompletableFuture<?>> future = new CompletableFuture<>();
    future.complete(future);
    assertThat(STANDARD_REPRESENTATION.toStringOf(future)).isEqualTo("CompletableFuture[Completed: " + future + "]");
  }
  
  @Test
  public void should_not_stack_overflow_when_formatting_future_with_reference_cycle() {
    CompletableFuture<CompletableFuture<?>> future1 = new CompletableFuture<>();
    CompletableFuture<CompletableFuture<?>> future2 = new CompletableFuture<>();
    future1.complete(future2);
    future2.complete(future1);
    assertThat(STANDARD_REPRESENTATION.toStringOf(future1)).isEqualTo("CompletableFuture[Completed: " + future2 + "]");
    assertThat(STANDARD_REPRESENTATION.toStringOf(future2)).isEqualTo("CompletableFuture[Completed: " + future1 + "]");
  }
}
