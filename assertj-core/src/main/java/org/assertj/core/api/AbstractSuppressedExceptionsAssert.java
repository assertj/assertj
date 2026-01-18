/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

public abstract class AbstractSuppressedExceptionsAssert<INITIAL extends AbstractThrowableAssert<INITIAL, THROWABLE>, THROWABLE extends Throwable>
    extends AbstractObjectArrayAssert<AbstractSuppressedExceptionsAssert<INITIAL, THROWABLE>, Throwable> {

  protected AbstractSuppressedExceptionsAssert(Throwable[] actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Navigates back to the initial {@link Throwable} under test.
   * <p>
   * Example:
   * <pre><code class='java'>Throwable throwable = new Throwable("boom!");
   * Throwable invalidArgException = new IllegalArgumentException("invalid argument");
   * Throwable ioException = new IOException("IO error");
   * throwable.addSuppressed(invalidArgException);
   * throwable.addSuppressed(ioException);
   *
   * assertThat(throwable).suppressedExceptions()
   *                      .containsOnly(invalidArgException, ioException)
   *                      .returnToInitialThrowable()
   *                      .hasMessage("boom!");</code></pre>
   *
   * @return the initial throwable assertion.
   * @since 4.0.0
   */
  public abstract AbstractThrowableAssert<INITIAL, THROWABLE> returnToInitialThrowable();
}
