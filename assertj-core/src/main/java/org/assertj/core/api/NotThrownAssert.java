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

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.description.Description;
import org.assertj.core.util.CheckReturnValue;

/**
 * Assertion class checking a {@link ThrowingCallable} throws no exception.
 * <p>
 * The class itself does not do much, it delegates the work to {@link ThrowableAssert} after calling {@link #isThrownBy(ThrowingCallable)}.
 *
 * @see ThrowableTypeAssert
 * @since 3.17.0
 */
public class NotThrownAssert implements Descriptable<NotThrownAssert> {

  protected Description description;

  /**
   * Assert that no exception of any type is thrown by the {@code throwingCallable}.
   * <p>
   * Example:
   * <pre><code class='java'>assertThatNoException().isThrownBy(() -&gt; { System.out.println("OK"); });</code></pre>
   *
   * @param code code not throwing any exception
   * @throws AssertionError if the actual statement raised a {@code Throwable}.
   */
  public void isThrownBy(final ThrowingCallable code) {
    Throwable throwable = ThrowableAssert.catchThrowable(code);
    assertThat(throwable).as(description).doesNotThrowAnyException();
  }

  /** {@inheritDoc} */
  @Override
  @CheckReturnValue
  public NotThrownAssert describedAs(Description description) {
    this.description = description;
    return this;
  }

}
