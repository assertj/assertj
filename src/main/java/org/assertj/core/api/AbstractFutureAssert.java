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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import java.util.concurrent.Future;

import org.assertj.core.internal.Futures;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractFutureAssert<S extends AbstractFutureAssert<S, A, T>, A extends Future<T>, T> extends
    AbstractAssert<S, A> {

  @VisibleForTesting
  Futures futures = Futures.instance();

  protected AbstractFutureAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the {@link Future} is cancelled.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> Future future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).isCancelled();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> Future future = new CompletableFuture();
   * assertThat(future).isCancelled();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isCancelled()
   * @since 2.7.0 / 3.7.0
   */
  public S isCancelled() {
    futures.assertIsCancelled(info, actual);
    return myself;
  }

  /**
   * Verifies that the {@link Future} is not cancelled.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> Future future = new CompletableFuture();
   * assertThat(future).isNotCancelled();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> Future future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).isNotCancelled();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isCancelled()
   * @since 2.7.0 / 3.7.0
   */
  public S isNotCancelled() {
    futures.assertIsNotCancelled(info, actual);
    return myself;
  }

  /**
   * Verifies that the {@link Future} is done.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> Future future = CompletableFuture.completedFuture("something");
   * assertThat(future).isDone();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> Future future = new CompletableFuture();
   * assertThat(future).isDone();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isDone()
   * @since 2.7.0 / 3.7.0
   */
  public S isDone() {
    futures.assertIsDone(info, actual);
    return myself;
  }

  /**
   * Verifies that the {@link Future} is not done.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> Future future = new CompletableFuture();
   * assertThat(future).isNotDone();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> Future future = CompletableFuture.completedFuture("something");
   * assertThat(future).isNotDone();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isDone()
   * @since 2.7.0 / 3.7.0
   */
  public S isNotDone() {
    futures.assertIsNotDone(info, actual);
    return myself;
  }
}
