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
 * Copyright 2012-2024 the original author or authors.
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

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Futures;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for {@link CompletableFuture}.
 *
 * @param <RESULT> type of the value contained in the {@link CompletableFuture}.
 */
// TODO deprecate completed for succeeds?
public abstract class AbstractCompletableFutureAssert<SELF extends AbstractCompletableFutureAssert<SELF, RESULT>, RESULT> extends
    AbstractAssert<SELF, CompletableFuture<RESULT>> {

  @VisibleForTesting
  Futures futures = Futures.instance();

  protected AbstractCompletableFutureAssert(CompletableFuture<RESULT> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the {@link CompletableFuture} is {@link CompletableFuture#isDone() done} i.e. completed normally, exceptionally, or via cancellation.
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
   * Verifies that the {@link CompletableFuture} is {@link CompletableFuture#isCompletedExceptionally() completed exceptionally}.
   * <p>
   * Possible causes include cancellation, explicit invocation of completeExceptionally, and abrupt termination of a CompletionStage action.
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
   * Verifies that the {@link CompletableFuture} is {@link CompletableFuture#isCancelled() cancelled}.
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
   * Verifies that the {@link CompletableFuture} is completed normally (i.e.{@link CompletableFuture#isDone() done} but not
   * {@link CompletableFuture#isCompletedExceptionally() completed exceptionally}) or {@link CompletableFuture#isCancelled() cancelled}.
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
    // cancelled is included in completed exceptionally
    if (actual.isCompletedExceptionally() || !actual.isDone()) throwAssertionError(shouldBeCompleted(actual));
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
   * <p>
   * Combine isCompletedExceptionally with isNotCancelled instead:
   *
   * <pre><code class='java'> assertThat(future).isCompletedExceptionally()
   *                   .isNotCancelled();</code></pre>
   *
   * This assertion is deprecated to change the semantics of failed to correspond to {@link CompletableFuture#get()} failing.
   * <p>
   * <b>Original javadoc</b>
   * <p>
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
   *
   * @deprecated
   */
  @Deprecated
  public SELF hasFailed() {
    isNotNull();
    if (!(actual.isCompletedExceptionally() && !actual.isCancelled())) throwAssertionError(shouldHaveFailed(actual));
    return myself;
  }

  /**
   * <p>
   * Use matches with the following combination instead:
   *
   * <pre><code class='java'> assertThat(future).matches (f -&gt; f.isNotCompletedExceptionally() {@literal ||} f.isCancelled());</code></pre>
   *
   * This assertion is deprecated because its semantic is not obvious.
   * <p>
   * <b>Original javadoc</b>
   * <p>
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
   *
   * @deprecated
   */
  @Deprecated
  public SELF hasNotFailed() {
    isNotNull();
    if (actual.isCompletedExceptionally() && !actual.isCancelled()) throwAssertionError(shouldNotHaveFailed(actual));
    return myself;
  }

  /**
   * Waits if necessary for at most the given time for this future to complete, and then returns its result for further assertions.
   * <p>
   * If the future's result is not available for any reason an assertion error is thrown.
   * <p>
   * To get assertions for the future result's type use {@link #succeedsWithin(Duration, InstanceOfAssertFactory)} instead.
   * <p>
   * Examples:
   * <pre><code class='java'> CompletableFuture&lt;String&gt; future = CompletableFuture.completedFuture("ook!");
   * Duration timeout = Duration.ofMillis(100);
   *
   * // assertion succeeds
   * assertThat(future).succeedsWithin(timeout)
   *                   .isEqualTo("ook!");
   *
   * // fails assuming the future is not done after the given timeout
   * CompletableFuture&lt;String&gt; future = ... ; // future too long to complete
   * assertThat(future).succeedsWithin(timeout);
   *
   * // fails as the future is cancelled
   * CompletableFuture future = new CompletableFuture();
   * future.cancel(false);
   * assertThat(future).succeedsWithin(timeout);</code></pre>
   *
   * @param timeout the maximum time to wait
   * @return a new assertion object on the future's result.
   * @throws AssertionError if the actual {@code CompletableFuture} is {@code null}.
   * @throws AssertionError if the actual {@code CompletableFuture} does not succeed within the given timeout.
   */
  public ObjectAssert<RESULT> succeedsWithin(Duration timeout) {
    return internalSucceedsWithin(timeout);
  }

  private ObjectAssert<RESULT> internalSucceedsWithin(Duration timeout) {
    RESULT result = futures.assertSucceededWithin(info, actual, timeout);
    return newObjectAssert(result);
  }

  // introduced to be proxied for assumptions and soft assertions.
  protected ObjectAssert<RESULT> newObjectAssert(RESULT objectUnderTest) {
    return new ObjectAssert<>(objectUnderTest);
  }

  /**
   * Waits if necessary for at most the given time for this future to complete, and then returns its result for further assertions.
   * <p>
   * If the future's result is not available for any reason an assertion error is thrown.
   * <p>
   * To get assertions for the future result's type use {@link #succeedsWithin(long, TimeUnit, InstanceOfAssertFactory)} instead.
   * <p>
   * Examples:
   * <pre><code class='java'> CompletableFuture&lt;String&gt; future = CompletableFuture.completedFuture("ook!");
   *
   * // assertion succeeds
   * assertThat(future).succeedsWithin(100, TimeUnit.MILLISECONDS)
   *                   .isEqualTo("ook!");
   *
   * // fails assuming the future is not done after the given timeout
   * CompletableFuture&lt;String&gt; future = ... ; // future too long to complete
   * assertThat(future).succeedsWithin(100, TimeUnit.MILLISECONDS);
   *
   * // fails as the future is cancelled
   * CompletableFuture future = new CompletableFuture();
   * future.cancel(false);
   * assertThat(future).succeedsWithin(100, TimeUnit.MILLISECONDS);</code></pre>
   *
   * @param timeout the maximum time to wait
   * @param unit the time unit of the timeout argument
   * @return a new assertion object on the future's result.
   * @throws AssertionError if the actual {@code CompletableFuture} is {@code null}.
   * @throws AssertionError if the actual {@code CompletableFuture} does not succeed within the given timeout.
   */
  public ObjectAssert<RESULT> succeedsWithin(long timeout, TimeUnit unit) {
    return internalSucceedsWithin(timeout, unit);
  }

  private ObjectAssert<RESULT> internalSucceedsWithin(long timeout, TimeUnit unit) {
    RESULT result = futures.assertSucceededWithin(info, actual, timeout, unit);
    return newObjectAssert(result);
  }

  /**
   * Waits if necessary for at most the given time for this future to complete, the {@link InstanceOfAssertFactory}
   * parameter is used to return assertions specific to the future's result type.
   * <p>
   * If the future's result is not available for any reason an assertion error is thrown.
   * <p>
   * Examples:
   * <pre><code class='java'> CompletableFuture&lt;String&gt; future = CompletableFuture.completedFuture("ook!");
   * Duration timeout = Duration.ofMillis(100);
   *
   * // assertion succeeds
   * // using asInstanceOf is recommended to get assertions for the future result's type
   * assertThat(future).succeedsWithin(timeout, InstanceOfAssertFactories.STRING)
   *                   .contains("ok");
   *
   * // assertion fails if the narrowed type for assertions is incompatible with the future's result type.
   * assertThat(future).succeedsWithin(timeout, InstanceOfAssertFactories.DATE)
   *                   .isToday();</code></pre>
   *
   * @param <ASSERT> the type of the resulting {@code Assert}
   * @param timeout the maximum time to wait
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the value of the {@link CompletableFuture}
   * @throws AssertionError if the actual {@code CompletableFuture} is {@code null}.
   * @throws IllegalStateException if the actual {@code CompletableFuture} does not succeed within the given timeout.
   */
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT succeedsWithin(Duration timeout,
                                                                     InstanceOfAssertFactory<RESULT, ASSERT> assertFactory) {
    return internalSucceedsWithin(timeout).asInstanceOf(assertFactory);
  }

  /**
   * Waits if necessary for at most the given time for this future to complete, the {@link InstanceOfAssertFactory}
   * parameter is used to return assertions specific to the future's result type.
   * <p>
   * If the future's result is not available for any reason an assertion error is thrown.
   * <p>
   * Examples:
   * <pre><code class='java'> CompletableFuture&lt;String&gt; future = CompletableFuture.completedFuture("ook!");
   *
   * // assertion succeeds
   * // using asInstanceOf is recommended to get assertions for the future result's type
   * assertThat(future).succeedsWithin(100, TimeUnit.MILLISECONDS, InstanceOfAssertFactories.STRING)
   *                   .contains("ok");
   *
   * // assertion  fails if the narrowed type for assertions is incompatible with the future's result type.
   * assertThat(future).succeedsWithin(100, TimeUnit.MILLISECONDS, InstanceOfAssertFactories.DATE)
   *                   .isToday();</code></pre>
   *
   * @param <ASSERT> the type of the resulting {@code Assert}
   * @param timeout the maximum time to wait
   * @param unit the time unit of the timeout argument
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the value of the {@link CompletableFuture}
   * @throws AssertionError if the actual {@code CompletableFuture} is {@code null}.
   * @throws AssertionError if the actual {@code CompletableFuture} does not succeed within the given timeout.
   */
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT succeedsWithin(long timeout, TimeUnit unit,
                                                                     InstanceOfAssertFactory<RESULT, ASSERT> assertFactory) {
    return internalSucceedsWithin(timeout, unit).asInstanceOf(assertFactory);
  }

  /**
   * <p>
   * Although not 100% the same, consider using {@link #failsWithin(Duration)} or {@link #failsWithin(long, TimeUnit)} instead:
   *
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException("boom!"));
   *
   * assertThat(future).failsWithin(1, TimeUnit.SECONDS)
   *                   .withThrowableOfType(RuntimeException.class)
   *                   .withMessage("boom!"); </code></pre>
   *
   * This assertion is deprecated because it relies on {@link #hasFailed()} semantics which we want to move away from (they
   * are not clear!) and to use failure semantics corresponding to {@link CompletableFuture#get()} failing.
   * <p>
   * <b>Original javadoc</b>
   * <p>
   * Verifies that the {@link CompletableFuture} has completed exceptionally and
   * returns a Throwable assertion object allowing to check the Throwable that has caused the future to fail.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException("boom!"));
   *
   * assertThat(future).hasFailedWithThrowableThat().isInstanceOf(RuntimeException.class);
   *                                                .hasMessage("boom!"); </code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> CompletableFuture future = new CompletableFuture();
   * future.completeExceptionally(new RuntimeException());
   *
   * assertThat(future).hasFailedWithThrowableThat().isInstanceOf(IllegalArgumentException.class);
   * </code></pre>
   *
   * @return an exception assertion object.
   *
   * @deprecated
   */
  @Deprecated
  public AbstractThrowableAssert<?, ? extends Throwable> hasFailedWithThrowableThat() {
    hasFailed();
    try {
      actual.join();
      return assertThat((Throwable) null);
    } catch (CompletionException e) {
      return assertThat(e.getCause());
    }
  }

  /**
   * Checks that the future does not complete within the given time (by calling {@link Future#get(long, TimeUnit)}) and returns
   * the exception that caused the failure for further (exception) assertions, the exception can be any of
   * {@link InterruptedException}, {@link ExecutionException}, {@link TimeoutException} or {@link CancellationException}.
   * <p>
   * <b>WARNING</b>
   * <p>
   * {@code failsWithin} does not fully integrate with soft assertions, if the future completes the test will fail immediately (the
   * error is not collected as a soft assertion error), if the assertion succeeds the chained assertions are executed and any
   * errors will be collected as a soft assertion errors.<br>
   * The rationale is that if we collect {@code failsWithin} error as a soft assertion error, the chained assertions would be
   * executed but that does not make sense since there is no exception to check as the future has completed.
   * <p>
   * Examples:
   * <pre><code class='java'> CompletableFuture&lt;?&gt; future = futureCompletingAfterMs(100);
   *
   * // assertion succeeds as the future is not completed after 50ms
   * assertThat(future).failsWithin(Duration.ofMillis(50))
   *                   .withThrowableOfType(TimeoutException.class)
   *                   .withMessage(null);
   *
   * // fails as the future is completed after within 200ms
   * assertThat(future).failsWithin(Duration.ofMillis(200));</code></pre>
   *
   * @param timeout the maximum time to wait
   * @return a new assertion instance on the future's exception.
   * @throws AssertionError if the actual {@code CompletableFuture} is {@code null}.
   * @throws AssertionError if the actual {@code CompletableFuture} succeeds within the given timeout.
   * @since 3.18.0
   */
  public WithThrowable failsWithin(Duration timeout) {
    return internalFailsWithin(timeout);
  }

  /**
   * Checks that the future does not complete within the given time (by calling {@link Future#get(long, TimeUnit)}) and returns
   * the exception that caused the failure for further (exception) assertions, the exception can be any of
   * {@link InterruptedException}, {@link ExecutionException}, {@link TimeoutException} or {@link CancellationException}.
   * <p>
   * <b>WARNING</b>
   * <p>
   * {@code failsWithin} does not fully integrate with soft assertions, if the future completes the test will fail immediately (the
   * error is not collected as a soft assertion error), if the assertion succeeds the chained assertions are executed and any
   * errors will be collected as a soft assertion errors.<br>
   * The rationale is that if we collect {@code failsWithin} error as a soft assertion error, the chained assertions would be
   * executed but that does not make sense since there is no exception to check as the future has completed.
   * <p>
   * Examples:
   * <pre><code class='java'> CompletableFuture&lt;?&gt; future = futureCompletingAfterMs(100);
   *
   * // assertion succeeds as the future is not completed after 50ms
   * assertThat(future).failsWithin(50, TimeUnit.MILLISECONDS)
   *                   .withThrowableOfType(TimeoutException.class)
   *                   .withMessage(null);
   *
   * // fails as the future is completed after within 200ms
   * assertThat(future).failsWithin(200, TimeUnit.MILLISECONDS);</code></pre>
   *
   * @param timeout the maximum time to wait
   * @param unit the time unit
   * @return a new assertion instance on the future's exception.
   * @throws AssertionError if the actual {@code CompletableFuture} is {@code null}.
   * @throws AssertionError if the actual {@code CompletableFuture} succeeds within the given timeout.
   * @since 3.18.0
   */
  public WithThrowable failsWithin(long timeout, TimeUnit unit) {
    return internalFailsWithin(timeout, unit);
  }

  private WithThrowable internalFailsWithin(Duration timeout) {
    Exception exception = futures.assertFailedWithin(info, actual, timeout);
    return new WithThrowable(exception);
  }

  private WithThrowable internalFailsWithin(long timeout, TimeUnit unit) {
    Exception exception = futures.assertFailedWithin(info, actual, timeout, unit);
    return new WithThrowable(exception);
  }

}
