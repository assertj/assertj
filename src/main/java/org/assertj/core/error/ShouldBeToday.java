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

import java.time.LocalDate;
import java.util.Date;

import org.assertj.core.internal.*;


/**
 * Creates an error message indicating that an assertion that verifies that a {@link Date} is today (matching only year, month and
 * day but not hours).
 * 
 * @author Joel Costigliola
 */
public class ShouldBeToday extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeToday}</code>.
   * @param actual the actual value in the failed assertion.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeToday(Date actual, ComparisonStrategy comparisonStrategy) {
    return new ShouldBeToday(actual, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeToday}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeToday(LocalDate actual) {
    return new ShouldBeToday(actual);
  }

  /**
   * Creates a new <code>{@link ShouldBeToday}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeToday(Date actual) {
    return new ShouldBeToday(actual, StandardComparisonStrategy.instance());
  }

  private ShouldBeToday(Date actual, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting:%n <%s>%nto be today%s but was not.", actual, comparisonStrategy);
  }

  private ShouldBeToday(LocalDate actual) {
    super("%nExpecting:%n <%s>%nto be today but was not.", actual);
  }
}
