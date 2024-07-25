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

import org.assertj.core.data.Offset;

/**
 * Assertion methods applicable to floating-point <code>{@link Number}</code>s.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY" target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ACTUAL> the type of the "actual" value.
 */
public interface FloatingPointNumberAssert<SELF extends FloatingPointNumberAssert<SELF, ACTUAL>, ACTUAL extends Number>
    extends NumberAssert<SELF, ACTUAL> {

  /**
   * Verifies that the actual number is close to the given one within the given offset value.
   * <p>
   * This assertion is the same as {@link #isCloseTo(Number, Offset)}.
   * <p>
   * When <i>abs(actual - expected) == offset value</i>, the assertion:
   * <ul>
   * <li><b>succeeds</b> when using {@link Assertions#within(Double)} or {@link Assertions#offset(Double)}</li>
   * <li><b>fails</b> when using {@link Assertions#byLessThan(Double)} or {@link Offset#strictOffset(Number)}</li>
   * </ul>
   * <p>
   * Examples:
   * <pre><code class='java'> // assertions succeed
   * assertThat(8.1).isEqualTo(8.0, within(0.2));
   * assertThat(8.1).isEqualTo(8.0, offset(0.2)); // alias of within
   * assertThat(8.1).isEqualTo(8.0, byLessThan(0.2)); // strict
   *
   * // assertions succeed when the difference == offset value ...
   * assertThat(8.1).isEqualTo(8.0, within(0.1));
   * assertThat(8.1).isEqualTo(8.0, offset(0.1));
   * // ... except when using byLessThan which is strict
   * assertThat(8.1).isEqualTo(8.0, byLessThan(0.1)); // strict =&gt; fail
   *
   * // this assertions also fails
   * assertThat(8.1).isEqualTo(8.0, within(0.001));</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  SELF isEqualTo(ACTUAL expected, Offset<ACTUAL> offset);

  /**
   * Verifies that the actual number is close to the given one within the given offset.<br>
   * If difference is equal to offset value, assertion is considered valid.
   * <p>
   * Example with double:
   * <pre><code class='java'> // assertions succeed:
   * assertThat(8.1).isCloseTo(new Double(8.0), within(0.2));
   *
   * // you can use offset if you prefer
   * assertThat(8.1).isCloseTo(new Double(8.0), offset(0.2));
   *
   * // if difference is exactly equals to the offset (0.1), it's ok
   * assertThat(8.1).isCloseTo(new Double(8.0), within(0.1));
   *
   * // assertion will fail
   * assertThat(8.1).isCloseTo(new Double(8.0), within(0.01));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not close to the given one.
   */
  @Override
  SELF isCloseTo(ACTUAL expected, Offset<ACTUAL> offset);

  /**
   * Verifies that the actual number is not close to the given one by less than the given offset.<br>
   * If the difference is equal to the offset value, the assertion fails.
   * <p>
   * Example with double:
   * <pre><code class='java'> // assertions succeed:
   * assertThat(8.3).isNotCloseTo(new Double(8.0), byLessThan(0.2));
   *
   * // you can use offset if you prefer
   * assertThat(8.3).isNotCloseTo(new Double(8.0), offset(0.2));
   *
   * // assertions fail
   * assertThat(8.1).isNotCloseTo(new Double(8.0), byLessThan(0.1));
   * assertThat(8.1).isNotCloseTo(new Double(8.0), byLessThan(0.2));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param offset the given positive offset.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   * @see Assertions#byLessThan(Double)
   * @since 2.6.0 / 3.6.0
   */
  @Override
  SELF isNotCloseTo(ACTUAL expected, Offset<ACTUAL> offset);

  /**
   * Verifies that the actual value is equal to {@code NaN}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(Double.NaN).isNaN();
   * assertThat(0.0 / 0.0).isNaN();
   * assertThat(0.0F * Float.POSITIVE_INFINITY).isNaN();
   *
   * // assertions fail
   * assertThat(1.0).isNaN();
   * assertThat(-1.0F).isNaN();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is not equal to {@code NaN}.
   */
  SELF isNaN();

  /**
   * Verifies that the actual value is not equal to {@code NaN}.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1.0).isNotNaN();
   * assertThat(-1.0F).isNotNaN();
   *
   * // assertions fail
   * assertThat(Double.NaN).isNotNaN();
   * assertThat(0.0 / 0.0).isNotNaN();
   * assertThat(0.0F * Float.POSITIVE_INFINITY).isNotNaN();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is equal to {@code NaN}.
   */
  SELF isNotNaN();

  SELF isFinite();

  SELF isNotFinite();

  SELF isInfinite();

  SELF isNotInfinite();

}
