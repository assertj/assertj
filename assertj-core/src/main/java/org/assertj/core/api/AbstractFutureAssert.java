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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.assertj.core.internal.Futures;
import org.assertj.core.util.VisibleForTesting;

public abstract class AbstractFutureAssert<SELF extends AbstractFutureAssert<SELF, ACTUAL, RESULT>, ACTUAL extends Future<RESULT>, RESULT>
    extends
    AbstractAssert<SELF, ACTUAL> {

  @VisibleForTesting
  Futures futures = Futures.instance();

  protected AbstractFutureAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the {@link Future} is cancelled.
   * <p>
   * Example:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override
   *   public String call() throws Exception {
   *     return "done";
   *   }
   * });
   *
   * // assertion will fail:
   * assertThat(future).isCancelled();
   *
   * // assertion will pass:
   * future.cancel(true);
   * assertThat(future).isCancelled();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isCancelled()
   * @since 2.7.0 / 3.7.0
   */
  public SELF isCancelled() {
    futures.assertIsCancelled(info, actual);
    return myself;
  }

  /**
   * Verifies that the {@link Future} is not cancelled.
   * <p>
   * Example:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override
   *   public String call() throws Exception {
   *     return "done";
   *   }
   * });
   *
   * // assertion will pass:
   * assertThat(future).isNotCancelled();
   *
   * // assertion will fail:
   * future.cancel(true);
   * assertThat(future).isNotCancelled();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isCancelled()
   * @since 2.7.0 / 3.7.0
   */
  public SELF isNotCancelled() {
    futures.assertIsNotCancelled(info, actual);
    return myself;
  }

  /**
   * Verifies that the {@link Future} is done.
   * <p>
   * Example:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override
   *   public String call() throws Exception {
   *     return "done";
   *   }
   * });
   *
   * // assertion will pass:
   * assertThat(future).isDone();
   *
   * future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override
   *   public String call() throws Exception {
   *     Thread.sleep(1000);
   *     return "done";
   *   }
   * });
   *
   * // assertion will fail:
   * assertThat(future).isDone();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isDone()
   * @since 2.7.0 / 3.7.0
   */
  public SELF isDone() {
    futures.assertIsDone(info, actual);
    return myself;
  }

  /**
   * Verifies that the {@link Future} is not done.
   * <p>
   * Example:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override
   *   public String call() throws Exception {
   *     Thread.sleep(1000);
   *     return "done";
   *   }
   * });
   *
   * // assertion will pass:
   * assertThat(future).isNotDone();
   *
   * future = executorService.submit(new Callable&lt;String&gt;() {
   *   {@literal @}Override
   *   public String call() throws Exception {
   *     return "done";
   *   }
   * });
   *
   * // assertion will fail:
   * assertThat(future).isNotDone();</code></pre>
   *
   * @return this assertion object.
   *
   * @see Future#isDone()
   * @since 2.7.0 / 3.7.0
   */
  public SELF isNotDone() {
    futures.assertIsNotDone(info, actual);
    return myself;
  }

  /**
   * Waits if necessary for at most the given time for this future to complete and then returns its result for further assertions.
   * <p>
   * If the future's result is not available for any reason an assertion error is thrown.
   * <p>
   * <b>WARNING</b>
   * <p>
   * {@code succeedsWithin} does not fully integrate with soft assertions, if it fails the test will fail immediately (the error
   * is not collected as a soft assertion error), if it succeeds the chained assertions are executed and any error will be
   * collected as a soft assertion error.<br>
   * The rationale is that if we collected {@code succeedsWithin} error as a soft assertion error, the chained assertions would be
   * executed against a future value that is actually not available.
   * <p>
   * To get assertions for the future result's type use {@link #succeedsWithin(Duration, InstanceOfAssertFactory)} instead.
   * <p>
   * Examples:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(() -&gt; {
   *   Thread.sleep(100);
   *   return "ook!";
   * });
   *
   * Duration timeout = Duration.ofMillis(200);
   *
   * // assertion succeeds
   * assertThat(future).succeedsWithin(timeout)
   *                   .isEqualTo("ook!");
   *
   * // fails as the future is not done after the given timeout
   * assertThat(future).succeedsWithin(Duration.ofMillis(50));
   *
   * // fails as the future is cancelled
   * Future&lt;String&gt; future = ... ;
   * future.cancel(false);
   * assertThat(future).succeedsWithin(timeout);</code></pre>
   *
   * @param timeout the maximum time to wait
   * @return a new assertion object on the future's result.
   * @throws AssertionError if the actual {@code CompletableFuture} is {@code null}.
   * @throws AssertionError if the actual {@code CompletableFuture} does not succeed within the given timeout.
   * @since 3.17.0
   */
  public ObjectAssert<RESULT> succeedsWithin(Duration timeout) {
    return internalSucceedsWithin(timeout);
  }

  /**
   * Waits if necessary for at most the given time for this future to complete and then returns its result for further assertions.
   * <p>
   * If the future's result is not available for any reason an assertion error is thrown.
   * <p>
   * <b>WARNING</b>
   * <p>
   * {@code succeedsWithin} does not fully integrate with soft assertions, if it fails the test will fail immediately (the error
   * is not collected as a soft assertion error), if it succeeds the chained assertions are executed and any error will be
   * collected as a soft assertion error.<br>
   * The rationale is that if we collected {@code succeedsWithin} error as a soft assertion error, the chained assertions would be
   * executed against a future value that is actually not available.
   * <p>
   * To get assertions for the future result's type use {@link #succeedsWithin(long, TimeUnit, InstanceOfAssertFactory)} instead.
   * <p>
   * Examples:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(() -&gt; {
   *   Thread.sleep(100);
   *   return "ook!";
   * });
   *
   * // assertion succeeds
   * assertThat(future).succeedsWithin(200, TimeUnit.MILLISECONDS)
   *                   .isEqualTo("ook!");
   *
   * // fails as the future is not done after the given timeout
   * assertThat(future).succeedsWithin(50, TimeUnit.MILLISECONDS);
   *
   * // fails as the future is cancelled
   * Future&lt;String&gt; future = ... ;
   * future.cancel(false);
   * assertThat(future).succeedsWithin(200, TimeUnit.MILLISECONDS);</code></pre>
   *
   * @param timeout the maximum time to wait
   * @param unit the time unit of the timeout argument
   * @return a new assertion object on the future's result.
   * @throws AssertionError if the actual {@code Future} is {@code null}.
   * @throws AssertionError if the actual {@code Future} does not succeed within the given timeout.
   * @since 3.17.0
   */
  public ObjectAssert<RESULT> succeedsWithin(long timeout, TimeUnit unit) {
    return internalSucceedsWithin(timeout, unit);
  }

  /**
   * Waits if necessary for at most the given time for this future to complete, the {@link InstanceOfAssertFactory}
   * parameter is used to return assertions specific to the future's result type.
   * <p>
   * If the future's result is not available for any reason an assertion error is thrown.
   * <p>
   * <b>WARNING</b>
   * <p>
   * {@code succeedsWithin} does not fully integrate with soft assertions, if it fails the test will fail immediately (the error
   * is not collected as a soft assertion error), if it succeeds the chained assertions are executed and any error will be
   * collected as a soft assertion error.<br>
   * The rationale is that if we collected {@code succeedsWithin} error as a soft assertion error, the chained assertions would be
   * executed against a future value that is actually not available.
   * <p>
   * Examples:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(() -&gt; {
   *   Thread.sleep(100);
   *   return "ook!";
   * });
   *
   * Duration timeout = Duration.ofMillis(200);
   *
   * // assertion succeeds, contains(String...) assertion can be called because InstanceOfAssertFactories.STRING
   * // indicates AssertJ to allow String assertions after succeedsWithin.
   * assertThat(future).succeedsWithin(timeout, InstanceOfAssertFactories.STRING)
   *                   .contains("ok");
   *
   * // fails as the future is not done after the given timeout
   * // as() is syntactic sugar for better readability.
   * assertThat(future).succeedsWithin(Duration.ofMillis(50), as(STRING));
   *
   * // assertion fails if the narrowed type for assertions is incompatible with the future's result type.
   * assertThat(future).succeedsWithin(timeout, InstanceOfAssertFactories.DATE)
   *                   .isToday();</code></pre>
   *
   * @param <ASSERT> the type of the resulting {@code Assert}
   * @param timeout the maximum time to wait
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the value of the {@link Future}
   * @throws AssertionError if the actual {@code Future} is {@code null}.
   * @throws IllegalStateException if the actual {@code Future} does not succeed within the given timeout.
   * @since 3.17.0
   */
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT succeedsWithin(Duration timeout,
                                                                     InstanceOfAssertFactory<RESULT, ASSERT> assertFactory) {
    // we don't call succeedsWithin(Duration) to avoid double proxying soft assertions.
    return internalSucceedsWithin(timeout).asInstanceOf(assertFactory);
  }

  /**
   * Waits if necessary for at most the given time for this future to complete, the {@link InstanceOfAssertFactory}
   * parameter is used to return assertions specific to the future's result type.
   * <p>
   * If the future's result is not available for any reason an assertion error is thrown.
   * <p>
   * <b>WARNING</b>
   * <p>
   * {@code succeedsWithin} does not fully integrate with soft assertions, if it fails the test will fail immediately (the error
   * is not collected as a soft assertion error), if it succeeds the chained assertions are executed and any error will be
   * collected as a soft assertion error.<br>
   * The rationale is that if we collected {@code succeedsWithin} error as a soft assertion error, the chained assertions would be
   * executed against a future value that is actually not available.
   * <p>
   * Examples:
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(() -&gt; {
   *   Thread.sleep(100);
   *   return "ook!";
   * });
   *
   * // assertion succeeds, contains(String...) assertion can be called because InstanceOfAssertFactories.STRING
   * // indicates AssertJ to allow String assertions after succeedsWithin.
   * assertThat(future).succeedsWithin(200, TimeUnit.MILLISECONDS, InstanceOfAssertFactories.STRING)
   *                   .contains("ok");
   *
   * // fails as the future is not done after the given timeout
   * // as() is syntactic sugar for better readability.
   * assertThat(future).succeedsWithin(50, TimeUnit.MILLISECONDS, as(STRING));
   *
   * // assertion  fails if the narrowed type for assertions is incompatible with the future's result type.
   * assertThat(future).succeedsWithin(200, TimeUnit.MILLISECONDS, InstanceOfAssertFactories.DATE)
   *                   .isToday();</code></pre>
   *
   * @param <ASSERT> the type of the resulting {@code Assert}
   * @param timeout the maximum time to wait
   * @param unit the time unit of the timeout argument
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance for assertions chaining on the value of the {@link Future}
   * @throws AssertionError if the actual {@code Future} is {@code null}.
   * @throws AssertionError if the actual {@code Future} does not succeed within the given timeout.
   * @since 3.17.0
   */
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT succeedsWithin(long timeout, TimeUnit unit,
                                                                     InstanceOfAssertFactory<RESULT, ASSERT> assertFactory) {
    // we don't call succeedsWithin(Duration) to avoid double proxying soft assertions.
    return internalSucceedsWithin(timeout, unit).asInstanceOf(assertFactory);
  }

  /**
   * Checks that the future does not complete within the given time and returns the exception that caused the failure for
   * further (exception) assertions, the exception can be any of {@link InterruptedException}, {@link ExecutionException},
   * {@link TimeoutException} or {@link CancellationException} as per {@link Future#get(long, TimeUnit)}.
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
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(() -&gt; {
   *   Thread.sleep(100);
   *   return "ook!";
   * });
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
   * Checks that the future does not complete within the given time and returns the exception that caused the failure for
   * further (exception) assertions, the exception can be any of {@link InterruptedException}, {@link ExecutionException},
   * {@link TimeoutException} or {@link CancellationException} as per {@link Future#get(long, TimeUnit)}.
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
   * <pre><code class='java'> ExecutorService executorService = Executors.newSingleThreadExecutor();
   *
   * Future&lt;String&gt; future = executorService.submit(() -&gt; {
   *   Thread.sleep(100);
   *   return "ook!";
   * });
   *
   * // assertion succeeds as the future is not completed after 50ms
   * assertThat(future).failsWithin(50, TimeUnit.MILLISECONDS)
   *                   .withThrowableOfType(TimeoutException.class)
   *                   .withMessage(null);
   *
   * // fails as the future is completed after the given timeout duration
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

  private ObjectAssert<RESULT> internalSucceedsWithin(Duration timeout) {
    RESULT result = futures.assertSucceededWithin(info, actual, timeout);
    return assertThat(result);
  }

  private ObjectAssert<RESULT> internalSucceedsWithin(long timeout, TimeUnit unit) {
    RESULT result = futures.assertSucceededWithin(info, actual, timeout, unit);
    return assertThat(result);
  }

}
