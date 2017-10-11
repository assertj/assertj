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
package org.assertj.core.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.error.future.ShouldBeCancelled.shouldBeCancelled;
import static org.assertj.core.error.future.ShouldBeCompleted.shouldBeCompleted;
import static org.assertj.core.error.future.ShouldBeCompletedExceptionally.shouldHaveCompletedExceptionally;
import static org.assertj.core.error.future.ShouldBeDone.shouldBeDone;
import static org.assertj.core.error.future.ShouldHaveFailed.shouldHaveFailed;
import static org.assertj.core.error.future.ShouldNotBeCancelled.shouldNotBeCancelled;
import static org.assertj.core.error.future.ShouldNotBeCompleted.shouldNotBeCompleted;
import static org.assertj.core.error.future.ShouldNotBeCompletedExceptionally.shouldNotHaveCompletedExceptionally;
import static org.assertj.core.error.future.ShouldNotBeDone.shouldNotBeDone;
import static org.assertj.core.error.future.ShouldNotHaveFailed.shouldNotHaveFailed;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Predicate;

import org.assertj.core.internal.Failures;
import org.assertj.core.presentation.PredicateDescription;

/**
 * Assertions for {@link CompletableFuture}.
 *
 * @param <RESULT> type of the value contained in the {@link CompletableFuture}.
 */
public abstract class AbstractCompletableFutureAssert<SELF extends AbstractCompletableFutureAssert<SELF, RESULT>, RESULT> extends
    AbstractAssert<SELF, CompletableFuture<RESULT>> {

  protected AbstractCompletableFutureAssert(CompletableFuture<RESULT> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the {@link CompletableFuture} is done i.e. completed normally, exceptionally, or via cancellation.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something")).isDone();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(new CompletableFuture()).isDone();</code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isDone()
   */
  public SELF isDone() {
    isNotNull();
    if (!actual.isDone()) throwAssertionError(shouldBeDone(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is not done.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(new CompletableFuture()).isNotDone();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something")).isNotDone();</code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isDone()
   */
  public SELF isNotDone() {
    isNotNull();
    if (actual.isDone()) throwAssertionError(shouldNotBeDone(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is completed exceptionally. 
   * Possible causes include cancellation, explicit invocation of completeExceptionally, and abrupt termination of a CompletionStage action.
   * <p>
   * If you only want to check that actual future is completed exceptionally but not cancelled, use {@link #hasFailed()} or {@link #hasFailedWithThrowableThat()}.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   * assertThat(future).isCompletedExceptionally();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something")).isCompletedExceptionally();</code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isCompletedExceptionally()
   */
  public SELF isCompletedExceptionally() {
    isNotNull();
    if (!actual.isCompletedExceptionally()) throwAssertionError(shouldHaveCompletedExceptionally(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is not completed exceptionally.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something")).isNotCompletedExceptionally();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   * assertThat(future).isNotCompletedExceptionally();</code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isCompletedExceptionally()
   */
  public SELF isNotCompletedExceptionally() {
    isNotNull();
    if (actual.isCompletedExceptionally()) throwAssertionError(shouldNotHaveCompletedExceptionally(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is cancelled.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).isCancelled();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(new CompletableFuture()).isCancelled();</code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isCancelled()
   */
  public SELF isCancelled() {
    isNotNull();
    if (!actual.isCancelled()) throwAssertionError(shouldBeCancelled(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is not cancelled.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(new CompletableFuture()).isNotCancelled();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).isNotCancelled();</code></pre>
   *
   * @return this assertion object.
   *
   * @see CompletableFuture#isCancelled()
   */
  public SELF isNotCancelled() {
    isNotNull();
    if (actual.isCancelled()) throwAssertionError(shouldNotBeCancelled(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is completed normally (i.e.{@link CompletableFuture#isDone() done} 
   * but not {@link CompletableFuture#isCompletedExceptionally() completed exceptionally}).
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something")).isCompleted();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(new CompletableFuture()).isCompleted();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF isCompleted() {
    isNotNull();
    if (!actual.isDone() || actual.isCompletedExceptionally()) throwAssertionError(shouldBeCompleted(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is not completed normally (i.e. incomplete, failed or cancelled).
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(new CompletableFuture()).isNotCompleted();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something")).isNotCompleted();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF isNotCompleted() {
    isNotNull();
    if (actual.isDone() && !actual.isCompletedExceptionally()) throwAssertionError(shouldNotBeCompleted(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is completed normally with the {@code expected} result.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something"))
   *           .isCompletedWithValue("something");</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something"))
   *           .isCompletedWithValue("something else");</code></pre>
   *
   * @param expected the expected result value of the {@link CompletableFuture}.
   * @return this assertion object.
   */
  public SELF isCompletedWithValue(RESULT expected) {
    isCompleted();

    RESULT actualResult = actual.join();
    if (!Objects.equals(actualResult, expected))
      throw Failures.instance().failure(info, shouldBeEqual(actualResult, expected, info.representation()));

    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} is completed normally with a result matching the {@code predicate}.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something"))
   *           .isCompletedWithValueMatching(result -&gt; result.equals("something"));</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something"))
   *           .isCompletedWithValueMatching(result -&gt; result.equals("something else"));</code></pre>
   *
   * @param predicate the {@link Predicate} to apply.
   * @return this assertion object.
   */
  public SELF isCompletedWithValueMatching(Predicate<? super RESULT> predicate) {
    return isCompletedWithValueMatching(predicate, PredicateDescription.GIVEN);
  }

  /**
   * Verifies that the {@link CompletableFuture} is completed normally with a result matching the {@code predicate}, 
   * the String parameter is used in the error message.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something"))
   *           .isCompletedWithValueMatching(result -&gt; result != null, "expected not null");</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(CompletableFuture.completedFuture("something"))
   *           .isCompletedWithValueMatching(result -&gt; result == null, "expected null");</code></pre>
   * Error message is:            
   * <pre><code class='java'> Expecting:
   *   &lt;"something"&gt;
   * to match 'expected null' predicate.</code></pre>
   *
   * @param predicate the {@link Predicate} to apply on the resulting value.
   * @param description the {@link Predicate} description.
   * @return this assertion object.
   */
  public SELF isCompletedWithValueMatching(Predicate<? super RESULT> predicate, String description) {
    return isCompletedWithValueMatching(predicate, new PredicateDescription(description));
  }

  private SELF isCompletedWithValueMatching(Predicate<? super RESULT> predicate, PredicateDescription description) {
    isCompleted();

    RESULT actualResult = actual.join();
    if (!predicate.test(actualResult))
      throw Failures.instance().failure(info, shouldMatch(actualResult, predicate, description));

    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} has completed exceptionally but has not been cancelled, 
   * this assertion is equivalent to: 
   * <pre><code class='java'> assertThat(future).isCompletedExceptionally()
   *                   .isNotCancelled();</code></pre>
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   * assertThat(future).hasFailed();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).hasFailed();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF hasFailed() {
    isNotNull();
    if (!actual.isCompletedExceptionally() || actual.isCancelled()) throwAssertionError(shouldHaveFailed(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} has not failed i.e: incomplete, completed or cancelled.<br>
   * This is different from {@link #isNotCompletedExceptionally()} as a cancelled future has not failed but is completed exceptionally.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.cancel(true);
   * assertThat(future).hasNotFailed();</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   * assertThat(future).hasNotFailed();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF hasNotFailed() {
    isNotNull();
    if (actual.isCompletedExceptionally() && !actual.isCancelled()) throwAssertionError(shouldNotHaveFailed(actual));
    return myself;
  }

  /**
   * Verifies that the {@link CompletableFuture} has completed exceptionally and 
   * returns a Throwable assertion object allowing to check the Throwable that has caused the future to fail.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException("boom!"));
   *
   * assertThat(future).hasFailedWithThrowableThat().isInstanceOf(RuntimeException.class);
   *                                                .hasMessage("boom!");
   * </code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   *
   * assertThat(future).hasFailedWithThrowableThat().isInstanceOf(IllegalArgumentException.class);
   * </code></pre>
   *
   * @return an exception assertion object.
   */
  public AbstractThrowableAssert<?, ? extends Throwable> hasFailedWithThrowableThat() {
    hasFailed();
    try {
      actual.join();
      return assertThat((Throwable) null);
    } catch (CompletionException e) {
      return assertThat(e.getCause());
    }
  }
}
