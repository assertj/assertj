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

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;

/**
 * Creates an error message indicating that an assertion that verifies that a group of elements does not contains a
 * subsequence of values failed. A group of elements can be a collection, an array or a {@code String}.
 * 
 * @author Chris Arnott
 */
public class ShouldNotContainSubsequence extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link ShouldNotContainSubsequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param subsequence the subsequence of values expected to be in {@code actual}.
   * @param comparisonStrategy the {@link ComparisonStrategy} used to evaluate assertion.
   * @param index the index of the unexpected subsequence.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainSubsequence(Object actual, Object subsequence,
      ComparisonStrategy comparisonStrategy, int index) {
    return new ShouldNotContainSubsequence(actual, subsequence, comparisonStrategy, index);
  }

  /**
   * Creates a new <code>{@link ShouldNotContainSubsequence}</code>.
   *
   * @param actual the actual value in the failed assertion.
   * @param subsequence the subsequence of values expected to be in {@code actual}.
   * @param index the index of the unexpected subsequence.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldNotContainSubsequence(Object actual, Object subsequence, int index) {
    return new ShouldNotContainSubsequence(actual, subsequence, StandardComparisonStrategy.instance(), index);
  }

  private ShouldNotContainSubsequence(Object actual, Object subsequence, ComparisonStrategy comparisonStrategy,
                                      int index) {
    super("%nExpecting:%n <%s>%nto not contain subsequence:%n <%s>%nbut was found starting at index %s%n%s",
          actual, subsequence, index, comparisonStrategy);
  }
}
