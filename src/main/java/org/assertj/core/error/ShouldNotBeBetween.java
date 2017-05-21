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
package org.assertj.core.error;

import java.util.Date;

import org.assertj.core.internal.*;


/**
 * Creates an error message indicating that an assertion that verifies that a {@link Date} is not between start - end dates
 * (inclusive or not) failed.
 *
 * @author Joel Costigliola
 */
public class ShouldNotBeBetween extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotBeBetween}</code>.
   * @param actual the actual value in the failed assertion.
   * @param start the lower boundary of date period.
   * @param end the lower boundary of date period.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeBetween(Date actual, Date start, Date end, boolean inclusiveStart,
      boolean inclusiveEnd, ComparisonStrategy comparisonStrategy) {
    return new ShouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldNotBeBetween}</code>.
   * @param actual the actual value in the failed assertion.
   * @param start the lower boundary of date period.
   * @param end the lower boundary of date period.
   * @param inclusiveStart whether to include start date in period.
   * @param inclusiveEnd whether to include end date in period.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotBeBetween(Date actual, Date start, Date end, boolean inclusiveStart,
      boolean inclusiveEnd) {
    return new ShouldNotBeBetween(actual, start, end, inclusiveStart, inclusiveEnd, StandardComparisonStrategy.instance());
  }

  private ShouldNotBeBetween(Date actual, Date start, Date end, boolean inclusiveStart, boolean inclusiveEnd,
      ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nnot to be in period:%n "+(inclusiveStart ? '[' : ']') +
        "%s, %s%s" + (inclusiveEnd ? ']' : '['), actual, start, end, comparisonStrategy);
  }
}
