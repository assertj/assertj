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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;

/**
 * Assertion methods applicable to <code>{@link Number}</code>s.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public interface NumberAssert<S extends NumberAssert<S, A>, A extends Number> {

  /**
   * Verifies that the actual value is equal to zero.
   * <p/>
   * Example:
   * <pre><code class='java'> int intZero = 0;
   * long longZero = 0L;
   *
   * // assertions will pass
   * assertThat(intZero).isZero();
   * assertThat(longZero).isZero();
   *
   * int meaningOfLife = 42;
   * double someOfPi = 3.142;
   *
   * // assertions will fail
   * assertThat(meaningOfLife).isZero();
   * assertThat(someOfPi).isZero();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  S isZero();

  /**
   * Verifies that the actual value is not equal to zero.
   * <p/>
   * Example:
   * <pre><code class='java'> int meaningOfLife = 42;
   * double someOfPi = 3.142;
   *
   * // assertions will pass
   * assertThat(meaningOfLife).isNotZero();
   * assertThat(someOfPi).isNotZero();
   *
   * int intZero = 0;
   * long longZero = 0L;
   *
   * // assertions will fail
   * assertThat(intZero).isNotZero();
   * assertThat(longZero).isNotZero();</code></pre>
   *
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  S isNotZero();

  /**
   * Verifies that the actual value is positive.
   * <p/>
   * Example:
   * <pre><code class='java'> int positive = 42;
   * int zero = 0;
   * int negative = -42;
   *
   * // assertions will pass
   * assertThat(positive).isPositive();
   *
   * // assertions will fail
   * assertThat(zero).isPositive();
   * assertThat(negative).isPositive();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive.
   */
  S isPositive();

  /**
   * Verifies that the actual value is negative.
   * <p/>
   * Example:
   * <pre><code class='java'> int positive = 42;
   * int zero = 0;
   * int negative = -42;
   *
   * // assertions will pass
   * assertThat(negative).isNegative();
   *
   * // assertions will fail
   * assertThat(zero).isNegative();
   * assertThat(positive).isNegative();</code></pre>
   * 
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative.
   */
  S isNegative();

  /**
   * Verifies that the actual value is non negative (positive or equal zero).
   * <p/>
   * Example:
   * <pre><code class='java'> int positive = 42;
   * int zero = 0;
   * int negative = -42;
   *
   * // assertions will pass
   * assertThat(positive).isNotNegative();
   * assertThat(zero).isNotNegative();
   *
   * // assertions will fail
   * assertThat(negative).isNotNegative();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not non negative.
   */
  S isNotNegative();

  /**
   * Verifies that the actual value is non positive (negative or equal zero).
   * <p/>
   * Example:
   * <pre><code class='java'> int positive = 42;
   * int zero = 0;
   * int negative = -42;
   *
   * // assertions will pass
   * assertThat(negative).isNotPositive();
   * assertThat(zero).isNotPositive();
   *
   * // assertions will fail
   * assertThat(positive).isNotPositive();</code></pre>
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not non positive.
   */
  S isNotPositive();

  /**
   * Verifies that the actual value is in [start, end] range (start included, end included).
   * <p/>
   * Example:
   * <pre><code class='java'> // these assertions succeed ...
   * assertThat(12).isBetween(10, 14);
   * assertThat(12).isBetween(12, 14);
   * assertThat(12).isBetween(10, 12);
   * 
   * // ... but this one fails
   * assertThat(12).isBetween(14, 16);</code></pre>
   * 
   * @param start the start value (inclusive), expected not to be null.
   * @param end the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in [start, end] range.
   */
  S isBetween(A start, A end);

  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   * <p/>
   * Example:
   * <pre><code class='java'> // this assertion succeeds ...
   * assertThat(12).isBetween(10, 14);
   * 
   * // ... but these ones fail
   * assertThat(12).isBetween(12, 14);
   * assertThat(12).isBetween(10, 12);
   * assertThat(12).isBetween(16, 18);</code></pre>
   * 
   * @param start the start value (exclusive), expected not to be null.
   * @param end the end value (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ range.
   */
  S isStrictlyBetween(A start, A end);

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
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  S isCloseTo(A expected, Offset<A> offset);

  /**
   * Verifies that the actual number is close to the given one within the given percentage.<br>
   * If difference is equal to the percentage value, assertion is considered valid.
   * <p>
   * Example with double:
   * <pre><code class='java'> // assertions will pass:
   * assertThat(11.0).isCloseTo(new Double(10.0), withinPercentage(20d));
   *
   * // if difference is exactly equals to the computed offset (1.0), it's ok
   * assertThat(11.0).isCloseTo(new Double(10.0), withinPercentage(10d));
   *
   * // assertion will fail
   * assertThat(11.0).isCloseTo(new Double(10.0), withinPercentage(5d));</code></pre>
   *
   * @param expected the given number to compare the actual value to.
   * @param percentage the given positive percentage.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given offset is {@code null}.
   * @throws NullPointerException if the expected number is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  S isCloseTo(A expected, Percentage percentage);
}
