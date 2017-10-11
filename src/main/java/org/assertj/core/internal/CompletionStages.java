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
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

import java.util.concurrent.CompletionStage;

/**
 * Reusable assertions for <code>{@link CompletionStage}</code>s.
 *
 * @author Ruben Dijkstra
 */
public class CompletionStages {

  private static final CompletionStages INSTANCE = new CompletionStages();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static CompletionStages instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Futures futures = Futures.instance();

  /**
   * Verifies that the {@link CompletionStage} is cancelled.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   */
  public void assertIsCancelled(AssertionInfo info, CompletionStage<?> actual) {
    assertNotNull(info, actual);
    futures.assertIsCancelled(info, actual.toCompletableFuture());
  }

  /**
   * Verifies that the {@link CompletionStage} is not cancelled.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   */
  public void assertIsNotCancelled(AssertionInfo info, CompletionStage<?> actual) {
    assertNotNull(info, actual);
    futures.assertIsNotCancelled(info, actual.toCompletableFuture());
  }

  /**
   * Verifies that the {@link CompletionStage} is done.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   */
  public void assertIsDone(AssertionInfo info, CompletionStage<?> actual) {
    assertNotNull(info, actual);
    futures.assertIsDone(info, actual.toCompletableFuture());
  }

  /**
   * Verifies that the {@link CompletionStage} is not done.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   */
  public void assertIsNotDone(AssertionInfo info, CompletionStage<?> actual) {
    assertNotNull(info, actual);
    futures.assertIsNotDone(info, actual.toCompletableFuture());
  }

  private void assertNotNull(AssertionInfo info, CompletionStage<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
