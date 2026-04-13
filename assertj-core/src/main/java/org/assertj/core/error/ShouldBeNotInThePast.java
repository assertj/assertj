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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import java.time.temporal.Temporal;
import java.util.Date;

import org.assertj.core.api.comparisonstrategy.ComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a {@link Date} or a {@link Temporal} is in the past failed.
 * 
 * @author Alban Dericbourg
 */
public class ShouldBeNotInThePast extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeNotInThePast}</code>.
   * @param actual the actual value in the failed assertion.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeNotInThePast(Date actual, ComparisonStrategy comparisonStrategy) {
    return new ShouldBeNotInThePast(actual, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeNotInThePast}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeNotInThePast(Date actual) {
    return new ShouldBeNotInThePast(actual, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldBeNotInThePast}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeNotInThePast(Temporal actual) {
    return new ShouldBeNotInThePast(actual);
  }

  private ShouldBeNotInThePast(Date actual, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto be in the future or now %s but was not.", actual, comparisonStrategy);
  }

  private ShouldBeNotInThePast(Temporal actual) {
    super("%nExpecting actual:%n  %s%nto be in the future or now but was not.", actual);
  }
}
