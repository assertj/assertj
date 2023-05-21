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
 * Copyright 2012-2023 the original author or authors.
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

  public static <RESULT> CompletableFutureAssert<RESULT> assertThatCompletionStage(CompletionStage<RESULT> actual) {
    return new CompletableFutureAssert<>(actual);
  }

  protected CompletableFutureAssert(CompletableFuture<RESULT> actual) {
    super(actual, CompletableFutureAssert.class);
  }

  protected CompletableFutureAssert(CompletionStage<RESULT> actual) {
    super(actual == null ? null : actual.toCompletableFuture(), CompletableFutureAssert.class);
  }

}
