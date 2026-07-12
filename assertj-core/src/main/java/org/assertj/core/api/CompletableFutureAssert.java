/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Assertions for {@link CompletableFuture}.
 *
 * @param <RESULT> type of the value contained in the {@link CompletableFuture}.
 */
public class CompletableFutureAssert<RESULT> extends AbstractCompletableFutureAssert<CompletableFutureAssert<RESULT>, RESULT> {

  /**
   * Creates an assertion for the given completion stage.
   *
   * @param <RESULT> the completion result type
   * @param actual the actual completion stage
   * @return the created assertion object
   */
  public static <RESULT> CompletableFutureAssert<RESULT> assertThatCompletionStage(CompletionStage<RESULT> actual) {
    return new CompletableFutureAssert<>(actual);
  }

  /**
   * Creates an assertion for the given completable future.
   *
   * @param actual the actual future
   */
  protected CompletableFutureAssert(CompletableFuture<RESULT> actual) {
    super(actual, CompletableFutureAssert.class);
  }

  /**
   * Creates an assertion for the given completion stage.
   *
   * @param actual the actual completion stage
   */
  protected CompletableFutureAssert(CompletionStage<RESULT> actual) {
    super(actual == null ? null : actual.toCompletableFuture(), CompletableFutureAssert.class);
  }

}
