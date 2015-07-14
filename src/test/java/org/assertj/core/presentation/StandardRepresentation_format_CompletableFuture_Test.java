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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.presentation;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThatThrownBy;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

public class StandardRepresentation_format_CompletableFuture_Test {

  @Test
  public void should_format_incomplete_future() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(new CompletableFuture<>()))
        .isEqualTo("java.util.concurrent.CompletableFuture[Incomplete]");
  }

  @Test
  public void should_format_complete_future() {
    assertThat(STANDARD_REPRESENTATION.toStringOf(CompletableFuture.completedFuture("done")))
        .isEqualTo("java.util.concurrent.CompletableFuture[Completed: \"done\"]");

    assertThat(STANDARD_REPRESENTATION.toStringOf(CompletableFuture.completedFuture(42)))
        .isEqualTo("java.util.concurrent.CompletableFuture[Completed: 42]");

    assertThat(STANDARD_REPRESENTATION.toStringOf(CompletableFuture.completedFuture(null)))
        .isEqualTo("java.util.concurrent.CompletableFuture[Completed: null]");
  }

  @Test
  public void should_format_failed_future() {
    CompletableFuture<Object> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException("some random error"));

    assertThat(STANDARD_REPRESENTATION.toStringOf(future))
        .isEqualTo("java.util.concurrent.CompletableFuture[Failed: java.lang.RuntimeException: some random error]");
  }

  @Test
  public void should_format_cancelled_future() {
    CompletableFuture<Object> future = new CompletableFuture<>();
    future.cancel(true);

    assertThat(STANDARD_REPRESENTATION.toStringOf(future))
        .isEqualTo("java.util.concurrent.CompletableFuture[Cancelled]");
  }
}
