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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error.future;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.future.ShouldNotHaveFailed.shouldNotHaveFailed;
import static org.assertj.core.error.future.Warning.WARNING;

import java.util.concurrent.CompletableFuture;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

public class ShouldNotHaveFailed_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    CompletableFuture<Object> future = new CompletableFuture<>();
    future.completeExceptionally(new RuntimeException());
    // WHEN
    String error = shouldNotHaveFailed(future).create(new TestDescription("TEST"));
    // THEN
    assertThat(error).startsWith(format("[TEST] %nExpecting%n  <CompletableFuture[Failed: java.lang.RuntimeException]%n"))
                     .contains("Caused by: java.lang.RuntimeException")
                     .endsWith(format("to not have failed.%n%s", WARNING));
  }
}
