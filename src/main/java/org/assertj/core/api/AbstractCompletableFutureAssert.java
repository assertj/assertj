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
package org.assertj.core.api;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.error.future.ShouldBeCancelled.shouldBeCancelled;
import static org.assertj.core.error.future.ShouldBeDone.shouldBeDone;
import static org.assertj.core.error.future.ShouldHaveCompletedExceptionally.shouldHaveCompletedExceptionally;
import static org.assertj.core.error.future.ShouldNotBeCancelled.shouldNotBeCancelled;
import static org.assertj.core.error.future.ShouldNotBeDone.shouldNotBeDone;
import static org.assertj.core.error.future.ShouldNotHaveCompletedExceptionally.shouldNotHaveCompletedExceptionally;

/**
 * Assertions for {@link CompletableFuture}.
 *
 * @param <T> type of the value contained in the {@link CompletableFuture}.
 */
public abstract class AbstractCompletableFutureAssert<S extends AbstractCompletableFutureAssert<S, T>, T> extends
    AbstractAssert<S, CompletableFuture<T>> {

  protected AbstractCompletableFutureAssert(CompletableFuture<T> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the {@link CompletableFuture} is done.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something")).isDone();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * assertThat(new CompletableFuture()).isDone();
   * </code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isDone()
   */
  public S isDone() {
    isNotNull();
    if (!actual.isDone()) throw failure(shouldBeDone(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is not done.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * assertThat(new CompletableFuture()).isNotDone();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something")).isNotDone();
   * </code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isDone()
   */
  public S isNotDone() {
    isNotNull();
    if (actual.isDone()) throw failure(shouldNotBeDone(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} has completed exceptionally.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   * assertThat(future).isCompletedExceptionally();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something")).isCompletedExceptionally();
   * </code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isCompletedExceptionally()
   */
  public S isCompletedExceptionally() {
    isNotNull();
    if (!actual.isCompletedExceptionally()) throw failure(shouldHaveCompletedExceptionally(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} has not completed exceptionally.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something")).isNotCompletedExceptionally();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   * assertThat(future).isNotCompletedExceptionally();
   * </code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isCompletedExceptionally()
   */
  public S isNotCompletedExceptionally() {
    isNotNull();
    if (actual.isCompletedExceptionally()) throw failure(shouldNotHaveCompletedExceptionally(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is cancelled.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * CompletableFuture future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).isCancelled();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * assertThat(new CompletableFuture()).isCancelled();
   * </code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isCancelled()
   */
  public S isCancelled() {
    isNotNull();
    if (!actual.isCancelled()) throw failure(shouldBeCancelled(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is not cancelled.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * assertThat(new CompletableFuture()).isNotCancelled();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * CompletableFuture future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).isNotCancelled();
   * </code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isCancelled()
   */
  public S isNotCancelled() {
    isNotNull();
    if (actual.isCancelled()) throw failure(shouldNotBeCancelled(actual));
    return myself;
  }
}
