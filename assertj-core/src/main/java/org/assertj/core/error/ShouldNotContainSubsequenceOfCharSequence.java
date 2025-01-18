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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that {@link CharSequence} does not contain
 * a subsequence of several {@code CharSequence} in order failed.
 */
public class ShouldNotContainSubsequenceOfCharSequence extends BasicErrorMessageFactory {

  /**
   * Creates a new {@link ShouldNotContainSubsequenceOfCharSequence}.
   *
   * @param actual the actual value in the failed assertion.
   * @param subsequence the subsequence of {@link CharSequence} expected not to be in {@code actual}.
   * @param subsequenceIndexes the indexes of items of {@code subsequence} found in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @return the created {@link ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainSubsequence(CharSequence actual, CharSequence[] subsequence,
                                                                int[] subsequenceIndexes, ComparisonStrategy comparisonStrategy) {
    return new ShouldNotContainSubsequenceOfCharSequence(actual, subsequence, subsequenceIndexes, comparisonStrategy);
  }

  /**
   * Creates a new {@link ShouldNotContainSubsequenceOfCharSequence}.
   *
   * @param actual the actual value in the failed assertion.
   * @param subsequence the subsequence of {@link CharSequence} expected not to be in {@code actual}.
   * @param subsequenceIndexes the indexes of items of {@code subsequence} found in {@code actual}.
   * @return the created {@link ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainSubsequence(CharSequence actual, CharSequence[] subsequence,
                                                                int[] subsequenceIndexes) {
    return new ShouldNotContainSubsequenceOfCharSequence(actual, subsequence, subsequenceIndexes,
                                                         StandardComparisonStrategy.instance());
  }

  private ShouldNotContainSubsequenceOfCharSequence(CharSequence actual, CharSequence[] subsequence, int[] subsequenceIndexes,
                                                    ComparisonStrategy comparisonStrategy) {
    super("%nExpecting actual:%n  %s%nto not contain subsequence:%n  %s%nbut was found with indexes:%n  %s%n%s", actual,
          subsequence, subsequenceIndexes, comparisonStrategy);
  }
}
