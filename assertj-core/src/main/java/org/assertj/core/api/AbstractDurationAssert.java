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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveDays;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveHours;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveMillis;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveMinutes;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveNanos;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveSeconds;
import static org.assertj.core.util.Preconditions.checkArgument;

import java.time.Duration;

import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;

/**
 * Assertions for {@link Duration} type.
 * @author Filip Hrisafov
 * @since 3.15.0
 */
public abstract class AbstractDurationAssert<SELF extends AbstractDurationAssert<SELF>>
    extends AbstractComparableAssert<SELF, Duration> {

  /**
   * Creates a new <code>{@link org.assertj.core.api.AbstractDurationAssert}</code>
   * @param duration the actual value to verify
   * @param selfType the "self type"
   */
  protected AbstractDurationAssert(Duration duration, Class<?> selfType) {
    super(duration, selfType);
  }

  /**
   * Verifies that the actual {@code Duration} is equal to {@link Duration#ZERO}.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Duration.ZERO).isZero();
   *
   * // assertion will fail
   * assertThat(Duration.ofMinutes(3)).isZero();</code></pre>
   * @return this assertion object
   * @throws AssertionError if the actual {@code Duration} is {@code null}
   * @throws AssertionError if the actual {@code Duration} is not {@link Duration#ZERO}
   * @since 3.15.0
   */
  public SELF isZero() {
    isNotNull();
    return isEqualTo(Duration.ZERO);
  }

  /**
   * Verifies that the actual {@code Duration} is negative (i.e. is less than {@link Duration#ZERO}).
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Duration.ofMinutes(-15)).isNegative();
   *
   * // assertion will fail
   * assertThat(Duration.ofMinutes(10)).isNegative();</code></pre>
   * @return this assertion object
   * @throws AssertionError if the actual {@code Duration} is {@code null}
   * @throws AssertionError if the actual {@code Duration} is not less than {@link Duration#ZERO}
   * @since 3.15.0
   */
  public SELF isNegative() {
    isNotNull();
    return isLessThan(Duration.ZERO);
  }

  /**
   * Verifies that the actual {@code Duration} is positive (i.e. is greater than {@link Duration#ZERO}).
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Duration.ofHours(5)).isPositive();
   *
   * // assertion will fail
   * assertThat(Duration.ofHours(-2)).isPositive();</code></pre>
   * @return this assertion object
   * @throws AssertionError if the actual {@code Duration} is {@code null}
   * @throws AssertionError if the actual {@code Duration} is not greater than {@link Duration#ZERO}
   * @since 3.15.0
   */
  public SELF isPositive() {
    isNotNull();
    return isGreaterThan(Duration.ZERO);
  }

  /**
   * Verifies that the actual {@code Duration} has the given nanos.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Duration.ofNanos(145)).hasNanos(145);
   *
   * // assertion will fail
   * assertThat(Duration.ofNanos(145)).hasNanos(50);</code></pre>
   *
   * @param otherNanos the expected nanoseconds value
   * @return this assertion object
   * @throws AssertionError if the actual {@code Duration} is {@code null}
   * @throws AssertionError if the actual {@code Duration} does not have the given nanos
   * @since 3.15.0
   */
  public SELF hasNanos(long otherNanos) {
    isNotNull();
    long actualNanos = actual.toNanos();
    if (otherNanos != actualNanos) {
      throw Failures.instance().failure(info, shouldHaveNanos(actual, actualNanos, otherNanos), actualNanos, otherNanos);
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code Duration} has the given millis.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Duration.ofMillis(250)).hasMillis(250);
   *
   * // assertion will fail
   * assertThat(Duration.ofMillis(250)).hasMillis(700);</code></pre>
   *
   * @param otherMillis the expected milliseconds value
   * @return this assertion object
   * @throws AssertionError if the actual {@code Duration} is {@code null}
   * @throws AssertionError if the actual {@code Duration} does not have the given millis
   * @since 3.15.0
   */
  public SELF hasMillis(long otherMillis) {
    isNotNull();
    long actualMillis = actual.toMillis();
    if (otherMillis != actualMillis) {
      throw Failures.instance().failure(info, shouldHaveMillis(actual, actualMillis, otherMillis), actualMillis, otherMillis);
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code Duration} has the given seconds.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Duration.ofSeconds(250)).hasSeconds(250);
   *
   * // assertion will fail
   * assertThat(Duration.ofSeconds(250)).hasSeconds(700);</code></pre>
   *
   * @param otherSeconds the expected seconds value
   * @return this assertion object
   * @throws AssertionError if the actual {@code Duration} is {@code null}
   * @throws AssertionError if the actual {@code Duration} does not have the given seconds
   * @since 3.15.0
   */
  public SELF hasSeconds(long otherSeconds) {
    isNotNull();
    long actualSeconds = actual.getSeconds();
    if (otherSeconds != actualSeconds) {
      throw Failures.instance().failure(info, shouldHaveSeconds(actual, actualSeconds, otherSeconds),
                                        actualSeconds, otherSeconds);
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code Duration} has the given minutes.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Duration.ofMinutes(65)).hasMinutes(65);
   *
   * // assertion will fail
   * assertThat(Duration.ofMinutes(65)).hasMinutes(25);</code></pre>
   *
   * @param otherMinutes the expected minutes value
   * @return this assertion object
   * @throws AssertionError if the actual {@code Duration} is {@code null}
   * @throws AssertionError if the actual {@code Duration} does not have the given minutes
   * @since 3.15.0
   */
  public SELF hasMinutes(long otherMinutes) {
    isNotNull();
    long actualMinutes = actual.toMinutes();
    if (otherMinutes != actualMinutes) {
      throw Failures.instance().failure(info, shouldHaveMinutes(actual, actualMinutes, otherMinutes),
                                        actualMinutes, otherMinutes);
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code Duration} has the given hours.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Duration.ofHours(15)).hasHours(15);
   *
   * // assertion will fail
   * assertThat(Duration.ofHours(15)).hasHours(5);</code></pre>
   *
   * @param otherHours the expected hours value
   * @return this assertion object
   * @throws AssertionError if the actual {@code Duration} is {@code null}
   * @throws AssertionError if the actual {@code Duration} does not have the given hours
   * @since 3.15.0
   */
  public SELF hasHours(long otherHours) {
    isNotNull();
    long actualHours = actual.toHours();
    if (otherHours != actualHours) {
      throw Failures.instance().failure(info, shouldHaveHours(actual, actualHours, otherHours), actualHours, otherHours);
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code Duration} has the given days.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Duration.ofDays(5)).hasDays(5);
   *
   * // assertion will fail
   * assertThat(Duration.ofDays(5)).hasDays(1);</code></pre>
   *
   * @param otherDays the expected days value
   * @return this assertion object
   * @throws AssertionError if the actual {@code Duration} is {@code null}
   * @throws AssertionError if the actual {@code Duration} does not have the given days
   * @since 3.15.0
   */
  public SELF hasDays(long otherDays) {
    isNotNull();
    long actualDays = actual.toDays();
    if (otherDays != actualDays) {
      throw Failures.instance().failure(info, shouldHaveDays(actual, actualDays, otherDays), actualDays, otherDays);
    }
    return myself;
  }

  /**
   * Verifies that the actual {@link Duration} is close to the given one within the given allowed difference (assertion succeeds if difference = allowed difference).
   * <p>
   * This is equivalent of: {@code abs(actual - expected) <= allowed difference}.
   * <p>
   * For readability you can use {@link Assertions#withMarginOf(Duration)} to express the allowed difference.
   * <p>
   * Examples:
   * <pre><code class='java'> Duration twoMinutes = Duration.ofMinutes(2);
   * // assertions succeed:
   * assertThat(twoMinutes).isCloseTo(Duration.ofMinutes(3), Duration.ofMinutes(5));
   * assertThat(twoMinutes).isCloseTo(Duration.ofMinutes(-3), Duration.ofMinutes(10));
   *
   * // assertion succeeds when difference is exactly equals to the allowed difference
   * assertThat(twoMinutes).isCloseTo(Duration.ofMinutes(3), Duration.ofMinutes(1));
   * assertThat(twoMinutes).isCloseTo(Duration.ofMinutes(-3), Duration.ofMinutes(5));
   *
   * // assertions using within syntactic sugar
   * assertThat(twoMinutes).isCloseTo(Duration.ofMinutes(3), withMarginOf(Duration.ofMinutes(5)));
   * assertThat(twoMinutes).isCloseTo(Duration.ofMinutes(3), withMarginOf(Duration.ofMinutes(1)));
   *
   * // assertions fail
   * assertThat(twoMinutes).isCloseTo(Duration.ofMinutes(5), withMarginOf(Duration.ofMinutes(1)));
   * assertThat(twoMinutes).isCloseTo(Duration.ofMinutes(-3), withMarginOf(Duration.ofMinutes(4)));</code></pre>
   *
   * @param expected the given {@link Duration} to compare to actual.
   * @param allowedDifference a positive {@link Duration} to express the maximum allowed difference.
   * @return {@code this} assertion object.
   * @throws IllegalArgumentException if the expected Duration is {@code null}.
   * @throws IllegalArgumentException if the allowed difference Duration is {@code null} or negative.
   * @throws AssertionError if the actual value is not close enough to the given one.
   * @since 3.18.0
   */
  public SELF isCloseTo(Duration expected, Duration allowedDifference) {
    Objects.instance().assertNotNull(info, actual);
    checkArgument(expected != null, "expected duration should not be null");
    checkArgument(allowedDifference != null, "allowed difference duration should not be null");
    checkArgument(!allowedDifference.isNegative(), "allowed difference duration should be >= 0");
    if (absDiff(actual, expected).compareTo(allowedDifference) > 0) {
      throw Failures.instance().failure(info, shouldBeCloseTo(actual, expected, allowedDifference, absDiff(actual, expected)));
    }
    return myself;
  }

  private static Duration absDiff(Duration actual, Duration expected) {
    return actual.minus(expected).abs();
  }

}
