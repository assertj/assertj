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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

public abstract class AbstractTryAssert<SELF extends AbstractTryAssert<SELF, ACTUAL, EX>, ACTUAL, EX extends Throwable>
    extends AbstractThrowableAssert<SELF, EX> {
  private final Try<ACTUAL, EX> attempt;

  protected AbstractTryAssert(Try<ACTUAL, EX> attempt, Class<?> selfType) {
    super(attempt.failure, selfType);
    this.attempt = attempt;
  }

  /**
   * Verifies that {@link ThrowingReturningCallable} didn't raise a throwable and returns a result that can be verified
   * with subsequent calls of methods of {@link ObjectAssert}.
   *
   * <p>Example :
   *
   * <pre><code class='java'>
   * assertThatReturningCode(() -&gt; 42)
   *   .doesNotThrowAnyExceptionAndReturns()
   *   .isEqualTo(42);
   * </code></pre>
   *
   * @return the assertions of result
   * @throws AssertionError if the actual statement raised a {@code Throwable}.
   */
  public ObjectAssert<ACTUAL> doesNotThrowAnyExceptionAndReturns() {
    doesNotThrowAnyException();
    return AssertionsForClassTypes.assertThat(attempt.result);
  }

  public interface ThrowingReturningCallable<V> {
    V call() throws Throwable;
  }

  public static final class Try<V, EX extends Throwable> {
    private final V result;
    private final EX failure;

    private Try(V result, EX failure) {
      this.result = result;
      this.failure = failure;
    }

    @SuppressWarnings("unchecked")
    static <VALUE, EX extends Throwable> Try<VALUE, EX> tryCallable(ThrowingReturningCallable<VALUE> callable) {
      try {
        return succeed(callable.call());
      } catch (Throwable failure) {
        return (Try<VALUE, EX>) failed(failure);
      }
    }

    public static <VALUE, EX extends Throwable> Try<VALUE, EX> succeed(VALUE result) {
      return new Try<>(result, null);
    }

    public static <VALUE, EX extends Throwable> Try<VALUE, EX> failed(EX failure) {
      return new Try<>(null, failure);
    }
  }
}
