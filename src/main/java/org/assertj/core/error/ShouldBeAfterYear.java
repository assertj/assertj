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

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;


/**
 * Creates an error message indicating that an assertion that verifies that a {@link Date} is after given year failed.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeAfterYear extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeAfterYear}</code>.
   * @param actual the actual value in the failed assertion.
   * @param year the year to compare the actual date's year to.
   * @param comparisonStrategy the {@link ComparisonStrategy} used.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeAfterYear(Date actual, int year, ComparisonStrategy comparisonStrategy) {
    return new ShouldBeAfterYear(actual, year, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeAfterYear}</code>.
   * @param actual the actual value in the failed assertion.
   * @param year the year to compare the actual date's year to.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeAfterYear(Date actual, int year) {
    return new ShouldBeAfterYear(actual, year, StandardComparisonStrategy.instance());
  }

  private ShouldBeAfterYear(Date actual, int year, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting year of:%n <%s>%nto be strictly after year:%n <%s>%s", actual, year, comparisonStrategy);
  }
}
