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

import static org.assertj.core.error.future.ShouldBeCancelled.shouldBeCancelled;
import static org.assertj.core.error.future.ShouldBeDone.shouldBeDone;
import static org.assertj.core.error.future.ShouldNotBeCancelled.shouldNotBeCancelled;
import static org.assertj.core.error.future.ShouldNotBeDone.shouldNotBeDone;

import java.util.concurrent.Future;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for <code>{@link Future}</code>s.
 *
 * @author Ruben Dijkstra
 */
public class Futures {

  private static final Futures INSTANCE = new Futures();

  /**
   * Returns the singleton instance of this class.
   *
   * @return the singleton instance of this class.
   */
  public static Futures instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  /**
   * Verifies that the {@link Future} is cancelled.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   */
  public void assertIsCancelled(AssertionInfo info, Future<?> actual) {
    assertNotNull(info, actual);
    if (!actual.isCancelled())
      throw failures.failure(info, shouldBeCancelled(actual));
  }

  /**
   * Verifies that the {@link Future} is not cancelled.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   */
  public void assertIsNotCancelled(AssertionInfo info, Future<?> actual) {
    assertNotNull(info, actual);
    if (actual.isCancelled())
      throw failures.failure(info, shouldNotBeCancelled(actual));
  }

  /**
   * Verifies that the {@link Future} is done.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   */
  public void assertIsDone(AssertionInfo info, Future<?> actual) {
    assertNotNull(info, actual);
    if (!actual.isDone())
      throw failures.failure(info, shouldBeDone(actual));
  }

  /**
   * Verifies that the {@link Future} is not done.
   * @param info contains information about the assertion.
   * @param actual the "actual" {@code Date}.
   */
  public void assertIsNotDone(AssertionInfo info, Future<?> actual) {
    assertNotNull(info, actual);
    if (actual.isDone())
      throw failures.failure(info, shouldNotBeDone(actual));
  }

  private void assertNotNull(AssertionInfo info, Future<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }
}
