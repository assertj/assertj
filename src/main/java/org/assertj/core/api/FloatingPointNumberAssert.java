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

import org.assertj.core.data.Offset;

/**
 * Assertion methods applicable to floating-point <code>{@link Number}</code>s.
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY" target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 * @author Jin Kwon
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
   * <pre><code class='java'> // assertions will pass
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
   * <pre><code class='java'> // assertions will pass:
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
   * <pre><code class='java'> // assertions will pass:
   * assertThat(8.3).isNotCloseTo(new Double(8.0), byLessThan(0.2));
   *
   * // you can use offset if you prefer
   * assertThat(8.3).isNotCloseTo(new Double(8.0), offset(0.2));
   *
   * // assertions will fail
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
   * <pre><code class='java'> // assertions will pass
   * assertThat(Double.NaN).isNaN();
   * assertThat(0.0 / 0.0).isNaN();
   * assertThat(0.0F * Float.POSITIVE_INFINITY).isNaN();
   * 
   * // assertions will fail
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
   * <pre><code class='java'> // assertions will pass
   * assertThat(1.0).isNotNaN();
   * assertThat(-1.0F).isNotNaN();
   * 
   * // assertions will fail
   * assertThat(Double.NaN).isNotNaN();
   * assertThat(0.0 / 0.0).isNotNaN();
   * assertThat(0.0F * Float.POSITIVE_INFINITY).isNotNaN();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual value is equal to {@code NaN}.
   */
  SELF isNotNaN();

  /**
   * Verifies that the actual value is a finite floating-point value.
   * <p>
   * Example:
   * <blockquote><pre>{@code
   * // assertions will pass
   * assertThat(+1.0f).isFinite();
   * assertThat(-1.0d).isFinite();
   *
   * // assertions will fail
   * assertThat(Float.NaN).isFinite();
   * assertThat(Float.NEGATIVE_INFINITY).isFinite();
   * assertThat(Float.POSITIVE_INFINITY).isFinite();
   * assertThat(Double.NaN).isFinite();
   * assertThat(Double.NEGATIVE_INFINITY).isFinite();
   * assertThat(Double.POSITIVE_INFINITY).isFinite();
   * }</pre></blockquote>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is not a finite floating-point value.
   * @see #isInfinite()
   */
  SELF isFinite();

  /**
   * Verifies that the actual value represents positive infinity or negative infinity.
   * <p>
   * Example:
   * <blockquote><pre>{@code
   * // assertions will pass
   * assertThat(Float.NEGATIVE_INFINITY).isInfinite();
   * assertThat(Float.POSITIVE_INFINITY).isInfinite();
   * assertThat(Double.NEGATIVE_INFINITY).isInfinite();
   * assertThat(Double.POSITIVE_INFINITY).isInfinite();
   *
   * // assertions will fail
   * assertThat(+1.0f).isInfinite();
   * assertThat(Float.NaN).isInfinite();
   * assertThat(-1.0d).isInfinite();
   * assertThat(Double.NaN).isInfinite();
   * }</pre></blockquote>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value doesn't represent the positive infinity nor negative infinity.
   * @see #isFinite()
   */
  SELF isInfinite();

  /**
   * Verifies that the actual value is equal to {@code NEGATIVE_INFINITY}.
   * <p>
   * Example:
   * <blockquote><pre>{@code
   * // assertions will pass
   * assertThat(Float.NEGATIVE_INFINITY).isNegativeInfinity();
   * assertThat(Double.NEGATIVE_INFINITY).isNegativeInfinity();
   *
   * // assertions will fail
   * assertThat(+1.0f).isNegativeInfinity();
   * assertThat(Float.NaN).isNegativeInfinity();
   * assertThat(Float.POSITIVE_INFINITY).isNegativeInfinity();
   * assertThat(-1.0d).isNegativeInfinity();
   * assertThat(Double.NaN).isNegativeInfinity();
   * assertThat(Double.POSITIVE_INFINITY).isNegativeInfinity();
   * }</pre></blockquote>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is not equal to {@code NEGATIVE_INFINITY}.
   * @see #isNotNegativeInfinity()
   */
  SELF isNegativeInfinity();

  /**
   * Verifies that the actual value is not equal to {@code NEGATIVE_INFINITY}.
   * <p>
   * Example:
   * <blockquote><pre>{@code
   * // assertions will pass
   * assertThat(+1.0f).isNotNegativeInfinity();
   * assertThat(Float.NaN).isNotNegativeInfinity();
   * assertThat(Float.POSITIVE_INFINITY).isNotNegativeInfinity();
   * assertThat(-1.0d).isNotNegativeInfinity();
   * assertThat(Double.NaN).isNegativeInfinity();
   * assertThat(Double.POSITIVE_INFINITY).isNotNegativeInfinity();
   *
   * // assertions will fail
   * assertThat(Float.NEGATIVE_INFINITY).isNotNegativeInfinity();
   * assertThat(Double.NEGATIVE_INFINITY).isNotNegativeInfinity();
   * }</pre></blockquote>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is equal to {@code NEGATIVE_INFINITY}.
   * @see #isNegativeInfinity()
   */
  SELF isNotNegativeInfinity();

  /**
   * Verifies that the actual value is equal to {@code POSITIVE_INFINITY}.
   * <p>
   * Example:
   * <blockquote><pre>{@code
   * // assertions will pass
   * assertThat(Float.POSITIVE_INFINITY).isPositiveInfinity();
   * assertThat(Double.POSITIVE_INFINITY).isPositiveInfinity();
   *
   * // assertions will fail
   * assertThat(+1.0f).isPositiveInfinity();
   * assertThat(Float.NaN).isPositiveInfinity();
   * assertThat(Float.NEGATIVE_INFINITY).isPositiveInfinity();
   * assertThat(-1.0d).isPositiveInfinity();
   * assertThat(Double.NaN).isPositiveInfinity();
   * assertThat(Double.NEGATIVE_INFINITY).isPositiveInfinity();
   * }</pre></blockquote>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is not equal to {@code POSITIVE_INFINITY}.
   * @see #isNotPositiveInfinity()
   */
  SELF isPositiveInfinity();

  /**
   * Verifies that the actual value is not equal to {@code POSITIVE_INFINITY}.
   * <p>
   * Example:
   * <blockquote><pre>{@code
   * // assertions will pass
   * assertThat(+1.0f).isNotPositiveInfinity();
   * assertThat(Float.NaN).isNotPositiveInfinity();
   * assertThat(Float.NEGATIVE_INFINITY).isNotPositiveInfinity();
   * assertThat(-1.0d).isNotPositiveInfinity();
   * assertThat(Double.NaN).isNotPositiveInfinity();
   * assertThat(Double.NEGATIVE_INFINITY).isNotPositiveInfinity();
   *
   * // assertions will fail
   * assertThat(Float.POSITIVE_INFINITY).isNotPositiveInfinity();
   * assertThat(Double.POSITIVE_INFINITY).isNotPositiveInfinity();
   * }</pre></blockquote>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is equal to {@code POSITIVE_INFINITY}.
   * @see #isPositiveInfinity()
   */
  SELF isNotPositiveInfinity();
}
