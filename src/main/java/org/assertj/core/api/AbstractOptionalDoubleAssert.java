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

import static java.lang.Math.abs;
import static org.assertj.core.error.OptionalDoubleShouldHaveValueCloseTo.shouldHaveValueCloseTo;
import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.error.OptionalShouldContain.shouldContain;

import java.util.OptionalDouble;

import org.assertj.core.data.Offset;
import org.assertj.core.internal.Doubles;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for {@link java.util.OptionalDouble}.
 *
 * @author Jean-Christophe Gay
 * @author Alexander Bischof
 * @author Grzegorz Piwowarek
 */
public abstract class AbstractOptionalDoubleAssert<SELF extends AbstractOptionalDoubleAssert<SELF>> extends
    AbstractAssert<SELF, OptionalDouble> {

  @VisibleForTesting
  Doubles doubles = Doubles.instance();

  protected AbstractOptionalDoubleAssert(OptionalDouble actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.OptionalDouble}.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalDouble.of(10.0)).isPresent();</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalDouble.empty()).isPresent();</code></pre>
   *
   * @return this assertion object.
   * @throws java.lang.AssertionError if actual value is empty.
   * @throws java.lang.AssertionError if actual is null.
   */
  public SELF isPresent() {
    isNotNull();
    if (!actual.isPresent()) throwAssertionError(shouldBePresent(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.Optional} is empty (alias of {@link #isEmpty()}).
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalDouble.empty()).isNotPresent();</code></pre>
   * 
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalDouble.of(10.0)).isNotPresent();</code></pre>
   *
   * @return this assertion object.
   */
  public SELF isNotPresent() {
    return isEmpty();
  }
  
  /**
   * Verifies that the actual {@link java.util.OptionalDouble} is empty.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalDouble.empty()).isEmpty();</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalDouble.of(10.0)).isEmpty();</code></pre>
   *
   * @return this assertion object.
   * @throws java.lang.AssertionError if actual value is present.
   * @throws java.lang.AssertionError if actual is null.
   */
  public SELF isEmpty() {
    isNotNull();
    if (actual.isPresent()) throwAssertionError(shouldBeEmpty(actual));
    return myself;
  }

  /**
   * Verifies that there is a value present in the actual {@link java.util.OptionalDouble}, it's an alias of {@link #isPresent()}.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalDouble.of(10.0)).isNotEmpty();</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalDouble.empty()).isNotEmpty();</code></pre>
   *
   * @return this assertion object.
   * @throws java.lang.AssertionError if actual value is empty.
   * @throws java.lang.AssertionError if actual is null.
   */
  public SELF isNotEmpty() {
    return isPresent();
  }

  /**
   * Verifies that the actual {@link java.util.OptionalDouble} has the value in argument.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalDouble.of(8.0)).hasValue(8.0);
   * assertThat(OptionalDouble.of(8.0)).hasValue(Double.valueOf(8.0));
   * assertThat(OptionalDouble.of(Double.NaN)).hasValue(Double.NaN); </code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalDouble.empty()).hasValue(8.0);
   * assertThat(OptionalDouble.of(7)).hasValue(8.0);</code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.OptionalDouble}.
   * @return this assertion object.
   * @throws java.lang.AssertionError if actual value is empty.
   * @throws java.lang.AssertionError if actual is null.
   * @throws java.lang.AssertionError if actual has not the value as expected.
   */
  public SELF hasValue(double expectedValue) {
    isNotNull();
    if (!actual.isPresent()) throwAssertionError(shouldContain(expectedValue));
    if (expectedValue != actual.getAsDouble()) throwAssertionError(shouldContain(actual, expectedValue));
    return myself;
  }

  /**
   * Verifies that the actual {@link java.util.OptionalDouble} has the value close to the argument.
   * <p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(OptionalDouble.of(8)).hasValueCloseTo(8.0, within(0d));
   * assertThat(OptionalDouble.of(8)).hasValueCloseTo(8.0, within(1d));
   * assertThat(OptionalDouble.of(7)).hasValueCloseTo(8.0, within(1d));</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(OptionalDouble.empty()).hasValueCloseTo(8.0, within(1d));
   * assertThat(OptionalDouble.of(7)).hasValueCloseTo(1.0, within(1d));
   * assertThat(OptionalDouble.of(7)).hasValueCloseTo(1.0, null);
   * assertThat(OptionalDouble.of(7)).hasValueCloseTo(1.0, within(-1d));</code></pre>
   *
   * @param expectedValue the expected value inside the {@link java.util.OptionalDouble}.
   * @param offset the given positive offset.
   * @return this assertion object.
   * @throws java.lang.AssertionError if actual value is empty.
   * @throws java.lang.AssertionError if actual is null.
   * @throws java.lang.AssertionError if actual has not the value as expected.
   * @throws java.lang.NullPointerException if offset is null
   * @throws java.lang.IllegalArgumentException if offset is not positive.
   */
  public SELF hasValueCloseTo(Double expectedValue, Offset<Double> offset) {
    isNotNull();
    if (!actual.isPresent()) throwAssertionError(shouldHaveValueCloseTo(expectedValue));
    // Reuses doubles functionality, catches potential assertion error and throw correct one
    try {
      doubles.assertIsCloseTo(info, actual.getAsDouble(), expectedValue, offset);
    } catch (AssertionError assertionError) {
      throwAssertionError(shouldHaveValueCloseTo(actual, expectedValue, offset, abs(expectedValue - actual.getAsDouble())));
    }
    return myself;
  }
}
