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

import static org.assertj.core.error.ShouldBePeriod.shouldBeNegative;
import static org.assertj.core.error.ShouldBePeriod.shouldBePositive;
import static org.assertj.core.error.ShouldHavePeriod.shouldHaveDays;
import static org.assertj.core.error.ShouldHavePeriod.shouldHaveMonths;
import static org.assertj.core.error.ShouldHavePeriod.shouldHaveYears;

import java.time.Period;

import org.assertj.core.internal.Failures;

/**
 * Assertions for {@link Period} type.
 * @author Hayden Meloche
 * @since 3.17.0
 */
public abstract class AbstractPeriodAssert<SELF extends AbstractPeriodAssert<SELF>> extends AbstractAssert<SELF, Period> {

  /**
   * Creates a new <code>{@link org.assertj.core.api.AbstractPeriodAssert}</code>
   * @param period the actual value to verify
   * @param selfType the "self type"
   */
  public AbstractPeriodAssert(Period period, Class<?> selfType) {
    super(period, selfType);
  }

  /**
   * Verifies that the actual {@code Period} has the given years.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Period.ofYears(5)).hasYears(5);
   *
   * // assertion will fail
   * assertThat(Period.ofYears(5)).hasYears(1);</code></pre>
   *
   * @param expectedYears the expected years value
   * @return this assertion object
   * @throws AssertionError if the actual {@code Period} is {@code null}
   * @throws AssertionError if the actual {@code Period} does not have the given years
   * @since 3.17.0
   */
  public SELF hasYears(int expectedYears) {
    isNotNull();
    int actualYears = actual.getYears();
    if (expectedYears != actualYears) {
      throw Failures.instance().failure(info, shouldHaveYears(actual, actualYears, expectedYears), actualYears, expectedYears);
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code Period} has the given months.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Period.ofMonths(5)).hasMonths(5);
   *
   * // assertion will fail
   * assertThat(Period.ofMonths(5)).hasMonths(1);</code></pre>
   *
   * @param expectedMonths the expected months value
   * @return this assertion object
   * @throws AssertionError if the actual {@code Period} is {@code null}
   * @throws AssertionError if the actual {@code Period} does not have the given months
   * @since 3.17.0
   */
  public SELF hasMonths(int expectedMonths) {
    isNotNull();
    int actualMonths = actual.getMonths();
    if (expectedMonths != actualMonths) {
      throw Failures.instance().failure(info, shouldHaveMonths(actual, actualMonths, expectedMonths), actualMonths,
                                        expectedMonths);
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code Period} has the given days.
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Period.ofDays(5)).hasDays(5);
   *
   * // assertion will fail
   * assertThat(Period.ofDays(5)).hasDays(1);</code></pre>
   *
   * @param expectedDays the expected days value
   * @return this assertion object
   * @throws AssertionError if the actual {@code Period} is {@code null}
   * @throws AssertionError if the actual {@code Period} does not have the given days
   * @since 3.17.0
   */
  public SELF hasDays(int expectedDays) {
    isNotNull();
    int actualDays = actual.getDays();
    if (expectedDays != actualDays) {
      throw Failures.instance().failure(info, shouldHaveDays(actual, actualDays, expectedDays), actualDays, expectedDays);
    }
    return myself;
  }

  /**
   * Verifies that the actual {@code Period} is positive (i.e. is greater than {@link Period#ZERO}).
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Period.ofMonths(5)).isPositive();
   *
   * // assertion will fail
   * assertThat(Period.ofMonths(-2)).isPositive();</code></pre>
   * @return this assertion object
   * @throws AssertionError if the actual {@code Period} is {@code null}
   * @throws AssertionError if the actual {@code Period} is not greater than {@link Period#ZERO}
   * @since 3.17.0
   */
  public SELF isPositive() {
    isNotNull();
    boolean negative = actual.isNegative();
    if (negative || Period.ZERO.equals(actual)) throw Failures.instance().failure(info, shouldBePositive(actual));
    return myself;
  }

  /**
   * Verifies that the actual {@code Period} is negative (i.e. is less than {@link Period#ZERO}).
   * <p>
   * Example :
   * <pre><code class='java'> // assertion will pass
   * assertThat(Period.ofMonths(-5)).isNegative();
   *
   * // assertion will fail
   * assertThat(Period.ofMonths(2)).isNegative();</code></pre>
   * @return this assertion object
   * @throws AssertionError if the actual {@code Period} is {@code null}
   * @throws AssertionError if the actual {@code Period} is not greater than {@link Period#ZERO}
   * @since 3.17.0
   */
  public SELF isNegative() {
    isNotNull();
    boolean negative = actual.isNegative();
    if (!negative || Period.ZERO.equals(actual)) throw Failures.instance().failure(info, shouldBeNegative(actual));
    return myself;
  }
}
