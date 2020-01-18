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
package org.assertj.core.error;

import org.assertj.core.data.Percentage;

import java.util.OptionalDouble;

/**
 * Build error message when an {@link java.util.OptionalDouble} should be close to an expected value within a positive
 * percentage.
 *
 * @author Joshua Kitchen
 */
public class OptionalDoubleShouldHaveValueCloseToPercentage extends BasicErrorMessageFactory {

  private OptionalDoubleShouldHaveValueCloseToPercentage(double expected) {
    super("%nExpecting an OptionalDouble with value:%n" +
        "  <%s>%n" +
        "but was empty.",
      expected);
  }

  private OptionalDoubleShouldHaveValueCloseToPercentage(OptionalDouble actual, double expected, Percentage percentage,
                                                         double expectedPercentage) {
    super("%nExpecting:%n" +
        "  <%s>%n" +
        "to be close to:%n" +
        "  <%s>%n" +
        "by less than %s but difference was %s%%.%n" +
        "(a difference of exactly %s being considered valid)",
      actual, expected, percentage, expectedPercentage, percentage);
  }

  /**
   * Indicates that the provided {@link java.util.OptionalDouble} is empty so it doesn't have the expected value.
   *
   * @param expectedValue the value we expect to be in an {@link java.util.OptionalDouble}.
   * @return a error message factory.
   */
  public static OptionalDoubleShouldHaveValueCloseToPercentage shouldHaveValueCloseToPercentage(double expectedValue) {
    return new OptionalDoubleShouldHaveValueCloseToPercentage(expectedValue);
  }

  /**
   * Indicates that the provided {@link java.util.OptionalDouble} has a value, but it is not within the given positive
   * percentage.
   *
   * @param optional the {@link java.util.OptionalDouble} which has a value
   * @param expectedValue the value we expect to be in the provided {@link java.util.OptionalDouble}
   * @param percentage the given positive percentage
   * @param difference the effective distance between actual and expected
   * @return an error message factory
   */
  public static OptionalDoubleShouldHaveValueCloseToPercentage shouldHaveValueCloseToPercentage(OptionalDouble optional,
                                                                                                double expectedValue,
                                                                                                Percentage percentage,
                                                                                                double difference) {
    double actualPercentage = difference / expectedValue * 100d;
    return new OptionalDoubleShouldHaveValueCloseToPercentage(optional, expectedValue, percentage, actualPercentage);
  }
}
