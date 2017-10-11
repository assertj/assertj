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

import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;

import java.util.OptionalInt;

import org.assertj.core.internal.Integers;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for {@link java.util.OptionalInt}.
 *
 * @author Jean-Christophe Gay
 * @author Alexander Bischof
 * @author Grzegorz Piwowarek
 */
public abstract class AbstractOptionalIntAssert<SELF extends AbstractOptionalIntAssert<SELF>> extends
    AbstractAssert<SELF, OptionalInt> {

  @VisibleForTesting
  Integers integers = Integers.instance();

  protected AbstractOptionalIntAssert(OptionalInt actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.OptionalInt}.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalInt.of(10)).isPresent();</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalInt.empty()).isPresent();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if actual value is empty.
   * @throws AssertionError if actual is null.
   */
  public SELF isPresent() {
    isNotNull();
    if (!actual.isPresent()) throwAssertionError(shouldBePresent(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.OptionalInt} is empty (alias of {@link #isEmpty()}).
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalInt.empty()).isNotPresent();</code></pre>
   * 
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalInt.of(10)).isNotPresent();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF isNotPresent() {
    return isEmpty();
  }
  
  /**
   * Verifies that the actual {@link java.util.OptionalInt} is empty.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalInt.empty()).isEmpty();</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalInt.of(10)).isEmpty();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if actual value is present.
   * @throws AssertionError if actual is null.
   */
  public SELF isEmpty() {
    isNotNull();
    if (actual.isPresent()) throwAssertionError(shouldBeEmpty(actual));
    return myself;
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.OptionalInt}, it's an alias of {@link #isPresent()}.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalInt.of(10)).isNotEmpty();</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalInt.empty()).isNotEmpty();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if actual value is empty.
   * @throws AssertionError if actual is null.
   */
  public SELF isNotEmpty() {
    return isPresent();
  }

  /**
   * Verifies that the actual {@link java.util.OptionalInt} has the value in argument.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalInt.of(8)).hasValue(8);
   * assertThat(OptionalInt.of(8)).hasValue(Integer.valueOf(8));</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalInt.empty()).hasValue(8);
   * assertThat(OptionalInt.of(7)).hasValue(8);</code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.OptionalInt}.
   * @return this assertion object.
   * @throws AssertionError if actual value is empty.
   * @throws AssertionError if actual is null.
   * @throws AssertionError if actual has not the value as expected.
   */
  public SELF hasValue(int expectedValue) {
    isNotNull();
    if (!actual.isPresent()) throwAssertionError(shouldContain(expectedValue));
    if (expectedValue != actual.getAsInt()) throwAssertionError(shouldContain(actual, expectedValue));
    return myself;
  }
}
