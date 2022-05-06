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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import java.util.concurrent.Callable;

/**
 * Base contract for all assertion types of executable instances, like {@link Runnable} or {@link Callable}.
 *
 * @param <SELF>   the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *                 target="_blank">Emulating
 *                 'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ACTUAL> the type of the "actual" executable.
 *
 * @since 3.23.0
 */
public interface ThrowingExecutableAssert<SELF extends ThrowingExecutableAssert<SELF, ACTUAL>, ACTUAL>
    extends Descriptable<SELF> {

  /**
   * Verifies that the executable did not raise a throwable.
   * <p>
   * Example:
   * <pre><code class='java'> assertThatCode(() -&gt; foo.bar()).doesNotThrowAnyException();</code></pre>
   *
   * @throws AssertionError if the actual executable raised a {@code Throwable}.
   * @return {@code this} assertion object.
   */
  SELF doesNotThrowAnyException();

  /**
   * Verifies that the executable raised a throwable and returns a {@link ThrowableAssertAlternative} instance for
   * chaining further assertions on the raised throwable.
   * <p>
   * Example:
   * <pre><code class='java'> assertThatCode(() -&gt; foo.bar()).hasThrownException()
   *                            .withNoCause();</code></pre>
   *
   * @throws AssertionError if the actual executable did not raise a {@code Throwable}.
   * @return a new {@code ThrowableAssertAlternative} for chaining further assertions on the raised {@code Throwable}.
   */
  ThrowableAssertAlternative<Throwable> hasThrownException();

}
