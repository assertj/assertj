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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.ComparisonStrategy;

public class ShouldContainAnyOf extends BasicErrorMessageFactory {

  private static final String DEFAULT_FORMAT = "%nExpecting actual:%n" +
                                               "  %s%n" +
                                               "to contain at least one of the following elements:%n" +
                                               "  %s%n" +
                                               "but none were found";

  private static final String FORMAT_WITH_COMPARISON_STRATEGY = DEFAULT_FORMAT + " %s";

  public static ErrorMessageFactory shouldContainAnyOf(Object actual, Object expected,
                                                       ComparisonStrategy comparisonStrategy) {
    return new ShouldContainAnyOf(actual, expected, comparisonStrategy);
  }

  public static ErrorMessageFactory shouldContainAnyOf(Object actual, Object expected) {
    return new ShouldContainAnyOf(actual, expected);
  }

  private ShouldContainAnyOf(Object actual, Object expected, ComparisonStrategy comparisonStrategy) {
    super(FORMAT_WITH_COMPARISON_STRATEGY, actual, expected, comparisonStrategy);
  }

  private ShouldContainAnyOf(Object actual, Object expected) {
    super(DEFAULT_FORMAT, actual, expected);
  }

}
