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

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

/**
 * Assertions for {@link Throwable#getSuppressed() suppressed exceptions} of a {@link Throwable}.
 * <p>
 * This class is instantiated via the {@link AbstractThrowableAssert#suppressedExceptions() suppressedExceptions()}
 * navigation method, and allows returning to the source assertion instance via {@link #returnToThrowable()}.
 *
 * @param <ORIGIN>    the type of the source assertion.
 * @param <THROWABLE> the type of the throwable under test in the source assertion.
 * @since 3.28.0
 */
public abstract class SuppressedExceptionsAssert<ORIGIN extends AbstractThrowableAssert<ORIGIN, THROWABLE>, THROWABLE extends Throwable>
    extends AbstractObjectArrayAssert<SuppressedExceptionsAssert<ORIGIN, THROWABLE>, Throwable> {

  private final ORIGIN originAssert;

  static <ORIGIN extends AbstractThrowableAssert<ORIGIN, THROWABLE>, THROWABLE extends Throwable> SuppressedExceptionsAssert<ORIGIN, THROWABLE> from(ORIGIN originAssert) {
    return new DefaultAssert<>(originAssert, originAssert.actual.getSuppressed()).withAssertionState(originAssert);
  }

  /**
   * Creates a new instance from an {@link ORIGIN} assert instance and an array of suppressed exceptions.
   *
   * @param originAssert the {@link ORIGIN} assert that initiated the navigation.
   * @param suppressedExceptions the suppressed exceptions.
   */
  protected SuppressedExceptionsAssert(ORIGIN originAssert, Throwable[] suppressedExceptions) {
    super(suppressedExceptions, SuppressedExceptionsAssert.class);
    this.originAssert = requireNonNull(originAssert, shouldNotBeNull("originAssert")::create);
  }

  @Override
  protected SuppressedExceptionsAssert<ORIGIN, THROWABLE> newObjectArrayAssert(Throwable[] suppressedExceptions) {
    return new DefaultAssert<>(originAssert, suppressedExceptions);
  }

  /**
   * Returns to the origin {@link AbstractThrowableAssert} instance that initiated the navigation.
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
   *                      .returnToThrowable()
   *                      .hasMessage("boom!");</code></pre>
   *
   * @return the origin {@link AbstractThrowableAssert} instance.
   */
  public ORIGIN returnToThrowable() {
    return originAssert;
  }

  private static class DefaultAssert<ORIGIN extends AbstractThrowableAssert<ORIGIN, THROWABLE>, THROWABLE extends Throwable>
      extends SuppressedExceptionsAssert<ORIGIN, THROWABLE> {

    private DefaultAssert(ORIGIN sourceAssert, Throwable[] suppressedExceptions) {
      super(sourceAssert, suppressedExceptions);
    }

  }

}
