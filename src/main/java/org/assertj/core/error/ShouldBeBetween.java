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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.error;

import java.util.Date;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Date} is between start - end dates (inclusive
 * or not) failed.
 *
 * @author Joel Costigliola
 */
public class ShouldBeBetween extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeBetween}</code>.
   * @param actual the actual value in the failed assertion.
   * @param start the lower boundary of date period.
   * @param end the lower boundary of date period.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeBetween(Date actual, Date start, Date end, boolean inclusiveStart,
                                                    boolean inclusiveEnd, ComparisonStrategy comparisonStrategy) {
    return new ShouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeBetween}</code>.
   * @param actual the actual value in the failed assertion.
   * @param start the lower boundary of date period.
   * @param end the lower boundary of date period.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeBetween(Date actual, Date start, Date end, boolean inclusiveStart,
                                                    boolean inclusiveEnd) {
    return new ShouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldBeBetween}</code>.
   * @param <T> the type of values to compare.
   * @param actual the actual value in the failed assertion.
   * @param start the lower boundary of range.
   * @param end the lower boundary of range.
   * @param inclusiveStart whether to include start value in range.
   * @param inclusiveEnd whether to include end value in range.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T> ErrorMessageFactory shouldBeBetween(T actual, T start, T end, boolean inclusiveStart, boolean inclusiveEnd,
                                                        ComparisonStrategy comparisonStrategy) {
    return new ShouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeBetween}</code>.
   * @param <T> the type of values to compare.
   * @param actual the actual value in the failed assertion.
   * @param start the lower boundary of range.
   * @param end the lower boundary of range.
   * @param inclusiveStart whether to include start value in range.
   * @param inclusiveEnd whether to include end value in range.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T> ErrorMessageFactory shouldBeBetween(T actual, T start, T end, boolean inclusiveStart, boolean inclusiveEnd) {
    return new ShouldBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, StandardComparisonStrategy.instance());
  }

  private ShouldBeBetween(Date actual, Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd,
                          ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto be in period:%n  " + (inclusiveStart ? '[' : ']') + "%s, %s" +
          (inclusiveEnd ? ']' : '[') + "%n%s", actual, start, end, comparisonStrategy);
  }

  private <T> ShouldBeBetween(T actual, T start, T end, boolean inclusiveStart, boolean inclusiveEnd,
                              ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto be between:%n  " + (inclusiveStart ? '[' : ']')
          + "%s, %s" + (inclusiveEnd ? ']' : '[') + "%n%s", actual, start, end, comparisonStrategy);
  }

}
