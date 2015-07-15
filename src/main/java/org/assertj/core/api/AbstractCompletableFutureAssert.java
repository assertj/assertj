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

import org.assertj.core.internal.Failures;
import org.assertj.core.presentation.PredicateDescription;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.error.future.ShouldBeCancelled.shouldBeCancelled;
import static org.assertj.core.error.future.ShouldBeCompleted.shouldBeCompleted;
import static org.assertj.core.error.future.ShouldBeDone.shouldBeDone;
import static org.assertj.core.error.future.ShouldHaveCompletedExceptionally.shouldHaveCompletedExceptionally;
import static org.assertj.core.error.future.ShouldHaveFailed.shouldHaveFailed;
import static org.assertj.core.error.future.ShouldNotBeCancelled.shouldNotBeCancelled;
import static org.assertj.core.error.future.ShouldNotBeCompleted.shouldNotBeCompleted;
import static org.assertj.core.error.future.ShouldNotBeDone.shouldNotBeDone;
import static org.assertj.core.error.future.ShouldNotHaveCompletedExceptionally.shouldNotHaveCompletedExceptionally;
import static org.assertj.core.error.future.ShouldNotHaveFailed.shouldNotHaveFailed;

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

  /**
   * Verifies that the {@link CompletableFuture} is completed normally.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something")).isCompleted();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * assertThat(new CompletableFuture()).isCompleted();
   * </code></pre>
   *
   * @return this assertion object.
   */
  public S isCompleted() {
    isNotNull();
    if (!actual.isDone() || actual.isCompletedExceptionally()) throw failure(shouldBeCompleted(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is not completed normally (i.e. incomplete, failed or cancelled).
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * assertThat(new CompletableFuture()).isNotCompleted();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something")).isNotCompleted();
   * </code></pre>
   *
   * @return this assertion object.
   */
  public S isNotCompleted() {
    isNotNull();
    if (actual.isDone() && !actual.isCompletedExceptionally()) throw failure(shouldNotBeCompleted(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is completed normally with the {@code expected} result.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something")).isCompletedWith("something");
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something")).isCompletedWith("something else");
   * </code></pre>
   *
   * @return this assertion object.
   */
  public S isCompletedWith(T expected) {
    isCompleted();

    T actualResult = actual.join();
    if (!Objects.equals(actualResult, expected))
      throw Failures.instance().failure(info, shouldBeEqual(actualResult, expected, info.representation()));

    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is completed normally with a result matching the {@code predicate}.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something"))
   *         .isCompletedMatching(result -> result.equals("something"));
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something"))
   *         .isCompletedMatching(result -> result.equals("something else"));
   * </code></pre>
   *
   * @return this assertion object.
   */
  public S isCompletedMatching(Predicate<? super T> predicate) {
    return isCompletedMatching(predicate, PredicateDescription.GIVEN);
  }

  /**
   * Verifies that the {@link CompletableFuture} is completed normally with a result matching the {@code predicate}.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something"))
   *         .isCompletedMatching(result -> result.equals("something"));
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * assertThat(CompletableFuture.completedFuture("something"))
   *         .isCompletedMatching(result -> result.equals("something else"));
   * </code></pre>
   *
   * @return this assertion object.
   */
  public S isCompletedMatching(Predicate<? super T> predicate, String description) {
    return isCompletedMatching(predicate, new PredicateDescription(description));
  }

  private S isCompletedMatching(Predicate<? super T> predicate, PredicateDescription description) {
    isCompleted();

    T actualResult = actual.join();
    if (!predicate.test(actualResult))
      throw Failures.instance().failure(info, shouldMatch(actualResult, predicate, description));

    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} has completed exceptionally, but has not been cancelled.
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   * assertThat(future).hasFailed();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * CompletableFuture future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).hasFailed();
   * </code></pre>
   *
   * @return this assertion object.
   */
  public S hasFailed() {
    isNotNull();
    if (!actual.isCompletedExceptionally() || actual.isCancelled()) throw failure(shouldHaveFailed(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} has not completed exceptionally
   * (i.e. incomplete, completed or cancelled).
   * <p>
   * Assertion will pass :
   *
   * <pre><code class='java'>
   * CompletableFuture future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).hasNotFailed();
   * </code></pre>
   *
   * Assertion will fail :
   *
   * <pre><code class='java'>
   * CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   * assertThat(future).hasNotFailed();
   * </code></pre>
   *
   * @return this assertion object.
   */
  public S hasNotFailed() {
    isNotNull();
    if (actual.isCompletedExceptionally() && !actual.isCancelled()) throw failure(shouldNotHaveFailed(actual));
    return myself;
  }
}
