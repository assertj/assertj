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

import org.assertj.core.data.Percentage;

/**
 * Creates an error message indicating that an assertion that
 * verifies that two numbers are not equal within a positive percentage failed.
 *
 * @author Chris Arnott
 */
public class ShouldNotBeEqualWithinPercentage extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotBeEqualWithinPercentage}</code>.
   *
   * @param <T> the type of the actual value and the type of values that given {@code Condition} takes.
   * @param actual the actual value in the failed assertion.
   * @param expected the expected value in the failed assertion.
   * @param percentage the given positive percentage.
   * @param difference the effective difference between actual and expected.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static <T extends Number> ErrorMessageFactory shouldNotBeEqualWithinPercentage(T actual, T expected,
                                                                                        Percentage percentage,
                                                                                        T difference) {
    double expectedPercentage = difference.doubleValue() / expected.doubleValue() * 100d;
    return new ShouldNotBeEqualWithinPercentage(actual, expected, percentage, expectedPercentage);
  }

  private <T extends Number> ShouldNotBeEqualWithinPercentage(Number actual, Number expected, Percentage percentage,
                                                              double expectedPercentage) {
    super("%nExpecting:%n" +
          "  <%s>%n" +
          "not to be close to:%n" +
          "  <%s>%n" +
          "by more than %s but difference was %s%%.%n" +
          "(a difference of exactly %s being considered incorrect)",
          actual, expected, percentage, expectedPercentage, percentage);
  }
}
