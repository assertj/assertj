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

import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanAssert extends AbstractAssert<AtomicBooleanAssert, AtomicBoolean> {

  public AtomicBooleanAssert(AtomicBoolean actual) {
    super(actual, AtomicBooleanAssert.class);
  }

  /**
   * Verifies that the actual atomic value is true.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicBoolean(true)).isTrue();
   *
   * // assertion will fail
   * assertThat(new AtomicBoolean(false)).isTrue();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is false.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicBooleanAssert isTrue() {
    isNotNull();
    assertEqual(true);
    return myself;
  }

  /**
   * Verifies that the actual atomic value is false.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new AtomicBoolean(false)).isFalse();
   *
   * // assertion will fail
   * assertThat(new AtomicBoolean(true)).isFalse();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual atomic is {@code null}.
   * @throws AssertionError if the actual atomic value is true.
   * 
   * @since 2.7.0 / 3.7.0
   */
  public AtomicBooleanAssert isFalse() {
    isNotNull();
    assertEqual(false);
    return myself;
  }

  private void assertEqual(boolean expected) {
    if (!objects.getComparisonStrategy().areEqual(actual.get(), expected)) {
      throwAssertionError(shouldHaveValue(actual, expected));
    }
  }

}
