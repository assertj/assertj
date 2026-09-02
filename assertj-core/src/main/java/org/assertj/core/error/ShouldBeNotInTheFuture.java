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
 * Creates an error message indicating that an assertion that verifies that a {@link Date} or a {@link Temporal} is not in the future failed.
 * 
 * @author Alban Dericbourg
 */
public class ShouldBeNotInTheFuture extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldBeNotInTheFuture}</code>.
   * @param actual the actual value in the failed assertion.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeNotInTheFuture(Date actual, ComparisonStrategy comparisonStrategy) {
    return new ShouldBeNotInTheFuture(actual, comparisonStrategy);
  }

  /**
   * Creates a new <code>{@link ShouldBeNotInTheFuture}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeNotInTheFuture(Date actual) {
    return new ShouldBeNotInTheFuture(actual, StandardComparisonStrategy.instance());
  }

  /**
   * Creates a new <code>{@link ShouldBeNotInTheFuture}</code>.
   * @param actual the actual value in the failed assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldBeNotInTheFuture(Temporal actual) {
    return new ShouldBeNotInTheFuture(actual);
  }

  private ShouldBeNotInTheFuture(Date actual, ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto be in the past or now %s but was not.", actual, comparisonStrategy);
  }

  private ShouldBeNotInTheFuture(Temporal actual) {
    super("%nExpecting actual:%n  %s%nto be in the past or now but was not.", actual);
  }
}
